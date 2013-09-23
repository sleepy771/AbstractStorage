package com_gmail_sleepy771.astorage.handlers;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com_gmail_sleepy771.astorage.Storable;
import com_gmail_sleepy771.astorage.exceptions.SerializationException;
import com_gmail_sleepy771.astorage.utilities.ObjectData;
import com_gmail_sleepy771.astorage.utilities.UDID;

public class SerializationHandler extends Handler {

	private static final Logger LOGGER = Logger
			.getLogger(SerializationHandler.class.getName());

	private LinkedBlockingQueue<Storable> storableObjectsQueue = null;
	private WeakHashMap<Storable, UDID> serialsMap = new WeakHashMap<Storable, UDID>();
	private LinkedBlockingQueue<ObjectData> toStorageQueue = null;
	private Vector<Object> unserializedObjects = null;

	private static class PoisonObject implements Storable {
		private static final long serialVersionUID = 5868390962887763523L;
		@Override
		public Map<String, Object> serialize() throws SerializationException {
			return null;
		}
	}

	public SerializationHandler() {
		storableObjectsQueue = new LinkedBlockingQueue<Storable>();
		toStorageQueue = new LinkedBlockingQueue<ObjectData>();
		unserializedObjects = new Vector<Object>();
	}

	public void offerObject(Storable object) {
		if (object == null)
			return;
		unprocessed();
		storableObjectsQueue.offer(object);
	}

	public void offerCollection(Collection<Storable> objects)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public ObjectData takeData() throws InterruptedException {
		return toStorageQueue.take();
	}

	public LinkedBlockingQueue<Storable> getInputObjectQueue() {
		return this.storableObjectsQueue;
	}

	public LinkedBlockingQueue<ObjectData> getOutputDataQueue() {
		return this.toStorageQueue;
	}

	public void run() {
		Storable obj = null;
		Map<Storable, UDID> storables = null;
		ObjectData od = null;
		boolean helthy = true;
		try {
			while (helthy) {

				obj = storableObjectsQueue.take();

					if (obj.getClass().equals(
							SerializationHandler.PoisonObject.class)) {
						LOGGER.warning("SerializationHandler thread poisoned, object will live no longer");
						helthy = false;
						if (super.getRunning() == 1
								&& storableObjectsQueue.size() != 0) {
							LOGGER.info("Ustored waiting data send to unstored queue");
							unserializedObjects.addAll(storableObjectsQueue);
						}
						continue;
					}

					try {
						Map<String, Object> variablesMap = obj.serialize();

						if (serialsMap.containsKey(obj))
							od = new ObjectData(obj, serialsMap.get(obj));
						else
							od = new ObjectData(obj);

						od.putAll(variablesMap);

						storables = od.pollAllStorableReferences();
						for (Storable storable : storables.keySet()) {
							this.storableObjectsQueue.put(storable);
						}
						serialsMap.putAll(storables);
						storables = null;

						toStorageQueue.offer(od);
						od = null;

					} catch (SerializationException se) {
						LOGGER.throwing(obj.getClass().getName(),
								"serialize()", se);
						unserializedObjects.add(obj);
						LOGGER.info("Object "
								+ obj.getClass().getName()
								+ ((serialsMap.containsKey(obj)) ? (" with serial number: " + serialsMap
										.get(obj)) : "")
								+ " sended to unserialized objects");
					}

					obj = null;

					if (storableObjectsQueue.isEmpty())
						processed();
			}
		} catch (InterruptedException e) {
			LOGGER.warning("Serialization thread interrupted!");
			LOGGER.throwing(SerializationHandler.class.getName(), "", e);

			this.storableObjectsQueue.notify();
			e.printStackTrace();

			if (obj != null) {
				this.storableObjectsQueue.offer(obj);
				LOGGER.info("Object " + obj.toString() + " send back to Queue");
			}

			if (od != null) {
				this.toStorageQueue.offer(od);
				LOGGER.info("Data " + od.getSerialNumber() + " stored to queue");
				od = null;
			}

			if (storables != null) {
				for (Storable storable : storables.keySet()) {
					this.storableObjectsQueue.offer(storable);
				}
				serialsMap.putAll(storables);
				storables = null;
				LOGGER.info("Storable object variables pushed back to queue");
			}
		}
	}

	@Override
	protected void poisonThread() {
		PoisonObject poison = new PoisonObject();
		storableObjectsQueue.offer(poison);
	}
}
