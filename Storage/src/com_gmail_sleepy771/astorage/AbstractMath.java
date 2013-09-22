package com_gmail_sleepy771.astorage;

public class AbstractMath {
	public static <T extends Comparable<T>> T min(T a, T b){
		return a.compareTo(b)<0?a:b;
	}
	
	public static <T extends Comparable<T>> T min(T...a){
		T min = a[0];
		for(int i=1; i<a.length; i++)
			min = min(min, a[i]);
		return min;
	}
	
	public static <T extends Comparable<T>> T max(T a, T b){
		return a.compareTo(b)<0?b:a;
	}
	
	public static <T extends Comparable<T>> T max(T...a){
		T max = a[0];
		for(int i=1; i<a.length; i++)
			max = max(max, a[i]);
		return max;
	}
}
