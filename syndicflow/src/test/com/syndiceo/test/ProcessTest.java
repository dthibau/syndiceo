package com.syndiceo.test;

import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.JbpmJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.syndiceo.model.Account;
import com.syndiceo.model.Affectation;
import com.syndiceo.model.Immeuble;
import com.syndiceo.model.Role;
import com.syndiceo.proc.JbpmHelper;
import com.syndiceo.proc.ProcessListener;
import com.syndiceo.proc.TaskHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public abstract class ProcessTest extends JbpmJUnitTestCase {

	// Spring bean
	@Autowired
	protected TaskHandler taskHandler;
	
	@Autowired
	protected JbpmHelper jbpmHelper;

	
	protected static Role ADMINISTRATEUR, CONSEIL, GESTIONNAIRE, COPRO;
	protected Account administrateur,conseil,gestionnaire,gestionnaire2,copro;
	protected Immeuble immeuble;

	KnowledgeRuntimeLogger logger;
	StatefulKnowledgeSession ksession;
	
	public ProcessTest() {
		super(false);
	}

	
	@Before
	public void setUp() {
		super.setUp();
//		// Initialize Spring
//		ApplicationContext ctx = 
//			new ClassPathXmlApplicationContext("spring-test.xml");
		
		
		initUsers();

		ksession = jbpmHelper.getKsession();
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
				taskHandler);
		ksession.addEventListener(ProcessListener.getInstance());

		// Associer un logger à la session
		logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "ar-circ");
	}


	@After
	public void tearDown() {
		logger.close();
	}



	
	protected void initUsers() {
		ADMINISTRATEUR = new Role("r_admin");
		
		immeuble = new Immeuble();
		immeuble.setNumero("1");
		
		CONSEIL = new Role("r_conseil");
		GESTIONNAIRE = new Role("r_gestionnaire");
		administrateur = new Account("administrateur");
		Affectation aff = new Affectation(administrateur,ADMINISTRATEUR,null);
		administrateur.addAffectation(aff);

		conseil = new Account("conseil");
		aff = new Affectation(conseil,CONSEIL,immeuble);
		conseil.addAffectation(aff);
	
		gestionnaire = new Account("gestionnaire");
		aff = new Affectation(gestionnaire,GESTIONNAIRE,immeuble);
		gestionnaire.addAffectation(aff); 
		
		gestionnaire2 = new Account("gestionnaire2");
		aff = new Affectation(gestionnaire2,GESTIONNAIRE,immeuble);
		gestionnaire2.addAffectation(aff); 
		
		COPRO = new Role("r_copro");
		copro = new Account("copro");
		aff = new Affectation(copro,COPRO,immeuble);
		copro.addAffectation(aff); 
	}
	

}
