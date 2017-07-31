/**
 * 
 */
package com.diarioas.guialigas.utils.comparator;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.utils.StringUtils;

/**
 * @author robertosanchez
 * 
 */
public class TeamComparator implements Comparator<Team> {

	
	private Collator deCollator;
	public TeamComparator() {
		deCollator = Collator.getInstance(Locale.getDefault());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Team obj1, Team obj2) {
		

		return deCollator.compare(obj1.getShortName(), obj2.getShortName());
	}
}
