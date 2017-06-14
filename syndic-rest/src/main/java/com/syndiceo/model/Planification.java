package com.syndiceo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

@Entity
public class Planification {

	@Id @GeneratedValue
	private long id;
	@ManyToOne
	private Intervention intervention;
	@Temporal(TemporalType.DATE)
	private Date date;
	@ManyToOne
	private Horaire horaire;
	private String horaireLibre;
	@Length(max=65025)
	private String prestataire;
	
	private String outcome;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Intervention getIntervention() {
		return intervention;
	}
	public void setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPrestataire() {
		return prestataire;
	}
	public void setPrestataire(String prestataire) {
		this.prestataire = prestataire;
	}
	public Horaire getHoraire() {
		return horaire;
	}
	public void setHoraire(Horaire horaire) {
		this.horaire = horaire;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getHoraireLibre() {
		return horaireLibre;
	}
	public void setHoraireLibre(String horaireLibre) {
		this.horaireLibre = horaireLibre;
	}
	@Override
	public String toString() {
		return "Planification [date=" + date + ", horaire=" + horaire
				+ ", horaireLibre=" + horaireLibre + ", prestataire="
				+ prestataire + ", outcome=" + outcome + "]";
	}
	
	
}
