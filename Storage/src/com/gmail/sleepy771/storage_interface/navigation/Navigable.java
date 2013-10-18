package com.gmail.sleepy771.storage_interface.navigation;
import java.util.List;



public interface Navigable<T extends Comparable<T>> {
    public Path<T> getCurrentLocation();
    public Path<T> goUp();
    public Path<T> go(DataKey<T> key);
    public List<DataKey<T>> listPaths();
    public Path<T> go(Path<DataKey<T>> key);
    public Path<T> goHome(Path<DataKey<T>> key);
    public boolean isLeaf();
}
