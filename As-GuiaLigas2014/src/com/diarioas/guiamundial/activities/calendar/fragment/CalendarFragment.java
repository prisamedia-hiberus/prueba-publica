package com.diarioas.guiamundial.activities.calendar.fragment;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.carrusel.CarruselDetailActivity;
import com.diarioas.guiamundial.dao.model.calendar.Match;
import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.dao.reader.CalendarDAO;
import com.diarioas.guiamundial.dao.reader.CalendarDAO.CalendarDayDAOListener;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.utils.Defines;
import com.diarioas.guiamundial.utils.Defines.DateFormat;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.DrawableUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;

public abstract class CalendarFragment extends Fragment implements
		CalendarDayDAOListener {

	protected static final int FOOTER_ID = 10530;
	/**
	 * 
	 */

	protected View generalView;
	protected int competitionId;
	protected boolean faseActiva;
	protected SimpleDateFormat dateFormatPull;
	protected SimpleDateFormat formatterDate;
	protected SimpleDateFormat formatterHour;
	protected LayoutInflater inflater;
	// protected String urlImagePrefix;
	protected HashMap<String, Team> teams;
	protected double scale;

	// private String urlDataPrefix;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		competitionId = getArguments().getInt("competitionId");
		faseActiva = getArguments().getBoolean("faseActiva");
		formatterDate = new SimpleDateFormat(DateFormat.DAY_FORMAT,
				Locale.getDefault());
		formatterHour = new SimpleDateFormat(DateFormat.HOUR_FORMAT,
				Locale.getDefault());

		dateFormatPull = new SimpleDateFormat(DateFormat.PULL_FORMAT,
				Locale.getDefault());

		teams = DatabaseDAO.getInstance(getActivity().getApplicationContext())
				.getCalendarInfoTeams(competitionId);

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		scale = (double) size.x / size.y;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configureView();
	}

	protected abstract void configureView();

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (generalView != null) {
			generalView = null;
		}

		if (teams != null) {
			teams.clear();
			teams = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();

	}

	/**
	 * 
	 */
	protected void reUpdateCarrusel() {
		CalendarDAO.getInstance(getActivity().getApplicationContext())
				.addDayListener(this);
		CalendarDAO.getInstance(getActivity().getApplicationContext())
				.refreshCalendarDay(getArguments().getString("urlCalendar"),
						competitionId + "_calendar.json",
						getArguments().getInt("faseId"),
						getArguments().getInt("jornadaId"));
		// CalendarDAO.getInstance(getActivity().getApplicationContext()).refreshCalendarDay(getArguments().getString("urlCalendar"),competitionId
		// + "_calendar.json", day.getNumDay() - 1);

	}

	protected View getFooter() {
		RelativeLayout rel = new RelativeLayout(getActivity()
				.getApplicationContext());
		rel.setId(FOOTER_ID);

		int dim15 = DimenUtils.getRegularPixelFromDp(getActivity()
				.getApplicationContext(), 15);

		ImageView image = new ImageView(getActivity().getApplicationContext());
		image.setId(200);
		image.setBackgroundResource(R.drawable.icon_jornadanoactiva);

		RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsImage.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsImage.topMargin = dim15;
		paramsImage.bottomMargin = dim15;
		image.setLayoutParams(paramsImage);

		TextView messageText = new TextView(getActivity()
				.getApplicationContext());
		messageText.setText(getActivity().getString(
				R.string.calendar_day_not_active));
		messageText.setTextColor(getActivity().getResources().getColor(
				R.color.black));

		messageText.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
				.getResources().getDimension(R.dimen.size_12_px));

		FontUtils.setCustomfont(getActivity().getApplicationContext(),
				messageText, FontTypes.ROBOTO_LIGHT);
		RelativeLayout.LayoutParams paramsMessage = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsMessage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsMessage.addRule(RelativeLayout.BELOW, image.getId());
		paramsMessage.topMargin = dim15;
		messageText.setLayoutParams(paramsMessage);

		rel.setPadding(0, dim15, 0, dim15);

		rel.addView(image);
		rel.addView(messageText);

		return rel;
	}

	protected View getGapView(int height) {
		View convertView;
		convertView = new View(getActivity().getApplicationContext());
		convertView.setMinimumHeight(height);
		return convertView;
	}

	public void goToTeam(View view) {
		// Intent intent = new Intent(getActivity().getApplicationContext(),
		// TeamActivity.class);
		// intent.putExtra("teamId", String.valueOf(view.getTag()));
		// startActivityForResult((intent, ReturnRequestCodes.PUBLI_BACK);
		// getActivity().overridePendingTransition(R.anim.grow_from_middle,
		// R.anim.shrink_to_middle);
	}

	public void goToResult(View view) {
		Match match = (Match) view.getTag();
		Intent intent = new Intent(getActivity().getApplicationContext(),
				CarruselDetailActivity.class);
		intent.putExtra("dataLink", match.getDataLink());
		intent.putExtra("link", match.getLink());
		intent.putExtra("dayName",
				"Jornada: " + (getArguments().getInt("jornadaId") + 1));

		String shield = DatabaseDAO.getInstance(
				getActivity().getApplicationContext()).getTeamGridShield(
				match.getLocalId());
		int idLocal = DrawableUtils.getDrawableId(getActivity()
				.getApplicationContext(), shield, 4);

		shield = DatabaseDAO.getInstance(getActivity().getApplicationContext())
				.getTeamGridShield(match.getAwayId());
		int idAway = DrawableUtils.getDrawableId(getActivity()
				.getApplicationContext(), shield, 4);

		intent.putExtra("idLocal", idLocal);
		intent.putExtra("idAway", idAway);

		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
	}

	protected void paintTVs(Match currentMatch, RelativeLayout tvContainerAll) {
		tvContainerAll.removeAllViews();
		if (currentMatch.getTelevisiones().size() > 0) {
			ImageView gapImage = new ImageView(getActivity()
					.getApplicationContext());
			gapImage.setId(500);
			gapImage.setBackgroundResource(R.color.clear_gray);
			int margin = DimenUtils.getRegularPixelFromDp(getActivity()
					.getApplicationContext(), 2);
			RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, margin);
			paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
			paramsImage.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			gapImage.setLayoutParams(paramsImage);
			tvContainerAll.addView(gapImage);

			LinearLayout tvContainer = new LinearLayout(getActivity()
					.getApplicationContext());
			tvContainer.setId(550);
			tvContainer.setPadding(margin, margin, margin, margin);
			tvContainer.setOrientation(LinearLayout.HORIZONTAL);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.addRule(RelativeLayout.BELOW, gapImage.getId());
			tvContainer.setLayoutParams(params);

			int i = 0, j = 10;
			int id = 0;
			boolean iconFound = false;
			LinearLayout.LayoutParams paramsCell;
			RelativeLayout.LayoutParams paramsIcon;
			RelativeLayout rel;
			ImageView tvIcon;
			for (String key : currentMatch.getTelevisiones()) {

				id = DrawableUtils.getDrawableId(getActivity()
						.getApplicationContext(), key, 4);
				if (id != 0) {
					tvIcon = new ImageView(getActivity()
							.getApplicationContext());
					rel = new RelativeLayout(getActivity()
							.getApplicationContext());
					iconFound = true;
					// tvIcon.setBackgroundResource(id);
					tvIcon.setImageDrawable(getResources().getDrawable(id));
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
					tvContainer.addView(new View(getActivity()
							.getApplicationContext()), paramsCell);
					tvContainer.addView(rel, paramsCell);
					i++;
					if (i >= Defines.NUM_MAX_TVS) {
						tvContainerAll.addView(tvContainer);
						id = tvContainer.getId();

						tvContainer = new LinearLayout(getActivity()
								.getApplicationContext());
						tvContainer.setId(id + 5);
						tvContainer.setPadding(margin, margin, margin, margin);
						tvContainer.setOrientation(LinearLayout.HORIZONTAL);
						params = new RelativeLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.CENTER_HORIZONTAL);
						params.addRule(RelativeLayout.BELOW, id);
						tvContainer.setLayoutParams(params);
						i = 0;
					}

				}
			}
			paramsCell = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT);
			paramsCell.weight = 1;
			paramsCell.leftMargin = margin;
			paramsCell.rightMargin = margin;
			tvContainer
					.addView(new View(getActivity().getApplicationContext()),
							paramsCell);

			if (iconFound) {
				tvContainerAll.addView(tvContainer);
				tvContainerAll.setVisibility(View.VISIBLE);
			} else {
				tvContainerAll.setVisibility(View.GONE);
			}
		} else {
			tvContainerAll.setVisibility(View.GONE);
		}
	}

}
