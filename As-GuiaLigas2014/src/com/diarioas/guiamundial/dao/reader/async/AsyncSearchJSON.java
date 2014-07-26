package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.search.SearchItem;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParseJSONSearch;
import com.diarioas.guiamundial.utils.Defines.Prefix;
import com.diarioas.guiamundial.utils.Reachability;

public class AsyncSearchJSON extends
		AsyncTask<String, Void, ArrayList<SearchItem>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncSearchJSONListener {
		void onSuccessfulExecute(ArrayList<SearchItem> retorno);

		void onFailureExecute();
	}

	AsyncSearchJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncSearchJSON(AsyncSearchJSONListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<SearchItem> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			String strFileContents;
			if (Reachability.isOnline(this.appContext) == true) {
				Log.d("ASYNCSEARCHJSON", "Buscando: " + urls[1]);
				// TODO: Pasarle la Url
				// String searchPath = "http://as.com/apps/datos/buscador.pl";
				strFileContents = ReadRemote.readRemoteFile(urls[0] + "?q="
						+ urls[1].replaceAll(" ", "%20"), false);

				String urlPrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_DATA);
				ParseJSONSearch parse = new ParseJSONSearch();
				ArrayList<SearchItem> list = parse.parseSearch(strFileContents,
						urlPrefix);
				return list;
			} else
				return null;

		} catch (IOException e) {
			this.error = true;
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.error = true;
			Log.e("ASYNCSEARCHJSON", "Error: " + e.getMessage());
			return null;

		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(ArrayList<SearchItem> retorno) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(retorno);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
