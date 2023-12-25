package com.cjg.sonata.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.sonata.comm.EncodingStatus;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;

@Service
@Transactional
public class ApiService {
	
	@Autowired
	BatchRepository batchRepository;
	
	public Map<String, Object> batchInsert(BatchDTO batchDTO){
		Map<String, Object> result = new HashMap<String, Object>();
		
		Batch batch = new Batch();
		batch.setMediaId(batchDTO.getMediaId());
		batch.setType(batchDTO.getType());
		batch.setStatus(EncodingStatus.WAITING.getName());
		batch.setOriginalFile(batchDTO.getOriginalFile());
		batch.setReturnUrl(batchDTO.getReturnUrl());
		
		batchRepository.save(batch);	

		result.put("code", HttpStatus.CREATED.value());
		result.put("message", "success");
		
		return result;
	}

}
