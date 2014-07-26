/**
 * 
 */
package com.diarioas.guiamundial.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guiamundial.dao.model.calendar.Fase;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadClasificationJSON;
import com.diarioas.guiamundial.dao.reader.async.AsyncLoadClasificationJSON.AsyncLoadClasificationJSONListener;
import com.diarioas.guiamundial.utils.Reachability;

/**
 * @author robertosanchez
 * 
 */
public class RemoteClasificacionDAO implements
		AsyncLoadClasificationJSONListener {

	public interface RemoteClasificacionDAOListener {

		void onSuccessRemoteconfig(Fase fase);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	private static RemoteClasificacionDAO sInstance;
	private Context mContext;
	private ArrayList<RemoteClasificacionDAOListener> listeners;

	private AsyncLoadClasificationJSON mainStaticLoadClasificaiconJSONReader;

	public static RemoteClasificacionDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteClasificacionDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteClasificacionDAOListener>();
		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemoteClasificacionDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteClasificacionDAOListener listener) {
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

	private void responseUpdateDabase(Fase fase) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(fase);
			}
		}
	}

	/*****************************************************************/

	public void refreshClasificationWithNewResults(String url) {
		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(url);
		} else {
			responseNotConnection();
		}
	}

	private void loadMainSettings(String url) {

		this.mainStaticLoadClasificaiconJSONReader = new AsyncLoadClasificationJSON(
				this, mContext);
		this.mainStaticLoadClasificaiconJSONReader.execute(url);
		// ArrayList<HashMap<String, String>> teams = getFakeTeams();
		// responseUpdateDabase(teams);
	}

	// /************ ASYNC SETTINGS CLASIFICATION READER *******************/

	@Override
	public void onSuccessfulExecute(Fase fase) {

		if (fase == null) {
			responseFailureRemoteConfig();
			return;
		} else {
			// He leido el plist y lo he cargardo en la DDBB
			// responseUpdateDabase(teams)
			responseUpdateDabase(fase);

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
	/*****************************************************************/
}
