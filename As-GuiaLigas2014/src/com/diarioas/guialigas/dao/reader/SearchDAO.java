/**
 * 
 */
package com.diarioas.guialigas.dao.reader;

import java.util.ArrayList;

import android.content.Context;

import com.diarioas.guialigas.dao.model.search.SearchItem;
import com.diarioas.guialigas.dao.reader.async.AsyncSearchJSON;
import com.diarioas.guialigas.dao.reader.async.AsyncSearchJSON.AsyncSearchJSONListener;
import com.diarioas.guialigas.utils.Reachability;

/**
 * @author robertosanchez
 * 
 */
public class SearchDAO implements AsyncSearchJSONListener {

	public interface SearchDAOListener {

		void onSuccessSearch(ArrayList<SearchItem> retorno);

		void onFailureSearch();

		void onFailureNoResultSearch();

		void onFailureNotConnection();

	}

	private static SearchDAO sInstance;
	private Context mContext;
	private ArrayList<SearchDAOListener> listeners;
	private AsyncSearchJSON mainStaticSearchJSONReader;

	public static SearchDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new SearchDAO();
			sInstance.mContext = ctx;
			sInstance.listeners = new ArrayList<SearchDAOListener>();
		}
		return sInstance;
	}

	/********************** Gestion de Listeners ****************************/
	/**
	 * 
	 * @param listener
	 */
	public void addListener(SearchDAOListener listener) {
		if (this.listeners != null) {
			this.listeners.clear();
			this.listeners.add(listener);
		}
	}

	public void removeListener(SearchDAOListener listener) {
		if (this.listeners != null) {
			if (sInstance.listeners.contains(listener)) {
				sInstance.listeners.remove(listener);
			}
		}
		if (listeners.size() == 0 && mainStaticSearchJSONReader != null) {
			this.mainStaticSearchJSONReader.cancel(true);
		}
	}

	private void responseSuccess(ArrayList<SearchItem> retorno) {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).onSuccessSearch(retorno);
		}
	}

	private void responseFailureSearch() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureSearch();
			}
		}
	}

	private void responseNoResult() {
		if (this.listeners != null) {
			for (int i = 0; i < this.listeners.size(); i++) {
				this.listeners.get(i).onFailureNoResultSearch();
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

	/**
	 * @param search2
	 * @param currentTeamId
	 ***************************************************************/

	public void search(String urlSearch, String search) {
		if (Reachability.isOnline(this.mContext)) {
			loadMainSettings(urlSearch, search);
		} else {
			responseNotConnection();
		}
	}

	/**
	 * 
	 */
	public void cancelSearch() {
		if (mainStaticSearchJSONReader != null)
			this.mainStaticSearchJSONReader.cancel(true);

	}

	private void loadMainSettings(String urlSearch, String search) {

		this.mainStaticSearchJSONReader = new AsyncSearchJSON(this, mContext);
		this.mainStaticSearchJSONReader.execute(urlSearch, search);
	}

	/************ ASYNC SETTINGS TEAM READER *******************/

	@Override
	public void onSuccessfulExecute(ArrayList<SearchItem> retorno) {
		this.mainStaticSearchJSONReader = null;
		if (this.listeners != null) {
			if (retorno != null)
				if (retorno.size() != 0)
					responseSuccess(retorno);
				else
					responseNoResult();
			else
				responseFailureSearch();
		}
	}

	@Override
	public void onFailureExecute() {
		responseFailureSearch();
	}
	/*****************************************************************/

}
