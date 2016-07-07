package com.phoneutils.crosspromotion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class Utility 
{
	public Utility() {
	}

	public static void openAppSettings(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", context.getPackageName(), null);
		intent.setData(uri);
		context.startActivity(intent);
	}

	public static void showPermissionRequiredDialog(final Context activity, String title, String msg, DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder builder;// = new AlertDialog.Builder(activity);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
		} else {
			builder = new AlertDialog.Builder(activity);
		}

		builder.setCancelable(true);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(R.string.go_to_app_settings, onClickListener);

		builder.show();
	}

	public static boolean isConnected(Context context){
		ConnectivityManager cm =
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
				activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}
}
