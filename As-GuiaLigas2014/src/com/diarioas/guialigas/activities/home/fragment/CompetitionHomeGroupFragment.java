package com.diarioas.guialigas.activities.home.fragment;

import java.util.ArrayList;

import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.model.competition.Group;
import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public class CompetitionHomeGroupFragment extends CompetitionHomeFragment {

	private final int NUMTEAMSROW = 4;
	private ArrayList<Group> groups;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (groups != null) {
			groups.clear();
			groups = null;
		}

	}

	@Override
	protected void configureView() {
		groups = DatabaseDAO.getInstance(mContext).getInfoGroups(competitionId);

		if (groups.size() == 0)
			return;

		// Header
		if (!getArguments().getBoolean("manyCompetititons"))
			linear.addView(getGapView(DimenUtils.getRegularPixelFromDp(
					mContext, 15)));

		ArrayList<Team> teams;
		View convertView;
		LinearLayout teamsContent;
		TextView nameGroup;
		if (groups.size() > 1) {
			for (int i = 0; i < groups.size(); i++) {
				convertView = inflater.inflate(R.layout.item_grid_group, null);
				nameGroup = (TextView) convertView.findViewById(R.id.nameGroup);
				FontUtils.setCustomfont(mContext, nameGroup,
						FontTypes.HELVETICANEUE);
				nameGroup.setText(getString(R.string.group)
						+ groups.get(i).getName());
				teamsContent = (LinearLayout) convertView
						.findViewById(R.id.teamsContent);
				teams = groups.get(i).getTeams();
				fillLinearGroup(teams, teamsContent);
				linear.addView(convertView);
			}
		} else {
			convertView = inflater.inflate(R.layout.item_grid_group, null);
			convertView.findViewById(R.id.nameGroup).setVisibility(View.GONE);

			teamsContent = (LinearLayout) convertView
					.findViewById(R.id.teamsContent);
			teams = groups.get(0).getTeams();
			fillLinearGroup(teams, teamsContent);
			linear.addView(convertView);
		}
		// Footer
		linear.addView(getGapView(DimenUtils
				.getRegularPixelFromDp(mContext, 50)));

	}

	private LinearLayout fillLinearGroup(ArrayList<Team> teams,
			LinearLayout teamsContent) {

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		int width = (size.x) / NUMTEAMSROW;
		LinearLayout.LayoutParams paramsItem = new LinearLayout.LayoutParams(
				width, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		View convertView;
		LinearLayout ll;
		ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setGravity(Gravity.CENTER_VERTICAL);

		for (int j = 0; j < teams.size(); j++) {

			convertView = getTeamView(teams.get(j));

			ll.addView(convertView, paramsItem);
			if ((j + 1) % NUMTEAMSROW == 0) {
				teamsContent.addView(ll, paramsLinear);
				ll = new LinearLayout(mContext);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setGravity(Gravity.CENTER_VERTICAL);
			}
		}
		int mod;
		if (teams.size() < NUMTEAMSROW)
			mod = NUMTEAMSROW - teams.size();
		else
			mod = NUMTEAMSROW - (teams.size() % NUMTEAMSROW);

		if (mod > 0) {
			for (int j = 0; j < mod; j++) {
				ll.addView(new View(mContext), paramsItem);
			}
		}
		teamsContent.addView(ll, paramsLinear);

		return teamsContent;
	}

}
