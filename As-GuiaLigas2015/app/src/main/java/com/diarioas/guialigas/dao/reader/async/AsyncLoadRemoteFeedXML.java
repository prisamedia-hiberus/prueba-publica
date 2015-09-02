package com.diarioas.guialigas.dao.reader.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.GeneralSettings;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistCompetition;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistLoad;
import com.diarioas.guialigas.utils.Defines.Prefix;
import com.diarioas.guialigas.utils.Defines.ReturnDataDatabases;
import com.diarioas.guialigas.utils.Defines.SECTIONS;

import java.util.ArrayList;
import java.util.HashMap;

public class AsyncLoadRemoteFeedXML extends
		AsyncTask<String, Void, GeneralSettings> {

	public interface AsyncRemoteFeedXMLListener {

		void onRemoteSuccessfulExecute(GeneralSettings generalSettings);

		void onRemoteFailureExecute();
	}

	AsyncRemoteFeedXMLListener listener;
	Context appContext;
	boolean error;

	// private String strFileContents;

	public AsyncLoadRemoteFeedXML(AsyncRemoteFeedXMLListener listener,
			Context ctx) {
		this.listener = listener;
		this.appContext = ctx;
		this.error = false;
	}

	@Override
	protected GeneralSettings doInBackground(String... urls) {
		this.error = false;
		GeneralSettings generalSettings = new GeneralSettings();

		try {

			HashMap<?, ?> splash;
			HashMap<?, ?> header;
			
			HashMap<String, String> prefix;
			// String strFileContents = readRemoteFile(urls[0]);
			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);
			if (strFileContents == null || strFileContents.equalsIgnoreCase("")) {
				this.error = true;
			} else {

				ParsePlistLoad parse = new ParsePlistLoad(strFileContents);
				// Se actualiza la info del Splash
				splash = parse.parsePlistSplash();
				DatabaseDAO.getInstance(appContext).updateStaticSplash(splash);
				generalSettings.setSplash(splash);
				
				// Se actualiza la info de la cabecera				
				header= parse.parsePlistHeader();
				DatabaseDAO.getInstance(appContext).updateStaticHeader(header);
				generalSettings.setHeader(header);

				// Se obtiene la informacion de los prefijos
				prefix = parse.parsePlistPrefix();
				DatabaseDAO.getInstance(appContext).updateStaticPrefix(prefix);
				generalSettings.setPrefix(prefix);

				// Se obtiene la informacion de los estados de los partidos
				HashMap<String, String> status = parse.parsePlistStatus();
				DatabaseDAO.getInstance(appContext).updateStaticStatus(status);
				generalSettings.setStatus(status);

				// Se obtiene la informacion de los sistemas de juego
				HashMap<String, String> gameplay = parse.parsePlistGamePlay();
				DatabaseDAO.getInstance(appContext).updateGamePlay(gameplay);
				generalSettings.setGamePlay(gameplay);

				// Se obtiene la informacion de las picas
				HashMap<String, String> spades = parse.parsePlistSpades();
				DatabaseDAO.getInstance(appContext).updateSpades(spades);
				generalSettings.setStatus(status);

				// Se obtiene la informacion de los labels de la clasificacion
				HashMap<String, String> clasificationLabels = parse
						.parsePlistClasificationLabels();
				DatabaseDAO.getInstance(appContext).updateClasificationLabels(
						clasificationLabels);
				generalSettings.setClasificationLabels(clasificationLabels);

				// Se obtiene la info de las competiciones
				ArrayList<Competition> competitions = parse.parsePlistCompetitions();
				generalSettings.setCompetitions(readCompetitions(competitions));

				HashMap<String, String> cookies = parse.parsePlistCookies();
				generalSettings.setCookies(cookies);

				// Se copia el fichero en local
				ReadRemote.copyFile(appContext,
						ReturnDataDatabases.DB_SETTINGS_FILE_NAME,
						strFileContents);
			}
		} catch (Exception e) {
			this.error = true;
		}
		return generalSettings;
	}

	private ArrayList<Competition> readCompetitions(
			ArrayList<Competition> competitions) throws Exception {
		String strFileContents;
		ParsePlistCompetition parse;
		// DatabaseDAO.getInstance(appContext).getCompetitions();
		Competition comp;
		for (Competition competition : competitions) {
			// Si la competicion no existe, se actualiza
			// Si la fecha de actualizacion del fichero es mayor, se actualiza
			comp = DatabaseDAO.getInstance(appContext).getCompetition(
					competition.getId());
			 if (true) {
			//if (comp == null
			//		|| competition.getFecModificacion() > comp
			//				.getFecModificacion()) {
				try {
					// Se lee el fichero de configuracion de la competicion
					// strFileContents = readRemoteFile(competition.getUrl());
					strFileContents = ReadRemote.readRemoteFile(
							competition.getUrl(), false);
					// Se copia el fichero en local
					ReadRemote
							.copyFile(appContext,
									getLocalName(competition.getUrl()),
									strFileContents);
					if (strFileContents != null) {
						String dataPrefix = DatabaseDAO.getInstance(appContext)
								.getPrefix(Prefix.PREFIX_DATA);
						String dataRSSPrefix = DatabaseDAO.getInstance(
								appContext).getPrefix(Prefix.PREFIX_DATA_RSS);
						parse = new ParsePlistCompetition(strFileContents,
								dataPrefix, dataRSSPrefix);

						ArrayList<HashMap<String, ?>> menu = parse
								.parsePlistMenu();
						int offset = 1;
						String nameSection;
						for (int i = 0; i < menu.size(); i++) {
							nameSection = ((String) menu.get(i).get("name"));
							if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_TEAM)) {
								// Se actualiza la info de los equipos
								competition.addSection(parse.parsePlistGrid(i,
										offset));
								// competition.addSection(parse.parsePlistGroups(1,
								// true));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_SORT)) {
								// Se actualiza la info del Comparador ??
								competition.addSection(parse.parsePlistSort(i,
										offset));								
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_CALENDAR)) {
								// Se actualiza la info del Calendario
								competition.addSection(parse
										.parsePlistCalendar(i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_PALMARES)) {
								// Se actualiza la info del Palmares
								competition.addSection(parse
										.parsePlistPalmares(i, offset));

								// Se obtiene la informacion de los labels del
								// palmares
								if (menu.get(i).containsKey(
										"legendHistoricalPalmares")) {
									HashMap<String, String> palmaresPosition = parse
											.parsePlistPalmaresLabelsPosition((HashMap<String, ?>) menu
													.get(i)
													.get("legendHistoricalPalmares"));
									DatabaseDAO.getInstance(appContext)
											.updateMundialPalmaresPosition(
													palmaresPosition,
													competition.getId());

									HashMap<String, String> palmaresLegend = parse
											.parsePlistPalmaresLabelsLegend((HashMap<String, ?>) menu
													.get(i)
													.get("legendHistoricalPalmares"));
									DatabaseDAO.getInstance(appContext)
											.updateMundialPalmaresLegend(
													palmaresLegend,
													competition.getId());

									HashMap<String, HashMap<String, String>> palmaresYear = parse
											.parsePlistPalmaresLabelsYear((HashMap<String, ?>) menu
													.get(i)
													.get("legendHistoricalPalmares"));
									DatabaseDAO.getInstance(appContext)
											.updateMundialPalmaresYear(
													palmaresYear,
													competition.getId());
								}

							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_STADIUMS)) {
								// Se actualiza la info del Estadio
								competition.addSection(parse.parsePlistStadium(
										i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_COMPARATOR)) {
								// Se actualiza la info del Comparador ??
								competition.addSection(parse
										.parsePlistComparator(i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_SEARCH)) {
								// Se actualiza la info del Buscador
								competition.addSection(parse.parsePlistSearch(
										i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_CLASIFICATION)) {
								// Se actualiza la info de la clasificacion
								competition.addSection(parse
										.parsePlistClasificacion(i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_CARRUSEL)) {
								// Se obtiene la info del Carrusel
								competition.addSection(parse
										.parsePlistCarrusel(i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_NEWS)) {
								// Se obtiene la info de las noticias
								competition.addSection(parse.parsePlistNews(i, offset));								
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_NEWS_TAGS)) {
								// Se obtiene la info de las noticias
								competition.addSection(parse.parsePlistNewsTags(i,
										offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_VIDEOS)) {
								// Se obtiene la info de los videos
								competition.addSection(parse.parsePlistVideos(
										i, offset));
							} else if (nameSection
									.equalsIgnoreCase(SECTIONS.SECTION_NAME_PHOTOGALLERY)) {
								// Se obtiene la info de las fotos
								competition.addSection(parse.parsePlistPhotos(
										i, offset));
							}else if (((String) menu.get(i).get("viewType")).equalsIgnoreCase(SECTIONS.LINK_VIEW_OUTSIDE)) {
								// Se obtiene la info del link
								competition.addSection(parse.parsePlistLink(i,
										offset));
							}
						}

						DatabaseDAO.getInstance(appContext)
								.updateStaticCompetition(competition);
					} else {
						Log.e("AsyncLoadRemoteFeedXML",
								"Error al leer el fichero de una competicion: "
										+ competition.getName());
					}
				} catch (Exception e) {
					Log.e("AsyncLoadRemoteFeedXML",
							"Error al cargar una competicion: "
									+ competition.getName() + " Mens: "
									+ e.getMessage());
				}
			}
		}
		return competitions;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(GeneralSettings generalSettings) {
		super.onPostExecute(generalSettings);
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onRemoteSuccessfulExecute(generalSettings);
			} else {
				this.listener.onRemoteFailureExecute();
			}
		}
	}

	/**
	 * Recorta la url hasta quedarse con el nombre, quita el xml y a�ade un
	 * plist
	 * 
	 * @param urlCompetition
	 * @return
	 */
	private String getLocalName(String urlCompetition) {
		String[] url;
		String nameUrl;
		url = urlCompetition.split("/");
		nameUrl = url[url.length - 1];
		nameUrl = nameUrl.substring(0, nameUrl.length() - 3) + "plist";
		return nameUrl;
	}

	public void removeListener(AsyncRemoteFeedXMLListener listener) {
		if (this.listener != null) {
			this.listener = null;
		}
	}

}
