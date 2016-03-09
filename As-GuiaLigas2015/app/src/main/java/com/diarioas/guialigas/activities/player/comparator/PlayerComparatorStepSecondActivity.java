/**
 * 
 */
package com.diarioas.guialigas.activities.player.comparator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.home.fragment.CompetitionHomeFragment;
import com.diarioas.guialigas.activities.player.comparator.fragment.ComparatorPlayerSelectTeam;
import com.diarioas.guialigas.activities.player.comparator.fragment.ComparatorPlayerSelectTeamGroup;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.TeamSection;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.Defines.SECTIONS;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FileUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll.ScrollEndListener;
import com.diarioas.guialigas.utils.viewpager.CustomViewPagerLeague;

import java.util.ArrayList;
import java.util.List;

/**
 * @author robertosanchez
 * 
 */
public class PlayerComparatorStepSecondActivity extends GeneralFragmentActivity
		implements ViewPager.OnPageChangeListener, ScrollEndListener {

	private static final int PREV_BUTTON = 201;
	private static final int POST_BUTTON = 202;

	private static final int SCROLL_WITH = 5;

	private CustomViewPagerLeague playerComparatorViewPager;
	protected int widthButton;
	private int width;

	/********************** Search RightPlayer ***************************/
	// First Step
	private ArrayList<Competition> competitions;
	private CustomHoizontalScroll countrySroll;

	private ImageView buttonPrev;
	private ImageView buttonNext;
	private boolean headerVisibility;
	private int currentCompetitionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_player_comparator);
		spinner = (RelativeLayout) findViewById(R.id.spinner);

		Point size = DimenUtils.getSize(getWindowManager());

		width = size.x;
		widthButton = size.x / SCROLL_WITH;
		configActionBar();
		competitions = RemoteDataDAO.getInstance(getApplicationContext()).getOrderedCompetitions();
		configView();

	}

	@Override
	public void onResume() {
		super.onResume();
		StatisticsDAO.getInstance(this).sendStatisticsState(
				getApplication(),
                FileUtils.readOmnitureProperties(this, "SECTION_COMPARATOR"),
				FileUtils.readOmnitureProperties(this, "SUBSECTION_TEAMS"),
				null,
				null,
                FileUtils.readOmnitureProperties(this, "TYPE_PORTADA"),
                FileUtils.readOmnitureProperties(this, "DETAILPAGE_DETALLE") + " " + FileUtils.readOmnitureProperties(this, "SECTION_COMPARATOR") + " " + FileUtils.readOmnitureProperties(this, "SUBSECTION_TEAMS") + " " +
						RemoteDataDAO.getInstance(this).getGeneralSettings().getCurrentCompetition().getName().toLowerCase(),
				null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_down);
	}

	@Override
	protected void configActionBar() {
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.header_bar_comparator);

		getSupportActionBar().getCustomView().findViewById(R.id.backContent)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();

					}
				});

		getSupportActionBar().getCustomView().findViewById(R.id.cancelContent)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

	}

	public void buttonClick(View view) {
		int tag = Integer.valueOf((String) view.getTag());
		Log.d("COMPARATOR", "ButtonClicked: " + tag);
		switch (tag) {
		case PREV_BUTTON:
			if (playerComparatorViewPager.getCurrentItem() > 0)
				playerComparatorViewPager
						.setCurrentItem(playerComparatorViewPager
								.getCurrentItem() - 1);
			break;
		case POST_BUTTON:
			if (playerComparatorViewPager.getCurrentItem() < competitions
					.size())
				playerComparatorViewPager
						.setCurrentItem(playerComparatorViewPager
								.getCurrentItem() + 1);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_RETURN_OK) {
				if (data != null && data.getExtras() != null
						&& data.getExtras().containsKey("playerId")
						&& data.getExtras().containsKey("teamName")) {
					int playerId = (Integer) data.getExtras().get("playerId");
					Intent newIntent = new Intent();
					newIntent.putExtra("playerId", playerId);
					String teamName = (String) data.getExtras().get("teamName");
					newIntent.putExtra("teamName", teamName);
					setResult(RESULT_OK, newIntent);
					this.finish();
				}
			} else if (requestCode == ReturnRequestCodes.COMPARATOR_NO_PLAYER) {
				Intent newIntent = new Intent();
				setResult(ReturnRequestCodes.COMPARATOR_NO_PLAYER, newIntent);
				this.finish();
			}
		}
	}

	/************************************ FIRST STEP ******************************************/

	public void initialize() {
		findViewById(R.id.contentCompetition).setVisibility(View.GONE);

		if (buttonPrev != null)
			buttonPrev = null;
		if (buttonNext != null)
			buttonNext = null;
		if (playerComparatorViewPager != null)
			playerComparatorViewPager.removeAllViews();

		configView();
	}

	private void configView() {
		countrySroll = (CustomHoizontalScroll) findViewById(R.id.countrySroll);
		if (competitions.size() > 1) {
			configureHeader();
			headerVisibility = true;
		} else {
			countrySroll.setVisibility(View.GONE);
			headerVisibility = false;
		}
		configViewPagerFirstStep();
	}

	private void configViewPagerFirstStep() {
		playerComparatorViewPager = (CustomViewPagerLeague) findViewById(R.id.leagueViewPager);

		List<Fragment> fragments = getFragmentsFirstStep();

		playerComparatorViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		playerComparatorViewPager.setCurrentItem(0, true);
		currentCompetitionId = competitions.get(0).getId();
		playerComparatorViewPager.setOnPageChangeListener(this);

		setButtonsVisibility();
	}

	private List<Fragment> getFragmentsFirstStep() {
		List<Fragment> fList = new ArrayList<Fragment>();

		CompetitionHomeFragment competitionFragment;
		Bundle args;
		TeamSection section;
		String typeOrder;
		for (Competition competition : competitions) {
			playerComparatorViewPager.addChildId(competition.getId() * 100);
			section = (TeamSection) RemoteDataDAO.getInstance(getApplicationContext()).getGeneralSettings().getCompetition(competition.getId()).getSection(SECTIONS.TEAMS);
			if (section!=null) {
				typeOrder = section.getTypeOrder();
				if (typeOrder == null
						|| typeOrder.equalsIgnoreCase(SECTIONS.TEAMS_ORDER_GROUP))
					competitionFragment = new ComparatorPlayerSelectTeamGroup();
				else
					competitionFragment = new ComparatorPlayerSelectTeam();
				args = new Bundle();
				args.putInt("competitionId", competition.getId());
				args.putString("competitionName", competition.getName());
				competitionFragment.setArguments(args);
				fList.add(competitionFragment);
			}
		}

		return fList;
	}

	@SuppressLint("NewApi")
	private void configureHeader() {
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < competitions.size(); i++) {
			strings.add(competitions.get(i).getName());
		}

		countrySroll.setScreenWidth(width);
		countrySroll.setFont(FontTypes.ROBOTO_LIGHT);
		countrySroll.setMainColor(getResources().getColor(
				R.color.red_comparator));
		countrySroll.setSecondColor(getResources()
				.getColor(R.color.medium_gray));
		countrySroll.addScrollEndListener(this);
		countrySroll.addViews(strings);

		buttonPrev = (ImageView) findViewById(R.id.buttonPrev);
		buttonNext = (ImageView) findViewById(R.id.buttonNext);

		final RelativeLayout rel1 = (RelativeLayout) findViewById(R.id.contentCompetition);

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
						findViewById(R.id.buttonLeft).setLayoutParams(
								paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						findViewById(R.id.buttonRight).setLayoutParams(
								paramsRight);
					}
				});
	}

	private void setButtonsVisibility() {
		if (!headerVisibility)
			return;

		if (playerComparatorViewPager.getCurrentItem() > 0) {
			buttonPrev.setVisibility(View.VISIBLE);
		} else {
			buttonPrev.setVisibility(View.INVISIBLE);
		}

		if (playerComparatorViewPager.getCurrentItem() < competitions.size() - 1) {
			buttonNext.setVisibility(View.VISIBLE);
		} else {
			buttonNext.setVisibility(View.INVISIBLE);
		}

	}

	/************************************ FIRST STEP ******************************************/

	/**************** Metodos de OnPageChangeListener *****************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int pos) {

		countrySroll.setHeaderPosition(pos);
		currentCompetitionId = competitions.get(pos).getId();
		setButtonsVisibility();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll.ScrollEndListener
	 * #onScrollEnd(int, int, int, int, int)
	 */
	@Override
	public void onScrollEnd(int x, int y, int oldx, int oldy, int pos) {
		// TODO Auto-generated method stub
		playerComparatorViewPager.setCurrentItem(pos, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll.ScrollEndListener
	 * #onItemClicked(int)
	 */
	@Override
	public void onItemClicked(int position) {
		// TODO Auto-generated method stub

	}

	/***************************************************************************************************/

}
