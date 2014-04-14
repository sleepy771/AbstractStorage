package com.gmail.sleepy771.prod_cons;

public interface Consumer<T> {
    public void injectObject(T obj);

    public void onRecieve(T obj);
}
