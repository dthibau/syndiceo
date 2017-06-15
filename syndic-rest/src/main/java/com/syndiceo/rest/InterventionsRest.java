package com.syndiceo.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.Affectation;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Role;
import com.syndiceo.service.TaskService;

@RestController
public class InterventionsRest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -355044670516614813L;

	public static String D_ENCOURS = "encours";
	public static String D_ALL = "all";

	private String filter = D_ALL;

	@Autowired
	TaskService taskService;

	Account loggedUser;

	private String pattern;
	private String stateCode;
	private Immeuble immeuble;


	List<DemandeDTO> demandes;
	List<DemandeDTO> filteredDemandes;

	@GetMapping("/interventions")
	public List<DemandeDTO> getDemandes() {

		loggedUser = new Account();
		Affectation affectation = new Affectation(loggedUser, new Role("r_admin"), null);
		loggedUser.addAffectation(affectation);
		
		if (loggedUser.isOnly("r_copro")) {
			filter = D_ALL;
		}
		if (filter.equals(D_ENCOURS)) {
			demandes = taskService.getEnCoursPetitTravaux(loggedUser);
		} else if (filter.equals(D_ALL)) {
			if (loggedUser.is("r_admin")) {
				demandes = taskService.getAllPetitsTravaux(loggedUser);
			} else if (loggedUser.is("r_gestionnaire")) {
				demandes = taskService.getAllMinePetitTravaux(loggedUser);
			} else { // Co-pro, conseil (par immeubles)
				demandes = taskService.getAllImmeublesPetitTravaux(loggedUser);
			}
		}

		filteredDemandes = _filter();

		return filteredDemandes;
	}

	private List<DemandeDTO> _filter() {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();
		if (pattern != null && pattern.length() > 0) {
			for (DemandeDTO d : demandes) {
				if (d.containsPattern(pattern, loggedUser.is("r_gestionnaire") || loggedUser.is("r_admin"))) {
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
		if (stateCode != null && stateCode.length() > 0) {
			List<DemandeDTO> filterState = new ArrayList<DemandeDTO>();
			for (DemandeDTO d : ret) {
				if (d.getStatusCode().equals(stateCode)) {
					filterState.add(d);
				}
			}
			ret = filterState;
		}
		return ret;
	}


}
