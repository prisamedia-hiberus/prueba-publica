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
import com.diarioas.guialigas.dao.model.player.PlayerStats;
import com.diarioas.guialigas.dao.model.player.TituloPlayer;
import com.diarioas.guialigas.dao.model.player.Trayectoria;

/**
 * @author robertosanchez
 * 
 */
public class ParseJSONPlayer {

	private static final String TAG = "ParseJSONPlayer";

	/**
	 * @param currentPlayer
	 * @param strFileContents
	 * @return
	 * @throws JSONException
	 */
	public Player parseJSONPlayer(Player player, String strFileContents,
			String imagePrefix) throws JSONException {
		JSONObject json = new JSONObject(strFileContents);
		JSONObject datosFic = (JSONObject) json.get("datos_fichero");
		int fecMod = (Integer) datosFic.get("ultima_modificacion");
		if (fecMod > player.getFecModificacion()) {

			JSONObject datosGenerales = (JSONObject) json
					.get("datos_generales");
			player.setFecModificacion(fecMod);
			if (datosFic.has("url_ficha"))
				player.setUrlFicha(datosFic.getString("url_ficha"));
			if (datosFic.has("url_tag"))
				player.setUrlTag(datosFic.getString("url_tag"));
			if (datosFic.has("img_src_100"))
				player.setUrlFoto(imagePrefix
						+ datosFic.getString("img_src_100"));
			// if (datosFic.has("img_src_60"))
			// player.setUrlThumb(imagePrefix+datosFic.getString("img_src_60"));
			if (player.getId() == 0 && datosFic.has("id"))
				player.setId(datosFic.getInt("id"));

			if (datosFic.has("competiciones_con_datos")) {
				String comp = datosFic.getString("competiciones_con_datos");
				comp = comp.substring(1, comp.length() - 1);// Se quitan los
															// corchetes
				comp = comp.replace("\"", "");// Se quiten las comillas
				comp = comp.replace(",", ", ");// Se a�ade un espacio entre
												// competiciones
				player.setCompeticiones(comp + ".");
			}
			if (datosGenerales.has("nombre"))
				player.setName(datosGenerales.getString("nombre"));
			if (datosGenerales.has("nombre_corto"))
				try {
					player.setShortName(datosGenerales
							.getString("nombre_corto"));
				} catch (Exception e) {
					player.setShortName("");
				}

			if (datosGenerales.has("demarcacion"))
				try {
					player.setDemarcation(datosGenerales
							.getString("demarcacion"));
				} catch (Exception e) {
					player.setShortName("");
				}

			if (datosGenerales.has("edad"))
				try {
					player.setAge(datosGenerales.getInt("edad"));
				} catch (Exception e) {
					player.setAge(0);
				}

			if (datosGenerales.has("fecha_nacimiento"))
				try {
					player.setDateBorn(datosGenerales
							.getString("fecha_nacimiento"));
				} catch (Exception e) {
					player.setDateBorn("");
				}

			if (datosGenerales.has("peso"))
				try {
					player.setWeight(datosGenerales.getInt("peso"));
				} catch (Exception e) {
					player.setWeight(0);
				}

			if (datosGenerales.has("altura"))
				try {
					player.setHeight(datosGenerales.getInt("altura"));
				} catch (Exception e) {
					player.setHeight(0);
				}

			if (datosGenerales.has("nacionalidad"))
				try {
					player.setNacionality(datosGenerales
							.getString("nacionalidad"));
				} catch (Exception e) {
					player.setNacionality("");
				}

			if (datosGenerales.has("equipo")) {
				JSONObject equipoJSON = datosGenerales.getJSONObject("equipo");
				if ((player.getIdTeam() == null || player.getIdTeam()
						.equalsIgnoreCase(""))) {
					try {
						if (equipoJSON.has("id"))
							player.setIdTeam(equipoJSON.getString("id"));
					} catch (Exception e) {
						player.setIdTeam("");
					}
				}

				if ((player.getNameTeam() == null || player.getNameTeam()
						.equalsIgnoreCase(""))) {
					try {
						if (equipoJSON.has("nombre"))
							player.setNameTeam(equipoJSON.getString("nombre"));
					} catch (Exception e) {
						player.setNameTeam("");
					}
				}
			}

			// Recupero datos de la Trayectoria
			ArrayList<Trayectoria> trayectoria = getTrayectoria(json);
			player.setTrayectoria(trayectoria);

			// Recupero datos del Palmares
			ArrayList<TituloPlayer> palmares = getPalmares(json);
			player.setPalmares(palmares);

			// Recupero datos de las Estadisticas
			HashMap<String, PlayerStats> stats = getStats(json);
			player.setStats(stats);

			return player;
		} else {
			return null;
		}
	}

