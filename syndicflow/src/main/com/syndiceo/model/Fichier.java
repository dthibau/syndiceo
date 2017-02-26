package com.syndiceo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Fichier {
	/**
	 * Documents
	 */
	public final static String DOCUMENT_EXTENSION					= ".";
	public final static String DOCUMENT_DUPLICATE_NAME_SEPERATOR	= "-";
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	protected int 	id;
	protected String 	fileName;
	protected String 	contentType;
	@Lob
	protected byte[] 	data;
	@Temporal(TemporalType.DATE)
	protected Date 	uploadDate;
	
	
	public Fichier() {
		super();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	} 	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date creationDate) {
		this.uploadDate = creationDate;
	}	
	
	
	@Override
	public String toString() {
		return fileName;
	}
	
	
	@Transient
	protected String getDocumentExtension(String fileName) {
		String ext = "";
		if(fileName.contains(DOCUMENT_EXTENSION)){
			ext = fileName.substring(fileName.lastIndexOf(DOCUMENT_EXTENSION) + 1, fileName.length());
		}
		return ext;
	}
	@Transient
	protected String getOrignalNameWithoutExtension(String str) {
		if(str.contains(DOCUMENT_EXTENSION)){
			str = str.substring(0 , str.lastIndexOf(DOCUMENT_EXTENSION));
		}
		
		Integer index = str.lastIndexOf(DOCUMENT_DUPLICATE_NAME_SEPERATOR);
		if(index != -1){
			try {	// Making sure that its a valid copy
				Integer.parseInt(str.substring(index+1, str.length()));
			} catch (NumberFormatException nfe) {
				return str ;
			}
			str = str.substring(0, index) ;	
		}
		return str;
	}
	
	protected Fichier clone(Fichier clone) {
		clone.setContentType(this.getContentType());
		clone.setUploadDate(new Date());
		clone.setData(this.getData());
		clone.setFileName(this.getFileName());	
		return clone;
		
	}
	
	@Transient
	public Boolean isImage() {
		return (contentType != null && contentType.contains("image")) ? Boolean.TRUE : Boolean.FALSE;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fichier other = (Fichier) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
