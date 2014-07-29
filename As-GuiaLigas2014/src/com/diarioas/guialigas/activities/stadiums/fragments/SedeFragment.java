package com.diarioas.guialigas.activities.stadiums.fragments;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FragmentAdapter;

public abstract class SedeFragment extends Fragment implements
		OnPageChangeListener {

	protected static final int CIRCLE_ID = 850;
	protected View generalView;
	protected LinearLayout circleIndicator;
	protected ViewPager galleryViewPager;

	protected abstract void configureView();

	protected void configureGallery(int viewPagerId) {
		ArrayList<String> photos = getArguments().getStringArrayList("photos");

		if (photos != null && photos.size() > 0) {
			circleIndicator = (LinearLayout) generalView
					.findViewById(R.id.circleIndicator);

			galleryViewPager = (ViewPager) generalView
					.findViewById(viewPagerId);
			galleryViewPager.setOnPageChangeListener(this);
			galleryViewPager.setAdapter(new FragmentAdapter(this
					.getChildFragmentManager(), getFragments(photos)));

			setInitialPosition();
		} else {
			generalView.findViewById(R.id.galleryContainer).setVisibility(
					View.GONE);
		}
	}

	public void setViewPagerPosition(int pos) {
		galleryViewPager.setCurrentItem(pos, true);
	}

	protected abstract List<Fragment> getFragments(ArrayList<String> photos);

	protected View getCircle(int id) {
		ImageView circle = new ImageView(getActivity().getApplicationContext());
		circle.setId(CIRCLE_ID + id);
		circle.setBackgroundResource(R.drawable.paginador_off);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = DimenUtils.getRegularPixelFromDp(getActivity()
				.getApplicationContext(), 5);
		params.rightMargin = params.leftMargin;
		circle.setLayoutParams(params);
		return circle;
	}

	private void setInitialPosition() {
		galleryViewPager.setCurrentItem(0, true);
		circleIndicator.findViewById(CIRCLE_ID).setBackgroundResource(
				R.drawable.paginador_on);
	}

	/**************** ViewPager Methods *****************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		if (circleIndicator != null) {
			for (int i = 0; i < circleIndicator.getChildCount(); i++) {
				if (i == arg0)
					circleIndicator.findViewById(CIRCLE_ID + i)
							.setBackgroundResource(R.drawable.paginador_on);
				else
					circleIndicator.findViewById(CIRCLE_ID + i)
							.setBackgroundResource(R.drawable.paginador_off);
			}
		}

	}

	/**************** ViewPager Methods *****************************/
}
