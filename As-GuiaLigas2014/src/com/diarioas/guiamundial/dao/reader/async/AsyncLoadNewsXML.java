package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.news.NewsItem;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.RemoteDataDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParsePlistNews;

public class AsyncLoadNewsXML extends
		AsyncTask<String, Void, ArrayList<NewsItem>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadNewsXMLListener {

		void onSuccessfulExecute(ArrayList<NewsItem> news);

		void onFailureExecute();
	}

	AsyncLoadNewsXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadNewsXML(AsyncLoadNewsXMLListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		Log.d("NEWS", "onPreExecute");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	}

	@Override
	protected ArrayList<NewsItem> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);
			ParsePlistNews parse = new ParsePlistNews();
			ArrayList<NewsItem> news = parse.parsePlistNews(appContext,
					strFileContents);

			if (news == null || news.size() == 0) {
				this.error = true;
			}

			int competitionId = RemoteDataDAO.getInstance(appContext)
					.getGeneralSettings().getCurrentCompetition().getId();
			DatabaseDAO.getInstance(appContext).updateNews(news, competitionId);

			Log.d("NEWS", "doInBackground");

			return news;

		} catch (IOException e) {
			this.error = true;
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.error = true;
			return null;
		}

	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		Log.d("NEWS", "onCancelled");
	}

	@Override
	protected void onCancelled(ArrayList<NewsItem> result) {
		// TODO Auto-generated method stub
		// super.onCancelled(result);
		Log.d("NEWS", "onCancelled");
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(ArrayList<NewsItem> news) {
		Log.d("NEWS", "onPostExecute");
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(news);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
