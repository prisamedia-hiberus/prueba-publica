package com.diarioas.guialigas.activities.team.fragment;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.home.fragment.CompetitionHomeFragment;
import com.diarioas.guialigas.activities.home.fragment.CompetitionHomeGroupFragment;
import com.diarioas.guialigas.activities.home.fragment.CompetitionTeamHomeFragment;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.TeamSection;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
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

public class TeamsSectionFragment extends SectionFragment implements
		ScrollEndListener, OnPageChangeListener {

	private static final int SCROLL_WITH = 5;

	private CustomViewPagerLeague leagueViewPager;
	private CustomHoizontalScroll countrySroll;
	private ImageView buttonPrev;
	private ImageView buttonNext;

	private int width;
	private int widthButton;
	private boolean headerVisibility;

	private ArrayList<Competition> competitions;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.adSection = NativeAds.AD_PORTADA;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		generalView = inflater.inflate(R.layout.fragment_teams_section,
				container, false);
		return generalView;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	public void setReturnFromReorder() {
		super.setReturnFromReorder();
		competitions = RemoteDataDAO.getInstance(mContext).getOrderedCompetitions();
		if (competitions != null && competitions.size() > 1) {
			configureHeader(competitions);
			headerVisibility = true;
		} else {
			countrySroll.setVisibility(View.GONE);
			headerVisibility = false;
		}
		configViewPager();
		
	}
	@Override
	protected void loadInformation() {
		if (competitions != null && competitions.size() > 1) {
			configureHeader(competitions);
			headerVisibility = true;
		} else {
			countrySroll.setVisibility(View.GONE);
			headerVisibility = false;
		}
		configViewPager();
	}

	@Override
	protected void configureView() {
		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		width = size.x;
		
		widthButton = width / SCROLL_WITH;

		competitions = RemoteDataDAO.getInstance(mContext).getOrderedCompetitions();
		countrySroll = (CustomHoizontalScroll) generalView
				.findViewById(R.id.countrySroll);
		callToOmniture();
	}

	private void configureHeader(final ArrayList<Competition> competitions) {
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < competitions.size(); i++) {
			strings.add(competitions.get(i).getName());
		}

		countrySroll.setScreenWidth(width);
		countrySroll.setFont(FontTypes.ROBOTO_REGULAR);
		countrySroll.setMainColor(getResources().getColor(
				R.color.red_comparator));
		countrySroll.setSecondColor(getResources()
				.getColor(R.color.medium_gray));
		countrySroll.addScrollEndListener(this);
		int previousPosition = getPreviousTeamSectionPosition();
		countrySroll.setInitPosition(previousPosition);
		countrySroll.addViews(strings);

		buttonPrev = (ImageView) generalView.findViewById(R.id.buttonPrev);
		((View) buttonPrev.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (leagueViewPager.getCurrentItem() > 0)
							leagueViewPager.setCurrentItem(leagueViewPager
									.getCurrentItem() - 1);

						callToOmniture();
					}
				});
		buttonNext = (ImageView) generalView.findViewById(R.id.buttonNext);
		((View) buttonNext.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (leagueViewPager.getCurrentItem() < competitions
								.size())
							leagueViewPager.setCurrentItem(leagueViewPager
									.getCurrentItem() + 1);

						callToOmniture();
					}
				});

		final RelativeLayout rel1 = (RelativeLayout) generalView
				.findViewById(R.id.contentCompetition);
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
						generalView.findViewById(R.id.buttonLeft)
								.setLayoutParams(paramsLeft);

						RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
								widthButton, rel1.getHeight());
						paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						generalView.findViewById(R.id.buttonRight)
								.setLayoutParams(paramsRight);
					}
				});
	}

	private void configViewPager() {
		leagueViewPager = (CustomViewPagerLeague) generalView
				.findViewById(R.id.leagueViewPager);

		List<Fragment> fragments = getFragments();
		// if (competitions.get(0).getId() != 0) {
		// currentCompetition = competitions.get(0);
		// }

		leagueViewPager.setAdapter(new FragmentAdapter(
				getChildFragmentManager(), fragments));
		int previousPosition = getPreviousTeamSectionPosition();
		if ((previousPosition>=0)&&(previousPosition<competitions.size())) {
			leagueViewPager.setCurrentItem(previousPosition);
			leagueViewPager.setOnPageChangeListener(this);

			setButtonsVisibility();

			((HomeActivity) getActivity()).stopAnimation();
			
			
	
		}
		
	}
	
	private int getPreviousTeamSectionPosition() {
		if ((competitions!=null)&&(competitions.size()>0)) {			
			int currentCompetitionId = Integer.parseInt(this.competitionId);
			for (int i = 0; i < competitions.size(); i++) {
				Competition compt = (Competition)competitions.get(i);
				if (compt!=null) {
					
					if (compt.getId()==currentCompetitionId) {
						return i;						
					}
				}
						
			}
		}
		
		return -1;
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		CompetitionHomeFragment competitionFragment;
		Bundle args;
		boolean manyCompetititons = competitions.size() > 1;
		String typeOrder;
		TeamSection currentSection;
		for (Competition competition : competitions) {
			leagueViewPager.addChildId(competition.getId() * 100);
			currentSection = (TeamSection) RemoteDataDAO.getInstance(mContext).getGeneralSettings().getCompetition(competition.getId()).getSection(SECTIONS.TEAMS);
			if (section!=null) {
				typeOrder = currentSection.getTypeOrder();
	//			String typeOrder = ((TeamSection) section).getTypeOrder();
				if (typeOrder == null || typeOrder.equalsIgnoreCase(SECTIONS.TEAMS_ORDER_GROUP))
					competitionFragment = new CompetitionHomeGroupFragment();
				else
					competitionFragment = new CompetitionTeamHomeFragment();
	
				args = new Bundle();
				args.putInt("competitionId", competition.getId());
				args.putString("competitionName", competition.getName());
				args.putBoolean("manyCompetititons", manyCompetititons);
				competitionFragment.setArguments(args);
				fList.add(competitionFragment);
			}
		}

		return fList;
	}

	private void setButtonsVisibility() {
		if (!headerVisibility)
			return;

		if (leagueViewPager.getCurrentItem() > 0) {
			buttonPrev.setVisibility(View.VISIBLE);
		} else {
			buttonPrev.setVisibility(View.INVISIBLE);
		}

		if (leagueViewPager.getCurrentItem() < competitions.size() - 1) {
			buttonNext.setVisibility(View.VISIBLE);
		} else {
			buttonNext.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void setCurrentcompetition(int position) {
		countrySroll.setHeaderPosition(position);
		setButtonsVisibility();
		
		if ((competitions!=null)&&(competitions.size()>position)) {
			Competition compt = (Competition)competitions.get(position);
			this.competitionId=Integer.toString(compt.getId());
		}
		
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
				RemoteDataDAO.getInstance(this.mContext).getGeneralSettings().getCurrentCompetition().getName().toLowerCase(),
                null,
				null,
				null,
                FileUtils.readOmnitureProperties(mContext, "TYPE_PORTADA"),
                FileUtils.readOmnitureProperties(mContext, "TYPE_HOME"),
				null);
	}

	/***************************************************************************/
	/** ScrollEndListener **/
	/***************************************************************************/

	@Override
	public void onScrollEnd(int x, int y, int oldx, int oldy, int pos) {
		leagueViewPager.setCurrentItem(pos, true);					
	}

	@Override
	public void onItemClicked(int position) {
		
	}

	/***************************************************************************/
	/** OnPageChangeListener methods **/
	/***************************************************************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int pos) {
		((HomeActivity) getActivity()).setCurrentcompetition(pos);
	}

}
