/**
 * 
 */
package com.diarioas.guialigas.activities.carrusel.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.model.carrusel.StatsInfo;
import com.diarioas.guialigas.dao.reader.CarruselDAO;
import com.diarioas.guialigas.dao.reader.CarruselDAO.CarruselDAOStatsListener;
import com.diarioas.guialigas.utils.Defines.ReturnComparator;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

/**
 * @author robertosanchez
 * 
 */
public class CarruselStatsFragment extends CarruselFragment implements
		CarruselDAOStatsListener {

	private ArrayList<StatsInfo> stats;
	private StatsAdapter statsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_carrusel_stats,
				container, false);

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
		CarruselDAO.getInstance(mContext).removeStatsListener(this);
	}

	/**
	 * 
	 */
	@Override
	protected void configureView() {
		super.configureView();

		ListView statsContent = (ListView) generalView
				.findViewById(R.id.statsContent);
		statsContent.setDivider(null);
		statsContent.setClickable(false);
		statsContent.setCacheColorHint(0);
		statsAdapter = new StatsAdapter();
		statsContent.setAdapter(statsAdapter);

	}

	public void updateInfo() {

		if (url != null) {

			if (CarruselDAO.getInstance(mContext).isDetailUpdating(url)) {
				stats = (ArrayList<StatsInfo>) CarruselDAO
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
		CarruselDAO.getInstance(mContext).addStatsListener(this);
		CarruselDAO.getInstance(mContext).getStats(url);
	}

	/**
	 * 
	 */
	private void loadData() {
		statsAdapter.addItems(stats);
	}

	/**************** Metodos de CarruselDAOSpadesListener *****************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOSpadesListener#
	 * onSuccessSpades(es.prisacom.as.dao.model.calendar.Fase)
	 */
	@Override
	public void onSuccessStats(ArrayList<StatsInfo> stats) {
		CarruselDAO.getInstance(mContext).removeStatsListener(this);
		this.stats = stats;
		stopAnimation();
		loadData();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOSpadesListener#
	 * onFailureSpades()
	 */
	@Override
	public void onFailureStats() {
		CarruselDAO.getInstance(mContext).removeStatsListener(this);
		stopAnimation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOSpadesListener#
	 * onFailureNotConnection()
	 */
	@Override
	public void onFailureNotConnection() {
		CarruselDAO.getInstance(mContext).removeStatsListener(this);
		stopAnimation();
	}

	/********************************************************************************/
	/****************** ADAPTER *****************************************************/
	/********************************************************************************/

	class StatsAdapter extends BaseAdapter {

		private static final int ITEM_NORMAL_TYPE = 0;
		private static final int ITEM_POSESION_TYPE = ITEM_NORMAL_TYPE + 1;
		private static final int TOTAL_ITEMS_TYPE = ITEM_POSESION_TYPE + 1;
		private ArrayList<StatsInfo> stats;

		public StatsAdapter() {
			this.stats = new ArrayList<StatsInfo>();

		}

		@Override
		public int getViewTypeCount() {
			return TOTAL_ITEMS_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			StatsInfo item = stats.get(position);
			if (item.getTypo().equalsIgnoreCase(ReturnComparator.POSESION)) {
				return ITEM_POSESION_TYPE;
			} else {
				return ITEM_NORMAL_TYPE;
			}
		}

		public void addItems(ArrayList<StatsInfo> stats) {
			this.stats = stats;
			notifyDataSetChanged();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stats.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public StatsInfo getItem(int arg0) {
			// TODO Auto-generated method stub
			return stats.get(arg0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			StatsInfo statInfo = stats.get(position);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list_stats, null);

				FontUtils.setCustomfont(mContext,
						convertView.findViewById(R.id.textComparatorImage),
						FontTypes.ROBOTO_REGULAR);
				FontUtils.setCustomfont(mContext,
						convertView.findViewById(R.id.statsTextPL),
						FontTypes.HELVETICANEUELIGHT);
				FontUtils.setCustomfont(mContext,
						convertView.findViewById(R.id.statsTextPR),
						FontTypes.HELVETICANEUELIGHT);

			}

			ImageView comparatorImage = (ImageView) convertView
					.findViewById(R.id.comparatorImage);
			int icon = getIcon(statInfo.getTypo());
			comparatorImage.setBackgroundResource(icon);
			int playerLeft = statInfo.getLocal();
			int playerRight = statInfo.getAway();
			int total = playerLeft + playerRight;

			TextView textComparatorImage = (TextView) convertView
					.findViewById(R.id.textComparatorImage);
			textComparatorImage.setText(statInfo.getTypo());

			TextView statsTextPL = ((TextView) convertView
					.findViewById(R.id.statsTextPL));
			TextView statsTextPR = ((TextView) convertView
					.findViewById(R.id.statsTextPR));

			int type = getItemViewType(position);
			if (type == ITEM_POSESION_TYPE) {
				statsTextPL.setText(String.valueOf(playerLeft) + " %");
				statsTextPR.setText(String.valueOf(playerRight) + " %");
			} else {
				statsTextPL.setText(String.valueOf(playerLeft));
				statsTextPR.setText(String.valueOf(playerRight));
			}

			View stasOffPL = convertView.findViewById(R.id.stasOffPL);
			LinearLayout.LayoutParams pOffPL = (LinearLayout.LayoutParams) stasOffPL
					.getLayoutParams();
			View stasOnPL = convertView.findViewById(R.id.stasOnPL);
			LinearLayout.LayoutParams pOnPL = (LinearLayout.LayoutParams) stasOnPL
					.getLayoutParams();

			View stasOffPR = convertView.findViewById(R.id.stasOffPR);
			LinearLayout.LayoutParams pOffPR = (LinearLayout.LayoutParams) stasOffPR
					.getLayoutParams();
			View stasOnPR = convertView.findViewById(R.id.stasOnPR);
			LinearLayout.LayoutParams pOnPR = (LinearLayout.LayoutParams) stasOnPR
					.getLayoutParams();

			if (total > 0) {
				pOffPL.weight = (total - playerLeft) * 100 / total;
				pOnPL.weight = playerLeft * 100 / total;

				pOffPR.weight = (total - playerRight) * 100 / total;
				pOnPR.weight = playerRight * 100 / total;
			} else {
				pOffPL.weight = 100;
				pOnPL.weight = 0;
				pOffPR.weight = 100;
				pOnPR.weight = 0;
			}

			stasOffPL.setLayoutParams(pOffPL);
			stasOnPL.setLayoutParams(pOnPL);

			stasOnPR.setLayoutParams(pOnPR);
			stasOffPR.setLayoutParams(pOffPR);

			return convertView;
		}

		/**
		 * @param typo
		 * @return
		 */
		private int getIcon(String typo) {
			if (typo.equalsIgnoreCase(ReturnComparator.POSESION)) {
				return R.drawable.icn_posesion;
			} else if (typo.equalsIgnoreCase(ReturnComparator.REMATES_TOTALES)) {
				return R.drawable.icn_balonespuerta;
			} else if (typo.equalsIgnoreCase(ReturnComparator.REMATES_FUERA)) {
				return R.drawable.icn_balonesfuera;
			} else if (typo.equalsIgnoreCase(ReturnComparator.REMATES_POSTE)) {
				return R.drawable.icn_balonesposte;
			} else if (typo.equalsIgnoreCase(ReturnComparator.REMATES_PORTERIA)) {
				return R.drawable.icn_balonespuerta;
			} else if (typo.equalsIgnoreCase(ReturnComparator.REMATES_OTROS)) {
				return R.drawable.icn_balonespuerta;
			} else if (typo.equalsIgnoreCase(ReturnComparator.ASISTENCIAS)) {
				return R.drawable.icn_asistencias;
			} else if (typo.equalsIgnoreCase(ReturnComparator.PARADAS)) {
				return R.drawable.icn_paradas;
			} else if (typo.equalsIgnoreCase(ReturnComparator.GOLES)) {
				return R.drawable.icn_goles;
			} else if (typo.equalsIgnoreCase(ReturnComparator.TARJ_ROJAS)) {
				return R.drawable.icn_rojas;
			} else if (typo.equalsIgnoreCase(ReturnComparator.TARJ_AMAR)) {
				return R.drawable.icn_amarillas;
			} else if (typo.equalsIgnoreCase(ReturnComparator.FALTAS_RECIBIDAS)) {
				return R.drawable.icn_faltasrecibidas;
			} else if (typo.equalsIgnoreCase(ReturnComparator.FALTAS_COMETIDAS)) {
				return R.drawable.icn_faltascometidas;
			} else if (typo.equalsIgnoreCase(ReturnComparator.BALONES_PERDIDOS)) {
				return R.drawable.icn_balonesperdidos;
			} else if (typo
					.equalsIgnoreCase(ReturnComparator.BALONES_RECUPERADOS)) {
				return R.drawable.icn_balonesrecuperados;
			} else if (typo.equalsIgnoreCase(ReturnComparator.FUERA_JUEGO)) {
				return R.drawable.icn_fdejuego;
			} else if (typo.equalsIgnoreCase(ReturnComparator.PENALTIES)) {
				return R.drawable.icn_penalties02;
			} else if (typo.equalsIgnoreCase(ReturnComparator.INTERV_PORTERO)) {
				return R.drawable.icn_intervenciones;
			} else {
				return R.drawable.icn_goles;
			}

		}

	}

}
