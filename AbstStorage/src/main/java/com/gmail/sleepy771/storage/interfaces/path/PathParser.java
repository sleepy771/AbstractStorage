package com.gmail.sleepy771.storage.interfaces.path;


public interface PathParser {
    public PathParser parsePath(String path);
    
    public boolean isGlobal(String path);
    
    public boolean isLocal(String path);
    
    public GlobalPath asGlobal();
    
    public Path asLocal();
    
    public Path asPath();
    
    public String asString(Path p);
    
}
