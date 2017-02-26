package com.syndiceo.proc.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.syndiceo.model.Account;

@Entity
@DiscriminatorValue("LongWithMessage")
public class LongTaskWithMessage extends LongTask {
	
	@ManyToMany
	@JoinTable(name="task_participants")
	private List<Account> participants = new ArrayList<Account>();

	public List<Account> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Account> participants) {
		this.participants = participants;
	}
	
	public void addParticipants(List<Account> accounts) {
		for ( Account account : accounts ) {
			if ( !participants.contains(account) ) {
				participants.add(account);
			}
		}
	}

	@Override
	public boolean containsParticipant(Account account) {
		
		return participants.contains(account);
	}

}
