/**
 * 
 */
package com.diarioas.guiamundial.dao.model.team;

/**
 * @author robertosanchez
 * 
 */
public class Estadio {

	private String name;
	private String country;
	private String city;
	private int capacity;
	private String address;
	private double dimX;
	private double dimY;
	private double lat;
	private double lon;

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
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the dimX
	 */
	public double getDimX() {
		return dimX;
	}

	/**
	 * @param dimX
	 *            the dimX to set
	 */
	public void setDimX(double dimX) {
		this.dimX = dimX;
	}

	/**
	 * @return the dimY
	 */
	public double getDimY() {
		return dimY;
	}

	/**
	 * @param dimY
	 *            the dimY to set
	 */
	public void setDimY(double dimY) {
		this.dimY = dimY;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon
	 *            the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}
}
