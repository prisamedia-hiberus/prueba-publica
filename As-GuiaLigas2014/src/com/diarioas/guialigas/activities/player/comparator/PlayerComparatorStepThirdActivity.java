/**
 * 
 */
package com.diarioas.guialigas.activities.player.comparator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.activities.team.fragment.TeamPlayersFragment;
import com.diarioas.guialigas.dao.model.player.Player;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteTeamDAO;
import com.diarioas.guialigas.dao.reader.RemoteTeamDAO.RemoteTeamDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.viewpager.CustomViewPagerLeague;

public class PlayerComparatorStepThirdActivity extends GeneralFragmentActivity
		implements RemoteTeamDAOListener {

	private CustomViewPagerLeague playerComparatorViewPager;
	private String teamId;

	/********************** Search RightPlayer ***************************/

	// Second Step
	private Team teamPR;
	private boolean resultOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_player_comparator);
		spinner = (RelativeLayout) findViewById(R.id.spinner);
		((TextView) findViewById(R.id.spinnerMessage))
				.setText(getString(R.string.spinner_message_team));

		teamId = getIntent().getExtras().getString("teamId");
		int competitionId = getIntent().getExtras().getInt("competitionId");

		configActionBar();
		startAnimation();
		RemoteTeamDAO.getInstance(this).addListener(this);
		RemoteTeamDAO.getInstance(this).getTeamData(teamId,
				String.valueOf(competitionId));

	}

	@Override
	protected void onStart() {
		super.onStart();
		resultOk = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		StatisticsDAO.getInstance(this).sendStatisticsState(
				getApplication(),
				Omniture.SECTION_COMPARATOR,
				Omniture.SUBSECTION_COUNTRIES,
				Omniture.SUBSUBSECTION_PLAYERS,
				null,
				Omniture.TYPE_ARTICLE,
				Omniture.DETAILPAGE_DETALLE + " " + Omniture.SECTION_COMPARATOR
						+ " " + Omniture.SUBSECTION_COUNTRIES + " "
						// TODO: Falta el nombre del equipo
						+ Omniture.SUBSUBSECTION_PLAYERS, null);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (resultOk)
			overridePendingTransition(R.anim.null_anim, R.anim.slide_out_down);
		else
			overridePendingTransition(R.anim.grow_from_middle,
					R.anim.shrink_to_middle);
	}

	@Override
	protected void configActionBar() {
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.header_bar_comparator);

		getSupportActionBar().getCustomView().findViewById(R.id.backContent)
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();

					}
				});

		getSupportActionBar().getCustomView().findViewById(R.id.cancelContent)
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent newIntent = new Intent();

						setResult(RESULT_OK, newIntent);
						resultOk = true;

						onBackPressed();
					}
				});

	}

	/************************************ SECOND STEP ******************************************/

	private void configViewPager() {

		List<Fragment> fragments = getFragmentsSecondStep();
		playerComparatorViewPager = (CustomViewPagerLeague) findViewById(R.id.leagueViewPager);

		playerComparatorViewPager.setAdapter(new FragmentAdapter(
				getSupportFragmentManager(), fragments));
		playerComparatorViewPager.setCurrentItem(0, true);
	}

	private List<Fragment> getFragmentsSecondStep() {
		ArrayList<Fragment> fList = new ArrayList<Fragment>();

		// if (teamPR.getPlantilla().size() != 0) {
		Fragment playerFragment = Fragment.instantiate(this,
				TeamPlayersFragment.class.getName());
		// Fragment Players
		Bundle args = new Bundle();
		if (teamPR.getName() != null)
			args.putString("name", teamPR.getName());
		else
			args.putString("name", teamPR.getShortName());
		args.putString("shield", teamPR.getCalendarShield());
		Bundle bundlePlant = new Bundle();
		Bundle bundlePlayer;
		String dorsal;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		for (Player player : teamPR.getPlantilla()) {
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

			bundlePlant.putBundle(player.getShortName(), bundlePlayer);
		}
		args.putBundle("plantilla", bundlePlant);
		playerFragment.setArguments(args);

		fList.add(playerFragment);
		// }
		return fList;
	}

	public void playerClicked(View view) {

		Integer playerId = Integer.valueOf((String) view.getTag());
		String url = DatabaseDAO.getInstance(getApplicationContext())
				.getPlayer(playerId).getUrl();
		if (url != null && url.length() > 1) {
			Intent newIntent = new Intent();
			newIntent.putExtra("playerId", playerId);
			newIntent.putExtra("teamName", teamPR.getShortName());
			setResult(RESULT_OK, newIntent);
			resultOk = true;
//			onBackPressed();
			overridePendingTransition(R.anim.null_anim, R.anim.slide_out_down);
			finish();
		} else {
			// AlertManager.showAlertOkDialog(
			// this,
			// getResources().getString(
			// R.string.player_comparator_not_content),
			// getResources()
			// .getString(R.string.connection_atention_title),
			//
			// new OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// });
		}
	}

	/*********************************** Metodos de RemoteTeam *****************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemoteTeamDAO.RemoteTeamDAOListener#
	 * onSuccessRemoteconfig()
	 */
	@Override
	public void onSuccessTeamRemoteconfig(Team team) {
		RemoteTeamDAO.getInstance(this).removeListener(this);
		teamPR = team;
		configViewPager();
		stopAnimation();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemoteTeamDAO.RemoteTeamDAOListener#
	 * onFailureRemoteconfig()
	 */
	@Override
	public void onFailureTeamRemoteconfig(Team team) {
		RemoteTeamDAO.getInstance(this).removeListener(this);

		if (team != null && team.getPlantilla().size() > 0) {
			teamPR = team;
			findViewById(R.id.errorContent).setVisibility(View.INVISIBLE);

			AlertManager.showAlertOkDialog(this,
					getResources().getString(R.string.team_detail_error),
					getResources().getString(R.string.connection_error_title),

					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							stopAnimation();

						}
					});
		} else {
			stopAnimation();
			((TextView) findViewById(R.id.errorMessage))
					.setText(getString(R.string.error_team_message));
			findViewById(R.id.errorContent).setVisibility(View.INVISIBLE);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.RemoteTeamDAO.RemoteTeamDAOListener#
	 * onFailureNotConnection()
	 */
	@Override
	public void onFailureNotTeamConnection(Team team) {
		RemoteTeamDAO.getInstance(this).removeListener(this);

		if (team != null && team.getPlantilla().size() > 0) {
			teamPR = team;
			findViewById(R.id.errorContent).setVisibility(View.INVISIBLE);

			AlertManager.showAlertOkDialog(this,
					getResources().getString(R.string.team_detail_not_updated),
					getResources().getString(R.string.connection_error_title),

					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							stopAnimation();
							configViewPager();
						}
					});
		} else {
			stopAnimation();
			((TextView) findViewById(R.id.errorMessage))
					.setText(getString(R.string.error_team_message));
			findViewById(R.id.errorContent).setVisibility(View.VISIBLE);
		}
	}

	/*********************************** Metodos de RemoteTeam *****************************************/

}
