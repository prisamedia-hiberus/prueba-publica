package com.diarioas.guialigas.dao.model.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.diarioas.guialigas.dao.model.carrusel.PlayerOnField;
import com.diarioas.guialigas.dao.model.clasificacion.ClasificacionInfo;
import com.diarioas.guialigas.dao.model.player.Player;
import com.diarioas.guialigas.utils.Defines.ShieldName;
import com.diarioas.guialigas.utils.Defines.StaffCharge;

public class Team implements Serializable {

	private static final long serialVersionUID = 5L;

	private String id;
	private String name;
	private String shortName;
	private boolean staticInfo;
	private String url;
	private String urlInfo;
	private String urlFicha;
	private String urlTag;
	private HashMap<String, String> shields;// Colocacion, url
	private String history;
	private Estadio estadio;
	private ArrayList<String> shirts;
	private ArrayList<TituloTeam> palmares;
	private HashMap<String, TeamStats> stats;// Año, Estadistica
	private HashMap<String, Staff> teamStaff;// Puesto, Persona
	private ArrayList<Player> plantilla;
	private ArrayList<PlayerOnField> idealPlayers;
	private String gameSystem;
	private Article article;
	private int fecModificacion;
	private String web;
	private String country;
	private String city;
	private String fundation;
	private ClasificacionInfo clasificacion;
	private ArrayList<String> historicalPalmares;
	private String federationFoundation;
	private String federationAffiliation;
	private String NumPlayers;
	private String NumClubs;
	private String NumReferees;

