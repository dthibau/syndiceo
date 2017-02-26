package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.Task;

@ContextConfiguration
public class TestCancel extends ProcessTest {

	@Test
	public void testAdministrateurCancel() {
		// Démarrer le processus
		ProcessInstance processInstance = ksession.startProcess("main", null);

		// Tester les noeuds actifs
		assertNodeActive(processInstance.getId(), ksession, "Deposer demande");
		// Tester si le conseil peut déposer
		Task task = taskHandler.findAssignedTask(conseil, "DEPOT",
				processInstance.getId());
		assertNotNull(task);

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gestionnaire", gestionnaire);
		results.put("conseil", conseil);
		taskHandler.completeTask(conseil, task, results);

		// Tester si l'administrateur peut annuler
		// assertNodeActive(processInstance.getId(), ksession,
		// "Annuler (BEER)");
		Task cancelTask = taskHandler.findAssignedTask(administrateur,
				"CANCEL", processInstance.getId());
		assertNotNull(cancelTask);

		taskHandler.completeTask(administrateur, cancelTask, null);

		// Tester la fin du processus
		assertProcessInstanceCompleted(processInstance.getId(), ksession);

	}

	@Test
	public void testGestionnaireAnnulle() {
		// Démarrer le processus
		ProcessInstance processInstance = ksession.startProcess("main", null);

		// Tester les noeuds actifs
		assertNodeActive(processInstance.getId(), ksession, "Déposer demande");
		// Tester si le conseil peut déposer
		Task task = taskHandler.findAssignedTask(conseil, "DEPOT",
				processInstance.getId());
		assertNotNull(task);

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gestionnaire", gestionnaire);
		results.put("conseil", conseil);
		taskHandler.completeTask(conseil, task, results);

		// Tester si le gestionnaire peut annuler
		// assertNodeActive(processInstance.getId(), ksession, "Annulation");
		Task cancelTask = taskHandler.findAssignedTask(gestionnaire, "CANCEL",
				processInstance.getId());
		assertNull(cancelTask);


	}

	@Test
	public void testConseilCancel() {
		// Démarrer le processus
		ProcessInstance processInstance = ksession.startProcess("main", null);
		Task task = taskHandler.findAssignedTask(conseil, "DEPOT",
				processInstance.getId());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("gestionnaire", gestionnaire);
		results.put("conseil", conseil);
		taskHandler.completeTask(conseil, task, results);

		// Le conseil peut-il toujours annuler .?
		Task annuler = taskHandler.findAssignedTask(conseil, "CANCEL",
				processInstance.getId());
		assertNotNull(annuler);

		// la demande est acceptée par l'ut
		Task verifier = taskHandler.findAssignedTask(gestionnaire, "CHECK",
				processInstance.getId());
		assertNotNull(verifier);
		results = new HashMap<String, Object>();
		results.put("ret_depot", "OK");
		results.put("type_travaux", "PETIT");
		taskHandler.completeTask(gestionnaire, verifier, results);

		// Le conseil peut-il toujours annuler .?
		annuler = taskHandler.findAssignedTask(conseil, "CANCEL",
				processInstance.getId());
		assertNotNull(annuler);
		taskHandler.completeTask(conseil, annuler, null);

		// Tester la fin du processus
		assertProcessInstanceCompleted(processInstance.getId(), ksession);

	}

}
