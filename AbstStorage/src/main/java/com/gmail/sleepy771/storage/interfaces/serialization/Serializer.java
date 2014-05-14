package com.gmail.sleepy771.storage.interfaces.serialization;

import com.gmail.sleepy771.storage.interfaces.datastructures.Data;

public interface Serializer {
    public Data serialize(Object obj);
}
