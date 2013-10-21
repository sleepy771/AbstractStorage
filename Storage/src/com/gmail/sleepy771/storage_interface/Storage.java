package com.gmail.sleepy771.storage_interface;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.NoSuchObjectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.gmail.sleepy771.storage_interface.exceptions.PathException;
import com.gmail.sleepy771.storage_interface.misc.Data;
import com.gmail.sleepy771.storage_interface.misc.Result;
import com.gmail.sleepy771.storage_interface.misc.Serial;
import com.gmail.sleepy771.storage_interface.misc.StorageInterface;
import com.gmail.sleepy771.storage_interface.misc.Writable;
import com.gmail.sleepy771.storage_interface.navigation.DataKey;
import com.gmail.sleepy771.storage_interface.navigation.Path;
import com.gmail.sleepy771.storage_interface.utils.Organizable;
import com.gmail.sleepy771.storage_interface.utils.SimpleReadAccesible;
import com.gmail.sleepy771.storage_interface.utils.SimpleWriteAccesible;
/* singleton */
public class Storage implements StorageInterface, SimpleReadAccesible, SimpleWriteAccesible, Organizable {
    
    public static final Storage INSTANCE = new Storage();
    
    private final HashMap<String, StorageInterface> storages = new HashMap<String, StorageInterface>();
    
    private Storage(){
    }
    
    public void init(){
    }
    
    public void loadStorages() {
	
    }
    
    public StorageInterface getStorage(String storage) throws NoSuchObjectException {
	StorageInterface storageInterface = storages.get(storage);
	if (storageInterface == null)
	    throw new NoSuchObjectException("Storage "+storage+" does not exist");
	return storageInterface;
    }
    
    public StorageInterface getStorage(DataKey<String> storageKey) throws NoSuchObjectException {
	return getStorage(storageKey.get());
    }
    
    @Override
    public void setWorkingPath(Path<? extends Comparable<?>> path) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public Result<Long> capacity() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Long> remainingSpace() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> move(Path<? extends Comparable<?>> from,
	    Path<? extends Comparable<?>> to) {
		return null;
	// TODO Auto-generated method stub
	
    }

    @Override
    public Result<Void> copy(Path<? extends Comparable<?>> from,
	    Path<? extends Comparable<?>> to) {
		return null;
	// TODO Auto-generated method stub
	
    }

    @Override
    public Result<Void> clear() {
	return null;
	// TODO Auto-generated method stub
	
    }

    @Override
    public Result<Void> flush() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> close() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Long> size() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addBoolean(Path<DataKey<?>> path, boolean b)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addByte(Path<DataKey<?>> path, byte b)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addCharacter(Path<DataKey<?>> path, char c)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addShort(Path<DataKey<?>> path, short s)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addInteger(Path<DataKey<?>> path, int i)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addLong(Path<DataKey<?>> path, long l)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addFloat(Path<DataKey<?>> path, float f)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addDouble(Path<DataKey<?>> path, double d)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addBigDecimal(Path<DataKey<?>> path, BigDecimal bd)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> addBigInteger(Path<DataKey<?>> path, BigInteger bi)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Boolean> getBoolean(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Byte> getByte(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Character> getCharacter(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Short> getShort(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Integer> getInteger(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Long> getLong(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Float> getFloat(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Double> getDouble(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<BigInteger> getBigInteger(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<BigDecimal> getBigDecimal(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<String> getString(Path<DataKey<?>> path)
	    throws NoSuchFieldException, IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Serial> save(Path<?> path, Data data) throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Serial> save(Data data) throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Result<Serial>> saveAll(Map<Path<?>, Data> dataMap)
	    throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Result<Serial>> saveAll(Collection<Data> data)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> add(Path<?> path, Writable<?> writable)
	    throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Result<Void>> addAll(Path<?> path,
	    Collection<Writable<?>> writable) throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Writable<?>> set(Path<?> path, Writable<?> writable)
	    throws IOException, PathException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Result<Data>> load(Path<?> path) throws NoSuchElementException,
	    IOException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<Result<Void>> removeAll(Collection<Path<?>> paths)
	    throws IOException, NoSuchElementException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Map<Path<?>, List<Result<Data>>> loadAll(Collection<Path<?>> paths)
	    throws NoSuchElementException, IOException {
	//TODO split paths by storage from which they will get data
	
	return null;
    }

    @Override
    public Result<Writable<?>> get(Path<?> path) throws IOException,
	    NoSuchElementException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Void> remove(Path<?> path) throws IOException,
	    NoSuchElementException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Result<Long> size(Path<?> path) {
	// TODO Auto-generated method stub
	return null;
    }
}
