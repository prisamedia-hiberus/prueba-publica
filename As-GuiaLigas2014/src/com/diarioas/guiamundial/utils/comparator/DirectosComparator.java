/**
 * 
 */
package com.diarioas.guiamundial.utils.comparator;

import java.util.Comparator;

import com.diarioas.guiamundial.dao.model.carrusel.ItemDirecto;

/**
 * @author robertosanchez
 * 
 */
public class DirectosComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object obj1, Object obj2) {
		// TODO Auto-generated method stub
		return ((ItemDirecto) obj2).getNumComent()
				- ((ItemDirecto) obj1).getNumComent();
	}
}
