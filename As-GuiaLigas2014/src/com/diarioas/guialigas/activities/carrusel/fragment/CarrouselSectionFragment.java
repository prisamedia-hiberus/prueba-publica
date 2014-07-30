package com.diarioas.guialigas.activities.carrusel.fragment;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.carrusel.CarruselDetailActivity;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.model.calendar.Day;
import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.model.calendar.Grupo;
import com.diarioas.guialigas.dao.model.calendar.Match;
import com.diarioas.guialigas.dao.model.carrusel.Gol;
import com.diarioas.guialigas.dao.model.carrusel.Tarjeta;
import com.diarioas.guialigas.dao.model.competition.Group;
import com.diarioas.guialigas.dao.model.general.TeamSection;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.reader.CarruselDAO;
import com.diarioas.guialigas.dao.reader.CarruselDAO.CarruselDAOListener;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.ImageDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines;
import com.diarioas.guialigas.utils.Defines.DateFormat;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.RequestTimes;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.Defines.SECTIONS;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.Reachability;
import com.diarioas.guialigas.utils.comparator.GroupComparator;
import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class CarrouselSectionFragment extends SectionFragment implements
		CarruselDAOListener {

	private Timer t;

	private SimpleDateFormat formatterDate;
	private SimpleDateFormat formatterHour;
	private SimpleDateFormat dateFormatPull;

	private PullToRefreshListView carruselContent;
	private PullToRefreshScrollView carruselContentGroup;

	private long timeToUpdate;
	private long timeToUpdateOld;

	private TextView headerText;

	private Fase fase;

	private ArrayList<Group> groups;

	private RelativeLayout container;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_carrousel_section,
				container, false);

		return generalView;
	}

	@Override
	public void onStop() {
		super.onStop();
		CarruselDAO.getInstance(mContext).removeListener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (t != null) {
			t.cancel();
			t = null;
		}
		
		ImageDAO.getInstance(getActivity()).closePlayerCache();
		ImageDAO.getInstance(getActivity()).erasePlayerCache();
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@Override
	protected void configureView() {
		dateFormatPull = new SimpleDateFormat(DateFormat.PULL_FORMAT,
				Locale.getDefault());
		formatterDate = new SimpleDateFormat(DateFormat.DAY_FORMAT,
				Locale.getDefault());
		formatterHour = new SimpleDateFormat(DateFormat.HOUR_FORMAT,
				Locale.getDefault());

		timeToUpdate = RequestTimes.TIMER_CARRUSEL_UPDATE_NO_ACTIVE;
		timeToUpdateOld = timeToUpdate;

		generalView.findViewById(R.id.gapBar).setVisibility(View.VISIBLE);
		headerText = (TextView) generalView.findViewById(R.id.headerText);
		FontUtils.setCustomfont(mContext, headerText, FontTypes.HELVETICANEUE);

		container = (RelativeLayout) generalView.findViewById(R.id.container);

		callToOmniture();
	}

	@Override
	protected void buildView() {
		TeamSection sectionTeams = (TeamSection) RemoteDataDAO.getInstance(
				mContext).getSectionByType(Integer.valueOf(competitionId),
				SECTIONS.TEAMS);
		groups = sectionTeams.getGroups();
		configurePullFromGroup();
		configurePullFromRegular();

		updateCarrusel();

	}

	private void configurePullFromRegular() {
		carruselContent = (PullToRefreshListView) generalView
				.findViewById(R.id.carruselContent);

		// carruselContent.setClickable(false);
		carruselContent.setPullLabel(getString(R.string.ptr_pull_to_refresh));
		carruselContent.setRefreshingLabel(getString(R.string.ptr_refreshing));
		carruselContent
				.setReleaseLabel(getString(R.string.ptr_release_to_refresh));
		carruselContent.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				reloadData();
			}

		});
	}

	private void configurePullFromGroup() {
		carruselContentGroup = (PullToRefreshScrollView) generalView
				.findViewById(R.id.carruselContentGroup);
		carruselContentGroup.setClickable(false);
		carruselContentGroup
				.setPullLabel(getString(R.string.ptr_pull_to_refresh));
		carruselContentGroup
				.setRefreshingLabel(getString(R.string.ptr_refreshing));
		carruselContentGroup
				.setReleaseLabel(getString(R.string.ptr_release_to_refresh));
		carruselContentGroup
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						reloadData();
					}
				});
	}

	private void loadData() {

		container.removeAllViews();
		carruselContentGroup.setPullToRefreshEnabled(false);
		carruselContentGroup.setPullToRefreshOverScrollEnabled(false);
		generalView.findViewById(R.id.headerContainer).setVisibility(
				View.VISIBLE);
		headerText.setText("Jornada: " + fase.getIdFase());

		if (fase.getGrupos().size() == 1) {
			configRegularView();
		} else {
			configGroupView();
		}

		checkTime();
	}

	private void configRegularView() {
		carruselContent.setVisibility(View.VISIBLE);
		CarruselAdapter carruselAdapter = new CarruselAdapter();
		carruselContent.setAdapter(carruselAdapter);

		carruselAdapter.addItems(fase.getGrupos().get(0).getJornadas().get(0)
				.getMatches());
		// carruselContent.getRefreshableView().scrollTo(0, (int) scrollHeight);
	}

	private LinearLayout getItemsContent() {
		LinearLayout teamContent = new LinearLayout(mContext);
		teamContent.setOrientation(LinearLayout.VERTICAL);
		teamContent.setClickable(false);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		teamContent.setLayoutParams(params);
		return teamContent;
	}

	private void configGroupView() {

		View convertView;
		Team localTeam, awayTeam;
		boolean first;
		container.removeAllViews();
		LinearLayout itemsLayout = getItemsContent();
		container.addView(itemsLayout);
		// LinearLayout itemsLayout = (LinearLayout)
		// generalView.findViewById(R.id.itemsLayout);
		// itemsLayout.removeAllViews();

		ArrayList<Grupo> grupos = fase.getGrupos();
		Collections.sort(grupos, new GroupComparator());

		for (Grupo grupo : grupos) {
			// Escribir la cabecera del grupo

			convertView = getContainerGroup(grupo.getName());
			itemsLayout.addView(convertView);
			first = true;
			for (Match match : grupo.getJornadas().get(0).getMatches()) {
				// Escribir los layout por cada partido
				localTeam = findTeam(match.getLocalId());
				awayTeam = findTeam(match.getAwayId());
				convertView = getContainerMatch(first, match);
				itemsLayout.addView(convertView);
				first = false;
			}
		}
	}

	private View getContainerGroup(String name) {
		RelativeLayout rel = new RelativeLayout(mContext);
		rel.setTag(StickyScrollView.STICKY_TAG);
		int padding = DimenUtils.getRegularPixelFromDp(mContext, 10);
		rel.setPadding(0, padding, 0, padding);

		TextView title = new TextView(mContext);
		title.setId(150);
		title.setText(getString(R.string.calendar_group) + name);
		title.setTextColor(getResources().getColor(R.color.red));
		title.setTextSize(TypedValue.COMPLEX_UNIT_PT, getResources()
				.getDimension(R.dimen.size_10_px));
		FontUtils.setCustomfont(mContext, title, FontTypes.ROBOTO_LIGHT);

		RelativeLayout.LayoutParams paramsTitle = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsTitle.addRule(RelativeLayout.CENTER_HORIZONTAL);
		title.setLayoutParams(paramsTitle);
		rel.addView(title);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		rel.setLayoutParams(params);
		rel.setBackgroundColor(getResources().getColor(
				R.color.calendar_clear_gray));
		return rel;
	}

	private View getContainerMatch(boolean first, Match currentMatch) {
		View convertView;
		if (first)
			convertView = inflater.inflate(R.layout.item_list_carrusel_first,
					null);
		else
			convertView = inflater.inflate(R.layout.item_list_carrusel, null);

		final View centerContainer = convertView
				.findViewById(R.id.centerContainer);
		TextView leftTeamText = (TextView) convertView
				.findViewById(R.id.leftTeamText);
		FontUtils.setCustomfont(mContext, leftTeamText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		leftTeamText.setText(currentMatch.getLocalTeamName());
		// leftTeamText.setSelected(true);

		TextView rightTeamText = (TextView) convertView
				.findViewById(R.id.rightTeamText);
		FontUtils.setCustomfont(mContext, rightTeamText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		rightTeamText.setText(currentMatch.getAwayTeamName());
		// rightTeamText.setSelected(true);

		View resultContainer = convertView.findViewById(R.id.resultContainer);
		final TextView resultText = (TextView) convertView
				.findViewById(R.id.resultText);
		FontUtils.setCustomfont(mContext, resultText,
				FontUtils.FontTypes.ROBOTO_BLACK);

		TextView stateTeamText = (TextView) convertView
				.findViewById(R.id.stateTeamText);
		FontUtils.setCustomfont(mContext, stateTeamText,
				FontUtils.FontTypes.ROBOTO_LIGHT);

		TextView dateTeamText = (TextView) convertView
				.findViewById(R.id.dateTeamText);
		FontUtils.setCustomfont(mContext, dateTeamText,
				FontUtils.FontTypes.ROBOTO_REGULAR);

		TextView goleadoresText = (TextView) convertView
				.findViewById(R.id.goleadoresText);
		FontUtils.setCustomfont(mContext, goleadoresText,
				FontUtils.FontTypes.ROBOTO_REGULAR);

		TextView tarjeteadosText = (TextView) convertView
				.findViewById(R.id.tarjeteadosText);
		FontUtils.setCustomfont(mContext, tarjeteadosText,
				FontUtils.FontTypes.ROBOTO_REGULAR);
		ImageView localShield = (ImageView) convertView
				.findViewById(R.id.localShield);
		ImageView awayShield = (ImageView) convertView
				.findViewById(R.id.awayShield);

		RelativeLayout tvContainerAll = (RelativeLayout) convertView
				.findViewById(R.id.tvContainerAll);

		dateTeamText.setTextColor(getResources().getColor(R.color.medium_gray));

		Time date = new Time(Long.valueOf(currentMatch.getDate()) * 1000);
		final String resultString;
		if (currentMatch.getState().equalsIgnoreCase(
				Defines.MatchStates.FORPLAY)) {
			stateTeamText.setVisibility(View.GONE);
			String dateString = formatterHour.format(date);
			// if (!dateString.equalsIgnoreCase("00:00"))
			dateString += " h";
			// else {
			// dateString = "  -  ";
			// }
			resultString = dateString;
			resultText.setText(resultString);
			resultContainer.setBackgroundColor(getResources().getColor(
					R.color.calendar_yellow));
			dateTeamText.setText(formatterDate.format(date));
			paintTVs(currentMatch, tvContainerAll);
			convertView.findViewById(R.id.extraDataContent).setVisibility(
					View.GONE);
		} else {
			resultString = String.valueOf(currentMatch.getMarkerLocalTeam())
					+ " - " + String.valueOf(currentMatch.getMarkerAwayTeam());

			stateTeamText.setVisibility(View.VISIBLE);

			stateTeamText.setText(DatabaseDAO.getInstance(mContext).getStatus(
					currentMatch.getStateCode()));
			resultText.setText(resultString);
			dateTeamText.setVisibility(View.GONE);
			// Goles y tarjetas
			String goles = "";
			for (Gol gol : currentMatch.getGoles()) {
				goles += gol.getMin() + "' " + gol.getPlayer() + "("
						+ gol.getScoreBoard() + "), ";
			}
			String tarjetas = "";
			for (Tarjeta tajeta : currentMatch.getTarjetasRojas()) {
				tarjetas += tajeta.getMin() + "' " + tajeta.getPlayer() + ", ";
			}

			if (goles.equalsIgnoreCase("") && tarjetas.equalsIgnoreCase("")) {
				convertView.findViewById(R.id.extraDataContent).setVisibility(
						View.GONE);
			} else {
				if (!goles.equalsIgnoreCase("")) {
					goles = goles.substring(0, goles.length() - 2);
					goleadoresText.setText(goles);
				} else {
					convertView.findViewById(R.id.golesContent).setVisibility(
							View.GONE);
				}

				if (!tarjetas.equalsIgnoreCase("")) {
					tarjetas = tarjetas.substring(0, tarjetas.length() - 2);
					tarjeteadosText.setText(tarjetas);
				} else {
					convertView.findViewById(R.id.tarjetasContent)
							.setVisibility(View.GONE);
				}
			}
			// Goles y tarjetas
			if (currentMatch.getState().equalsIgnoreCase(
					Defines.MatchStates.PLAYING)) {
				resultContainer.setBackgroundColor(getResources().getColor(
						R.color.calendar_red));
				paintTVs(currentMatch, tvContainerAll);

			} else if (currentMatch.getState().equalsIgnoreCase(
					Defines.MatchStates.PLAYED)) {
				resultContainer.setBackgroundColor(getResources().getColor(
						R.color.calendar_gray));
				tvContainerAll.setVisibility(View.GONE);
			}
		}

		final int idLocal;
		// if (currentMatch.getLocalId() != null) {
		// idLocal = DrawableUtils.getDrawableId(mContext,
		// ShieldName.PREFIX_FLAG + currentMatch.getLocalId(), 0);
		// } else {
		idLocal = 0;
		// }

		localShield.setBackgroundResource(idLocal);

		final int idAway;
		// if (currentMatch.getAwayId() != null) {
		// idAway = DrawableUtils.getDrawableId(mContext,
		// ShieldName.PREFIX_FLAG + currentMatch.getAwayId(), 0);
		// } else {
		idAway = 0;
		// }

		awayShield.setBackgroundResource(idAway);

		centerContainer.setTag(currentMatch);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToMatch(
						(Match) v.findViewById(R.id.centerContainer).getTag(),
						idLocal, idAway, resultString);

			}
		});
		return convertView;
	}

	private Team findTeam(String teamId) {
		for (Group group : groups) {
			for (Team team : group.getTeams()) {
				if (team.getId().equalsIgnoreCase(teamId))
					return team;
			}
		}
		return null;

	}

	protected void paintTVs(Match currentMatch, RelativeLayout tvContainerAll) {
		tvContainerAll.removeAllViews();
		if (currentMatch.getTelevisiones().size() > 0) {
			ImageView gapImage = new ImageView(mContext);
			gapImage.setId(500);
			gapImage.setBackgroundResource(R.color.clear_gray);
			int margin = DimenUtils.getRegularPixelFromDp(mContext, 2);
			RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, margin);
			paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
			paramsImage.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			gapImage.setLayoutParams(paramsImage);
			tvContainerAll.addView(gapImage);

			LinearLayout tvContainer = new LinearLayout(mContext);
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

				id = DrawableUtils.getDrawableId(mContext, key, 4);
				if (id != 0) {
					tvIcon = new ImageView(mContext);
					rel = new RelativeLayout(mContext);
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
					tvContainer.addView(new View(mContext), paramsCell);
					tvContainer.addView(rel, paramsCell);
					i++;
					if (i >= Defines.NUM_MAX_TVS) {
						tvContainerAll.addView(tvContainer);
						id = tvContainer.getId();

						tvContainer = new LinearLayout(mContext);
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
			tvContainer.addView(new View(mContext), paramsCell);
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

	/***************************************************************************/
	/** Timer methods **/
	/***************************************************************************/

	private void checkTime() {
		// SE busca si hay algun partido activo,
		boolean carruselActive = false;
		for (Grupo grupo : fase.getGrupos()) {
			for (Day jornada : grupo.getJornadas()) {
				for (Match match : jornada.getMatches()) {
					if (match.getStateCode() > 0 && match.getStateCode() < 7) {
						carruselActive = true;
						break;
					}
				}

			}
		}

		// Si hay algun partido activo, se actualiza el tiempo
		if (carruselActive) {
			timeToUpdate = RequestTimes.TIMER_CARRUSEL_UPDATE_ACTIVE;
		} else {
			timeToUpdate = RequestTimes.TIMER_CARRUSEL_UPDATE_NO_ACTIVE;
		}
	}

	private void startTimer() {
		if (t == null || timeToUpdate != timeToUpdateOld) {

			if (t != null)
				t.cancel();

			t = new Timer();
			t.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (Reachability.isOnline(mContext))
						updateCarrusel();
					else {
						stopTimer();

					}
				}
			}, 0, timeToUpdate);
			timeToUpdateOld = timeToUpdate;
		}
	}

	protected void stopTimer() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (t != null)
					t.cancel();

				AlertManager.showAlertOkDialog(
						getActivity(),
						getResources().getString(
								R.string.section_error_download),
						getResources().getString(
								R.string.connection_error_title),
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}

						});
			}
		});

	}

	private void updateCarrusel() {

		CarruselDAO.getInstance(mContext).addListener(this);
		CarruselDAO.getInstance(mContext).refreshCarrusel(section.getUrl());
	}

	private void reloadData() {

		if (t != null) {
			t.cancel();
			t = null;
		}

		CarruselDAO.getInstance(mContext).addListener(this);
		CarruselDAO.getInstance(mContext).refreshCarrusel(section.getUrl());
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(), Omniture.SECTION_CARROUSEL,
				null, null, null, Omniture.TYPE_PORTADA,
				Omniture.DETAILPAGE_PORTADA + " " + Omniture.SECTION_CARROUSEL,
				null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_CARROUSEL + "/" + NativeAds.AD_PORTADA);
	}

	private void closePullToRefresh(Date date) {
		if (carruselContent != null) {
			carruselContent.onRefreshComplete();
			carruselContent
					.setLastUpdatedLabel(getString(R.string.ptr_last_updated)
							+ dateFormatPull.format(date));
		}
		if (carruselContentGroup != null) {
			carruselContentGroup.onRefreshComplete();
			carruselContentGroup
					.setLastUpdatedLabel(getString(R.string.ptr_last_updated)
							+ dateFormatPull.format(date));
		}
	}

	/***************************************************************************/
	/** CarruselDAOListener methods **/
	/***************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.RemoteCalendarDAO.RemoteCalendarDAOListener
	 * #onSuccessRemoteconfig(java.util.ArrayList)
	 */
	@Override
	public void onSuccessCarrusel(Fase fase) {
		CarruselDAO.getInstance(mContext).removeListener(this);
		closePullToRefresh(fase.getDateUpdated());
		this.fase = fase;
		loadData();
		((HomeActivity) getActivity()).stopAnimation();
		// Se da inicio al timer ora vez, por si hubiera cambios en la jornada
		startTimer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.RemoteCalendarDAO.RemoteCalendarDAOListener
	 * #onFailureRemoteconfig()
	 */
	@Override
	public void onFailureCarrusel() {
		CarruselDAO.getInstance(mContext).removeListener(this);
		showError(getResources().getString(R.string.calendar_error));
	}

	private void showError(String message) {
		closePullToRefresh(new Date());
		AlertManager.showAlertOkDialog(getActivity(), message, getResources()
				.getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// getActivity().onBackPressed();
						container.removeAllViews();
						container.addView(getErrorContainer());
						carruselContentGroup.setPullToRefreshEnabled(true);
						carruselContentGroup
								.setPullToRefreshOverScrollEnabled(true);
						generalView.findViewById(R.id.headerContainer)
								.setVisibility(View.GONE);
						if (carruselContent != null)
							carruselContent.setVisibility(View.GONE);

					}

				});

		((HomeActivity) getActivity()).stopAnimation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.RemoteCalendarDAO.RemoteCalendarDAOListener
	 * #onFailureNotConnection()
	 */
	@Override
	public void onFailureNotConnection() {
		CarruselDAO.getInstance(mContext).removeListener(this);
		showError(getResources().getString(R.string.section_not_conection));

	}

	public void goToMatch(Match match, int idLocal, int idAway,
			String resultString) {

		Intent intent = new Intent(mContext, CarruselDetailActivity.class);
		// intent.putExtra("state", match.getState());
		intent.putExtra("dataLink", match.getDataLink());
		intent.putExtra("link", match.getLink());

		if (headerText != null)
			intent.putExtra("dayName", headerText.getText().toString());
		else
			intent.putExtra("dayName", "Jornada");

		intent.putExtra("idLocal", idLocal);
		intent.putExtra("idAway", idAway);
		// intent.putExtra("resultString", resultString);

		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);

	}

	/***************************************************************************/
	/** CarruselAdapter **/
	/***************************************************************************/

	class CarruselAdapter extends BaseAdapter {

		private ArrayList<Match> matches;

		public CarruselAdapter() {
			this.matches = new ArrayList<Match>();

		}

		public void addItems(ArrayList<Match> matches) {
			this.matches = matches;
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
			return matches.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return matches.get(arg0);
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

			Match currentMatch = matches.get(position);
			// Team localTeam = findTeam(currentMatch.getLocalId());
			// Team awayTeam = findTeam(currentMatch.getAwayId());
			Team localTeam = DatabaseDAO.getInstance(mContext).getTeam(
					currentMatch.getLocalId());
			Team awayTeam = DatabaseDAO.getInstance(mContext).getTeam(
					currentMatch.getAwayId());

			final ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.item_list_carrusel_first, null);
				holder = new ViewHolder();

				holder.container = convertView.findViewById(R.id.container);
				holder.centerContainer = convertView
						.findViewById(R.id.centerContainer);
				holder.leftTeamText = (TextView) convertView
						.findViewById(R.id.leftTeamText);
				FontUtils.setCustomfont(mContext, holder.leftTeamText,
						FontUtils.FontTypes.ROBOTO_REGULAR);
				// holder.leftTeamText.setSelected(true);

				holder.resultContainer = convertView
						.findViewById(R.id.resultContainer);
				holder.resultText = (TextView) convertView
						.findViewById(R.id.resultText);
				FontUtils.setCustomfont(mContext, holder.resultText,
						FontUtils.FontTypes.ROBOTO_BLACK);

				holder.stateTeamText = (TextView) convertView
						.findViewById(R.id.stateTeamText);
				FontUtils.setCustomfont(mContext, holder.stateTeamText,
						FontUtils.FontTypes.ROBOTO_LIGHT);

				holder.rightTeamText = (TextView) convertView
						.findViewById(R.id.rightTeamText);
				FontUtils.setCustomfont(mContext, holder.rightTeamText,
						FontUtils.FontTypes.ROBOTO_REGULAR);
				// holder.rightTeamText.setSelected(true);

				holder.dateTeamText = (TextView) convertView
						.findViewById(R.id.dateTeamText);
				FontUtils.setCustomfont(mContext, holder.dateTeamText,
						FontUtils.FontTypes.ROBOTO_REGULAR);

				holder.goleadoresText = (TextView) convertView
						.findViewById(R.id.goleadoresText);
				FontUtils.setCustomfont(mContext, holder.goleadoresText,
						FontUtils.FontTypes.ROBOTO_REGULAR);

				holder.tarjeteadosText = (TextView) convertView
						.findViewById(R.id.tarjeteadosText);
				FontUtils.setCustomfont(mContext, holder.tarjeteadosText,
						FontUtils.FontTypes.ROBOTO_REGULAR);

				holder.localShield = (ImageView) convertView
						.findViewById(R.id.localShield);
				holder.awayShield = (ImageView) convertView
						.findViewById(R.id.awayShield);

				holder.tvContainerAll = (RelativeLayout) convertView
						.findViewById(R.id.tvContainerAll);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position % 2 == 0) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.white));
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.calendar_clear_gray));
			}

			holder.leftTeamText.setText(currentMatch.getLocalTeamName());
			holder.rightTeamText.setText(currentMatch.getAwayTeamName());

			holder.dateTeamText.setTextColor(getResources().getColor(
					R.color.medium_gray));

			final String resultString;
			if (currentMatch.getState().equalsIgnoreCase(
					Defines.MatchStates.FORPLAY)) {
				Time date = new Time(
						Long.valueOf(currentMatch.getDate()) * 1000);
				holder.stateTeamText.setVisibility(View.GONE);
				String dateString = formatterHour.format(date);
				// if (!dateString.equalsIgnoreCase("00:00"))
				dateString += " h";
				// else {
				// dateString = "  -  ";
				// }
				resultString = dateString;
				holder.resultText.setText(dateString);
				holder.resultContainer.setBackgroundColor(getResources()
						.getColor(R.color.calendar_yellow));
				holder.dateTeamText.setText(formatterDate.format(date));
				paintTVs(currentMatch, holder.tvContainerAll);
				convertView.findViewById(R.id.extraDataContent).setVisibility(
						View.GONE);
			} else {
				resultString = String
						.valueOf(currentMatch.getMarkerLocalTeam())
						+ " - "
						+ String.valueOf(currentMatch.getMarkerAwayTeam());
				holder.stateTeamText.setVisibility(View.VISIBLE);
				holder.stateTeamText.setText(DatabaseDAO.getInstance(mContext)
						.getStatus(currentMatch.getStateCode()));
				holder.resultText.setText(resultString);
				holder.dateTeamText.setVisibility(View.GONE);
				// Goles y tarjetas
				String goles = "";
				for (Gol gol : currentMatch.getGoles()) {
					goles += gol.getMin() + "' " + gol.getPlayer() + "("
							+ gol.getScoreBoard() + "), ";
				}
				String tarjetas = "";
				for (Tarjeta tajeta : currentMatch.getTarjetasRojas()) {
					tarjetas += tajeta.getMin() + "' " + tajeta.getPlayer()
							+ ", ";
				}

				if (goles.equalsIgnoreCase("") && tarjetas.equalsIgnoreCase("")) {
					convertView.findViewById(R.id.extraDataContent)
							.setVisibility(View.GONE);
				} else {
					convertView.findViewById(R.id.extraDataContent)
							.setVisibility(View.VISIBLE);
					if (!goles.equalsIgnoreCase("")) {
						goles = goles.substring(0, goles.length() - 2);
						holder.goleadoresText.setText(goles);
						convertView.findViewById(R.id.golesContent)
								.setVisibility(View.VISIBLE);
					} else {
						convertView.findViewById(R.id.golesContent)
								.setVisibility(View.GONE);
					}

					if (!tarjetas.equalsIgnoreCase("")) {
						tarjetas = tarjetas.substring(0, tarjetas.length() - 2);
						holder.tarjeteadosText.setText(tarjetas);
						convertView.findViewById(R.id.tarjetasContent)
								.setVisibility(View.VISIBLE);
					} else {
						convertView.findViewById(R.id.tarjetasContent)
								.setVisibility(View.GONE);
					}
				}
				// Goles y tarjetas
				if (currentMatch.getState().equalsIgnoreCase(
						Defines.MatchStates.PLAYING)) {
					holder.resultContainer.setBackgroundColor(getResources()
							.getColor(R.color.calendar_red));
					paintTVs(currentMatch, holder.tvContainerAll);
				} else if (currentMatch.getState().equalsIgnoreCase(
						Defines.MatchStates.PLAYED)) {
					holder.resultContainer.setBackgroundColor(getResources()
							.getColor(R.color.calendar_gray));
					holder.tvContainerAll.setVisibility(View.GONE);
				}
			}

			final int idLocal;
			if (localTeam != null && localTeam.getCalendarShield() != null
					&& !localTeam.getCalendarShield().equalsIgnoreCase("")) {
				idLocal = DrawableUtils.getDrawableId(mContext,
						localTeam.getCalendarShield(), 4);
			} else {
				idLocal = R.drawable.escudo_generico_size03;
			}

			// if (idLocal != 0)
			holder.localShield.setBackgroundResource(idLocal);
			// else
			// holder.localShield
			// .setBackgroundResource(R.drawable.escudo_generico_size03);

			final int idAway;
			if (awayTeam != null && awayTeam.getCalendarShield() != null
					&& !awayTeam.getCalendarShield().equalsIgnoreCase("")) {
				idAway = DrawableUtils.getDrawableId(mContext,
						awayTeam.getCalendarShield(), 4);
			} else {
				idAway = R.drawable.escudo_generico_size03;
			}
			// if (idAway != 0)
			holder.awayShield.setBackgroundResource(idAway);
			// else
			// holder.awayShield
			// .setBackgroundResource(R.drawable.escudo_generico_size03);

			// holder.awayShield.setBackgroundResource(idAway);

			holder.centerContainer.setTag(currentMatch);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToMatch((Match) v.findViewById(R.id.centerContainer)
							.getTag(), idLocal, idAway, resultString);

				}
			});

			return convertView;
		}

	}

	static class ViewHolder {

		public TextView tarjeteadosText;
		public TextView goleadoresText;
		public View container;
		public TextView stateTeamText;
		public View resultContainer;
		public View centerContainer;
		public TextView resultText;
		public RelativeLayout tvContainerAll;
		public TextView dateTeamText;
		public TextView leftTeamText;
		public TextView rightTeamText;
		public ImageView localShield;
		public ImageView awayShield;

	}

}
