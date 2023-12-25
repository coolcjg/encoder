package com.cjg.sonata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionAdvice {
	
	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ExceptionEntity> exceptionHandler(HttpServletRequest request, final RuntimeException e){
		e.printStackTrace();
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ExceptionEntity.builder()
				.code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message(e.getMessage())
				.build());
	}

}
