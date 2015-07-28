package com.diarioas.guialigas.dao.reader.async;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.news.SectionNewsTagSections;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONNews;

public class AsyncTagReader extends
		AsyncTask<String, Void, SectionNewsTagSections> {

	public interface AsyncTagReaderListener {

		void onSuccessfulTagExecute(SectionNewsTagSections data);

		void onFailureTagExecute();
	}

	AsyncTagReaderListener listener;
	Context appContext;
	boolean error;

	public AsyncTagReader(AsyncTagReaderListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected SectionNewsTagSections doInBackground(String... urls) {
		SectionNewsTagSections results;
		this.error = false;
		try {
			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);

			ParseJSONNews parser = new ParseJSONNews();
			results = parser.parsePlistTagNews(appContext, strFileContents);
			if (results == null) {
				this.error = true;
			}
			return results;

		} catch (Exception e) {
			this.error = true;
			return null;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(SectionNewsTagSections data) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulTagExecute(data);
			} else {
				this.listener.onFailureTagExecute();
			}

		}
	}

}
