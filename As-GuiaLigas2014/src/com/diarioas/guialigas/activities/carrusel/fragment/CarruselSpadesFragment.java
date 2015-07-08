/**
 * 
 */
package com.diarioas.guialigas.activities.carrusel.fragment;

import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.carrusel.CarruselDetailActivity;
import com.diarioas.guialigas.dao.model.carrusel.Spade;
import com.diarioas.guialigas.dao.model.carrusel.SpadeInfo;
import com.diarioas.guialigas.dao.reader.CarruselDAO;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.CarruselDAO.CarruselDAOSpadesListener;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

/**
 * @author robertosanchez
 * 
 */
public class CarruselSpadesFragment extends CarruselFragment implements
		CarruselDAOSpadesListener {

	private SpadeInfo spadeInfo;

	private int idShieldLocal;
	private int idShieldAway;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_carrusel_picas,
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
		CarruselDAO.getInstance(mContext).removeSpadesListener(this);
	}

	public void updateInfo() {
		if (url != null) {

			if (CarruselDAO.getInstance(mContext).isDetailUpdating(url)) {
				spadeInfo = (SpadeInfo) CarruselDAO.getInstance(mContext)
						.getDetailCached(url);
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
		CarruselDAO.getInstance(mContext).addSpadesListener(this);
		CarruselDAO.getInstance(mContext).getSpades(url);
	}

	/**
	 * 
	 */
	private void loadData() {
		if (spadeInfo != null) {
			LinearLayout itemsLayout = (LinearLayout) generalView
					.findViewById(R.id.itemsLayout);
			itemsLayout.removeAllViews();

			Set<Integer> keySet;

			itemsLayout.addView(getContainerGroup(spadeInfo.getLocal(),
					idShieldLocal));
			keySet = spadeInfo.getLocalSpades().keySet();
			for (Iterator<Integer> iterator = keySet.iterator(); iterator
					.hasNext();) {
				itemsLayout.addView(getItem(
						spadeInfo.getLocalSpades().get(iterator.next()),
						spadeInfo.getLocal()));
			}

			itemsLayout.addView(getContainerGroup(spadeInfo.getAway(),
					idShieldAway));
			keySet = spadeInfo.getAwaySpades().keySet();
			for (Iterator<Integer> iterator = keySet.iterator(); iterator
					.hasNext();) {
				itemsLayout.addView(getItem(
						spadeInfo.getAwaySpades().get(iterator.next()),
						spadeInfo.getAway()));
			}
		}
	}

	/**
	 * @param spade
	 * @param nameTeam
	 * @return
	 */
	private View getItem(final Spade spade, String nameTeam) {
		View item = inflater.inflate(R.layout.item_list_spades, null);
		// item.setTag(spade.getPlayerId());
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToPlayer(spade.getPlayerId(), spade.getUrl());
			}
		});

		ImageDAO.getInstance(mContext).loadPlayerTeamDetailImage(
				spade.getUrlPhoto(), R.drawable.foto_plantilla_generica,
				(ImageView) item.findViewById(R.id.playerImage),
				R.drawable.foto_plantilla_mask);

		TextView dorsalText = (TextView) item.findViewById(R.id.dorsalText);
		FontUtils.setCustomfont(mContext, dorsalText,
				FontTypes.ROBOTO_BOLD);
		dorsalText.setText(spade.getDorsal() + ".");

		TextView titleText = (TextView) item.findViewById(R.id.titleText);
		FontUtils.setCustomfont(mContext, titleText, FontTypes.ROBOTO_REGULAR);
		titleText.setText(spade.getPlayer());

		TextView teamText = (TextView) item.findViewById(R.id.teamText);
		FontUtils.setCustomfont(mContext, teamText, FontTypes.ROBOTO_REGULAR);
		teamText.setText(nameTeam);

		LinearLayout spadesContent = (LinearLayout) item
				.findViewById(R.id.spadesContent);
		ImageView image;
		LinearLayout.LayoutParams params;
		String value = DatabaseDAO.getInstance(mContext).getSpades(
				spade.getPunt());
		TextView spadesText = (TextView) item.findViewById(R.id.spadesText);
		try {
			int valueInt = Integer.valueOf(value);
			for (int i = 0; i < valueInt; i++) {

				image = new ImageView(mContext);
				image.setBackgroundResource(R.drawable.icn_picas);

				params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
				params.rightMargin = 3;
				image.setLayoutParams(params);
				spadesContent.addView(image);

				spadesText.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			spadesText.setText(value);
			FontUtils.setCustomfont(mContext, spadesText,
					FontTypes.ROBOTO_REGULAR);
			spadesText.setVisibility(View.VISIBLE);
		}

		return item;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private View getContainerGroup(String name, int idImage) {
		View header = inflater.inflate(R.layout.item_list_spades_header, null);
		TextView nameTeam = (TextView) header.findViewById(R.id.nameTeam);
		FontUtils.setCustomfont(mContext, nameTeam, FontTypes.ROBOTO_REGULAR);
		nameTeam.setText(name);

		ImageView photoTeam = (ImageView) header.findViewById(R.id.photoTeam);
		photoTeam.setBackgroundResource(idImage);

		return header;
	}

	/**************** Metodos de CarruselDAOSpadesListener *****************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CarruselDAO.CarruselDAOSpadesListener#
	 * onSuccessSpades(es.prisacom.as.dao.model.calendar.Fase)
	 */
	@Override
	public void onSuccessSpades(SpadeInfo spadeInfo) {
		CarruselDAO.getInstance(mContext).removeSpadesListener(this);
		this.spadeInfo = spadeInfo;
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
	public void onFailureSpades() {
		CarruselDAO.getInstance(mContext).removeSpadesListener(this);
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
		CarruselDAO.getInstance(mContext).removeSpadesListener(this);
		stopAnimation();
	}

}
