package com.chinatelecom.facedetecion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.face.Face_Detection_Activity;
import com.chinatelecom.face.Face_Detection_Guide_Activity;
import com.chinatelecom.util.Constants;

public class GuideActivity extends Face_Detection_Guide_Activity {
	private String idCardStorageNum = "";// 身份证入库编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initLisener();
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

	private void initLisener() {
		((Button) findViewById(R.id.face_detection_guide_start_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent detectionIntent = new Intent(GuideActivity.this,
								MainActivity.class);
						detectionIntent.putExtra(Constants.IDCardStorageNum,
								idCardStorageNum);
						startActivityForResult(detectionIntent, 12);

					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// 处理结果
		if (requestCode == 12) {
			switch (resultCode) {
			case Face_Detection_Activity.FACE_DETECTION_SUCCESS:// 活体检测成功
				// 通知认证选择页面修改界面信息(可以使用EventBus)
				finish();
				break;
			case Face_Detection_Activity.FACE_DETECTION_OTHER_METHOD_RESULTCODE:
				// 活体检测超过次数选择“其他方式“,跳转到其他方式
				finish();// 关闭引导页面
				break;

			default:
				break;
			}
		}
	}
}
