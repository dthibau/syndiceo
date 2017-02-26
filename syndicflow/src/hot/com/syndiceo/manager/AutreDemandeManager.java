package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.syndiceo.Application;
import com.syndiceo.Labels;
import com.syndiceo.dto.AutreDemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Critere;
import com.syndiceo.model.Event;
import com.syndiceo.model.SousCritere;
import com.syndiceo.model.dao.AutreDemandeDao;
import com.syndiceo.model.dao.CritereDao;
import com.syndiceo.model.dao.LuDao;
import com.syndiceo.proc.TaskButtonConf;
import com.syndiceo.proc.TaskConf;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.Task;
import com.syndiceo.service.NotificationService;
import com.syndiceo.service.TaskService;
import com.syndiceo.util.PdfGenerator;
import com.syndiceo.util.ResponseHandler;

@Name("autreDemandeManager")
@Scope(ScopeType.CONVERSATION)
public class AutreDemandeManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5167663820704348071L;

	@Out(required = false)
	AutreDemandeDTO autreDemandeDTO;

	@In
	EntityManager entityManager;
	@In(required = false)
	FacesContext facesContext;
	@In
	FacesMessages facesMessages;

	@In(create = true)
	TaskService taskService;

	@In("#{autreDemandeDao}")
	AutreDemandeDao autreDemandeDao;

	@In(create = true)
	NotificationService notificationService;

	@In(create = true)
	DemandeManager demandeManager;
	
	@In
	Account loggedUser;

	@In
	Map<String, TaskConf> taskConf;

	@In(create = true)
	DocumentHelper documentHelper;

	Task currentTask;
	TaskButtonConf currentTaskButtonConf;
	Event event;

	Boolean history, form;

	private List<SousCritere> sousCriteres;
	private Critere selectedCritere = new Critere();
	
	@RequestParameter
	String dId;
	@Logger
	Log log;

	@Begin(join = true)
	public String select() {
		AutreDemande autreDemande = entityManager.find(AutreDemande.class,Long
				.parseLong(dId));
		new LuDao(entityManager).set(loggedUser,autreDemande);
		this.autreDemandeDTO = (AutreDemandeDTO)taskService.getAutreDemandeDTO(loggedUser,
				autreDemande);
;
		if (form == null) {
			form = autreDemandeDTO.getMode() == Application.M_EDIT_DEMANDE;

			history = false;
		}
		demandeManager.setDemandeDTO(autreDemandeDTO);
		documentHelper.setCreateAutreDemande(false);
		return "/workspace/autreDemande/autreDemande.xhtml";
	}

	@Begin(join = true)
	public String select(AutreDemandeDTO autreDemande) {
		entityManager.clear();
		new LuDao(entityManager).set(loggedUser,autreDemande.getDemande());
		this.autreDemandeDTO = (AutreDemandeDTO)taskService.getAutreDemandeDTO(loggedUser,
				autreDemande.getAutreDemande());
		this.autreDemandeDTO.setAutreDemande(autreDemandeDao
				.fullLoad(autreDemande.getAutreDemande().getId()));


		if (form == null) {
			form = autreDemandeDTO.getMode() == Application.M_EDIT_DEMANDE;
			history = false;
		}
		demandeManager.setDemandeDTO(autreDemandeDTO);
		documentHelper.setCreateAutreDemande(false);
		return "/workspace/autreDemande/autreDemande.xhtml";
	}




	@RaiseEvent("demandesUpdated")
	public String completeTask() {
		demandeManager.completeTask();		
		return select(autreDemandeDTO);
	}



	public void doStuff(AutreDemandeDTO autreDemandeDTO, TaskButtonConf buttonConf) {
		select(autreDemandeDTO);
		demandeManager.doStuff(buttonConf);
	}

	public void doStuff(AutreDemandeDTO autreDemandeDTO, DirectTask directTask) {
		select(autreDemandeDTO);
		demandeManager.doStuff(directTask);
	}


	public void doMsg(AutreDemandeDTO autreDemandeDTO) {
		select(autreDemandeDTO);
		demandeManager.prepareComplete(null, taskConf.get("MSG").getButtonConfs()[0]);
	}


	public void servePDF() {
		String outputFileName = autreDemandeDTO.getId() + ".pdf";

		ResponseHandler response = new ResponseHandler(facesContext
				.getExternalContext().getResponse());
		try {

			byte[] pdf = PdfGenerator.generateDemandePDF(
					demandeManager.getServerUrl(facesContext), autreDemandeDTO);

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ outputFileName + "\"");
			response.setHeader("Cache-Control", "private"); // Patch IE6 bug
			response.setHeader("Cache-Control", "public"); // Patch IE6 bug for
															// https
			response.setHeader("Pragma", ""); // Patch IE6 bug for https
			response.setHeader("Expires", ""); // Patch IE6 bug for https
			response.setContentLength(pdf.length);
			response.getOutputStream().write(pdf);

		} catch (Exception e) {
			e.printStackTrace();
		}

		facesContext.responseComplete();

	}


	public List<SousCritere> getSousCriteres() {
		if (sousCriteres == null
				|| !selectedCritere.equals(autreDemandeDTO.getCritere())) {
			sousCriteres = new ArrayList<SousCritere>();
			if (autreDemandeDTO.getCritere() != null) {
				sousCriteres = new CritereDao(entityManager)
						.getSousCriteres(autreDemandeDTO.getCritere());
				selectedCritere = autreDemandeDTO.getCritere();
			}
		}
		return sousCriteres;
	}

	
	public void updateDemande(String field) {
		facesMessages.add(Severity.INFO, Labels.get("ack.updated"));
		// Envoi des modifications du demandeur à l'UT
		if ( loggedUser.is(Application.CONSEIL_ROLE.getId()) && (autreDemandeDTO.getCurrentTask().getCode().equals("COMPLETE") ||
				autreDemandeDTO.getCurrentTask().getCode().equals("UPDATE")) ) {
			demandeManager.recordUpdate(field);
		}
		if ( field.equals(Labels.get("intervention.field.specialite")) ) {
			sousCriteres = null;
			autreDemandeDTO.getAutreDemande().setSousCritere(null);
		}
	}
		

}
