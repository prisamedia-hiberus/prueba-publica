package com.diarioas.guialigas.activities.calendar.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ScrollView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.model.calendar.Day;
import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.model.calendar.Grupo;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.reader.CalendarDAO;
import com.diarioas.guialigas.dao.reader.CalendarDAO.CalendarDAOListener;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.RequestTimes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.FragmentAdapter;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll;
import com.diarioas.guialigas.utils.scroll.CustomHoizontalScroll.ScrollEndListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class CalendarSectionFragment extends SectionFragment implements
		CalendarDAOListener, ScrollEndListener, OnPageChangeListener {

	private Timer timer;
	private boolean calendarFases;
	private ArrayList<Fase> fases;

	private int currentDay = -1;
	private ArrayList<Day> days;
	private CustomHoizontalScroll calendarSrolldays;
	private int width;
	private ViewPager daysViewPager;
	private Section section;
	private String competitionId;
	private CalendarAdapter calendarAdapter;

	private PullToRefreshScrollView calendarContent;
	private ScrollView refreshableContentView;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_calendar_section,
				container, false);
		return generalView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CalendarDAO.getInstance(mContext).removeListener(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@Override
	protected void configureView() {
		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		width = size.x;

		section = (Section) getArguments().getSerializable("section");
		competitionId = getArguments().getString("competitionId");

		calendarContent = (PullToRefreshScrollView) generalView
				.findViewById(R.id.calendarContent);
		calendarContent.setClickable(false);
		calendarContent.setPullLabel(getString(R.string.ptr_pull_to_refresh));
		calendarContent.setRefreshingLabel(getString(R.string.ptr_refreshing));
		calendarContent
				.setReleaseLabel(getString(R.string.ptr_release_to_refresh));
		calendarContent
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						updateCalendar();
					}
				});

		refreshableContentView = calendarContent.getRefreshableView();
		// refreshableContentView.setDivider(null);
		refreshableContentView.setClickable(false);
		refreshableContentView.setHorizontalScrollBarEnabled(false);
	}

	@Override
	public void buildView() {
		updateCalendar();

	}

	private void loadData() {
		refreshableContentView.removeAllViews();
		daysViewPager = (ViewPager) generalView
				.findViewById(R.id.calendarDayViewPager);
		// Pintar los datos de la Current Day
		List<Fragment> fragments = getFragments();
		if (fragments.size() != 0) {
			if (calendarAdapter == null) {
				calendarAdapter = new CalendarAdapter(fragments);
				daysViewPager.setAdapter(calendarAdapter);
				daysViewPager.setOnPageChangeListener(this);

			} else {
				calendarAdapter.setNewFragments(fragments);
			}
			daysViewPager.setCurrentItem(currentDay, true);

		}
	}

	private List<Fragment> getFragments() {

		List<Fragment> fList = new ArrayList<Fragment>();
		Fragment calendarDayFragment;
		Bundle args;
		int faseCont = 0;
		for (Fase fase : fases) {
			// for (Grupo grupo : fase.getGrupos()) {
			if (fase.getGrupos().size() == 1) {

				// Fase que no es de grupos
				for (Day day : fase.getGrupos().get(0).getJornadas()) {
					args = new Bundle();
					args.putParcelable("day", day);
					args.putInt("competitionId", Integer.valueOf(competitionId));
					args.putString("urlCalendar", section.getUrl());
					args.putInt("faseId", faseCont);
					args.putInt("jornadaId", day.getNumDay() - 1);
					args.putBoolean("faseActiva", fase.isActive());
					calendarDayFragment = Fragment.instantiate(getActivity(),
							CalendarRegularFragment.class.getName());
					calendarDayFragment.setArguments(args);
					fList.add(calendarDayFragment);
				}
			} else {
				// Recorrer todos los grupos, para cada uno, obtener la 1
				// jornada y mandarla al fragment

				for (Day day : fase.getGrupos().get(0).getJornadas()) {
					args = new Bundle();
					args.putInt("competitionId", Integer.valueOf(competitionId));
					args.putString("urlCalendar", section.getUrl());
					args.putBoolean("faseActiva", fase.isActive());
					args.putInt("faseId", faseCont);
					args.putInt("jornadaId", day.getNumDay() - 1);
					args.putParcelableArrayList("grupos", fase.getGrupos());
					calendarDayFragment = Fragment.instantiate(getActivity(),
							CalendarGroupFragment.class.getName());
					calendarDayFragment.setArguments(args);
					fList.add(calendarDayFragment);
				}
			}
			faseCont++;
		}

		return fList;

	}

	private void configureHeader(int firstPosition) {
		if (!this.calendarFases) {
			days = fases.get(0).getGrupos().get(0).getJornadas();
			generateSliderRegular(firstPosition);
		} else {
			if (currentDay == -1)
				this.currentDay = 0;
			generateSliderGroup(firstPosition);

		}
	}

	private void generateSliderRegular(int firstPosition) {

		ArrayList<String> strings = new ArrayList<String>();
		for (int i = 0; i < days.size(); i++) {
			strings.add(getResources().getString(R.string.day)
					+ days.get(i).getNumDay());
		}

		calendarSrolldays = (CustomHoizontalScroll) generalView
				.findViewById(R.id.calendarSrolldays);
		calendarSrolldays.setScreenWidth(width);
		calendarSrolldays.setFont(FontTypes.ROBOTO_LIGHT);
		calendarSrolldays.setMainColor(getResources().getColor(
				R.color.red_comparator));
		calendarSrolldays.setSecondColor(getResources().getColor(
				R.color.medium_gray));
		calendarSrolldays.addScrollEndListener(this);
		calendarSrolldays.setInitPosition(firstPosition);
		calendarSrolldays.addViews(strings);
		selectFirstPosition(firstPosition);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void selectFirstPosition(final int positionDay) {
		currentDay = positionDay;

		final ViewTreeObserver vto1 = this.calendarSrolldays
				.getViewTreeObserver();
		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					vto1.removeOnGlobalLayoutListener(this);
				} else {
					vto1.removeGlobalOnLayoutListener(this);
				}

				daysViewPager.setCurrentItem(currentDay, true);

			}
		});
	}

	private void generateSliderGroup(int firstPosition) {
		Fase fase;
		Grupo grupo;

		days = new ArrayList<Day>();
		ArrayList<String> strings = new ArrayList<String>();
		String nameTab;
		for (int j = 0; j < fases.size(); j++) {
			fase = fases.get(j);
			grupo = fase.getGrupos().get(0);
			for (int i = 0; i < grupo.getJornadas().size(); i++) {
				days.add(grupo.getJornadas().get(i));
				if (grupo.getJornadas().size() == 2) {
					if (i == 0) {
						nameTab = fase.getName() + " Ida";
					} else {
						nameTab = fase.getName() + " Vuelta";
					}
				} else if (grupo.getJornadas().size() > 1) {
					nameTab = fase.getName() + " Jornada " + (i + 1);
				} else {
					nameTab = fase.getName();
				}
				strings.add(nameTab);
			}
		}

		calendarSrolldays = (CustomHoizontalScroll) generalView
				.findViewById(R.id.calendarSrolldays);
		calendarSrolldays.setScreenWidth(width);
		calendarSrolldays.setFont(FontTypes.ROBOTO_LIGHT);
		calendarSrolldays.setMainColor(getResources().getColor(
				R.color.red_comparator));
		calendarSrolldays.setSecondColor(getResources().getColor(
				R.color.medium_gray));
		calendarSrolldays.addScrollEndListener(this);
		calendarSrolldays.setInitPosition(firstPosition);
		calendarSrolldays.addViews(strings);
		selectFirstPosition(firstPosition);
	}

	/***************************************************************************/
	/** Timer methods **/
	/***************************************************************************/
	private void updateCalendar() {
		Log.d("CARRUSELTIMER", "updateCalendar " + new Date().toString());

		CalendarDAO.getInstance(mContext).addListener(this);
		CalendarDAO.getInstance(mContext).refreshCalendarWithNewResults(
				section.getUrl(), competitionId + "_calendar.json");
	}

	private void startTimer() {
		if (timer == null) {

			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					updateCalendar();
				}
			}, 0, RequestTimes.TIMER_CALENDAR_UPDATE);
		}
	}

	private void stopAnimation() {
		((HomeActivity) getActivity()).stopAnimation();
		if (calendarContent != null) {
			calendarContent.onRefreshComplete();
			// contentGallery
			// .setLastUpdatedLabel(getString(R.string.ptr_last_updated)
			// + dateFormatPull.format(new Date()));
		}
	}

	/***************************************************************************/
	/** Libraries methods **/
	/**
	 * @param pos
	 *************************************************************************/
	@Override
	protected void callToOmniture() {
		callToOmniture(currentDay);
	}

	private void callToOmniture(int pos) {
		Competition comp = DatabaseDAO.getInstance(mContext).getCompetition(
				Integer.valueOf(competitionId));
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
				comp.getName().toLowerCase(),
				Omniture.SECTION_CALENDAR,
				"jornada " + (pos + 1),
				null,
				Omniture.TYPE_PORTADA,
				Omniture.DETAILPAGE_DETALLE + " " + Omniture.SECTION_CALENDAR
						+ " jornada " + (pos + 1), null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_CALENDAR+ "/" + NativeAds.AD_PORTADA);
	}

	/***************************************************************************/
	/** CalendarDAOListener methods **/
	/***************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CalendarDAO.CalendarDAOListener
	 * #onSuccessRemoteconfig(java.util.ArrayList)
	 */
	@Override
	public void onSuccessRemoteconfig(ArrayList<Fase> fases) {
		CalendarDAO.getInstance(mContext).removeListener(this);

		this.fases = fases;

		int firstPosition = 0;
		if (currentDay == -1) {
			for (int i = 0; i < fases.size(); i++) {
				if (fases.get(i).isDefecto()) {
					for (int k = 0; k < fases.get(i).getGrupos().get(0)
							.getJornadas().size(); k++) {
						if (fases.get(i).getGrupos().get(0).getJornadas()
								.get(k).isDefecto()) {
							break;
						}
						firstPosition++;
					}
					break;
				}
				firstPosition += fases.get(i).getGrupos().get(0).getJornadas()
						.size();
			}
		} else {
			firstPosition = currentDay;
		}

		if (fases.size() == 1) {
			this.calendarFases = false;
		} else {
			this.calendarFases = true;
		}
		configureHeader(firstPosition);
		loadData();
		stopAnimation();
		startTimer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CalendarDAO.CalendarDAOListener
	 * #onFailureRemoteconfig()
	 */
	@Override
	public void onFailureRemoteconfig() {
		CalendarDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.calendar_error),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						refreshableContentView.removeAllViews();
						refreshableContentView.addView(getErrorContainer());

					}

				});

		stopAnimation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.CalendarDAO.CalendarDAOListener
	 * #onFailureNotConnection()
	 */
	@Override
	public void onFailureNotConnection() {
		CalendarDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						refreshableContentView.removeAllViews();
						refreshableContentView.addView(getErrorContainer());
					}

				});

		stopAnimation();
	}

	/***************************************************************************/
	/** ScrollEndListener **/
	/***************************************************************************/
	@Override
	public void onScrollEnd(int x, int y, int oldx, int oldy, int pos) {
		daysViewPager.setCurrentItem(pos, true);

	}

	@Override
	public void onItemClicked(int position) {
		daysViewPager.setCurrentItem(position, true);

	}

	/***************************************************************************/
	/** OnPageChangeListener **/
	/***************************************************************************/
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int pos) {
		calendarSrolldays.setHeaderPosition(pos);
		callToOmniture(pos);
	}

	/***************************************************************************/
	/** CalendarAdapter **/
	/***************************************************************************/

	public class CalendarAdapter extends FragmentAdapter {

		public CalendarAdapter(List<Fragment> fragments) {
			super(getActivity().getSupportFragmentManager(), fragments);
		}

		public void setNewFragments(List<Fragment> fragments) {
			this.fragments = (ArrayList<Fragment>) fragments;
			notifyDataSetChanged();
		}

	}

}
