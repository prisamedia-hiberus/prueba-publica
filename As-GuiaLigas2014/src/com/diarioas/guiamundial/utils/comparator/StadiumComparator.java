package com.diarioas.guiamundial.utils.comparator;

import java.util.Comparator;

import com.diarioas.guiamundial.dao.model.stadium.Stadium;

public class StadiumComparator implements Comparator<Stadium> {
	@Override
	public int compare(Stadium lhs, Stadium rhs) {

		return lhs.getId() - rhs.getId();
	}
}
