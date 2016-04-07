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

public class SimpleTimeOutDialogFragment extends DialogFragment implements
		OnClickListener {
	private final static String ARG_REQUEST_CODE = "request_code";
	private final static int DEFAULT_REQUEST_CODE = -42;

	private Face_BaseActivity activity;
	private View timeoutView;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (Face_BaseActivity) activity;
	}

	public static SimpleTimeOutDialogFragment newInstance(View v) {
		SimpleTimeOutDialogFragment fragment = new SimpleTimeOutDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_REQUEST_CODE, DEFAULT_REQUEST_CODE);
		fragment.setArguments(args);
		fragment.timeoutView = v;
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

		Button posBtn = (Button) v.findViewById(R.id.positiveButton);
		Button negBtn = (Button) v.findViewById(R.id.negativeButton);
		((LinearLayout) v.findViewById(R.id.title_template))
				.setVisibility(View.GONE);
		LinearLayout panel = (LinearLayout) v.findViewById(R.id.contentPanel);
		panel.removeAllViews();
		panel.addView(timeoutView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
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
