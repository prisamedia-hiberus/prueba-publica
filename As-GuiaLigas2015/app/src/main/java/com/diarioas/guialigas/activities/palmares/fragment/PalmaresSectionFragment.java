package com.diarioas.guialigas.activities.palmares.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.ListViewSectionFragment;
import com.diarioas.guialigas.activities.palmares.PalmaresDetailActivity;
import com.diarioas.guialigas.dao.model.palmares.Palmares;
import com.diarioas.guialigas.dao.reader.RemotePalmaresDAO;
import com.diarioas.guialigas.dao.reader.RemotePalmaresDAO.RemotePalmaresDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FileUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

import java.util.ArrayList;
import java.util.HashMap;

public class PalmaresSectionFragment extends ListViewSectionFragment implements
		RemotePalmaresDAOListener {

	// private ArrayList<Palmares> array;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.adSection = NativeAds.AD_PALMARES + "/" + NativeAds.AD_PORTADA;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_palmares_section,
				container, false);
		return generalView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		RemotePalmaresDAO.getInstance(mContext).removeListener(this);
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@Override
	protected void loadInformation() {
		if (RemotePalmaresDAO.getInstance(mContext).isPalmaresLoaded(
				competitionId)) {
			array = RemotePalmaresDAO.getInstance(mContext)
					.getPalmaresPreloaded(competitionId);
			loadData();
		} else {
			RemotePalmaresDAO.getInstance(mContext).addListener(this);
			RemotePalmaresDAO.getInstance(mContext).getPalmaresInfo(
					section.getUrl(), competitionId);
		}

	}

	@Override
	protected void configureView() {
		super.configureView();

		array = new ArrayList<Palmares>();
		adapter = new PalmaresAdapter();

		contentListView = (ListView) generalView
				.findViewById(R.id.palmaresContent);

		configureListView();

	}

	@Override
	protected void goToDetail(Object url) {
		Intent intent = new Intent(getActivity(), PalmaresDetailActivity.class);
		intent.putExtra("url", (String) url);
		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.slide_in_left,
				R.anim.null_anim);

	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	@Override
	protected void callToOmniture() {
		StatisticsDAO.getInstance(mContext).sendStatisticsState(
				getActivity().getApplication(),
				FileUtils.readOmnitureProperties(mContext, "SECTION_PALMARES"),
				FileUtils.readOmnitureProperties(mContext, "SUBSECTION_PORTADA") + " " + FileUtils.readOmnitureProperties(mContext, "SECTION_PALMARES"),
				null,
				null,
                FileUtils.readOmnitureProperties(mContext, "TYPE_PORTADA"),
				null,
				null);
	}

	/***************************************************************************/
	/** RemotePalmaresDAOListener methods **/
	/***************************************************************************/

	@Override
	public void onSuccessRemoteconfig(ArrayList<Palmares> palmaresArray) {
		RemotePalmaresDAO.getInstance(mContext).removeListener(this);
		this.array = palmaresArray;
		headerView = null;
		loadData();
		stopAnimation();
	}

	@Override
	public void onFailureRemoteconfig() {
		RemotePalmaresDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadOnFailure();
					}

				});

		stopAnimation();
	}

	@Override
	public void onFailureNotConnection() {
		RemotePalmaresDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.section_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadOnFailure();
					}

				});

		stopAnimation();

	}

	/***************************************************************************/
	/** PalmaresAdapter **/
	/***************************************************************************/
	class PalmaresAdapter extends BaseAdapter {

		private static final int NORMAL_TYPE = 0;
		private static final int ERROR_TYPE = NORMAL_TYPE + 1;
		private static final int TOTAL_TYPE = ERROR_TYPE + 1;
		private final HashMap<String, Integer> winners;

		public PalmaresAdapter() {
			this.winners = new HashMap<String, Integer>();
		}

		private void reloadWinners(ArrayList<Palmares> palmares) {
			winners.clear();
			for (Palmares pal : palmares) {
				if (winners.containsKey(pal.getWinner())) {
					winners.put(pal.getWinner(),
							winners.get(pal.getWinner()) + 1);
				} else {
					winners.put(pal.getWinner(), 1);
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return TOTAL_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			if (headerView != null && position == 0)
				return ERROR_TYPE;
			else
				return NORMAL_TYPE;
		}

		@Override
		public void notifyDataSetChanged() {
			reloadWinners((ArrayList<Palmares>) array);
			super.notifyDataSetChanged();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			if (headerView == null)
				return array.size();
			else
				return array.size() + 1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
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

			int type = getItemViewType(position);
			if (type == ERROR_TYPE)
				return headerView;
			else {
				final ViewHolder holder;
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = inflater.inflate(R.layout.item_list_palmares,
							null);
					holder.resultText = (TextView) convertView
							.findViewById(R.id.result);
					FontUtils.setCustomfont(mContext, holder.resultText,
							FontTypes.ROBOTO_BLACK);
					FontUtils.setCustomfont(mContext,
							convertView.findViewById(R.id.resultLabel),
							FontTypes.ROBOTO_REGULAR);
					holder.finalistText = (TextView) convertView
							.findViewById(R.id.finalist);
					FontUtils.setCustomfont(mContext, holder.finalistText,
							FontTypes.ROBOTO_BLACK);
					FontUtils.setCustomfont(mContext,
							convertView.findViewById(R.id.finalistLabel),
							FontTypes.ROBOTO_REGULAR);
					holder.winnerText = (TextView) convertView
							.findViewById(R.id.winner);
					FontUtils.setCustomfont(mContext, holder.winnerText,
							FontTypes.ROBOTO_BLACK);
					holder.nameText = (TextView) convertView
							.findViewById(R.id.name);
					FontUtils.setCustomfont(mContext, holder.nameText,
							FontTypes.ROBOTO_REGULAR);
					FontUtils.setCustomfont(mContext,
							convertView.findViewById(R.id.nameLabel),
							FontTypes.ROBOTO_REGULAR);

					holder.image = (ImageView) convertView
							.findViewById(R.id.image);
					holder.flagWinnerImage = (ImageView) convertView
							.findViewById(R.id.flagWinnerImage);
					holder.flagStarsImage = (ImageView) convertView
							.findViewById(R.id.flagStarsImage);

					convertView.setTag(holder);
					// convertView.startAnimation(getAnimation(position,
					// convertView));
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				Palmares palmares = (Palmares) array.get(position);
				holder.flagStarsImage
						.setBackgroundResource(getStarResource(winners
								.get(palmares.getWinner())));
				holder.flagWinnerImage
						.setBackgroundResource(getFlagResource(palmares
								.getWinner()));
				holder.resultText.setText(palmares.getResult());
				holder.finalistText.setText(palmares.getFinalist());
				holder.winnerText.setText(palmares.getWinner());
				holder.nameText.setText(palmares.getName());

				if (palmares.getPhoto() != null
						&& palmares.getPhoto().length() > 0) {
					// holder.image.setBackgroundResource(R.drawable.palmares_fondo_2010);
					holder.image.setBackgroundResource(DrawableUtils
							.getDrawableId(mContext, palmares.getPhoto(), 4));
				}

				if (palmares.getUrl() != null && palmares.getUrl().length() > 0) {
					holder.image.setTag(palmares.getUrl());
					holder.image.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							goToDetail(holder.image.getTag());
						}
					});
				} else {
					holder.image.setTag(null);
					convertView.setOnClickListener(null);
				}

				return convertView;
			}
		}

		private int getFlagResource(String winner) {
//			if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_argentina))) {
//				return R.drawable.palmares_flag_argentina;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_brasil))) {
//				return R.drawable.palmares_flag_brazil;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_england))) {
//				return R.drawable.palmares_flag_england;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_france))) {
//				return R.drawable.palmares_flag_france;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_germany))) {
//				return R.drawable.palmares_flag_germany;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_italy))) {
//				return R.drawable.palmares_flag_italy;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_spain))) {
//				return R.drawable.palmares_flag_spain;
//			} else if (winner
//					.equalsIgnoreCase(getString(R.string.team_champion_uruguay))) {
//				return R.drawable.palmares_flag_uruguay;
//			}
			return 0;
		}

		private int getStarResource(Integer numStar) {
//			switch (numStar) {
//			case 1:
//				return R.drawable.team_1star;
//			case 2:
//				return R.drawable.team_2star;
//			case 3:
//				return R.drawable.team_3star;
//			case 4:
//				return R.drawable.team_4star;
//			case 5:
//				return R.drawable.team_5star;
//			case 6:
//				return R.drawable.team_6star;
//
//			default:
//				return R.drawable.team_1star;
//			}
			return 0;
		}

	}

	static class ViewHolder {

		public ImageView flagStarsImage;
		public ImageView flagWinnerImage;
		public TextView nameText;
		public TextView winnerText;
		public TextView finalistText;
		public TextView resultText;
		public ImageView image;

	}
}
