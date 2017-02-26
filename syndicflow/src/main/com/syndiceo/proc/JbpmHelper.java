package com.syndiceo.proc;

import javax.persistence.EntityManagerFactory;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItemManager;


public interface JbpmHelper {
	
	public StatefulKnowledgeSession getKsession();
	
	public EntityManagerFactory getEntityManagerFactory();
	public long getRootProcessInstanceId(long processInstanceId);
	public WorkItemManager getWorkItemManager();
	
	
}