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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "board")
@DynamicUpdate
public class Media {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mediaId;
	
	@ManyToOne
	@JoinColumn(name="boardId", nullable = false)
	private Board board;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd hh:mm")
	@Column(nullable = false)
	private LocalDateTime regDate;
	
	@Column(nullable = false, length = 10)
	private String type;
	
	@Column(nullable = false, length = 8)
	private String status;
	
	@Column(nullable = false)
	private String originalFilePath;
	
	@Column(nullable = false)
	private String originalFileName;
	
	@Column(nullable = false)
	private String originalFileClientName;
		
	@Column(nullable = false)
	private Long originalFileSize;
		
	
	private String encodingFilePath;
	
	private String encodingFileName;
	
	private Long encodingFileSize;
	
	private String thumbnailPath;
	
	private String thumbPath;
	
	@Column(nullable = false, length = 3)
	private int percent;
	
}