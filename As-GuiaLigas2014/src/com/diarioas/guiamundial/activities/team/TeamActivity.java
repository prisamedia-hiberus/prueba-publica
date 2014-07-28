package com.diarioas.guiamundial.activities.team;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.GeneralFragmentActivity;
import com.diarioas.guiamundial.activities.player.PlayerActivity;
import com.diarioas.guiamundial.activities.team.fragment.TeamInfoFragment;
import com.diarioas.guiamundial.activities.team.fragment.TeamPlayersFragment;
import com.diarioas.guiamundial.activities.team.fragment.TeamStatsFragment;
import com.diarioas.guiamundial.dao.model.player.Player;
import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.dao.model.team.TituloTeam;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.RemoteTeamDAO;
import com.diarioas.guiamundial.dao.reader.RemoteTeamDAO.RemoteTeamDAOListener;
import com.diarioas.guiamundial.utils.AlertManager;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.Defines.NativeAds;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FragmentAdapter;
import com.diarioas.guiamundial.utils.StringUtils;
import com.diarioas.guiamundial.utils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;
import com.diarioas.guiamundial.utils.scroll.CustomHoizontalScroll.ScrollEndListener;



public class TeamActivity extends GeneralFragmentActivity implements
		RemoteTeamDAOListener, OnPageChangeListener, ScrollEndListener {

	private static final int BUTTON_STATS = 0;
	private static final int BUTTON_INFO = 1;
	private static final int BUTTON_PLAYERS = 2;
	private static final int BUTTON_WEB = 101;
	private static final int BUTTON_ESTADIO = 102;
	private static final int BUTTON_NOTICIAS = 103;
	private static final int BUTTON_PREV = 201;
	private static final int BUTTON_NEXT = 202;
	
	private Button statsButton;
	private Button dataButton;
	private Button playersButton;
	
	private int currentPos = -1;
	
	private Team currentTeam;
	private ViewPager teamViewPager;
	private String competitionId;
	private ImageFetcher mImageFetcher;
	private ImageFetcher mImageFetcher2;
	private ImageFetcher mImageFetcher3;
	private List<Fragment> fragments;
	private TeamStatsFragment statsFragment;
	private Fragment infoFragment;
	private Fragment playerFragment;

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
		statsButton = ((Button) findViewById(R.id.statsButton));
		FontUtils.setCustomfont(getApplicationContext(), statsButton,
				FontUtils.FontTypes.ROBOTO_BLACK);
		dataButton = ((Button) findViewById(R.id.dataButton));
		FontUtils.setCustomfont(getApplicationContext(), dataButton,
				FontUtils.FontTypes.ROBOTO_BLACK);
		playersButton = ((Button) findViewById(R.id.playersButton));
		FontUtils.setCustomfont(getApplicationContext(), playersButton,
				FontUtils.FontTypes.ROBOTO_BLACK);
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
		
		if (statsFragment != null) {
			currentPos = 1;
		} else {
			currentPos = 0;
		}
		teamViewPager.setCurrentItem(currentPos, true);
		
//		callToOmniture(1);
		teamViewPager.setOnPageChangeListener(this);
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		Bundle args;

		// Fragment Stats
		if (currentTeam.getStats().size() != 0
				|| currentTeam.getPalmares().size() != 0) {
			statsFragment = (TeamStatsFragment) Fragment.instantiate(this,
					TeamStatsFragment.class.getName());
			args = new Bundle();
			if (currentTeam.getName() != null)
				args.putString("name", currentTeam.getName());
			else
				args.putString("name", currentTeam.getShortName());
			args.putString("shield", currentTeam.getCalendarShield());
			Bundle bundlePalm = new Bundle();
			for (TituloTeam palm : currentTeam.getPalmares()) {
				bundlePalm.putParcelable(palm.getName(), palm);
			}
			args.putBundle("palmares", bundlePalm);
			Bundle bundle = new Bundle();
			String year;
			for (Iterator<String> iterator = currentTeam.getStats().keySet()
					.iterator(); iterator.hasNext();) {
				year = iterator.next();
				bundle.putParcelable(year, currentTeam.getStats().get(year));
			}
			args.putBundle("stats", bundle);
			statsFragment.setArguments(args);
			fList.add(statsFragment);
		}
		// Fragment Info
		infoFragment = Fragment.instantiate(this,
				TeamInfoFragment.class.getName());
		args = new Bundle();
		if (currentTeam.getName() != null)
			args.putString("name", currentTeam.getName());
		else
			args.putString("name", currentTeam.getShortName());
		args.putString("fundation", currentTeam.getfundation());
		args.putString("web", currentTeam.getWeb());
		args.putString("shield", currentTeam.getDetailShield());
		if (currentTeam.getEstadio() != null) {
			if (currentTeam.getEstadio().getName() != null) {
				NumberFormat formatter = new DecimalFormat("###,###");

				args.putString(
						"nameEstadio",
						currentTeam.getEstadio().getName()
								+ " (aforo "
								+ formatter.format(currentTeam.getEstadio()
										.getCapacity()) + ")");
			} else
				args.putString("nameEstadio", null);

			String address = "";
			if (currentTeam.getEstadio().getAddress() != null)
				address = currentTeam.getEstadio().getAddress();
			if (currentTeam.getEstadio().getAddress() != null)
				address += " " + currentTeam.getEstadio().getCity();

			args.putString("addressEstadio", address);
		}

		if (currentTeam.getCountShirts() > 0)
			args.putString("shirt1", currentTeam.getShirts().get(0));
		if (currentTeam.getCountShirts() > 1)
			args.putString("shirt2", currentTeam.getShirts().get(1));
		if (currentTeam.getCountShirts() > 2)
			args.putString("shirt3", currentTeam.getShirts().get(2));
		args.putString("history", currentTeam.getHistory());
		args.putParcelable("president", currentTeam.getPresident());
		args.putParcelable("manager", currentTeam.getManager());
		args.putParcelable("mister", currentTeam.getMister());
		if (currentTeam.getArticle() != null) {
			args.putString("author", currentTeam.getArticle().getAuthor());
			args.putString("charge", currentTeam.getArticle().getCharge());
			args.putString("title", currentTeam.getArticle().getTitle());
			args.putString("subtitle", currentTeam.getArticle().getSubTitle());
			args.putString("article", currentTeam.getArticle().getBody());
		}
		infoFragment.setArguments(args);
		fList.add(infoFragment);

		// if (currentTeam.getPlantilla().size() != 0) {
		playerFragment = Fragment.instantiate(this,
				TeamPlayersFragment.class.getName());
		// Fragment Players
		args = new Bundle();
		if (currentTeam.getName() != null)
			args.putString("name", currentTeam.getName());
		else
			args.putString("name", currentTeam.getShortName());
		args.putString("shield", currentTeam.getCalendarShield());
		Bundle bundlePlant = new Bundle();
		Bundle bundlePlayer;
		String dorsal;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		for (Player player : currentTeam.getPlantilla()) {
			bundlePlayer = new Bundle();
			dorsal = String.valueOf(player.getDorsal());
			bundlePlayer.putString("id", String.valueOf(player.getId()));
			bundlePlayer.putString("shortName", player.getShortName());
			bundlePlayer.putString("dorsal", dorsal);
			bundlePlayer.putString("photo", player.getUrlFoto());
			if (player.getUrl() != null
					&& !player.getUrl().equalsIgnoreCase(""))
				bundlePlayer.putString("url", player.getUrl());
			bundlePlayer.putString("demarcacion", player.getDemarcation());

			bundlePlant.putBundle(nf.format(player.getDorsal()), bundlePlayer);
		}
		args.putBundle("plantilla", bundlePlant);
		playerFragment.setArguments(args);
		fList.add(playerFragment);
		// }

		return fList;

	}

	
	private void goToWeb(String url) {
		if (url != null && !url.equalsIgnoreCase("")) {
			if (url.contains("http") == false) {
				url = "http://" + url;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			TeamActivity.this.startActivity(intent);
		}

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

//		String subsection = headerNames.get(pos);
//		if (subsection.startsWith("la") || subsection.startsWith("La")
//				|| subsection.startsWith("El") || subsection.startsWith("el"))
//			subsection = subsection.substring(3, subsection.length());
//		if (subsection.startsWith("en los") || subsection.startsWith("En los"))
//			subsection = subsection.substring(7, subsection.length());
//
//		subsection = StringUtils
//				.getNormalizeText(subsection, true, false, true);
//
//		String section = "";
//		if (currentTeam.getShortName() != null)
//			section = StringUtils.getNormalizeText(currentTeam.getShortName());
//
//		StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsState(
//				getApplication(),
//				section,
//				subsection,
//				null,
//				null,
//				Omniture.TYPE_PORTADA,
//				Omniture.DETAILPAGE_INFORMACION + " " + section + " "
//						+ subsection, null);

	}

	
	public void buttonClick(View v) {
		int tag = Integer.valueOf((String) v.getTag());

		switch (tag) {
		case BUTTON_STATS:
		case BUTTON_INFO:
		case BUTTON_PLAYERS:
			changeView(tag);
			break;
		case BUTTON_WEB:
			if (currentTeam.getWeb() != null
					&& !currentTeam.getWeb().equalsIgnoreCase("")) {
//				StatisticsDAO.getInstance(this).sendStaticsOnAction(
//						currentTeam.getShortName().toLowerCase(),
//						Defines.Ommniture.SUBSECTION_TEAM_NEWS, null, null,
//						Defines.Ommniture.HOMEPAGE_CONTENT_TYPE,
//						currentTeam.getShortName().toLowerCase(), null);
				goToWeb(currentTeam.getWeb());
			}
			break;
		case BUTTON_ESTADIO:

			if (currentTeam.getEstadio().getLon() != 0
					&& currentTeam.getEstadio().getLat() != 0) {
//				StatisticsDAO.getInstance(this).sendStaticsOnAction(
//						currentTeam.getShortName().toLowerCase(),
//						Defines.Ommniture.SUBSECTION_TEAM_MAP, null, null,
//						Defines.Ommniture.HOMEPAGE_CONTENT_TYPE,
//						currentTeam.getShortName().toLowerCase(), null);

				// String uri = String.format(Locale.ENGLISH,
				// "geo:%f,%f?z=%d&q=%f,%f (%s)", currentTeam.getEstadio()
				// .getLat(), currentTeam.getEstadio().getLon(), 20,
				// currentTeam.getEstadio().getLat(), currentTeam.getEstadio()
				// .getLon(), currentTeam.getEstadio().getName());

				String uri = String.format(Locale.ENGLISH,
						"geo:%f,%f?z=%d&q=%f,%f ", currentTeam.getEstadio()
								.getLat(), currentTeam.getEstadio().getLon(),
						20, currentTeam.getEstadio().getLat(), currentTeam
								.getEstadio().getLon());

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				this.startActivity(intent);
			}
			break;
		case BUTTON_NOTICIAS:
			if (currentTeam.getUrlTag() != null
					&& !currentTeam.getUrlTag().equalsIgnoreCase("")) {
				goToWeb(currentTeam.getUrlTag());
			}
			break;
		case BUTTON_PREV:
			statsFragment.setPrevCompetition();
			break;
		case BUTTON_NEXT:
			statsFragment.setNextCompetition();
			break;

		default:
			break;
		}

	}
	
	private void changeView(int pos) {
		if (currentPos != pos) {
			currentPos = pos;
			if (teamViewPager != null) {
				teamViewPager.setCurrentItem(pos);
			}

			switch (pos) {
			case BUTTON_STATS:
				statsButton.setTextColor(getResources().getColor(R.color.red));
				statsButton
						.setBackgroundResource(R.drawable.textura_footer_team_selected);
				dataButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				dataButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				playersButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				playersButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				break;
			case BUTTON_INFO:
				statsButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				statsButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				dataButton.setTextColor(getResources().getColor(R.color.red));
				dataButton
						.setBackgroundResource(R.drawable.textura_footer_team_selected);
				playersButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				playersButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				break;
			case BUTTON_PLAYERS:
				statsButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				statsButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				dataButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				dataButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				playersButton
						.setTextColor(getResources().getColor(R.color.red));
				playersButton
						.setBackgroundResource(R.drawable.textura_footer_team_selected);
				break;

			default:
				break;
			}
		}

	}
	/**************** RemoteTeamDAO Methods *****************************/
	@Override
	public void onSuccessTeamRemoteconfig(Team team) {
		RemoteTeamDAO.getInstance(getApplicationContext()).removeListener(this);
		findViewById(R.id.buttonBar).setVisibility(View.VISIBLE);
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
//		headerSroll.setHeaderPosition(pos);
//		callToOmniture(pos);

		// ((TeamFragment) fragments.get(pos)).onShown();

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
