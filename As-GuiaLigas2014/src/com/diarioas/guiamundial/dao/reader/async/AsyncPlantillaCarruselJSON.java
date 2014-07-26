package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guiamundial.dao.model.carrusel.GameSystem;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParseJSONCarrusel;
import com.diarioas.guiamundial.utils.Defines.Prefix;

public class AsyncPlantillaCarruselJSON extends
		AsyncTask<String, Void, GameSystem> {

	public interface AsyncPlantillaCarruselJSONListener {
		void onSuccessfulPlantillaExecute(GameSystem gameSystem);

		void onFailurePlantillaExecute();
	}

	AsyncPlantillaCarruselJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncPlantillaCarruselJSON(
			AsyncPlantillaCarruselJSONListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected GameSystem doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			GameSystem gs = null;

			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);

			ParseJSONCarrusel parse = new ParseJSONCarrusel();
			String imagePrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_IMAGE);
			String imageData = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_DATA);
			gs = parse.parsePlistGameSystem(strFileContents, imagePrefix,
					imageData);

			if (gs == null) {
				this.error = true;
			}

			return gs;

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
	protected void onPostExecute(GameSystem gameSystem) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulPlantillaExecute(gameSystem);
			} else {
				this.listener.onFailurePlantillaExecute();
			}

		}
	}

}
