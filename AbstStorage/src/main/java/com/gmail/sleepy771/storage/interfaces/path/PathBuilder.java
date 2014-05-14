package com.gmail.sleepy771.storage.interfaces.path;

import java.util.List;

import com.gmail.sleepy771.storage.interfaces.Buildable;

public interface PathBuilder extends Buildable<Path> {
    public PathBuilder addPlace(String place);
    
    public PathBuilder addSubpath(Path subpath);
    
    public PathBuilder addSubpath(List<String> subpath);
    
    public PathBuilder addSubpath(String[] subpath);
    
    public PathBuilder addSubpath(int idx, Path path);
    
    public PathBuilder addSubpath(String before, Path path);
    
    public PathBuilder removeAbove(String place);
    
    public PathBuilder removeBelow(String place);
    
    public PathBuilder removeLast();
    
    public PathBuilder removeFirst();
    
    public PathBuilder trim(int iIdx, int fIdx);
    
    public PathBuilder remove(String place);
    
    public PathBuilder remove(Path matchingpath);
    
    public PathBuilder reomve(List<String> places);
    
    public String pathHash();
}
