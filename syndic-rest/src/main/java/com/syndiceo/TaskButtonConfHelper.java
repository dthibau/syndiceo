package com.syndiceo;

import org.springframework.stereotype.Component;

import com.syndiceo.proc.TaskButtonConf;

@Component
public class TaskButtonConfHelper {

	
	public TaskButtonConfHelper() {
		super();
	}

	public int getHauteur(TaskButtonConf tbConf) {
		if ( tbConf != null ) {
			if ( tbConf.getDialogForm() == Application.DF_MSG ) {
				return 550;
			} 
		}
		return 400;
	}
	
	public int getLargeur(TaskButtonConf tbConf) {
		if ( tbConf != null ) { 
			if ( tbConf.getDialogForm() == Application.DF_MSG ) {
				return 880;
			} 
		}
		return 450;
	}
	public String getDialogTitle(TaskButtonConf tbConf) {
		if ( tbConf != null ) {
			return Application.msgBundle.getString(tbConf.getLabelCode()+".dialogTitle");
		} 
		return "";
	}

	public static String getDialogInfo(TaskButtonConf tbConf) {
		if ( tbConf != null ) {
			return Application.msgBundle.getString(tbConf.getLabelCode()+".dialogInfo");
		}
		return "";
	}



	public static int[] getNotificationCodes(TaskButtonConf tbConf) {
		String notificationString = tbConf.getNotificationString();
		if (notificationString != null) {
			int[] notificationCodes = new int[notificationString.length()];
			for (int i = 0; i < notificationString.length(); i++) {
				notificationCodes[i] = Integer.parseInt(notificationString
						.substring(i, i + 1));
			}
			return notificationCodes;
		}
		return null;
	}
}
