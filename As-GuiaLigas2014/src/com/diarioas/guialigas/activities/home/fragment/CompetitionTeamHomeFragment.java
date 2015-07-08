package com.diarioas.guialigas.activities.home.fragment;

import java.util.ArrayList;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.diarioas.guialigas.dao.model.team.Team;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.utils.DimenUtils;

public class CompetitionTeamHomeFragment extends CompetitionHomeFragment {

	private final int NUMTEAMSROW = 4;
	private ArrayList<Team> teams;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (teams != null) {
			teams.clear();
			teams = null;
		}

	}

	@Override
	protected void configureView() {
		teams = DatabaseDAO.getInstance(mContext).getShortInfoTeams(
				competitionId);
		if (teams.size() == 0)
			return;

		if (!getArguments().getBoolean("manyCompetititons"))
			linear.addView(getGapView(DimenUtils.getRegularPixelFromDp(
					mContext, 15)));

		View convertView;
		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		int width = (size.x) / NUMTEAMSROW;

		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams paramsItem = new LinearLayout.LayoutParams(
				width, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		for (int i = 0; i < teams.size(); i++) {
			convertView = getTeamView(teams.get(i));

			ll.addView(convertView, paramsItem);
			if ((i + 1) % NUMTEAMSROW == 0) {
				linear.addView(ll, paramsLinear);
				ll = new LinearLayout(mContext);
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

		linear.addView(ll, paramsLinear);

		// Footer
		linear.addView(getGapView(DimenUtils
				.getRegularPixelFromDp(mContext, 50)));
	}

}
