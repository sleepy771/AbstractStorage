package com.gmail.sleepy771.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GlobalPath implements Path {

    public static class Builder implements Buildable<GlobalPath> {
	private final List<String> pathbuilder = new ArrayList<>();

	public Builder() {
	    this("");
	}

	public Builder(final String storage) {
	    pathbuilder.add(storage);
	}

	public Builder add(final Collection<String> c) {
	    for(String s : c) {
		pathbuilder.add(s);
	    }
	    return this;
	}

	public Builder add(final Path p) {
	    for (String s : p) {
		pathbuilder.add(s);
	    }
	    return this;
	}

	public Builder add(final String path) {
	    pathbuilder.add(path);
	    return this;
	}

	public Builder add(final String[] path) {
	    for(String s : path) {
		pathbuilder.add(s);
	    }
	    return this;
	}

	@Override
	public GlobalPath build() {
	    if (pathbuilder.get(0).equals(""))
		throw new UnsupportedOperationException(
			"Storage should be defined first");
	    return new GlobalPath(new LinkedList<>(pathbuilder));
	}

	public String getStorage() {
	    return pathbuilder.get(0);
	}

	public Builder remove(final int idx) {
	    if(idx < 0)
		throw new UnsupportedOperationException("Can not remove storage name");
	    pathbuilder.remove(idx + 1);
	    return this;
	}

	public Builder removeLast() {
	    if(pathbuilder.size() < 2)
		throw new UnsupportedOperationException("Can not remove empty list");
	    pathbuilder.remove(pathbuilder.size() - 1);
	    return this;
	}

	public Builder set(final int idx, final String path) {
	    if (idx < 0)
		throw new UnsupportedOperationException("Can not set storage name by this method");
	    pathbuilder.set(idx + 1, path);
	    return this;
	}

	public Builder setStorage(final String storage){
	    pathbuilder.set(0, storage);
	    return this;
	}

	@Override
	public String toString() {
	    StringBuilder sb =  new StringBuilder();
	    for (String s : pathbuilder) {
		sb.append(s).append("/");
	    }
	    return sb.toString();
	}

    }

    private LinkedList<String> path;

    private GlobalPath(final LinkedList<String> path) {
	this.path = path;
    }

    @Override
    public List<String> asList() {
	return new LinkedList<>(path);
    }

    @Override
    public void dispose() {
	path.clear();
	path = null;
    }

    @Override
    public String get(final int idx) {
	return path.get(idx + 1);
    }

    @Override
    public String getNode() {
	return path.getFirst();
    }

    public String getStorageName() {
	return path.getFirst();
    }

    @Override
    public Path getSubpath() {
	return new LocalPath.Builder(this).build();
    }

    @Override
    public Iterator<String> iterator() {
	return new Iterator<String>() {
	    private final Iterator<String> iter = path.iterator();

	    @Override
	    public boolean hasNext() {
		return iter.hasNext();
	    }

	    @Override
	    public String next() {
		return iter.next();
	    }

	    @Override
	    public void remove() {
		throw new UnsupportedOperationException();
	    }

	};
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder("\\gp@");
	for (Iterator<String> pIter = path.iterator(); pIter.hasNext();) {
	    sb.append(pIter.next());
	    if(pIter.hasNext())
		sb.append(".");
	}
	return sb.toString();
    }
}
