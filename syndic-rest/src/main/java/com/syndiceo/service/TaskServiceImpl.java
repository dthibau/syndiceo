package com.syndiceo.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syndiceo.dto.AutreDemandeDTO;
import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.dto.InterventionDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Intervention;
import com.syndiceo.model.Lu;
import com.syndiceo.model.dao.AutreDemandeDao;
import com.syndiceo.model.dao.InterventionDao;
import com.syndiceo.model.dao.LuDao;
import com.syndiceo.proc.JbpmHelperImpl;
import com.syndiceo.proc.TaskHandler;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTask;
import com.syndiceo.proc.model.Task;

@Service
public class TaskServiceImpl implements TaskService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2285506329089580634L;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	JbpmHelperImpl jbpmHelper;
	
	@Autowired
	InterventionDao interventionDao;
	
	@Autowired
	AutreDemandeDao autreDemandeDao;
	
	public List<DemandeDTO> getEnCours(Account account) {
		List<Long> pids = new ArrayList<Long>();
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<Task> assignedTasks = TaskHandler.getInstance().findAssignedTasks(
				account);
		for (Task task : assignedTasks) {
			pids.add(task.getProcessInstanceId());
		}

		List<Intervention> demandes = interventionDao.findInterventions(pids);

		for (Intervention demande : demandes) {
			if ( !account.isOnly("co_pro") || !demande.getConfidentielle() ) {
				ret.add(_buildDTO(account, demande, assignedTasks));
			}
		}
		Collections.sort(ret);
		return ret;
	}
	

	public List<DemandeDTO> getAll(Account account) {

		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<Intervention> allCurrentInterventions = interventionDao.findUnArchived();
		for (Intervention demande : allCurrentInterventions) {
			if ( !account.isOnly("co_pro") || !demande.getConfidentielle() ) {
				ret.add(getInterventionDTO(account, demande));
			}
		}

		Collections.sort(ret);
		return ret;
	}

	public List<DemandeDTO> getAllMine(Account account) {

		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<Intervention> allCurrentInterventions = interventionDao.findUnArchived(account);
		for (Intervention demande : allCurrentInterventions) {
			if ( !account.isOnly("co_pro") || !demande.getConfidentielle() ) {
				ret.add(getInterventionDTO(account, demande));
			}
		}

		Collections.sort(ret);
		return ret;
	}
	
	public List<DemandeDTO> getAllImmeubles(Account account) {

		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<Intervention> allCurrentInterventions = interventionDao.findUnArchivedByImmeubles(account.getImmeubles());
		for (Intervention demande : allCurrentInterventions) {
			if ( !account.isOnly("r_copro") || !demande.getConfidentielle() ) {
				ret.add(getInterventionDTO(account, demande));
			}
		}

		Collections.sort(ret);
		return ret;
	}

	public DemandeDTO getDemandeDTO(Account account, Demande demande) {

		if ( demande instanceof Intervention ) {
			return getInterventionDTO(account, (Intervention)demande);
		} else 
			return getAutreDemandeDTO(account, (AutreDemande)demande);
	}
	
	public InterventionDTO getInterventionDTO(Account account, Intervention intervention) {

		List<Task> assignedTasks = TaskHandler.getInstance().findAssignedTasks(
				account, intervention.getProcessInstanceId());

		InterventionDTO demandeDTO = new InterventionDTO(account, intervention);
		for (Task task : assignedTasks) {
			// Only one LongTask
			if (task instanceof LongTask) {
				demandeDTO.setCurrentTask((LongTask) task);
			} else {
				demandeDTO.addDirectTask((DirectTask) task);
			}
		}
		_setLu(demandeDTO,account,intervention);
		return demandeDTO;
	}
	
	public AutreDemandeDTO getAutreDemandeDTO(Account account, AutreDemande autreDemande) {

		List<Task> assignedTasks = TaskHandler.getInstance().findAssignedTasks(
				account, autreDemande.getProcessInstanceId());

		AutreDemandeDTO demandeDTO = new AutreDemandeDTO(account, autreDemande);
		for (Task task : assignedTasks) {
			// Only one LongTask
			if (task instanceof LongTask) {
				demandeDTO.setCurrentTask((LongTask) task);
			} else {
				demandeDTO.addDirectTask((DirectTask) task);
			}
		}
		_setLu(demandeDTO,account,autreDemande);
		return demandeDTO;
	}

	public long startProcess(Account demandeur, Intervention intervention) {
		StatefulKnowledgeSession ksession = jbpmHelper.getKsession();

		ProcessInstance processInstance = ksession.startProcess("main", null);
		Task depot = TaskHandler.getInstance().findAssignedTask(demandeur,
				"DEPOT", processInstance.getId());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("conseil", demandeur);
		results.put("gestionnaire", intervention.getImmeuble()
				.getGestionnaire());
		TaskHandler.getInstance().completeTask(demandeur, depot, results);

		return processInstance.getId();
	}
	
	public long startProcess(Account demandeur, AutreDemande autreDemande) {
		StatefulKnowledgeSession ksession = jbpmHelper.getKsession();

		ProcessInstance processInstance = ksession.startProcess("autre", null);
		Task depot = TaskHandler.getInstance().findAssignedTask(demandeur,
				"AUTRE_DEPOT", processInstance.getId());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("demandeur", demandeur);
		results.put("gestionnaire", autreDemande.getImmeuble()
				.getGestionnaire());
		TaskHandler.getInstance().completeTask(demandeur, depot, results);

		return processInstance.getId();
	}

	@Override
	public void storeTask(Account account, Map<String, Object> result,
			long pid, LongTask task) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeTask(Account account, Map<String, Object> results,
			long pid, Task task) {
		TaskHandler.getInstance().completeTask(account, task, results);

	}

	private InterventionDTO _buildDTO(Account account, Intervention intervention,
			List<Task> assignedTasks) {

		InterventionDTO demandeDTO = new InterventionDTO(account, intervention);
		for (Task task : assignedTasks) {
			if (task.getProcessInstanceId() == intervention
					.getProcessInstanceId()) {
				// Only one LongTask
				if (task instanceof LongTask) {
					demandeDTO.setCurrentTask((LongTask) task);
				} else {
					demandeDTO.addDirectTask((DirectTask) task);
				}

			}
		}
		_setLu(demandeDTO,account,intervention);
		
		return demandeDTO;
	}

	private void _setLu(DemandeDTO demandeDTO, Account account, Demande demande ) {
		LuDao lDao = new LuDao(entityManager);
		Lu lu = lDao.get(account, demande);
		if ( lu == null || demande.getLastMessage().getTimestamp().after(lu.getDate()) ) {
			demandeDTO.setLu(false);
		} else {
			demandeDTO.setLu(true);
		}
	}
	@Override
	public List<DemandeDTO> getAllPetitsTravaux(Account account) {
		return _filterPetitsTravaux(getAll(account),true);
	}

	@Override
	public List<DemandeDTO> getAllMinePetitTravaux(Account account) {
		return _filterPetitsTravaux(getAllMine(account),true);
	}

	@Override
	public List<DemandeDTO> getAllImmeublesPetitTravaux(Account account) {
		return _filterPetitsTravaux(getAllImmeubles(account),true);
	}

	@Override
	public List<DemandeDTO> getEnCoursPetitTravaux(Account account) {
		return _filterPetitsTravaux(getEnCours(account),true);
	}

	@Override
	public List<DemandeDTO> getAllGrosTravaux(Account account) {
		return _filterPetitsTravaux(getAll(account),false);
	}

	@Override
	public List<DemandeDTO> getAllMineGrosTravaux(Account account) {
		return _filterPetitsTravaux(getAllMine(account),false);
	}

	@Override
	public List<DemandeDTO> getAllImmeublesGrosTravaux(Account account) {
		return _filterPetitsTravaux(getAllImmeubles(account),false);
	}
	
	@Override
	public List<DemandeDTO> getEnCoursGrosTravaux(Account account) {
		return _filterPetitsTravaux(getEnCours(account),false);
	}
	
	@Override
	public List<DemandeDTO> getAllAutreDemandes(Account account) {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<AutreDemande> allCurrentAutreDemandes = autreDemandeDao.findUnArchived();
		for (AutreDemande demande : allCurrentAutreDemandes) {
			ret.add(getAutreDemandeDTO(account, demande));
		}

		return ret;
	}

	@Override
	public List<DemandeDTO> getAllMineAutreDemandes(Account account) {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<AutreDemande> allCurrentAutreDemandes = autreDemandeDao.findUnArchived(account);
		for (AutreDemande demande : allCurrentAutreDemandes) {
			ret.add(getAutreDemandeDTO(account, demande));
		}

		return ret;
	}

	@Override
	public List<DemandeDTO> getAllImmeublesAutreDemandes(Account account) {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();

		List<AutreDemande> allCurrentAutreDemandes = autreDemandeDao.findUnArchivedByImmeubles(account.getImmeubles());
		for (AutreDemande demande : allCurrentAutreDemandes) {
			ret.add(getAutreDemandeDTO(account, demande));
		}

		return ret;
	}

	private List<DemandeDTO>  _filterPetitsTravaux( List<DemandeDTO> tmp, boolean petitTravaux) {
		List<DemandeDTO> ret = new ArrayList<DemandeDTO>();
		
		for ( DemandeDTO dto : tmp) {
			if ( petitTravaux && !dto.getStatusCode().equals("ODJ_AG") ) {
				ret.add(dto);
			}
			if ( !petitTravaux && dto.getStatusCode().equals("ODJ_AG") ) {
				ret.add(dto);
			}
		}
		return ret;
	}

}
