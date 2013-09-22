package com_gmail_sleepy771.astorage;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;

public class DeserializationHandler extends Handler {
	public static final TreeMap<String, Deserializer> DESERIALIZERS = new TreeMap<String, Deserializer>();

	private static final Logger LOGGER = Logger
			.getLogger(DeserializationHandler.class.getName());

	@SuppressWarnings("serial")
	private static class PoisonObject extends ObjectData {

		public PoisonObject() {
			super(new Storable() {
				@Override
				public Map<String, Object> serialize()
						throws SerializationException {
					return null;
				}

			});
		}

	}

	private final ReentrantLock deserializerLock;
	private final Condition deserializationFinished;

	private TreeMap<Long, ObjectData> helperDataMap = null;
	private TreeMap<Long, Object> helperObjectMap = null;

	private Vector<Object> outputObjects = null;
	private LinkedBlockingQueue<ObjectData> inputData = null;
	private Vector<ObjectData> undeserializedData = null;
	private Vector<ObjectData> damagedData = null;

	public DeserializationHandler() {
		inputData = new LinkedBlockingQueue<ObjectData>();
		deserializerLock = new ReentrantLock();
		deserializationFinished = deserializerLock.newCondition();
	}

	public Vector<Object> deserializeData(Collection<ObjectData> data,
			Collection<ObjectData> helperData) throws InterruptedException {
		deserializerLock.lock();
		unprocessed();
		Vector<Object> output = null;
		try {
			outputObjects = new Vector<Object>(data.size());
			helperDataMap = new TreeMap<Long, ObjectData>();
			for (ObjectData hData : helperData) {
				helperDataMap.put(hData.getSerialNumber(), hData);
			}
			inputData.addAll(data);
			deserializationFinished.await();
			output = this.outputObjects;
			this.outputObjects = null;
			this.helperDataMap = null;
		} finally {
			deserializerLock.unlock();
		}
		return output;
	}
	
	public Object deserializeData(ObjectData data, Collection<ObjectData> helperData) throws InterruptedException {
		deserializerLock.lock();
		unprocessed();
		Object output = null;
		try{
			outputObjects = new Vector<Object>(1);
			for (ObjectData hData : helperData) {
				helperDataMap.put(hData.getSerialNumber(), hData);
			}
			inputData.offer(data);
			deserializationFinished.await();
			output = this.outputObjects.get(0);
			this.outputObjects = null;
			this.helperDataMap = null;
		} finally {
			deserializerLock.unlock();
		}
		return output;
	}

	/*
	 * public Vector<Object> deserializeData(ObjectData data) throws
	 * InterruptedException { deserializerLock.lock(); Vector<Object> output =
	 * null; try { outputObjects = new Vector<Object>(1); inputData.add(data);
	 * deserializationFinished.await(); output = this.outputObjects;
	 * this.outputObjects = null; } finally { deserializerLock.unlock(); }
	 * return output; }
	 */

	@Override
	public void run() {
		Object obj = null;
		ObjectData od = null;
		boolean helthy = true;
		try{
			while (helthy) {
				od = inputData.take();
					if (od.getClass().equals(
							DeserializationHandler.PoisonObject.class)) {
						LOGGER.warning("StorageWriter thread poisoned, object will live no longer");
						if (super.getRunning() == 1 && inputData.size() != 0) {
							LOGGER.info("Usdeserialized data waiting in input queue, data send to undeserialized vector");
							undeserializedData.addAll(inputData);
						}
						helthy = false;
						continue;
					}

					Vector<Long> references = new Vector<Long>(od
							.getReferenceSerials().values());

					if (!references.isEmpty()) {
						boolean offered = false;
						for (Long reference : references) {
							if ((offered |= !helperObjectMap
									.containsKey(reference))) {
								inputData.offer(helperDataMap.get(reference));
							}
						}
						if (offered) {
							inputData.offer(od);
							continue;
						}
					}

					String classType = od.getClassName();

					Deserializer des = null;

					if ((des = DESERIALIZERS.get(classType)) != null) {
						des = Deserializer.class.cast(des.clone()); // overit ci
																	// je nutne
																	// object
																	// klonovat
						LOGGER.finest("Deserializer find!!");
					} else {
						damagedData.add(od);
						LOGGER.warning("Deserializer object for "
								+ classType
								+ " not found! Data stored in queue. Install or create Deserializer object, in order to deserialize that data");
						System.err
								.println("Install module for deserializing object type: "
										+ classType);
						continue;
					}
					try {
						TreeMap<String, Object> objectMapping = new TreeMap<String, Object>();
						objectMapping.putAll(od);
						for (Map.Entry<String, Long> reference : od
								.getReferenceSerials().entrySet()) {
							objectMapping.put(reference.getKey(),
									helperObjectMap.get(reference.getValue()));
						}
						obj = des.deserialize(objectMapping);
						LOGGER.finest("Data sn: " + od.getSerialNumber()
								+ " sucessfully deserialized");
						if (helperDataMap.containsKey(od.getSerialNumber())) {
							helperObjectMap.put(od.getSerialNumber(), obj);
						} else {
							outputObjects.add(obj);
						}
						od = null;

						obj = null;
					} catch (DeserializationException de) {
						damagedData.add(od);
						od = null;
						obj = null;
						LOGGER.throwing(des.getClass().getName(),
								"deserialize(ObjectData od)", de);
						LOGGER.warning("Data file send to damaged data queue!");
						de.printStackTrace();
					}

					if (inputData.isEmpty()) {
						processed();
						deserializationFinished.signal();
					}
			}
		} catch (InterruptedException ie) {
			LOGGER.warning("Deserialization Thread interrupted!");
			LOGGER.throwing(inputData.getClass().getName(), "take()", ie);
			if (od != null) {
				inputData.offer(od);
				od = null;
				// TODO log
			}
			if (obj != null) {
				outputObjects.add(obj);
				obj = null;
				// TODO log
			}
			ie.printStackTrace();
		}
	}

	public boolean hasUndeserializedData() {
		deserializerLock.lock();
		boolean hasUndeser;
		try {
			hasUndeser = !(this.undeserializedData == null || this.undeserializedData
					.isEmpty());
		} finally {
			deserializerLock.unlock();
		}
		return hasUndeser;
	}

	public boolean hasDamagedData() {
		deserializerLock.lock();
		boolean hasDam;
		try {
			hasDam = !(this.damagedData == null || this.damagedData.isEmpty());
		} finally {
			deserializerLock.unlock();
		}
		return hasDam;
	}

	public Vector<ObjectData> getUndeserializedData()
			throws InterruptedException {
		deserializerLock.lock();
		Vector<ObjectData> vec;
		try {
			deserializationFinished.await();
			vec = this.undeserializedData;
			this.undeserializedData = null;
		} finally {
			deserializerLock.unlock();
		}
		return vec;
	}

	public Vector<ObjectData> getDamagedData() throws InterruptedException {
		deserializerLock.lock();
		Vector<ObjectData> vec;
		try {
			deserializationFinished.await();
			vec = this.damagedData;
			this.damagedData = null;
		} finally {
			deserializerLock.unlock();
		}
		return vec;
	}

	@Override
	protected void poisonThread() {
		PoisonObject poison = new PoisonObject();
		inputData.offer(poison);
	}
}
