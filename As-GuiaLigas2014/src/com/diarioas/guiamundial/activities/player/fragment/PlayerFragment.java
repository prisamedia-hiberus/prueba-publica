package com.diarioas.guiamundial.activities.player.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.player.PlayerActivity;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;

public abstract class PlayerFragment extends Fragment {

	protected View generalView;
	protected Context mContext;
	protected LayoutInflater inflater;
	protected double scale;

	protected int width;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		width = size.x;
		scale = (double) size.x / size.y;
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
		inflater = null;
		mContext = null;
	}

	/**
	 * 
	 */
	private void configureView() {
		configureHeader();
		configureData();
	}

	/**
	 * 
	 */
	protected abstract void configureData();

	protected void configureHeader() {

		TextView namePlayer = (TextView) generalView
				.findViewById(R.id.namePlayer);
		FontUtils.setCustomfont(mContext, namePlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		namePlayer.setText(getArguments().getString("name"));

		TextView teamPlayer = (TextView) generalView
				.findViewById(R.id.teamPlayer);
		FontUtils.setCustomfont(mContext, teamPlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		teamPlayer.setText(getArguments().getString("teamName"));

		TextView datePlayer = (TextView) generalView
				.findViewById(R.id.datePlayer);
		FontUtils.setCustomfont(mContext, datePlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		String date = getArguments().getString("date");
		if (date != null) {
			datePlayer.setVisibility(View.VISIBLE);
			datePlayer.setText(Html.fromHtml(formatSentence(
					getString(R.string.player_fecha_de_nacimiento), date)));

		} else {
			datePlayer.setVisibility(View.INVISIBLE);
		}

		TextView paisPlayer = (TextView) generalView
				.findViewById(R.id.paisPlayer);
		FontUtils.setCustomfont(mContext, paisPlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		String pais = getArguments().getString("pais");
		if (pais != null) {
			paisPlayer.setVisibility(View.VISIBLE);
			paisPlayer.setText(Html.fromHtml(formatSentence(
					getString(R.string.player_pais), pais)));
		} else {
			paisPlayer.setVisibility(View.INVISIBLE);
		}

		TextView edadPlayer = (TextView) generalView
				.findViewById(R.id.edadPlayer);
		FontUtils.setCustomfont(mContext, edadPlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		int edad = getArguments().getInt("edad");
		if (edad > 0) {
			edadPlayer.setVisibility(View.VISIBLE);
			edadPlayer.setText(Html.fromHtml(formatSentence(
					getString(R.string.player_edad), "" + edad)));
		} else {
			edadPlayer.setVisibility(View.INVISIBLE);
		}

		String url = getArguments().getString("foto");

		if (url != null) {
			((PlayerActivity) getActivity()).getmImageFetcher().loadImage(url,
					((ImageView) generalView.findViewById(R.id.photoPlayer)),
					R.drawable.mask_foto);
		}

		if (!getArguments().getBoolean("isTag")) {
			generalView.findViewById(R.id.playerTagButton).setVisibility(
					View.GONE);
		}
	}

	private String formatSentence(String sentence, String message) {
		return "<font color=\"" + getResources().getColor(R.color.black)
				+ "\" >" + sentence + "</font>" + message;
	}

}
