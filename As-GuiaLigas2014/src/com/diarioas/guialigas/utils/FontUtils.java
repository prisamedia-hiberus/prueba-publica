package com.diarioas.guialigas.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtils {

	public static interface FontTypes {

		public static String ROBOTO_BLACK = "Roboto-Black";
		public static String ROBOTO_LIGHT = "Roboto-Light";
		public static String ROBOTO_REGULAR = "Roboto-Regular";

		public static String HELVETICANEUE = "HelveticaNeue";
		public static String HELVETICANEUEBOLD = "HelveticaNeueBold";
		public static String HELVETICANEUELIGHT = "HelveticaNeueLight";
		public static String HELVETICANEUEULIGHT = "HelveticaNeueUltraLight";

	}

	private static Map<String, String> fontMap = new HashMap<String, String>();

	static {

		fontMap.put(FontTypes.ROBOTO_BLACK, "fonts/Roboto-Black.ttf");
		fontMap.put(FontTypes.ROBOTO_LIGHT, "fonts/Roboto-Light.ttf");
		fontMap.put(FontTypes.ROBOTO_REGULAR, "fonts/Roboto-Regular.ttf");
		fontMap.put(FontTypes.HELVETICANEUE, "fonts/HelveticaNeue.ttf");
		fontMap.put(FontTypes.HELVETICANEUEBOLD, "fonts/HelveticaNeueBold.ttf");
		fontMap.put(FontTypes.HELVETICANEUELIGHT,
				"fonts/HelveticaNeueLight.ttf");
		fontMap.put(FontTypes.HELVETICANEUEULIGHT,
				"fonts/HelveticaNeueUltraLight.ttf");
	}

	/* cache for loaded Roboto typefaces */
	private static Map<String, Typeface> typefaceCache = new HashMap<String, Typeface>();

	private static Typeface getRobotoTypeface(Context context, String fontType) {

		String fontPath = fontMap.get(fontType);
		if (!typefaceCache.containsKey(fontType)) {
			typefaceCache.put(fontType,
					Typeface.createFromAsset(context.getAssets(), fontPath));
		}
		return typefaceCache.get(fontType);

	}

	public static void setCustomfont(Context context, View view, String typeFont) {
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				setCustomfont(context, ((ViewGroup) view).getChildAt(i),
						typeFont);
			}
		} else if (view instanceof TextView) {
			((TextView) view).setTypeface(getRobotoTypeface(context, typeFont));
		}

	}

}
