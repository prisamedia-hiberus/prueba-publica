/**
 * 
 */
package com.diarioas.guialigas.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.diarioas.guialigas.R;

/**
 * @author robertosanchez
 * 
 */
public class AlertManager {

	public static void showCancleableDialog(Context ctx, String message, String title,
											DialogInterface.OnClickListener listener,
											boolean cancelable) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		alertDialogBuilder.setTitle(title);

		alertDialogBuilder.setMessage(message);

		alertDialogBuilder.setIcon(R.drawable.icon).setCancelable(cancelable)
				.setPositiveButton(R.string.accept, listener);

		if (cancelable) {
			alertDialogBuilder.setNegativeButton(R.string.cancel, listener);
		}

		AlertDialog alertDialog = alertDialogBuilder.create();

		if (!((Activity) ctx).isFinishing()) {
			alertDialog.show();
		}

		return;
	}

	public static void showAlertOkDialog(Context ctx, String message,
										 String title, DialogInterface.OnClickListener listener) {

		showCancleableDialog(ctx, message, title, listener, false);
	}
}
