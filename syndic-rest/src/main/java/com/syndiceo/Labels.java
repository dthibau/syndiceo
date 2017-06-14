package com.syndiceo;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


public class Labels {


	
	public final static String get(String key) {
		try {
			return ResourceBundle.getBundle("labels").getString(key);
		} catch (MissingResourceException e) {
			return "??" + key + "??";
		}
		
	}

	public final static String textToHtml(String text) {
		return text.replaceAll("\n", "<br/>");
	}
	
}
