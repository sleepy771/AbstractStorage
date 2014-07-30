package com.gmail.sleepy771.storage.base;

public interface Connector {
	public Response recieve();
	
	public void send(Task task);
	
	public void addListener(ResponseListener l);
	
	public void removeListener(ResponseListener l);
}
