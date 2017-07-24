package com.diarioas.guialigas.activities.palmares;

import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.diarioas.guialigas.R;
import com.diarioas.guialigas.activities.general.GeneralFragmentActivity;
import com.diarioas.guialigas.dao.reader.CookieDAO;
import com.diarioas.guialigas.utils.AlertManager;
import com.diarioas.guialigas.utils.Defines.NativeAds;
import com.diarioas.guialigas.utils.Reachability;

public class PalmaresDetailActivity extends GeneralFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_palmares_detail);

		configureView();
		if (Reachability.isOnline(getApplicationContext())) {
			startAnimation();

			String url = getIntent().getExtras().getString("url");

			WebView webview_palmares = (WebView) findViewById(R.id.webview_palmares);
			webview_palmares.setWebViewClient(new MyWebViewClient());

			webview_palmares.loadUrl(url);
		} else {
			AlertManager.showAlertOkDialog(this,
					getResources().getString(R.string.section_not_conection),
					getResources().getString(R.string.connection_error_title),
					new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							onBackPressed();
						}

					});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		callToAds(NativeAds.AD_PALMARES+NativeAds.AD_DETAIL, false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		super.onBackPressed();
		overridePendingTransition(R.anim.null_anim, R.anim.slide_out_left);
	}

	private void configureView() {

		spinner = (RelativeLayout) findViewById(R.id.spinner);

		CookieDAO.getInstance(getApplicationContext()).initCookie();

		configActionBar();

	}

	/***************************************************************************/
	/** WebView methods **/
	/***************************************************************************/
	class MyWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler,
									   SslError error) {
			AlertManager.showCancleableDialog(getBaseContext(),
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
			stopAnimation();
		}

	}
}
