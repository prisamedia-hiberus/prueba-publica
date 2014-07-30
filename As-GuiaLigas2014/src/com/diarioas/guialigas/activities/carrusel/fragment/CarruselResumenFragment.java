/**
 * 
 */
package com.diarioas.guialigas.activities.carrusel.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.model.calendar.Match;
import com.diarioas.guialigas.dao.model.carrusel.Gol;
import com.diarioas.guialigas.dao.model.carrusel.Tarjeta;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

/**
 * @author robertosanchez
 * 
 */
public class CarruselResumenFragment extends CarruselFragment {

	private Match match;
	private int stateCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflating layout
		this.inflater = inflater;
		match = getArguments().getParcelable("match");

		stateCode = match.getStateCode();
		if (stateCode == 0) {
			generalView = inflater
					.inflate(R.layout.fragment_carrusel_resumen_before,
							container, false);
			configureViewBeforStart();
		} else {
			generalView = inflater.inflate(
					R.layout.fragment_carrusel_resumen_after, container, false);
			configureViewAfterStart();
		}

		configureView();

		return generalView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.activities.carrusel.fragment.CarruselFragment#updateData()
	 */
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		super.updateData();
		match = getArguments().getParcelable("match");
		if (match.getStateCode() != stateCode) {
			stateCode = match.getStateCode();
			if (stateCode == 0) {
				generalView = inflater.inflate(
						R.layout.fragment_carrusel_resumen_before, null, false);
				configureViewBeforStart();
			} else {
				generalView = inflater.inflate(
						R.layout.fragment_carrusel_resumen_after, null, false);
				configureViewAfterStart();
			}
		} else {
			if (stateCode == 0) {
				configureViewBefore();
			} else {
				configureViewAfter();
			}
		}
		configureView();
	}

	private void configureViewBefore() {
		if (generalView == null) {
			generalView = inflater.inflate(
					R.layout.fragment_carrusel_resumen_before, null, false);
		}
		configureViewBeforStart();

	}

	private void configureViewAfter() {
		if (generalView == null) {
			generalView = inflater.inflate(
					R.layout.fragment_carrusel_resumen_after, null, false);

		}
		configureViewAfterStart();

	}

	/**
	 * 
	 */
	private void configureViewBeforStart() {
		final String urlPrevia = match.getCronica();
		Button prevMatchButton = (Button) generalView
				.findViewById(R.id.prevMatchButton);
		if (urlPrevia != null && !urlPrevia.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, prevMatchButton,
					FontTypes.HELVETICANEUE);
			prevMatchButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToWeb(urlPrevia);

				}
			});
			prevMatchButton.setVisibility(View.VISIBLE);
		} else {
			prevMatchButton.setVisibility(View.GONE);
		}
		
		configureIcon();

	}

	private void configureIcon() {
		ImageView title_menuIcon = (ImageView)generalView.findViewById(R.id.title_menuIcon);
		
		int iconId = DrawableUtils.getDrawableId(mContext, RemoteDataDAO.getInstance(mContext).getGeneralSettings().getCurrentCompetition().getImage().toLowerCase(),4);
		title_menuIcon.setImageDrawable(getResources().getDrawable(iconId));
	}

	/**
	 * 
	 */
	private void configureViewAfterStart() {
		final String urlCronica = match.getCronica();
		Button postMatchButton = (Button) generalView
				.findViewById(R.id.postMatchButton);
		if (urlCronica != null && !urlCronica.equalsIgnoreCase("")) {
			FontUtils.setCustomfont(mContext, postMatchButton,
					FontTypes.HELVETICANEUE);
			postMatchButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToWeb(urlCronica);

				}
			});
			postMatchButton.setVisibility(View.VISIBLE);
		} else {
			postMatchButton.setVisibility(View.GONE);
		}
		
		configureIcon();

		FontUtils
				.setCustomfont(mContext,
						generalView.findViewById(R.id.golText),
						FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.tarjetaAmText),
				FontTypes.HELVETICANEUE);
		FontUtils.setCustomfont(mContext,
				generalView.findViewById(R.id.tarjetaRoText),
				FontTypes.HELVETICANEUE);

		LinearLayout golesContainer = (LinearLayout) generalView
				.findViewById(R.id.golesContainer2);
		golesContainer.removeAllViews();
		LinearLayout tarjetasAmarillasContainer = (LinearLayout) generalView
				.findViewById(R.id.tarjetasAmarillasContainer2);
		tarjetasAmarillasContainer.removeAllViews();
		LinearLayout tarjetasRojasContainer = (LinearLayout) generalView
				.findViewById(R.id.tarjetasRojasContainer2);
		tarjetasRojasContainer.removeAllViews();

		LinearLayout rel;
		TextView text;

		if (match.getGoles().size() > 0) {
			for (Gol gol : match.getGoles()) {
				if (gol.getTeamSide().equalsIgnoreCase("local"))
					rel = (LinearLayout) inflater.inflate(
							R.layout.item_list_carrusel_resumen_left, null);
				else
					rel = (LinearLayout) inflater.inflate(
							R.layout.item_list_carrusel_resumen_right, null);
				text = (TextView) rel.findViewById(R.id.text);
				FontUtils
						.setCustomfont(mContext, text, FontTypes.HELVETICANEUE);
				text.setText(gol.getMin() + "'" + gol.getPlayer());
				golesContainer.addView(rel);
			}
			generalView.findViewById(R.id.golesContainer).setVisibility(
					View.VISIBLE);
		} else {
			generalView.findViewById(R.id.golesContainer).setVisibility(
					View.GONE);
		}
		if (match.getTarjetasAmarillas().size() > 0) {
			for (Tarjeta tarjeta : match.getTarjetasAmarillas()) {
				if (tarjeta.getTeamSide() != null
						&& tarjeta.getTeamSide().equalsIgnoreCase("local"))
					rel = (LinearLayout) inflater.inflate(
							R.layout.item_list_carrusel_resumen_left, null);
				else
					rel = (LinearLayout) inflater.inflate(
							R.layout.item_list_carrusel_resumen_right, null);
				text = (TextView) rel.findViewById(R.id.text);
				FontUtils
						.setCustomfont(mContext, text, FontTypes.HELVETICANEUE);
				text.setText(tarjeta.getMin() + "'" + tarjeta.getPlayer());
				tarjetasAmarillasContainer.addView(rel);
			}
			generalView.findViewById(R.id.tarjetasAmarillasContainer)
					.setVisibility(View.VISIBLE);
		} else {
			generalView.findViewById(R.id.tarjetasAmarillasContainer)
					.setVisibility(View.GONE);
		}
		if (match.getTarjetasRojas().size() > 0) {
			for (Tarjeta tarjeta : match.getTarjetasRojas()) {
				if (tarjeta.getTeamSide() != null
						&& tarjeta.getTeamSide().equalsIgnoreCase("local"))
					rel = (LinearLayout) inflater.inflate(
							R.layout.item_list_carrusel_resumen_left, null);
				else
					rel = (LinearLayout) inflater.inflate(
							R.layout.item_list_carrusel_resumen_right, null);
				text = (TextView) rel.findViewById(R.id.text);
				FontUtils
						.setCustomfont(mContext, text, FontTypes.HELVETICANEUE);
				text.setText(tarjeta.getMin() + "'" + tarjeta.getPlayer());
				tarjetasRojasContainer.addView(rel);
			}
			generalView.findViewById(R.id.tarjetasRojasContainer)
					.setVisibility(View.VISIBLE);
		} else {
			generalView.findViewById(R.id.tarjetasRojasContainer)
					.setVisibility(View.GONE);
		}

	}

	private void goToWeb(String url) {
		if (url != null && !url.equalsIgnoreCase("")) {
			if (url.contains("http") == false) {
				url = "http://" + url;
			}
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			getActivity().startActivityForResult(intent,
					ReturnRequestCodes.PUBLI_BACK);
		}

	}

	/**
	 * 
	 */
	@Override
	protected void configureView() {

		super.configureView();

		TextView localTeamName = (TextView) generalView
				.findViewById(R.id.localTeamName);
		FontUtils.setCustomfont(mContext, localTeamName,
				FontTypes.HELVETICANEUE);
		localTeamName.setText(match.getLocalTeamName());

		ImageView localShield = (ImageView) generalView
				.findViewById(R.id.localShield);
		String localShieldURL = DatabaseDAO.getInstance(mContext)
				.getTeamShield(match.getLocalId());
		localShield.setBackgroundResource(DrawableUtils.getDrawableId(mContext,localShieldURL,4));

		View localContent = generalView.findViewById(R.id.localContent);
		localContent.setTag(match.getLocalId());
		localContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToTeam(String.valueOf(v.getTag()));
			}
		});

		TextView awayTeamName = (TextView) generalView
				.findViewById(R.id.awayTeamName);
		FontUtils
				.setCustomfont(mContext, awayTeamName, FontTypes.HELVETICANEUE);
		awayTeamName.setText(match.getAwayTeamName());
		ImageView awayShield = (ImageView) generalView
				.findViewById(R.id.awayShield);
		String awayShieldURL = DatabaseDAO.getInstance(mContext).getTeamShield(
				match.getAwayId());
		awayShield.setBackgroundResource(DrawableUtils.getDrawableId(mContext,awayShieldURL,4));

		View awayContent = generalView.findViewById(R.id.awayContent);
		awayContent.setTag(match.getAwayId());
		awayContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToTeam(String.valueOf(v.getTag()));

			}
		});

		SimpleDateFormat sdformat = new SimpleDateFormat(
				DateFormat.CARRUSEL_FORMAT, Locale.getDefault());
		TextView dateText = (TextView) generalView.findViewById(R.id.dateText);
		FontUtils
				.setCustomfont(mContext, dateText, FontTypes.HELVETICANEUEBOLD);
		if (match.getDate() != null) {
			Date date = new Date(Long.valueOf(match.getDate()) * 1000);
			String dateString = sdformat.format(date) + " h.";
			dateString = dateString.substring(0, 1).toUpperCase()
					+ dateString.substring(1);
			dateText.setText(dateString);
		}

		// TVs
		paintTVs(match,
				(RelativeLayout) generalView.findViewById(R.id.tvContainerAll));

		TextView jornadaName = (TextView) generalView
				.findViewById(R.id.jornadaName);
		FontUtils.setCustomfont(mContext, jornadaName, FontTypes.HELVETICANEUE);
		jornadaName.setText(getArguments().getString("jornadaName"));

		TextView placeName = (TextView) generalView
				.findViewById(R.id.placeName);
		FontUtils.setCustomfont(mContext, placeName, FontTypes.HELVETICANEUE);
		placeName.setText(match.getPlace());

		TextView refereeName = (TextView) generalView
				.findViewById(R.id.refereeName);
		FontUtils.setCustomfont(mContext, refereeName, FontTypes.HELVETICANEUE);
		refereeName.setText(match.getReferee());

	}

	public void goToTeam(String tag) {
		// Intent intent = new Intent(getActivity().getApplicationContext(),
		// TeamActivity.class);
		// intent.putExtra("teamId", tag);
		// startActivityForResult((intent, ReturnRequestCodes.PUBLI_BACK);
		// getActivity().overridePendingTransition(R.anim.grow_from_middle,
		// R.anim.shrink_to_middle);
	}

	protected void paintTVs(Match currentMatch, RelativeLayout tvContainerAll) {
		tvContainerAll.removeAllViews();
		if (currentMatch.getTelevisiones().size() > 0) {

			int margin = DimenUtils.getRegularPixelFromDp(mContext, 2);

			LinearLayout tvContainer = new LinearLayout(mContext);
			tvContainer.setId(550);
			tvContainer.setPadding(margin, margin, margin, margin);
			tvContainer.setOrientation(LinearLayout.HORIZONTAL);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			tvContainer.setLayoutParams(params);

			int i = 0;
			int id = 0;
			boolean iconFound = false;
			LinearLayout.LayoutParams paramsCell;
			RelativeLayout.LayoutParams paramsIcon;
			RelativeLayout rel;
			ImageView tvIcon;
			for (String key : currentMatch.getTelevisiones()) {

				id = DrawableUtils.getDrawableId(mContext,key,4);
				if (id != 0) {
					tvIcon = new ImageView(mContext);
					rel = new RelativeLayout(mContext);
					iconFound = true;
					tvIcon.setBackgroundResource(id);
					paramsIcon = new RelativeLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					paramsIcon.addRule(RelativeLayout.CENTER_IN_PARENT);
					tvIcon.setLayoutParams(paramsIcon);
					rel.addView(tvIcon);
					paramsCell = new LinearLayout.LayoutParams(0,
							LayoutParams.WRAP_CONTENT);
					paramsCell.weight = 1;
					paramsCell.leftMargin = margin;
					paramsCell.rightMargin = margin;
					tvContainer.addView(rel);
					i++;
					if (i >= Defines.NUM_MAX_TVS) {
						tvContainerAll.addView(tvContainer);
						id = tvContainer.getId();

						tvContainer = new LinearLayout(mContext);
						tvContainer.setId(id + 5);
						tvContainer.setPadding(margin, margin, margin, margin);
						tvContainer.setOrientation(LinearLayout.HORIZONTAL);
						params = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.CENTER_HORIZONTAL);
						params.addRule(RelativeLayout.BELOW, id);
						tvContainer.setLayoutParams(params);
						i = 0;
					}

				}
			}
			if (iconFound) {
				tvContainerAll.addView(tvContainer);
				tvContainerAll.setVisibility(View.VISIBLE);
			} else {
				tvContainerAll.setVisibility(View.GONE);
				generalView.findViewById(R.id.gapBarTvs).setVisibility(
						View.GONE);
			}
		} else {
			tvContainerAll.setVisibility(View.GONE);
			generalView.findViewById(R.id.gapBarTvs).setVisibility(View.GONE);
		}
	}


}
