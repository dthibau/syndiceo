package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.SousSpecialite;
import com.syndiceo.model.Specialite;

public class SpecialiteDao {

	private EntityManager entityManager;
	
	public SpecialiteDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<Specialite> getAllSpecialites() {
		Query q = entityManager.createQuery("from Specialite");
		return (List<Specialite>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SousSpecialite> getSousSpecialites(Specialite specialite) {
		Query q = entityManager.createQuery("from SousSpecialite ss where ss.specialite = :specialite");
		q.setParameter("specialite", specialite);
		return (List<SousSpecialite>)q.getResultList();
	}
	
}
