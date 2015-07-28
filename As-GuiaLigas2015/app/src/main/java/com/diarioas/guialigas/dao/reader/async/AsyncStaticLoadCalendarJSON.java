package com.diarioas.guialigas.dao.reader.async;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guialigas.dao.model.calendar.Fase;
import com.diarioas.guialigas.dao.reader.DatabaseDAO;
import com.diarioas.guialigas.dao.reader.parser.ParseJSONCalendar;
import com.diarioas.guialigas.utils.Defines.Prefix;

public class AsyncStaticLoadCalendarJSON extends
		AsyncTask<String, Void, ArrayList<Fase>> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncSettingsCalendarJSONListener {
		void onSuccessfulExecute(ArrayList<?> data);

		void onFailureExecute();
	}

	AsyncSettingsCalendarJSONListener listener;
	Context appContext;
	boolean error;

	public AsyncStaticLoadCalendarJSON(
			AsyncSettingsCalendarJSONListener listener, Context ctx) {
		this.listener = listener;
		this.error = false;
		this.appContext = ctx;

	}

	@Override
	protected ArrayList<Fase> doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			ArrayList<Fase> fases = null;

			// fases = downloadFeedAtUrl(urls);
			String strFileContents = ReadRemote.readRemoteFile(urls[0], true);
			// ReadRemote.copyFile(appContext,urls[1], strFileContents);

			ParseJSONCalendar parse = new ParseJSONCalendar();
			String dataPrefix = DatabaseDAO.getInstance(appContext).getPrefix(
					Prefix.PREFIX_DATA);
			fases = parse.parsePlistCalendar(strFileContents, dataPrefix);

			// Se descarga siempre de remoto
			// if (fases == null || fases.size() == 0) {
			// fases = parseLocalFeedAtUrl(urls[1]);
			// }

			if (fases == null || fases.size() == 0) {
				this.error = true;
			}

			return fases;

		} catch (IOException e) {
			this.error = true;
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.error = true;
			return null;
		}

	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(ArrayList<Fase> fases) {

		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(fases);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	// private ArrayList<Fase> parseLocalFeedAtUrl(String fileName)
	// throws IOException, Exception {
	// InputStream is = null;
	// String strFileContents;
	// BufferedReader br = null;
	// try {
	//
	// File sectionCacheFile = new File(appContext.getFilesDir(), fileName);
	// StringBuilder sb = new StringBuilder();
	//
	// int len;
	//
	// if (sectionCacheFile.exists() == false) {
	// strFileContents = FileUtils.readFileFromAssets(appContext
	// .getResources().getAssets(), fileName);
	// } else {
	//
	// FileInputStream fileInputStream = new FileInputStream(
	// sectionCacheFile.getPath());
	// br = new BufferedReader(new InputStreamReader(fileInputStream,
	// "utf8"), 1024);
	// char[] buffer = new char[1024];
	// while ((len = br.read(buffer)) != -1) {
	// sb.append(new String(buffer, 0, len));
	// }
	// strFileContents = sb.toString();
	//
	// }
	//
	// ParseJSONCalendar parse = new ParseJSONCalendar();
	// return parse.parsePlistCalendar(strFileContents);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// this.error = true;
	// return null;
	// } finally {
	// if (is != null) {
	// is.close();
	// }
	// if (br != null)
	// br.close();
	// }
	// }

	/**
	 * @param string
	 * @return
	 * @throws IOException
	 */
	// private ArrayList<Fase> downloadFeedAtUrl(String[] urls) throws
	// IOException {
	// BufferedReader rd = null;
	// String strFileContents = null;
	//
	// HttpClient client;
	// HttpGet request;
	// HttpResponse response;
	//
	// try {
	// client = new DefaultHttpClient();
	// request = new HttpGet(urls[0]);
	// request.addHeader("Accept-Encoding", "gzip");
	// response = client.execute(request);
	//
	// InputStream instream = response.getEntity().getContent();
	// Header contentEncoding = response
	// .getFirstHeader("Content-Encoding");
	// if (contentEncoding != null
	// && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
	// instream = new GZIPInputStream(instream);
	// }
	//
	// // Get the response
	// rd = new BufferedReader(new InputStreamReader(instream));
	//
	// String line = "";
	// StringBuilder sb = new StringBuilder();
	//
	// while ((line = rd.readLine()) != null) {
	// sb.append(line);
	// }
	//
	// strFileContents = sb.toString();
	//
	// copyFile(urls[1], strFileContents);
	//
	// ParseJSONCalendar parse = new ParseJSONCalendar();
	// return parse.parsePlistCalendar(strFileContents);
	//
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	//
	// } finally {
	// if (rd != null) {
	// rd.close();
	// }
	//
	// }
	// return null;
	//
	// }

}
