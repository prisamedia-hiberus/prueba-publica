/**
 * 
 */
package com.diarioas.guialigas.utils.comparator;

import java.util.Comparator;
import java.util.Locale;

/**
 * @author robertosanchez
 * 
 */
public class CarruselComparator implements Comparator<String> {

	static enum PrevOrder {
		resumen(1), plantilla(2);

		private final int order;

		PrevOrder(int order) {
			this.order = order;
		}

		public int getOrder() {
			return this.order;
		}

	}

	static enum DirectOrder {
		resumen(1), retransmision(2), endirecto(2), estadisticas(3), estadísticas(
				3), plantilla(4);

		private final int order;

		DirectOrder(int order) {
			this.order = order;
		}

		public int getOrder() {
			return this.order;
		}

	}

	static enum PostOrder {
		resumen(1), picas(2), retransmision(3), endirecto(3), estadisticas(4), estadísticas(
				4), plantilla(5);

		private final int order;

		PostOrder(int order) {
			this.order = order;
		}

		public int getOrder() {
			return this.order;
		}

	}

	private final int stateCode;

	/**
	 * @param stateCode
	 */
	public CarruselComparator(int stateCode) {
		this.stateCode = stateCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String comp1, String comp2) {
		int order1 = 0, order2 = 0;

		switch (stateCode) {
		case 0:
			order1 = getPrevOrder(comp1);
			order2 = getPrevOrder(comp2);
			break;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			order1 = getDirectOrder(comp1);
			order2 = getDirectOrder(comp2);
			break;
		case 7:
			order1 = getPostOrder(comp1);
			order2 = getPostOrder(comp2);
			break;
		default:
			break;
		}

		return order1 - order2;
	}

	/**
	 * @param comp1
	 * @return
	 */
	private int getPostOrder(String obj) {
		int order;
		String comp;
		try {
			comp = obj.replaceAll(" ", "").toLowerCase(Locale.getDefault());
			order = PostOrder.valueOf(comp).getOrder();
		} catch (Exception e) {
			order = 100;
		}
		return order;
	}

	/**
	 * @param comp1
	 * @return
	 */
	private int getDirectOrder(String comp) {
		int order;
		try {
			order = DirectOrder.valueOf(comp.toLowerCase(Locale.getDefault()))
					.getOrder();
		} catch (Exception e) {
			order = 100;
		}
		return order;
	}

	private int getPrevOrder(String comp) {
		int order;
		try {
			order = PrevOrder.valueOf(comp.toLowerCase(Locale.getDefault()))
					.getOrder();
		} catch (Exception e) {
			order = 100;
		}
		return order;
	}
}
