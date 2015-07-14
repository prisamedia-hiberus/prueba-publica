package com.diarioas.guialigas.activities.sort;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.dao.model.competition.Competition;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.diarioas.guialigas.utils.listview.DynamicListView;
import com.diarioas.guialigas.utils.listview.DynamicListView.DynamicListViewCustomListener;

public class SortActivity extends GeneralFragmentActivity implements
		DynamicListViewCustomListener {

	private static final String TAG = "SortActivity";
	private DynamicListView competitionList;
	private ListAdapter competitionAdapter;
	private LayoutInflater inflater;
	private Context mContext;
	private View currentSelectedView;
	private boolean changeList = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setContentView(R.layout.activity_sort);
		spinner = (RelativeLayout) findViewById(R.id.spinner);

		configActionBar();
		configView();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_down);
	}

	@Override
	protected void configActionBar() {
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setCustomView(R.layout.header_bar_sort);

		getSupportActionBar().getCustomView().findViewById(R.id.doneContent)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						saveAction();

					}
				});

		getSupportActionBar().getCustomView().findViewById(R.id.cancelContent)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});

	}

	/************************************************************************************************
	 * Configuration Methods
	 *************************************************************************************************/
	private void configView() {

		FontUtils.setCustomfont(mContext, findViewById(R.id.sortMessage),
				FontTypes.ROBOTO_REGULAR);

		ArrayList<Competition> competitions = RemoteDataDAO.getInstance(mContext).getOrderedCompetitions();

		competitionList = (DynamicListView) findViewById(R.id.competitionList);
		competitionList.setCustomListener(this);
		competitionList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.competitionList.setDivider(null);
		this.competitionList.setDividerHeight(0);
		this.competitionList.setCacheColorHint(0);
		this.competitionList.setList(competitions);

		competitionAdapter = new CompetitionAdapter(competitions);

		competitionList.setAdapter(competitionAdapter);

	}

	private void saveAction() {
		if (changeList) {
			ArrayList<Competition> competitions = (ArrayList<Competition>) this.competitionList.getList();
			//save new order
			RemoteDataDAO.getInstance(mContext).updateOrderCompetition(competitions);
		}
		onBackPressed();
	}

	/************************************************************************************************
	 * DynamicListViewCustomListener Methods
	 *************************************************************************************************/
	@SuppressLint("NewApi")
	@Override
	public void changeSelectedView(long mMobileItemId, View selectedView) {
		Log.d(TAG, "changeSelectedView: "+mMobileItemId);
		if (currentSelectedView == null) {
			this.currentSelectedView = selectedView;
		
			currentSelectedView.setBackgroundColor(getResources().getColor(R.color.gray_sort_background));
			((ImageView) currentSelectedView.findViewById(R.id.imageReordering)).setImageDrawable(getResources().getDrawable(R.drawable.icn_draganddrop_on));
			((ImageView) currentSelectedView.findViewById(R.id.separator_sort)).setVisibility(View.GONE);
			FontUtils.setCustomfont(mContext, ((TextView) currentSelectedView.findViewById(R.id.title)),FontTypes.ROBOTO_REGULAR);
		}

	}

	@Override
	public void unChangeSelectedView(long mMobileItemId) {
		Log.d(TAG, "unChangeSelectedView: "+mMobileItemId);
		if (currentSelectedView != null) {
			currentSelectedView.setBackgroundColor(getResources().getColor(R.color.white));
			((ImageView) currentSelectedView.findViewById(R.id.imageReordering)).setImageDrawable(getResources().getDrawable(R.drawable.icn_draganddrop_off));
			((ImageView) currentSelectedView.findViewById(R.id.separator_sort)).setVisibility(View.VISIBLE);
			FontUtils.setCustomfont(mContext, ((TextView) currentSelectedView.findViewById(R.id.title)),FontTypes.ROBOTO_LIGHT);
			
			currentSelectedView = null;
		}
	}
	
	@Override
	public void swapElements() {
		((ImageView)getSupportActionBar().getCustomView().findViewById(R.id.doneButton)).setImageDrawable(getResources().getDrawable(R.drawable.icn_done_on));
		changeList = true;
	}

	/************************************************************************************************
	 * CompetitionAdapter Methods
	 *************************************************************************************************/
	class CompetitionAdapter extends ArrayAdapter<Competition> {

		private ArrayList<Competition> competitionsList;

		public CompetitionAdapter(ArrayList<Competition> competitions) {
			super(mContext, 0, competitions);

			this.competitionsList = competitions;
		}

		@Override
		public int getCount() {
			if (competitionsList != null) {
				return competitionsList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Competition getItem(int position) {
			if (competitionsList != null)
				return competitionsList.get(position);

			return null;
		}

		@Override
		public long getItemId(int position) {
			try {
				if (competitionsList != null)
					return competitionsList.get(position).getId();
				else
					return 0;
			} catch (Exception e) {
				return 0;
			}

		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			VHolder holder;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_sort_list, null);
				holder = new VHolder();

				holder.imageShield = (ImageView) convertView
						.findViewById(R.id.imageShield);
				holder.imageReordering = (ImageView) convertView
						.findViewById(R.id.imageReordering);
				holder.separator_sort = (ImageView) convertView
						.findViewById(R.id.separator_sort);

				holder.title = (TextView) convertView.findViewById(R.id.title);
			

				convertView.setTag(holder);
			} else {
				holder = (VHolder) convertView.getTag();
			}

			Competition competition = getItem(position);
			
			holder.title.setText(competition.getName());
			FontUtils.setCustomfont(mContext, holder.title,FontTypes.ROBOTO_LIGHT);
			
			convertView.setBackgroundColor(getResources().getColor(R.color.white));
			
			holder.separator_sort.setVisibility(View.VISIBLE);
			
			int icon = 0;
			if (competition.getImage() != null
					&& competition.getImage().length() > 0) {
				String url = competition.getImage().toLowerCase();
				url = url.replace("sliding_logo", "icn_order");
				icon = DrawableUtils.getDrawableId(mContext, url, 4);
			}
			holder.imageReordering.setImageDrawable(getResources().getDrawable(R.drawable.icn_draganddrop_off)); 
			if (icon > 0) {
				holder.imageShield.setImageDrawable(getResources().getDrawable(
						icon));
				holder.imageShield.setVisibility(View.VISIBLE);
			} else {
				holder.imageShield.setVisibility(View.INVISIBLE);
			}

			return convertView;

		}

	}

	public static class VHolder {

		public ImageView separator_sort;
		public ImageView imageReordering;
		public ImageView imageShield;
		public TextView title;

	}

}
