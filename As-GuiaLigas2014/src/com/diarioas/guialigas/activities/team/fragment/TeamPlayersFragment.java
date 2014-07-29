package com.diarioas.guialigas.activities.team.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.comparator.DorsalComparator;

public class TeamPlayersFragment extends TeamFragment {

	/**
	 * 
	 */
	private static final int NUM_VIEWS_ROW = 4;

	private LayoutInflater inflater;
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

		name.setText((String) getArguments().get("name"));

		FontUtils.setCustomfont(mContext, name,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		if (shield != null && !shield.equalsIgnoreCase(""))
			((ImageView) generalView.findViewById(R.id.photoTeam))
					.setBackgroundResource(mContext.getResources()
							.getIdentifier(
									shield.substring(0, shield.length() - 4),
									"drawable", mContext.getPackageName()));

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
			// generalView.findViewById(R.id.porteroGap).setVisibility(
			// View.VISIBLE);
			// generalView.findViewById(R.id.defensaGap).setVisibility(
			// View.VISIBLE);
			// generalView.findViewById(R.id.medioGap).setVisibility(View.VISIBLE);
			// porterosText.setVisibility(View.VISIBLE);
			// defensaText.setVisibility(View.VISIBLE);
			// medioText.setVisibility(View.VISIBLE);
			// delanteroText.setVisibility(View.VISIBLE);

			String dorsalPlayer, data;
			ArrayList<HashMap<String, String>> porteros = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> defensas = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> medios = new ArrayList<HashMap<String, String>>();
			ArrayList<HashMap<String, String>> delanteros = new ArrayList<HashMap<String, String>>();

			ArrayList<String> dorsalArray = new ArrayList<String>(
					plantilla.keySet());
			Collections.sort(dorsalArray, new DorsalComparator());

			HashMap<String, String> jugador;
			Bundle player;
			for (Iterator<String> iterator = dorsalArray.iterator(); iterator
					.hasNext();) {
				jugador = new HashMap<String, String>();
				dorsalPlayer = iterator.next();
				player = plantilla.getBundle(dorsalPlayer);
				for (Iterator<String> iterator2 = player.keySet().iterator(); iterator2
						.hasNext();) {
					data = iterator2.next();
					jugador.put(data, player.getString(data));

				}
				if (player.getString("demarcacion").equalsIgnoreCase("portero"))
					porteros.add(jugador);
				else if (player.getString("demarcacion").equalsIgnoreCase(
						"defensa"))
					defensas.add(jugador);
				else if (player.getString("demarcacion").equalsIgnoreCase(
						"centrocampista"))
					medios.add(jugador);
				else if (player.getString("demarcacion").equalsIgnoreCase(
						"delantero"))
					delanteros.add(jugador);

			}

			LinearLayout porteroTable = (LinearLayout) generalView
					.findViewById(R.id.porteroTable);
			LinearLayout defensaTable = (LinearLayout) generalView
					.findViewById(R.id.defensaTable);

			LinearLayout medioTable = (LinearLayout) generalView
					.findViewById(R.id.medioTable);
			LinearLayout delanteroTable = (LinearLayout) generalView
					.findViewById(R.id.delanteroTable);
			Point size = DimenUtils.getSize(getActivity().getWindowManager());
			int width = (size.x) / 4;

			setTableLinear(porteroTable, porteros, width);
			setTableLinear(defensaTable, defensas, width);
			setTableLinear(medioTable, medios, width);
			setTableLinear(delanteroTable, delanteros, width);
			// } else {
			// generalView.findViewById(R.id.porteroGap).setVisibility(View.GONE);
			// generalView.findViewById(R.id.defensaGap).setVisibility(View.GONE);
			// generalView.findViewById(R.id.medioGap).setVisibility(View.GONE);
			//
			// porterosText.setVisibility(View.GONE);
			// defensaText.setVisibility(View.INVISIBLE);
			// medioText.setVisibility(View.INVISIBLE);
			// delanteroText.setVisibility(View.GONE);
		}
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

			if (Integer.parseInt(players.get(i).get("dorsal")) >= 0)
				((TextView) item.findViewById(R.id.playerDorsal))
						.setText(players.get(i).get("dorsal"));

			url = players.get(i).get("photo");
			if (players.get(i).get("url") != null) {
				item.setTag(players.get(i).get("id"));
			} else {
				item.setTag(-1);
			}
			if (url != null) {
				ImageDAO.getInstance(getActivity()).loadSmallPlayerImage(url,
						((ImageView) item.findViewById(R.id.playerImage)),
						R.drawable.foto_plantilla_mask);
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
