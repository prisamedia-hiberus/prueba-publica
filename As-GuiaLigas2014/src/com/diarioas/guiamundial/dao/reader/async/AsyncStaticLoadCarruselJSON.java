package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guiamundial.dao.model.calendar.Fase;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParseJSONCarrusel;
import com.diarioas.guiamundial.utils.Defines.Prefix;

public class AsyncStaticLoadCarruselJSON extends AsyncTask<String, Void, Fase> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncStaticLoadCarruselJSONListener {
		void onSuccessfulExecute(Fase fase);

		void onFailureExecute();
	}

	AsyncStaticLoadCarruselJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncStaticLoadCarruselJSON(
			AsyncStaticLoadCarruselJSONListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected Fase doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			Fase fase;

			// fases = downloadFeedAtUrl(urls);
			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);
			// ReadRemote.copyFile(appContext,urls[1], strFileContents);

			ParseJSONCarrusel parse = new ParseJSONCarrusel();
			String dataPrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_DATA);
			String resultadosPrefix = DatabaseDAO.getInstance(appContext)
					.getPrefix(Prefix.PREFIX_RESULT);
			fase = parse.parsePlistCarrusel(strFileContents, dataPrefix,
					resultadosPrefix);

			if (fase == null) {
				this.error = true;
			}

			return fase;

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
	protected void onPostExecute(Fase fase) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(fase);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
