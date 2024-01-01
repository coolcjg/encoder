package com.cjg.sonata.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.common.FFprobeUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;

@Service
@Transactional
public class ApiService {
	
	
	
	@Autowired
	BatchRepository batchRepository;
	
	@Autowired
	FFprobeUtil ffprobeUtil;	
	
	public Map<String, Object> batchInsert(BatchDTO batchDTO){
		Map<String, Object> result = new HashMap<String, Object>();
		
		Batch batch = new Batch();
		batch.setMediaId(batchDTO.getMediaId());
		
		String type = ffprobeUtil.getType(batchDTO.getOriginalFile());
		System.out.println("----------------type : " + type);
		
		batch.setType("video");
		batch.setStatus(EncodingStatus.WAITING.getName());
		
		int index = batchDTO.getOriginalFile().lastIndexOf("/");
		String originalFilePath = batchDTO.getOriginalFile().substring(0, index+1);
		batch.setOriginalFilePath(originalFilePath);
		batch.setOriginalFileName(batchDTO.getOriginalFile().substring(index+1));
		
		String encodingFilePath = originalFilePath.replaceAll("original", "encoding");
		batch.setEncodingFilePath(encodingFilePath);
		batch.setEncodingFileName(batchDTO.getMediaId() + ".mp4");
		
		batch.setReturnUrl(batchDTO.getReturnUrl());
		
		batchRepository.save(batch);	

		result.put("code", HttpStatus.CREATED.value());
		result.put("message", "success");
		
		return result;
	}

}
