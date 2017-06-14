package com.syndiceo.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Event;
import com.syndiceo.model.Immeuble;

public class AutreDemandeDao {
	private EntityManager entityManager;

	public AutreDemandeDao() {
		super();
	}

	public AutreDemandeDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AutreDemande fullLoad(Long id) {
		Query q = entityManager
				.createQuery("from AutreDemande autreDemande left join fetch autreDemande.events where autreDemande.id = :id");
		q.setParameter("id", id);
		return (AutreDemande) q.getSingleResult();

	}

	@SuppressWarnings("unchecked")
	public List<AutreDemande> findAutreDemandes(List<Long> pids) {
		if (pids == null || pids.isEmpty()) {
			return new ArrayList<AutreDemande>();
		}
		Query q = entityManager
				.createQuery("from AutreDemande autreDemande where autreDemande.processInstanceId in (:pids) order by autreDemande.createdDate");
		q.setParameter("pids", pids);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AutreDemande> findUnArchived() {
		Query q = entityManager
				.createQuery("from AutreDemande autreDemande where autreDemande.archivedDate is null order by autreDemande.createdDate");

		return q.getResultList();
	}

	public List<AutreDemande> findUnArchived(Account account) {
		List<AutreDemande> autreDemandes = findUnArchived();
		List<AutreDemande> ret = new ArrayList<AutreDemande>();
		for (AutreDemande autreDemande : autreDemandes) {
			for (Event event : autreDemande.getEvents()) {
				if (event.isConcerned(account)) {
					ret.add(autreDemande);
					break;
				}
			}
		}
		return ret;
	}

	public List<AutreDemande> findUnArchivedByImmeubles(
			Collection<Immeuble> immeubles) {
		List<AutreDemande> autreDemandes = findUnArchived();
		List<AutreDemande> ret = new ArrayList<AutreDemande>();
		for (AutreDemande autreDemande : autreDemandes) {
			if (immeubles.contains(autreDemande.getImmeuble())) {
				ret.add(autreDemande);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<AutreDemande> findArchived() {
		Query q = entityManager
				.createQuery("from AutreDemande autreDemande where autreDemande.archivedDate != null order by autreDemande.archivedDate desc");

		return q.getResultList();
	}

	public List<AutreDemande> findArchived(Account account) {
		List<AutreDemande> autreDemandes = findArchived();
		List<AutreDemande> ret = new ArrayList<AutreDemande>();
		for (AutreDemande autreDemande : autreDemandes) {
			for (Event event : autreDemande.getEvents()) {
				if (event.isConcerned(account)) {
					ret.add(autreDemande);
					break;
				}
			}
		}
		return ret;
	}
	public List<AutreDemande> findArchivedByImmeubles(Account account) {
		List<AutreDemande> autreDemandes = findArchived();
		List<AutreDemande> ret = new ArrayList<AutreDemande>();
		Collection<Immeuble> immeubles = account.getImmeubles();
		for (AutreDemande autreDemande : autreDemandes) {
			if ( immeubles.contains(autreDemande.getImmeuble()) ) {
				ret.add(autreDemande);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<AutreDemande> findByStatus(String statusCode) {
		Query q = entityManager
				.createQuery("select autreDemande from AutreDemande autreDemande where autreDemande.statusCode = :statusCode");

		q.setParameter("statusCode", statusCode);

		return (List<AutreDemande>) (q.getResultList());
	}

}
