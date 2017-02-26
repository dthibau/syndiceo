package com.syndiceo.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.syndiceo.Application;
import com.syndiceo.model.Account;
import com.syndiceo.model.Affectation;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.SousSpecialite;
import com.syndiceo.model.Specialite;

public class InterventionDTO extends DemandeDTO {


	private Intervention intervention;

	public InterventionDTO(Account account, Intervention intervention) {
		super(account,intervention);
		this.intervention = intervention;
	}
	

	public Intervention getIntervention() {
		return intervention;
	}
	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
		super.setDemande(intervention);
	}
	

	public Specialite getSpecialite() {
		return intervention.getSpecialite();
	}
	public SousSpecialite getSousSpecialite() {
		return intervention.getSousSpecialite();
	}
	public String getContactNom() {
		return intervention.getContactDemandeur() ? intervention.getDemandeur().getNomComplet() : intervention.getContact();
	}
	public String getContactEmail() {
		return intervention.getContactDemandeur() ? intervention.getDemandeur().getEmail() : intervention.getContactEmail();
	}
	public String getContactTelephone() {
		return intervention.getContactDemandeur() ? intervention.getDemandeur().getTelephone() : intervention.getContactTelephone();
	}

	public Date getDateSouhaitee() {
		return intervention.getDateSouhaitee();
	}


	public Date getNextDateIntervention() {
		if ( !intervention.getPlanifications().isEmpty() ) {
			return intervention.getPlanifications().get(0).getDate();
		}
		return null;
	}

	public boolean containsPattern(String pattern, boolean bImmeuble) {
		String lowerPattern = pattern.toLowerCase();
		return (""+getId()).indexOf(lowerPattern) != -1
				|| getTitre().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.dateFormat.format(getCreatedDate()).indexOf(
						lowerPattern) != -1			
				|| getSpecialite().getNom().toLowerCase().indexOf(lowerPattern) != -1
				|| getDemandeur().getNomComplet().toLowerCase().indexOf(lowerPattern) != -1
				|| (getNextDateIntervention()!= null && Application.dateFormat.format(getNextDateIntervention()).indexOf(
								lowerPattern) != -1	)		
				|| Application.msgBundle
						.getString(getStatusCode()).toLowerCase().indexOf(
								lowerPattern) != -1
				|| ( bImmeuble && getImmeuble().toString().toLowerCase().indexOf(lowerPattern) != -1);
	}


	public List<Account> getConseils() {
		return intervention.getConseils();
	}
	@Override
	public List<Account> getPourInfo() {
		List<Account> ret = new ArrayList<Account>();
		for ( Account a : intervention.getConseils() ) {
			ret.add(a);
		}
		if (intervention.getPourInfo1() != null && intervention.getPourInfo1().length() > 0) {
			Account a = new Account();
			a.setEmail(intervention.getPourInfo1());
			a.setNom(intervention.getPourInfo1());
			ret.add(a);
		}
		if (intervention.getPourInfo2() != null && intervention.getPourInfo2().length() > 0) {
			Account a = new Account();
			a.setEmail(intervention.getPourInfo2());
			a.setNom(intervention.getPourInfo2());
			ret.add(a);
		}
		if (intervention.getPourInfo3() != null && intervention.getPourInfo3().length() > 0) {
			Account a = new Account();
			a.setEmail(intervention.getPourInfo3());
			a.setNom(intervention.getPourInfo3());
			ret.add(a);
		}
		return ret;
	}
	
	@Override
	public boolean hasTask() {
		if ( super.hasTask() ) {
			return true;
		}
			
		return  hasMessageTask() || hasUpdateDestTask();
		

	}

	public boolean hasMessageTask() {
		if ( intervention.getConseils().contains(getAccount()) ) {
			return true;
		}
		if ( !intervention.getConfidentielle() ) {
			for ( Affectation affectation : getAccount().getAffectations() ) {
				if ( affectation.getRole().getId().equals("r_admin") || affectation.getImmeuble().equals(intervention.getImmeuble()) && (affectation.getRole().getId().equals("r_copro") 
						|| affectation.getRole().getId().equals("r_conseil")) ) {
					return true;
				}
			}
		} else {
			for ( Affectation affectation : getAccount().getAffectations() ) {
				if ( affectation.getRole().getId().equals("r_admin") || affectation.getRole().getId().equals("r_conseil") ) {
					return true;
				}
			}
		}

	return false;
		
	}
	
	public boolean hasUpdateDestTask() {
		if ( getAccount().is("r_admin") || intervention.getDemandeur().equals(getAccount()) || intervention.getConseils().contains(getAccount()) ) {
			return true;
		}
		return false;
	}

	public String getConseilsAsString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Account a : getConseils() ) {
				if (first) {
					sb.append(a.getNomComplet());
					first = false;
				} else {
					sb.append(", " + a.getNomComplet());
				}

		}
		return sb.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((intervention == null) ? 0 : intervention.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InterventionDTO other = (InterventionDTO) obj;
		if (intervention == null) {
			if (other.intervention != null)
				return false;
		} else if (!intervention.equals(other.intervention))
			return false;
		return true;
	}
	
	
}
