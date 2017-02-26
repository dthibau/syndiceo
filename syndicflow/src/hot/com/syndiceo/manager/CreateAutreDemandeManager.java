package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;

import com.syndiceo.Application;
import com.syndiceo.Labels;
import com.syndiceo.TaskButtonConfHelper;
import com.syndiceo.dto.AutreDemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Event;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.SousCritere;
import com.syndiceo.model.dao.CritereDao;
import com.syndiceo.proc.ProcessListener;
import com.syndiceo.proc.TaskButtonConf;
import com.syndiceo.service.NotificationService;
import com.syndiceo.service.TaskService;

@Name("createAutreDemandeManager")
@Scope(ScopeType.CONVERSATION)
public class CreateAutreDemandeManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1213233854910542503L;

	@In
	Account loggedUser;


	@In
	FacesContext facesContext;

	@In
	FacesMessages facesMessages;

	@In(create = true)
	TaskService taskService;

	@In(create = true)
	NotificationService notificationService;
	
	@In(create = true)
	DocumentHelper documentHelper;

	@Out
	AutreDemande autreDemande;

	@In
	EntityManager entityManager;


	@Begin(join = true)
	@Create
	public String init() {
		autreDemande = new AutreDemande();
		autreDemande.setDemandeur(loggedUser);
		if ( loggedUser.getImmeubles().size() == 1 ) {
			autreDemande.setImmeuble((Immeuble)loggedUser.getImmeubles().toArray()[0]);
		}	
		documentHelper.setCreateAutreDemande(true);
		return "/workspace/autreDemande/create.xhtml";
	}


	@RaiseEvent("autreDemandesUpdated")
	public String startProcess(String typeDemande) {


			long processInstanceId = taskService.startProcess(loggedUser, autreDemande);
			autreDemande.setProcessInstanceId(processInstanceId);
			autreDemande.setStatusCode(ProcessListener.getInstance().getStateCode());
			autreDemande.setCreatedDate(new Date());
			int lastIntervention = autreDemande.getImmeuble().getNoIntervention();
			autreDemande.setNoIntervention(lastIntervention+1);
			autreDemande.getImmeuble().setNoIntervention(lastIntervention+1);

			entityManager.merge(autreDemande.getImmeuble());
			entityManager.persist(autreDemande);


			// Add event
			
			TaskButtonConf buttonConf = Application.taskConf.get("AUTRE_DEPOT")
					.getButtonConfs()[0];

			Event event = new Event();
			event.setComment(buttonConf.getDefaultComment());
			event.setActor(loggedUser);
			event.setTaskCode("AUTRE_DEPOT");
			event.setTimestamp(new Date());
			event.setDemande(autreDemande);
			// Update event.destinataires (may have been entered by user)
			notificationService.resolveDestinataires(loggedUser,
					TaskButtonConfHelper.getNotificationCodes(buttonConf),
					new AutreDemandeDTO(loggedUser, autreDemande), event);

			notificationService.sendMail(1000, Application.getServerUrl(facesContext),
					autreDemande, event);
			autreDemande.addEvent(event);

			facesMessages.add(Severity.INFO, Labels.get("ack.demandeSent"));
			return "/workspace/autreDemandes.xhtml";
		
	}

	public List<SousCritere> getSousCriteres() {
		List<SousCritere> sousCriteres = new ArrayList<SousCritere>();
		if ( autreDemande.getCritere() != null ) {
			sousCriteres = new CritereDao(entityManager).getSousCriteres(autreDemande.getCritere());
		}
		return sousCriteres;
	}
	


	
}
