package com.diarioas.guialigas.dao.reader.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import xmlwise.Plist;
import xmlwise.XmlParseException;
import android.util.Log;

import com.diarioas.guialigas.dao.model.competition.Group;
import com.diarioas.guialigas.dao.model.general.ClasificacionSection;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.model.general.TeamSection;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.utils.Defines.SECTIONS;
import com.diarioas.guialigas.utils.Defines.ShieldName;

public class ParsePlistCompetition {

	private String dataPrefix;
	private String dataRSSPrefix;
	private ArrayList<HashMap<String, ?>> menu;

	/**
	 * @throws XmlParseException
	 * @throws IOException
	 * @throws XmlPullParserException
	 * 
	 */
	public ParsePlistCompetition(String source, String dataPrefix,
			String dataRSSPrefix) throws XmlParseException,
			XmlPullParserException, IOException {
		HashMap<?, ?> hashMap;
		try {
			this.dataPrefix = dataPrefix;
			this.dataRSSPrefix = dataRSSPrefix;
			hashMap = (HashMap<?, ?>) Plist.objectFromXml(source);
			menu = (ArrayList<HashMap<String, ?>>) hashMap.get("menu");
		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			Log.w("ParsePlistCompetition",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			try {
				hashMap = readXml(source);
			} catch (XmlPullParserException xe) {
				Log.e("ParsePlistCompetition",
						"No se ha parseado bien el fichero - Metodo 2: "
								+ xe.getMessage());
				throw xe;
			} catch (IOException ie) {
				Log.e("ParsePlistCompetition",
						"No se ha parseado bien el fichero - Metodo 2: "
								+ ie.getMessage());
				throw ie;
			}

			// e.printStackTrace();
		}
	}

	public ArrayList<HashMap<String, ?>> parsePlistMenu() {
		return menu;
	}

	/**
	 * @param order
	 * @param active
	 * @param strFileContents
	 * @return
	 */
	public Section parsePlistCalendar(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.CALENDAR,
					"calendarWS", dataPrefix, order + offset);

		} else {
			return null;
		}
	}

	public Section parsePlistSearch(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.SEARCHER, "queryPath",
					dataPrefix, order + offset);
		} else {
			return null;
		}
	}

	/**
	 * @param order
	 * @param strFileContents
	 * @return
	 */
	public Section parsePlistCarrusel(int order, int offset) {
		if (menu != null) {

			return readSection(menu.get(order), SECTIONS.CARROUSEL,
					"carruselWS", dataPrefix, order + offset);

		} else {
			return null;
		}
	}

	public Section parsePlistStadium(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.STADIUMS,
					"stadiumInfo", null, order + offset);

		} else {
			return null;
		}
	}

	public Section parsePlistPalmares(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.PALMARES,
					"palmaresInfo", null, order + offset);
		} else {
			return null;
		}

	}

	public Section parsePlistComparator(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.COMPARATOR, null,
					null, order + offset);
		} else {
			return null;
		}

	}

	public Section parsePlistNews(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.NEWS, "noticiasWS",
					dataRSSPrefix, order + offset);

		} else {
			return null;
		}
	}

	public Section parsePlistVideos(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.VIDEOS, "videosWS",
					dataRSSPrefix, order + offset);

		} else {
			return null;
		}
	}

	public Section parsePlistPhotos(int order, int offset) {
		if (menu != null) {
			return readSection(menu.get(order), SECTIONS.PHOTOS,
					"photoGalleryWS", null, order + offset);

		} else {
			return null;
		}
	}

	private Section readSection(HashMap<String, ?> sectionJSON, String type,
			String key, String prefix, int order) {
		String retorno = null;
		if (key != null) {
			if (sectionJSON.containsKey(key)) {
				if (prefix != null) {
					if (sectionJSON.get(key) != null
							&& !((String) sectionJSON.get(key))
									.equalsIgnoreCase("")) {
						retorno = prefix + (String) sectionJSON.get(key);
					}
				} else {
					retorno = (String) sectionJSON.get(key);
				}

			}
		}

		boolean start = false;
		if (sectionJSON.containsKey("default")) {
			start = (Boolean) sectionJSON.get("default");
		}

		if (sectionJSON.containsKey("available"))
			return new Section((String) sectionJSON.get("name"), type, (String) sectionJSON.get("viewType"),
					retorno, order, start,
					(Boolean) sectionJSON.get("available"));
		else
			return new Section((String) sectionJSON.get("name"), type, (String) sectionJSON.get("viewType"),
					retorno, order, start);

	}

	public Section parsePlistLink(int order, int offset) {
		if (menu != null) {

			Section section = null;
			try {

				HashMap<String, ?> sectionJSON = menu.get(order);

				section = new Section();
				section.setType(SECTIONS.LINK);
				section.setName((String) sectionJSON.get("name"));
				section.setViewType((String) sectionJSON.get("viewType"));
				section.setOrder(order + offset);
				section.setActive((Boolean) sectionJSON.get("available"));
				if (sectionJSON.containsKey("default")) {
					section.setStart((Boolean) sectionJSON.get("default"));
				}
				section.setUrl((String) sectionJSON.get("triviaWV"));
				section.setViewType((String) sectionJSON.get("viewType"));

				return section;

			} catch (Exception e) {

				Log.d("PARSEPLISTCOMPETITION",
						"No se ha leido correctamente trivias");
			}

			return section;
		} else {
			return null;
		}
	}

	public Section parsePlistExternalLink(int order, int offset) {
		if (menu != null) {

			Section section = null;
			try {

				HashMap<String, ?> sectionJSON = menu.get(order);

				section = new Section();
				section.setType(SECTIONS.LINK_VIEW_OUTSIDE);
				section.setName((String) sectionJSON.get("name"));
				section.setViewType((String) sectionJSON.get("viewType"));
				section.setOrder(order + offset);
				section.setActive((Boolean) sectionJSON.get("available"));
				if (sectionJSON.containsKey("default")) {
					section.setStart((Boolean) sectionJSON.get("default"));
				}
				section.setUrl((String) sectionJSON.get("adidasWV"));

				return section;

			} catch (Exception e) {

				Log.d("PARSEPLISTCOMPETITION",
						"No se ha leido correctamente trivias");
			}

			return section;
		} else {
			return null;
		}
	}

	/**
	 * @param order
	 * @return
	 */
	public Section parsePlistClasificacion(int order, int offset) {
		if (menu != null) {
			HashMap<String, String> retorno;
			ClasificacionSection section = null;
			try {

				HashMap<String, ?> sectionJSON = menu.get(order);

				retorno = (HashMap<String, String>) sectionJSON
						.get("clasificationWS");
				section = new ClasificacionSection();
				section.setType(SECTIONS.CLASIFICATION);
				section.setName((String) sectionJSON.get("name"));
				section.setViewType((String) sectionJSON.get("viewType"));
				section.setOrder(order + offset);
				section.setActive((Boolean) sectionJSON.get("available"));
				if (sectionJSON.containsKey("default")) {
					section.setStart((Boolean) sectionJSON.get("default"));
				}
				if (retorno != null) {
					String key;
					for (Iterator<String> iterator = retorno.keySet()
							.iterator(); iterator.hasNext();) {
						key = iterator.next();
						section.addUrl(key, dataPrefix + retorno.get(key));
					}
				}

				return section;

			} catch (Exception e) {
				retorno = null;
				Log.d("PARSEPLISTCOMPETITION",
						"No se ha leido correctamente la clasificacion");
			}

			return section;
		} else {
			return null;
		}
	}

	public Section parsePlistGrid(int order, int offset) {
		if (menu != null) {
			HashMap<String, ?> sectionJSON = menu.get(order);

			TeamSection section = new TeamSection();
			section.setType(SECTIONS.TEAMS);
			section.setName((String) sectionJSON.get("name"));
			section.setViewType((String) sectionJSON.get("viewType"));
			section.setOrder(order + offset);
			section.setTypeOrder((String) sectionJSON.get("order"));
			section.setActive((Boolean) sectionJSON.get("available"));
			if (sectionJSON.containsKey("default")) {
				section.setStart((Boolean) sectionJSON.get("default"));
			}

			if (sectionJSON.containsKey("teamsByGroup")) {
				section.setGroups(getTeamsByGroup((HashMap<String, ArrayList<HashMap<?, ?>>>) sectionJSON
						.get("teamsByGroup")));
			} else if (sectionJSON.containsKey("teams")) {
				section.setGroups(getTeams((HashMap<String, HashMap<?, ?>>) sectionJSON
						.get("teams")));
			}

			return section;

		} else {
			return null;
		}

	}

	private ArrayList<Group> getTeams(HashMap<String, HashMap<?, ?>> groupPlist) {
		Team team;
		ArrayList<Group> groups = new ArrayList<Group>();
		Group group = new Group();

		Set<String> groupKeys = groupPlist.keySet();
		String id;
		HashMap<String, String> shields;
		for (Iterator<String> iterator = groupKeys.iterator(); iterator
				.hasNext();) {
			id = iterator.next();
			// Lee la informacion b‡sica del equipo
			team = new Team();
			team.setId(id);
			team.setShortName((String) groupPlist.get(id).get("name"));
			team.setUrl(dataPrefix
					+ ((String) groupPlist.get(id).get("teamWS")));
			team.setUrlInfo((String) groupPlist.get(id).get("teamInfo"));
			if (groupPlist.get(id).containsKey("shields")) {
				shields = (HashMap<String, String>) groupPlist.get(id).get(
						"shields");
				team.addShield(ShieldName.GRID, shields.get(ShieldName.GRID));
				team.addShield(ShieldName.CALENDAR,
						shields.get(ShieldName.CALENDAR));
				team.addShieldDetail(shields.get(ShieldName.DETAIL));
				team.addShield(ShieldName.DETAIL,
						shields.get(ShieldName.DETAIL));
			}
			group.addTeam(team);
		}
		groups.add(group);
		return groups;
	}

	private ArrayList<Group> getTeamsByGroup(
			HashMap<String, ArrayList<HashMap<?, ?>>> groupPlist) {
		Team team;
		ArrayList<Group> groups = new ArrayList<Group>();
		Group group;

		Set<String> groupKeys = groupPlist.keySet();

		ArrayList<HashMap<?, ?>> groupMap;
		HashMap<String, String> shields;

		for (String groupKey : groupKeys) {
			groupMap = groupPlist.get(groupKey);

			group = new Group();
			group.setName(groupKey);

			for (HashMap<?, ?> t : groupMap) {
				// Lee la informacion b‡sica del equipo
				team = new Team();
				team.setId((String) t.get("id"));
				team.setUrl(dataPrefix + ((String) t.get("teamWS")));
				team.setUrlInfo((String) t.get("teamInfo"));
				team.setShortName((String) t.get("name"));
				if (t.containsKey("shields")) {
					// team.setShields((HashMap<String, String>)
					// t.get("shields"));
					shields = (HashMap<String, String>) t.get("shields");
					team.addShield(ShieldName.GRID,
							shields.get(ShieldName.GRID));
					team.addShield(ShieldName.CALENDAR,
							shields.get(ShieldName.CALENDAR));
					team.addShieldDetail(shields.get(ShieldName.DETAIL));
					// team.addShield(ShieldName.DETAIL,shields.get(ShieldName.DETAIL));
				}

				group.addTeam(team);
			}

			groups.add(group);

		}
		return groups;
	}

	public HashMap<String, String> parsePlistPalmaresLabelsPosition(
			HashMap<String, ?> legendHistoricalPalmares) {

		return (HashMap<String, String>) legendHistoricalPalmares
				.get("positionY");
	}

	public HashMap<String, HashMap<String, String>> parsePlistPalmaresLabelsYear(
			HashMap<String, ?> legendHistoricalPalmares) {

		return (HashMap<String, HashMap<String, String>>) legendHistoricalPalmares
				.get("positionX");
	}

	public HashMap<String, String> parsePlistPalmaresLabelsLegend(
			HashMap<String, ?> legendHistoricalPalmares) {

		return (HashMap<String, String>) legendHistoricalPalmares.get("legend");
	}

	/*******************************************************************************/
	private static final String ns = null;

	/**
	 * @param source
	 * @return
	 */
	private HashMap<?, ?> readXml(String source) throws XmlPullParserException,
			IOException {
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(source.getBytes());
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			if (in != null)
				in.close();
		}

	}

	/**
	 * @param parser
	 * @return
	 */
	private HashMap<?, ?> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, Object> hasmap = new HashMap<String, Object>();

		parser.require(XmlPullParser.START_TAG, ns, "plist");

		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");
		String text;
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				text = readText(parser);
				if (text.equalsIgnoreCase("calendar")) {
					// leer calendario
					hasmap.put("calendar", readFeedCalendar(parser));
				} else if (text.equalsIgnoreCase("clasification")) {
					// leer clasificacion
					hasmap.put("clasification", readFeedClasificacion(parser));
					// readFeedClasificacion(parser);
				} else if (text.equalsIgnoreCase("carrusel")) {
					hasmap.put("carrusel", readFeedCarrusel(parser));
				} else if (text.equalsIgnoreCase("teams")) {
					// leer teams
					hasmap.put("teams", readFeedTeams(parser));
				} else {
					skip(parser);
				}
			} else {
				skip(parser);
			}
		}

		return hasmap;
	}

	/**
	 * @param parser
	 * @return
	 */
	private HashMap<String, HashMap<String, String>> readFeedClasificacion(
			XmlPullParser parser) throws XmlPullParserException, IOException {
		HashMap<String, String> hashmap;
		HashMap<String, HashMap<String, String>> hashMap2 = new HashMap<String, HashMap<String, String>>();
		String text1, text2;
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");

		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				text1 = readText(parser);
				if (text1.equalsIgnoreCase("clasificationWS")) {
					hashmap = new HashMap<String, String>();
					while (parser.nextTag() != XmlPullParser.END_TAG) {
						if (parser.getName().equalsIgnoreCase("key")) {
							text2 = readText(parser);
							if (parser.nextTag() != XmlPullParser.END_TAG) {
								if (parser.getName().equalsIgnoreCase("string")) {
									hashmap.put(text2, readText(parser));
								} else {
									skip(parser);
								}
							} else {
								skip(parser);
							}
						}
					}
					hashMap2.put(text1, hashmap);
				} else {
					skip(parser);
				}
			} else {
				skip(parser);
			}
		}

		return hashMap2;
	}

	/**
	 * @param parser
	 * @return
	 */
	private HashMap<String, String> readFeedCarrusel(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, String> hasmap = new HashMap<String, String>();
		String text;
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");

		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				text = readText(parser);
				if (text.equalsIgnoreCase("carruselWS")) {
					hasmap.put("carruselWS", readString(parser));
				} else {
					skip(parser);
				}
			} else {
				skip(parser);
			}
		}

		return hasmap;
	}

	/**
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private HashMap<String, String> readFeedCalendar(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, String> hasmap = new HashMap<String, String>();
		String text;
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");

		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				text = readText(parser);
				if (text.equalsIgnoreCase("calendarWS")) {
					hasmap.put("calendarWS", readString(parser));
				} else if (text.equalsIgnoreCase("resultsURL")) {
					hasmap.put("resultsURL", readString(parser));
				} else {
					skip(parser);
				}
			} else {
				skip(parser);
			}
		}

		return hasmap;
	}

	/**
	 * @param parser
	 * @return
	 */
	private HashMap<String, Object> readFeedTeams(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, Object> hasmap = new HashMap<String, Object>();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");
		String id;
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				id = readText(parser);
				hasmap.put(id, readTeam(parser));
			} else {
				skip(parser);
			}
		}

		return hasmap;
	}

	/**
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private Object readTeam(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");
		String text, subj;
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				text = readText(parser);
				if (text.equalsIgnoreCase("name")) {
					subj = readString(parser);
					hashMap.put("name", subj);
				} else if (text.equalsIgnoreCase("teamWS")) {
					subj = readString(parser);
					hashMap.put("teamWS", subj);
				} else if (text.equalsIgnoreCase("teamInfo")) {
					subj = readString(parser);
					hashMap.put("teamInfo", subj);
				} else if (text.equalsIgnoreCase("shields")) {
					hashMap.put("shields", readFeedShields(parser));
				} else {
					skip(parser);
				}
			} else {
				skip(parser);
			}

		}
		return hashMap;
	}

	/**
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private Object readFeedShields(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "dict");
		String text, subj;
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			if (parser.getName().equalsIgnoreCase("key")) {
				text = readText(parser);
				if (text.equalsIgnoreCase("grid")) {
					subj = readString(parser);
					hashMap.put("grid", subj);
				} else if (text.equalsIgnoreCase("calendar")) {
					subj = readString(parser);
					hashMap.put("calendar", subj);
				} else if (text.equalsIgnoreCase("detail")) {
					subj = readString(parser);
					hashMap.put("detail", subj);
				}
			}
		}
		return hashMap;
	}

	/**
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readString(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.nextTag();
		parser.require(XmlPullParser.START_TAG, ns, "string");
		return readText(parser);
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
