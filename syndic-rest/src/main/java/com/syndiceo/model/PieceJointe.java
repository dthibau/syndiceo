package com.syndiceo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class PieceJointe extends Fichier {

	@ManyToOne
	protected Demande demande;
	
	public PieceJointe() {
		
	}
	public PieceJointe(Fichier pj, Demande demande) {
		this.fileName = pj.getFileName();
		this.contentType = pj.getContentType();
		this.data = pj.getData();
		this.uploadDate = pj.getUploadDate();
		this.demande = demande;
	}
	

	public Demande getDemande() {
		return demande;
	}
	public void setDemande(Demande demande) {
		this.demande = demande;
	}
	@Transient
	public String getDuplicateName(List<PieceJointe> pjs){
		
		String fileName = getFileName(); 
		String ext = getDocumentExtension(fileName);
		
		// compare file Extensions
		boolean sameExtension = false; 
		for(Fichier pj : pjs){
			String tempExt = getDocumentExtension(pj.getFileName());
			if(ext.equalsIgnoreCase(tempExt) ){
				sameExtension = true;
				break;
			}
		}
		
		if(sameExtension){	// if same or no file extension
		
			// compare fileNames without extensions 
			int totalCopies = 0;
			String name = getOrignalNameWithoutExtension(fileName);
			for(Fichier pj : pjs){
				String tempName = getOrignalNameWithoutExtension(pj.getFileName());
				String tempExt = getDocumentExtension(pj.getFileName());
				if(name.equals(tempName) && ext.equals(tempExt)){
					totalCopies++;
				}
			}
			if(totalCopies != 0 ) {
				fileName =  name + DOCUMENT_DUPLICATE_NAME_SEPERATOR  + totalCopies;
				if(ext.length() > 0){// if Extension exists
					fileName =  fileName + DOCUMENT_EXTENSION + ext;	
				} 
			}
		}
		return fileName;
	}
}
