package com.diarioas.guiamundial.dao.reader.async;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.competition.Competition;
import com.diarioas.guiamundial.dao.model.general.GeneralSettings;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParsePlistCompetition;
import com.diarioas.guiamundial.dao.reader.parser.ParsePlistLoad;
import com.diarioas.guiamundial.utils.Defines.Prefix;
import com.diarioas.guiamundial.utils.FileUtils;

public class AsyncLoadLocalFeedXML extends
		AsyncTask<String, Void, GeneralSettings> {

	public interface AsyncLocalFeedXMLListener {
		void onLocalSuccessfulExecute(GeneralSettings generalSettings);

		void onLocalFailureExecute();
	}

	AsyncLocalFeedXMLListener listener;
	Context appContext;
	boolean error;

	// private String strFileContents;

	public AsyncLoadLocalFeedXML(AsyncLocalFeedXMLListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected GeneralSettings doInBackground(String... urls) {
		// protected ArrayList<Competition> doInBackground(String... urls) {
		GeneralSettings generalSettings = new GeneralSettings();

		this.error = false;
		try {

			String strFileContents = readLocalFile(urls[0]);
			if (strFileContents == null || strFileContents.equalsIgnoreCase("")) {
				this.error = true;
			} else {

				ParsePlistLoad parse = new ParsePlistLoad(strFileContents);

				// Se actualiza la info del Splash
				generalSettings.setSplash(parse.parsePlistSplash());
				// DatabaseDAO.getInstance(appContext).updateStaticSplash(splash);

				// Se obtiene la informacion de los prefijos
				generalSettings.setPrefix(parse.parsePlistPrefix());
				// DatabaseDAO.getInstance(appContext).updateStaticPrefix(prefix);

				// Se obtiene la informacion de los estados de los partidos
				generalSettings.setStatus(parse.parsePlistStatus());
				// DatabaseDAO.getInstance(appContext).updateStaticStatus(status);

				// Se obtiene la informacion de los sistemas de juego
				generalSettings.setGamePlay(parse.parsePlistGamePlay());
				// DatabaseDAO.getInstance(appContext).updateGamePlay(gameplay);

				// Se obtiene la informacion de los sistemas de juego
				generalSettings.setSpades(parse.parsePlistSpades());
				// DatabaseDAO.getInstance(appContext).updateSpades(spades);

				// Se obtiene la informacion de los estados de los partidos
				generalSettings.setClasificationLabels(parse
						.parsePlistClasificationLabels());
				// DatabaseDAO.getInstance(appContext).updateClasificationLabels(
				// clasificationLabels);

				// Se obtiene la info de las competiciones
				ArrayList<Competition> competitions = readCompetitions(parse
						.parsePlistCompetitions());
				generalSettings.setCompetitions(competitions);

			}
		} catch (Exception e) {
			this.error = true;
		}
		return generalSettings;
	}

	private ArrayList<Competition> readCompetitions(
			ArrayList<Competition> competitions) throws Exception {

		ArrayList<Competition> competitionsBack = new ArrayList<Competition>();
		Competition comp;
		for (Competition competition : competitions) {
			// Si la fecha de actualizacion del fichero es mayor, se actualiza
			comp = DatabaseDAO.getInstance(appContext).getCompetition(
					competition.getId());
			if (comp == null
					|| competition.getFecModificacion() > comp
							.getFecModificacion()) {
				competitionsBack.add(readCompetition(competition));
			} else {
				// read competition for Database
				competition.setSections(DatabaseDAO.getInstance(appContext)
						.getSectionsCompetition(competition.getId()));
				competitionsBack.add(competition);

			}

		}
		return competitionsBack;
	}

	// private Section getSectionTeams(int competitionId) {
	// TeamSection section = new TeamSection();
	// section.setOrder(1);
	// section.setType(SECTIONS.TEAMS);
	// section.setGroups(DatabaseDAO.getInstance(appContext).getInfoGroups(
	// competitionId));
	// return section;
	// }

	private Competition readCompetition(Competition competition) {
		String strFileContents;
		String nameUrl;
		ParsePlistCompetition parse;
		try {

			nameUrl = getLocalName(competition.getUrl());
			// Se lee el fichero de configuracion de la competicion
			strFileContents = readLocalFile(nameUrl);
			if (strFileContents != null) {
				String dataPrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_DATA);
				String dataRSSPrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_DATA_RSS);
				parse = new ParsePlistCompetition(strFileContents, dataPrefix,
						dataRSSPrefix);

				ArrayList<HashMap<String, ?>> menu = parse.parsePlistMenu();
				int offset = 1;
				for (int i = 0; i < menu.size(); i++) {
					if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("grid")) {
						// Se actualiza la info de los equipos
						competition.addSection(parse.parsePlistGrid(i, offset));
						// competition.addSection(parse.parsePlistGroups(1,
						// true));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("calendar")) {
						// Se actualiza la info del Calendario
						competition.addSection(parse.parsePlistCalendar(i,
								offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("palmares")) {
						// Se actualiza la info del Palmares
						competition.addSection(parse.parsePlistPalmares(i,
								offset));

						// Se obtiene la informacion de los labels del palmares
						if (menu.get(i).containsKey("legendHistoricalPalmares")) {
							HashMap<String, String> palmaresPosition = parse
									.parsePlistPalmaresLabelsPosition((HashMap<String, ?>) menu
											.get(i).get(
													"legendHistoricalPalmares"));
							DatabaseDAO.getInstance(appContext)
									.updateMundialPalmaresPosition(
											palmaresPosition,
											competition.getId());

							HashMap<String, String> palmaresLegend = parse
									.parsePlistPalmaresLabelsLegend((HashMap<String, ?>) menu
											.get(i).get(
													"legendHistoricalPalmares"));
							DatabaseDAO
									.getInstance(appContext)
									.updateMundialPalmaresLegend(
											palmaresLegend, competition.getId());

							HashMap<String, HashMap<String, String>> palmaresYear = parse
									.parsePlistPalmaresLabelsYear((HashMap<String, ?>) menu
											.get(i).get(
													"legendHistoricalPalmares"));
							DatabaseDAO.getInstance(appContext)
									.updateMundialPalmaresYear(palmaresYear,
											competition.getId());
						}

					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("stadiums")) {
						// Se actualiza la info del Estadio
						competition.addSection(parse.parsePlistStadium(i,
								offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("comparador")) {
						// Se actualiza la info del Comparador ??
						competition.addSection(parse.parsePlistComparator(i,
								offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("search")) {
						// Se actualiza la info del Buscador
						competition.addSection(parse
								.parsePlistSearch(i, offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("clasification")) {
						// Se actualiza la info de la clasificacion
						competition.addSection(parse.parsePlistClasificacion(i,
								offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("carrusel")) {
						// Se obtiene la info del Carrusel
						competition.addSection(parse.parsePlistCarrusel(i,
								offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("noticias")) {
						// Se obtiene la info de las noticias
						competition.addSection(parse.parsePlistNews(i, offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("videos")) {
						// Se obtiene la info de los videos
						competition.addSection(parse
								.parsePlistVideos(i, offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("triviaAS")) {
						// Se obtiene la info del link
						competition.addSection(parse.parsePlistLink(i, offset));
					} else if (((String) menu.get(i).get("name"))
							.equalsIgnoreCase("photoGallery")) {
						// Se obtiene la info de las fotos
						competition.addSection(parse
								.parsePlistPhotos(i, offset));
					}
				}

				DatabaseDAO.getInstance(appContext).updateStaticCompetition(
						competition);

			}
		} catch (Exception e) {
			Log.e("AsyncLoadLocalFeedXML", "Error al cargar una competicion: "
					+ competition.getName() + " Mens: " + e.getMessage());
		}

		return competition;
	}

	private String getLocalName(String urlCompetition) {
		String[] url;
		String nameUrl;
		url = urlCompetition.split("/");
		nameUrl = url[url.length - 1];
		// nameUrl = "local" + nameUrl.substring(0, nameUrl.length() - 3)+
		// "plist";
		nameUrl = nameUrl.substring(0, nameUrl.length() - 3) + "plist";
		return nameUrl;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(GeneralSettings generalSettings) {
		super.onPostExecute(generalSettings);
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onLocalSuccessfulExecute(generalSettings);
			} else {
				this.listener.onLocalFailureExecute();
			}

		}
	}

	/**
	 * @param fileName
	 * @return
	 * @return
	 * @throws IOException
	 * 
	 */
	private String readLocalFile(String fileName) throws IOException {
		String strFileContents = null;
		BufferedReader br = null;
		try {
			File sectionCacheFile = new File(appContext.getFilesDir(), fileName);
			StringBuilder sb = new StringBuilder();

			int len;

			if (sectionCacheFile.exists() == false) {
				// No existe el fichero en FileDir--> se recupera el plist
				// inicial
				// if (fileName.startsWith("local"))
				// fileName = fileName.substring(5, fileName.length());
				strFileContents = FileUtils.readFileFromAssets(appContext
						.getResources().getAssets(), fileName);

			} else {
				// Se lee el fichero de FileDir

				// Old Method
				// FileReader fr = new FileReader(sectionCacheFile);
				// br = new BufferedReader(fr);
				// char[] buffer = new char[1024];
				//
				// while ((len = br.read(buffer)) != -1) {
				// sb.append(new String(buffer, 0, len));
				// }
				// strFileContents = sb.toString();

				// New Method
				FileInputStream fileInputStream = new FileInputStream(
						sectionCacheFile.getPath());
				br = new BufferedReader(new InputStreamReader(fileInputStream,
						"utf8"), 1024);
				char[] buffer = new char[1024];
				while ((len = br.read(buffer)) != -1) {
					sb.append(new String(buffer, 0, len));
				}
				strFileContents = sb.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				br.close();
		}
		return strFileContents;
	}
}
