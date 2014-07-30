package com.diarioas.guialigas.activities.general.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.dao.reader.PubliDAO;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;
import com.prisadigital.realmedia.adlib.AdView;

public abstract class SectionFragment extends Fragment {

	protected Context mContext;
	protected View generalView;
	protected boolean firstExecution;
	protected Section section;
	protected String competitionId;
	private AdView banner;

	protected LayoutInflater inflater;

	protected boolean adsActive = true;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity().getApplicationContext();

		section = (Section) getArguments().getSerializable("section");
		competitionId = getArguments().getString("competitionId");

		this.firstExecution = true;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return generalView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState == null) {

			if (this.firstExecution) {
				configureView();
				this.firstExecution = false;
			}

		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adsActive) {
			adsActive = false;
			callToAds();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext = null;
		generalView = null;
		section = null;
		inflater = null;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	protected abstract void configureView();

	protected abstract void buildView();

	public void setCurrentcompetition(int position) {

	}

	public void onOpenSlidingMenu() {
		Log.d("SLIDINGMENU", "onOpenSlidingMenu");
	}

	public void onCloseSlidingMenu() {
		Log.d("SLIDINGMENU", "onCloseSlidingMenu");

	}

	public void closedSlidingMenu() {
		Log.d("SLIDINGMENU", "onCloseSlidingMenu");
		buildView();

	}

	/***************************************************************************/
	/** Libraries methods **/
	/***************************************************************************/
	protected abstract void callToOmniture();

	public abstract void callToAds();

	/**
	 * Call To publi
	 * 
	 * @param section
	 */
	protected void callToAds(String name) {
		if (section == null)
			return;
		// Delete old banner
		if (banner != null) {
			banner.removeBanner();
			((ViewGroup) getActivity().findViewById(R.id.publiContent))
					.removeView(banner);
		}

		// Create new Banner
		banner = PubliDAO.getInstance(getActivity()).getBanner(name);

		// Set the new banner
		if (banner != null)
			((ViewGroup) getActivity().findViewById(R.id.publiContent))
					.addView(banner);

		// if (inter)
		// shows interstitial
		PubliDAO.getInstance(getActivity()).displayInterstitial(name);
	}

	public void setAdsActive() {
		adsActive = true;

	}

	/***************************************************************************/
	/** Aux methods **/
	/***************************************************************************/

	protected View getGapView(int topMargin) {
		View gapView = new View(mContext);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(1,
				topMargin);
		gapView.setLayoutParams(params);

		return gapView;
	}

	protected View getErrorContainer() {

		RelativeLayout errorContent = (RelativeLayout) inflater.inflate(
				R.layout.item_error, null);

		TextView message = (TextView) errorContent
				.findViewById(R.id.errorMessage);
		FontUtils.setCustomfont(mContext, message, FontTypes.HELVETICANEUE);

		int height = ((HomeActivity) getActivity()).getHeight();

		ImageView imageError = (ImageView) errorContent
				.findViewById(R.id.errorImage);
		RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsImage.topMargin = height / 5;
		imageError.setLayoutParams(paramsImage);

		return errorContent;
	}

}
