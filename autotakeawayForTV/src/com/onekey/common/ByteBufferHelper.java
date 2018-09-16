package com.onekey.common;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public final class ByteBufferHelper {
	
//	private String defaultEncoding = "UnicodeLittle";
	private static String defaultEncoding = "utf-8";
	public static ByteBuffer clone(ByteBuffer original){
        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
        original.rewind();//copy from the beginning
        clone.put(original);
        original.rewind();
        clone.flip();
        return clone;
    }
	
	public static void copy(ByteBuffer dest, ByteBuffer src) {
		src.rewind();//copy from the beginning
		dest.put(src);
        src.rewind();
        dest.flip();
	}
	
	/**
	 * Read specified length bytes, return String.<br/>
	 * Default encoding is "UnicodeLittle"
	 * @author shen
	 */
	public static String getBytesAsString(ByteBuffer buffer, int len) {
		return getBytesAsString(buffer, len, defaultEncoding);
	}
	
	/**
	 * Read specified length bytes, return String.
	 * @author shen
	 */
	public static String getBytesAsString(ByteBuffer buffer, int len, String charset) {			
		String text = null;			
		byte[] cstr = new byte[len];
		buffer.get(cstr, 0, len);
		try {
			text = new String(cstr, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * Read specified length bytes, return String('\0' is dropped)<br/>
	 * Default encoding is "UnicodeLittle"
	 * @author shen
	 * HH Note: len is count of bytes
	 */
	public static String getCStrAsString(ByteBuffer buffer, int len) {
		String text = getBytesAsString(buffer, len, defaultEncoding);
		return text.substring(0, text.indexOf('\0'));
	}
	
	/**
	 * Read specified length bytes, return String('\0' is dropped)
	 * @author shen
	 */
	public static String getCStrAsString(ByteBuffer buffer, int len, String charset) {
		String text = getBytesAsString(buffer, len, charset);
		return text.substring(0, text.indexOf('\0'));
	}
	
	/** 
	 * Wrapper for put char array 
	 * @author shen
	 */
    public static void putChars(ByteBuffer buffer, char[] array) {
    	if (array != null) {
			for (char ch: array) {
				buffer.putChar(ch);
			}
    	}
	}
    
    /**
     * Write UUID type
     * @author shen
     */
    public static void putUuid(ByteBuffer buffer, UUID uuid) {
    	buffer.order(ByteOrder.LITTLE_ENDIAN);
    	buffer.putLong(uuid.getMostSignificantBits());
    	buffer.putLong(uuid.getLeastSignificantBits());
    }
    
    /**
     * Read UUID type
     * @author shen
     */
    public static UUID getUuid(ByteBuffer buffer) {
    	buffer.order(ByteOrder.LITTLE_ENDIAN);
    	return new UUID(buffer.getLong(), buffer.getLong());
    }
}
