package com.gmail.sleepy771.storage.serialization;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {
    private static Map<String, Class<Serializer<?>>> serializers = new HashMap<>();
    
    // Through spring DI
    protected static void registrateNewSerializer(String name, Class<Serializer<?>> clazz) {
	serializers.put(name, clazz);
    }
    
    public static Serializer<?> getSerializer(String clazzName) throws InstantiationException, IllegalAccessException {
	// In constructor should be injected Class
	return serializers.get(clazzName).newInstance();
    }
    
    public static Class<?> removeSerializer(String clazzName) {
	return serializers.remove(clazzName);
    }
}
