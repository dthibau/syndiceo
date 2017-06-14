package com.syndiceo.proc;

import java.util.ArrayList;
import java.util.List;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;

public class ProcessListener implements ProcessEventListener {

	private static ProcessListener instance;
	
	private ThreadLocal<Long> subProcessId = new ThreadLocal<Long>();
	private ThreadLocal<String> stateCode = new ThreadLocal<String>();
	private ThreadLocal<List<Long>> processesCompleted = new ThreadLocal<List<Long>>();


	
	private ProcessListener() {
		super();
	}
	
	public static ProcessListener getInstance() {
		if ( instance == null ) {
			instance = new ProcessListener();
		}
		return instance;
	}
	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		
		if ( event.getNodeInstance().getNodeName().startsWith("Annuler")) {
			stateCode.set("ANNULE");
		} 
		
	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		System.out.println("Node is "+event.getNodeInstance().getNodeName());
		if ( event.getNodeInstance().getNodeName().equals("Verifier la demande") ) {
			stateCode.set("QUALIFICATION");
		} else if ( event.getNodeInstance().getNodeName().equals("Completer la demande")) {
			stateCode.set("COMPLETER");
		}  else if ( event.getNodeInstance().getNodeName().equals("Planifier intervention")) {
			stateCode.set("A_PLANIFIER");
		} else if ( event.getNodeInstance().getNodeName().equals("Verifier intervention")) {
			stateCode.set("VERIFICATION");
		} else if ( event.getNodeInstance().getNodeName().equals("Replanifier intervention")) {
			stateCode.set("A_REPLANIFIER");
		} else if ( event.getNodeInstance().getNodeName().equals("INSCRIT AG")) {
			stateCode.set("ODJ_AG");
		} else if ( event.getNodeInstance().getNodeName().equals("CLOTURE")) {
			stateCode.set("CLOTURE");
		} else if ( event.getNodeInstance().getNodeName().equals("Annuler")) {
			stateCode.set("ANNULE");
		} else if ( event.getNodeInstance().getNodeName().equals("Verifier") ) {
			stateCode.set("EN_ATTENTE");
		} else if ( event.getNodeInstance().getNodeName().equals("Preciser") ) {
			stateCode.set("A_PRECISER");
		}
		System.out.println("State code is "+stateCode.get());

	}

	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		List<Long> pIds = processesCompleted.get();
		if ( pIds == null ) {
			pIds = new ArrayList<Long>();
		}
		pIds.add(event.getProcessInstance().getId());
		processesCompleted.set(pIds);

	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {

		
		subProcessId.set(event.getProcessInstance().getId());

	}

	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent arg0) {


	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
		

	}

	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
		

	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
		

	}

	@Override
	public void beforeProcessStarted(ProcessStartedEvent arg0) {
		

	}

	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent arg0) {
		// TODO Auto-generated method stub

	}

	public Long getSubProcessId() {
		return subProcessId.get();
	}

	public String getStateCode() {
		return stateCode.get();
	}

	public Boolean hasProcessCompleted(Long pId) {
		List<Long> pIds = processesCompleted.get();
		return pIds != null && pIds.contains(pId);
	}

	
}
