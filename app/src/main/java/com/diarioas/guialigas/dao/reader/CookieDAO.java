package com.diarioas.guialigas.dao.reader;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.diarioas.guialigas.utils.Defines.ReturnSharedPreferences;
import com.diarioas.guialigas.utils.Defines.URL_DATA;

public class CookieDAO {

	private static CookieDAO sInstance;
	private Context mContext;

	public static CookieDAO getInstance(Context ctx) {
		if (sInstance == null) {
			sInstance = new CookieDAO();
			sInstance.mContext = ctx;
		}
		return sInstance;
	}

	public void initCookie() {
		// Inserto la Cookie de avisopc
		this.setCookie(URL_DATA.URL_SITE,
				ReturnSharedPreferences.SP_COOKIE_NAME_AVISOPC, "true");

	}

	private void setCookie(String siteName, String cookieName,
			String cookieValue) {

		CookieSyncManager.createInstance(mContext);

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		String cookieString = cookieName + "=" + cookieValue + ";";
		cookieManager.setCookie(siteName, cookieString);

		CookieSyncManager.getInstance().sync();
	}

	public void deleteOnlyAppCookies() {
		// CookieSyncManager.createInstance(mContext);
		// CookieManager cookieManager = CookieManager.getInstance();
		// cookieManager.setAcceptCookie(true);
		//
		// // String cookieString = ReturnSharedPreferences.SP_COOKIE_NAME_UID +
		// "=";
		// // cookieManager.setCookie(URL_DATA.URL_SITE, cookieString);
		// // Log.d("ELPAIS", "Borro la cookie del WS: "
		// // + ReturnSharedPreferences.SP_COOKIE_NAME_UID);
		//
		//
		// CookieSyncManager.getInstance().sync();

	}

}
