/**
 * 
 */
package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.diarioas.guialigas.dao.model.player.Player;
import com.diarioas.guialigas.dao.model.team.Estadio;
import com.diarioas.guialigas.dao.model.team.Staff;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.model.team.TeamStats;
import com.diarioas.guialigas.dao.model.team.TituloTeam;
import com.diarioas.guialigas.utils.Defines.StaffCharge;

/**
 * @author robertosanchez
 * 
 */
public class ParseJSONTeam {

	private static final String TAG = "ParseJSONTeam";

	/**
	 * Read the dynamic team info
	 * 
	 * @param team
	 * @param strFileContents
	 * @param dataPrefix
	 * @param imagePrefix
	 * @return
	 * @throws JSONException
	 */
	public Team parseJSONTeam(Team team, String strFileContents,
			String imagePrefix, String dataPrefix) throws JSONException {
		JSONObject json = new JSONObject(strFileContents);
		JSONObject datosFic = (JSONObject) json.get("datos_fichero");
		int fecMod = (Integer) datosFic.get("ultima_modificacion");
		// if (true) {
		if (fecMod > team.getFecModificacion()) {
			// Log.d("Parser", "Se actualiza el fichero");
			JSONObject datosGenerales = (JSONObject) json
					.get("datos_generales");
			team.setFecModificacion(fecMod);
			if (datosFic.has("url_ficha"))
				team.setUrlFicha(datosFic.getString("url_ficha"));
			if (datosFic.has("url_tag")) {
				team.setUrlTag(datosFic.getString("url_tag"));

			}
			if (datosGenerales.has("nombre"))
				team.setName(datosGenerales.getString("nombre"));
			if (datosGenerales.has("nombre_corto"))
				team.setShortName(datosGenerales.getString("nombre_corto"));
			if (datosGenerales.has("web")) {
				team.setWeb(datosGenerales.getString("web"));
			}
			if (datosGenerales.has("pais"))
				team.setCountry(datosGenerales.getString("pais"));
			if (datosGenerales.has("localidad"))
				team.setCity(datosGenerales.getString("localidad"));
			if (datosGenerales.has("fecha_fundacion"))
				team.setFundation(datosGenerales.getString("fecha_fundacion"));
			// Se machacan los datos del presidente
			if (datosGenerales.has("presidente")) {
				if (team.getPresident() == null) {
					Staff st = new Staff();
					st.setCharge(StaffCharge.PRESIDENT);
					team.addPresident(st);
				}

				team.getPresident().setName(
						datosGenerales.getString("presidente"));
			}
			// Se machacan los datos del entrenador
			if (datosGenerales.has("entrenador")) {
				if (team.getMister() == null) {
					Staff st = new Staff();
					st.setCharge(StaffCharge.MISTER);
					team.addMister(st);
				}

				team.getMister()
						.setName(datosGenerales.getString("entrenador"));
			}

			// Recupero datos del estadio
			Estadio estadio = getEstadio(datosGenerales);
			team.setEstadio(estadio);

			// Recupero datos de la plantilla
			ArrayList<Player> plantilla = getPlantilla(json, imagePrefix,
					dataPrefix);
			team.setPlantilla(plantilla);

			// Recupero datos del Palmares
			ArrayList<TituloTeam> palmares = getPalmares(json);
			team.setPalmares(palmares);

			// Recupero datos de las estadisticas
			HashMap<String, TeamStats> stats = getEstadisticas(json);
			team.setStats(stats);
			return team;
		} else {
			// Log.d("Parser", "NO Se actualiza el fichero!!!!");
			return null;
		}

	}

	private HashMap<String, TeamStats> getEstadisticas(JSONObject json)
			throws JSONException {
		HashMap<String, TeamStats> stats = new HashMap<String, TeamStats>();
		if (json.has("estadisticas")) {
			JSONObject estadJSON = (JSONObject) json.get("estadisticas");
			TeamStats stat;
			JSONObject temporada, competicion;
			String comp = null, year;
			for (Iterator<String> iterator = estadJSON.keys(); iterator
					.hasNext();) {
				year = iterator.next();
				stat = new TeamStats();
				stat.setYear(year);
				temporada = (JSONObject) estadJSON.get(year);
				for (Iterator<String> it = temporada.keys(); it.hasNext();) {
					try {

						comp = it.next();
						// Log.d("Parser", "A–o: " + year + "Competicion: " +
						// comp);
						competicion = (JSONObject) temporada.get(comp);
						if (competicion.has("Goles a favor"))
							stat.setGolesAFavor(comp,
									competicion.getInt("Goles a favor"));
						if (competicion.has("Goles a favor"))
							stat.setPartidosJugados(comp,
									competicion.getInt("Partidos jugados"));
						if (competicion.has("Tarjetas amarillas"))
							stat.setTarjetasAmarillas(comp,
									competicion.getInt("Tarjetas amarillas"));
						if (competicion.has("Tarjetas rojas"))
							stat.setTarjetasRojas(comp,
									competicion.getInt("Tarjetas rojas"));
						if (competicion.has("Partidos ganados"))
							stat.setPartidosGanados(comp,
									competicion.getInt("Partidos ganados"));
						if (competicion.has("Partidos empatados"))
							stat.setPartidosEmpatados(comp,
									competicion.getInt("Partidos empatados"));
						if (competicion.has("Partidos perdidos"))
							stat.setPartidosPerdidos(comp,
									competicion.getInt("Partidos perdidos"));
					} catch (Exception e) {
						Log.d(TAG, "Fallo al parsear estadisticas: " + comp
								+ " : " + year);
					}
				}
				stat.setYear(year);
				stats.put(year, stat);
			}
		}
		return stats;
		// return new HashMap<String, TeamStats>();
	}

