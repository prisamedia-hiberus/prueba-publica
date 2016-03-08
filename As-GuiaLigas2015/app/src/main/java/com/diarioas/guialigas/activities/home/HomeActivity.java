package com.diarioas.guialigas.activities.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.BuildConfig;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.calendar.fragment.CalendarSectionFragment;
import com.diarioas.guialigas.activities.carrusel.fragment.CarrouselSectionFragment;
import com.diarioas.guialigas.activities.clasification.fragment.ClasificationSectionFragment;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.general.fragment.GeneralWebViewFragment;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.fragment.HomeMenuFragment;
import com.diarioas.guialigas.activities.link.fragment.LinkSectionFragment;
import com.diarioas.guialigas.activities.news.fragment.NewsSectionFragment;
import com.diarioas.guialigas.activities.news.fragment.NewsTagSectionFragment;
import com.diarioas.guialigas.activities.photo.fragment.PhotosSectionFragment;
import com.diarioas.guialigas.activities.player.comparator.fragment.ComparatorPlayerSectionFragment;
import com.diarioas.guialigas.activities.search.fragment.SearcherSectionFragment;
import com.diarioas.guialigas.activities.sort.SortActivity;
import com.diarioas.guialigas.activities.team.fragment.TeamsSectionFragment;
import com.diarioas.guialigas.activities.videos.fragment.VideosSectionFragment;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.Defines.SECTIONS;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

import java.util.ArrayList;

public class HomeActivity extends GeneralFragmentActivity implements
		android.support.v4.widget.SlidingPaneLayout.PanelSlideListener {

	private Context mContext = null;
	private FragmentManager fragmentManager;

	protected boolean sectionChanged;
	protected int selectedSectionIndex;
	private SectionFragment selectedFragment;

	private boolean firstTime = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mContext = this;
		this.fragmentManager = getSupportFragmentManager();
		setContentView(R.layout.activity_home_fragments);

		configureView();
	}

	@Override
	public void onBackPressed() {
		if (this.fragmentManager.getBackStackEntryCount() > 0) {
			this.fragmentManager.popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!this.firstTime) {
			this.firstTime = true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ReturnRequestCodes.PUBLI_BACK) {
				this.selectedFragment.callToAds();
				if (selectedFragment instanceof SectionFragment) {
					((SectionFragment) selectedFragment)
							.setReturnFromDetail(true);
				}
			} else if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_LEFT) {
				if (data != null && data.getExtras() != null
						&& data.getExtras().containsKey("playerId")
						&& data.getExtras().containsKey("teamName")) {
					String teamName = (String) data.getExtras().get("teamName");
					int playerId = (Integer) data.getExtras().get("playerId");
					((ComparatorPlayerSectionFragment) this.selectedFragment)
							.setLeftPlayer(teamName, playerId);

				}
			} else if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_RIGHT) {
				if (data != null && data.getExtras() != null
						&& data.getExtras().containsKey("playerId")
						&& data.getExtras().containsKey("teamName")) {
					String teamName = (String) data.getExtras().get("teamName");
					int playerId = (Integer) data.getExtras().get("playerId");
					((ComparatorPlayerSectionFragment) this.selectedFragment)
							.setRightPlayer(teamName, playerId);
				}
			}else if(requestCode == ReturnRequestCodes.SORT_BACK) {
				if (selectedFragment instanceof SectionFragment) {
					((SectionFragment) selectedFragment).setReturnFromReorder();					
				}								
			}
		}
	}

	/******************* Configuration methods ********************************/
	/************************************************************************/

	protected void configureView() {
		SlidingPaneLayout slidingPane = (SlidingPaneLayout) findViewById(R.id.slidingPane);
		slidingPane.setPanelSlideListener(this);

		RelativeLayout menu_button_actionBar = (RelativeLayout) findViewById(R.id.menu_button_actionBar);
		menu_button_actionBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				togglePane();
			}
		});
		
		String header = DatabaseDAO.getInstance(mContext).getHeaderInfo();
		if (header!=null && header.length()>0) {
			 findViewById(R.id.ads_button_actionBar).setVisibility(View.VISIBLE);
			 ImageDAO.getInstance(mContext).loadHeaderSectionImage(header, (ImageView) findViewById(R.id.adsButton));
//			 findViewById(R.id.ads_button_actionBar).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
			
		}
