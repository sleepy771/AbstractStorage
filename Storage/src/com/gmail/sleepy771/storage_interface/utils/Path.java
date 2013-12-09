package com.gmail.sleepy771.storage_interface.utils;

import java.util.List;

public interface Path extends Iterable<String> {
    public List<String> asList();

    public List<String> list();

    public String getStorage();

    public Path innerPath();

    public Path subPath(String name);
    
    public String getFieldName();
}
