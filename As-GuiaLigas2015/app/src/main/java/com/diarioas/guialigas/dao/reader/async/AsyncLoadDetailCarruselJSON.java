package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.calendar.Match;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONCarrusel;
import com.diarioas.guialigas.utils.Defines.Prefix;

public class AsyncLoadDetailCarruselJSON extends AsyncTask<String, Void, Match> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadDetailCarruselJSONListener {
		void onSuccessfulDetailExecute(Match match);

		void onFailureDetailExecute();
	}

	AsyncLoadDetailCarruselJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadDetailCarruselJSON(
			AsyncLoadDetailCarruselJSONListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected Match doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			Match match;

			// fases = downloadFeedAtUrl(urls);
			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);
			// ReadRemote.copyFile(appContext,urls[1], strFileContents);

			ParseJSONCarrusel parse = new ParseJSONCarrusel();
			String dataPrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_DATA);
			match = parse.parsePlistDetailCarrusel(strFileContents, dataPrefix);

			if (match == null) {
				this.error = true;
			}

			return match;

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
	protected void onPostExecute(Match match) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulDetailExecute(match);
			} else {
				this.listener.onFailureDetailExecute();
			}

		}
	}

}
