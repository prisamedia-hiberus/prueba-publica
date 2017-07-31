/**
 * 
 */
package com.diarioas.guialigas.activities.player.comparator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.player.comparator.PlayerComparatorStepFirstActivity;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.FontUtils;

/**
 * @author robertosanchez
 * 
 */
public class ComparatorPlayerFragmentStepFirst extends Fragment {

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
				R.layout.fragment_player_comparator_first, container, false);

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
		String url = getArguments().getString("fotoPL");
		if (url != null) {

			ImageDAO.getInstance(mContext).loadRegularImage(url,
					(ImageView) generalView.findViewById(R.id.photoPlayerLeft),
					-1, R.drawable.mask_foto, true);
		}

		TextView namePlayerLeft = (TextView) generalView
				.findViewById(R.id.namePlayerLeft);
		FontUtils.setCustomfont(mContext, namePlayerLeft,
				FontUtils.FontTypes.ROBOTO_BOLD);
		namePlayerLeft.setText(getArguments().getString("namePL"));

		TextView teamPlayerLeft = (TextView) generalView
				.findViewById(R.id.teamPlayerLeft);
		FontUtils.setCustomfont(mContext, teamPlayerLeft,
				FontUtils.FontTypes.ROBOTO_BOLD);

		teamPlayerLeft.setText(getArguments().getString("teamNamePL"));

		TextView datePlayerLeft = (TextView) generalView
				.findViewById(R.id.datePlayerLeft);
		FontUtils.setCustomfont(mContext, datePlayerLeft,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		datePlayerLeft.setText(getArguments().getString("datePL"));

		TextView paisPlayerLeft = (TextView) generalView
				.findViewById(R.id.paisPlayerLeft);
		FontUtils.setCustomfont(mContext, paisPlayerLeft,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		paisPlayerLeft.setText(getArguments().getString("paisPL")
				+ getString(R.string.separator)
				+ getArguments().getInt("edadPL")
				+ getString(R.string.player_anos).toUpperCase());

		TextView positionPlayerLeft = (TextView) generalView
				.findViewById(R.id.positionPlayerLeft);
		FontUtils.setCustomfont(mContext, positionPlayerLeft,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		String positionPL = getArguments().getString("positionPL");
		if (positionPL != null)
			positionPlayerLeft.setText(getArguments().getString("positionPL")
					.toUpperCase());

		TextView playerDorsalLeft = (TextView) generalView
				.findViewById(R.id.playerDorsalLeft);
		FontUtils.setCustomfont(mContext, playerDorsalLeft,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		playerDorsalLeft.setText(String.valueOf(getArguments().getInt(
				"dorsalPL")));

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
