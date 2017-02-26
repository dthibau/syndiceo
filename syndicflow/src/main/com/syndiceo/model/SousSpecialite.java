package com.syndiceo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

@Entity
public class SousSpecialite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6848841816799422055L;
	@Id
	@Length(max=10)
	private String code;
	private String nom;
	@ManyToOne
	private Specialite specialite;
	

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public Specialite getSpecialite() {
		return specialite;
	}
	public void setSpecialite(Specialite specialite) {
		this.specialite = specialite;
	}
	
}
