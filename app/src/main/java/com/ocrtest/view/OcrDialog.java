package com.ocrtest.view;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.chinatelecom.facedetecion.R;


public class OcrDialog extends DialogFragment implements OnClickListener {
	private static ocrDialogListener ocrDialogListener;
	
	// 定义回调事件，用于dialog的点击事件
	public interface ocrDialogListener {
		public void confirm();
		public void cancel();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public static OcrDialog newInstance(ocrDialogListener myDialogListener){
		ocrDialogListener = myDialogListener;
		OcrDialog fragment = new OcrDialog();
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(false);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.ocr_idcard_dialog, null);
		Button btnCancel = (Button) v.findViewById(R.id.ocr_dialog_btn_cancel);
		btnCancel.setOnClickListener(this);

		Button btnConfirm = (Button) v.findViewById(R.id.or_dialog_btn_confirm);
		btnConfirm.setOnClickListener(this);
		
		OCRCustomAlertDialog dialog = new OCRCustomAlertDialog(getActivity());
		dialog.addContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		return dialog;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ocr_dialog_btn_cancel){
			ocrDialogListener.cancel();
			dismiss();
		} else if (v.getId() == R.id.or_dialog_btn_confirm){
			ocrDialogListener.confirm();
			dismiss();
		}
	}
	
}
