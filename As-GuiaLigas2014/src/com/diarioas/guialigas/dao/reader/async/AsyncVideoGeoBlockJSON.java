package com.diarioas.guialigas.dao.reader.async;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncVideoGeoBlockJSON extends AsyncTask<String, Void, String> {

	public interface AsyncVideoGeoBlockJSONListener {

		void onSuccessfulVideoGeoBlock(String finalURL, String url);

		void onFailureVideoGeoBlock(String url);
	}

	AsyncVideoGeoBlockJSONListener listener;
	Context appContext;
	boolean error;
	private String url;

	public AsyncVideoGeoBlockJSON(AsyncVideoGeoBlockJSONListener listener,
			Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;
	}

	@Override
	protected String doInBackground(String... urls) {
		String finalURL = null;
		this.error = false;
		try {
			this.url = urls[0];
			String strFileContents = null;

			if(this.url!=null)
			{
				this.url = this.url.replace(" ", "");
			}
			strFileContents = ReadRemote.readRemoteFile(this.url, false);

			ParseGeoBlockVideos parser = new ParseGeoBlockVideos();
			finalURL = parser.parse(strFileContents);
			if (finalURL == null) {
				this.error = true;
			}
			return finalURL;

		} catch (Exception e) {
			this.error = true;
			return null;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String finalURL) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulVideoGeoBlock(finalURL, url);
			} else {
				this.listener.onFailureVideoGeoBlock(url);
			}

		}
	}
	
	public static String INIT_FUNCTION_GEOBLOCK = "EPET_VideoPlayer_callback(";

	public class ParseGeoBlockVideos {

		public String parse(String source) {
			String urlVideo = null;
			try {

				source = source.replace(INIT_FUNCTION_GEOBLOCK, "");
				source = source.replace(");", "");

				JSONObject jsonObject = new JSONObject(source);
				if (jsonObject != null && jsonObject.has("mp4")
						&& !jsonObject.isNull("mp4")) {
					urlVideo = jsonObject.getString("mp4");
				}

			} catch (JSONException e) {
				Log.w("ParseGeoBlockVideos",
						"parse geo block video error" + e.getMessage());
				return null;
			}
			return urlVideo;
		}
	}

}
