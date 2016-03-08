package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.diarioas.guialigas.BuildConfig;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.GeneralSettings;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadLocalFeedXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadLocalFeedXML.AsyncLocalFeedXMLListener;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadRemoteFeedXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadRemoteFeedXML.AsyncRemoteFeedXMLListener;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.ReturnSharedPreferences;
import com.diarioas.guialigas.utils.Reachability;
import com.diarioas.guialigas.utils.comparator.CompetitionComparator;
import com.diarioas.guialigas.utils.comparator.SectionComparator;

public class RemoteDataDAO implements AsyncLocalFeedXMLListener,
		AsyncRemoteFeedXMLListener {

	private static final String TAG = "RemoteDataDAO";
	private static RemoteDataDAO sInstance = null;
	private Context mContext = null;

	private AsyncLoadLocalFeedXML mainStaticLoadFeedXMLReader;
	private AsyncLoadRemoteFeedXML mainStaticRemoteFeedXMLReader;

	private ArrayList<RemoteDataDAOListener> listeners;
	private GeneralSettings generalSettings;
	private HashMap<Integer, ArrayList<Section>> sectionsMap;

	public static RemoteDataDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteDataDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteDataDAOListener>();
			sInstance.sectionsMap = new HashMap<Integer, ArrayList<Section>>();
		}
		return sInstance;
	}

	public interface RemoteDataDAOListener {
		void onSuccessRemoteconfig();

		void onFailureRemoteconfig();
	}

	/********************** Gestion de Listeners ****************************/
	/************************************************************************/

	public void addListener(RemoteDataDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteDataDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
	}

	public void cancelGetData() {
		if (mainStaticLoadFeedXMLReader != null) {
			mainStaticLoadFeedXMLReader.cancel(true);
			mainStaticLoadFeedXMLReader = null;
		}
		if (mainStaticRemoteFeedXMLReader != null) {
			mainStaticRemoteFeedXMLReader.cancel(true);
			mainStaticRemoteFeedXMLReader = null;
		}
	}

	private void responseFailureRemoteConfig() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureRemoteconfig();
			}
		}
	}

	private void responseUpdateDabase() {

		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig();
			}
		}
	}

	/*****************************************************************/

	public void refreshDatabaseWithNewResults() {
		this.mainStaticLoadFeedXMLReader = null;
		// Cargamos de Local
		this.mainStaticLoadFeedXMLReader = new AsyncLoadLocalFeedXML(this,
				mContext);
		this.mainStaticLoadFeedXMLReader
				.execute(Defines.ReturnDataDatabases.DB_SETTINGS_FILE_NAME);
	}

	public void loadRemoteSettings() {
		this.mainStaticRemoteFeedXMLReader = null;
		this.mainStaticRemoteFeedXMLReader = new AsyncLoadRemoteFeedXML(this,
				mContext);
		this.mainStaticRemoteFeedXMLReader
				.execute(BuildConfig.URL_REMOTE_CONFIG_FILE);
	}

	/**
	 * @return the generalSettings
	 */
	public GeneralSettings getGeneralSettings() {
		return generalSettings;
	}

	/************ ASYNC LOCAL SETTINGS FEED READER *******************/

	@Override
	public void onLocalSuccessfulExecute(GeneralSettings generalSettings) {
		this.mainStaticLoadFeedXMLReader = null;
		boolean conexion = false;
		if (Reachability.isOnline(this.mContext)) {
			loadRemoteSettings();
			conexion = true;
		}
		if (generalSettings != null) {
			// Se elimina el listener Remoto
			if (this.mainStaticRemoteFeedXMLReader != null) {
				this.mainStaticRemoteFeedXMLReader.removeListener(this);
				this.mainStaticRemoteFeedXMLReader = null;
			}
			this.generalSettings = generalSettings;
			// He leido el plist y lo he cargardo en la DDBB
			
			//Actualizamos las competiciones de generalSettings para cargar las competiciones favoritas del usuario
			this.generalSettings.setCompetitions(getOrderedCompetitions());
			
			responseUpdateDabase();
		} else if (!conexion) {
			// // Error en local, y no hay conexion en remoto
			responseFailureRemoteConfig();
		}
	}

	@Override
	public void onLocalFailureExecute() {
		this.mainStaticLoadFeedXMLReader = null;
		// Si hay conexion, se carga en remoto
		if (Reachability.isOnline(this.mContext)) {
			loadRemoteSettings();
		} else {
			// Sino hay conexion, se lanza el error
			responseFailureRemoteConfig();
		}
	}

	/************ ASYNC REMOTE SETTINGS FEED READER *******************/
	@Override
	public void onRemoteSuccessfulExecute(GeneralSettings generalSettings) {
		this.mainStaticRemoteFeedXMLReader = null;
		if (generalSettings == null) {
			responseFailureRemoteConfig();
		} else {
			if (this.mainStaticRemoteFeedXMLReader != null) {
				this.mainStaticRemoteFeedXMLReader.removeListener(this);
				this.mainStaticRemoteFeedXMLReader = null;
			}
			this.generalSettings = generalSettings;
			
			//Actualizamos las competiciones de generalSettings para cargar las competiciones favoritas del usuario
			this.generalSettings.setCompetitions(getOrderedCompetitions());
			
			// He leido el plist y lo he cargardo en la DDBB
			responseUpdateDabase();
		}
	}

	@Override
	public void onRemoteFailureExecute() {
		this.mainStaticRemoteFeedXMLReader = null;
		// responseFailureRemoteConfig();
	}

	/************ ASYNC REMOTE SETTINGS FEED READER *******************/

	public ArrayList<Section> getOrderedSections(int competitionId) {
		ArrayList<Section> sections;
		if (!sectionsMap.containsKey(competitionId)) {
			Competition competition = generalSettings
					.getCompetition(competitionId);
			sections = competition.getSections();

			Collections.sort(sections, new SectionComparator());
			sectionsMap.put(competitionId, sections);
		} else {
			sections = sectionsMap.get(competitionId);
		}

		return sections;
	}

	public Section getSectionByType(int competitionId, String sectionType) {
		ArrayList<Section> sections = this.getOrderedSections(competitionId);
		for (Section section : sections) {
			if (section.getType().equalsIgnoreCase(sectionType)) {
				return section;
			}
		}
		return null;
	}

	public Section getDefaultSections(int competitionId) {
		ArrayList<Section> sections = this.getOrderedSections(competitionId);
		if (sections.size() > 0) {
			for (Section section : sections) {
				if (section.isStart()) {
					return section;
				}
			}
			return sections.get(0);
		} else {
			return null;
		}
	}

	public static void remove() {
		if (sInstance != null) {
			if (sInstance.mainStaticLoadFeedXMLReader != null) {
				sInstance.mainStaticLoadFeedXMLReader.cancel(true);
				sInstance.mainStaticLoadFeedXMLReader = null;
			}
			if (sInstance.mainStaticRemoteFeedXMLReader != null) {
				sInstance.mainStaticRemoteFeedXMLReader.cancel(true);
				sInstance.mainStaticRemoteFeedXMLReader = null;
			}
			sInstance.mContext = null;
			sInstance.generalSettings = null;
			if (sInstance.listeners != null) {
				sInstance.listeners.clear();
				sInstance.listeners = null;
			}
			if (sInstance.sectionsMap != null) {
				sInstance.sectionsMap.clear();
				sInstance.sectionsMap = null;
			}
			sInstance = null;
		}
	}

	public ArrayList<Competition> getOrderedCompetitions() {
		ArrayList<Competition> competitions ;
		competitions= this.generalSettings.getCompetitions();
		
		SharedPreferences prefs = mContext.getSharedPreferences(ReturnSharedPreferences.SP_ORDERCOMPETITION, Context.MODE_PRIVATE);
		
		if (prefs.contains(ReturnSharedPreferences.SP_ORDERCOMPETITION_ENABLED)) {
			String orderCompetitions = prefs.getString(ReturnSharedPreferences.SP_ORDERCOMPETITION_ORDER, null);
			Log.d(TAG, "Orden Custom: "+orderCompetitions);
			if (orderCompetitions!=null && orderCompetitions.length()>0) {
				int id;
				ArrayList<Competition> originalCompetitions = new ArrayList<Competition>() ;
				originalCompetitions.addAll(generalSettings.getCompetitions());
				ArrayList<Competition> newCompetitions = new ArrayList<Competition>() ;
				
				String[] comps = orderCompetitions.split(":");
				for (int i = 0; i < comps.length; i++) {
					id = Integer.valueOf(comps[i]);
					for (Competition competition : originalCompetitions) {
						if (id == competition.getId()) {
							newCompetitions.add(competition);
							originalCompetitions.remove(competition);
							break;
						}
					}
				}
				
				if (originalCompetitions.size()>0) {
					newCompetitions.addAll(originalCompetitions);
				}
				return newCompetitions;
			}
		}
			
		Collections.sort(competitions, new CompetitionComparator());
		return competitions;
	}

	public void updateOrderCompetition(ArrayList<Competition> competitions) {
		SharedPreferences prefs = mContext.getSharedPreferences(ReturnSharedPreferences.SP_ORDERCOMPETITION, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(ReturnSharedPreferences.SP_ORDERCOMPETITION_ENABLED, true);
		
		String order="";
		for (int i = 0; i < competitions.size(); i++) {
			order+=competitions.get(i).getId()+":";
		}
		editor.putString(ReturnSharedPreferences.SP_ORDERCOMPETITION_ORDER, order);
		editor.commit();
		
		this.generalSettings.setCompetitions(competitions);
	}

	public boolean isFirstTimeRun() {
		String orderedKey = ReturnSharedPreferences.SP_ORDERCOMPETITION;
		SharedPreferences prefs = mContext.getSharedPreferences(orderedKey, Context.MODE_PRIVATE);

		if (prefs.contains(ReturnSharedPreferences.SP_ORDERCOMPETITION_ENABLED)) {
			String orderCompetitions = prefs.getString(ReturnSharedPreferences.SP_ORDERCOMPETITION_ORDER, null);
			if (orderCompetitions!=null){
				return false;
			}
			Editor editor = prefs.edit();
			editor.remove(orderedKey);
			editor.commit();
		}

		return true;
	}


}
