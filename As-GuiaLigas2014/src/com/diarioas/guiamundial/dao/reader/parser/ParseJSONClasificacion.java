package com.diarioas.guiamundial.dao.reader.parser;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.diarioas.guiamundial.dao.model.calendar.Fase;
import com.diarioas.guiamundial.dao.model.calendar.Grupo;
import com.diarioas.guiamundial.dao.model.clasificacion.Clasificacion;
import com.diarioas.guiamundial.dao.model.clasificacion.ClasificacionInfo;
import com.diarioas.guiamundial.dao.model.clasificacion.LeyendaInfo;
import com.diarioas.guiamundial.dao.model.team.Team;

public class ParseJSONClasificacion {
	private static final String ns = null;

	/**
	 * @param strFileContents
	 * @return
	 * @throws JSONException
	 */
	public Fase parsePlistClasificacion(String strFileContents)
			throws Exception {
		Fase fase = null;
		try {
			JSONObject json = new JSONObject(strFileContents);

			fase = readCompetition(json);
		} catch (JSONException e) {
			Log.e("CALENDAR",
					"Error al crear el JSON del calendario: " + e.getMessage());
		} catch (Exception e) {
			Log.e("CALENDAR",
					"Error al parsear el calendario: " + e.getMessage());
			// e.printStackTrace();
		}

		return fase;
	}

	/**
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private Fase readCompetition(JSONObject json) throws Exception {

		JSONObject faseJSON = json.getJSONObject("fase");

		Fase fase = new Fase();
		fase.setName(faseJSON.getString("nombre"));

		JSONObject gruposJSON = json.getJSONObject("grupos");
		JSONObject grupoJSON;

		for (Iterator<String> iterator = gruposJSON.keys(); iterator.hasNext();) {
			grupoJSON = gruposJSON.getJSONObject(iterator.next());

			Grupo grupo = new Grupo();
			grupo.setName(grupoJSON.getString("nombre"));
			grupo.setClasificacion(readClasificacion(grupoJSON
					.getJSONObject("clasificacion")));
			fase.addGrupo(grupo);
		}
		if (json.has("zonas") && !json.isNull("zonas"))
			fase.setLeyenda(readLeyenda(json.getJSONObject("zonas")));
		return fase;
	}

	/**
	 * @param jsonObject
	 * @throws JSONException
	 */
	private Clasificacion readClasificacion(JSONObject json)
			throws JSONException {
		Clasificacion clasificacion = new Clasificacion();
		// HashMap<String, Team> clasif = new HashMap<String, Team>();
		JSONObject item, casa, fuera;
		Team team;
		ClasificacionInfo clas;
		String next;
		for (Iterator<String> iterator = json.keys(); iterator.hasNext();) {
			next = iterator.next();
			item = json.getJSONObject(next);
			team = new Team();
			team.setId(item.getString("id"));
			team.setShortName(item.getString("nom"));
			team.setUrl(item.getString("url"));
			team.addShieldCalendar(item.getString("escudo"));
			clas = new ClasificacionInfo();
			// Generales
			clas.setPos(item.getInt("pos"));
			clas.setPos_dif(item.getInt("pos_dif"));
			clas.setPts(item.getInt("pts"));
			clas.setPj(item.getInt("pj"));
			clas.setPg(item.getInt("pg"));
			clas.setPp(item.getInt("pp"));
			clas.setPe(item.getInt("pe"));
			clas.setGf(item.getInt("gf"));
			clas.setGc(item.getInt("gc"));
			// Home
			casa = item.getJSONObject("casa");
			clas.setPtsHome(casa.getInt("pts"));
			clas.setPjHome(casa.getInt("pj"));
			clas.setPgHome(casa.getInt("pg"));
			clas.setPpHome(casa.getInt("pp"));
			clas.setPeHome(casa.getInt("pe"));
			clas.setGfHome(casa.getInt("gf"));
			clas.setGcHome(casa.getInt("gc"));
			// Away
			fuera = item.getJSONObject("fuera");
			clas.setPtsAway(fuera.getInt("pts"));
			clas.setPjHome(fuera.getInt("pj"));
			clas.setPgAway(fuera.getInt("pg"));
			clas.setPpAway(fuera.getInt("pp"));
			clas.setPeAway(fuera.getInt("pe"));
			clas.setGfAway(fuera.getInt("gf"));
			clas.setGcAway(fuera.getInt("gc"));
			team.setClasificacion(clas);
			clasificacion.addTeamToClasificacion(next, team);
		}
		return clasificacion;
	}

	/**
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private HashMap<Integer, LeyendaInfo> readLeyenda(JSONObject json)
			throws JSONException {
		HashMap<Integer, LeyendaInfo> leyenda = new HashMap<Integer, LeyendaInfo>();
		String next;
		String titulo;
		// String tipo;
		int posicion;
		JSONObject jsonObjet;
		LeyendaInfo info;
		for (Iterator<String> iterator = json.keys(); iterator.hasNext();) {
			next = iterator.next();
			jsonObjet = json.getJSONObject(next);

			info = new LeyendaInfo();

			info.setPosicion(jsonObjet.getInt("posicion"));
			info.setTitulo(jsonObjet.getString("titulo"));
			info.setTipo(jsonObjet.getString("tipo"));

			leyenda.put(Integer.valueOf(next), info);
		}

		return leyenda;
	}
}
