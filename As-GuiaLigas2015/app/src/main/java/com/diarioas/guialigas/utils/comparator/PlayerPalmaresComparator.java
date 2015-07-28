/**
 * 
 */
package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

/**
 * @author robertosanchez
 * 
 */
public class PlayerPalmaresComparator implements Comparator<String> {
	static enum Competitions {
		mundial(1), eurocopa(2), primeradivision(3), primeradivisión(3), ligadecampeones(
				4), copadeeuropa(5), mundialdeclubes(6), copaintercontinental(7), supercopadeeuropa(
				8), europaleague(9), recopadeeuropa(10), copadelrey(11), supercopadeespaña(
				12);

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