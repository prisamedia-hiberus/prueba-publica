/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.model.calendar.Match;
import com.diarioas.guialigas.dao.model.carrusel.GameSystem;
import com.diarioas.guialigas.dao.model.carrusel.ItemDirecto;
import com.diarioas.guialigas.dao.model.carrusel.SpadeInfo;
import com.diarioas.guialigas.dao.model.carrusel.StatsInfo;
import com.diarioas.guialigas.dao.reader.async.AsyncDirectoCarruselJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncDirectoCarruselJSON.AsyncDirectoCarruselJSONListener;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadDetailCarruselJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadDetailCarruselJSON.AsyncLoadDetailCarruselJSONListener;
import com.diarioas.guialigas.dao.reader.async.AsyncPlantillaCarruselJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncPlantillaCarruselJSON.AsyncPlantillaCarruselJSONListener;
import com.diarioas.guialigas.dao.reader.async.AsyncSpadesCarruselJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncSpadesCarruselJSON.AsyncSpadesCarruselJSONListener;
import com.diarioas.guialigas.dao.reader.async.AsyncStaticLoadCarruselJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncStaticLoadCarruselJSON.AsyncStaticLoadCarruselJSONListener;
import com.diarioas.guialigas.dao.reader.async.AsyncStatsCarruselJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncStatsCarruselJSON.AsyncStatsCarruselJSONListener;
import com.diarioas.guialigas.utils.Defines.CarruselDetail;
import com.diarioas.guialigas.utils.Reachability;

/**
 * @author robertosanchez
 * 
 */
