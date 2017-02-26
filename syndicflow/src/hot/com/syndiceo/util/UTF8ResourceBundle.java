package com.syndiceo.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

public abstract class UTF8ResourceBundle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 300009707093332677L;

	public static final ResourceBundle getBundle(String baseName) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName);
		return createUtf8PropertyResourceBundle(bundle);
	}

	public static final ResourceBundle getBundle(String baseName, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
		return createUtf8PropertyResourceBundle(bundle);
	}

	public static ResourceBundle getBundle(String baseName, Locale locale,
			ClassLoader loader) {
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale,
				loader);
		return createUtf8PropertyResourceBundle(bundle);
	}

	private static ResourceBundle createUtf8PropertyResourceBundle(
			ResourceBundle bundle) {
		if (!(bundle instanceof PropertyResourceBundle))
			return bundle;

		return new Utf8PropertyResourceBundle((PropertyResourceBundle) bundle);
	}

	private static class Utf8PropertyResourceBundle extends ResourceBundle implements Map<String,String>,Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5834564464991537019L;
		PropertyResourceBundle bundle;

		private Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
			this.bundle = bundle;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.ResourceBundle#getKeys()
		 */
		public Enumeration<String> getKeys() {
			return bundle.getKeys();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
		 */
		protected Object handleGetObject(String key) {
			String value = bundle.getString(key);
			if (value == null)
				return null;
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// Shouldn't fail - but should we still add logging message?
				return null;
			}
		}

		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean containsKey(Object key) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean containsValue(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

		public String get(Object key) {
			return getString(key.toString());
		}

		public String put(String key, String value) {
			// TODO Auto-generated method stub
			return null;
		}

		public String remove(Object key) {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings("rawtypes")
		public void putAll(Map t) {
			// TODO Auto-generated method stub
			
		}

		public void clear() {
			// TODO Auto-generated method stub
			
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Set keySet() {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Collection values() {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Set entrySet() {
			// TODO Auto-generated method stub
			return null;
		}
		
		

	}
}