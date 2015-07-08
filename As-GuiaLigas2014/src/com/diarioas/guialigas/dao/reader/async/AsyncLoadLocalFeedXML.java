package com.diarioas.guialigas.dao.reader.async;

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

import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.GeneralSettings;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistCompetition;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistLoad;
import com.diarioas.guialigas.utils.Defines.Prefix;
import com.diarioas.guialigas.utils.Defines.SECTIONS;
import com.diarioas.guialigas.utils.FileUtils;

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

				// Se obtiene la informacion de los prefijos
				generalSettings.setPrefix(parse.parsePlistPrefix());

				// Se obtiene la informacion de los estados de los partidos
				generalSettings.setStatus(parse.parsePlistStatus());

				// Se obtiene la informacion de los sistemas de juego
				generalSettings.setGamePlay(parse.parsePlistGamePlay());

				// Se obtiene la informacion de los sistemas de juego
				generalSettings.setSpades(parse.parsePlistSpades());

				// Se obtiene la informacion de los estados de los partidos
				generalSettings.setClasificationLabels(parse
						.parsePlistClasificationLabels());

				generalSettings.setCookies(parse.parsePlistCookies());

				// Se obtiene la info de las competiciones
				ArrayList<Competition> competitions = readCompetitions(parse
						.parsePlistCompetitions(),generalSettings.getPrefix());
				generalSettings.setCompetitions(competitions);

			}
		} catch (Exception e) {
			this.error = true;
		}
		return generalSettings;
	}

	private ArrayList<Competition> readCompetitions(
			ArrayList<Competition> competitions,HashMap<String, String> localPrefixes) throws Exception {

		ArrayList<Competition> competitionsBack = new ArrayList<Competition>();
		for (Competition competition : competitions) {
			// Si la fecha de actualizacion del fichero es mayor, se actualiza
			if (DatabaseDAO.getInstance(appContext).getCompetition(
					competition.getId()) == null
					|| competition.getFecModificacion() > DatabaseDAO
							.getInstance(appContext)
							.getCompetition(competition.getId())
							.getFecModificacion()) {
				competitionsBack.add(readCompetition(competition,localPrefixes));
			} else {
				// read competition for Database
				// competitionsBack.add(DatabaseDAO.getInstance(appContext)
				// .getCompetition(competition.getId()));
				competition.setSections(DatabaseDAO.getInstance(appContext)
						.getSectionsCompetition(competition.getId()));
				// competition.addSection(getSectionTeams(competition.getId()));
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

	private Competition readCompetition(Competition competition,HashMap<String, String> localPrefixes) {
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
				if((dataPrefix==null || dataPrefix.length()==0) && localPrefixes!=null && localPrefixes.containsKey(Prefix.PREFIX_DATA))
				{
					dataPrefix = localPrefixes.get(Prefix.PREFIX_DATA);
				}
				String dataRSSPrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_DATA_RSS);
				if((dataRSSPrefix==null || dataRSSPrefix.length()==0) && localPrefixes!=null && localPrefixes.containsKey(Prefix.PREFIX_DATA_RSS))
				{
					dataRSSPrefix = localPrefixes.get(Prefix.PREFIX_DATA_RSS);
				}
				parse = new ParsePlistCompetition(strFileContents, dataPrefix,
						dataRSSPrefix);

				ArrayList<HashMap<String, ?>> menu = parse.parsePlistMenu();
				int offset = 1;
				String nameSection;
				for (int i = 0; i < menu.size(); i++) {
					nameSection = ((String) menu.get(i).get("name"));
					if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_TEAM)) {
						// Se actualiza la info de los equipos
						competition.addSection(parse.parsePlistGrid(i, offset));
						// competition.addSection(parse.parsePlistGroups(1,
						// true));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_CALENDAR)) {
						// Se actualiza la info del Calendario
						competition.addSection(parse.parsePlistCalendar(i,
								offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_PALMARES)) {
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

					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_STADIUMS)) {
						// Se actualiza la info del Estadio
						competition.addSection(parse.parsePlistStadium(i,
								offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_COMPARATOR)) {
						// Se actualiza la info del Comparador ??
						competition.addSection(parse.parsePlistComparator(i,
								offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_SEARCH)) {
						// Se actualiza la info del Buscador
						competition.addSection(parse
								.parsePlistSearch(i, offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_CLASIFICATION)) {
						// Se actualiza la info de la clasificacion
						competition.addSection(parse.parsePlistClasificacion(i,
								offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_CARRUSEL)) {
						// Se obtiene la info del Carrusel
						competition.addSection(parse.parsePlistCarrusel(i,
								offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_NEWS)) {
						// Se obtiene la info de las noticias
						competition.addSection(parse.parsePlistNews(i, offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_NEWS_TAGS)) {
						// Se obtiene la info de las noticias
						competition.addSection(parse.parsePlistNewsTags(i, offset));						
					} else if (nameSection
							.equalsIgnoreCase("videos")) {
						// Se obtiene la info de los videos
						competition.addSection(parse
								.parsePlistVideos(i, offset));
					} else if (nameSection
							.equalsIgnoreCase(SECTIONS.SECTION_NAME_PHOTOGALLERY)) {
						// Se obtiene la info de las fotos
						competition.addSection(parse.parsePlistPhotos(i, offset));						
					}else if (((String) menu.get(i).get("viewType")).equalsIgnoreCase(SECTIONS.LINK_VIEW_OUTSIDE)) {
						// Se obtiene la info del link
						competition.addSection(parse.parsePlistLink(i, offset));
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
