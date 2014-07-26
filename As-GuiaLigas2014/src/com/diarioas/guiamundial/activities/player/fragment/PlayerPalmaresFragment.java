package com.diarioas.guiamundial.activities.player.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.dao.model.player.TituloPlayer;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.comparator.PlayerTitlesComparator;
import com.diarioas.guiamundial.utils.comparator.YearComparator;

public class PlayerPalmaresFragment extends PlayerFragment {

	private ArrayList<TituloPlayer> palmares;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_player_palmares,
				container, false);

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
		// if (palmares != null) {
		// palmares.clear();
		// palmares = null;
		// }
	}

	@Override
	protected void configureData() {

		palmares = getArguments().getParcelableArrayList("palmares");
		Collections.sort(palmares, new PlayerTitlesComparator());
		LinearLayout ll = ((LinearLayout) generalView
				.findViewById(R.id.linearTrayectoria));

		if (palmares.size() != 0) {

			ImageView imageView;
			String team, name, years;
			int numTitle;
			int i = 0, j = 0;
			RelativeLayout relativeLeft;
			LinearLayout linear, linearRight;
			RelativeLayout relativeSmallRight;
			TextView numTitles, nomTitle, teamTitle, yearsTitle;
			ArrayList<String> yearsArray;
			for (TituloPlayer palm : palmares) {

				LinearLayout.LayoutParams paramsItemLeft = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT, 4);
				LinearLayout.LayoutParams paramsItemRight = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT, 5);

				name = palm.getName();
				numTitle = palm.getNumTitle();

				numTitles = new TextView(mContext);
				numTitles.setId(300 + i);
				numTitles.setText("" + numTitle);
				numTitles.setTextColor(getResources().getColor(R.color.red));
				numTitles.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
						.getResources().getDimension(R.dimen.size_10_px));
				// numTitles.setTextSize(getResources().getDimension(
				// R.dimen.size_8));

				nomTitle = new TextView(mContext);
				nomTitle.setId(400 + i);
				nomTitle.setText(name);
				nomTitle.setMaxLines(5);
				nomTitle.setTextColor(getResources().getColor(R.color.black));
				nomTitle.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
						.getResources().getDimension(R.dimen.size_10_px));
				// nomTitle.setTextSize(getResources()
				// .getDimension(R.dimen.size_8));

				RelativeLayout.LayoutParams paramNumTitle = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramNumTitle.leftMargin = DimenUtils.getRegularPixelFromDp(
						mContext, 5);
				paramNumTitle.addRule(RelativeLayout.CENTER_VERTICAL);
				RelativeLayout.LayoutParams paramNomTitle = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramNomTitle.leftMargin = DimenUtils.getRegularPixelFromDp(
						mContext, 5);
				paramNomTitle.addRule(RelativeLayout.RIGHT_OF,
						numTitles.getId());
				paramNomTitle.addRule(RelativeLayout.CENTER_VERTICAL);

				relativeLeft = new RelativeLayout(mContext);
				relativeLeft.setGravity(Gravity.CENTER_VERTICAL);

				relativeLeft.addView(numTitles, paramNumTitle);
				relativeLeft.addView(nomTitle, paramNomTitle);

				linearRight = new LinearLayout(mContext);
				linearRight.setOrientation(LinearLayout.VERTICAL);
				linearRight.setGravity(Gravity.CENTER_VERTICAL);

				yearsArray = new ArrayList<String>(palm.getYears().keySet());
				Collections.sort(yearsArray, new YearComparator());

				for (Iterator<String> iterator = yearsArray.iterator(); iterator
						.hasNext();) {

					team = iterator.next();

					teamTitle = new TextView(mContext);
					teamTitle.setId(500 + j);
					teamTitle.setText(team);
					teamTitle.setMaxLines(2);
					// teamTitle.setBackgroundColor(Color.BLUE);
					teamTitle.setTextColor(getResources().getColor(
							R.color.black));
					teamTitle.setTextSize(
							TypedValue.COMPLEX_UNIT_PT,
							getActivity().getResources().getDimension(
									R.dimen.size_10_px));
					// teamTitle.setTextSize(getResources().getDimension(
					// R.dimen.size_8));
					teamTitle.setGravity(Gravity.CENTER);

					RelativeLayout.LayoutParams paramsRightItemLeft = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					paramsRightItemLeft.rightMargin = DimenUtils
							.getRegularPixelFromDp(mContext, 5);
					paramsRightItemLeft.addRule(RelativeLayout.CENTER_VERTICAL);
					paramsRightItemLeft
							.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

					years = "";
					for (String year : palm.getYears(team)) {
						years += year + ", ";
					}
					years = years.substring(0, years.length() - 2);

					yearsTitle = new TextView(mContext);
					yearsTitle.setGravity(Gravity.LEFT
							| Gravity.CENTER_VERTICAL);
					yearsTitle.setTextColor(getResources().getColor(
							R.color.medium_gray));
					yearsTitle.setTextSize(
							TypedValue.COMPLEX_UNIT_PT,
							getActivity().getResources().getDimension(
									R.dimen.size_8_px));
					// yearsTitle.setTextSize(getResources().getDimension(
					// R.dimen.size_8));
					yearsTitle.setText(years);
					// yearsTitle.setBackgroundColor(Color.RED);

					RelativeLayout.LayoutParams paramsRightItemRight = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					paramsRightItemRight
							.addRule(RelativeLayout.CENTER_VERTICAL);
					paramsRightItemRight
							.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					paramsRightItemRight.addRule(RelativeLayout.RIGHT_OF,
							teamTitle.getId());

					relativeSmallRight = new RelativeLayout(mContext);

					relativeSmallRight.addView(teamTitle, paramsRightItemLeft);
					relativeSmallRight
							.addView(yearsTitle, paramsRightItemRight);

					LinearLayout.LayoutParams paramsSmallRight = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, 2);
					paramsSmallRight.gravity = Gravity.CENTER_VERTICAL;
					paramsSmallRight.topMargin = DimenUtils
							.getRegularPixelFromDp(mContext, 5);
					paramsSmallRight.bottomMargin = DimenUtils
							.getRegularPixelFromDp(mContext, 5);

					linearRight.addView(relativeSmallRight, paramsSmallRight);
					j++;
				}

				linear = new LinearLayout(mContext);
				linear.setGravity(Gravity.CENTER_VERTICAL);
				linear.setOrientation(LinearLayout.HORIZONTAL);

				linear.addView(relativeLeft, paramsItemLeft);
				linear.addView(linearRight, paramsItemRight);

				LinearLayout.LayoutParams paramsRelative = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				paramsRelative.setMargins(
						DimenUtils.getRegularPixelFromDp(mContext, 5),
						DimenUtils.getRegularPixelFromDp(mContext, 15),
						DimenUtils.getRegularPixelFromDp(mContext, 5),
						DimenUtils.getRegularPixelFromDp(mContext, 15));

				imageView = new ImageView(mContext);
				imageView.setBackgroundColor(getResources().getColor(
						R.color.clear_gray));

				RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						DimenUtils.getRegularPixelFromDp(mContext, 2));
				paramsImage.setMargins(
						DimenUtils.getRegularPixelFromDp(mContext, 8),
						DimenUtils.getRegularPixelFromDp(mContext, 15),
						DimenUtils.getRegularPixelFromDp(mContext, 15),
						DimenUtils.getRegularPixelFromDp(mContext, 8));

				ll.addView(imageView, paramsImage);
				ll.addView(linear, paramsRelative);
				i++;

			}

		} else {
			View noData = inflater.inflate(R.layout.item_player_no_palmares,
					null);
			ll.addView(noData);
		}
	}

}
