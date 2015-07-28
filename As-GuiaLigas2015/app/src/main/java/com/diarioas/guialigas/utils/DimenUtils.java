package com.diarioas.guialigas.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DimenUtils {

	public static int getRegularPixelFromDp(Context context, float dpPixel) {

		// Get the screen's density scale
		final float scale = context.getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (dpPixel * scale + 0.5f);
	}

	@SuppressLint("NewApi")
	public static Point getSize(WindowManager wm) {
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		try {
			display.getSize(size);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			size.x = display.getWidth();
			size.y = display.getHeight();
		}
		return size;
	}
}
