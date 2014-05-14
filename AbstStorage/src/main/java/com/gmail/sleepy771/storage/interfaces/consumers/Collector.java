package com.gmail.sleepy771.storage.interfaces.consumers;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

import com.gmail.sleepy771.storage.exceptions.ElementsNotFullyCollectedException;

public interface Collector {
    public void addFutureObject(String name, Future<Object> f);
    public void addAllFutureObjects(Map<String, Future<Object>> c);
    public void removeFutureObject(String name);
    public void removeAllFutureObjects(Collection<String> c);
    public void collect();
    public boolean isCollected();
    public Map<String, Object> getObjects() throws ElementsNotFullyCollectedException;
}
