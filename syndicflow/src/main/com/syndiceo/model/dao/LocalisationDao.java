package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Localisation;

public class LocalisationDao {

	private EntityManager entityManager;
	
	public LocalisationDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Localisation> getAllLocalisations() {
		Query q = entityManager.createQuery("from Localisation");
		return (List<Localisation>)q.getResultList();
	}
	
	
}
