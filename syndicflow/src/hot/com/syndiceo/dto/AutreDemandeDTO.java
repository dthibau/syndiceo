package com.syndiceo.dto;

import java.util.ArrayList;
import java.util.List;

import com.syndiceo.Application;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Critere;
import com.syndiceo.model.SousCritere;

public class AutreDemandeDTO extends DemandeDTO {


	private AutreDemande autreDemande;
	
	public AutreDemandeDTO(Account account, AutreDemande autreDemande) {
		super(account,autreDemande);
		this.autreDemande = autreDemande;
	}
	
	public AutreDemande getAutreDemande() {
		return autreDemande;
	}

	public void setAutreDemande(AutreDemande autreDemande) {
		this.autreDemande = autreDemande;
	}

	public Critere getCritere() {
		return autreDemande.getCritere();
	}
	public SousCritere getSousCritere() {
		return autreDemande.getSousCritere();
	}
	
	public boolean containsPattern(String pattern, boolean bImmeuble) {
		String lowerPattern = pattern.toLowerCase();
		return (""+getId()).indexOf(lowerPattern) != -1
				|| getTitre().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.dateFormat.format(getCreatedDate()).indexOf(
						lowerPattern) != -1			
				|| getCritere().getNom().toLowerCase().indexOf(lowerPattern) != -1
				|| getDemandeur().getNomComplet().toLowerCase().indexOf(lowerPattern) != -1
				|| Application.msgBundle
						.getString(getStatusCode()).toLowerCase().indexOf(
								lowerPattern) != -1
				|| ( bImmeuble && getImmeuble().toString().toLowerCase().indexOf(lowerPattern) != -1);
	}

	@Override
	public List<Account> getPourInfo() {
		return new ArrayList<Account>();
	}
	
}
