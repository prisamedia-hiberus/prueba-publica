package com.diarioas.guialigas.activities.general;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.diarioas.guialigas.BuildConfig;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.prisa.google_ads.ads.PBSDFPAdDAO;
import com.prisa.google_ads.ads.PBSDFPAdDAO.AdDAOPrerolListener;
import com.prisa.google_ads.player.DemoPlayer;
import com.prisa.google_ads.player.TrackingVideoView.CompleteCallback;

public class GeneralPBSGoogleAdPrerrolActivity extends Activity implements
		AdDAOPrerolListener, CompleteCallback {

	private DemoPlayer demoplayer = null;
	private AdsLoader adsLoader = null;
	private AdsManager adsManager = null;

	protected boolean isAdStarted = false;
	protected boolean isAdPlaying = false;
	protected boolean contentStarted = false;

	protected boolean needToCallAds = true;
	protected boolean needToFinishManuallyAds = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private final int DELAY_CALL_ADS = 500;

	@Override
	protected void onStart() {
		super.onStart();

		if (needToCallAds) {
			needToCallAds = false;
			configureLoadingImage();
			configVideo();

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					callToAds();
				}
			}, DELAY_CALL_ADS);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (demoplayer != null && isAdPlaying) {
			demoplayer.pauseContent();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PBSDFPAdDAO.getInstance(this).removePrerolListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (demoplayer != null && needToFinishManuallyAds) {
			demoplayer.stopAd();
			demoplayer.getUiContainer().setVisibility(View.GONE);

			if (adsLoader != null)
				adsLoader.contentComplete();

			playVideo();
		}
	}

	public void configVideo() {
		demoplayer = new DemoPlayer(this);
		demoplayer.setCompletionCallback(this);

		if (demoplayer.getParent() != null) {
			((ViewGroup) demoplayer.getParent()).removeView(demoplayer);
		}

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		demoplayer.setLayoutParams(params);

		RelativeLayout general_video_view = (RelativeLayout) findViewById(R.id.video_content_layout);
		if (general_video_view != null) {
			general_video_view.addView(demoplayer);
		}
	}

	private void callToAds() {

		if (adsLoader != null) {
			adsLoader.contentComplete();
		}
		String section = getAdSection();

		PBSDFPAdDAO.getInstance(this).addPrerolListener(this);
		adsLoader = PBSDFPAdDAO.getInstance(this).createVideoPreroll(
				demoplayer, demoplayer.getUiContainer(), section, BuildConfig.AD_KEY);
	}

	public String getAdSection() {
		return null;
	}

	public void playVideo() {
		needToFinishManuallyAds = false;
		PBSDFPAdDAO.getInstance(this).removePrerolListener(this);
		if (demoplayer != null) {
			RelativeLayout general_video_view = (RelativeLayout) findViewById(R.id.video_content_layout);
			if (general_video_view != null) {
				general_video_view.removeView(demoplayer);
			}
		}
		if (adsLoader != null)
			adsLoader.contentComplete();

		contentStarted = true;
	}

	/**************** Loading View *******************/

	private RotateAnimation rotateAnimationFirst = null;

	private void configureLoadingImage() {
		ImageView iconLoadingVideo = (ImageView) findViewById(R.id.iconLoadingVideo);
		if (iconLoadingVideo != null) {
			rotateAnimationFirst = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimationFirst.setInterpolator(new LinearInterpolator());
			rotateAnimationFirst.setDuration(1000);
			rotateAnimationFirst.setFillAfter(true);
			rotateAnimationFirst.setFillEnabled(true);
			rotateAnimationFirst.setRepeatCount(Animation.INFINITE);
			rotateAnimationFirst.setRepeatMode(Animation.RESTART);
			iconLoadingVideo.startAnimation(rotateAnimationFirst);
		}
	}

	public void showAnimationElements() {
		if (findViewById(R.id.iconLoadingVideo) != null
				&& findViewById(R.id.iconLoadingBack) != null) {
			ImageView iconLoadingVideo = (ImageView) findViewById(R.id.iconLoadingVideo);
			ImageView iconLoadingBack = (ImageView) findViewById(R.id.iconLoadingBack);

			iconLoadingVideo.startAnimation(rotateAnimationFirst);

			iconLoadingVideo.setVisibility(View.VISIBLE);
			iconLoadingBack.setVisibility(View.VISIBLE);
		}
	}

	public void hideAnimationElements() {
		if (findViewById(R.id.iconLoadingVideo) != null
				&& findViewById(R.id.iconLoadingBack) != null) {
			ImageView iconLoadingVideo = (ImageView) findViewById(R.id.iconLoadingVideo);
			ImageView iconLoadingBack = (ImageView) findViewById(R.id.iconLoadingBack);

			iconLoadingVideo.setVisibility(View.GONE);
			iconLoadingBack.setVisibility(View.GONE);
			iconLoadingVideo.clearAnimation();
		}
	}

	public void removeAnimation() {
		ImageView iconLoadingVideo = (ImageView) findViewById(R.id.iconLoadingVideo);
		if (iconLoadingVideo != null) {
			iconLoadingVideo.clearAnimation();
		}
	}

	/************** Prerrol methods **************************/

	@Override
	public void onPrerolErrorListener(String message) {
		if (demoplayer != null && demoplayer.isContentPlaying()) {
			if (adsLoader != null)
				adsLoader.contentComplete();
		} else {
			playVideo();
		}
	}

	@Override
	public void onPrerolAdsManagerLoaded(AdsManager adsManager) {
		this.adsManager = adsManager;
	}

	@Override
	public void onPrerolLoaded() {
		adsManager.start();
	}

	@Override
	public void onPrerolSkipped() {
	}

	@Override
	public void onPrerolResumeRequested() {

		if (contentStarted)
			demoplayer.resumeContent();

		if (adsLoader != null)
			adsLoader.contentComplete();

		playVideo();
	}

	@Override
	public void onPrerolResumed() {
		isAdPlaying = true;
	}

	@Override
	public void onPrerolPauseRequested() {
		if (contentStarted) {
			demoplayer.pauseContent();
		}
	}

	@Override
	public void onPrerolPaused() {
		isAdPlaying = false;
	}

	@Override
	public void onPrerolStarted() {
		isAdStarted = true;
		isAdPlaying = true;
		needToFinishManuallyAds = true;

		hideAnimationElements();
	}

	@Override
	public void onPrerolFirstdQuarter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrerolSecondQuarter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrerolThirdQuarter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrerolCompleted() {
		isAdStarted = false;
		isAdPlaying = false;

		showAnimationElements();
	}

	@Override
	public void onPrerolDestroyAd() {

		if (adsLoader != null) {
			adsLoader.contentComplete();
			adsLoader = null;
		}
	}

	/*************************************************************************************************/
	/** CompleteCallback Listener **/
	/*************************************************************************************************/

	@Override
	public void onComplete() {

		if (isAdPlaying) {
			if (adsLoader != null)
				adsLoader.contentComplete();
			finishPrerrolAction();
		}
	}

	public void finishPrerrolAction() {

	}

}
