package com_gmail_sleepy771.astorage;

import java.io.IOException;
import java.util.Set;

public abstract class AbstractStorage {
	
	public abstract void store(ObjectData data) throws IOException;
	public abstract void storeAll(Set<ObjectData> dataCollection) throws IOException;
	public abstract Set<ObjectData> load(Query q) throws IOException;
	
	public abstract boolean contains(Query q) throws IOException;
	public abstract long count(Query q) throws IOException;
	
	public abstract long getLastSerial() throws IOException;
	
	public abstract long size() throws IOException;
	public abstract long capacity() throws IOException, UnsupportedOperationException;
	
	public abstract Object backup() throws IOException;
	public abstract boolean restore(Object backup) throws IOException;
}
