package com.gmail.sleepy771.storage.serialization;

import java.util.HashMap;
import java.util.Map;

public class DeserializerFactory {
    private static Map<String, Class<ObjectBuilder<?>>> builders = new HashMap<>();
    
    public static void registrateObjectBuilder(String clazzName, Class<ObjectBuilder<?>> clazz) {
	DeserializerFactory.builders.put(clazzName, clazz);
    }
    
    public static <T> ObjectBuilder<T> getBuilder(String clazz) throws InstantiationException, IllegalAccessException {
	return (ObjectBuilder<T>) builders.get(clazz).newInstance();
    }
    
    public static void removeBuilder(String clazz) {
	builders.remove(clazz);
    }
}
