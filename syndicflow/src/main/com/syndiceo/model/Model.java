package com.syndiceo.model;

import java.io.Serializable;

public class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2206967089369044527L;

	//Roles 
	public final static Role CONSEIL_ROLE = new Role("r_conseil");
	public final static Role GESTIONNAIRE_ROLE = new Role("r_gestionnaire");
	public final static Role ADMINISTRATEUR_ROLE = new Role("r_admin");
	public final static Role COPRO_ROLE = new Role("r_copro");
	public final static Role SYSTEM_ROLE = new Role("r_system");




}
