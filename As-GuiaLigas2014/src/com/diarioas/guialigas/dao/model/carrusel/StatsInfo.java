/**
 * 
 */
package com.diarioas.guialigas.dao.model.carrusel;

/**
 * @author robertosanchez
 * 
 */
public class StatsInfo {

	private String typo;
	private int local;
	private int away;

	/**
	 * @return the typo
	 */
	public String getTypo() {
		return typo;
	}

	/**
	 * @param typo
	 *            the typo to set
	 */
	public void setTypo(String typo) {
		this.typo = typo.toUpperCase();
	}

	/**
	 * @return the local
	 */
	public int getLocal() {
		return local;
	}

	/**
	 * @param local
	 *            the local to set
	 */
	public void setLocal(int local) {
		this.local = local;
	}

	/**
	 * @return the away
	 */
	public int getAway() {
		return away;
	}

	/**
	 * @param away
	 *            the away to set
	 */
	public void setAway(int away) {
		this.away = away;
	}
}
