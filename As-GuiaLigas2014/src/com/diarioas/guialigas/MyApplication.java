package com.diarioas.guialigas;

import android.app.Application;

import com.comscore.analytics.comScore;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		/************* URBAN AIRSHIP ****************/
		AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
		UAirship.takeOff(this, options);
        UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
		
		/************* COMSCORE ****************/
		comScore.setAppContext(this.getApplicationContext());
		comScore.setAppName(getString(R.string.app_name_normalize));
	}
}

