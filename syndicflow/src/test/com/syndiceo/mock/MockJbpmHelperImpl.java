package com.syndiceo.mock;

import javax.persistence.EntityManagerFactory;

import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.JbpmJUnitTestCase;
import org.jbpm.process.instance.ContextInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;

import com.syndiceo.proc.JbpmHelper;

public class MockJbpmHelperImpl extends JbpmJUnitTestCase implements JbpmHelper {
	KnowledgeRuntimeLogger logger;
	StatefulKnowledgeSession ksession;
	
	public MockJbpmHelperImpl () {
		super(false);
	}
	
	public void init() {
		
		ksession = createKnowledgeSession("main.bpmn", "validation.bpmn", "intervention.bpmn", "autre.bpmn");


		// Associer un logger à la session
		logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "syndiceo");
	}
	@Override
	public StatefulKnowledgeSession getKsession() {
		
		return ksession;
	}

	@Override
	public WorkItemManager getWorkItemManager() {
		return getKsession().getWorkItemManager();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		
		return null;
	}

	@Override
	public long getRootProcessInstanceId(long processInstanceId) {
		RuleFlowProcessInstance processInstance = (RuleFlowProcessInstance)ksession.getProcessInstance(processInstanceId);

		for ( ContextInstance cInst : processInstance.getContextInstances("VariableScope") ) {
			VariableScopeInstance vInst = (VariableScopeInstance)cInst; 
			if ( vInst.getVariable("top_id") != null ) {
				return (Long)vInst.getVariable("top_id");
			}
		}
		throw new RuntimeException("Unable to find top_id");
	}

}
