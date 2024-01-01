package com.cjg.sonata.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@DynamicUpdate
public class Batch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long batchId;
	
	@Column(nullable = false)
	private Long mediaId;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd hh:mm")
	@Column(nullable = false)
	private LocalDateTime regDate;
	
	@Column(nullable = false, length = 10)
	private String type;
	
	@Column(nullable = false, length = 8)
	private String status;
	
	private String originalFilePath;
	private String originalFileName;
	
	private String encodingFilePath;
	private String encodingFileName;
	
	private String thumbnailPath;
	
	private String returnUrl;

}
