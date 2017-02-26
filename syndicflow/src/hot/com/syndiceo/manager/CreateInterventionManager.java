package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
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
import com.syndiceo.dto.ConseilDTO;
import com.syndiceo.dto.InterventionDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Event;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.Model;
import com.syndiceo.model.SousSpecialite;
import com.syndiceo.model.dao.AccountDao;
import com.syndiceo.model.dao.SpecialiteDao;
import com.syndiceo.proc.ProcessListener;
import com.syndiceo.proc.TaskButtonConf;
import com.syndiceo.service.NotificationService;
import com.syndiceo.service.TaskService;

@Name("createInterventionManager")
@Scope(ScopeType.CONVERSATION)
public class CreateInterventionManager implements Serializable {

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
	Intervention intervention;

	@In
	EntityManager entityManager;
	AccountDao accountDao;
	List<ConseilDTO> conseils;

	@Begin(join = true)
	@Create
	public String init() {
		intervention = new Intervention();
		intervention.setDemandeur(loggedUser);
		if (loggedUser.getImmeublesActeur().size() == 1) {
			intervention.setImmeuble((Immeuble) loggedUser.getImmeublesActeur()
					.toArray()[0]);
		}
		accountDao = new AccountDao(entityManager);
		documentHelper.setCreateIntervention(true);

		return "/workspace/intervention/create.xhtml";
	}

	@RaiseEvent("demandesUpdated")
	public String startProcess(String typeDemande) {


			long processInstanceId = taskService.startProcess(loggedUser, intervention);
			intervention.setProcessInstanceId(processInstanceId);
			intervention.setStatusCode(ProcessListener.getInstance().getStateCode());
			intervention.setCreatedDate(new Date());
			int lastIntervention = intervention.getImmeuble().getNoIntervention();
			intervention.setNoIntervention(lastIntervention+1);
			intervention.getImmeuble().setNoIntervention(lastIntervention+1);
			if ( intervention.getContactDemandeur() && (loggedUser.getEmail() == null || loggedUser.getTelephone() == null)) {
				if ( loggedUser.getEmail() == null ) {
					loggedUser.setEmail(intervention.getContactEmail());
				}
				if ( loggedUser.getTelephone() == null ) {
					loggedUser.setTelephone(intervention.getContactTelephone());
				}
				entityManager.merge(loggedUser);
			}
			for ( ConseilDTO c : conseils ) {
				if ( c.isSelected() ) {
					intervention.addConseil(c.getAccount());
				}
			}
			entityManager.merge(intervention.getImmeuble());
			entityManager.persist(intervention);


			// Add event
			
			TaskButtonConf buttonConf = Application.taskConf.get("DEPOT")
					.getButtonConfs()[0];

			Event event = new Event();
			event.setComment(buttonConf.getDefaultComment());
			event.setActor(loggedUser);
			event.setTaskCode("DEPOT");
			event.setTimestamp(new Date());
			event.setDemande(intervention);
			// Update event.destinataires (may have been entered by user)
			notificationService.resolveDestinataires(loggedUser,
					TaskButtonConfHelper.getNotificationCodes(buttonConf),
					new InterventionDTO(loggedUser, intervention), event);

			Hibernate.initialize(intervention.getPlanifications());
			notificationService.sendMail(1000, Application.getServerUrl(facesContext),
					intervention, event);
			intervention.addEvent(event);

			facesMessages.add(Severity.INFO, Labels.get("ack.demandeSent"));
			return "/workspace/interventions.xhtml";
		
	}

	public List<SousSpecialite> getSousSpecialites() {
		List<SousSpecialite> sousSpecialites = new ArrayList<SousSpecialite>();
		if (intervention.getSpecialite() != null) {
			sousSpecialites = new SpecialiteDao(entityManager)
					.getSousSpecialites(intervention.getSpecialite());
		}
		return sousSpecialites;
	}

	public void changeImmeuble() {
		conseils = null;
	}

	public List<ConseilDTO> getConseils() {
		if (conseils == null) {
			conseils = new ArrayList<ConseilDTO>();
			if (intervention.getImmeuble() != null) {
				List<Account> accounts = accountDao.getContactsByRole(
						intervention.getImmeuble(), Model.CONSEIL_ROLE);
				for (Account a : accounts) {
					if ( !a.equals(loggedUser) ) {
						conseils.add(new ConseilDTO(a));
					}
				}
			}
		}
		return conseils;
	}

	public void setConseils(List<ConseilDTO> conseils) {
		this.conseils = conseils;
	}

}
