package com.diarioas.guialigas.activities.player.comparator.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.player.comparator.PlayerComparatorStepSecondActivity;
import com.diarioas.guialigas.dao.model.player.ItemStats;
import com.diarioas.guialigas.dao.model.player.Player;
import com.diarioas.guialigas.dao.model.player.PlayerStats;
import com.diarioas.guialigas.dao.model.player.TituloPlayer;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemotePlayerDAO;
import com.diarioas.guialigas.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.comparator.PlayerPalmaresComparator;
import com.diarioas.guialigas.utils.comparator.PlayerTitlesComparator;
import com.diarioas.guialigas.utils.comparator.YearComparator;

public class ComparatorPlayerSectionFragment extends SectionFragment implements
		RemotePlayerDAOListener {

	private static final int ID_TAB_YEAR_PL = 100;
	private static final int ID_TAB_YEAR_PR = 300;

	private static final int ID_TAB_COMPETITION_PL = 500;
	private static final int ID_TAB_COMPETITION_PR = 800;

	private int currentPlayerLeftId;
	private int currentPlayerRightId;
	private Player currentPlayerLeft;
	private Player currentPlayerRight;
	private String teamPLName;
	private String teamPRName;
	private TextView namePlayerLeft;
	private TextView teamPlayerLeft;
	private TextView datePlayerLeft;
	private TextView paisPlayerLeft;
	private TextView positionPlayerLeft;
	private TextView playerDorsalLeft;
	private TextView messageNoPlayerLeft;
	private TextView messageNoPlayerRight;
	private TextView namePlayerRight;
	private TextView teamPlayerRight;
	private TextView datePlayerRight;
	private TextView paisPlayerRight;
	private TextView positionPlayerRight;
	private TextView playerDorsalRight;

	private int widthScroll;
	private int widthButton;

	private int currentPLYear = -1;
	private ArrayList<String> yearsPLArray;
	private HorizontalScrollView yearPLScroll;
	private LinearLayout yearPLLinear;

	private int currentPLCompetition = -1;
	private ArrayList<String> competitionPLArray;
	private HorizontalScrollView competitionPLScroll;
	private LinearLayout competitionPLLinear;

	private int currentPRYear = -1;
	private ArrayList<String> yearsPRArray;
	private HorizontalScrollView yearPRScroll;
	private LinearLayout yearPRLinear;

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
	private LinearLayout statsLinear;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(
				R.layout.fragment_comparatorplayer_section, container, false);
		return generalView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ImageDAO.getInstance(getActivity()).closePlayerCache();
		ImageDAO.getInstance(getActivity()).erasePlayerCache();
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void buildView() {

		widthScroll = (((HomeActivity) getActivity()).getWidth() / 2);
		widthButton = widthScroll / 2;

	}

	@Override
	protected void configureView() {
		configurePlayerLeft();
		configurePlayerRight();
		configureVS();

		((HomeActivity) getActivity()).stopAnimation();

		callToOmniture();

	}

	private void configurePlayerLeft() {
		ImageView imagePL = (ImageView) generalView
				.findViewById(R.id.photoPlayerLeft);
		imagePL.setBackgroundResource(R.drawable.button_addplayer);

		messageNoPlayerLeft = (TextView) generalView
				.findViewById(R.id.messageNoPlayerLeft);
		FontUtils.setCustomfont(mContext, messageNoPlayerLeft,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		messageNoPlayerLeft.setText(getActivity().getString(
				R.string.player_comparator_noplayer_left));

		generalView.findViewById(R.id.playerImagesContentLeft)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								PlayerComparatorStepSecondActivity.class);
						getActivity().startActivityForResult(intent,
								ReturnRequestCodes.COMPARATORPLAYER_LEFT);
						getActivity().overridePendingTransition(
								R.anim.slide_in_up, R.anim.null_anim);
					}
				});

		namePlayerLeft = (TextView) generalView
				.findViewById(R.id.namePlayerLeft);
		FontUtils.setCustomfont(mContext, namePlayerLeft,
				FontUtils.FontTypes.HELVETICANEUEBOLD);

		teamPlayerLeft = (TextView) generalView
				.findViewById(R.id.teamPlayerLeft);
		FontUtils.setCustomfont(mContext, teamPlayerLeft,
				FontUtils.FontTypes.HELVETICANEUEBOLD);

		datePlayerLeft = (TextView) generalView
				.findViewById(R.id.datePlayerLeft);
		FontUtils.setCustomfont(mContext, datePlayerLeft,
				FontUtils.FontTypes.HELVETICANEUE);

		paisPlayerLeft = (TextView) generalView
				.findViewById(R.id.paisPlayerLeft);
		FontUtils.setCustomfont(mContext, paisPlayerLeft,
				FontUtils.FontTypes.HELVETICANEUE);

		positionPlayerLeft = (TextView) generalView
				.findViewById(R.id.positionPlayerLeft);
		FontUtils.setCustomfont(mContext, positionPlayerLeft,
				FontUtils.FontTypes.HELVETICANEUE);

		playerDorsalLeft = (TextView) generalView
				.findViewById(R.id.playerDorsalLeft);
		FontUtils.setCustomfont(mContext, playerDorsalLeft,
				FontUtils.FontTypes.HELVETICANEUE);

	}

	private void configurePlayerRight() {
		ImageView imagePR = (ImageView) generalView
				.findViewById(R.id.photoPlayerRight);
		imagePR.setBackgroundResource(R.drawable.button_addplayer);

		messageNoPlayerRight = (TextView) generalView
				.findViewById(R.id.messageNoPlayerRight);
		FontUtils.setCustomfont(mContext, messageNoPlayerRight,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		messageNoPlayerRight.setText(getActivity().getString(
				R.string.player_comparator_noplayer_right));

		generalView.findViewById(R.id.playerImagesContentRight)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								PlayerComparatorStepSecondActivity.class);
						getActivity().startActivityForResult(intent,
								ReturnRequestCodes.COMPARATORPLAYER_RIGHT);
						getActivity().overridePendingTransition(
								R.anim.slide_in_up, R.anim.null_anim);

					}
				});
		namePlayerRight = (TextView) generalView
				.findViewById(R.id.namePlayerRight);
		FontUtils.setCustomfont(mContext, namePlayerRight,
				FontUtils.FontTypes.HELVETICANEUEBOLD);

		teamPlayerRight = (TextView) generalView
				.findViewById(R.id.teamPlayerRight);
		FontUtils.setCustomfont(mContext, teamPlayerRight,
				FontUtils.FontTypes.HELVETICANEUEBOLD);

		datePlayerRight = (TextView) generalView
				.findViewById(R.id.datePlayerRight);
		FontUtils.setCustomfont(mContext, datePlayerRight,
				FontUtils.FontTypes.HELVETICANEUE);

		paisPlayerRight = (TextView) generalView
				.findViewById(R.id.paisPlayerRight);
		FontUtils.setCustomfont(mContext, paisPlayerRight,
				FontUtils.FontTypes.HELVETICANEUE);

		positionPlayerRight = (TextView) generalView
				.findViewById(R.id.positionPlayerRight);
		FontUtils.setCustomfont(mContext, positionPlayerRight,
				FontUtils.FontTypes.HELVETICANEUE);

		playerDorsalRight = (TextView) generalView
				.findViewById(R.id.playerDorsalRight);
		FontUtils.setCustomfont(mContext, playerDorsalRight,
				FontUtils.FontTypes.HELVETICANEUE);
	}

	private void configureVS() {
		statsLinear = (LinearLayout) generalView.findViewById(R.id.statsLinear);

		buttonYearPLPrev = (ImageView) generalView
				.findViewById(R.id.buttonYearPLPrev);
		((RelativeLayout) buttonYearPLPrev.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setYearFromParentPL(-1);

					}
				});
		buttonYearPLNext = (ImageView) generalView
				.findViewById(R.id.buttonYearPLNext);
		((RelativeLayout) buttonYearPLNext.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setYearFromParentPL(1);

					}
				});
		yearPLScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.yearsPlayerLeftSroll);

		yearPLLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_years_playerLeft);

		buttonCompPLPrev = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPLPrev);
		((RelativeLayout) buttonCompPLPrev.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setCompetitionFromParentPL(-1);

					}
				});
		buttonCompPLNext = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPLNext);
		((RelativeLayout) buttonCompPLNext.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setCompetitionFromParentPL(1);

					}
				});
		competitionPLScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.competitionPlayerLeftSroll);

		competitionPLLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_competition_playerLeft);

		buttonYearPRPrev = (ImageView) generalView
				.findViewById(R.id.buttonYearPRPrev);
		((RelativeLayout) buttonYearPRPrev.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setYearFromParentPR(-1);

					}
				});
		buttonYearPRNext = (ImageView) generalView
				.findViewById(R.id.buttonYearPRNext);
		((RelativeLayout) buttonYearPRNext.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setYearFromParentPR(1);

					}
				});
		yearPRScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.yearsPlayerRightSroll);
		yearPRLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_years_playerRight);

		buttonCompPRPrev = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPRPrev);
		((RelativeLayout) buttonCompPRPrev.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setCompetitionFromParentPR(-1);

					}
				});
		buttonCompPRNext = (ImageView) generalView
				.findViewById(R.id.buttonCompetitionPRNext);
		((RelativeLayout) buttonCompPRNext.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setCompetitionFromParentPR(1);

					}
				});
		competitionPRScroll = (HorizontalScrollView) generalView
				.findViewById(R.id.competitionPlayerRightSroll);
		competitionPRLinear = (LinearLayout) generalView
				.findViewById(R.id.linear_tabs_competition_playerRight);
	}

	public void setLeftPlayer(String teamName, int playerId) {
		currentPlayerLeftId = playerId;
		teamPLName = teamName;
		((HomeActivity) getActivity()).startAnimation();
		RemotePlayerDAO.getInstance(mContext).addListener(this);
		RemotePlayerDAO.getInstance(mContext).refreshDatabaseWithNewResults(
				playerId);

	}

	public void setRightPlayer(String teamName, int playerId) {
		// TODO Auto-generated method stub
		if (currentPlayerLeftId == 0) {
			currentPlayerLeftId = playerId;
			teamPLName = teamName;
		} else {
			currentPlayerRightId = playerId;
			teamPRName = teamName;
		}

		((HomeActivity) getActivity()).startAnimation();
		RemotePlayerDAO.getInstance(mContext).addListener(this);
		RemotePlayerDAO.getInstance(mContext).refreshDatabaseWithNewResults(
				playerId);

	}

	private void loadPlayers(Player player) {
		if (currentPlayerLeftId == 0 || currentPlayerLeftId == player.getId()) {
			currentPlayerLeft = player;

		} else if (currentPlayerRightId == player.getId()) {
			currentPlayerRight = player;

		}

		loadPlayers();
	}

	private void loadPlayers() {
		if (currentPlayerLeft != null) {
			loadLeftPlayer();
			if (currentPlayerRight != null) {
				loadRightPlayer();
				generalView.findViewById(R.id.dataPlayersScroll).setVisibility(
						View.VISIBLE);
				generalView.findViewById(R.id.noDataPlayersScroll)
						.setVisibility(View.GONE);
				loadLeftVSRight();
				StatisticsDAO.getInstance(mContext).sendStatisticsState(
						getActivity().getApplication(),
						Omniture.SECTION_COMPARATOR,
						null,
						null,
						null,
						Omniture.DETAILPAGE_RESULTADO,
						Omniture.DETAILPAGE_RESULTADO + ":"
								+ Omniture.DETAILPAGE_RESULTADO + " "
								+ Omniture.SECTION_COMPARATOR, null);
			} else {
				callToOmniture();
				generalView.findViewById(R.id.noDataPlayersScroll)
						.setVisibility(View.VISIBLE);
				generalView.findViewById(R.id.dataPlayersScroll).setVisibility(
						View.GONE);
			}
		}

	}

	/*********************************** General Data Methods ****************************************/
	private void loadLeftPlayer() {
		ImageView imageView = (ImageView) generalView
				.findViewById(R.id.photoPlayerLeft);
		String url = currentPlayerLeft.getUrlFoto();
		if (url != null) {

			ImageDAO.getInstance(mContext).loadPlayerImage(url, imageView,
					R.drawable.mask_foto);
		} else {
			imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.foto_generica));
		}

		generalView.findViewById(R.id.playerDataContentLeft).setVisibility(
				View.VISIBLE);
		generalView.findViewById(R.id.dorsalContentLeft).setVisibility(
				View.VISIBLE);
		messageNoPlayerLeft.setVisibility(View.GONE);

		namePlayerLeft.setText(currentPlayerLeft.getName());
		teamPlayerLeft.setText(teamPLName);
		if (currentPlayerLeft.getDateBorn() != null
				&& !currentPlayerLeft.getDateBorn().equalsIgnoreCase("")) {
			datePlayerLeft.setText(currentPlayerLeft.getDateBorn());
			datePlayerLeft.setVisibility(View.VISIBLE);
		} else {
			datePlayerLeft.setVisibility(View.GONE);
		}

		String pais = null;
		if (currentPlayerLeft.getNacionality() != null
				&& !currentPlayerLeft.getNacionality().equalsIgnoreCase("")) {
			pais = currentPlayerLeft.getNacionality();
			if (currentPlayerLeft.getAge() > 0)
				pais += getString(R.string.separator)
						+ currentPlayerLeft.getAge()
						+ getString(R.string.player_anos).toUpperCase();
		} else {
			if (currentPlayerLeft.getAge() > 0)
				pais = getString(R.string.separator)
						+ currentPlayerLeft.getAge()
						+ getString(R.string.player_anos).toUpperCase();
		}

		if (pais != null && !pais.equalsIgnoreCase("")) {
			paisPlayerLeft.setText(pais);
			paisPlayerLeft.setVisibility(View.VISIBLE);
		} else {
			paisPlayerLeft.setVisibility(View.GONE);
		}

		String positionPL = currentPlayerLeft.getDemarcation();
		if (positionPL != null)
			positionPlayerLeft.setText(positionPL.toUpperCase());
		int dorsal = currentPlayerLeft.getDorsal();
		if (dorsal > 0) {
			playerDorsalLeft.setText(String.valueOf(dorsal));
			playerDorsalLeft.setVisibility(View.VISIBLE);
		} else {
			playerDorsalLeft.setVisibility(View.INVISIBLE);
		}

	}

	private void loadRightPlayer() {
		ImageView imageView = (ImageView) generalView
				.findViewById(R.id.photoPlayerRight);
		String url = currentPlayerRight.getUrlFoto();
		if (url != null) {

			ImageDAO.getInstance(mContext).loadImage(url, imageView,
					R.drawable.mask_foto);
		} else {
			imageView.setImageDrawable(getResources().getDrawable(
					R.drawable.foto_generica));
		}

		generalView.findViewById(R.id.playerImagesContentRight).setVisibility(
				View.VISIBLE);
		generalView.findViewById(R.id.dorsalContentRight).setVisibility(
				View.VISIBLE);
		generalView.findViewById(R.id.playerDataContentRight).setVisibility(
				View.VISIBLE);
		messageNoPlayerRight.setVisibility(View.GONE);

		namePlayerRight.setText(currentPlayerRight.getName());
		teamPlayerRight.setText(teamPRName);
		if (currentPlayerRight.getDateBorn() != null
				&& !currentPlayerRight.getDateBorn().equalsIgnoreCase("")) {
			datePlayerRight.setText(currentPlayerRight.getDateBorn());
			datePlayerRight.setVisibility(View.VISIBLE);
		} else {
			datePlayerRight.setVisibility(View.GONE);
		}

		String pais = null;
		if (currentPlayerRight.getNacionality() != null
				&& !currentPlayerRight.getNacionality().equalsIgnoreCase("")) {
			pais = currentPlayerRight.getNacionality();
			if (currentPlayerRight.getAge() > 0)
				pais += getString(R.string.separator)
						+ currentPlayerRight.getAge()
						+ getString(R.string.player_anos).toUpperCase();
		} else {
			if (currentPlayerRight.getAge() > 0)
				pais = getString(R.string.separator)
						+ currentPlayerRight.getAge()
						+ getString(R.string.player_anos).toUpperCase();
		}

		if (pais != null && !pais.equalsIgnoreCase("")) {
			paisPlayerRight.setText(pais);
			paisPlayerRight.setVisibility(View.VISIBLE);
		} else {
			paisPlayerRight.setVisibility(View.GONE);
		}

		String positionPR = currentPlayerRight.getDemarcation();
		if (positionPR != null)
			positionPlayerRight.setText(positionPR.toUpperCase());
		int dorsal = currentPlayerRight.getDorsal();
		if (dorsal > 0) {
			playerDorsalRight.setText(String.valueOf(dorsal));
			playerDorsalRight.setVisibility(View.VISIBLE);
		} else {
			playerDorsalRight.setVisibility(View.INVISIBLE);
		}
	}

	private void loadLeftVSRight() {

		yearsPLArray = new ArrayList<String>(currentPlayerLeft.getStats()
				.keySet());
		currentPLYear=0;

		yearsPRArray = new ArrayList<String>(currentPlayerRight.getStats()
				.keySet());
		currentPRYear=0;

		setPalmaresPL();
		setPalmaresPR();

		if (yearsPLArray.size() > 0 && yearsPRArray.size() > 0) {
			Collections.sort(yearsPLArray, new YearComparator());
			configureYearsPLScroll(yearsPLArray);

			Collections.sort(yearsPRArray, new YearComparator());
			configureYearsPRScroll(yearsPRArray);
		}else{
			yearPLLinear.removeAllViews();
			buttonYearPLPrev.setVisibility(View.GONE);
			buttonYearPLNext.setVisibility(View.GONE);
			
			competitionPLLinear.removeAllViews();
			buttonCompPLPrev.setVisibility(View.GONE);
			buttonCompPLNext.setVisibility(View.GONE);
			
			yearPRLinear.removeAllViews();
			buttonYearPRPrev.setVisibility(View.GONE);
			buttonYearPRNext.setVisibility(View.GONE);
			
			competitionPRLinear.removeAllViews();
			buttonCompPRPrev.setVisibility(View.GONE);
			buttonCompPRNext.setVisibility(View.GONE);
			
			statsLinear.removeAllViews();
		}

	}

	/*********************************** General Data Methods ****************************************/

	/*********************************** Palmares Methods ****************************************/
	private void setPalmaresPL() {
		LinearLayout palmaresLinearPL = (LinearLayout) generalView
				.findViewById(R.id.palmaresLinearPL);
		palmaresLinearPL.removeAllViews();
		ArrayList<TituloPlayer> palmaresPL = currentPlayerLeft.getPalmares();
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

	private void setPalmaresPR() {
		LinearLayout palmaresLinearPR = (LinearLayout) generalView
				.findViewById(R.id.palmaresLinearPR);
		palmaresLinearPR.removeAllViews();
		ArrayList<TituloPlayer> palmaresPR = currentPlayerRight.getPalmares();
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

	/*********************************** Palmares Methods ****************************************/

	/******************************************************************************************/
	/*********************************** Stats Methods ****************************************/
	/******************************************************************************************/
	/****************************** YearPlayerRight Methods ***********************************/
	private void configureYearsPRScroll(ArrayList<String> yearsArray) {

		yearPRLinear.removeAllViews();

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

					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

		PlayerStats playerStat = currentPlayerRight.getStats(yearsPRArray
				.get(currentPRYear));
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
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
		PlayerStats playerStat = currentPlayerRight.getStats(yearsPRArray
				.get(currentPRYear));
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

	/****************************** YearPlayerRight Methods ***********************************/
	/*************************** CompetitionPlayerRight Methods *******************************/

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

					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

	/*************************** CompetitionPlayerRight Methods *******************************/
	/******************************* YearPlayerLeft Methods ***********************************/

	private void configureYearsPLScroll(ArrayList<String> yearsArray) {

		yearPLLinear.removeAllViews();
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

					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

		PlayerStats playerStat = currentPlayerLeft.getStats(yearsPLArray
				.get(currentPLYear));
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
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

		PlayerStats playerStat = currentPlayerLeft.getStats(yearsPLArray
				.get(currentPLYear));
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

	/******************************* YearPlayerLeft Methods ***********************************/
	/**************************** CompetitionPlayerLeft Methods *******************************/

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

					@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

	/**************************** CompetitionPlayerLeft Methods *******************************/
	/************************************ Stats Methods ***************************************/

	private void configureStats() {
		if (currentPLCompetition != -1 && currentPRCompetition != -1) {
			statsLinear.removeAllViews();
			ItemStats itemStatsPL = null, itemStatsPR = null;

			String currentYear = yearsPLArray.get(currentPLYear);
			String currentComp = competitionPLArray.get(currentPLCompetition);
			if (currentYear != null && !currentYear.equalsIgnoreCase("")
					&& currentComp != null && !currentComp.equalsIgnoreCase("")) {
				PlayerStats playerStatPL = currentPlayerLeft
						.getStats(currentYear);
				if (playerStatPL != null)
					itemStatsPL = playerStatPL.getStats(currentComp);
			}

			currentYear = yearsPRArray.get(currentPRYear);
			currentComp = competitionPRArray.get(currentPRCompetition);
			if (currentYear != null && !currentYear.equalsIgnoreCase("")
					&& currentComp != null && !currentComp.equalsIgnoreCase("")) {
				PlayerStats playerStatPR = currentPlayerRight
						.getStats(currentYear);
				if (playerStatPR != null)
					itemStatsPR = playerStatPR.getStats(currentComp);
			}
			if (itemStatsPL != null && itemStatsPR != null)
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

		if (currentPlayerLeft.getDemarcation().equalsIgnoreCase("portero")
				&& currentPlayerRight.getDemarcation().equalsIgnoreCase(
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
		} else if ((getArguments().getString("positionPL") != null && getArguments()
				.getString("positionPL").equalsIgnoreCase("portero"))
				|| (getArguments().getString("positionPR") != null && getArguments()
						.getString("positionPR").equalsIgnoreCase("portero"))) {
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

	/************************************ Stats Methods ***************************************/
	/******************************************************************************************/
	/*********************************** Stats Methods ****************************************/
	/******************************************************************************************/

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
				Omniture.SECTION_COMPARATOR,
				null,
				null,
				null,
				Omniture.TYPE_PORTADA,
				Omniture.TYPE_PORTADA + ":" + Omniture.DETAILPAGE_DETALLE + " "
						+ Omniture.SECTION_COMPARATOR, null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_COMPARATOR);
	}

	/***************************************************************************/
	/** RemotePlayerDAOListener **/
	/***************************************************************************/
	@Override
	public void onSuccessPlayerRemoteconfig(Player player) {
		RemotePlayerDAO.getInstance(mContext).removeListener(this);
		loadPlayers(player);
		((HomeActivity) getActivity()).stopAnimation();
	}

	@Override
	public void onFailurePlayerRemoteconfig() {
		RemotePlayerDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_download_error),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		((HomeActivity) getActivity()).stopAnimation();
	}

	@Override
	public void onFailureNotPlayerConnection(int currentPlayerId) {
		RemotePlayerDAO.getInstance(mContext).removeListener(this);
		Player player = DatabaseDAO.getInstance(mContext).getPlayer(
				currentPlayerId);
		if (player != null) {
			loadPlayers(player);
		} else {

		}
		((HomeActivity) getActivity()).stopAnimation();
	}

	@Override
	public void forceUpdatePlayerNeeded() {
		RemotePlayerDAO.getInstance(mContext).removeListener(this);

	}
	/***************************************************************************/
	/** RemotePlayerDAOListener **/
	/***************************************************************************/

}
