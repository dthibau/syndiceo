package com.syndiceo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Affectation implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5855243327488628206L;
	
	@Id @GeneratedValue
	private long id;
	@ManyToOne
	@JsonManagedReference
	private Account account;
	@ManyToOne
	private Role role;
	@ManyToOne
	private Immeuble immeuble;
	
	public Affectation() {
		super();
	}
	public Affectation(Account account, Role role, Immeuble immeuble) {
		this.account = account;
		this.role = role;
		this.immeuble = immeuble;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Immeuble getImmeuble() {
		return immeuble;
	}
	public void setImmeuble(Immeuble immeuble) {
		this.immeuble = immeuble;
	}
	
	
	
}
