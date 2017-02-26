package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import com.syndiceo.Application;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.dao.AutreDemandeDao;
import com.syndiceo.util.Util;

@Name("autreDemandesArchivesManager")
@Scope(ScopeType.CONVERSATION)
public class AutreDemandesArchivesManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355044670516614813L;


	
	@In("#{autreDemandeDao}")
	AutreDemandeDao autreDemandeDao;
	
	@In
	Account loggedUser;
	
	
	@In(scope=ScopeType.APPLICATION)
	private Date lastAction;
	
	private String pattern;
	private Immeuble immeuble;
	private boolean filterChanged = true;
	
	private Date lastRequest;
	
	List<AutreDemande> autreDemandes;
	List<AutreDemande> filteredAutreDemandes;
	
	@Begin(join=true)
	public List<AutreDemande> getAutreDemandes() {
		if ( autreDemandes == null || lastAction.after(lastRequest) ) {
			if ( loggedUser.is("r_admin") ) {
				autreDemandes = autreDemandeDao.findArchived();
			} else { 
				autreDemandes = autreDemandeDao.findArchivedByImmeubles(loggedUser);
			}
			lastRequest = new Date();
		}
		if ( filterChanged  ) {
			filteredAutreDemandes = _filter();
			filterChanged = false;
		}
		return filteredAutreDemandes;
	}
	
	@Observer("demandesUpdated")
	public void refresh() {
		autreDemandes = null;
	}
	
	private List<AutreDemande> _filter() {
		List<AutreDemande> ret = new ArrayList<AutreDemande>();
		if ( pattern != null && pattern.length() > 0 ) {
			for ( AutreDemande d : autreDemandes ) {
				if ( containsPattern(d,pattern,loggedUser.is("r_gestionnaire") || loggedUser.is("r_admin")) ) {
					ret.add(d);
				}
			}
		} else {
			ret = autreDemandes;
		}
		if (immeuble != null) {
			List<AutreDemande> filterImmeubles = new ArrayList<AutreDemande>();
			for (AutreDemande d : ret) {
				if (d.getImmeuble().equals(immeuble)) {
					filterImmeubles.add(d);
				}
			}
			ret = filterImmeubles;
		}
		return ret;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		if ( !Util.isEqual(pattern,this.pattern) ) {
			this.pattern = pattern;
			filterChanged=true;
		}
	}


	public Immeuble getImmeuble() {
		return immeuble;
	}

	public void setImmeuble(Immeuble immeuble) {
		if (!Util.isEqual(immeuble, this.immeuble)) {
			this.immeuble = immeuble;
			filterChanged = true;
		}
	}

	@Transient
	public boolean containsPattern(AutreDemande autreDemande, String pattern, boolean bImmeuble) {
		String lowerPattern = pattern.toLowerCase();
		return (""+autreDemande.getId()).indexOf(lowerPattern) != -1
				|| autreDemande.getTitre().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.dateFormat.format(autreDemande.getCreatedDate()).indexOf(
						lowerPattern) != -1			
				|| autreDemande.getCritere().getNom().toLowerCase().indexOf(lowerPattern) != -1
				|| autreDemande.getDemandeur().getNomComplet().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.msgBundle
						.getString(autreDemande.getStatusCode()).toLowerCase().indexOf(
								lowerPattern) != -1
				|| ( bImmeuble && autreDemande.getImmeuble().toString().toLowerCase().indexOf(lowerPattern) != -1);
	}

	
	
}
