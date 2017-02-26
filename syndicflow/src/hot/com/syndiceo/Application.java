package com.syndiceo;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;
import org.richfaces.component.SortOrder;

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
import com.syndiceo.util.RequestHandler;
import com.syndiceo.util.UTF8ResourceBundle;

@Scope(ScopeType.APPLICATION)
@Name("application")
@Startup
public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2206967089369044527L;

	@In("#{springConf}")
	SpringConf springConf;
	
	@Out(required=true)
	private Date lastAction=new Date();
	
	// Sauvegarde intermédiaire
	@Out
	public final static int T_SAVE=1;
	// Fin de la tâche
	@Out
	public final static int T_COMPLETE=2;
	// Envoi de message
	@Out
	public final static int T_MSG=3;
	@Out
	public final static int T_FORK=4;
	// Mise à jour des destinataires
	@Out
	public final static int T_UPDATE_DEST=5;
	@Out
	public final static int F_DEMANDE =0;
	@Out
	public final static int F_EDITEUR =1;
	@Out
	public final static int F_AUTRE_DEMANDE =2;
//	@Out
//	public final static int F_DRTPE =2;
//	@Out
//	public final static int F_DIFFUSION =3;


	// Dialog forms
	@Out
	public final static int DF_DEFAULT =0;
	@Out
	public final static int DF_COMMENT_REQUIRED =1;
	@Out
	public final static int DF_PLANIFICATION =2;
	@Out
	public final static int DF_NONCONFORME =3;
	@Out
	public final static int DF_MSG =6;
	@Out
	public final static int DF_UPDATE_DEST =7;
	//Roles 
	@Out
	public final static Role CONSEIL_ROLE = Model.CONSEIL_ROLE;
	@Out
	public final static Role GESTIONNAIRE_ROLE = Model.GESTIONNAIRE_ROLE;
	@Out
	public final static Role ADMINISTRATEUR_ROLE = Model.ADMINISTRATEUR_ROLE;
	@Out
	public final static Role COPRO_ROLE = Model.COPRO_ROLE;
	@Out
	public final static Role SYSTEM_ROLE = Model.SYSTEM_ROLE;


	// Mode d'une demande
	@Out
	public final static int M_VISU = 0;
	@Out
	public final static int M_EDIT_DEMANDE = 1;
	
	@Out
	public final static int M_EDIT_INTERVENTION = 2;

	@Out
	public final static SortOrder ASCENDING=SortOrder.ascending;
	@Out
	public final static SortOrder DESCENDING=SortOrder.descending;
	
	public final static ResourceBundle msgBundle = ResourceBundle.getBundle("labels");
	public final static ResourceBundle mimeTypesBundle = UTF8ResourceBundle.getBundle("mime-type");
	public final static ResourceBundle projectBundle = UTF8ResourceBundle.getBundle("syndicflow");
	
	@Out(required = false)
	public static Map<String,TaskConf> taskConf;

	@Out
	public final static DateFormat dateFormat = new SimpleDateFormat(projectBundle.getString("dateFormat"));
	public final static String SYSTEM_ACCOUNT = projectBundle.getString("SYSTEM_ACCOUNT");
	public final static boolean MAIL_ENABLED = Boolean.parseBoolean(projectBundle.getString("MAIL_ENABLED"));
	public final static String ADMIN_EMAIL = projectBundle.getString("ADMIN_EMAIL");


	@Out(required=false)
	List<Specialite> specialites;
	
	@Out(required=false)
	List<Horaire> horaires;

	@Out(required=false)
	List<Localisation> localisations;
	
	@Out(required=false)
	List<SelectItem> stateSelectItems;
	
	@Out(required=false)
	List<Critere> criteres;
	
	
	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	

	@Create
	public void init() {
		taskConf = springConf.getTaskConf();
	}
	
	@Factory(value="specialites")
	public void initSpecialites() {
		SpecialiteDao dao = new SpecialiteDao(entityManager);
		specialites = dao.getAllSpecialites();
		
	}
	public void refreshSepecialites() {
		specialites = null;
	}
	@Factory(value="criteres")
	public void initCriteres() {
		CritereDao dao = new CritereDao(entityManager);
		criteres = dao.getAllCriteres();
		
	}
	public void refreshCriteres() {
		criteres = null;
	}
	@Factory(value="horaires")
	public void initHoraires() {
		HoraireDao dao = new HoraireDao(entityManager);
		horaires = dao.getAllHoraires();
		
	}
	public void refreshHoraires() {
		horaires = null;
	}
	@Factory(value="localisations")
	public void initLocalisation() {
		LocalisationDao dao = new LocalisationDao(entityManager);
		localisations = dao.getAllLocalisations();
		
	}
	@Factory(value="stateSelectItems")
	public void initStates() {
		stateSelectItems = new ArrayList<SelectItem>();
		stateSelectItems.add(new SelectItem("",Labels.get("noSelection.state")));
		stateSelectItems.add(new SelectItem("QUALIFICATION",Labels.get("QUALIFICATION")));
		stateSelectItems.add(new SelectItem("COMPLETER",Labels.get("COMPLETER")));
		stateSelectItems.add(new SelectItem("A_PLANIFIER",Labels.get("A_PLANIFIER")));
		stateSelectItems.add(new SelectItem("VERIFICATION",Labels.get("VERIFICATION")));
		stateSelectItems.add(new SelectItem("A_REPLANIFIER",Labels.get("A_REPLANIFIER")));
		
	}
	public void refreshLocalisations() {
		localisations = null;
	}
	public static String getServerUrl(FacesContext facesContext) {
		RequestHandler request = new RequestHandler(facesContext
				.getExternalContext().getRequest());
		URI server = null;
		try {
			server = request.getURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return server == null ? "" : server.toString();
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
