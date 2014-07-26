package com.diarioas.guiamundial.dao.reader.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.diarioas.guiamundial.dao.model.news.GalleryMediaItem;
import com.diarioas.guiamundial.dao.model.news.PhotoMediaItem;

public class ParsePlistGallery {

	public ArrayList<GalleryMediaItem> parsePlistGallery(String source) {
		ArrayList<GalleryMediaItem> galleryArray = new ArrayList<GalleryMediaItem>();
		GalleryMediaItem gallery;
		JSONObject json;

		try {

			JSONObject jsonObject = new JSONObject(source);
			JSONArray jsonArray = jsonObject.getJSONArray("items");

			for (int i = 0; i < jsonArray.length(); i++) {
				json = (JSONObject) jsonArray.get(i);
				gallery = new GalleryMediaItem();

				if (json.has("title"))
					gallery.setTitle((String) json.get("title"));
				if (json.has("link"))
					gallery.setLink((String) json.get("link"));

				if (json.has("photos")) {
					JSONObject photos = (JSONObject) json.get("photos");

					// if (photos.has("news")) {
					// if (photos.getJSONObject("news").has("photo1")) {
					// JSONObject photosJSON = photos
					// .getJSONObject("news").getJSONObject(
					// "photo1");
					//
					// String key;
					// for (Iterator<String> iterator = photosJSON.keys();
					// iterator
					// .hasNext();) {
					// key = iterator.next();
					// if (key.equalsIgnoreCase("big")) {
					// gallery.addMedia(getMediaItem(photosJSON
					// .getJSONObject(key)));
					// } else {
					// gallery.addMedia(getMediaItem(photosJSON
					// .getJSONObject(key)));
					// }
					//
					// }
					// }
					// }
					if (photos.has("thumbnail")) {
						gallery.setCoverPhoto(getMediaItem((JSONObject) photos
								.get("thumbnail")));
					}
				}
				galleryArray.add(gallery);
			}
		} catch (JSONException e) {
			Log.w("parsePlistVideos",
					"No se ha parseado bien el fichero - Metodo 1: "
							+ e.getMessage());
			return null;
		}
		return galleryArray;
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
