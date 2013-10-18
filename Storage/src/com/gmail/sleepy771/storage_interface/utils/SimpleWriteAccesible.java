package com.gmail.sleepy771.storage_interface.utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.gmail.sleepy771.storage_interface.exceptions.PathException;
import com.gmail.sleepy771.storage_interface.misc.Result;
import com.gmail.sleepy771.storage_interface.navigation.DataKey;
import com.gmail.sleepy771.storage_interface.navigation.Path;


public interface SimpleWriteAccesible {
    public Result<Void> addBoolean(Path<DataKey<?>> path, boolean b) throws IOException, PathException;
    public Result<Void> addByte(Path<DataKey<?>> path, byte b) throws IOException, PathException;
    public Result<Void> addCharacter(Path<DataKey<?>> path, char c) throws IOException, PathException;
    public Result<Void> addShort(Path<DataKey<?>> path, short s) throws IOException, PathException;
    public Result<Void> addInteger(Path<DataKey<?>> path, int i) throws IOException, PathException;
    public Result<Void> addLong(Path<DataKey<?>> path, long l) throws IOException, PathException;
    public Result<Void> addFloat(Path<DataKey<?>> path, float f) throws IOException, PathException;
    public Result<Void> addDouble(Path<DataKey<?>> path, double d) throws IOException, PathException;
    public Result<Void> addBigDecimal(Path<DataKey<?>> path, BigDecimal bd) throws IOException, PathException;
    public Result<Void> addBigInteger(Path<DataKey<?>> path, BigInteger bi) throws IOException, PathException;
    //public void addByteArray(Path<DataKey<?>> path, ExtendedByteBuffer ba) throws IOException, PathException;
    //public void add
    //TODO set methods
}
