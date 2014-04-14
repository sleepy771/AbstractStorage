package com.gmail.sleepy771.storage.serialization;

import com.gmail.sleepy771.datastructures.DataBuilder;
import com.gmail.sleepy771.datastructures.DataBuilderImpl;

import java.util.Map;
import java.util.TreeMap;

// 1 -> 1
public abstract class Serializer<T> implements Serializable {
    private Map<String, Object> notSerialized = new TreeMap<String, Object>();
    private Map<String, T> forSerialization = new TreeMap<>();
    private Class<T> canSerialize;
    
    public Serializer(Class<T> clazz) {
	this.canSerialize = clazz;
    }
    
    @SuppressWarnings("unchecked")
    public void insertObject(String name, Object obj) {
	if(canSerialize.isInstance(obj)) {
	    forSerialization.put(name, (T) obj);
	} else {
	    notSerialized.put(name, obj);
	}
    }
    
    protected DataBuilder createDataBuilder(String objName) {
	return new DataBuilderImpl(objName, this.canSerialize);
    }
}
