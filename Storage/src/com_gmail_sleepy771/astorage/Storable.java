package com_gmail_sleepy771.astorage;

import java.io.Serializable;
import java.util.Map;

import com_gmail_sleepy771.astorage.exceptions.SerializationException;

public interface Storable extends Serializable {
	public Map<String, Object> serialize() throws SerializationException;
}
