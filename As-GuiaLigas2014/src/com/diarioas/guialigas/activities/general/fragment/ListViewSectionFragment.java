package com.diarioas.guialigas.activities.general.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.DimenUtils;

public abstract class ListViewSectionFragment extends SectionFragment {

	// private ArrayList<Stadium> stadiums;
	protected BaseAdapter adapter;
	protected ArrayList<?> array;

	protected ListView contentListView;
	private SwipeRefreshLayout swipeRefreshLayout = null;

	protected Section section;
	protected String competitionId;
	protected SimpleDateFormat dateFormatPull;
	protected View headerView;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_stadium_section,
				container, false);
		return generalView;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@Override
	protected void configureView() {
		section = (Section) getArguments().getSerializable("section");
		competitionId = getArguments().getString("competitionId");

		dateFormatPull = new SimpleDateFormat(DateFormat.PULL_FORMAT,
				Locale.getDefault());

		callToOmniture();

	}

	protected void configureListView() {
		configureSwipeLoader();

		contentListView.setClickable(false);
		contentListView.setDivider(null);
		contentListView.setClickable(false);
		contentListView.setCacheColorHint(0);
		int regularPixelFromDp = DimenUtils.getRegularPixelFromDp(mContext, 8);
		contentListView.addFooterView(getGapView(regularPixelFromDp));
		contentListView.addHeaderView(getGapView(regularPixelFromDp));
		contentListView.setAdapter(adapter);
	}

	private void configureSwipeLoader() {
//		this.swipeRefreshLayout = (SwipeRefreshLayout) generalView
//				.findViewById(R.id.swipe_stadiumsContent);
//		this.swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//
//			@Override
//			public void onRefresh() {
//				loadInformation();
//			}
//		});
//		this.swipeRefreshLayout.setColorSchemeResources(R.color.black,
//				R.color.red, R.color.white);
	}

	protected void stopAnimation() {
		((HomeActivity) getActivity()).stopAnimation();
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	/***************************************************************************/

	protected void loadData() {
		loadData(false);
	}

	protected void loadOnFailure() {
		boolean pullEnabled;
		if (!(array != null && array.size() > 0)) {
			headerView = getErrorContainer();
			pullEnabled = true;
		} else {
			pullEnabled = false;
		}

		loadData(pullEnabled);

	}

	private void loadData(boolean pullEnabled) {
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setRefreshing(false);
		}
		adapter.notifyDataSetChanged();
		stopAnimation();
	}

	protected Animation getAnimation(int position, View convertView) {
		int durationMillis = 400;
		if (position <= 10) {
			durationMillis += 200 * position;
		} else {
			durationMillis += 1000;
		}
		// TranslateAnimation traslacion = new TranslateAnimation(0, 0, -800,
		// 0);
		Animation traslacion = AnimationUtils.loadAnimation(mContext,
				R.anim.slide_in_down_photo);

		traslacion.setDuration(durationMillis);
		traslacion.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// view.setVisibility(View.VISIBLE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}

		});

		return traslacion;
	}

	protected abstract void goToDetail(Object id);

	protected abstract void callToOmniture();

}
