package com.ocrtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.chinatelecom.facedetecion.R;
import com.chinatelecom.ocr.Ocr_IDCard_Star_Collect_Activity;
import com.chinatelecom.util.Constants;
import com.chinatelecom.util.ICamera;

public class OcrStarCollectActivity extends Ocr_IDCard_Star_Collect_Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initLisener();
		ICamera mICamera = new ICamera();
		boolean isHasBackAndFront = mICamera.hasBackAndFrontFacingCamera();
//		if (!isHasBackAndFront) {
//			Toast.makeText(this, "您的设备不存在前置跟后置摄像头", Toast.LENGTH_SHORT).show();
//			finish();
//		}
	}

	private void initLisener() {
		findViewById(R.id.ocr_btn_star_collect).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivityForResult(new Intent(
								OcrStarCollectActivity.this,
								OcrFrontActivity.class), 1);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Constants.OCR_RESULTCODE_SUCCESS) {
				finish();
			}
			break;
		}
	}

}
