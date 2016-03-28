package com.diarioas.guialigas.activities.general;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

public class GeneralPBSGoogleAdActivity extends ActionBarActivity implements
		AdDAOInterstitialListener, AdDAOBannerListener {

	private PublisherInterstitialAd interstitial;
	private PublisherAdView banner;

	public boolean needIntestitialListenerResumed = false;
	public boolean needBannerListenerResumed = false;
	protected boolean abortCallInterstitial = false;
	private boolean showingInterstitial = false;

	private String section = null;

	@Override
	public void onResume() {
		super.onResume();

		if (needIntestitialListenerResumed) {
			abortCallInterstitial = false;
			PBSDFPAdDAO.getInstance(this).addInterstitialListener(this);
		}
		if (needBannerListenerResumed) {
			PBSDFPAdDAO.getInstance(this).addBannerListener(this);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (!showingInterstitial) {
			abortCallInterstitial = true;
			PBSDFPAdDAO.getInstance(this).removeBannerListener(this);
			PBSDFPAdDAO.getInstance(this).removeInterstitialListener(this);
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

	public void callToInterAndBannerAction(String section) {

		if (banner != null && findViewById(R.id.publiContent)!=null) {
			((ViewGroup) findViewById(R.id.publiContent)).removeView(banner);
			banner.destroy();
		}

		this.section = section;

		if (section != null && !section.equalsIgnoreCase("")) {
			abortCallInterstitial = false;
			PBSDFPAdDAO.getInstance(this).addInterstitialListener(this);
			needIntestitialListenerResumed = true;

			interstitial = PBSDFPAdDAO.getInstance(this).createInterstitial(
					this.section, NativeAds.AD_KEY);
		}
	}

	@Override
	public void onShowInterstitial() {
		Log.v("AS ADS", "Show interstitial GeneralPBSGoogleAdActivity");
		if (interstitial != null && interstitial.isLoaded()
				&& !abortCallInterstitial) {
			showingInterstitial = true;
			interstitial.show();
		}
	}

	public void callToBannerAction(String section) {
		if (banner != null && ((ViewGroup) findViewById(R.id.publiContent))!=null) {
			((ViewGroup) findViewById(R.id.publiContent)).removeView(banner);
			banner.destroy();
		}

		this.section = section;

		if (section != null && !section.equalsIgnoreCase("")) {
			PBSDFPAdDAO.getInstance(this).addBannerListener(this);
			needBannerListenerResumed = true;

			banner = PBSDFPAdDAO.getInstance(this).createBanner(section,
                    BuildConfig.AD_KEY,AdSize.BANNER);
		}
	}

	@Override
	public void onHideInterstitial() {

		abortCallInterstitial = true;
		PBSDFPAdDAO.getInstance(this).removeInterstitialListener(this);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onShowBanner(PublisherAdView adView) {
		if (banner != null) {
			if (banner.getParent() != null) {
				((ViewGroup) banner.getParent()).removeView(banner);
			}
			if (((ViewGroup) findViewById(R.id.publiContent)) != null) {
				((ViewGroup) findViewById(R.id.publiContent)).addView(banner);
			}
		}
	}

	@Override
	public void onHideBanner(PublisherAdView adView) {
		if (banner != null) {
			if (banner.getParent() != null) {
				((ViewGroup) banner.getParent()).removeView(banner);
			}
			banner.destroy();
			banner = null;

			needBannerListenerResumed = false;
			PBSDFPAdDAO.getInstance(this).removeBannerListener(this);
		}
	}

	@Override
	public void onAlertBanner(PublisherAdView adView, String message) {
		if (adView.getAdSize().equals(AdSize.BANNER)) {

		}
	}
}
