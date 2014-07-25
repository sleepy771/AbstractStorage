package com.gmail.sleepy771.storage.core.serialization;

import java.util.HashMap;
import java.util.Map;

import com.gmail.sleepy771.storage.exceptions.SerializerNotFoundException;
import com.gmail.sleepy771.storage.interfaces.serialization.Serializer;

public class SerilaizerFactory {
	private final Map<Class<?>, Class<Serializer>> serializers = new HashMap<>();

	public void registrateSerializer(Class<?> objectClass,
			Class<Serializer> serializerClass) {
		serializers.put(objectClass, serializerClass);
	}

	public void unregistrateSerializer(Class<?> objectClass) {
		serializers.remove(objectClass);
	}

	public Serializer instantiateSerializerFor(Class<?> clazz)
			throws SerializerNotFoundException, InstantiationException,
			IllegalAccessException {
		if (!serializers.containsKey(clazz))
			throw new SerializerNotFoundException();
		return serializers.get(clazz).newInstance();
	}

	public void dispose() {

	}
}
