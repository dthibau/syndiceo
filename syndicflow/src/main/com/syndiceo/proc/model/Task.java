package com.syndiceo.proc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.syndiceo.model.Account;
import com.syndiceo.model.OrganizationalEntity;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tasktype", discriminatorType=DiscriminatorType.STRING)
public abstract class Task {

	public static int NOT_STARTED=0;
	public static int COMPLETED=1;
	
	@Id @GeneratedValue
	private long id;
	private int status = NOT_STARTED;
	private int ordre = 9;
	private String actorId;
	private long workItemId;
	private long processInstanceId;
	private long subProcessInstanceId;
	private String form;
	private String code;
	
	@ManyToMany
	@JoinTable(name="task_owners")
	private List<OrganizationalEntity> owners = new ArrayList<OrganizationalEntity>();

	public abstract boolean containsParticipant(Account account); 
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(long workItemId) {
		this.workItemId = workItemId;
	}

	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}


	public long getSubProcessInstanceId() {
		return subProcessInstanceId;
	}
	public void setSubProcessInstanceId(long subProcessInstanceId) {
		this.subProcessInstanceId = subProcessInstanceId;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String name) {
		this.code = name;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int order) {
		this.ordre = order;
	}

	public String getActorId() {
		return actorId;
	}
	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
	
	
	public List<OrganizationalEntity> getOwners() {
		return owners;
	}

	public void setOwners(List<OrganizationalEntity> owners) {
		this.owners = owners;
	}

	@Transient
	public boolean isAssignable(Account account) {

		for (OrganizationalEntity oe : getOwners() ) {
			if ( oe.match(account) ) {
				return true;
			}
		}
		return false;
	}
	

	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Task other = (Task) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", actorId=" + actorId + ", code=" + code
				+ ", owners=" + owners + "]";
	}
	
	
}
