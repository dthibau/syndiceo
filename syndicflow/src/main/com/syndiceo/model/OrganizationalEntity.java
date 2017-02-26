package com.syndiceo.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public abstract class OrganizationalEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1055308736775417961L;
	@Id
	private String id;
	@Size(max = 40)
    private String nom;
	
	public OrganizationalEntity() {
		super();
	}
	
	public OrganizationalEntity(String id) {
		this.id = id;
	}

    public String getNom() {
        return nom;
    }

    public void setNom(String name) {
        this.nom = name;
    }
	public abstract boolean match(Account account);
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		OrganizationalEntity other = (OrganizationalEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrganizationalEntity [id=" + id + "]";
	}


	
	
}
