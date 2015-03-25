package com.prisadigital.realmedia.adlib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;

import com.prisadigital.realmedia.adlib.AdView.AdUnitFilledRunnable;
import com.prisadigital.realmedia.adlib.data.AdFormat;
import com.prisadigital.realmedia.adlib.data.AdUnit;
import com.prisadigital.realmedia.adlib.utils.AdLibUtils;
import com.prisadigital.realmedia.adlib.utils.AdParser;
import com.prisadigital.realmedia.adlib.utils.AdParser.UnableToParseAdException;
import com.prisadigital.realmedia.adlib.utils.GifDecoderFactory;
import com.prisadigital.realmedia.adlib.utils.StorageProvider;

public class AdViewManager {
    @SuppressWarnings("unused")
    private static final String TAG = "AdViewManager";

    // Context
    private WeakReference<Context> contextRef;
    // AdView
    private WeakReference<AdView>  adViewRef;
    // AdUnit
    private WeakReference<AdUnit>  adUnitRef;

    public AdViewManager(Context context, AdView adView, AdUnit adUnit) {
        // Save references
        this.contextRef = new WeakReference<Context>(context);
        this.adViewRef = new WeakReference<AdView>(adView);
        this.adUnitRef = new WeakReference<AdUnit>(adUnit);
    }

    public void handleAd() throws IOException, UnableToParseAdException {
        AdView adView = adViewRef.get();
        Context context = contextRef.get();
        AdUnit adUnit = adUnitRef.get();

        if (adView != null && context != null && adUnit != null) {
            // Create Url
            URL url = new URL(AdLibUtils.getUrl(context, adView.getAdKey(), AdLibUtils.SIZE_BANNER));
            // Retrieve XML, parse and fill AdUnit
            AdViewManager.fillAdUnitFromServer(context, url, adUnit);

            // Download Images
            downloadImages();

            // Report to AdView about the data downloaded
            showAd();
        }
    }
    
    public void handleAdImages() throws IOException, UnableToParseAdException {
        AdView adView = adViewRef.get();

        if (adView != null) {
            // Download Images
            downloadImages();

            // Report to AdView about the data downloaded
            showAd();
        }
    }
    
    public static void fillAdUnitFromServer(Context context, URL url, AdUnit adUnit) throws IOException, UnableToParseAdException
    {
        // Create Connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(2000);
        // Get Input Stream
        InputStream is = connection.getInputStream();

        // Get Full XML
        String fullXml = AdParser.readXmlFromServer(is);
        // Clean XML
        fullXml = AdParser.cleanXml(fullXml);
        // Download and Parse XML
        AdParser.parseXml(context, fullXml, adUnit);

        // Check if the AdParser has parsed something
        if (!adUnit.isAdFormat(AdUnit.AD_FORMAT_BASIC)) {
            throw new UnableToParseAdException("Server's response doesn't contain any adFormat");
        }
        
        // Close Connection
        is.close();
        connection.disconnect();
        
        // Check if the adFormat exists and its type is redirect
        AdFormat firstAdFormat = adUnit.getAdFormat(AdUnit.AD_FORMAT_BASIC);
        AdFormat secondAdFormat = adUnit.getAdFormat(AdUnit.AD_FORMAT_EXPANDED);

        if (firstAdFormat != null && firstAdFormat.getType() == AdFormat.TYPE_XML) {
            // Download and Parse XML
            AdFormat newAdFormat = AdViewManager.updateAdFormatFromUrl(context, adUnit, AdUnit.AD_FORMAT_BASIC, firstAdFormat.getUrl());
            // Set it to adUnit
            adUnit.setAdFormat(newAdFormat, AdUnit.AD_FORMAT_BASIC);
        }
        
        if (secondAdFormat != null && secondAdFormat.getType() == AdFormat.TYPE_XML) {
            // Download and Parse XML
            AdFormat newAdFormat = AdViewManager.updateAdFormatFromUrl(context, adUnit, AdUnit.AD_FORMAT_EXPANDED, secondAdFormat.getUrl());
            // Set it to adUnit
            adUnit.setAdFormat(newAdFormat, AdUnit.AD_FORMAT_EXPANDED);
        }
    }
    
    private static AdFormat updateAdFormatFromUrl(Context context, AdUnit adUnit, int adFormat, String sUrl) throws IOException, UnableToParseAdException    
    {
        // Get New URL to retrieve adFormat data
        URL url = new URL(sUrl);
        // Create Connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(2000);
        // Get Input Stream
        InputStream is = connection.getInputStream();
        // Get Full XML
        String fullXml = AdParser.readXmlFromServer(is);
        // Clean XML
        fullXml = AdParser.cleanXml(fullXml);
        
        // Close Connection
        is.close();
        connection.disconnect();
        
        // Download and Parse XML
        return AdParser.parseXml(context, fullXml, adUnit, true, adFormat);
    }

