/**
 * 
 */
package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;

import com.diarioas.guialigas.dao.model.player.TituloPlayer;

/**
 * @author robertosanchez
 * 
 */
public class PlayerTitlesComparator implements Comparator<TituloPlayer> {
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
	public int compare(TituloPlayer obj1, TituloPlayer obj2) {

		int order1 = 100, order2 = 100;
		try {
			String comp1 = obj1.getName().replaceAll(" ", "").toLowerCase();
			order1 = Competitions.valueOf(comp1).getOrder();
		} catch (Exception e) {
			order1 = 100;
		}
		try {
			String comp2 = obj2.getName().replaceAll(" ", "").toLowerCase();
			order2 = Competitions.valueOf(comp2).getOrder();
		} catch (Exception e) {
			order2 = 100;
		}

		return order1 - order2;
	}

}