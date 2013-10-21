package com.gmail.sleepy771.storage_interface.misc;

public class Result<T> {
    enum Flag{
	SUCCESS, NOT_COMPLETE, COMPLETE, ERROR, WARNING
    }
    T data;
    Flag type;
    
    public Result(Flag type, T data) {
	this.data = data;
	this.type = type;
    }
    
    public T get() {
	return data;
    }
    
    public Flag getFlag() {
	return this.type;
    }
    
    public void setFlag(Flag f) {
	
    }
    
    public boolean isIncomplete() {
	return this.type == Flag.NOT_COMPLETE;
    }
}
