package com_gmail_sleepy771.astorage.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com_gmail_sleepy771.astorage.AbstractStorage;
import com_gmail_sleepy771.astorage.Storable;
import com_gmail_sleepy771.astorage.exceptions.SerializationException;
import com_gmail_sleepy771.astorage.utilities.ObjectData;

public class StorageWriter extends Handler {

	private static final Logger LOGGER = Logger.getLogger(StorageWriter.class
			.getName());
	static{
		LOGGER.setParent(Logger.getGlobal());
		LOGGER.setUseParentHandlers(true);
	}

	private LinkedBlockingQueue<ObjectData> to;
	private Vector<ObjectData> unstoredObjects;

	private static class PoisonObject extends ObjectData {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5886769685377764818L;

		public PoisonObject() {
			super(new Storable() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -7170913505930083420L;

				@Override
				public Map<String, Object> serialize()
						throws SerializationException {
					return null;
				}

			});
		}
	}

	private AbstractStorage storage = null;

	@Override
	public void run() {
		boolean helthy = true;
		ObjectData od = null;
		try {
			while (helthy) {
				od = to.take();
				unprocessed();
				if (od.getClass().equals(PoisonObject.class)) {
					LOGGER.warning("StorageWriter thread poisoned, object will live no longer");
					helthy = false;
					if (super.getRunning() == 1 && to.size() != 0) {
						LOGGER.info("Ustored waiting data send to unstored queue");
						unstoredObjects.addAll(to);
					}
					continue;
				}
				try {
					storage.store(od);
				} catch (IOException e) {
					unstoredObjects.add(od);
					LOGGER.severe("Storage cann't store object "
							+ od.getSerialNumber()
							+ ". Stopping storage handler threads!!!");
					LOGGER.throwing(storage.getClass().getName(),
							"store(ObjectData od)", e);
					stopAllThreads();
					e.printStackTrace();
				}
				od = null;
				if (to.isEmpty())
					processed();
			}
		} catch (InterruptedException ie) {
			LOGGER.warning("Storage Writer thread interrupted!!!");
			if (od != null) {
				to.offer(od);
				LOGGER.info("Data " + od.getSerialNumber()
						+ " send back to queue");
				od = null;
			}
			ie.printStackTrace();
		}
	}
	
	public void offerData(ObjectData data){
		this.to.offer(data);
	}
	
	public void offerCollection(Collection<ObjectData> dataCollection){
		this.to.addAll(dataCollection);
	}
	
	public LinkedBlockingQueue<ObjectData> getInputQueue() {
		return this.to;
	}

	public void setInputQueue(LinkedBlockingQueue<ObjectData> input) throws NullPointerException {
		if(input == null)
			throw new NullPointerException();
		this.to = input;
	}

	public void setStorage(AbstractStorage storage) throws NullPointerException {
		if(storage == null)
			throw new NullPointerException();
		this.storage = storage;
	}

	@Override
	protected void poisonThread() {
		PoisonObject poison = new PoisonObject();
		to.offer(poison);
	}

}
