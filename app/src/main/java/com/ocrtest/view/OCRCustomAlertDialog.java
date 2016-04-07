package com.ocrtest.view;

import android.app.Dialog;
import android.content.Context;

import com.chinatelecom.facedetecion.R;

public class OCRCustomAlertDialog extends Dialog {

	public OCRCustomAlertDialog(Context context, int theme) {
		super(context, theme);
	}

	public OCRCustomAlertDialog(Context context) {
		this(context, R.style.OcrDialog);
	}

}
