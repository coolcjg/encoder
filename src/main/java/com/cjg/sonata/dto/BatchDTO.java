package com.cjg.sonata.dto;

import lombok.Data;

@Data
public class BatchDTO {
	
	private Long mediaId;
	
	private String type;
	
	private String originalFile;
	
	private String returnUrl;
	
}
