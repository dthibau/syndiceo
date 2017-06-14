package com.syndiceo.model;

import javax.persistence.Entity;

@Entity
public class FichierEvent extends Fichier {

	public FichierEvent() {
		
	}
	
	public FichierEvent(Fichier pj) {
		this.fileName = pj.getFileName();
		this.contentType = pj.getContentType();
		this.data = pj.getData();
		this.uploadDate = pj.getUploadDate();

	}
	
}
