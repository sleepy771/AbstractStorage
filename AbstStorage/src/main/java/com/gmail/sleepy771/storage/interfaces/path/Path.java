package com.gmail.sleepy771.storage.interfaces.path;

import java.util.List;

public interface Path extends Iterable<String> {

    public List<String> asList();

    public void dispose();

    public String get(int idx);

    public String getNode();
    
    public String getObjectName();
    
    public String element(); // Toto nebude jednoduche

    public Path getSubpath();

}
