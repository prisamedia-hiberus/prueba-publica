/**
 * 
 */
package com.diarioas.guialigas.activities.player;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.player.comparator.PlayerComparatorStepFirstActivity;
import com.diarioas.guialigas.activities.player.fragment.PlayerInfoFragment;
import com.diarioas.guialigas.activities.player.fragment.PlayerPalmaresFragment;
import com.diarioas.guialigas.activities.player.fragment.PlayerTrayectoriaFragment;
import com.diarioas.guialigas.dao.model.player.Player;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemotePlayerDAO;
import com.diarioas.guialigas.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.FileUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.StringUtils;
import com.diarioas.guialigas.utils.viewpager.CustomViewPagerPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerActivity extends GeneralFragmentActivity implements
		ViewPager.OnPageChangeListener, RemotePlayerDAOListener {

	private static final int BUTTON_PALMARES = 0;
	private static final int BUTTON_INFO = 1;
	private static final int BUTTON_TRAYECTORIA = 2;

	private static final int BUTTON_INFO_PLAYER = 103;
	private static final int BUTTON_COMPARATOR_PLAYER = 104;

	private static final int BUTTON_PREV = 201;
	private static final int BUTTON_NEXT = 202;

	// private int currentTeamId;
	private int currentPos = 1;
	private Button palmaresPlayerButton;
	private Button dataPlayerButton;
	private Button trayectoriaPlayerButton;
	private CustomViewPagerPlayer playerViewPager;
	private Player currentPlayer;
	private String teamName;

	private PlayerInfoFragment infoFragment;
	private boolean isLoaded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_player);
		spinner = (RelativeLayout) findViewById(R.id.spinner);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isLoaded) {
			Bundle extras = getIntent().getExtras();
			int currentPlayerId = extras.getInt("playerId");
			if (extras.containsKey("teamName"))
				teamName = extras.getString("teamName");

			configActionBar();
			configView();

			startAnimation();
			RemotePlayerDAO.getInstance(this).addListener(this);
			if (extras.containsKey("playerUrl")) {
				RemotePlayerDAO.getInstance(this)
						.refreshDatabaseWithNewResults(currentPlayerId,
								extras.getString("playerUrl"));
			} else {
				RemotePlayerDAO.getInstance(this)
						.refreshDatabaseWithNewResults(currentPlayerId);
			}
			isLoaded = true;
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		RemotePlayerDAO.getInstance(this).removeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playerViewPager != null) {
			playerViewPager.removeAllViews();
			playerViewPager = null;
		}
		palmaresPlayerButton = null;
		dataPlayerButton = null;
		trayectoriaPlayerButton = null;
		if (currentPlayer != null)
			currentPlayer = null;
	}

	@Override
	public void onBackPressed() {
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
		case R.id.share:
			sharePlayer();
			break;
		}
		return true;
	}

	private void sharePlayer() {
		if (currentPlayer != null && currentPlayer.getName() != null) {
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

			// intent.putExtra(Intent.EXTRA_SUBJECT,
			// getString(R.string.app_name));
			Resources res = getResources();
			String body = String.format(res.getString(R.string.mens_share_player),
					currentPlayer.getName(),res.getString(R.string.share_google_play_url));

			
			intent.putExtra(Intent.EXTRA_TEXT, body);
			// i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
			startActivity(Intent.createChooser(intent,
					getString(R.string.share_mens_title)
							+ getString(R.string.app_name)));

			String playerName = StringUtils.getNormalizeText(currentPlayer
					.getName());
			StatisticsDAO.getInstance(getApplicationContext()).sendStatisticsShare(getApplication(),
					playerName,
					FileUtils.readOmnitureProperties(this, "SECTION_SEARCHER"),
                    FileUtils.readOmnitureProperties(this, "SUBSECTION_PLANTILLA"),
                    FileUtils.readOmnitureProperties(this, "SUBSECTION_FICHA"));
		}

	}

	@Override
	protected void configActionBar() {
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		if (teamName != null && !teamName.equalsIgnoreCase("")) {
			getSupportActionBar().setDisplayShowHomeEnabled(false);
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setTitle(teamName);
		} else {
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
		}

	}

	/**
	 * 
	 */
	private void configView() {

		palmaresPlayerButton = ((Button) findViewById(R.id.palmaresPlayerButton));
		FontUtils.setCustomfont(getApplicationContext(), palmaresPlayerButton,
				FontTypes.ROBOTO_BLACK);
		dataPlayerButton = ((Button) findViewById(R.id.dataPlayerButton));
		FontUtils.setCustomfont(getApplicationContext(), dataPlayerButton,
				FontTypes.ROBOTO_BLACK);
		trayectoriaPlayerButton = ((Button) findViewById(R.id.trayectoriaPlayerButton));
		FontUtils.setCustomfont(getApplicationContext(),
				trayectoriaPlayerButton, FontTypes.ROBOTO_BLACK);

	}

	private void configViewPager() {
		playerViewPager = (CustomViewPagerPlayer) findViewById(R.id.playerViewPager);
		playerViewPager.setTeamName(teamName);
		playerViewPager.setPlayerName(currentPlayer.getShortName());
		// Pintar los datos de la Current Day
		List<Fragment> fragments = getFragments();
		playerViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		setFirstPosition();
		playerViewPager.setOnPageChangeListener(this);
	}

	private void setFirstPosition() {
		playerViewPager.setCurrentItem(currentPos, true);
		playerViewPager.setActiveFragment(currentPos);
		callToOmniture(currentPos);

	}

	private void callToOmniture(int pos) {
		String normalize = StringUtils.getNormalizeText(teamName);
		String playerName = StringUtils.getNormalizeText(currentPlayer
				.getName());
		String team = StringUtils.getNormalizeText(currentPlayer
				.getNameTeam());
		switch (pos) {
		case 0:
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsState(getApplication(),
							//Omniture.SECTION_SEARCHER,
							team,
							FileUtils.readOmnitureProperties(this, "SUBSECTION_PLANTILLA"),
                            FileUtils.readOmnitureProperties(this, "SUBSECTION_FICHA"),
							FileUtils.readOmnitureProperties(this, "TEMA_PALMARES"),
                            FileUtils.readOmnitureProperties(this, "TYPE_ARTICLE"),
							playerName + " " + FileUtils.readOmnitureProperties(this, "TEMA_PALMARES"),
							null);
			break;
		case 1:
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsState(getApplication(),
							//Omniture.SECTION_SEARCHER,
							team,
                            FileUtils.readOmnitureProperties(this, "SUBSECTION_PLANTILLA"),
                            FileUtils.readOmnitureProperties(this, "SUBSECTION_FICHA"),
                            FileUtils.readOmnitureProperties(this, "TEAM_INFORMATION"),
                            FileUtils.readOmnitureProperties(this, "TYPE_ARTICLE"),
							playerName + " " + FileUtils.readOmnitureProperties(this, "TEMA_INFORMATION"),
							null);
			break;
		case 2:
			StatisticsDAO.getInstance(getApplicationContext())
					.sendStatisticsState(getApplication(),
							//Omniture.SECTION_SEARCHER,
							team,
                            FileUtils.readOmnitureProperties(this, "SUBSECTION_PLANTILLA"),
                            FileUtils.readOmnitureProperties(this, "SUBSECTION_FICHA"),
							FileUtils.readOmnitureProperties(this, "TEMA_TRAYECTORIA"),
                            FileUtils.readOmnitureProperties(this, "TYPE_ARTICLE"),
							playerName + " " + FileUtils.readOmnitureProperties(this, "TEMA_TRAYECTORIA"),
							null);
			break;

		default:
			break;
		}
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		Bundle args;

		Fragment palmaresFragment = Fragment.instantiate(this,
				PlayerPalmaresFragment.class.getName());
		args = new Bundle();
		if (currentPlayer.getName() != null) {
			args.putString("name", currentPlayer.getName());
		} else {
			args.putString("name", currentPlayer.getShortName());
		}
		args.putString("foto", currentPlayer.getUrlFoto());
		args.putString("teamName", teamName);
		args.putString("date", currentPlayer.getDateBorn());
		args.putString("pais", currentPlayer.getNacionality());
		args.putInt("edad", currentPlayer.getAge());
		args.putParcelableArrayList("palmares", currentPlayer.getPalmares());
		args.putBoolean("isTag", currentPlayer.getUrlTag() != null
				&& !currentPlayer.getUrlTag().equalsIgnoreCase(""));

		palmaresFragment.setArguments(args);

		infoFragment = (PlayerInfoFragment) Fragment.instantiate(this,
				PlayerInfoFragment.class.getName());
		args = new Bundle();
		if (currentPlayer.getName() != null) {
			args.putString("name", currentPlayer.getName());
		} else {
			args.putString("name", currentPlayer.getShortName());
		}
		args.putString("foto", currentPlayer.getUrlFoto());
		args.putString("teamName", teamName);
		args.putString("date", currentPlayer.getDateBorn());
		args.putString("pais", currentPlayer.getNacionality());
		args.putInt("edad", currentPlayer.getAge());
		args.putDouble("altura", currentPlayer.getHeight());
		args.putDouble("peso", currentPlayer.getWeight());
		args.putString("position", currentPlayer.getDemarcation());
		args.putBoolean("isTag", currentPlayer.getUrlTag() != null
				&& !currentPlayer.getUrlTag().equalsIgnoreCase(""));

		Bundle bundleStats = new Bundle(currentPlayer.getStats().size());
		String year;
		for (Iterator<String> iterator = currentPlayer.getStats().keySet()
				.iterator(); iterator.hasNext();) {
			year = iterator.next();
			bundleStats.putParcelable(year, currentPlayer.getStats(year));
		}
		args.putBundle("stats", bundleStats);

		infoFragment.setArguments(args);
		Fragment trayectoriaFragment = Fragment.instantiate(this,
				PlayerTrayectoriaFragment.class.getName());
		args = new Bundle();
		if (currentPlayer.getName() != null) {
			args.putString("name", currentPlayer.getName());
		} else {
			args.putString("name", currentPlayer.getShortName());
		}
		args.putString("foto", currentPlayer.getUrlFoto());
		args.putString("teamName", teamName);
		args.putString("date", currentPlayer.getDateBorn());
		args.putString("pais", currentPlayer.getNacionality());
		args.putInt("edad", currentPlayer.getAge());
		args.putString("competiciones", currentPlayer.getCompeticiones());
		args.putParcelableArrayList("trayectoria",
				currentPlayer.getTrayectoria());
		args.putBoolean("isTag", currentPlayer.getUrlTag() != null
				&& !currentPlayer.getUrlTag().equalsIgnoreCase(""));

		trayectoriaFragment.setArguments(args);

		fList.add(palmaresFragment);
		fList.add(infoFragment);
		fList.add(trayectoriaFragment);
		return fList;
	}

	private void loadInformation(Player player) {
		currentPlayer = player;
		if (teamName == null || teamName.equalsIgnoreCase("")) {
			// teamName =
			// DatabaseDAO.getInstance(getApplicationContext()).getTeamName(player.getIdTeam());
			teamName = player.getNameTeam();
			// getSupportActionBar().setTitle(teamName);
		}
		configViewPager();
		if (player.getShortName() != null) {
			String section = NativeAds.AD_PLAYER
			// + StringUtils.getNormalizeText(player.getShortName())
			;
			callToAds(section, true);
		}

	}

	private void changeView(int pos) {
		if (currentPos != pos) {
			currentPos = pos;
			if (playerViewPager != null) {
				playerViewPager.setCurrentItem(pos);
			}

			switch (pos) {
			case BUTTON_PALMARES:
				palmaresPlayerButton.setTextColor(getResources().getColor(
						R.color.red));
				palmaresPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team_selected);
				dataPlayerButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				dataPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				trayectoriaPlayerButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				trayectoriaPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team);

				break;
			case BUTTON_INFO:
				palmaresPlayerButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				palmaresPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				dataPlayerButton.setTextColor(getResources().getColor(
						R.color.red));
				dataPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team_selected);
				trayectoriaPlayerButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				trayectoriaPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team);

				break;
			case BUTTON_TRAYECTORIA:
				palmaresPlayerButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				palmaresPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				dataPlayerButton.setTextColor(getResources().getColor(
						R.color.medium_gray));
				dataPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team);
				trayectoriaPlayerButton.setTextColor(getResources().getColor(
						R.color.red));
				trayectoriaPlayerButton
						.setBackgroundResource(R.drawable.textura_footer_team_selected);

				break;

			default:
				break;
			}
			callToOmniture(pos);
		}

	}

	public void buttonClick(View v) {
		int tag = Integer.valueOf((String) v.getTag());

		switch (tag) {
		case BUTTON_INFO:
		case BUTTON_PALMARES:
		case BUTTON_TRAYECTORIA:
			changeView(tag);
			break;
		case BUTTON_INFO_PLAYER:
			// if (teamName != null && !teamName.equalsIgnoreCase("")) {
			// StatisticsDAO.getInstance(this).sendStatisticsState(
			// getApplication(), teamName.toLowerCase(),
			// Ommniture.SUBSECTION_PLAYER_NEWS, null, null,
			// Ommniture.HOMEPAGE_CONTENT_TYPE,
			// currentPlayer.getShortName().toLowerCase(), null);
			// }
			goToWeb(currentPlayer.getUrlTag());

			break;
		case BUTTON_COMPARATOR_PLAYER:
			Intent intent = new Intent(PlayerActivity.this,
					PlayerComparatorStepFirstActivity.class);
			intent.putExtra("playerId", currentPlayer.getId());
			intent.putExtra("teamName", teamName);
			PlayerActivity.this.startActivityForResult(intent,
					ReturnRequestCodes.PUBLI_BACK);
			overridePendingTransition(R.anim.slide_in_left, R.anim.null_anim);
			break;
		case BUTTON_PREV:
			infoFragment.setPrevCompetition();
			break;
		case BUTTON_NEXT:
			infoFragment.setNextCompetition();
			break;
		default:
			break;
		}

	}

	private void goToWeb(String url) {
		if (url != null && !url.equalsIgnoreCase("")) {
			if (url.contains("http") == false) {
				url = "http://" + url;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			PlayerActivity.this.startActivityForResult(intent,
					ReturnRequestCodes.PUBLI_BACK);
		}

	}

	/**************** Metodos de RemotePlayer *****************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * onSuccessRemoteconfig(es.prisacom.as.dao.model.database.Player)
	 */
	@Override
	public void onSuccessPlayerRemoteconfig(Player player) {
		RemotePlayerDAO.getInstance(this).removeListener(this);
		stopAnimation();
		loadInformation(player);
		findViewById(R.id.buttonBar).setVisibility(View.VISIBLE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * onFailureRemoteconfig()
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
	 * onFailureNotConnection()
	 */
	@Override
	public void onFailureNotPlayerConnection(int currentPlayerId) {
		RemotePlayerDAO.getInstance(this).removeListener(this);
		final Player player = DatabaseDAO.getInstance(getApplicationContext())
				.getPlayer(currentPlayerId);
		stopAnimation();
		if (player != null && player.getName() != null) {
			findViewById(R.id.errorContent).setVisibility(View.INVISIBLE);

			findViewById(R.id.buttonBar).setVisibility(View.VISIBLE);
			AlertManager.showAlertOkDialog(this,
					getResources()
							.getString(R.string.player_detail_not_updated),
					getResources().getString(R.string.connection_error_title),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							loadInformation(player);
						}
					});
		} else {
			findViewById(R.id.errorContent).setVisibility(View.VISIBLE);
			// AlertManager.showAlertOkDialog(
			// this,
			// getResources().getString(
			// R.string.no_internet_connection_player_detail),
			// getResources().getString(R.string.connection_error_title),
			// new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// stopAnimation();
			// }
			// });

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemotePlayerDAO.RemotePlayerDAOListener#
	 * forceUpdateNeeded()
	 */
	@Override
	public void forceUpdatePlayerNeeded() {
		RemotePlayerDAO.getInstance(this).removeListener(this);

	}

	/*********************************************************************************/

	/**************** Metodos de OnPageChangeListener *****************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// Toast.makeText(mContext, "Pagina cambiada: ", Toast.LENGTH_SHORT)
		// .show();
		changeView(pos);

	}

	/**********************************************************************************/
}
