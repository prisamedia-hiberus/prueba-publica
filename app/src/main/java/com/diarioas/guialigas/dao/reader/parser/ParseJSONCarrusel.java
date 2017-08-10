package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.diarioas.guialigas.dao.model.calendar.Day;
import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.model.calendar.Grupo;
import com.diarioas.guialigas.dao.model.calendar.Match;
import com.diarioas.guialigas.dao.model.carrusel.GameSystem;
import com.diarioas.guialigas.dao.model.carrusel.Gol;
import com.diarioas.guialigas.dao.model.carrusel.ItemDirecto;
import com.diarioas.guialigas.dao.model.carrusel.PlayerOnField;
import com.diarioas.guialigas.dao.model.carrusel.Spade;
import com.diarioas.guialigas.dao.model.carrusel.SpadeInfo;
import com.diarioas.guialigas.dao.model.carrusel.StatsInfo;
import com.diarioas.guialigas.dao.model.carrusel.Tarjeta;
import com.diarioas.guialigas.utils.Defines.CarruselDetail;
import com.diarioas.guialigas.utils.comparator.DirectosComparator;
import com.diarioas.guialigas.utils.comparator.PlayerOnFieldComparator;

public class ParseJSONCarrusel {
	private static final String ns = null;

	/**
	 * @param strFileContents
	 * @return
	 * @throws JSONException
	 */
	public Fase parsePlistCarrusel(String strFileContents, String dataPrefix,
			String resultadosPrefix) throws Exception {
		Fase fase = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			fase = readCompetition(json, dataPrefix, resultadosPrefix);
		} catch (JSONException e) {
			Log.e("CARRUSEL",
					"Error al crear el JSON del carrusel: " + e.getMessage());
		} catch (Exception e) {
			Log.e("CARRUSEL", "Error al parsear el carrusel: " + e.getMessage());
			// e.printStackTrace();
		}

