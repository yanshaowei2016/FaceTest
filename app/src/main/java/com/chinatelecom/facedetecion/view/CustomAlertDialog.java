package com.chinatelecom.facedetecion.view;

import com.chinatelecom.facedetecion.R;

import android.app.Dialog;
import android.content.Context;

public class CustomAlertDialog extends Dialog {

	public CustomAlertDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, R.style.CustomAlertDialog);
		setCancelable(cancelable);
		setOnCancelListener(cancelListener);
	}

	public CustomAlertDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public CustomAlertDialog(Context context) {
		this(context, R.style.CustomAlertDialog);
		// TODO Auto-generated constructor stub
	}

}
