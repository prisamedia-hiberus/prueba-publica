package com.diarioas.guialigas.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

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
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void setBackGround(View view, Drawable drawable) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			view.setBackgroundDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}
}
