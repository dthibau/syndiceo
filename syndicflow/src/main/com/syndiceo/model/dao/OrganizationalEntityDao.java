package com.syndiceo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.syndiceo.model.Account;
import com.syndiceo.model.OrganizationalEntity;

public class OrganizationalEntityDao {
	private EntityManager entityManager;
	
	public OrganizationalEntityDao() {
		super();
	}
	public OrganizationalEntityDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	public void synchronizeDirectory(List<OrganizationalEntity> accounts) {
		for (OrganizationalEntity account : accounts) {
			synchronizeDirectory(account);
		}
	}
	public void synchronizeAccountDirectory(List<Account> accounts) {
		for (Account account : accounts) {
			synchronizeDirectory(account);
		}
	}
	public void synchronizeDirectory(OrganizationalEntity account) {
		if (entityManager.find(OrganizationalEntity.class, account.getId()) == null) {
			entityManager.persist(account);
		}

	}

}
