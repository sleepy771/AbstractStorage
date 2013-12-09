package com.gmail.sleepy771.storage_interface.utils;

import java.io.InvalidClassException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SerializerHandler {
    private static final Set<Class<?>> WRITABLE = new HashSet<>(Arrays.asList(
	    Boolean.class, Byte.class, Character.class, Short.class,
	    Integer.class, Float.class, Double.class, String.class, Path.class));
    private final ExecutorService srv;

    public SerializerHandler(final ExecutorService srv) {
	this.srv = srv;
    }

    private void recSerialization(final Data d) throws UnknowClassException{
	for(Map.Entry<String, Object> entry : d.entrySet()) {
	    if(!WRITABLE.contains(entry.getValue().getClass())) {
		if (entry.getValue() instanceof Serializable) {
		    Data nDat = Serializable.class.cast(entry.getValue()).serialize();
		    entry.setValue(nDat);
		    recSerialization(nDat);
		} else {
		    ExternalSerializable ser = StorageInterface.INSTANCE
			    .getSerializers(entry.getValue().getClass());
		    if (ser == null)
			throw new UnknowClassException();
		    ser.put(entry.getValue());
		    Data nDat = ser.serialize();
		    entry.setValue(nDat);
		    recSerialization(nDat);
		}
	    }
	}
    }

    public Future<Data> serialize(final Object obj) {
	return srv.submit(new Callable<Data>() {
	    @Override
	    public Data call() throws Exception {
		if (obj instanceof Serializable) {
		    Data d = Serializable.class.cast(obj).serialize();
		    recSerialization(d);
		    return d;
		} else {
		    ExternalSerializable ser = StorageInterface.INSTANCE
			    .getSerializers(obj.getClass());
		    if (ser == null)
			throw new UnknowClassException();
		    ser.put(obj);
		    Data dat = ser.serialize();
		    recSerialization(dat);
		    validate(dat);
		    return dat;
		}
	    }
	});
    }

    public void validate(final Data d) throws InvalidClassException {
	for (Map.Entry<String, Object> dataEntry : d.entrySet()) {
	    if (Data.class.isInstance(dataEntry.getValue())) {
		validate(Data.class.cast(dataEntry.getValue()));
	    } else if (!WRITABLE.contains(dataEntry.getValue().getClass()))
		throw new InvalidClassException("Class "
			+ dataEntry.getValue().getClass().getName()
			+ "in variable " + dataEntry.getKey()
			+ " is not a writable format!");
	}
    }
}
