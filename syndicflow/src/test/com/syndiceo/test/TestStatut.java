package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;

import com.syndiceo.proc.ProcessListener;
import com.syndiceo.proc.model.Task;

public class TestStatut extends ProcessTest {

	@Test
	public void testAllStatuts() {
		// Démarrer le processus
		ProcessInstance processInstance = ksession.startProcess("top", null);
		Task task = taskHandler.findAssignedTask(gestionnaire, "DEPOT",
				processInstance.getId());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("typeDemande", "Demandeur");
		results.put("demandeur", conseil);
		results.put("gestionnaire", gestionnaire);
		taskHandler.completeTask(gestionnaire, task, results);

		assertTrue(ProcessListener.getInstance().getStateCode()
				.equals("QUALIFICATION"));

		Task check = taskHandler.findAssignedTask(gestionnaire, "CHECK",
				processInstance.getId());
		results = new HashMap<String, Object>();
		results.put("ret_depot", "OK");
		results.put("gestionnaire", gestionnaire);
		taskHandler.completeTask(gestionnaire, check, results);

		assertTrue(ProcessListener.getInstance().getStateCode()
				.equals("INSTRUCTION"));

		Task edit = taskHandler.findAssignedTask(gestionnaire, "EDIT",processInstance.getId());
		assertNotNull(edit);
		taskHandler.completeTask(gestionnaire, edit, null);

		assertTrue(ProcessListener.getInstance().getStateCode().equals("VERIFICATION"));

		Task verifier = taskHandler.findAssignedTask(administrateur,"VERIFICATION_BEER", processInstance.getId());
		assertNotNull(verifier);
		results = new HashMap<String, Object>();
		results.put("ret_beer", "OK");
		taskHandler.completeTask(administrateur, verifier, results);

		assertTrue(ProcessListener.getInstance().getStateCode().equals("ENREGISTREMENT"));


		Task publier = taskHandler.findAssignedTask(administrateur, "PUBLIER",processInstance.getId());
		assertNotNull(publier);
		taskHandler.completeTask(administrateur, publier, null);

		assertTrue(ProcessListener.getInstance().getStateCode().equals("PUBLIE"));
		
		Task prolonger = taskHandler.findAssignedTask(conseil, "PROLONGE",processInstance.getId());
		assertNotNull(prolonger);
		taskHandler.completeTask(conseil, prolonger, null);

		assertTrue(ProcessListener.getInstance().getStateCode().equals("PROLONGATION"));
		
	}

	@Test
	public void testAutreAllStatuts() {
		// Démarrer le processus
		ProcessInstance processInstance = ksession.startProcess("autre", null);
		Task task = taskHandler.findAssignedTask(copro, "AUTRE_DEPOT",
				processInstance.getId());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("demandeur", copro);
		results.put("gestionnaire", gestionnaire);
		taskHandler.completeTask(gestionnaire, task, results);

		assertTrue(ProcessListener.getInstance().getStateCode()
				.equals("EN_ATTENTE"));

		Task check = taskHandler.findAssignedTask(gestionnaire, "AUTRE_CHECK",
				processInstance.getId());
		results = new HashMap<String, Object>();
		results.put("ret_depot", "NOK");
		taskHandler.completeTask(gestionnaire, check, results);

		assertTrue(ProcessListener.getInstance().getStateCode()
				.equals("A_PRECISER"));

		Task preciser = taskHandler.findAssignedTask(copro, "AUTRE_PRECISER",processInstance.getId());
		assertNotNull(preciser);
		taskHandler.completeTask(copro, preciser, null);

		assertTrue(ProcessListener.getInstance().getStateCode().equals("EN_ATTENTE"));


		check = taskHandler.findAssignedTask(gestionnaire, "AUTRE_CHECK",processInstance.getId());
		assertNotNull(check);
		results = new HashMap<String, Object>();
		results.put("ret_depot", "OK");
		taskHandler.completeTask(gestionnaire, check, results);

		assertTrue(ProcessListener.getInstance().getStateCode().equals("CLOTURE"));
		
				
	}

}
