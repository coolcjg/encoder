package com.cjg.sonata.sched;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cjg.sonata.service.SchedService;

@Component
public class Sched {
	
	Logger logger = LoggerFactory.getLogger(Sched.class);
	
	@Autowired
	SchedService schedService;
	
	/* 초(0~59)
	 * 분(0~59)
	 * 시간(0~23)
	 * 일(1~31)
	 * 월(1~12)
	 * 요일(0-6)(0:일 ~ 6:토)
	 * ? : 설정 값 없음.(날짜, 요일에서만 사용 가능)
	 */
	
	
	@Scheduled(fixedDelay = 10000)
	public void scheduleTaskUsingCronExpression() {
		
		System.out.println("함수 실행");
		schedService.encoding();
	}
	
	
	
}
