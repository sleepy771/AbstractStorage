package com_gmail_sleepy771.astorage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com_gmail_sleepy771.astorage.exceptions.IncompleteDataException;
import com_gmail_sleepy771.astorage.exceptions.StorageException;
import com_gmail_sleepy771.astorage.handlers.DeserializationHandler;
import com_gmail_sleepy771.astorage.handlers.SerializationHandler;
import com_gmail_sleepy771.astorage.handlers.StorageReaderSingle;
import com_gmail_sleepy771.astorage.handlers.StorageWriter;
import com_gmail_sleepy771.astorage.logging.ASDefaultLogger;
import com_gmail_sleepy771.astorage.utilities.ObjectData;
import com_gmail_sleepy771.astorage.utilities.Query;
/**
 * StorageControl is central static class for simple access to storage interface objects.
 * Object is useful for simplifying code for IO operations, splitting Storage from code and option to change storage type (e.g. XML to SQL)
 * by adding new storage part to interface.
 * <code>
 * AbstractStorage storage = ...
 * StorageControl.init(storage);
 * ...
 * StorageControl.close();
 * </code>
 * @author filip
 *
 */
public class StorageControl {
	private static final Logger LOGGER = Logger.getLogger(StorageControl.class.getName());
	//Maybe as singletons
	private static final SerializationHandler serializer = new SerializationHandler();
	private static final DeserializationHandler deserializer = new DeserializationHandler();
	private static final StorageWriter writer = new StorageWriter();
	private static final StorageReaderSingle reader = new StorageReaderSingle();
	private static AbstractStorage storage = null;
	
	private static int threadCount = 0;
	
	private StorageControl(){
	}
	
