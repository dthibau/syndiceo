package com.syndiceo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.syndiceo.model.Critere;
import com.syndiceo.model.Horaire;
import com.syndiceo.model.Localisation;
import com.syndiceo.model.Model;
import com.syndiceo.model.Role;
import com.syndiceo.model.Specialite;
import com.syndiceo.model.dao.CritereDao;
import com.syndiceo.model.dao.HoraireDao;
import com.syndiceo.model.dao.LocalisationDao;
import com.syndiceo.model.dao.SpecialiteDao;
import com.syndiceo.proc.SpringConf;
import com.syndiceo.proc.TaskButtonConf;
import com.syndiceo.proc.TaskConf;

@Component
public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2206967089369044527L;

	@Autowired
	SpringConf springConf;
	
	private Date lastAction=new Date();
	
	// Sauvegarde intermédiaire	
	public final static int T_SAVE=1;
	// Fin de la tâche
	public final static int T_COMPLETE=2;
	// Envoi de message
	public final static int T_MSG=3;
	public final static int T_FORK=4;
	// Mise à jour des destinataires
	public final static int T_UPDATE_DEST=5;
	public final static int F_DEMANDE =0;
	public final static int F_EDITEUR =1;
	public final static int F_AUTRE_DEMANDE =2;


	// Dialog forms	
	public final static int DF_DEFAULT =0;
	public final static int DF_COMMENT_REQUIRED =1;
	public final static int DF_PLANIFICATION =2;
	public final static int DF_NONCONFORME =3;
	public final static int DF_MSG =6;
	public final static int DF_UPDATE_DEST =7;
	//Roles 
	public final static Role CONSEIL_ROLE = Model.CONSEIL_ROLE;
	public final static Role GESTIONNAIRE_ROLE = Model.GESTIONNAIRE_ROLE;
	public final static Role ADMINISTRATEUR_ROLE = Model.ADMINISTRATEUR_ROLE;
	public final static Role COPRO_ROLE = Model.COPRO_ROLE;
	public final static Role SYSTEM_ROLE = Model.SYSTEM_ROLE;


	// Mode d'une demande
	public final static int M_VISU = 0;
	public final static int M_EDIT_DEMANDE = 1;
	public final static int M_EDIT_INTERVENTION = 2;

	
	
	public final static ResourceBundle msgBundle = ResourceBundle.getBundle("labels");
	public final static ResourceBundle mimeTypesBundle = ResourceBundle.getBundle("mime-type");
	public final static ResourceBundle projectBundle = ResourceBundle.getBundle("syndicflow");
	
	public static Map<String,TaskConf> taskConf;

	
	public final static DateFormat dateFormat = new SimpleDateFormat(projectBundle.getString("dateFormat"));
	public final static String SYSTEM_ACCOUNT = projectBundle.getString("SYSTEM_ACCOUNT");
	public final static boolean MAIL_ENABLED = Boolean.parseBoolean(projectBundle.getString("MAIL_ENABLED"));
	public final static String ADMIN_EMAIL = projectBundle.getString("ADMIN_EMAIL");



	List<Specialite> specialites;
	

	List<Horaire> horaires;


	List<Localisation> localisations;
	

	List<Critere> criteres;
	
	

	EntityManager entityManager;
	

	


	public void init() {
		taskConf = springConf.getTaskConf();
	}
	

	public void initSpecialites() {
		SpecialiteDao dao = new SpecialiteDao(entityManager);
		specialites = dao.getAllSpecialites();
		
	}
	public void refreshSepecialites() {
		specialites = null;
	}

	public void initCriteres() {
		CritereDao dao = new CritereDao(entityManager);
		criteres = dao.getAllCriteres();
		
	}
	public void refreshCriteres() {
		criteres = null;
	}

	public void initHoraires() {
		HoraireDao dao = new HoraireDao(entityManager);
		horaires = dao.getAllHoraires();
		
	}
	public void refreshHoraires() {
		horaires = null;
	}

	public void initLocalisation() {
		LocalisationDao dao = new LocalisationDao(entityManager);
		localisations = dao.getAllLocalisations();
		
	}

	public void refreshLocalisations() {
		localisations = null;
	}
	

	public TaskButtonConf getButtonConf(String labelCode) {
		for ( TaskConf taskConf : springConf.getTaskConf().values() ) {
			for ( TaskButtonConf taskButtonConf : taskConf.getButtonConfs() ) {
				if ( taskButtonConf.getLabelCode().equals(labelCode) ) {
					return taskButtonConf;
				}
			}
		}
		return null;
	}
}
