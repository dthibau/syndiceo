package com.syndiceo.test;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.ProcessInstance;
import org.junit.Test;

import com.syndiceo.proc.TaskHandler;
import com.syndiceo.proc.model.Task;

public class TestDepot extends ProcessTest {

	   @Test
	   public void testBoucle() {
	       // Démarrer le processus 
	       ProcessInstance processInstance = ksession.startProcess("main",null);
	       Task task = taskHandler.findAssignedTask(conseil, "DEPOT", processInstance.getId());
	       Map<String,Object> results = new HashMap<String, Object>();
		   results.put("gestionnaire", gestionnaire);
		   results.put("conseil", conseil);
	       taskHandler.completeTask(conseil, task, results);
	       
	       // la demande est refusée par le gestionnaire
	       Task verifier = taskHandler.findAssignedTask(administrateur,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       verifier = taskHandler.findAssignedTask(gestionnaire,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       results = new HashMap<String, Object>();
	       results.put("gestionnaire", gestionnaire);
	       results.put("ret_depot", "NOK");
   		   taskHandler.completeTask(gestionnaire,verifier, results);
	       
	       assertNodeActive(processInstance.getId(), ksession, "Compléter la demande");
	       
	       Task modifier = taskHandler.findAssignedTask(conseil,"COMPLETE",processInstance.getId());
	       assertNotNull(modifier);
   		   taskHandler.completeTask(conseil,modifier, null );

	       
	       assertNodeActive(processInstance.getId(), ksession, "Vérifier la demande");

	       // Accepter la demande
	       verifier = taskHandler.findAssignedTask(gestionnaire,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       results = new HashMap<String, Object>();
	       results.put("ret_depot", "OK");
	       results.put("gestionnaire", gestionnaire);
	       results.put("type_travaux", "PETIT");
	       taskHandler.completeTask(gestionnaire,verifier, results );
	       
	       assertNodeActive(processInstance.getId(), ksession, "Planifier intervention");
	       Task editer = taskHandler.findAssignedTask(gestionnaire,"PLANIFY",processInstance.getId());
	       assertNotNull(editer);

		   
	   }

	   @Test
	   public void testModifier() {
	       // Démarrer le processus 
	       ProcessInstance processInstance = ksession.startProcess("main",null);
	       Task task = taskHandler.findAssignedTask(conseil, "DEPOT", processInstance.getId());
	       Map<String,Object> results = new HashMap<String, Object>();
		   results.put("gestionnaire", gestionnaire);
		   results.put("conseil", conseil);
	       taskHandler.completeTask(conseil, task, results);
	       
	       // Le conseil peut modifier sa demande
	       Task modifier = taskHandler.findAssignedTask(conseil,"UPDATE",processInstance.getId());
	       assertNotNull(modifier);
	       Task verifier = taskHandler.findAssignedTask(gestionnaire,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       results = new HashMap<String, Object>();
	       results.put("ret_depot", "UPDATE");
	       results.put("gestionnaire", gestionnaire);
   		   taskHandler.completeTask(conseil,modifier, results);
	       
	       assertNodeActive(processInstance.getId(), ksession, "Vérifier la demande", "Modifier la demande");
	       
	       // refus de la demande par l'gestionnaire
	       verifier = taskHandler.findAssignedTask(gestionnaire,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       results = new HashMap<String, Object>();
	       results.put("ret_depot", "NOK");
	       results.put("gestionnaire", gestionnaire);
	       taskHandler.completeTask(gestionnaire,verifier, results );
	       
	       assertNodeActive(processInstance.getId(), ksession, "Compléter la demande");
	       
	       Task completer = taskHandler.findAssignedTask(conseil,"COMPLETE",processInstance.getId());
	       assertNotNull(completer);
   		   taskHandler.completeTask(conseil,completer, null );

	       
	       assertNodeActive(processInstance.getId(), ksession, "Vérifier la demande", "Modifier la demande");

	       modifier = taskHandler.findAssignedTask(conseil,"UPDATE",processInstance.getId());
	       assertNotNull(modifier);
		   
	   }

	   @Test
	   public void testGestionnaire() {
	       // Démarrer le processus 
	       ProcessInstance processInstance = ksession.startProcess("main",null);
	       Task task = taskHandler.findAssignedTask(gestionnaire, "DEPOT", processInstance.getId());
	       Map<String,Object> results = new HashMap<String, Object>();
		   results.put("gestionnaire", gestionnaire);
		   results.put("conseil", conseil);
	       taskHandler.completeTask(gestionnaire, task, results);
	       
	       // la demande est acceptée par le gestionnaire
	       Task verifier = taskHandler.findAssignedTask(administrateur,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       verifier = taskHandler.findAssignedTask(gestionnaire,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       results = new HashMap<String, Object>();
	       results.put("gestionnaire", gestionnaire);
	       results.put("ret_depot", "OK");
	       results.put("type_travaux", "PETIT");
	       taskHandler.completeTask(gestionnaire,verifier, results);
	       
       
	       assertNodeActive(processInstance.getId(), ksession, "Planifier intervention");
	       Task editer = taskHandler.findAssignedTask(gestionnaire,"PLANIFY",processInstance.getId());
	       assertNotNull(editer);

		   
	   }

	   @Test
	   public void testAdministrateur() {
	       // Démarrer le processus 
	       ProcessInstance processInstance = ksession.startProcess("main",null);
	       Task task = taskHandler.findAssignedTask(administrateur, "DEPOT", processInstance.getId());
	       Map<String,Object> results = new HashMap<String, Object>();
		   results.put("gestionnaire", gestionnaire);
		   results.put("conseil", administrateur);
	       taskHandler.completeTask(administrateur, task, results);
	       
	       // la demande est acceptée par le gestionnaire
	       Task verifier = taskHandler.findAssignedTask(administrateur,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       verifier = taskHandler.findAssignedTask(gestionnaire,"CHECK",processInstance.getId());
	       assertNotNull(verifier);
	       results = new HashMap<String, Object>();
	       results.put("gestionnaire", gestionnaire);
	       results.put("ret_depot", "OK");
	       results.put("type_travaux", "PETIT");
	       taskHandler.completeTask(administrateur,verifier, results);
	       
       
	       assertNodeActive(processInstance.getId(), ksession, "Planifier intervention");
	       Task editer = taskHandler.findAssignedTask(administrateur,"PLANIFY",processInstance.getId());
	       assertNotNull(editer);

		   
	   }

}
