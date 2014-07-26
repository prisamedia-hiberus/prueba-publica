package com.diarioas.guiamundial.dao.reader.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import xmlwise.Plist;
import xmlwise.XmlParseException;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.stadium.Stadium;
import com.diarioas.guiamundial.utils.comparator.StadiumComparator;

public class ParsePlistStadiums {

	/**
	 * @param strFileContents
	 * @return
	 */
	public ArrayList<Stadium> parsePlistStadiums(String source) {

		try {
			HashMap<?, ?> hashMap = (HashMap<?, ?>) Plist.objectFromXml(source);

			ArrayList<Stadium> stadiums = new ArrayList<Stadium>();
			HashMap<String, HashMap<String, String>> stadiumsJson = (HashMap<String, HashMap<String, String>>) hashMap
					.get("stadiums");

			HashMap<String, String> object;
			String key;
			Stadium stadium;
			for (Iterator<String> iterator = stadiumsJson.keySet().iterator(); iterator
					.hasNext();) {
				stadium = new Stadium();
				key = iterator.next();
				object = stadiumsJson.get(key);
				stadium.setId(Integer.valueOf(key));
				if (object.containsKey("name"))
					stadium.setStadiumName(object.get("name"));
				if (object.containsKey("city"))
					stadium.setCityName(object.get("city"));
				if (object.containsKey("photo"))
					stadium.setPhoto(object.get("photo"));
				if (object.containsKey("stadiumInfo"))
					stadium.setUrlInfo(object.get("stadiumInfo"));
				// stadium.setOrder(Integer.valueOf(key));

				stadiums.add(stadium);
			}

			Collections.sort(stadiums, new StadiumComparator());
			return stadiums;

		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			Log.w("ParsePlistCompetition",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			return null;
		}

	}

	public Stadium parsePlistDetailStadium(Stadium stadium, String source,
			String imagePrefix) {

		try {
			HashMap<?, ?> hashMap = (HashMap<?, ?>) Plist.objectFromXml(source);

			stadium.setStadiumHistory((String) hashMap.get("history"));
			if (hashMap.containsKey("stadiumCharacteristics")) {
				HashMap<String, ?> stadiumCharacteristics = (HashMap<String, ?>) hashMap
						.get("stadiumCharacteristics");
				// Stadium Data
				HashMap<String, ?> stadiumData = (HashMap<String, ?>) stadiumCharacteristics
						.get("stadium");
				stadium.setStadiumName((String) stadiumData.get("name"));
				stadium.setStadiumMap((String) stadiumData.get("photo"));
				stadium.setStadiumCapacity(Integer
						.valueOf(((String) stadiumData.get("capacity"))
								.replace(".", "")));
				stadium.setStadiumYear(Integer.valueOf((String) stadiumData
						.get("yearBuilt")));

				for (String stad : (ArrayList<String>) stadiumData
						.get("stadiumArray")) {
					stadium.addStadiumPhoto(imagePrefix + stad);
				}

				// stadium.addStadiumPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385923094_album_grande.jpg");
				// stadium.addStadiumPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385923153_album_grande.jpg");
				// stadium.addStadiumPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385923206_album_grande.jpg");
				// stadium.addStadiumPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385923254_album_grande.jpg");
				// City Data
				HashMap<String, ?> cityData = (HashMap<String, ?>) stadiumCharacteristics
						.get("city");
				stadium.setCityName((String) cityData.get("name"));
				stadium.setCityState((String) cityData.get("state"));
				stadium.setCityPopulation((String) cityData.get("population"));
				stadium.setCityAltitude((String) cityData.get("altitude"));
				stadium.setCityHistory((String) cityData.get("history"));
				stadium.setCityTransport((String) cityData.get("transport"));
				stadium.setCityEconomy((String) cityData.get("economy"));
				stadium.setCityTourism((String) cityData.get("tourism"));

				for (String stad : (ArrayList<String>) cityData
						.get("cityArray")) {
					stadium.addCityPhoto(imagePrefix + stad);
				}
				// stadium.addCityPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385921108_album_grande.jpg");
				// stadium.addCityPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385921234_album_grande.jpg");
				// stadium.addCityPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385921338_album_grande.jpg");
				// stadium.addCityPhoto("http://futbol.as.com/futbol/imagenes/2013/11/30/album/1385839428_382684_1385921434_album_grande.jpg");
			}

			return stadium;
		} catch (XmlParseException e) {
			// TODO Auto-generated catch block
			Log.w("ParsePlistCompetition",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			return null;
		}

	}

}
