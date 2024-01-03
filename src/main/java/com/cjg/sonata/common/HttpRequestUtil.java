package com.cjg.sonata.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpRequestUtil {
	
	Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);
	
	public String encodingRequest(String urlParam, Map<String, Object> map) {
		
		logger.info("encodingRequest PARAM : " + map);
		
		String myResult = "";
		
		try {
			
			URL url = new URL(urlParam);
			
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			
			//전송모드 설정
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestMethod("POST");
			
			//헤더 세팅 (form으로 넘어온것과 같은 방식으로 처리)
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			
			//서버로 값 전송
			StringBuffer buffer = new StringBuffer();
			
			if(map != null) {
				for(String key : map.keySet()) {
					buffer.append(key).append("=").append(map.get(key)).append("&");
				}
			}
			
			OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(outStream);
			writer.write(buffer.toString());
			writer.flush();
			
			
			//서버에서 전송 받기
			InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(tmp);
			StringBuilder builder = new StringBuilder();
			String str;
			
			while((str = reader.readLine()) != null) {
				builder.append(str + "\n");
			}
			
			myResult = builder.toString();
			
			
		}catch(MalformedURLException e) {
			logger.error("ERROR : ", e);
		}catch(IOException e) {
			logger.error("ERROR : ", e);
		}
		
		return myResult;
		
	}	

}
