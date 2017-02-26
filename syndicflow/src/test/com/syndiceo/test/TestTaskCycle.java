package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;

import com.syndiceo.proc.model.Task;

public class TestTaskCycle extends ProcessTest {

	public void testLongTask() {
	       // Démarrer le processus 
		   Map<String,Object> params = new HashMap<String, Object>();
		   params.put("Demandeur", conseil);
	       ProcessInstance processInstance = ksession.startProcess("top",params);
	       Task t = taskHandler.findAssignedTask(administrateur, "DEPOT", processInstance.getId());
	       Map<String,Object> results = new HashMap<String, Object>();
		   results.put("typeDemande", "Beer");
		   results.put("ut", gestionnaire);
		   results.put("demandeur", administrateur);
	       taskHandler.completeTask(administrateur, t, results);
	       
	       // Tester les noeuds actifs
	       assertNodeActive(processInstance.getId(), ksession, "Annuler (DRTPE)","Annuler (Demandeur)");
	       
	

	}
	
}
