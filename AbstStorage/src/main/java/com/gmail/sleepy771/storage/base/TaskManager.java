package com.gmail.sleepy771.storage.base;

import com.gmail.sleepy771.storage.datastructures.Heap;

public interface TaskManager extends Manager {
	
	public void updateHeap();
	
	public void registrateTask(Task t, ResponseListener l);
	
	public void removeTask(Task t);
}
