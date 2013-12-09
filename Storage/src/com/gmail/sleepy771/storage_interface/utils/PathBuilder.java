package com.gmail.sleepy771.storage_interface.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PathBuilder implements Builder<Path> {
    private final LinkedList<String> path = new LinkedList<>();

    public PathBuilder(final String storageName) {
	path.add(storageName);
    }

    public PathBuilder add(final String node) {
	path.add(node);
	return this;
    }

    @Override
    public Path build() {
	// TODO Auto-generated method stub
	return new Path() {
	    LinkedList<String> path = PathBuilder.this.path;

	    @Override
	    public List<String> asList() {
		return new ArrayList<String>(path);
	    }

	    @Override
	    public String getFieldName() {
		return path.getLast();
	    }

	    @Override
	    public String getStorage() {
		return path.getFirst();
	    }

	    @Override
	    public Path innerPath() {
		path.removeFirst();
		return this;
	    }

	    @Override
	    public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return path.iterator();
	    }

	    @Override
	    public List<String> list() {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Path subPath(final String name) {
		// TODO Auto-generated method stub
		return null;
	    }

	};
    }

}
