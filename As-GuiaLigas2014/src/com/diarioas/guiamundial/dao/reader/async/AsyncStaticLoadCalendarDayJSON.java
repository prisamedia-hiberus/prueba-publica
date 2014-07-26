package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guiamundial.dao.model.calendar.Fase;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParseJSONCalendar;
import com.diarioas.guiamundial.utils.Defines.Prefix;

public class AsyncStaticLoadCalendarDayJSON extends
		AsyncTask<String, Void, Fase> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncSettingsCalendarDayJSONListener {
		void onSuccessfulCalendarDayExecute(Fase fase);

		void onFailureCalendarDayExecute();
	}

	AsyncSettingsCalendarDayJSONListener listener;
	Context appContext;
	boolean error;
	private final int numFase;
	private final int numDay;

	public AsyncStaticLoadCalendarDayJSON(
			AsyncSettingsCalendarDayJSONListener listener, Context ctx,
			int numFase, int numDay) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
		this.numFase = numFase;
		this.numDay = numDay;
	}

	@Override
	protected Fase doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			Fase fase = null;

			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);

			ParseJSONCalendar parse = new ParseJSONCalendar();
			String dataPrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_DATA);
			fase = parse.parsePlistCalendarDay(strFileContents, dataPrefix,
					numFase, numDay);

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
				this.listener.onSuccessfulCalendarDayExecute(fase);
			} else {
				this.listener.onFailureCalendarDayExecute();
			}

		}
	}

}
