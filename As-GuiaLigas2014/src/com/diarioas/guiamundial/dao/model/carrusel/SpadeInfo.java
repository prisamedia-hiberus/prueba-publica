/**
 * 
 */
package com.diarioas.guiamundial.dao.model.carrusel;

import java.util.HashMap;

/**
 * @author robertosanchez
 * 
 */
public class SpadeInfo {

	private String local;
	private HashMap<Integer, Spade> localSpades;
	private String away;
	private HashMap<Integer, Spade> awaySpades;

	/**
	 * @return the local
	 */
	public String getLocal() {
		return local;
	}

	/**
	 * @param local
	 *            the local to set
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * @return the localSpades
	 */
	public HashMap<Integer, Spade> getLocalSpades() {
		return localSpades;
	}

	/**
	 * @param localSpades
	 *            the localSpades to set
	 */
	public void setLocalSpades(HashMap<Integer, Spade> localSpades) {
		this.localSpades = localSpades;
	}

	/**
	 * @return the away
	 */
	public String getAway() {
		return away;
	}

	/**
	 * @param away
	 *            the away to set
	 */
	public void setAway(String away) {
		this.away = away;
	}

	/**
	 * @return the awaySpades
	 */
	public HashMap<Integer, Spade> getAwaySpades() {
		return awaySpades;
	}

	/**
	 * @param awaySpades
	 *            the awaySpades to set
	 */
	public void setAwaySpades(HashMap<Integer, Spade> awaySpades) {
		this.awaySpades = awaySpades;
	}
}
