/**
 * 
 */
package com.diarioas.guialigas.dao.model.search;

/**
 * @author rober
 * 
 */
public class SearchItem {
	private String id;
	private String nombre;
	private String normalizado;
	private String url;
	private String urlDatos;
	private String alias;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the normalizado
	 */
	public String getNormalizado() {
		return normalizado;
	}

	/**
	 * @param normalizado
	 *            the normalizado to set
	 */
	public void setNormalizado(String normalizado) {
		this.normalizado = normalizado;
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
	 * @return the urlDatos
	 */
	public String getUrlDatos() {
		return urlDatos;
	}

	/**
	 * @param urlDatos
	 *            the urlDatos to set
	 */
	public void setUrlDatos(String url) {
		if (url != null && !url.startsWith("http://")
				&& !url.startsWith("https://"))
			url = "http://" + url;
		this.urlDatos = url;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

}
