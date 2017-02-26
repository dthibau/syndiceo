package com.syndiceo;

import java.util.MissingResourceException;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("Labels")
@Scope(ScopeType.APPLICATION)
public class Labels {


	
	public final static String get(String key) {
		try {
			return org.jboss.seam.core.ResourceBundle.instance().getString(key);
		} catch (MissingResourceException e) {
			return "??" + key + "??";
		}
		
	}

	public final static String textToHtml(String text) {
		return text.replaceAll("\n", "<br/>");
	}
	
}
