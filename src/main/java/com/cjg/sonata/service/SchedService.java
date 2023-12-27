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

import com.cjg.sonata.comm.EncodingStatus;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.repository.BatchRepository;

@Service
public class SchedService {
	
	Logger logger = LoggerFactory.getLogger(SchedService.class);
	
	@Value("${encoderPath}")
	private String encoderPath;
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	@Autowired
	BatchRepository batchRepository;
	
	public void encoding() {
		
		List<Batch> batchList = batchRepository.findAllByStatusOrderByRegDateAsc(EncodingStatus.WAITING.getName());
		
		if(batchList == null || batchList.size() == 0) {
			return;		
		}
		
		for(Batch batch : batchList) {
			
			logger.info("BATCH MEDIA : " + batch.getMediaId());
			logger.info("BATCH MEDIA PATH : " + batch.getOriginalFilePath() + batch.getOriginalFileName());
			
			batch.setStatus(EncodingStatus.ENCODING.getName());
			batchRepository.save(batch);
			
			
			String input = uploadPath + batch.getOriginalFilePath() + batch.getOriginalFileName();
			String output = uploadPath + batch.getEncodingFilePath() + batch.getEncodingFileName();
			
			File outputDir = new File(uploadPath + batch.getEncodingFilePath());
			
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
			
			System.out.println("PROCESS PARAM LIST : " + list);
			
			ProcessBuilder pb = new ProcessBuilder(list);
			pb.redirectErrorStream(true);
			
			//타겟 하나 잡기
			try {
				Process process = pb.start();
				
				InputStreamReader in = new InputStreamReader(process.getInputStream());
				BufferedReader br = new BufferedReader(in);
				
				String data = "";
				while((data = br.readLine()) != null) {
					data = br.readLine();
					System.out.println("data : " + data);
				}
				
				int exitValue = process.exitValue();
				
				logger.info("FFMPEG exitValue : " + exitValue);
				
				if(exitValue == 0) {
					batch.setStatus(EncodingStatus.SUCCESS.getName());
					batchRepository.save(batch);
				}
			
			}catch(IOException e) {
				System.out.println("ERROR : " + e);
				
			};				
			
		}
		
	}	

}

