package com.syndiceo.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.dto.InterventionDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Event;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.OrganizationalEntity;
import com.syndiceo.model.Role;
import com.syndiceo.model.dao.AccountDao;
import com.syndiceo.model.dao.OrganizationalEntityDao;
import com.syndiceo.proc.JbpmHelperImpl;

@Service
public class NotificationServiceImpl implements NotificationService,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2285506329089580634L;

	public final static int DEMANDEUR = 0;
	public final static int GESTIONNAIRE = 1;
	public final static int ADMINISTRATEUR = 2;
	public final static int DESTINATAIRES = 3;
	public final static int OWNER = 4;
	public final static int MSG = 5;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	JbpmHelperImpl jbpmHelper;

	@Autowired
	AccountDao accountDao;

	@Autowired
	OrganizationalEntityDao organizationalEntityDao;


	private static Logger log = Logger.getLogger(NotificationServiceImpl.class.getName());


	@Override
	public List<Account> resolveDestinataires(Account acteur,
			int[] notificationCodes, DemandeDTO demande, Event event) {
		List<Account> destinataires = new ArrayList<Account>();
		List<Account> pourInfo = new ArrayList<Account>();

		if (notificationCodes != null) {
			for (int notificationCode : notificationCodes) {
				if (notificationCode == MSG) {
					if (acteur.equals(demande.getDemandeur())) {
						notificationCode = GESTIONNAIRE;
					} else {
						notificationCode = DEMANDEUR;
					}
				}
				switch (notificationCode) {
				case GESTIONNAIRE:
					if (!destinataires.contains(demande.getGestionnaire())) {
						organizationalEntityDao.synchronizeDirectory(demande
								.getGestionnaire());
						destinataires.add(demande.getGestionnaire());
					}
					break;
				case DEMANDEUR:
					if (!destinataires.contains(demande.getDemandeur())) {
						organizationalEntityDao.synchronizeDirectory(demande
								.getDemandeur());
						destinataires.add(demande.getDemandeur());
					}
					break;
				case DESTINATAIRES:
					organizationalEntityDao.synchronizeAccountDirectory(event
							.getDestinataires());
					for (Account a : event.getDestinataires()) {
						if (!destinataires.contains(a)) {
							destinataires.add(a);
						}
					}
					organizationalEntityDao.synchronizeAccountDirectory(event
							.getPourInfo());
					for (Account a : event.getPourInfo()) {
						if (!pourInfo.contains(a)) {
							pourInfo.add(a);
						}
					}
					break;
				case ADMINISTRATEUR:
					List<Account> accounts = accountDao
							.getAdministrateurs();
					organizationalEntityDao
							.synchronizeAccountDirectory(accounts);
					for (Account a : accounts) {
						if (!destinataires.contains(a)) {
							destinataires.add(a);
						}
					}
					break;
				case OWNER:
					for (OrganizationalEntity oe : demande.getCurrentTask()
							.getOwners()) {
						if (oe instanceof Account
								&& !destinataires.contains(oe)) {
							destinataires.add((Account) oe);
						} else {
							accounts = accountDao.getContactsByRole(demande.getImmeuble(),(Role) oe);
							organizationalEntityDao
									.synchronizeAccountDirectory(accounts);
							for (Account a : event.getDestinataires()) {
								if (!destinataires.contains(a)) {
									destinataires.add(a);
								}
							}
						}
					}
					break;

				}
			}
		}

		event.setDestinataires(destinataires);
		if ( demande instanceof InterventionDTO ) {
			for ( Account a : ((InterventionDTO)demande).getConseils() ) {
				if ( !event.getPourInfo().contains(a) ) {
					event.addPourInfo(a);
				}
			}
			_setAutresEmails(event, ((InterventionDTO)demande).getIntervention());
		}
		event.getPourInfo().addAll(pourInfo);
		// Remove acteur 
		if ( destinataires.contains(acteur) ) {
			destinataires.remove(acteur);
		}
		return destinataires;

	}

