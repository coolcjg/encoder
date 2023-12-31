package com.cjg.sonata.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.domain.Media;
import com.cjg.sonata.repository.BatchRepository;
import com.cjg.sonata.repository.MediaRepository;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

@Service
public class SchedService {
	
	Logger logger = LoggerFactory.getLogger(SchedService.class);
	
	@Value("${encoderPath}")
	private String encoderPath;
	
	@Autowired
	BatchRepository batchRepository;
	
	@Autowired
	MediaRepository mediaRepository;
	
	public void encoding() {
		
		List<Batch> batchList = batchRepository.findAllByStatusOrderByRegDateAsc(EncodingStatus.WAITING.getName());
		
		if(batchList == null || batchList.size() == 0) {
			return;		
		}
		
		for(Batch batch : batchList) {
			
			Media media = mediaRepository.findByMediaId(batch.getMediaId());
			
			try {
				
				Long frames = 0l;
				
				FFprobe ffprobe = new FFprobe(encoderPath + "ffprobe.exe");
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
				
				list.add(encoderPath + "ffmpeg.exe");
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
				
				logger.info("FFMPEG ENCODING PARAM LIST : " + list);
				
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
						updatePercent(media, frames, line);
					}
					
				}
				
				int exitValue = process.exitValue();				
				if(exitValue != 0) {					
					logger.error("FFMPEG encoding exitValue : " + exitValue);
					encodingFail(batch, media);
					return;
				}
				
				List<String> thumbnailArgList = new ArrayList();
				
				thumbnailArgList.add(encoderPath + "ffmpeg.exe");
				thumbnailArgList.add("-y");
				
				thumbnailArgList.add("-loglevel");
				thumbnailArgList.add("info");
				
				thumbnailArgList.add("-i");
				thumbnailArgList.add(output);

				thumbnailArgList.add("-ss");
				thumbnailArgList.add("00:00:03");

				thumbnailArgList.add("-vframes");
				thumbnailArgList.add("1");
				
				String thumbnailOutput = batch.getEncodingFilePath() + batch.getMediaId() + ".jpg";
				thumbnailArgList.add(thumbnailOutput);
				
				logger.info("FFMPEG THUMBNAIL PARAM LIST : " + thumbnailArgList);				
				
				ProcessBuilder pb2 = new ProcessBuilder(thumbnailArgList);
				pb2.redirectErrorStream(true);
				
				Process process2 = pb2.start();
				
				process2.waitFor();
				
				int exitValue2 = process2.exitValue();
				logger.info("FFMPEG thumbnail exitValue : " + exitValue2);
				
				if(exitValue2 == 0) {
					batch.setThumbPath(thumbnailOutput);
					batch.setStatus(EncodingStatus.SUCCESS.getName());
					batchRepository.save(batch);
				}else {
					encodingFail(batch, media);
				}
			
			}catch(IOException | InterruptedException e ) {
				logger.info("ERROR : " + e);
				encodingFail(batch, media);
				continue;
			};				
			
		}
		
	}
	
	private void encodingFail(Batch batch, Media media) {
		batch.setStatus(EncodingStatus.FAIL.getName());
		batchRepository.save(batch);				
		
		media.setStatus(EncodingStatus.FAIL.getName());
		mediaRepository.save(media);
	}
	
	private void updatePercent(Media media, Long frames, String line) {
		Long currentFrame =  Long.parseLong(line.split("\s+")[1]);		
		int percent = (int)((currentFrame*1.0) / (frames*1.0) * 100.0);
		
		logger.info("currentFrame : " + currentFrame + ", percent : " + percent);
		media.setPercent(percent);
		mediaRepository.save(media);
		
	}

}

