package com.chinatelecom.facedetecion.view;

import com.chinatelecom.face.Face_BaseActivity;
import com.chinatelecom.facedetecion.R;
import com.chinatelecom.facedetecion.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleDialogFragment extends DialogFragment implements
		OnClickListener {

	private static final String ARGS_REQUEST_MSG = "request_msg";
	private static final String ARGS_REQUEST_TITLE = "request_title";
	private static final String ARGS_REQUEST_POS_TEXT = "request_pos";
	private static final String ARGS_REQUEST_NEG_TEXT = "request_neg";
	private final static String ARG_REQUEST_CODE = "request_code";
	private final static int DEFAULT_REQUEST_CODE = -42;

	private Face_BaseActivity activity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (Face_BaseActivity) activity;
	}

	public static SimpleDialogFragment newInstance(String title, String msg,
			String posText, String negText) {
		SimpleDialogFragment fragment = new SimpleDialogFragment();
		Bundle args = new Bundle();
		args.putString(ARGS_REQUEST_MSG, msg);
		args.putString(ARGS_REQUEST_TITLE, title);
		args.putString(ARGS_REQUEST_POS_TEXT, posText);
		args.putString(ARGS_REQUEST_NEG_TEXT, negText);
		args.putInt(ARG_REQUEST_CODE, DEFAULT_REQUEST_CODE);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setCancelable(false);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.custom_alert_dialog, null);
		TextView titleTv = (TextView) v.findViewById(R.id.alertTitle);
		TextView massageTV = (TextView) v.findViewById(R.id.message);
		Button posBtn = (Button) v.findViewById(R.id.positiveButton);
		Button negBtn = (Button) v.findViewById(R.id.negativeButton);
		Bundle bundle = getArguments();
		String title = bundle.getString(ARGS_REQUEST_TITLE);
		String message = bundle.getString(ARGS_REQUEST_MSG);
		String posText = bundle.getString(ARGS_REQUEST_POS_TEXT);
		String negText = bundle.getString(ARGS_REQUEST_NEG_TEXT);
		((LinearLayout) v.findViewById(R.id.title_template))
				.setVisibility(View.GONE);

		if (message != null && !"".equals(message)) {
			massageTV.setText(message);
		}

		if (posText != null && !"".equals(posText)) {
			posBtn.setVisibility(View.VISIBLE);
			posBtn.setText(posText);
		} else {
			posBtn.setVisibility(View.GONE);
		}

		if (negText != null && !"".equals(negText)) {
			negBtn.setVisibility(View.VISIBLE);
			negBtn.setText(negText);
		} else {
			negBtn.setVisibility(View.GONE);
		}
		posBtn.setOnClickListener(this);
		negBtn.setOnClickListener(this);
		CustomAlertDialog dialog = new CustomAlertDialog(getActivity());
		dialog.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		return dialog;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDialog().getWindow().setLayout(Utils.dp2px(getActivity(), 280),
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.positiveButton:
			activity.onFacePositiveButtonClicked(getTag(), DEFAULT_REQUEST_CODE);
			dismiss();
			break;
		case R.id.negativeButton:
			activity.onFaceNegativeButtonClicked(getTag(), DEFAULT_REQUEST_CODE);
			dismiss();
			break;
		default:
			break;
		}
	}
}
