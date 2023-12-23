package com.cjg.sonata.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.service.ApiService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ApiController {
	
	@Autowired
	ApiService apiService;
	
	@PostMapping("/batch")
	public Map<String, Object> batchInsert(HttpServletRequest request, BatchDTO batchDTO){
		return apiService.batchInsert(batchDTO);
	}
	
	
}
