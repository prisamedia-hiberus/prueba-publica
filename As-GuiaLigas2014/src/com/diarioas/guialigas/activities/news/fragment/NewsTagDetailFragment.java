package com.diarioas.guialigas.activities.news.fragment;

import java.lang.reflect.InvocationTargetException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.fragment.GeneralFragment;
import com.diarioas.guialigas.dao.model.news.NewsItemTag;
import com.diarioas.guialigas.dao.reader.RemoteDataDAO;
import com.diarioas.guialigas.dao.reader.RemoteNewsDAO.NewsTag;
import com.diarioas.guialigas.utils.Defines.Prefix;

public class NewsTagDetailFragment extends GeneralFragment {

	private Context mContext;

	private NewsItemTag currentItem = null;
	private boolean needAutoLoad = false;
	private boolean alreadyLoaded = false;
	private int position = -1;

	/***************************************************************************/
	/** Fragment lifecycle methods **/
	/***************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity().getApplicationContext();

		if (getArguments().containsKey("currentNews")) {
			currentItem = (NewsItemTag) getArguments().getSerializable(
					"currentNews");
		}
		if (getArguments().containsKey("needAutoLoad")) {
			needAutoLoad = getArguments().getBoolean("needAutoLoad", false);
		}
		if (getArguments().containsKey("position")) {
			position = getArguments().getInt("position");
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
					.findViewById(R.id.webview_rich_push);
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
		generalView = inflater.inflate(R.layout.tag_detail_fragment, container,
				false);
		return generalView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		configureView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mContext = null;
		generalView = null;
	}

	private void configureView() {

		startAnimation();
		configureWebView();

		if (this.needAutoLoad) {
			loadInformation();
		}
	}

	@SuppressLint("NewApi")
	private void configureWebView() {

		WebView newsWebView = (WebView) generalView
				.findViewById(R.id.webview_rich_push);
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
				stopAnimation();
			}
		});

	}

	/******************* Load information ********************/

	private void loadInformation() {
		if (generalView != null && currentItem != null
				&& currentItem.getLink() != null
				&& currentItem.getLink().length() > 0 && !alreadyLoaded) {
			alreadyLoaded = true;

			String movileString = currentItem.getLink();
			if (!currentItem.getLink().contains(NewsTag.as_domain_mobile)) {
				movileString = currentItem.getLink().replace(NewsTag.as_domain,
						NewsTag.as_domain_mobile);
			}

			if (RemoteDataDAO.getInstance(mContext).getGeneralSettings() != null
					&& RemoteDataDAO.getInstance(mContext).getGeneralSettings()
							.getPrefix() != null
					&& RemoteDataDAO.getInstance(mContext).getGeneralSettings()
							.getPrefix().get(Prefix.NEWS_SUFFIX) != null) {
				movileString = movileString
						+ RemoteDataDAO.getInstance(mContext)
								.getGeneralSettings().getPrefix()
								.get(Prefix.NEWS_SUFFIX)
						+ Prefix.NEWS_SUFFIX_SO;
			}
			
			WebView newsWebView = (WebView) generalView
					.findViewById(R.id.webview_rich_push);
			newsWebView.loadUrl(movileString);
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
