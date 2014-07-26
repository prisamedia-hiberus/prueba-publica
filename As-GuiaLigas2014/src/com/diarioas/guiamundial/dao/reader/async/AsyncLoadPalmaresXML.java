package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guiamundial.dao.model.palmares.Palmares;
import com.diarioas.guiamundial.dao.reader.parser.ParsePlistPalmares;

public class AsyncLoadPalmaresXML extends
		AsyncTask<String, Void, ArrayList<Palmares>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadPalmaresXMLListener {
		void onSuccessfulExecute(ArrayList<Palmares> palmares);

		void onFailureExecute();
	}

	AsyncLoadPalmaresXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadPalmaresXML(AsyncLoadPalmaresXMLListener listener,
			Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<Palmares> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {

			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);

			ParsePlistPalmares parse = new ParsePlistPalmares();
			ArrayList<Palmares> palmares = parse
					.parsePlistStadiums(strFileContents);

			if (palmares == null || palmares.size() == 0) {
				this.error = true;
			}

			return palmares;

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
	protected void onPostExecute(ArrayList<Palmares> palmares) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(palmares);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
