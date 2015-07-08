package com.diarioas.guialigas.dao.reader.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.diarioas.guialigas.dao.model.news.NewsItemMedia;
import com.diarioas.guialigas.dao.model.news.NewsItemTag;
import com.diarioas.guialigas.dao.model.news.SectionNewsTagSections;

public class ParseJSONNews {
	
	private static final String TAG = "PARSEJSONNEWS";

	public SectionNewsTagSections parsePlistTagNews(Context appContext,
			String source) {

		SectionNewsTagSections data;
		try {

			data = new SectionNewsTagSections();
			JSONObject jsonObject = new JSONObject(source);


			if (jsonObject.has("link") && !jsonObject.isNull("link"))
				data.setLink(jsonObject.getString("link"));

			if (jsonObject.has("TSBuildDate")
					&& !jsonObject.isNull("TSBuildDate"))
				data.settSBuildDate(jsonObject.getInt("TSBuildDate"));

			if (jsonObject.has("numItems") && !jsonObject.isNull("numItems"))
				data.setNumItems(jsonObject.getInt("numItems"));

			if (jsonObject.has("items") && !jsonObject.isNull("items")) {
				data.setItems(readItems(jsonObject.getJSONArray("items")));
			}

		} catch (JSONException e) {
			Log.e(TAG, "parsePlistTagNews No se ha parseado bien el fichero: "
					+ e.getMessage());
			return null;
		}
		return data;
	}
	
	private ArrayList<NewsItemTag> readItems(JSONArray jsonArray) throws JSONException {
		ArrayList<NewsItemTag> newsArray = new ArrayList<NewsItemTag>();
		JSONObject json;
		NewsItemTag newsItem;
		for (int i = 0; i < jsonArray.length(); i++) {
			json = (JSONObject) jsonArray.get(i);
			newsItem = new NewsItemTag();

			if (json.has("titleSection")) {
				newsItem.setTitle((String) json.get("titleSection"));
			} else if (json.has("titleNews")) {
				newsItem.setTitle((String) json.get("titleNews"));
			} else if (json.has("title")) {
				newsItem.setTitle((String) json.get("title"));
			}

			if (json.has("typology") && !json.isNull("typology"))
				newsItem.setTypology(json.getString("typology"));
			
			if (json.has("preTitle"))
				newsItem.setPreTitle((String) json.get("preTitle"));

			if (json.has("author") && !json.isNull("author"))
				newsItem.setAuthor((String) json.get("author"));

			if (json.has("section"))
				newsItem.setSection((String) json.get("section"));

			if (json.has("jumpToWeb")) {
				newsItem.setJumpToWeb(Integer.valueOf(json
						.getString("jumpToWeb")));
			}

			if (json.has("abstract"))
				newsItem.setAbstract_((String) json.get("abstract"));

			if (json.has("link"))
				newsItem.setLink((String) json.get("link"));

			if (json.has("ts"))
				newsItem.setTimeStamp(json.getInt("ts"));

			if (json.has("photos") && !json.isNull("photos")) {
				newsItem.setPhotos(readPhotos((JSONObject) json.get("photos")));
			}

			if (json.has("videos") && !json.isNull("videos")) {
				newsItem.setVideos(readVideos((JSONObject) json.get("videos")));
			}

			if (json.has("tags") && !json.isNull("tags"))
				newsItem.setTags(json.getString("tags"));

			newsItem.setJson(json.toString());

			newsArray.add(newsItem);
		}
		return newsArray;
	}
	
	
	private ArrayList<NewsItemMedia> readPhotos(JSONObject photosJSON)
			throws JSONException {
		ArrayList<NewsItemMedia> photos = new ArrayList<NewsItemMedia>();

		JSONObject photosNameJSON, photosSizeJSON;
		String category, name, size;

		for (Iterator<String> iterator = photosJSON.keys(); iterator.hasNext();) {
			category = iterator.next();
			photosNameJSON = (JSONObject) photosJSON.get(category);
			if (category.equalsIgnoreCase("thumbnail")) {
				photos.add(readPhoto(photosNameJSON, category, null, null));
			} else {
				for (Iterator<String> iterator2 = photosNameJSON.keys(); iterator2
						.hasNext();) {
					name = iterator2.next();
					photosSizeJSON = (JSONObject) photosNameJSON.get(name);
					for (Iterator<String> iterator3 = photosSizeJSON.keys(); iterator3
							.hasNext();) {
						size = iterator3.next();
						photos.add(readPhoto(
								(JSONObject) photosSizeJSON.get(size), size,
								name, size));
					}

				}
			}

		}

		return photos;
	}

	private NewsItemMedia readPhoto(JSONObject photoJSON, String category,
			String name, String size) throws JSONException {
		NewsItemMedia photo;
		photo = new NewsItemMedia();
		photo.setCategory(category);
		// photo.setName(name);
		// photo.setSize(size);
		if (photoJSON.has("caption") && !photoJSON.isNull("caption"))
			photo.setCaption(photoJSON.getString("caption"));

		if (photoJSON.has("url") && !photoJSON.isNull("url"))
			photo.setUrl(photoJSON.getString("url"));

		if (photoJSON.has("author") && !photoJSON.isNull("author"))
			photo.setAuthor(photoJSON.getString("author"));

		if (photoJSON.has("width") && !photoJSON.isNull("width"))
			photo.setWidth(photoJSON.getInt("width"));

		if (photoJSON.has("height") && !photoJSON.isNull("height"))
			photo.setHeight(photoJSON.getInt("height"));
		return photo;
	}

	private ArrayList<NewsItemMedia> readVideos(JSONObject videosJSON)
			throws JSONException {
		ArrayList<NewsItemMedia> videos = new ArrayList<NewsItemMedia>();

		JSONObject videosNameJSON;
		String category, name;
		for (Iterator<String> iterator = videosJSON.keys(); iterator.hasNext();) {
			category = iterator.next();
			videosNameJSON = (JSONObject) videosJSON.get(category);
			for (Iterator<String> iterator2 = videosNameJSON.keys(); iterator2
					.hasNext();) {
				name = iterator2.next();
				videos.add(readVideo((JSONObject) videosNameJSON.get(name),
						category, name, null));

			}

		}

		return videos;
	}

	private NewsItemMedia readVideo(JSONObject videoJSON, String category,
			String name, String size) throws JSONException {
		JSONObject photoJSON;
		NewsItemMedia video;

		video = new NewsItemMedia();
		video.setCategory(category);
		// video.setName(name);
		// video.setSize(size);

		if (videoJSON.has("url") && !videoJSON.isNull("url"))
			video.setUrl(videoJSON.getString("url"));

		if (videoJSON.has("width") && !videoJSON.isNull("width"))
			video.setWidth(videoJSON.getInt("width"));

		if (videoJSON.has("height") && !videoJSON.isNull("height"))
			video.setHeight(videoJSON.getInt("height"));
		if (videoJSON.has("photo") && !videoJSON.isNull("photo")) {
			photoJSON = videoJSON.getJSONObject("photo");
			if (photoJSON.has("url") && !photoJSON.isNull("url"))
				video.setUrlPhoto(photoJSON.getString("url"));
		}
		return video;
	}
	
	
}
