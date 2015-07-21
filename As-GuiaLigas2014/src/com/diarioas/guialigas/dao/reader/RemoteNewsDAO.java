package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guialigas.dao.model.news.NewsItem;
import com.diarioas.guialigas.dao.model.news.SectionNewsTagSections;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadNewsXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadNewsXML.AsyncLoadNewsXMLListener;
import com.diarioas.guialigas.dao.reader.async.AsyncTagReader;
import com.diarioas.guialigas.dao.reader.async.AsyncTagReader.AsyncTagReaderListener;
import com.diarioas.guialigas.utils.Defines.Prefix;
import com.diarioas.guialigas.utils.Reachability;

public class RemoteNewsDAO implements AsyncTagReaderListener, AsyncLoadNewsXMLListener {

	public static interface NewsTag {
		public static final String urlTags = "http://as.com/tag/listado/G3n3r4_json_qu3ry.pl?tag=";
		public static final String first_string = "http://as.com/tag/";
		public static final String second_string = "/";
		public static final String as_domain = "as.com/";
		public static final String as_domain_mobile = "as.com/m/";
	}

	
	public interface RemoteNewsDAOListener {
		void onSuccessRemoteconfig(ArrayList<NewsItem> news);

		void onFailureRemoteconfig();

		void onFailureNotConnectionRemoteconfig();
	}
	
	public interface RemoteNewsTagDAOListener {
		void onSuccessTagRemoteconfig(SectionNewsTagSections results);

		void onFailureTagRemoteconfig();

		void onFailureTagNotConnectionRemoteconfig();
	}

	private static RemoteNewsDAO sInstance = null;
	private Context mContext = null;
	private AsyncTagReader asynSettingsReader;
	private AsyncLoadNewsXML asyncNewsReader;
	private ArrayList<RemoteNewsDAOListener> listeners;
	private ArrayList<RemoteNewsTagDAOListener> tagListeners;
	private ArrayList<NewsItem> news;

	public static RemoteNewsDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteNewsDAO();
			sInstance.mContext = ctx;
			sInstance.tagListeners = new ArrayList<RemoteNewsTagDAOListener>();
			sInstance.listeners = new ArrayList<RemoteNewsDAOListener>();

		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addTagListener(RemoteNewsTagDAOListener listener) {
		if (this.tagListeners != null) {
			this.tagListeners.clear();
			this.tagListeners.add(listener);
		}
	}

	public void removeTagListener(RemoteNewsTagDAOListener listener) {
		if (this.tagListeners != null) {
			if (sInstance.tagListeners.contains(listener)) {
				sInstance.tagListeners.remove(listener);
			}
		}
	}

	private void responseNotConnectionRemoteConfigForTag() {
		if (this.tagListeners != null) {
			for (int i = 0; i < this.tagListeners.size(); i++) {
				this.tagListeners.get(i).onFailureTagNotConnectionRemoteconfig();
			}
		}
	}
	
	public void addListener(RemoteNewsDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteNewsDAOListener listener) {
		if (this.listeners != null) {
			if (this.listeners.contains(listener)) {
				this.listeners.remove(listener);
			}
		}
	}

	private void responseNotConnectionRemoteConfig() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureNotConnectionRemoteconfig();
			}
		}
	}

	/******************************************************************************/

	public void loadTagData(String url) {
		if (Reachability.isOnline(mContext) == true) {
			if (!url.startsWith("http://") && !url.startsWith("https://")) {

				if (RemoteDataDAO.getInstance(mContext) != null
						&& RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings() != null
						&& RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings().getPrefix() != null
						&& RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings().getPrefix()
								.get(Prefix.PREFIX_DATA_RSS) != null)
					url = RemoteDataDAO.getInstance(mContext)
							.getGeneralSettings().getPrefix()
							.get(Prefix.PREFIX_DATA_RSS)
							+ url;

			}
			this.asynSettingsReader = new AsyncTagReader(this, mContext);
			this.asynSettingsReader.execute(url);
		} else {
			responseNotConnectionRemoteConfigForTag();
		}

	}

	/********* AsyncSettingsReaderListener methods ************/

	@Override
	public void onSuccessfulTagExecute(SectionNewsTagSections results) {
		if (this.tagListeners != null) {
			for (int i = 0; i < this.tagListeners.size(); i++) {
				this.tagListeners.get(i).onSuccessTagRemoteconfig(results);
			}
		}
	}

	@Override
	public void onFailureTagExecute() {
		if (this.tagListeners != null) {
			for (int i = 0; i < this.tagListeners.size(); i++) {
				this.tagListeners.get(i).onFailureTagRemoteconfig();
			}
		}
	}
	/******************************************************************************/

	public void loadData(String url) {
		if (Reachability.isOnline(mContext) == true) {
			if (!url.startsWith("http://") && !url.startsWith("https://")) {

				if (RemoteDataDAO.getInstance(mContext) != null
						&& RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings() != null
						&& RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings().getPrefix() != null
						&& RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings().getPrefix()
								.get(Prefix.PREFIX_DATA_RSS) != null)
					url = RemoteDataDAO.getInstance(mContext)
							.getGeneralSettings().getPrefix()
							.get(Prefix.PREFIX_DATA_RSS)
							+ url;

			}
			this.asyncNewsReader = new AsyncLoadNewsXML(this, mContext);
			this.asyncNewsReader.execute(url);
		} else {
			responseNotConnectionRemoteConfig();
		}

	}
	

	public boolean isURLNeedOut(String urlOut) {
		if (urlOut == null || urlOut.length() <= 0) {
			return true;
		}

		if (urlOut.contains(NewsTag.as_domain)) {
			return false;
		} else {
			return true;
		}
	}

	public NewsItem getNewsPreloaded(int idCompetition, int position) {
		return getNewsPreloaded(idCompetition).get(position);

	}
	
	public ArrayList<NewsItem> getNewsPreloaded(int idCompetition) {
		if (news == null || news.size() == 0) {
			news = DatabaseDAO.getInstance(mContext).getNews(idCompetition);
		}
		return news;
	}
	
	@Override
	public void onSuccessfulExecute(ArrayList<NewsItem> news) {
		this.news = news;
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(news);
			}
		}
	}

	@Override
	public void onFailureExecute() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureRemoteconfig();
			}
		}		
	}


}
