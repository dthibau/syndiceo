package com.syndiceo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;

@Entity
@DiscriminatorValue(value="Intervention")
public class Intervention extends Demande implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7017693808937000531L;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="intervention")
	@OrderBy("date DESC")
	List<Planification> planifications = new ArrayList<Planification>();
	
	private String batiment;
	@ManyToOne
	private Localisation localisation;
	

	// Données
	@ManyToOne
	private Specialite specialite;
	@ManyToOne
	private SousSpecialite sousSpecialite;
	
	private Boolean confidentielle = true;
	
	private Boolean contactDemandeur = true;
	private String contact;
	@Email
	private String contactEmail;
	@Email
	private String pourInfo1;
	@Email
	private String pourInfo2;
	@Email
	private String pourInfo3;
	private String contactTelephone;
	private Date dateSouhaitee;
	@ManyToOne
	private Horaire horaireSouhaite;
	@ManyToMany
	private List<Account> conseils = new ArrayList<Account>();

	public Specialite getSpecialite() {
		return specialite;
	}
	public void setSpecialite(Specialite specialite) {
		this.specialite = specialite;
	}
	public SousSpecialite getSousSpecialite() {
		return sousSpecialite;
	}
	public void setSousSpecialite(SousSpecialite sousSpecialite) {
		this.sousSpecialite = sousSpecialite;
	}
	public Boolean getContactDemandeur() {
		return contactDemandeur;
	}
	public void setContactDemandeur(Boolean contactDemandeur) {
		this.contactDemandeur = contactDemandeur;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactTelephone() {
		return contactTelephone;
	}
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}
	
	public String getPourInfo1() {
		return pourInfo1;
	}
	public void setPourInfo1(String pourInfo1) {
		this.pourInfo1 = pourInfo1;
	}
	public String getPourInfo2() {
		return pourInfo2;
	}
	public void setPourInfo2(String pourInfo2) {
		this.pourInfo2 = pourInfo2;
	}
	public String getPourInfo3() {
		return pourInfo3;
	}
	public void setPourInfo3(String pourInfo3) {
		this.pourInfo3 = pourInfo3;
	}
	public Date getDateSouhaitee() {
		return dateSouhaitee;
	}
	public void setDateSouhaitee(Date dateSouhaitee) {
		this.dateSouhaitee = dateSouhaitee;
	}
	public Horaire getHoraireSouhaite() {
		return horaireSouhaite;
	}
	public void setHoraireSouhaite(Horaire horaireSouhaite) {
		this.horaireSouhaite = horaireSouhaite;
	}

	
	public List<Planification> getPlanifications() {
		return planifications;
	}
	public void setPlanifications(List<Planification> planifications) {
		this.planifications = planifications;
	}
	public void addPlanification(Planification planification) {
		planifications.add(planification);
	}
	@Transient
	public Planification getLastPlanification() {
		if ( !getPlanifications().isEmpty() )
			return getPlanifications().get(0);
		return null;
	}
	@Transient
	public Date getLastPlanificationDate() {
		if ( !getPlanifications().isEmpty() )
			return getPlanifications().get(0).getDate();
		return null;
	}
	
	public String getBatiment() {
		return batiment;
	}
	public void setBatiment(String batiment) {
		this.batiment = batiment;
	}
	public Localisation getLocalisation() {
		return localisation;
	}
	public void setLocalisation(Localisation localisation) {
		this.localisation = localisation;
	}
	public List<Account> getConseils() {
		return conseils;
	}
	public void setConseils(List<Account> conseils) {
		this.conseils = conseils;
	}
	public void addConseil(Account conseil) {
		this.conseils.add(conseil);
	}
	public Boolean getConfidentielle() {
		return confidentielle;
	}
	public void setConfidentielle(Boolean confidentielle) {
		this.confidentielle = confidentielle;
	}
	

}
