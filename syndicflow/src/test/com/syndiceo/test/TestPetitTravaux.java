package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;

import com.syndiceo.proc.model.Task;

public class TestPetitTravaux extends ProcessTest {

	 @Test
	   public void testBoucle() {
			// Démarrer le processus
			ProcessInstance processInstance = ksession.startProcess("main", null);
			Task task = taskHandler.findAssignedTask(conseil, "DEPOT",
					processInstance.getId());
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("gestionnaire", gestionnaire);
			results.put("conseil", conseil);
			taskHandler.completeTask(conseil, task, results);

			// la demande est acceptée par le gestionnaire
			Task verifier = taskHandler.findAssignedTask(gestionnaire, "CHECK",
					processInstance.getId());
			assertNotNull(verifier);
			results = new HashMap<String, Object>();
			results.put("ret_depot", "OK");
			results.put("type_travaux", "PETIT");
			taskHandler.completeTask(gestionnaire, verifier, results);

			assertNodeActive(processInstance.getId(), ksession, "Planifier intervention");

			Task planify = taskHandler.findAssignedTask(gestionnaire, "PLANIFY",
					processInstance.getId());
			assertNotNull(planify);
			taskHandler.completeTask(gestionnaire, planify, null);

			assertNodeActive(processInstance.getId(), ksession, "Vérifier intervention");

			Task check = taskHandler.findAssignedTask(gestionnaire, "CHECK_INTERVENTION",
					processInstance.getId());
			results = new HashMap<String, Object>();
			results.put("ret_intervention", "NOK");
			assertNotNull(check);
			taskHandler.completeTask(gestionnaire, check, results);

			assertNodeActive(processInstance.getId(), ksession, "Replanifier intervention");

			planify = taskHandler.findAssignedTask(gestionnaire, "REPLANIFY",
					processInstance.getId());
			assertNotNull(check);
			taskHandler.completeTask(gestionnaire, planify, null);
			
			assertNodeActive(processInstance.getId(), ksession, "Vérifier intervention");

			check = taskHandler.findAssignedTask(gestionnaire, "CHECK_INTERVENTION",
					processInstance.getId());
			results = new HashMap<String, Object>();
			results.put("ret_intervention", "OK");
			assertNotNull(check);
			taskHandler.completeTask(gestionnaire, check, results);

			
			// Tester la fin du processus
			assertProcessInstanceCompleted(processInstance.getId(), ksession);
		   
	   }


	   

}
