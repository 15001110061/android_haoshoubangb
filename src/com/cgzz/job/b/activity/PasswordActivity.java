package com.cgzz.job.b.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.wheel.GoOffWheelView.WheelDateChoiseListener;

/***
 * 
 * @author wjm 忘记密码
 */
public class PasswordActivity extends BaseActivity implements OnClickListener,
		WheelDateChoiseListener {
	TimeCount countdown;
	EditText et_signed_phones, et_signed_captchas, et_signed_passwords;
	TextView et_signed_send_captchas_code, tv_signed_next;
	LinearLayout llLeft;
	public GlobalVariables application;
	String Code = "";
	String mobiles = "";
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.sendCode_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserSendCode(data);
					Code = bundle.get("result").toString();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserSendCode(data);
					ToastUtil.makeShortText(PasswordActivity.this,
							bundle.get("msg").toString());
					break;
				default:
					break;
				}

				break;

			case HttpStaticApi.forgetPassword_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserForgetPassword(data);
					ToastUtil.makeShortText(PasswordActivity.this,
							bundle.get("msg").toString());
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserForgetPassword(data);
					ToastUtil.makeShortText(PasswordActivity.this,
							bundle.get("msg").toString());
					break;
				default:
					break;
				}

				break;

			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		application = (GlobalVariables) getApplicationContext();
		setTitle("忘记密码", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");

		initView();
		initListenger();

	}

	private void initView() {

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		et_signed_phones = (EditText) findViewById(R.id.et_signed_phones);
		et_signed_captchas = (EditText) findViewById(R.id.et_signed_captchas);
		et_signed_passwords = (EditText) findViewById(R.id.et_signed_passwords);
		et_signed_send_captchas_code = (TextView) findViewById(R.id.et_signed_send_captchas_code);
		tv_signed_next = (TextView) findViewById(R.id.tv_signed_next);
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		tv_signed_next.setOnClickListener(this);
		et_signed_send_captchas_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.tv_signed_next:// 提交
			mobiles = et_signed_phones.getText().toString();
			String passwords = et_signed_passwords.getText().toString();
			if (Utils.isEmpty(passwords)) {
				ToastUtil.makeShortText(this, "请输入新密码");
				return;
			}
			String captchas = et_signed_captchas.getText().toString();
			if (Utils.isEmpty(captchas)) {
				ToastUtil.makeShortText(this, "验证码不能为空");
				return;
			}

			if (!Code.equals(captchas)) {
				ToastUtil.makeShortText(this, "验证码不正确");
				return;
			}
			getForgetPassword(UrlConfig.forgetPassword_Http, mobiles,
					passwords, "1", true);
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.et_signed_send_captchas_code:// 验证码
			// 构造CountDownTimer对象
			mobiles = et_signed_phones.getText().toString();
			if (Utils.isEmpty(mobiles) || !Utils.isPhoneNumberValid(mobiles)) {
				ToastUtil.makeShortText(this, "请输入正确手机号");
				return;
			}
			// 获取验证码
			getCode(UrlConfig.sendCode_Http, mobiles, true);
			countdown = new TimeCount(60000, 1000);
			countdown.start();
			et_signed_send_captchas_code.requestFocus();

			break;
		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			et_signed_send_captchas_code.setText("重新发送");
			et_signed_send_captchas_code.setClickable(true);
			et_signed_send_captchas_code.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			et_signed_send_captchas_code.setClickable(false);
			et_signed_send_captchas_code.setEnabled(false);
			et_signed_send_captchas_code.setText(millisUntilFinished / 1000
					+ "秒后重新发送");
		}
	}

	private void getCode(String url, String phone, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", phone);
//		map.put("apptype", "1");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				PasswordActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(PasswordActivity.this),
				HttpStaticApi.sendCode_Http, null, loadedtype);
	}

	private void getForgetPassword(String url, String mobile, String password,
			String apptype, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", mobile);
		map.put("password", password);
		map.put("apptype", apptype);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				PasswordActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(PasswordActivity.this),
				HttpStaticApi.forgetPassword_Http, null, loadedtype);
	}

	@Override
	public void chooseTime(String date) {
		ToastUtil.makeShortText(PasswordActivity.this, date);

	}

}
