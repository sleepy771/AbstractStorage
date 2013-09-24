package com_gmail_sleepy771.astorage.utilities;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com_gmail_sleepy771.astorage.Storable;
import com_gmail_sleepy771.astorage.exceptions.SerializationException;
/*
 * 192 bit implementation
 * TODO cloneable
 */
public class UDID implements Comparable<UDID>, Cloneable {
	
	private final byte[] udid = new byte[24];
	
	public UDID(Object object, long timeInMillis){
		MessageDigest md5 = null;
		try {
			
			md5 = MessageDigest.getInstance("MD5");
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(output);
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.close();
			System.arraycopy(toByteArray(timeInMillis), 0, udid, 0, 8);
			System.arraycopy(md5.digest(output.toByteArray()), 0, udid, 8, 16);
			
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Oh snap, we've got an err here!");
		} catch (IOException e) {
			System.err.println("Oh snap, we've got an err here!");
		}
	}
	
	public UDID(Object object){
		this(object, System.currentTimeMillis());
	}
	
	public UDID(byte[] udid){
		int offset = 0;
		if(udid.length < 24){
			offset = this.udid.length - udid.length;
		}
		System.arraycopy(udid, 0, this.udid, offset, 24 - offset);
	}
	
	public UDID(BigInteger number){
		this(number.toByteArray());
	}
	
	public UDID(String number){
		this(new BigInteger(number, 16));
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<24; i++){
			sb.append(Integer.toHexString(udid[i] & 0xff));
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o){
		if(!o.getClass().equals(UDID.class))
			return false;
		return compareTo(UDID.class.cast(o))==0;
	}
	
	public BigInteger toBigInteger(){
		return new BigInteger(1, udid);
	}
	
	public static byte[] toByteArray(long value){
		/*byte[] out = new byte[8];
		for(int i=0; i<8; i++){
			out[7-i] = (byte) ((value>>(8*i))&0xff);
		}*/
		return ByteBuffer.allocate(8).putLong(value).array();
	}
	
	@Override
	public int compareTo(UDID o) {
		int ret = 0;
		for(int i=0; i<24; i++){
			if ((ret = (udid[i]&0xff) - (o.udid[i]&0xff))!=0)
				return ret;
		}
		return 0;
	}
	
	public static UDID valueOf(String udidNumber) {
		return new UDID(udidNumber);
	}
	
	public long getCreationTime() {
		long time = 0;
		for(int k = 0; k < 8; k++){
			time |= ((udid[7-k] & 0xff)<<(8*k));
		}
		return time;
	}
	
	public static void main(String[] arg){
		Storable o = new Storable(){

			/**
			 * 
			 */
			private static final long serialVersionUID = -6637195634784318108L;

			@Override
			public Map<String, Object> serialize()
					throws SerializationException {
				return null;
			}
			
		};
		long time = System.currentTimeMillis();
		UDID uqid = new UDID(o);
		time = System.currentTimeMillis() - time;
		System.out.printf("Creation time: %d\n", time);
		System.out.println(uqid);
		System.out.printf("BigInteger value: %s\n", uqid.toBigInteger());
		SimpleDateFormat form = new SimpleDateFormat();
		Date date = new Date(uqid.getCreationTime());
		System.out.println(date);
		System.out.println(new Date());
	}
}
