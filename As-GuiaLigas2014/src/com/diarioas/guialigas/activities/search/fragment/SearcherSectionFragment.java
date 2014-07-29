package com.diarioas.guialigas.activities.search.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.player.PlayerActivity;
import com.diarioas.guialigas.activities.team.TeamActivity;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.model.search.SearchItem;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.SearchDAO;
import com.diarioas.guialigas.dao.reader.SearchDAO.SearchDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public class SearcherSectionFragment extends SectionFragment implements
		SearchDAOListener {

	private static final int NUM_MIN_CHARACTER = 3;

	private ListView listSearch;
	private EditText searchEditText;
	private String search;

	private SearchAdapter searchAdapter;

	private String urlSearch;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		Section section = (Section) getArguments().getSerializable("section");
		urlSearch = section.getUrl();
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_searcher_section,
				container, false);
		return generalView;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void buildView() {

	}

	@Override
	protected void configureView() {

		configureListView();

		searchEditText = (EditText) generalView
				.findViewById(R.id.searchEditText);
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.length() >= NUM_MIN_CHARACTER) {
					findSentence(s);
				} else {
					searchAdapter.resetItems();
					generalView.findViewById(R.id.noContent).setVisibility(
							View.VISIBLE);
					generalView.findViewById(R.id.errorContent).setVisibility(
							View.GONE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0
						&& s.subSequence(s.length() - 1, s.length()).toString()
								.equals("\n")) {
					s.replace(s.length() - 1, s.length(), "");

				}
			}
		});
		searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if ((actionId == EditorInfo.IME_NULL && (event.getAction() == KeyEvent.ACTION_DOWN))
						|| actionId == EditorInfo.IME_ACTION_DONE
						|| actionId == EditorInfo.IME_ACTION_GO
						|| actionId == EditorInfo.IME_ACTION_SEARCH) {
					closeKeyboard();
				}
				return false;
			}
		});

		callToOmniture();
	}

	/**
	 * Configure the ListView
	 */
	private void configureListView() {
		listSearch = (ListView) generalView.findViewById(R.id.listSearch);
		listSearch.setDivider(null);
		listSearch.setClickable(false);
		listSearch.setCacheColorHint(0);

		searchAdapter = new SearchAdapter();
		listSearch.setAdapter(searchAdapter);

	}

	private void findSentence(CharSequence s) {
		((HomeActivity) getActivity()).startAnimation();
		search = s.toString();
		SearchDAO.getInstance(mContext).cancelSearch();
		SearchDAO.getInstance(mContext).addListener(this);
		SearchDAO.getInstance(mContext).search(urlSearch, search);
		generalView.findViewById(R.id.noContent).setVisibility(View.GONE);
		generalView.findViewById(R.id.errorContent).setVisibility(View.GONE);
		listSearch.setVisibility(View.GONE);
	}

	protected void goToDetail(boolean isTeam, String id, String url) {
		Intent intent;
		if (isTeam) {
			intent = new Intent(mContext, TeamActivity.class);
			intent.putExtra("teamId", id);
			intent.putExtra("teamUrl", url);

		} else {
			Integer playerId = Integer.valueOf(id);
			DatabaseDAO.getInstance(mContext).getTeamNameFromPlayer(playerId);
			intent = new Intent(mContext, PlayerActivity.class);
			intent.putExtra("playerId", playerId);
			// intent.putExtra("teamName", team);
			intent.putExtra("playerUrl", url);
		}
		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);

	}

	@Override
	public void onOpenSlidingMenu() {
		closeKeyboard();
	}

	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(), Omniture.SECTION_SEARCHER,
				null, null, null, Omniture.TYPE_PORTADA,
				Omniture.SECTION_SEARCHER + " " + Omniture.DETAILPAGE_PORTADA,
				null);
	}

	@Override
	public void callToAds() {
		callToAds(NativeAds.AD_SEARCHER);
	}

	/***************************************************************************/
	/** SearchDAOListener methods **/
	/***************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.SearchDAO.SearchDAOListener#onSuccessSearch
	 * (java.util.ArrayList)
	 */
	@Override
	public void onSuccessSearch(ArrayList<SearchItem> retorno) {
		SearchDAO.getInstance(mContext).removeListener(this);
		// Log.d("SEARCHACTIVITY", "Retorno: " + retorno.size());
		((HomeActivity) getActivity()).stopAnimation();
		listSearch.setVisibility(View.VISIBLE);
		generalView.findViewById(R.id.noContent).setVisibility(View.GONE);
		generalView.findViewById(R.id.errorContent).setVisibility(View.GONE);
		searchAdapter.addItems(retorno);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.SearchDAO.SearchDAOListener#onFailureSearch()
	 */
	@Override
	public void onFailureSearch() {
		SearchDAO.getInstance(mContext).removeListener(this);
		searchAdapter.resetItems();
		((HomeActivity) getActivity()).stopAnimation();
		((TextView) generalView.findViewById(R.id.errorMessage))
				.setText(getString(R.string.search_error_message));
		generalView.findViewById(R.id.noContent).setVisibility(View.GONE);
		generalView.findViewById(R.id.errorContent).setVisibility(View.VISIBLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.SearchDAO.SearchDAOListener#onFailureNoResultSearch
	 * ()
	 */
	@Override
	public void onFailureNoResultSearch() {
		SearchDAO.getInstance(mContext).removeListener(this);
		searchAdapter.resetItems();
		((HomeActivity) getActivity()).stopAnimation();
		((TextView) generalView.findViewById(R.id.errorMessage))
				.setText(getString(R.string.search_error_noresult_message));
		generalView.findViewById(R.id.noContent).setVisibility(View.GONE);
		generalView.findViewById(R.id.errorContent).setVisibility(View.VISIBLE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.prisacom.as.dao.reader.SearchDAO.SearchDAOListener#onFailureNotConnection
	 * ()
	 */
	@Override
	public void onFailureNotConnection() {
		SearchDAO.getInstance(mContext).removeListener(this);
		searchAdapter.resetItems();
		searchEditText.setText("");
		closeKeyboard();
		((HomeActivity) getActivity()).stopAnimation();
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((TextView) generalView.findViewById(R.id.errorMessage))
								.setText(getString(R.string.section_download_error));
						generalView.findViewById(R.id.noContent).setVisibility(
								View.GONE);
						generalView.findViewById(R.id.errorContent)
								.setVisibility(View.VISIBLE);
					}

				});

	}

	/********************************************************************************************/
	/** Adapter **/
	/*******************************************************************************************/
	class SearchAdapter extends BaseAdapter {

		private ArrayList<SearchItem> items = new ArrayList<SearchItem>();

		public SearchAdapter() {
		}

		/**
		 * 
		 */
		public void resetItems() {
			this.items = new ArrayList<SearchItem>();
			notifyDataSetChanged();
		}

		public void addItems(ArrayList<SearchItem> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return items.size();

		}

		@Override
		public SearchItem getItem(int arg0) {
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SearchItem item = getItem(position);
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_list_search, null);

				FontUtils.setCustomfont(mContext,
						convertView.findViewById(R.id.titleText),
						FontTypes.ROBOTO_REGULAR);
				FontUtils.setCustomfont(mContext,
						convertView.findViewById(R.id.aliasText),
						FontTypes.ROBOTO_REGULAR);

			}

			TextView titleText = (TextView) convertView
					.findViewById(R.id.titleText);
			String nombre = manageString(item.getNombre());
			titleText.setText(Html.fromHtml(nombre));

			TextView aliasText = (TextView) convertView
					.findViewById(R.id.aliasText);
			String alias = manageString(item.getAlias());
			aliasText.setText(Html.fromHtml(alias));

			ImageView image = (ImageView) convertView.findViewById(R.id.image);

			final String[] ids = item.getId().split("#");

			if (ids[0].equalsIgnoreCase("fce")) {
				image.setBackgroundResource(R.drawable.img_escudostandard);
			} else {
				image.setBackgroundResource(R.drawable.img_jugadorstandard);
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goToDetail(ids[0].equalsIgnoreCase("fce"), ids[1],
							item.getUrlDatos());

				}
			});
			return convertView;

		}

		private String manageString(String string) {

			if (string.contains(search)) {
				string = string
						.replace(search, "<b><font face="
								+ FontTypes.ROBOTO_BLACK + ">" + search
								+ "</font></b>");
			}

			String search2 = search.toUpperCase();
			if (string.contains(search2)) {
				string = string.replace(search2, "<b><font face="
						+ FontTypes.ROBOTO_BLACK + ">" + search2
						+ "</font></b>");
			}

			String search3 = search.toLowerCase();
			if (string.contains(search3)) {
				string = string.replace(search3, "<b><font face="
						+ FontTypes.ROBOTO_BLACK + ">" + search3
						+ "</font></b>");
			}
			return string;
		}

	}
}
