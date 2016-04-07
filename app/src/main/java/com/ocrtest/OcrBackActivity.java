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
import com.chinatelecom.ocr.Ocr_IDCard_Back_Activity;

import com.chinatelecom.util.Constants;
import com.chinatelecom.util.MResoure;
import com.ocrtest.util.ToastUtil;
import com.ocrtest.view.OcrDialog;
import com.ocrtest.view.OcrDialog.ocrDialogListener;

public class OcrBackActivity extends Ocr_IDCard_Back_Activity {

	private Handler mHandler;
	public String IDCardBackFirstPart, IDCardBackLastPart;
	private OcrInfoBean ocrInfos;// 身份证信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mHandler = new Handler();
		initData();
		initLisener();
	}

	private void initData() {
		if (getIntent() != null) {
			// 读取身份证正面信息
			ocrInfos = (OcrInfoBean) getIntent().getSerializableExtra(
					Constants.IDCardInfoBean);
		} else {
			setResult(Constants.OCR_RESULTCODE_STAR_COLLECT);
			finish();
		}
	}

	private void initLisener() {
		findViewById(R.id.ocr_btn_next).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 显示加载对话框
						showFaceProgressDialogFragment("",
								Constants.OCR_PROGRESS_TAG);
						// 获取日志ID
						// String traceLogId = Util.getKeep();

						// 调用申请挑战值接口
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// 接口返回失败，隐藏加载中弹框，并且弹出异常弹框，msg:接口返回的错误信息,区分token失效
								// showFaceTwoBtnDialogFragment(msg,
								// Constants.OCR_ERROR_DIALOG);

								// 接口返回成功，
								// 图片加密的key的方式为，获取32位前8位，然后从第9位开始取3的mod，一共凑成16位
								String key = CrypIdentify
										.getFcRandomResDto("12345678123456781234567812345678");
								// 身份证反面加密前512byte数据
								IDCardBackFirstPart = CrypIdentify
										.getEncryptFirst(key, IDCardBack);
								// 身份证反面未加密数据
								IDCardBackLastPart = CrypIdentify
										.getEncryptLast(IDCardBack);

								// 调用身份证反面识别接口
								mHandler.postDelayed(new Runnable() {

									@Override
									public void run() {
										// 接口返回失败，隐藏加载中弹框，并且弹出异常弹框，msg:接口返回的错误信息,区分token失效
										// showFaceTwoBtnDialogFragment(msg,
										// Constants.OCR_ERROR_DIALOG);

										// 接口返回的反面信息
										ocrInfos.setIdCardStorageNum("接口返回的入库编号");
										ocrInfos.setIssuedBy("广州市公安局");
										ocrInfos.setValidStartDate("20090503");
										ocrInfos.setValidEndDate("20190503");
										Intent intent = new Intent(
												OcrBackActivity.this,
												OcrInformationActivity.class);
										intent.putExtra(Constants.IDCardFront,
												IDCardFront);
										intent.putExtra(Constants.IDCardBack,
												IDCardBack);
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mHandler.removeCallbacksAndMessages(null);
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
			} else if (resultCode == Constants.OCR_RESULTCODE_AGAIN_COLLECT) {
				finish();
			}
			break;
		}
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
	public void showFaceProgressDialogFragment(String msg, String tag) {
		super.showFaceProgressDialogFragment(msg, tag);
		ProgressDialogFragment.newInstance(msg).show(
				getSupportFragmentManager(), tag);
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

}
