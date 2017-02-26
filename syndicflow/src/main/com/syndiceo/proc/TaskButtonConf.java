package com.syndiceo.proc;

import java.util.Map;

public class TaskButtonConf {

	private String labelCode;
	private int issue;
	private Map<String, Object> results;
	private int dialogForm;


	private String defaultComment;
	private String notificationString;

	public TaskButtonConf() {
		super();
	}

	public TaskButtonConf(String labelCode, int issue,
			Map<String, Object> results) {
		this.labelCode = labelCode;
		this.issue = issue;
		this.results = results;
	}

	public int getIssue() {
		return issue;
	}

	public void setIssue(int issue) {
		this.issue = issue;
	}

	public String getListLabelCode() {
		return "list." + labelCode;
	}

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}

	public Map<String, Object> getResults() {
		return results;
	}

	public void setResults(Map<String, Object> results) {
		this.results = results;
	}

	public String getDefaultComment() {
		return defaultComment;
	}

	public void setDefaultComment(String defaultComment) {
		this.defaultComment = defaultComment;
	}

	public int getDialogForm() {
		return dialogForm;
	}

	public void setDialogForm(int dialogForm) {
		this.dialogForm = dialogForm;
	}

	public String getNotificationString() {
		return notificationString;
	}

	public void setNotificationString(String notificationString) {
		this.notificationString = notificationString;
	}


}
