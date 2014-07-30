package com.gmail.sleepy771.storage.base;

import java.util.concurrent.Callable;

public interface Task extends Comparable<Task> {
	/**
	 * This methods returns priority of this Task instance. The higher the priority of execution is, the higher the number should be.
	 * The value is dependent on task implementation.
	 * @return task priority as <b>int</b>.
	 */
	public int taskPriority();
	
	public Executable taskExecutable();
	
	public long taskID();
	
	public boolean isExecutable();
}
