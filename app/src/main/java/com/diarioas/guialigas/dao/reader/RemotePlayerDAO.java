/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guialigas.dao.model.player.Player;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadPlayerJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadPlayerJSON.AsyncLoadPlayerJSONListener;
import com.diarioas.guialigas.utils.Reachability;

/**
 * @author robertosanchez
 * 
 */
public class RemotePlayerDAO implements AsyncLoadPlayerJSONListener {

	public interface RemotePlayerDAOListener {

		void onSuccessPlayerRemoteconfig(Player player);

		void onFailurePlayerRemoteconfig();

		void onFailureNotPlayerConnection(int currentPlayerId);

		void forceUpdatePlayerNeeded();

	}

	private static RemotePlayerDAO sInstance;
	private Context mContext;
	private ArrayList<RemotePlayerDAOListener> listeners;
	private AsyncLoadPlayerJSON mainStaticLoadPlayerJSONReader;

	public static RemotePlayerDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemotePlayerDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemotePlayerDAOListener>();
		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemotePlayerDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemotePlayerDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
		if (listeners.size() == 0 && mainStaticLoadPlayerJSONReader != null) {
			this.mainStaticLoadPlayerJSONReader.cancel(true);
		}
	}

	private void responseFailureRemoteConfig() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailurePlayerRemoteconfig();
			}
		}
	}

	private void responseNotConnection(int currentPlayerId) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureNotPlayerConnection(
						currentPlayerId);
			}
		}

	}

	private void responseUpdateDabase(Player player) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessPlayerRemoteconfig(player);
			}
		}
	}

	/**
	 * @param currentTeamId
	 ***************************************************************/

	public void refreshDatabaseWithNewResults(int currentPlayerId) {
		// cargamos de local siempre
		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(currentPlayerId);
		} else {
			responseNotConnection(currentPlayerId);
		}
	}

	/**
	 * @param currentPlayerId
	 * @param playerUrl
	 */
	public void refreshDatabaseWithNewResults(int currentPlayerId,
			String playerUrl) {
		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(currentPlayerId, playerUrl);
		} else {
			responseNotConnection(currentPlayerId);
		}

	}

	private void loadMainSettings(int currentPlayerId) {
		Player player = DatabaseDAO.getInstance(mContext).getPlayer(
				currentPlayerId);
		loadPlayer(player);
	}

	/**
	 * @param currentPlayerId
	 * @param playerUrl
	 */
	private void loadMainSettings(int currentPlayerId, String playerUrl) {
		Player player = DatabaseDAO.getInstance(mContext).getPlayer(
				currentPlayerId);
		player.setUrl(playerUrl);
		loadPlayer(player);

	}

	private void loadPlayer(Player player) {
		this.mainStaticLoadPlayerJSONReader = new AsyncLoadPlayerJSON(this,
				mContext, player);
		this.mainStaticLoadPlayerJSONReader.execute(player.getUrl());
	}

	/************ ASYNC SETTINGS TEAM READER *******************/

	@Override
	public void onSuccessfulExecute(Player player) {

		if (player == null) {
			responseFailureRemoteConfig();
			return;
		} else {
			responseUpdateDabase(player);
		}
	}

	@Override
	public void onFailureExecute() {

		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailurePlayerRemoteconfig();
			}
		}
	}
	/*****************************************************************/

}
