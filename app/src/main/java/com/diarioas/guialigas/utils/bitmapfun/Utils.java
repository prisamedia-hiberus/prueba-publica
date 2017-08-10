/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diarioas.guialigas.utils.bitmapfun;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.diarioas.guialigas.MyApplication;
import com.diarioas.guialigas.R;

import java.util.List;


/**
 * Class containing some static utility methods.
 */
public class Utils {
	private Utils() {
	};


	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static void openExternalURL(Context context, String url){

		try{
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url.toLowerCase()));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PackageManager manager = context.getPackageManager();
			List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
			if (infos.size() > 0) {
				context.startActivity(intent);
			} else {
				Toast.makeText(context,
						context.getString(R.string.error_invalid_url), Toast.LENGTH_LONG).show();
			}
		}catch (Exception e){
			Toast.makeText(context,
					context.getString(R.string.error_toast_browser), Toast.LENGTH_LONG).show();
		}
	}

	public static boolean isAppRunning() {
		ActivityManager activityManager =
				(ActivityManager) MyApplication.get().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> services = activityManager
				.getRunningTasks(Integer.MAX_VALUE);
		boolean isActivityFound = false;

		if (services.get(0).topActivity.getPackageName()
				.equalsIgnoreCase(MyApplication.get().getPackageName())) {
			isActivityFound = true;
		}
		return isActivityFound;
	}
}
