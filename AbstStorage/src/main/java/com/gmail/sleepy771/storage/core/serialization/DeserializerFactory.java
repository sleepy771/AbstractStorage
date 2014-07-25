package com.gmail.sleepy771.storage.core.serialization;

import java.util.HashMap;
import java.util.Map;

import com.gmail.sleepy771.storage.exceptions.DeserializerNotFoundException;
import com.gmail.sleepy771.storage.interfaces.serialization.Deserializer;

public class DeserializerFactory {
	private final Map<Class<?>, Class<Deserializer>> deserializers = new HashMap<>();

	public void registrateDeserializer(Class<?> objectClass,
			Class<Deserializer> deserializerClass) {
		deserializers.put(objectClass, deserializerClass);
	}

	public void unregistrateDeserializer(Class<?> objectClass) {
		deserializers.remove(objectClass);
	}

	public Deserializer getDeserializer(Class<?> objectClass)
			throws DeserializerNotFoundException, InstantiationException,
			IllegalAccessException {
		if (!deserializers.containsKey(objectClass))
			throw new DeserializerNotFoundException();
		return deserializers.get(objectClass).newInstance();
	}

	public void dispose() {

	}
}
