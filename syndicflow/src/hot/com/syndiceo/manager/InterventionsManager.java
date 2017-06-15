package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.dto.DemandeSorter;
import com.syndiceo.model.Account;
import com.syndiceo.model.Immeuble;
import com.syndiceo.service.TaskService;
import com.syndiceo.util.Util;


public class InterventionsManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355044670516614813L;

	@Out
	public static String D_ENCOURS="encours";
	@Out
	public static String D_ALL="all";
	
	private String filter = D_ALL;
	
	@In(create=true)
	TaskService taskService;
	
	@In
	Account loggedUser;
	
	
	@In(scope=ScopeType.APPLICATION)
	private Date lastAction;
	
	private String pattern;
	private String stateCode;
	private Immeuble immeuble;
	private boolean filterChanged=true;
	
	private Date lastRequest;
	
	List<DemandeDTO> demandes;
	List<DemandeDTO> filteredDemandes;
	
	@In(create=true)
	DemandeSorter demandeSorter;

	
	@Create
	public void init() {
		demandeSorter.sortBy("createdDate");
		// Twice to put it in desc order
		demandeSorter.sortBy("createdDate");
		
	}
	@Begin(join=true)
	public List<DemandeDTO> getDemandes() {
		if ( demandes == null || lastAction.after(lastRequest) ) {
			if ( loggedUser.isOnly("r_copro") ) {
				filter = D_ALL;
			}
			if ( getFilter().equals(D_ENCOURS) ) {
				demandes = taskService.getEnCoursPetitTravaux(loggedUser);
			} else if ( getFilter().equals(D_ALL) ) {
				if ( loggedUser.is("r_admin") ) {
					demandes = taskService.getAllPetitsTravaux(loggedUser);
				} else if ( loggedUser.is("r_gestionnaire") ) {
					demandes = taskService.getAllMinePetitTravaux(loggedUser);
				} else { // Co-pro, conseil (par immeubles)
					demandes = taskService.getAllImmeublesPetitTravaux(loggedUser);
				}
			}
			lastRequest = new Date();
			filterChanged = true;
		}
		if ( filterChanged  ) {
			filteredDemandes = _filter();
			filterChanged=false;
		}
		return filteredDemandes;
	}
	
	@Observer("demandesUpdated")
	public void refresh() {
		demandes = null;
	}
	
	private List<DemandeDTO> _filter() {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();
		if ( pattern != null && pattern.length() > 0 ) {
			for ( DemandeDTO d : demandes ) {
				if ( d.containsPattern(pattern,loggedUser.is("r_gestionnaire") || loggedUser.is("r_admin")) ) {
					ret.add(d);
				}
			}
		} else {
			ret = demandes;
		}
		if ( immeuble != null ) {
			List<DemandeDTO> filterImmeubles = new ArrayList<DemandeDTO>();
			for ( DemandeDTO d : ret ) {
				if ( d.getImmeuble().equals(immeuble) ) {
					filterImmeubles.add(d);
				}
			}
			ret= filterImmeubles;
		}
		if ( stateCode != null && stateCode.length() > 0 ) {
			List<DemandeDTO> filterState = new ArrayList<DemandeDTO>();
			for ( DemandeDTO d : ret ) {
				if ( d.getStatusCode().equals(stateCode) ) {
					filterState.add(d);
				}
			}
			ret= filterState;
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

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		if ( !Util.isEqual(filter,this.filter) ) {
			this.filter = filter;
			demandes=null;
		}
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		if ( !Util.isEqual(stateCode,this.stateCode) ) {
			this.stateCode = stateCode;
			filterChanged=true;
		}
	}

	public Immeuble getImmeuble() {
		return immeuble;
	}

	public void setImmeuble(Immeuble immeuble) {
		if ( !Util.isEqual(immeuble,this.immeuble) ) {
			this.immeuble = immeuble;
			filterChanged=true;
		}
		
	}



	
	
}
