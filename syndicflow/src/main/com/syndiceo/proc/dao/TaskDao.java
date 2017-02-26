package com.syndiceo.proc.dao;

import java.util.List;

import com.syndiceo.proc.model.DirectTask;
import com.syndiceo.proc.model.LongTask;
import com.syndiceo.proc.model.Task;

public interface TaskDao {
	public void persist(Task task);
	public void remove(Task task);
	public void removeAll(long processInstanceId);
	public void removeAll(long processInstanceId,String taskCode);
	public void removeAllSub(long subProcessInstanceId);
	public void update(Task task);
	public List<Task> getAllTasks();
	public List<Task> getTasks(long processInstanceId);
	public List<LongTask> getAllLongTasks();
	public List<LongTask> getLongTasks(long processInstanceId);
	public List<DirectTask> getAllDirectTasks();
	public List<DirectTask> getDirectTasks(long processInstanceId);

}
