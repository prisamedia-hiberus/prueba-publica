package com.diarioas.guialigas.dao.reader;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.prisadigital.realmedia.adlib.AdView;
import com.prisadigital.realmedia.adlib.InterstitialAd;
import com.prisadigital.realmedia.adlib.PreRollVideo;
import com.prisadigital.realmedia.adlib.PreRollVideo.PreRollVideoListener;

public class PubliDAO {

	private static PubliDAO sInstance;
	private Context mContext;

	public static PubliDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new PubliDAO();
			sInstance.mContext = ctx;
		}
		return sInstance;
	}

	public AdView getBanner(String adUnitKey) {

		// Build adkey
		String adurl = NativeAds.AD_KEY + adUnitKey;

		AdView mAdView = new AdView(mContext, AdView.BANNER_SMARTPHONE, adurl);

		LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mAdView.setLayoutParams(params);

		Log.d("PUBLI", "Mostrando Banner: " + adurl);
		return mAdView;
	}

	public void displayInterstitial(String adUnitKey) {
		 // Build adkey
		 String adurl = NativeAds.AD_KEY + adUnitKey;
		 String adPosition = "Position2";
		
		 // Create Interstitial
		 InterstitialAd iad = new InterstitialAd(mContext, adurl, adPosition);
		 // Show
		 iad.loadAd();
		 Log.d("PUBLI", "Mostrando InterstitialAd: " + adurl);
	}

	public void showPreRoll(String adUnitKey, PreRollVideoListener listener) {
		 String adurl = NativeAds.AD_KEY + adUnitKey;
		 String adPosition = "Prerroll";
		
		 Log.d("PUBLI", "PREROLL::URL: " + adurl + " Position: " +
		 adPosition);
		
		 PreRollVideo.queryForVideo(mContext, adurl, adPosition, listener);
		 Log.d("PUBLI", "Mostrando PreRollVideo: " + adurl);

	}

}
