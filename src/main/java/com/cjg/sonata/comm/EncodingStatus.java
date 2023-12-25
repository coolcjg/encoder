package com.cjg.sonata.comm;

public enum EncodingStatus {
	WAITING("waiting")
	, ENCODING("encoding")
	, SUCCESS("success");
	
	final private String name;
	
	public String getName() {
		return name;
	}
	
	private EncodingStatus(String name) {
		this.name = name;
	}
}
