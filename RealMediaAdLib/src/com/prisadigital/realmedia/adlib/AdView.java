package com.prisadigital.realmedia.adlib;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.prisadigital.realmedia.adlib.animations.ExpandAnimation;
import com.prisadigital.realmedia.adlib.data.AdFormat;
import com.prisadigital.realmedia.adlib.data.AdUnit;
import com.prisadigital.realmedia.adlib.utils.AdLibUtils;
import com.prisadigital.realmedia.adlib.utils.StorageProvider;

public class AdView extends RelativeLayout implements OnClickListener, Callback {
    public static final String       TAG                             = "RMADLIB";

    private static final int         DELAY_TO_RETRY_ERROR_CONNECTION = 10;
    private static final int         DELAY_TO_RETRY_NO_CONNECTION    = 10;
    private static final int         DELAY_TO_LOAD_NEW_AD            = -1;
    @SuppressWarnings("unused")
    private static final int         DELAY_TO_COLLAPSE               = 10;
    private static final float       BANNER_BUTTONS_REDUCTION        = 1.5f;

    public static final int          BANNER_SMARTPHONE               = 0;
    public static final int          INTERSTITIAL                    = 1;

    // Determina si hay que volver a pedir un anuncio en caso de error
    public static final boolean      RETRY_AD                        = false;

    // Context
    private WeakReference<Context>   context;
    // Type
    private int                      type;
    // Key
    private String                   key;
    // Sizes
    private int                      width, height;
    private int                      screenWidth, screenHeight;
    // Density
    private float                    density;
    // Scheduler for background tasks
    private ScheduledExecutorService scheduler;
    private boolean                  isScheduled;
    private ScheduledFuture<?>       collapsedScheduled, delayedScheduled, removeScheduled;
    // Window
    private boolean                  hasWindow;
    // Handler
    private Handler                  handler;
    // AdFormat Index
    private int                      adFormatIndex;
    // Flag
    private boolean                  readyToPaint;
    private boolean                  isRemoved;

    // Image View
    private ImageView                mImageView;
    private ImageView                mCloseView;
    // Video View
    private SurfaceView              mVideoView;
    private SurfaceHolder            mVideoHolder;
    private MediaPlayer              mMediaPlayer;
    // Video Items
    private ImageView                mVideoPlay;
    private ImageView                mVideoMute;
    private LinearLayout             mLayoutVideoButton;
    // Video Flag
    private boolean                  playingVideo;
    private boolean                  videoMuted;
    private boolean                  videoSurfaceAdded;
    private boolean                  videoPrepared;
    // WebView HTML5
    private WebView                  mWebView;
    // Manager
    private AdViewManager            adViewManager;
    // The data of the ad
    private AdUnit                   adUnit;

    // Listener
    private AdViewListener           mAdViewListener;

    // Storage Provider
    private StorageProvider          storageProvider;

    public AdView(Context context, int adType, String adUnitKey) {
        // Call to super
        super(context);

        // Save Properties
        this.type = adType;
        this.key = adUnitKey;
        this.context = new WeakReference<Context>(context);

        // Initialize
        if (!isInEditMode()) {
            initialize();
        }
    }
    
    // Retrocompatibility with old lib versions. AdPosition is not needed anymore
    public AdView(Context context, int adType, String adKey, String adPosition) {
        this(context, adType, adKey);
    }

    public AdView(Context context, int adType, String adUnitKey, AdUnit adUnit) {
        // Call to super
        super(context);

        // Save Properties
        this.type = adType;
        this.key = adUnitKey;
        this.context = new WeakReference<Context>(context);
        this.adUnit = adUnit;

        // Initialize
        if (!isInEditMode()) {
            initialize();
        }
    }

    public AdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        // Iterate attributes (context.obtainStyledAttributes can't be used in
        // Library)
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String name = attrs.getAttributeName(i);

