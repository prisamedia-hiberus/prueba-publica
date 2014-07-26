/**
 * 
 */
package com.diarioas.guiamundial.dao.model.player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author robertosanchez
 * 
 */
public class Player {
	private int id;
	private String idTeam;
	private String nameTeam;
	private String name;
	private String shortName;
	private String url;
	private String urlFoto;
	private String urlFicha;
	private String urlTag;
	private int age;
	private String dateBorn;
	private int dorsal;
	private String demarcation;
	private int position;
	private double height;
	private double weight;
	private String nacionality;
	private String placeBorn;
	private int fecModificacion;
	private String competiciones;
	private ArrayList<TituloPlayer> palmares;
	private ArrayList<Trayectoria> trayectoria;
	private HashMap<String, PlayerStats> stats;// A–o, Estadistica

	public Player() {
		palmares = new ArrayList<TituloPlayer>();
		trayectoria = new ArrayList<Trayectoria>();
		stats = new HashMap<String, PlayerStats>();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the idTeam
	 */
	public String getIdTeam() {
		return idTeam;
	}

	/**
	 * @param idTeam
	 *            the idTeam to set
	 */
	public void setIdTeam(String idTeam) {
		this.idTeam = idTeam;
	}

	/**
	 * @return the nameTeam
	 */
	public String getNameTeam() {
		return nameTeam;
	}

	/**
	 * @param nameTeam
	 *            the nameTeam to set
	 */
	public void setNameTeam(String nameTeam) {
		this.nameTeam = nameTeam;
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
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		if (url != null && !url.startsWith("http://")
				&& !url.startsWith("https://"))
			url = "http://" + url;

		this.url = url;
	}

	/**
	 * @return the urlFoto
	 */
	public String getUrlFoto() {
		return urlFoto;
	}

	/**
	 * @param urlFoto
	 *            the urlFoto to set
	 */
	public void setUrlFoto(String urlFoto) {
		// this.urlFoto = urlFoto.replaceAll("//", "/");
		if (urlFoto != null && !urlFoto.startsWith("http://")
				&& !urlFoto.startsWith("https://"))
			urlFoto = "http://" + urlFoto;

		this.urlFoto = urlFoto;
	}

	/**
	 * @return the urlFicha
	 */
	public String getUrlFicha() {
		return urlFicha;
	}

	/**
	 * @param urlFicha
	 *            the urlFicha to set
	 */
	public void setUrlFicha(String urlFicha) {
		this.urlFicha = urlFicha;
	}

	/**
	 * @return the urlTag
	 */
	public String getUrlTag() {
		return urlTag;
	}

	/**
	 * @param urlTag
	 *            the urlTag to set
	 */
	public void setUrlTag(String urlTag) {
		if (urlTag != null && !urlTag.startsWith("http://")
				&& !urlTag.startsWith("https://"))
			urlTag = "http://" + urlTag;

		this.urlTag = urlTag;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the placeBorn
	 */
	public String getPlaceBorn() {
		return placeBorn;
	}

	/**
	 * @param placeBorn
	 *            the placeBorn to set
	 */
	public void setPlaceBorn(String placeBorn) {
		this.placeBorn = placeBorn;
	}

	/**
	 * @return the dateBorn
	 */
	public String getDateBorn() {
		return dateBorn;
	}

	/**
	 * @param dateBorn
	 *            the dateBorn to set
	 */
	public void setDateBorn(String dateBorn) {
		this.dateBorn = dateBorn;
	}

	/**
	 * @return the dorsal
	 */
	public int getDorsal() {
		return dorsal;
	}

	/**
	 * @param dorsal
	 *            the dorsal to set
	 */
	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}

	/**
	 * @return the demarcation
	 */
	public String getDemarcation() {
		return demarcation;
	}

	/**
	 * @param demarcation
	 *            the demarcation to set
	 */
	public void setDemarcation(String demarcation) {
		this.demarcation = demarcation;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the nacionality
	 */
	public String getNacionality() {
		return nacionality;
	}

	/**
	 * @param nacionality
	 *            the nacionality to set
	 */
	public void setNacionality(String nacionality) {
		this.nacionality = nacionality;
	}

	/**
	 * @return the fecModificacion
	 */
	public int getFecModificacion() {
		return fecModificacion;
	}

	/**
	 * @param fecModificacion
	 *            the fecModificacion to set
	 */
	public void setFecModificacion(int fecModificacion) {
		this.fecModificacion = fecModificacion;
	}

	/**
	 * @return the competiciones
	 */
	public String getCompeticiones() {
		return competiciones;
	}

	/**
	 * @param competiciones
	 *            the competiciones to set
	 */
	public void setCompeticiones(String competiciones) {
		this.competiciones = competiciones;
	}

	/**
	 * @return the palmares
	 */
	public ArrayList<TituloPlayer> getPalmares() {
		return palmares;
	}

	/**
	 * @param palmares
	 *            the palmares to set
	 */
	public void setPalmares(ArrayList<TituloPlayer> palmares) {
		this.palmares = palmares;
	}

	public void addTitle(TituloPlayer title) {
		this.palmares.add(title);
	}

	public void addTitle(String name, int numTitle,
			HashMap<String, ArrayList<String>> years) {
		this.palmares.add(new TituloPlayer(name, numTitle, years));
	}

	/**
	 * @return the trayectoria
	 */
	public ArrayList<Trayectoria> getTrayectoria() {
		return trayectoria;
	}

	/**
	 * @param trayectoria
	 *            the trayectoria to set
	 */
	public void setTrayectoria(ArrayList<Trayectoria> trayectoria) {
		this.trayectoria = trayectoria;
	}

	public void addTeamToTrayectoria(Trayectoria trayectoria) {
		this.trayectoria.add(trayectoria);
	}

	/**
	 * @return the stats
	 */
	public HashMap<String, PlayerStats> getStats() {
		return stats;
	}

	public PlayerStats getStats(String year) {
		return this.stats.get(year);
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(HashMap<String, PlayerStats> stats) {
		this.stats = stats;
	}

	public void addStats(String year, PlayerStats stats) {
		this.stats.put(year, stats);
	}

}
