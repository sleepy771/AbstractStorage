package com.gmail.sleepy771.storage_interface.utils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DeserializerHandler {

    private final ExecutorService srv;

    public DeserializerHandler(final ExecutorService srv) {
	this.srv = srv;
    }

    public Future<Object> deserialize(final Data d) {
	return srv.submit(new Callable<Object>() {
	    @Override
	    public Object call() throws Exception {
		return recDeserialize(d);
	    }
	});
    }

    public Future<Object> deserialize(final Future<Data> fd)
	    throws InterruptedException, ExecutionException {
	return deserialize(fd.get());
    }

    private Object recDeserialize(final Data d) throws UnknowClassException {
	for (Map.Entry<String, Object> dataEntry : d.entrySet()) {
	    if (dataEntry.getValue() instanceof Data) {
		Object o = recDeserialize(d);
		dataEntry.setValue(o);
	    }
	}
	Object obj = d.get(DataBuilder.CLASS); // TODO vytvorit CLASS static
					       // field
	Deserializer des = StorageInterface.INSTANCE.getDeserializer(obj
		.toString());
	if (des == null)
	    throw new UnknowClassException();
	return des.deserialize(d);
    }
}