	/**
	 * Initializes and setup environment for proper run of storage interface.
	 * @param storage
	 * @throws StorageException
	 */
	public static void init(AbstractStorage storage) throws StorageException{
		setStorage(storage);
		try {
			ASDefaultLogger.setup();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDefaultThreadCount();
		writer.setInputQueue(serializer.getOutputDataQueue());
	}
	
	/**
	 * Method run serialization threads and stores output to defined storage.
	 * @param storables
	 */
	public static void store(Collection<Storable> storables){
		if(writer.getRunning() == 0 )
			writer.newThread();
		if(serializer.getRunning() == 0){
			for(int t = 0; t < threadCount; t++)
				serializer.newThread();
		}
		for(Storable storable : storables)
			serializer.offerObject(storable);
	}
	
	/**
	 * Method run serialization threads on array of parameters objects and stores output to defined storage.
	 * @param Storable object
	 */
	public static void store(Storable... storables){
		if(writer.getRunning() == 0 )
			writer.newThread();
		if(serializer.getRunning() == 0){
			for(int t = 0; t < threadCount; t++)
				serializer.newThread();
		}
		for(Storable storable : storables)
			serializer.offerObject(storable);
	}
	
	/**
	 * Method run serialization threads on parameter and stores output to defined storage.
	 * @param Storable object
	 */
	public static void store(Storable storable){
		if(writer.getRunning() == 0 )
			writer.newThread();
		if(serializer.getRunning() == 0){
			for(int t = 0; t < threadCount; t++)
				serializer.newThread();
		}
		try{
			serializer.offerObject(storable);
		}
		catch(NullPointerException npe){
			LOGGER.throwing(serializer.getClass().getName(), "offerObject(Storable storable)", npe.fillInStackTrace());
		}
	}
	
	/**
	 * Method stores data object to storage.
	 * @param data
	 */
	public static void storeData(ObjectData data){
		if(writer.getRunning() == 0 )
			writer.newThread();
		if(serializer.getRunning() == 0){
			for(int t = 0; t < threadCount; t++)
				serializer.newThread();
		}
		writer.offerData(data);
	}
	
	/**
	 * Method stores collection of data objects to storage.
	 * @param data
	 */
	public static void storeData(Collection<ObjectData> dataCollection){
		if(writer.getRunning() == 0 )
			writer.newThread();
		if(serializer.getRunning() == 0){
			for(int t = 0; t < threadCount; t++)
				serializer.newThread();
		}
		writer.offerCollection(dataCollection);
	}
	
	/**
	 * Method for loading <b>objects</b> by query from storage.
	 * @param Query by which is selected certain object(s) to be loaded from storage.
	 * @return Vector of objects that satisfies query
	 * @throws InterruptedException while waiting for output objects is interrupted
	 * @throws IncompleteDataException when loader object cann't find all necessary data to create an object.
	 */
	public static Vector<Object> load(Query q) throws InterruptedException, IncompleteDataException{
		if(deserializer.getRunning() == 0){
			for(int p = 0; p < threadCount; p++){
				StorageControl.deserializer.newThread();
			}
		}
		LinkedList<ObjectData> dataList = reader.getData(q);
		ObjectData last = dataList.removeFirst();
		int index = dataList.indexOf(last);
		List<ObjectData> requestedData = dataList.subList(0, index);
		List<ObjectData> helperData = dataList.subList(index + 1, dataList.size() - 1);
		Vector<Object> output = deserializer.deserializeData(requestedData, helperData);
		return output;
		
	}
	
	/**
	 * Method for loading <b>objects</b> by queries from storage.
	 * @param Collection of Queries by which is selected certain object(s) to be loaded from storage.
	 * @return Map mapping Query respectively to objects which satisfies query requirements
	 * @throws InterruptedException while waiting for output objects is interrupted
	 * @throws IncompleteDataException when loader object cann't find all necessary data to create an object.
	 */
	public static HashMap<Query,Vector<Object>> load(Collection<Query> queries) throws InterruptedException, IncompleteDataException{
		if(deserializer.getRunning() == 0){
			for(int p = 0; p < threadCount; p++){
				StorageControl.deserializer.newThread();
			}
		}
		HashMap<Query,Vector<Object>> queryMap = new HashMap<Query, Vector<Object>>();
		for(Query q : queries){
			Vector<Object> objects = load(q);
			queryMap.put(q, objects);
		}
		return queryMap;
	}
	
	/**
	 * Method for loading <b>objects</b> by queries from storage.
	 * @param Array of Queries by which is selected certain object(s) to be loaded from storage.
	 * @return Map mapping Query respectively to objects which satisfies query requirements
	 * @throws InterruptedException while waiting for output objects is interrupted
	 * @throws IncompleteDataException when loader object cann't find all necessary data to create an object.
	 */
	public static HashMap<Query,Vector<Object>> load(Query...queries) throws InterruptedException, IncompleteDataException{
		if(deserializer.getRunning() == 0){
			for(int p = 0; p < threadCount; p++){
				StorageControl.deserializer.newThread();
			}
		}
		HashMap<Query,Vector<Object>> queryMap = new HashMap<Query, Vector<Object>>();
		for(int k = 0; k < queries.length; k++){
			Vector<Object> objects = load(queries[k]);
			queryMap.put(queries[k], objects);
		}
		return queryMap;
	}
	
	/**
	 * Method for loading <b>data</b> by queries from storage.
	 * @param Query by which are selected certain data to be loaded from storage.
	 * @return Vector of data objects that satisfy query requirements.
	 * @throws IOException when encounters some errors by loading from storage.
	 */
	public static Vector<ObjectData> loadData(Query q) throws IOException {
		return new Vector<ObjectData>(storage.load(q));
	}
	
	/**
	 * Method that pulls certain variable value from data stored in storage that satisfies query.
	 * @param q Query by which are selected certain data to be loaded from storage.
	 * @param index of data object, when they are loaded more then one (in most cases 0).
	 * @param varName name of variable which to pull.
	 * @return variable value object
	 * @throws IOException when encounters some errors by loading from storage.
	 */
	public static Object get(Query q, int index, String varName) throws IOException{
		Vector<ObjectData> dataVector = loadData(q);
		ObjectData data = dataVector.get(index);
		return data.get(varName);
	}
	
	/**
	 * Method for setting variable value in storage.
	 * @param q
	 * @param index
	 * @param variableName
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static Object set(Query q, int index, String variableName, Object obj) throws IOException {
		Object old = null;
		Vector<ObjectData> dataVector = loadData(q);
		ObjectData data = dataVector.get(index);
		try{
			if(!data.containsKey(variableName) || !data.get(variableName).getClass().equals(obj.getClass())){
				LOGGER.warning("Data object should contain variable of selected name and value classes should be equal!");
				return null;
			}
			old = data.put(variableName, obj);
			storeData(data);
		} catch(IllegalArgumentException iae){
			LOGGER.warning("To storage should be written expicitly Values that can be stored");
			old = null;
		}
		return old;
	}

	public static SerializationHandler getSerializer() {
		return serializer;
	}
	
	public static DeserializationHandler getDeserializer(){
		return deserializer;
	}
	
	/**
	 * Method for changing storage on the fly.
	 * @param storage
	 */
	public synchronized static void setStorage(AbstractStorage storage){
		StorageControl.storage = storage;
		StorageControl.writer.setStorage(storage);
		StorageControl.reader.setStorage(storage);
	}
	
	/**
	 * Method for changing default thread count in (de)serialization objects. Not working propperly yet.
	 * @param threads
	 */
	public synchronized static void setThreadCount(int threads) throws UnsupportedOperationException{
		throw new UnsupportedOperationException();
	}
	
	public synchronized static int getThreadCount(){
		return StorageControl.threadCount;
	}
	
	/**
	 * Sets default thread count by number of processors in system.
	 */
	public synchronized static void setDefaultThreadCount(){
		StorageControl.threadCount = Runtime.getRuntime().availableProcessors();
	}
	
	/**
	 * Ends run of interface. Should be called when application using this interface stops.
	 */
	public static void stop(){
		StorageControl.writer.close();
		StorageControl.serializer.close();
		StorageControl.deserializer.close();
	}
}
