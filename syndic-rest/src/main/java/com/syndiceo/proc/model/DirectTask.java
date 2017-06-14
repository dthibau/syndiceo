package com.syndiceo.proc.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.syndiceo.model.Account;
import com.syndiceo.model.OrganizationalEntity;

@Entity
@DiscriminatorValue("Direct")
public class DirectTask extends Task {

	@Transient
	public boolean isAssignable(Account account) {
		for (OrganizationalEntity oe : getOwners() ) {
			if ( oe.match(account) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsParticipant(Account account) {
		return false;
	}
	

}
