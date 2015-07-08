/**
 * 
 */
package com.diarioas.guialigas.dao.model.clasificacion;

import java.util.HashMap;

import com.diarioas.guialigas.dao.model.team.Team;

/**
 * @author robertosanchez
 * 
 */
public class Clasificacion {

	private HashMap<String, Team> clasificacion;

	// private HashMap<String, Team> zonas;

	public Clasificacion() {
		this.setClasificacion(new HashMap<String, Team>());
	}

	/**
	 * @return the clasificacion
	 */
	public HashMap<String, Team> getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion
	 *            the clasificacion to set
	 */
	public void setClasificacion(HashMap<String, Team> clasificacion) {
		this.clasificacion = clasificacion;
	}

	/**
	 * @param next
	 * @param team
	 */
	public void addTeamToClasificacion(String key, Team team) {
		this.clasificacion.put(key, team);

	}
}
