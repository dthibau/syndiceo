package com.syndiceo;

import java.util.Collection;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;

import com.syndiceo.model.Account;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.dao.ImmeubleDao;

@Name("sessionData")
@Scope(ScopeType.SESSION)
@AutoCreate
public class SessionData {

	@In(required=false) // Nécesssaire pour le clear
	private Account loggedUser;
	@In("#{immeubleDao}")
	ImmeubleDao immeubleDao;
	
	@Out(required=false)
	Collection<Immeuble> immeubles;
	
	@Factory("immeubles")
	public void initImmeubles() {
		if ( loggedUser != null ) {
			if ( loggedUser.is("r_admin") ) {
				immeubles = immeubleDao.findAll();
			} else if ( loggedUser.is("r_gestionnaire") ) {
				immeubles = immeubleDao.findByGestionnaire(loggedUser);
			} else {
				immeubles = loggedUser.getImmeubles();
			}	
		}
	}
	
	public void clear() {
		immeubles = null;
	}
}
