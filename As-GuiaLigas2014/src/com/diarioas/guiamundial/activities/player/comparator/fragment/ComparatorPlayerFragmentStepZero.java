/**
 * 
 */
package com.diarioas.guiamundial.activities.player.comparator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.utils.FontUtils;

/**
 * @author robertosanchez
 * 
 */
public class ComparatorPlayerFragmentStepZero extends Fragment {

	private View generalView;
	private Context mContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflating layout

		// this.inflater = inflater;
		generalView = inflater.inflate(
				R.layout.fragment_player_comparator_zero, container, false);

		configureView();
		return generalView;
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
	}

	/**
	 * 
	 */
	private void configureView() {

		configurePlayerLeft();
		configurePlayerRight();

	}

	/*******************************************************************************************************
	 * ************************************ PLAYER LEFT
	 * *****************************************************************************************************/

	private void configurePlayerLeft() {
		ImageView imagePL = (ImageView) generalView
				.findViewById(R.id.photoPlayerLeft);
		imagePL.setBackgroundResource(R.drawable.button_addplayer);

		TextView messageNoPlayerLeft = (TextView) generalView
				.findViewById(R.id.messageNoPlayerLeft);
		FontUtils.setCustomfont(mContext, messageNoPlayerLeft,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		messageNoPlayerLeft.setText(getActivity().getString(
				R.string.player_comparator_noplayer_left));

	}

	private void configurePlayerRight() {
		ImageView imagePR = (ImageView) generalView
				.findViewById(R.id.photoPlayerRight);
		imagePR.setBackgroundResource(R.drawable.button_addplayer);

		TextView messageNoPlayerRight = (TextView) generalView
				.findViewById(R.id.messageNoPlayerRight);
		FontUtils.setCustomfont(mContext, messageNoPlayerRight,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		messageNoPlayerRight.setText(getActivity().getString(
				R.string.player_comparator_noplayer_right));

	}
}
