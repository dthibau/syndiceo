package com.syndiceo.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;

import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Immeuble;
import com.syndiceo.service.TaskService;
import com.syndiceo.util.Util;

@Name("grosTravauxManager")
@Scope(ScopeType.CONVERSATION)
public class GrosTravauxManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355044670516614813L;



	@In(create = true)
	TaskService taskService;

	@In
	Account loggedUser;

	@In(scope = ScopeType.APPLICATION)
	private Date lastAction;

	private String pattern;
	private Immeuble immeuble;

	private Date lastRequest;

	List<DemandeDTO> demandes;
	List<DemandeDTO> filteredDemandes;
	private boolean filterChanged = true;

	@Begin(join = true)
	public List<DemandeDTO> getDemandes() {
		if (demandes == null || lastAction.after(lastRequest)) {

			if (loggedUser.is("r_admin")) {
				demandes = taskService.getAllGrosTravaux(loggedUser);
			} else if (loggedUser.is("r_gestionnaire")) {
				demandes = taskService.getAllMineGrosTravaux(loggedUser);
			} else { // Co-pro, conseil (par immeubles)
				demandes = taskService.getAllImmeublesGrosTravaux(loggedUser);
			}

			lastRequest = new Date();
		}
		if (filterChanged) {
			filteredDemandes = _filter();
			filterChanged = false;
		}
		return filteredDemandes;
	}

	@Observer("demandesUpdated")
	public void refresh() {
		demandes = null;
	}

	private List<DemandeDTO> _filter() {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();
		if (pattern != null && pattern.length() > 0) {
			for (DemandeDTO d : demandes) {
				if (d.containsPattern(pattern, loggedUser.is("r_gestionnaire")
						|| loggedUser.is("r_admin"))) {
					ret.add(d);
				}
			}
		} else {
			ret = demandes;
		}
		if (immeuble != null) {
			List<DemandeDTO> filterImmeubles = new ArrayList<DemandeDTO>();
			for (DemandeDTO d : ret) {
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

}
