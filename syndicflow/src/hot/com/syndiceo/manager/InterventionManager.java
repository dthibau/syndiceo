package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
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
import com.syndiceo.dto.ConseilDTO;
import com.syndiceo.dto.InterventionDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.Lu;
import com.syndiceo.model.Model;
import com.syndiceo.model.Planification;
import com.syndiceo.model.SousSpecialite;
import com.syndiceo.model.Specialite;
import com.syndiceo.model.dao.AccountDao;
import com.syndiceo.model.dao.InterventionDao;
import com.syndiceo.model.dao.LuDao;
import com.syndiceo.model.dao.SpecialiteDao;
import com.syndiceo.proc.TaskButtonConf;
import com.syndiceo.proc.TaskConf;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.Task;
import com.syndiceo.service.NotificationService;
import com.syndiceo.service.TaskService;
import com.syndiceo.util.PdfGenerator;
import com.syndiceo.util.ResponseHandler;

@Name("interventionManager")
@Scope(ScopeType.CONVERSATION)
public class InterventionManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5167663820704348071L;

	@Out(required = false)
	InterventionDTO interventionDTO;

	@In
	EntityManager entityManager;
	@In(required = false)
	FacesContext facesContext;
	@In
	FacesMessages facesMessages;

	@In(create = true)
	TaskService taskService;

	@In("#{interventionDao}")
	InterventionDao interventionDao;
	
	@In("#{accountDao}")
	AccountDao accountDao;

	@In(create = true)
	NotificationService notificationService;

	@In(create = true)
	DemandeManager demandeManager;
	@In(create = true)
	DocumentHelper documentHelper;

	@In
	Account loggedUser;

	@In
	Map<String, TaskConf> taskConf;

	Task currentTask;
	TaskButtonConf currentTaskButtonConf;

	Boolean history, form;

	private List<SousSpecialite> sousSpecialites;
	private Specialite selectedSpecialite = new Specialite();

	private Planification newPlanification;

	List<ConseilDTO> conseils;
	List<Account> conseilsAccounts;
	
	List<Account> oldConseils;
	String oldInfo1,oldInfo2,oldInfo3;
	@RequestParameter
	String dId;
	@Logger
	Log log;

	@Begin(join = true)
	public String select() {
		Intervention intervention = interventionDao.fullLoad(Long
				.parseLong(dId));
		new LuDao(entityManager).set(loggedUser,intervention);
		
		this.interventionDTO = (InterventionDTO) taskService
				.getInterventionDTO(loggedUser, intervention);

		newPlanification = new Planification();
		newPlanification.setIntervention(interventionDTO.getIntervention());
		if (form == null) {
			form = interventionDTO.getMode() == Application.M_EDIT_DEMANDE;

			history = false;
		}
		demandeManager.setDemandeDTO(interventionDTO);
		documentHelper.setCreateIntervention(false);
		return "/workspace/intervention/intervention.xhtml";
	}

	@Begin(join = true)
	public String select(InterventionDTO interventionDTO) {
		entityManager.clear();
		Intervention intervention = interventionDao.fullLoad(interventionDTO.getId());
		new LuDao(entityManager).set(loggedUser,intervention);
		this.interventionDTO = (InterventionDTO) taskService
				.getInterventionDTO(loggedUser, intervention);
		this.interventionDTO.setIntervention(interventionDao
				.fullLoad(interventionDTO.getId()));

		newPlanification = new Planification();
		newPlanification.setIntervention(interventionDTO.getIntervention());

		if (form == null) {
			form = this.interventionDTO.getMode() == Application.M_EDIT_DEMANDE;
			history = false;
		}
		demandeManager.setDemandeDTO(this.interventionDTO);
		return "/workspace/intervention/intervention.xhtml";
	}

	@RaiseEvent("demandesUpdated")
	public String completeTask() {
		if (demandeManager.getCurrentTaskButtonConf().getDialogForm() == Application.DF_PLANIFICATION) {
			demandeManager.getEvent().setComment(_buildPlanificationComment(newPlanification));
			interventionDTO.getIntervention().addPlanification(newPlanification);
			

		}
		if (demandeManager.getCurrentTaskButtonConf().getDialogForm() == Application.DF_UPDATE_DEST) {
			_updateConseils();
			

		}
		demandeManager.completeTask();

		Hibernate.initialize(interventionDTO.getIntervention()
				.getPlanifications());
		return select(interventionDTO);
	}

	public void doStuff(InterventionDTO interventionDTO,
			TaskButtonConf buttonConf) {
		select(interventionDTO);
		demandeManager.doStuff(buttonConf);
	}

	public void doStuff(InterventionDTO interventionDTO, DirectTask directTask) {
		select(interventionDTO);
		demandeManager.doStuff(directTask);
	}

	public void doMsg() {
		Intervention intervention = interventionDao.fullLoad(Long
				.parseLong(dId));
		
		this.interventionDTO = (InterventionDTO) taskService
				.getInterventionDTO(loggedUser, intervention);
		doMsg(interventionDTO);
	}
	
	public void doMsg(InterventionDTO interventionDTO) {
		select(interventionDTO);
		demandeManager.prepareComplete(null, taskConf.get("MSG")
				.getButtonConfs()[0]);
	}
	public void doUpdateDest() {
		if ( dId != null && dId.length() > 0 ) {
			Intervention intervention = interventionDao.fullLoad(Long
					.parseLong(dId));
			
			this.interventionDTO = (InterventionDTO) taskService
					.getInterventionDTO(loggedUser, intervention);
			doUpdateDest(interventionDTO);
		} else {
			demandeManager.prepareComplete(null, taskConf.get("UPDATE_DEST").getButtonConfs()[0]);
		}
		
	}
	public void doUpdateDest(InterventionDTO interventionDTO) {
		select(interventionDTO);
		demandeManager.prepareComplete(null, taskConf.get("UPDATE_DEST").getButtonConfs()[0]);
	}
	public void servePDF() {
		String outputFileName = interventionDTO.getId() + ".pdf";

		ResponseHandler response = new ResponseHandler(facesContext
				.getExternalContext().getResponse());
		try {

			byte[] pdf = PdfGenerator.generateDemandePDF(
					demandeManager.getServerUrl(facesContext), interventionDTO);

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

	public List<SousSpecialite> getSousSpecialites() {
		if (sousSpecialites == null
				|| !selectedSpecialite.equals(interventionDTO.getSpecialite())) {
			sousSpecialites = new ArrayList<SousSpecialite>();
			if (interventionDTO.getSpecialite() != null) {
				sousSpecialites = new SpecialiteDao(entityManager)
						.getSousSpecialites(interventionDTO.getSpecialite());
				selectedSpecialite = interventionDTO.getSpecialite();
			}
		}
		return sousSpecialites;
	}

	public Planification getNewPlanification() {
		return newPlanification;
	}

	public void setNewPlanification(Planification newPlanification) {
		this.newPlanification = newPlanification;
	}

	public Planification getLastPlanification() {
		if (interventionDTO != null)
			return interventionDTO.getIntervention().getLastPlanification();
		return null;
	}

	public void updateDemande(String field) {
		facesMessages.add(Severity.INFO, Labels.get("ack.updated"));
		
		if (loggedUser.is(Application.CONSEIL_ROLE.getId())
				&& (interventionDTO.getCurrentTask().getCode()
						.equals("COMPLETE") || interventionDTO.getCurrentTask()
						.getCode().equals("UPDATE"))) {
			demandeManager.recordUpdate(field);
		}
		if (field.equals(Labels.get("intervention.field.specialite"))) {
			sousSpecialites = null;
			interventionDTO.getIntervention().setSousSpecialite(null);
		}
		if (field.equals(Labels.get("intervention.field.conseils"))) {
			_updateConseils();
		}
	}

	public List<ConseilDTO> getConseils() {
		if (conseils == null && interventionDTO != null) {
			conseils = new ArrayList<ConseilDTO>();

				List<Account> accounts = accountDao.getContactsByRole(
						interventionDTO.getImmeuble(), Model.CONSEIL_ROLE);
				for (Account a : accounts) {
					if ( !a.equals(interventionDTO.getDemandeur()) ) {
						if ( interventionDTO.getConseils().contains(a) ) {
							conseils.add(new ConseilDTO(a,true));
						} else {
							conseils.add(new ConseilDTO(a,false));
						}
					}
				}

		}
		return conseils;
	}

	public void setConseils(List<ConseilDTO> conseils) {
		this.conseils = conseils;
	}
	
	public List<Account> getConseilsAccounts() {
		if (conseilsAccounts == null && interventionDTO != null) {
		conseilsAccounts = accountDao.getContactsByRole(
						interventionDTO.getImmeuble(), Model.CONSEIL_ROLE);
				
		}
		return conseilsAccounts;
	}

	private void _updateConseils() {
		for ( ConseilDTO cDto : getConseils() ) {
			if ( interventionDTO.getIntervention().getConseils().contains(cDto.getAccount()) &&
					!cDto.isSelected() ) {
				interventionDTO.getIntervention().getConseils().remove(cDto.getAccount());
			}
			if ( !interventionDTO.getIntervention().getConseils().contains(cDto.getAccount()) &&
					cDto.isSelected() ) {
				interventionDTO.getIntervention().getConseils().add(cDto.getAccount());
			}
		}
	}
	
	private String _buildPlanificationComment(Planification planification) {
		StringBuffer sbf = new StringBuffer(Labels.get("event.planification.comment") + " : " + Application.dateFormat.format(planification.getDate()) + "\n");

		if ( planification.getHoraire() != null ) {
			sbf.append(Labels.get("intervention.field.planification.horaire")).append(" : ").append(planification.getHoraire().toString()).append("\n");
		}
		if ( planification.getHoraireLibre() != null ) {
			sbf.append(Labels.get("intervention.field.planification.horaire")).append(" : ").append(planification.getHoraireLibre()).append("\n");
		}
		if ( planification.getPrestataire() != null ) {
			sbf.append(Labels.get("intervention.field.planification.prestataire")).append(" : ").append(planification.getPrestataire()).append("\n");
		}
		return sbf.toString();
	}
}
