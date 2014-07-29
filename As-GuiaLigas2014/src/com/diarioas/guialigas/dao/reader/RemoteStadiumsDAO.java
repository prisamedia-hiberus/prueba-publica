package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.diarioas.guialigas.dao.model.stadium.Stadium;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadStadiumDetailXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadStadiumDetailXML.AsyncLoadStadiumDetailXMLListener;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadStadiumsXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadStadiumsXML.AsyncLoadStadiumXMLListener;
import com.diarioas.guialigas.utils.Defines.STADIUM_IMAGE_TYPE;
import com.diarioas.guialigas.utils.Reachability;

public class RemoteStadiumsDAO implements AsyncLoadStadiumXMLListener,
		AsyncLoadStadiumDetailXMLListener {

	public interface RemoteStadiumsDAOListener {

		void onSuccessRemoteconfig(ArrayList<Stadium> stadiums);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	public interface RemoteStadiumDetailDAOListener {

		void onSuccessRemoteconfig(Stadium stadium);

		void onFailureRemoteconfig(Stadium stadium);

		void onFailureNotConnection(Stadium stadium);

	}

	private static RemoteStadiumsDAO sInstance;
	private Context mContext;
	private ArrayList<RemoteStadiumsDAOListener> listeners;
	private ArrayList<RemoteStadiumDetailDAOListener> listenersDetail;
	private AsyncLoadStadiumsXML mainStaticLoadStadiumsJSONReader;
	private AsyncLoadStadiumDetailXML detailStaticLoadStadiumJSONReader;
	private String currentCompetition;

	private static HashMap<String, ArrayList<Stadium>> stadiums;

	public static RemoteStadiumsDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteStadiumsDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteStadiumsDAOListener>();
			sInstance.listenersDetail = new ArrayList<RemoteStadiumDetailDAOListener>();
			sInstance.stadiums = new HashMap<String, ArrayList<Stadium>>();

		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemoteStadiumsDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteStadiumsDAOListener listener) {
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

	private void responseSuccessRemoteConfig(ArrayList<Stadium> stadiums) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(stadiums);
			}
		}
	}

	public void addDetailListener(RemoteStadiumDetailDAOListener listener) {
		if (this.listenersDetail != null) {
			this.listenersDetail.clear();
			this.listenersDetail.add(listener);
		}
	}

	public void removeDetailListener(RemoteStadiumDetailDAOListener listener) {
		if (this.listenersDetail != null) {
			if (this.listenersDetail.contains(listener)) {
				this.listenersDetail.remove(listener);
			}
		}
	}

	private void responseFailureRemoteConfigDetail(Stadium stadium) {
		if (this.listenersDetail != null) {
			for (int i = 0; i < this.listenersDetail.size(); i++) {
				this.listenersDetail.get(i).onFailureRemoteconfig(stadium);
			}
		}
	}

	private void responseNotConnectionDetail(Stadium stadium) {
		if (this.listenersDetail != null) {
			for (int i = 0; i < this.listenersDetail.size(); i++) {
				this.listenersDetail.get(i).onFailureNotConnection(stadium);
			}
		}
	}

	private void responseSuccessRemoteConfigDetail(Stadium stadium) {
		if (this.listenersDetail != null) {
			for (int i = 0; i < this.listenersDetail.size(); i++) {
				this.listenersDetail.get(i).onSuccessRemoteconfig(stadium);
			}
		}
	}

	/*****************************************************************/

	public void getStadiumsInfo(String competitionId, String url) {
		if (Reachability.isOnline(this.mContext)) {
			currentCompetition = competitionId;
			loadMainSettings(url);
		} else {
			responseNotConnection();
		}
	}

	private void loadMainSettings(String url) {
		Log.d("REMOTESTADIUMSDAO", "PIDIENDO ESTADIOS");
		this.mainStaticLoadStadiumsJSONReader = new AsyncLoadStadiumsXML(this,
				mContext);
		this.mainStaticLoadStadiumsJSONReader.execute(url);
	}

	public void getStadiumData(int id) {
		Stadium stadium = DatabaseDAO.getInstance(mContext).getStadium(id);
		if (Reachability.isOnline(this.mContext)) {
			loadDetailSettings(stadium);
		} else {
			stadium = reloadStadiumFromDatabase(stadium);
			responseNotConnectionDetail(stadium);
		}
	}

	private void loadDetailSettings(Stadium stadium) {
		this.detailStaticLoadStadiumJSONReader = new AsyncLoadStadiumDetailXML(
				this, mContext);
		this.detailStaticLoadStadiumJSONReader.execute(stadium);

	}

	private Stadium reloadStadiumFromDatabase(Stadium stadium) {
		stadium = DatabaseDAO.getInstance(mContext).getStadium(stadium.getId());
		stadium.setStadiumPhotos(DatabaseDAO.getInstance(mContext)
				.getPhotoStadium(stadium.getId(),
						STADIUM_IMAGE_TYPE.TYPE_STADIUM));
		stadium.setCityPhotos(DatabaseDAO.getInstance(mContext)
				.getPhotoStadium(stadium.getId(), STADIUM_IMAGE_TYPE.TYPE_CITY));
		return stadium;
	}

	public boolean isStadiumsPreLoaded(String competitionId) {
		if (stadiums.containsKey(competitionId)
				&& stadiums.get(competitionId).size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<Stadium> getStadiumsPreLoaded(String competitionId) {
		// TODO Auto-generated method stub
		return stadiums.get(competitionId);
	}

	public boolean isStadiumsDeepPreLoaded(String competitioId) {
		if (isStadiumsPreLoaded(competitioId))
			return true;
		else {
			ArrayList<Stadium> stad = DatabaseDAO.getInstance(mContext)
					.getStadiums(competitioId);
			stadiums.put(competitioId, stad);
			if (stadiums != null && stadiums.size() > 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/
	@Override
	public void onSuccessfulExecute(ArrayList<Stadium> stadiums) {
		this.stadiums.put(currentCompetition, stadiums);
		DatabaseDAO.getInstance(mContext).updateStaticStadiums(
				currentCompetition, stadiums);
		responseSuccessRemoteConfig(stadiums);
	}

	@Override
	public void onFailureExecute() {
		responseFailureRemoteConfig();
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/

	@Override
	public void onSuccessfulDetailExecute(Stadium stadium) {
		DatabaseDAO.getInstance(mContext).updateStatiStadium(stadium);
		responseSuccessRemoteConfigDetail(stadium);
	}

	@Override
	public void onFailureDetailExecute(Stadium stadium) {
		stadium = reloadStadiumFromDatabase(stadium);
		responseFailureRemoteConfigDetail(stadium);

	}

}
