/**
 * 
 */
package com.diarioas.guialigas.activities.player.comparator.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.player.comparator.PlayerComparatorStepFirstActivity;
import com.diarioas.guialigas.dao.model.player.ItemStats;
import com.diarioas.guialigas.dao.model.player.PlayerStats;
import com.diarioas.guialigas.dao.model.player.TituloPlayer;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.comparator.PlayerPalmaresComparator;
import com.diarioas.guialigas.utils.comparator.PlayerTitlesComparator;
import com.diarioas.guialigas.utils.comparator.YearComparator;

/**
 * @author robertosanchez
 * 
 */
@SuppressLint("NewApi")
public class ComparatorPlayerFragmentStepFour extends Fragment {

	// private static final int ID_BUTTON_YEAR = 40;
	// private static final int ID_BUTTON_COMPETITION = 70;
	private static final int ID_TAB_YEAR_PL = 100;
	private static final int ID_TAB_YEAR_PR = 300;
	private static final int ID_TAB_COMPETITION_PL = 500;
	private static final int ID_TAB_COMPETITION_PR = 800;

	// private static final int HEIGHT_ITEM_PALMARES = 60;
	// private static final int PADDING_ITEM_PALMARES = 5;

	private View generalView;
	private Context mContext;

	private int widthScroll;
	private int widthButton;

	private int currentPLYear = -1;
	private ArrayList<String> yearsPLArray;
	private HorizontalScrollView yearPLScroll;
	private LinearLayout yearPLLinear;

	private Bundle statsPL;
	private int currentPLCompetition = -1;
	private ArrayList<String> competitionPLArray;
	private HorizontalScrollView competitionPLScroll;
	private LinearLayout competitionPLLinear;

	private int currentPRYear = -1;
	private ArrayList<String> yearsPRArray;
	private HorizontalScrollView yearPRScroll;
	private LinearLayout yearPRLinear;

	private Bundle statsPR;
	private int currentPRCompetition = -1;
	private ArrayList<String> competitionPRArray;
	private HorizontalScrollView competitionPRScroll;
	private LinearLayout competitionPRLinear;

	private ImageView buttonYearPLPrev;
	private ImageView buttonYearPLNext;

	private ImageView buttonCompPLPrev;
	private ImageView buttonCompPLNext;

	private ImageView buttonYearPRPrev;
	private ImageView buttonYearPRNext;

	private ImageView buttonCompPRPrev;
	private ImageView buttonCompPRNext;
	private LayoutInflater inflater;
	private LinearLayout statsLinear;
	private double scale;

