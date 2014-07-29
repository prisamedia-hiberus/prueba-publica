/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.reader.async.AsyncStaticLoadCalendarDayJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncStaticLoadCalendarDayJSON.AsyncSettingsCalendarDayJSONListener;
import com.diarioas.guialigas.dao.reader.async.AsyncStaticLoadCalendarJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncStaticLoadCalendarJSON.AsyncSettingsCalendarJSONListener;
import com.diarioas.guialigas.utils.Reachability;

/**
 * @author robertosanchez
 * 
 */
public class CalendarDAO implements AsyncSettingsCalendarJSONListener,
		AsyncSettingsCalendarDayJSONListener {

	public interface CalendarDAOListener {

		void onSuccessRemoteconfig(ArrayList<Fase> fases);

		void onFailureRemoteconfig();

		void onFailureNotConnection();

	}

	public interface CalendarDayDAOListener {

		void onSuccessCalendarDay(Fase fase);

		void onFailureCalendarDay();

		void onFailureNotConnectionCalendarDay();

	}

	private static CalendarDAO sInstance;
	private Context mContext;
	private ArrayList<CalendarDAOListener> listeners;
	private ArrayList<CalendarDayDAOListener> dayListeners;

	// private AsyncStaticLoadCalendarJSON mainStaticLoadCalendarJSONReader;

	public static CalendarDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new CalendarDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<CalendarDAOListener>();
			sInstance.dayListeners = new ArrayList<CalendarDayDAOListener>();
		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(CalendarDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.add(listener);
		}
	}

	public void removeListener(CalendarDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
	}

	private void responseFailureRemoteConfig() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureRemoteconfig();
			}
		}
	}

	private void responseNotConnection() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureNotConnection();
			}
		}
	}

	private void responseUpdateDabase(ArrayList<Fase> fases) {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onSuccessRemoteconfig(fases);
			}
		}
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addDayListener(CalendarDayDAOListener listener) {
		if (this.dayListeners != null) {
			this.dayListeners.clear();
			this.dayListeners.add(listener);
		}
	}

	public void removeDayListener(CalendarDayDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.dayListeners.contains(listener)) {
				sInstance.dayListeners.remove(listener);
			}
		}
	}

	private void responseFailureDay() {
		if (this.dayListeners != null) {
			for (int i = 0; i < this.dayListeners.size(); i++) {
				this.dayListeners.get(i).onFailureCalendarDay();
			}
		}
	}

	private void responseNotConnectionDay() {
		if (this.dayListeners != null) {
			for (int i = 0; i < this.dayListeners.size(); i++) {
				this.dayListeners.get(i).onFailureNotConnectionCalendarDay();
			}
		}

	}

	private void responseSuccess(Fase fase) {
		if (this.dayListeners != null) {
			for (int i = 0; i < this.dayListeners.size(); i++) {
				this.dayListeners.get(i).onSuccessCalendarDay(fase);
			}
		}
	}

	/*****************************************************************/

	public void refreshCalendarWithNewResults(String url, String name) {
		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(url, name);
		} else {
			responseNotConnection();
		}
	}

	/**
	 * @param string
	 * @param string2
	 * @param numDay
	 */
	public void refreshCalendarDay(String url, String name, int numFase,
			int numDay) {
		if (Reachability.isOnline(this.mContext)) {
			loadCalendarDay(url, name, numFase, numDay);
		} else {
			responseNotConnectionDay();
		}

	}

	private void loadMainSettings(String url, String name) {

		AsyncStaticLoadCalendarJSON mainStaticLoadCalendarJSONReader = new AsyncStaticLoadCalendarJSON(
				this, mContext);
		mainStaticLoadCalendarJSONReader.execute(url, name);

	}

	/**
	 * @param url
	 * @param name
	 * @param numDay
	 */
	private void loadCalendarDay(String url, String name, int numFase,
			int numDay) {
		AsyncStaticLoadCalendarDayJSON mainStaticLoadCalendarDayJSONReader = new AsyncStaticLoadCalendarDayJSON(
				this, mContext, numFase, numDay);
		mainStaticLoadCalendarDayJSONReader.execute(url, name);
	}

	/************ ASYNC SETTINGS CALENDAR READER *******************/

	@Override
	public void onSuccessfulExecute(ArrayList<?> fases) {

		if (fases.size() == 0) {
			responseFailureRemoteConfig();
			return;
		} else {
			// He leido el plist y lo he cargardo en la DDBB
			responseUpdateDabase((ArrayList<Fase>) fases);

		}
	}

	@Override
	public void onFailureExecute() {

		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureRemoteconfig();
			}
		}
	}

	/*****************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncStaticLoadCalendarDayJSON.
	 * AsyncSettingsCalendarDayJSONListener
	 * #onSuccessfulCalendarDayExecute(es.prisacom.as.dao.model.calendar.Day)
	 */
	@Override
	public void onSuccessfulCalendarDayExecute(Fase fase) {
		responseSuccess(fase);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.prisacom.as.dao.reader.Async.AsyncStaticLoadCalendarDayJSON.
	 * AsyncSettingsCalendarDayJSONListener#onFailureCalendarDayExecute()
	 */
	@Override
	public void onFailureCalendarDayExecute() {
		if (this.dayListeners != null) {
			for (int i = 0; i < this.dayListeners.size(); i++) {
				this.dayListeners.get(i).onFailureCalendarDay();
			}
		}

	}

}
