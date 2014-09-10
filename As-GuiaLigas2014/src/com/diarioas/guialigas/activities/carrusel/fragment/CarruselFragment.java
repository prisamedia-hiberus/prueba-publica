/**
 * 
 */
package com.diarioas.guialigas.activities.carrusel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.GeneralFragment;
import com.diarioas.guialigas.activities.player.PlayerActivity;
import com.diarioas.guialigas.dao.reader.CarruselDAO;
import com.diarioas.guialigas.utils.Defines.ReturnRequestCodes;

/**
 * @author robertosanchez
 * 
 */
public class CarruselFragment extends GeneralFragment {

	protected LayoutInflater inflater;
	protected String url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		setRetainInstance(true);
		configureView();

		return generalView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		url = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		CarruselDAO.getInstance(mContext).cleanCache(url);
		url = null;
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
		generalView = null;
	}

	/**
	 * 
	 */
	protected void configureView() {

		spinner = (RelativeLayout) generalView.findViewById(R.id.spinner);
		if (getArguments().containsKey("url"))
			url = getArguments().getString("url");
	}

	protected void goToPlayer(int tag, String url) {
		// String urlLocal =
		// DatabaseDAO.getInstance(mContext).getPlayer(tag).getUrl();
		 if (url != null && url.length() > 1) {
			Intent intent = new Intent(getActivity(), PlayerActivity.class);
			intent.putExtra("playerId", tag);
			intent.putExtra("playerUrl", url);
			// intent.putExtra("teamName", currentTeam.getShortName());
			startActivityForResult(intent, ReturnRequestCodes.PUBLI_BACK);
			getActivity().overridePendingTransition(R.anim.grow_from_middle,
					R.anim.shrink_to_middle);
		// } else {
		// AlertManager.showAlertOkDialog(getActivity(),
		// mContext.getString(R.string.player_detail_error),
		// mContext.getString(R.string.connection_error_title),
		//
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		 }
	}

	/**
	 * 
	 */
	public void updateData() {
		if (generalView == null) {
			Log.d("CARRUSELUPDATE", "No upUpdating: "
					+ this.getClass().getCanonicalName());
			return;
		} else {
			Log.d("CARRUSELUPDATE", "Updating: "
					+ this.getClass().getCanonicalName());
			CarruselDAO.getInstance(mContext).cleanCache(url);
		}

	}
}