//		

		this.spinner = (RelativeLayout) findViewById(R.id.spinner);
		FontUtils.setCustomfont(mContext, (findViewById(R.id.spinnerMessage)),
				FontTypes.ROBOTO_REGULAR);

		hideActionBar();
		configureDefaultSection();


		//Check if the user run the application by first time and load the sort view
		checkFirstTimeRun();
	}

	private void configureDefaultSection() {
		int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		Section defaultSection = RemoteDataDAO.getInstance(this.mContext)
				.getDefaultSections(currentCompetitionId);
		if (defaultSection != null) {
			this.selectedSectionIndex = defaultSection.getOrder() - 1;
			startAnimation();
			loadSectionFragment(defaultSection, currentCompetitionId);
		}

	}

	/**
	 *	checkFirstTimeRun
	 *
	 * This method will check if the user was running by first time the app, and the load the
	 * order view, to let the user to personalize all the cometitions
	 */
	private void checkFirstTimeRun() {
		if (RemoteDataDAO.getInstance(this.mContext).isFirstTimeRun()) {
            if(!BuildConfig.SINGLE_COMPETITION){
                openSortSection();
            }
		}
	}

	/******************* Section methods ********************************/
	/************************************************************************/

	private void loadSectionFragment(Section section, int competitionId) {

		
//		if (section.getType().equalsIgnoreCase(SECTIONS.ORDER)) {
//			openSortSection();
//			stopAnimation();
//		}else {
			configureLeftMenuSelectedItem(section.getType());
			this.fragmentManager.popBackStackImmediate();
	
			FragmentTransaction fragmentTransaction = this.fragmentManager
					.beginTransaction();
			this.selectedFragment = getSelectedFragment(section, competitionId);
	
			fragmentTransaction.replace(R.id.section_fragment,
					this.selectedFragment, section.getName());
			fragmentTransaction.commit();
//		}
	}

	public void openSortSection() {
		Intent intent = new Intent(mContext,
				SortActivity.class);

		startActivityForResult(intent,ReturnRequestCodes.SORT_BACK);
		overridePendingTransition(
				R.anim.slide_in_up, R.anim.null_anim);
		
	}

	private void configureLeftMenuSelectedItem(String type) {

		HomeMenuFragment homeMenuFragment = (HomeMenuFragment) this.fragmentManager
				.findFragmentById(R.id.left_menu);
		homeMenuFragment.reloadMenuList(type);
	}

	private SectionFragment getSelectedFragment(Section section,
			int competitionId) {
		SectionFragment fragment = null;
		Bundle args = new Bundle();

		if (section.getType().equalsIgnoreCase(SECTIONS.WEB_VIEW)) {
			fragment = new GeneralWebViewFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.CALENDAR)) {
			fragment = new CalendarSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.CARROUSEL)) {
			fragment = new CarrouselSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.CLASIFICATION)) {
			fragment = new ClasificationSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.PALMARES)) {
			fragment = new GeneralWebViewFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.STADIUMS)) {
			fragment = new GeneralWebViewFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.TEAMS)) {
			fragment = new TeamsSectionFragment();
			ArrayList<String> competitionsId = new ArrayList<String>();
			for (Competition competition : RemoteDataDAO.getInstance(mContext)
					.getGeneralSettings().getCompetitions()) {
				competitionsId.add(String.valueOf(competition.getId()));
			}
			args.putStringArrayList("competitions", competitionsId);
		} else if (section.getType().equalsIgnoreCase(SECTIONS.COMPARATOR)) {
			fragment = new ComparatorPlayerSectionFragment();			
		} else if (section.getType().equalsIgnoreCase(SECTIONS.SEARCHER)) {
			fragment = new SearcherSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.NEWS)) {
            fragment = section.getViewType().equals("tag_view") ? new NewsTagSectionFragment() : new NewsSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.VIDEOS)) {
			fragment = new VideosSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.PHOTOS)) {
			fragment = new PhotosSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.LINK)) {
			fragment = new LinkSectionFragment();
		}
		
		if (fragment!=null) {
			args.putSerializable("section", section);
			args.putString("competitionId", String.valueOf(competitionId));
			fragment.setArguments(args);
		}
		return fragment;

	}

	/**************** Call from external methods *****************************/
	/************************************************************************/

	public void setCurrentcompetition(int position) {
		HomeMenuFragment homeMenuFragment = (HomeMenuFragment) this.fragmentManager
				.findFragmentById(R.id.left_menu);
		homeMenuFragment.setCurrentCompetition(position);
		this.selectedFragment.setCurrentcompetition(position);
	}

	public void openLink(String url) {
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			url = "http://" + url;
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	public boolean isSectionChanged() {
		return this.sectionChanged;
	}

	public void setSectionChanged(boolean sectionChanged) {
		this.sectionChanged = sectionChanged;
	}

	public int getSelectedSectionIndex() {
		return this.selectedSectionIndex;
	}

	public void setSelectedSectionIndex(int selectedSectionIndex) {
		this.selectedSectionIndex = selectedSectionIndex;
	}

	/************** SlidingPaneLayout.PanelSlideListener methods ***************/
	/***************************************************************************/

	@Override
	public void onPanelClosed(View arg0) {
		findViewById(R.id.menuButton).setBackgroundResource(
				R.drawable.button_menu_on);
		if (this.sectionChanged) {

			HomeMenuFragment homeMenuFragment = (HomeMenuFragment) this.fragmentManager
					.findFragmentById(R.id.left_menu);
			Section selectedSection = (Section) homeMenuFragment
					.getItemAtPosition(this.selectedSectionIndex);

			int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
					.getGeneralSettings().getCurrentCompetition().getId();
			loadSectionFragment(selectedSection, currentCompetitionId);

			this.sectionChanged = false;
		}
	}

	@Override
	public void onPanelOpened(View arg0) {
		findViewById(R.id.menuButton).setBackgroundResource(
				R.drawable.button_menu_off);
	}

	@Override
	public void onPanelSlide(View arg0, float arg1) {
	}

	public void togglePane() {
		SlidingPaneLayout slidingPane = (SlidingPaneLayout) findViewById(R.id.slidingPane);
		if (slidingPane.isOpen()) {
			slidingPane.closePane();
		} else {
			slidingPane.openPane();
		}
	}
}
