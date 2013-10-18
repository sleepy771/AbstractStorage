package com.gmail.sleepy771.storage_interface.fromat;


public interface Format<T> {
    public T get();
    public void set(T type);
    //representation
    public <R> Format<R> changeFormat(Formater<T, R> formater);
}
