package com.gmail.sleepy771.storage.datastructures;

import java.util.Comparator;

import com.gmail.sleepy771.storage.exceptions.HeapException;

public interface Heap<T extends ObtainableElement<T>> {
	public void push(T element) throws HeapException;
	
	public T pull() throws HeapException;
	
	public T pick() throws HeapException;
	
	public boolean remove(T element);
	
	public int size();
	
	public int capacity();
	
	public void forceHeapRebuild();
	
	public void setComparator(Comparator<T> comp);
	
	public Comparator<T> getComparator();
	
	public boolean isResizable();
	
	public boolean isEmpty();
	
	public boolean canObtainElement();
}
