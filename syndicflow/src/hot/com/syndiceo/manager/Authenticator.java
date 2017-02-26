package com.syndiceo.manager;

import java.io.IOException;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityNotFoundException;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.jboss.seam.web.Session;

import com.syndiceo.Application;
import com.syndiceo.SessionData;
import com.syndiceo.model.Account;
import com.syndiceo.model.Affectation;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Role;

@Name("authenticator")
public class Authenticator {
	@Logger
	private Log log;

	public static String AFF_SEPARATOR = ";";
	public static String FIELD_SEPARATOR = "!";

	@In
	@Out
	Identity identity;
	@In
	Credentials credentials;

	@In
	EntityManager entityManager;

	@RequestParameter
	String accountId;
	@RequestParameter
	String email;
	@RequestParameter
	String nom;
	@RequestParameter
	String telephone;
	@RequestParameter
	String affectations;

	@Out(scope = ScopeType.SESSION, required = false)
	Account loggedUser;

	@In(required=false)
	SessionData sessionData;
	@In
	FacesContext facesContext;

	public String authenticate() {
		log.info("authenticating {0}", accountId);
		if ( sessionData != null ) {
			sessionData.clear();
		}


		try {
			_createLoggedUser();
			if (loggedUser != null) {
				return "OK";
			}
		} catch (EntityNotFoundException e) {
			return "NOK";
		}
		return "NOK";
	}
	
	public void getToken() {
		log.info("authenticating {0} via token", accountId);

		try {
			_createLoggedUser();
			if (loggedUser != null) {
				_sendToken();
			}
		} catch (EntityNotFoundException e) {
			_sendError();
		}
		_sendError();
	}

	private void _sendToken() {

		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.setContentType("text/plain");

		ServletOutputStream out;
		try {
			out = response.getOutputStream();
			out.print(""+new Date().getTime());
			out.flush();
		} catch (IOException e) {
			// TODO: error here
		}
		facesContext.responseComplete();

	}
	
	private void _sendError() {

		HttpServletResponse response = (HttpServletResponse) facesContext
				.getExternalContext().getResponse();
		response.setContentType("text/plain");

		try {
			response.sendError(403);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		facesContext.responseComplete();

	}

	private void _createLoggedUser() {
		// creating loggedUser from the request
		loggedUser = _parseAccount();
		// Synchronize SyndicEo directory
		for (Affectation affectation : loggedUser.getAffectations()) {
			if ( affectation.getImmeuble() != null ) {
				Immeuble immeuble = entityManager.find(Immeuble.class, affectation
						.getImmeuble().getNumero());
				if (immeuble == null) {
					entityManager.persist(affectation.getImmeuble());
					log.info("Creating immeuble {0}", affectation.getImmeuble());
				} else {
					immeuble.setAdresse(affectation.getImmeuble().getAdresse());
					immeuble.setGestionnaire(affectation.getImmeuble().getGestionnaire());
					affectation.setImmeuble(immeuble);
					log.info("Updating immeuble {0}", affectation.getImmeuble());
				}
			}
		}
		if (entityManager.find(Account.class, accountId) != null) {
			Account a = entityManager.find(Account.class, accountId);
			for ( Affectation aff : a.getAffectations() ) {
				entityManager.remove(aff);
			}
			a.setAffectations(null);

			loggedUser = entityManager.merge(loggedUser);
			log.info("Updating account {0}", loggedUser);
		} else {
			entityManager.persist(loggedUser);
			log.info("Creating account {0}", loggedUser);
		}

	}

	private Account _parseAccount() {
		Account ret = new Account();
		ret.setId(accountId);
		ret.setEmail(email);
		ret.setTelephone(telephone != null && telephone.length() >= 10 ? telephone : "PAS RENSEIGNE DANS ICS");
		ret.setNom(nom);
		if (affectations != null) {
			String[] sAffectations = affectations.split(AFF_SEPARATOR);
			for (String sAffectation : sAffectations) {
				String[] sFields = sAffectation.split(FIELD_SEPARATOR);
				Affectation affectation = new Affectation();
				affectation.setAccount(ret);
				affectation.setRole(new Role(sFields[0]));
				if ( !sFields[0].equals("r_admin") ) {
					Immeuble immeuble = new Immeuble();
					immeuble.setNumero(sFields[1]);
					immeuble.setAdresse(sFields[2]);
					Account gestionnaire = entityManager.find(Account.class,sFields[3]);
					if ( gestionnaire == null ) {
						gestionnaire = new Account();
						gestionnaire.setId(sFields[3]);
						gestionnaire.setEmail(Application.ADMIN_EMAIL);
						gestionnaire.setTelephone("PAS ENCORE LU DANS ICS");
						gestionnaire.setNom("PAS ENCORE LU DANS ICS");
						entityManager.persist(gestionnaire);
					}
					immeuble.setGestionnaire(gestionnaire);
					affectation.setImmeuble(immeuble);
				}
				ret.addAffectation(affectation);
			}
		}
		return ret;
	}

	public String logout() {

		identity.logout();
		// On timeout facesContext is null!
		if (facesContext != null) {

			// try {
			// ((HttpServletResponse)facesContext.getExternalContext().getResponse()).sendRedirect(facesContext.getExternalContext().getRequestContextPath());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			Session.instance().invalidate();
		}

		return "home";

	}

}
