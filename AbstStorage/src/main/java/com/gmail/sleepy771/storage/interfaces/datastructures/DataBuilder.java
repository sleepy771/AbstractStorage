package com.gmail.sleepy771.storage.interfaces.datastructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.gmail.sleepy771.storage.interfaces.Buildable;
import com.gmail.sleepy771.storage.interfaces.path.Path;

public interface DataBuilder extends Buildable<Data>, Data {
    
    public static final Set<Class<?>> WRITABLE_CLASSES = Collections.unmodifiableSet(new HashSet<Class<?>>(Arrays.asList(
	    Byte.class, Integer.class, Short.class, Long.class, Boolean.class, Character.class, String.class, Double.class, Float.class)));
    
    public DataBuilder put(String name, Object obj);
    
    public DataBuilder putAll(Map<String, Object> data);
    
    public DataBuilder putAll(Data d);
    
    public DataBuilder putAll(DataBuilder d);
    
    public DataBuilder setName(String name);
    
    public DataBuilder setClass(Class<?> clazz);
    
    public DataBuilder remove(String name);
    
    public DataBuilder removeAll(Collection<String> names);
    
    public Map<String, Object> getRaw();
    
    public DataBuilder clear();
    
    public Object get(String name);
    
    public Object get(Path p);
    
    public String getName();
    
    public Map<String, Object> getByClass(Class<?> clazz);
    
    public Class<?> getRepresentedClass();
}
