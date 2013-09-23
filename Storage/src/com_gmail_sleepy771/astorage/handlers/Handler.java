package com_gmail_sleepy771.astorage.handlers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public abstract class Handler implements Runnable{
	
	private static Logger LOGGER = Logger.getLogger(Handler.class.getName());
	
	private ReentrantLock handlerLock = new ReentrantLock();
	protected ReentrantLock queuesLock = new ReentrantLock();
	protected Condition isEmpty = queuesLock.newCondition();
	
	private boolean isProcessed = true;
	
	protected int threadsRunning = 0;
	
	
	public synchronized void newThread() {
		handlerLock.lock();
		try{
			//Maybe
			unprocessed();
			new Thread(this).start();
			this.threadsRunning++;
		}
		finally{
			handlerLock.unlock();
		}
	}
	
	public void stopThread(){
		handlerLock.lock();
		try {
			if(threadsRunning>0){
				poisonThread();
				this.threadsRunning--;
			}
		} finally {
			handlerLock.unlock();
		}
	}
	
	public int getRunning(){
		int running = 0;
		handlerLock.lock();
		try{
			running = this.threadsRunning;
		} finally {
			handlerLock.unlock();
		}
		return running;
	}
	
	protected abstract void poisonThread();
	
	public boolean isProcessed(){
		queuesLock.lock();
		boolean processed;
		try{
			processed = this.isProcessed;
		}
		finally {
			queuesLock.unlock();
		}
		return processed;
	}
	
	public void processed(){
		queuesLock.lock();
		try{
			isProcessed = true;
			isEmpty.signal();
		}
		finally{
			queuesLock.unlock();
		}
	}
	
	public void unprocessed(){
		queuesLock.lock();
		try{
			isProcessed = false;
		} finally{
			queuesLock.unlock();
		}
	}
	
	public void stopAllThreads(){
		handlerLock.lock();
		try{
			int running = getRunning();
			for(int k = 0; k < running; k++)
				stopThread();
		}
		finally{
			handlerLock.unlock();
		}
	}
	
	public void close(){
		flush();
		stopAllThreads();
	}
	
	public void flush(){
		queuesLock.lock();
		//TODO is processed if not and no thread is running then create one and 
		try{
			if(!isProcessed()){
				if(getRunning()==0)
					newThread();
				isEmpty.await();
			}
				
		}
		catch(InterruptedException ie){
			LOGGER.warning("Waiting for processing data interrupted");
			ie.printStackTrace();
		} finally {
			queuesLock.unlock();
		}
	}
}
