package com.diarioas.guialigas.utils.scroll;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.diarioas.guialigas.utils.scroll.BounceScrollView.onChangeScrollListener;

public class ParallaxScrollView extends BounceScrollView implements
		onChangeScrollListener {

	public interface onParallaxListener {

		void onParallaxEfect(float currentPercentScale);

	}

	private float currentPercentScale = (float) 1.0;
	private float oldPercent = (float) 0.0;
	private static final float GAP = (float) 0.06;

	private ArrayList<onParallaxListener> listeners;
	private View imageInBackground;

	public ParallaxScrollView(Context context) {
		super(context);
		init();
	}

	public ParallaxScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		this.setAllowBounceBottom(false);
		listeners = new ArrayList<onParallaxListener>();
		setOnChangeScrollListener(this);
		setBottomLimit(60);

	}

	public void setImageInBackground(View imageView) {
		this.imageInBackground = imageView;
	}

	/**************** BounceScrollView delegate methods ******************/

	@Override
	public void onChangeScroll(int x, int y, int oldX, int oldY) {
		float percent = (-y) / 1;
		int div = 100;
		float scale;
		if (y < 0) {

			if (percent > 0) {
				if (percent > oldPercent) {
					scale = currentPercentScale + percent / div;
					scaleImage(scale);
					currentPercentScale += (percent - oldPercent) / div;
					// Log.d("SCROLL", "ScaleMax--> Y: " + y + " OldY: " + oldY
					// + " percent: " + percent + " oldPercent: "
					// + oldPercent + " currentPercentScale: "
					// + currentPercentScale);
				} else if (percent < oldPercent) {
					scale = currentPercentScale - percent / div;
					// avoid currentPercent will be less tan 1.0
					if (scale < 1) {
						scale = 1;
					}

					scaleImage(scale);
					if (percent > 1)
						currentPercentScale -= (oldPercent - percent) / div;
					else
						currentPercentScale = 1;

					// Log.d("SCROLL", "ScaleMin--> Y: " + y + " OldY: " + oldY
					// + " percent: " + percent + " oldPercent: "
					// + oldPercent + " currentPercentScale: "
					// + currentPercentScale);
				}
				informToListeners(currentPercentScale);
				oldPercent = percent;
			}
		}

	}

	/**************** BounceScrollView delegate methods ******************/

	private void scaleImage(float scale) {
		ScaleAnimation scaleAnimation = new ScaleAnimation(currentPercentScale,
				scale, currentPercentScale, scale, Animation.RELATIVE_TO_SELF,
				(float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.1);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setDuration(20);
		imageInBackground.startAnimation(scaleAnimation);

	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(onParallaxListener listener) {
		if (listeners != null) {
			listeners.add(listener);
		}
	}

	public void removeListener(onParallaxListener listener) {
		if (this.listeners != null) {
			if (listeners.contains(listener)) {
				listeners.remove(listener);
			}
		}
	}

	private void informToListeners(float currentPercentScale) {
		for (onParallaxListener listener : listeners) {
			listener.onParallaxEfect(currentPercentScale);
		}

	}
}
