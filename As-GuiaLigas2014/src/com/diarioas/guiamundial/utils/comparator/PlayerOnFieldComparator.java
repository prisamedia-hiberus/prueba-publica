/**
 * 
 */
package com.diarioas.guiamundial.utils.comparator;

import java.util.Comparator;

import com.diarioas.guiamundial.dao.model.carrusel.PlayerOnField;

/**
 * @author rober
 * 
 */
public class PlayerOnFieldComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object obj1, Object obj2) {
		// TODO Auto-generated method stub
		return ((PlayerOnField) obj1).getPosition()
				- ((PlayerOnField) obj2).getPosition();
	}
}
