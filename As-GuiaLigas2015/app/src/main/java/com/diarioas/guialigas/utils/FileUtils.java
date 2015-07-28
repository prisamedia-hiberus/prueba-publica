/**
 * 
 */
package com.diarioas.guialigas.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.AssetManager;
import android.util.Log;

/**
 * @author robertosanchez
 * 
 */
public class FileUtils {

	public static String readFileFromAssets(AssetManager assetManager,
			String url) {
		BufferedReader br = null;
		InputStream in = null;
		StringBuilder sb = new StringBuilder();
		String strFileContents = null;
		try {
			in = assetManager.open(url);

			br = new BufferedReader(new InputStreamReader(in, "utf8"), 1024);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			strFileContents = sb.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("FileUtils", "No se ha encontrado el fichero: " + url);
			// e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		return strFileContents;

	}
}
