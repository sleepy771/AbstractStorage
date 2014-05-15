package com.gmail.sleepy771.storage.interfaces.serialization;

import com.gmail.sleepy771.storage.exceptions.UnserializableObjectException;
import com.gmail.sleepy771.storage.interfaces.datastructures.Data;

public interface Serializer {
    public Data serialize(Object obj, String name) throws UnserializableObjectException;
    
    public void dispose();
}
