package com.gmail.sleepy771.storage.base;

public interface StorageControler {
	public Connector createConnector(Manager manager);
	
	public void removeConnector(Connector connector);
}
