package com_gmail_sleepy771.astorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ObjectData extends TreeMap<String, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2118142051309880033L;

	public static final String CLASS = "a2f2ed4f8ebc2cbb4c21a29dc40ab61d";

	@SuppressWarnings("rawtypes")
	public static final HashSet<Class> writableClasses = new HashSet<Class>(
			Arrays.<Class> asList(Boolean.class, Byte.class, Character.class,
					Short.class, Integer.class, Long.class, Float.class,
					Double.class, String.class));

	private TreeMap<String, Long> referenceSerials; // Maybe String instead of
													// Long for large objects
													// with lots of
													// serializables
	private TreeSet<Long> serials;
	private final long serialNumber;
	private TreeSet<String> storableReferenceNames = null;
	
	public ObjectData(String classType, long serial){
		this.put(CLASS, classType);
		this.serialNumber = serial;
	}

	public ObjectData(Storable obj, long serial) {
		this(obj.getClass().getName(), serial);
	}

	public ObjectData(Storable obj) {
		this(obj, Serials.generateSerial());
	}

	public boolean isMultidimensional(String name) {
		Object obj = get(name);
		if (obj == null)
			return false;
		return obj.getClass().isArray() || Collection.class.isInstance(obj);
	}

	public TreeMap<String, Long> getReferenceSerials()
			throws NullPointerException {
		if (this.referenceSerials == null)
			throw new NullPointerException(
					"Data doesn't contain any serial numbers of other references");
		return new TreeMap<String, Long>(this.referenceSerials); // send only
																	// cloned
																	// object
	}

	public void putReferenceSerial(String varName, long sn) {
		if (this.referenceSerials == null)
			this.referenceSerials = new TreeMap<String, Long>();
		this.referenceSerials.put(varName, sn);
	}
	
	public void putAllReferenceSerials(Map<String, Long> references){
		if (this.referenceSerials == null)
			this.referenceSerials = new TreeMap<String, Long>();
		this.referenceSerials.putAll(references);
	}

	public long removeReferenceSerial(String varName)
			throws NullPointerException {
		if (this.referenceSerials == null)
			throw new NullPointerException(
					"Data doesn't contain any serial numbers of other references");
		return this.referenceSerials.remove(varName);
	}

	public Set<String> getVariableNamesSet() {
		TreeSet<String> variables = new TreeSet<String>(this.keySet());
		variables.addAll(this.referenceSerials.keySet());
		return variables;
	}

	public Map<String, Object> toMap(Map<String, Object> map) {
		map.putAll(this);
		return map;
	}

	public String getClassName() {
		return get(CLASS).toString();
	}

	public long getSerialNumber() {
		return this.serialNumber;
	}

	public Set<String> getStorableReferenceNameSet() {
		return new TreeSet<String>(this.storableReferenceNames);
	}

	public HashMap<Storable, Long> pollAllStorableReferences() {
		HashMap<Storable, Long> storables = new HashMap<Storable, Long>();
		this.referenceSerials = new TreeMap<String, Long>();
		if(storableReferenceNames == null){
			return storables;
		}
		for (String refName : this.storableReferenceNames) {
			Storable storable = Storable.class.cast(remove(refName));
			long serialNumber = Serials.generateSerial();
			this.referenceSerials.put(refName, serialNumber);
			storables.put(storable, serialNumber);
		}
		this.storableReferenceNames = null;
		return storables;
	}

	public synchronized static boolean isWritableFormat(Object o) {
		return writableClasses.contains(o.getClass()) || Storable.class.isInstance(o);
	}

	// TODO make arrays writtable

	public synchronized static boolean isCollectionOfWritables(Object o) {
		if (o != null && Collection.class.isInstance(o)) {
			@SuppressWarnings("unchecked")
			Collection<Object> col = Collection.class.cast(o);
			Iterator<Object> iter = col.iterator();
			if (iter.hasNext()) {
				return isWritableFormat(iter.next());
			}
		}
		return false;
	}

	public boolean containsSerial(long serial) {
		if (serials == null) {
			this.serials = new TreeSet<Long>();
			this.serials.addAll(this.referenceSerials.values());
		}
		return this.serials.contains(serial);
	}

	public boolean containsSerials(Collection<Long> serials) {
		if (serials == null) {
			this.serials = new TreeSet<Long>();
			this.serials.addAll(this.referenceSerials.values());
		}
		return this.serials.containsAll(serials);
	}

	@Override
	public Object put(String key, Object value) throws IllegalArgumentException {
		if (!(isWritableFormat(value) || isCollectionOfWritables(value))) {
			throw new IllegalArgumentException(
					"Value should be primitive type, String or Collection of both!!!");
		}
		if (Storable.class.isInstance(value)) {
			if (this.storableReferenceNames == null)
				this.storableReferenceNames = new TreeSet<String>();
			this.storableReferenceNames.add(key);
		}

		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> map)
			throws IllegalArgumentException {
		for (Map.Entry<? extends String, ? extends Object> entry : map
				.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}
}
