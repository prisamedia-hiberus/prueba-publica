package com.diarioas.guiamundial.dao.reader.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParseJSONTeam;
import com.diarioas.guiamundial.dao.reader.parser.ParsePlistTeamStatic;
import com.diarioas.guiamundial.utils.Defines.Prefix;
import com.diarioas.guiamundial.utils.Reachability;

public class AsyncLoadTeamJSON extends AsyncTask<String, Void, Team> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadTeamJSONListener {
		void onSuccessfulExecute(Team team);

		void onFailureExecute();
	}

	AsyncLoadTeamJSONListener listener;
	Context appContext;
	boolean error;
	private final Team currentTeam;

	public AsyncLoadTeamJSON(AsyncLoadTeamJSONListener listener, Context ctx,
			Team currentTeam) {
		this.listener = listener;
		this.error = false;
		this.currentTeam = currentTeam;
		this.appContext = ctx;
	}

	@Override
	protected Team doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			Team team = null;
			if (Reachability.isOnline(this.appContext) == true) {
				String strFileContents;
				if (!currentTeam.isStaticInfo()) {
					try {
						Log.d("LOADTEAM",
								"Carga Estatica: " + currentTeam.getShortName()
										+ " ::  " + currentTeam.getUrlInfo());
						strFileContents = ReadRemote.readRemoteFile(
								currentTeam.getUrlInfo(), true);
//						String videoPrefix = DatabaseDAO
//								.getInstance(appContext).getPrefix(
//										Prefix.PREFIX_VIDEO);
//						String imagePrefix = DatabaseDAO
//								.getInstance(appContext).getPrefix(
//										Prefix.PREFIX_IMAGE);
//						String dataPrefix = DatabaseDAO.getInstance(appContext)
//								.getPrefix(Prefix.PREFIX_DATA);
						ParsePlistTeamStatic parseStatic = new ParsePlistTeamStatic(
								strFileContents);
//						parseStatic.parsePlistTeam(currentTeam, dataPrefix,imagePrefix, videoPrefix);
						
						parseStatic.parsePlistTeam(currentTeam);

						DatabaseDAO.getInstance(appContext).updateStaticTeam(
								currentTeam, urls[0]);
					} catch (Exception e) {
						Log.e("LOADTEAM",
								"Fallo en la carga Estatica: "
										+ currentTeam.getShortName() + " -- "
										+ e.getMessage());
					}

				}
				Log.d("LOADTEAM",
						"Carga Dinamica: " + currentTeam.getShortName()
								+ " ::  " + currentTeam.getUrl());
				strFileContents = ReadRemote.readRemoteFile(
						currentTeam.getUrl(), true);
				ParseJSONTeam parse = new ParseJSONTeam();
				String imagePrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_IMAGE);
				String dataPrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_DATA);
				Team teamFinal = parse.parseJSONTeam(currentTeam,
						strFileContents, imagePrefix, dataPrefix);

				if (teamFinal != null) {
					DatabaseDAO.getInstance(appContext).updateTeam(teamFinal);
					return teamFinal;
				} else {
					return currentTeam;
				}
			} else {
				// if (team == null) {
				this.error = true;
				// }
				return team;
			}

			// } catch (IOException e) {
			// this.error = true;
			// return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.error = true;
			return null;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(Team team) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(team);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
