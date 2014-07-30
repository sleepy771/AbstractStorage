package com.gmail.sleepy771.storage.datastructures;

import java.util.ArrayList;
import java.util.Comparator;

import com.gmail.sleepy771.storage.exceptions.HeapException;

public class DynamicalyAllocatedHeap<T extends Comparable<T>> implements Heap<T> {
	
	private final ArrayList<T> innerList;
	private Comparator<T> comparator;
	
	public DynamicalyAllocatedHeap(Comparator<T> comp, int initSize) {
		innerList = new ArrayList<T>(initSize);
		comparator = comp;
	}
	
	@Override
	public void push(T element) {
		innerList.add(element);
		upHeapBuble();
	}

	private void upHeapBuble() {
		int index = size() - 1;
		while (index > 0) {
			int parent = (index - 1) >> 1;
			if (compareOrNull(innerList.get(parent), innerList.get(index)) >= 0)
				break;
			swap(parent, index);
			index = parent;
		}
	}

	@Override
	public T pull() throws HeapException {
		if (innerList.isEmpty())
			throw new HeapException("Empty heap exception");
		try {
			return innerList.get(0);
		} finally {
			innerList.set(0, null);
			downHeapBuble(0);
		}
	}

	private void downHeapBuble(int index) {
		int child = index << 1;
		while(child + 2 < size()) {
			child = findMax(child+1, child+2);
			if (compareOrNull(innerList.get(index), innerList.get(child)) >= 0)
				break;
			swap(index, child);
			index = child;
			child <<= 1;
		}
	}

	@Override
	public T pick() throws HeapException {
		if(innerList.isEmpty())
			throw new HeapException("Empty heap exception");
		return innerList.get(0);
	}

	@Override
	public boolean remove(T element) {
		int idx = innerList.indexOf(element);
		if (idx > -1) {
			innerList.set(idx, null);
			downHeapBuble(idx);
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return innerList.size();
	}

	@Override
	public int capacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void forceHeapRebuild() {
		ArrayList<T> tmpList = new ArrayList<T>(this.innerList);
		this.innerList.clear();
		for(T element : tmpList) {
			push(element);
		}
	}

	@Override
	public void setComparator(Comparator<T> comp) {
		this.comparator = comp;
	}

	@Override
	public Comparator<T> getComparator() {
		return comparator;
	}

	@Override
	public boolean isEmpty() {
		return innerList.isEmpty();
	}

	@Override
	public boolean isResizable() {
		return true;
	}
	
	private int compareOrNull(T o1, T o2) {
		if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {
			if (comparator != null) {
				return comparator.compare(o1, o2);
			} else {
				return o1.compareTo(o2);
			}
		}
	}
	
	private void swap(int m, int n) {
		T element = innerList.get(m);
		innerList.set(m, innerList.get(n));
		innerList.set(n, element);
	}
	
	private int findMax(int k, int l) {
		return compareOrNull(innerList.get(k), innerList.get(l)) < 0?l:k;
	}

}
