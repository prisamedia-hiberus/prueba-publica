package com.diarioas.guiamundial.dao.reader;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.diarioas.guiamundial.dao.model.palmares.Palmares;
import com.diarioas.guiamundial.dao.model.stadium.Stadium;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadPalmaresXML;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadPalmaresXML.AsyncLoadPalmaresXMLListener;
import com.diarioas.guiamundial.utils.Reachability;

public class RemotePalmaresDAO implements AsyncLoadPalmaresXMLListener {

	public interface RemotePalmaresDAOListener {

		void onSuccessRemoteconfig(ArrayList<Palmares> palmares);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	private static RemotePalmaresDAO sInstance;
	private Context mContext;
	private ArrayList<RemotePalmaresDAOListener> listeners;
	private AsyncLoadPalmaresXML mainStaticLoadPalmaresXMLReader;
	private String currentCompetition;
	private static HashMap<String, ArrayList<Palmares>> palmares;

	public static RemotePalmaresDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemotePalmaresDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemotePalmaresDAOListener>();
			sInstance.palmares = new HashMap<String, ArrayList<Palmares>>();

		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemotePalmaresDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemotePalmaresDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
	}

	private void responseFailureRemoteConfig() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureRemoteconfig();
			}
		}
	}

	private void responseNotConnection() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureNotConnection();
			}
		}
	}

	private void responseSuccessRemoteConfig(ArrayList<Palmares> palmares) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(palmares);
			}
		}
	}

	/*****************************************************************/

	public void getPalmaresInfo(String url, String competitionId) {
		if (Reachability.isOnline(this.mContext)) {
			currentCompetition = competitionId;
			loadMainSettings(url);
		} else {
			responseNotConnection();
		}
	}

	private void loadMainSettings(String url) {

		this.mainStaticLoadPalmaresXMLReader = new AsyncLoadPalmaresXML(this,
				mContext);
		this.mainStaticLoadPalmaresXMLReader.execute(url);
	}

	public boolean isPalmaresLoaded(String competitionId) {
		if (palmares.containsKey(competitionId) && palmares.get(competitionId).size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<Palmares> getPalmaresPreloaded(String competitionId) {
		return palmares.get(competitionId);
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/
	@Override
	public void onSuccessfulExecute(ArrayList<Palmares> palmares) {
		sInstance.palmares.put(currentCompetition, palmares);
		responseSuccessRemoteConfig(palmares);
	}

	@Override
	public void onFailureExecute() {
		responseFailureRemoteConfig();
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/

}
