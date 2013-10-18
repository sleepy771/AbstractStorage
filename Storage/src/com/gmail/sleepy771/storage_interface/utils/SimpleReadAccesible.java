package com.gmail.sleepy771.storage_interface.utils;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.gmail.sleepy771.storage_interface.misc.Result;
import com.gmail.sleepy771.storage_interface.navigation.DataKey;
import com.gmail.sleepy771.storage_interface.navigation.Path;


public interface SimpleReadAccesible{
    public Result<Boolean> getBoolean(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Byte> getByte(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Character> getCharacter(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Short> getShort(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Integer> getInteger(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Long> getLong(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Float> getFloat(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<Double> getDouble(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<BigInteger> getBigInteger(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<BigDecimal> getBigDecimal(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    public Result<String> getString(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    //public ByteArrayInputStream getByteArrayInputStream(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
    //public InputStream getInputStream() throws IOException;
    //public ExtendedByteBuffer getByteArray(Path<DataKey<?>> path) throws NoSuchFieldException, IOException;
}
