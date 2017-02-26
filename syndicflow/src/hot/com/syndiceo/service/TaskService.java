package com.syndiceo.service;

import java.util.List;
import java.util.Map;

import com.syndiceo.dto.AutreDemandeDTO;
import com.syndiceo.dto.DemandeDTO;
import com.syndiceo.dto.InterventionDTO;
import com.syndiceo.model.Account;
import com.syndiceo.model.AutreDemande;
import com.syndiceo.model.Demande;
import com.syndiceo.model.Intervention;
import com.syndiceo.proc.model.LongTask;
import com.syndiceo.proc.model.Task;

public interface TaskService {

	/**
	 * Toutes les tâches en cours concernant des petits travaux (accès administrateur)
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllPetitsTravaux(Account account);
	
	/**
	 * Toutes les tâches en cours me concernant ou m'ayant concernée et concernant des petits travaux.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllMinePetitTravaux(Account account);
	
	
	/**
	 * Toutes les interventions petits travaux en cours concernant un ensemble d'immeubles.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllImmeublesPetitTravaux(Account account);
	/**
	 * Les dossiers dont l'utilisateur a des tâche en cours concernant des petits travaux.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getEnCoursPetitTravaux(Account account);
	
	/**
	 * Toutes les tâches en cours concernant des gros travaux (accès administrateur)
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllGrosTravaux(Account account);
	
	/**
	 * Toutes les tâches en cours me concernant ou m'ayant concernée et concernant des gros travaux.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllMineGrosTravaux(Account account);
	
	/**
	 * Toutes les interventions gros travaux en cours concernant un ensemble d'immeubles.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllImmeublesGrosTravaux(Account account);
	/**
	 * Les dossiers dont l'utilisateur a des tâche en cours concernant des gros travaux.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getEnCoursGrosTravaux(Account account);
	
	/**
	 * Toutes les tâches en cours (accès administrateur)
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAll(Account account);
	
	/**
	 * Toutes les tâches en cours me concernant ou m'ayant concernée.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllMine(Account account);
	/**
	 * Les dossiers dont l'utilisateur a des tâche en cours.
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getEnCours(Account account);
	

	/**
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllAutreDemandes(Account account);
	/**
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllMineAutreDemandes(Account account);
	/**
	 * @param account
	 * @return
	 */
	public List<DemandeDTO> getAllImmeublesAutreDemandes(Account account);
	
	
	public DemandeDTO getDemandeDTO(Account account, Demande demande);
	public InterventionDTO getInterventionDTO(Account account, Intervention intervention);
	public AutreDemandeDTO getAutreDemandeDTO(Account account, AutreDemande autreDemande);
	
	public long startProcess(Account demandeur, Intervention intervention);
	public long startProcess(Account demandeur, AutreDemande autreDemande);

	public void storeTask(Account account, Map<String,Object> result, long pid, LongTask task);

	public void completeTask(Account account, Map<String,Object> result, long pid, Task task);
	
	
}
