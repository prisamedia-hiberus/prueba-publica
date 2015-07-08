package com.diarioas.guialigas.dao.reader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.diarioas.guialigas.dao.model.video.VideoItem;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadVideosXML;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadVideosXML.AsyncLoadVideosXMLListener;
import com.diarioas.guialigas.dao.reader.async.AsyncVideoGeoBlockJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncVideoGeoBlockJSON.AsyncVideoGeoBlockJSONListener;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Defines.Prefix;
import com.diarioas.guialigas.utils.Reachability;

public class RemoteVideosDAO implements AsyncLoadVideosXMLListener,
		AsyncVideoGeoBlockJSONListener {

	public interface RemoteVideosDAOListener {
		void onSuccessRemoteVideos(ArrayList<VideoItem> videos);

		void onFailureRemoteVideos();
	}

	public interface RemoteVideosDAOVideoGeoBlock {
		void onSuccessVideoGeoBlock(String urlVideo);

		void onFailureVideoGeoBlock();
	}

	private static RemoteVideosDAO sInstance;
	private Context mContext;
	private ArrayList<RemoteVideosDAOListener> listeners;
	private ArrayList<RemoteVideosDAOVideoGeoBlock> listenersVideoBlock = null;
	private AsyncLoadVideosXML mainStaticLoadVideosXMLReader;
	private String dateVideoUpdated;
	private ArrayList<VideoItem> videos;
	private AsyncVideoGeoBlockJSON asyncVideoGeoBlockJSON = null;

	public static RemoteVideosDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteVideosDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteVideosDAOListener>();
			sInstance.listenersVideoBlock = new ArrayList<RemoteVideosDAOVideoGeoBlock>();

		}
		return sInstance;
	}

	/********************** Listeners methods ******************************/
	/*************************************************************************/

	public void addVideosListener(RemoteVideosDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeVideosListener(RemoteVideosDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
	}

	private void responseRemoteVideosFailure() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureRemoteVideos();
			}
		}
	}

	private void responseRemoteVideosSuccess(ArrayList<VideoItem> videos) {

		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteVideos(videos);
			}
		}
	}

	/************************ Public methods ***************************/
	/******************************************************************/

	public void getVideosInfo(String url) {

		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(url);
		} else {
			responseRemoteVideosFailure();
		}
	}

	public void cancelCall() {
		if (this.mainStaticLoadVideosXMLReader != null) {
			if (this.listeners != null) {
				this.listeners.clear();

				this.mainStaticLoadVideosXMLReader.cancel(true);
				this.mainStaticLoadVideosXMLReader = null;
			}

		}

	}

	public boolean isVideosLoaded() {
		if (this.videos != null && this.videos.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<VideoItem> getVideosPreloaded() {
		return this.videos;
	}

	public String getDateVideoUpdated() {
		return this.dateVideoUpdated;
	}

	/************************ Private methods ***************************/
	/******************************************************************/

	private void loadMainSettings(String url) {
		this.mainStaticLoadVideosXMLReader = new AsyncLoadVideosXML(this,
				mContext);
		this.mainStaticLoadVideosXMLReader.execute(url);
	}

	/**************** Metodos de AsyncLoadStadiumXMLListener ***************************/
	/***********************************************************************************/

	@Override
	public void onSuccessfulVideosExecute(ArrayList<VideoItem> videos) {
		this.videos = videos;
		SimpleDateFormat dateFormatPull = new SimpleDateFormat(
				DateFormat.PULL_FORMAT, Locale.getDefault());
		this.dateVideoUpdated = dateFormatPull.format(new Date());
		responseRemoteVideosSuccess(videos);
	}

	@Override
	public void onFailureVideosExecute() {
		responseRemoteVideosFailure();
	}

	/******************* Geo Block Methods ********************/

	public void requestVideoBlock(String url) {

		if (Reachability.isOnline(this.mContext)) {
			if (this.asyncVideoGeoBlockJSON != null) {
				this.asyncVideoGeoBlockJSON.cancel(true);
				this.asyncVideoGeoBlockJSON = null;
			}

			this.asyncVideoGeoBlockJSON = new AsyncVideoGeoBlockJSON(this,
					mContext);
			
			String prefixGeo = "";
			
			if (RemoteDataDAO.getInstance(mContext) != null
					&& RemoteDataDAO.getInstance(mContext).getGeneralSettings() != null
					&& RemoteDataDAO.getInstance(mContext).getGeneralSettings()
							.getPrefix() != null
					&& RemoteDataDAO.getInstance(mContext).getGeneralSettings()
							.getPrefix().get(Prefix.PREFIX_GEO_VIDEO) != null) {
				prefixGeo = RemoteDataDAO.getInstance(mContext).getGeneralSettings()
						.getPrefix().get(Prefix.PREFIX_GEO_VIDEO);
			}
			
			this.asyncVideoGeoBlockJSON
					.execute(prefixGeo + url);
		} else {
			responseFailureVideoBlock();
		}
	}

	public void addVideoBlockListener(RemoteVideosDAOVideoGeoBlock listener) {
		if (this.listenersVideoBlock != null) {
			this.listenersVideoBlock.clear();
		}
		this.listenersVideoBlock.add(listener);
	}

	public void removeVideoBlockListener(RemoteVideosDAOVideoGeoBlock listener) {
		if (this.listenersVideoBlock != null) {
			if (sInstance.listenersVideoBlock.contains(listener)) {
				sInstance.listenersVideoBlock.remove(listener);
			}
		}
	}

	private void responseFailureVideoBlock() {
		if (this.listenersVideoBlock != null) {
			for (int i = 0; i < this.listenersVideoBlock.size(); i++) {
				this.listenersVideoBlock.get(i).onFailureVideoGeoBlock();
			}
		}
	}

	private void responseSuccessVideoBlock(String url) {
		for (int i = 0; i < this.listenersVideoBlock.size(); i++) {
			this.listenersVideoBlock.get(i).onSuccessVideoGeoBlock(url);
		}
	}

	/******************* AsyncVideoGeoBlockJSONListener methods *********************/

	@Override
	public void onSuccessfulVideoGeoBlock(String finalURL, String url) {

		if (RemoteDataDAO.getInstance(mContext) != null
				&& RemoteDataDAO.getInstance(mContext).getGeneralSettings() != null
				&& RemoteDataDAO.getInstance(mContext).getGeneralSettings()
						.getPrefix() != null
				&& RemoteDataDAO.getInstance(mContext).getGeneralSettings()
						.getPrefix().get(Prefix.PREFIX_VIDEO) != null) {
			responseSuccessVideoBlock(RemoteDataDAO.getInstance(mContext)
					.getGeneralSettings().getPrefix().get(Prefix.PREFIX_VIDEO)
					+ finalURL);
		} else {
			responseSuccessVideoBlock(finalURL);
		}
	}

	@Override
	public void onFailureVideoGeoBlock(String url) {
		responseFailureVideoBlock();
	}
}
