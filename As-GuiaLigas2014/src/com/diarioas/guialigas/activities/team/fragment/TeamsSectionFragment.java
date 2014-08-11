package com.diarioas.guialigas.activities.team.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll.ScrollEndListener;
import com.diarioas.guialigas.utils.viewpager.CustomViewPagerLeague;

public class TeamsSectionFragment extends SectionFragment implements
		ScrollEndListener, OnPageChangeListener {

	private static final int SCROLL_WITH = 5;

	private CustomViewPagerLeague leagueViewPager;
	// private CustomHeaderMagneticHorizontalScroll countrySroll;
	private CustomHoizontalScroll countrySroll;
	private ImageView buttonPrev;
	private ImageView buttonNext;

	private int width;
	private int widthButton;
	private boolean headerVisibility;

	private ArrayList<Competition> competitions;

	private int currentComp;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_teams_section,
				container, false);
		return generalView;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void buildView() {
		 currentComp=0;
		if (competitionId !=null) {
//			 for (Competition competition : competitions) {
			 for (int i = 0; i < competitions.size(); i++) {
				if (competitionId.equalsIgnoreCase(String.valueOf(competitions.get(i).getId()))){
					currentComp = i;
					break;
				}
			}
		 }
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

		width = ((HomeActivity) getActivity()).getWidth();
		widthButton = width / SCROLL_WITH;

		competitions = RemoteDataDAO.getInstance(mContext).getGeneralSettings()
				.getCompetitions();
		countrySroll = (CustomHoizontalScroll) generalView
				.findViewById(R.id.countrySroll);
		
	}

	private void configureHeader(final ArrayList<Competition> competitions) {
		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < competitions.size(); i++) {
			strings.add(competitions.get(i).getName());
		}

		countrySroll.setScreenWidth(width);
		countrySroll.setFont(FontTypes.HELVETICANEUE);
		countrySroll.setMainColor(getResources().getColor(
				R.color.red_comparator));
		countrySroll.setSecondColor(getResources()
				.getColor(R.color.medium_gray));
		countrySroll.addScrollEndListener(this);
		countrySroll.addViews(strings);
		
		buttonPrev = (ImageView) generalView.findViewById(R.id.buttonPrev);
		((View) buttonPrev.getParent())
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (leagueViewPager.getCurrentItem() > 0)
							leagueViewPager.setCurrentItem(leagueViewPager
									.getCurrentItem() - 1);
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


		leagueViewPager.setAdapter(new FragmentAdapter(
				getChildFragmentManager(), fragments));
		
		
		leagueViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					leagueViewPager.getViewTreeObserver()
							.removeOnGlobalLayoutListener(this);
				} else {
					leagueViewPager.getViewTreeObserver()
							.removeGlobalOnLayoutListener(this);
				}
				leagueViewPager.setCurrentItem(currentComp, true);
			}
		}
		);
		
		
		leagueViewPager.setOnPageChangeListener(this);
		setButtonsVisibility();
		callToOmniture();
		((HomeActivity) getActivity()).stopAnimation();
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		CompetitionHomeFragment competitionFragment;
		Bundle args;
		boolean manyCompetititons = competitions.size() > 1;
		TeamSection sectionAux;
		for (Competition competition : competitions) {
			sectionAux = (TeamSection) RemoteDataDAO.getInstance(mContext)
					.getGeneralSettings().getCompetition(competition.getId())
					.getSection(SECTIONS.TEAMS);
			leagueViewPager.addChildId(competition.getId() * 100);
			String typeOrder = ((TeamSection) sectionAux).getTypeOrder();
			// Log.d("GROUPS",
			// "seccion: "+sectionAux.getName()+" Orden: "+typeOrder);
			if (typeOrder == null
					|| typeOrder
							.equalsIgnoreCase(SECTIONS.TEAMS_ORDER_ALPHABETIC))
				competitionFragment = new CompetitionTeamHomeFragment();
			else
				competitionFragment = new CompetitionHomeGroupFragment();

			args = new Bundle();
			args.putInt("competitionId", competition.getId());
			args.putString("competitionName", competition.getName());
			args.putBoolean("manyCompetititons", manyCompetititons);
			competitionFragment.setArguments(args);
			fList.add(competitionFragment);
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
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		String competitionName = competitions.get(leagueViewPager.getCurrentItem()).getName();
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(), competitionName,Omniture.SECTION_PORTADA,
				null, null, Omniture.TYPE_PORTADA, Omniture.DETAILPAGE_PORTADA,
				null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_PORTADA);
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
		leagueViewPager.setCurrentItem(position, true);
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
		callToOmniture();

	}

}
