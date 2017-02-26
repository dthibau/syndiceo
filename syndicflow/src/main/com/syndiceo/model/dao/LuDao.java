package com.syndiceo.model.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Account;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Lu;
import com.syndiceo.model.Model;
import com.syndiceo.model.Role;

public class LuDao {

	public static String FULL_LOAD = "select a from Account a left join fetch a.affectations where a.id=:id";
	public static String BY_ROLE = "select distinct a from Account a left join fetch a.affectations aff where aff.immeuble=:immeuble and aff.role = :role";
	public static String BY_ROLE2 = "select distinct a from Account a left join fetch a.affectations aff where aff.role = :role";
	private EntityManager entityManager;
	
	public LuDao() {
		super();
	}
	public LuDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Lu get(Account account, Demande demande) {
		Query q = entityManager.createQuery("select l from Lu l where l.account=:account and l.demande=:demande");
		q.setParameter("account", account);
		q.setParameter("demande", demande);
		try {
			return (Lu)q.getSingleResult();
		} catch(Exception e) {
			return null;
		}
	}
	public void set(Account account, Demande demande) {
		Lu lu = get(account, demande);
		if ( lu != null ) {
			lu.setDate(new Date());
		} else {
			lu = new Lu();
			lu.setAccount(account);
			lu.setDemande(demande);
			lu.setDate(new Date());
			entityManager.persist(lu);
		}

	}
}
