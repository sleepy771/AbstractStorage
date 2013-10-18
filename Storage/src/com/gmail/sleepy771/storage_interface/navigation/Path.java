package com.gmail.sleepy771.storage_interface.navigation;
import java.util.List;


//Ustredny element
public interface Path<T extends Comparable<T>> extends Iterable<DataKey<T>>, Navigable<T>{
	public List<DataKey<T>> asList();
	public DataKey<String> getStorageKey();
	public DataKey<String> pollStorageKey();
	public DataKey<String> setStorageKey(DataKey<String> storageKey);
	public void add(DataKey<T> element);
	public void addPath(Path<T> path);
	public void addFirst(DataKey<T> element);
	public void addLast(DataKey<T> element);
	public void setElement(int index, DataKey<T> element) throws IndexOutOfBoundsException;
	public void removeAbove(int index);
	public void removeAbove(DataKey<T> element);
}
