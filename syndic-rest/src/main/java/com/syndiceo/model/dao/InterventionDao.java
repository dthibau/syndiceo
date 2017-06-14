package com.syndiceo.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import com.syndiceo.model.Account;
import com.syndiceo.model.Event;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Intervention;

public class InterventionDao {
	private EntityManager entityManager;

	public InterventionDao() {
		super();
	}

	public InterventionDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Intervention fullLoad(Long id) {
		Query q = entityManager
				.createQuery("from Intervention intervention left join fetch intervention.events where intervention.id = :id");
		q.setParameter("id", id);
		Intervention ret = (Intervention) q.getSingleResult();
		Hibernate.initialize(ret.getConseils()); // Needed for displaying panel "Modifier les destinataires" from liste
//		Hibernate.initialize(ret.getPlanifications());
		Hibernate.initialize(ret.getPiecesJointes());
		for ( Event e : ret.getEvents() ) {
			Hibernate.initialize(e.getFichiers());
			Hibernate.initialize(e.getDestinataires());
			Hibernate.initialize(e.getPourInfo());
		}
		
		return ret;

	}

	@SuppressWarnings("unchecked")
	public List<Intervention> findInterventions(List<Long> pids) {
		if (pids == null || pids.isEmpty()) {
			return new ArrayList<Intervention>();
		}
		Query q = entityManager
				.createQuery("from Intervention intervention where intervention.processInstanceId in (:pids) order by intervention.dateSouhaitee");
		q.setParameter("pids", pids);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Intervention> findUnArchived() {
		Query q = entityManager
				.createQuery("from Intervention intervention where intervention.archivedDate is null order by intervention.dateSouhaitee");

		return q.getResultList();
	}

	public List<Intervention> findUnArchived(Account account) {
		List<Intervention> interventions = findUnArchived();
		List<Intervention> ret = new ArrayList<Intervention>();
		for (Intervention intervention : interventions) {
			for (Event event : intervention.getEvents()) {
				if (event.isConcerned(account)) {
					ret.add(intervention);
					break;
				}
			}
		}
		return ret;
	}

	public List<Intervention> findUnArchivedByImmeubles(
			Collection<Immeuble> immeubles) {
		List<Intervention> interventions = findUnArchived();
		List<Intervention> ret = new ArrayList<Intervention>();
		for (Intervention intervention : interventions) {
			if (immeubles.contains(intervention.getImmeuble())) {
				ret.add(intervention);
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Intervention> findArchived() {
		Query q = entityManager
				.createQuery("from Intervention intervention where intervention.archivedDate != null order by intervention.archivedDate desc");

		return q.getResultList();
	}

	public List<Intervention> findArchived(Account account) {
		List<Intervention> interventions = findArchived();
		List<Intervention> ret = new ArrayList<Intervention>();
		for (Intervention intervention : interventions) {
			for (Event event : intervention.getEvents()) {
				if (event.isConcerned(account)) {
					ret.add(intervention);
					break;
				}
			}
		}
		return ret;
	}

	public List<Intervention> findArchivedByImmeubles(Account account) {
		List<Intervention> interventions = findArchived();
		List<Intervention> ret = new ArrayList<Intervention>();
		Collection<Immeuble> immeubles = account.getImmeubles();
		for (Intervention intervention : interventions) {
			if (!account.is("r_copro") || !intervention.getConfidentielle()) {
				if (immeubles.contains(intervention.getImmeuble())) {
					ret.add(intervention);
				}
			}
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Intervention> findByStatus(String statusCode) {
		Query q = entityManager
				.createQuery("select intervention from Intervention intervention where intervention.statusCode = :statusCode");

		q.setParameter("statusCode", statusCode);

		return (List<Intervention>) (q.getResultList());
	}

}
