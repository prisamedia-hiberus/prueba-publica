package com.diarioas.guialigas.activities.team.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
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

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.model.team.TeamStats;
import com.diarioas.guialigas.dao.model.team.TituloTeam;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.comparator.TeamPalmaresComparator;
import com.diarioas.guialigas.utils.comparator.YearComparator;

public class TeamStatsFragment extends TeamFragment {

	private static final int SCROLL_WITH = 2;

	private static final int NUMYEARSINVIEW = 3;
	private static final int NUMCOMPETITIONSINVIEW = 1;

	private HorizontalScrollView yearViewPager;
	private Bundle stats;
	private ArrayList<String> yearsArray;
	private TeamStats currentStats;
	private ArrayList<String> competitionsArray;
	private LinearLayout competitionLinear;
	private LinearLayout yearLinear;
	private HorizontalScrollView competitionHScroll;

	private int currentCompetition;
	private TextView statsText;
	private TextView palmaresText;

	private RelativeLayout relContentStats;

	private ImageView imageLeft;
	private ImageView imageRight;

	private int width;

	//

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_stats, container,
				false);

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		width = size.x;

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
		if (yearViewPager != null) {
			yearViewPager.removeAllViews();
			yearViewPager = null;
		}

		if (yearLinear != null) {
			yearLinear.removeAllViews();
			yearLinear = null;
		}
		if (yearsArray != null) {
			yearsArray.clear();
			yearsArray = null;
		}
		currentStats = null;
		if (competitionsArray != null) {
			competitionsArray.clear();
			competitionsArray = null;
		}
		if (competitionLinear != null) {
			competitionLinear.removeAllViews();
			competitionLinear = null;
		}
		if (competitionHScroll != null) {
			competitionHScroll.removeAllViews();
			competitionHScroll = null;
		}

		statsText = null;
		palmaresText = null;

		if (relContentStats != null) {
			relContentStats.removeAllViews();
			relContentStats = null;
		}

		imageLeft = null;
		imageRight = null;
	}

	@Override
	protected void configureView() {
		yearLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_years);
		yearViewPager = (HorizontalScrollView) generalView
				.findViewById(R.id.yearsTeamSroll);

		competitionLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_competition);
		competitionHScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.competitionTeamSroll);

		configureGeneral();

		configureStats();

		configurePalmares();
	}

	private void configureGeneral() {

		/************************** Info General ******************************************/
		String shield = (String) getArguments().get("shield");
		TextView name = (TextView) generalView.findViewById(R.id.nameTeam);

		name.setText((String) getArguments().get("name"));
		FontUtils.setCustomfont(mContext, name,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		if (!(shield == null || shield.equalsIgnoreCase("")))
			((ImageView) generalView.findViewById(R.id.photoTeam))
					.setBackgroundResource(mContext.getResources()
							.getIdentifier(
									shield.substring(0, shield.length() - 4),
									"drawable", mContext.getPackageName()));

		statsText = (TextView) generalView.findViewById(R.id.statsText);
		FontUtils.setCustomfont(mContext, statsText,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		palmaresText = (TextView) generalView.findViewById(R.id.palmaresText);
		FontUtils.setCustomfont(mContext, palmaresText,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		relContentStats = (RelativeLayout) generalView
				.findViewById(R.id.contentStats);
		ViewTreeObserver vto = relContentStats.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					relContentStats.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					relContentStats.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}

				RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
						width / SCROLL_WITH, relContentStats.getHeight());
				paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				paramsLeft.addRule(RelativeLayout.CENTER_VERTICAL);
				generalView.findViewById(R.id.buttonLeft).setLayoutParams(
						paramsLeft);

				RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
						width / SCROLL_WITH, relContentStats.getHeight());
				paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				paramsRight.addRule(RelativeLayout.CENTER_VERTICAL);
				generalView.findViewById(R.id.buttonRight).setLayoutParams(
						paramsRight);

			}
		});

	}

	/********************************* YEARS ******************************************************/

	private void configureStats() {

		stats = getArguments().getBundle("stats");
		if (stats.keySet().size() > 0) {
			setVisivility(View.VISIBLE);

			LinearLayout.LayoutParams paramsGeneralTab = new LinearLayout.LayoutParams(
					width / NUMYEARSINVIEW, LayoutParams.MATCH_PARENT);
			RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			paramsGeneralTab.topMargin = DimenUtils.getRegularPixelFromDp(
					getActivity().getApplicationContext(), 10);
			paramsGeneralTab.bottomMargin = DimenUtils.getRegularPixelFromDp(
					getActivity().getApplicationContext(), 10);

			yearsArray = new ArrayList<String>(stats.keySet());
			Collections.sort(yearsArray, new YearComparator());

			int pad = DimenUtils.getRegularPixelFromDp(getActivity()
					.getApplicationContext(), 5);
			for (int i = 0; i < yearsArray.size(); i++) {

				RelativeLayout generalTab = new RelativeLayout(getActivity()
						.getApplicationContext());
				generalTab.setId(700 + i);
				generalTab.setLayoutParams(paramsGeneralTab);

				Button tabButton = new Button(getActivity()
						.getApplicationContext());
				tabButton.setId(90);
				tabButton.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));
				tabButton.setLayoutParams(paramsButton);
				tabButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
						.getResources().getDimension(R.dimen.size_10_px));
				// tabButton.setTextSize(getResources().getDimension(
				// R.dimen.size_10));
				tabButton.setMaxLines(1);
				tabButton.setPadding(pad, 0, pad, 0);
				tabButton.setGravity(Gravity.CENTER);
				tabButton.setTextColor(getResources().getColor(
						R.color.medium_gray));

				tabButton.setLayoutParams(paramsButton);
				generalTab.addView(tabButton);

				yearLinear.addView(generalTab);

				setUpYearButton(generalTab, yearsArray.get(i), i);
			}

			selectFirstYear(yearsArray.size() - 1);
		} else {
			setVisivility(View.GONE);

		}

	}

	private void setVisivility(int visibility) {
		statsText.setVisibility(visibility);

		generalView.findViewById(R.id.yearsGapUpper).setVisibility(visibility);
		generalView.findViewById(R.id.yearsGapDown).setVisibility(visibility);

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
			generalTab = (RelativeLayout) generalView.findViewById(700 + i);
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

		if (position < 0)
			return;
		resetYearTabs(position);

		// Se coloca el Scroll de Arriba

		if ((position == 0) || (position == yearsArray.size() - 1)) {
			yearViewPager.smoothScrollTo(position * (width / NUMYEARSINVIEW),
					yearViewPager.getScrollY());
		} else {
			yearViewPager.smoothScrollTo((position - 1)
					* (width / NUMYEARSINVIEW), yearViewPager.getScrollY());
		}
		resetSubHeader();
		configureSubHeader(yearsArray.get(position));
		setCompetition(0);

	}

	private void selectFirstYear(final int position) {
		if (position < 0)
			return;
		resetYearTabs(position);
		// Se coloca el Scroll de Arriba

		final ViewTreeObserver vto1 = yearViewPager.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					vto1.removeOnGlobalLayoutListener(this);
				} else {
					vto1.removeGlobalOnLayoutListener(this);
				}

				if ((position == 0) || (position == yearsArray.size() - 1)) {
					yearViewPager.smoothScrollTo(position
							* (width / NUMYEARSINVIEW),
							yearViewPager.getScrollY());
				} else {
					yearViewPager.smoothScrollTo((position - 1)
							* (width / NUMYEARSINVIEW),
							yearViewPager.getScrollY());
				}

			}
		});
		resetSubHeader();
		configureSubHeader(yearsArray.get(position));
		setCompetition(0);
	}

	/********************************* YEARS ******************************************************/

	/********************************* COMPETITION ******************************************************/

	private void resetSubHeader() {
		competitionLinear.removeAllViews();
	}

	/**
	 * @param currentYear
	 */
	private void configureSubHeader(String currentYear) {

		LinearLayout.LayoutParams paramsGeneralTab = new LinearLayout.LayoutParams(
				(width / NUMCOMPETITIONSINVIEW), LayoutParams.MATCH_PARENT);

		paramsGeneralTab.topMargin = DimenUtils.getRegularPixelFromDp(mContext,
				10);
		paramsGeneralTab.bottomMargin = DimenUtils.getRegularPixelFromDp(
				mContext, 10);

		currentStats = ((TeamStats) stats.get(currentYear));

		competitionsArray = new ArrayList<String>(currentStats.getStats()
				.keySet());
		Collections.sort(competitionsArray, new TeamPalmaresComparator());
		LayoutInflater inf = LayoutInflater.from(mContext);
		for (int i = 0; i < competitionsArray.size(); i++) {

			RelativeLayout generalTab = getContentHeader(i);

			RelativeLayout.LayoutParams paramsData = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			paramsData.addRule(RelativeLayout.CENTER_HORIZONTAL);
			paramsData.addRule(RelativeLayout.BELOW, generalTab.getId());

			LinearLayout data = getContentView(inf, competitionsArray.get(i));

			RelativeLayout rel = new RelativeLayout(mContext);
			rel.setLayoutParams(paramsGeneralTab);
			rel.addView(generalTab);
			rel.addView(data, paramsData);

			competitionLinear.addView(rel);

			setUpCompetitionButton(generalTab, competitionsArray.get(i), i);
		}
		if (imageLeft != null) {
			relContentStats.removeView(imageLeft);
			imageLeft = null;
		}

		if (imageRight != null) {
			relContentStats.removeView(imageRight);
			imageRight = null;
		}
		// relContentStats.removeAllViews();
		imageLeft = new ImageView(mContext);
		imageLeft.setBackgroundResource(R.drawable.button_previus);
		RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsLeft.leftMargin = DimenUtils.getRegularPixelFromDp(mContext, 20);
		paramsLeft.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 18);
		relContentStats.addView(imageLeft, paramsLeft);

		imageRight = new ImageView(mContext);
		imageRight.setBackgroundResource(R.drawable.button_next);
		RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsRight.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsRight.rightMargin = DimenUtils
				.getRegularPixelFromDp(mContext, 20);
		paramsRight.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 18);

		relContentStats.addView(imageRight, paramsRight);

	}

	private RelativeLayout getContentHeader(int i) {
		RelativeLayout.LayoutParams paramsButton = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		RelativeLayout generalTab = new RelativeLayout(mContext);
		generalTab.setId(500 + i);

		Button tabButton = new Button(getActivity().getApplicationContext());
		tabButton.setId(60);
		tabButton.setBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		tabButton.setLayoutParams(paramsButton);
		tabButton.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
				.getResources().getDimension(R.dimen.size_10_px));
		// tabButton.setTextSize(getResources().getDimension(R.dimen.size_10));

		tabButton.setMaxLines(1);
		tabButton.setGravity(Gravity.CENTER);
		tabButton.setTextColor(getResources().getColor(R.color.medium_gray));

		tabButton.setLayoutParams(paramsButton);
		generalTab.addView(tabButton);
		return generalTab;
	}

	private LinearLayout getContentView(LayoutInflater inf, String comp) {
		TextView golesText;
		TextView partJugText;
		TextView partGanText;
		TextView partEmpText;
		TextView partPerText;
		TextView tarjetasAmText;
		TextView tarjetasRoText;
		LinearLayout data = (LinearLayout) inf.inflate(
				R.layout.item_team_palmares, null);

		golesText = (TextView) data.findViewById(R.id.golesText);
		FontUtils.setCustomfont(mContext, golesText,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		golesText.setText(String.valueOf(currentStats.getGolesAFavor(comp)));
		partJugText = (TextView) data.findViewById(R.id.partJugText);
		FontUtils.setCustomfont(mContext, partJugText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		partJugText.setText(String.valueOf(currentStats
				.getPartidosJugados(comp)));
		partGanText = (TextView) data.findViewById(R.id.partGanText);
		FontUtils.setCustomfont(mContext, partGanText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		partGanText.setText(String.valueOf(currentStats
				.getPartidosGanados(comp)));
		partEmpText = (TextView) data.findViewById(R.id.partEmpText);
		FontUtils.setCustomfont(mContext, partEmpText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		partEmpText.setText(String.valueOf(currentStats
				.getPartidosEmpatados(comp)));
		partPerText = (TextView) data.findViewById(R.id.partPerText);
		FontUtils.setCustomfont(mContext, partPerText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		partPerText.setText(String.valueOf(currentStats
				.getPartidosPerdidos(comp)));
		tarjetasAmText = (TextView) data.findViewById(R.id.tarjetasAmText);
		FontUtils.setCustomfont(mContext, tarjetasAmText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		tarjetasAmText.setText(String.valueOf(currentStats
				.getTarjetasAmarillas(comp)));
		tarjetasRoText = (TextView) data.findViewById(R.id.tarjetasRoText);
		FontUtils.setCustomfont(mContext, tarjetasRoText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		tarjetasRoText.setText(String.valueOf(currentStats
				.getTarjetasRojas(comp)));
		return data;
	}

	private void setUpCompetitionButton(final RelativeLayout tab, String text,
			final int position) {
		((Button) tab.findViewById(60)).setText(text);
		FontUtils.setCustomfont(mContext, (tab.findViewById(60)),
				FontUtils.FontTypes.ROBOTO_LIGHT);

		((Button) tab.findViewById(60))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						setCompetition(position);
					}
				});
	}

	/**
	 * @param position
	 */
	protected void setCompetition(int position) {

		currentCompetition = position;

		resetCompetitionTabs(position);

		setButtonsVisibility(position);
		// Se coloca el Scroll de Arriba

		competitionHScroll.smoothScrollTo(position
				* ((width / NUMCOMPETITIONSINVIEW)),
				competitionHScroll.getScrollY());

	}

	/**
	 * @param position
	 * 
	 */
	private void setButtonsVisibility(int position) {
		if (currentCompetition > 0) {
			imageLeft.setVisibility(View.VISIBLE);
		} else {
			imageLeft.setVisibility(View.INVISIBLE);
		}

		if (currentCompetition < (competitionsArray.size() - 1)) {
			imageRight.setVisibility(View.VISIBLE);
		} else {
			imageRight.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 
	 */
	public void setPrevCompetition() {
		if (currentCompetition > 0)
			setCompetition(currentCompetition - 1);

	}

	/**
	 * 
	 */
	public void setNextCompetition() {
		if (currentCompetition < (competitionsArray.size() - 1))
			setCompetition(currentCompetition + 1);

	}

	private void resetCompetitionTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < competitionsArray.size(); i++) {
			generalTab = (RelativeLayout) generalView.findViewById(500 + i);
			if (position != i)
				((TextView) generalTab.findViewById(60))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(60))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	/********************************* COMPETITION ******************************************************/

	/********************************* PALMARES ******************************************************/
	/**
	 * 
	 */
	private void configurePalmares() {

		LinearLayout ll = (LinearLayout) generalView
				.findViewById(R.id.linearStats);
		Bundle bundlePalmares = getArguments().getBundle("palmares");
		ArrayList<String> palmares = new ArrayList<String>(
				bundlePalmares.keySet());
		Collections.sort(palmares, new TeamPalmaresComparator());

		String comp;
		if (palmares.size() > 0) {
			palmaresText.setVisibility(View.VISIBLE);
			generalView.findViewById(R.id.palmaresGap).setVisibility(
					View.VISIBLE);

			LinearLayout linear;
			RelativeLayout relativeLeft;
			RelativeLayout relativeRight;
			ImageView imageView;
			TextView numTitles, nomTitle, yearsTitle;
			TituloTeam title;
			String years = "";
			int i = 0;
			for (Iterator<String> iterator = palmares.iterator(); iterator
					.hasNext();) {

				LinearLayout.LayoutParams paramsItemLeft = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT, 4);
				LinearLayout.LayoutParams paramsItemRight = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT, 5);

				comp = iterator.next();
				title = bundlePalmares.getParcelable(comp);

				numTitles = new TextView(mContext);
				numTitles.setId(400 + i);
				numTitles.setText("" + title.getNumTitle());
				numTitles.setTextColor(getResources().getColor(R.color.red));
				numTitles.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
						.getResources().getDimension(R.dimen.size_10_px));
				// numTitles.setTextSize(getResources().getDimension(
				// R.dimen.size_8));

				nomTitle = new TextView(mContext);
				nomTitle.setId(500 + i);
				nomTitle.setText(title.getName());
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

				yearsTitle = new TextView(mContext);
				yearsTitle.setMaxLines(-1);
				yearsTitle.setGravity(Gravity.LEFT);
				yearsTitle.setTextColor(getResources().getColor(
						R.color.medium_gray));
				yearsTitle.setTextSize(
						TypedValue.COMPLEX_UNIT_PT,
						getActivity().getResources().getDimension(
								R.dimen.size_8_px));
				// yearsTitle.setTextSize(getResources().getDimension(
				// R.dimen.size_8));

				yearsTitle.setSingleLine(false);
				years = "";
				for (String year : title.getYear()) {
					years += year + ", ";
				}
				years = years.substring(0, years.length() - 2);
				yearsTitle.setText(years);

				relativeRight = new RelativeLayout(mContext);
				relativeRight
						.setPadding(
								DimenUtils.getRegularPixelFromDp(mContext, 15),
								0, 0, 0);
				RelativeLayout.LayoutParams paramYearsTitle = new RelativeLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				paramYearsTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				paramYearsTitle.leftMargin = DimenUtils.getRegularPixelFromDp(
						mContext, 0);
				paramYearsTitle.rightMargin = DimenUtils.getRegularPixelFromDp(
						mContext, 10);

				relativeRight.addView(yearsTitle, paramYearsTitle);

				linear = new LinearLayout(mContext);
				linear.setGravity(Gravity.CENTER_VERTICAL);
				linear.setOrientation(LinearLayout.HORIZONTAL);

				linear.addView(relativeLeft, paramsItemLeft);
				linear.addView(relativeRight, paramsItemRight);

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
			palmaresText.setVisibility(View.GONE);
			generalView.findViewById(R.id.palmaresGap).setVisibility(View.GONE);
		}

	}

	/********************************* PALMARES ******************************************************/

}
