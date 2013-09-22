package com_gmail_sleepy771.astorage;

import java.util.Map;

public interface Storable {
	public Map<String, Object> serialize() throws SerializationException;
}
