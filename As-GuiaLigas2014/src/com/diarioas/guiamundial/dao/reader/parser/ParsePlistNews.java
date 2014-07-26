package com.diarioas.guiamundial.dao.reader.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.diarioas.guiamundial.dao.model.news.NewsItem;
import com.diarioas.guiamundial.dao.model.news.PhotoMediaItem;
import com.diarioas.guiamundial.dao.model.news.VideoMediaItem;
import com.diarioas.guiamundial.utils.Defines.ReturnSharedPreferences;

public class ParsePlistNews {

	public ArrayList<NewsItem> parsePlistNews(Context appContext, String source) {
		ArrayList<NewsItem> newsArray = new ArrayList<NewsItem>();
		try {
			NewsItem newsItem;

			JSONObject json;
			JSONObject jsonObject = new JSONObject(source);

			if (jsonObject.has("TSBuildDate"))
				saveDate(appContext, (Integer) jsonObject.get("TSBuildDate"));

			JSONArray jsonArray = jsonObject.getJSONArray("items");
			String body;
			VideoMediaItem videoMediaItem;
			for (int i = 0; i < jsonArray.length(); i++) {
				json = (JSONObject) jsonArray.get(i);
				newsItem = new NewsItem();

				if (json.has("body")) {
					body = (String) json.get("body");
				} else {
					body = null;
				}

				videoMediaItem = null;
				if (json.has("videos")) {
					JSONObject videos = (JSONObject) json.get("videos");
					if (videos.has("news")) {
						if (videos.getJSONObject("news").has("video1")) {
							videoMediaItem = getVideoMediaItem(videos
									.getJSONObject("news").getJSONObject(
											"video1"));

						}

					}
				}

				if (body != null || videoMediaItem != null) {

					if (body != null)
						newsItem.setBody(body);

					if (videoMediaItem != null)
						newsItem.setVideo(videoMediaItem);

					if (json.has("title"))
						newsItem.setTitle((String) json.get("title"));

					if (json.has("preTitle"))
						newsItem.setPreTitle((String) json.get("preTitle"));

					if (json.has("subtitle"))
						newsItem.setSubTitle((String) json.get("subtitle"));

					if (json.has("author"))
						newsItem.setAuthor((String) json.get("author"));

					if (json.has("comments"))
						newsItem.setUrlComments((String) json.get("comments"));

					if (json.has("typology"))
						newsItem.setTypology((String) json.get("typology"));

					if (json.has("abstract"))
						newsItem.setAbstract((String) json.get("abstract"));

					if (json.has("portal"))
						newsItem.setPortal((String) json.get("portal"));

					if (json.has("link"))
						newsItem.setUrlDetail((String) json.get("link"));

					if (json.has("tags"))
						newsItem.setTags((String) json.get("tags"));

					if (json.has("titleSection"))
						newsItem.setTitleSection((String) json
								.get("titleSection"));

					if (json.has("section"))
						newsItem.setSection((String) json.get("section"));

					if (json.has("preTitleSection"))
						newsItem.setPreTitleSection((String) json
								.get("preTitleSection"));

					if (json.has("titleNews"))
						newsItem.setTitleNews((String) json.get("titleNews"));

					if (json.has("titleFront"))
						newsItem.setTitleFront((String) json.get("titleFront"));

					if (json.has("preTitleFront"))
						newsItem.setPreTitleFront((String) json
								.get("preTitleFront"));

					if (json.has("ts"))
						newsItem.setDate(new Date(Long.valueOf(json
								.getString("ts")) * 1000));

					if (json.has("photos")) {
						JSONObject photos = (JSONObject) json.get("photos");
						if (photos.has("thumbnail")) {
							newsItem.setPhotoThumbnail(getMediaItem((JSONObject) photos
									.get("thumbnail")));
						}
						if (photos.has("news")) {
							if (photos.getJSONObject("news").has("photo1")) {
								JSONObject photosJSON = photos.getJSONObject(
										"news").getJSONObject("photo1");

								String key;
								for (Iterator<String> iterator = photosJSON
										.keys(); iterator.hasNext();) {
									key = iterator.next();
									if (key.equalsIgnoreCase("big")) {
										newsItem.setPhotoBig(getMediaItem(photosJSON
												.getJSONObject(key)));
									} else {
										newsItem.setPhotoNormal(getMediaItem(photosJSON
												.getJSONObject(key)));
									}

								}
							}
						}

					}

					newsArray.add(newsItem);
				}

			}

		} catch (JSONException e) {
			Log.e("parsePlistNews",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			return null;
		}
		return newsArray;
	}

	private void saveDate(Context appContext, Integer date) {
		SharedPreferences prefs = appContext.getSharedPreferences(
				ReturnSharedPreferences.SP_NAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putFloat(ReturnSharedPreferences.SP_NEWSDATE, date);
		editor.commit();
	}

	private VideoMediaItem getVideoMediaItem(JSONObject jsonObject)
			throws JSONException {
		String url = null;
		if (jsonObject.has("url"))
			url = jsonObject.getString("url");

		if (url != null && !url.equalsIgnoreCase("")) {
			VideoMediaItem item = new VideoMediaItem();
			item.setUrl(url);
			if (jsonObject.has("width"))
				item.setWidth(jsonObject.getInt("width"));

			if (jsonObject.has("height"))
				item.setHeight(jsonObject.getInt("height"));

			if (jsonObject.has("photo"))
				item.setPhoto(getMediaItem(jsonObject.getJSONObject("photo")));

			return item;
		} else
			return null;
	}

	private PhotoMediaItem getMediaItem(JSONObject jsonObject)
			throws JSONException {
		PhotoMediaItem item = new PhotoMediaItem();

		if (jsonObject.has("width"))
			item.setWidth(jsonObject.getInt("width"));

		if (jsonObject.has("height"))
			item.setHeight(jsonObject.getInt("height"));

		if (jsonObject.has("url"))
			item.setUrl(jsonObject.getString("url"));

		if (jsonObject.has("caption"))
			item.setCaption(jsonObject.getString("caption"));

		if (jsonObject.has("author"))
			item.setAuthor(jsonObject.getString("author"));

		return item;
	}
}
