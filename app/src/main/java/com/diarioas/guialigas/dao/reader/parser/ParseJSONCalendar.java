package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.diarioas.guialigas.dao.model.calendar.Day;
import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.model.calendar.Grupo;
import com.diarioas.guialigas.dao.model.calendar.Match;

public class ParseJSONCalendar {
	private static final String ns = null;

	/**
	 * @param strFileContents
	 * @param numDay
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<Fase> parsePlistCalendar(String strFileContents,
			String dataPrefix) throws Exception {
		ArrayList<Fase> fases = new ArrayList<Fase>();
		try {
			JSONObject json = new JSONObject(strFileContents);
			JSONObject calendarioJSON = json.getJSONObject("calendario");
			JSONArray fasesJSON = calendarioJSON.getJSONArray("fases");

			if (fasesJSON.length() > 1) {
				fases = readCompetitionGroup(fasesJSON, dataPrefix);
			} else {
				fases = readCompetitionRegular(fasesJSON, dataPrefix);
			}
		} catch (JSONException e) {
			Log.e("CALENDAR",
					"Error al crear el JSON del calendario: " + e.getMessage());
		} catch (Exception e) {
			Log.e("CALENDAR",
					"Error al parsear el calendario: " + e.getMessage());
			// e.printStackTrace();
		}

		return fases;
	}

	/**
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	private ArrayList<Fase> readCompetitionGroup(JSONArray fasesJSON,
			String dataPrefix) throws JSONException {
		ArrayList<Fase> fases = new ArrayList<Fase>();
		Fase fase;
		JSONObject faseJSON;

		for (int i = 0; i < fasesJSON.length(); i++) {

			faseJSON = ((JSONObject) fasesJSON.get(i)).getJSONObject("fase");
			fase = readFase(
					((JSONObject) fasesJSON.get(i)).getJSONObject("fase"),
					dataPrefix);
			fases.add(fase);
		}
		return fases;
	}

	/**
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Fase> readCompetitionRegular(JSONArray fasesJSON,
			String dataPrefix) throws Exception {
		ArrayList<Fase> fases = new ArrayList<Fase>();
		JSONObject faseJSON = ((JSONObject) fasesJSON.get(0))
				.getJSONObject("fase");

		Fase fase = new Fase();
		fase.setName(faseJSON.getString("nombre"));
		try {
			fase.setActive(faseJSON.getBoolean("activa"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo activa de la fase"
							+ fase.getName());
			fase.setActive(false);
		}
		try {
			fase.setDefecto(faseJSON.getBoolean("defecto"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo defecto de la fase"
							+ fase.getName());
			fase.setDefecto(false);
		}

		JSONArray gruposJSON = faseJSON.getJSONArray("grupos");
		if (gruposJSON.length() == 1) {
			// Solo tiene una fase, y un grupo
			Grupo grupo = readGrupo((JSONObject) gruposJSON.get(0), dataPrefix);
			fase.addGrupo(grupo);
			fases.add(fase);
		} else {
			throw new Exception(
					"No existe una competicion con una fase y varios grupos");
			// Tiene una fase y varios grupos??? No es Posible
		}

		return fases;
	}

	/**
	 * @param object
	 * @return
	 * @throws JSONException
	 */
	private Fase readFase(JSONObject faseJSON, String dataPrefix)
			throws JSONException {
		Fase fase = new Fase();
		fase.setName(faseJSON.getString("nombre"));
		// Log.d("PARSEJSONCALENDARREAD", "Leyendo Fase: " + fase.getName());
		try {
			fase.setActive(faseJSON.getBoolean("activa"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo activa de la fase"
							+ fase.getName());
			fase.setActive(false);
		}
		try {
			fase.setDefecto(faseJSON.getBoolean("defecto"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo defecto de la fase"
							+ fase.getName());
			fase.setDefecto(false);
		}
		Grupo grupo;
		for (int i = 0; i < faseJSON.getJSONArray("grupos").length(); i++) {
			grupo = readGrupo(
					(JSONObject) faseJSON.getJSONArray("grupos").get(i),
					dataPrefix);
			fase.addGrupo(grupo);
		}
		return fase;
	}

	/**
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private Grupo readGrupo(JSONObject grupoJSON, String dataPrefix)
			throws JSONException {
		Grupo grupo = new Grupo();
		grupo.setName(grupoJSON.getString("nombre"));
		try {
			grupo.setDefecto(grupoJSON.getBoolean("defecto"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo defecto del grupo"
							+ grupo.getName());
			grupo.setDefecto(false);
		}

		grupo.setJornadas(readJornadas(grupoJSON.getJSONArray("jornadas"),
				dataPrefix));
		return grupo;
	}

	private ArrayList<Day> readJornadas(JSONArray jornadasJSON,
			String dataPrefix) throws JSONException {
		ArrayList<Day> days = new ArrayList<Day>();
		ArrayList<Match> matches;
		Day day;

		JSONObject jorn;

		for (int i = 0; i < jornadasJSON.length(); i++) {
			jorn = (JSONObject) jornadasJSON.get(i);
			day = new Day();
			try {
				day.setDate(jorn.getString("fecha"));
			} catch (Exception e) {
				Log.w("PARSEJSONCALENDAR",
						"No se ha podido leer el atributo date del grupo"
								+ day.getNumDay());
				day.setDate("");
			}
			try {
				day.setNumDay(Integer.valueOf(jorn.getString("nombre")));
			} catch (Exception e) {
				Log.w("PARSEJSONCALENDAR",
						"No se ha podido leer el atributo nombre del grupo"
								+ day.getNumDay());
				day.setNumDay(0);
			}

			try {
				day.setDefecto(jorn.getBoolean("defecto"));
			} catch (Exception e) {
				Log.w("PARSEJSONCALENDAR",
						"No se ha podido leer el atributo defecto del grupo"
								+ day.getNumDay());
				day.setDefecto(false);
			}

			matches = readMatches(jorn.getJSONArray("partidos"), dataPrefix);
			day.setMatches(matches);

			days.add(day);
		}
		return days;
	}

	private ArrayList<Match> readMatches(JSONArray partidosJSON,
			String dataPrefix) throws JSONException {

		ArrayList<Match> matches = new ArrayList<Match>();
		Match match;

		JSONObject part;
		JSONObject obj;
		JSONArray teles;
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
				if(!TextUtils.isEmpty(obj.getString("equipo_visitante"))){
					match.setMarkerAwayTeam(Integer.valueOf(obj.getString("equipo_visitante")));
				}

				if (part.has("enlace_directo")
						&& !part.isNull("enlace_directo")
						&& !part.getString("enlace_directo").equalsIgnoreCase(
								""))
					match.setLink(dataPrefix + part.getString("enlace_directo"));

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
						Log.e("CALENDAR", "Error al leer una TV: " + e);
					}
				}
				matches.add(match);
			} catch (Exception e) {
				Log.e("CALENDAR", "Error al leer un partido: " + e);
			}
		}
		return matches;
	}

	/**
	 * @param strFileContents
	 * @param dataPrefix
	 * @param numDay2
	 * @param numGroup
	 * @return
	 */
	public Fase parsePlistCalendarDay(String strFileContents,
			String dataPrefix, int numFase, int numDay) {
		Fase fase = null;
		try {
			JSONObject json = new JSONObject(strFileContents);
			JSONObject calendarioJSON = json.getJSONObject("calendario");
			JSONArray fasesJSON = calendarioJSON.getJSONArray("fases");

			if (fasesJSON.length() > 1) {
				fase = readCompetitionGroup(fasesJSON, dataPrefix, numFase,
						numDay);
			} else {
				fase = readCompetitionRegular(fasesJSON, dataPrefix, numFase,
						numDay);
			}
		} catch (JSONException e) {
			Log.e("CALENDAR",
					"Error al crear el JSON del calendarioDay: "
							+ e.getMessage());
		} catch (Exception e) {
			Log.e("CALENDAR",
					"Error al parsear el calendarioDay: " + e.getMessage());
			// e.printStackTrace();
		}

		return fase;
	}

	/**
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	private Fase readCompetitionGroup(JSONArray fasesJSON, String dataPrefix,
			int numFase, int numDay) throws JSONException {

		Fase fase = new Fase();

		JSONObject faseJSON = ((JSONObject) fasesJSON.get(numFase))
				.getJSONObject("fase");
		fase.setName(faseJSON.getString("nombre"));
		try {
			fase.setActive(faseJSON.getBoolean("activa"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo activa de la fase"
							+ fase.getName());
			fase.setActive(false);
		}
		try {
			fase.setDefecto(faseJSON.getBoolean("defecto"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo defecto de la fase"
							+ fase.getName());
			fase.setDefecto(false);
		}
		JSONArray gruposJSON = faseJSON.getJSONArray("grupos");
		Grupo grupo;
		for (int i = 0; i < gruposJSON.length(); i++) {
			grupo = readGrupo((JSONObject) gruposJSON.get(i), dataPrefix);
			fase.addGrupo(grupo);
		}

		return fase;
	}

	/**
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private Fase readCompetitionRegular(JSONArray fasesJSON, String dataPrefix,
			int numFase, int numDay) throws Exception {

		JSONObject faseJSON = ((JSONObject) fasesJSON.get(numFase))
				.getJSONObject("fase");

		Fase fase = new Fase();
		try {
			fase.setActive(faseJSON.getBoolean("activa"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo activa de la fase"
							+ fase.getName());
			fase.setActive(false);
		}
		try {
			fase.setDefecto(faseJSON.getBoolean("defecto"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo defecto de la fase"
							+ fase.getName());
			fase.setDefecto(false);
		}

		JSONArray gruposJSON = faseJSON.getJSONArray("grupos");

		JSONArray jornadasJSON = ((JSONObject) gruposJSON.get(0))
				.getJSONArray("jornadas");

		JSONObject jorn = (JSONObject) jornadasJSON.get(numDay);
		Day day = new Day();
		try {
			day.setDate(jorn.getString("fecha"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo date del grupo"
							+ day.getNumDay());
			day.setDate("");
		}
		try {
			if(!TextUtils.isEmpty(jorn.getString("nombre"))){
				day.setNumDay(Integer.valueOf(jorn.getString("nombre")));
			}
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo nombre del grupo"
							+ day.getNumDay());
			day.setNumDay(0);
		}

		try {
			day.setDefecto(jorn.getBoolean("defecto"));
		} catch (Exception e) {
			Log.w("PARSEJSONCALENDAR",
					"No se ha podido leer el atributo defecto del grupo"
							+ day.getNumDay());
			day.setDefecto(false);
		}

		ArrayList<Match> matches = readMatches(jorn.getJSONArray("partidos"),
				dataPrefix);
		day.setMatches(matches);

		Grupo grupo = new Grupo();
		grupo.addJornada(day);
		fase.addGrupo(grupo);

		return fase;
	}
}
