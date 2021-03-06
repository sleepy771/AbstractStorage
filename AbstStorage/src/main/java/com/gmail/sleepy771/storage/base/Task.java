package com.gmail.sleepy771.storage.base;

import java.util.concurrent.Callable;

import com.gmail.sleepy771.storage.datastructures.ObtainableElement;

public interface Task extends ObtainableElement<Task> {
	/**
	 * This methods returns priority of this Task instance. The higher the priority of execution is, the higher the number should be.
	 * The value is dependent on task implementation.
	 * @return task priority as <b>int</b>.
	 */
	public int taskPriority();
	
	public Executable taskExecutable();
	
	public long taskID();
	
	public boolean isExecutable();
	
	public Manager sendTo();
}
