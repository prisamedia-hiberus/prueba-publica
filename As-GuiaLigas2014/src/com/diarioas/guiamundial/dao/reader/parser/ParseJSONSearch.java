package com.diarioas.guiamundial.dao.reader.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.diarioas.guiamundial.dao.model.search.SearchItem;

public class ParseJSONSearch {
	private static final String ns = null;

	/**
	 * @param strFileContents
	 * @param urlPrefix
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<SearchItem> parseSearch(String strFileContents,
			String urlPrefix) throws Exception {
		ArrayList<SearchItem> items = new ArrayList<SearchItem>();
		try {
			JSONObject json = new JSONObject(strFileContents);
			int num = json.getInt("num");
			JSONArray answersJSONArray = json.getJSONArray("answers");
			JSONObject answerJson;
			SearchItem item;

			for (int i = 0; i < num; i++) {
				item = new SearchItem();
				answerJson = answersJSONArray.getJSONObject(i);
				if (answerJson.has("id") && !answerJson.isNull("id"))
					item.setId(answerJson.getString("id"));
				if (answerJson.has("nombre") && !answerJson.isNull("nombre"))
					item.setNombre(answerJson.getString("nombre"));
				if (answerJson.has("normalizado")
						&& !answerJson.isNull("normalizado"))
					item.setNormalizado(answerJson.getString("normalizado"));
				if (answerJson.has("url") && !answerJson.isNull("url"))
					item.setUrl(answerJson.getString("url"));
				if (answerJson.has("url_datos")
						&& !answerJson.isNull("url_datos"))
					item.setUrlDatos(urlPrefix
							+ answerJson.getString("url_datos"));
				if (answerJson.has("alias") && !answerJson.isNull("alias"))
					item.setAlias(answerJson.getString("alias"));
				items.add(item);
			}

		} catch (JSONException e) {
			Log.e("CALENDAR",
					"Error al crear el JSON del calendario: " + e.getMessage());
		} catch (Exception e) {
			Log.e("CALENDAR",
					"Error al parsear el calendario: " + e.getMessage());
			// e.printStackTrace();
		}

		return items;
	}

}
