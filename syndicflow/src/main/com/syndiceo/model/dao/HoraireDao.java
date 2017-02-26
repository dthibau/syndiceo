package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Horaire;

public class HoraireDao {

	private EntityManager entityManager;
	
	public HoraireDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Horaire> getAllHoraires() {
		Query q = entityManager.createQuery("from Horaire");
		return (List<Horaire>)q.getResultList();
	}
	
	
}