	// private String imagePrefix;

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

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		widthScroll = (size.x / 2);
		widthButton = widthScroll / 2;
		scale = (double) size.x / size.y;
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
		// imagePrefix = DatabaseDAO.getInstance(mContext).getPrefix(
		// Defines.ReturnPrefix.PREFIX_IMAGE);
		this.inflater = inflater;
		generalView = inflater.inflate(R.layout.fragment_player_comparator,
				container, false);

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
		statsPL = getArguments().getBundle("statsPL");
		yearsPLArray = new ArrayList<String>(statsPL.keySet());
		statsPR = getArguments().getBundle("statsPR");
		yearsPRArray = new ArrayList<String>(statsPR.keySet());
		statsLinear = (LinearLayout) generalView.findViewById(R.id.statsLinear);
		configurePlayerLeft();
		configurePlayerRight();

	}

	private void configureStats() {
		if (currentPLCompetition != -1 && currentPRCompetition != -1) {
			statsLinear.removeAllViews();
			// Log.d("COMPARATOR",
			// "YearPL: " + yearsPLArray.get(currentPLYear)
			// + " CompetitionPL: "
			// + competitionPLArray.get(currentPLCompetition));
			// Log.d("COMPARATOR",
			// "YearPr: " + yearsPRArray.get(currentPRYear)
			// + " CompetitionPR: "
			// + competitionPRArray.get(currentPRCompetition));
			PlayerStats playerStatPL = (PlayerStats) statsPL
					.getParcelable(yearsPLArray.get(currentPLYear));
			ItemStats itemStatsPL = playerStatPL.getStats(competitionPLArray
					.get(currentPLCompetition));

			PlayerStats playerStatPR = (PlayerStats) statsPR
					.getParcelable(yearsPRArray.get(currentPRYear));
			ItemStats itemStatsPR = playerStatPR.getStats(competitionPRArray
					.get(currentPRCompetition));
			configureStats(itemStatsPL, itemStatsPR);
		}
	}

	private void configureStats(ItemStats itemStatsPL, ItemStats itemStatsPR) {

		String team;
		int pjPL = 0, mjPL = 0, taPL = 0, trPL = 0, gmPL = 0, gePL = 0, asPL = 0, paPL = 0;
		for (Iterator<String> iterator = itemStatsPL.getData().keySet()
				.iterator(); iterator.hasNext();) {
			team = iterator.next();
			pjPL += itemStatsPL.getPartidosJugados(team);
			mjPL += itemStatsPL.getMinutos(team);
			taPL += itemStatsPL.getTarjetasAmarillas(team);
			trPL += itemStatsPL.getTarjetasRojas(team);
			gmPL += itemStatsPL.getGolesMarcados(team);
			gePL += itemStatsPL.getGolesEncajados(team);
			paPL += itemStatsPL.getParadas(team);
			asPL += itemStatsPL.getAsistencias(team);

		}

		int pjPR = 0, mjPR = 0, taPR = 0, trPR = 0, gmPR = 0, gePR = 0, asPR = 0, paPR = 0;
		for (Iterator<String> iterator = itemStatsPR.getData().keySet()
				.iterator(); iterator.hasNext();) {
			team = iterator.next();

			pjPR += itemStatsPR.getPartidosJugados(team);
			mjPR += itemStatsPR.getMinutos(team);
			taPR += itemStatsPR.getTarjetasAmarillas(team);
			trPR += itemStatsPR.getTarjetasRojas(team);
			gmPR += itemStatsPR.getGolesMarcados(team);
			gePR += itemStatsPR.getGolesEncajados(team);
			paPR += itemStatsPR.getParadas(team);
			asPR += itemStatsPR.getAsistencias(team);

		}

		if (getArguments().getString("positionPL").equalsIgnoreCase("portero")
				&& getArguments().getString("positionPR").equalsIgnoreCase(
						"portero")) {
			if (gmPL != 0 || gmPR != 0)
				statsLinear.addView(setGolesMarcados(gmPL, gmPR));
			if (asPL != 0 || asPR != 0)
				statsLinear.addView(setAsistencias(asPL, asPR));

			statsLinear.addView(setGolesEncajados(gePL, gePR));
			statsLinear.addView(setParadas(paPL, paPR));
			statsLinear.addView(setPartidosJugados(pjPL, pjPR));
			statsLinear.addView(setMinutos(mjPL, mjPR));
			statsLinear.addView(setTarjetasAmarillas(taPL, taPR));
			statsLinear.addView(setTarjetasRojas(trPL, trPR));
		} else if (getArguments().getString("positionPL").equalsIgnoreCase(
				"portero")
				|| getArguments().getString("positionPR").equalsIgnoreCase(
						"portero")) {
			statsLinear.addView(setGolesMarcados(gmPL, gmPR));
			statsLinear.addView(setAsistencias(asPL, asPR));
			statsLinear.addView(setGolesEncajados(gePL, gePR));
			statsLinear.addView(setParadas(paPL, paPR));
			statsLinear.addView(setPartidosJugados(pjPL, pjPR));
			statsLinear.addView(setMinutos(mjPL, mjPR));
			statsLinear.addView(setTarjetasAmarillas(taPL, taPR));
			statsLinear.addView(setTarjetasRojas(trPL, trPR));
		} else {
			statsLinear.addView(setGolesMarcados(gmPL, gmPR));
			statsLinear.addView(setAsistencias(asPL, asPR));
			if (gePL != 0 || gePR != 0)
				statsLinear.addView(setGolesEncajados(gePL, gePR));
			if (paPL != 0 || paPR != 0)
				statsLinear.addView(setParadas(paPL, paPR));
			statsLinear.addView(setPartidosJugados(pjPL, pjPR));
			statsLinear.addView(setMinutos(mjPL, mjPR));
			statsLinear.addView(setTarjetasAmarillas(taPL, taPR));
			statsLinear.addView(setTarjetasRojas(trPL, trPR));
		}

	}

	private RelativeLayout setGolesMarcados(int gmPL, int gmPR) {

		return setCell(gmPL, gmPR, R.drawable.icn_goles,
				R.string.player_comparator_goles_marc);
	}

	private RelativeLayout setGolesEncajados(int gePL, int gePR) {

		return setCell(gePL, gePR, R.drawable.icn_goles,
				R.string.player_comparator_goles_enc);
	}

	private RelativeLayout setPartidosJugados(int pjPL, int pjPR) {

		return setCell(pjPL, pjPR, R.drawable.icn_minutos,
				R.string.player_comparator_partidos);
	}

	private RelativeLayout setMinutos(int mjPL, int mjPR) {

		return setCell(mjPL, mjPR, R.drawable.icn_partidos,
				R.string.player_comparator_minutos);

	}

	private RelativeLayout setTarjetasAmarillas(int taPL, int taPR) {

		return setCell(taPL, taPR, R.drawable.icn_amarillas,
				R.string.player_comparator_tarjetas_am);

	}

	private RelativeLayout setTarjetasRojas(int trPL, int trPR) {
		return setCell(trPL, trPR, R.drawable.icn_rojas,
				R.string.player_comparator_tarjetas_ro);

	}

	private RelativeLayout setParadas(int paPL, int paPR) {
		return setCell(paPL, paPR, R.drawable.icn_paradas,
				R.string.player_comparator_paradas);

	}

	private RelativeLayout setAsistencias(int asPL, int asPR) {
		return setCell(asPL, asPR, R.drawable.icn_asistencias,
				R.string.player_comparator_asistencias);

	}

	private RelativeLayout setCell(int playerLeft, int playerRight, int icon,
			int text) {

		RelativeLayout cell = (RelativeLayout) inflater.inflate(
				R.layout.player_comparator_item, null);

		int total = playerLeft + playerRight;

		ImageView comparatorImage = (ImageView) cell
				.findViewById(R.id.comparatorImage);
		comparatorImage.setBackgroundResource(icon);
		TextView textComparatorImage = (TextView) cell
				.findViewById(R.id.textComparatorImage);
		FontUtils.setCustomfont(mContext, textComparatorImage,
				FontTypes.HELVETICANEUE);
		textComparatorImage.setText(getActivity().getString(text));

		TextView statsTextPL = ((TextView) cell.findViewById(R.id.statsTextPL));
		FontUtils.setCustomfont(mContext, statsTextPL, FontTypes.HELVETICANEUE);
		statsTextPL.setText(String.valueOf(playerLeft));

		TextView statsTextPR = ((TextView) cell.findViewById(R.id.statsTextPR));
		FontUtils.setCustomfont(mContext, statsTextPR, FontTypes.HELVETICANEUE);
		statsTextPR.setText(String.valueOf(playerRight));

		View stasOffPL = cell.findViewById(R.id.stasOffPL);
		LinearLayout.LayoutParams pOffPL = (LinearLayout.LayoutParams) stasOffPL
				.getLayoutParams();
		View stasOnPL = cell.findViewById(R.id.stasOnPL);
		LinearLayout.LayoutParams pOnPL = (LinearLayout.LayoutParams) stasOnPL
				.getLayoutParams();

		View stasOffPR = cell.findViewById(R.id.stasOffPR);
		LinearLayout.LayoutParams pOffPR = (LinearLayout.LayoutParams) stasOffPR
				.getLayoutParams();
		View stasOnPR = cell.findViewById(R.id.stasOnPR);
		LinearLayout.LayoutParams pOnPR = (LinearLayout.LayoutParams) stasOnPR
				.getLayoutParams();

		if (total > 0) {
			pOffPL.weight = (total - playerLeft) * 100 / total;
			pOnPL.weight = playerLeft * 100 / total;

			pOffPR.weight = (total - playerRight) * 100 / total;
			pOnPR.weight = playerRight * 100 / total;
		} else {
			pOffPL.weight = 100;
			pOnPL.weight = 0;
			pOffPR.weight = 100;
			pOnPR.weight = 0;
		}

		stasOffPL.setLayoutParams(pOffPL);
		stasOnPL.setLayoutParams(pOnPL);

		stasOnPR.setLayoutParams(pOnPR);
		stasOffPR.setLayoutParams(pOffPR);

		return cell;
	}

	/*******************************************************************************************************
	 * ************************************ PLAYER LEFT
	 * *****************************************************************************************************/

	private void configurePlayerLeft() {
		String url = getArguments().getString("fotoPL");
		ImageView imagePL = (ImageView) generalView
				.findViewById(R.id.photoPlayerLeft);
		if (url != null) {
			ImageDAO.getInstance(getActivity()).loadPlayerImage(url, imagePL,
							R.drawable.mask_foto);
		}

		imagePL.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				((PlayerComparatorStepFirstActivity) getActivity())
						.selectCompetition(ReturnRequestCodes.COMPARATORPLAYER_LEFT);
				return false;
			}
		});

		imagePL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((PlayerComparatorStepFirstActivity) getActivity())
						.selectCompetition(ReturnRequestCodes.COMPARATORPLAYER_LEFT);
			}
		});

		TextView namePlayerLeft = (TextView) generalView
				.findViewById(R.id.namePlayerLeft);
		FontUtils.setCustomfont(mContext, namePlayerLeft,
				FontUtils.FontTypes.HELVETICANEUEBOLD);
		namePlayerLeft.setText(getArguments().getString("namePL"));

		TextView teamPlayerLeft = (TextView) generalView
				.findViewById(R.id.teamPlayerLeft);
		FontUtils.setCustomfont(mContext, teamPlayerLeft,
				FontUtils.FontTypes.HELVETICANEUEBOLD);

		teamPlayerLeft.setText(getArguments().getString("teamNamePL"));

		TextView datePlayerLeft = (TextView) generalView
				.findViewById(R.id.datePlayerLeft);
		FontUtils.setCustomfont(mContext, datePlayerLeft,
				FontUtils.FontTypes.HELVETICANEUE);
		datePlayerLeft.setText(getArguments().getString("datePL"));

		TextView paisPlayerLeft = (TextView) generalView
				.findViewById(R.id.paisPlayerLeft);

		String pais = null;
		String paisPL = getArguments().getString("paisPL");
		int edadPL = getArguments().getInt("edadPL");
		if (paisPL != null && !paisPL.equalsIgnoreCase("null")) {
			pais = paisPL;
			if (edadPL != 0) {
				pais += getString(R.string.separator) + edadPL
						+ getString(R.string.player_anos).toUpperCase();
			}

		} else {
			if (edadPL != 0) {
				pais = getString(R.string.separator) + edadPL
						+ getString(R.string.player_anos).toUpperCase();
			}
		}
		if (pais != null) {
			FontUtils.setCustomfont(mContext, paisPlayerLeft,
					FontUtils.FontTypes.HELVETICANEUE);
			paisPlayerLeft.setText(pais);
		}

		TextView positionPlayerLeft = (TextView) generalView
				.findViewById(R.id.positionPlayerLeft);
		FontUtils.setCustomfont(mContext, positionPlayerLeft,
				FontUtils.FontTypes.HELVETICANEUE);
		positionPlayerLeft.setText(getArguments().getString("positionPL")
				.toUpperCase());

		TextView playerDorsalLeft = (TextView) generalView
				.findViewById(R.id.playerDorsalLeft);
		FontUtils.setCustomfont(mContext, playerDorsalLeft,
				FontUtils.FontTypes.HELVETICANEUE);
		playerDorsalLeft.setText(String.valueOf(getArguments().getInt(
				"dorsalPL")));
		/************************************************************************/

		buttonYearPLPrev = (ImageView) generalView
				.findViewById(R.id.buttonYearPLPrev);
		buttonYearPLNext = (ImageView) generalView
				.findViewById(R.id.buttonYearPLNext);
		yearPLScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.yearsPlayerLeftSroll);

		yearPLLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_years_playerLeft);

		buttonCompPLPrev = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPLPrev);
		buttonCompPLNext = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPLNext);
		competitionPLScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.competitionPlayerLeftSroll);

		competitionPLLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_competition_playerLeft);

		yearsPLArray = new ArrayList<String>(statsPL.keySet());

		if (yearsPLArray.size() > 0 && yearsPRArray.size() > 0) {
			Collections.sort(yearsPLArray, new YearComparator());
			configureYearsPLScroll(yearsPLArray);
		}
		/************************************************************************/
		setPalmaresPL();

	}

	private void setPalmaresPL() {
		LinearLayout palmaresLinearPL = (LinearLayout) generalView
				.findViewById(R.id.palmaresLinearPL);
		ArrayList<TituloPlayer> palmaresPL = getArguments()
				.getParcelableArrayList("palmaresPL");
		Collections.sort(palmaresPL, new PlayerTitlesComparator());
		TextView numTitles, nomTitle;
		RelativeLayout relativeLeft;

		for (TituloPlayer palm : palmaresPL) {

			relativeLeft = (RelativeLayout) inflater.inflate(
					R.layout.item_comparator_palmares, null);
			numTitles = (TextView) relativeLeft.findViewById(R.id.numTitle);
			FontUtils.setCustomfont(mContext, numTitles,
					FontTypes.HELVETICANEUE);
			numTitles.setText(String.valueOf(palm.getNumTitle()));
			nomTitle = (TextView) relativeLeft.findViewById(R.id.nameTitle);
			FontUtils
					.setCustomfont(mContext, nomTitle, FontTypes.HELVETICANEUE);
			nomTitle.setText(palm.getName());

			palmaresLinearPL.addView(relativeLeft);

		}
	}

	private void configureYearsPLScroll(ArrayList<String> yearsArray) {

		LinearLayout.LayoutParams paramsGeneral = new LinearLayout.LayoutParams(
				(widthScroll), LayoutParams.MATCH_PARENT);

		paramsGeneral.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 5);
		paramsGeneral.bottomMargin = DimenUtils.getRegularPixelFromDp(mContext,
				2);

		RelativeLayout generalTab;
		for (int i = 0; i < yearsArray.size(); i++) {

			generalTab = (RelativeLayout) inflater.inflate(
					R.layout.item_comparator_scroll, null);
			generalTab.setId(ID_TAB_YEAR_PL + i);
			generalTab.setLayoutParams(paramsGeneral);
			yearPLLinear.addView(generalTab);

			setUpYearsPLButton(generalTab, yearsArray.get(i), i);
		}

		final RelativeLayout rel1 = (RelativeLayout) generalView
				.findViewById(R.id.yearsPlayerLeftContent);

		rel1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

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
								widthButton, rel1.getHeight());
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						generalView.findViewById(R.id.buttonYearPLLeft)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						generalView.findViewById(R.id.buttonYearPLRight)
								.setLayoutParams(paramsRight);
					}
				});

		setFirstYearPL(yearsArray.size() - 1);
	}

	private void setUpYearsPLButton(final RelativeLayout tab, String year,
			final int position) {
		Button button = ((Button) tab.findViewById(R.id.nameItemScroll));
		button.setText(year);
		FontUtils.setCustomfont(mContext, button,
				FontUtils.FontTypes.HELVETICANEUE);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setYearPL(position);
			}
		});
	}

	public void setYearFromParentPL(int sum) {

		if ((sum == 1 && currentPLYear < yearsPLArray.size() - 1)
				|| (sum == -1 && currentPLYear > 0)) {
			setYearPL(currentPLYear + sum);
		}

	}

	private void setYearPL(int position) {
		currentPLYear = position;
		setVibilityButtonYearPL();
		resetYearsPLTabs(position);
		yearPLScroll.smoothScrollTo(position * widthScroll,
				yearPLScroll.getScrollY());

		PlayerStats playerStat = (PlayerStats) statsPL
				.getParcelable(yearsPLArray.get(currentPLYear));
		competitionPLArray = new ArrayList<String>(playerStat.getStats()
				.keySet());
		Collections.sort(competitionPLArray, new PlayerPalmaresComparator());

		competitionPLLinear.removeAllViews();

		configureCompetitionPLScroll(competitionPLArray);
		// setFirstCompetitionPL(0);
	}

	private void setFirstYearPL(final int position) {
		currentPLYear = position;
		setVibilityButtonYearPL();
		resetYearsPLTabs(position);
		final ViewTreeObserver vto1 = yearPLScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					yearPLScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					yearPLScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}
				yearPLScroll.smoothScrollTo(position * widthScroll,
						yearPLScroll.getScrollY());

			}
		});

		PlayerStats playerStat = (PlayerStats) statsPL
				.getParcelable(yearsPLArray.get(currentPLYear));
		competitionPLArray = new ArrayList<String>(playerStat.getStats()
				.keySet());
		Collections.sort(competitionPLArray, new PlayerPalmaresComparator());

		competitionPLLinear.removeAllViews();

		configureCompetitionPLScroll(competitionPLArray);
	}

	private void setVibilityButtonYearPL() {
		if (currentPLYear > 0) {
			buttonYearPLPrev.setVisibility(View.VISIBLE);
		} else {
			buttonYearPLPrev.setVisibility(View.INVISIBLE);
		}

		if (currentPLYear < yearsPLArray.size() - 1) {
			buttonYearPLNext.setVisibility(View.VISIBLE);
		} else {
			buttonYearPLNext.setVisibility(View.INVISIBLE);
		}

	}

	private void resetYearsPLTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < yearsPLArray.size(); i++) {
			generalTab = (RelativeLayout) generalView
					.findViewById(ID_TAB_YEAR_PL + i);
			if (position != i)
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	private void configureCompetitionPLScroll(ArrayList<String> competitionArray) {

		LinearLayout.LayoutParams paramsGeneral = new LinearLayout.LayoutParams(
				(widthScroll), LayoutParams.MATCH_PARENT);
		paramsGeneral.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 2);
		paramsGeneral.bottomMargin = DimenUtils.getRegularPixelFromDp(mContext,
				2);

		RelativeLayout generalTab;
		for (int i = 0; i < competitionArray.size(); i++) {
			generalTab = (RelativeLayout) inflater.inflate(
					R.layout.item_comparator_scroll, null);
			generalTab.setId(ID_TAB_COMPETITION_PL + i);
			generalTab.setLayoutParams(paramsGeneral);
			competitionPLLinear.addView(generalTab);

			setUpCompetitionsPLButton(generalTab, competitionArray.get(i), i);
		}

		final RelativeLayout rel1 = (RelativeLayout) generalView
				.findViewById(R.id.competitionsPlayerLeftContent);

		rel1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

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
								widthButton, rel1.getHeight());
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						generalView.findViewById(R.id.buttonCompetitionPLLeft)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						generalView.findViewById(R.id.buttonCompetitionPLRight)
								.setLayoutParams(paramsRight);
					}
				});

		setFirstCompetitionPL(0);
	}

	private void setUpCompetitionsPLButton(final RelativeLayout tab,
			String year, final int position) {
		Button button = ((Button) tab.findViewById(R.id.nameItemScroll));
		button.setText(year);
		FontUtils.setCustomfont(mContext, button,
				FontUtils.FontTypes.HELVETICANEUE);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCompetitionPL(position);
			}
		});
	}

	public void setCompetitionFromParentPL(int sum) {

		if ((sum == 1 && currentPLCompetition < competitionPLArray.size() - 1)
				|| (sum == -1 && currentPLCompetition > 0)) {
			setCompetitionPL(currentPLCompetition + sum);
		}

	}

	private void setCompetitionPL(int position) {
		currentPLCompetition = position;
		setVibilityButtonCompetitionPL();
		resetCompetitionPLTabs(position);
		competitionPLScroll.smoothScrollTo(position * widthScroll,
				competitionPLScroll.getScrollY());
		configureStats();

	}

	private void setFirstCompetitionPL(final int position) {
		currentPLCompetition = position;
		setVibilityButtonCompetitionPL();
		resetCompetitionPLTabs(position);
		final ViewTreeObserver vto1 = competitionPLScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					competitionPLScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					competitionPLScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}
				competitionPLScroll.smoothScrollTo(position * widthScroll,
						competitionPLScroll.getScrollY());
			}
		});

		configureStats();

	}

	private void setVibilityButtonCompetitionPL() {
		if (currentPLCompetition > 0) {
			buttonCompPLPrev.setVisibility(View.VISIBLE);
		} else {
			buttonCompPLPrev.setVisibility(View.INVISIBLE);
		}

		if (currentPLCompetition < competitionPLArray.size() - 1) {
			buttonCompPLNext.setVisibility(View.VISIBLE);
		} else {
			buttonCompPLNext.setVisibility(View.INVISIBLE);
		}

	}

	private void resetCompetitionPLTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < competitionPLArray.size(); i++) {
			generalTab = (RelativeLayout) generalView
					.findViewById(ID_TAB_COMPETITION_PL + i);
			if (position != i)
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	/*******************************************************************************************************
	 * ************************************ PLAYER RIGHT
	 * *****************************************************************************************************/
	private void configurePlayerRight() {
		String url = getArguments().getString("fotoPR");
		ImageView imagePR = (ImageView) generalView
				.findViewById(R.id.photoPlayerRight);
		if (url != null) {
			ImageDAO.getInstance(getActivity()).loadPlayerImage(url, imagePR,
							R.drawable.mask_foto);
		}
		imagePR.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				((PlayerComparatorStepFirstActivity) getActivity())
						.selectCompetition(ReturnRequestCodes.COMPARATORPLAYER_RIGHT);
				return false;
			}
		});

		imagePR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((PlayerComparatorStepFirstActivity) getActivity())
						.selectCompetition(ReturnRequestCodes.COMPARATORPLAYER_RIGHT);
			}
		});

		TextView namePlayerRight = (TextView) generalView
				.findViewById(R.id.namePlayerRight);
		FontUtils.setCustomfont(mContext, namePlayerRight,
				FontUtils.FontTypes.HELVETICANEUEBOLD);
		namePlayerRight.setText(getArguments().getString("namePR"));

		TextView teamPlayerRight = (TextView) generalView
				.findViewById(R.id.teamPlayerRight);
		FontUtils.setCustomfont(mContext, teamPlayerRight,
				FontUtils.FontTypes.HELVETICANEUEBOLD);
		teamPlayerRight.setText(getArguments().getString("teamNamePR"));

		TextView datePlayerRight = (TextView) generalView
				.findViewById(R.id.datePlayerRight);
		FontUtils.setCustomfont(mContext, datePlayerRight,
				FontUtils.FontTypes.HELVETICANEUE);
		datePlayerRight.setText(getArguments().getString("datePR"));

		TextView paisPlayerRight = (TextView) generalView
				.findViewById(R.id.paisPlayerRight);

		String pais = null;
		String paisPR = getArguments().getString("paisPR");
		int edadPR = getArguments().getInt("edadPR");
		if (paisPR != null && !paisPR.equalsIgnoreCase("null")) {
			pais = paisPR;
			if (edadPR != 0) {
				pais += getString(R.string.separator) + edadPR
						+ getString(R.string.player_anos).toUpperCase();
			}
		} else {
			if (edadPR != 0) {
				pais = getString(R.string.separator) + edadPR
						+ getString(R.string.player_anos).toUpperCase();
			}
		}
		if (pais != null) {
			FontUtils.setCustomfont(mContext, paisPlayerRight,
					FontUtils.FontTypes.HELVETICANEUE);
			paisPlayerRight.setText(pais);
		}

		TextView positionPlayerRight = (TextView) generalView
				.findViewById(R.id.positionPlayerRight);
		FontUtils.setCustomfont(mContext, positionPlayerRight,
				FontUtils.FontTypes.HELVETICANEUE);
		positionPlayerRight.setText(getArguments().getString("positionPR")
				.toUpperCase());

		TextView playerDorsalRight = (TextView) generalView
				.findViewById(R.id.playerDorsalRight);
		FontUtils.setCustomfont(mContext, playerDorsalRight,
				FontUtils.FontTypes.HELVETICANEUE);
		playerDorsalRight.setText(String.valueOf(getArguments().getInt(
				"dorsalPR")));

		/************************************************************************/

		buttonYearPRPrev = (ImageView) generalView
				.findViewById(R.id.buttonYearPRPrev);
		buttonYearPRNext = (ImageView) generalView
				.findViewById(R.id.buttonYearPRNext);
		yearPRScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.yearsPlayerRightSroll);
		yearPRLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_years_playerRight);

		buttonCompPRPrev = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPRPrev);
		buttonCompPRNext = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPRNext);
		competitionPRScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.competitionPlayerRightSroll);
		competitionPRLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_competition_playerRight);

		if (yearsPLArray.size() > 0 && yearsPRArray.size() > 0) {
			Collections.sort(yearsPRArray, new YearComparator());
			configureYearsPRScroll(yearsPRArray);
		}
		/************************************************************************/
		setPalmaresPR();

	}

	private void setPalmaresPR() {
		LinearLayout palmaresLinearPR = (LinearLayout) generalView
				.findViewById(R.id.palmaresLinearPR);
		ArrayList<TituloPlayer> palmaresPR = getArguments()
				.getParcelableArrayList("palmaresPR");
		Collections.sort(palmaresPR, new PlayerTitlesComparator());
		TextView numTitles, nomTitle;
		RelativeLayout relativeRight;
		for (TituloPlayer palm : palmaresPR) {
			relativeRight = (RelativeLayout) inflater.inflate(
					R.layout.item_comparator_palmares, null);
			numTitles = (TextView) relativeRight.findViewById(R.id.numTitle);
			FontUtils.setCustomfont(mContext, numTitles,
					FontTypes.HELVETICANEUE);
			numTitles.setText(String.valueOf(palm.getNumTitle()));
			nomTitle = (TextView) relativeRight.findViewById(R.id.nameTitle);
			FontUtils
					.setCustomfont(mContext, nomTitle, FontTypes.HELVETICANEUE);
			nomTitle.setText(palm.getName());

			palmaresLinearPR.addView(relativeRight);
		}
	}

	private void configureYearsPRScroll(ArrayList<String> yearsArray) {

		LinearLayout.LayoutParams paramsGeneral = new LinearLayout.LayoutParams(
				(widthScroll), LayoutParams.MATCH_PARENT);

		paramsGeneral.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 5);
		paramsGeneral.bottomMargin = DimenUtils.getRegularPixelFromDp(mContext,
				2);
		RelativeLayout generalTab;
		for (int i = 0; i < yearsArray.size(); i++) {
			generalTab = (RelativeLayout) inflater.inflate(
					R.layout.item_comparator_scroll, null);
			generalTab.setId(ID_TAB_YEAR_PR + i);
			generalTab.setLayoutParams(paramsGeneral);
			yearPRLinear.addView(generalTab);

			setUpYearsPRButton(generalTab, yearsArray.get(i), i);
		}

		final RelativeLayout rel1 = (RelativeLayout) generalView
				.findViewById(R.id.yearsPlayerRightContent);

		rel1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

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
								widthButton, rel1.getHeight());
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						generalView.findViewById(R.id.buttonYearPRLeft)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						generalView.findViewById(R.id.buttonYearPRRight)
								.setLayoutParams(paramsRight);
					}
				});

		setFirstYearPR(yearsArray.size() - 1, 0);
	}

	private void setUpYearsPRButton(final RelativeLayout tab, String year,
			final int position) {
		Button button = ((Button) tab.findViewById(R.id.nameItemScroll));
		button.setText(year);
		FontUtils.setCustomfont(mContext, button,
				FontUtils.FontTypes.HELVETICANEUE);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setYearPR(position);
			}
		});
	}

	public void setYearFromParentPR(int sum) {

		if ((sum == 1 && currentPRYear < yearsPRArray.size() - 1)
				|| (sum == -1 && currentPRYear > 0)) {
			setYearPR(currentPRYear + sum);
		}

	}

	private void setYearPR(int position) {
		currentPRYear = position;
		setVibilityButtonYearPR();
		resetYearsPRTabs(position);
		yearPRScroll.smoothScrollTo(position * widthScroll,
				yearPRScroll.getScrollY());

		PlayerStats playerStat = (PlayerStats) statsPR
				.getParcelable(yearsPRArray.get(currentPRYear));
		competitionPRArray = new ArrayList<String>(playerStat.getStats()
				.keySet());
		Collections.sort(competitionPRArray, new PlayerPalmaresComparator());

		competitionPRLinear.removeAllViews();

		configureCompetitionPRScroll(competitionPRArray);
	}

	private void setFirstYearPR(final int position, int competition) {
		currentPRYear = position;
		setVibilityButtonYearPR();
		resetYearsPRTabs(position);
		final ViewTreeObserver vto1 = yearPRScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					yearPRScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					yearPRScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}
				yearPRScroll.smoothScrollTo(position * widthScroll,
						yearPRScroll.getScrollY());

			}
		});
		PlayerStats playerStat = (PlayerStats) statsPR
				.getParcelable(yearsPRArray.get(currentPRYear));
		competitionPRArray = new ArrayList<String>(playerStat.getStats()
				.keySet());
		Collections.sort(competitionPRArray, new PlayerPalmaresComparator());

		competitionPRLinear.removeAllViews();

		configureCompetitionPRScroll(competitionPRArray);
	}

	private void setVibilityButtonYearPR() {
		if (currentPRYear > 0) {
			buttonYearPRPrev.setVisibility(View.VISIBLE);
		} else {
			buttonYearPRPrev.setVisibility(View.INVISIBLE);
		}

		if (currentPRYear < yearsPRArray.size() - 1) {
			buttonYearPRNext.setVisibility(View.VISIBLE);
		} else {
			buttonYearPRNext.setVisibility(View.INVISIBLE);
		}

	}

	private void resetYearsPRTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < yearsPRArray.size(); i++) {
			generalTab = (RelativeLayout) generalView
					.findViewById(ID_TAB_YEAR_PR + i);
			if (position != i)
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

	private void configureCompetitionPRScroll(ArrayList<String> competitionArray) {

		LinearLayout.LayoutParams paramsGeneral = new LinearLayout.LayoutParams(
				(widthScroll), LayoutParams.MATCH_PARENT);
		paramsGeneral.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 2);
		paramsGeneral.bottomMargin = DimenUtils.getRegularPixelFromDp(mContext,
				2);
		RelativeLayout generalTab;
		for (int i = 0; i < competitionArray.size(); i++) {
			generalTab = (RelativeLayout) inflater.inflate(
					R.layout.item_comparator_scroll, null);
			generalTab.setId(ID_TAB_COMPETITION_PR + i);
			generalTab.setLayoutParams(paramsGeneral);
			competitionPRLinear.addView(generalTab);

			setUpCompetitionsPRButton(generalTab, competitionArray.get(i), i);
		}

		final RelativeLayout rel1 = (RelativeLayout) generalView
				.findViewById(R.id.competitionsPlayerRightContent);

		rel1.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

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
								widthButton, rel1.getHeight());
						paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						generalView.findViewById(R.id.buttonCompetitionPRLeft)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						generalView.findViewById(R.id.buttonCompetitionPRRight)
								.setLayoutParams(paramsRight);
					}
				});

		setFirstCompetitionPR(0);
	}

	private void setUpCompetitionsPRButton(final RelativeLayout tab,
			String year, final int position) {
		Button button = ((Button) tab.findViewById(R.id.nameItemScroll));
		button.setText(year);
		FontUtils.setCustomfont(mContext, button,
				FontUtils.FontTypes.HELVETICANEUE);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCompetitionPR(position);
			}
		});
	}

	public void setCompetitionFromParentPR(int sum) {

		if ((sum == 1 && currentPRCompetition < competitionPRArray.size() - 1)
				|| (sum == -1 && currentPRCompetition > 0)) {
			setCompetitionPR(currentPRCompetition + sum);
		}

	}

	private void setCompetitionPR(int position) {
		currentPRCompetition = position;
		setVibilityButtonCompetitionPR();
		resetCompetitionPRTabs(position);
		competitionPRScroll.smoothScrollTo(position * widthScroll,
				competitionPRScroll.getScrollY());
		configureStats();

	}

	private void setFirstCompetitionPR(final int position) {
		currentPRCompetition = position;
		setVibilityButtonCompetitionPR();
		resetCompetitionPRTabs(position);
		final ViewTreeObserver vto1 = competitionPRScroll.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					competitionPRScroll.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					competitionPRScroll.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}
				competitionPRScroll.smoothScrollTo(position * widthScroll,
						competitionPRScroll.getScrollY());
			}
		});
		configureStats();
	}

	private void setVibilityButtonCompetitionPR() {
		if (currentPRCompetition > 0) {
			buttonCompPRPrev.setVisibility(View.VISIBLE);
		} else {
			buttonCompPRPrev.setVisibility(View.INVISIBLE);
		}

		if (currentPRCompetition < competitionPRArray.size() - 1) {
			buttonCompPRNext.setVisibility(View.VISIBLE);
		} else {
			buttonCompPRNext.setVisibility(View.INVISIBLE);
		}

	}

	private void resetCompetitionPRTabs(int position) {
		RelativeLayout generalTab;
		for (int i = 0; i < competitionPRArray.size(); i++) {
			generalTab = (RelativeLayout) generalView
					.findViewById(ID_TAB_COMPETITION_PR + i);
			if (position != i)
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(
								R.color.medium_gray));
			else
				((TextView) generalTab.findViewById(R.id.nameItemScroll))
						.setTextColor(getResources().getColor(R.color.red));
		}
	}

}
