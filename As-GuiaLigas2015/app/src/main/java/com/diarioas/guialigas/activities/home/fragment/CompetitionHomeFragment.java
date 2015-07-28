package com.diarioas.guialigas.activities.home.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.team.TeamActivity;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.DrawableUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public abstract class CompetitionHomeFragment extends Fragment {

	protected Context mContext;
	protected LayoutInflater inflater;

	protected RelativeLayout generalView = null;
	protected LinearLayout linear;

	protected int competitionId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		competitionId = getArguments().getInt("competitionId");
		this.inflater = inflater;

		generalView = new RelativeLayout(mContext);
		generalView.setBackgroundColor(Color.WHITE);

		ScrollView scroll = getCustomScroll();
		generalView.addView(scroll);

		LayoutParams paramsGeneral = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		generalView.setLayoutParams(paramsGeneral);
		return generalView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		configureView();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mContext = null;
		inflater = null;

	}

	private ScrollView getCustomScroll() {
		ScrollView scroll = new ScrollView(mContext);
		scroll.setVerticalScrollBarEnabled(false);
		scroll.setHorizontalScrollBarEnabled(false);
		scroll.setId(competitionId * 100);
		scroll.setPadding(DimenUtils.getRegularPixelFromDp(mContext, 5),
				DimenUtils.getRegularPixelFromDp(mContext, 2),
				DimenUtils.getRegularPixelFromDp(mContext, 5),
				DimenUtils.getRegularPixelFromDp(mContext, 2));
		linear = new LinearLayout(mContext);
		linear.setOrientation(LinearLayout.VERTICAL);
		LayoutParams paramsLinear = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scroll.addView(linear, paramsLinear);
		RelativeLayout.LayoutParams paramsScroll = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramsScroll.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsScroll.addRule(RelativeLayout.CENTER_HORIZONTAL);

		scroll.setLayoutParams(paramsScroll);

		return scroll;
	}

	protected abstract void configureView();

	protected View getTeamView(Team team) {
		View convertView;
		ImageView photo;
		TextView name;

		convertView = inflater.inflate(R.layout.item_grid_team, null);
		convertView.setTag(team.getId());
		photo = (ImageView) convertView.findViewById(R.id.teamImage);
		name = (TextView) convertView.findViewById(R.id.teamName);

		name.setText(team.getShortName());
		FontUtils.setCustomfont(mContext, name, FontTypes.ROBOTO_REGULAR);
		int id = 0;
		if (team.getGridShield() != null
				&& !team.getGridShield().equalsIgnoreCase("")) {
			id = DrawableUtils.getDrawableId(mContext, team.getGridShield(), 4);
		}
		
		if (id==0){
			id = R.drawable.escudo_generico_size02;
		}
		photo.setBackgroundResource(id);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedTeam(String.valueOf(v.getTag()));
			}
		});
		return convertView;
	}

	protected View getGapView(int height) {
		View convertView;
		convertView = new View(mContext);
		convertView.setMinimumHeight(height);
		return convertView;
	}

	protected void selectedTeam(String teamId) {
		Intent intent = new Intent(mContext, TeamActivity.class);
		intent.putExtra("teamId", teamId);
		intent.putExtra("competitionId", String.valueOf(competitionId));

		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.PUBLI_BACK);
		getActivity().overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);
	}

}
