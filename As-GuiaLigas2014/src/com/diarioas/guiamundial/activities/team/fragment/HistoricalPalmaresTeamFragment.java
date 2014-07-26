package com.diarioas.guiamundial.activities.team.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.dao.model.competition.Competition;
import com.diarioas.guiamundial.dao.model.team.PalmaresLabel;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.utils.Defines.LEGEND_TYPE;
import com.diarioas.guiamundial.utils.DimenUtils;
import com.diarioas.guiamundial.utils.FontUtils;
import com.diarioas.guiamundial.utils.FontUtils.FontTypes;

public class HistoricalPalmaresTeamFragment extends TeamFragment {

	private PalmaresAdapter palmnaresAdapter;
	private LayoutInflater inflater;
	private int competitionId;
	private int teamId;
	private ArrayList<View> views;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;

		competitionId = Integer.valueOf(getArguments().getString(
				"competitionId"));
		teamId = Integer.valueOf(getArguments().getString("teamId"));

		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_team_hispalmares,
				container, false);

		return generalView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		inflater = null;
		if (palmnaresAdapter != null) {
			palmnaresAdapter.clear();
			palmnaresAdapter = null;
		}

		if (views != null) {
			views.clear();
			views = null;
		}
	}

	@Override
	protected void configureView() {
		configureHeaderView();
		configureListView();

	}

	@Override
	protected void startFirstShow() {
		// startAnimation(0);
	}

	// private void startAnimation(final int numView) {
	// if (numView == views.size())
	// return;
	//
	// final View view = views.get(numView).findViewById(R.id.viewBlue);
	// views.get(numView).findViewById(R.id.worldCup)
	// .setVisibility(View.INVISIBLE);
	//
	// ScaleAnimation animation = new ScaleAnimation(0, 1.0F, 1.0F, 1.0F);
	// animation.setFillAfter(true);
	// animation.setDuration(900);
	//
	// animation.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	// view.setVisibility(View.VISIBLE);
	// try {
	// Thread.sleep(150);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// startAnimation(numView + 1);
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// view.clearAnimation();
	// if ((Boolean) view.getTag()) {
	// showCup(views.get(numView).findViewById(R.id.worldCup));
	// }
	// }
	// });
	// view.startAnimation(animation);
	// }
	//
	// protected void showCup(final View view) {
	// Animation animation = AnimationUtils.loadAnimation(mContext,
	// R.anim.animation_worldcup_fadein);
	// animation.setAnimationListener(new AnimationListener() {
	//
	// @Override
	// public void onAnimationStart(Animation animation) {
	// view.setVisibility(View.VISIBLE);
	//
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// // TODO Auto-generated method stub
	// view.clearAnimation();
	// }
	// });
	//
	// view.setAnimation(animation);
	//
	// }

	private void configureHeaderView() {
		LinearLayout headerBar = (LinearLayout) generalView
				.findViewById(R.id.headerBar);
		TextView competitionName = (TextView) generalView
				.findViewById(R.id.competitionName);
		FontUtils.setCustomfont(mContext, competitionName,
				FontTypes.HELVETICANEUE);

		Competition competition = DatabaseDAO.getInstance(mContext)
				.getCompetition(competitionId);
		// competitionName.setText(competition.getName());
		competitionName.setText("MUNDIAL");

		ArrayList<String> labels = DatabaseDAO.getInstance(mContext)
				.getPalmaresHistoricoLabels(competitionId);
		View view;
		TextView itemText;
		LinearLayout.LayoutParams params;
		for (String label : labels) {
			view = inflater.inflate(R.layout.item_list_his_palmares_header,
					null);
			itemText = (TextView) view.findViewById(R.id.itemText);
			FontUtils
					.setCustomfont(mContext, itemText, FontTypes.HELVETICANEUE);
			itemText.setText(label);
			params = new LinearLayout.LayoutParams(0,
					LayoutParams.MATCH_PARENT, 1);

			headerBar.addView(view, params);
		}
	}

	private void configureListView() {
		views = new ArrayList<View>();
		ArrayList<PalmaresLabel> labels = DatabaseDAO.getInstance(mContext)
				.getPalmaresHistoricoYears(competitionId);
		ArrayList<String> teamPalmares = DatabaseDAO.getInstance(mContext)
				.getPalmaresTeamHistoricoYears(String.valueOf(competitionId),
						String.valueOf(teamId));

		palmnaresAdapter = new PalmaresAdapter(labels, teamPalmares);

		ListView palmaresContent = (ListView) generalView
				.findViewById(R.id.palmaresContent);
		palmaresContent.addFooterView(getFooter());
		palmaresContent.setDivider(null);
		palmaresContent.setClickable(false);
		palmaresContent.setCacheColorHint(0);

		palmaresContent.setAdapter(palmnaresAdapter);

	}

	private LinearLayout getFooter() {
		LinearLayout footerBar = new LinearLayout(mContext);
		footerBar.setOrientation(LinearLayout.VERTICAL);
		AbsListView.LayoutParams paramsLinear = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		footerBar.setLayoutParams(paramsLinear);

		ArrayList<String> legends = DatabaseDAO.getInstance(mContext)
				.getPalmaresHistoricoLegend(competitionId);

		int topMargin = DimenUtils.getRegularPixelFromDp(mContext, 10);

		footerBar.addView(getGapView(topMargin));

		for (int i = (legends.size() - 1); i >= 0; i--) {
			footerBar.addView(getItem(topMargin, legends.get(i), i + 1));
		}

		footerBar.addView(getGapView(DimenUtils.getRegularPixelFromDp(mContext,
				getResources().getDimension(R.dimen.padding_footer))));
		return footerBar;
	}

	private View getItem(int topMargin, String legend, int type) {
		View convertView = inflater.inflate(
				R.layout.item_list_his_palmares_legend, null);
		TextView textLegend = (TextView) convertView
				.findViewById(R.id.textLegend);
		FontUtils.setCustomfont(mContext, textLegend, FontTypes.HELVETICANEUE);
		textLegend.setText(legend);

		ImageView iconLegend = (ImageView) convertView
				.findViewById(R.id.iconLegend);
		switch (type) {
		case LEGEND_TYPE.LEGEND_TYPE_1:
			iconLegend.setBackgroundResource(R.drawable.estrella_amarilla);
			break;
		case LEGEND_TYPE.LEGEND_TYPE_2:
			iconLegend.setBackgroundResource(R.drawable.estrella_naranja);
			break;
		case LEGEND_TYPE.LEGEND_TYPE_3:
			iconLegend.setBackgroundResource(R.drawable.estrella_roja);
			break;

		default:
			break;
		}
		return convertView;
	}

	private View getGapView(int topMargin) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1,
				topMargin);
		View gapView = new View(mContext);
		gapView.setLayoutParams(params);

		return gapView;
	}

	class PalmaresAdapter extends BaseAdapter {

		private ArrayList<PalmaresLabel> labels;
		private ArrayList<String> teamLabels;

		public PalmaresAdapter(ArrayList<PalmaresLabel> labels,
				ArrayList<String> teamPalmares) {
			this.labels = labels;
			this.teamLabels = teamPalmares;
		}

		public void clear() {
			labels.clear();
			labels = null;
			teamLabels.clear();
			teamLabels = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return labels.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return labels.get(arg0);
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

			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_list_his_palmares,
						null);
				holder.dataContent = (LinearLayout) convertView
						.findViewById(R.id.dataContent);
				holder.yearText = (TextView) convertView
						.findViewById(R.id.yearText);
				FontUtils.setCustomfont(mContext, holder.yearText,
						FontTypes.HELVETICANEUE);
				holder.countryText = (TextView) convertView
						.findViewById(R.id.countryText);
				FontUtils.setCustomfont(mContext, holder.countryText,
						FontTypes.HELVETICANEUE);

				holder.viewBlue = (ImageView) convertView
						.findViewById(R.id.viewBlue);
				holder.viewWhite = (ImageView) convertView
						.findViewById(R.id.viewWhite);
				holder.worldCup = (ImageView) convertView
						.findViewById(R.id.worldCup);
				holder.iconLegend = (ImageView) convertView
						.findViewById(R.id.iconLegend);
				views.add(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PalmaresLabel lab = labels.get(position);
			holder.yearText.setText(lab.getYear());
			holder.countryText.setText(lab.getCountry());

			LinearLayout.LayoutParams paramsBlue = (LinearLayout.LayoutParams) holder.viewBlue
					.getLayoutParams();
			int blueZone = 1;
			if (teamLabels.size() > position) {
				blueZone += Integer.valueOf(teamLabels.get(position));
				if (blueZone == 7) {
					holder.viewBlue.setTag(true);
					holder.worldCup.setVisibility(View.VISIBLE);
				} else {
					holder.viewBlue.setTag(false);
					holder.worldCup.setVisibility(View.GONE);
				}
			}
			paramsBlue.weight = blueZone;
			holder.viewBlue.setLayoutParams(paramsBlue);

			LinearLayout.LayoutParams paramsWhite = (LinearLayout.LayoutParams) holder.viewWhite
					.getLayoutParams();
			paramsWhite.weight = 7 - blueZone;
			holder.viewWhite.setLayoutParams(paramsWhite);

			if (lab.getLegend() != null) {
				((RelativeLayout) holder.iconLegend.getParent())
						.setVisibility(View.VISIBLE);
				switch (Integer.valueOf(lab.getLegend())) {
				case LEGEND_TYPE.LEGEND_TYPE_1:
					holder.iconLegend
							.setBackgroundResource(R.drawable.estrella_amarilla);
					break;
				case LEGEND_TYPE.LEGEND_TYPE_2:
					holder.iconLegend
							.setBackgroundResource(R.drawable.estrella_naranja);
					break;
				case LEGEND_TYPE.LEGEND_TYPE_3:
					holder.iconLegend
							.setBackgroundResource(R.drawable.estrella_roja);
					break;

				default:
					break;
				}
			} else {
				((RelativeLayout) holder.iconLegend.getParent())
						.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}

	}

	static class ViewHolder {

		public ImageView iconLegend;
		public ImageView worldCup;
		public ImageView viewWhite;
		public ImageView viewBlue;
		public LinearLayout dataContent;
		public TextView countryText;
		public TextView yearText;

	}
}
