package com.gmail.sleepy771.storage.base;

public interface Manager {
	public void init();
	
	public void close();
	
	public void setConnector(Connector connector);
	
	public Connector getConnector();
}
