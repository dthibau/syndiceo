package com.syndiceo.mock;

import java.util.ArrayList;
import java.util.List;

import com.syndiceo.proc.dao.TaskDao;
import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTask;
import com.syndiceo.proc.model.Task;

public class MockTaskDaoImpl implements TaskDao {

	private List<Task> currentTasks = new ArrayList<Task>();
	
	@Override
	public void persist(Task task) {
		task.setId(currentTasks.size());
		currentTasks.add(task);
	}

	@Override
	public void remove(Task task) {
		currentTasks.remove(task);

	}

	@Override
	public void removeAll(long processInstanceId) {
		List<Task> toRemove = new ArrayList<Task>();
		for ( Task task : currentTasks ) {
			if ( task.getProcessInstanceId() == processInstanceId ) {
				toRemove.add(task);
			}
		}
		for ( Task task : toRemove ) {
			currentTasks.remove(task);
		}
		
	}

	@Override
	public void removeAll(long processInstanceId, String taskCode) {
		List<Task> toRemove = new ArrayList<Task>();
		for ( Task task : currentTasks ) {
			if ( task.getProcessInstanceId() == processInstanceId && task.getCode().equals(taskCode) ) {
				toRemove.add(task);
			}
		}
		for ( Task task : toRemove ) {
			currentTasks.remove(task);
		}
		
	}

	@Override
	public void removeAllSub(long subProcessInstanceId) {
		List<Task> toRemove = new ArrayList<Task>();
		for ( Task task : currentTasks ) {
			if ( task.getSubProcessInstanceId() == subProcessInstanceId ) {
				toRemove.add(task);
			}
		}
		for ( Task task : toRemove ) {
			currentTasks.remove(task);
		}
		
	}

	@Override
	public void update(Task task) {
		remove(task);
		persist(task);

	}

	@Override
	public List<Task> getAllTasks() {

		return currentTasks;
	}

	
	@Override
	public List<Task> getTasks(long processInstanceId) {
		List<Task> ret = new ArrayList<Task>();
		for ( Task task : currentTasks ) {
			if ( task.getProcessInstanceId() == processInstanceId) {
				ret.add(task);
			}
		}
		return ret;
	}


	@Override
	public List<LongTask> getAllLongTasks() {
		List<LongTask> ret = new ArrayList<LongTask>();
		for ( Task task : currentTasks ) {
			if ( task instanceof LongTask ) {
				ret.add((LongTask)task);
			}
		}
		return ret;
	}

	@Override
	public List<LongTask> getLongTasks(long processInstanceId) {
		List<LongTask> ret = new ArrayList<LongTask>();
		for ( Task task : currentTasks ) {
			if ( task.getProcessInstanceId() == processInstanceId && task instanceof LongTask ) {
				ret.add((LongTask)task);
			}
		}
		return ret;
	}

	@Override
	public List<DirectTask> getAllDirectTasks() {
		List<DirectTask> ret = new ArrayList<DirectTask>();
		for ( Task task : currentTasks ) {
			if ( task instanceof DirectTask ) {
				ret.add((DirectTask)task);
			}
		}
		return ret;
	}

	@Override
	public List<DirectTask> getDirectTasks(long processInstanceId) {
		List<DirectTask> ret = new ArrayList<DirectTask>();
		for ( Task task : currentTasks ) {
			if ( task.getProcessInstanceId() == processInstanceId && task instanceof DirectTask ) {
				ret.add((DirectTask)task);
			}
		}
		return ret;
	}


}
