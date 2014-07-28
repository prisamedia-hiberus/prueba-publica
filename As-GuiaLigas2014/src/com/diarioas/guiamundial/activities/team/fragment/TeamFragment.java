package com.diarioas.guiamundial.activities.team.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.View;

public abstract class TeamFragment extends Fragment {

	protected View generalView;
	protected Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configureView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		generalView = null;
		mContext = null;
	}

	/**
	 * 
	 */
	protected abstract void configureView();


	protected String getDate(String sentence, String born) {
		if (born != null)
			return "<font color=\"Black\" >" + sentence + "</font>" + born;
		else
			return "<font color=\"Black\" >" + sentence + "</font>";
	}

}
