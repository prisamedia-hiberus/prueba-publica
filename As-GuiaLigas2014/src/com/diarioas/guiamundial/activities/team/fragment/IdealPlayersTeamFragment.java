package com.diarioas.guiamundial.activities.team.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.player.PlayerActivity;
import com.diarioas.guiamundial.activities.team.TeamActivity;
import com.diarioas.guiamundial.dao.model.carrusel.PlayerOnField;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;

public class IdealPlayersTeamFragment extends TeamFragment {

	private ArrayList<View> views;
	private String[] localSG;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_idealplayers,
				container, false);

		return generalView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (views != null) {
			views.clear();
			views = null;
		}
	}

	@Override
	protected void configureView() {
		String gameSystem = getArguments().getString("gameSystem");

		String gameSys = DatabaseDAO.getInstance(mContext).getGameplay(
				gameSystem);

		ArrayList<PlayerOnField> idealPlayers = getArguments()
				.getParcelableArrayList("idealPlayers");

		LinearLayout idealPlayersContainer = (LinearLayout) generalView
				.findViewById(R.id.idealPlayersContainer);

		idealPlayersContainer.removeAllViews();

		localSG = gameSys.split("-");

		views = new ArrayList<View>();
		int contJugadores = 0;

		boolean rombo = false;
		boolean dp = false;
		int numRows = localSG.length;
		if (localSG[numRows - 1].equalsIgnoreCase("DP")) {
			dp = true;
			numRows--;
		} else if (localSG[numRows - 1].equalsIgnoreCase("Rombo")) {
			rombo = true;
			numRows--;
		}

		int numPlayers;

		// Add Portero;
		addRow(1, idealPlayersContainer, idealPlayers, contJugadores++, false,
				false);
		for (int i = 0; i < numRows; i++) {
			numPlayers = Integer.valueOf(localSG[i]);
			if (i == 1) {
				addRow(numPlayers, idealPlayersContainer, idealPlayers,
						contJugadores, rombo, dp);
			} else {
				addRow(numPlayers, idealPlayersContainer, idealPlayers,
						contJugadores, false, false);
			}
			contJugadores += numPlayers;
		}

	}

	@Override
	public void startFirstShow() {
		// startAnimPlayerRow(0, 0, 0);
	}

	//
	// protected void startAnimationTeam(int row, int numPlayer) {
	//
	// int numPlayers = Integer.valueOf(localSG[row]);
	//
	// if (numPlayer >= numPlayers) {
	// row += 1;
	// numPlayer = 0;
	// }
	//
	// if (row >= localSG.length)
	// return;
	//
	// int numTotal = 0;
	// for (int j = 0; j < row; j++) {
	// numTotal += Integer.valueOf(localSG[j]);
	// }
	//
	// startAnimPlayerRow(numPlayer + 1, row, numTotal);
	//
	// }

	// private void startAnimPlayerRow(final int i, final int row,
	// final int numTotal) {
	// Animation animation;
	// animation = getAnimation();
	//
	// animation.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	// views.get(numTotal + i).setVisibility(View.VISIBLE);
	// try {
	// Thread.sleep(150);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// startAnimationTeam(row, i);
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	//
	// String url = (String) views.get(numTotal + i).getTag();
	// if (!(url != null && url.length() > 0)) {
	// AlphaAnimation alpha = new AlphaAnimation(1, 0.5F);
	// alpha.setDuration(0);
	// alpha.setFillAfter(true);
	// views.get(numTotal + i).startAnimation(alpha);
	// }
	// views.get(numTotal + i).clearAnimation();
	// }
	//
	// });
	//
	// views.get(numTotal + i).startAnimation(animation);
	// }
	//
	// private Animation getAnimation() {
	// int durationMillis = 200;
	// TranslateAnimation traslacion = new TranslateAnimation(-700, 0, 0, 0);
	// traslacion.setDuration(durationMillis);
	//
	// return traslacion;
	// }

	private void addRow(int numPlayers, LinearLayout container,
			ArrayList<PlayerOnField> players, int contJugadores, boolean rombo,
			boolean dp) {

		LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 2);

		if ((!rombo && !dp)) {
			if (numPlayers < 5) {
				container.addView(
						addPlayers(numPlayers, players, contJugadores), params);
			} else {
				container.addView(addPlayersInTwoRows(players, contJugadores),
						params);
			}

		} else {
			if (rombo) {
				// uno en el centro
				container.addView(addPlayers(1, players, contJugadores));
				// Dos en las bandas
				contJugadores += 2;

				container.addView(
						addPlayersBanda(true, players, contJugadores), params);
				// Uno en el centro
				contJugadores += 1;

				container
						.addView(addPlayers(1, players, contJugadores), params);

			} else {
				// Dos en el centro
				container.addView(
						addPlayersBanda(false, players, contJugadores), params);
				// Dos en las bandas
				contJugadores += 2;
				container.addView(
						addPlayersBanda(true, players, contJugadores), params);

			}
		}
	}

	private LinearLayout addPlayersInTwoRows(ArrayList<PlayerOnField> players,
			int contJugadores) {
		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);

		lin.addView(addPlayersMiddleBanda(players, contJugadores), params);
		lin.addView(addPlayersMiddleCenter(players, contJugadores + 3), params);

		// LinearLayout.LayoutParams layoutParams = new
		// LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		// layoutParams.topMargin = DimenUtils.getRegularPixelFromDp(mContext,
		// 25);
		// layoutParams.topMargin = DimenUtils.getRegularPixelFromDp(mContext,
		// 25);
		// lin.setLayoutParams(layoutParams);

		return lin;
	}

	private LinearLayout addPlayersBanda(boolean isBanda,
			ArrayList<PlayerOnField> players, int init) {

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		PlayerOnField player;

		// Banda Izquierda
		player = players.get(init);

		if (!isBanda) {
			// 1Extra
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		}

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		if (isBanda) {
			// 2Extra
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		}
		// Banda Derecha

		player = players.get(init + 1);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		if (!isBanda) {
			// 1Extra
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		}

		return lin;
	}

	private LinearLayout addPlayersMiddleBanda(
			ArrayList<PlayerOnField> players, int init) {

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		PlayerOnField player;

		player = players.get(init);
		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

		player = players.get(init + 1);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

		player = players.get(init + 2);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));

		return lin;
	}

	private LinearLayout addPlayers(int numPlayers,
			ArrayList<PlayerOnField> players, int init) {
		PlayerOnField player;

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams paramsItem;

		for (int j = 0; j < numPlayers; j++) {

			paramsItem = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1);

			player = players.get(init + j);

			lin.addView(addPlayer(player), paramsItem);
		}

		return lin;
	}

	private LinearLayout addPlayersMiddleCenter(
			ArrayList<PlayerOnField> players, int init) {

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		PlayerOnField player;

		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
		player = players.get(init);
		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

		player = players.get(init + 1);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

		lin.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		return lin;
	}

	private RelativeLayout addPlayer(final PlayerOnField player) {
		RelativeLayout item;
		TextView playerName;

		item = (RelativeLayout) inflater.inflate(
				R.layout.item_ideal_players_player, null);

		playerName = (TextView) item.findViewById(R.id.playerName);
		FontUtils.setCustomfont(mContext, playerName,
				FontTypes.HELVETICANEUEBOLD);
		playerName.setText(player.getName());

		// TextView playerDorsal = (TextView)
		// item.findViewById(R.id.playerDorsal);
		// FontUtils.setCustomfont(mContext, playerDorsal,
		// FontTypes.HELVETICANEUEBOLD);
		// playerDorsal.setText(String.valueOf(player.getDorsal()));

		try {
			if (player.getUrlPhoto() != null
					&& player.getUrlPhoto().length() > 0)
				((TeamActivity) getActivity()).getmImageFetcher3().loadImage(
						player.getUrlPhoto(),
						(ImageView) item.findViewById(R.id.playerImage),
						R.drawable.foto_plantilla_generica);

		} catch (Exception e) {
			Log.e("IDEALPLAYERS",
					"No se ha podido descargar la imagen de la url: "
							+ player.getUrlPhoto());
		}
		item.setTag(player.getUrl());
		if (player.getUrl() != null && player.getUrl().length() > 0) {
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					goToPlayer(player.getId(), player.getUrl());

				}
			});
		} else {
			AlphaAnimation alpha = new AlphaAnimation(1, 0.5F);
			alpha.setDuration(0); // Make animation instant
			alpha.setFillAfter(true); // Tell it to persist after the animation
			// ends
			item.startAnimation(alpha);
		}
		views.add(item);
		return item;
	}

	protected void goToPlayer(int tag, String url) {
		// if (url != null && url.length() > 0) {
		Intent intent = new Intent(getActivity(), PlayerActivity.class);
		intent.putExtra("playerId", tag);
		intent.putExtra("teamName", getArguments().getString("teamName"));
		intent.putExtra("playerUrl", url);
		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);
		//
		// } else {
		// AlertManager.showAlertOkDialog(getActivity(), getResources()
		// .getString(R.string.player_detail_error), getResources()
		// .getString(R.string.connection_error_title),
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });

		// }

	}
}
