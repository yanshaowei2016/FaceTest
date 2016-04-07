package com.ocrtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.chinatelecom.bean.OcrInfoBean;
import com.chinatelecom.cryptools.CrypIdentify;
import com.chinatelecom.facedetecion.R;
import com.chinatelecom.facedetecion.view.ProgressDialogFragment;
import com.chinatelecom.ocr.Ocr_IDCard_Front_Activity;
import com.chinatelecom.util.Constants;
import com.chinatelecom.util.Util;
import com.ocrtest.util.ToastUtil;
import com.ocrtest.view.OcrDialog;
import com.ocrtest.view.OcrDialog.ocrDialogListener;

public class OcrFrontActivity extends Ocr_IDCard_Front_Activity {

	private Handler mHandler;
	public String IDCardFrontFirstPart, IDCardFrontLastPart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHandler = new Handler();
		initLisener();
	}

	private void initLisener() {
		findViewById(R.id.ocr_btn_next).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// api网关：调用申请挑战值接口获取挑战值以及正面识别接口识别正面信息,客户端实现
						// 获取日志ID
						// String traceLogId = Util.getKeep();
						// 显示加载对话框
						showFaceProgressDialogFragment("",
								Constants.OCR_PROGRESS_TAG);

						// 调用申请挑战值接口
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// 接口返回失败，隐藏加载中弹框，并且弹出异常弹框，msg:接口返回的错误信息,区分token失效
								// showFaceTwoBtnDialogFragment(msg,
								// Constants.OCR_ERROR_DIALOG);
								
								//接口返回成功， 将申请挑战值接口得到的32位挑战值转化成16位
								String key = CrypIdentify
										.getFcRandomResDto("12345678123456781234567812345678");
								// 身份证人像面加密前512byte数据
								IDCardFrontFirstPart = CrypIdentify
										.getEncryptFirst(key, IDCardFront);
								// 身份证人像面未加密数据
								IDCardFrontLastPart = CrypIdentify
										.getEncryptLast(IDCardFront);

								// 调用身份证正面识别接口
								mHandler.postDelayed(new Runnable() {

									@Override
									public void run() {
										// 接口返回失败，隐藏加载中弹框，并且弹出异常弹框，msg:接口返回的错误信息,区分token失效
										// showFaceTwoBtnDialogFragment(msg,
										// Constants.OCR_ERROR_DIALOG);
										
										// 接口返回的正面信息
										OcrInfoBean ocrInfos = new OcrInfoBean();
										ocrInfos.setIdCardStorageNum("接口返回的入库编号");
										ocrInfos.setIdCardName("张三");
										ocrInfos.setIdCardGender("男");
										ocrInfos.setIdCardNumber("360233198911043541");
										ocrInfos.setIdCardBirthday("19891104");
										ocrInfos.setIdCardAddress("广东省广州市****");
										ocrInfos.setIdCardRace("汉");

										Intent intent = new Intent(
												OcrFrontActivity.this,
												OcrBackActivity.class);
										intent.putExtra(Constants.IDCardFront,
												IDCardFront);
										intent.putExtra(
												Constants.IDCardInfoBean,
												ocrInfos);
										startActivityForResult(intent, 1);

										// 关闭加载对话框
										dismissFaceDialogFragment(Constants.OCR_PROGRESS_TAG);
									}
								}, 3000);
							}
						});
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == Constants.OCR_RESULTCODE_SUCCESS) {
				setResult(Constants.OCR_RESULTCODE_SUCCESS);
				finish();
			} else if (resultCode == Constants.OCR_RESULTCODE_STAR_COLLECT) {
				setResult(Constants.OCR_RESULTCODE_STAR_COLLECT);
				finish();
			}
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void showAppToast(String msg) {
		// TODO Auto-generated method stub
		super.showAppToast(msg);
		ToastUtil.showToast(this, msg);// 显示身份识别过程中的弹出信息
	}

	/**
	 * 如果60秒内没有识别出身份证，弹出的错误提示对话框
	 * 
	 * @param msg
	 *            标题
	 * @param tag
	 *            标签
	 */
	@Override
	public void showFaceTwoBtnDialogFragment(String msg, final String tag) {
		super.showFaceTwoBtnDialogFragment(msg, tag);
		if (Constants.OCR_ERROR_DIALOG.equals(tag)) {
			FragmentManager ft = getSupportFragmentManager();
			OcrDialog ocrDialog = OcrDialog
					.newInstance(new ocrDialogListener() {

						@Override
						public void confirm() {
							onFacePositiveButtonClicked(tag, 1);// 重新识别身份证
						}

						@Override
						public void cancel() {
							onFaceNegativeButtonClicked(tag, 1);// 返回开始采集页
						}
					});
			ocrDialog.show(ft, tag);
		}
	}

	@Override
	public void onFaceNegativeButtonClicked(String tag, int requestCode) {
		// TODO Auto-generated method stub
		super.onFaceNegativeButtonClicked(tag, requestCode);
	}

	@Override
	public void onFacePositiveButtonClicked(String tag, int requestCode) {
		// TODO Auto-generated method stub
		super.onFacePositiveButtonClicked(tag, requestCode);
	}

	@Override
	public void dismissFaceDialogFragment(String tag) {
		// TODO Auto-generated method stub
		super.dismissFaceDialogFragment(tag);
		FragmentManager fm = getSupportFragmentManager();
		DialogFragment dialogFragment = (DialogFragment) fm
				.findFragmentByTag(tag);
		if (dialogFragment != null) {
			fm.beginTransaction().remove(dialogFragment)
					.commitAllowingStateLoss();
		}
	}

	@Override
	public void showFaceProgressDialogFragment(String msg, String tag) {
		// TODO Auto-generated method stub
		super.showFaceProgressDialogFragment(msg, tag);
		ProgressDialogFragment.newInstance(msg).show(
				getSupportFragmentManager(), tag);
	}

}
