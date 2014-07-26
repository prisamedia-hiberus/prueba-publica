package com.diarioas.guiamundial.dao.reader.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import xmlwise.Plist;
import xmlwise.XmlParseException;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.palmares.Palmares;
import com.diarioas.guiamundial.utils.comparator.PalmaresComparator;

public class ParsePlistPalmares {

	public ArrayList<Palmares> parsePlistStadiums(String source) {

		try {
			HashMap<?, ?> hashMap = (HashMap<?, ?>) Plist.objectFromXml(source);

			ArrayList<Palmares> palmaresArray = new ArrayList<Palmares>();
			HashMap<String, HashMap<String, String>> palmaresJson = (HashMap<String, HashMap<String, String>>) hashMap
					.get("palmares");

			HashMap<String, String> object;
			String key;
			Palmares palmares;
			for (Iterator<String> iterator = palmaresJson.keySet().iterator(); iterator
					.hasNext();) {
				palmares = new Palmares();
				key = iterator.next();
				palmares.setName(key);
				object = palmaresJson.get(key);
				if (object.containsKey("winner"))
					palmares.setWinner(object.get("winner"));
				if (object.containsKey("finalist"))
					palmares.setFinalist(object.get("finalist"));
				if (object.containsKey("result"))
					palmares.setResult(object.get("result"));
				if (object.containsKey("year"))
					palmares.setDate(Integer.valueOf(object.get("year")));
				if (object.containsKey("photo"))
					palmares.setPhoto(object.get("photo"));
				if (object.containsKey("urlHtml"))
					palmares.setUrl(object.get("urlHtml"));

				palmaresArray.add(palmares);
			}

			Collections.sort(palmaresArray, new PalmaresComparator());
			return palmaresArray;

		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			Log.w("ParsePlistCompetition",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			return null;
		}

	}
}
