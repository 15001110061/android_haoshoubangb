package com.cgzz.job.b.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.gesture.GestureContentView;
import com.cgzz.job.b.gesture.GestureDrawline.GestureCallBack;
import com.cgzz.job.b.utils.ToastUtil;

/**
 * 
 * 手势绘制/校验界面
 * 
 */
public class GestureVerifyActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	/** 手机号码 */
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private ImageView mImgUserLogo;
	private TextView mTextPhoneNumber;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	// private TextView mTextOther;
	private String mParamPhoneNumber;
	private long mExitTime = 0;
	private int mParamIntentCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_verify);

		setTitle("手势登录", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "注册");
		ObtainExtraData();
		setUpViews();
		setUpListeners();
	}

	LinearLayout llLeft;

	private void ObtainExtraData() {
		mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
	}

	private void setUpViews() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
		mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextPhoneNumber.setText(getProtectedMobile(application.getMobile()));

		// 初始化一个显示各个点的viewGroup
		mGestureContentView = new GestureContentView(this, true,
				application.getGesturesPasswords(), new GestureCallBack() {

					@Override
					public void onGestureCodeInput(String inputCode) {

					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						ToastUtil.makeShortText(GestureVerifyActivity.this, "密码正确");

						Intent intent = new Intent(GestureVerifyActivity.this,
								TabMainActivity.class);
						intent.putExtra("cityname", application.getCityName());
						intent.putExtra("cityid", application.getCityCode());
						startActivity(intent);
						GestureVerifyActivity.this.finish();
					}

					@Override
					public void checkedFail() {
						mGestureContentView.clearDrawlineState(1300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText(Html
								.fromHtml("<font color='#c70c1e'>密码错误</font>"));
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils
								.loadAnimation(GestureVerifyActivity.this,
										R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}

	private void setUpListeners() {
		mTextForget.setOnClickListener(this);
		llLeft.setOnClickListener(this);
	}

	private String getProtectedMobile(String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(phoneNumber.subSequence(0, 3));
		builder.append("****");
		builder.append(phoneNumber.subSequence(7, 11));
		return builder.toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.text_forget_gesture:// 忘记密码

			application.setGestures(false);
			application.setLogon(false);

			startActivity(new Intent(GestureVerifyActivity.this,
					LoginActivity.class));
			onBackPressed() ;
			break;
		case R.id.ll_title_left:// 返回
			onBackPressed() ;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
