package com.gmail.sleepy771.storage_interface.utils;

public class Interval<T extends Comparable<T>> {
    private final T initialPoint, finalPoint;
    
    private Interval(T initialPoint, T finalPoint) {
	this.initialPoint = initialPoint;
	this.finalPoint = finalPoint;
    }
    
    public boolean contains(T point) {
	return initialPoint.compareTo(point) <= 0 && finalPoint.compareTo(point) >= 0;
    }
    
    public static <T extends Comparable<T>> Interval<T> createInterval(T initialPoint, T finalPoint) {
	if(initialPoint == null || finalPoint == null)
	    throw new NullPointerException();
	return new Interval<T>(initialPoint, finalPoint);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Interval<T> createBottomBoundedInterval(T initialPoint) {
	if(initialPoint == null)
	    throw new NullPointerException();
	Comparable<T> finalPoint = new Comparable<T>(){

	    @Override
	    public int compareTo(T o) {
		return 1;
	    }
	    
	};
	return new Interval<T>(initialPoint, (T) finalPoint);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Interval<T> createUpperBoundedInterval(T finalPoint) {
	if(finalPoint == null)
	    throw new NullPointerException();
	Comparable<T> initialPoint = new Comparable<T>(){

	    @Override
	    public int compareTo(T o) {
		return -1;
	    }
	    
	};
	return new Interval<T>((T) initialPoint, finalPoint);
    }
}
