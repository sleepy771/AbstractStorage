package com.gmail.sleepy771.storage.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gmail.sleepy771.datastructures.Data;

public class SerializationHandler {
    private final Map<Class<?>, Serializer<?>> serializers;
    private final ExecutorService srv;

    public SerializationHandler(final ExecutorService srv,
	    final HashMap<Class<?>, Serializer<?>> serializers) {
	this.srv = srv;
	// should be injected
	this.serializers = serializers;
    }

    public Data serialize(final Object o) throws InterruptedException,
	    ExecutionException {
	if (o instanceof Serializable) {
	    Callable<Data> scall = new Callable<Data>() {
		@Override
		public Data call() throws Exception {
		    return Serializable.class.cast(o).serialize();
		}

	    };
	    Future<Data> fdata = srv.submit(scall);
	    return fdata.get();
	} else {

	}
	return null;
    }

}
