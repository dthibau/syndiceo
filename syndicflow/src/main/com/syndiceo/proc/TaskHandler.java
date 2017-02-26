package com.syndiceo.proc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

import com.syndiceo.model.Account;
import com.syndiceo.model.OrganizationalEntity;
import com.syndiceo.model.Role;
import com.syndiceo.proc.dao.TaskDao;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTask;
import com.syndiceo.proc.model.LongTaskWithMessage;
import com.syndiceo.proc.model.Task;

public class TaskHandler implements WorkItemHandler {

	private static TaskHandler instance;
	// Injected Spring's bean
	private TaskDao taskDao;
	private JbpmHelper jbpmHelper;
	
	
	
	private TaskHandler() {
		super();		
	}
	
	public static TaskHandler getInstance() {
		if ( instance == null ) {
			instance = new TaskHandler();
		}
		return instance;
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		List<Task> toRemove = new ArrayList<Task>();
		
		
		
		for ( Task task : getAllTasks() ) {
			if ( task.getWorkItemId() == workItem.getId() ) {
				toRemove.add(task);
			}
		}
		System.out.println("About to remove "+toRemove);
		for ( Task task : toRemove ) {
			taskDao.remove(task);
		}

	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		int type = 2;
		int order = 9;
		try { // Expected 2 digits for priority
			String sType = ((String)workItem.getParameter("Priority")).substring(0,1);
			String sOrder = ((String)workItem.getParameter("Priority")).substring(1);
			type = Integer.parseInt(sType);
			order = Integer.parseInt(sOrder);
		} catch (Exception e) {
//			log.warn("No priority defined in task "+workItem.getParameter("TaskName"));
		}
		Task task;
		if ( type == 1 ) {
			task = new DirectTask();
		} else if ( type == 3 ) {
			task = new LongTaskWithMessage();
		} else {
			task = new LongTask();
		}
		
		task.setOrdre(order);
		task.setWorkItemId(workItem.getId());
		task.setProcessInstanceId(jbpmHelper.getRootProcessInstanceId(workItem.getProcessInstanceId()));
		task.setSubProcessInstanceId(workItem.getProcessInstanceId());
		task.setCode((String)workItem.getParameter("TaskName"));

		
		List<OrganizationalEntity> owners = new ArrayList<OrganizationalEntity>();

        String actorId = (String) workItem.getParameter("ActorId");
		if (actorId != null && actorId.trim().length() > 0) {
			String[] actorIds = actorId.split(" ");
			for (String id: actorIds) {
				owners.add(new Account(id.trim()));
			}
        }
		
        String groupId = (String) workItem.getParameter("GroupId");
		if (groupId != null && groupId.trim().length() > 0) {
			String[] groupIds = groupId.split(",");
			for (String id: groupIds) {
				owners.add(new Role(id.trim()));
			}
		}
		task.setOwners(owners);
		System.out.println("Creating task :"+task);
		taskDao.persist(task);

	}
	
	public List<Task> getAllTasks() {
		return taskDao.getAllTasks();
	}
	
	public void completeTask(Account account, Task task, Map<String,Object> results) {
		System.out.println(account + " completing task :"+task+ " with results "+results);
		if ( results == null ) {
			results = new HashMap<String, Object>();
		}
		results.put("ActorId", account.getId());
		jbpmHelper.getWorkItemManager().completeWorkItem(task.getWorkItemId(), results);
//		task.setStatus(Task.COMPLETED);
//		task.setActorId(account.getId());
//		taskDao.update(task);
		if ( jbpmHelper.getKsession().getProcessInstance(task.getProcessInstanceId()) == null ) { // Process completed
			taskDao.removeAll(task.getProcessInstanceId());
		} else if ( jbpmHelper.getKsession().getProcessInstance(task.getSubProcessInstanceId()) == null ) { // SubProcess completed
			taskDao.removeAllSub(task.getSubProcessInstanceId());
		} else {
			taskDao.remove(task);
		}

	}

	public List<Task> findAssignedTasks(Account account) {
		List<Task> ret = new ArrayList<Task>();
		Map<Long,List<String>> codes = new HashMap<Long, List<String>>();
		List<Task> allTasks = taskDao.getAllTasks();
		for ( Task task : allTasks ) {
			if ( task.isAssignable(account) || task.containsParticipant(account)  ) {
				if ( codes.get(task.getProcessInstanceId()) == null ) {
					codes.put(task.getProcessInstanceId(), new ArrayList<String>());
				}
				if ( !codes.get(task.getProcessInstanceId()).contains(task.getCode()) ) {
					ret.add(task);
					codes.get(task.getProcessInstanceId()).add(task.getCode());
				}
			}
		}
		return ret;
	}
	public List<LongTask> findLongAssignedTasks(Account account) {
		List<LongTask> ret = new ArrayList<LongTask>();
		Map<Long,List<String>> codes = new HashMap<Long, List<String>>();
		List<LongTask> allTasks = taskDao.getAllLongTasks();
		for ( LongTask task : allTasks ) {
			if ( (task.isAssignable(account) || task.containsParticipant(account) )  ) {
				if ( codes.get(task.getProcessInstanceId()) == null ) {
					codes.put(task.getProcessInstanceId(), new ArrayList<String>());
				}
				if ( !codes.get(task.getProcessInstanceId()).contains(task.getCode()) ) {
					ret.add(task);
					codes.get(task.getProcessInstanceId()).add(task.getCode());
				}
			}
		}
		return ret;
	}
	
	public List<Task> findAssignedTasks(Account account, long processInstanceId) {
		List<Task> ret = new ArrayList<Task>();
		Map<Long,List<String>> codes = new HashMap<Long, List<String>>();
		List<Task> tasks = taskDao.getTasks(processInstanceId);
		
		for ( Task task : tasks ) {
			if ( (task.isAssignable(account) || task.containsParticipant(account)) ) {
				if ( codes.get(task.getProcessInstanceId()) == null ) {
					codes.put(task.getProcessInstanceId(), new ArrayList<String>());
				}
				if ( !codes.get(task.getProcessInstanceId()).contains(task.getCode()) ) {
					ret.add(task);
					codes.get(task.getProcessInstanceId()).add(task.getCode());
				}
			}
		}
		return ret;
	}
	
	public Task findAssignedTask(Account account,String name, long processInstanceId) {
		List<Task> tasks = taskDao.getTasks(processInstanceId);
		
		Task ret = null;
		for ( Task task : tasks ) {
			if ( task.getCode().equals(name) && (task.isAssignable(account) || task.containsParticipant(account)) ) {
				if ( ret == null || ret.getId() < task.getId() ) {
					ret = task;
					System.out.println("Assigned task setting ret="+ret.getId());
				}
			}
		}
		return ret;
	}
	

	// Spring's injections
	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public JbpmHelper getJbpmHelper() {
		return jbpmHelper;
	}

	public void setJbpmHelper(JbpmHelper jbpmHelper) {
		this.jbpmHelper = jbpmHelper;
	}


}
