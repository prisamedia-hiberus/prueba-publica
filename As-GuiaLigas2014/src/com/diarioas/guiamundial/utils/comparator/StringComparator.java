/**
 * 
 */
package com.diarioas.guiamundial.utils.comparator;

import java.util.Comparator;

/**
 * @author robertosanchez
 * 
 */
public class StringComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object obj1, Object obj2) {
		return ((String) obj1).compareTo((String) obj2);
	}
}
