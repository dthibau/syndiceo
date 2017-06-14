package com.syndiceo.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Role")
public class Role extends OrganizationalEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6320628631528889722L;
	

	public Role() {
		super();
	}
	public Role(String id) {
		super(id);
	}

	@Override
	public boolean match(Account account) {
		return account.getRoles().contains(this);
	}
}
