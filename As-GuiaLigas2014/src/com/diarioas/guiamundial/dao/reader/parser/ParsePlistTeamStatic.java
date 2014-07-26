package com.diarioas.guiamundial.dao.reader.parser;

import java.util.ArrayList;
import java.util.HashMap;

import xmlwise.Plist;
import xmlwise.XmlParseException;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.carrusel.PlayerOnField;
import com.diarioas.guiamundial.dao.model.team.Article;
import com.diarioas.guiamundial.dao.model.team.Staff;
import com.diarioas.guiamundial.dao.model.team.Star;
import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.utils.Defines.StaffCharge;

public class ParsePlistTeamStatic {

	private HashMap<?, ?> hashMap;

	/**
	 * @throws XmlParseException
	 * 
	 */
	public ParsePlistTeamStatic(String source) throws XmlParseException {
		try {

			hashMap = (HashMap<?, ?>) Plist.objectFromXml(source);
		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			Log.e("ParsePlistTeamStatic", "No se ha parseado bien el fichero: "
					+ e.getMessage());
			throw e;
			// e.printStackTrace();
		}
	}

	public Team parsePlistTeam(Team team, String dataPrefix,
			String imagePrefix, String videoPrefix) {
		if (hashMap != null) {

			if (hashMap.containsKey("shirtsArray")) {
				team.setShirts((ArrayList<String>) hashMap.get("shirtsArray"));
			}

			if (hashMap.containsKey("name")) {
				team.setName((String) hashMap.get("name"));
			}
			if (hashMap.containsKey("foundation")) {
				team.setFederationFoundation((String) hashMap.get("foundation"));
			}
			if (hashMap.containsKey("affiliation")) {
				team.setFederationAffiliation((String) hashMap
						.get("affiliation"));
			}
			if (hashMap.containsKey("web")) {
				team.setWeb((String) hashMap.get("web"));
			}
			if (hashMap.containsKey("players_number")) {
				team.setNumPlayers((String) hashMap.get("players_number"));
			}
			if (hashMap.containsKey("clubs")) {
				team.setNumClubs((String) hashMap.get("clubs"));
			}
			if (hashMap.containsKey("Referees")) {
				team.setNumReferees((String) hashMap.get("Referees"));
			}

			if (hashMap.containsKey("historicalPalmares")) {
				team.setHistoricalPalmares(((HashMap<String, ArrayList<String>>) hashMap
						.get("historicalPalmares")).get("positionY"));

			}

			Staff staff;
			// Read the staff
			HashMap<String, HashMap<String, String>> staffPeople = (HashMap<String, HashMap<String, String>>) hashMap
					.get("teamStaff");

			if (staffPeople.containsKey(StaffCharge.MISTER)) {
				staff = readPerson(staffPeople.get(StaffCharge.MISTER),
						imagePrefix);
				staff.setCharge(StaffCharge.MISTER);
				team.addMister(staff);
			}
			if (staffPeople.containsKey(StaffCharge.STAR)) {
				staff = readStar(staffPeople.get(StaffCharge.STAR), dataPrefix,
						imagePrefix);
				staff.setCharge(StaffCharge.STAR);
				team.addStar(staff);
			}
			if (hashMap.containsKey("system")) {
				team.setGameSystem((String) hashMap.get("system"));
			}
			if (hashMap.containsKey("players")) {
				team.setIdealPlayers(readPlayers(
						(ArrayList<HashMap<String, String>>) hashMap
								.get("players"), dataPrefix, imagePrefix));
			}

			// Read the article
			if (hashMap.containsKey("article")) {
				Article article = readArticle(
						(HashMap<String, String>) hashMap.get("article"),
						imagePrefix, videoPrefix);
				team.setArticle(article);
			}

			team.setStaticInfo(true);
			return team;

		} else
			return null;
	}

	private ArrayList<PlayerOnField> readPlayers(
			ArrayList<HashMap<String, String>> playersJSON, String dataPrefix,
			String imagePrefix) {

		ArrayList<PlayerOnField> players = new ArrayList<PlayerOnField>();
		PlayerOnField player;
		for (HashMap<String, String> playerJSON : playersJSON) {
			player = new PlayerOnField();
			if (playerJSON.containsKey("name"))
				player.setName(playerJSON.get("name"));
			if (playerJSON.containsKey("id") && playerJSON.get("id") != null
					&& !playerJSON.get("id").equalsIgnoreCase(""))
				player.setId(Integer.valueOf(playerJSON.get("id")));
			if (playerJSON.containsKey("pos"))
				player.setPosition(Integer.valueOf(playerJSON.get("pos")));
			if (playerJSON.containsKey("photo")
					&& playerJSON.get("photo").length() > 0)
				player.setUrlPhoto(imagePrefix + playerJSON.get("photo"));
			if (playerJSON.containsKey("ficha_player")
					&& playerJSON.get("ficha_player").length() > 0)
				player.setUrl(dataPrefix + playerJSON.get("ficha_player"));

			players.add(player);

		}
		return players;
	}

	private Article readArticle(HashMap<String, ?> art, String imagePrefix,
			String videoPrefix) {
		Article article = new Article();
		if (art.containsKey("author"))
			article.setAuthor((String) art.get("author"));
		if (art.containsKey("video")
				&& !((String) art.get("video")).equalsIgnoreCase(""))
			article.setUrlVideo(videoPrefix + art.get("video"));
		if (art.containsKey("thumbnails")) {
			ArrayList<String> thumbnails = (ArrayList<String>) art
					.get("thumbnails");
			if (thumbnails != null && thumbnails.size() > 0
					&& !thumbnails.get(0).equalsIgnoreCase(""))
				article.setUrlVideoImage(imagePrefix + thumbnails.get(0));
		}
		if (art.containsKey("body"))
			article.setBody((String) art.get("body"));

		return article;
	}

	private Staff readPerson(HashMap<String, String> person, String imagePrefix) {
		Staff staff = new Staff();
		if (person.containsKey("name"))
			staff.setName(person.get("name"));
		if (person.containsKey("photo"))
			staff.setPhoto(imagePrefix + person.get("photo"));
		if (person.containsKey("history"))
			staff.setHistory(person.get("history"));

		return staff;
	}

	private Staff readStar(HashMap<String, String> person, String dataPrefix,
			String imagePrefix) {
		Star staff = new Star();
		if (person.containsKey("name"))
			staff.setName(person.get("name"));
		if (person.containsKey("photo") && person.get("photo").length() > 0)
			staff.setPhoto(imagePrefix + person.get("photo"));
		if (person.containsKey("position"))
			staff.setPosition(person.get("position"));
		if (person.containsKey("internationalTimes"))
			staff.setNumInternational(person.get("internationalTimes"));
		if (person.containsKey("age"))
			staff.setAge(person.get("age"));
		if (person.containsKey("stature"))
			staff.setStature(person.get("stature"));
		if (person.containsKey("weight"))
			staff.setWeight(person.get("weight"));
		if (person.containsKey("club"))
			staff.setClubName(person.get("club"));
		if (person.containsKey("clubLogo"))
			staff.setClubShield(person.get("clubLogo"));
		if (person.containsKey("history"))
			staff.setHistory(person.get("history"));
		if (person.containsKey("data_player")
				&& person.get("data_player").length() > 0)
			staff.setUrl(dataPrefix + person.get("data_player"));
		if (person.containsKey("id"))
			staff.setPlayerId(person.get("id"));

		return staff;
	}

}
