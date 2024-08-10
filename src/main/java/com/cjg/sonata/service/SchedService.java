package com.cjg.sonata.service;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.common.HttpRequestUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.domain.Gallery;
import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;
import com.cjg.sonata.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchedService {
	
	Logger logger = LoggerFactory.getLogger(SchedService.class);

	@Setter
	@Value("${ffmpegPath}")
	private String ffmpegPath;

	@Setter
	@Value("${ffprobePath}")
	private String ffprobePath;

	@Setter
	@Value("${imageEncoderPath}")
	private String imageEncoderPath;
	
	@Autowired
	BatchRepository batchRepository;

	@Autowired
	GalleryRepository galleryRepository;
	
	@Autowired
	HttpRequestUtil httpRequestUtil;

	@Autowired
	ApiService apiService;

	public void encoding(BatchDTO batchDTO) {

		apiService.batchInsert(batchDTO);
		Batch batch = batchRepository.findByMediaId(batchDTO.getMediaId());

		if(batch == null) {
			return;
		}

		switch (batch.getType()) {
			case "video" -> encodingVideo(batch);
			case "audio" -> encodingAudio(batch);
			case "image" -> encodingImage(batch);
		}

	}
	
	private void encodingVideo(Batch batch) {
		
		
		/*테스트*/
		
		try {
			
			Long frames = 0L;

			FFprobe ffprobe = new FFprobe(ffprobePath);
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
			
			List<String> list = new ArrayList<String>();
			
			list.add(ffmpegPath);
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
				if(line.contains("frame=")){
					encodingPercent(batch, frames, line);
				}
				
			}

			process.waitFor();

			int exitValue = process.exitValue();				
			if(exitValue != 0) {					
				logger.error("FFMPEG encoding exitValue : " + exitValue);
				encodingFail(batch);
				return;
			}
			
			List<String> thumbnailArgList = new ArrayList<String>();
			
			thumbnailArgList.add(ffmpegPath);
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
				if(returnUrl != null && !returnUrl.isEmpty()) {
					insertGallery(setGalleryParam(batch));
					encodingSuccess(batch);
				}
				
			}else {
				encodingFail(batch);
			}
		
		}catch(IOException | InterruptedException e ) {
			logger.info("ERROR : " + e);
			encodingFail(batch);
		}
		
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
			
			List<String> list = new ArrayList<String>();
			
			list.add(ffmpegPath);
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
				logger.info("audio encoding : " + line);
			}

			process.waitFor();
			
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
		
		}catch(IOException | InterruptedException e) {
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
			
			list.add(imageEncoderPath);
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
			}

			process.waitFor();
			
			int exitValue = process.exitValue();

			if(exitValue == 0) {
				batch.setThumbnailPath(output);
				batch.setStatus(EncodingStatus.SUCCESS.getName());
				batchRepository.save(batch);
				
				String returnUrl = batch.getReturnUrl();
				if(returnUrl != null && !returnUrl.equals("")) {
					encodingSuccess(batch);
					insertGallery(setGalleryParam(batch));
				}
				
			}else {
				encodingFail(batch);
			}
		
		}catch(IOException | InterruptedException e) {
			logger.info("ERROR : " + e);
			encodingFail(batch);
		};		
		
	}
	
	
	
	public void encodingFail(Batch batch) {
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

	public Gallery insertGallery(Gallery gallery){
		return galleryRepository.save(gallery);
	}

	public Gallery setGalleryParam(Batch batch){

		//기본 파라미터 설정
		Gallery gallery = new Gallery();
		gallery.setMediaId(batch.getMediaId());
		gallery.setType(batch.getType());

		int index  = batch.getEncodingFilePath().lastIndexOf("/upload/") + 7;

		gallery.setThumbnailFilePath(batch.getEncodingFilePath().substring(index));
		gallery.setThumbnailFileName(batch.getMediaId() + ".jpg");

		gallery.setEncodingFilePath(batch.getEncodingFilePath().substring(index));
		gallery.setEncodingFileName(batch.getEncodingFileName());

		gallery.setStatus("N");

		return gallery;

	}

}

