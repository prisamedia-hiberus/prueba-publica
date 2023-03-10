package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import xmlwise.Plist;
import xmlwise.XmlParseException;
import android.util.Log;

import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.utils.comparator.CompetitionComparator;

public class ParsePlistLoad {

	private HashMap<?, ?> hashMap;

	/**
	 * 
	 */
	public ParsePlistLoad(String source) {
		try {

			hashMap = (HashMap<?, ?>) Plist.objectFromXml(source);
		} catch (XmlParseException e) {
			Log.e("PARSEPLISTLOAD",
					"Error al leer el fichero: " + e.getMessage());
		}
	}

	public HashMap<?, ?> parsePlistSplash() {
		if (hashMap != null)
			return (HashMap<String, HashMap<String, String>>) hashMap
					.get("splash");
		else
			return null;
	}
	
	public HashMap<?, ?> parsePlistHeader() {
		if (hashMap != null)
			return (HashMap<String, HashMap<String, String>>) hashMap
					.get("header");
		else
			return null;
	}

	
	public HashMap<String, String> parsePlistPrefix() {
		return getHashMap("URLPrefixes");
	}

	/**
	 * @return
	 */
	public HashMap<String, String> parsePlistStatus() {
		return getHashMap("status");
	}

	/**
	 * @return
	 */
	public HashMap<String, String> parsePlistGamePlay() {
		return getHashMap("system");
	}

	/**
	 * @return
	 */
	public HashMap<String, String> parsePlistSpades() {
		return getHashMap("spades");
	}

	/**
	 * @return
	 */
	public HashMap<String, String> parsePlistClasificationLabels() {
		return getHashMap("clasificationLabels");
	}

	public HashMap<String, String> parsePlistCookies() {
		if (hashMap.containsKey("cookies")) {
			ArrayList<HashMap<String, String>> cookies = (ArrayList<HashMap<String, String>>) hashMap
					.get("cookies");
			if (cookies != null) {
				HashMap<String, String> currentCookieParsed = new HashMap<String, String>();
				int numCookies = cookies.size();
				for (int i = 0; i < numCookies; i++) {
					HashMap<String, String> cookie = cookies.get(i);
					if (cookie.containsKey("name")
							&& cookie.containsKey("value")) {
						currentCookieParsed.put(cookie.get("name"),
								cookie.get("value"));
					}
				}
				return currentCookieParsed;
			}
		}
		
		return null;
	}

	// /**
	// * @return
	// */
	// public String parsePlistSearch() {
	// if (hashMap != null) {
	// HashMap<String, String> search = (HashMap<String, String>) hashMap
	// .get("search");
	// return search.get("queryPath");
	// } else
	// return null;
	// }

	private HashMap<String, String> getHashMap(String item) {
		if (hashMap != null)
			return (HashMap<String, String>) hashMap.get(item);
		else
			return null;
	}

	/**
	 * @return
	 */
	public ArrayList<Competition> parsePlistCompetitions() {

		ArrayList<Competition> competitions = new ArrayList<Competition>();

		HashMap<String, HashMap<String, String>> competitionsPlist = (HashMap<String, HashMap<String, String>>) hashMap
				.get("competitions");
		Set<String> keys = competitionsPlist.keySet();
		Competition competition;
		HashMap<?, ?> c;
		for (String key : keys) {

			c = competitionsPlist.get(key);
			competition = new Competition();
			competition.setId(Integer.valueOf(key));

			competition.setName((String) c.get("description"));
			competition.setImage((String) c.get("headerLogo"));
			competition.setUrl((String) c.get("confFileURL"));
			competition.setOrder(Integer.valueOf((String) c.get("order")));
			Date date = (Date) c.get("conFileTimestamp");
			competition.setFecModificacion(date.getTime());

			competitions.add(competition);

		}

		Collections.sort(competitions, new CompetitionComparator());
		return competitions;
	}

}
