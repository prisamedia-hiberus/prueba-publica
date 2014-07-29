/**
 * 
 */
package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

import com.diarioas.guialigas.dao.model.player.Trayectoria;

/**
 * @author robertosanchez
 * 
 */
public class TrayectoriaComparator implements Comparator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Object obj1, Object obj2) {
		int y1 = ((Trayectoria) obj1).getYear();
		int y2 = ((Trayectoria) obj2).getYear();

		return y2 - y1;
	}
}
