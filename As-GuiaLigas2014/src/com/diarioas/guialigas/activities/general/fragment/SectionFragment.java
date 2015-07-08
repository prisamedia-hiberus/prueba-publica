package com.diarioas.guialigas.activities.general.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.dao.model.general.Section;
import com.diarioas.guialigas.utils.DimenUtils;
import com.diarioas.guialigas.utils.FontUtils;
import com.diarioas.guialigas.utils.FontUtils.FontTypes;

public abstract class SectionFragment extends GeneralPBSGoogleAdFragment {

	protected Context mContext;
	protected View generalView;
	protected boolean firstExecution;
	protected Section section;
	protected String competitionId;

	protected LayoutInflater inflater;
	protected boolean adsActive = true;

	public String adSection = null;
	private boolean returnFromDetail;

	/********************* Fragment lifecycle methods *****************************/
	/***************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mContext = getActivity().getApplicationContext();
		this.section = (Section) getArguments().getSerializable("section");
		this.competitionId = getArguments().getString("competitionId");

		this.firstExecution = true;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState == null) {
			if (this.firstExecution) {
				this.firstExecution = false;
			}
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		configureView();
		loadInformation();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (isReturnFromDetail()) {
			setReturnFromDetail(false);
			callToOmniture();
		}
		if (this.adsActive) {
			this.adsActive = false;
			callToAds(this.adSection);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.mContext = null;
		this.generalView = null;
		this.section = null;
		this.inflater = null;
	}

	/********************* Configuration methods ********************************/
	/***************************************************************************/

	protected abstract void configureView();

	protected abstract void loadInformation();

	public void setCurrentcompetition(int position) {
	}

	public void onOpenSlidingMenu() {
	}
	

	public boolean isReturnFromDetail() {
		return returnFromDetail;
	}

	public void setReturnFromDetail(boolean returnFromDetail) {
		this.returnFromDetail = returnFromDetail;
	}
	/******************** Libraries methods ***************************************/
	/***************************************************************************/
	protected abstract void callToOmniture();

	public void callToAds() {
		if (this.adSection != null) {
			callToAds(this.adSection);
		}
	}

	protected void callToAds(String name) {
		if (name == null || name.length() == 0)
			return;

		this.adSection = name;
		callToInterAndBannerAction(this.adSection);
	}

	public void setAdsActive() {
		adsActive = true;
	}

	/******************** General methods ***********************************/
	/**********************************************************************/

	protected View getGapView(int topMargin) {
		View gapView = new View(mContext);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(1,
				topMargin);
		gapView.setLayoutParams(params);

		return gapView;
	}

	protected View getErrorContainer() {

		if (this.inflater == null) {
			return null;
		}

		RelativeLayout errorContent = (RelativeLayout) inflater.inflate(
				R.layout.item_error, null);

		TextView message = (TextView) errorContent
				.findViewById(R.id.errorMessage);
		FontUtils.setCustomfont(mContext, message, FontTypes.ROBOTO_REGULAR);

		Point size = DimenUtils.getSize(getActivity().getWindowManager());
		int height = size.y;

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
