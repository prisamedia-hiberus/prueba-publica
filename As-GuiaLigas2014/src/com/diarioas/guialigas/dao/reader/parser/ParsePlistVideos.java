package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.diarioas.guialigas.dao.model.video.VideoItem;

public class ParsePlistVideos {

	public ArrayList<VideoItem> parsePlistVideos(String source) {
		ArrayList<VideoItem> videoArray = new ArrayList<VideoItem>();
		try {

			JSONObject jsonObject = new JSONObject(source);

			if (jsonObject.has("items") && !jsonObject.isNull("items")) {
				JSONArray currentItems = jsonObject.getJSONArray("items");

				int numItems = currentItems.length();
				for (int i = 0; i < numItems; i++) {
					VideoItem currentViewItem = new VideoItem();
					JSONObject currentNewsItem = currentItems.getJSONObject(i);

					if (currentNewsItem.has("author")
							&& !currentNewsItem.isNull("author"))
						currentViewItem.setAuthor((String) currentNewsItem
								.get("author"));

					if (currentNewsItem.has("description")
							&& !currentNewsItem.isNull("description"))
						currentViewItem.setDescription((String) currentNewsItem
								.get("description"));

					if (currentNewsItem.has("link")
							&& !currentNewsItem.isNull("link"))
						currentViewItem.setLink((String) currentNewsItem
								.get("link"));

					if (currentNewsItem.has("title")
							&& !currentNewsItem.isNull("title"))
						currentViewItem.setTitle((String) currentNewsItem
								.get("title"));

					if (currentNewsItem.has("ts")
							&& !currentNewsItem.isNull("ts")) {

						Date currentDate = new Date(
								currentNewsItem.getInt("ts") * 1000);
						currentViewItem.setDatePub(currentDate);

					}

					if (currentNewsItem.has("videos")
							&& !currentNewsItem.isNull("videos")) {

						JSONObject currentVideoObject = currentNewsItem
								.getJSONObject("videos");
						if (currentVideoObject.has("news")
								&& !currentVideoObject.isNull("news")) {
							JSONObject currentNewsVideoObject = currentVideoObject
									.getJSONObject("news");
							if (currentNewsVideoObject.has("video1")
									&& !currentNewsVideoObject.isNull("video1")) {
								JSONObject currentNewsVideo1Object = currentNewsVideoObject
										.getJSONObject("video1");
								if (currentNewsVideo1Object.has("idBC")
										&& !currentNewsVideo1Object
												.isNull("idBC")) {
									currentViewItem
											.setIdBC(currentNewsVideo1Object
													.getString("idBC"));
								}
								if (currentNewsVideo1Object.has("url")
										&& !currentNewsVideo1Object
												.isNull("url")) {
									currentViewItem
											.setUrlEnclosure(currentNewsVideo1Object
													.getString("url"));
								}
								if (currentNewsVideo1Object.has("photo")
										&& !currentNewsVideo1Object
												.isNull("photo")) {
									JSONObject photoObject = currentNewsVideo1Object
											.getJSONObject("photo");

									if (photoObject.has("url")
											&& !photoObject.isNull("url")) {
										currentViewItem
												.setUrlThumbnail(photoObject
														.getString("url"));
									}
								}

							}

						}
					}

					videoArray.add(currentViewItem);
				}

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
