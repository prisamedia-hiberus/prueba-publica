package com.diarioas.guialigas.utils.imageutils.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class MemoryReleaseUtils {

	public static interface ReturnResultCodes {

		public static int TRAINING_BACK_MENU = 0;
		public static int GAME_BACK_MENU = 1;
		public static int NO_ACTION = 2;
		public static int INIT_NEW_GAME = 3;
		public static int LOGOUT = 4;
		public static int EXIT_APP = 5;
	}

	public static interface ReturnRequestCodes {

		public static int TRAINING = 0;
		public static int GAME = 1;
		public static int PACKAGE_SELECTION = 2;
		public static int GAME_SELECTION = 3;
		public static int HOME = 4;
		public static int REGISTER = 5;
		public static int FORGOT_PASSWORD = 6;
		public static int LOGIN_USER_INFO = 7;
		public static int SETTINGS = 8;
		public static int SEARCH_USER = 9;
		public static int MY_FRIENDS = 10;
		public static int COINS_STORE = 11;
		public static int RANKING_VIEW = 12;
		public static int AD_ACTIVITY = 13;
	}

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
