package com.cjg.sonata.service;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ApiService {
	
	Logger logger = LoggerFactory.getLogger(ApiService.class);
	
	@Autowired
	BatchRepository batchRepository;
	
	public Map<String, Object> batchInsert(BatchDTO batchDTO){

		Map<String, Object> result = new HashMap<String, Object>();
		String type = batchDTO.getType();
		
		Batch batch = new Batch();
		batch.setMediaId(batchDTO.getMediaId());
		batch.setType(type);
		batch.setStatus(EncodingStatus.WAITING.getName());
		
		int index = batchDTO.getOriginalFile().lastIndexOf("/");
		String originalFilePath = batchDTO.getOriginalFile().substring(0, index+1);
		batch.setOriginalFilePath(originalFilePath);
		batch.setOriginalFileName(batchDTO.getOriginalFile().substring(index+1));
		
		String encodingFilePath = originalFilePath.replaceAll("original", "encoding");
		batch.setEncodingFilePath(encodingFilePath);
		
		if(type.equals("video")) {
			batch.setEncodingFileName(batchDTO.getMediaId() + ".mp4");
		}else if(type.equals("audio")) {
			batch.setEncodingFileName(batchDTO.getMediaId() + ".mp3");
		}else if(type.equals("image")) {
			batch.setEncodingFileName(batchDTO.getMediaId() + ".jpg");
		}
		
		batch.setReturnUrl(batchDTO.getReturnUrl());
		
		batchRepository.save(batch);

		result.put("message", "success");
		
		return result;
	}

}
