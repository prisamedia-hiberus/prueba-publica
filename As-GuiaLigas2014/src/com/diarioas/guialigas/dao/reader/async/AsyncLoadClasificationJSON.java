package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONClasificacion;

public class AsyncLoadClasificationJSON extends AsyncTask<String, Void, Fase> {

	public interface AsyncLoadClasificationJSONListener {
		void onSuccessfulExecute(Fase fase);

		void onFailureExecute();
	}

	AsyncLoadClasificationJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadClasificationJSON(
			AsyncLoadClasificationJSONListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected Fase doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {

			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);

			ParseJSONClasificacion parse = new ParseJSONClasificacion();
			Fase fase = parse.parsePlistClasificacion(strFileContents);

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
