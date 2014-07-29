package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.carrusel.StatsInfo;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONCarrusel;

public class AsyncStatsCarruselJSON extends
		AsyncTask<String, Void, ArrayList<StatsInfo>> {

	public interface AsyncStatsCarruselJSONListener {
		void onSuccessfulStatsExecute(ArrayList<StatsInfo> stats);

		void onFailureStatsExecute();
	}

	AsyncStatsCarruselJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncStatsCarruselJSON(AsyncStatsCarruselJSONListener listener,
			Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<StatsInfo> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			ArrayList<StatsInfo> stats;

			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);

			ParseJSONCarrusel parse = new ParseJSONCarrusel();
			stats = parse.parsePlistStats(strFileContents);

			if (stats == null || stats.size() == 0) {
				this.error = true;
			}

			return stats;

		} catch (IOException e) {
			this.error = true;
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.error = true;
			return null;
		}

	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(ArrayList<StatsInfo> stats) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulStatsExecute(stats);
			} else {
				this.listener.onFailureStatsExecute();
			}

		}
	}

}
