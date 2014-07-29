package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;
import java.util.HashMap;

import xmlwise.Plist;
import xmlwise.XmlParseException;
import android.util.Log;

import com.diarioas.guialigas.dao.model.team.Article;
import com.diarioas.guialigas.dao.model.team.Staff;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.utils.Defines.StaffCharge;

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

	public Team parsePlistTeam(Team team) {
		if (hashMap != null) {

			team.setHistory((String) hashMap.get("history"));
			team.setShirts((ArrayList<String>) hashMap.get("shirtsArray"));

			// Read the staff
			HashMap<String, HashMap<String, String>> staffPeople = (HashMap<String, HashMap<String, String>>) hashMap
					.get("teamStaff");
			HashMap<String, String> person = staffPeople
					.get(StaffCharge.PRESIDENT);
			Staff staff = new Staff();
			staff.setCharge(StaffCharge.PRESIDENT);
			staff.setName(person.get("name"));
			// String born = person.get("bornPlace");
			// staff.setBorn(born.substring(born.indexOf("(") + 1,
			// born.length() - 1));
			staff.setBorn(person.get("bornPlace"));
			staff.setContract(person.get("contractDuration"));
			staff.setPhoto(person.get("photo"));
			team.addPresident(staff);

			person = staffPeople.get(StaffCharge.MANAGER);
			staff = new Staff();
			staff.setCharge(StaffCharge.MANAGER);
			staff.setName(person.get("name"));
			// born = person.get("bornPlace");
			// staff.setBorn(born.substring(born.indexOf("(") + 1,
			// born.length() - 1));
			staff.setBorn(person.get("bornPlace"));
			staff.setContract(person.get("contractDuration"));
			staff.setPhoto(person.get("photo"));
			team.addManager(staff);

			person = staffPeople.get(StaffCharge.MISTER);
			staff = new Staff();
			staff.setCharge(StaffCharge.MISTER);
			staff.setName(person.get("name"));
			// born = person.get("bornPlace");
			// staff.setBorn(born.substring(born.indexOf("(") + 1,
			// born.length() - 1));
			staff.setBorn(person.get("bornPlace"));
			staff.setContract(person.get("endContractDate"));
			staff.setPhoto(person.get("photo"));
			team.addMister(staff);

			// Read the article
			HashMap<String, String> art = (HashMap<String, String>) hashMap
					.get("article");
			Article article = new Article();
			article.setAuthor(art.get("author"));
			article.setCharge(art.get("charge"));
			article.setTitle(art.get("title"));
			article.setSubTitle(art.get("subtitle"));
			article.setBody(art.get("body"));
			team.setArticle(article);

			team.setStaticInfo(true);
			return team;

			// Set<String> keys = teamsPlist.keySet();
			// HashMap<String, HashMap<String, String>> staffPeople;
			// HashMap<String, String> person;
			// Staff staff;
			// Article article;

			// HashMap<?, ?> t;
			// for (String key : keys) {
			//
			// team = new Team();
			// t = teamsPlist.get(key);
			// team.setId(Integer.valueOf(key));
			// team.setUrl((String) t.get("teamWS"));
			// team.setUrlInfo((String) t.get("teamInfo"));
			// team.setShortName((String) t.get("name"));
			// if (t.containsKey("shields"))
			// team.setShields((HashMap<String, String>) t.get("shields"));
			// if (t.containsKey("history"))
			// team.setHistory((String) t.get("history"));
			// if (t.containsKey("shirtsArray"))
			//
			//

			//
			// teams.add(team);
			// }
			//
			// return teams;

		} else
			return null;
	}
}
