package com.diarioas.guiamundial.utils.comparator;

import java.util.Comparator;

import com.diarioas.guiamundial.dao.model.competition.Competition;
import com.diarioas.guiamundial.dao.model.stadium.Stadium;

public class CompetitionComparator implements  Comparator<Competition> {
	@Override
	public int compare(Competition lhs, Competition rhs) {

		return lhs.getOrder() - rhs.getOrder();
	}

}
