package com_gmail_sleepy771.astorage;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class StorageReaderSingle {
	private static final Logger LOGGER = Logger.getLogger(StorageReader.class.getName());
	
	AbstractStorage storage = null;
	
	public LinkedList<ObjectData> getData(Query q) throws IncompleteDataException{
		LinkedList<ObjectData> outputData = new LinkedList<ObjectData>();
		try{
			Set<ObjectData> returnedDataSet = storage.load(q);
			outputData.addAll(returnedDataSet);
			TreeSet<Long> requiredData = new TreeSet<Long>();
			for(ObjectData data : returnedDataSet){
				requiredData.addAll(data.getReferenceSerials().values());
			}
			if(!requiredData.isEmpty()){
				Query rq = new Query();
				rq.addEqualityCriteria(Query.REFERENCE_SERIALS, requiredData);
				outputData.addAll(getData(rq));
			}
			else if(!isClosed(outputData)){
					throw new IncompleteDataException();
			}
		}
		catch(IOException ioe){
			LOGGER.severe("IOError occured on storage side, Storage Reader is goiong to be closed!");
			LOGGER.throwing(storage.getClass().getName(), "load(Query query)", ioe);
			ioe.printStackTrace();
			if(!isClosed(outputData))
				throw new IncompleteDataException();
		}
		return outputData;
	}
	
	public static boolean isClosed(Collection<ObjectData> dataCollection){
		TreeSet<Long> contained = new TreeSet<Long>();
		for(ObjectData data : dataCollection){
			contained.add(data.getSerialNumber());
		}
		boolean contains = true;
		for(ObjectData data : dataCollection){
			contains &= contained.containsAll(data.getReferenceSerials().values());
		}
		return contains;
	}

	public void setStorage(AbstractStorage storage) {
		this.storage = storage;
	}
}
