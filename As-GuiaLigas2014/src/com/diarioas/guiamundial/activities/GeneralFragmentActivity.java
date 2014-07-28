/**
 * 
 */
package com.diarioas.guiamundial.activities;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.utils.imageutils.MemoryReleaseUtils;
import com.prisadigital.realmedia.adlib.AdView;

/**
 * @author robertosanchez
 * 
 */
@SuppressLint("NewApi")
public class GeneralFragmentActivity extends SherlockFragmentActivity {

	private AdView banner;

	protected final int drawables[] = new int[] { R.drawable.spinner_guia0000,
			R.drawable.spinner_guia0001, R.drawable.spinner_guia0002,
			R.drawable.spinner_guia0003, R.drawable.spinner_guia0004,
			R.drawable.spinner_guia0005, R.drawable.spinner_guia0006,
			R.drawable.spinner_guia0007 };
	protected RelativeLayout spinner;

	protected void configActionBar() {
		getSupportActionBar().setIcon(R.drawable.logo_home_menulateral);

		getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
	}

	/**************** Metodos de Spinner *****************************/
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
		// ((View) spinner.getParent()).setVisibility(View.VISIBLE);
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

	public void stopAnimation() {
		// ViewParent parent = spinner.getParent();
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

	/***********************************************************************************/

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		banner = null;
	}

	/**
	 * Call To publi
	 * 
	 * @param section
	 */
	protected void callToAds(String section, boolean inter) {
		// if (section == null)
		// return;
		// // Delete old banner
		// if (banner != null) {
		// banner.removeBanner();
		// ((ViewGroup) this.findViewById(R.id.publiContent))
		// .removeView(banner);
		// }
		//
		// // Create new Banner
		// banner = PubliDAO.getInstance(getApplicationContext()).getBanner(
		// section);
		//
		// // Set the new banner
		// if (banner != null)
		// ((ViewGroup) this.findViewById(R.id.publiContent)).addView(banner);
		//
		// if (inter)
		// // shows interstitial
		// PubliDAO.getInstance(getApplicationContext()).displayInterstitial(
		// section);
	}
}
