package com.diarioas.guialigas.activities.team.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

public abstract class TeamFragment extends Fragment {

	protected View generalView;
	protected Context mContext;
	protected LayoutInflater inflater;
	// private boolean firstExecution;
	private boolean firstShow;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		// this.firstExecution = true;
		this.firstShow = true;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configureView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		generalView = null;
		mContext = null;
		inflater = null;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	protected abstract void configureView();

	public void onShown() {
		if (firstShow) {
			startFirstShow();
			firstShow = false;
		}
	}

	protected abstract void startFirstShow();

	/***************************************************************************/
	/** Aux methods **/
	/***************************************************************************/
	protected String getDate(String sentence, String born) {
		if (born != null)
			return "<font color=\"Black\" >" + sentence + "</font>" + born;
		else
			return "<font color=\"Black\" >" + sentence + "</font>";
	}

}
