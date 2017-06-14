package com.syndiceo.proc.model;

import java.util.Map;

import com.syndiceo.model.Account;
import com.syndiceo.model.Demande;

public class TaskResult {

	private Account gestionnaire;
	
	public TaskResult(Demande demande) {
		this.gestionnaire = demande.getImmeuble().getGestionnaire();
	}
	
	public Map<String,Object> merge(Map<String,Object> results, Demande demande) {
		if ( results != null && results.get("task.gestionnaire") != null && getGestionnaire() != null) {
			results.put("gestionnaire", getGestionnaire());
		}
		
		return results;
	}

	public Account getGestionnaire() {
		return gestionnaire;
	}

	public void setGestionnaire(Account gestionnaire) {
		this.gestionnaire = gestionnaire;
	}


	
	
}
