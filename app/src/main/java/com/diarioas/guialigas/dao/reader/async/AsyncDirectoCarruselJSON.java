package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.carrusel.ItemDirecto;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONCarrusel;
import com.diarioas.guialigas.utils.Defines.Prefix;

public class AsyncDirectoCarruselJSON extends
		AsyncTask<String, Void, ArrayList<ItemDirecto>> {

	public interface AsyncDirectoCarruselJSONListener {
		void onSuccessfulDirectoExecute(ArrayList<ItemDirecto> directos);

		void onFailureDirectoExecute();
	}

	AsyncDirectoCarruselJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncDirectoCarruselJSON(AsyncDirectoCarruselJSONListener listener,
			Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<ItemDirecto> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			ArrayList<ItemDirecto> directos = null;

			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);

			ParseJSONCarrusel parse = new ParseJSONCarrusel();
			String imagePrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_IMAGE);
			directos = parse.parsePlistDirecto(strFileContents, imagePrefix);

			if (directos == null || directos.size() == 0) {
				this.error = true;
			}

			return directos;

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
	protected void onPostExecute(ArrayList<ItemDirecto> directos) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulDirectoExecute(directos);
			} else {
				this.listener.onFailureDirectoExecute();
			}

		}
	}

}
