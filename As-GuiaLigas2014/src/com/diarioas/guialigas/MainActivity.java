package com.diarioas.guialigas;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;

import com.comscore.analytics.comScore;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.reader.CookieDAO;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO.RemoteDataDAOListener;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.ComscoreCode;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.imageutils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guialigas.utils.imageutils.bitmapfun.ImageFetcher;

public class MainActivity extends FragmentActivity implements
		RemoteDataDAOListener {

	private static final int STOPSPLASH = 0;
	private static final long DEFAULT_SPLASHTIME = 3000;
	private static long splashTime = DEFAULT_SPLASHTIME;
	// private static final long SPLASHTIME = 3000;

//	private FrameLayout fragmentContainerLoading;
//	private LoadingSplashFragment loadingFragment;
	
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
	private Context mContext;

	// private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		this.mContext = this.getApplicationContext();

		ArrayList<Object> splash = DatabaseDAO.getInstance(mContext)
				.getSplashInfo();
		if (splash.size() > 0) {
			splashTime = ((Integer) splash.get(1)) * 1000;

			ImageCacheParams cacheParams = new ImageCacheParams(this,
					Defines.NAME_CACHE_THUMBS+"splash");
			cacheParams.setMemCacheSizePercent(0.25f);

			ImageFetcher mImageFetcher = new ImageFetcher(this, getResources()
					.getDimensionPixelSize(R.dimen.image_thumb_height));
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			mImageFetcher.addImageCache(fragmentManager, cacheParams);
			mImageFetcher.setImageFadeIn(true);
			mImageFetcher.loadImage((String) splash.get(0),
					((ImageView) findViewById(R.id.publi_splash)));
			
//			ImageDAO.getInstance(this).loadSplashImage((String) splash.get(0),((ImageView) findViewById(R.id.publi_splash)));

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		comScore.onEnterForeground();
		RemoteDataDAO.getInstance(mContext).addListener(this);
		RemoteDataDAO.getInstance(mContext).refreshDatabaseWithNewResults();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
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

		// GCMRegistrar.onDestroy(mContext);

		if (splashHandler != null) {
			splashHandler.removeMessages(STOPSPLASH);
			splashHandler = null;
		}

		// if (timer != null) {
		// timer.cancel();
		// timer = null;
		// }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			CookieDAO.getInstance(getApplicationContext())
					.deleteOnlyAppCookies();
			RemoteDataDAO.getInstance(mContext).removeListener(this);
			RemoteDataDAO.remove();

			finish();
		}
	}

	private void openMainActivity() {

		Intent intent = new Intent(MainActivity.this, HomeActivity.class);
		MainActivity.this.startActivityForResult(intent,
				ReturnRequestCodes.HOME);
		overridePendingTransition(R.anim.push_fade_in, R.anim.push_fade_out);
		// startTimer();
	}

	/************************** TIMER ****************************************/
	// private void startTimer() {
	// if (timer == null) {
	//
	// timer = new Timer();
	// timer.scheduleAtFixedRate(new TimerTask() {
	// @Override
	// public void run() {
	// updateRemoteFile();
	// }
	// }, 0, RequestTimes.TIMER_REMOTEFILE_UPDATE);
	// }
	// }
	protected void updateRemoteFile() {
		Log.d("TIMER", "Updating Remote Fie");
		RemoteDataDAO.getInstance(mContext).loadRemoteSettings();
	}

	/************************** TIMER ****************************************/
//	/************************************* Configuration methods *************************************************/
//	private void configureLoadingView() {
//		Log.d("LOADING", "configureLoadingView");
//
//		// Create the database
//		DatabaseDAO.getInstance(getApplicationContext());
//
//		fragmentContainerLoading = (FrameLayout) findViewById(R.id.spinner_fragment);
//
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//
//		loadingFragment = new LoadingSplashFragment();
//		fragmentTransaction.add(R.id.spinner_fragment, loadingFragment);
//		fragmentTransaction.commit();
//	}
//
//	/***************************************************************************/
//	/** Loading methods **/
//	/***************************************************************************/
//
//	public void startAnimation() {
//		Log.d("LOADING", "startAnimation");
//		fragmentContainerLoading.setVisibility(View.VISIBLE);
//		if (!loadingFragment.isLoadingAnimation())
//			loadingFragment.startAnimation();
//	}
//
//	public void stopAnimation() {
//		Log.d("LOADING", "stopAnimation");
//		fragmentContainerLoading.setVisibility(View.GONE);
//		if (loadingFragment.isLoadingAnimation())
//			loadingFragment.stopAnimation();
//	}

	/************************************* Configuration methods *************************************************/
	

	@Override
	public void onSuccessRemoteconfig() {
		RemoteDataDAO.getInstance(mContext).removeListener(this);
		if (splashHandler != null) {
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, splashTime);
		}

	}

	@Override
	public void onFailureRemoteconfig() {
		RemoteDataDAO.getInstance(mContext).removeListener(this);
		if (splashHandler != null) {
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, splashTime);
		}

	}

	@Override
	public void onFailureNotConnection() {
		RemoteDataDAO.getInstance(mContext).removeListener(this);
		if (splashHandler != null) {
			Message msg = new Message();
			msg.what = STOPSPLASH;
			splashHandler.sendMessageDelayed(msg, splashTime);
		}
	}

}
