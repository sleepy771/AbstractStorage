package com.gmail.sleepy771.storage_interface.misc.serialization;

import java.util.HashMap;
import java.util.UUID;

import com.gmail.sleepy771.storage_interface.misc.Data;

public interface Desrializer {
    
    public void deserialize(Data data, HashMap<UUID, Object> map);
}