	public Team() {
		shirts = new ArrayList<String>();
		shields = new HashMap<String, String>();
		teamStaff = new HashMap<String, Staff>();
		plantilla = new ArrayList<Player>();
		idealPlayers = new ArrayList<PlayerOnField>();
		palmares = new ArrayList<TituloTeam>();
		stats = new HashMap<String, TeamStats>();
		historicalPalmares = new ArrayList<String>();
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the staticInfo
	 */
	public boolean isStaticInfo() {
		return staticInfo;
	}

	/**
	 * @param staticInfo
	 *            the staticInfo to set
	 */
	public void setStaticInfo(boolean staticInfo) {
		this.staticInfo = staticInfo;
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
		this.url = url;
	}

	/**
	 * @return the urlInfo
	 */
	public String getUrlInfo() {
		return urlInfo;
	}

	/**
	 * @param urlInfo
	 *            the urlInfo to set
	 */
	public void setUrlInfo(String urlInfo) {
		this.urlInfo = urlInfo;
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
	 * @return the shields
	 */
	public HashMap<String, String> getShields() {
		return shields;
	}

	public String getGridShield() {
		return shields.get(ShieldName.GRID);
	}

	public String getCalendarShield() {
		return shields.get(ShieldName.CALENDAR);
	}

	public String getDetailShield() {
		return shields.get(ShieldName.DETAIL);
	}

	/**
	 * @param shields
	 *            the shields to set
	 */
	public void setShields(HashMap<String, String> shields) {
		this.shields = shields;
	}

	/**
	 * @param string
	 * @param string
	 */
	public void addShield(String detail, String shield) {
		// if (!shield.startsWith(ShieldName.PREFIX_FLAG))
		// shield = ShieldName.PREFIX_FLAG + shield;
		this.shields.put(detail, shield);

	}

	public void addShieldGrid(String shield) {
		// if (!shield.startsWith(ShieldName.PREFIX_FLAG))
		// shield = ShieldName.PREFIX_FLAG + shield;
		this.shields.put(ShieldName.GRID, shield);

	}

	public void addShieldCalendar(String shield) {
		// if (!shield.startsWith(ShieldName.PREFIX_FLAG))
		// shield = ShieldName.PREFIX_FLAG + shield;
		this.shields.put(ShieldName.CALENDAR, shield);

	}

	public void addShieldDetail(String shield) {
		// if (!shield.startsWith(ShieldName.PREFIX_SHIELD))
		// shield = ShieldName.PREFIX_SHIELD + shield;
		this.shields.put(ShieldName.DETAIL, shield);

	}

	/**
	 * @return the history
	 */
	public String getHistory() {
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(String history) {
		this.history = history;
	}

	/**
	 * @return the estadio
	 */
	public Estadio getEstadio() {
		return estadio;
	}

	/**
	 * @param estadio
	 *            the estadio to set
	 */
	public void setEstadio(Estadio estadio) {
		this.estadio = estadio;
	}

	/**
	 * @return the shirts
	 */
	public ArrayList<String> getShirts() {
		return shirts;
	}

	public int getCountShirts() {

		return shirts.size();
	}

	/**
	 * @param shirts
	 *            the shirts to set
	 */
	public void setShirts(ArrayList<String> shirts) {
		this.shirts = shirts;
	}

	/**
	 * @param string
	 */
	public void addShirt(String shirt) {
		this.shirts.add(shirt);

	}

	/**
	 * @return the palmares
	 */
	public ArrayList<TituloTeam> getPalmares() {
		return palmares;
	}

	/**
	 * @param palmares
	 *            the palmares to set
	 */
	public void setPalmares(ArrayList<TituloTeam> palmares) {
		this.palmares = palmares;
	}

	/**
	 * @param string
	 * @param object
	 */
	public void addTitle(String name, ArrayList<String> years) {
		this.palmares.add(new TituloTeam(name, years));
	}

	/**
	 * @param string
	 * @param object
	 */
	public void addTitle(TituloTeam title) {
		this.palmares.add(title);
	}

	/**
	 * @return the stats
	 */
	public HashMap<String, TeamStats> getStats() {
		return stats;
	}

	public TeamStats getStatYear(String year) {
		return stats.get(year);
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(HashMap<String, TeamStats> stats) {
		this.stats = stats;
	}

	public void addStat(String year, TeamStats stat) {
		stat.setYear(year);
		this.stats.put(year, stat);
	}

	// public void countStats() {
	// Set<String> keys = stats.keySet();
	// int sum = 0;
	// for (String key : keys) {
	// TeamStats st = stats.get(key);
	// sum += st.getStats().keySet().size();
	// }
	// Log.d("STATS", "NumeroStats: " + sum);
	// }

	/**
	 * @return the teamStaff
	 */
	public HashMap<String, Staff> getTeamStaff() {
		return teamStaff;
	}

	/**
	 * @param teamStaff
	 *            the teamStaff to set
	 */
	public void setTeamStaff(HashMap<String, Staff> teamStaff) {
		this.teamStaff = teamStaff;
	}

	/**
	 * @param string
	 * @param staff
	 */
	public void addTeamStaff(String charge, Staff staff) {
		this.teamStaff.put(charge, staff);

	}

	public void addPresident(Staff staff) {
		this.teamStaff.put(StaffCharge.PRESIDENT, staff);
	}

	public void addManager(Staff staff) {
		this.teamStaff.put(StaffCharge.MANAGER, staff);
	}

	public void addMister(Staff staff) {
		this.teamStaff.put(StaffCharge.MISTER, staff);
	}

	// public void addStar(Staff staff) {
	// this.teamStaff.put(StaffCharge.STAR, staff);
	//
	// }

	public Staff getStaff(String staff) {
		return this.teamStaff.get(staff);
	}

	public Staff getPresident() {
		return this.teamStaff.get(StaffCharge.PRESIDENT);
	}

	public Staff getManager() {
		return this.teamStaff.get(StaffCharge.MANAGER);
	}

	public Staff getMister() {
		return this.teamStaff.get(StaffCharge.MISTER);
	}

	/**
	 * @return the plantilla
	 */
	public ArrayList<Player> getPlantilla() {
		return plantilla;
	}

	/**
	 * @param plantilla
	 *            the plantilla to set
	 */
	public void setPlantilla(ArrayList<Player> plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * @param player
	 */
	public void addPlayer(Player player) {
		// this.plantilla.add(player.getDorsal(), player);
		this.plantilla.add(player);

	}

	/**
	 * @return the idealPlayers
	 */
	public ArrayList<PlayerOnField> getIdealPlayers() {
		return idealPlayers;
	}

	/**
	 * @param idealPlayers
	 *            the idealPlayers to set
	 */
	public void setIdealPlayers(ArrayList<PlayerOnField> idealPlayers) {
		this.idealPlayers = idealPlayers;
	}

	/**
	 * @param player
	 */
	public void addIdealPlayer(PlayerOnField player) {
		// this.plantilla.add(player.getDorsal(), player);
		this.idealPlayers.add(player);

	}

	/**
	 * @return the gameSystem
	 */
	public String getGameSystem() {
		return gameSystem;
	}

	/**
	 * @param gameSystem
	 *            the gameSystem to set
	 */
	public void setGameSystem(String gameSystem) {
		this.gameSystem = gameSystem;
	}

	/**
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * @param article
	 *            the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}

	/**
	 * @return the fecModificacion
	 */
	public int getFecModificacion() {
		return fecModificacion;
	}

	/**
	 * @return the federationFoundation
	 */
	public String getFederationFoundation() {
		return federationFoundation;
	}

	/**
	 * @param federationFoundation
	 *            the federationFoundation to set
	 */
	public void setFederationFoundation(String federationFoundation) {
		this.federationFoundation = federationFoundation;
	}

	/**
	 * @return the federationAffiliation
	 */
	public String getFederationAffiliation() {
		return federationAffiliation;
	}

	/**
	 * @param federationAffiliation
	 *            the federationAffiliation to set
	 */
	public void setFederationAffiliation(String federationAffiliation) {
		this.federationAffiliation = federationAffiliation;
	}

	/**
	 * @return the numPlayers
	 */
	public String getNumPlayers() {
		return NumPlayers;
	}

	/**
	 * @param numPlayers
	 *            the numPlayers to set
	 */
	public void setNumPlayers(String numPlayers) {
		NumPlayers = numPlayers;
	}

	/**
	 * @return the numClubs
	 */
	public String getNumClubs() {
		return NumClubs;
	}

	/**
	 * @param numClubs
	 *            the numClubs to set
	 */
	public void setNumClubs(String numClubs) {
		NumClubs = numClubs;
	}

	/**
	 * @return the numReferees
	 */
	public String getNumReferees() {
		return NumReferees;
	}

	/**
	 * @param numReferees
	 *            the numReferees to set
	 */
	public void setNumReferees(String numReferees) {
		NumReferees = numReferees;
	}

	/**
	 * @return the fundation
	 */
	public String getFundation() {
		return fundation;
	}

	/**
	 * @param fecModificacion
	 *            the fecModificacion to set
	 */
	public void setFecModificacion(int fecModificacion) {
		this.fecModificacion = fecModificacion;
	}

	/**
	 * @return the web
	 */
	public String getWeb() {
		return web;
	}

	/**
	 * @param web
	 *            the web to set
	 */
	public void setWeb(String web) {
		if (web != null && !web.equalsIgnoreCase("")
				&& !web.startsWith("http://") && !web.startsWith("https://"))
			web = "http://" + web;
		this.web = web;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the fundation
	 */
	public String getfundation() {
		return fundation;
	}

	/**
	 * @param fundation
	 *            the fundation to set
	 */
	public void setFundation(String fundation) {
		this.fundation = fundation;
	}

	/**
	 * @return the clasificacion
	 */
	public ClasificacionInfo getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion
	 *            the clasificacion to set
	 */
	public void setClasificacion(ClasificacionInfo clasificacion) {
		this.clasificacion = clasificacion;
	}

	/**
	 * @return the historicalPalmares
	 */
	public ArrayList<String> getHistoricalPalmares() {
		return historicalPalmares;
	}

	/**
	 * @param historicalPalmares
	 *            the historicalPalmares to set
	 */
	public void setHistoricalPalmares(ArrayList<String> historicalPalmares) {
		this.historicalPalmares = historicalPalmares;
	}

}
