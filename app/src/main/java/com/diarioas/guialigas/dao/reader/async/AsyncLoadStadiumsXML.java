package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.stadium.Stadium;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistStadiums;

public class AsyncLoadStadiumsXML extends
		AsyncTask<String, Void, ArrayList<Stadium>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadStadiumXMLListener {
		void onSuccessfulExecute(ArrayList<Stadium> stadiums);

		void onFailureExecute();
	}

	AsyncLoadStadiumXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadStadiumsXML(AsyncLoadStadiumXMLListener listener,
			Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<Stadium> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {

			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);

			ParsePlistStadiums parse = new ParsePlistStadiums();
			ArrayList<Stadium> stadiums = parse
					.parsePlistStadiums(strFileContents);

			if (stadiums == null || stadiums.size() == 0) {
				this.error = true;
			}

			return stadiums;

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
	protected void onPostExecute(ArrayList<Stadium> stadiums) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(stadiums);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
