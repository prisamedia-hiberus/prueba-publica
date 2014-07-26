package com.diarioas.guiamundial.activities.player.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.dao.model.player.ItemStats;
import com.diarioas.guiamundial.dao.model.player.PlayerStats;
import com.diarioas.guiamundial.utils.CustomHorizontalScrollView;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.comparator.PlayerPalmaresComparator;
import com.diarioas.guiamundial.utils.comparator.StringComparator;
import com.diarioas.guiamundial.utils.comparator.YearComparator;

public class PlayerInfoFragment extends PlayerFragment {

	private static final int SCROLL_WITH = 2;

	private static final int NUMYEARSINVIEW = 3;
	private static final int NUMCOMPINVIEW = 1;
	private static final int NUMCTEAMINVIEW = 3;

	private Bundle stats;

	private int currentYear;
	private ArrayList<String> yearsArray;

	private LinearLayout yearLinear;
	private HorizontalScrollView yearScroll;

	private LinearLayout compLinear;
	private CustomHorizontalScrollView compScroll;

	private int currentCompetition;
	private ArrayList<String> competitionArray;

	private int currentTeam;

	private ImageView buttonPrev;
	private ImageView buttonNext;

	private boolean yearsButtonAvailable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		stats = getArguments().getBundle("stats");
		this.inflater = inflater;
		generalView = inflater.inflate(R.layout.fragment_player_info,
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

		// if (stats != null) {
		// stats.clear();
		// stats = null;
		// }

		if (yearsArray != null) {
			yearsArray.clear();
			yearsArray = null;
		}

		if (yearLinear != null) {
			yearLinear.removeAllViews();
			yearLinear = null;
		}
		if (yearScroll != null) {
			yearScroll.removeAllViews();
			yearScroll = null;
		}

		if (competitionArray != null) {
			competitionArray.clear();
			competitionArray = null;
		}

		if (compLinear != null) {
			compLinear.removeAllViews();
			compLinear = null;
		}
		if (compScroll != null) {
			compScroll.removeAllViews();
			compScroll = null;
		}
		buttonPrev = null;
		buttonNext = null;
	}

	@Override
	protected void configureData() {

		LinearLayout ll = ((LinearLayout) generalView
				.findViewById(R.id.linearTrayectoria));

		if (stats.keySet().size() > 0) {
			yearLinear = (LinearLayout) generalView
					.findViewById(R.id.linear_tabs_years);
			yearScroll = (HorizontalScrollView) generalView
					.findViewById(R.id.yearsPlayerSroll);

			compLinear = (LinearLayout) generalView
					.findViewById(R.id.linear_tabs_competition);
			compScroll = (CustomHorizontalScrollView) generalView
					.findViewById(R.id.competitionPlayerSroll);

			yearsArray = new ArrayList<String>(stats.keySet());
			Collections.sort(yearsArray, new YearComparator());

			buttonPrev = (ImageView) generalView.findViewById(R.id.buttonPrev);
			buttonNext = (ImageView) generalView.findViewById(R.id.buttonNext);

			configureYearsScroll();
			setFirstYear(yearsArray.size() - 1);

		} else {
			View noData = inflater
					.inflate(R.layout.item_player_no_status, null);
			ll.addView(noData);
		}
	}

	/******************************* YEARS SCROLL ****************************************************/
	private void configureYearsScroll() {

		LinearLayout.LayoutParams paramsGeneralTab = new LinearLayout.LayoutParams(
				width / NUMYEARSINVIEW, LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramsGeneralTab.topMargin = DimenUtils.getRegularPixelFromDp(mContext,
				10);
		paramsGeneralTab.bottomMargin = DimenUtils.getRegularPixelFromDp(
				mContext, 10);
		int pad = DimenUtils.getRegularPixelFromDp(mContext, 5);
		for (int i = 0; i < yearsArray.size(); i++) {

			RelativeLayout generalTab = new RelativeLayout(getActivity()
					.getApplicationContext());
			generalTab.setId(800 + i);
			generalTab.setLayoutParams(paramsGeneralTab);

			Button tabButton = new Button(mContext);
			tabButton.setId(90);
			tabButton.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
			tabButton.setLayoutParams(paramsButton);

			tabButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
					.getResources().getDimension(R.dimen.size_10_px));
			// tabButton.setTextSize(getResources().getDimension(R.dimen.size_10));
			tabButton.setMaxLines(1);
			tabButton.setPadding(pad, 0, pad, 0);
			tabButton.setGravity(Gravity.CENTER);
			tabButton
					.setTextColor(getResources().getColor(R.color.medium_gray));

			tabButton.setLayoutParams(paramsButton);
			generalTab.addView(tabButton);

			yearLinear.addView(generalTab);

			setUpYearButton(generalTab, yearsArray.get(i), i);
		}
	}