public class CarruselDAO implements AsyncStaticLoadCarruselJSONListener,
		AsyncLoadDetailCarruselJSONListener, AsyncSpadesCarruselJSONListener,
		AsyncStatsCarruselJSONListener, AsyncDirectoCarruselJSONListener,
		AsyncPlantillaCarruselJSONListener {

	public interface CarruselDAOListener {

		void onSuccessCarrusel(Fase fase);

		void onFailureCarrusel();

		void onFailureNotConnection();

	}

	public interface CarruselDAODetailListener {

		void onSuccessDetailCarrusel(Match match);

		void onFailureDetailCarrusel();

		void onFailureDetailNotConnection();

	}

	public interface CarruselDAOSpadesListener {

		void onSuccessSpades(SpadeInfo spades);

		void onFailureSpades();

		void onFailureNotConnection();

	}

	public interface CarruselDAOStatsListener {

		void onSuccessStats(ArrayList<StatsInfo> stats);

		void onFailureStats();

		void onFailureNotConnection();

	}

	public interface CarruselDAODirectoListener {

		void onSuccessDirecto(ArrayList<ItemDirecto> directos);

		void onFailureDirecto();

		void onFailureDirectoNotConnection();

	}

	public interface CarruselDAOGameSystemListener {

		void onSuccessGameSystem(GameSystem gameSystem);

		void onFailureGameSystem();

		void onFailureGameSystemNotConnection();

	}

	private static CarruselDAO sInstance;
	private Context mContext;
	private ArrayList<CarruselDAOListener> listeners;
	private ArrayList<CarruselDAODetailListener> detailListeners;
	private ArrayList<CarruselDAOSpadesListener> spadesListeners;
	private ArrayList<CarruselDAOStatsListener> statsListeners;
	private ArrayList<CarruselDAODirectoListener> directoListeners;
	private ArrayList<CarruselDAOGameSystemListener> gameSystemListeners;
	private AsyncStaticLoadCarruselJSON mainStaticLoadCarruselJSONReader;
	private AsyncLoadDetailCarruselJSON loadCarruselDetailJSONReader;
	private AsyncSpadesCarruselJSON spadesCarruselAsyncTask;
	private AsyncStatsCarruselJSON statsCarruselAsyncTask;
	private AsyncPlantillaCarruselJSON plantillaCarruselAsyncTask;
	private AsyncDirectoCarruselJSON directoCarruselAsyncTask;
	// private String currentUrl;

	private static HashMap<String, Object> map;
	private static HashMap<String, String> urlMap;

	public static CarruselDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new CarruselDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<CarruselDAOListener>();
			sInstance.detailListeners = new ArrayList<CarruselDAODetailListener>();
			sInstance.spadesListeners = new ArrayList<CarruselDAOSpadesListener>();
			sInstance.statsListeners = new ArrayList<CarruselDAOStatsListener>();
			sInstance.directoListeners = new ArrayList<CarruselDAODirectoListener>();
			sInstance.gameSystemListeners = new ArrayList<CarruselDAOGameSystemListener>();
			map = new HashMap<String, Object>();
			urlMap = new HashMap<String, String>();
		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(CarruselDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(CarruselDAOListener listener) {
		if (this.listeners != null && sInstance.listeners.contains(listener)) {
			sInstance.listeners.remove(listener);
			if (sInstance.listeners.size() == 0
					&& mainStaticLoadCarruselJSONReader != null) {
				mainStaticLoadCarruselJSONReader.cancel(true);
				mainStaticLoadCarruselJSONReader = null;
			}
		}

	}

	private void responseFailureRemoteConfig() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureCarrusel();
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

	private void responseUpdateDabase(Fase fase) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessCarrusel(fase);
			}
		}
	}

	/********************** Gestion de DetailListeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addDetailListener(CarruselDAODetailListener listener) {
		if (this.detailListeners != null) {
			this.detailListeners.clear();
			this.detailListeners.add(listener);
		}
	}

	public void removeDetailListener(CarruselDAODetailListener listener) {
		if (this.detailListeners != null
				&& sInstance.detailListeners.contains(listener)) {
			sInstance.detailListeners.remove(listener);
			if (sInstance.detailListeners.size() == 0
					&& loadCarruselDetailJSONReader != null) {
				loadCarruselDetailJSONReader.cancel(true);
				loadCarruselDetailJSONReader = null;
			}
		}

	}

	private void responseFailureDetailNotConnection() {
		if (this.detailListeners != null) {
			for (int i = 0; i < this.detailListeners.size(); i++) {
				this.detailListeners.get(i).onFailureDetailNotConnection();
			}
		}

	}

	/******************************************************************************/
	/********************** Gestion de Listeners Spades ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addSpadesListener(CarruselDAOSpadesListener listener) {
		if (this.spadesListeners != null) {
			this.spadesListeners.clear();
			this.spadesListeners.add(listener);
		}
	}

	public void removeSpadesListener(CarruselDAOSpadesListener listener) {
		if (this.spadesListeners != null
				&& sInstance.spadesListeners.contains(listener)) {
			sInstance.spadesListeners.remove(listener);

			if (sInstance.spadesListeners.size() == 0
					&& spadesCarruselAsyncTask != null) {
				spadesCarruselAsyncTask.cancel(true);
				spadesCarruselAsyncTask = null;
			}
		}

	}

	private void responseNotConnectionSpades() {
		if (this.spadesListeners != null) {
			for (int i = 0; i < this.spadesListeners.size(); i++) {
				this.spadesListeners.get(i).onFailureNotConnection();
			}
		}

	}

	/******************************************************************************/
	/********************** Gestion de Listeners Stats ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addStatsListener(CarruselDAOStatsListener listener) {
		if (this.statsListeners != null) {
			this.statsListeners.clear();
			this.statsListeners.add(listener);
		}
	}

	public void removeStatsListener(CarruselDAOStatsListener listener) {
		if (sInstance.statsListeners != null
				&& sInstance.statsListeners.contains(listener)) {
			sInstance.statsListeners.remove(listener);
			if (sInstance.statsListeners.size() == 0
					&& statsCarruselAsyncTask != null) {
				statsCarruselAsyncTask.cancel(true);
				statsCarruselAsyncTask = null;
			}
		}

	}

	private void responseNotConnectionStats() {
		if (this.statsListeners != null) {
			for (int i = 0; i < this.statsListeners.size(); i++) {
				this.statsListeners.get(i).onFailureNotConnection();
			}
		}

	}

	/******************************************************************************/
	/********************** Gestion de Listeners Directos ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addDirectoListener(CarruselDAODirectoListener listener) {
		if (this.directoListeners != null) {
			this.directoListeners.clear();
			this.directoListeners.add(listener);
		}
	}

	public void removeDirectoListener(CarruselDAODirectoListener listener) {
		if (this.directoListeners != null
				&& sInstance.directoListeners.contains(listener)) {
			sInstance.directoListeners.remove(listener);

			if (sInstance.directoListeners.size() == 0
					&& directoCarruselAsyncTask != null) {
				directoCarruselAsyncTask.cancel(true);
				directoCarruselAsyncTask = null;
			}
		}

	}

	private void responseDirectoNotConnection() {
		if (this.directoListeners != null) {
			for (int i = 0; i < this.directoListeners.size(); i++) {
				this.directoListeners.get(i).onFailureDirectoNotConnection();
			}
		}

	}

	/******************************************************************************/
	/********************** Gestion de Listeners Plantilla ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addGameSystemListener(CarruselDAOGameSystemListener listener) {
		if (this.gameSystemListeners != null) {
			this.gameSystemListeners.clear();
			this.gameSystemListeners.add(listener);
		}
	}

	public void removeGameSystemListener(CarruselDAOGameSystemListener listener) {
		if (this.gameSystemListeners != null
				&& sInstance.gameSystemListeners.contains(listener)) {
			sInstance.gameSystemListeners.remove(listener);

			if (sInstance.gameSystemListeners.size() == 0) {
				sInstance.plantillaCarruselAsyncTask.cancel(true);
				sInstance.plantillaCarruselAsyncTask = null;
			}
		}
	}

	private void responseGameSystemNotConnectionStats() {
		if (this.gameSystemListeners != null) {
			for (int i = 0; i < this.gameSystemListeners.size(); i++) {
				this.gameSystemListeners.get(i)
						.onFailureGameSystemNotConnection();
			}
		}

	}

	/*****************************************************************/

	public void refreshCarrusel(String url) {
		if (Reachability.isOnline(this.mContext)) {
			this.mainStaticLoadCarruselJSONReader = new AsyncStaticLoadCarruselJSON(
					this, mContext);
			this.mainStaticLoadCarruselJSONReader.execute(url);
		} else {
			responseNotConnection();
		}
	}

	/**
	 * @param dataLink
	 */
	public void getDetail(String dataLink) {
		if (Reachability.isOnline(this.mContext)) {
			if (loadCarruselDetailJSONReader == null) {
				loadCarruselDetailJSONReader = new AsyncLoadDetailCarruselJSON(
						this, mContext);
				loadCarruselDetailJSONReader.execute(dataLink);
			}
		} else {
			responseFailureDetailNotConnection();
		}
	}

	/**
	 * @param string
	 */
	public void getSpades(String url) {
		if (Reachability.isOnline(this.mContext)) {
			map.remove(url);
			urlMap.put(CarruselDetail.CARRUSEL_PICAS, url);
			if (spadesCarruselAsyncTask == null) {
				spadesCarruselAsyncTask = new AsyncSpadesCarruselJSON(this,
						mContext);
				spadesCarruselAsyncTask.execute(url);
			}
		} else {
			responseNotConnectionSpades();
		}

	}

	/**
	 * @param string
	 */
	public void getStats(String url) {
		if (Reachability.isOnline(this.mContext)) {
			map.remove(url);
			urlMap.put(CarruselDetail.CARRUSEL_ESTADISTICAS, url);
			if (statsCarruselAsyncTask == null) {
				statsCarruselAsyncTask = new AsyncStatsCarruselJSON(this,
						mContext);
				statsCarruselAsyncTask.execute(url);
			}
		} else {
			responseNotConnectionStats();
		}

	}

	/**
	 * @param string
	 */
	public void getDirecto(String url) {
		if (Reachability.isOnline(this.mContext)) {
			map.remove(url);
			urlMap.put(CarruselDetail.CARRUSEL_DIRECTO, url);
			// Evitamos dos descargas simultaneas
			if (directoCarruselAsyncTask == null) {
				directoCarruselAsyncTask = new AsyncDirectoCarruselJSON(this,
						mContext);
				directoCarruselAsyncTask.execute(url);
			}

		} else {
			responseDirectoNotConnection();
		}

	}

	/**
	 * @param string
	 */
	public void getPlantilla(String url) {
		if (Reachability.isOnline(this.mContext)) {
			map.remove(url);
			urlMap.put(CarruselDetail.CARRUSEL_PLANTILLA, url);
			if (plantillaCarruselAsyncTask == null) {
				plantillaCarruselAsyncTask = new AsyncPlantillaCarruselJSON(
						this, mContext);
				plantillaCarruselAsyncTask.execute(url);
			}

		} else {
			responseGameSystemNotConnectionStats();
		}

	}

	/*****************************************************************/
	/************ ASYNC SETTINGS CARRUSEL READER *******************/

	@Override
	public void onSuccessfulExecute(Fase fase) {

		if (fase == null) {
			responseFailureRemoteConfig();
			return;
		} else {
			// He leido el plist y lo he cargardo en la DDBB
			responseUpdateDabase(fase);

		}
	}

	@Override
	public void onFailureExecute() {

		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureCarrusel();
			}
		}
	}

	/***********************************************************************/
	/************ ASYNC SETTINGS CARRUSEL-DETAIL READER *******************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncLoadDetailCarruselJSON.
	 * AsyncLoadDetailCarruselJSONListener
	 * #onSuccessfulExecute(es.prisacom.as.dao.model.calendar.Match)
	 */
	@Override
	public void onSuccessfulDetailExecute(Match match) {
		if (this.detailListeners != null) {
			for (int i = 0; i < this.detailListeners.size(); i++) {
				this.detailListeners.get(i).onSuccessDetailCarrusel(match);
			}
		}
		loadCarruselDetailJSONReader = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncLoadDetailCarruselJSON.
	 * AsyncLoadDetailCarruselJSONListener#onFailureDetailExecute()
	 */
	@Override
	public void onFailureDetailExecute() {
		if (this.detailListeners != null) {
			for (int i = 0; i < this.detailListeners.size(); i++) {
				this.detailListeners.get(i).onFailureDetailCarrusel();
			}
		}
		loadCarruselDetailJSONReader = null;
	}

	/***********************************************************************/
	/************ ASYNC SETTINGS CARRUSEL-SPADES READER *******************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncSpadesCarruselJSON.
	 * AsyncSpadesCarruselJSONListener
	 * #onSuccessfulSpadesExecute(es.prisacom.as.dao.model.calendar.Fase)
	 */
	@Override
	public void onSuccessfulSpadesExecute(SpadeInfo spadeInfo) {
		map.put(urlMap.get(CarruselDetail.CARRUSEL_PICAS), spadeInfo);
		if (this.spadesListeners != null) {
			for (int i = 0; i < this.spadesListeners.size(); i++) {
				this.spadesListeners.get(i).onSuccessSpades(spadeInfo);
			}
		}
		spadesCarruselAsyncTask = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncSpadesCarruselJSON.
	 * AsyncSpadesCarruselJSONListener#onFailureSpadesExecute()
	 */
	@Override
	public void onFailureSpadesExecute() {
		if (this.spadesListeners != null) {
			for (int i = 0; i < this.spadesListeners.size(); i++) {
				this.spadesListeners.get(i).onFailureSpades();
			}
		}
		spadesCarruselAsyncTask = null;
	}

	/***********************************************************************/
	/************ ASYNC SETTINGS CARRUSEL-STATS READER *********************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncStatsCarruselJSON.
	 * AsyncStatsCarruselJSONListener
	 * #onSuccessfulStatsExecute(es.prisacom.as.dao.model.carrusel.SpadeInfo)
	 */
	@Override
	public void onSuccessfulStatsExecute(ArrayList<StatsInfo> stats) {
		map.put(urlMap.get(CarruselDetail.CARRUSEL_ESTADISTICAS), stats);
		if (this.statsListeners != null) {
			for (int i = 0; i < this.statsListeners.size(); i++) {
				this.statsListeners.get(i).onSuccessStats(stats);
			}
		}
		statsCarruselAsyncTask = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncStatsCarruselJSON.
	 * AsyncStatsCarruselJSONListener#onFailureStatsExecute()
	 */
	@Override
	public void onFailureStatsExecute() {
		if (this.statsListeners != null) {
			for (int i = 0; i < this.statsListeners.size(); i++) {
				this.statsListeners.get(i).onFailureStats();
			}
		}
		statsCarruselAsyncTask = null;
	}

	/***********************************************************************/
	/************ ASYNC SETTINGS CARRUSEL-DIRECTO READER *********************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncDirectoCarruselJSON.
	 * AsyncDirectoCarruselJSONListener#onSuccessfulDirectoExecute()
	 */
	@Override
	public void onSuccessfulDirectoExecute(ArrayList<ItemDirecto> directos) {
		map.put(urlMap.get(CarruselDetail.CARRUSEL_DIRECTO), directos);
		if (this.directoListeners != null) {
			for (int i = 0; i < this.directoListeners.size(); i++) {
				this.directoListeners.get(i).onSuccessDirecto(directos);

			}
		}
		directoCarruselAsyncTask = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncDirectoCarruselJSON.
	 * AsyncDirectoCarruselJSONListener#onFailureDirectoExecute()
	 */
	@Override
	public void onFailureDirectoExecute() {
		if (this.directoListeners != null) {
			for (int i = 0; i < this.directoListeners.size(); i++) {
				this.directoListeners.get(i).onFailureDirecto();
			}
		}
		directoCarruselAsyncTask = null;

	}

	/***********************************************************************/
	/************ ASYNC SETTINGS CARRUSEL-PLANTILLA READER *********************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncPlantillaCarruselJSON.
	 * AsyncPlantillaCarruselJSONListener#onSuccessfulPlantillaExecute()
	 */
	@Override
	public void onSuccessfulPlantillaExecute(GameSystem gameSystem) {
		map.put(urlMap.get(CarruselDetail.CARRUSEL_PLANTILLA), gameSystem);
		if (this.gameSystemListeners != null) {
			for (int i = 0; i < this.gameSystemListeners.size(); i++) {
				this.gameSystemListeners.get(i).onSuccessGameSystem(gameSystem);
			}
		}
		plantillaCarruselAsyncTask = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncPlantillaCarruselJSON.
	 * AsyncPlantillaCarruselJSONListener#onFailurePlantillaExecute()
	 */
	@Override
	public void onFailurePlantillaExecute() {
		if (this.gameSystemListeners != null) {
			for (int i = 0; i < this.gameSystemListeners.size(); i++) {
				this.gameSystemListeners.get(i).onFailureGameSystem();
			}
		}
		plantillaCarruselAsyncTask = null;
	}

	/***********************************************************************/
	public boolean isDetailUpdating(String url) {
		if (map.containsKey(url)) {
			Log.d("CARRUSELDAO", "Detail " + url + " is in Cache");
			return true;
		} else {
			return false;
		}
	}

	public Object getDetailCached(String url) {
		Log.d("CARRUSELDAO", "Detail from Cache: " + url);
		return map.get(url);
	}

	/**
	 * 
	 */
	public void clean() {
		listeners.clear();
		detailListeners.clear();
		spadesListeners.clear();
		statsListeners.clear();
		directoListeners.clear();
		gameSystemListeners.clear();
		map.clear();
		urlMap.clear();
	}

	/**
	 * @param url
	 */
	public void cleanCache(String url) {
		map.remove(url);
		urlMap.clear();
	}

}
