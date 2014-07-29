/**
 * 
 */
package com.diarioas.guiamundial.activities.player.comparator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.GeneralFragmentActivity;
import com.diarioas.guiamundial.activities.player.comparator.fragment.ComparatorPlayerFragmentStepFirst;
import com.diarioas.guiamundial.activities.player.comparator.fragment.ComparatorPlayerFragmentStepFour;
import com.diarioas.guiamundial.activities.player.comparator.fragment.ComparatorPlayerFragmentStepZero;
import com.diarioas.guiamundial.dao.model.player.Player;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.RemotePlayerDAO;
import com.diarioas.guiamundial.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener;
import com.diarioas.guiamundial.dao.reader.StatisticsDAO;
import com.diarioas.guiamundial.utils.AlertManager;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.Defines.Omniture;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.FragmentAdapter;
import com.diarioas.guiamundial.utils.bitmapfun.ImageCache.ImageCacheParams;
import com.diarioas.guiamundial.utils.bitmapfun.ImageFetcher;
import com.diarioas.guiamundial.utils.viewpager.CustomViewPagerLeague;

/**
 * @author robertosanchez
 * 
 */
public class PlayerComparatorStepFirstActivity extends GeneralFragmentActivity
		implements RemotePlayerDAOListener {

	private static final int PREV_BUTTON_YEAR_PL = 151;
	private static final int POST_BUTTON_YEAR_PL = 152;

	private static final int PREV_BUTTON_YEAR_PR = 251;
	private static final int POST_BUTTON_YEAR_PR = 252;

	private static final int PREV_BUTTON_COMPETITION_PL = 153;
	private static final int POST_BUTTON_COMPETITION_PL = 154;

	private static final int PREV_BUTTON_COMPETITION_PR = 253;
	private static final int POST_BUTTON_COMPETITION_PR = 254;

	private CustomViewPagerLeague playerComparatorViewPager;

	private String teamPLName;
	private Player currentPlayerLeft;
	private Player currentPlayerRight;
	/********************** Search RightPlayer ***************************/

	private int currentPlayerLeftId;
	private Fragment playerCompFragment;
	private String teamPRName;
	private int currentPlayerRightId;
	private ImageFetcher mImageFetcher;

	// Third Step

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_player_comparator);
		spinner = (RelativeLayout) findViewById(R.id.spinner);

		playerComparatorViewPager = (CustomViewPagerLeague) findViewById(R.id.leagueViewPager);

		if (getIntent().getExtras().getInt("playerId") != 0) {
			teamPLName = getIntent().getExtras().getString("teamName");
			currentPlayerLeftId = getIntent().getExtras().getInt("playerId");
			currentPlayerLeft = DatabaseDAO
					.getInstance(getApplicationContext()).getPlayer(
							currentPlayerLeftId);
		}
		configureImageFetcher();
		configViewPager();
		configActionBar();

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
		mImageFetcher.setExitTasksEarly(false);
		StatisticsDAO.getInstance(this)
				.sendStatisticsState(
						getApplication(),
						Omniture.SECTION_COMPARATOR,
						Omniture.SUBSECTION_PORTADA,
						null,
						null,
						Omniture.TYPE_PORTADA,
						Omniture.DETAILPAGE_DETALLE + " "
								+ Omniture.SECTION_COMPARATOR, null);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mImageFetcher.setExitTasksEarly(false);
		mImageFetcher.flushCache();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		mImageFetcher.clearCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
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
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_left);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return false;
		case R.id.share:
			sharePlayer();
			break;
		}
		return true;
	}

	private void sharePlayer() {
		if (currentPlayerLeft != null && currentPlayerLeft.getName() != null) {
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");

			String body = getString(R.string.mens_share_part1_2)
					+ currentPlayerLeft.getName()
					+ getString(R.string.mens_share_part2)
			// + getString(R.string.share_mens_url_long)
			;

			intent.putExtra(Intent.EXTRA_TEXT, body);
			startActivity(Intent.createChooser(intent,
					getString(R.string.share_mens_title)
							+ getString(R.string.app_name)));
		}

	}

	@Override
	protected void configActionBar() {

		getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		if (teamPLName != null) {
			getSupportActionBar().setDisplayShowHomeEnabled(false);
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setTitle(teamPLName);
		} else {
			getSupportActionBar().setIcon(R.drawable.home_icn_logoheader);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}

	}

	/**
	 * @param result
	 * 
	 */
	public void selectCompetition(int result) {
		Intent intent = new Intent(PlayerComparatorStepFirstActivity.this,
				PlayerComparatorStepSecondActivity.class);
		PlayerComparatorStepFirstActivity.this.startActivityForResult(intent,
				result);
		overridePendingTransition(R.anim.slide_in_up, R.anim.null_anim);

	}

	public void buttonClick(View view) {
		int tag = Integer.valueOf((String) view.getTag());
		Log.d("COMPARATOR", "ButtonClicked: " + tag);
		switch (tag) {
		case 501:
			selectCompetition(ReturnRequestCodes.COMPARATORPLAYER_LEFT);
			break;
		case 502:
			selectCompetition(ReturnRequestCodes.COMPARATORPLAYER_RIGHT);
			break;
		case PREV_BUTTON_YEAR_PL:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setYearFromParentPL(-1);
			break;
		case POST_BUTTON_YEAR_PL:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setYearFromParentPL(1);
			break;
		case PREV_BUTTON_COMPETITION_PL:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setCompetitionFromParentPL(-1);
			break;
		case POST_BUTTON_COMPETITION_PL:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setCompetitionFromParentPL(1);
			break;
		case PREV_BUTTON_YEAR_PR:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setYearFromParentPR(-1);
			break;
		case POST_BUTTON_YEAR_PR:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setYearFromParentPR(1);
			break;
		case PREV_BUTTON_COMPETITION_PR:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setCompetitionFromParentPR(-1);
			break;
		case POST_BUTTON_COMPETITION_PR:
			((ComparatorPlayerFragmentStepFour) playerCompFragment)
					.setCompetitionFromParentPR(1);
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
			if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_RIGHT) {
				StatisticsDAO.getInstance(this).sendStatisticsState(
						getApplication(),
						Omniture.SECTION_COMPARATOR,
						Omniture.SUBSECTION_RESULT,
						null,
						null,
						Omniture.TYPE_PORTADA,
						Omniture.SUBSECTION_RESULT + " "
								+ Omniture.SECTION_COMPARATOR, null);
				if (data != null && data.getExtras() != null
						&& data.getExtras().containsKey("playerId")
						&& data.getExtras().containsKey("teamName")) {
					teamPRName = (String) data.getExtras().get("teamName");
					int playerId = (Integer) data.getExtras().get("playerId");
					if (currentPlayerLeftId == 0) {
						currentPlayerLeftId = playerId;
					} else {
						currentPlayerRightId = playerId;
					}
					playerComparatorViewPager.removeAllViews();
					startAnimation();
					RemotePlayerDAO.getInstance(this).addListener(this);
					RemotePlayerDAO.getInstance(this)
							.refreshDatabaseWithNewResults(playerId);
				}
			} else if (requestCode == ReturnRequestCodes.COMPARATORPLAYER_LEFT) {
				if (data != null && data.getExtras() != null
						&& data.getExtras().containsKey("playerId")
						&& data.getExtras().containsKey("teamName")) {

					int playerId = (Integer) data.getExtras().get("playerId");
					if (getSupportActionBar().getTitle().toString()
							.equalsIgnoreCase(teamPLName)) {
						currentPlayerRightId = playerId;
					} else {
						currentPlayerLeftId = playerId;
						teamPLName = (String) data.getExtras().get("teamName");
					}

					playerComparatorViewPager.removeAllViews();
					startAnimation();
					RemotePlayerDAO.getInstance(this).addListener(this);
					RemotePlayerDAO.getInstance(this)
							.refreshDatabaseWithNewResults(playerId);
				}
			}
		}
	}

	private void configViewPager() {

		List<Fragment> fragments = getFragments();

		playerComparatorViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		playerComparatorViewPager.setCurrentItem(0, true);
	}

	/**
	 * @return
	 */
	private List<Fragment> getFragments() {
		ArrayList<Fragment> fList = new ArrayList<Fragment>();
		if (currentPlayerLeft != null && currentPlayerRight != null) {
			playerCompFragment = Fragment.instantiate(this,
					ComparatorPlayerFragmentStepFour.class.getName());
			// Fragment Players
			Bundle args = new Bundle();
			args.putString("teamNamePL", teamPLName);
			if (currentPlayerLeft.getName() != null) {
				args.putString("namePL", currentPlayerLeft.getName());
			} else {
				args.putString("namePL", currentPlayerLeft.getShortName());
			}
			args.putString("fotoPL", currentPlayerLeft.getUrlFoto());
			args.putString("datePL", currentPlayerLeft.getDateBorn());
			args.putString("paisPL", currentPlayerLeft.getNacionality());
			args.putInt("edadPL", currentPlayerLeft.getAge());
			args.putDouble("alturaPL", currentPlayerLeft.getHeight());
			args.putDouble("pesoPL", currentPlayerLeft.getWeight());
			args.putString("positionPL", currentPlayerLeft.getDemarcation());
			args.putInt("dorsalPL", currentPlayerLeft.getDorsal());

			Bundle bundleStatsPL = new Bundle(currentPlayerLeft.getStats()
					.size());
			String year;
			for (Iterator<String> iterator = currentPlayerLeft.getStats()
					.keySet().iterator(); iterator.hasNext();) {
				year = iterator.next();
				bundleStatsPL.putParcelable(year,
						currentPlayerLeft.getStats(year));
			}
			args.putBundle("statsPL", bundleStatsPL);
			args.putParcelableArrayList("palmaresPL",
					currentPlayerLeft.getPalmares());

			args.putString("teamNamePR", teamPRName);
			if (currentPlayerRight.getName() != null) {
				args.putString("namePR", currentPlayerRight.getName());
			} else {
				args.putString("namePR", currentPlayerRight.getShortName());
			}
			args.putString("fotoPR", currentPlayerRight.getUrlFoto());
			args.putString("datePR", currentPlayerRight.getDateBorn());
			args.putString("paisPR", currentPlayerRight.getNacionality());
			args.putInt("edadPR", currentPlayerRight.getAge());
			args.putDouble("alturaPR", currentPlayerRight.getHeight());
			args.putDouble("pesoPR", currentPlayerRight.getWeight());
			args.putString("positionPR", currentPlayerRight.getDemarcation());
			args.putInt("dorsalPR", currentPlayerRight.getDorsal());
			Bundle bundleStatsPR = new Bundle(currentPlayerRight.getStats()
					.size());
			for (Iterator<String> iterator = currentPlayerRight.getStats()
					.keySet().iterator(); iterator.hasNext();) {
				year = iterator.next();
				bundleStatsPR.putParcelable(year,
						currentPlayerRight.getStats(year));
			}
			args.putBundle("statsPR", bundleStatsPR);
			args.putParcelableArrayList("palmaresPR",
					currentPlayerRight.getPalmares());

			playerCompFragment.setArguments(args);

			fList.add(playerCompFragment);
		} else if (currentPlayerLeft != null) {
			playerCompFragment = Fragment.instantiate(this,
					ComparatorPlayerFragmentStepFirst.class.getName());
			// Fragment Players
			Bundle args = new Bundle();
			args.putString("teamNamePL", teamPLName);
			if (currentPlayerLeft.getName() != null) {
				args.putString("namePL", currentPlayerLeft.getName());
			} else {
				args.putString("namePL", currentPlayerLeft.getShortName());
			}
			args.putString("fotoPL", currentPlayerLeft.getUrlFoto());
			args.putString("datePL", currentPlayerLeft.getDateBorn());
			args.putString("paisPL", currentPlayerLeft.getNacionality());
			args.putInt("edadPL", currentPlayerLeft.getAge());
			args.putDouble("alturaPL", currentPlayerLeft.getHeight());
			args.putDouble("pesoPL", currentPlayerLeft.getWeight());
			args.putString("positionPL", currentPlayerLeft.getDemarcation());
			args.putInt("dorsalPL", currentPlayerLeft.getDorsal());

			playerCompFragment.setArguments(args);

			fList.add(playerCompFragment);
		} else {
			playerCompFragment = Fragment.instantiate(this,
					ComparatorPlayerFragmentStepZero.class.getName());
			fList.add(playerCompFragment);
		}
		return fList;
	}

	private void configureImageFetcher() {
		ImageCacheParams cacheParams = new ImageCacheParams(this,
				Defines.NAME_CACHE_THUMBS);
		cacheParams.setMemCacheSizePercent(0.25f);

		mImageFetcher = new ImageFetcher(this, getResources()
				.getDimensionPixelSize(R.dimen.image_player_height));
		mImageFetcher.setLoadingImage(R.drawable.foto_generica);
		mImageFetcher.addImageCache(this.getSupportFragmentManager(),
				cacheParams);
	}

	/**
	 * 
	 * @return
	 */
	public ImageFetcher getmImageFetcher() {
		return mImageFetcher;
	}

	/************************************ THIRD STEP ******************************************/

	/********************************** Metodos de RemotePlayer ****************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * onSuccessPlayerRemoteconfig(es.prisacom.as.dao.model.database.Player)
	 */
	@Override
	public void onSuccessPlayerRemoteconfig(Player player) {
		RemotePlayerDAO.getInstance(this).removeListener(this);

		if (currentPlayerLeftId == 0 || currentPlayerLeftId == player.getId()) {
			currentPlayerLeft = player;
		} else if (currentPlayerRightId == player.getId()) {
			currentPlayerRight = player;

		}

		configViewPager();
		stopAnimation();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * onFailurePlayerRemoteconfig()
	 */
	@Override
	public void onFailurePlayerRemoteconfig() {
		RemotePlayerDAO.getInstance(this).removeListener(this);
		stopAnimation();
		((TextView) findViewById(R.id.errorMessage))
				.setText(getString(R.string.player_detail_error));
		findViewById(R.id.errorContent).setVisibility(View.VISIBLE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * onFailureNotPlayerConnection(int)
	 */
	@Override
	public void onFailureNotPlayerConnection(int currentPlayerId) {
		RemotePlayerDAO.getInstance(this).removeListener(this);
		currentPlayerRight = DatabaseDAO.getInstance(getApplicationContext())
				.getPlayer(currentPlayerId);

		findViewById(R.id.errorContent).setVisibility(View.INVISIBLE);

		AlertManager.showAlertOkDialog(this,
				getResources().getString(R.string.player_detail_not_updated),
				getResources().getString(R.string.connection_error_title),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						configViewPager();
						stopAnimation();
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * forceUpdatePlayerNeeded()
	 */
	@Override
	public void forceUpdatePlayerNeeded() {
		RemotePlayerDAO.getInstance(this).removeListener(this);

	}
	/********************************** Metodos de RemotePlayer ****************************************/

}
