package com.syndiceo.util;

public class Util {

	public static boolean isEqual(Object o1, Object o2) {
		if ( o1 == null ) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}
}
