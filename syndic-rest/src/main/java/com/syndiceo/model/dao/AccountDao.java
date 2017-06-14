package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Account;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Model;
import com.syndiceo.model.Role;

public class AccountDao {

	public static String FULL_LOAD = "select a from Account a left join fetch a.affectations where a.id=:id";
	public static String BY_ROLE = "select distinct a from Account a left join fetch a.affectations aff where aff.immeuble=:immeuble and aff.role = :role";
	public static String BY_ROLE2 = "select distinct a from Account a left join fetch a.affectations aff where aff.role = :role";
	private EntityManager entityManager;
	
	public AccountDao() {
		super();
	}
	public AccountDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@SuppressWarnings("unchecked")
	public List<Account> getContactsByRole(Immeuble immeuble, Role role) {
		Query q = entityManager.createQuery(BY_ROLE);
		q.setParameter("immeuble", immeuble);
		q.setParameter("role", role);
		return q.getResultList();
	}
	
	public Account loadAccount(String accountId) {
		Query q = entityManager.createQuery(FULL_LOAD);
		q.setParameter("id", accountId);
		return (Account)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Account> getAdministrateurs() {
		Query q = entityManager.createQuery(BY_ROLE2);
		q.setParameter("role", Model.ADMINISTRATEUR_ROLE);
		return q.getResultList();
	}
}
