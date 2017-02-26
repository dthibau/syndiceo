package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Critere;
import com.syndiceo.model.SousCritere;

public class CritereDao {

	private EntityManager entityManager;
	
	public CritereDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Critere> getAllCriteres() {
		Query q = entityManager.createQuery("from Critere");
		return (List<Critere>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SousCritere> getSousCriteres(Critere critere) {
		Query q = entityManager.createQuery("from SousCritere sc where sc.critere = :critere");
		q.setParameter("critere", critere);
		return (List<SousCritere>)q.getResultList();
	}
	
}
