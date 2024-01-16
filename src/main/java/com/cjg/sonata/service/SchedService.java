package com.cjg.sonata.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.common.HttpRequestUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.repository.BatchRepository;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@Service
public class SchedService {
	
	Logger logger = LoggerFactory.getLogger(SchedService.class);
	
	@Value("${ffmpegEncoderPath}")
	private String ffmpegEncoderPath;
	
	@Value("${imageEncoderPath}")
	private String imageEncoderPath;	
	
	@Autowired
	BatchRepository batchRepository;
	
	@Autowired
	HttpRequestUtil httpRequestUtil;
	
	public void encoding() {
		
		List<Batch> batchList = batchRepository.findAllByStatusOrderByRegDateAsc(EncodingStatus.WAITING.getName());
		
		if(batchList == null || batchList.size() == 0) {
			return;		
		}
		
		for(Batch batch : batchList) {
			
			if(batch.getType().equals("video")) {
				encodingVideo(batch);
			}else if(batch.getType().equals("audio")) {
				encodingAudio(batch);
			}else if(batch.getType().equals("image")) {
				encodingImage(batch);
			}
			
		}
		
	}
	
	private void encodingVideo(Batch batch) {
		
		try {
			
			Long frames = 0l;
			
			FFprobe ffprobe = new FFprobe(ffmpegEncoderPath + "ffprobe.exe");
			FFmpegProbeResult probeResult = ffprobe.probe(batch.getOriginalFilePath() + batch.getOriginalFileName());
			FFmpegStream stream = probeResult.getStreams().get(0);
			frames = stream.nb_frames;
			logger.info("frames : " + frames);
			
			logger.info("BATCH MEDIA : " + batch.getMediaId());
			logger.info("BATCH MEDIA PATH : " + batch.getOriginalFilePath() + batch.getOriginalFileName());
			
			batch.setStatus(EncodingStatus.ENCODING.getName());
			batchRepository.save(batch);
			
			
			String input = batch.getOriginalFilePath() + batch.getOriginalFileName();
			String output = batch.getEncodingFilePath() + batch.getEncodingFileName();
			
			File outputDir = new File(batch.getEncodingFilePath());
			
			if(!outputDir.exists()) {
				outputDir.mkdirs();
			}
			
			List<String> list = new ArrayList();
			
			list.add(ffmpegEncoderPath + "ffmpeg.exe");
			list.add("-y");
			
			list.add("-loglevel");
			list.add("info");
			
			list.add("-strict");
			list.add("experimental");
			
			list.add("-i");
			list.add(input);
			
			list.add("-c:v");
			list.add("libx264");
			
			list.add("-c:a");
			list.add("aac");
			
			list.add("-ab");
			list.add("192k");
			
			list.add("-ac");
			list.add("2");
			
			list.add(output);
			
			logger.info("VIDEO ENCODING PARAM LIST : " + list);
			
			ProcessBuilder pb = new ProcessBuilder(list);
			pb.redirectErrorStream(true);
			
			//타겟 하나 잡기
			Process process = pb.start();
			
			InputStreamReader in = new InputStreamReader(process.getInputStream());
			BufferedReader br = new BufferedReader(in);
			
			String line = "";
			while((line = br.readLine()) != null) {
				logger.info("line : " + line);
				
				if(line != null && line.contains("frame=")){
					encodingPercent(batch, frames, line);
				}
				
			}
			
			int exitValue = process.exitValue();				
			if(exitValue != 0) {					
				logger.error("FFMPEG encoding exitValue : " + exitValue);
				encodingFail(batch);
				return;
			}
			
			List<String> thumbnailArgList = new ArrayList();
			
			thumbnailArgList.add(ffmpegEncoderPath + "ffmpeg.exe");
			thumbnailArgList.add("-y");
			
			thumbnailArgList.add("-loglevel");
			thumbnailArgList.add("info");
			
			thumbnailArgList.add("-i");
			thumbnailArgList.add(output);

			thumbnailArgList.add("-ss");
			thumbnailArgList.add("00:00:03");

			thumbnailArgList.add("-vframes");
			thumbnailArgList.add("1");
			
			String thumbnailPath = batch.getEncodingFilePath() + batch.getMediaId() + ".jpg";
			thumbnailArgList.add(thumbnailPath);
			
			logger.info("VIDEO THUMBNAIL PARAM LIST : " + thumbnailArgList);				
			
			ProcessBuilder pb2 = new ProcessBuilder(thumbnailArgList);
			pb2.redirectErrorStream(true);
			
			Process process2 = pb2.start();
			
			process2.waitFor();
			
			int exitValue2 = process2.exitValue();
			logger.info("FFMPEG thumbnail exitValue : " + exitValue2);
			
			if(exitValue2 == 0) {
				batch.setThumbnailPath(thumbnailPath);
				batch.setStatus(EncodingStatus.SUCCESS.getName());
				batchRepository.save(batch);
				
				String returnUrl = batch.getReturnUrl();
				if(returnUrl != null && !returnUrl.equals("")) {
					encodingSuccess(batch);
				}
				
			}else {
				encodingFail(batch);
			}
		
		}catch(IOException | InterruptedException e ) {
			logger.info("ERROR : " + e);
			encodingFail(batch);
		};
		
	}
	
