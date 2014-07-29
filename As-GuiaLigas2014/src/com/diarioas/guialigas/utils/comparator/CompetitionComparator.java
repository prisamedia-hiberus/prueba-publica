package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

import com.diarioas.guialigas.dao.model.competition.Competition;

public class CompetitionComparator implements Comparator<Competition> {
	@Override
	public int compare(Competition lhs, Competition rhs) {

		return lhs.getOrder() - rhs.getOrder();
	}

}
