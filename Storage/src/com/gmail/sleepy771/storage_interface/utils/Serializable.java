package com.gmail.sleepy771.storage_interface.utils;

import java.util.Collection;

public interface Serializable {
    public Collection<String> fieldNames();

    public Data serialize();
}