    /**
     * Se llama desde los interstitials
     * @param context
     * @param adUnitKey
     * @return
     * @throws IOException
     * @throws UnableToParseAdException
     */
    public static AdUnit retrieveAdUnit(Context context, String adUnitKey) throws IOException,
            UnableToParseAdException {
        // Create AdUnit
        AdUnit adUnit = new AdUnit();

        // Create Url
        URL url = new URL(AdLibUtils.getUrl(context, adUnitKey, AdLibUtils.SIZE_INTERSTIAL));
        
        // Retrieve XML, parse and fill AdUnit
        AdViewManager.fillAdUnitFromServer(context, url, adUnit);

        // Return AdUnit
        return adUnit;
    }

    private void showAd() {
        AdView adView = adViewRef.get();

        if (adView != null) {
            // Get Handler
            Handler handler = adView.getHandler();

            if (handler != null)
                handler.post(new AdUnitFilledRunnable(adView));
        }
    }

    private void downloadImages() throws IOException {
        byte data[];

        // Iterate AdFormats
        for (AdFormat adFormat : adUnitRef.get().getAdFormats()) {
            // Check if the adFormat exists
            if (adFormat != null) {
                // Check type
                switch (adFormat.getType()) {
                    case AdFormat.TYPE_IMAGE:
                        // Get Image
                        Bitmap image = downloadImage(adFormat.getUrl());
                        // Set it
                        adFormat.setImage(image);
                        break;
                    case AdFormat.TYPE_GIF:
                        // Get Movie Byte Array
                        data = downloadMovie(adFormat.getUrl());
                        // Set it
                        adFormat.setAnimationDrawable(GifDecoderFactory.decodeByteArray(data));
                        break;
                    case AdFormat.TYPE_VIDEO:
                        // adFormat.setUrl("http://www.arturogutierrez.com/test/test.mp4");
                        // Download video to cache
                        downloadContentIfNotCached(adFormat.getUrl());
                        break;
                }
            }
        }
    }
    
    public void downloadAdFormatImages(AdFormat adFormat) throws IOException {
        byte data[];

        // Iterate AdFormats
            // Check if the adFormat exists
            if (adFormat != null) {
                // Check type
                switch (adFormat.getType()) {
                    case AdFormat.TYPE_IMAGE:
                        // Get Image
                        Bitmap image = downloadImage(adFormat.getUrl());
                        // Set it
                        adFormat.setImage(image);
                        break;
                    case AdFormat.TYPE_GIF:
                        // Get Movie Byte Array
                        data = downloadMovie(adFormat.getUrl());
                        // Set it
                        adFormat.setAnimationDrawable(GifDecoderFactory.decodeByteArray(data));
                        break;
                    case AdFormat.TYPE_VIDEO:
                        // adFormat.setUrl("http://www.arturogutierrez.com/test/test.mp4");
                        // Download video to cache
                        downloadContentIfNotCached(adFormat.getUrl());
                        break;
                }
            }
    }

    private void downloadContentIfNotCached(String url) throws IOException {
        // Get Context
        Context context = contextRef.get();

        if (context != null) {
            File cachedFile = StorageProvider.getStorage(context).getFile(url);

            if (!cachedFile.exists() || (cachedFile.exists() && cachedFile.length() <= 0)) {
                // Get Stream of the remote file
                InputStream is = getInputStreamFromMovie(url);

                // Save it to cache
                StorageProvider.getStorage(context).saveFile(is, url);
            }
        }
    }

    private InputStream getInputStreamFromMovie(String imageUrl) throws IOException {
        // Create Url
        URL url = new URL(imageUrl);
        // Create Connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Get Input Stream
        return connection.getInputStream();
    }

    private byte[] downloadMovie(String imageUrl) throws IOException {
        // Get Input Stream
        InputStream is = getInputStreamFromMovie(imageUrl);

        // Load Bitmap
        return AdLibUtils.getFileContents(is);
    }

    private Bitmap downloadImage(String imageUrl) throws IOException {
        // Get Input Stream
        InputStream is = getInputStreamFromMovie(imageUrl);

        // Load Bitmap
        return BitmapFactory.decodeStream(is);
    }

    public void launchUrl(String url) {
        Context context = contextRef.get();

        if (context != null) {
            // Create Intent
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Start Activity
            context.startActivity(browserIntent);
        }
    }
}
