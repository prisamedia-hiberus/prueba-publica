/*
Copyright 2009-2014 Urban Airship Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE URBAN AIRSHIP INC ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL URBAN AIRSHIP INC OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.diarioas.guialigas.activities;

import android.app.Application;
import android.util.Log;

import com.comscore.analytics.comScore;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.notifications.IntentReceiver;
import com.diarioas.guialigas.utils.Defines.ComscoreCode;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.location.UALocationManager;
import com.urbanairship.push.CustomPushNotificationBuilder;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;

public class MyApplication extends Application {

	@Override
	public void onCreate() {

		super.onCreate();
//		COMSCORE
		comScore.setAppContext(this.getApplicationContext());
		comScore.setAppName(getString(R.string.app_name_normalize));

		//URBAN
		AirshipConfigOptions options = AirshipConfigOptions
				.loadDefaultOptions(this);

		// Optionally, customize your config at runtime:
		//
//		options.inProduction = false;

		UAirship.takeOff(this, options);
		Logger.logLevel = Log.VERBOSE;

		// use CustomPushNotificationBuilder to specify a custom layout
		CustomPushNotificationBuilder nb = new CustomPushNotificationBuilder();

		nb.statusBarIconDrawableId = R.drawable.icon;// custom status bar
															// icon

		nb.layout = R.layout.notification;
		nb.layoutIconDrawableId = R.drawable.icon;// custom layout icon
		nb.layoutIconId = R.id.icon;
		nb.layoutSubjectId = R.id.subject;
		nb.layoutMessageId = R.id.message;

		// customize the sound played when a push is received
		// nb.soundUri =
		// Uri.parse("android.resource://"+this.getPackageName()+"/"
		// +R.raw.cat);
		PushManager.enablePush();
		PushPreferences pushPrefs = PushManager.shared().getPreferences();
		pushPrefs.setVibrateEnabled(true);
		pushPrefs.setSoundEnabled(true);
		pushPrefs.setQuietTimeEnabled(true);
		UALocationManager.enableBackgroundLocation();
		UALocationManager.enableForegroundLocation();

		PushManager.shared().setNotificationBuilder(nb);
		PushManager.shared().setIntentReceiver(IntentReceiver.class);

	}
}
