package com_gmail_sleepy771.astorage;

import java.util.Map;

import com_gmail_sleepy771.astorage.exceptions.DeserializationException;
import com_gmail_sleepy771.astorage.utilities.ObjectData;

public abstract class Deserializer implements Cloneable{

	public abstract Object deserialize(Map<String, Object> od) throws DeserializationException;
	
	@Override
	public abstract Object clone();
	
	public abstract String description();
	
	public abstract boolean isValid(ObjectData od);
}
