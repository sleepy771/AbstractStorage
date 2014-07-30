package com.gmail.sleepy771.storage.base;

public interface Response {
	enum ResponseCode{
		OK, ERROR, 
	}
	
	public ResponseCode code(); 
	
	public long respondedTo();
	
	public Object get();
}
