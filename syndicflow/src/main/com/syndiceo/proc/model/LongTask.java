package com.syndiceo.proc.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.syndiceo.model.Account;

@Entity
@DiscriminatorValue("Long")
public class LongTask extends Task {
	

	public static int STARTED=2;
	public static int CANCELLED=3;
	@Override
	public boolean containsParticipant(Account account) {
		return false;
	}

	


}
