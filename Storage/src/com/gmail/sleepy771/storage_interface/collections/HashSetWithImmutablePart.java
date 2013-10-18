package com.gmail.sleepy771.storage_interface.collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HashSetWithImmutablePart<E> implements Set<E> {
    private Set<E> immutableSet;
    private Set<E> mutableSet;

    public HashSetWithImmutablePart() {
	this(null);
    }

    public HashSetWithImmutablePart(Collection<E> immutablesCollection) {
	if (immutablesCollection != null && !immutablesCollection.isEmpty()) {
	    this.immutableSet = new HashSet<E>();
	    for (E element : immutablesCollection) {
		if (element != null)
		    this.immutableSet.add(element);
	    }
	}
	this.mutableSet = new HashSet<E>();
    }

    @Override
    public synchronized int size() {
	int size = 0;
	if (!immutableIsNull())
	    size += immutableSet.size();
	size += mutableSet.size();
	return size;
    }

    @Override
    public synchronized boolean isEmpty() {
	return immutableIsNull() && mutableSet.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
	if (!immutableIsNull() && immutableSet.contains(o))
	    return true;
	return mutableSet.contains(o);
    }

    @Override
    public synchronized Iterator<E> iterator() {
	HashSet<E> iterableSet = new HashSet<E>(size());
	if (immutableIsNull())
	    iterableSet.addAll(immutableSet);
	iterableSet.addAll(mutableSet);
	return iterableSet.iterator();
	/*
	 * return new Iterator<E>(){
	 * 
	 * boolean isMutable = immutableIsNull(); private Iterator<E> iterator =
	 * isMutable?mutableSet.iterator():immutableSet.iterator();
	 * 
	 * @Override public boolean hasNext() { if(!iterator.hasNext() &&
	 * !isMutable){ return mutableSet.isEmpty(); } return
	 * iterator.hasNext(); }
	 * 
	 * @Override public E next() { if(!iterator.hasNext() && !isMutable) {
	 * iterator = mutableSet.iterator(); } return iterator.next(); }
	 * 
	 * @Override public void remove() throws UnsupportedOperationException{
	 * if(!isMutable) throw new UnsupportedOperationException();
	 * iterator.remove(); }
	 * 
	 * };
	 */
    }

    @Override
    public synchronized Object[] toArray() {
	ArrayList<E> tmpList = new ArrayList<E>(
		immutableIsNull() ? mutableSet.size() : immutableSet.size()
			+ mutableSet.size());
	tmpList.addAll(immutableSet);
	tmpList.addAll(mutableSet);
	return tmpList.toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] a) {
	ArrayList<E> tmpList = new ArrayList<E>(
		immutableIsNull() ? mutableSet.size() : immutableSet.size()
			+ mutableSet.size());
	tmpList.addAll(immutableSet);
	tmpList.addAll(mutableSet);
	return tmpList.toArray(a);
    }

    @Override
    public synchronized boolean add(E e) {
	if (!immutableIsNull() && immutableSet.contains(e)) {
	    return false;
	}
	if (e == null)
	    return false;
	return mutableSet.add(e);
    }

    @Override
    public synchronized boolean remove(Object o) {
	return mutableSet.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
	if (c == null || c.isEmpty()) {
	    return true;
	}
	boolean contains = true;
	for (Object o : c) {
	    contains &= contains(o);
	}
	return contains;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
	if (c == null || c.isEmpty()) {
	    return false;
	}
	boolean added = true;
	for (E element : c) {
	    added &= add(element);
	}
	return added;
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
	return mutableSet.retainAll(c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
	return mutableSet.removeAll(c);
    }

    @Override
    public synchronized void clear() {
	mutableSet.clear();
    }

    private boolean immutableIsNull() {
	return immutableSet == null;
    }

}
