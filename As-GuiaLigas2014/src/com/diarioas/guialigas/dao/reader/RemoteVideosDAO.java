package com.diarioas.guialigas.dao.reader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.diarioas.guialigas.dao.model.video.VideoItem;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadVideosXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadVideosXML.AsyncLoadVideosXMLListener;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Reachability;

public class RemoteVideosDAO implements AsyncLoadVideosXMLListener {

	public interface RemoteVideosDAOListener {

		void onSuccessRemoteconfig(ArrayList<VideoItem> videos);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	private static RemoteVideosDAO sInstance;
	private Context mContext;
	private ArrayList<RemoteVideosDAOListener> listeners;
	private AsyncLoadVideosXML mainStaticLoadVideosXMLReader;
	private String dateVideoUpdated;
	private static ArrayList<VideoItem> videos;

	public static RemoteVideosDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteVideosDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteVideosDAOListener>();

		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemoteVideosDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteVideosDAOListener listener) {
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

	private void responseSuccessRemoteConfig(ArrayList<VideoItem> videos) {

		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(videos);
			}
		}
	}

	/*****************************************************************/

	public void getVideosInfo(String url) {

		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(url);
		} else {
			responseNotConnection();
		}
	}

	private void loadMainSettings(String url) {
		this.mainStaticLoadVideosXMLReader = new AsyncLoadVideosXML(this,
				mContext);
		this.mainStaticLoadVideosXMLReader.execute(url);
	}

	public boolean isVideosLoaded() {
		if (videos != null && videos.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<VideoItem> getVideosPreloaded() {
		return videos;
	}

	public String getDateVideoUpdated() {
		return dateVideoUpdated;
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/
	@Override
	public void onSuccessfulExecute(ArrayList<VideoItem> videos) {
		sInstance.videos = videos;
		SimpleDateFormat dateFormatPull = new SimpleDateFormat(
				DateFormat.PULL_FORMAT, Locale.getDefault());
		dateVideoUpdated = dateFormatPull.format(new Date());
		responseSuccessRemoteConfig(videos);
	}

	@Override
	public void onFailureExecute() {
		responseFailureRemoteConfig();
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/

}
