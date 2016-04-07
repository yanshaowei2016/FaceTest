package com.ocrtest.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast mToast;

	public static void showToast(Context context, String text) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		mToast.show();
	}

}
