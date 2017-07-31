package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.news.GalleryMediaItem;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistGallery;

public class AsyncLoadGalleryXML extends
		AsyncTask<String, Void, ArrayList<GalleryMediaItem>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadGalleryXMLListener {
		void onSuccessfulExecute(ArrayList<GalleryMediaItem> palmares);

		void onFailureExecute();
	}

	AsyncLoadGalleryXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadGalleryXML(AsyncLoadGalleryXMLListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<GalleryMediaItem> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);
			ParsePlistGallery parse = new ParsePlistGallery();
			ArrayList<GalleryMediaItem> gallery = parse
					.parsePlistGallery(strFileContents);

			if (gallery == null || gallery.size() == 0) {
				this.error = true;
			}

			return gallery;

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
	protected void onPostExecute(ArrayList<GalleryMediaItem> videos) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(videos);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

}
