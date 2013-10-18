package com.gmail.sleepy771.storage_interface.misc;

public class Result<T> {
    enum Type{
	SUCCESS, NOT_COMPLETE, ERROR, WARNING
    }
    T data;
    Type type;
    
    public Result(Type type, T data) {
	this.data = data;
	this.type = type;
    }
    
    public T get() {
	return data;
    }
    
    public Type getResultType() {
	return this.type;
    }
}
