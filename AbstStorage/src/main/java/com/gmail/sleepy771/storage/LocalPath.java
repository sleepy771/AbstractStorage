package com.gmail.sleepy771.storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LocalPath implements Path {

    public static class Builder implements Buildable<LocalPath> {

	private final LinkedList<String> pathbuilder = new LinkedList<>();

	public Builder() {
	}

	public Builder(final Path p) {
	    for (String n : p) {
		pathbuilder.add(n);
	    }
	    if (p.getClass().equals(GlobalPath.class)) {
		removeFirst();
	    }
	}

	public Builder add(final Collection<String> c) {
	    pathbuilder.addAll(c);
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
	    for (String s : path) {
		pathbuilder.add(s);
	    }
	    return this;
	}

	@Override
	public LocalPath build() {
	    return new LocalPath(new LinkedList<>(pathbuilder));
	}

	public Builder insert(final int idx, final Collection<String> c) {
	    pathbuilder.addAll(idx, c);
	    return this;
	}

	public Builder insert(final int idx, final Path p) {
	    return insert(idx, p.asList());
	}

	public Builder insert(final int idx, final String s) {
	    pathbuilder.add(idx, s);
	    return this;
	}

	public Builder remove(final int idx) {
	    pathbuilder.remove(idx);
	    return this;
	}

	public Builder removeFirst() {
	    pathbuilder.removeFirst();
	    return this;
	}

	public Builder removeLast() {
	    pathbuilder.removeLast();
	    return this;
	}

	public Builder set(final int idx, final String path) {
	    pathbuilder.set(idx, path);
	    return this;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    for (String s : pathbuilder) {
		sb.append(s).append("/");
	    }
	    return sb.toString();
	}

    }

    private LinkedList<String> path;

    private LocalPath(final LinkedList<String> path) {
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
	return path.get(idx);
    }

    @Override
    public String getNode() {
	return path.getFirst();
    }

    @Override
    public Path getSubpath() {
	return new Builder(this).removeFirst().build();
    }

    @Override
    public Iterator<String> iterator() {
	return new Iterator<String>() {
	    Iterator<String> iter = path.iterator();

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
	StringBuilder sb = new StringBuilder("/");
	for (String p : path) {
	    sb.append(p).append("/");
	}
	return sb.toString();
    }

}
