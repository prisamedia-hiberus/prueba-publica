package com.diarioas.guialigas.utils;

import android.content.Context;

public class DrawableUtils {

	public static int getDrawableId(Context ctx, String url,
			int numCharacterExtension) {
		int idLocal = 0;
		if (url != null && ctx!=null)
			idLocal = ctx.getResources().getIdentifier(
					url.substring(0, url.length() - numCharacterExtension),
					"drawable", ctx.getPackageName());
		return idLocal;
	}
}
