package com.diarioas.guialigas.dao.model.general;

import java.util.HashMap;

public class ClasificacionSection extends Section {

	private static final long serialVersionUID = 3L;
	
	private HashMap<String, String> urls;

	public ClasificacionSection() {
		setUrls(new HashMap<String, String>());
	}

	/**
	 * @return the urls
	 */
	public HashMap<String, String> getUrls() {
		return urls;
	}

	/**
	 * @param urls
	 *            the urls to set
	 */
	public void setUrls(HashMap<String, String> urls) {
		this.urls = urls;
	}

	public void addUrl(String key, String url) {
		this.getUrls().put(key, url);

	}
}
