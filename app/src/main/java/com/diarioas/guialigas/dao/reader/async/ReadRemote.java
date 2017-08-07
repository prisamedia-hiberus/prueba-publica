package com.diarioas.guialigas.dao.reader.async;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

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

		HttpClient client;
		HttpGet request;
		HttpResponse response;

		try {

			HttpParams httpParameters = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			client = new DefaultHttpClient(httpParameters);
			request = new HttpGet(myurl);

			request.addHeader("Cache-Control", "no-cache");
			if (gzip) {
				request.addHeader("Accept-Encoding", "gzip");
			}
			response = client.execute(request);

			InputStream instream = response.getEntity().getContent();

			if (gzip) {
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}
			}

			rd = new BufferedReader(new InputStreamReader(instream, "UTF-8"));

			String line = "";
			StringBuilder sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			strFileContents = sb.toString();
			if (strFileContents.startsWith("\uFEFF")) {
				strFileContents = strFileContents.replace("\uFEFF", "");
			}

			return strFileContents;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (rd != null) {
				rd.close();
			}

		}
		return null;
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