		return fase;
	}

	/**
	 * @param strFileContents
	 * @param dataPrefix
	 * @return
	 */
	public Match parsePlistDetailCarrusel(String strFileContents,
			String dataPrefix) throws Exception {
		Match match = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			match = readMatch(json, dataPrefix);

		} catch (JSONException e) {
			Log.e("CARRUSEL",
					"Error al crear el JSON del detalle del carrusel: "
							+ e.getMessage());
		} catch (Exception e) {
			Log.e("CARRUSEL", "Error al parsear el JSON detalle del carrusel: "
					+ e.getMessage());
			// e.printStackTrace();
		}
		return match;
	}

	/**
	 * @param dataPrefix
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	private Fase readCompetition(JSONObject json, String dataPrefix,
			String resultadosPrefix) throws JSONException {

		Fase fase = new Fase();
		JSONObject faseJSON = json.getJSONObject("fase");
		fase.setName(faseJSON.getString("nombre"));
		fase.setIdFase(json.getString("jornada"));
		JSONObject carruselJSON = json.getJSONObject("carrusel");
		JSONObject grupoJSON;
		Grupo grupo;

		for (Iterator<String> iterator = carruselJSON.keys(); iterator
				.hasNext();) {
			grupoJSON = carruselJSON.getJSONObject(iterator.next());
			grupo = new Grupo();
			grupo.setName(grupoJSON.getString("nombre"));
			grupo.setJornadas(readJornadas(grupoJSON, dataPrefix,
					resultadosPrefix));
			fase.addGrupo(grupo);
		}

		fase.setDateUpdated(new Date());
		return fase;
	}

	private ArrayList<Day> readJornadas(JSONObject jornadasJSON,
			String dataPrefix, String resultadosPrefix) throws JSONException {
		ArrayList<Day> days = new ArrayList<Day>();
		ArrayList<Match> matches;
		Day day = new Day();

		matches = readMatches(jornadasJSON.getJSONArray("partidos"),
				dataPrefix, resultadosPrefix);
		day.setMatches(matches);
		days.add(day);

		return days;
	}

	private ArrayList<Match> readMatches(JSONArray partidosJSON,
			String dataPrefix, String resultadosPrefix) throws JSONException {

		ArrayList<Match> matches = new ArrayList<Match>();
		Match match;

		JSONObject part;
		JSONObject obj;
		JSONArray teles, goles, tarjetas;
		for (int j = 0; j < partidosJSON.length(); j++) {
			try {

				part = partidosJSON.getJSONObject(j);
				match = new Match();
				match.setState(part.getString("estado_class"));
				if(!TextUtils.isEmpty(part.getString("estado"))){
					match.setStateCode(Integer.valueOf(part.getString("estado")));
				}
				match.setDate(String.valueOf(part.get("timestamp")));

				obj = part.getJSONObject("equipo_local");
				match.setLocalId(obj.getString("id"));
				match.setLocalTeamName(obj.getString("nombre"));
				match.setLocalTeamShieldName(obj.getString("escudo"));

				obj = part.getJSONObject("equipo_visitante");
				match.setAwayId(obj.getString("id"));
				match.setAwayTeamName(obj.getString("nombre"));
				match.setAwayTeamShieldName(obj.getString("escudo"));

				obj = part.getJSONObject("resultado");
				if(!TextUtils.isEmpty(obj.getString("equipo_local"))){
					match.setMarkerLocalTeam(Integer.valueOf(obj.getString("equipo_local")));
				}
				if (!TextUtils.isEmpty(obj.getString("equipo_visitante"))){
					match.setMarkerAwayTeam(Integer.valueOf(obj.getString("equipo_visitante")));
				}

				match.setPlace(part.getString("estadio"));
				if(part.has("arbitro")){
					match.setReferee(part.getString("arbitro"));
				}

				if (part.has("enlace_directo")
						&& !part.isNull("enlace_directo")
						&& !part.getString("enlace_directo").equalsIgnoreCase(
								""))
					match.setLink(resultadosPrefix
							+ part.getString("enlace_directo"));

				if (part.has("datos_directo")
						&& !part.isNull("datos_directo")
						&& !part.getString("datos_directo")
								.equalsIgnoreCase(""))
					match.setDataLink(dataPrefix
							+ part.getString("datos_directo"));

				teles = part.getJSONArray("televisiones");
				for (int i = 0; i < teles.length(); i++) {
					try {
						obj = (JSONObject) teles.get(i);
						match.addTelevision(obj.getString("id"));
					} catch (Exception e) {
						Log.e("CARRUSEL", "Error al leer una TV: " + e);
					}
				}
				goles = part.getJSONArray("goles");
				Gol gol;
				for (int i = 0; i < goles.length(); i++) {
					try {
						obj = (JSONObject) goles.get(i);
						gol = new Gol();
						gol.setScoreBoard(obj.getString("marcador"));
						if(TextUtils.isEmpty(obj.getString("minuto"))){
							gol.setMin(Integer.valueOf(obj.getString("minuto")));
						}
						if(TextUtils.isEmpty(obj.getString("parte"))){
							gol.setPart(Integer.valueOf(obj.getString("parte")));
						}
						gol.setPlayer(obj.getString("jugador"));
						gol.setUrlPlayer(obj.getString("enlace_jugador"));
						match.addGol(gol);
					} catch (Exception e) {
						Log.e("CARRUSEL", "Error al leer un gol: " + e);
					}
				}

				tarjetas = part.getJSONArray("tarjetas");
				Tarjeta tarjeta;
				for (int i = 0; i < tarjetas.length(); i++) {
					try {
						obj = (JSONObject) tarjetas.get(i);
						tarjeta = new Tarjeta();
						if(TextUtils.isEmpty(obj.getString("minuto"))){
							tarjeta.setMin(Integer.valueOf(obj.getString("minuto")));
						}
						if(TextUtils.isEmpty(obj.getString("parte"))){
							tarjeta.setPart(Integer.valueOf(obj.getString("parte")));
						}
						tarjeta.setPlayer(obj.getString("jugador"));
						tarjeta.setUrlPlayer(obj.getString("enlace_jugador"));
						match.addTarjetaRoja(tarjeta);
					} catch (Exception e) {
						Log.e("CARRUSEL", "Error al leer un gol: " + e);
					}
				}
				matches.add(match);
			} catch (Exception e) {
				Log.e("CARRUSEL", "Error al leer un partido: " + e);
			}
		}
		return matches;
	}

	/**
	 * @param json
	 * @param dataPrefix
	 * @return
	 * @throws JSONException
	 */
	private Match readMatch(JSONObject json, String dataPrefix)
			throws JSONException {

		JSONObject obj;

		Match match = new Match();
		if (json.has("info_partido") && !json.isNull("info_partido")) {
			obj = json.getJSONObject("info_partido");
			if (obj.has("estado") && !obj.isNull("estado"))
				match.setStateCode(obj.getInt("estado"));
			if (obj.has("minuto") && !obj.isNull("minuto"))
				match.setMinute(obj.getInt("minuto"));
		}

		if (json.has("localizacion") && !json.isNull("localizacion")) {
			obj = json.getJSONObject("localizacion");
			if (obj.has("estadio") && !obj.isNull("estadio"))
				match.setPlace(obj.getString("estadio"));
			if (obj.has("fecha") && !obj.isNull("fecha"))
				match.setDate(String.valueOf(obj.get("fecha")));
		}

		if (json.has("arbitro") && !json.isNull("arbitro"))
			match.setReferee(json.getString("arbitro"));

		if (json.has("local") && !json.isNull("local")) {
			obj = json.getJSONObject("local");
			if (obj.has("id") && !obj.isNull("id"))
				match.setLocalId(obj.getString("id"));
			if (obj.has("nombre") && !obj.isNull("nombre"))
				match.setLocalTeamName(obj.getString("nombre"));
			if (obj.has("puntuacion") && !obj.isNull("puntuacion"))
				obj = obj.getJSONObject("puntuacion");
			if (obj.has("goles") && !obj.isNull("goles"))
				match.setMarkerLocalTeam(obj.getInt("goles"));
		}

		if (json.has("visitante") && !json.isNull("visitante")) {
			obj = json.getJSONObject("visitante");
			if (obj.has("id") && !obj.isNull("id"))
				match.setAwayId(obj.getString("id"));
			if (obj.has("nombre") && !obj.isNull("nombre"))
				match.setAwayTeamName(obj.getString("nombre"));
			if (obj.has("puntuacion") && !obj.isNull("puntuacion"))
				obj = obj.getJSONObject("puntuacion");
			if (obj.has("goles") && !obj.isNull("goles"))
				match.setMarkerAwayTeam(obj.getInt("goles"));
		}

		// match.setLink(dataPrefix + part.getString("enlace_directo"));
		// match.setDataLink(dataPrefix + part.getString("datos_directo"));

		if (json.has("tvs") && !json.isNull("tvs")) {
			try {
				JSONObject teles = json.getJSONObject("tvs");
				String key;
				for (Iterator<String> iterator = teles.keys(); iterator
						.hasNext();) {
					try {
						key = iterator.next();
						obj = (JSONObject) teles.get(key);
						if (obj.has("__attributes")
								&& !obj.isNull("__attributes")) {
							obj = obj.getJSONObject("__attributes");
							if (obj.has("id") && !obj.isNull("id"))
								match.addTelevision(obj.getString("id"));
						}
					} catch (Exception e) {
						Log.e("CARRUSEL", "Error al leer una TV: " + e);
					}
				}
			} catch (Exception e) {
				Log.e("CARRUSEL", "Error al leer las TVs: " + e.getMessage());
			}
		}
		if (json.has("resumen_goles") && !json.isNull("resumen_goles")) {
			try {
				JSONArray goles = json.getJSONArray("resumen_goles");
				Gol gol;
				for (int i = 0; i < goles.length(); i++) {
					try {
						obj = (JSONObject) goles.get(i);
						gol = new Gol();
						if (obj.has("goles_local")
								&& !obj.isNull("goles_local")
								&& obj.has("goles_visitante")
								&& !obj.isNull("goles_visitante"))
							gol.setScoreBoard("(" + obj.getInt("goles_local")
									+ " - " + obj.getInt("goles_visitante")
									+ ")");
						if (obj.has("min") && !obj.isNull("min"))
							gol.setMin(obj.getInt("min"));
						if (obj.has("jug") && !obj.isNull("jug"))
							gol.setPlayer(obj.getString("jug"));
						if (obj.has("jug_enlace_ficha")
								&& !obj.isNull("jug_enlace_ficha"))
							gol.setUrlPlayer(obj.getString("jug_enlace_ficha"));
						if (obj.has("marca") && !obj.isNull("marca"))
							gol.setTeamSide(obj.getString("marca"));
						match.addGol(gol);
					} catch (Exception e) {
						Log.e("CARRUSEL",
								"Error al leer un gol: " + e.getMessage());
					}
				}
			} catch (Exception e) {
				Log.e("CARRUSEL", "Error al leer los goles: " + e.getMessage());
			}
		}
		JSONArray tarjetas;
		Tarjeta tarjeta;
		if (json.has("resumen_tarjetas_rojas")
				&& !json.isNull("resumen_tarjetas_rojas")) {
			try {
				tarjetas = json.getJSONArray("resumen_tarjetas_rojas");
				for (int i = 0; i < tarjetas.length(); i++) {
					try {
						obj = (JSONObject) tarjetas.get(i);
						tarjeta = new Tarjeta();
						if (obj.has("min") && !obj.isNull("min"))
							tarjeta.setMin(obj.getInt("min"));
						if (obj.has("jug") && !obj.isNull("jug"))
							tarjeta.setPlayer(obj.getString("jug"));
						if (obj.has("jug_enlace_ficha")
								&& !obj.isNull("jug_enlace_ficha"))
							tarjeta.setUrlPlayer(obj
									.getString("jug_enlace_ficha"));
						if (obj.has("marca") && !obj.isNull("marca"))
							tarjeta.setTeamSide(obj.getString("marca"));
						match.addTarjetaRoja(tarjeta);
					} catch (Exception e) {
						Log.e("CARRUSEL", "Error al leer una tarjeta Roja: "
								+ e.getMessage());
					}
				}
			} catch (Exception e) {
				Log.e("CARRUSEL",
						"Error al leer las tarjetas Rojas: " + e.getMessage());
			}
		}
		if (json.has("resumen_tarjetas_amarillas")
				&& !json.isNull("resumen_tarjetas_amarillas")) {
			try {
				tarjetas = json.getJSONArray("resumen_tarjetas_amarillas");
				for (int i = 0; i < tarjetas.length(); i++) {
					try {
						obj = (JSONObject) tarjetas.get(i);
						tarjeta = new Tarjeta();
						if (obj.has("min") && !obj.isNull("min"))
							tarjeta.setMin(obj.getInt("min"));
						if (obj.has("jug") && !obj.isNull("jug"))
							tarjeta.setPlayer(obj.getString("jug"));
						if (obj.has("jug_enlace_ficha")
								&& !obj.isNull("jug_enlace_ficha"))
							tarjeta.setUrlPlayer(obj
									.getString("jug_enlace_ficha"));
						if (obj.has("marca") && !obj.isNull("marca"))
							tarjeta.setTeamSide(obj.getString("marca"));
						match.addTarjetaAmarilla(tarjeta);
					} catch (Exception e) {
						Log.e("CARRUSEL",
								"Error al leer una tarjeta Amarilla: "
										+ e.getMessage());
					}
				}
			} catch (Exception e) {
				Log.e("CARRUSEL",
						"Error al leer las tarjetas Amarillas: "
								+ e.getMessage());
			}
		}

		if (json.has("noticias") && !json.isNull("noticias")) {
			String noticias = json.getString("noticias");
			if (noticias.startsWith("[")) {
				noticias = noticias.substring(1, noticias.length());
			}
			if (noticias.endsWith("]")) {
				noticias = noticias.substring(0, noticias.length() - 1);
			}

			if (!noticias.equalsIgnoreCase("")) {
				match.setCronica(json.getString("noticias"));
			}
		}

		if (json.has("referencias") && !json.isNull("referencias")) {
			obj = json.getJSONObject("referencias");
			if (obj.has("plantilla") && !obj.isNull("plantilla"))
				match.addReferencia(CarruselDetail.CARRUSEL_PLANTILLA,
						dataPrefix + obj.getString("plantilla"));

			if (obj.has("estadisticas") && !obj.isNull("estadisticas"))
				match.addReferencia(CarruselDetail.CARRUSEL_ESTADISTICAS,
						dataPrefix + obj.getString("estadisticas"));

			if (obj.has("retransmision") && !obj.isNull("retransmision"))
				match.addReferencia(CarruselDetail.CARRUSEL_DIRECTO, dataPrefix
						+ obj.getString("retransmision"));

			if (obj.has("picas") && !obj.isNull("picas"))
				match.addReferencia(CarruselDetail.CARRUSEL_PICAS, dataPrefix
						+ obj.getString("picas"));
		}
		return match;
	}

	/**
	 * @param strFileContents
	 * @return
	 */
	public SpadeInfo parsePlistSpades(String strFileContents,
			String imagePrefix, String dataPrefix) {
		SpadeInfo spadeInfo = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			spadeInfo = readSpades(json, imagePrefix, dataPrefix);
		} catch (JSONException e) {
			Log.e("SPADES",
					"Error al crear el JSON de spades: " + e.getMessage());
		} catch (Exception e) {
			Log.e("SPADES",
					"Error al parsear el JSON de spades: " + e.getMessage());
			// e.printStackTrace();
		}
		return spadeInfo;
	}

	/**
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	private SpadeInfo readSpades(JSONObject json, String imagePrefix,
			String dataPrefix) throws JSONException {

		SpadeInfo info = new SpadeInfo();
		JSONObject localJSON = json.getJSONObject("local");
		info.setLocal(getSpadesNameFrom(localJSON));
		info.setLocalSpades(getSpadesFrom(localJSON, imagePrefix, dataPrefix));

		JSONObject awayJSON = json.getJSONObject("visitante");
		info.setAway(getSpadesNameFrom(awayJSON));
		info.setAwaySpades(getSpadesFrom(awayJSON, imagePrefix, dataPrefix));

		return info;
	}

	/**
	 * @param localJSON
	 * @return
	 * @throws JSONException
	 */
	private String getSpadesNameFrom(JSONObject json) throws JSONException {
		JSONObject nomJSON = json.getJSONObject("nom");
		return nomJSON.getString("__value");
	}

	private HashMap<Integer, Spade> getSpadesFrom(JSONObject json,
			String imagePrefix, String dataPrefix) throws JSONException {
		HashMap<Integer, Spade> spades = new HashMap<Integer, Spade>();

		JSONObject entsJSON = json.getJSONObject("ents");
		JSONObject __value = entsJSON.getJSONObject("__value");

		JSONObject entJSON, __attributes, __valueEnt, nom, val, dorsal, foto, jsonValue;
		Spade spade;
		String key;
		for (Iterator<String> iterator = __value.keys(); iterator.hasNext();) {
			key = iterator.next();
			entJSON = __value.getJSONObject(key);

			spade = new Spade();

			__attributes = entJSON.getJSONObject("__attributes");
			if (__attributes.has("id") && !__attributes.isNull("id"))
				spade.setPlayerId(__attributes.getInt("id"));

			if (__attributes.has("id_prov") && !__attributes.isNull("id_prov"))
				spade.setPlayerProvId(__attributes.getInt("id_prov"));

			__valueEnt = entJSON.getJSONObject("__value");
			if (__valueEnt.has("nom") && !__valueEnt.isNull("nom")) {
				nom = __valueEnt.getJSONObject("nom");
				if (nom.has("__value") && !nom.isNull("__value"))
					spade.setPlayer(nom.getString("__value"));
			}

			if (__valueEnt.has("val") && !__valueEnt.isNull("val")) {
				val = __valueEnt.getJSONObject("val");
				if (val.has("__value") && !val.isNull("__value"))
					spade.setPunt(val.getInt("__value"));
			}

			if (__valueEnt.has("dorsal") && !__valueEnt.isNull("dorsal")) {
				dorsal = __valueEnt.getJSONObject("dorsal");
				if (dorsal.has("__value") && !dorsal.isNull("__value"))
					spade.setDorsal(dorsal.getInt("__value"));
			}

			if (__valueEnt.has("foto") && !__valueEnt.isNull("foto")) {
				foto = __valueEnt.getJSONObject("foto");
				if (foto.has("__value") && !foto.isNull("__value"))
					spade.setUrlPhoto(imagePrefix + foto.getString("__value"));
			}

			if (__valueEnt.has("json") && !__valueEnt.isNull("json")) {
				jsonValue = __valueEnt.getJSONObject("json");
				if (jsonValue.has("__value") && !jsonValue.isNull("__value"))
					spade.setUrl(dataPrefix + jsonValue.getString("__value"));
			}

			spades.put(Integer.valueOf(key.substring(4)), spade);
		}
		return spades;
	}

	/**
	 * @param strFileContents
	 * @return
	 */
	public ArrayList<StatsInfo> parsePlistStats(String strFileContents) {
		ArrayList<StatsInfo> stats = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			stats = readStats(json);
		} catch (JSONException e) {
			Log.e("SPADES",
					"Error al crear el JSON de stats: " + e.getMessage());
		} catch (Exception e) {
			Log.e("SPADES",
					"Error al parsear el JSON de stats: " + e.getMessage());
			// e.printStackTrace();
		}
		return stats;
	}

	/**
	 * @param json
	 * @return
	 * @return
	 * @throws JSONException
	 */
	private ArrayList<StatsInfo> readStats(JSONObject json)
			throws JSONException {

		ArrayList<StatsInfo> stats = new ArrayList<StatsInfo>();

		if (json.has("pos_per") && !json.isNull("pos_per"))
			stats.add(readStatsInfo(json.getJSONObject("pos_per")));

		if (json.has("rem") && !json.isNull("rem"))
			stats.add(readStatsInfo(json.getJSONObject("rem")));

		if (json.has("rem_fue") && !json.isNull("rem_fue"))
			stats.add(readStatsInfo(json.getJSONObject("rem_fue")));

		if (json.has("rem_post") && !json.isNull("rem_post"))
			stats.add(readStatsInfo(json.getJSONObject("rem_post")));

		if (json.has("rem_port") && !json.isNull("rem_port"))
			stats.add(readStatsInfo(json.getJSONObject("rem_port")));

		if (json.has("rem_otr") && !json.isNull("rem_otr"))
			stats.add(readStatsInfo(json.getJSONObject("rem_otr")));

		if (json.has("asis") && !json.isNull("asis"))
			stats.add(readStatsInfo(json.getJSONObject("asis")));

		if (json.has("para") && !json.isNull("para"))
			stats.add(readStatsInfo(json.getJSONObject("para")));

		if (json.has("gol") && !json.isNull("gol"))
			stats.add(readStatsInfo(json.getJSONObject("gol")));

		if (json.has("otros") && !json.isNull("otros")) {
			JSONObject otrosJSON = json.getJSONObject("otros");
			if (otrosJSON.has("tar_ama") && !otrosJSON.isNull("tar_ama"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("tar_ama")));

			if (otrosJSON.has("tar_roj") && !otrosJSON.isNull("tar_roj"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("tar_roj")));

			if (otrosJSON.has("falt_rec") && !otrosJSON.isNull("falt_rec"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("falt_rec")));

			if (otrosJSON.has("falt") && !otrosJSON.isNull("falt"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("falt")));

			if (otrosJSON.has("bal_per") && !otrosJSON.isNull("bal_per"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("bal_per")));

			if (otrosJSON.has("rec") && !otrosJSON.isNull("rec"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("rec")));

			if (otrosJSON.has("fue_jue") && !otrosJSON.isNull("fue_jue"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("fue_jue")));

			if (otrosJSON.has("penal") && !otrosJSON.isNull("penal"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("penal")));

			if (otrosJSON.has("int_por") && !otrosJSON.isNull("int_por"))
				stats.add(readStatsInfo(otrosJSON.getJSONObject("int_por")));
		}

		return stats;
	}

	private StatsInfo readStatsInfo(JSONObject posPerJSON) throws JSONException {
		StatsInfo info = new StatsInfo();
		info.setTypo(posPerJSON.getString("tipo"));
		info.setLocal(posPerJSON.getInt("local"));
		info.setAway(posPerJSON.getInt("visitante"));
		return info;
	}

	/**
	 * @param strFileContents
	 * @param imagePrefix
	 */
	public ArrayList<ItemDirecto> parsePlistDirecto(String strFileContents,
			String imagePrefix) {
		ArrayList<ItemDirecto> directos = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			directos = readDirectos(json);
			Collections.sort(directos, new DirectosComparator());
		} catch (JSONException e) {
			Log.e("DIRECTO",
					"Error al crear el JSON de directos: " + e.getMessage());
		} catch (Exception e) {
			Log.e("DIRECTO",
					"Error al parsear el JSON de directos: " + e.getMessage());
			// e.printStackTrace();
		}
		return directos;

	}

	/**
	 * @param json
	 * @return
	 */
	private ArrayList<ItemDirecto> readDirectos(JSONObject json)
			throws JSONException {
		ArrayList<ItemDirecto> items = new ArrayList<ItemDirecto>(json.length());

		String key;
		JSONObject itemJSON;
		ItemDirecto item;
		String[] split;
		for (Iterator<String> iterator = json.keys(); iterator.hasNext();) {
			key = iterator.next();
			itemJSON = json.getJSONObject(key);

			item = new ItemDirecto();
			split = key.split("_");

			item.setNumComent(Integer.valueOf(split[split.length - 1]));
			item.setId(key);
			if (itemJSON.has("tipo") && !itemJSON.isNull("tipo"))
				item.setTipo(itemJSON.getString("tipo"));

			if (itemJSON.has("min") && !itemJSON.isNull("min"))
				item.setMin(itemJSON.getInt("min"));

			if (itemJSON.has("texto") && !itemJSON.isNull("texto"))
				item.setTexto(Html.fromHtml(itemJSON.getString("texto"))
						.toString());

			if (itemJSON.has("class") && !itemJSON.isNull("class"))
				item.set_class(itemJSON.getString("class"));

			if (itemJSON.has("icono") && !itemJSON.isNull("icono"))
				item.setIcon(itemJSON.getString("icono"));

			if (itemJSON.has("jugador") && !itemJSON.isNull("jugador"))
				item.setPlayerName(itemJSON.getString("jugador"));

			if (itemJSON.has("jugador_id") && !itemJSON.isNull("jugador_id"))
				item.setIdPlayer(itemJSON.getInt("jugador_id"));

			if (itemJSON.has("jugador_nom_norm")
					&& !itemJSON.isNull("jugador_nom_norm"))
				item.setPlayerNorm(itemJSON.getString("jugador_nom_norm"));

			if (itemJSON.has("jugador_enlace_ficha")
					&& !itemJSON.isNull("jugador_enlace_ficha"))
				item.setUrlPlayer(itemJSON.getString("jugador_enlace_ficha"));

			if (itemJSON.has("goles_local") && !itemJSON.isNull("goles_local"))
				item.setGolLocal(itemJSON.getInt("goles_local"));

			if (itemJSON.has("goles_visitante")
					&& !itemJSON.isNull("goles_visitante"))
				item.setGolAway(itemJSON.getInt("goles_visitante"));

			if (itemJSON.has("marca") && !itemJSON.isNull("marca"))
				item.setMarca(itemJSON.getString("marca"));

			items.add(item);
		}
		return items;
	}

	/**
	 * @param strFileContents
	 * @param imagePrefix
	 * @param imageData
	 * @return
	 */
	public GameSystem parsePlistGameSystem(String strFileContents,
			String imagePrefix, String imageData) {
		GameSystem gs = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			gs = readGameSystem(json, imagePrefix, imageData);

		} catch (JSONException e) {
			Log.e("PLANTILLA", "Error al crear el JSON del sistema de juego: "
					+ e.getMessage());
		} catch (Exception e) {
			Log.e("PLANTILLA",
					"Error al parsear el JSON del sistema de juego: "
							+ e.getMessage());
			// e.printStackTrace();
		}

		return gs;
	}

	/**
	 * @param json
	 * @param imagePrefix
	 * @param imageData
	 * @return
	 */
	private GameSystem readGameSystem(JSONObject json, String imagePrefix,
			String imageData) throws JSONException {

		JSONObject objectJSON;

		GameSystem gSystem = new GameSystem();
		if (json.has("local") && !json.isNull("local")) {
			objectJSON = json.getJSONObject("local");
			if (objectJSON.has("id") && !objectJSON.isNull("id"))
				gSystem.setLocalTeamId(objectJSON.getInt("id"));
			if (objectJSON.has("nombre") && !objectJSON.isNull("nombre"))
				gSystem.setLocalTeamName(objectJSON.getString("nombre"));
			if (objectJSON.has("sistema") && !objectJSON.isNull("sistema"))
				gSystem.setLocalTeamGSId(objectJSON.getInt("sistema"));
			if (objectJSON.has("entrenador")
					&& !objectJSON.isNull("entrenador"))
				gSystem.setLocalTeamMisterName(objectJSON
						.getString("entrenador"));
		}

		if (json.has("visitante") && !json.isNull("visitante")) {
			objectJSON = json.getJSONObject("visitante");
			if (objectJSON.has("id") && !objectJSON.isNull("id"))
				gSystem.setAwayTeamId(objectJSON.getInt("id"));
			if (objectJSON.has("nombre") && !objectJSON.isNull("nombre"))
				gSystem.setAwayTeamName(objectJSON.getString("nombre"));
			if (objectJSON.has("sistema") && !objectJSON.isNull("sistema"))
				gSystem.setAwayTeamGSId(objectJSON.getInt("sistema"));
			if (objectJSON.has("entrenador")
					&& !objectJSON.isNull("entrenador"))
				gSystem.setAwayTeamMisterName(objectJSON
						.getString("entrenador"));
		}

		if (json.has("jugadores") && !json.isNull("jugadores")) {
			objectJSON = json.getJSONObject("jugadores");
			JSONObject playersJSON;
			JSONArray titularesJSON, suplentesJSON;

			if (objectJSON.has("local") && !objectJSON.isNull("local")) {
				playersJSON = objectJSON.getJSONObject("local");
				if (playersJSON.has("titulares")
						&& !playersJSON.isNull("titulares")) {
					titularesJSON = playersJSON.getJSONArray("titulares");
					gSystem.setLocalTitulares(readPlayers(titularesJSON,
							imagePrefix, imageData));
				}
				if (playersJSON.has("suplentes")
						&& !playersJSON.isNull("suplentes")) {
					suplentesJSON = playersJSON.getJSONArray("suplentes");
					gSystem.setLocalSuplentes(readPlayers(suplentesJSON,
							imagePrefix, imageData));
				}
			}
			if (objectJSON.has("visitante") && !objectJSON.isNull("visitante")) {
				playersJSON = objectJSON.getJSONObject("visitante");
				if (playersJSON.has("titulares")
						&& !playersJSON.isNull("titulares")) {
					titularesJSON = playersJSON.getJSONArray("titulares");
					gSystem.setAwayTitulares(readPlayers(titularesJSON,
							imagePrefix, imageData));
				}
				if (playersJSON.has("suplentes")
						&& !playersJSON.isNull("suplentes")) {
					suplentesJSON = playersJSON.getJSONArray("suplentes");
					gSystem.setAwaySuplentes(readPlayers(suplentesJSON,
							imagePrefix, imageData));
				}
			}
		}

		if (json.has("eventos_jugadores") && !json.isNull("eventos_jugadores")) {
			try {

				objectJSON = json.getJSONObject("eventos_jugadores");
				objectJSON = objectJSON.getJSONObject("jugador");
				objectJSON = objectJSON.getJSONObject("das");

				String key;
				JSONObject idJSON, jsonEvento;
				JSONArray eventosJSONARRAY;
				for (Iterator iterator = objectJSON.keys(); iterator.hasNext();) {
					key = (String) iterator.next();
					idJSON = objectJSON.getJSONObject(key);
					if (idJSON.has("cambio") && !idJSON.isNull("cambio")) {
						eventosJSONARRAY = idJSON.getJSONArray("cambio");
						for (int i = 0; i < eventosJSONARRAY.length(); i++) {
							jsonEvento = (JSONObject) eventosJSONARRAY.get(i);
							gSystem.addCambio(Integer.valueOf(key),
									jsonEvento.getString("time"));

						}

					}

					if (idJSON.has("goles") && !idJSON.isNull("goles")) {
						eventosJSONARRAY = idJSON.getJSONArray("goles");
						for (int i = 0; i < eventosJSONARRAY.length(); i++) {
							jsonEvento = (JSONObject) eventosJSONARRAY.get(i);
							gSystem.addGol(Integer.valueOf(key),
									jsonEvento.getString("time"));

						}
					}

					if (idJSON.has("tarjetas_1")
							&& !idJSON.isNull("tarjetas_1")) {
						eventosJSONARRAY = idJSON.getJSONArray("tarjetas_1");
						for (int i = 0; i < eventosJSONARRAY.length(); i++) {
							jsonEvento = (JSONObject) eventosJSONARRAY.get(i);
							gSystem.addTarjetaAmarilla(Integer.valueOf(key),
									jsonEvento.getString("time"));

						}
					}

					if (idJSON.has("tarjetas_2")
							&& !idJSON.isNull("tarjetas_2")) {
						eventosJSONARRAY = idJSON.getJSONArray("tarjetas_2");
						for (int i = 0; i < eventosJSONARRAY.length(); i++) {
							jsonEvento = (JSONObject) eventosJSONARRAY.get(i);
							gSystem.addTarjetaRoja(Integer.valueOf(key),
									jsonEvento.getString("time"));

						}
					}
				}
			} catch (Exception e) {
				Log.e("GameSystem",
						"Error al parsear el JSON de GameSystem|eventos_jugadores: "
								+ e.getMessage());
			}
		}

		return gSystem;

	}

	/**
	 * @param imagePrefix
	 * @param imageData
	 * @param titularesJSON
	 * @throws JSONException
	 */
	private ArrayList<PlayerOnField> readPlayers(JSONArray playersJSONARRAY,
			String imagePrefix, String imageData) throws JSONException {
		PlayerOnField player;
		ArrayList<PlayerOnField> players = new ArrayList<PlayerOnField>();
		JSONObject playerJSON, __attributesJSON, __valueJSON, objectJSON;
		for (int i = 0; i < playersJSONARRAY.length(); i++) {
			playerJSON = (JSONObject) playersJSONARRAY.get(i);

			player = new PlayerOnField();

			__attributesJSON = playerJSON.getJSONObject("__attributes");
			if (__attributesJSON.has("id") && !__attributesJSON.isNull("id"))
				player.setId(__attributesJSON.getInt("id"));

			__valueJSON = playerJSON.getJSONObject("__value");
			if (__valueJSON.has("nom") && !__valueJSON.isNull("nom")) {
				objectJSON = __valueJSON.getJSONObject("nom");
				player.setName(objectJSON.getString("__value"));
			}
			// if (__valueJSON.has("enlace_ficha")
			// && !__valueJSON.isNull("enlace_ficha")) {
			// objectJSON = __valueJSON.getJSONObject("enlace_ficha");
			// player.setUrl(objectJSON.getString("__value"));
			// }
			if (__valueJSON.has("json") && !__valueJSON.isNull("json")) {
				objectJSON = __valueJSON.getJSONObject("json");
				player.setUrl(imageData + objectJSON.getString("__value"));
			}
			if (__valueJSON.has("foto") && !__valueJSON.isNull("foto")) {
				objectJSON = __valueJSON.getJSONObject("foto");
				player.setUrlPhoto(imagePrefix
						+ objectJSON.getString("__value"));
			}
			if (__valueJSON.has("attrs") && !__valueJSON.isNull("attrs")) {
				objectJSON = __valueJSON.getJSONObject("attrs");
				objectJSON = objectJSON.getJSONObject("__value");
				String tipo;
				JSONObject valJSON, atrJSON;
				for (Iterator iterator = objectJSON.keys(); iterator.hasNext();) {
					valJSON = objectJSON
							.getJSONObject((String) iterator.next());

					atrJSON = valJSON.getJSONObject("__attributes");
					tipo = atrJSON.getString("tipo");
					if (tipo.equalsIgnoreCase("dor")) {
						player.setDorsal(Integer.valueOf(valJSON
								.getString("__value")));
					} else if (tipo.equalsIgnoreCase("pue")) {
						player.setPuesto(valJSON.getString("__value"));
					} else if (tipo.equalsIgnoreCase("pos")) {
						player.setPosition(Integer.valueOf(valJSON
								.getString("__value")));
					} else if (tipo.equalsIgnoreCase("dem")) {
						player.setDem(valJSON.getString("__value"));
					}

				}

			}

			players.add(player);
		}

		// Se ordenan por su posicion
		Collections.sort(players, new PlayerOnFieldComparator());
		return players;
	}

}
