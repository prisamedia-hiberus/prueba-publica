package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.carrusel.SpadeInfo;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONCarrusel;
import com.diarioas.guialigas.utils.Defines.Prefix;

public class AsyncSpadesCarruselJSON extends AsyncTask<String, Void, SpadeInfo> {

	public interface AsyncSpadesCarruselJSONListener {
		void onSuccessfulSpadesExecute(SpadeInfo spades);

		void onFailureSpadesExecute();
	}

	AsyncSpadesCarruselJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncSpadesCarruselJSON(AsyncSpadesCarruselJSONListener listener,
			Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected SpadeInfo doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			SpadeInfo spadeInfo;

			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);

			ParseJSONCarrusel parse = new ParseJSONCarrusel();
			String imagePrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_IMAGE);
			String dataPrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_DATA);
			spadeInfo = parse.parsePlistSpades(strFileContents, imagePrefix,
					dataPrefix);

			if (spadeInfo == null) {
				this.error = true;
			}

			return spadeInfo;

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
	protected void onPostExecute(SpadeInfo spades) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulSpadesExecute(spades);
			} else {
				this.listener.onFailureSpadesExecute();
			}

		}
	}

}