	private HashMap<String, PlayerStats> getStats(JSONObject json) {
		HashMap<String, PlayerStats> estadisticas = new HashMap<String, PlayerStats>();

		try {

			JSONObject estadisticasJSON = (JSONObject) json.get("estadisticas");
			JSONObject statsJSON;
			String comp, team, year;
			PlayerStats stats;
			int ge, gm, pa, as, pr, mi, ta, tr;
			for (Iterator<String> iterator = estadisticasJSON.keys(); iterator
					.hasNext();) {
				year = iterator.next();
				stats = new PlayerStats(year);

				for (Iterator<String> iter = estadisticasJSON.getJSONObject(
						year).keys(); iter.hasNext();) {
					comp = iter.next();
					for (Iterator<String> it = estadisticasJSON
							.getJSONObject(year).getJSONObject(comp).keys(); it
							.hasNext();) {
						team = it.next();
						statsJSON = estadisticasJSON.getJSONObject(year)
								.getJSONObject(comp).getJSONObject(team);

						if (!statsJSON.isNull("Goles encajados")) {
							try {
								ge = statsJSON.getInt("Goles encajados");
							} catch (JSONException e) {
								ge = 0;
							}
						} else {
							ge = 0;
						}
						if (!statsJSON.isNull("Goles")) {
							try {
								gm = statsJSON.getInt("Goles");
							} catch (JSONException e) {
								gm = 0;
							}
						} else {
							gm = 0;
						}
						if (!statsJSON.isNull("Paradas")) {
							try {
								pa = statsJSON.getInt("Paradas");
							} catch (JSONException e) {
								pa = 0;
							}
						} else {
							pa = 0;
						}
						if (!statsJSON.isNull("Asistencias")) {
							try {
								as = statsJSON.getInt("Asistencias");
							} catch (JSONException e) {
								as = 0;
							}
						} else {
							as = 0;
						}
						try {
							pr = statsJSON.getInt("Partidos");
						} catch (JSONException e) {
							pr = 0;
						}
						try {
							mi = statsJSON.getInt("Minutos");
						} catch (JSONException e) {
							mi = 0;
						}
						try {
							ta = statsJSON.getInt("Tarjetas amarillas");
						} catch (JSONException e) {
							ta = 0;
						}
						try {
							tr = statsJSON.getInt("Tarjetas rojas");
						} catch (JSONException e) {
							tr = 0;
						}
						stats.addStat(comp, team, gm, ge, pr, mi, ta, tr, pa,
								as);

					}

				}
				estadisticas.put(year, stats);
			}
		} catch (JSONException e) {
			Log.d(TAG, "Fallo al parsear estadisticas del jugador");
		}
		return estadisticas;
	}

	private ArrayList<TituloPlayer> getPalmares(JSONObject json) {
		ArrayList<TituloPlayer> palmares = new ArrayList<TituloPlayer>();
		try {

			JSONObject palmaresJSON = (JSONObject) json.get("palmares");
			JSONObject teams, teamJSON = null;
			JSONArray temporadas;
			String nameCompetition, nameTeam = null;
			int numTitulos = 0;
			TituloPlayer title;
			for (Iterator<String> iterator = palmaresJSON.keys(); iterator
					.hasNext();) {
				try {

					nameCompetition = iterator.next();
					teams = (JSONObject) palmaresJSON.get(nameCompetition);
					if (teams.has("titulos"))
						numTitulos = teams.getInt("titulos");
					if (teams.has("equipos"))
						teamJSON = teams.getJSONObject("equipos");
					title = new TituloPlayer(nameCompetition, numTitulos);
					for (Iterator<String> it = teamJSON.keys(); it.hasNext();) {
						nameTeam = it.next();
						temporadas = teamJSON.getJSONObject(nameTeam)
								.getJSONArray("temporadas");
						for (int i = 0; i < temporadas.length(); i++) {
							title.addYear(nameTeam, temporadas.getString(i));
						}
					}
					palmares.add(title);
				} catch (JSONException e) {
					Log.d(TAG, "Fallo al parsear palmares del jugador: "
							+ nameTeam);
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, "Fallo al parsear palmares del jugador ");
		}
		return palmares;
	}

	private ArrayList<Trayectoria> getTrayectoria(JSONObject json) {

		ArrayList<Trayectoria> trayectoria = new ArrayList<Trayectoria>();
		try {

			JSONObject trayectoriaJSON = (JSONObject) json.get("trayectoria");
			JSONObject trayJSON;
			JSONArray equipos = null;
			String year = null;
			Trayectoria trayec;
			for (Iterator<String> iterator = trayectoriaJSON.keys(); iterator
					.hasNext();) {
				try {
					year = iterator.next();
					trayJSON = trayectoriaJSON.getJSONObject(year);
					if (trayJSON.has("equipos"))
						equipos = trayJSON.getJSONArray("equipos");
					trayec = new Trayectoria(Integer.valueOf(year),
							trayJSON.getString("descripcion"));
					for (int i = 0; i < equipos.length(); i++) {
						trayec.addEquipo(((JSONObject) equipos.get(i))
								.getString("nombre"), ((JSONObject) equipos
								.get(i)).getInt("partidos"),
								((JSONObject) equipos.get(i)).getInt("goles"));
					}
					trayectoria.add(trayec);
				} catch (JSONException e) {
					Log.d(TAG, "Fallo al parsear trayectoria del jugador: "
							+ year);
				}

			}
		} catch (JSONException e) {
			Log.d(TAG, "Fallo al parsear trayectoria del jugador: ");
		}
		return trayectoria;
	}
}