//	@Asynchronous
//	@TransactionAttribute(value = TransactionAttributeType.NOT_SUPPORTED)
	public void sendMail(long waitingTime, String server,
			Demande demande, Event event) {
		if (demande instanceof Intervention) {
			_sendMailIntervention(server, (Intervention) demande, event);
		} else {
			_sendMailAutreDemande(server, (AutreDemande) demande, event);
		}
	}

	private void _sendMailIntervention(String server,
			Intervention intervention, Event event) {
//		if (Application.MAIL_ENABLED && Renderer.instance() != null) {
//			Contexts.getEventContext().set("server", server);
//			Contexts.getEventContext().set("event", event);
//			Contexts.getEventContext().set("demande", intervention);
//			Contexts.getEventContext().set("intervention", intervention);
//			Contexts.getEventContext().set("lastPlanification",
//					intervention.getLastPlanification());

			for (Account account : event.getDestinataires()) {
				if (!account.equals(event.getActor())) {
					_sendAccount(account, event, false);
				}
			}
			for (Account account : event.getPourInfo()) {
				if (!account.equals(event.getActor())
						&& !event.getDestinataires().contains(account)) {
					_sendAccount(account, event, true);
				}
			}
			if (intervention.getPourInfo1() != null && intervention.getPourInfo1().length() > 0) {
				Account a = new Account();
				a.setEmail(intervention.getPourInfo1());
				a.setNom(intervention.getPourInfo1());
				_sendAccount(a, event, true);
			}
			if (intervention.getPourInfo2() != null && intervention.getPourInfo2().length() > 0) {
				Account a = new Account();
				a.setEmail(intervention.getPourInfo2());
				a.setNom(intervention.getPourInfo2());
				_sendAccount(a, event, true);
			}
			if (intervention.getPourInfo3() != null && intervention.getPourInfo3().length() > 0) {
				Account a = new Account();
				a.setEmail(intervention.getPourInfo3());
				a.setNom(intervention.getPourInfo3());
				_sendAccount(a, event, true);
			}
//		}

	}


	private void _sendMailAutreDemande(String server,
			AutreDemande autreDemande, Event event) {
//		if (Application.MAIL_ENABLED && Renderer.instance() != null) {
//			Contexts.getEventContext().set("server", server);
//			Contexts.getEventContext().set("event", event);
//			Contexts.getEventContext().set("demande", autreDemande);
//			Contexts.getEventContext().set("autreDemande", autreDemande);

			for (Account account : event.getDestinataires()) {
				if (!account.equals(event.getActor())) {
					_sendAccount(account, event, false);

				}
			}
			for (Account account : event.getPourInfo()) {
				if (!account.equals(event.getActor())
						&& !event.getDestinataires().contains(account)) {
					_sendAccount(account, event, true);
				}
			}
//		}

	}
	
	private void _sendAccount(Account account, Event event, boolean pourInfo) {
			log.info("Une notification a été envoyée à " + account.getEmail() + " event :"
					+ event.getTaskCode());
	}

	private void _setAutresEmails(Event event, Intervention intervention) {
		boolean first=true;
		StringBuilder sb = new StringBuilder();
		if ( intervention.getPourInfo1() != null && intervention.getPourInfo1().length() > 0) {
			if (first) {
				sb.append(intervention.getPourInfo1());
				first = false;
			} else {
				sb.append(", " + intervention.getPourInfo1());
			}
		}
		if ( intervention.getPourInfo2() != null && intervention.getPourInfo2().length() > 0) {
			if (first) {
				sb.append(intervention.getPourInfo2());
				first = false;
			} else {
				sb.append(", " + intervention.getPourInfo2());
			}
		}
		if ( intervention.getPourInfo3() != null && intervention.getPourInfo3().length() > 0) {
			if (first) {
				sb.append(intervention.getPourInfo3());
				first = false;
			} else {
				sb.append(", " + intervention.getPourInfo3());
			}
		}
		event.setAutreEmails(sb.toString());

	}
}
