package com.gmail.sleepy771.storage_interface.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gmail.sleepy771.storage_interface.utils.Data;
import com.gmail.sleepy771.storage_interface.utils.Path;
import com.gmail.sleepy771.storage_interface.utils.Result;
import com.gmail.sleepy771.storage_interface.utils.Storage;
import com.gmail.sleepy771.storage_interface.utils.UnknownPathException;

public class InMemStorage implements Storage {
    Map<String, Data> storage = new TreeMap<>();
    @Override
    public long capacity() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void go(final String name) {
	// TODO Auto-generated method stub

    }

    @Override
    public List<String> list(final Path p) {
	return new ArrayList<>(storage.keySet());
    }

    @Override
    public List<Path> listPaths() {
	return null;
    }

    @Override
    public Data read(final Path path) throws IOException, UnknownPathException {
	return storage.get(path.getFieldName());
    }

    @Override
    public Result read(final Path path, final String name) throws IOException,
	    UnknownPathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Data remove(final String name) {
	return storage.remove(name);
    }

    @Override
    public String toString() {
	return storage.toString();
    }

    @Override
    public Result write(final Data data) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result write(final Path path, final Data data) {
	storage.put(path.getFieldName(), data);
	System.out.println(storage);
	return null;
    }

    @Override
    public Result writeRel(final Path path, final Data data) {
	write(path, data);
	return null;
    }

    @Override
    public Result writeRel(final String name, final Data data) {
	storage.put(name, data);
	return null;
    }
}
