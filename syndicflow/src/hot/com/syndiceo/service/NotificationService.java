package com.syndiceo.service;

import java.util.List;

import org.jboss.seam.annotations.async.Duration;

import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Event;

public interface NotificationService {

	public List<Account> resolveDestinataires(Account actor, int[] notificationCodes, DemandeDTO demande, Event event);
	
	public void sendMail( @Duration long waitingTime, String server, Demande demande, Event event);
	
	
}
