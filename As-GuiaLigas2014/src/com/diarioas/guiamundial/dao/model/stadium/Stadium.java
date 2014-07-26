package com.diarioas.guiamundial.dao.model.stadium;

import java.util.ArrayList;

public class Stadium {

	private int id;
	private String photo;
	private String urlInfo;
	private String stadiumName;
	private String stadiumMap;
	private int stadiumCapacity;
	private int stadiumYear;
	private String stadiumHistory;
	private ArrayList<String> stadiumPhotos;
	private String cityName;
	private String cityState;
	private String cityPopulation;
	private String cityAltitude;
	private String cityHistory;
	private String cityTransport;
	private String cityEconomy;
	private String cityTourism;
	private ArrayList<String> cityPhotos;

	public Stadium() {
		stadiumPhotos = new ArrayList<String>();
		cityPhotos = new ArrayList<String>();
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
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
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
	 * @return the stadium_name
	 */
	public String getStadiumName() {
		return stadiumName;
	}

	/**
	 * @param stadium_name
	 *            the stadium_name to set
	 */
	public void setStadiumName(String stadium_name) {
		this.stadiumName = stadium_name;
	}

	/**
	 * @return the stadiumMap
	 */
	public String getStadiumMap() {
		return stadiumMap;
	}

	/**
	 * @param stadiumMap
	 *            the stadiumMap to set
	 */
	public void setStadiumMap(String stadiumMap) {
		this.stadiumMap = stadiumMap;
	}

	/**
	 * @return the stadiumCapacity
	 */
	public int getStadiumCapacity() {
		return stadiumCapacity;
	}

	/**
	 * @param stadiumCapacity
	 *            the stadiumCapacity to set
	 */
	public void setStadiumCapacity(int stadiumCapacity) {
		this.stadiumCapacity = stadiumCapacity;
	}

	/**
	 * @return the stadiumYear
	 */
	public int getStadiumYear() {
		return stadiumYear;
	}

	/**
	 * @param stadiumYear
	 *            the stadiumYear to set
	 */
	public void setStadiumYear(int stadiumYear) {
		this.stadiumYear = stadiumYear;
	}

	/**
	 * @return the stadiumHistory
	 */
	public String getStadiumHistory() {
		return stadiumHistory;
	}

	/**
	 * @param stadiumHistory
	 *            the stadiumHistory to set
	 */
	public void setStadiumHistory(String stadiumHistory) {
		this.stadiumHistory = stadiumHistory;
	}

	/**
	 * @return the stadiumPhotos
	 */
	public ArrayList<String> getStadiumPhotos() {
		return stadiumPhotos;
	}

	/**
	 * @param stadiumPhotos
	 *            the stadiumPhotos to set
	 */
	public void setStadiumPhotos(ArrayList<String> stadiumPhotos) {
		this.stadiumPhotos = stadiumPhotos;
	}

	public void addStadiumPhoto(String photo) {
		this.stadiumPhotos.add(photo);
	}

	/**
	 * @return the city_name
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param city_name
	 *            the city_name to set
	 */
	public void setCityName(String city_name) {
		this.cityName = city_name;
	}

	/**
	 * @return the cityState
	 */
	public String getCityState() {
		return cityState;
	}

	/**
	 * @param cityState
	 *            the cityState to set
	 */
	public void setCityState(String cityState) {
		this.cityState = cityState;
	}

	/**
	 * @return the cityPopulation
	 */
	public String getCityPopulation() {
		return cityPopulation;
	}

	/**
	 * @param cityPopulation
	 *            the cityPopulation to set
	 */
	public void setCityPopulation(String cityPopulation) {
		this.cityPopulation = cityPopulation;
	}

	/**
	 * @return the cityAltitude
	 */
	public String getCityAltitude() {
		return cityAltitude;
	}

	/**
	 * @param cityAltitude
	 *            the cityAltitude to set
	 */
	public void setCityAltitude(String cityAltitude) {
		this.cityAltitude = cityAltitude;
	}

	/**
	 * @return the cityHistory
	 */
	public String getCityHistory() {
		return cityHistory;
	}

	/**
	 * @param cityHistory
	 *            the cityHistory to set
	 */
	public void setCityHistory(String cityHistory) {
		this.cityHistory = cityHistory;
	}

	/**
	 * @return the cityTransport
	 */
	public String getCityTransport() {
		return cityTransport;
	}

	/**
	 * @param cityTransport
	 *            the cityTransport to set
	 */
	public void setCityTransport(String cityTransport) {
		this.cityTransport = cityTransport;
	}

	/**
	 * @return the cityEconomy
	 */
	public String getCityEconomy() {
		return cityEconomy;
	}

	/**
	 * @param cityEconomy
	 *            the cityEconomy to set
	 */
	public void setCityEconomy(String cityEconomy) {
		this.cityEconomy = cityEconomy;
	}

	/**
	 * @return the cityTourism
	 */
	public String getCityTourism() {
		return cityTourism;
	}

	/**
	 * @param cityTourism
	 *            the cityTourism to set
	 */
	public void setCityTourism(String cityTourism) {
		this.cityTourism = cityTourism;
	}

	/**
	 * @return the cityPhotos
	 */
	public ArrayList<String> getCityPhotos() {
		return cityPhotos;
	}

	/**
	 * @param cityPhotos
	 *            the cityPhotos to set
	 */
	public void setCityPhotos(ArrayList<String> cityPhotos) {
		this.cityPhotos = cityPhotos;
	}

	public void addCityPhoto(String photo) {
		this.cityPhotos.add(photo);
	}

}
