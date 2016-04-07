package com.chinatelecom.facedetecion;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.chinatelecom.face.Face_Detection_Activity;
import com.chinatelecom.facedetecion.view.ProgressDialogFragment;
import com.chinatelecom.facedetecion.view.SimpleDialogFragment;
import com.chinatelecom.facedetecion.view.SimpleTimeOutDialogFragment;
import com.chinatelecom.util.Constants;

public class MainActivity extends Face_Detection_Activity {
	private String idCardStorageNum = "";// 身份证入库编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initData();
	}

	/**
	 * 
	 * @Method_name: initData
	 * @Description: TODO初始化数据
	 * @return_type: void
	 * @throws
	 * @author 对应开发人员
	 */
	private void initData() {
		if (getIntent() != null) {
			// 读取身份证入库编号
			idCardStorageNum = getIntent().getStringExtra(
					Constants.IDCardStorageNum);
		} else {
			finish();
		}
	}

	@Override
	protected void applyEncryptFactorRequest(String traceLogId) {
		// TODO Auto-generated method stub
		super.applyEncryptFactorRequest(traceLogId);
		// 调用api网关接口申请加密因子，客户端实现
		showFaceProgressDialogFragment(
				getString(R.string.face_detection_collecting_txt),
				FACE_DETECTION_COLLECTING_TAG);// "加载中"弹框
		// 调用api网关接口,客户端实现
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 接口返回失败，隐藏加载中弹框，并且弹出异常弹框，msg:接口返回的错误信息,区分token失效
				// dismissFaceDialogFragment(FACE_DETECTION_COLLECTING_TAG);//隐藏弹框
				// showFaceTwoBtnDialogFragment(msg,
				// FACE_DETECTION_COMMIT_EXCEPTION_TAG);
				// 接口返回成功，则回调父类的加密因子处理函数
				String aesKey = "123456789";// 加密因子
				String storageNum = "123455";// 入库编号，从ocr那里传过来
				applyEncryFactorSuccess(aesKey, storageNum);// 加密因子获取成功记得回调此方法
			}
		}, 5000);
	}

	@Override
	protected void validateFaceRequest(String traceLogId, String storageNum,
			String originalImgEncryptFirstPart, String originalImgLastPart,
			String faceImgEncryptFirstPart, String faceImgLastPart) {
		// TODO Auto-generated method stub
		super.validateFaceRequest(traceLogId, storageNum,
				originalImgEncryptFirstPart, originalImgLastPart,
				faceImgEncryptFirstPart, faceImgLastPart);
		// 调用api网关接口上传大头照跟脸图，客户端实现
		// 接口返回完成，隐藏弹框
		dismissFaceDialogFragment(FACE_DETECTION_COLLECTING_TAG);// 隐藏弹框
		// 接口返回失败，弹出异常弹框，msg:接口返回的错误信息,区分token失效
		// showFaceTwoBtnDialogFragment(msg,
		// FACE_DETECTION_COMMIT_EXCEPTION_TAG);
		// 接口返回成功
		showFaceToastWithIcon(getString(R.string.face_detection_success_txt));// 提示采集成功
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setResult(FACE_DETECTION_SUCCESS);
				finish();
			}
		}, 500);
	}

	@Override
	public void showFaceProgressDialogFragment(String msg, String tag) {
		// TODO Auto-generated method stub
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

	@Override
	public void showFaceTwoBtnDialogFragment(String msg, String tag) {
		// TODO Auto-generated method stub
		super.showFaceTwoBtnDialogFragment(msg, tag);
		if (FACE_DETECTION_EXCEED_NUM_TAG.equals(tag)) {
			SimpleDialogFragment.newInstance("", msg,
					getString(R.string.face_detection_other_method_txt),
					getString(R.string.face_detection_repair_txt)).show(
					getSupportFragmentManager(), tag);
		} else if (FACE_DETECTION_COMMIT_EXCEPTION_TAG.equals(tag)) {
			SimpleDialogFragment.newInstance("", msg, "确定", "取消").show(
					getSupportFragmentManager(), tag);
		}
	}

	@Override
	public void showFaceTwoBtnDialogFragment(View v, String tag) {
		// TODO Auto-generated method stub
		super.showFaceTwoBtnDialogFragment(v, tag);
		SimpleTimeOutDialogFragment.newInstance(v).show(
				getSupportFragmentManager(), tag);
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
		if (FACE_DETECTION_EXCEED_NUM_TAG.equals(tag)) {
			showAppToast("请您使用其他方式认证，谢谢！");
			// 跳转到其他方式认证页面
		}
	}

	@Override
	public void showAppToast(String msg) {
		// TODO Auto-generated method stub
		super.showAppToast(msg);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
