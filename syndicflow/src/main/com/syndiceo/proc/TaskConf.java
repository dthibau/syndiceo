package com.syndiceo.proc;



public class TaskConf {

	private String code;
	private int form;
	private TaskButtonConf[] buttonConfs;
	
	public TaskConf() {
		super();
	}
	public TaskConf(String code, int form, TaskButtonConf [] buttonCodes ) {
		this.code = code;
		this.form = form;
		this.buttonConfs = buttonCodes;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public int getForm() {
		return form;
	}


	public void setForm(int form) {
		this.form = form;
	}


	public TaskButtonConf[] getButtonConfs() {
		return buttonConfs;
	}


	public void setButtonConfs(TaskButtonConf[] buttonCodes) {
		this.buttonConfs = buttonCodes;
	}
	
	public TaskButtonConf getUniqueButtonConf() {
		if ( buttonConfs.length == 1) {
			return buttonConfs[0];
		}
		return null;
	}
	public boolean hasButtonConf() {
		return buttonConfs.length > 0;
	}
}
