package com.cjg.sonata.common;

public enum EncodingStatus {
	WAITING("waiting")
	, ENCODING("encoding")
	, SUCCESS("success")
	, FAIL("fail");
	
	final private String name;
	
	public String getName() {
		return name;
	}
	
	private EncodingStatus(String name) {
		this.name = name;
	}
}