	private void encodingAudio(Batch batch) {

		try {
			
			logger.info("BATCH MEDIA : " + batch.getMediaId());
			logger.info("BATCH MEDIA PATH : " + batch.getOriginalFilePath() + batch.getOriginalFileName());
			
			batch.setStatus(EncodingStatus.ENCODING.getName());
			batchRepository.save(batch);
			
			String input = batch.getOriginalFilePath() + batch.getOriginalFileName();
			String output = batch.getEncodingFilePath() + batch.getEncodingFileName();
			
			File outputDir = new File(batch.getEncodingFilePath());
			
			if(!outputDir.exists()) {
				outputDir.mkdirs();
			}
			
			List<String> list = new ArrayList();
			
			list.add(ffmpegEncoderPath + "ffmpeg.exe");
			list.add("-y");
			
			list.add("-loglevel");
			list.add("info");
			
			list.add("-strict");
			list.add("experimental");
			
			list.add("-i");
			list.add(input);
			
			list.add("-ab");
			list.add("192k");
			
			list.add("-ac");
			list.add("2");
			
			list.add(output);
			
			logger.info("AUDIO ENCODING PARAM LIST : " + list);
			
			ProcessBuilder pb = new ProcessBuilder(list);
			pb.redirectErrorStream(true);
			
			//타겟 하나 잡기
			Process process = pb.start();
			
			InputStreamReader in = new InputStreamReader(process.getInputStream());
			BufferedReader br = new BufferedReader(in);
			
			String line = "";
			while((line = br.readLine()) != null) {
				logger.info("line : " + line);				
			}
			
			int exitValue = process.exitValue();				
			if(exitValue != 0) {					
				logger.error("FFMPEG encoding exitValue : " + exitValue);
				encodingFail(batch);
				return;
			}else {
				
				batch.setStatus(EncodingStatus.SUCCESS.getName());
				batchRepository.save(batch);
				
				String returnUrl = batch.getReturnUrl();
				if(returnUrl != null && !returnUrl.equals("")) {
					encodingSuccess(batch);
				}				
				
			}
		
		}catch(IOException e) {
			logger.info("ERROR : " + e);
			encodingFail(batch);
		};
		
	}
	
	private void encodingImage(Batch batch) {
		
		try {
			
			logger.info("BATCH MEDIA : " + batch.getMediaId());
			logger.info("BATCH MEDIA PATH : " + batch.getOriginalFilePath() + batch.getOriginalFileName());
			
			batch.setStatus(EncodingStatus.ENCODING.getName());
			batchRepository.save(batch);
			
			String input = batch.getOriginalFilePath() + batch.getOriginalFileName();
			String output = batch.getEncodingFilePath() + batch.getEncodingFileName();
			
			File outputDir = new File(batch.getEncodingFilePath());
			
			if(!outputDir.exists()) {
				outputDir.mkdirs();
			}
			
			List<String> list = new ArrayList();
			
			list.add(imageEncoderPath + "magick.exe");
			list.add(input);
			list.add(output);
			
			logger.info("IMAGE ENCODING PARAM LIST : " + list);
			
			ProcessBuilder pb = new ProcessBuilder(list);
			pb.redirectErrorStream(true);
			
			//타겟 하나 잡기
			Process process = pb.start();
			
			InputStreamReader in = new InputStreamReader(process.getInputStream());
			BufferedReader br = new BufferedReader(in);
			
			String line = "";
			while((line = br.readLine()) != null) {
				logger.info("line : " + line);			
			}
			
			int exitValue = process.exitValue();

			if(exitValue == 0) {
				batch.setThumbnailPath(output);
				batch.setStatus(EncodingStatus.SUCCESS.getName());
				batchRepository.save(batch);
				
				String returnUrl = batch.getReturnUrl();
				if(returnUrl != null && !returnUrl.equals("")) {
					encodingSuccess(batch);
				}
				
			}else {
				encodingFail(batch);
			}
		
		}catch(IOException e) {
			logger.info("ERROR : " + e);
			encodingFail(batch);
		};		
		
	}
	
	
	
	private void encodingFail(Batch batch) {
		batch.setStatus(EncodingStatus.FAIL.getName());
		batchRepository.save(batch);
		
		Map<String, Object> param = new HashMap();
		param.put("mediaId", batch.getMediaId());
		param.put("status", EncodingStatus.FAIL.getName());
		httpRequestUtil.encodingRequest(batch.getReturnUrl(), param);
	}
	
	private void encodingSuccess(Batch batch) {
		
		Map<String,Object> param = new HashMap();
		param.put("mediaId", batch.getMediaId());
		param.put("status", EncodingStatus.SUCCESS.getName());
		param.put("encodingFileName", batch.getEncodingFileName());
		param.put("encodingFilePath", batch.getEncodingFilePath());
		
		File encodingFile = new File(batch.getEncodingFilePath() + batch.getEncodingFileName());
		param.put("encodingFileSize", encodingFile.length() + "");
		
		param.put("thumbnailPath", batch.getThumbnailPath());
		
		httpRequestUtil.encodingRequest(batch.getReturnUrl(), param);		
		
	}
	
	private void encodingPercent(Batch batch, Long frames, String line) {
		Long currentFrame =  Long.parseLong(line.split("\s+")[1]);		
		int percent = (int)((currentFrame*1.0) / (frames*1.0) * 100.0);
		
		logger.info("currentFrame : " + currentFrame + ", percent : " + percent);
		
		Map<String,Object> param = new HashMap();
		param.put("mediaId", batch.getMediaId());
		param.put("status", EncodingStatus.ENCODING.getName());
		param.put("percent", percent);
		
		httpRequestUtil.encodingRequest(batch.getReturnUrl(), param);
		
	}

}

