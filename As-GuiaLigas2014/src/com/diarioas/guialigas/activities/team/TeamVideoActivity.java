package com.diarioas.guialigas.activities.team;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.reader.PubliDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.prisadigital.realmedia.adlib.PreRollVideo.PreRollVideoListener;

public class TeamVideoActivity extends Activity implements PreRollVideoListener {

	private VideoView currentVideo;
	private String urlVideo;
	private Button publiButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_video_detail_page);

		urlVideo = getIntent().getExtras().getString("url");

		publiButton = (Button) findViewById(R.id.publiButton);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		configVideo();
		callToAds();
//		loadVideo();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		currentVideo.pause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		currentVideo.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		currentVideo.stopPlayback();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		currentVideo.destroyDrawingCache();

		currentVideo = null;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void configVideo() {
		currentVideo = (VideoView) findViewById(R.id.video);

		RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		videoParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		currentVideo.setLayoutParams(videoParams);

		MediaController mediacontroller = new MediaController(this);
		mediacontroller.setAnchorView(currentVideo);
		mediacontroller.setClickable(false);
		currentVideo.setMediaController(mediacontroller);

	}

	private void loadVideoAd(String videoUrl, final String clickUrl) {

		currentVideo.setVideoURI(Uri.parse(videoUrl));
		currentVideo.requestFocus();
		final RotateAnimation rotateAnimation = configureLoadingSplash();
		currentVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				stopSplashAnimating(rotateAnimation);

			}
		});
		currentVideo.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				loadVideo();
			}
		});

		currentVideo.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				loadVideo();
				return false;
			}
		});

		currentVideo.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(clickUrl));
				startActivity(i);
				return false;
			}
		});
		publiButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadVideo();

			}
		});
		publiButton.setVisibility(View.VISIBLE);
		currentVideo.start();
	}

	private void loadVideo() {
		final String sectionName = getIntent().getExtras().getString("section");
		final String subsection = getIntent().getExtras().getString(
				"subsection");

		publiButton.setVisibility(View.GONE);
		currentVideo.setVideoURI(Uri.parse(urlVideo));
		currentVideo.requestFocus();
		final RotateAnimation rotateAnimation = configureLoadingSplash();
		currentVideo.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				stopSplashAnimating(rotateAnimation);

			}
		});
		currentVideo.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (sectionName != null && subsection != null) {
					HashMap<String, String> info = new HashMap<String, String>();
					info.put("event12", sectionName);
					StatisticsDAO.getInstance(getApplicationContext())
							.sendStatisticsAction(getApplication(),
									Omniture.SECTION_VIDEOS, subsection, null,
									null, null, null, info);
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
		if (sectionName != null && subsection != null) {
			HashMap<String, String> info = new HashMap<String, String>();
			info.put("event11", sectionName);
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsAction(getApplication(),
							Omniture.SECTION_VIDEOS, subsection, null, null,
							null, null, info);
		}
		currentVideo.setOnTouchListener(null);
		currentVideo.start();
	}

	private void callToAds() {
		PubliDAO.getInstance(getApplicationContext()).showPreRoll(
				NativeAds.AD_VIDEOS + "/" + NativeAds.AD_DETAIL, this);
	}

	private RotateAnimation configureLoadingSplash() {

		ImageView splashIcon = (ImageView) findViewById(R.id.splashVideo);
		RotateAnimation rotateAnimation = null;
		if (splashIcon != null) {
			rotateAnimation = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setInterpolator(new LinearInterpolator());
			rotateAnimation.setDuration(1000);
			rotateAnimation.setRepeatCount(Animation.INFINITE);
			rotateAnimation.setRepeatMode(Animation.RESTART);
			splashIcon.setVisibility(View.VISIBLE);
			splashIcon.startAnimation(rotateAnimation);

		}
		return rotateAnimation;
	}

	private void stopSplashAnimating(RotateAnimation rotateAnimation) {

		ImageView splashIcon = (ImageView) findViewById(R.id.splashVideo);
		splashIcon.setVisibility(View.GONE);

		if (rotateAnimation != null) {
			rotateAnimation.cancel();
			splashIcon.clearAnimation();
		}
	}

	/********************************************** PreRoll Listeners Method *******************************************************************/

	@Override
	public void onVideoRetreived(String videoUrl, String clickUrl) {
		if (!clickUrl.startsWith("http://") && !clickUrl.startsWith("https://"))
			clickUrl = "http://" + clickUrl;
		// Log.d("PUBLI", "Showing the PreRoll: " + videoUrl + " Clickurl: "+
		// clickUrl);
		loadVideoAd(videoUrl, clickUrl);
	}

	@Override
	public void onVideoNotFound() {
		Log.e("PUBLI", "PreRoll Error");
		loadVideo();

	}

}
