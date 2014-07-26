package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guiamundial.dao.model.stadium.Stadium;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParsePlistStadiums;
import com.diarioas.guiamundial.utils.Defines.Prefix;

public class AsyncLoadStadiumDetailXML extends
		AsyncTask<Stadium, Void, Stadium> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadStadiumDetailXMLListener {
		void onSuccessfulDetailExecute(Stadium stadium);

		void onFailureDetailExecute(Stadium stadium);
	}

	AsyncLoadStadiumDetailXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadStadiumDetailXML(
			AsyncLoadStadiumDetailXMLListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected Stadium doInBackground(Stadium... stadiums) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			Stadium stadium = stadiums[0];

			String strFileContents = ReadRemote.readRemoteFile(
					stadium.getUrlInfo(), false);

			ParsePlistStadiums parse = new ParsePlistStadiums();
			String imagePrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_IMAGE);
			stadium = parse.parsePlistDetailStadium(stadium, strFileContents,
					imagePrefix);

			if (stadium == null) {
				this.error = true;
			}

			return stadium;

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
	protected void onPostExecute(Stadium stadium) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulDetailExecute(stadium);
			} else {
				this.listener.onFailureDetailExecute(stadium);
			}

		}
	}

}
