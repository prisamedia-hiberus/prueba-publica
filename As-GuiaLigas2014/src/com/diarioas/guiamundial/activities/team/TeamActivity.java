package com.diarioas.guiamundial.activities.team;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.GeneralFragmentActivity;
import com.diarioas.guiamundial.activities.player.PlayerActivity;
import com.diarioas.guiamundial.activities.team.fragment.DetailTeamFragment;
import com.diarioas.guiamundial.activities.team.fragment.HistoricalPalmaresTeamFragment;
import com.diarioas.guiamundial.activities.team.fragment.IdealPlayersTeamFragment;
import com.diarioas.guiamundial.activities.team.fragment.TeamFragment;
import com.diarioas.guiamundial.activities.team.fragment.TeamPlayersFragment;
import com.diarioas.guiamundial.dao.model.player.Player;
import com.diarioas.guiamundial.dao.model.team.Staff;
import com.diarioas.guiamundial.dao.model.team.Star;
import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.RemoteTeamDAO;
import com.diarioas.guiamundial.dao.reader.RemoteTeamDAO.RemoteTeamDAOListener;
import com.diarioas.guiamundial.dao.reader.StatisticsDAO;
import com.diarioas.guiamundial.utils.AlertManager;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.Defines.Demarcation;
import com.diarioas.guiamundial.utils.Defines.NativeAds;
import com.diarioas.guiamundial.utils.Defines.Omniture;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.Defines.StaffCharge;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.diarioas.guiamundial.utils.FragmentAdapter;
import com.diarioas.guiamundial.utils.StringUtils;
import com.diarioas.guiamundial.utils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;
import com.diarioas.guiamundial.utils.scroll.CustomHoizontalScroll;
import com.diarioas.guiamundial.utils.scroll.CustomHoizontalScroll.ScrollEndListener;

