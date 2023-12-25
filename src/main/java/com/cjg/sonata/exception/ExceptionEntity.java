package com.cjg.sonata.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExceptionEntity {
	private int code;
	private String message;
	
	@Builder
	public ExceptionEntity(HttpStatus status, int code, String message) {
		this.code = code;
		this.message = message;
	}	

}
