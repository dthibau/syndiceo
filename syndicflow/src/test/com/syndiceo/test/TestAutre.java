package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;

import com.syndiceo.proc.model.Task;

public class TestAutre extends ProcessTest {

	 @Test
	   public void testBoucle() {
			// Démarrer le processus
			ProcessInstance processInstance = ksession.startProcess("autre", null);
			Task task = taskHandler.findAssignedTask(copro, "AUTRE_DEPOT",
					processInstance.getId());
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("gestionnaire", gestionnaire);
			results.put("demandeur", copro);
			taskHandler.completeTask(copro, task, results);

			// la demande est refusée par le gestionnaire
			Task check = taskHandler.findAssignedTask(gestionnaire, "AUTRE_CHECK",
					processInstance.getId());
			Task cancel = taskHandler.findAssignedTask(gestionnaire, "AUTRE_CANCEL",
					processInstance.getId());	
		
			assertNotNull(check);
			assertNotNull(cancel);

			results = new HashMap<String, Object>();
			results.put("ret_depot", "NOK");
			taskHandler.completeTask(gestionnaire, check, results);

			assertNodeActive(processInstance.getId(), ksession, "Preciser");
			
			Task precision = taskHandler.findAssignedTask(copro, "AUTRE_PRECISER",
					processInstance.getId());
			assertNotNull(precision);

			taskHandler.completeTask(copro, precision, null);

			check = taskHandler.findAssignedTask(gestionnaire, "AUTRE_CHECK",
					processInstance.getId());
			assertNotNull(check);
			results = new HashMap<String, Object>();
			results.put("ret_depot", "OK");
			taskHandler.completeTask(gestionnaire, check, results);
			
			// Tester la fin du processus
			assertProcessInstanceCompleted(processInstance.getId(), ksession);


		   
	   }


	   

}
