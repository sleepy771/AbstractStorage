package com.gmail.sleepy771.storage.core.datastructures;

public class Canceled {
    private final String reason;
    
    public Canceled() {
	this("unknown");
    }
    
    public Canceled(String reason) {
	this.reason = reason;
    }
    
    public String getReason() {
	return this.reason;
    }
}
