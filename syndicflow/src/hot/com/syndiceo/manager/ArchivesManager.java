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
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.dao.InterventionDao;
import com.syndiceo.util.Util;

@Name("archivesManager")
@Scope(ScopeType.CONVERSATION)
public class ArchivesManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355044670516614813L;


	
	@In("#{interventionDao}")
	InterventionDao interventionDao;
	
	@In
	Account loggedUser;
	
	
	@In(scope=ScopeType.APPLICATION)
	private Date lastAction;
	
	private String pattern;
	private Immeuble immeuble;
	private boolean filterChanged = true;
	
	private Date lastRequest;
	
	List<Intervention> interventions;
	List<Intervention> filteredInterventions;
	
	@Begin(join=true)
	public List<Intervention> getInterventions() {
		if ( interventions == null || lastAction.after(lastRequest) ) {
			if ( loggedUser.is("r_admin") ) {
				interventions = interventionDao.findArchived();
			} else { 
				interventions = interventionDao.findArchivedByImmeubles(loggedUser);
			}
			lastRequest = new Date();
		}
		if ( filterChanged  ) {
			filteredInterventions = _filter();
			filterChanged = false;
		}
		return filteredInterventions;
	}
	
	@Observer("demandesUpdated")
	public void refresh() {
		interventions = null;
	}
	
	private List<Intervention> _filter() {
		List<Intervention> ret = new ArrayList<Intervention>();
		if ( pattern != null && pattern.length() > 0 ) {
			for ( Intervention d : interventions ) {
				if ( containsPattern(d,pattern,loggedUser.is("r_gestionnaire") || loggedUser.is("r_admin")) ) {
					ret.add(d);
				}
			}
		} else {
			ret = interventions;
		}
		if (immeuble != null) {
			List<Intervention> filterImmeubles = new ArrayList<Intervention>();
			for (Intervention d : ret) {
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
	public boolean containsPattern(Intervention intervention, String pattern, boolean bImmeuble) {
		String lowerPattern = pattern.toLowerCase();
		return (""+intervention.getId()).indexOf(lowerPattern) != -1
				|| intervention.getTitre().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.dateFormat.format(intervention.getCreatedDate()).indexOf(
						lowerPattern) != -1			
				|| intervention.getSpecialite().getNom().toLowerCase().indexOf(lowerPattern) != -1
				|| intervention.getDemandeur().getNomComplet().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.msgBundle
						.getString(intervention.getStatusCode()).toLowerCase().indexOf(
								lowerPattern) != -1
				|| ( bImmeuble && intervention.getImmeuble().toString().toLowerCase().indexOf(lowerPattern) != -1);
	}

	
	
}
