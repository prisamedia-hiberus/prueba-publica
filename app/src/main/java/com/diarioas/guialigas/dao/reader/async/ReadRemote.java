package com.diarioas.guialigas.dao.reader.async;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author robertosanchez
 * 
 */
public class ReadRemote {

	public static String readRemoteFile(String myurl, boolean gzip)
			throws IOException, Exception {
		return readRemoteFile(myurl, gzip, 0, 0);
	}

	public static String readRemoteFile(String myurl, boolean gzip,
			int timeoutConnection, int timeoutSocket) throws IOException,
			Exception {

		BufferedReader rd = null;
		String strFileContents = null;
		try {
			URL url = new URL(myurl);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setConnectTimeout(timeoutConnection);
			connection.setReadTimeout(timeoutSocket);
			if (gzip) {
				connection.setRequestProperty("Accept-Encoding", "gzip");
			}
			connection.connect();
			InputStream is = connection.getInputStream();
			if (gzip) {
				if ("gzip".equals(connection.getContentEncoding())) {
					is = new GZIPInputStream(connection.getInputStream());
				}
			}
			rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			strFileContents = sb.toString();
			if (strFileContents.startsWith("\uFEFF")) {
				strFileContents = strFileContents.replace("\uFEFF", "");
			}

			return strFileContents;
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
		return strFileContents;
	}

	public static void copyFile(Context ctx, String name, String strFileContents)
			throws FileNotFoundException, IOException {
		FileOutputStream outputStream;
		File file = new File(ctx.getFilesDir(), name);
		if (file.exists()) {
			file.delete();
		}
		outputStream = ctx.openFileOutput(name, Context.MODE_PRIVATE);
		outputStream.write(strFileContents.getBytes());
		if (outputStream != null) {
			outputStream.close();
		}
		return;
	}

}
