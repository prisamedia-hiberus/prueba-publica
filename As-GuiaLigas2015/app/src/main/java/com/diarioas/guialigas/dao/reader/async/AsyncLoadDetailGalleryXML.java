package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.news.PhotoMediaItem;
import com.diarioas.guialigas.dao.reader.parser.ParseXMLGallery;

public class AsyncLoadDetailGalleryXML extends
		AsyncTask<String, Void, ArrayList<PhotoMediaItem>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadDetailGalleryXMLListener {
		void onSuccessDetailfulExecute(ArrayList<PhotoMediaItem> photos);

		void onFailureDetailExecute();
	}

	AsyncLoadDetailGalleryXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadDetailGalleryXML(
			AsyncLoadDetailGalleryXMLListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<PhotoMediaItem> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);
			ParseXMLGallery parse = new ParseXMLGallery();
			ArrayList<PhotoMediaItem> gallery = parse
					.parsePlistDetailGallery(strFileContents);

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
	protected void onPostExecute(ArrayList<PhotoMediaItem> photos) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessDetailfulExecute(photos);
			} else {
				this.listener.onFailureDetailExecute();
			}

		}
	}

}
