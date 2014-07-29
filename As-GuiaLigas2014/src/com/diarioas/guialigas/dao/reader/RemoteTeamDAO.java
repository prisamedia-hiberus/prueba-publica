/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadTeamJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncLoadTeamJSON.AsyncLoadTeamJSONListener;
import com.diarioas.guialigas.utils.Reachability;

/**
 * @author robertosanchez
 * 
 */
public class RemoteTeamDAO implements AsyncLoadTeamJSONListener {

	public interface RemoteTeamDAOListener {

		void onSuccessTeamRemoteconfig(Team team);

		void onFailureTeamRemoteconfig(Team team);

		void onFailureNotTeamConnection(Team team);

	}

	private static RemoteTeamDAO sInstance;
	private Context mContext;
	private ArrayList<RemoteTeamDAOListener> listeners;
	private AsyncLoadTeamJSON mainStaticLoadTeamJSONReader;
	private Team currentTeam;

	public static RemoteTeamDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new RemoteTeamDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<RemoteTeamDAOListener>();
		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(RemoteTeamDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(RemoteTeamDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
		if (listeners.size() == 0 && mainStaticLoadTeamJSONReader != null) {
			this.mainStaticLoadTeamJSONReader.cancel(true);
		}
	}

	private void responseFailureRemoteConfig(Team team) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureTeamRemoteconfig(team);
			}
		}
	}

	private void responseNotConnection(Team team) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureNotTeamConnection(team);
			}
		}

	}

	private void responseUpdateDabase(Team team) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessTeamRemoteconfig(team);
			}
		}
	}

	/**
	 * @param currentTeamId
	 ***************************************************************/

	public void getTeamData(String currentTeamId, String competitionId) {
		currentTeam = getTeamFromDatabase(currentTeamId, competitionId);
		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(currentTeam, competitionId);
		} else {
			responseNotConnection(currentTeam);
		}
	}

	// public void refreshDatabaseWithNewResults(String currentTeamId, String
	// url) {
	//
	// if (Reachability.isOnline(this.mContext)) {
	// loadMainSettings(currentTeamId, url);
	// } else {
	// responseNotConnection(currentTeamId);
	// }
	// }

	private void loadMainSettings(Team team, String competitionId) {
		loadTeam(team, competitionId);
	}

	private Team getTeamFromDatabase(String currentTeamId, String competitionId) {
		Team team = DatabaseDAO.getInstance(mContext).getTeam(currentTeamId);
		team.setHistoricalPalmares(DatabaseDAO.getInstance(mContext)
				.getPalmaresTeamHistoricoYears(competitionId, currentTeamId));
		team.setIdealPlayers(DatabaseDAO.getInstance(mContext).getIdealPlayers(
				currentTeamId));
		return team;
	}

	private void loadTeam(Team team, String competitionId) {
		this.mainStaticLoadTeamJSONReader = new AsyncLoadTeamJSON(this,
				mContext, team);
		this.mainStaticLoadTeamJSONReader.execute(competitionId);
	}

	/************ ASYNC SETTINGS TEAM READER *******************/

	@Override
	public void onSuccessfulExecute(Team team) {
		this.mainStaticLoadTeamJSONReader = null;
		if (team == null) {
			responseFailureRemoteConfig(currentTeam);
			return;
		} else {
			responseUpdateDabase(team);
		}
	}

	@Override
	public void onFailureExecute() {
		this.mainStaticLoadTeamJSONReader = null;
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureTeamRemoteconfig(currentTeam);
			}
		}
	}
	/*****************************************************************/

}
