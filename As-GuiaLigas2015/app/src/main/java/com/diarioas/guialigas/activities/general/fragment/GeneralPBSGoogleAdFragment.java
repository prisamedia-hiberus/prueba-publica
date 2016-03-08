package com.diarioas.guialigas.activities.general.fragment;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.diarioas.guialigas.BuildConfig;
import com.diarioas.guialigas.R;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.prisa.google_ads.ads.PBSDFPAdDAO;
import com.prisa.google_ads.ads.PBSDFPAdDAO.AdDAOBannerListener;
import com.prisa.google_ads.ads.PBSDFPAdDAO.AdDAOInterstitialListener;

public class GeneralPBSGoogleAdFragment extends Fragment implements
		AdDAOInterstitialListener, AdDAOBannerListener {

	private PublisherInterstitialAd interstitial = null;
	private PublisherAdView banner = null;
	private String section = null;

	public boolean needIntestitialListenerResumed = false;
	public boolean needBannerListenerResumed = false;

	private boolean showingInterstitial = false;
	protected boolean isFromFrontpage = false;
	protected boolean abortCallInterstitial = false;

	int idResourceBanner = R.id.publiContent;

	@Override
	public void onResume() {
		super.onResume();

		if (needIntestitialListenerResumed) {
			abortCallInterstitial = false;
			PBSDFPAdDAO.getInstance(getActivity())
					.addInterstitialListener(this);
		}
		if (needBannerListenerResumed) {
			PBSDFPAdDAO.getInstance(getActivity()).addBannerListener(this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (!showingInterstitial) {

			abortCallInterstitial = true;
			PBSDFPAdDAO.getInstance(getActivity()).removeBannerListener(this);
			PBSDFPAdDAO.getInstance(getActivity()).removeInterstitialListener(
					this);
			if (banner != null) {
				if (banner.getParent() != null) {
					((ViewGroup) banner.getParent()).removeView(banner);
				}
				banner.destroy();
			}
		} else {
			showingInterstitial = false;
		}
	}

	public void callToInterAndBannerAction(String currentSection) {

		ViewGroup viewGroup = (ViewGroup) getView().findViewById(
				this.idResourceBanner);
		if (viewGroup != null && banner != null) {
			viewGroup.removeView(banner);
			banner.destroy();
		}

		this.section = currentSection;

		if (this.section != null && !this.section.equalsIgnoreCase("")) {

			abortCallInterstitial = false;
			PBSDFPAdDAO.getInstance(getActivity())
					.addInterstitialListener(this);
			needIntestitialListenerResumed = true;
			interstitial = PBSDFPAdDAO.getInstance(getActivity())
					.createInterstitial(section, BuildConfig.AD_KEY);
		}
	}

	@Override
	public void onShowInterstitial() {
		if (interstitial != null && interstitial.isLoaded()
				&& !abortCallInterstitial) {
			showingInterstitial = true;
			interstitial.show();
		}
	}

	public void callToBannerAction(String section) {

		if (!isVisible()) {
			return;
		}

		if (banner != null
				&& (ViewGroup) getView() != null
				&& (ViewGroup) getView().findViewById(this.idResourceBanner) != null) {
			((ViewGroup) getView().findViewById(this.idResourceBanner))
					.removeView(banner);
			banner.destroy();
		}

		this.section = section;

		if (section != null && !section.equalsIgnoreCase("")) {
			PBSDFPAdDAO.getInstance(getActivity()).addBannerListener(this);
			needBannerListenerResumed = true;

			banner = PBSDFPAdDAO.getInstance(getActivity()).createBanner(
					section, BuildConfig.AD_KEY,AdSize.BANNER);
		}
	}

	@Override
	public void onHideInterstitial() {

		abortCallInterstitial = true;
		PBSDFPAdDAO.getInstance(getActivity()).removeInterstitialListener(this);
		needIntestitialListenerResumed = false;

		if (interstitial != null) {
			interstitial = null;
		}

		if (this.section != null && this.section.length() > 0) {
			callToBannerAction(this.section);
		}
	}

	@Override
	public void onAlertInterstitial(String message) {
	}

	@Override
	public void onShowBanner(PublisherAdView adView) {
		if (adView.getAdSize().equals(AdSize.BANNER)) {
			if (banner != null) {
				if (banner.getParent() != null) {
					((ViewGroup) banner.getParent()).removeView(banner);
				}
				if (((ViewGroup) getView()) != null
						&& ((ViewGroup) getView().findViewById(R.id.publiContent)) != null) {
					((ViewGroup) getView().findViewById(R.id.publiContent))
							.addView(banner);
				}
			}
		}
	}

	@Override
	public void onHideBanner(PublisherAdView adView) {
		if (adView.getAdSize().equals(AdSize.BANNER)) {
			if (banner != null) {
				if (banner.getParent() != null) {
					((ViewGroup) banner.getParent()).removeView(banner);
				}
				banner.destroy();
				banner = null;

				needBannerListenerResumed = false;
				PBSDFPAdDAO.getInstance(getActivity()).removeBannerListener(this);
			}
		}
	}

	@Override
	public void onAlertBanner(PublisherAdView adView, String message) {
		if (adView.getAdSize().equals(AdSize.BANNER)) {

		}
	}
}
