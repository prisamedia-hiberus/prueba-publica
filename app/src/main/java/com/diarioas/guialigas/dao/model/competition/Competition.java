package com.diarioas.guialigas.dao.model.competition;

import java.util.ArrayList;

import com.diarioas.guialigas.dao.model.general.Section;

public class Competition {

	private int id;
	private String name;
	private String image;

	private String url;
	private int order;
	private float fecModificacion;

	private ArrayList<Section> sections;

	// private ArrayList<Group> groups;
	// private String urlCalendar;
	// private String urlCarrusel;
	// private String urlStadium;
	// private String urlPalmares;
	// private HashMap<String, String> urlClasificacion;

	public Competition() {
		// groups = new ArrayList<Group>();
		sections = new ArrayList<Section>();
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
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	//
	// /**
	// * @return the teams
	// */
	// public ArrayList<Group> getGroups() {
	// return groups;
	// }
	//
	// /**
	// * @param teams
	// * the teams to set
	// */
	// public void setGroups(ArrayList<Group> groups) {
	// this.groups = groups;
	// }
	//
	// /**
	// * @param team
	// */
	// public void addGroup(Group group) {
	// this.groups.add(group);
	// }

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

	// /**
	// * @return the urlCalendar
	// */
	// public String getUrlCalendar() {
	// return urlCalendar;
	// }
	//
	// /**
	// * @param urlCalendar
	// * the urlCalendar to set
	// */
	// public void setUrlCalendar(String url) {
	// if (url != null && !url.startsWith("http://")
	// && !url.startsWith("https://"))
	// url = "http://" + url;
	//
	// this.urlCalendar = url;
	// }
	//
	// /**
	// * @return the urlCarrusel
	// */
	// public String getUrlCarrusel() {
	// return urlCarrusel;
	// }
	//
	// /**
	// * @param urlCarrusel
	// * the urlCarrusel to set
	// */
	// public void setUrlCarrusel(String urlCarrusel) {
	// this.urlCarrusel = urlCarrusel;
	// }
	//
	// /**
	// * @return the urlStadium
	// */
	// public String getUrlStadium() {
	// return urlStadium;
	// }
	//
	// /**
	// * @param urlStadium
	// * the urlStadium to set
	// */
	// public void setUrlStadium(String urlStadium) {
	// this.urlStadium = urlStadium;
	// }
	//
	// /**
	// * @return the urlPalmares
	// */
	// public String getUrlPalmares() {
	// return urlPalmares;
	// }
	//
	// /**
	// * @param urlPalmares
	// * the urlPalmares to set
	// */
	// public void setUrlPalmares(String urlPalmares) {
	// this.urlPalmares = urlPalmares;
	// }
	//
	// /**
	// * @return the urlClasificacion
	// */
	// public HashMap<String, String> getUrlClasificacion() {
	// return urlClasificacion;
	// }
	//
	// /**
	// * @param urlClasificacion
	// * the urlClasificacion to set
	// */
	// public void setUrlClasificacion(HashMap<String, String>
	// clasificacionResult) {
	// // if (clasificacionResult != null &&
	// // !clasificacionResult.startsWith("http://")
	// // && !clasificacionResult.startsWith("https://"))
	// // clasificacionResult = "http://" + clasificacionResult;
	// this.urlClasificacion = clasificacionResult;
	// }

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
	 * @return the fecModificacion
	 */
	public float getFecModificacion() {
		return fecModificacion;
	}

	/**
	 * @param fecModificacion
	 *            the fecModificacion to set
	 */
	public void setFecModificacion(float fecModificacion) {
		this.fecModificacion = fecModificacion;
	}

	/**
	 * @return the sections
	 */
	public ArrayList<Section> getSections() {
		return sections;
	}

	public Section getDefaultSections() {
		return sections.get(0);
	}

	/**
	 * @param sections
	 *            the sections to set
	 */
	public void setSections(ArrayList<Section> sections) {
		this.sections = sections;
	}

	public void addSection(Section section) {
		if (section != null)
			this.sections.add(section);
	}

	public Section getSection(String name) {
		for (Section section : sections) {
			if (name.equalsIgnoreCase(section.getType())) {
				return section;
			}
		}
		return null;
	}

}
