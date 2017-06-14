package com.syndiceo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Demande implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2251390098387528132L;

	/**
	 * 
	 */

	@Id @GeneratedValue
	private Long id;
	
	private int noIntervention;
	// Workflow
	private long processInstanceId;
	private String statusCode;
	@ManyToOne
	private Account demandeur;
	@ManyToOne
	private Immeuble immeuble;	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="demande")
	@OrderBy("timestamp DESC")
	List<Event> events = new ArrayList<Event>();
	@OneToMany(cascade=CascadeType.ALL,mappedBy="demande")
	List<PieceJointe> piecesJointes = new ArrayList<PieceJointe>();	

	private String titre;

	
	@Length(max=65025)
	private String description;
	
	private Date createdDate;
	private Date archivedDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Transient 
	public boolean isNew() {
		return getId() == null || getId() == 0;
	}
	
	public int getNoIntervention() {
		return noIntervention;
	}
	public void setNoIntervention(int noIntervention) {
		this.noIntervention = noIntervention;
	}
	@Transient
	public String getNumero() {
		return getImmeuble().getNumero() + "-" + getNoIntervention();
	}
	public long getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Account getDemandeur() {
		return demandeur;
	}
	public void setDemandeur(Account demandeur) {
		this.demandeur = demandeur;
	}
	public Immeuble getImmeuble() {
		return immeuble;
	}
	public void setImmeuble(Immeuble immeuble) {
		this.immeuble = immeuble;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public void addEvent(Event event) {
		events.add(event);
		event.setDemande(this);
	}
	@Transient
	public List<FichierEvent> getFichiers() {
		List<FichierEvent> ret = new ArrayList<FichierEvent>();
		
		for ( Event e : getEvents() ) {
			ret.addAll(e.getFichiers());
		}
		return ret;
	}
	@Transient
	public Event getLastMessage() {
		return getEvents().size() > 0 ? getEvents().get(0) : null;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	

	public String getDescription() {
		return description;
	}
	public String getDescriptionAsHTML() {
		return description.replaceAll("\n", "<br/>");
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getArchivedDate() {
		return archivedDate;
	}
	public void setArchivedDate(Date archivedDate) {
		this.archivedDate = archivedDate;
	}
	

	public List<PieceJointe> getPiecesJointes() {
		return piecesJointes;
	}
	public void setPiecesJointes(List<PieceJointe> piecesJointes) {
		this.piecesJointes = piecesJointes;
	}
	
	public void addPieceJointe(PieceJointe pj) {
		piecesJointes.add(pj);
	}
	
	public void removePieceJointe(PieceJointe pj) {
		piecesJointes.remove(pj);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Demande other = (Demande) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	

}
