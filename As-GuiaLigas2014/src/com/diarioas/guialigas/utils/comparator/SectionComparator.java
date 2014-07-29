package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

import com.diarioas.guialigas.dao.model.general.Section;

public class SectionComparator implements Comparator<Section> {

	@Override
	public int compare(Section lhs, Section rhs) {

		return lhs.getOrder() - rhs.getOrder();
	}

}
