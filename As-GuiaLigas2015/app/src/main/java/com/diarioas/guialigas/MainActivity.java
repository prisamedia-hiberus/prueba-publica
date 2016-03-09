package com.diarioas.guialigas;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;

import com.comscore.analytics.comScore;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.reader.CookieDAO;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.OmnitureDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO.RemoteDataDAOListener;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.FileUtils;
import com.urbanairship.google.PlayServicesUtils;

public class MainActivity extends FragmentActivity implements
		RemoteDataDAOListener {

	private static final int STOPSPLASH = 0;
	private static final long DEFAULT_SPLASHTIME = 3000;
	private static long splashTime = DEFAULT_SPLASHTIME;
	private Context mContext;

	/************************** Life Cycle methods ****************************/
	/*************************************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.mContext = this.getApplicationContext();

		ArrayList<Object> splash = DatabaseDAO.getInstance(mContext)
				.getSplashInfo();
		if (splash.size() > 0) {
			splashTime = ((Integer) splash.get(1)) * 1000;

			ImageDAO.getInstance(this.mContext).loadThreePartScreenImage(
					(String) splash.get(0),
					(ImageView) findViewById(R.id.publi_splash));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		RemoteDataDAO.getInstance(this.mContext).addListener(this);
		RemoteDataDAO.getInstance(this.mContext)
				.refreshDatabaseWithNewResults();
		comScore.onEnterForeground();
	}

	@Override
	protected void onPause() {
		super.onPause();
		comScore.onExitForeground();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.splashHandler != null) {
			this.splashHandler.removeMessages(STOPSPLASH);
			this.splashHandler = null;
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (PlayServicesUtils.isGooglePlayStoreAvailable()) {
		    PlayServicesUtils.handleAnyPlayServicesError(this);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			CookieDAO.getInstance(getApplicationContext())
					.deleteOnlyAppCookies();
			RemoteDataDAO.getInstance(this.mContext).removeListener(this);
			RemoteDataDAO.remove();

			finish();
		}
	}

	/************************** Private methods ****************************/
	/***************************************************************************/

	private void openMainActivity() {

		configureCookies();
		Intent intent = new Intent(MainActivity.this, HomeActivity.class);
		MainActivity.this.startActivityForResult(intent,
				ReturnRequestCodes.HOME);
		overridePendingTransition(R.anim.push_fade_in, R.anim.push_fade_out);
	}

	private void configureCookies() {

		//final CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);

		if (RemoteDataDAO.getInstance(getApplicationContext())
				.getGeneralSettings() != null
				&& RemoteDataDAO.getInstance(getApplicationContext())
						.getGeneralSettings().getCookies() != null) {

			HashMap<String, String> cookies = RemoteDataDAO
					.getInstance(getApplicationContext()).getGeneralSettings()
					.getCookies();

			for (String key : cookies.keySet()) {
				String cookieString = key + "=" + cookies.get(key) + ";";
				cookieManager.setCookie(Defines.DOMAIN_COOKIES, cookieString);
			}

			//cookieSyncManager.sync();
		}

		/*
		 * String cookieStringAds = Defines.COOKIE_APP_DIARIOAS_PUBID + "=" +
		 * PBSDFPAdDAO.getInstance(this).getPublisherProvidedId() + ";";
		 * cookieManager.setCookie(Defines.DOMAIN_COOKIES, cookieStringAds);
		 * CookieSyncManager.getInstance().sync();
		 */
	}

	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				openMainActivity();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/************************** Remote info methods ****************************/
	/***************************************************************************/

	@Override
	public void onSuccessRemoteconfig() {
		RemoteDataDAO.getInstance(this.mContext).removeListener(this);
		if (this.splashHandler != null) {
			Message msg = new Message();
			msg.what = STOPSPLASH;
			this.splashHandler.sendMessageDelayed(msg, splashTime);
		}

	}

	@Override
	public void onFailureRemoteconfig() {
		RemoteDataDAO.getInstance(this.mContext).removeListener(this);
		if (this.splashHandler != null) {
			Message msg = new Message();
			msg.what = STOPSPLASH;
			this.splashHandler.sendMessageDelayed(msg, splashTime);
		}

	}

}
