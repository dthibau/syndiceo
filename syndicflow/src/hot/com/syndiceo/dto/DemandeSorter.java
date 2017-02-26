package com.syndiceo.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.richfaces.component.SortOrder;

@Name("demandeSorter")
@Scope(ScopeType.CONVERSATION)
public class DemandeSorter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7195151251448467588L;

	@Out
	public Map<String,SortOrder> orders = new HashMap<String,SortOrder>();

    
    @Create
    public void init() {
    	orders.put(	"numero", SortOrder.unsorted);
    	orders.put(	"titre", SortOrder.unsorted);
    	orders.put(	"createdDate", SortOrder.unsorted);
    	orders.put(	"specialite", SortOrder.unsorted);
    	orders.put(	"critere", SortOrder.unsorted);
    	orders.put(	"demandeur", SortOrder.unsorted);
    	orders.put(	"planification", SortOrder.unsorted);
    	orders.put(	"immeuble", SortOrder.unsorted);
    	orders.put(	"status", SortOrder.unsorted);
    	
    }
    public void sortBy(String column) {
    	for ( String key : orders.keySet() ) {
    		if ( key.equals(column) ) {
    			if (orders.get(key).equals(SortOrder.ascending)) {
    	            orders.put(key,SortOrder.descending);
    	        } else {
    	        	orders.put(key,SortOrder.ascending);
    	        }
    		} else {
    			orders.put(key,SortOrder.unsorted);
    		}
    	}
    }
    
    
}
