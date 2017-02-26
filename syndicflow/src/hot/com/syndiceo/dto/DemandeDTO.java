package com.syndiceo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syndiceo.Application;
import com.syndiceo.model.Account;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Immeuble;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTask;

public abstract class DemandeDTO implements Comparable<DemandeDTO> {

	private Account account;
	private Demande demande;
	private LongTask currentTask;
	private List<DirectTask> directTasks = new ArrayList<DirectTask>();
	private int mode;
	private boolean lu;
	
	public DemandeDTO(Account account, Demande demande) {
		this.account = account;
		this.demande = demande;
	}
	

	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}

	
	public Demande getDemande() {
		return demande;
	}


	public void setDemande(Demande demande) {
		this.demande = demande;
	}


	public long getId() {
		return demande.getId();
	}
	public long getProcessInstanceId() {
		return demande.getProcessInstanceId();
	}
	public String getStatusCode() {
		return demande.getStatusCode();
	}
	public void setStatusCode(String statusCode) {
		this.demande.setStatusCode(statusCode);
	}
	public String getTitre() {
		return demande.getTitre();
	}
	public Date getCreatedDate() {
		return demande.getCreatedDate();
	}
	public String getNumero() {
		return demande.getImmeuble().getNumero() + "-" + demande.getNoIntervention();
	}
	public Immeuble getImmeuble() {
		return demande.getImmeuble();
	}


	public String getDescription() {
		return demande.getDescription();
	}


	public int getMode() {
		return mode;
	}

	public LongTask getCurrentTask() {
		return currentTask;
	}

	public List<DirectTask> getDirectTasks() {
		return directTasks;
	}
	public void setDirectTasks(List<DirectTask> directTasks) {
		this.directTasks = directTasks;
	}
	public void addDirectTask(DirectTask task) {
		directTasks.add(task);
	}
	
	public void setCurrentTask(LongTask currentTask) {
		this.currentTask = currentTask;
		
		if (currentTask != null && currentTask.isAssignable(account)) {
			if ( currentTask.getCode().equals("UPDATE") || currentTask.getCode().equals("CHECK") || currentTask.getCode().equals("COMPLETE") 
					|| currentTask.getCode().equals("AUTRE_PRECISER") ) {
				mode = Application.M_EDIT_DEMANDE;
			} else if ( currentTask.getCode().equals("CHECK_INTERVENTION") ) {
				mode = Application.M_EDIT_INTERVENTION;
			} else {
				mode = Application.M_VISU;
			}
		} else {
			mode = Application.M_VISU;
		}

	}
	public boolean hasTask() {
		return currentTask != null || !directTasks.isEmpty();
	}
	
	public boolean hasOneTask() {
		int count=0;
		if ( currentTask != null ) {
			count = Application.taskConf.get(currentTask.getCode()).getButtonConfs().length;
		}
		count+=directTasks.size();

		return count==1;
	}
	public boolean hasMoreThanOneTask() {
		int count=0;
		if ( currentTask != null ) {
			count = Application.taskConf.get(currentTask.getCode()).getButtonConfs().length;
		}
		count+=directTasks.size();
	
		return count>1;
	}

	public Account getGestionnaire() {
		return demande.getImmeuble().getGestionnaire();
	}
	public Account getDemandeur() {
		return demande.getDemandeur();
	}

	public boolean isLu() {
		return lu;
	}


	public void setLu(boolean lu) {
		this.lu = lu;
	}


	@Override
	public int compareTo(DemandeDTO o) {
		
		return -getCreatedDate().compareTo(o.getCreatedDate());
	}

	abstract public List<Account> getPourInfo();
	abstract public boolean containsPattern(String pattern, boolean bImmeuble);
}
