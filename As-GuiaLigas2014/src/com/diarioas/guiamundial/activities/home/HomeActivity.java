package com.diarioas.guiamundial.activities.home;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.MainActivity;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.calendar.fragment.CalendarSectionFragment;
import com.diarioas.guiamundial.activities.carrusel.fragment.CarrouselSectionFragment;
import com.diarioas.guiamundial.activities.clasification.fragment.ClasificationSectionFragment;
import com.diarioas.guiamundial.activities.general.fragment.SectionFragment;
import com.diarioas.guiamundial.activities.link.fragment.LinkSectionFragment;
import com.diarioas.guiamundial.activities.news.fragment.NewsSectionFragment;
import com.diarioas.guiamundial.activities.palmares.fragment.PalmaresSectionFragment;
import com.diarioas.guiamundial.activities.photo.fragment.PhotosSectionFragment;
import com.diarioas.guiamundial.activities.player.comparator.fragment.ComparatorPlayerSectionFragment;
import com.diarioas.guiamundial.activities.search.fragment.SearcherSectionFragment;
import com.diarioas.guiamundial.activities.stadiums.fragments.StadiumSectionFragment;
import com.diarioas.guiamundial.activities.team.fragment.TeamsSectionFragment;
import com.diarioas.guiamundial.activities.videos.fragment.VideosSectionFragment;
import com.diarioas.guiamundial.dao.model.competition.Competition;
import com.diarioas.guiamundial.dao.model.general.Section;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.ImageDAO;
import com.diarioas.guiamundial.dao.reader.RemoteDataDAO;
import com.diarioas.guiamundial.utils.Defines.RequestSectionTypes;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.Defines.SECTIONS;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;
import com.diarioas.guiamundial.utils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guiamundial.utils.imageutils.MemoryReleaseUtils;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @author robertosanchez
 * 
 */
public class HomeActivity extends SlidingFragmentActivity implements
		OnOpenListener, OnCloseListener, OnClosedListener {

	private static final int MENU_BUTTON = 101;

	private Context mContext = null;
	private FragmentManager fragmentManager;

	private ListView sectionList;
	private MenuSectionsAdapter menuSectionsAdapter;

	protected final int drawables[] = new int[] { R.drawable.spinner_guia0000,
			R.drawable.spinner_guia0001, R.drawable.spinner_guia0002,
			R.drawable.spinner_guia0003, R.drawable.spinner_guia0004,
			R.drawable.spinner_guia0005, R.drawable.spinner_guia0006,
			R.drawable.spinner_guia0007,R.drawable.spinner_guia0008,
			R.drawable.spinner_guia0009};
	protected RelativeLayout spinner;
	protected boolean sectionChanged;
	protected int selectedSectionIndex;
	//
	private ArrayList<Section> menuSections;
	private SectionFragment selectedFragment;
	private String currentSelectedType;

	// private AdView banner;

	private int height;
	private int width;

	private boolean firstTime = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			Intent reLaunchMain = new Intent(this, MainActivity.class);
			reLaunchMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(reLaunchMain);
		} else {
			this.mContext = this;
			setContentView(R.layout.activity_home_fragments);
			setBehindContentView(R.layout.menu_behind_profiles);

			fragmentManager = getSupportFragmentManager();

		}
		configureView();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		ImageDAO.getInstance(mContext).clearCache();
	}

	@Override
	public void onBackPressed() {
		if (this.fragmentManager.getBackStackEntryCount() > 0) {
			this.fragmentManager.popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ImageDAO.getInstance(mContext).exitTaskEarly();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!firstTime) {
			selectedFragment.closedSlidingMenu();
			firstTime = true;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ImageDAO.getInstance(mContext).exitTaskEarly();
		ImageDAO.getInstance(mContext).flushCache();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// this.banner = null;
		if (this.menuSections != null) {
			this.menuSections.clear();
			this.menuSections = null;
		}
		
		ImageDAO.removeInstance();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ReturnRequestCodes.PUBLI_BACK) {
				selectedFragment.callToAds();
			} else if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_LEFT) {
				if (data != null && data.getExtras() != null
						&& data.getExtras().containsKey("playerId")
						&& data.getExtras().containsKey("teamName")) {
					String teamName = (String) data.getExtras().get("teamName");
					int playerId = (Integer) data.getExtras().get("playerId");
					((ComparatorPlayerSectionFragment) selectedFragment)
							.setLeftPlayer(teamName, playerId);

				}
			} else if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_RIGHT) {
				String teamName = (String) data.getExtras().get("teamName");
				int playerId = (Integer) data.getExtras().get("playerId");
				((ComparatorPlayerSectionFragment) selectedFragment)
						.setRightPlayer(teamName, playerId);
			}

		}
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	/**
	 * Configure all the view
	 */
	protected void configureView() {
		spinner = (RelativeLayout) findViewById(R.id.spinner);
		FontUtils.setCustomfont(mContext, (findViewById(R.id.spinnerMessage)),
				FontTypes.HELVETICANEUE);
		configActionBar();
		configureSize();
		configSlidingMenu();
		configureDefaultSection();
	}

	private void configActionBar() {
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.header_bar);

		final String headerLink = DatabaseDAO.getInstance(mContext)
				.getHeaderInfo();