            if (name.equalsIgnoreCase("adType")) {
                this.type = attrs.getAttributeIntValue(i, BANNER_SMARTPHONE);
            } else if (name.equalsIgnoreCase("adKey")) {
                this.key = attrs.getAttributeValue(i);
            }
        }

        // Save Context
        this.context = new WeakReference<Context>(context);

        // Initialize
        if (!isInEditMode()) {
            initialize();
        }
    }

    private void initialize() {
        // Get Density
        density = context.get().getResources().getDisplayMetrics().density;

        // Initialize Storage Provider
        storageProvider = StorageProvider.getStorage(context.get());

        // Calculate Size
        calculateSize();

        // Initialize No Ad Format
        adFormatIndex = AdUnit.AD_FORMAT_NONE;
        // Mark as !ready
        readyToPaint = false;
        // Initialize removed flag
        isRemoved = false;
        // Create ImageView
        mImageView = new ImageView(context.get());
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // Set OnClickListener (do not use OnTouchEvent because there is a bug
        // which ACTION_UP is
        // not called correctly).
        mImageView.setOnClickListener(this);

        // Create Close View
        mCloseView = new ImageView(context.get());
        mCloseView.setImageResource(R.drawable.rmadlib_close);
        mCloseView.setOnClickListener(this);

        // Create VideoView
        mVideoView = new SurfaceView(context.get());
        // Set listener to surface
        mVideoView.setOnClickListener(this);
        // Get holder from SurfaceView
        mVideoHolder = mVideoView.getHolder();
        mVideoHolder.addCallback(this);
        mVideoHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // mVideoHolder.setFormat(PixelFormat.TRANSPARENT);

        // Create Video Buttons
        mVideoPlay = new ImageView(context.get());
        mVideoPlay.setImageResource(R.drawable.rmadlib_play);
        mVideoPlay.setPadding(9, 9, 0, 0);
        mVideoPlay.setAdjustViewBounds(true);
        mVideoPlay.setScaleType(ImageView.ScaleType.FIT_XY);

        mVideoMute = new ImageView(context.get());
        mVideoMute.setImageResource(R.drawable.rmadlib_muted);
        mVideoMute.setPadding(9, 9, 0, 0);
        mVideoMute.setAdjustViewBounds(true);
        mVideoMute.setScaleType(ImageView.ScaleType.FIT_XY);

        // Set button listeners
        mVideoPlay.setOnClickListener(this);
        mVideoMute.setOnClickListener(this);
        // Create layout to wrap them
        mLayoutVideoButton = new LinearLayout(context.get());
        mLayoutVideoButton.setOrientation(LinearLayout.HORIZONTAL);

        // Reducimos a la mitad la franja donde van los botones play/mute
        // mLayoutVideoButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 100));

        mLayoutVideoButton.addView(mVideoPlay);
        mLayoutVideoButton.addView(mVideoMute);

        // Initialize flag
        playingVideo = false;
        videoSurfaceAdded = false;
        videoPrepared = false;

        // Create the AdUnit if proceed
        if (type != AdView.INTERSTITIAL) {
            adUnit = new AdUnit();
        }

        // Create Manager
        adViewManager = new AdViewManager(context.get(), this, adUnit);

        // Create Handler
        handler = new Handler();

        // Create Pool
        scheduler = Executors.newScheduledThreadPool(5);

        if (key != null && key.length() > 0) {
            if (AdLibUtils.isInternetConnection(context.get())) {
                // Rotate First Ad
                rotateAd();
            } else // No Connection, try 10 seconds later
            {
                rotateDelayedAd(DELAY_TO_RETRY_NO_CONNECTION);
            }
        } else {
            // Show error message
            Log.e(AdView.TAG,
                    "The adKey and/or adPosition provisioned is not correct. Please, be sure the adKey/adPosition is the correct key provisioned by RealMedia.");
        }
    }

    private boolean hasWindow() {
        return hasWindow;
    }

    private boolean canHandleAd() {
        return adFormatIndex == AdUnit.AD_FORMAT_BASIC || adFormatIndex == AdUnit.AD_FORMAT_NONE;
    }

    private void rotateDelayedAd(int seconds) {
        // Check if the time is positive
        if (seconds > 0) {
            // If there is not window we stop the scheduled
            if (!hasWindow) {
                isScheduled = false;
                return;
            }

            // Perform task
            isScheduled = true;
            // Execute Handle Ad
            delayedScheduled = scheduler.schedule(new HandleAdRunnable(adViewManager, this),
                    seconds, TimeUnit.SECONDS);
        }
    }

    private void rotateAd() {
        // If there is not window we stop the scheduled
        if (!hasWindow) {
            isScheduled = false;
            return;
        }

        // Perform task
        isScheduled = true;
        // Execute Handle Ad
        scheduler.schedule(new HandleAdRunnable(adViewManager, this), 0, TimeUnit.SECONDS);
    }

    private void calculateSize() {
        int adWidthOriginal, adHeightOriginal;

        switch (type) {
            case AdView.BANNER_SMARTPHONE:
            default:
                adWidthOriginal = 320;
                adHeightOriginal = 50;

                // Check Density (only scale when <= 1.5
                if (density > 1.5)
                    density = 1.5f;

                // Scale size
                width = (int) (adWidthOriginal * density);
                height = (int) (adHeightOriginal * density);
                break;
            case AdView.INTERSTITIAL:
                Context ctx = context.get();

                if (ctx != null) {
                    screenWidth = width = context.get().getResources().getDisplayMetrics().widthPixels;
                    screenHeight = height = context.get().getResources().getDisplayMetrics().heightPixels;
                } else {
                    screenWidth = width = 0;
                    screenHeight = height = 0;
                }
                break;
        }

    }

    private void clickAd() {
        // Get Url
        String clickUrl = adUnit.getAdFormat(adFormatIndex).getClickUrl();
        // Check if the url exists
        if (clickUrl != null) {
            // Launch URL
            adViewManager.launchUrl(clickUrl);

            if (mAdViewListener != null)
                mAdViewListener.onAdClicked(this);
        }
    }

    private void expandAd(AdFormat newAdFormat, AdFormat actualAdFormat) {
        
        // If the banner was a video we need to stop and change the player
        if (actualAdFormat.getType() == AdFormat.TYPE_VIDEO) {
            // Stop Video
            stopVideo();
        }

        switch (newAdFormat.getType()) {
            case AdFormat.TYPE_GIF:
            case AdFormat.TYPE_IMAGE:
                expandImage(newAdFormat, actualAdFormat);
                break;
            case AdFormat.TYPE_VIDEO:
                expandVideo(newAdFormat, actualAdFormat);
                break;
            case AdFormat.TYPE_HTML5:
                expandHTML5(newAdFormat, actualAdFormat);
                break;
        }

        // Set Duration
        if (newAdFormat.getDuration() > 0) {
            // Launch timer to Collapse
            collapsedScheduled = scheduler.schedule(new CollapseAdRunnable(this),
                    newAdFormat.getDuration(), TimeUnit.SECONDS);
        }
    }

    private void expandHTML5(final AdFormat newAdFormat, AdFormat actualAdFormat) {
     // Change state
        adFormatIndex = AdUnit.AD_FORMAT_EXPANDED;

        if (mWebView == null) {
            mWebView = new WebView(context.get());
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.setWebViewClient(new AdWebClient(newAdFormat));
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setPluginsEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(false);
            mWebView.setInitialScale(0);
        }

        // Change Height
        height = newAdFormat.getHeight();
        // Create Layout Params
        LayoutParams lpWeb = new LayoutParams(width, height);
        // Add web view to Layout
        addView(mWebView, lpWeb);

        // Add Close
        // Create Layout Params
        LayoutParams lpCloseHtml = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpCloseHtml.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpCloseHtml.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // Add to layout
        addView(mCloseView, lpCloseHtml);

        // Load Url
        mWebView.loadUrl(newAdFormat.getUrl());
        
    }

    private void expandVideo(AdFormat newAdFormat, AdFormat actualAdFormat) {
     // Remove listener from common baner
        mImageView.setOnClickListener(null);

        // Change state
        adFormatIndex = AdUnit.AD_FORMAT_EXPANDED;

        // Fit Video to Screen
        if (type == AdView.INTERSTITIAL) {
            fitVideo(newAdFormat);
        }

        try {
            LayoutParams lpVideo;

            // Change Height
            height = newAdFormat.getHeight();
            // Create Layout Params
            if (type == AdView.INTERSTITIAL) {
                lpVideo = new LayoutParams(newAdFormat.getWidth(), newAdFormat.getHeight());
                lpVideo.addRule(RelativeLayout.CENTER_VERTICAL);
            } else {
                lpVideo = new LayoutParams(width, height);
            }
            // Change holder size
            mVideoHolder.setFixedSize(newAdFormat.getWidth(), newAdFormat.getHeight());
            mVideoView.setLayoutParams(lpVideo);
            // Create mMediaPlayer
            createVideoPlayer();
            // Remove the old player if proceed
            if (!videoSurfaceAdded) {
                // Mark as added
                videoSurfaceAdded = true;
                // Add ImageView to Layout
                addView(mVideoView, lpVideo);
                // Add video buttons to layout
                addView(mLayoutVideoButton);
            } else {
                // Reset Video
                // launchVideoFirstTime(); // Line commented because the
                // play is it done on surfaceChanged listener.
            }

            // Add Close if the view is not a interstitial
            if (type != AdView.INTERSTITIAL) {
                LayoutParams lpClose = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                lpClose.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lpClose.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                // Add to layout
                addView(mCloseView, lpClose);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void expandImage(AdFormat newAdFormat, AdFormat actualAdFormat) {
     // Change state
        adFormatIndex = AdUnit.AD_FORMAT_EXPANDED;

        // Set listener again
        mImageView.setOnClickListener(this);
        // Change Image
        
        
        setImageOrGif(mImageView, newAdFormat);
        
        // Change layout size to height banner
        height = (int) (newAdFormat.getHeight() * density);
        // Show it
        if (actualAdFormat.getType() == AdFormat.TYPE_VIDEO) {
            // Create Layout Params
            LayoutParams lp = new LayoutParams(width, height);
            // Add to layout
            addView(mImageView, lp);
        } else {
            // Hack: We don't apply animation to the view if it is over
            // a SurfaceView
            // because the view is only shown during 1 sec!
            // Calculate size
            
            float scaleY = newAdFormat.getHeight() / actualAdFormat.getHeight();
            // Create Animation
            ExpandAnimation sa = new ExpandAnimation(mImageView, 1.0f, scaleY);
            sa.setDuration(1000);
            // Start Animation
            mImageView.startAnimation(sa);
        }

        // Create Layout Params
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // Add to layout
        addView(mCloseView, lp);
        
    }

    private void collapseAd() {
        // Get AdFormats
        AdFormat actualAdFormat = adUnit.getAdFormat(AdUnit.AD_FORMAT_EXPANDED);
        AdFormat newAdFormat = adUnit.getAdFormat(AdUnit.AD_FORMAT_BASIC);

        // Check if the ad is already collapsed
        if (adFormatIndex == AdUnit.AD_FORMAT_EXPANDED) {
            // Stop Task if it is running
            if (collapsedScheduled != null)
                collapsedScheduled.cancel(false);

            // Change state
            adFormatIndex = AdUnit.AD_FORMAT_BASIC;

            switch (actualAdFormat.getType()) {
                case AdFormat.TYPE_VIDEO:
                    if (mMediaPlayer != null) {
                        stopVideo();
                    }

                    if (newAdFormat.getType() != AdFormat.TYPE_VIDEO) {
                        // Remove surface
                        videoSurfaceAdded = false;
                        // Remove Video and buttons
                        removeView(mVideoView);
                        removeView(mLayoutVideoButton);
                    }
                    // Remove Close if it was added becase the view is a
                    // interstitial
                    if (type != AdView.INTERSTITIAL) {
                        removeView(mCloseView);
                    }
                    break;
                case AdFormat.TYPE_HTML5:
                    removeView(mWebView);
                    removeView(mCloseView);
                    break;
                case AdFormat.TYPE_IMAGE:
                case AdFormat.TYPE_GIF:
                    removeView(mCloseView);

                    if (newAdFormat.getType() == AdFormat.TYPE_VIDEO) {
                        // Remove Image view
                        removeView(mImageView);
                    }
                    break;
            }

            // Show the previous ad if the banner normal has adDuration <= 0. We
            // need to remove all Ad in other case
            if (newAdFormat != null && newAdFormat.getDuration() <= 0) {
                // Check the next type to remove or add the right view
                switch (newAdFormat.getType()) {
                    case AdFormat.TYPE_IMAGE:
                    case AdFormat.TYPE_GIF:
                        // Set listener again
                        mImageView.setOnClickListener(this);
                        // Change Image
                        setImageOrGif(mImageView, newAdFormat);
                        // Change layout size to height banner
                        height = (int) (adUnit.getAdFormat(adFormatIndex).getHeight() * density);
                        // Change Size
                        mImageView.getLayoutParams().height = height;

                        // Update layout
                        requestLayout();
                        break;
                    case AdFormat.TYPE_VIDEO:
                        // Change layout size to height banner
                        height = (int) (adUnit.getAdFormat(adFormatIndex).getHeight() * density);
                        // Set size to video holder
                        mVideoHolder.setFixedSize(width, height);
                        // Show Ad
                        pushAd();
                        break;
                }

                // Repaint
                invalidate();
            } else {
                // The AdView needs to be removed from the layout
                removeBanner();
            }

            if (mAdViewListener != null) {
                mAdViewListener.onAdCollapsed(this);
            }
        }
    }

    public void removeBanner() {
        if (!isRemoved) {
            // Only remove if the banner is not expanded
            if (adFormatIndex == AdUnit.AD_FORMAT_BASIC) {
                // Mark as removed
                isRemoved = true;

                // Stop video if procceed
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    stopVideo();
                }

                // Remove all views
                removeAllViews();

                // Call to listener
                if (mAdViewListener != null) {
                    mAdViewListener.onAdRemoved(this);
                }
            }
        }
    }

    private void setImageOrGif(final ImageView view, AdFormat adFormat) {
        if (adFormat.isAnimationDrawable()) {
            // Remove possible image older
            view.setImageBitmap(null);

            // Get Animation
            AnimationDrawable ad = adFormat.getMovie();
            // Set Animation
            view.setBackgroundDrawable(ad);
            view.setImageBitmap(null);
            // Start (in UI Thread, just in case)
            view.post(new Runnable() {
                @Override
                public void run() {
                    // Start Animation
                    ((AnimationDrawable) view.getBackground()).stop();
                    ((AnimationDrawable) view.getBackground()).start();
                }
            });
        } else {
            // Remove possible animation
            view.setBackgroundDrawable(null);

            // Set Image Bitmap
            view.setImageBitmap(adFormat.getImage());
        }
    }

    public String getAdKey() {
        return key;
    }

    @Override
    public void onClick(View v) {
        if (v == mImageView) {
            clickBanner();
        } else if (v == mVideoPlay) {
            clickPlayPauseButton();
        } else if (v == mVideoMute) {
            clickSoundButton();
        } else if (v == mVideoView) {
            clickVideo();
        } else if (v == mCloseView) {
            clickClose();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Get Size Size
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (readyToPaint) {
            if (width > 0 && parentWidthSize >= width)
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);

            if (height > 0 && parentHeightSize >= height)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            // Size 0,0 when no ad
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }

        // Save Size
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        if (!isInEditMode()) {
            if (visibility == VISIBLE) {
                // Window
                hasWindow = true;

                invalidate();

                // Call to ad if there is not ad
                if (!isScheduled) {
                    // Rotate Ad
                    rotateAd();
                }
            } else {
                // No Window
                this.hasWindow = false;

                // Collapse if proceed
                collapseAd();

                // If playing video we need to pause it
                if (playingVideo && mMediaPlayer != null) {
                    // Pause video
                    pauseVideo();
                    // Reset it
                    mMediaPlayer.reset();
                    // Mark as reset
                    videoPrepared = false;
                }

            }
        }

        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        // Free Resources
        if (mMediaPlayer != null) {
            // Free MediaPlayer
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        // Cancel Scheduler
        if (delayedScheduled != null) {
            delayedScheduled.cancel(true);
            delayedScheduled = null;
        }

        if (removeScheduled != null) {
            removeScheduled.cancel(true);
            removeScheduled = null;
        }

        super.onDetachedFromWindow();
    }

    public int getType() {
        return type;
    }

    private void clickPlayPauseButton() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                // Pause Video
                pauseVideo();
            } else {
                // Start Video
                startVideo();
            }
        }
    }

    private void muteVideo() {
        if (mMediaPlayer != null) {
            videoMuted = true;
            mMediaPlayer.setVolume(0.0f, 0.0f);
            // Change Image
            mVideoMute.setImageResource(R.drawable.rmadlib_muted);
        }
    }

    private void unmuteVideo() {
        if (mMediaPlayer != null) {
            videoMuted = false;
            mMediaPlayer.setVolume(1.0f, 1.0f);
            // Change Image
            mVideoMute.setImageResource(R.drawable.rmadlib_unmuted);
        }
    }

    private void clickVideo() {
        clickBanner();
    }

    private void clickSoundButton() {
        if (videoMuted) {
            unmuteVideo();
        } else {
            muteVideo();
        }
    }

    private void clickBanner() {
        // Check State
        if (adFormatIndex == AdUnit.AD_FORMAT_BASIC) {
            if (adUnit.isAdFormat(AdUnit.AD_FORMAT_EXPANDED)) {
                // Expand Bannder
                expandAd(adUnit.getAdFormat(AdUnit.AD_FORMAT_EXPANDED), adUnit.getAdFormat(AdUnit.AD_FORMAT_BASIC));
            } else // No Expanded
            {
                // Click Ad
                clickAd();
            }
        } else if (adFormatIndex == AdUnit.AD_FORMAT_EXPANDED) {
            // Click Ad
            clickAd();
            // Collapse Ad
            collapseAd();
        }
    }

    private void clickClose() {
        // Collapse Ad
        collapseAd();
    }

    private void didFinishVideo() {
        playingVideo = false;

        // We need to repet the video if the video is in a usual banner,
        // otherwise we need to close
        // the expanded video
        if (adFormatIndex == AdUnit.AD_FORMAT_BASIC) {
            startVideo();
        } else if (adFormatIndex == AdUnit.AD_FORMAT_EXPANDED) {
            // Collapse ad
            collapseAd();
        }
    }

    private void stopVideo() {
        // Reset flags
        playingVideo = false;
        videoPrepared = false;
        // Stop Video
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
        // Free Resource
        mMediaPlayer.setDisplay(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.release();

        mMediaPlayer = null;
        // Change image button
        mVideoPlay.setImageResource(R.drawable.rmadlib_play);
    }

    private void pauseVideo() {
        // Mark flag
        playingVideo = false;
        // Pause Video
        mMediaPlayer.pause();
        // Change image button
        mVideoPlay.setImageResource(R.drawable.rmadlib_play);
    }

    private void startVideo() {
        try {
            if (hasWindow) {
                if (!videoPrepared) {
                    // Get File from cache
                    File cachedFile = storageProvider.getFile(adUnit.getAdFormat(adFormatIndex)
                            .getUrl());
                    String absPath = cachedFile.getAbsolutePath();
                    // Open file input stream
                    FileInputStream fis = new FileInputStream(absPath);
                    // Set path file to VideoView
                    mMediaPlayer.setDataSource(fis.getFD());
                    // Prepare player
                    mMediaPlayer.prepare();
                    // Mark as prepared
                    videoPrepared = true;

                    // Close File Input
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
                }
                // Start again
                mMediaPlayer.start();
                // Mark flag
                playingVideo = true;
                // Change image button
                mVideoPlay.setImageResource(R.drawable.rmadlib_pause);
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable to instance the video.");
        }
    }

    private void launchVideoFirstTime() {
        Log.d(TAG, "launch Video First Time");
        // Obtiene el tama�o del contenedor del video
        AdFormat adformat = adUnit.getAdFormat(adFormatIndex);
        float h = (adformat.getHeight());
        float w = (adformat.getWidth());
        // Ratio para escalar
        float ratioImagen = h/w;

        // Obtiene el ancho de la pantalla
        WindowManager wm = (WindowManager)  context.get().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        
        Log.d(TAG, "Ratio: "+ratioImagen);
        Log.d(TAG, "launchVideoFirstTime. set video size:"+w+","+h);
        Log.d(TAG, "launchVideoFirstTime. set video calculado:"+width+","+(width/ratioImagen));

        // Se escala el player del v�deo en funci�n del ratio
        LayoutParams lpVideo = new LayoutParams(width,(int) (width/ratioImagen));
        lpVideo.addRule(RelativeLayout.CENTER_VERTICAL);
        mVideoView.setLayoutParams(lpVideo);

        if (adformat.isAutoplay()) {
            // Start video inmediatly
            startVideo();
        }

        if (adformat.isAutomute()) {
            muteVideo();
        } else {
            unmuteVideo();
        }
    }

    private void createVideoPlayer() {
        if (mMediaPlayer == null) {
            // Create MediaPlayer
            mMediaPlayer = new MediaPlayer();

            // Set listeners to video
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // Doesn't show errors to the user
                    return true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    didFinishVideo();
                }
            });
        }
    }

    private void scheduleRemoveBanner() {
        if (removeScheduled == null) {
            // Get AdFormat
            AdFormat adFormat = adUnit.getAdFormat(AdUnit.AD_FORMAT_BASIC);

            // Only remove the banner if the duration is > 0 seconds
            if (adFormat != null && adFormat.getDuration() > 0) {
                // Launch timer to Collapse
                removeScheduled = scheduler.schedule(new RemoveAdRunnable(this),
                        adFormat.getDuration(), TimeUnit.SECONDS);
            }
        }
    }

    private void fitVideo(AdFormat adFormat) {
        float width = adFormat.getWidth();
        float height = adFormat.getHeight();
        float ratio = width / height;

        if (width > height) {
            adFormat.setWidth(screenWidth);
            adFormat.setHeight((int) (screenWidth / ratio));
        } else {
            adFormat.setWidth((int) (screenHeight * ratio));
            adFormat.setHeight(screenHeight);
        }
    }

    public void pushAd() {
        
        // Change AdFormatIndex to Basic (simple banner)
        adFormatIndex = AdUnit.AD_FORMAT_BASIC;

        // Mark as ready to paint
        readyToPaint = true;

        // Get AdFormat
        final AdFormat adFormat = adUnit.getAdFormat(adFormatIndex);
        // Check AdFormat type
        switch (adFormat.getType()) {
            case AdFormat.TYPE_IMAGE:
            case AdFormat.TYPE_GIF:
                // Set listener
                mImageView.setOnClickListener(this);
                // Set bitmap or animation drawable to ImageView.
                if (adFormat.getType() == AdFormat.TYPE_IMAGE) {
                    mImageView.setImageBitmap(adFormat.getImage());
                } else if (adFormat.getType() == AdFormat.TYPE_GIF) {
                    setImageOrGif(mImageView, adFormat);
                }
                
                // Create Layout Params
                LayoutParams lp = new LayoutParams(width, height);
                // Remove old imageView if procceed
                removeAllViews();
                // Add ImageView to Layout
                addView(mImageView, lp);
                break;
            case AdFormat.TYPE_VIDEO:
                // Remove listener from common baner
                mImageView.setOnClickListener(null);

                // Fit Video to Screen
                if (type == AdView.INTERSTITIAL) {
                    fitVideo(adFormat);
                }

                try {
                    LayoutParams lpVideo;

                    // Create mMediaPlayer
                    createVideoPlayer();
                    // Create Layout Params
                    if (type == AdView.INTERSTITIAL) {
                        lpVideo = new LayoutParams(adFormat.getWidth(), adFormat.getHeight());
                        lpVideo.addRule(RelativeLayout.CENTER_VERTICAL);
                    } else {
                        lpVideo = new LayoutParams(width, height);
                        // Reduce play/mute buttons size for banner ads
                        mVideoMute.setMaxHeight((int) (height / BANNER_BUTTONS_REDUCTION));
                        mVideoMute.setMinimumHeight((int) (height / BANNER_BUTTONS_REDUCTION));
                        mVideoPlay.setMaxHeight((int) (height / BANNER_BUTTONS_REDUCTION));
                        mVideoPlay.setMinimumHeight((int) (height / BANNER_BUTTONS_REDUCTION));

                    }
                    // Remove the old player if proceed
                    if (!videoSurfaceAdded) {
                        // Mark as added
                        videoSurfaceAdded = true;
                        // Add ImageView to Layout
                        addView(mVideoView, lpVideo);
                        // Add video buttons to layout
                        addView(mLayoutVideoButton);
                    } else {
                        // Reset Video
                        launchVideoFirstTime();
                    }
                } catch (Exception e) {
                }
                break;
            case AdFormat.TYPE_HTML5:
                if (mWebView == null) {
                    mWebView = new WebView(context.get());
                    mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
                    mWebView.setWebViewClient(new AdWebClient(adFormat));
                    mWebView.setWebChromeClient(new WebChromeClient());
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.getSettings().setPluginsEnabled(true);
                    mWebView.getSettings().setBuiltInZoomControls(false);
                    mWebView.setInitialScale(0);
                }

                // Create Layout Params
                LayoutParams lpWeb = new LayoutParams(width, height);
                // Remove old imageView if procceed
                removeAllViews();
                // Add ImageView to Layout
                addView(mWebView, lpWeb);

                // Load Url
                mWebView.loadUrl(adFormat.getUrl());
                break;
        }

        // Schedule the timer to remove the banner
        scheduleRemoveBanner();

        // Repaint
        invalidate();

        if (mAdViewListener != null)
            mAdViewListener.onAdDisplayed(this);
    }

    public Handler getHandler() {
        return handler;
    }

    private class HandleAdRunnable implements Runnable {
        private WeakReference<AdViewManager> adViewManagerReference;
        private WeakReference<AdView>        adViewReference;

        public HandleAdRunnable(AdViewManager adViewManager, AdView adView) {
            this.adViewManagerReference = new WeakReference<AdViewManager>(adViewManager);
            this.adViewReference = new WeakReference<AdView>(adView);
        }

        public void run() {
            // Get references
            final AdViewManager adViewManager = adViewManagerReference.get();
            AdView adView = adViewReference.get();

            // Check integrity of that references
            if (adViewManager != null && adView != null) {
                // Check if the layout has window
                if (!adView.hasWindow()) {
                    // Remove scheduled
                    adView.isScheduled = false;
                    // We don't change anything
                    return;
                }

                // Check if the banner can rotate (it isn't expandable, etc...)
                if (!adView.canHandleAd()) {
                    // Schedule rotate
                    adView.rotateDelayedAd(DELAY_TO_LOAD_NEW_AD);
                    // Finish Run
                    return;
                }

                try {
                    if (type == AdView.INTERSTITIAL) {
                        // Download Images
                        adViewManager.handleAdImages();
                    } else {
                        // Load Ad
                        adViewManager.handleAd();
                        // Scheduled Next Ad
                        adView.rotateDelayedAd(DELAY_TO_LOAD_NEW_AD);
                    }

                    // Scheduler Pings to Pixels
                    scheduler.schedule(new PingPixelsRunnable(adUnit), 0, TimeUnit.SECONDS);
                } catch (Exception e) // Exception caughted, try again
                {
                    if (RETRY_AD) {
                        // Get Context
                        Context context = adView.getContext();
    
                        // Check integrity
                        if (context != null) {
                            int seconds = DELAY_TO_RETRY_ERROR_CONNECTION;
    
                            if (!AdLibUtils.isInternetConnection(context)) {
                                // Change seconds
                                seconds = DELAY_TO_RETRY_NO_CONNECTION;
                            }
    
                            // Rotate Ad
                            adView.rotateDelayedAd(seconds);
                        }
                    }
                    if (adView.mAdViewListener != null)
                        adView.mAdViewListener.onAdNotFound(adView);
                }
            }
        }
    }

    public static class AdUnitFilledRunnable implements Runnable {
        private WeakReference<AdView> adViewRef;

        public AdUnitFilledRunnable(AdView adView) {
            this.adViewRef = new WeakReference<AdView>(adView);
        }

        @Override
        public void run() {
            AdView adView = adViewRef.get();

            if (adView != null) {
                // Push Ad to screen
                adView.pushAd();
            }
        }
    }

    public static class PingPixelsRunnable implements Runnable {
        private WeakReference<AdUnit> adUnitRef;

        public PingPixelsRunnable(AdUnit adUnit) {
            adUnitRef = new WeakReference<AdUnit>(adUnit);
        }

        @Override
        public void run() {
            // Get AdUnit
            AdUnit adUnit = adUnitRef.get();

            // Iterate all formats
            for (AdFormat adFormat : adUnit.getAdFormats()) {
                if (adFormat != null) {
                    // Iterate all pixels
                    for (String p : adFormat.getPixels()) {
                        if (p != null && p.length() > 7) {
                            try {
                                URL url = new URL(p);

                                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                                urlc.setRequestProperty("User-Agent",
                                        "Android Application: RealMedia AdLib");
                                urlc.setRequestProperty("Connection", "close");
                                urlc.setConnectTimeout(1000 * 30); // mTimeout
                                                                   // is in
                                                                   // seconds
                                urlc.connect();

                                if (urlc.getResponseCode() == 200) {
                                    // Download pixel
                                    @SuppressWarnings("unused")
                                    byte data[] = AdLibUtils.getFileContents(urlc.getInputStream());
                                    // Free memory
                                    data = null;
                                }

                                // Close Connection
                                urlc.disconnect();
                            } catch (Exception e) {
                                // Problem downloading pixel
                            }
                        }
                    }
                }
            }
        }
    }

    public static class RemoveAdRunnable implements Runnable {
        private WeakReference<AdView> adViewRef;

        public RemoveAdRunnable(AdView adView) {
            this.adViewRef = new WeakReference<AdView>(adView);
        }

        @Override
        public void run() {
            final AdView adView = adViewRef.get();

            if (adView != null) {
                // Get Handler
                Handler handler = adView.getHandler();

                // Check Integrity
                if (handler != null) {
                    // Run in UI
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Remove Banner
                            adView.removeBanner();
                        }
                    });
                }
            }
        }
    }

    public static class CollapseAdRunnable implements Runnable {
        private WeakReference<AdView> adViewRef;

        public CollapseAdRunnable(AdView adView) {
            this.adViewRef = new WeakReference<AdView>(adView);
        }

        @Override
        public void run() {
            final AdView adView = adViewRef.get();

            if (adView != null) {
                // Get Handler
                Handler handler = adView.getHandler();

                // Check Integrity
                if (handler != null) {
                    // Run in UI
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Collapse Ad
                            adView.collapseAd();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created");
        if (mMediaPlayer != null && mVideoHolder != null && mVideoHolder.getSurface().isValid()) {
            // Add Surface
            mMediaPlayer.setDisplay(mVideoHolder);
            // Start Video
            launchVideoFirstTime();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mMediaPlayer != null && holder.getSurface().isValid()) {
            // Set Holder
            mMediaPlayer.setDisplay(holder);
            // Start Video if it is necessary
            launchVideoFirstTime();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void setAdViewListener(AdViewListener adViewListener) {
        this.mAdViewListener = adViewListener;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public interface AdViewListener {
        public void onAdDisplayed(AdView view);

        public void onAdCollapsed(AdView view);

        public void onAdClicked(AdView view);

        public void onAdNotFound(AdView view);

        public void onAdRemoved(AdView view);
    }

    private class AdWebClient extends WebViewClient {

        private final AdFormat mAdFormat;

        public AdWebClient(AdFormat adFormat) {
            super();
            mAdFormat = adFormat;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (shouldOpenInNavigator(url)) {
                context.get().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } else {
                mWebView.loadUrl(url);
            }
            return true;
        }

        /*
         * Should open url in a new navigator if: 1. Property 'shouldOpenInNavigator' is true 2. URL
         * is in a different domain
         */
        private boolean shouldOpenInNavigator(String url) {
        	Log.v("AdView URL","opening current URL:"+url);
            String currentHost = Uri.parse(mWebView.getUrl()).getHost();
            String newHost = Uri.parse(url).getHost();
            return (mAdFormat.isOpenInNavigator() && (!newHost.equalsIgnoreCase(currentHost)));
        }
    }
}
