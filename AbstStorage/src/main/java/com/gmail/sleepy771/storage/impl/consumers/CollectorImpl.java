package com.gmail.sleepy771.storage.impl.consumers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.print.attribute.UnmodifiableSetException;

import com.gmail.sleepy771.storage.core.datastructures.Canceled;
import com.gmail.sleepy771.storage.exceptions.ElementsNotFullyCollectedException;
import com.gmail.sleepy771.storage.interfaces.consumers.Collector;

public class CollectorImpl implements Collector {
    public static final Canceled CANCELED = new Canceled();
    
    private final Map<String, Future<Object>> futureSet;
    private final Map<String, Object> elements;
    private boolean changabe = true, complete = false;
    
    private final Runnable collectorTask = new Runnable() {
	
	@Override
	public void run() {
	    Object timerObj = new Object();
	    List<String> remFutures = new LinkedList<String>(); 
	    try{
		while(!CollectorImpl.this.complete) {
		    for(Entry<String, Future<Object>> entry : futureSet.entrySet()) {
			if(entry.getValue().isDone()) {
			    remFutures.add(entry.getKey());
			    try {
				elements.put(entry.getKey(), entry.getValue().get());
			    } catch (ExecutionException e) {
				// TODO Better handle this exception
				e.printStackTrace();
			    }
			}
			if(entry.getValue().isCancelled()) {
			    remFutures.add(entry.getKey());
			    elements.put(entry.getKey(), CollectorImpl.CANCELED);
			}
		    }
		    for(String remKeys : remFutures) {
			futureSet.remove(remKeys);
		    }
		    complete = futureSet.isEmpty();
		    if (!complete)
			timerObj.wait(100);
	    	}
	    } catch(InterruptedException ie) {
		ie.printStackTrace();
	    }
	}
	
    };
    
    private final Thread collectorThread = new Thread(collectorTask);
    
    public CollectorImpl() {
	futureSet = new HashMap<String, Future<Object>>();
	elements = new HashMap<String, Object>();
    }
    
    @Override
    public synchronized boolean isCollected() {
	return this.complete;
    }

    @Override
    public synchronized Map<String, Object> getObjects() throws ElementsNotFullyCollectedException {
	if (!this.complete) {
	    throw new ElementsNotFullyCollectedException();
	}
	try {
	    return new HashMap<String, Object>(this.elements);
	} finally {
	    resetCollector();
	}
    }

    @Override
    public synchronized void addFutureObject(String name, Future<Object> f) {
	if(!this.changabe) 
	    throw new UnmodifiableSetException();
	this.futureSet.put(name, f);
    }

    @Override
    public synchronized void addAllFutureObjects(Map<String, Future<Object>> c) {
	if(!this.changabe) 
	    throw new UnmodifiableSetException();
	this.futureSet.putAll(c);
    }

    @Override
    public synchronized void removeFutureObject(String name) {
	if(!this.changabe) 
	    throw new UnmodifiableSetException();
	this.futureSet.remove(name);
    }

    @Override
    public synchronized void removeAllFutureObjects(Collection<String> c) {
	if(!this.changabe) 
	    throw new UnmodifiableSetException();
	for (String key : c)
	    this.futureSet.remove(key);
    }

    @Override
    public synchronized void collect() {
	this.changabe = false;
	this.collectorThread.start();
    }
    
    public Map<String, Object> waitForResult() throws InterruptedException, ElementsNotFullyCollectedException {
	this.collectorThread.join();
	return getObjects();
    }
    
    public synchronized void resetCollector() {
	if(this.collectorThread.isAlive())
	    this.collectorThread.interrupt();
	this.complete = false;
	this.changabe = true;
	this.elements.clear();
	this.futureSet.clear();
    }

}
