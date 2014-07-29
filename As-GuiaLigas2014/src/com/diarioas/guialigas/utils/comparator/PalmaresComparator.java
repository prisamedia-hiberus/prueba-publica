package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

import com.diarioas.guialigas.dao.model.palmares.Palmares;

public class PalmaresComparator implements Comparator {

	@Override
	public int compare(Object obj1, Object obj2) {

		int year1 = ((Palmares) obj1).getDate();
		int year2 = ((Palmares) obj2).getDate();
		return year2 - year1;
	}

}
