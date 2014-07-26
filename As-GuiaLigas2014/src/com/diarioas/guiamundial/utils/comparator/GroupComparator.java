/**
 * 
 */
package com.diarioas.guiamundial.utils.comparator;

import java.util.Comparator;

import com.diarioas.guiamundial.dao.model.calendar.Grupo;

/**
 * @author robertosanchez
 * 
 */
public class GroupComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object obj1, Object obj2) {
		String name1 = ((Grupo) obj1).getName();
		String name2 = ((Grupo) obj2).getName();

		return name1.compareTo(name2);
	}
}
