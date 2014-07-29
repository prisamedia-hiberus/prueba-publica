package com.diarioas.guiamundial.activities.general.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.diarioas.guiamundial.R;

public class LoadingSplashFragment extends Fragment {

	private static final int VELOCITY = 500;

	private boolean isLoadingAnimation = false;

	private Context mContext;
	private View generalView;

	private ImageView splashBackground;
	private ImageView splashMessage01;
	private ImageView splashMessage02;
	private ImageView splashShadow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		generalView = inflater.inflate(R.layout.loading_splash_fragment,
				container, false);
		return generalView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configureView();
		startAnimation();
	}

	private void configureView() {
		splashBackground = (ImageView) generalView
				.findViewById(R.id.splashBackground);
		splashMessage01 = (ImageView) generalView
				.findViewById(R.id.splashMessage01);
		splashMessage02 = (ImageView) generalView
				.findViewById(R.id.splashMessage02);
		splashShadow = (ImageView) generalView.findViewById(R.id.splashShadow);
//		generalView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startAnimation();
//
//			}
//		});
	}

	public void startAnimation() {
		generalView.setVisibility(View.VISIBLE);
		startAnimation(splashBackground, R.anim.splash_background, new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				startAnimation(splashMessage01, R.anim.splash_message01);
				startAnimation(splashMessage02, R.anim.splash_message02);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
				splashBackground.setVisibility(View.VISIBLE);
			}
		});

		startAnimation(splashShadow, R.anim.splash_shadow);
	}


	private void startAnimation(final ImageView view, int animId) {
		startAnimation(view, animId, new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
				view.setVisibility(View.VISIBLE);
			}
		});
	}
	
	private void startAnimation(final ImageView view,
			int animId, AnimationListener animationListener) {
		Animation anim = AnimationUtils.loadAnimation(mContext, animId);
		anim.setAnimationListener(animationListener);
		anim.setFillAfter(true);
		anim.setRepeatCount(0);
		anim.setDuration(VELOCITY);
		view.startAnimation(anim);
	}


	public void stopAnimation() {
	}

	public boolean isLoadingAnimation() {
		return isLoadingAnimation;
	}

	public void setLoadingAnimation(boolean isLoadingAnimation) {
		this.isLoadingAnimation = isLoadingAnimation;
	}

}
