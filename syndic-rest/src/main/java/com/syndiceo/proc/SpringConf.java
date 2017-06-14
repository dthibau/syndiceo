package com.syndiceo.proc;

import java.util.Map;



public class SpringConf {

	private Map<String,TaskConf> taskConf;

	public Map<String, TaskConf> getTaskConf() {
		return taskConf;
	}

	public void setTaskConf(Map<String, TaskConf> taskConf) {
		this.taskConf = taskConf;
	}
	
	
}