	private void setUpYearButton(final RelativeLayout tab, String text,
			final int position) {

		((Button) tab.findViewById(90)).setText(text);
		FontUtils.setCustomfont(mContext, (tab.findViewById(90)),
				FontUtils.FontTypes.ROBOTO_LIGHT);

		((Button) tab.findViewById(90))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setYear(position);
					}
				});
	}

	private void resetYearTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < yearsArray.size(); i++) {
			generalTab = (RelativeLayout) generalView.findViewById(800 + i);
			if (position != i)
				((TextView) generalTab.findViewById(90))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(90))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	private void setYear(int position) {
		if (yearsButtonAvailable) {
			yearsButtonAvailable = false;
			currentYear = position;
			resetYearTabs(position);

			if ((position == 0) || (position == yearsArray.size() - 1)) {
				yearScroll.smoothScrollTo(position * (width / NUMYEARSINVIEW),
						yearScroll.getScrollY());
			} else {
				yearScroll.smoothScrollTo((position - 1)
						* (width / NUMYEARSINVIEW), yearScroll.getScrollY());
			}

			PlayerStats playerStat = (PlayerStats) stats
					.getParcelable(yearsArray.get(currentYear));
			competitionArray = new ArrayList<String>(playerStat.getStats()
					.keySet());
			Collections.sort(competitionArray, new PlayerPalmaresComparator());

			compLinear.removeAllViews();

			configureCompetitionScroll();
			setFirstCompetition(0);
		}

	}

	@SuppressLint("NewApi")
	private void setFirstYear(final int position) {
		yearsButtonAvailable = false;
		currentYear = position;
		resetYearTabs(position);

		// Se coloca el Scroll de Arriba

		final ViewTreeObserver vto1 = yearScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					yearScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					yearScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}

				if ((position == 0) || (position == yearsArray.size() - 1)) {
					yearScroll.smoothScrollTo(position
							* (width / NUMYEARSINVIEW), yearScroll.getScrollY());
				} else {
					yearScroll.smoothScrollTo((position - 1)
							* (width / NUMYEARSINVIEW), yearScroll.getScrollY());
				}

			}
		});

		PlayerStats playerStat = (PlayerStats) stats.getParcelable(yearsArray
				.get(currentYear));
		competitionArray = new ArrayList<String>(playerStat.getStats().keySet());
		Collections.sort(competitionArray, new PlayerPalmaresComparator());

		compLinear.removeAllViews();
		configureCompetitionScroll();
		setFirstCompetition(0);

	}

	/******************************* YEARS SCROLL ****************************************************/
	/******************************* COMPETITION SCROLL ****************************************************/

	private void configureCompetitionScroll() {
		currentCompetition = 0;
		// Log.d("SCROLL", "configureCompetitionScroll");

		LinearLayout.LayoutParams paramsGeneral = new LinearLayout.LayoutParams(
				(width / NUMCOMPINVIEW), LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramsGeneral.topMargin = DimenUtils
				.getRegularPixelFromDp(mContext, 10);
		paramsGeneral.bottomMargin = DimenUtils.getRegularPixelFromDp(mContext,
				10);
		LayoutInflater inf = LayoutInflater.from(getActivity()
				.getApplicationContext());

		LinearLayout teamLinear;
		ItemStats itemStats;
		HorizontalScrollView teamScroll;
		RelativeLayout data;
		PlayerStats playerStat = (PlayerStats) stats.getParcelable(yearsArray
				.get(currentYear));
		ArrayList<String> teamsArray;
		for (int i = 0; i < competitionArray.size(); i++) {

			itemStats = playerStat.getStats(competitionArray.get(i));
			Button tabButton = new Button(mContext);
			tabButton.setId(40);
			tabButton.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
			tabButton.setLayoutParams(paramsButton);
			tabButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
					.getResources().getDimension(R.dimen.size_10_px));
			// tabButton.setTextSize(getResources().getDimension(R.dimen.size_10));
			tabButton.setGravity(Gravity.CENTER);
			tabButton.setMaxLines(2);
			tabButton
					.setTextColor(getResources().getColor(R.color.medium_gray));

			tabButton.setLayoutParams(paramsButton);

			RelativeLayout generalTab = new RelativeLayout(getActivity()
					.getApplicationContext());
			generalTab.setId(300 + i);
			generalTab.setLayoutParams(paramsGeneral);
			generalTab.addView(tabButton);

			RelativeLayout teamLayout = (RelativeLayout) inf.inflate(
					R.layout.item_player_team, null);
			RelativeLayout.LayoutParams paramsData = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			teamLinear = (LinearLayout) teamLayout
					.findViewById(R.id.linear_tabs_team);
			teamScroll = (HorizontalScrollView) teamLayout
					.findViewById(R.id.teamPlayerSroll);
			teamScroll.setId(900 + i);
			teamsArray = getTeamArray(i);
			Collections.sort(teamsArray, new StringComparator());
			configureTeamScroll(teamScroll, teamLinear, teamsArray, i);
			paramsData.addRule(RelativeLayout.CENTER_HORIZONTAL);
			paramsData.addRule(RelativeLayout.BELOW, teamScroll.getId());

			for (int j = 0; j < teamsArray.size(); j++) {
				data = (RelativeLayout) inf.inflate(
						R.layout.item_player_statistics, null);
				data.setId(800 + i * 10 + j);
				// data.setVisibility(View.INVISIBLE);
				teamLayout.addView(data, paramsData);

				loadData(itemStats, teamsArray.get(j), data);
			}

			RelativeLayout.LayoutParams paramsGeneralTab = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			RelativeLayout rel = new RelativeLayout(mContext);
			rel.setLayoutParams(paramsGeneral);

			rel.addView(generalTab, paramsGeneralTab);
			RelativeLayout.LayoutParams paramsTeamLayout = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			paramsTeamLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			paramsTeamLayout.addRule(RelativeLayout.BELOW, generalTab.getId());

			rel.addView(teamLayout, paramsTeamLayout);

			compLinear.addView(rel);

			setUpCompetitionButton(generalTab, competitionArray.get(i), i);
		}
		setFirstTeam(getTeamArray(0), 0);

		final RelativeLayout rel1 = (RelativeLayout) compLinear
				.findViewById(300);
		rel1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							rel1.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							rel1.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}

						RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
								width / SCROLL_WITH, rel1.getHeight());
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						generalView.findViewById(R.id.buttonLeft1)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								width / SCROLL_WITH, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						generalView.findViewById(R.id.buttonRight1)
								.setLayoutParams(paramsRight);
						yearsButtonAvailable = true;
					}
				});

		final LinearLayout rel2 = (LinearLayout) compLinear
				.findViewById(R.id.contentStats2);

		rel2.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							rel2.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							rel2.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}

						RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
								width / SCROLL_WITH,
								(rel2.getHeight() + DimenUtils
										.getRegularPixelFromDp(getActivity()
												.getApplicationContext(),
												10 + 10)));
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						generalView.findViewById(R.id.buttonLeft2)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								width / SCROLL_WITH,
								(rel2.getHeight() + DimenUtils
										.getRegularPixelFromDp(getActivity()
												.getApplicationContext(),
												10 + 10)));
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						generalView.findViewById(R.id.buttonRight2)
								.setLayoutParams(paramsRight);

					}
				});

	}

	private void setUpCompetitionButton(final RelativeLayout tab, String text,
			final int position) {

		((Button) tab.findViewById(40)).setText(text);
		FontUtils.setCustomfont(mContext, (tab.findViewById(40)),
				FontUtils.FontTypes.ROBOTO_LIGHT);

		((Button) tab.findViewById(40))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setCompetitionFromLeft(position);
					}
				});
	}

	private void resetCompetitionTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < competitionArray.size(); i++) {
			generalTab = (RelativeLayout) generalView.findViewById(300 + i);
			if (position != i)
				((TextView) generalTab.findViewById(40))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(40))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	private void setCompetitionFromLeft(int position) {
		setCompetition(position, 0);
	}

	private void setCompetition(int position, int team) {
		// Log.d("SCROLL", "Pos: " + position + " ::Team " + team);
		currentCompetition = position;
		resetCompetitionTabs(position);

		compScroll.smoothScrollTo(position * (width / NUMCOMPINVIEW),
				compScroll.getScrollY());

		setFirstTeam(getTeamArray(currentCompetition), team);
	}

	@SuppressLint("NewApi")
	private void setFirstCompetition(final int position) {
		currentCompetition = position;
		resetCompetitionTabs(position);

		// Se coloca el Scroll de Arriba
		final ViewTreeObserver vto1 = compScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					compScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					compScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}

				compScroll.smoothScrollTo(position * (width / NUMCOMPINVIEW),
						compScroll.getScrollY());

			}
		});

		setFirstTeam(getTeamArray(currentCompetition), 0);

	}

	private void setButtonsVisibility() {

		ArrayList<String> teamsArray = getTeamArray(currentCompetition);
		Log.d("SCROLL", "currentCompetition: " + currentCompetition
				+ " : currentTeam: " + currentTeam + " : NumCompetitions"
				+ competitionArray.size() + " : NumTeams" + teamsArray.size()
				+ " buttonPrev: "
				+ (buttonPrev.getVisibility() == View.VISIBLE)
				+ " buttonNext: "
				+ (buttonNext.getVisibility() == View.VISIBLE));

		if (currentCompetition > 0 || currentTeam > 0) {
			buttonPrev.setVisibility(View.VISIBLE);
		} else {
			buttonPrev.setVisibility(View.INVISIBLE);
		}

		if (currentCompetition < (competitionArray.size() - 1)
				|| currentTeam < (teamsArray.size() - 1)) {
			buttonNext.setVisibility(View.VISIBLE);
		} else {
			buttonNext.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 
	 */
	public void setPrevCompetition() {

		ArrayList<String> teamsArray;
		if (currentCompetition > 0 || currentTeam > 0) {
			if (currentTeam > 0) {
				teamsArray = getTeamArray(currentCompetition);
				setTeam(teamsArray, currentTeam - 1);

			} else {
				teamsArray = getTeamArray(currentCompetition - 1);
				setCompetition(currentCompetition - 1, teamsArray.size() - 1);
			}
		}

	}

	private ArrayList<String> getTeamArray(int comp) {

		PlayerStats playerStat = (PlayerStats) stats.getParcelable(yearsArray
				.get(currentYear));
		ItemStats itemStats = playerStat.getStats(competitionArray.get(comp));
		ArrayList<String> teamsArray = new ArrayList<String>(itemStats
				.getData().keySet());
		Collections.sort(teamsArray, new StringComparator());

		return teamsArray;
	}

	/**
	 * 
	 */
	public void setNextCompetition() {
		ArrayList<String> teamsArray = getTeamArray(currentCompetition);
		// Log.d("SCROLL", "currentCompetition: " + currentCompetition
		// + " : currentTeam: " + currentTeam + " : NumCompetitions"
		// + competitionArray.size() + " : NumTeams" + teamsArray.size()
		// + " buttonPrev: "
		// + (buttonPrev.getVisibility() == View.VISIBLE)
		// + " buttonNext: "
		// + (buttonNext.getVisibility() == View.VISIBLE));
		if (currentCompetition < (competitionArray.size() - 1)
				|| currentTeam < (teamsArray.size() - 1)) {
			Log.d("SCROLL", "Paso 1");
			if (currentTeam < (teamsArray.size() - 1)) {
				Log.d("SCROLL", "Paso 2");
				setTeam(teamsArray, currentTeam + 1);

			} else {
				Log.d("SCROLL", "Paso 3");
				setCompetitionFromLeft(currentCompetition + 1);
			}
		}

	}

	/******************************* COMPETITION SCROLL ****************************************************/
	/*******************************
	 * TEAM SCROLL
	 * 
	 * @param teamLinear
	 * @param teamScroll
	 * @param teamsArray
	 ****************************************************/
	private void configureTeamScroll(HorizontalScrollView teamScroll,
			LinearLayout teamLinear, ArrayList<String> teamsArray, int offset) {

		LinearLayout.LayoutParams paramsGeneralTab = new LinearLayout.LayoutParams(
				width / NUMCTEAMINVIEW, LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramsGeneralTab.topMargin = DimenUtils.getRegularPixelFromDp(mContext,
				5);
		paramsGeneralTab.bottomMargin = DimenUtils.getRegularPixelFromDp(
				mContext, 5);

		for (int i = 0; i < teamsArray.size(); i++) {

			RelativeLayout generalTab = new RelativeLayout(getActivity()
					.getApplicationContext());
			generalTab.setId(100 + 10 * offset + i);
			generalTab.setLayoutParams(paramsGeneralTab);

			Button tabButton = new Button(mContext);
			tabButton.setId(20);
			tabButton.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
			tabButton.setLayoutParams(paramsButton);
			tabButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
					.getResources().getDimension(R.dimen.size_8_px));
			// tabButton.setTextSize(getResources().getDimension(R.dimen.size_8));
			tabButton.setMaxLines(2);
			tabButton.setGravity(Gravity.LEFT);
			tabButton
					.setTextColor(getResources().getColor(R.color.medium_gray));

			tabButton.setLayoutParams(paramsButton);
			generalTab.addView(tabButton);

			teamLinear.addView(generalTab);

			setUpTeamButton(generalTab, teamsArray, i);
		}
	}

	private void setUpTeamButton(final RelativeLayout tab,
			final ArrayList<String> teamsArray, final int position) {

		((Button) tab.findViewById(20)).setText(teamsArray.get(position));
		FontUtils.setCustomfont(mContext, (tab.findViewById(20)),
				FontUtils.FontTypes.ROBOTO_LIGHT);
		tab.setPadding(DimenUtils.getRegularPixelFromDp(mContext, 10), 0, 0, 0);

		((Button) tab.findViewById(20))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setTeam(teamsArray, position);
					}
				});
	}

	private void reseteTeamTabs(ArrayList<String> teamsArray, int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < teamsArray.size(); i++) {
			generalTab = (RelativeLayout) generalView.findViewById(100 + 10
					* currentCompetition + i);
			if (position != i)
				((TextView) generalTab.findViewById(20))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(20))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	private void setTeam(ArrayList<String> teamsArray, int position) {
		currentTeam = position;
		for (int i = 0; i < teamsArray.size(); i++) {
			if (i == position) {
				generalView.findViewById(800 + currentCompetition * 10 + i)
						.setVisibility(View.VISIBLE);
			} else {
				generalView.findViewById(800 + currentCompetition * 10 + i)
						.setVisibility(View.INVISIBLE);
			}

		}
		HorizontalScrollView teamScroll = (HorizontalScrollView) compScroll
				.findViewById(900 + currentCompetition);
		// Log.d("SCROLL", "ScrollId-- " + teamScroll.getId());

		reseteTeamTabs(teamsArray, position);
		setButtonsVisibility();

		if ((position == 0) || (position == teamsArray.size() - 1)) {
			teamScroll.smoothScrollTo(position * (width / NUMCTEAMINVIEW),
					teamScroll.getScrollY());
		} else {
			teamScroll.smoothScrollTo(
					(position - 1) * (width / NUMCTEAMINVIEW),
					teamScroll.getScrollY());
		}

	}

	@SuppressLint("NewApi")
	private void setFirstTeam(final ArrayList<String> teamsArray,
			final int position) {
		currentTeam = position;
		for (int i = 0; i < teamsArray.size(); i++) {
			if (i == position) {
				generalView.findViewById(800 + currentCompetition * 10 + i)
						.setVisibility(View.VISIBLE);
			} else {
				generalView.findViewById(800 + currentCompetition * 10 + i)
						.setVisibility(View.INVISIBLE);
			}

		}
		reseteTeamTabs(teamsArray, position);
		setButtonsVisibility();

		// Se coloca el Scroll de Arriba

		final HorizontalScrollView teamScroll = (HorizontalScrollView) compScroll
				.findViewById(900 + currentCompetition);
		// Log.d("SCROLL", "ScrollId-- " + teamScroll.getId());

		final ViewTreeObserver vto1 = teamScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					teamScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					teamScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}

				if ((position == 0) || (position == teamsArray.size() - 1)) {
					teamScroll.smoothScrollTo(position
							* (width / NUMCTEAMINVIEW), teamScroll.getScrollY());
				} else {
					teamScroll.smoothScrollTo((position - 1)
							* (width / NUMCTEAMINVIEW), teamScroll.getScrollY());
				}

			}
		});

	}

	private void loadData(ItemStats itemStats, String team, RelativeLayout data) {
		TextView goles, partidos, minutos, tarjetasA, tarjetasR, dataText, datosLabelPlayer;
		ImageView datosPlayerImage;

		goles = (TextView) data.findViewById(R.id.golesPlayer);
		FontUtils.setCustomfont(mContext, goles,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		partidos = (TextView) data.findViewById(R.id.partJugPlayer);
		FontUtils.setCustomfont(mContext, partidos,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		minutos = (TextView) data.findViewById(R.id.minutosPlayer);
		FontUtils.setCustomfont(mContext, minutos,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		tarjetasA = (TextView) data.findViewById(R.id.tarjAmarillasPlayer);
		FontUtils.setCustomfont(mContext, tarjetasA,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		tarjetasR = (TextView) data.findViewById(R.id.tarjRojasPlayer);
		FontUtils.setCustomfont(mContext, tarjetasR,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		dataText = (TextView) data.findViewById(R.id.datosPlayer);
		datosLabelPlayer = (TextView) data.findViewById(R.id.datosLabelPlayer);
		datosPlayerImage = (ImageView) data.findViewById(R.id.datosPlayerImage);
		FontUtils.setCustomfont(mContext, dataText,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		partidos.setText("" + itemStats.getPartidosJugados(team));
		minutos.setText("" + itemStats.getMinutos(team));
		tarjetasA.setText("" + itemStats.getTarjetasAmarillas(team));
		tarjetasR.setText("" + itemStats.getTarjetasRojas(team));
		if (getArguments().getString("position").equalsIgnoreCase("portero")) {
			((TextView) data.findViewById(R.id.golesLabelPlayer))
					.setText(getActivity().getString(R.string.player_goles_enc));

			goles.setText("" + itemStats.getGolesEncajados(team));
			datosLabelPlayer.setText(getString(R.string.player_paradas));
			datosPlayerImage.setBackgroundResource(R.drawable.icn_paradas);
			dataText.setText("" + itemStats.getParadas(team));
		} else {
			((TextView) data.findViewById(R.id.golesLabelPlayer))
					.setText(getActivity().getString(R.string.player_goles));
			goles.setText("" + itemStats.getGolesMarcados(team));
			datosLabelPlayer.setText(getString(R.string.player_asistencias));
			datosPlayerImage.setBackgroundResource(R.drawable.icn_asistencias);
			dataText.setText("" + itemStats.getAsistencias(team));
		}
	}

	/***************************************************************************************/
	@Override
	protected void configureHeader() {
		super.configureHeader();
		TextView alturaPlayer = (TextView) generalView
				.findViewById(R.id.alturaPlayer);
		FontUtils.setCustomfont(mContext, alturaPlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		alturaPlayer.setText(getArguments().get("altura") + " cm");

		TextView pesoPlayer = (TextView) generalView
				.findViewById(R.id.pesoPlayer);
		FontUtils.setCustomfont(mContext, pesoPlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		pesoPlayer.setText(getArguments().get("peso") + " kg");

		TextView positionPlayer = (TextView) generalView
				.findViewById(R.id.positionPlayer);
		FontUtils.setCustomfont(mContext, positionPlayer,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		String position = (String) getArguments().get("position");
		if (position != null) {
			if (position.equalsIgnoreCase("portero")) {
				((ImageView) generalView.findViewById(R.id.positionPlayerImage))
						.setBackgroundResource(R.drawable.portero);
			} else if (position.equalsIgnoreCase("defensa")) {
				((ImageView) generalView.findViewById(R.id.positionPlayerImage))
						.setBackgroundResource(R.drawable.defensa);
			} else if (position.equalsIgnoreCase("centrocampista")) {
				((ImageView) generalView.findViewById(R.id.positionPlayerImage))
						.setBackgroundResource(R.drawable.centrocampista);
			} else if (position.equalsIgnoreCase("delantero")) {
				((ImageView) generalView.findViewById(R.id.positionPlayerImage))
						.setBackgroundResource(R.drawable.delantero);
			}
			positionPlayer.setText(position);
		}
	}

}
