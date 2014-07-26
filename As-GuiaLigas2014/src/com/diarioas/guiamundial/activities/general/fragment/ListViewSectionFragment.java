package com.diarioas.guiamundial.activities.general.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.home.HomeActivity;
import com.diarioas.guiamundial.dao.model.general.Section;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.Defines.DateFormat;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public abstract class ListViewSectionFragment extends SectionFragment {

	// private ArrayList<Stadium> stadiums;
	protected BaseAdapter adapter;
	protected ArrayList<?> array;
	protected PullToRefreshListView pullToRefresh;

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

		pullToRefresh.setClickable(false);
		pullToRefresh.setPullLabel(getString(R.string.ptr_pull_to_refresh));
		pullToRefresh.setRefreshingLabel(getString(R.string.ptr_refreshing));
		pullToRefresh
				.setReleaseLabel(getString(R.string.ptr_release_to_refresh));
		pullToRefresh.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				buildView();
			}
		});

		ListView listView = pullToRefresh.getRefreshableView();
		listView.setDivider(null);
		listView.setClickable(false);
		listView.setCacheColorHint(0);
		int regularPixelFromDp = DimenUtils.getRegularPixelFromDp(mContext, 8);
		listView.addFooterView(getGapView(regularPixelFromDp));
		listView.addHeaderView(getGapView(regularPixelFromDp));
		listView.setAdapter(adapter);
	}

	protected void stopAnimation() {
		((HomeActivity) getActivity()).stopAnimation();
		if (pullToRefresh != null) {
			pullToRefresh.onRefreshComplete();
			pullToRefresh
					.setLastUpdatedLabel(getString(R.string.ptr_last_updated)
							+ dateFormatPull.format(new Date()));
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
		pullToRefresh.setPullToRefreshEnabled(pullEnabled);
		pullToRefresh.setPullToRefreshOverScrollEnabled(pullEnabled);
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
