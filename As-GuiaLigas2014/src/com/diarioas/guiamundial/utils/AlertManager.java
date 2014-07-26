/**
 * 
 */
package com.diarioas.guiamundial.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.diarioas.guiamundial.R;

/**
 * @author robertosanchez
 * 
 */
public class AlertManager {

	public static void showAlertOkDialog(Context ctx, String message,
			String title, DialogInterface.OnClickListener listener) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
		alertDialogBuilder.setTitle(title);

		alertDialogBuilder.setMessage(message);

		alertDialogBuilder.setIcon(R.drawable.icon).setCancelable(false)
				.setNeutralButton(R.string.accept, listener);

		AlertDialog alertDialog = alertDialogBuilder.create();

		if (!((Activity) ctx).isFinishing()) {
			alertDialog.show();
		}

		return;
	}
}
