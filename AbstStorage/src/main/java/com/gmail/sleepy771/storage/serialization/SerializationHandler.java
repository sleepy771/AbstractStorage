package com.gmail.sleepy771.storage.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gmail.sleepy771.datastructures.DataInt;

public class SerializationHandler {
    private final Map<Class<?>, Serializer<?>> serializers;
    private final ExecutorService srv;

    public SerializationHandler(final ExecutorService srv,
	    final HashMap<Class<?>, Serializer<?>> serializers) {
	this.srv = srv;
	// should be injected
	this.serializers = serializers;
    }

    public DataInt serialize(final Object o) throws InterruptedException,
	    ExecutionException {
	if (o instanceof Serializable) {
	    Callable<DataInt> scall = new Callable<DataInt>() {
		@Override
		public DataInt call() throws Exception {
		    return Serializable.class.cast(o).serialize();
		}

	    };
	    Future<DataInt> fdata = srv.submit(scall);
	    return fdata.get();
	} else {

	}
	return null;
    }

}
