package com.syndiceo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

@Entity
public class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6795094765556258478L;
	@Id @GeneratedValue
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	private String taskCode;
	@ManyToOne
	private Account actor;
	@ManyToMany
	@JoinTable(name="event_destinataires")
	private List<Account> destinataires = new ArrayList<Account>();
	@ManyToMany
	@JoinTable(name="event_pourinfo")
	private List<Account> pourInfo = new ArrayList<Account>();
	
	private String autreEmails;
	@Length(max=10000)
	private String comment;
	@ManyToOne
	private Demande demande;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<FichierEvent> fichiers = new ArrayList<FichierEvent>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public Account getActor() {
		return actor;
	}
	public void setActor(Account actor) {
		this.actor = actor;
	}
	public List<Account> getDestinataires() {
		return destinataires;
	}
	public void setDestinataires(List<Account> destinataires) {
		this.destinataires = destinataires;
	}
	public void addDestinataire(Account destinataire) {
		destinataires.add(destinataire);
	}
	public String getDestinatairesAsString() {
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for ( Account a : destinataires ) {
			if ( !a.equals(getActor()) ) {
				if ( first ) {
					ret.append(a.getNomComplet());
					first = false;
				} else {
					ret.append(", "+a.getNomComplet());
				}
			}
		}
		for ( Account a : pourInfo ) {
			if ( !a.equals(getActor()) ) {
				if ( first ) {
					ret.append(a.getNomComplet());
					first = false;
				} else {
					ret.append(", "+a.getNomComplet());
				}
			}
		}

		return ret.toString();
	}
	
	public List<Account> getPourInfo() {
		return pourInfo;
	}
	public void setPourInfo(List<Account> pourInfo) {
		this.pourInfo = pourInfo;
	}
	public void addPourInfo(Account account) {
		pourInfo.add(account);
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Demande getDemande() {
		return demande;
	}
	public void setDemande(Demande demande) {
		this.demande = demande;
	}
	
	public String getAutreEmails() {
		return autreEmails;
	}
	public void setAutreEmails(String autreEmails) {
		this.autreEmails = autreEmails;
	}
	
	public List<FichierEvent> getFichiers() {
		return fichiers;
	}
	public void setFichiers(List<FichierEvent> fichiers) {
		this.fichiers = fichiers;
	}
	public void addFichier(FichierEvent fichier) {
		this.fichiers.add(fichier);
	}
	public void removeFichier(FichierEvent fichier) {
		this.fichiers.remove(fichier);
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", timestamp=" + timestamp + ", taskCode="
				+ taskCode + ", actor=" + actor + ", destinataires="
				+ destinataires + ", comment=" + comment + "]";
	}
	
	@Transient
	public boolean isConcerned(Account account) {
		return getActor().equals(account) || getDestinataires().contains(account);
	}
	
	@Transient
	public String getMailCode() {
		return "mail."+getTaskCode();
	}
	
	@Transient
	public String getCommentAsHtml() {
		return comment != null ? comment.replace("\n", "<br/>") : "";
	}
	
	public Event clone() {
		Event ret = new Event();
		ret.setActor(getActor());
		ret.setComment(getComment());
		ret.setTaskCode(getTaskCode());
		ret.setTimestamp(getTimestamp());
		for ( Account account : getDestinataires() ) {
			ret.addDestinataire(account);
		}
		for ( Account account : getPourInfo() ) {
			ret.addPourInfo(account);
		}
		return ret;
	}
}
