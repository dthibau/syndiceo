package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;

import com.syndiceo.proc.model.Task;

public class TestCompleter extends ProcessTest {

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

			// la demande est refusée par le gestionnaire
			Task verifier = taskHandler.findAssignedTask(gestionnaire, "CHECK",
					processInstance.getId());
			assertNotNull(verifier);
			results = new HashMap<String, Object>();
			results.put("gestionnaire", gestionnaire);
			results.put("ret_depot", "NOK");
			taskHandler.completeTask(gestionnaire, verifier, results);

			assertNodeActive(processInstance.getId(), ksession, "Compléter la demande");
			
			verifier = taskHandler.findAssignedTask(gestionnaire, "CHECK",
					processInstance.getId());
			assertNull(verifier);

			Task completer = taskHandler.findAssignedTask(conseil, "COMPLETE",
					processInstance.getId());
			assertNotNull(completer);
			taskHandler.completeTask(conseil, completer, null);

			assertNodeActive(processInstance.getId(), ksession, "Vérifier la demande");

			verifier = taskHandler.findAssignedTask(gestionnaire, "CHECK",
					processInstance.getId());
			assertNotNull(verifier);


		   
	   }


	   

}
