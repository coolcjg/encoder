package com.cjg.sonata.domain;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@DynamicUpdate
public class User {
	
	@Id
	@Column(name ="USER_ID")
	private String userId;
	
	private String password;
	
	private String salt;
	
	private String name;
	
	private String phone;
	
	private String commnets;
	
	private LocalDate birthDay;
	
	@Embedded
	private Address address;
	
	private String refreshToken;
}
