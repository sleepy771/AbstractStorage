package com.gmail.sleepy771.storage_interface.collections;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExtendedByteBuffer implements Collection<Byte>, List<Byte>, RandomAccess, Comparable<ExtendedByteBuffer> {

    private class ListItr implements ListIterator<Byte> {
	private int cursor = 0;

	public ListItr() {
	    this(0);
	}

	public ListItr(int initialPosition) {
	    try {
		editLock.lock();
		ExtendedByteBuffer.this.verifyIndexExistence(initialPosition);
		cursor = initialPosition;
	    } finally {
		editLock.unlock();
	    }
	}

	@Override
	public boolean hasNext() {
	    return cursor <= size();
	}

	@Override
	public Byte next() {
	    if (cursor >= size())
		throw new NoSuchElementException();
	    return array[cursor++];
	}

	@Override
	public void remove() {
	    ExtendedByteBuffer.this.remove(cursor);
	}

	@Override
	public boolean hasPrevious() {
	    return cursor != 0;
	}

	@Override
	public Byte previous() {
	    if (cursor < 0)
		throw new NoSuchElementException();
	    return array[cursor--];
	}

	@Override
	public int nextIndex() {
	    return cursor;
	}

	@Override
	public int previousIndex() {
	    return cursor - 1;
	}

	@Override
	public void set(Byte e) {
	    if (cursor < 0 || cursor >= size())
		throw new IndexOutOfBoundsException();
	    ExtendedByteBuffer.this.set(cursor, e);
	}

	@Override
	public void add(Byte e) {
	    if (cursor < 0 || cursor > size())
		throw new IndexOutOfBoundsException();
	    ExtendedByteBuffer.this.add(cursor, e);
	}

    }
    
    private ByteBuffer bb;

    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    private static final int MAX_SIZE = Integer.MAX_VALUE;

    private int size;
    private byte[] array;
    private final Lock editLock;

    public ExtendedByteBuffer(int initCapacity) {
	if (initCapacity <= 0)
	    throw new IllegalArgumentException("Capacity should be > 0");
	array = new byte[initCapacity];
	size = 0;
	editLock = new ReentrantLock();
    }

    public ExtendedByteBuffer() {
	this(DEFAULT_INITIAL_CAPACITY);
    }
    
    public ExtendedByteBuffer(byte[] buffer) {
	array = new byte[buffer.length];
	System.arraycopy(buffer, 0, array, 0, buffer.length);
	size = buffer.length;
	editLock = new ReentrantLock();
    }
    
    public ExtendedByteBuffer(byte[]...buffers) {
	size = 0;
	for (int k = 0; k < buffers.length; k++)
	    size += buffers[k].length;
	array = new byte[size];
	int lindex = 0;
	for(int k = 0; k < buffers.length; k++) {
	    System.arraycopy(buffers[k], 0, array, lindex, buffers[k].length);
	    lindex += buffers[k].length;
	}
	editLock = new ReentrantLock();
    }
    
    public ExtendedByteBuffer(ExtendedByteBuffer bbuf) {
	try {
	    bbuf.editLock.lock();
	    array = new byte[bbuf.size];
	    System.arraycopy(bbuf.array, 0, array, 0, bbuf.size);
	    size = bbuf.size;
	    editLock = new ReentrantLock();
	} finally {
	    bbuf.editLock.unlock();
	}
    }

    public ByteArrayInputStream getByteArrayInputStram() {
	throw new UnsupportedOperationException();
    }

    public void setByteArrayOutputStream(ByteArrayOutputStream baos) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
	if (c != null && !c.isEmpty()) {
	    boolean realocated = false;
	    try {
		editLock.lock();
		canAddOnIndex(index);
		byte[] input = getNonNullByteElements(c);
		if (input.length > 0) {
		    realocated = realocateSizeWithOffsetOnIndex(input.length,
			    index);
		    if (realocated)
			System.arraycopy(input, 0, array, index, input.length);
		    else
			throw new OutOfMemoryError();
		}
	    } finally {
		editLock.unlock();
	    }
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public Byte get(int index) {
	Byte out = null;
	try {
	    editLock.lock();
	    if (index < 0 || index >= size)
		throw new IndexOutOfBoundsException("Index: " + index
			+ " is not in bounds of array of size: " + size);
	    out = array[index];
	} finally {
	    editLock.unlock();
	}
	return out;
    }
    
    public byte getByte(int index) {
	byte b = 0;
	try {
	    editLock.lock();
	    verifyIndexExistence(index);
	    b = array[index];
	} finally {
	    editLock.unlock();
	}
	return b;
    }

    @Override
    public Byte set(int index, Byte element) {
	Byte onPosition = null;
	if(element == null)
	    throw new NullPointerException();
	try{
	    editLock.lock();
	    verifyIndexExistence(index);
	    onPosition = array[index];
	    array[index] = element;
	} finally {
	    editLock.unlock();
	}
	return onPosition;
    }
    
    public byte setByte(int index, byte element) {
	byte onPosition = 0;
	try{
	    editLock.lock();
	    verifyIndexExistence(index);
	    onPosition = array[index];
	    array[index] = element;
	} finally {
	    editLock.unlock();
	}
	return onPosition;
    }

    @Override
    public void add(int index, Byte element) {
	boolean realocated = false;
	if (element == null)
	    throw new IllegalArgumentException("Byte should not be null");
	try {
	    editLock.lock();
	    canAddOnIndex(index);
	    realocated = realocateSizeWithOffsetOnIndex(1, index);
	    if(realocated)
		array[index] = element;
	    else 
		throw new OutOfMemoryError();
	} finally {
	    editLock.unlock();
	}
    }

    @Override
    public Byte remove(int index) {
	Byte onIndex = null;
	try {
	    editLock.lock();
	    verifyIndexExistence(index);
	    onIndex = array[index];
	    realocateSizeWithOffsetOnIndex(-1, index);
	} finally {
	    editLock.unlock();
	}
	return onIndex;
    }

    @Override
    public int indexOf(Object o) {
	throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
	throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Byte> listIterator() {
	return new ListItr();
    }

    @Override
    public ListIterator<Byte> listIterator(int index) {
	return new ListItr(index);
    }

    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
	List<Byte> arrayList = null;;
	try{
	    editLock.lock();
	    verifyIndexExistence(fromIndex);
	    verifyIndexExistence(toIndex);
	    arrayList = new ArrayList<Byte>(toIndex - fromIndex+1);
	    for(int k = fromIndex; k<=toIndex; k++) {
		arrayList.add(array[k]);
	    }
	} finally {
	    editLock.unlock();
	}
	return arrayList;
    }

    @Override
    public int size() {
	int size = 0;
	try {
	    editLock.lock();
	    size = this.size;
	} finally {
	    editLock.unlock();
	}
	return size;
    }

    @Override
    public boolean isEmpty() {
	return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
	throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Byte> iterator() {
	return new Iterator<Byte>() {
	    ListItr itr = new ListItr();

	    @Override
	    public boolean hasNext() {
		return itr.hasNext();
	    }

	    @Override
	    public Byte next() {
		return itr.next();
	    }

	    @Override
	    public void remove() {
		itr.remove();
	    }
	};
    }

    @Override
    public Object[] toArray() {
	Object[] out = null;
	try {
	    editLock.lock();
	    out = new Object[size];
	    for(int i=0; i<size; i++) {
		out[i] = array[i];
	    }
	} finally {
	    editLock.unlock();
	}
	return out;
    }
    
    public byte[] getArrayCopy() {
	byte[] a = null;
	try {
	    editLock.lock();
	    a = new byte[size];
	    System.arraycopy(array, 0, a, 0, size);
	} finally {
	    editLock.unlock();
	}
	return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Byte e) {
	try {
	    add(size(), e);
	} catch(OutOfMemoryError mem ) {
	    mem.printStackTrace();
	    return false;
	}
	return true;
    }

    @Override
    public boolean remove(Object o) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Byte> c) {
	return addAll(size(), c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
	try {
	    editLock.lock();
	    array = new byte[DEFAULT_INITIAL_CAPACITY];
	    size = 0;
	} finally {
	    editLock.unlock();
	}
    }

    private boolean realocateSizeWithOffsetOnIndex(int size, int index) {
	int newSize = size + size();
	if (newSize <= MAX_SIZE) {
	    byte[] tmp = array;
	    if (newSize > array.length || (newSize<<1)<=array.length) {
		int arraySize = 1;
		do {
		    arraySize = arraySize << 1;
		} while (arraySize < newSize);
		if (arraySize >= MAX_SIZE)
		    arraySize = MAX_SIZE;
		array = new byte[arraySize];
	    }
	    System.arraycopy(tmp, 0, array, 0, index);
	    System.arraycopy(tmp, index + 1, array, index + size, tmp.length
		    - index);
	    return true;
	} else {
	    return false;
	}
    }

    private byte[] getNonNullByteElements(Collection<? extends Byte> c) {
	byte[] tmp = new byte[c.size()];
	int k = 0;
	for (Byte b : c) {
	    if (b != null) {
		tmp[k++] = b;
	    }
	}
	byte[] out = new byte[k];
	System.arraycopy(tmp, 0, out, 0, k);
	return out;
    }
    
    private void verifyIndexExistence(int index) {
	if(index < 0 || index >= size) {
	    throw new IndexOutOfBoundsException("Index: "+index+" out of bounds of array: [0, "+(size-1)+"]");
	}
    }
    
    private void canAddOnIndex(int index) {
	if(index < 0 || index > size) {
	    throw new IndexOutOfBoundsException("Index: "+index+" out of bounds of array: [0, "+size+"]");
	}
    }

    @Override
    public int compareTo(ExtendedByteBuffer o) {
	int comp = 0;
	try{
	    o.editLock.lock();
	    editLock.lock();
	    for(int k = 0; k < Math.min(o.size, size); k++){
		if(comp!=0)
		    break;
		comp = ((o.array[k]&0xff) - (this.array[k]&0xff));
	    }
	    if(comp == 0) {
		comp = o.size - this.size;
	    }
	} finally {
	    editLock.unlock();
	    o.editLock.unlock();
	}
	return comp;
    }
    
    public CharSequence toCharSequance() {
	CharSequence seq = null;
	try {
	    editLock.lock();
	    CharBuffer cb = CharBuffer.allocate(size);
	    for(int k=0; k<size; k++) {
		cb.append((char)array[k]);
	    }
	    seq = cb;
	} finally {
	    editLock.unlock();
	}
	return seq;
    }
    
    public String toString(Charset charset) {
	String str = null;
	try {
	    editLock.lock();
	    str = new String(array, charset);
	} finally {
	    editLock.unlock();
	}
	return str;
    }
    
    @Override
    public String toString() {
	return toString(null);
    }
}
