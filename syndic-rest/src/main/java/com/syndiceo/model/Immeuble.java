package com.syndiceo.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Immeuble implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5513099606506158146L;
	@Id
	private String numero;
	private String codes;
	private String adresse;
	private int noIntervention;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JsonManagedReference
	private Account gestionnaire;
	

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
		this.codes = codes;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public Account getGestionnaire() {
		return gestionnaire;
	}
	public void setGestionnaire(Account gestionnaire) {
		this.gestionnaire = gestionnaire;
	}
	
	public int getNoIntervention() {
		return noIntervention;
	}
	public void setNoIntervention(int noIntervention) {
		this.noIntervention = noIntervention;
	}
	@Override
	public String toString() {
		return numero + ", " + adresse;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		Immeuble other = (Immeuble) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	
	
	
}
