package com.cjg.sonata.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Batch {
	
	@Id
	@GeneratedValue
	private Long batchId;
	
	private Long mediaId;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd hh:mm")
	@Column(nullable = false)
	private LocalDateTime regDate;
	
	@Column(nullable = false, length = 10)
	private String type;
	
	@Column(nullable = false, length = 8)
	private String status;
	
	private String originalFile;
	
	private String encodingFile;
	
	private String thumbPath;
	
	private String returnUrl;

}
