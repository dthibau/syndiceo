package com.syndiceo.proc;

import javax.naming.InitialContext;
import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.process.instance.ContextInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.ruleflow.instance.RuleFlowProcessInstance;

/**
 * This is a sample file to launch a process.
 */
//@Name("jbpmHelper")
//@Scope(ScopeType.APPLICATION)
//@Startup
public class JbpmHelperImpl implements JbpmHelper {
	
	private static EntityManagerFactory entityManagerFactory;
	
	private static StatefulKnowledgeSession ksession;

	
//	@Create
	public void init() {
		try {

			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			entityManagerFactory = (EntityManagerFactory)new InitialContext().lookup("java:/syndicflowEntityManagerFactory");
			Environment env = KnowledgeBaseFactory.newEnvironment();
				env.set( EnvironmentName.ENTITY_MANAGER_FACTORY, entityManagerFactory );
//				env.set( EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());
//				UserTransaction ut = (UserTransaction) new InitialContext().lookup( "java:comp/UserTransaction" );
//				ut.begin();

				try {
			ksession = JPAKnowledgeService.loadStatefulKnowledgeSession( 
                 1, kbase, null, env );
				} catch ( RuntimeException e) {
						ksession = JPAKnowledgeService.newStatefulKnowledgeSession( kbase, null, env );
				}
			ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
					TaskHandler.getInstance());
			ksession.addEventListener(ProcessListener.getInstance());
//			ut.commit();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public StatefulKnowledgeSession getKsession() {
		if ( ksession == null ) {
			init();
		}
		return ksession;
	}

	@Override
	public WorkItemManager getWorkItemManager() {
		
		return getKsession().getWorkItemManager();
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("main.bpmn"), ResourceType.BPMN2);
		kbuilder.add(ResourceFactory.newClassPathResource("validation.bpmn"), ResourceType.BPMN2);
		kbuilder.add(ResourceFactory.newClassPathResource("intervention.bpmn"), ResourceType.BPMN2);
		kbuilder.add(ResourceFactory.newClassPathResource("autre.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}


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