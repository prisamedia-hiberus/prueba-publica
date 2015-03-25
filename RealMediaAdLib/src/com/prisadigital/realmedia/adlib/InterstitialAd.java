package com.prisadigital.realmedia.adlib;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;

import com.prisadigital.realmedia.adlib.data.AdUnit;

public class InterstitialAd {
    // Context
    private WeakReference<Context>   mContext;
    // Key
    private String                   adKey;
    // Scheduler
    private ScheduledExecutorService scheduler;
    private InterstitialAdViewListener mInterstitialAdViewListener;

    public InterstitialAd(Context context, String adUnitKey) {
        this.mContext = new WeakReference<Context>(context);
        this.adKey = adUnitKey;
    }

    // Retrocompatibility with old lib versions. AdPosition is not needed anymore
    public InterstitialAd(Context context, String adUnitKey, String adPosition) {
        this(context, adUnitKey);
    }
    
    public void loadAd() {
        // Create Pool
        scheduler = Executors.newScheduledThreadPool(1);
        // Launch Task to retrieve AdUnit
        scheduler.schedule(new Runnable() {
            
            @Override
            public void run() {
                Context context = mContext.get();
                
                if (context != null) {
                    try {
                        // Load AdUnit
                        AdUnit adUnit = AdViewManager.retrieveAdUnit(context, adKey);
                        // Launch Activity
                        launchActivity(context, adUnit);
                    } catch (Exception e) {
                    }
                }
            }
        }, 0, TimeUnit.SECONDS);
    }

    private void launchActivity(Context context, AdUnit adUnit) {
        // Create Intent
        Intent i = new Intent(context, AdActivity.class);
        // Set Extras
        i.putExtra("adKey", adKey);
        i.putExtra("adUnit", adUnit);
        // Start Activity
        context.startActivity(i);
    }
    
    /**
     * Obtiene el interstitial pero delega en el listener el mostrarlo
     */
    public void retrieveAd() {
        
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new Runnable() {
            
            @Override
            public void run() {
                Context context = mContext.get();
                if (context != null) {
                try {
                    
                    final AdUnit adUnit = AdViewManager.retrieveAdUnit(context, adKey);
                    final Intent i = new Intent(context, AdActivity.class);
                    // Set Extras
                    i.putExtra("adKey", adKey);
                    i.putExtra("adUnit", adUnit);
                    
                    // Inform listerner
                    if (mInterstitialAdViewListener != null) {
                        mInterstitialAdViewListener.onAdRetrieved(i);
                    }
                } catch (Exception e) {
                    if (mInterstitialAdViewListener != null) {
                        mInterstitialAdViewListener.onAdNotFound();
                    }
                } 
                }
            }
        }, 0, TimeUnit.SECONDS);
        

        
    }
    
    
    public void setAdViewListener(InterstitialAdViewListener listener) {
        this.mInterstitialAdViewListener = listener;
    }
    
    public interface InterstitialAdViewListener {
        public void onAdRetrieved(Intent i);
        public void onAdNotFound();
    }
    
    
}
