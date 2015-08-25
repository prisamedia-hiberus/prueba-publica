package com.diarioas.guialigas.activities.stadiums.fragments;

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
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.activities.stadiums.StadiumsDetailActivity;
import com.diarioas.guialigas.dao.model.stadium.Stadium;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteStadiumsDAO;
import com.diarioas.guialigas.dao.reader.RemoteStadiumsDAO.RemoteStadiumsDAOListener;
import com.diarioas.guialigas.dao.reader.StatisticsDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.Omniture;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

import java.util.ArrayList;

public class StadiumSectionFragment extends ListViewSectionFragment implements
		RemoteStadiumsDAOListener {

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.adSection = NativeAds.AD_STADIUMS + "/" + NativeAds.AD_PORTADA;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_stadium_section,
				container, false);
		return generalView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		RemoteStadiumsDAO.getInstance(mContext).removeListener(this);
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/
	@Override
	protected void loadInformation() {
		if (RemoteStadiumsDAO.getInstance(mContext).isStadiumsPreLoaded(
				competitionId)) {
			array = RemoteStadiumsDAO.getInstance(mContext)
					.getStadiumsPreLoaded(competitionId);
			loadData();
		} else {
			RemoteStadiumsDAO.getInstance(mContext).addListener(this);
			RemoteStadiumsDAO.getInstance(mContext).getStadiumsInfo(
					competitionId, section.getUrl());
		}
	}

	@Override
	protected void configureView() {

		super.configureView();

		array = new ArrayList<Stadium>();
		adapter = new StadiumAdapter();
		contentListView = (ListView) generalView
				.findViewById(R.id.stadiumsContent);
		configureListView();

	}

	@Override
	protected void goToDetail(Object id) {
		Intent intent = new Intent(getActivity(), StadiumsDetailActivity.class);
		intent.putExtra("id", (Integer) id);
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
				Omniture.SECTION_SEDES,
				Omniture.SUBSECTION_PORTADA,
				null,
				null,
				Omniture.TYPE_ARTICLE,
				Omniture.SECTION_SEDES + " " + Omniture.TYPE_PORTADA,
				null);
	}

	/***************************************************************************/
	/** RemoteStadiumsDAOListener methods **/
	/***************************************************************************/
	@Override
	public void onSuccessRemoteconfig(ArrayList<Stadium> stadiums) {
		RemoteStadiumsDAO.getInstance(mContext).removeListener(this);
		this.array = stadiums;
		headerView = null;
		loadData();
		stopAnimation();
	}

	@Override
	public void onFailureRemoteconfig() {
		RemoteStadiumsDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.stadium_error),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (RemoteStadiumsDAO.getInstance(mContext)
								.isStadiumsDeepPreLoaded(competitionId))
							array = RemoteStadiumsDAO.getInstance(mContext)
									.getStadiumsPreLoaded(competitionId);

						loadOnFailure();
					}

				});

		((HomeActivity) getActivity()).stopAnimation();
	}

	@Override
	public void onFailureNotConnection() {
		RemoteStadiumsDAO.getInstance(mContext).removeListener(this);
		AlertManager.showAlertOkDialog(getActivity(),
				getResources().getString(R.string.stadium_not_conection),
				getResources().getString(R.string.connection_error_title),
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						array = DatabaseDAO.getInstance(mContext).getStadiums(
								getArguments().getString("competitionId"));
						loadOnFailure();
					}

				});

		((HomeActivity) getActivity()).stopAnimation();

	}

	/***************************************************************************/
	/** StadiumAdapter **/
	/***************************************************************************/
	class StadiumAdapter extends BaseAdapter {

		private static final int NORMAL_TYPE = 0;
		private static final int ERROR_TYPE = NORMAL_TYPE + 1;
		private static final int TOTAL_TYPE = ERROR_TYPE + 1;

		public StadiumAdapter() {

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
			return array.get(arg0);
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
					convertView = inflater.inflate(R.layout.item_list_stadium,
							null);
					holder.cityText = (TextView) convertView
							.findViewById(R.id.city);
					FontUtils.setCustomfont(mContext, holder.cityText,
							FontTypes.ROBOTO_REGULAR);
					holder.nameText = (TextView) convertView
							.findViewById(R.id.name);
					FontUtils.setCustomfont(mContext, holder.nameText,
							FontTypes.ROBOTO_BOLD);

					holder.image = (ImageView) convertView
							.findViewById(R.id.image);

					convertView.setTag(holder);
					// convertView.startAnimation(getAnimation(position,
					// convertView));
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				Stadium stadium = (Stadium) array.get(position);
				holder.cityText.setText(stadium.getCityName());
				holder.nameText.setText(stadium.getStadiumName());

				if (stadium.getPhoto() != null
						&& stadium.getPhoto().length() > 0) {
					int id = DrawableUtils.getDrawableId(mContext,
							stadium.getPhoto(), 4);
					if (id != 0) {
						holder.image.setBackgroundResource(id);
					}
				}

				if (stadium.getUrlInfo() != null
						&& stadium.getUrlInfo().length() > 0) {
					holder.image.setTag(stadium.getId());
					convertView.setOnClickListener(new OnClickListener() {

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

	}

	static class ViewHolder {

		public ImageView image;
		public TextView nameText;
		public TextView cityText;

	}
}