//		if (headerLink != null) {
//			View adsButtonContainer = getSupportActionBar().getCustomView()
//					.findViewById(R.id.adsButtonContainer);
//			adsButtonContainer.setVisibility(View.VISIBLE);
//			ImageView adsButton = (ImageView) getSupportActionBar()
//					.getCustomView().findViewById(R.id.adsButton);
//			
//			ImageCacheParams cacheParams = new ImageCacheParams(this,
//					Defines.NAME_CACHE_THUMBS+"action");
//			cacheParams.setMemCacheSizePercent(0.25f);
//
//			ImageFetcher mImageFetcher = new ImageFetcher(this, getResources()
//					.getDimensionPixelSize(R.dimen.image_actionbar_height));
//			FragmentManager fragmentManager = this.getSupportFragmentManager();
//			mImageFetcher.addImageCache(fragmentManager, cacheParams);
//			mImageFetcher.setImageFadeIn(true);
//			mImageFetcher.loadImageWhitoutCache((String) headerLink,adsButton);
//			
////			ImageDAO.getInstance(this).loadActionBarImage(headerLink, adsButton);
//			// adsButtonContainer.setOnClickListener(new OnClickListener() {
//			//
//			// @Override
//			// public void onClick(View v) {
//			// // openLink(headerLink);
//			// }
//			// });
//		}
	}

	private void configSlidingMenu() {

		int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		menuSections = RemoteDataDAO.getInstance(this.mContext)
				.getOrderedSections(currentCompetitionId);

		this.getSlidingMenu().setOnOpenListener(this);
		this.getSlidingMenu().setOnCloseListener(this);
		this.getSlidingMenu().setOnClosedListener(this);

		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		getSlidingMenu().setBehindWidth((int) (width * 0.85));

		this.sectionList = (ListView) getSlidingMenu().findViewById(
				R.id.sectionList);
		this.sectionList.setDivider(null);
		this.sectionList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		this.sectionList.setItemsCanFocus(true);
		this.sectionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				if (selectedSectionIndex == position) {
					sectionChanged = false;
				} else {
					Section selectedSection = menuSections.get(position);
					if (selectedSection.isActive()) {
						if (selectedSection.getViewType().equalsIgnoreCase(
								RequestSectionTypes.EXTERNAL_VIEW)) {
							openLink(selectedSection.getUrl());
						} else {
							if (!selectedSection.getType().equalsIgnoreCase(
									SECTIONS.SEARCHER))
								startAnimation();
							sectionChanged = true;
							selectedSectionIndex = position;
						}
						toggle();
					}
				}

			}

		});

		// Configure the section list adapter
		menuSectionsAdapter = new MenuSectionsAdapter();
		menuSectionsAdapter.addItems(menuSections);
		sectionList.setAdapter(menuSectionsAdapter);
	}

	/************************************ mImageFetcher Methods ************************************************************/

	private void configureSize() {
		Point size = DimenUtils.getSize(getWindowManager());

		height = (size.y - getSupportActionBar().getHeight());
		width = size.x;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Configure the default launching section
	 */
	private void configureDefaultSection() {
		int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		Section defaultSection = RemoteDataDAO.getInstance(this.mContext)
				.getDefaultSections(currentCompetitionId);
		if (defaultSection != null) {
			selectedSectionIndex = defaultSection.getOrder() - 1;

			startAnimation();

			loadSectionFragment(defaultSection, currentCompetitionId);
		}

	}

	private void loadSectionFragment(Section section, int competitionId) {

		configureLeftMenuSelectedItem(section.getType());
		this.fragmentManager.popBackStackImmediate();

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		selectedFragment = getSelectedFragment(section, competitionId);

		fragmentTransaction.replace(R.id.section_fragment, selectedFragment,
				section.getName());
		fragmentTransaction.commit();

		// callToAds("portada");

	}

	private void configureLeftMenuSelectedItem(String type) {
		currentSelectedType = type;
		menuSectionsAdapter.notifyDataSetChanged();
	}

	private SectionFragment getSelectedFragment(Section section,
			int competitionId) {
		SectionFragment fragment = null;
		Bundle args = new Bundle();
		if (section.getViewType()
				.equalsIgnoreCase(RequestSectionTypes.WEB_VIEW)) {
			fragment = new LinkSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.CALENDAR)) {
			fragment = new CalendarSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.CARROUSEL)) {
			fragment = new CarrouselSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.CLASIFICATION)) {
			fragment = new ClasificationSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.PALMARES)) {
			fragment = new PalmaresSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.STADIUMS)) {
			fragment = new StadiumSectionFragment();
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
			fragment = new NewsSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.VIDEOS)) {
			fragment = new VideosSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.PHOTOS)) {
			fragment = new PhotosSectionFragment();
		} else if (section.getType().equalsIgnoreCase(SECTIONS.LINK)) {
			fragment = new LinkSectionFragment();
		}

		args.putSerializable("section", section);
		args.putString("competitionId", String.valueOf(competitionId));
		fragment.setArguments(args);
		return fragment;

	}

	public void setCurrentcompetition(int position) {

		// Set the current Competition
		RemoteDataDAO.getInstance(this.mContext).getGeneralSettings()
				.setCurrentCompetition(position);
		int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		// Refresh the sliding menu
		menuSections = RemoteDataDAO.getInstance(this.mContext)
				.getOrderedSections(currentCompetitionId);
		this.menuSectionsAdapter.addItems(menuSections);

		selectedFragment.setCurrentcompetition(position);
	}

	/****************************************** NAVEGATION *******************************************************/

	public void buttonClick(View view) {
		int tag = Integer.valueOf((String) view.getTag());
		switch (tag) {
		case MENU_BUTTON:
			toggle();
			break;
		default:
			break;
		}
	}

	private void openLink(String url) {
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			url = "http://" + url;
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	/****************************************** NAVEGATION *******************************************************/

	/***************************************************************************/
	/** Loading methods **/
	/***************************************************************************/
	@SuppressLint("NewApi")
	private void configureloadingAnimationDrawable() {
		AnimationDrawable animationDrawable = new AnimationDrawable();

		BitmapDrawable[] bitmapDrawable = new BitmapDrawable[drawables.length];

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inScaled = false;
		o.inDither = false; // Disable Dithering mode
		o.inPurgeable = true; // Tell to gc that whether it needs free memory,
								// the Bitmap can be cleared
		for (int i = 0; i < drawables.length; i++) {
			bitmapDrawable[i] = new BitmapDrawable(this.getApplicationContext()
					.getResources(), BitmapFactory.decodeResource(
					getResources(), drawables[i], o));
			animationDrawable.addFrame(bitmapDrawable[i], 50);
		}
		animationDrawable.setOneShot(false);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			spinner.setBackgroundDrawable(animationDrawable);
		} else {
			spinner.setBackground(animationDrawable);
		}
		// ((View) spinner.getParent()).setVisibility(View.VISIBLE);
		(findViewById(R.id.spinnerContent)).setVisibility(View.VISIBLE);
	}

	public void startAnimation() {
		configureloadingAnimationDrawable();
		spinner.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) spinner
						.getBackground();
				if (frameAnimation != null) {
					frameAnimation.start();
				}
			}
		});
	}

	@SuppressLint("NewApi")
	public void stopAnimation() {
		// ViewParent parent = spinner.getParent();
		View parent = (findViewById(R.id.spinnerContent));
		if (parent != null && parent.getVisibility() != View.INVISIBLE) {
			parent.setVisibility(View.INVISIBLE);

			if (spinner.getBackground() != null) {
				AnimationDrawable frameAnimation = (AnimationDrawable) spinner
						.getBackground();
				frameAnimation.stop();

				MemoryReleaseUtils.releaseAnimationDrawables(frameAnimation,
						true);
				int sdk = android.os.Build.VERSION.SDK_INT;
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					spinner.setBackgroundDrawable(null);
				} else {
					spinner.setBackground(null);
				}
			}
		}
	}

	/******************************* Sliding Menu ***********************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.slidingmenu.lib.SlidingMenu.OnCloseListener#onClose()
	 */
	@Override
	public void onClose() {
		findViewById(R.id.menuButton).setBackgroundResource(
				R.drawable.button_menu_on);
		if (sectionChanged) {
			Section selectedSection = (Section) sectionList
					.getItemAtPosition(selectedSectionIndex);

			int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
					.getGeneralSettings().getCurrentCompetition().getId();
			loadSectionFragment(selectedSection, currentCompetitionId);

		}
		selectedFragment.onCloseSlidingMenu();

	}

	@Override
	public void onClosed() {

		if (sectionChanged) {
			selectedFragment.closedSlidingMenu();
			sectionChanged = false;
		}

		Section selectedSection = (Section) sectionList
				.getItemAtPosition(selectedSectionIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.slidingmenu.lib.SlidingMenu.OnOpenListener#onOpen()
	 */
	@Override
	public void onOpen() {
		findViewById(R.id.menuButton).setBackgroundResource(
				R.drawable.button_menu_off);
		selectedFragment.onOpenSlidingMenu();
	}

	/******************************* Sliding Menu ***********************************************************/
	/***************************************************************************/
	/** Left section list adapter **/
	/***************************************************************************/

	class MenuSectionsAdapter extends BaseAdapter {

		private ArrayList<Section> items = new ArrayList<Section>();
		private final LayoutInflater inflater;

		public MenuSectionsAdapter() {
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addItems(ArrayList<Section> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Section getItem(int arg0) {
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Section item = items.get(position);
			ViewHolder holder;
			if (convertView == null) {

				convertView = inflater
						.inflate(R.layout.left_menu_section, null);

				holder = new ViewHolder();
				// Section icon
				holder.sectionIcon = (ImageView) convertView
						.findViewById(R.id.sectionIcon);
				holder.selectedSectionIcon = (ImageView) convertView
						.findViewById(R.id.selectedSectionIcon);
				// Section title
				holder.sectionName = (TextView) convertView
						.findViewById(R.id.sectionTitle);
				FontUtils.setCustomfont(mContext, holder.sectionName,
						FontTypes.HELVETICANEUELIGHT);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String name = "";
			int img = 0;
			Log.d("SECTIONS", "Seccion de tipo: " + item.getType());
			if (item.getType().equalsIgnoreCase(SECTIONS.CALENDAR)) {
				name = getString(R.string.menu_calendario);
				img = R.drawable.sliding_menu_icon_calendario;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.CARROUSEL)) {
				name = getString(R.string.menu_carrusel);
				img = R.drawable.icn_menu_carrusel;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.CLASIFICATION)) {
				name = getString(R.string.menu_clasificacion);
				img = R.drawable.icn_menu_clasificacion;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.PALMARES)) {
				name = getString(R.string.menu_palmares);
				img = R.drawable.sliding_menu_icon_palmares;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.STADIUMS)) {
				name = getString(R.string.menu_sedes);
				img = R.drawable.sliding_menu_icon_sedes;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.TEAMS)) {
				name = getString(R.string.menu_teams);
				img = R.drawable.icn_menu_equipos;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.COMPARATOR)) {
				name = getString(R.string.menu_comparador_jugadores);
				img = R.drawable.icn_menu_comparador;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.SEARCHER)) {
				name = getString(R.string.menu_search);
				img = R.drawable.icn_menu_buscador;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.NEWS)) {
				name = getString(R.string.menu_news);
				img = R.drawable.icn_menu_noticias;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.PHOTOS)) {
				name = getString(R.string.menu_photos);
				img = R.drawable.icn_menu_fotos;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.VIDEOS)) {
				name = getString(R.string.menu_videos);
				img = R.drawable.icn_menu_videos;
			} else if (item.getType().equalsIgnoreCase(
					SECTIONS.LINK_VIEW_OUTSIDE)) {
				name = item.getName();
				img = R.drawable.icn_menu_trivias;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.LINK)) {
				name = getString(R.string.menu_trivias);
				img = R.drawable.icn_menu_trivias;
			}

			holder.sectionName.setText(name);
			if (item.getType().equalsIgnoreCase(currentSelectedType)) {
				holder.selectedSectionIcon.setVisibility(View.VISIBLE);
			} else {
				holder.selectedSectionIcon.setVisibility(View.GONE);
			}
			holder.sectionIcon.setBackgroundResource(img);
			if (!item.isActive()) {
				AlphaAnimation alpha = new AlphaAnimation(1, 0.5F);
				alpha.setDuration(0); // Make animation instant
				alpha.setFillAfter(true);
				convertView.startAnimation(alpha);
				convertView.setClickable(true);
			} else {
				AlphaAnimation alpha = new AlphaAnimation(0.5F, 1);
				alpha.setDuration(0); // Make animation instant
				alpha.setFillAfter(true);
				convertView.startAnimation(alpha);
				convertView.setClickable(false);
			}

			return convertView;
		}
	}

	static class ViewHolder {

		public TextView sectionName;
		public ImageView selectedSectionIcon;
		public ImageView sectionIcon;

	}

}
