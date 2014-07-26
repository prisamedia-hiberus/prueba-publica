package com.diarioas.guiamundial.utils.imageutils;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class MemoryReleaseUtils {

	public static interface RankingTimeTypes {

		public static int WEEK = 0;
		public static int ALLTIMES = 1;
	}

	public static void unbindDrawables(View view) {
		if (view == null)
			return;

		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	public static void unbindDrawablesNotRecursive(View view) {
		if ((view != null) && (view.getBackground() != null)) {
			view.getBackground().setCallback(null);
		}
	}

	public static void releaseAnimationDrawables(AnimationDrawable ad,
			boolean needRecycle) {
		if (ad != null) {
			ad.stop();
			for (int i = 0; i < ad.getNumberOfFrames(); ++i) {
				Drawable frame = ad.getFrame(i);
				if (frame instanceof BitmapDrawable) {
					Bitmap currentBitmap = ((BitmapDrawable) frame).getBitmap();

					if (needRecycle) {
						currentBitmap.recycle();
					}
					currentBitmap = null;
				}
				frame.setCallback(null);
			}
			ad.setCallback(null);
		}
	}
}
