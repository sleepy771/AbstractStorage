package com.gmail.sleepy771.datastructures;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.gmail.sleepy771.storage.Buildable;
import com.gmail.sleepy771.storage.Path;

public interface DataBuilder extends Buildable<Data>, Iterable<Entry<String, Object>>, Data {
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
