package com.syndiceo.util;

import java.io.Serializable;

public class FileHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4208436327588979528L;

	private byte[] data;
	private String mimeType;
	private int    maxSize;
	private String id;
	private String msg;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte[] getData() {
		return data;
	}
	public FileHandler() {
		super();
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public FileHandler(String id, byte[] data, String mimeType, int maxSize) {
		super();
		this.data = data;
		this.mimeType = mimeType;
		this.maxSize = maxSize;
		this.id = id;
	}
	public FileHandler(byte[] data, String mimeType, int maxSize) {
		super();
		this.data = data;
		this.mimeType = mimeType;
		this.maxSize = maxSize;
	}
	public boolean isTooBig() {
		return data != null && data.length > maxSize;
	}
	public boolean isValid() {
		return data != null && mimeType != null;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
}
