package com.diarioas.guiamundial.dao.reader.async;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.diarioas.guiamundial.dao.model.player.Player;
import com.diarioas.guiamundial.dao.reader.DatabaseDAO;
import com.diarioas.guiamundial.dao.reader.parser.ParseJSONPlayer;
import com.diarioas.guiamundial.utils.Defines.Prefix;
import com.diarioas.guiamundial.utils.Reachability;

public class AsyncLoadPlayerJSON extends AsyncTask<String, Void, Player> {

	// final String nameFile = "ddbb_settings.plist";

	public interface AsyncLoadPlayerJSONListener {
		void onSuccessfulExecute(Player playerteam);

		void onFailureExecute();
	}

	AsyncLoadPlayerJSONListener listener;
	Context appContext;
	boolean error;
	private final Player currentPlayer;

	public AsyncLoadPlayerJSON(AsyncLoadPlayerJSONListener listener,
			Context ctx, Player currentPlayer) {
		this.listener = listener;
		this.error = false;
		this.currentPlayer = currentPlayer;
		this.appContext = ctx;
	}

	@Override
	protected Player doInBackground(String... urls) {

		// params comes from the execute() call: params[0] is the url.
		this.error = false;
		try {
			Player player = null;
			if (Reachability.isOnline(this.appContext) == true) {

				// player = downloadFeedAtUrl(urls[0]);
				String strFileContents = ReadRemote.readRemoteFile(urls[0],
						true);
				ParseJSONPlayer parse = new ParseJSONPlayer();
				String imagePrefix = DatabaseDAO.getInstance(appContext)
						.getPrefix(Prefix.PREFIX_IMAGE);
				Player playerFinal = parse.parseJSONPlayer(currentPlayer,
						strFileContents, imagePrefix);

				if (playerFinal != null) {
					DatabaseDAO.getInstance(appContext).updatePlayer(
							playerFinal);
					return playerFinal;
				} else {
					return currentPlayer;
				}

			} else {
				// if (player == null) {
				this.error = true;
				// }
				return player;
			}

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
	protected void onPostExecute(Player player) {
		if (this.listener != null) {
			if (this.error == false) {
				this.listener.onSuccessfulExecute(player);
			} else {
				this.listener.onFailureExecute();
			}

		}
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	// private Player downloadFeedAtUrl(String myurl) throws IOException,
	// Exception {
	// InputStream is = null;
	//
	// try {
	// // TODO: Agregarle el id del equipo
	// // String dataPrefix =
	// // DatabaseDAO.getInstance(appContext).getPrefix(
	// // Defines.ReturnPrefix.PREFIX_DATA);
	// // URL urlAux = new URL(dataPrefix + myurl);
	// URL urlAux = new URL(myurl);
	// URI uri = new URI(urlAux.getProtocol(), urlAux.getUserInfo(),
	// urlAux.getHost(), urlAux.getPort(), urlAux.getPath(),
	// urlAux.getQuery(), urlAux.getRef());
	// URL url = uri.toURL();
	//
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setReadTimeout(15000 /* milliseconds */);
	// conn.setConnectTimeout(30000 /* milliseconds */);
	// conn.setRequestMethod("GET");
	// conn.setDoInput(true);
	// // Starts the query
	// conn.connect();
	// int response = conn.getResponseCode();
	//
	// if (response != 200) {
	// return null;
	// }
	//
	// is = new BufferedInputStream(conn.getInputStream());
	//
	// String strFileContents = null;
	// FileOutputStream outputStream;
	//
	// try {
	//
	// byte[] buffer = new byte[1024];
	// int len;
	// StringBuilder sb = new StringBuilder();
	// while ((len = is.read(buffer)) != -1) {
	// sb.append(new String(buffer, 0, len));
	// }
	//
	// strFileContents = sb.toString();
	// if (strFileContents == null) {
	// return null;
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// ParseJSONPlayer parse = new ParseJSONPlayer();
	// Player playerFinal = parse.parseJSONPlayer(currentPlayer,
	// strFileContents);
	//
	// if (playerFinal != null) {
	// DatabaseDAO.getInstance(appContext).updatePlayer(playerFinal);
	// return playerFinal;
	// } else {
	// return currentPlayer;
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// return null;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// } finally {
	// // Makes sure that the InputStream is closed after the app is
	// // finished using it.
	// if (is != null) {
	// is.close();
	// }
	// }
	// }

}
