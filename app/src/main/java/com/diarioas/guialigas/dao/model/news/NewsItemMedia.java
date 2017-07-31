package com.diarioas.guialigas.dao.model.news;

import java.io.Serializable;

public class NewsItemMedia implements Serializable {

	private static final long serialVersionUID = 5L;

	protected int width;
	protected int height;
	protected String url = null;
	private String urlPhoto = null;
	protected String category = null;
	protected String author = null;
	private String caption = null;
	private String pubDate = null;
	private String description = null;
	private String idBC = null;

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
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
	 * @return the urlPhoto
	 */
	public String getUrlPhoto() {
		return urlPhoto;
	}

	/**
	 * @param urlPhoto
	 *            the urlPhoto to set
	 */
	public void setUrlPhoto(String urlPhoto) {
		this.urlPhoto = urlPhoto;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 
	 * @param idBC
	 * 
	 */
	public String getIdBC() {
		return idBC;
	}

	public void setIdBC(String idBC) {
		this.idBC = idBC;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

}
