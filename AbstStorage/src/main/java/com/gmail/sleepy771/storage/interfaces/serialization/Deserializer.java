package com.gmail.sleepy771.storage.interfaces.serialization;

import com.gmail.sleepy771.storage.exceptions.InvalidDataException;
import com.gmail.sleepy771.storage.interfaces.datastructures.Data;

public interface Deserializer {
	public Deserializer deserialize(Data d) throws InvalidDataException;

	public Object asObject();

	public <T> T asTypedObject();
}