	private ArrayList<TituloTeam> getPalmares(JSONObject json)
			throws JSONException {
		ArrayList<TituloTeam> palmares = new ArrayList<TituloTeam>();
		if (json.has("palmares")) {
			JSONObject palmaresJSON = (JSONObject) json.get("palmares");
			JSONArray yearsJSON;
			ArrayList<String> years;
			String key = null;
			for (Iterator<String> iterator = palmaresJSON.keys(); iterator
					.hasNext();) {
				try {

					key = iterator.next();
					yearsJSON = (JSONArray) palmaresJSON.get(key);
					years = new ArrayList<String>();
					for (int i = 0; i < yearsJSON.length(); i++) {
						years.add(yearsJSON.getString(i));
					}
					palmares.add(new TituloTeam(key, years));
				} catch (Exception e) {
					Log.d(TAG, "Fallo al parsear palmares: " + key);
				}
			}
		}
		return palmares;
		// return new ArrayList<TituloTeam>();
	}

	private ArrayList<Player> getPlantilla(JSONObject json, String imagePrefix,
			String dataPrefix) throws JSONException {
		ArrayList<Player> plantilla = new ArrayList<Player>();
		if (json.has("plantilla")) {
			JSONObject plantillaJSON = (JSONObject) json.get("plantilla");

			String id;
			int idFake = -1;
			Player player = null;
			JSONObject playerJSON;
			for (Iterator<String> iter = plantillaJSON.keys(); iter.hasNext();) {
				try {
					id = iter.next();
					playerJSON = (JSONObject) plantillaJSON.get(id);
					player = new Player();
					player.setId(Integer.valueOf(id));
					if (playerJSON.has("nombre"))
						player.setShortName(playerJSON.getString("nombre"));
					if (playerJSON.has("demarcacion"))
						player.setDemarcation(playerJSON
								.getString("demarcacion"));
					try {
						player.setDorsal(playerJSON.getInt("dorsal"));
					} catch (Exception e) {
						Log.e(TAG,
								player.getShortName() + ": " + e.getMessage());
						player.setDorsal(idFake);
						idFake--;
					}
					if (playerJSON.has("datos_jugador"))
						player.setUrl(dataPrefix
								+ playerJSON.getString("datos_jugador"));
					try {
						player.setUrlFoto(imagePrefix
								+ playerJSON.getString("imagen_jugador"));
					} catch (Exception e) {
						Log.e(TAG,
								player.getShortName() + ": " + e.getMessage());
						player.setUrlFoto(null);
					}
					plantilla.add(player);
				} catch (Exception e) {
					if (player != null)
						Log.e(TAG,
								"Ha fallado al recoger los datos del jugador: "
										+ player.getShortName());
				}
			}
		}
		return plantilla;
		// return new ArrayList<Player>();
	}

	private Estadio getEstadio(JSONObject datosGenerales) throws JSONException {
		Estadio estadio = new Estadio();
		if (datosGenerales.has("estadio")) {
			JSONObject estadioJson = (JSONObject) datosGenerales.get("estadio");

			if (estadioJson.has("nombre"))
				estadio.setName(estadioJson.getString("nombre"));
			if (estadioJson.has("pais"))
				estadio.setCountry(estadioJson.getString("pais"));
			if (estadioJson.has("localidad"))
				estadio.setCity(estadioJson.getString("localidad"));
			if (estadioJson.has("direccion"))
				estadio.setAddress(estadioJson.getString("direccion"));
			if (estadioJson.has("aforo"))
				estadio.setCapacity(estadioJson.getInt("aforo"));

			if (estadioJson.has("dimensiones")) {
				try {
					String[] dim = estadioJson.getString("dimensiones").split(
							"x");
					estadio.setDimX(Integer.valueOf(dim[0].replace(" ", "")));
					estadio.setDimY(Integer.valueOf(dim[1].replace(" ", "")));
				} catch (Exception e) {
					Log.d(TAG, "Las dimensiones del estadio no estan definidas");
				}

			}
			if (estadioJson.has("geolocalizacion")) {
				String[] coor = estadioJson.getString("geolocalizacion").split(
						",");
				try {
					estadio.setLat(Double.valueOf(coor[0].replace(" ", "")
							.substring(1)));
					estadio.setLon(Double
							.valueOf(coor[1].replace(" ", "").substring(0,
									coor[1].replace(" ", "").length() - 1)));
				} catch (Exception e) {
					Log.d(TAG, "Las coordenadas del estadio no estan definidas");
				}

			}
		}
		return estadio;
	}

}
