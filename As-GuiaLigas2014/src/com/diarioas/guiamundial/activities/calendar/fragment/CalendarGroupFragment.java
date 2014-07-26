package com.diarioas.guiamundial.activities.calendar.fragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.dao.model.calendar.Fase;
import com.diarioas.guiamundial.dao.model.calendar.Grupo;
import com.diarioas.guiamundial.dao.model.calendar.Match;
import com.diarioas.guiamundial.dao.model.team.Team;
import com.diarioas.guiamundial.dao.reader.CalendarDAO;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.utils.AlertManager;
import com.diarioas.guiamundial.utils.Defines.MatchStates;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.DrawableUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CalendarGroupFragment extends CalendarFragment {

	private final int pos = -1;

	private CalendarAdapter calendarAdapter;
	private PullToRefreshListView contentTeam;

	private int jornadaId;
	private ArrayList<Grupo> grupos;

	// private ArrayList<Match> matches;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		grupos = getArguments().getParcelableArrayList("grupos");

		competitionId = getArguments().getInt("competitionId");
		jornadaId = getArguments().getInt("jornadaId");
		faseActiva = getArguments().getBoolean("faseActiva");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_calendar_regular_days,
				container, false);

		return generalView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		configureView();
	}

	@Override
	protected void configureView() {

		contentTeam = (PullToRefreshListView) generalView
				.findViewById(R.id.contentTeam);
		contentTeam.setClickable(false);
		contentTeam.setPullLabel(getString(R.string.ptr_pull_to_refresh));
		contentTeam.setRefreshingLabel(getString(R.string.ptr_refreshing));
		contentTeam.setReleaseLabel(getString(R.string.ptr_release_to_refresh));
		contentTeam.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				reUpdateCarrusel();
			}

		});
		ListView refreshableContentView = contentTeam.getRefreshableView();
		refreshableContentView.setDivider(null);
		refreshableContentView.setClickable(false);
		refreshableContentView.setCacheColorHint(0);
		refreshableContentView.setVerticalScrollBarEnabled(false);

		if (refreshableContentView.getHeaderViewsCount() == 1) {
			refreshableContentView.addHeaderView(getHeader());
		}

		calendarAdapter = new CalendarAdapter(this.getActivity()
				.getApplicationContext());
		contentTeam.setAdapter(calendarAdapter);

		if (faseActiva) {
			calendarAdapter.notifyDataSetChanged();
			// calendarAdapter.addItems(day.getMatches());
			refreshableContentView.addFooterView(getGapView(DimenUtils
					.getRegularPixelFromDp(getActivity()
							.getApplicationContext(), getResources()
							.getDimension(R.dimen.padding_footer_min))));
		} else {
			View footer = refreshableContentView.findViewById(FOOTER_ID);
			if (footer == null)
				refreshableContentView.addFooterView(getFooter());
		}
	}

	private View getHeader() {
		int padding = DimenUtils.getRegularPixelFromDp(getActivity()
				.getApplicationContext(), 16);

		RelativeLayout rel = new RelativeLayout(getActivity()
				.getApplicationContext());

		TextView dateText = new TextView(getActivity().getApplicationContext());
		dateText.setId(150);
		dateText.setText(grupos.get(0).getJornadas().get(jornadaId).getDate());
		dateText.setTextColor(getActivity().getResources().getColor(
				R.color.medium_gray));
		dateText.setTextSize(TypedValue.COMPLEX_UNIT_PT, getActivity()
				.getResources().getDimension(R.dimen.size_12_px));

		FontUtils.setCustomfont(getActivity().getApplicationContext(),
				dateText, FontTypes.ROBOTO_LIGHT);
		RelativeLayout.LayoutParams paramsDate = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsDate.addRule(RelativeLayout.CENTER_IN_PARENT);
		dateText.setLayoutParams(paramsDate);

		ImageView gapImage = new ImageView(getActivity()
				.getApplicationContext());
		gapImage.setId(500);
		gapImage.setBackgroundResource(R.color.clear_gray);
		int margin = DimenUtils.getRegularPixelFromDp(getActivity()
				.getApplicationContext(), 2);
		RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, margin);
		paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsImage.addRule(RelativeLayout.BELOW, dateText.getId());
		paramsImage.topMargin = padding;
		gapImage.setLayoutParams(paramsImage);

		rel.addView(dateText);
		rel.addView(gapImage);

		rel.setPadding(0, padding, 0, 0);

		return rel;
	}

	private void closePullToRefresh(Date date) {
		if (contentTeam != null) {
			contentTeam.onRefreshComplete();
			contentTeam
					.setLastUpdatedLabel(getString(R.string.ptr_last_updated)
							+ dateFormatPull.format(date));
		}
	}

	/*******************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CalendarDAO.CalendarDayDAOListener#
	 * onSuccessCalendarDay(es.prisacom.as.dao.model.calendar.Day)
	 */
	@Override
	public void onSuccessCalendarDay(Fase fase) {
		CalendarDAO.getInstance(getActivity().getApplicationContext())
				.removeDayListener(this);
		// closePullToRefresh(fase.getDateUpdated());
		closePullToRefresh(new Date());

		this.grupos = fase.getGrupos();
		if (faseActiva != fase.isActive()) {
			faseActiva = fase.isActive();
			calendarAdapter.notifyDataSetChanged();
			// calendarAdapter.addItems(day.getMatches());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CalendarDAO.CalendarDayDAOListener#
	 * onFailureCalendarDay()
	 */
	@Override
	public void onFailureCalendarDay() {
		CalendarDAO.getInstance(getActivity().getApplicationContext())
				.removeDayListener(this);
		closePullToRefresh(new Date());
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.calendar_error),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// onBackPressed();
					}

				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CalendarDAO.CalendarDayDAOListener#
	 * onFailureNotConnectionCalendarDay()
	 */
	@Override
	public void onFailureNotConnectionCalendarDay() {
		CalendarDAO.getInstance(getActivity().getApplicationContext())
				.removeDayListener(this);
		closePullToRefresh(new Date());
		AlertManager.showAlertOkDialog(
				getActivity(),
				getResources().getString(
						R.string.section_not_conection_notupdated),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// onBackPressed();
					}

				});

	}

	/*******************************************************************************/
	class CalendarAdapter extends BaseAdapter {

		private static final int NORMAL_TYPE = 0;
		private static final int FIRST_TYPE = NORMAL_TYPE + 1;
		private static final int TOTAL_TYPE = FIRST_TYPE + 1;
		private final Context mContext;
		private int size = -1;

		public CalendarAdapter(Context ctx, ArrayList<Match> matches) {
			this.mContext = ctx;

		}

		/**
		 * @param applicationContext
		 */
		public CalendarAdapter(Context ctx) {
			this.mContext = ctx;

		}

		@Override
		public int getItemViewType(int position) {
			int i = 0;
			Match match;
			for (Grupo grupo : grupos) {
				for (int j = 0; j < grupo.getJornadas().get(jornadaId)
						.getMatches().size(); j++) {
					match = grupo.getJornadas().get(jornadaId).getMatches()
							.get(j);
					if (i == position) {
						if (j == 0) {
							return FIRST_TYPE;
						} else {
							return NORMAL_TYPE;
						}
					}
					i++;
				}
			}
			return NORMAL_TYPE;

		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return TOTAL_TYPE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			if (size < 0) {
				size = 0;
				for (Grupo grupo : grupos) {
					size += grupo.getJornadas().get(jornadaId).getMatches()
							.size();
				}
			}
			return size;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Match getItem(int position) {
			int i = 0;
			for (Grupo grupo : grupos) {
				for (Match match : grupo.getJornadas().get(jornadaId)
						.getMatches()) {

					if (i == position) {
						return match;
					}
					i++;

				}

			}
			return null;
		}

		public String getGroupName(int position) {
			int i = 0;
			for (Grupo grupo : grupos) {
				for (Match match : grupo.getJornadas().get(jornadaId)
						.getMatches()) {
					if (i == position) {
						return grupo.getName();
					}
					i++;

				}

			}
			return null;
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
		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// View view = new View(mContext);
			// Random rnd = new Random();
			// int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),
			// rnd.nextInt(256));
			// view.setBackgroundColor(color);
			// view.setMinimumHeight(100);

			Match currentMatch = getItem(position);
			Team localTeam = teams.get(currentMatch.getLocalId());
			Team awayTeam = teams.get(currentMatch.getAwayId());

			int itemViewType = getItemViewType(position);

			final ViewHolder holder;
			if (convertView == null) {
				// int type = getItemViewType(position);
				// Log.d("CALENDAR", "Position: " + position + " Type: " +
				// type);
				holder = new ViewHolder();

				switch (itemViewType) {
				case FIRST_TYPE:
					convertView = inflater.inflate(
							R.layout.item_list_calendar_withogroup, null);
					holder.groupName = (TextView) convertView
							.findViewById(R.id.groupName);
					FontUtils.setCustomfont(mContext, holder.groupName,
							FontTypes.ROBOTO_LIGHT);
					break;
				case NORMAL_TYPE:
					convertView = inflater.inflate(R.layout.item_list_calendar,
							null);
					break;
				}

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

				holder.localShieldContainer = (RelativeLayout) convertView
						.findViewById(R.id.localShieldContainer);
				holder.awayShieldContainer = (RelativeLayout) convertView
						.findViewById(R.id.awayShieldContainer);
				holder.localShield = (ImageView) convertView
						.findViewById(R.id.localShield);
				holder.awayShield = (ImageView) convertView
						.findViewById(R.id.awayShield);
				// holder.tvContainer = (LinearLayout) convertView
				// .findViewById(R.id.tvContainer);
				holder.tvContainerAll = (RelativeLayout) convertView
						.findViewById(R.id.tvContainerAll);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			switch (itemViewType) {
			case FIRST_TYPE:
				holder.groupName.setText(getString(R.string.group)
						+ getGroupName(position));
				break;
			case NORMAL_TYPE:

				break;
			}
			holder.centerContainer.getViewTreeObserver()
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@Override
						public void onGlobalLayout() {

							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								holder.centerContainer.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							} else {
								holder.centerContainer.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							}
							LayoutParams paramsLSC = holder.localShieldContainer
									.getLayoutParams();
							paramsLSC.height = holder.centerContainer
									.getHeight();
							holder.localShieldContainer
									.setLayoutParams(paramsLSC);
							LayoutParams paramsASC = holder.awayShieldContainer
									.getLayoutParams();
							paramsASC.height = holder.centerContainer
									.getHeight();
							holder.awayShieldContainer
									.setLayoutParams(paramsASC);
						}
					});
			// if (currentMatch.getLink() != null
			// && !currentMatch.getLink().equalsIgnoreCase("")) {
			holder.centerContainer.setTag(currentMatch);
			holder.centerContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToResult(v);

				}
			});
			// }
			holder.localShieldContainer.setTag(currentMatch.getLocalId());
			holder.localShieldContainer
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							goToTeam(v);

						}
					});
			holder.awayShieldContainer.setTag(currentMatch.getAwayId());
			holder.awayShieldContainer
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							goToTeam(v);

						}
					});

			holder.leftTeamText.setText(currentMatch.getLocalTeamName());
			holder.rightTeamText.setText(currentMatch.getAwayTeamName());

			holder.dateTeamText.setTextColor(getResources().getColor(
					R.color.medium_gray));

			// holder.tvContainer.removeAllViews();

			Time date = new Time(Long.valueOf(currentMatch.getDate()) * 1000);
			String resultString;
			if (currentMatch.getState().equalsIgnoreCase(MatchStates.FORPLAY)) {
				holder.stateTeamText.setVisibility(View.GONE);
				resultString = formatterHour.format(date);
				// if (!resultString.equalsIgnoreCase("00:00"))
				resultString += " h";
				// else {
				// resultString = "  -  ";
				// }
				holder.resultText.setText(resultString);
				holder.resultContainer.setBackgroundColor(getActivity()
						.getResources().getColor(R.color.calendar_yellow));
				holder.dateTeamText.setText(formatterDate.format(date));
				paintTVs(currentMatch, holder.tvContainerAll);

			} else {
				resultString = String
						.valueOf(currentMatch.getMarkerLocalTeam())
						+ " - "
						+ String.valueOf(currentMatch.getMarkerAwayTeam());
				if (currentMatch.getState().equalsIgnoreCase(
						MatchStates.PLAYING)) {
					holder.stateTeamText.setVisibility(View.VISIBLE);
					holder.stateTeamText.setText(DatabaseDAO.getInstance(
							mContext).getStatus(currentMatch.getStateCode()));
					holder.resultText.setText(resultString);
					holder.resultContainer.setBackgroundColor(getActivity()
							.getResources().getColor(R.color.calendar_red));
					holder.dateTeamText.setVisibility(View.GONE);

					paintTVs(currentMatch, holder.tvContainerAll);

				} else if (currentMatch.getState().equalsIgnoreCase(
						MatchStates.PLAYED)) {
					holder.stateTeamText.setVisibility(View.VISIBLE);
					holder.stateTeamText.setText(DatabaseDAO.getInstance(
							mContext).getStatus(currentMatch.getStateCode()));
					holder.resultText.setText(resultString);
					holder.resultContainer.setBackgroundColor(getActivity()
							.getResources().getColor(R.color.calendar_gray));
					holder.dateTeamText.setVisibility(View.GONE);
					holder.tvContainerAll.setVisibility(View.GONE);
				}
			}

			int idLocal = 0;
			if (localTeam != null && localTeam.getCalendarShield() != null
					&& !localTeam.getCalendarShield().equalsIgnoreCase("")) {
				idLocal = DrawableUtils.getDrawableId(getActivity()
						.getApplicationContext(),
						localTeam.getCalendarShield(), 4);
			}

			holder.localShield.setBackgroundResource(idLocal);

			int idAway = 0;
			if (awayTeam != null && awayTeam.getCalendarShield() != null
					&& !awayTeam.getCalendarShield().equalsIgnoreCase("")) {
				idAway = DrawableUtils.getDrawableId(getActivity()
						.getApplicationContext(), awayTeam.getCalendarShield(),
						4);
			}

			holder.awayShield.setBackgroundResource(idAway);

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView groupName;
		public View container;
		public RelativeLayout awayShieldContainer;
		public RelativeLayout localShieldContainer;
		public TextView stateTeamText;
		public View resultContainer;
		public View centerContainer;
		public TextView resultText;
		public RelativeLayout tvContainerAll;
		// public LinearLayout tvContainer;
		public TextView dateTeamText;
		public TextView leftTeamText;
		public TextView rightTeamText;
		public ImageView localShield;
		public ImageView awayShield;
	}

}
