package com.diarioas.guialigas.activities.videos;

import java.util.HashMap;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.comscore.analytics.comScore;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralPBSGoogleAdPrerrolActivity;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;

public class VideoActivity extends GeneralPBSGoogleAdPrerrolActivity {

	private VideoView currentVideo = null;
	private String urlVideo = null;
	private String sectionName = null;
	private String subsectionName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_video_detail_page);

		urlVideo = getIntent().getExtras().getString("url");
		sectionName = getIntent().getExtras().getString("section");
		subsectionName = getIntent().getExtras().getString("subsection");
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (currentVideo.isPlaying()) {
			currentVideo.pause();
		}
		comScore.onExitForeground();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (contentStarted)
			currentVideo.start();
		comScore.onEnterForeground();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (currentVideo != null) {
			currentVideo.stopPlayback();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (currentVideo != null) {
			currentVideo.destroyDrawingCache();
			currentVideo = null;
		}
		removeAnimation();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void configVideo() {
		super.configVideo();
		currentVideo = (VideoView) findViewById(R.id.video);

		if (currentVideo != null) {
			RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			videoParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			currentVideo.setLayoutParams(videoParams);

			MediaController mediacontroller = new MediaController(this);
			mediacontroller.setAnchorView(currentVideo);
			mediacontroller.setClickable(false);
			currentVideo.setMediaController(mediacontroller);
		}
	}

	@Override
	public void playVideo() {
		super.playVideo();

		if (currentVideo == null) {
			configVideo();
		}

		currentVideo.setVideoURI(Uri.parse(urlVideo));
		currentVideo.requestFocus();
		// final RotateAnimation rotateAnimation = configureLoadingSplash();
		currentVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				removeAnimation();
				hideAnimationElements();
			}
		});
		currentVideo.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (sectionName != null) {
					HashMap<String, String> info = new HashMap<String, String>();
					info.put("event12", sectionName);
					StatisticsDAO.getInstance(getApplicationContext())
							.sendStatisticsAction(getApplication(),
									Omniture.SECTION_VIDEOS, subsectionName,
									null, null, null, null, info);
				}
				onBackPressed();
			}
		});

		currentVideo.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				onBackPressed();
				return false;
			}
		});
		if (sectionName != null) {
			HashMap<String, String> info = new HashMap<String, String>();
			info.put("event11", sectionName);
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsAction(getApplication(),
							Omniture.SECTION_VIDEOS, subsectionName, null,
							null, null, null, info);
		}
		currentVideo.setOnTouchListener(null);
		currentVideo.start();
	}

	@Override
	public void finishPrerrolAction() {
		super.finishPrerrolAction();
		if (sectionName != null) {
			HashMap<String, String> info = new HashMap<String, String>();
			info.put("event14", sectionName);
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsAction(getApplication(),
							Omniture.SECTION_VIDEOS, subsectionName, null,
							null, null, null, info);
		}
	}

	@Override
	public void onPrerolStarted() {
		super.onPrerolStarted();
		isAdStarted = true;
		isAdPlaying = true;
		if (sectionName != null) {
			HashMap<String, String> info = new HashMap<String, String>();
			info.put("event13", sectionName);
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsAction(getApplication(),
							Omniture.SECTION_VIDEOS, subsectionName, null,
							null, null, null, info);
		}
	}

	@Override
	public String getAdSection() {
		return NativeAds.PUBLI_PRERROL_UNIT;
	}
}
