/**
 * 
 */
package com.diarioas.guialigas.activities.general;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.comscore.analytics.comScore;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.imageutils.MemoryReleaseUtils;

public class GeneralFragmentActivity extends GeneralPBSGoogleAdActivity {

	protected RelativeLayout spinner = null;

	protected final int drawables[] = new int[] { R.drawable.spinner_guia0000,
			R.drawable.spinner_guia0001, R.drawable.spinner_guia0002,
			R.drawable.spinner_guia0003, R.drawable.spinner_guia0004,
			R.drawable.spinner_guia0005, R.drawable.spinner_guia0006,
			R.drawable.spinner_guia0007 };

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		comScore.onEnterForeground();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		comScore.onExitForeground();
	}

	/**************** Action Bar methods ******************************/
	/******************************************************************/

	protected void configActionBar() {
		getSupportActionBar().setIcon(R.drawable.home_icn_logoheader);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	protected void hideActionBar() {
		getSupportActionBar().hide();
	}

	/**************** Loading element methods *****************************/
	/********************************************************************/

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
			bitmapDrawable[i] = new BitmapDrawable(this.getApplicationContext()
					.getResources(), BitmapFactory.decodeResource(
					getResources(), drawables[i], o));
			animationDrawable.addFrame(bitmapDrawable[i], 50);
		}
		animationDrawable.setOneShot(false);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			spinner.setBackgroundDrawable(animationDrawable);
		} else {
			spinner.setBackground(animationDrawable);
		}
		(findViewById(R.id.spinnerContent)).setVisibility(View.VISIBLE);
	}

	public void startAnimation() {
		configureloadingAnimationDrawable();
		spinner.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) spinner
						.getBackground();
				if (frameAnimation != null) {
					frameAnimation.start();
				}
			}
		});
	}

	@SuppressLint("NewApi")
	public void stopAnimation() {
		View parent = (findViewById(R.id.spinnerContent));
		if (parent.getVisibility() != View.INVISIBLE) {
			parent.setVisibility(View.INVISIBLE);

			if (spinner.getBackground() != null) {
				AnimationDrawable frameAnimation = (AnimationDrawable) spinner
						.getBackground();
				frameAnimation.stop();

				MemoryReleaseUtils.releaseAnimationDrawables(frameAnimation,
						true);
				int sdk = android.os.Build.VERSION.SDK_INT;
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					spinner.setBackgroundDrawable(null);
				} else {
					spinner.setBackground(null);
				}
			}
		}
	}

	/********************* Ad methods *************************************/
	/*********************************************************************/

	protected void callToAds(String section, boolean inter) {
		if (section != null) {
			if (inter) {
				callToInterAndBannerAction(section);
			} else {
				callToBannerAction(section);
			}
		}
	}
}
