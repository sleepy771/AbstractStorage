package com.gmail.sleepy771.storage_interface.utils;

import com.gmail.sleepy771.storage_interface.misc.Result;
import com.gmail.sleepy771.storage_interface.navigation.Path;

public interface Organizable {
    public void setWorkingPath(Path<? extends Comparable<?>> path);
    
    public Result<Long> size();
    
    public Result<Long> capacity();
    
    public Result<Long> remainingSpace();
    
    public Result<Void> move(Path<? extends Comparable<?>> from, Path<? extends Comparable<?>> to);
    
    public Result<Void> copy(Path<? extends Comparable<?>> from, Path<? extends Comparable<?>> to);
    
    public Result<Void> clear();
}
