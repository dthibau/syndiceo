package com.syndiceo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Entity
@DiscriminatorValue("Account")
public class Account extends OrganizationalEntity  implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 303051186027936314L;

    private String adresse;
    @Email
    private String email;
    @Length(min=10)
    private String telephone;
	@OneToMany(cascade=CascadeType.ALL,mappedBy="account")
    private List<Affectation> affectations = new ArrayList<Affectation>();

    public Account() {
    	super();
    }
    public Account(String id) {
    	super(id);
    }

    
    @Transient
    public String getNomComplet() {
    	return getNom();
    }

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Affectation> getAffectations() {
		return affectations;
	}
	public void setAffectations(List<Affectation> affectations) {
		this.affectations = affectations;
	}
	public void addAffectation(Affectation affectation) {
		affectations.add(affectation);
	}
	@Transient 
	public Set<Immeuble> getImmeublesActeur() {
		Set<Immeuble> ret = new HashSet<Immeuble>();
		for ( Affectation affectation : getAffectations() ) {
			if ( affectation.getRole().equals(Model.CONSEIL_ROLE) || affectation.getRole().equals(Model.GESTIONNAIRE_ROLE)) {
				ret.add(affectation.getImmeuble());
			}
		}
		return ret;
	}
	@Transient 
	public Set<Immeuble> getImmeubles() {
		Set<Immeuble> ret = new HashSet<Immeuble>();
		for ( Affectation affectation : getAffectations() ) {
			ret.add(affectation.getImmeuble());
		}
		return ret;
	}
	@Transient 
	public Set<Role> getRoles() {
		Set<Role> ret = new HashSet<Role>();
		for ( Affectation affectation : getAffectations() ) {
			ret.add(affectation.getRole());
		}
		return ret;
	}
	
	public String getRolesAsString() {
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for ( Role r : getRoles() ) {
			if ( first ) {
				ret.append(r.getId());
				first=false;
			} else {
				ret.append(", "+r.getId());
			}
		}
		return ret.toString();
	}

	
	public boolean is(String idRole) {
		return getRoles().contains(new Role(idRole));
	}
	
	public boolean isOnly(String idRole) {
		Role toCompare = new Role(idRole);
		if ( getRoles().isEmpty() ) {
			return false;
		}
		for ( Role role : getRoles() ) {
			if ( !role.equals(toCompare) ) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean match(Account account) {
		return account.equals(this);
	}

	
}
