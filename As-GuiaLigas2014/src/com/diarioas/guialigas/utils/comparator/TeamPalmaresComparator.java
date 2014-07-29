/**
 * 
 */
package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

/**
 * @author robertosanchez
 * 
 */
public class TeamPalmaresComparator implements Comparator<String> {
	static enum Competitions {
		primeradivision(1), primeradivisi—n(1), ligadecampeones(2), copadeeuropa(
				3), mundialdeclubes(4), copaintercontinental(5), supercopadeeuropa(
				6), europaleague(7), recopadeeuropa(8), copadelrey(9), supercopadeespa–a(
				10);

		private final int order;

		Competitions(int order) {
			this.order = order;
		}

		public int getOrder() {
			return this.order;
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String obj1, String obj2) {

		String comp1 = obj1.replaceAll(" ", "").toLowerCase();
		String comp2 = obj2.replaceAll(" ", "").toLowerCase();

		int order1, order2;
		try {
			order1 = Competitions.valueOf(comp1).getOrder();
		} catch (Exception e) {
			order1 = 100;
		}
		try {
			order2 = Competitions.valueOf(comp2).getOrder();
		} catch (Exception e) {
			order2 = 100;
		}

		return order1 - order2;
	}

}
