package com.diarioas.guiamundial.activities.general.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.flip.FlipViewController.ViewFlipListener;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.home.HomeActivity;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.diarioas.guiamundial.utils.imageutils.MemoryReleaseUtils;

public abstract class FlipSectionFragment extends SectionFragment implements
		ViewFlipListener {

	protected final int drawables[] = new int[] { R.drawable.spinner_guia0000,
			R.drawable.spinner_guia0001, R.drawable.spinner_guia0002,
			R.drawable.spinner_guia0003, R.drawable.spinner_guia0004,
			R.drawable.spinner_guia0005, R.drawable.spinner_guia0006,
			R.drawable.spinner_guia0007 };

	protected RelativeLayout playerSpinner;
	protected View firstView;

	protected FlipViewController flipView;

	protected int numGrades;
	protected TextView titlePulltoRefresh;
	protected TextView datePulltoRefresh;

	protected View errorContainer;

	protected ArrayList<?> array;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (flipView != null)
			flipView.onResume();
		((HomeActivity) getActivity()).setExitTaskEarlyAllImageFetcher();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (flipView != null)
			flipView.onPause();
		((HomeActivity) getActivity()).setExitTaskEarlyAllImageFetcher();
		((HomeActivity) getActivity()).setFlushCacheAllImageFetcher();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		flipView.destroyDrawingCache();
		flipView = null;
		// ((HomeActivity) getActivity()).setCloseCAcheAllImageFetcher();
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void configureView() {
		playerSpinner = (RelativeLayout) generalView
				.findViewById(R.id.playerSpinner);

		titlePulltoRefresh = (TextView) generalView
				.findViewById(R.id.titlePulltoRefresh);
		FontUtils.setCustomfont(mContext, titlePulltoRefresh,
				FontTypes.HELVETICANEUEBOLD);
		datePulltoRefresh = (TextView) generalView
				.findViewById(R.id.datePulltoRefresh);
		FontUtils.setCustomfont(mContext, datePulltoRefresh,
				FontTypes.HELVETICANEUE);

		numGrades = (int) Math.ceil(100 / drawables.length);
		errorContainer = getErrorContainer();
		errorContainer.setVisibility(View.INVISIBLE);
		((RelativeLayout) generalView).addView(errorContainer);
	}

	public void loadData(boolean animation) {
		errorContainer.setVisibility(View.INVISIBLE);
		flipView.onDeFrost();
		if (animation) {
			AlphaAnimation alpha = new AlphaAnimation(1, 0);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			alpha.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					updateData();
					AlphaAnimation alpha = new AlphaAnimation(0, 1);
					alpha.setDuration(300);
					alpha.setFillAfter(true);
					flipView.startAnimation(alpha);

				}

			});
			flipView.startAnimation(alpha);
		} else {
			updateData();
		}
	}

	protected abstract void updateData();

	/***************************************************************************/
	/** Spinner Methods **/
	/***************************************************************************/

	public void startPlayerAnimation() {
		configureloadingAnimationDrawable();
		playerSpinner.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) playerSpinner
						.getBackground();
				if (frameAnimation != null) {
					frameAnimation.start();
				}
			}
		});
	}

	@SuppressLint("NewApi")
	private void configureloadingAnimationDrawable() {
		AnimationDrawable animationDrawable = new AnimationDrawable();

		BitmapDrawable[] bitmapDrawable = new BitmapDrawable[drawables.length];

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inScaled = false;
		o.inDither = false; // Disable Dithering mode
		o.inPurgeable = true; // Tell to gc that whether it needs free memory,
								// the Bitmap can be cleared
		for (int i = 0; i < drawables.length; i++) {
			bitmapDrawable[i] = new BitmapDrawable(mContext.getResources(),
					BitmapFactory.decodeResource(getResources(), drawables[i],
							o));
			animationDrawable.addFrame(bitmapDrawable[i], 50);
		}
		animationDrawable.setOneShot(false);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			playerSpinner.setBackgroundDrawable(animationDrawable);
		} else {
			playerSpinner.setBackground(animationDrawable);
		}
		// ((View) playerSpinner.getParent()).setVisibility(View.VISIBLE);

	}

	@SuppressLint("NewApi")
	public void stopPlayerAnimation(String newsDate) {
		titlePulltoRefresh.setText(getString(R.string.ptr_pull_to_refresh));
		datePulltoRefresh.setText(getString(R.string.ptr_last_updated)
				+ newsDate);
		// ViewParent parent = spinner.getParent();
		View parent = (generalView.findViewById(R.id.playerSpinnerContent));
		if (parent.getVisibility() != View.INVISIBLE) {
			parent.setVisibility(View.INVISIBLE);

			if (playerSpinner.getBackground() != null
					&& playerSpinner.getBackground() instanceof AnimationDrawable) {
				AnimationDrawable frameAnimation = (AnimationDrawable) playerSpinner
						.getBackground();
				frameAnimation.stop();

				MemoryReleaseUtils.releaseAnimationDrawables(frameAnimation,
						true);
				int sdk = android.os.Build.VERSION.SDK_INT;
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					playerSpinner.setBackgroundDrawable(null);
				} else {
					playerSpinner.setBackground(null);
				}
			}
		}
	}

	/***************************************************************************/
	/** ViewFlipListener methods **/
	/***************************************************************************/
	@Override
	public void onViewFlipped(View view, int position) {
		Log.d("FLIP", "Flipped..." + position);

	}

	@Override
	public void movetTo(int anglePageIndex, float accumulatedAngle,
			boolean onActionUp) {

		if (anglePageIndex == 0) {
			if (firstView != null && firstView.getVisibility() == View.VISIBLE) {
				if (firstView != null)
					firstView.setVisibility(View.INVISIBLE);

				(generalView.findViewById(R.id.playerSpinnerContent))
						.setVisibility(View.VISIBLE);

			}
			if (accumulatedAngle <= 100) {
				setPlayerAnim(accumulatedAngle);

				if (accumulatedAngle >= 90) {
					titlePulltoRefresh
							.setText(getString(R.string.ptr_release_to_refresh));
					if (onActionUp) {
						startPlayerAnimation();
						// Reload
						Log.d("NEWS", "Updating Rss...");
						titlePulltoRefresh
								.setText(getString(R.string.ptr_refreshing));
						reloadData();
						flipView.onFrezeAnimation();

					}
				} else {
					titlePulltoRefresh
							.setText(getString(R.string.ptr_pull_to_refresh));
					if (accumulatedAngle <= 5) {
						if (firstView != null)
							firstView.setVisibility(View.VISIBLE);

						(generalView.findViewById(R.id.playerSpinnerContent))
								.setVisibility(View.INVISIBLE);
						stopPlayerAnimation();
					}
				}

			}
		} else {
			if (firstView != null
					&& firstView.getVisibility() == View.INVISIBLE) {
				if (firstView != null)
					firstView.setVisibility(View.VISIBLE);

				(generalView.findViewById(R.id.playerSpinnerContent))
						.setVisibility(View.INVISIBLE);
				stopPlayerAnimation();
			}
		}

	}

	protected abstract void stopPlayerAnimation();

	private void setPlayerAnim(float accumulatedAngle) {
		int div;
		if (accumulatedAngle < numGrades)
			div = 0;
		else
			div = (((int) accumulatedAngle) / numGrades) - 1;

		// Log.d("FLIP", "Angle: " + accumulatedAngle + " numGrades: " +
		// numGrades
		// + " div: " + div);

		playerSpinner.setBackgroundResource(drawables[div]);
	}

	protected abstract void reloadData();

}