public class TeamActivity extends GeneralFragmentActivity implements
		RemoteTeamDAOListener, OnPageChangeListener, ScrollEndListener {

	private Team currentTeam;
	private ViewPager teamViewPager;
	private CustomHoizontalScroll headerSroll;
	private String competitionId;
	private ImageFetcher mImageFetcher;
	private ImageFetcher mImageFetcher2;
	private ImageFetcher mImageFetcher3;
	private ArrayList<String> headerNames;
	private List<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_team);
		configActionBar();

		spinner = (RelativeLayout) findViewById(R.id.spinner);
		configView();

		configureImageFetcher();

		String teamId = getIntent().getExtras().getString("teamId");
		competitionId = getIntent().getExtras().getString("competitionId");
		currentTeam = DatabaseDAO.getInstance(getApplicationContext()).getTeam(
				teamId);

		startAnimation();
		RemoteTeamDAO.getInstance(this).addListener(this);
		RemoteTeamDAO.getInstance(this).getTeamData(teamId, competitionId);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
		mImageFetcher2.setExitTasksEarly(false);
		mImageFetcher3.setExitTasksEarly(false);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ReturnRequestCodes.PUBLI_BACK) {
				if (currentTeam != null && currentTeam.getShortName() != null) {
					String section = NativeAds.AD_COUNTRY
							+ StringUtils.getNormalizeText(currentTeam
									.getShortName());
					callToAds(section, true);
					callToOmniture(teamViewPager.getCurrentItem());
				}
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mImageFetcher.setExitTasksEarly(false);
		mImageFetcher.flushCache();
		mImageFetcher2.setExitTasksEarly(false);
		mImageFetcher2.flushCache();
		mImageFetcher3.setExitTasksEarly(false);
		mImageFetcher3.flushCache();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		mImageFetcher.clearCache();
		mImageFetcher2.clearCache();
		mImageFetcher3.clearCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
		mImageFetcher2.closeCache();
		mImageFetcher3.closeCache();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return false;
			// case R.id.share:
			// shareTeam();
			// break;
		}
		return true;
	}

	/**
	 * 
	 */
	// private void shareTeam() {
	// if (currentTeam != null && currentTeam.getName() != null) {
	// Intent i = new Intent(Intent.ACTION_SEND);
	// i.setType("text/plain");
	//
	// i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
	// String body = getString(R.string.mens_share_part1_1)
	// + currentTeam.getName()
	// + getString(R.string.mens_share_part2)
	// // + getString(R.string.share_mens_url_long)
	// // + getString(R.string.share_mens_part2)
	// // + getString(R.string.share_mens_url_short)
	// // + getString(R.string.share_mens_part4)
	// ;
	//
	// i.putExtra(Intent.EXTRA_TEXT, body);
	// // i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
	// startActivity(Intent.createChooser(i,
	// getString(R.string.share_mens_title)
	// + getString(R.string.app_name)));
	//
	// }
	//
	// }

	private void configView() {
		// TODO Auto-generated method stub

	}

	private void configureImageFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(this,
				Defines.NAME_CACHE_THUMBS);
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.image_thumb_height));
		mImageFetcher.setLoadingImage(R.drawable.galeria_imagenrecurso);
		mImageFetcher.addImageCache(this.getSupportFragmentManager(),
				cacheParams);

		ImageCacheParams cacheParams2 = new ImageCacheParams(this,
				Defines.NAME_CACHE_THUMBS + "2");
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher2 = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.image_player_height));
		mImageFetcher2.setLoadingImage(R.drawable.foto_generica);
		mImageFetcher2.addImageCache(this.getSupportFragmentManager(),
				cacheParams2);

		ImageCacheParams cacheParams3 = new ImageCacheParams(this,
				Defines.NAME_CACHE_THUMBS + "3");
		cacheParams.setMemCacheSizePercent(0.25f);
		mImageFetcher3 = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.image_player_height_small));
		mImageFetcher3.setLoadingImage(R.drawable.foto_plantilla_generica);
		mImageFetcher3.addImageCache(this.getSupportFragmentManager(),
				cacheParams3);

	}

	/**
	 * @return the mImageFetcher
	 */
	public ImageFetcher getmImageFetcher() {
		return mImageFetcher;
	}

	/**
	 * @return the mImageFetcher
	 */
	public ImageFetcher getmImageFetcher2() {
		return mImageFetcher2;
	}

	/**
	 * @return the mImageFetcher
	 */
	public ImageFetcher getmImageFetcher3() {
		return mImageFetcher3;
	}

	private void loadInformation(Team team) {
		currentTeam = team;
		configViewPager();
		if (currentTeam.getShortName() != null) {
			String section = NativeAds.AD_COUNTRY
					+ StringUtils.getNormalizeText(currentTeam.getShortName());
			callToAds(section, true);
		}

	}

	private void configViewPager() {
		teamViewPager = (ViewPager) findViewById(R.id.teamViewPager);

		fragments = getFragments();

		teamViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		teamViewPager.setCurrentItem(0, true);
		callToOmniture(0);
		teamViewPager.setOnPageChangeListener(this);
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		headerNames = new ArrayList<String>();

		Bundle args;
		Fragment fragment;

		// Resumen
		fragment = new DetailTeamFragment();
		fList.add(fragment);
		args = new Bundle();
		args.putString("teamId", currentTeam.getId());
		args.putString("teamName", currentTeam.getShortName());
		args.putString("competitionId", competitionId);
		args.putString("urlShield", currentTeam.getDetailShield());
		if (currentTeam.getCountShirts() > 0)
			args.putString("shirt1", currentTeam.getShirts().get(0));
		if (currentTeam.getCountShirts() > 1)
			args.putString("shirt2", currentTeam.getShirts().get(1));
		if (currentTeam.getCountShirts() > 2)
			args.putString("shirt3", currentTeam.getShirts().get(2));

		if (currentTeam.getArticle() != null) {
			args.putString("body", currentTeam.getArticle().getBody());
			args.putString("videoUrl", currentTeam.getArticle().getUrlVideo());
			args.putString("videoImageUrl", currentTeam.getArticle()
					.getUrlVideoImage());
		}
		Staff mister = currentTeam.getStaff(StaffCharge.MISTER);
		if (mister != null) {
			args.putString("misterImage", mister.getPhoto());
			args.putString("misterName", mister.getName());
			args.putString("misterHistory", mister.getHistory());
		}

		Star star = (Star) currentTeam.getStaff(StaffCharge.STAR);
		if (star != null) {
			args.putString("starImage", star.getPhoto());
			args.putString("starName", star.getName());
			args.putString("starAge", star.getAge());
			args.putString("starWeight", star.getWeight());
			args.putString("starStature", star.getStature());
			args.putString("starPosition", star.getPosition());
			args.putString("starNumInternational", star.getNumInternational());
			args.putString("starClubName", star.getClubName());
			args.putString("starClubShield", star.getClubShield());
			args.putString("starHistory", star.getHistory());
			args.putString("starId", star.getPlayerId());
			args.putString("starUrl", star.getUrl());
		}

		args.putString("teamFederationName", currentTeam.getName());
		args.putString("teamFederationFoundation",
				currentTeam.getFederationFoundation());
		args.putString("teamFederationAffiliation",
				currentTeam.getFederationAffiliation());
		args.putString("teamFederationWeb", currentTeam.getWeb());
		args.putString("numJug", currentTeam.getNumPlayers());
		args.putString("numClub", currentTeam.getNumClubs());
		args.putString("numArb", currentTeam.getNumReferees());
		args.putString("subsection",
				getString(R.string.team_fragmentname_resumen));
		fragment.setArguments(args);
		headerNames.add(getString(R.string.team_fragmentname_resumen));

		if ((currentTeam.getPlantilla() != null && currentTeam.getPlantilla()
				.size() > 0)
				|| (currentTeam.getIdealPlayers() != null && currentTeam
						.getIdealPlayers().size() > 0)) {
			Bundle bundlePlant = new Bundle();
			Bundle bundlePlayer;
			fragment = new TeamPlayersFragment();
			args = new Bundle();
			args.putString("teamName", currentTeam.getShortName());
			args.putString("shield", currentTeam.getCalendarShield());

			// Plantilla
			if (currentTeam.getPlantilla() != null
					&& currentTeam.getPlantilla().size() > 0) {

				String dorsal;
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumIntegerDigits(3);
				for (Player player : currentTeam.getPlantilla()) {
					dorsal = String.valueOf(player.getDorsal());
					if (dorsal != null && dorsal.length() > 0) {
						bundlePlayer = new Bundle();
						bundlePlayer.putString("id",
								String.valueOf(player.getId()));
						bundlePlayer.putString("shortName",
								player.getShortName());
						bundlePlayer.putString("dorsal", dorsal);
						bundlePlayer.putString("photo", player.getUrlFoto());
						bundlePlayer.putString("position",
								String.valueOf(player.getPosition()));
						if (player.getUrl() != null
								&& !player.getUrl().equalsIgnoreCase(""))
							bundlePlayer.putString("url", player.getUrl());
						bundlePlayer.putString(Demarcation.DEMARCATION,
								player.getDemarcation());

						if (player.getDorsal() != 0)
							bundlePlant
									.putBundle(nf.format(player.getDorsal()),
											bundlePlayer);
						else {
							bundlePlant.putBundle("000", bundlePlayer);
						}
						// bundlePlant.putBundle(player.getShortName(),bundlePlayer);
					}
				}

				if (bundlePlant.size() > 0) {
					args.putBundle("plantilla", bundlePlant);
					fragment.setArguments(args);
					fList.add(fragment);
					headerNames
							.add(getString(R.string.team_fragmentname_plantilla));
				}

			}

			// Once Ideal
			if (currentTeam.getIdealPlayers() != null
					&& currentTeam.getIdealPlayers().size() > 0) {

				// for (PlayerOnField player : currentTeam.getIdealPlayers()) {
				// if (!bundlePlant.containsKey(player.getName())) {
				// bundlePlayer = new Bundle();
				// bundlePlayer.putString("id",
				// String.valueOf(player.getId()));
				// bundlePlayer.putString("shortName", player.getName());
				// if (player.getDorsal() > 0)
				// bundlePlayer.putString("dorsal",
				// String.valueOf(player.getDorsal()));
				// bundlePlayer.putString("photo", player.getUrlPhoto());
				// bundlePlayer.putString("position",
				// String.valueOf(player.getPosition()));
				// if (player.getUrl() != null
				// && !player.getUrl().equalsIgnoreCase(""))
				// bundlePlayer.putString("url", player.getUrl());
				//
				// bundlePlant.putBundle(player.getName(), bundlePlayer);
				// }
				// }

				// if (bundlePlant.size() > 0) {
				// args.putBundle("plantilla", bundlePlant);
				// fragment.setArguments(args);
				// fList.add(fragment);
				// headerNames
				// .add(getString(R.string.team_fragmentname_plantilla));
				// }

				fragment = new IdealPlayersTeamFragment();
				fList.add(fragment);
				args = new Bundle();
				args.putString("teamName", currentTeam.getShortName());
				args.putString("gameSystem", currentTeam.getGameSystem());
				args.putParcelableArrayList("idealPlayers",
						currentTeam.getIdealPlayers());
				fragment.setArguments(args);
				headerNames.add(getString(R.string.team_fragmentname_elonce));

			}
		}
		// Palmares
		if (currentTeam.getHistoricalPalmares() != null
				&& currentTeam.getHistoricalPalmares().size() > 0) {
			fragment = new HistoricalPalmaresTeamFragment();
			args = new Bundle();
			args.putString("teamId", currentTeam.getId());
			args.putString("competitionId", competitionId);
			fragment.setArguments(args);
			fList.add(fragment);
			headerNames.add(getString(R.string.team_fragmentname_palmares));
		}

		headerSroll = (CustomHoizontalScroll) findViewById(R.id.headerSroll);
		Point size = DimenUtils.getSize(getWindowManager());
		headerSroll.setScreenWidth(size.x);
		headerSroll.setFont(FontTypes.HELVETICANEUE);
		headerSroll.setMainColor(getResources().getColor(R.color.red_sedes));
		headerSroll.setSecondColor(getResources().getColor(R.color.gray_sedes));
		headerSroll.addScrollEndListener(this);
		headerSroll.setInitPosition(0);
		headerSroll.addViews(headerNames);

		return fList;
	}

	public void playerClicked(View view) {
		int tagged;
		// try {
		String tag = (String) view.getTag();
		tagged = Integer.valueOf(tag);
		// } catch (Exception e) {
		// tagged = (Integer) view.getTag();
		// }
		if (tagged > 0) {

			String url = DatabaseDAO.getInstance(getApplicationContext())
					.getPlayer(tagged).getUrl();
			if (url != null && url.length() > 1) {
				Intent intent = new Intent(this, PlayerActivity.class);
				intent.putExtra("playerId", tagged);
				if (currentTeam.getName() != null)
					intent.putExtra("teamName", currentTeam.getName());
				else
					intent.putExtra("teamName", currentTeam.getShortName());
				startActivity(intent);
				overridePendingTransition(R.anim.grow_from_middle,
						R.anim.shrink_to_middle);
			} else {
				// showNoPlayerAlert();
			}
		} else {
			// showNoPlayerAlert();
		}
	}

	// private void showNoPlayerAlert() {
	// AlertManager.showAlertOkDialog(this,
	// getResources()
	// .getString(R.string.player_comparator_not_content),
	// getResources().getString(R.string.connection_atention_title),
	//
	// new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// }

	private void callToOmniture(final int pos) {

		String subsection = headerNames.get(pos);
		if (subsection.startsWith("la") || subsection.startsWith("La")
				|| subsection.startsWith("El") || subsection.startsWith("el"))
			subsection = subsection.substring(3, subsection.length());
		if (subsection.startsWith("en los") || subsection.startsWith("En los"))
			subsection = subsection.substring(7, subsection.length());

		subsection = StringUtils
				.getNormalizeText(subsection, true, false, true);

		String section = "";
		if (currentTeam.getShortName() != null)
			section = StringUtils.getNormalizeText(currentTeam.getShortName());

		StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsState(
				getApplication(),
				section,
				subsection,
				null,
				null,
				Omniture.TYPE_PORTADA,
				Omniture.DETAILPAGE_INFORMACION + " " + section + " "
						+ subsection, null);

	}

	/**************** RemoteTeamDAO Methods *****************************/
	@Override
	public void onSuccessTeamRemoteconfig(Team team) {
		RemoteTeamDAO.getInstance(getApplicationContext()).removeListener(this);
		loadInformation(team);
		stopAnimation();
	}

	@Override
	public void onFailureTeamRemoteconfig(Team team) {
		RemoteTeamDAO.getInstance(getApplicationContext()).removeListener(this);
		if (team != null) {
			stopAnimation();
			AlertManager.showAlertOkDialog(this,
					getResources().getString(R.string.team_detail_error),
					getResources().getString(R.string.connection_error_title),

					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			loadInformation(team);
		} else {
			((TextView) findViewById(R.id.errorMessage))
					.setText(getString(R.string.team_detail_error));
			findViewById(R.id.errorContent).setVisibility(View.VISIBLE);
			stopAnimation();
		}
	}

	@Override
	public void onFailureNotTeamConnection(Team team) {
		RemoteTeamDAO.getInstance(getApplicationContext()).removeListener(this);
		if (team != null) {
			stopAnimation();
			AlertManager.showAlertOkDialog(this,
					getResources().getString(R.string.team_detail_not_updated),
					getResources().getString(R.string.connection_error_title),

					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			loadInformation(team);
		} else {
			((TextView) findViewById(R.id.errorMessage))
					.setText(getString(R.string.error_team_message));
			findViewById(R.id.errorContent).setVisibility(View.VISIBLE);
			stopAnimation();
		}

	}

	/**************** RemoteTeamDAO Methods *****************************/
	/**************** ViewPager Methods *****************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(final int pos) {
		Log.d("SCROLL", "onPageSelected");
		// ViewTreeObserver vto = teamViewPager.getViewTreeObserver();
		// vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		// @SuppressLint("NewApi")
		// @Override
		// public void onGlobalLayout() {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		// teamViewPager.getViewTreeObserver()
		// .removeOnGlobalLayoutListener(this);
		// } else {
		// teamViewPager.getViewTreeObserver()
		// .removeGlobalOnLayoutListener(this);
		// }
		// headerSroll.setHeaderPosition(pos);
		// }
		// });
		headerSroll.setHeaderPosition(pos);
		callToOmniture(pos);

		((TeamFragment) fragments.get(pos)).onShown();

	}

	/**************** ViewPager Methods *****************************/
	/**************** HorizontalCustomScroll Methods *****************************/
	@Override
	public void onScrollEnd(int x, int y, int oldx, int oldy, int pos) {
		teamViewPager.setCurrentItem(pos, true);

	}

	@Override
	public void onItemClicked(int pos) {
		teamViewPager.setCurrentItem(pos, true);
	}
}
