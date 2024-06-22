package com.cjg.sonata.controller;

import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Controller
public class ApiController {
	
	@Autowired
	ApiService apiService;
	
	@PostMapping("/batch")
	public Map<String, Object> batchInsert(BatchDTO batchDTO){
		return apiService.batchInsert(batchDTO);
	}

	@GetMapping("/")
	public String home(){
		return "Hello World!";
	}
	
	
}
