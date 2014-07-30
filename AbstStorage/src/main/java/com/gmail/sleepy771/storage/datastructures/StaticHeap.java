package com.gmail.sleepy771.storage.datastructures;

import java.util.Comparator;

import com.gmail.sleepy771.storage.exceptions.HeapException;

public class StaticHeap<T extends ObtainableElement<T>> implements Heap<T> {
	
	private final T[] elements;
	private Comparator<T> comparator;
	private int size;
	private final int capacity;
	
	@SuppressWarnings("unchecked")
	public StaticHeap(Comparator<T> comparator, int capacity) {
		elements = (T[]) new Object[capacity];
		this.capacity = capacity;
		this.comparator = comparator;
		size = 0;
	}
	
	@Override
	public void push(T element) throws HeapException {
		if (size == capacity)
			throw new HeapException("Heap owerflowed");
		elements[size] = element;
		size++;
		upHeapBuble();
	}

	private void upHeapBuble() {
		int index = size();
		while (index > 1) {
			int parent = index / 2;
			if (compareOrNull(elements[index], elements[parent]) <= 0) {
				break;
			}
			swap(index, parent);
			index = parent;
		}
	}

	@Override
	public T pull() throws HeapException {
		if (size == 0 || !elements[0].canObtain())
			throw new HeapException("Empty heap exception");
		try {
			return elements[0];
		} finally {
			size--;
			elements[0] = null;
			downHeapBuble(0);
		}
	}

	private void downHeapBuble(int index) {
		while(true) {
			int child = index << 1;
			if (child > size) {
				break;
			}
			if (child + 1 <= size) {
				child = findMax(child);
			}
			
			if (compareOrNull(elements[index], elements[child + 1]) >= 0)
				break;
			swap(index, child);
			index = child;
		}
	}

	@Override
	public T pick() throws HeapException {
		if (size == 0)
			throw new HeapException("Empty heap exception");
		return elements[0];
	}

	@Override
	public boolean remove(T element) {
		int idx = findIndex(element);
		if (idx > -1) {
			elements[idx] = null;
			downHeapBuble(idx);
		}
		return false;
	}
	
	private int findIndex(T element) {
		return -1;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public int capacity() {
		return capacity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void forceHeapRebuild() {
		T[] tmpArray = (T[]) new Object[size];
		System.arraycopy(this.elements, 0, tmpArray, 0, size);
		clearHeap();
		for (int k=0; k<tmpArray.length; k++) {
			elements[size] = tmpArray[k];
			size++;
			upHeapBuble();
		}
	}
	
	private void clearHeap() {
		for (int idx=0; idx<size; idx++)
			elements[idx] = null;
		size = 0;
	}

	@Override
	public void setComparator(Comparator<T> comp) {
		comparator = comp;
	}

	@Override
	public Comparator<T> getComparator() {
		return comparator;
	}

	@Override
	public boolean isResizable() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	private int compareOrNull(T o1, T o2) {
		if (o1 == null || !o1.canObtain()) {
			return -1;
		} else if (o2 == null || !o2.canObtain()) {
			return 1;
		} else {
			if (comparator == null) {
				return o1.compareTo(o2);
			} else {
				return comparator.compare(o1, o2);
			}
		}
	}
	
	private void swap(int idx1, int idx2) {
		T tmp = elements[idx1];
		elements[idx1] = elements[idx2];
		elements[idx2] = tmp;
	}
	
	private int findMax(int idx) {
		if (compareOrNull(elements[idx], elements[idx + 1]) > 0) {
			return idx << 1;
		} else {
			return idx << 1 + 1; 
		}
	}

	@Override
	public boolean canObtainElement() {
		return elements[0].canObtain();
	}

}
