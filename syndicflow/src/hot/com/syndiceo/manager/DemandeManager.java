package com.syndiceo.manager;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.syndiceo.Application;
import com.syndiceo.Labels;
import com.syndiceo.TaskButtonConfHelper;
import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Event;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.dao.LuDao;
import com.syndiceo.proc.ProcessListener;
import com.syndiceo.proc.TaskButtonConf;
import com.syndiceo.proc.TaskConf;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTaskWithMessage;
import com.syndiceo.proc.model.Task;
import com.syndiceo.proc.model.TaskResult;
import com.syndiceo.service.NotificationService;
import com.syndiceo.service.TaskService;
import com.syndiceo.util.RequestHandler;

@Name("demandeManager")
@Scope(ScopeType.CONVERSATION)
public class DemandeManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5167663820704348071L;

	@Out(required=false)
	DemandeDTO demandeDTO;
	
	@In
	EntityManager entityManager;
	@In(required = false)
	FacesContext facesContext;
	@In
	FacesMessages facesMessages;

	@In
	Application application;
	
	@In(create = true)
	TaskService taskService;

	@In(create = true)
	NotificationService notificationService;

	@In
	Account loggedUser;

	@In
	Map<String, TaskConf> taskConf;

	Task currentTask;
	TaskButtonConf currentTaskButtonConf;
	@Out(required=false)
	Event event;

	Boolean history, form;


	private TaskResult taskResult;
	
	@RequestParameter
	String dId;
	@RequestParameter
	String labelCode;
	@Logger
	Log log;


	private void _completeTask(Task task, Map<String, Object> results) {
		taskService.completeTask(loggedUser, results,
				demandeDTO.getProcessInstanceId(), task);
		if (ProcessListener.getInstance().getStateCode() != null) {
			demandeDTO.setStatusCode(ProcessListener.getInstance()
					.getStateCode());
		}
		if (ProcessListener.getInstance().hasProcessCompleted(demandeDTO.getProcessInstanceId()) ){
			demandeDTO.getDemande().setArchivedDate(new Date());
		}

	}

	public void completeTask() {
		
		if (currentTaskButtonConf.getIssue() == Application.T_COMPLETE) {
			Map<String,Object> results = taskResult.merge(currentTaskButtonConf.getResults(),demandeDTO.getDemande());
			_completeTask(currentTask, results);
		}
		demandeDTO = taskService.getDemandeDTO(loggedUser,demandeDTO.getDemande());
		// Add event
		event.setTaskCode(currentTaskButtonConf.getLabelCode());
		event.setTimestamp(new Date());
		
		// Update event.destinataires (may have been entered by user)
		notificationService
				.resolveDestinataires(loggedUser,TaskButtonConfHelper
						.getNotificationCodes(currentTaskButtonConf),
						demandeDTO, event);

		notificationService.sendMail(1000, getServerUrl(facesContext),
				demandeDTO.getDemande(), event);
		if ( !entityManager.contains(demandeDTO.getDemande()) ) {
			demandeDTO.setDemande(entityManager.merge(demandeDTO.getDemande()));
		}
		demandeDTO.getDemande().addEvent(event);
	
		if (currentTaskButtonConf.getIssue() == Application.T_MSG
				&& currentTask != null) {
			((LongTaskWithMessage) currentTask).addParticipants(event
					.getDestinataires());
		}

		if (currentTaskButtonConf.getIssue() == Application.T_COMPLETE) {
			facesMessages.add(Severity.INFO, Labels.get("ack.taskCompleted"));
		} else if (currentTaskButtonConf.getIssue() == Application.T_MSG) {
			facesMessages.add(Severity.INFO, Labels.get("ack.msgSent"));
		} else if (currentTaskButtonConf.getIssue() == Application.T_UPDATE_DEST) {
			facesMessages.add(Severity.INFO, Labels.get("ack.destUpdated"));
		}
		form = null;
		log.info("Complete task created new Event : " + event);
		new LuDao(entityManager).set(loggedUser,demandeDTO.getDemande());
		entityManager.flush();

	}

	public void doStuff() {
		doStuff(application.getButtonConf(labelCode));
	}
	public void doStuff(TaskButtonConf buttonConf) {
		switch (buttonConf.getIssue()) {
		case Application.T_SAVE:
			break;
		case Application.T_MSG:
			_prepareMsg(demandeDTO.getCurrentTask(), buttonConf);
			break;
		case Application.T_COMPLETE:
			prepareComplete(demandeDTO.getCurrentTask(), buttonConf);
			break;
		}
	}



	public void doStuff(DirectTask task) {
		prepareComplete(task, taskConf.get(task.getCode()).getButtonConfs()[0]);
	}


	public void doMsg() {
		prepareComplete(null, taskConf.get("MSG").getButtonConfs()[0]);
	}
	
	public void doUpdateDest() {
		prepareComplete(null, taskConf.get("UPDATE_DEST").getButtonConfs()[0]);
	}

	private void _prepareMsg(Task task, TaskButtonConf buttonConf) {
		currentTask = entityManager.find(Task.class, task.getId());
		currentTaskButtonConf = buttonConf;
		// taskResult = new TaskResult(demandeDTO.getDemande());
		event = new Event();
		event.setComment(buttonConf.getDefaultComment());
		event.setActor(loggedUser);
		event.setDemande(demandeDTO.getDemande());
		// Update event.destinataires
		notificationService
				.resolveDestinataires(loggedUser, TaskButtonConfHelper
						.getNotificationCodes(currentTaskButtonConf),
						demandeDTO, event);
	}

	protected void prepareComplete(Task task, TaskButtonConf buttonConf) {
		currentTask = task;
		currentTaskButtonConf = buttonConf;
		taskResult = new TaskResult(demandeDTO.getDemande());
		event = new Event();
		if ( buttonConf.getDefaultComment() != null ) {
			event.setComment(MessageFormat.format(buttonConf.getDefaultComment(),demandeDTO.getNumero(),demandeDTO.getTitre(), new SimpleDateFormat(Labels.get("syndicflow.dateformat")).format(demandeDTO.getCreatedDate())));
		}
		event.setActor(loggedUser);
		event.setDemande(demandeDTO.getDemande());
		// Update event.destinataires
		notificationService
				.resolveDestinataires(loggedUser, TaskButtonConfHelper
						.getNotificationCodes(currentTaskButtonConf),
						demandeDTO, event);

	}

	public TaskButtonConf getCurrentTaskButtonConf() {
		return currentTaskButtonConf;
	}

	public void setCurrentTaskButtonConf(TaskButtonConf currentTaskButtonConf) {
		this.currentTaskButtonConf = currentTaskButtonConf;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}


	public String getServerUrl(FacesContext facesContext) {
		RequestHandler request = new RequestHandler(facesContext
				.getExternalContext().getRequest());
		URI server = null;
		try {
			server = request.getURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return server == null ? "" : server.toString();
	}

	public boolean isForm() {
		return form != null && form;
	}

	public void showForm() {
		this.history = false;
		this.form = true;
	}

	public boolean isInfo() {
		return form == null || !form;
	}

	public void showInfo() {
		this.history = false;
		this.form = false;
	}

	public boolean isHistory() {
		return history != null && history;
	}

	public void showHistory() {
		this.history = true;
		this.form = false;
	}


	public String getEventDestinatairesAsString() {
		StringBuffer ret = new StringBuffer();
		if (event != null) {
			boolean first = true;
			for (Account a : event.getDestinataires()) {
				if (!a.equals(event.getActor())) {
					if (first) {
						ret.append(a.getNomComplet());
						first = false;
					} else {
						ret.append(", " + a.getNomComplet());
					}
				}
			}
			for (Account a : event.getPourInfo()) {
				if (!a.equals(event.getActor())) {
					if (first) {
						ret.append(a.getNomComplet());
						first = false;
					} else {
						ret.append(", " + a.getNomComplet());
					}
				}
			}
			if ( event.getAutreEmails() != null && event.getAutreEmails().length() > 0 ) {
				if (first) {
					ret.append(event.getAutreEmails());
					first = false;
				} else {
					ret.append(", " + event.getAutreEmails());
				}
			}
//			if ( event.getDemande() instanceof Intervention ) {
//				Intervention intervention = (Intervention)event.getDemande();
//				if ( intervention.getPourInfo1() != null && intervention.getPourInfo1().length() > 0) {
//					if (first) {
//						ret.append(intervention.getPourInfo1());
//						first = false;
//					} else {
//						ret.append(", " + intervention.getPourInfo1());
//					}
//				}
//				if ( intervention.getPourInfo2() != null && intervention.getPourInfo2().length() > 0) {
//					if (first) {
//						ret.append(intervention.getPourInfo2());
//						first = false;
//					} else {
//						ret.append(", " + intervention.getPourInfo2());
//					}
//				}
//				if ( intervention.getPourInfo3() != null && intervention.getPourInfo3().length() > 0) {
//					if (first) {
//						ret.append(intervention.getPourInfo3());
//						first = false;
//					} else {
//						ret.append(", " + intervention.getPourInfo3());
//					}
//				}
//			}
		}

		return ret.toString();
	}

	public String getEventDestinatairesAsString(Event event) {
		if (event == null) {
			return "";
		}
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for (Account a : event.getDestinataires()) {
			if (!a.equals(event.getActor())) {
				if (first) {
					ret.append(a.getNomComplet());
					first = false;
				} else {
					ret.append(", " + a.getNomComplet());
				}
			}
		}
		for (Account a : event.getPourInfo()) {
			if (!a.equals(event.getActor())) {
				if (first) {
					ret.append(a.getNomComplet());
					first = false;
				} else {
					ret.append(", " + a.getNomComplet());
				}
			}
		}

		if ( event.getDemande() instanceof Intervention ) {
			Intervention intervention = (Intervention)event.getDemande();
			if ( intervention.getPourInfo1() != null && intervention.getPourInfo1().length() > 0) {
				if (first) {
					ret.append(intervention.getPourInfo1());
					first = false;
				} else {
					ret.append(", " + intervention.getPourInfo1());
				}
			}
			
			if ( intervention.getPourInfo2() != null && intervention.getPourInfo2().length() > 0) {
				if (first) {
					ret.append(intervention.getPourInfo2());
					first = false;
				} else {
					ret.append(", " + intervention.getPourInfo2());
				}
			}
			if ( intervention.getPourInfo3() != null && intervention.getPourInfo3().length() > 0) {
				if (first) {
					ret.append(intervention.getPourInfo3());
					first = false;
				} else {
					ret.append(", " + intervention.getPourInfo3());
				}
			}
		}
		return ret.toString();
	}


	public TaskResult getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(TaskResult taskResult) {
		this.taskResult = taskResult;
	}
	

	protected void recordUpdate(String field) {
		event = new Event();
		event.setActor(loggedUser);

		event.setComment(Labels.get("field.updated") + "'"+field+"' " );
		event.setTaskCode("UPDATE_FIELD");
		event.setTimestamp(new Date());
		event.setDemande(demandeDTO.getDemande());
		demandeDTO.getDemande().addEvent(event);
		entityManager.flush();
		
	}
	public DemandeDTO getDemandeDTO() {
		return demandeDTO;
	}
	public void setDemandeDTO(DemandeDTO demandeDTO) {
		this.demandeDTO = demandeDTO;
	}	

	
}
