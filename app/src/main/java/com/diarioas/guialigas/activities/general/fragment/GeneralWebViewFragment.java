package com.diarioas.guialigas.activities.general.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.home.HomeActivity;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Defines.SECTIONS;

import java.lang.reflect.InvocationTargetException;

public class GeneralWebViewFragment extends SectionFragment {

	private boolean alreadyLoaded = false;
	private View errorContainer = null;
	private SwipeRefreshLayout swipeRefreshLayout = null;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (section != null && section.getType() != null
				&& section.getType().equalsIgnoreCase(SECTIONS.PALMARES)) {
			this.adSection = NativeAds.AD_PALMARES + "/" + NativeAds.AD_PORTADA;
		} else if (section != null && section.getType() != null
				&& section.getType().equalsIgnoreCase(SECTIONS.STADIUMS)) {
			this.adSection = NativeAds.AD_STADIUMS + "/" + NativeAds.AD_PORTADA;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		pauseDetailWebView();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			loadInformation();
		}
	}

	public void pauseDetailWebView() {

		if (generalView != null) {
			WebView newsWebView = (WebView) generalView
					.findViewById(R.id.webview_information);
			if (newsWebView != null) {
				String javascriptCall = "javascript:(noticia_pierde_foco())";
				newsWebView.loadUrl(javascriptCall);
				try {
					Class.forName("android.webkit.WebView")
							.getMethod("onPause", (Class[]) null)
							.invoke(newsWebView, (Object[]) null);

				} catch (ClassNotFoundException cnfe) {
				} catch (NoSuchMethodException nsme) {
				} catch (InvocationTargetException ite) {
				} catch (IllegalAccessException iae) {
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		this.generalView = inflater.inflate(R.layout.general_webview,
				container, false);
		return generalView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mContext = null;
		generalView = null;
	}

	@Override
	protected void configureView() {

		configureErrorView();
		configureWebView();
		callToOmniture();
		configureSwipeLoader();
	}

	private void configureErrorView() {
		errorContainer = getErrorContainer();
		if (errorContainer != null) {
			errorContainer.setVisibility(View.INVISIBLE);
			((RelativeLayout) generalView).addView(errorContainer);
		}
	}

	private void configureSwipeLoader() {
		this.swipeRefreshLayout = (SwipeRefreshLayout) generalView
				.findViewById(R.id.swipe_container);
		this.swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadInformation();
			}
		});
		this.swipeRefreshLayout.setColorSchemeResources(R.color.black,
				R.color.red, R.color.white);
	}

	@SuppressLint("NewApi")
	private void configureWebView() {

		WebView newsWebView = (WebView) generalView
				.findViewById(R.id.webview_information);
		newsWebView.getSettings().setJavaScriptEnabled(true);
		newsWebView.setWebChromeClient(new WebChromeClient());
		newsWebView.getSettings().setAllowFileAccess(true);
		newsWebView.getSettings().setPluginState(PluginState.ON);
		newsWebView.getSettings().setDomStorageEnabled(true);

		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
			newsWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);

		newsWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			public void onPageFinished(WebView view, String url) {
				((HomeActivity) getActivity()).stopAnimation();
			}
		});

	}

	/******************* Load information ********************/

	@Override
	protected void loadInformation() {
		closePullToRefresh();
		if (generalView != null && this.section != null
				&& this.section.getUrl() != null
				&& this.section.getUrl().length() > 0 && !alreadyLoaded) {
			alreadyLoaded = true;
			((HomeActivity) getActivity()).startAnimation();
			WebView newsWebView = (WebView) generalView
					.findViewById(R.id.webview_information);
			newsWebView.loadUrl(this.section.getUrl());
		} else if (this.section == null || this.section.getUrl() == null
				|| this.section.getUrl().length() == 0) {
			if (errorContainer != null) {
				errorContainer.setVisibility(View.VISIBLE);
			}
			((HomeActivity) getActivity()).stopAnimation();
		}
	}

	private void closePullToRefresh() {
		if (this.swipeRefreshLayout != null) {
			this.swipeRefreshLayout.setRefreshing(false);
		}
	}

	@Override
	protected void callToOmniture() {
		
//		String omnitureSection = null;
//		if (section != null && section.getType() != null
//				&& section.getType().equalsIgnoreCase(SECTIONS.PALMARES)) {
//			omnitureSection = Omniture.SECTION_PALMARES;
//		} else if (section != null && section.getType() != null
//				&& section.getType().equalsIgnoreCase(SECTIONS.STADIUMS)) {
//			omnitureSection = Omniture.SECTION_SEDES;
//		}
//
//		StatisticsDAO.getInstance(mContext)
//		.sendStatisticsState(
//				getActivity().getApplication(),
//				omnitureSection,
//				null,
//				null,
//				null,
//				Omniture.TYPE_PORTADA,
//				Omniture.DETAILPAGE_PORTADA + " "
//						+ omnitureSection, null);

	}
}
