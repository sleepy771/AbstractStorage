package com_gmail_sleepy771.astorage.handlers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import com_gmail_sleepy771.astorage.AbstractStorage;
import com_gmail_sleepy771.astorage.utilities.ObjectData;
import com_gmail_sleepy771.astorage.utilities.Query;
import com_gmail_sleepy771.astorage.utilities.UDID;

public class StorageReader extends Handler{
	
	/*
	 * Multi-threaded writing to storage not used yet.
	 */
	
	private static final Logger LOGGER = Logger.getLogger(StorageReader.class.getName());
	
	private final ReentrantLock readerLock = new ReentrantLock();
	private final Condition finishedReading = readerLock.newCondition();
	
	private LinkedBlockingDeque<Query> queryQueue = new LinkedBlockingDeque<Query>();
	
	private AbstractStorage storage = null;
	
	private LinkedList<ObjectData> outputData = null;
	private TreeSet<UDID> requiredSerials = null, loadedSerials = null;
	
	public synchronized Vector<ObjectData> getData(Query q) throws InterruptedException{
		readerLock.lock();
		try{
			//outputMap = new TreeMap<Long, Vector<ObjectData>>();
			queryQueue.offerFirst(q);
			finishedReading.await();
		}
		finally{
			readerLock.unlock();
		}
		
		return null;
	}
	
	@Override
	public void run() {
		Thread currentThread = Thread.currentThread();
		Query query = null;
		try{
			while(!currentThread.isInterrupted()){
				query = queryQueue.takeFirst();
				try{
					Set<ObjectData> dataSet = storage.load(query);
					if(outputData == null){
						outputData = new LinkedList<ObjectData>(dataSet); // uvidime ci synchronizovat treba
						// for notifying last querying data objects
						outputData.addFirst(outputData.getLast());
					}
					else{
						outputData.addAll(dataSet);
					}
					TreeSet<UDID> serials = new TreeSet<UDID>();
					for(ObjectData data : dataSet){
						serials.addAll(data.getReferenceSerials().values());
						loadedSerials.add(data.getSerialNumber());
					}
					requiredSerials.addAll(serials);
					if(!serials.isEmpty()){
						Query q = new Query();
						q.addEqualityCriteria(Query.REFERENCE_SERIALS, serials);
						queryQueue.offerFirst(q);
					}
					
				}
				catch(IOException ioe){
					LOGGER.severe("IOError occured on storage side, Storage Reader is goiong to be closed!");
					LOGGER.throwing(storage.getClass().getName(), "load(Query query)", ioe);
					ioe.printStackTrace();
					finishedReading.signal();
					queryQueue.offer(query);
					LOGGER.info("Query put back to queue");
					stopAllThreads();
				}
				query = null;
			}
		}
		catch(InterruptedException ie){
			LOGGER.warning("Storage Reader thread inerrupted!");
			LOGGER.throwing(queryQueue.getClass().getName(), "take()", ie);
			if(query!=null){
				queryQueue.offer(query);
				LOGGER.info("Query put back to queue");
			}
			ie.printStackTrace();
		}
	}

	@Override
	protected void poisonThread() {
		// TODO Auto-generated method stub
		
	}
}
