package com.diarioas.guialigas.activities.home.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.utils.Defines.SECTIONS;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public class HomeMenuFragment extends Fragment {

	Context mContext = null;
	View generalView = null;
	LayoutInflater mInflater = null;

	private ListView sectionList;
	private MenuSectionsAdapter menuSectionsAdapter;
	private ArrayList<Section> menuSections;

	private String currentSelectedType;

	/******** Fragment lifecycle methods *************************/
	/************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mInflater = inflater;
		this.generalView = inflater.inflate(R.layout.menu_behind_profiles,
				container, false);

		return this.generalView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		configureView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (this.menuSections != null) {
			this.menuSections.clear();
			this.menuSections = null;
		}
	}

	/******** Configure View methods ****************************/
	/************************************************************/

	private void configureView() {
		if (this.generalView != null) {

			int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
					.getGeneralSettings().getCurrentCompetition().getId();
			this.menuSections = RemoteDataDAO.getInstance(this.mContext)
					.getOrderedSections(currentCompetitionId);

			this.sectionList = (ListView) this.generalView
					.findViewById(R.id.sectionList);
			this.sectionList.setDivider(null);
			this.sectionList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			this.sectionList.setItemsCanFocus(true);
			this.sectionList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long arg3) {
					if (((HomeActivity) getActivity())
							.getSelectedSectionIndex() == position) {
						((HomeActivity) getActivity()).setSectionChanged(false);
					} else {
						Section selectedSection = menuSections.get(position);
						if (selectedSection.isActive()) {
							if (selectedSection.getType().equalsIgnoreCase(
									SECTIONS.LINK)) {
								((HomeActivity) getActivity())
										.openLink(selectedSection.getUrl());
							}else if (selectedSection.getType().equalsIgnoreCase(
									SECTIONS.ORDER)) {
								((HomeActivity) getActivity())
								.openSortSection();
							} else {
								if (!selectedSection.getType()
										.equalsIgnoreCase(SECTIONS.SEARCHER))

									((HomeActivity) getActivity())
											.startAnimation();
								((HomeActivity) getActivity())
										.setSectionChanged(true);
								((HomeActivity) getActivity())
										.setSelectedSectionIndex(position);
							}
							((HomeActivity) getActivity()).togglePane();
						}
					}
				}
			});

			this.menuSectionsAdapter = new MenuSectionsAdapter();
			this.menuSectionsAdapter.addItems(this.menuSections);
			this.sectionList.setAdapter(this.menuSectionsAdapter);
		}
	}

	/******** Configure View methods ****************************/
	/************************************************************/

	public void setCurrentCompetition(int position) {
		RemoteDataDAO.getInstance(this.mContext).getGeneralSettings()
				.setCurrentCompetition(position);
		int currentCompetitionId = RemoteDataDAO.getInstance(this.mContext)
				.getGeneralSettings().getCurrentCompetition().getId();
		this.menuSections = RemoteDataDAO.getInstance(this.mContext)
				.getOrderedSections(currentCompetitionId);
		this.menuSectionsAdapter.addItems(menuSections);
	}

	public Section getItemAtPosition(int item) {
		if (sectionList != null) {
			return (Section) sectionList.getItemAtPosition(item);
		} else {
			return null;
		}
	}

	public void reloadMenuList(String type) {

		this.currentSelectedType = type;
		if (this.menuSectionsAdapter != null) {
			this.menuSectionsAdapter.notifyDataSetChanged();
		}
	}

	/***************************************************************************/
	/** Left section list adapter **/
	/***************************************************************************/

	private class MenuSectionsAdapter extends BaseAdapter {

		private ArrayList<Section> items = new ArrayList<Section>();
		private final LayoutInflater inflater;

		public MenuSectionsAdapter() {
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addItems(ArrayList<Section> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Section getItem(int arg0) {
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Section item = items.get(position);
			ViewHolder holder;
			if (convertView == null) {

				convertView = inflater
						.inflate(R.layout.left_menu_section, null);

				holder = new ViewHolder();
				// Section icon
				holder.sectionIcon = (ImageView) convertView
						.findViewById(R.id.sectionIcon);
				holder.selectedSectionIcon = (ImageView) convertView
						.findViewById(R.id.selectedSectionIcon);
				// Section title
				holder.sectionName = (TextView) convertView
						.findViewById(R.id.sectionTitle);
				FontUtils.setCustomfont(mContext, holder.sectionName,
						FontTypes.ROBOTO_LIGHT);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String name = "";
			int img = 0;
			// Log.d("SECTIONS", "Seccion de tipo: " + item.getType());
			if (item.getType().equalsIgnoreCase(SECTIONS.CALENDAR)) {
				name = getString(R.string.menu_calendario);
				img = R.drawable.sliding_menu_icon_calendario;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.CARROUSEL)) {
				name = getString(R.string.menu_carrusel);
				img = R.drawable.icn_menu_carrusel;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.CLASIFICATION)) {
				name = getString(R.string.menu_clasificacion);
				img = R.drawable.icn_menu_clasificacion;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.PALMARES)) {
				name = getString(R.string.menu_palmares);
				img = R.drawable.sliding_menu_icon_palmares;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.STADIUMS)) {
				name = getString(R.string.menu_sedes);
				img = R.drawable.sliding_menu_icon_sedes;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.TEAMS)) {
				name = getString(R.string.menu_teams);
				img = R.drawable.icn_menu_equipos;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.ORDER)) {
				name = getString(R.string.menu_order);
				img = R.drawable.icn_menu_noticias;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.COMPARATOR)) {
				name = getString(R.string.menu_comparador_jugadores);
				img = R.drawable.icn_menu_comparador;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.SEARCHER)) {
				name = getString(R.string.menu_search);
				img = R.drawable.icn_menu_buscador;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.NEWS) || item.getType().equalsIgnoreCase(SECTIONS.NEWS_TAG)) {
				name = getString(R.string.menu_news);
				img = R.drawable.icn_menu_noticias;				
			} else if (item.getType().equalsIgnoreCase(SECTIONS.PHOTOS)) {
				name = getString(R.string.menu_photos);
				img = R.drawable.icn_menu_fotos;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.VIDEOS)) {
				name = getString(R.string.menu_videos);
				img = R.drawable.icn_menu_videos;
			} else if (item.getType().equalsIgnoreCase(SECTIONS.LINK)) {
//				name = getString(R.string.menu_trivias);
				name = item.getName();
				img = R.drawable.icn_menu_trivias;
			}

			holder.sectionName.setText(name);
			if (item.getType().equalsIgnoreCase(currentSelectedType)) {
				holder.selectedSectionIcon.setVisibility(View.VISIBLE);
			} else {
				holder.selectedSectionIcon.setVisibility(View.GONE);
			}
			holder.sectionIcon.setBackgroundResource(img);
			if (!item.isActive()) {
				AlphaAnimation alpha = new AlphaAnimation(1, 0.5F);
				alpha.setDuration(0); // Make animation instant
				alpha.setFillAfter(true);
				convertView.startAnimation(alpha);
				convertView.setClickable(true);
			} else {
				AlphaAnimation alpha = new AlphaAnimation(0.5F, 1);
				alpha.setDuration(0); // Make animation instant
				alpha.setFillAfter(true);
				convertView.startAnimation(alpha);
				convertView.setClickable(false);
			}

			return convertView;
		}
	}

	private static class ViewHolder {

		public TextView sectionName;
		public ImageView selectedSectionIcon;
		public ImageView sectionIcon;
	}
}
