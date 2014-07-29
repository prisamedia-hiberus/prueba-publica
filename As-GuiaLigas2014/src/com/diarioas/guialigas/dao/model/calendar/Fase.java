/**
 * 
 */
package com.diarioas.guialigas.dao.model.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.diarioas.guialigas.dao.model.clasificacion.LeyendaInfo;

/**
 * @author robertosanchez
 * 
 */
public class Fase {

	private String name;
	private String idFase;
	private boolean active;
	private boolean defecto;
	private ArrayList<Grupo> grupos;
	private HashMap<Integer, LeyendaInfo> leyenda;
	private Date dateUpdated;

	/**
	 * 
	 */
	public Fase() {
		grupos = new ArrayList<Grupo>();
		leyenda = new HashMap<Integer, LeyendaInfo>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the idFase
	 */
	public String getIdFase() {
		return idFase;
	}

	/**
	 * @param idFase
	 *            the idFase to set
	 */
	public void setIdFase(String idFase) {
		this.idFase = idFase;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the defecto
	 */
	public boolean isDefecto() {
		return defecto;
	}

	/**
	 * @param defecto
	 *            the defecto to set
	 */
	public void setDefecto(boolean defecto) {
		this.defecto = defecto;
	}

	/**
	 * @return the grupos
	 */
	public ArrayList<Grupo> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos
	 *            the grupos to set
	 */
	public void setGrupos(ArrayList<Grupo> grupos) {
		this.grupos = grupos;
	}

	public void addGrupo(Grupo grupo) {
		this.grupos.add(grupo);
	}

	/**
	 * @return the leyenda
	 */
	public HashMap<Integer, LeyendaInfo> getLeyenda() {
		return leyenda;
	}

	/**
	 * @param hashMap
	 *            the leyenda to set
	 */
	public void setLeyenda(HashMap<Integer, LeyendaInfo> hashMap) {
		this.leyenda = hashMap;
	}

	/**
	 * @return the dateUpdated
	 */
	public Date getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * @param dateUpdated
	 *            the dateUpdated to set
	 */
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

}
