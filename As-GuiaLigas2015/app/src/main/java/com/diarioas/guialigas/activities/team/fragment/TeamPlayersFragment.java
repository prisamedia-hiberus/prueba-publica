package com.diarioas.guialigas.activities.team.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.player.comparator.PlayerComparatorStepThirdActivity;
import com.diarioas.guialigas.activities.team.TeamActivity;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.Defines.Demarcation;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.comparator.DorsalComparator;

public class TeamPlayersFragment extends TeamFragment {

	private static final int NUM_VIEWS_ROW = 4;

	private Bundle plantilla;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_players,
				container, false);
		return generalView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		inflater = null;

	}

	@Override
	protected void configureView() {

		/************************** Info General ******************************************/
		String shield = (String) getArguments().get("shield");
		TextView name = (TextView) generalView.findViewById(R.id.nameTeam);

		String teamName = (String) getArguments().get("name");
		name.setText(teamName);

		FontUtils.setCustomfont(mContext, name,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		if (shield != null && !shield.equalsIgnoreCase("")) {
			int shieldResourceInt = DrawableUtils.getDrawableId(
					mContext, shield, 4);
			if (shieldResourceInt<=0) {
				shieldResourceInt=R.drawable.escudo_generico_size03;
			}
			((ImageView) generalView.findViewById(R.id.photoTeam))
					.setBackgroundResource(shieldResourceInt);
		} else {
			((ImageView) generalView.findViewById(R.id.photoTeam))
			.setBackgroundResource(R.drawable.escudo_generico_size03);
		}

		/************************** Info Plantilla ******************************************/
		TextView porterosText = (TextView) generalView
				.findViewById(R.id.porterosText);
		TextView defensaText = (TextView) generalView
				.findViewById(R.id.defensaText);
		TextView medioText = (TextView) generalView
				.findViewById(R.id.medioText);
		TextView delanteroText = (TextView) generalView
				.findViewById(R.id.delanteroText);

		FontUtils.setCustomfont(mContext, porterosText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		FontUtils.setCustomfont(mContext, defensaText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		FontUtils.setCustomfont(mContext, medioText,
				FontUtils.FontTypes.ROBOTO_LIGHT);
		FontUtils.setCustomfont(mContext, delanteroText,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		plantilla = getArguments().getBundle("plantilla");

		if (plantilla.keySet().size() > 0) {

			String shortNamePlayer, data;
			ArrayList<HashMap<String, String>> porteros = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> defensas = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> medios = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> delanteros = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> generic = new ArrayList<HashMap<String, String>>();

			ArrayList<String> dorsalArray = new ArrayList<String>(
					plantilla.keySet());
			// if (plantilla.getBundle(dorsalArray.get(0)).getString(
			// Demarcation.DEMARCATION) == null) {
			//
			// } else {
			Collections.sort(dorsalArray, new DorsalComparator());
			// }

			HashMap<String, String> jugador;
			Bundle player;
			for (Iterator<String> iterator = dorsalArray.iterator(); iterator
					.hasNext();) {
				jugador = new HashMap<String, String>();
				shortNamePlayer = iterator.next();
				player = plantilla.getBundle(shortNamePlayer);
				for (Iterator<String> iterator2 = player.keySet().iterator(); iterator2
						.hasNext();) {
					data = iterator2.next();
					jugador.put(data, player.getString(data));

				}
				if (player.getString(Demarcation.DEMARCATION) == null
						|| player.getString(Demarcation.DEMARCATION)
								.equalsIgnoreCase(""))
					generic.add(jugador);
				else if (player.getString(Demarcation.DEMARCATION)
						.equalsIgnoreCase(Demarcation.GOALKEEPER))
					porteros.add(jugador);
				else if (player.getString(Demarcation.DEMARCATION)
						.equalsIgnoreCase(Demarcation.DEFENDER))
					defensas.add(jugador);
				else if (player.getString(Demarcation.DEMARCATION)
						.equalsIgnoreCase(Demarcation.MIDFIELD))
					medios.add(jugador);
				else if (player.getString(Demarcation.DEMARCATION)
						.equalsIgnoreCase(Demarcation.SCORER))
					delanteros.add(jugador);
				else
					generic.add(jugador);

			}

			LinearLayout porteroTable = (LinearLayout) generalView
					.findViewById(R.id.porteroTable);
			LinearLayout defensaTable = (LinearLayout) generalView
					.findViewById(R.id.defensaTable);

			LinearLayout medioTable = (LinearLayout) generalView
					.findViewById(R.id.medioTable);
			LinearLayout delanteroTable = (LinearLayout) generalView
					.findViewById(R.id.delanteroTable);
			LinearLayout genericTable = (LinearLayout) generalView
					.findViewById(R.id.genericTable);
			Point size = DimenUtils.getSize(getActivity().getWindowManager());
			int width = (size.x) / 4;

			if (porteros.size() > 0) {
				setTableLinear(porteroTable, porteros, width);
				porteroTable.setVisibility(View.VISIBLE);
			} else {
				porteroTable.setVisibility(View.GONE);
				porterosText.setVisibility(View.GONE);
				generalView.findViewById(R.id.porteroGap).setVisibility(
						View.GONE);
			}

			if (defensas.size() > 0) {
				setTableLinear(defensaTable, defensas, width);
				defensaTable.setVisibility(View.VISIBLE);
			} else {
				defensaTable.setVisibility(View.GONE);
				defensaText.setVisibility(View.GONE);
				generalView.findViewById(R.id.defensaGap).setVisibility(
						View.GONE);
			}

			if (medios.size() > 0) {
				setTableLinear(medioTable, medios, width);
				medioTable.setVisibility(View.VISIBLE);
			} else {
				medioTable.setVisibility(View.GONE);
				medioText.setVisibility(View.GONE);
				generalView.findViewById(R.id.medioGap)
						.setVisibility(View.GONE);
			}

			if (delanteros.size() > 0) {
				setTableLinear(delanteroTable, delanteros, width);
				delanteroTable.setVisibility(View.VISIBLE);
			} else {
				delanteroTable.setVisibility(View.GONE);
				delanteroText.setVisibility(View.GONE);
			}

			if (generic.size() > 0) {
				setTableLinear(genericTable, generic, width);
				genericTable.setVisibility(View.VISIBLE);
			} else {
				genericTable.setVisibility(View.GONE);
			}

		}
	}

	@Override
	protected void startFirstShow() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param ll
	 * @param players
	 * @param width
	 */
	private void setTableLinear(LinearLayout ll,
			ArrayList<HashMap<String, String>> players, int width) {

		RelativeLayout item;
		// create a new TableRow
		String url;
		LinearLayout linear = new LinearLayout(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams paramsItem = new LinearLayout.LayoutParams(
				width, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		for (int i = 0; i < players.size(); i++) {
			item = (RelativeLayout) inflater.inflate(R.layout.item_grid_player,
					null);

			((TextView) item.findViewById(R.id.playerName)).setText(players
					.get(i).get("shortName"));

			if (players.get(i).get("dorsal") != null
					&& Integer.parseInt(players.get(i).get("dorsal")) > 0)
				((TextView) item.findViewById(R.id.playerDorsal))
						.setText(players.get(i).get("dorsal"));
			else
				item.findViewById(R.id.dorsalContent).setVisibility(View.GONE);

			url = players.get(i).get("photo");
			if (players.get(i).get("url") != null) {
				item.setTag(players.get(i).get("id"));
			} else {
				item.setTag("-1");
			}
			if (url != null && url.length() > 0) {
				FragmentActivity act = getActivity();
				if (getActivity() instanceof TeamActivity) {

					ImageDAO.getInstance(mContext).loadRegularImage(url,
							(ImageView) item.findViewById(R.id.playerImage),
							R.drawable.foto_plantilla_generica, R.drawable.foto_plantilla_generica, true);
					
				} else {

					ImageDAO.getInstance(mContext).loadRegularImage(url,
							(ImageView) item.findViewById(R.id.playerImage),
							R.drawable.foto_plantilla_generica,R.drawable.foto_plantilla_generica, true);
				}

			}

			if (!(players.get(i).get("url") != null && players.get(i)
					.get("url").length() > 0)) {
				AlphaAnimation alpha = new AlphaAnimation(1, 0.5F);
				alpha.setDuration(0); // Make animation instant
				alpha.setFillAfter(true); // Tell it to persist after the
											// animation
											// ends
				item.startAnimation(alpha);

			}

			linear.addView(item, paramsItem);
			if ((i + 1) % NUM_VIEWS_ROW == 0) {
				ll.addView(linear);
				linear = new LinearLayout(mContext);
			}

		}
		int mod;
		if (players.size() < NUM_VIEWS_ROW)
			mod = NUM_VIEWS_ROW - players.size();
		else
			mod = NUM_VIEWS_ROW - (players.size() % NUM_VIEWS_ROW);

		if (mod > 0) {
			for (int j = 0; j < mod; j++) {
				linear.addView(new View(mContext), paramsItem);
			}
		}

		// add the TableRow to the TableLayout
		ll.addView(linear, params);
	}

}
