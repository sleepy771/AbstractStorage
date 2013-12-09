package com.gmail.sleepy771.storage_interface.example;

import java.util.concurrent.ExecutionException;

import com.gmail.sleepy771.storage_interface.utils.Path;
import com.gmail.sleepy771.storage_interface.utils.PathBuilder;
import com.gmail.sleepy771.storage_interface.utils.Storage;
import com.gmail.sleepy771.storage_interface.utils.StorageInterface;

public class SimpleExample {
    public static void main(final String[] arg) throws InterruptedException,
	    ExecutionException {
	StorageInterface si = StorageInterface.INSTANCE;
	Storage inM = new InMemStorage();
	si.register("inMem", inM);
	si.putDesrializer(SimpleObject.class.getName(),
		new SimpleObjectDeserializer());
	SimpleObject obj = new SimpleObject();
	obj.setName("kon");
	obj.setNumber(25);
	obj.setDecimal(2.14f);
	Path p = new PathBuilder("inMem").add("kon").build();
	si.save(p, obj);
	System.out.println("Atempt to save");

	// Thread.sleep(2000);
	System.out.println("Atempt to load");
	System.out.println(inM);
	p = new PathBuilder("inMem").add("kon").build();
	SimpleObject objnew = (SimpleObject) si.load(p);
	System.out.println(objnew.getName());
    }
}
