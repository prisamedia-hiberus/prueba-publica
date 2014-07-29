package com.diarioas.guialigas.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.imageutils.MemoryReleaseUtils;

@SuppressLint("NewApi")
public class GeneralFragment extends Fragment {

	protected Context mContext;
	protected View generalView;

	protected final int drawables[] = new int[] { R.drawable.spinner_guia0000,
			R.drawable.spinner_guia0001, R.drawable.spinner_guia0002,
			R.drawable.spinner_guia0003, R.drawable.spinner_guia0004,
			R.drawable.spinner_guia0005, R.drawable.spinner_guia0006,
			R.drawable.spinner_guia0007, R.drawable.spinner_guia0008,
			R.drawable.spinner_guia0009 };
	protected RelativeLayout spinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return generalView;
	}

	public void showSectionTopButtons() {
		System.out.println("showSectionTopButtons in");
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
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
			bitmapDrawable[i] = new BitmapDrawable(mContext.getResources(),
					BitmapFactory.decodeResource(mContext.getResources(),
							drawables[i], o));
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
		(generalView.findViewById(R.id.spinnerContent))
				.setVisibility(View.VISIBLE);
	}

	public void startAnimation() {
		if (spinner != null) {
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
	}

	public void stopAnimation() {
		if (spinner != null && generalView != null) {

			// View parent = (View) spinner.getParent();
			View parent = (generalView.findViewById(R.id.spinnerContent));
			if (parent.getVisibility() != View.INVISIBLE) {
				parent.setVisibility(View.INVISIBLE);

				if (spinner.getBackground() != null) {
					AnimationDrawable frameAnimation = (AnimationDrawable) spinner
							.getBackground();
					frameAnimation.stop();

					MemoryReleaseUtils.releaseAnimationDrawables(
							frameAnimation, true);
					int sdk = android.os.Build.VERSION.SDK_INT;
					if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
						spinner.setBackgroundDrawable(null);
					} else {
						spinner.setBackground(null);
					}
				}
			}
		}
	}
}
