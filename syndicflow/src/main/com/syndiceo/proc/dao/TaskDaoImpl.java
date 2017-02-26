package com.syndiceo.proc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.syndiceo.model.Account;
import com.syndiceo.model.dao.OrganizationalEntityDao;
import com.syndiceo.proc.JbpmHelper;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTask;
import com.syndiceo.proc.model.Task;

public class TaskDaoImpl implements TaskDao {

	JbpmHelper jbpmHelper;

	EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public JbpmHelper getJbpmHelper() {
		return jbpmHelper;
	}

	public void setJbpmHelper(JbpmHelper jbpmHelper) {
		this.jbpmHelper = jbpmHelper;
	}

	public void persist(Task task) {
		OrganizationalEntityDao oDao = new OrganizationalEntityDao(entityManager);
		try {
			oDao.synchronizeDirectory(task.getOwners());
			if ( task.getActorId() != null ) {
				oDao.synchronizeDirectory(new Account(task.getActorId()));
			}
			entityManager.persist(task);
		} finally {

		}

	}

	public void remove(Task task) {

		task = entityManager.find(Task.class, task.getId());
		entityManager.remove(task);

	}

	@Override
	public void removeAll(long processInstanceId) {
		List<Task> tasks = getTasks(processInstanceId);
		for ( Task task : tasks ) {
			remove(task);
		}
		
	}

	@Override
	public void removeAll(long processInstanceId, String taskCode) {
		List<Task> tasks = getTasks(processInstanceId);
		for ( Task task : tasks ) {
			if ( task.getCode().equals(taskCode) ) {
				remove(task);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeAllSub(long subProcessInstanceId) {
		Query q = entityManager
		.createQuery("select distinct t from Task t where t.subProcessInstanceId = :subProcessInstanceId");
		q.setParameter("subProcessInstanceId", subProcessInstanceId);
		List<Task> tasks = (List<Task>)q.getResultList();
		for ( Task task : tasks ) {
			remove(task);
		}
		
	}

	public void update(Task task) {
		OrganizationalEntityDao oDao = new OrganizationalEntityDao(entityManager);
		oDao.synchronizeDirectory(task.getOwners());
		oDao.synchronizeDirectory(new Account(task.getActorId()));
		entityManager.merge(task);

	}

	@SuppressWarnings("unchecked")
	public List<Task> getAllTasks() {

		Query q = entityManager
				.createQuery("select distinct t from Task t left join fetch t.owners order by t.ordre");
		return q.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getTasks(long processInstanceId) {
		Query q = entityManager
		.createQuery("select distinct t from Task t left join fetch t.owners where t.processInstanceId = :processInstanceId order by t.ordre");
		q.setParameter("processInstanceId", processInstanceId);

		return q.getResultList();
		
	}
	public Task getTask(long processInstanceId, String taskCode) {
		List<Task> tasks = getTasks(processInstanceId);
		for ( Task t : tasks ) {
			if ( t.getCode().equals(taskCode) ) {
				return t;
			}
		}
		return null;
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<LongTask> getAllLongTasks() {
		Query q = entityManager
				.createQuery("select distinct t from LongTask t left join fetch t.owners  order by t.ordre");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LongTask> getLongTasks(long processInstanceId) {
		Query q = entityManager
				.createQuery("select distinct t from LongTask t left join fetch t.owners where t.processInstanceId = :processInstanceId order by t.ordre");
		q.setParameter("processInstanceId", processInstanceId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DirectTask> getAllDirectTasks() {
		Query q = entityManager
				.createQuery("select distinct t from DirectTask t left join fetch t.owners order by t.ordre");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DirectTask> getDirectTasks(long processInstanceId) {
		Query q = entityManager
				.createQuery("select distinct t from DirectTask t left join fetch t.owners where t.processInstanceId = :processInstanceId order by t.ordre");
		q.setParameter("processInstanceId", processInstanceId);
		return q.getResultList();
	}


}
