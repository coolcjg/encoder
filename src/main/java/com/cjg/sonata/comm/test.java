package com.cjg.sonata.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class test {
	
	public static void main(String[] args) {
		
			//String filePath = "D:/NAS/upload/original/2023/12/21/e51b8abc-e01a-44a4-8c9c-ca3ea27b060e1.mp4";
			String filePath = "D:/NAS/aaa.txt";
			String endPath = "D:/NAS/result.mp4";
			
			List<String> list = new ArrayList();
			
			list.add("D:/NAS/encoder/ffmpeg.exe");
			list.add("-y");
			list.add("-i");
			list.add(filePath);
			list.add(endPath);
			
			String[] a = new String[list.size()];
			
			for(int i=0; i<list.size(); i++) {
				a[i] = list.get(i);
			}
			
			System.out.println("list : " + list);
			
			ProcessBuilder pb = new ProcessBuilder(list);
			pb.redirectErrorStream(true);
			
			//타겟 하나 잡기
			try {
				Process process = pb.start();
				
				InputStreamReader in = new InputStreamReader(process.getInputStream());
				BufferedReader br = new BufferedReader(in);
				
				String data = "";
				while((data = br.readLine()) != null) {
					data = br.readLine();
					
					System.out.println("data : " + data);
				}
				
				int exitValue = process.exitValue();
				
				System.out.println("exitValue : " + exitValue);
			
			}catch(IOException e) {
				System.out.println("ERROR : " + e);
				
			};		
		
	}

}
