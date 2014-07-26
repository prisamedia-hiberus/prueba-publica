/**
 * 
 */
package com.diarioas.guiamundial.dao.reader.async;

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

import android.content.Context;

/**
 * @author robertosanchez
 * 
 */
public class ReadRemote {

	public static String readRemoteFile(String myurl, boolean gzip)
			throws IOException, Exception {

		BufferedReader rd = null;
		String strFileContents = null;

		HttpClient client;
		HttpGet request;
		HttpResponse response;

		try {
			client = new DefaultHttpClient();
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
			// Get the response
			rd = new BufferedReader(new InputStreamReader(instream));

			String line = "";
			StringBuilder sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			strFileContents = sb.toString();
			// strFileContents = strFileContents.replaceAll("\\t", "");
			return strFileContents;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
