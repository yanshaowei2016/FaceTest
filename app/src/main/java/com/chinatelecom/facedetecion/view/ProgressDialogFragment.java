package com.chinatelecom.facedetecion.view;


import com.chinatelecom.facedetecion.R;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class ProgressDialogFragment extends DialogFragment {

	private static final String ARGS_REQUEST_MSG = "request_msg";

	public static ProgressDialogFragment newInstance(String msg) {
		ProgressDialogFragment fragment = new ProgressDialogFragment();
		Bundle args = new Bundle();
		args.putString(ARGS_REQUEST_MSG, msg);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(android.R.color.transparent));
		getDialog().setCanceledOnTouchOutside(false);
		Bundle args = getArguments();
		String msg = args.getString(ARGS_REQUEST_MSG);
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.custom_progress_dialog, container);
		TextView massageTv = (TextView) view.findViewById(R.id.textMessage);
		if (msg == null || "".equals(msg)) {
			massageTv.setText(R.string.progress_loading);
		} else {
			massageTv.setText(msg);
		}
		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}
}
