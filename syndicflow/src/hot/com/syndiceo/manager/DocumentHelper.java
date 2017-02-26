package com.syndiceo.manager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.MissingResourceException;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import com.syndiceo.Application;
import com.syndiceo.Labels;
import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Event;
import com.syndiceo.model.Fichier;
import com.syndiceo.model.FichierEvent;
import com.syndiceo.model.PieceJointe;

@Name("documentHelper")
@Scope(ScopeType.CONVERSATION)
public class DocumentHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 866462111132214029L;

	@In
	FacesContext facesContext;
	
	@In
	FacesMessages facesMessages;

	@In
	EntityManager entityManager;

	@In
	Account loggedUser;

	@RequestParameter
	String docId;
	
	@Logger
	Log log;
	
	@In(required=false)
	Demande intervention;
	
	@In(required=false)
	DemandeDTO interventionDTO;
	
	@In(required=false)
	Demande autreDemande;
	
	@In(required=false)
	DemandeDTO autreDemandeDTO;
	
	@In(required=false)
	Event event;
	
	private boolean createIntervention=false;
	private boolean createAutreDemande=false;

	public void fileUploadListener(FileUploadEvent event) {

		Demande demande = _getDemande();

		try {
			PieceJointe pj = (PieceJointe) createFromFileUpload(
					event, new PieceJointe());
			pj.setDemande(demande);
			pj.setFileName(pj.getDuplicateName(demande.getPiecesJointes()));

			demande.addPieceJointe(pj);

			facesMessages.add(Severity.INFO, Labels.get("ack.documentAdded"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeDocument(PieceJointe pieceJointe) {
		
		Demande demande = _getDemande();

		demande.removePieceJointe(pieceJointe);
		if (entityManager.contains(pieceJointe)) {
			entityManager.remove(pieceJointe);
		}

		facesMessages.add(Severity.INFO, Labels.get("ack.documentRemoved"));

	}

	public void removeAllDocuments() {
		
		Demande demande = _getDemande();

		for (PieceJointe pj : demande.getPiecesJointes()) {
			if (entityManager.contains(pj)) {
				entityManager.remove(pj);
			} else if (pj.getId() > 0) {
				pj = entityManager.merge(pj);
				entityManager.remove(pj);
			}
		}

		demande.setPiecesJointes(new ArrayList<PieceJointe>());


		facesMessages.add(Severity.INFO, Labels.get("ack.documentsRemoveAll"));

	}
	
	public void addFichier(FileUploadEvent fileUploadEvent) {
	

		try {
			FichierEvent fichier = (FichierEvent) createFromFileUpload(
					fileUploadEvent, new FichierEvent());


			event.addFichier(fichier);

			facesMessages.add(Severity.INFO, Labels.get("ack.documentAdded"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeFichier(FichierEvent fichier) {
		

		event.removeFichier(fichier);
		if (entityManager.contains(fichier)) {
			entityManager.remove(fichier);
		}

		facesMessages.add(Severity.INFO, Labels.get("ack.documentRemoved"));

	}
	
	public Fichier createFromFileUpload(FileUploadEvent event, Fichier document) {
		UploadedFile upi = event.getUploadedFile();
		String fileName = upi.getName();
		byte[] data = upi.getData();
		document.setData(data);
		document.setFileName(fileName);
		document.setContentType(findContentTypeByFileName(fileName));
		document.setUploadDate(new Date());

		return document;
	}



	public String findContentTypeByFileName(String fileName) {
		String ct = "text/plain";
		int i = fileName.lastIndexOf('.');
		if (i != -1) {
			String ext = fileName.substring(i + 1);

			try {
			ct = Application.mimeTypesBundle.getString(ext.toLowerCase());
			} catch (MissingResourceException e ) {
				log.info("Unknown file extension found :"+ext);
			}
		}

		return ct;

	}

	public void download() {
		if ( docId == null || docId.length() == 0 ) {
			String[] tokens = facesContext.getExternalContext()
			.getRequestServletPath().split("/");
			docId = tokens[3].substring(0, tokens[3].indexOf("."));
		}
		PieceJointe document = entityManager.find(PieceJointe.class, Integer
				.parseInt(docId));
		download(document);
	}

	/**
	 * 
	 * @param facesContext
	 * @param document
	 */
	public void download(Fichier document) {

		if (document == null) {
			document = entityManager.find(Fichier.class, Integer
					.parseInt(docId));
		}
		if (!facesContext.getResponseComplete() && document != null) {

			HttpServletResponse response = (HttpServletResponse) facesContext
					.getExternalContext().getResponse();
			response.setContentType(document.getContentType());
			response.setContentLength(document.getData() != null ? document
					.getData().length : 0);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ document.getFileName() + "\"");
			response.setHeader("Cache-Control", "private"); // Patch IE6 bug
			ServletOutputStream out;
			try {
				out = response.getOutputStream();
				out.write(document.getData());
				out.flush();
			} catch (IOException e) {
				// TODO: error here
			}
			facesContext.responseComplete();

		}
	}
	
	private Demande _getDemande() {
		if ( (intervention != null && intervention.getId() != null) || createIntervention ) {
			return intervention;
		}
		if ( (autreDemande != null && autreDemande.getId() != null) || createAutreDemande ) {
			return autreDemande;
		}
		if ( interventionDTO != null && interventionDTO.getId() != 0) {
			return interventionDTO.getDemande();
		}
		if ( autreDemandeDTO != null && autreDemandeDTO.getId() != 0) {
			return autreDemandeDTO.getDemande();
		}
		return null;
	}

	public boolean isCreateIntervention() {
		return createIntervention;
	}

	public void setCreateIntervention(boolean createIntervention) {
		this.createIntervention = createIntervention;
	}

	public boolean isCreateAutreDemande() {
		return createAutreDemande;
	}

	public void setCreateAutreDemande(boolean createAutreDemande) {
		this.createAutreDemande = createAutreDemande;
	}
	
	

}
