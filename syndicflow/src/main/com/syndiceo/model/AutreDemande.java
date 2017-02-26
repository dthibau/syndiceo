package com.syndiceo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue(value="AutreDemande")
public class AutreDemande extends Demande implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8271339015730651654L;
	/**
	 * 
	 */

	// Données
	@ManyToOne
	private Critere critere;
	@ManyToOne
	private SousCritere sousCritere;
	
	public Critere getCritere() {
		return critere;
	}
	public void setCritere(Critere critere) {
		this.critere = critere;
	}
	public SousCritere getSousCritere() {
		return sousCritere;
	}
	public void setSousCritere(SousCritere sousCritere) {
		this.sousCritere = sousCritere;
	}

	

	

}
