package com.diarioas.guialigas.activities.link.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.SectionFragment;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.utils.AlertManager;

public class LinkSectionFragment extends SectionFragment {

	private WebView webview;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.adSection = NativeAds.AD_LINK + "/" + NativeAds.AD_PORTADA;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		// Inflating layout
		generalView = inflater.inflate(R.layout.fragment_link_section,
				container, false);
		return generalView;
	}

	/***************************************************************************/
	/** Configuration methods **/
	/***************************************************************************/

	@SuppressLint("NewApi")
	@Override
	protected void configureView() {

		webview = (WebView) generalView.findViewById(R.id.webview);
		webview.setWebViewClient(new MyWebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			webview.getSettings().setDisplayZoomControls(false);
		}
	}

	@Override
	protected void loadInformation() {
		webview.loadUrl(section.getUrl());
	}

	@Override
	protected void callToOmniture() {
		// StatisticsDAO.getInstance(mContext).sendStatisticsState(
		// getActivity().getApplication(),
		// Omniture.SECTION_LINK,
		// null,
		// null,
		// null,
		// Omniture.TYPE_PORTADA,
		// Omniture.TYPE_PORTADA + ":" + Omniture.SECTION_LINK + " "
		// + Omniture.TYPE_PORTADA, null);
	}

	/***************************************************************************/
	/** WebView methods **/
	/***************************************************************************/
	class MyWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler,
									   SslError error) {
			AlertManager.showCancleableDialog(getActivity(),
					getResources().getString(R.string.section_ssl_error),
					getResources().getString(R.string.sll_error_title),
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									handler.proceed();
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									handler.cancel();
									break;
							}
							dialog.dismiss();
						}

					}, true);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			((HomeActivity) getActivity()).stopAnimation();
		}

	}

}
