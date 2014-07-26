package com.diarioas.guiamundial.dao.reader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;

import com.diarioas.guiamundial.dao.model.news.NewsItem;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadNewsXML;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadNewsXML.AsyncLoadNewsXMLListener;
import com.diarioas.guiamundial.utils.Defines.DateFormat;
import com.diarioas.guiamundial.utils.Defines.ReturnSharedPreferences;
import com.diarioas.guiamundial.utils.Reachability;

public class RemoteNewsDAO implements AsyncLoadNewsXMLListener {

	public interface RemoteNewsDAOListener {

		void onSuccessRemoteconfig(ArrayList<NewsItem> videos);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	private static RemoteNewsDAO sInstance;
	private Context mContext;
	private ArrayList<RemoteNewsDAOListener> listeners;
	private AsyncLoadNewsXML mainStaticLoadVideosXMLReader;
	private static ArrayList<NewsItem> news;

	public static RemoteNewsDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteNewsDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteNewsDAOListener>();

		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemoteNewsDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteNewsDAOListener listener) {
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

	private void responseSuccessRemoteConfig(ArrayList<NewsItem> videos) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(videos);
			}
		}
	}

	/*****************************************************************/

	public void getNewsInfo(String url) {

		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(url);
		} else {
			responseNotConnection();
		}
	}

	private void loadMainSettings(String url) {
		if (mainStaticLoadVideosXMLReader != null) {
			mainStaticLoadVideosXMLReader.cancel(true);
			mainStaticLoadVideosXMLReader = null;
		}
		this.mainStaticLoadVideosXMLReader = new AsyncLoadNewsXML(this,
				mContext);
		this.mainStaticLoadVideosXMLReader.execute(url);

	}

	public boolean isNewsLoaded(int idCompetition) {

		if (getNewsPreloaded(idCompetition) != null
				&& getNewsPreloaded(idCompetition).size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<NewsItem> getNewsPreloaded(int idCompetition) {
		if (news == null || news.size() == 0) {
			news = DatabaseDAO.getInstance(mContext).getNews(idCompetition);
		}
		return news;
	}

	public NewsItem getNewsPreloaded(int idCompetition, int position) {
		return getNewsPreloaded(idCompetition).get(position);

	}

	public String getNewsDate() {
		SharedPreferences sp = mContext.getSharedPreferences(
				ReturnSharedPreferences.SP_NAME, Context.MODE_PRIVATE);
		SimpleDateFormat dateFormatPull = new SimpleDateFormat(
				DateFormat.PULL_FORMAT, Locale.getDefault());
		float date = sp.getFloat(ReturnSharedPreferences.SP_NEWSDATE, 0);

		return dateFormatPull.format(date * 1000);

	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/
	@Override
	public void onSuccessfulExecute(ArrayList<NewsItem> videos) {
		news = videos;
		responseSuccessRemoteConfig(videos);
	}

	@Override
	public void onFailureExecute() {
		responseFailureRemoteConfig();
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener *****************************/

}
