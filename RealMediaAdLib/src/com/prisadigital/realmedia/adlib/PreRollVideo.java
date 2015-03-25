package com.prisadigital.realmedia.adlib;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.prisadigital.realmedia.adlib.data.AdFormat;
import com.prisadigital.realmedia.adlib.data.AdUnit;
import com.prisadigital.realmedia.adlib.utils.AdLibUtils;

public class PreRollVideo {
    private URL url;

    private PreRollVideo(Context context, String adUnitKey, PreRollVideoListener listener) {
        try {
            // Create the final URL
            url = new URL(AdLibUtils.getUrl(context, adUnitKey, AdLibUtils.SIZE_PREROLL));

            // Create a task to retrieve the video on background
            PreRollVideoFetcherTask task = new PreRollVideoFetcherTask(context, url, listener);
            // Execute task
            task.execute();
        } catch (MalformedURLException e) {
            // Call to listener
            if (listener != null) {
                listener.onVideoNotFound();
            }
        }

    }

    public static void queryForVideo(Context context, String adUnitKey, PreRollVideoListener listener) {
        // Check if the listener is setted
        if (listener != null) {
            // Create Instance (I know, I don't need the instance, there is should be a better way
            // to launch a private AsynTask from a static method...)
            new PreRollVideo(context, adUnitKey, listener);
        } else {
            Log.e(AdView.TAG,
                    "The adKey and/or adPosition provisioned is not correct. Please, be sure the adKey/adPosition is the correct key provisioned by RealMedia.");
        }
    }
    
    // Retrocompatibility with old lib versions. AdPosition is not needed anymore
    public static void queryForVideo(Context context, String adUnitKey, String adPosition, PreRollVideoListener listener) {
        queryForVideo(context, adUnitKey, listener);
    }

    private static void managePreRoll(AdUnit adUnit, PreRollVideoListener listener) {
        // Get the AdFormat
        AdFormat adFormat = adUnit.getAdFormat(AdUnit.AD_FORMAT_BASIC);

        // The AdFormat should be PREROLL type
        if (adFormat.getType() == AdFormat.TYPE_PREROLL) {
            // Get the URLs
            String videoUrl = adFormat.getUrl();
            String clickUrl = adFormat.getClickUrl();

            if (videoUrl != null) {
                // Call to listener
                if (listener != null) {
                    listener.onVideoRetreived(videoUrl, clickUrl);
                }
            } else {
                // Call to listener
                if (listener != null) {
                    listener.onVideoNotFound();
                }
            }
        } else {
            // Call to listener
            if (listener != null) {
                listener.onVideoNotFound();
            }
        }
    }

    public class PreRollVideoFetcherTask extends AsyncTask<Void, Void, AdUnit> {

        private WeakReference<Context>              contextRef;
        private WeakReference<PreRollVideoListener> listenerRef;
        private URL                                 url;

        public PreRollVideoFetcherTask(Context context, URL url, PreRollVideoListener listener) {
            this.contextRef = new WeakReference<Context>(context);
            this.listenerRef = new WeakReference<PreRollVideo.PreRollVideoListener>(listener);
            this.url = url;
        }

        @Override
        protected AdUnit doInBackground(Void... params) {
            Context context = contextRef.get();

            if (context != null) {
                try {
                    // Create AdUnit
                    AdUnit adUnit = new AdUnit();

                    // Retrieve XML, parse and fill AdUnit
                    AdViewManager.fillAdUnitFromServer(context, url, adUnit);

                    return adUnit;
                } catch (Exception e) {
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(AdUnit result) {
            PreRollVideoListener listener = listenerRef.get();

            if (result != null) {
                // Check Result
                managePreRoll(result, listener);
            } else {
                // Call to listener
                if (listener != null) {
                    listener.onVideoNotFound();
                }
            }
        }

    }

    public interface PreRollVideoListener {

        public void onVideoRetreived(String videoUrl, String clickUrl);

        public void onVideoNotFound();
    }
}
