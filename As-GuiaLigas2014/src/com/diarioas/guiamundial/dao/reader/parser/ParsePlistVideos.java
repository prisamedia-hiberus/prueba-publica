package com.diarioas.guiamundial.dao.reader.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.diarioas.guiamundial.dao.model.video.VideoItem;
import com.diarioas.guiamundial.utils.Defines.DateFormat;

public class ParsePlistVideos {

	public ArrayList<VideoItem> parsePlistVideos(String source) {
		ArrayList<VideoItem> videoArray = new ArrayList<VideoItem>();
		try {
			VideoItem videoItem;

			JSONObject json;
			JSONObject item_media;
			JSONArray jsonArray = new JSONArray(source);
			// for (int i = 0; i < jsonArray.length(); i++) {

			SimpleDateFormat format = new SimpleDateFormat(
					DateFormat.VIDEOPARSER_FORMAT, Locale.US);

			for (int i = 0; i < jsonArray.length(); i++) {
				json = (JSONObject) jsonArray.get(i);
				videoItem = new VideoItem();
				if (json.has("author"))
					videoItem.setAuthor((String) json.get("author"));

				if (json.has("description"))
					videoItem.setDescription((String) json.get("description"));

				if (json.has("link"))
					videoItem.setLink((String) json.get("link"));

				if (json.has("title"))
					videoItem.setTitle((String) json.get("title"));

				if (json.has("item_media")) {
					item_media = (JSONObject) json.get("item_media");
					if (item_media.has("enclosure"))
						videoItem.setUrlEnclosure(item_media
								.getString("enclosure"));
					if (item_media.has("thumbnail"))
						videoItem.setUrlThumbnail(item_media
								.getString("thumbnail"));
				}
				try {
					if (json.has("pubDate"))
						videoItem.setDatePub(format.parse((String) json
								.get("pubDate")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				videoArray.add(videoItem);
			}

		} catch (JSONException e) {
			Log.w("parsePlistVideos",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			return null;
		}
		return videoArray;
	}
}
