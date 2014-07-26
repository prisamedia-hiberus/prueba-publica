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

package com.diarioas.guiamundial.utils.bitmapfun;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

import com.diarioas.guiamundial.MainActivity;
import com.diarioas.guiamundial.activities.carrusel.CarruselDetailActivity;
import com.diarioas.guiamundial.activities.home.HomeActivity;
import com.diarioas.guiamundial.activities.player.PlayerActivity;
import com.diarioas.guiamundial.activities.player.comparator.PlayerComparatorStepFirstActivity;
import com.diarioas.guiamundial.activities.player.comparator.PlayerComparatorStepThirdActivity;
import com.diarioas.guiamundial.activities.stadiums.StadiumsDetailActivity;
import com.diarioas.guiamundial.activities.stadiums.StadiumsPhotoGalleryActivity;
import com.diarioas.guiamundial.activities.team.TeamActivity;

/**
 * Class containing some static utility methods.
 */
public class Utils {
	private Utils() {
	};

	@TargetApi(11)
	public static void enableStrictMode() {
		if (Utils.hasGingerbread()) {
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyLog();
			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyLog();

			if (Utils.hasHoneycomb()) {
				threadPolicyBuilder.penaltyFlashScreen();
				vmPolicyBuilder
						.setClassInstanceLimit(MainActivity.class, 1)
						.setClassInstanceLimit(HomeActivity.class, 6)
						.setClassInstanceLimit(TeamActivity.class, 3)
						.setClassInstanceLimit(PlayerActivity.class, 1)
						.setClassInstanceLimit(
								PlayerComparatorStepFirstActivity.class, 1)
						.setClassInstanceLimit(
								PlayerComparatorStepThirdActivity.class, 1)
						.setClassInstanceLimit(StadiumsDetailActivity.class, 1)
						.setClassInstanceLimit(CarruselDetailActivity.class, 1)
						.setClassInstanceLimit(
								StadiumsPhotoGalleryActivity.class, 1);
			}
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	}

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
}
