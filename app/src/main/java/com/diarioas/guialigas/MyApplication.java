package com.diarioas.guialigas;

import android.app.Application;

import com.comscore.analytics.comScore;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;

import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application {

	static MyApplication instance;
	@Override
	public void onCreate() {
		super.onCreate();
		setInstance(this);
		/************* URBAN AIRSHIP ****************/
		AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
		UAirship.takeOff(this, options);
        UAirship.shared().getPushManager().setUserNotificationsEnabled(true);
		
		/************* COMSCORE ****************/
		comScore.setAppContext(this.getApplicationContext());
		comScore.setAppName(getString(R.string.app_name_normalize));
	}

	private static synchronized void setInstance(MyApplication object) {
		instance = object;
	}
	public static MyApplication get() {
		return instance;
	}

}

