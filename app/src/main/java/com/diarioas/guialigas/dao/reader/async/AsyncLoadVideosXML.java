package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.video.VideoItem;
import com.diarioas.guialigas.dao.reader.parser.ParsePlistVideos;


public class AsyncLoadVideosXML extends
		AsyncTask<String, Void, ArrayList<VideoItem>> {

	public interface AsyncLoadVideosXMLListener {
		void onSuccessfulVideosExecute(ArrayList<VideoItem> palmares);

		void onFailureVideosExecute();
	}

	AsyncLoadVideosXMLListener listener;
	Context appContext;
	boolean error;

	public AsyncLoadVideosXML(AsyncLoadVideosXMLListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected ArrayList<VideoItem> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			String strFileContents = ReadRemote.readRemoteFile(urls[0], false);
			ParsePlistVideos parse = new ParsePlistVideos();
			ArrayList<VideoItem> videos = parse
					.parsePlistVideos(strFileContents);

			if (videos == null || videos.size() == 0) {
				this.error = true;
			}

			return videos;

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
	protected void onPostExecute(ArrayList<VideoItem> videos) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulVideosExecute(videos);
			} else {
				this.listener.onFailureVideosExecute();
			}

		}
	}
}
