package com.diarioas.guiamundial.activities.player.comparator.fragment;

import android.content.Intent;
import android.util.Log;

import com.diarioas.guiamundial.R;
import com.diarioas.guiamundial.activities.home.fragment.CompetitionHomeGroupFragment;
import com.diarioas.guiamundial.activities.player.comparator.PlayerComparatorStepThirdActivity;
import com.diarioas.guiamundial.utils.Defines.ReturnRequestCodes;

public class ComparatorPlayerSelectTeamGroup extends CompetitionHomeGroupFragment {

	@Override
	protected void selectedTeam(String teamId) {
		Log.d("COMPARATOR", "ButtonClicked: " + teamId);
		Intent intent = new Intent(getActivity(),
				PlayerComparatorStepThirdActivity.class);
		intent.putExtra("teamId", teamId);
		intent.putExtra("competitionId", competitionId);
		getActivity().startActivityForResult(intent,
				ReturnRequestCodes.COMPARATORPLAYER_RETURN_OK);
		getActivity().overridePendingTransition(R.anim.grow_from_middle,
				R.anim.shrink_to_middle);
	}

}
