package com.prisadigital.realmedia.adlib;

import com.prisadigital.realmedia.adlib.data.AdUnit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class AdActivity extends Activity implements View.OnClickListener, AdView.AdViewListener {
    // Layout
    private RelativeLayout layout;
    private ImageButton    closeButton;
    private ProgressBar    spinner;
    // Internal AdView
    private AdView         adView;
    // AdUnit preloaded (for interstitials)
    private AdUnit         adUnit;
    // Configuration
    private String         adKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Extras
        adKey = getIntent().getStringExtra("adKey");
        adUnit = (AdUnit) getIntent().getParcelableExtra("adUnit");

        // Check if the data is correct
        if (adKey != null && adKey.length() > 0) {
            // Prepare Layout
            prepareLayout();

            // Hide status bar
            requestWindowFeature(Window.FEATURE_NO_TITLE); 

            // Show Screen
            setContentView(layout);
        } else {
            Log.e(AdView.TAG,
                    "The adKey and/or adPosition provisioned is not correct. Please, be sure the adKey/adPosition are the correct keys provisioned by RealMedia.");
            // Close interstitial
            finish();
        }
    }

    private void prepareLayout() {
        RelativeLayout.LayoutParams lp;

        // Create Layout
        layout = new RelativeLayout(this);
        // Fill all screen
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        // Create Spinner
        spinner = new ProgressBar(this, null, android.R.attr.progressBarStyle);
        spinner.setIndeterminate(false);
        spinner.setVisibility(View.VISIBLE);
        // Create the params
        RelativeLayout.LayoutParams lpSpinner = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpSpinner.addRule(RelativeLayout.CENTER_IN_PARENT);
        // Add to Layout
        layout.addView(spinner, lpSpinner);

        // Create adView
        adView = new AdView(this, AdView.INTERSTITIAL, adKey, adUnit);
        // Set Listener
        adView.setAdViewListener(this);
        // Create the Params
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        // lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        // lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        // Add to Layout
        layout.addView(adView, lp);

        // Create Close Button
        closeButton = new ImageButton(this);
        // Set Image
        closeButton.setBackgroundDrawable(null);
        closeButton.setImageResource(R.drawable.rmadlib_close);
        closeButton.setPadding(9, 0, 9, 0);
        // Set Listener
        closeButton.setOnClickListener(this);
        // Create the Params
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        // Add to Layout
        layout.addView(closeButton, lp);
    }

    @Override
    public void onClick(View v) {
        if (v == closeButton) {
            // Close activity
            finish();
        }
    }

    @Override
    public void onAdDisplayed(AdView view) {
        if (spinner != null) {
            spinner.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAdCollapsed(AdView view) {
        // Close Interstitial when the expandable is closed
        finish();
    }

    @Override
    public void onAdClicked(AdView view) {
        // Close Interstitial if the user click on the ad
        finish();
    }

    @Override
    public void onAdNotFound(AdView view) {
        // No Ad found
        finish();
    }

    @Override
    public void onAdRemoved(AdView view) {
        finish();
    }
}
