package com.ocrtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.chinatelecom.bean.OcrInfoBean;
import com.chinatelecom.facedetecion.GuideActivity;
import com.chinatelecom.facedetecion.R;
import com.chinatelecom.facedetecion.view.ProgressDialogFragment;
import com.chinatelecom.ocr.Ocr_IDCard_Information_Activity;
import com.chinatelecom.util.Constants;

public class OcrInformationActivity extends Ocr_IDCard_Information_Activity {

	private Handler mHandler;
	private EditText ocrName, ocrGender, ocrIdCardNumber, ocrValidDate;
	private OcrInfoBean ocrInfos;// 身份证信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHandler = new Handler();
		initUI();
		initLisener();
		initData();
	}

	private void initUI() {
		ocrName = (EditText) findViewById(R.id.ocr_information_et_name);
		ocrGender = (EditText) findViewById(R.id.ocr_information_et_gender);
		ocrIdCardNumber = (EditText) findViewById(R.id.ocr_information_et_idCard_number);
		ocrValidDate = (EditText) findViewById(R.id.ocr_information_et_valid_date);
	}

	private void initData() {
		if (getIntent() != null) {
			// 读取身份证正面信息
			ocrInfos = (OcrInfoBean) getIntent().getSerializableExtra(
					Constants.IDCardInfoBean);
			if (ocrInfos != null) {
				ocrName.setText(ocrInfos.getIdCardName());
				ocrGender.setText(ocrInfos.getIdCardGender());
				ocrIdCardNumber.setText(ocrInfos.getIdCardNumber());
				ocrValidDate.setText(ocrInfos.getValidEndDate());
			}
		} else {
			setResult(Constants.OCR_RESULTCODE_STAR_COLLECT);
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void ocrInfoConfirmService(String traceLogId) {
		// TODO Auto-generated method stub
		super.ocrInfoConfirmService(traceLogId);
		// 显示加载对话框
		showFaceProgressDialogFragment("", Constants.OCR_PROGRESS_TAG);
		// 获取日志ID
		// String traceLogId = Util.getKeep();
		// 调用身份证信息确认接口
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 接口返回处理
				// 接口返回失败，隐藏加载中弹框，并且弹出异常弹框，msg:接口返回的错误信息,区分token失效
				// showFaceTwoBtnDialogFragment(msg,
				// Constants.OCR_ERROR_DIALOG);
				// 关闭加载对话框
				dismissFaceDialogFragment(Constants.OCR_PROGRESS_TAG);
				// 接口返回成功到活体检测引导界面
				// 提交成功toast提示
				showFaceToastWithIcon(getString(R.string.ocr_idcard_commit_success_txt));
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 身份证采集成功跳转到活体检测引导界面,
						Intent faceDetectionIntent = new Intent(
								OcrInformationActivity.this,
								GuideActivity.class);
						faceDetectionIntent.putExtra(
								Constants.IDCardStorageNum,
								ocrInfos.getIdCardStorageNum());// 入库编号
						startActivity(faceDetectionIntent);

						setResult(Constants.OCR_RESULTCODE_SUCCESS);
						finish();
					}
				}, 500);
			}
		}, 3000);
	}

	private void initLisener() {
		findViewById(R.id.ocr_information_iv_txt_again_collect)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setResult(Constants.OCR_RESULTCODE_AGAIN_COLLECT);
						finish();
					}
				});
	}

	/**
	 * 返回开始采集页
	 */
	@Override
	public void goStarGather() {
		setResult(Constants.OCR_RESULTCODE_STAR_COLLECT);
		finish();
	}

	@Override
	public void dismissFaceDialogFragment(String tag) {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		DialogFragment dialogFragment = (DialogFragment) fm
				.findFragmentByTag(tag);
		if (dialogFragment != null) {
			fm.beginTransaction().remove(dialogFragment)
					.commitAllowingStateLoss();
		}

	}

	@Override
	public void showAppToast(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showFaceProgressDialogFragment(String msg, String tag) {
		// TODO Auto-generated method stub
		ProgressDialogFragment.newInstance(msg).show(
				getSupportFragmentManager(), tag);
	}

	@Override
	public void showFaceTwoBtnDialogFragment(View arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showFaceTwoBtnDialogFragment(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
