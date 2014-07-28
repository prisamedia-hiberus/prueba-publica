/**
 * 
 */
package com.diarioas.guiamundial.activities.carrusel.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.carrusel.CarruselDetailActivity;
import com.diarioas.guiamundial.dao.model.carrusel.GameSystem;
import com.diarioas.guiamundial.dao.model.carrusel.GameSystem.Event;
import com.diarioas.guiamundial.dao.model.carrusel.PlayerOnField;
import com.diarioas.guiamundial.dao.reader.CarruselDAO;
import com.diarioas.guiamundial.dao.reader.CarruselDAO.CarruselDAOGameSystemListener;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.Defines.CarruselEventos;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.diarioas.guiamundial.utils.scroll.CustomScrollView;

/**
 * @author robertosanchez
 * 
 */
public class CarruselGameSystemFragment extends CarruselFragment implements
		CarruselDAOGameSystemListener {

	private GameSystem gameSystem;
	private int idShieldLocal;
	private int idShieldAway;
	private CustomScrollView fieldScroll;
	private boolean firstTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_carrusel_plantilla,
				container, false);

		idShieldLocal = getArguments().getInt("idShieldLocal");
		idShieldAway = getArguments().getInt("idShieldAway");

		configureView();
		updateInfo();

		return generalView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		CarruselDAO.getInstance(mContext).removeGameSystemListener(this);
	}

	/**
	 * 
	 */
	@Override
	protected void configureView() {
		super.configureView();
		fieldScroll = (CustomScrollView) generalView
				.findViewById(R.id.fieldScroll);
		firstTime = true;

	}

	public void updateInfo() {

		if (url != null) {
			if (CarruselDAO.getInstance(mContext).isDetailUpdating(url)) {
				this.gameSystem = (GameSystem) CarruselDAO
						.getInstance(mContext).getDetailCached(url);
				loadData();
			} else {
				updateData();
				startAnimation();
			}
		}
	}

	@Override
	public void updateData() {
		super.updateData();
		CarruselDAO.getInstance(mContext).addGameSystemListener(this);
		CarruselDAO.getInstance(mContext).getPlantilla(url);
	}

	/**
	 * 
	 */
	private void loadData() {
		generalView.findViewById(R.id.itemsLayout).setVisibility(View.VISIBLE);

		String gameSys = DatabaseDAO.getInstance(mContext).getGameplay(
				String.valueOf(gameSystem.getLocalTeamGSId()));

		if (gameSys == null || gameSys.equalsIgnoreCase("")) {
			gameSys = Defines.GAME_SYSTEM_DEFAULT;
		}
		// Titulares Locales
		setLocalTitulares(gameSys);

		// Suplentes Locales
		setLocalSuplentes(gameSys);

		gameSys = DatabaseDAO.getInstance(mContext).getGameplay(
				String.valueOf(gameSystem.getAwayTeamGSId()));

		if (gameSys == null || gameSys.equalsIgnoreCase("")) {
			gameSys = Defines.GAME_SYSTEM_DEFAULT;
		}
		// Titulares Foraneos
		setAwayTitulares(gameSys);

		// Suplentes Locales
		setAwaySuplentes(gameSys);

		final LinearLayout localSuplentesContainer = (LinearLayout) generalView
				.findViewById(R.id.localSuplentesContainer);
		final LinearLayout awaySuplentesContainer = (LinearLayout) generalView
				.findViewById(R.id.awaySuplentesContainer);

		localSuplentesContainer.getViewTreeObserver()
				.addOnGlobalLayoutListener(
						new ViewTreeObserver.OnGlobalLayoutListener() {
							@SuppressLint("NewApi")
							@Override
							public void onGlobalLayout() {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
									localSuplentesContainer
											.getViewTreeObserver()
											.removeOnGlobalLayoutListener(this);
								} else {
									localSuplentesContainer
											.getViewTreeObserver()
											.removeGlobalOnLayoutListener(this);
								}
								if (firstTime) {
									fieldScroll
											.scrollTo(0,
													localSuplentesContainer
															.getBottom());
									firstTime = false;
								}

								// fieldScroll
								// .setHeightTop(localSuplentesContainer
								// .getHeight());
							}
						});

		// awaySuplentesContainer.getViewTreeObserver().addOnGlobalLayoutListener(
		// new ViewTreeObserver.OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		// awaySuplentesContainer.getViewTreeObserver()
		// .removeOnGlobalLayoutListener(this);
		// } else {
		// awaySuplentesContainer.getViewTreeObserver()
		// .removeGlobalOnLayoutListener(this);
		// }
		// fieldScroll.setHeightBottom(awaySuplentesContainer
		// .getHeight());
		//
		// }
		// });

	}

	/**
	 * @param gameSys
	 * 
	 */
	private void setLocalSuplentes(String gameSys) {

		ImageView localShield = (ImageView) generalView
				.findViewById(R.id.localShield);

//		if (idShieldLocal == 0) {
//			// idShieldLocal = R.drawable.escudo_generico_size03;
//			idShieldLocal = R.drawable.flag_956;
//		}

		localShield.setBackgroundResource(idShieldLocal);

		TextView localGameSistem = (TextView) generalView
				.findViewById(R.id.localGameSistem);
		FontUtils.setCustomfont(mContext, localGameSistem,
				FontTypes.HELVETICANEUE);
		localGameSistem.setText(gameSys);

		TextView localMister = (TextView) generalView
				.findViewById(R.id.localMister);
		FontUtils.setCustomfont(mContext, localMister, FontTypes.HELVETICANEUE);
		localMister.setText(gameSystem.getLocalTeamMisterName());

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.localBanquillo),
				FontTypes.HELVETICANEUE);

		LinearLayout localSuplentesContainer = (LinearLayout) generalView
				.findViewById(R.id.localSuplentesContainer2);
		localSuplentesContainer.removeAllViews();
		PlayerOnField player;

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		int numPlayers = gameSystem.getLocalSuplentes().size();
		for (int i = 0; i < numPlayers; i++) {
			player = gameSystem.getLocalSuplentes().get(i);
			lin.addView(addSuplente(player));
			if (i % 2 != 0) {
				localSuplentesContainer.addView(lin);
				lin = new LinearLayout(mContext);
				lin.setOrientation(LinearLayout.HORIZONTAL);
			}
		}

		if (numPlayers % 2 != 0)
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));

		localSuplentesContainer.addView(lin);

	}

	/**
	 * @param gameSys
	 * 
	 */
	private void setAwaySuplentes(String gameSys) {

		ImageView awayShield = (ImageView) generalView
				.findViewById(R.id.awayShield);
//		if (idShieldAway == 0) {
//			// idShieldAway = R.drawable.escudo_generico_size03;
//			idShieldAway = R.drawable.flag_956;
//		}

		awayShield.setBackgroundResource(idShieldAway);

		TextView awayGameSistem = (TextView) generalView
				.findViewById(R.id.awayGameSistem);
		FontUtils.setCustomfont(mContext, awayGameSistem,
				FontTypes.HELVETICANEUE);
		awayGameSistem.setText(gameSys);

		TextView awayMister = (TextView) generalView
				.findViewById(R.id.awayMister);
		FontUtils.setCustomfont(mContext, awayMister, FontTypes.HELVETICANEUE);
		awayMister.setText(gameSystem.getAwayTeamMisterName());

		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.awayBanquillo),
				FontTypes.HELVETICANEUE);

		LinearLayout awaySuplentesContainer = (LinearLayout) generalView
				.findViewById(R.id.awaySuplentesContainer2);
		awaySuplentesContainer.removeAllViews();
		PlayerOnField player;

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		int numPlayers = gameSystem.getAwaySuplentes().size();
		for (int i = 0; i < numPlayers; i++) {
			player = gameSystem.getAwaySuplentes().get(i);
			lin.addView(addSuplente(player));
			if (i % 2 != 0) {
				awaySuplentesContainer.addView(lin);
				lin = new LinearLayout(mContext);
				lin.setOrientation(LinearLayout.HORIZONTAL);
			}
		}

		if (numPlayers % 2 != 0)
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));

		awaySuplentesContainer.addView(lin);

	}

	private void setAwayTitulares(String gameSys) {

		String[] localSG;
		LinearLayout awayTitularesContainer = (LinearLayout) generalView
				.findViewById(R.id.awayTitularesContainer);
		awayTitularesContainer.removeAllViews();

		localSG = gameSys.split("-");

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

		int contJugadores = 10;
		int numPlayers;
		for (int i = numRows - 1; i >= 0; i--) {
			numPlayers = Integer.valueOf(localSG[i]);
			if (i == 1) {
				addRow(numPlayers, awayTitularesContainer,
						this.gameSystem.getAwayTitulares(), contJugadores,
						rombo, dp, false);
			} else {
				addRow(numPlayers, awayTitularesContainer,
						this.gameSystem.getAwayTitulares(), contJugadores,
						false, false, false);
			}
			contJugadores -= numPlayers;
		}

		// Add Portero
		awayTitularesContainer.addView(addPlayers(1,
				this.gameSystem.getAwayTitulares(), contJugadores, false));
	}

	private void setLocalTitulares(String gameSys) {
		LinearLayout localTitularesContainer = (LinearLayout) generalView
				.findViewById(R.id.localTitularesContainer);

		String[] localSG;
		localTitularesContainer.removeAllViews();

		localSG = gameSys.split("-");

		int contJugadores = 0;
		// Add Portero
		localTitularesContainer.addView(addPlayers(1,
				this.gameSystem.getLocalTitulares(), contJugadores, true));

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
		contJugadores++;
		for (int i = 0; i < numRows; i++) {
			numPlayers = Integer.valueOf(localSG[i]);
			if (i == 1) {
				addRow(numPlayers, localTitularesContainer,
						this.gameSystem.getLocalTitulares(), contJugadores,
						rombo, dp, true);
			} else {
				addRow(numPlayers, localTitularesContainer,
						this.gameSystem.getLocalTitulares(), contJugadores,
						false, false, true);
			}
			contJugadores += numPlayers;
		}
	}

	private void addRow(int numPlayers, LinearLayout container,
			ArrayList<PlayerOnField> players, int contJugadores, boolean rombo,
			boolean dp, boolean order) {

		if ((!rombo && !dp)) {
			if (numPlayers < 5) {
				container.addView(addPlayers(numPlayers, players,
						contJugadores, order));
			} else {
				LinearLayout lin = addPlayersInTwoRows(players, contJugadores,
						order);

				container.addView(lin);
			}

		} else {
			if (rombo) {
				// uno en el centro
				container.addView(addPlayers(1, players, contJugadores, order));
				// Dos en las bandas
				if (order)
					contJugadores += 1;
				else
					contJugadores -= 1;
				container.addView(addPlayersBanda(true, players, contJugadores,
						order));
				// Uno en el centro
				if (order)
					contJugadores += 2;
				else
					contJugadores -= 2;
				container.addView(addPlayers(1, players, contJugadores, order));
			} else {
				if (order) {
					// Dos en el centro
					container.addView(addPlayersBanda(false, players,
							contJugadores, order));
					// Dos en las bandas
					contJugadores += 2;
					container.addView(addPlayersBanda(true, players,
							contJugadores, order));
				} else {
					// Dos en las bandas
					container.addView(addPlayersBanda(true, players,
							contJugadores, order));
					// Dos en el centro
					contJugadores -= 2;
					container.addView(addPlayersBanda(false, players,
							contJugadores, order));
				}

			}
		}
	}

	private LinearLayout addPlayersInTwoRows(ArrayList<PlayerOnField> players,
			int contJugadores, boolean order) {
		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.VERTICAL);
		if (order) {
			lin.addView(addPlayersMiddleBanda(players, contJugadores, order));
			lin.addView(addPlayersMiddleCenter(players, contJugadores + 3,
					order));
		} else {
			lin.addView(addPlayersMiddleCenter(players, contJugadores - 3,
					order));
			lin.addView(addPlayersMiddleBanda(players, contJugadores, order));
		}
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
		layoutParams.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 25);
		layoutParams.topMargin = DimenUtils.getRegularPixelFromDp(mContext, 25);
		lin.setLayoutParams(layoutParams);

		return lin;
	}

	private LinearLayout addPlayers(int numPlayers,
			ArrayList<PlayerOnField> players, int init, boolean order) {
		PlayerOnField player;

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams paramsItem;

		for (int j = 0; j < numPlayers; j++) {

			paramsItem = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1);

			if (order)
				player = players.get(init + j);
			else
				player = players.get(init - j);

			lin.addView(addPlayer(player), paramsItem);
		}

		lin.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		return lin;
	}

	private LinearLayout addPlayersMiddleBanda(
			ArrayList<PlayerOnField> players, int init, boolean order) {

		LinearLayout lin = new LinearLayout(mContext);
		lin.setOrientation(LinearLayout.HORIZONTAL);

		PlayerOnField player;

		player = players.get(init);
		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
		if (order)
			player = players.get(init + 1);
		else
			player = players.get(init - 1);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
		if (order)
			player = players.get(init + 2);
		else
			player = players.get(init - 2);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));

		lin.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		return lin;
	}

	private LinearLayout addPlayersMiddleCenter(
			ArrayList<PlayerOnField> players, int init, boolean order) {

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
		if (order)
			player = players.get(init + 1);
		else
			player = players.get(init - 1);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		// 1Extra
		lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));

		lin.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		return lin;
	}

	private LinearLayout addPlayersBanda(boolean isBanda,
			ArrayList<PlayerOnField> players, int init, boolean order) {

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
		if (order)
			player = players.get(init + 1);
		else
			player = players.get(init - 1);

		lin.addView(addPlayer(player), new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		if (!isBanda) {
			// 1Extra
			lin.addView(new View(mContext), new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		}

		lin.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		return lin;
	}

	private RelativeLayout addPlayer(final PlayerOnField player) {
		RelativeLayout item;
		TextView playerName, playerDorsal;
		item = (RelativeLayout) inflater.inflate(
				R.layout.item_grid_carrusel_player, null);

		playerName = (TextView) item.findViewById(R.id.playerName);
		FontUtils.setCustomfont(mContext, playerName,
				FontTypes.HELVETICANEUEBOLD);
		playerName.setText(player.getName());

		playerDorsal = (TextView) item.findViewById(R.id.playerDorsal);
		FontUtils.setCustomfont(mContext, playerDorsal,
				FontTypes.HELVETICANEUEBOLD);
		playerDorsal.setText(String.valueOf(player.getDorsal()));

		try {
			((CarruselDetailActivity) getActivity()).getmImageFetcher()
					.loadImage(player.getUrlPhoto(),
							(ImageView) item.findViewById(R.id.playerImage),
							R.drawable.foto_plantilla_mask);
		} catch (Exception e) {
			Log.e("CARRUSEL", "No se ha podido descargar la imagen de la url: "
					+ player.getUrlPhoto());
		}

		if (this.gameSystem.isScorer(player.getId()))
			item.findViewById(R.id.golContent).setVisibility(View.VISIBLE);

		if (this.gameSystem.isChanged(player.getId()))
			item.findViewById(R.id.changeContent).setVisibility(View.VISIBLE);

		if (this.gameSystem.isRedCard(player.getId())) {
			item.findViewById(R.id.tarjetaContent).setVisibility(View.VISIBLE);
			item.findViewById(R.id.tarjetaImage).setBackgroundResource(
					R.drawable.plantilla_roja);
		} else if (this.gameSystem.isYellowCard(player.getId())) {
			item.findViewById(R.id.tarjetaContent).setVisibility(View.VISIBLE);
			item.findViewById(R.id.tarjetaImage).setBackgroundResource(
					R.drawable.plantilla_amarilla);
		}

		// item.setTag(player.getId());
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToPlayer(player.getId(), player.getUrl());

			}
		});

		return item;
	}

	private LinearLayout addSuplente(final PlayerOnField player) {
		LinearLayout item;
		LinearLayout.LayoutParams params;
		TextView playerName;
		TextView playerDorsal;
		item = (LinearLayout) inflater.inflate(
				R.layout.item_grid_carrusel_player_suplente, null);
		params = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		item.setLayoutParams(params);
		playerName = (TextView) item.findViewById(R.id.playerName);
		FontUtils.setCustomfont(mContext, playerName, FontTypes.HELVETICANEUE);
		playerName.setText(player.getName());

		playerDorsal = (TextView) item.findViewById(R.id.playerDorsal);
		FontUtils.setCustomfont(mContext, playerDorsal,
				FontTypes.HELVETICANEUEBOLD);
		playerDorsal.setText(String.valueOf(player.getDorsal()));

		ArrayList<Event> list = gameSystem.getEventos(player.getId());
		if (list != null && list.size() > 0) {
			boolean rojaFound = false;

			for (Event event : list) {

				if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_CAMBIO)) {
					item.findViewById(R.id.changeContent).setVisibility(
							View.VISIBLE);
					playerName.setTextColor(mContext.getResources().getColor(
							R.color.black));
					playerDorsal.setTextColor(mContext.getResources().getColor(
							R.color.black));

					TextView changeText = (TextView) item
							.findViewById(R.id.changeText);
					FontUtils.setCustomfont(mContext, changeText,
							FontTypes.HELVETICANEUEBOLD);
					changeText.setText(String.valueOf("- " + event.getTime()
							+ "'"));

				} else if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_GOL)) {
					item.findViewById(R.id.golImage)
							.setVisibility(View.VISIBLE);

				} else if (event.getTipo().equalsIgnoreCase(
						CarruselEventos.CARRUSEL_EVENTO_TARJETA_ROJA)) {
					item.findViewById(R.id.tarjetaImage).setVisibility(
							View.VISIBLE);
					item.findViewById(R.id.tarjetaImage).setBackgroundResource(
							R.drawable.plantilla_roja);
					rojaFound = true;
				} else if (!rojaFound
						&& event.getTipo()
								.equalsIgnoreCase(
										CarruselEventos.CARRUSEL_EVENTO_TARJETA_AMARILLA)) {
					item.findViewById(R.id.tarjetaImage).setVisibility(
							View.VISIBLE);
				}
			}
		}

		// item.setTag(player.getId());
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToPlayer(player.getId(), player.getUrl());

			}
		});
		return item;
	}

	/**************** Metodos de CarruselDAOPlantillaListener *****************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOPlantillaListener#
	 * onSuccessPlantilla()
	 */
	@Override
	public void onSuccessGameSystem(GameSystem gameSystem) {
		CarruselDAO.getInstance(mContext).removeGameSystemListener(this);
		this.gameSystem = gameSystem;
		stopAnimation();
		if (gameSystem != null)
			loadData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOPlantillaListener#
	 * onFailurePlantilla()
	 */
	@Override
	public void onFailureGameSystem() {
		CarruselDAO.getInstance(mContext).removeGameSystemListener(this);
		stopAnimation();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOPlantillaListener#
	 * onFailurePlantillaNotConnection()
	 */
	@Override
	public void onFailureGameSystemNotConnection() {
		CarruselDAO.getInstance(mContext).removeGameSystemListener(this);
		stopAnimation();

	}

}
