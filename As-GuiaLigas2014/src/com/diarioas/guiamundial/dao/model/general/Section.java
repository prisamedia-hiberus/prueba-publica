package com.diarioas.guiamundial.dao.model.general;

import java.io.Serializable;

public class Section implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String type;
	private String viewType;
	private String url;
	private int order;
	private boolean active = true;
	private boolean start = false;

	public Section() {

	}

	public Section(String name, String type, String viewType, String url, int order,
			boolean start, boolean active) {
		this.type = type;
		this.url = url;
		this.order = order;
		this.start = start;
		this.active = active;
	}

	public Section(String name, String type, String viewType, String url, int order,
			boolean start) {
		this.type = type;
		this.url = url;
		this.order = order;
		this.start = start;
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
	 * @return the name
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the name to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the viewType
	 */
	public String getViewType() {
		return viewType;
	}

	/**
	 * @param viewType
	 *            the viewType to set
	 */
	public void setViewType(String viewType) {
		this.viewType = viewType;
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
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
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
	 * @return the start
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(boolean start) {
		this.start = start;
	}

}
