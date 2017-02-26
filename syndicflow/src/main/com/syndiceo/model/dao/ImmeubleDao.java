package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Account;
import com.syndiceo.model.Immeuble;

public class ImmeubleDao {

	private EntityManager entityManager;
	
	public ImmeubleDao() {
		super();
	}
	
	public ImmeubleDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Immeuble> findAll() {
		return entityManager.createQuery(" from Immeuble").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Immeuble> findByGestionnaire(Account gestionnaire) {
		Query q =  entityManager.createQuery(" from Immeuble i where i.gestionnaire = :gestionnaire");
		q.setParameter("gestionnaire", gestionnaire);
		return q.getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
}
