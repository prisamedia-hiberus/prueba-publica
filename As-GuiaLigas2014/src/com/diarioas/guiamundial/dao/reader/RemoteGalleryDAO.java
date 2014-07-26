package com.diarioas.guiamundial.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guiamundial.dao.model.news.GalleryMediaItem;
import com.diarioas.guiamundial.dao.model.news.PhotoMediaItem;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadDetailGalleryXML;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadDetailGalleryXML.AsyncLoadDetailGalleryXMLListener;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadGalleryXML;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadGalleryXML.AsyncLoadGalleryXMLListener;
import com.diarioas.guiamundial.utils.Reachability;

public class RemoteGalleryDAO implements AsyncLoadGalleryXMLListener,
		AsyncLoadDetailGalleryXMLListener {

	public interface RemotePhotosDAOListener {

		void onSuccessRemoteconfig(ArrayList<GalleryMediaItem> galleries);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	public interface RemotePhotosDetailDAOListener {

		void onSuccessDetailRemoteconfig(GalleryMediaItem gallery);

		void onFailureDetailRemoteconfig();

		void onFailureDetailNotConnection();

	}

	private static RemoteGalleryDAO sInstance;
	private Context mContext;
	private ArrayList<RemotePhotosDAOListener> listeners;
	private ArrayList<RemotePhotosDetailDAOListener> detailListeners;
	private AsyncLoadGalleryXML mainStaticLoadPhotosXMLReader;
	private AsyncLoadDetailGalleryXML detailStaticLoadPhotosXMLReader;
	private int currentGallery;

	private static ArrayList<GalleryMediaItem> galleries;

	public static RemoteGalleryDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteGalleryDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemotePhotosDAOListener>();
			sInstance.detailListeners = new ArrayList<RemotePhotosDetailDAOListener>();

		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemotePhotosDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemotePhotosDAOListener listener) {
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

	private void responseSuccessRemoteConfig(
			ArrayList<GalleryMediaItem> galleries) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(galleries);
			}
		}
	}

	public void addDetailListener(RemotePhotosDetailDAOListener listener) {
		if (this.detailListeners != null) {
			this.detailListeners.add(listener);
		}

	}

	public void removeDetailListener(RemotePhotosDetailDAOListener listener) {
		if (this.detailListeners != null) {
			if (this.detailListeners.contains(listener)) {
				this.detailListeners.remove(listener);
			}
		}
	}

	private void responseDetailFailureRemoteConfig() {
		if (this.detailListeners != null) {
			for (int i = 0; i < this.detailListeners.size(); i++) {
				this.detailListeners.get(i).onFailureDetailRemoteconfig();
			}
		}
	}

	private void responseDetailNotConnection() {
		if (this.detailListeners != null) {
			for (int i = 0; i < this.detailListeners.size(); i++) {
				this.detailListeners.get(i).onFailureDetailNotConnection();
			}
		}
	}

	private void responseDetailSuccessRemoteConfig(GalleryMediaItem gallery) {
		if (this.detailListeners != null) {
			for (int i = 0; i < this.detailListeners.size(); i++) {
				this.detailListeners.get(i)
						.onSuccessDetailRemoteconfig(gallery);
			}
		}
	}

	/*****************************************************************/

	public void getGalleries(String url) {

		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(url);
		} else {
			responseNotConnection();
		}
	}

	private void loadMainSettings(String url) {
		this.mainStaticLoadPhotosXMLReader = new AsyncLoadGalleryXML(this,
				mContext);
		this.mainStaticLoadPhotosXMLReader.execute(url);
	}

	public void getDetailGallery(int position) {

		if (Reachability.isOnline(this.mContext)) {
			currentGallery = position;
			loadDetailSettings(galleries.get(position));
		} else {
			responseDetailNotConnection();
		}
	}

	private void loadDetailSettings(GalleryMediaItem gallery) {
		this.detailStaticLoadPhotosXMLReader = new AsyncLoadDetailGalleryXML(
				this, mContext);
		String url = gallery.getLink();
		if (url != null)
			url = gallery.getLink().replace(".html", ".ai.xml");

		this.detailStaticLoadPhotosXMLReader.execute(url);

	}

	public boolean isGalleryPreLoaded() {
		if (galleries != null && galleries.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isGalleryDetailPreLoaded(int numGallery) {
		if (galleries != null && galleries.size() > numGallery
				&& galleries.get(numGallery).getPhotos().size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public GalleryMediaItem getGalleryDetailPreloaded(int numGallery) {
		return galleries.get(numGallery);
	}

	public ArrayList<GalleryMediaItem> getGalleryPreloaded() {
		return galleries;
	}

	/**************** Metodos de AsyncLoadGalleryXMLListener *****************************/
	@Override
	public void onSuccessfulExecute(ArrayList<GalleryMediaItem> galItems) {
		galleries = galItems;
		responseSuccessRemoteConfig(galleries);
	}

	@Override
	public void onFailureExecute() {
		responseFailureRemoteConfig();
	}

	/**************** Metodos de AsyncLoadGalleryXMLListener *****************************/
	/**************** Metodos de AsyncLoadDetailGalleryXMLListener *****************************/
	@Override
	public void onSuccessDetailfulExecute(ArrayList<PhotoMediaItem> photos) {
		GalleryMediaItem gallery = galleries.get(currentGallery);
		gallery.setPhotos(photos);
		responseDetailSuccessRemoteConfig(gallery);
	}

	@Override
	public void onFailureDetailExecute() {
		responseDetailFailureRemoteConfig();
	}
	/**************** Metodos de AsyncLoadDetailGalleryXMLListener *****************************/

}
