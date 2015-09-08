package com.cgzz.job.b.activity;

import java.util.HashMap;
import java.util.Set;

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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends BaseActivity implements OnClickListener {

	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.loginB_Http:// 登录
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserLoginB(data);

					application.setAccess(bundle);
					 JPushInterface.init(getApplicationContext());
					 JPushInterface.resumePush(getApplicationContext());
					 showWaitDialog();
					 mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS,
							"b"+ bundle.get("id")));
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserLogin(data);
					ToastUtil.makeShortText(LoginActivity.this,
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
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;
			default:
			}
		}
	};
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				dismissWaitDialog();
				// ToastUtil.makeShortText(LoginActivity.this, "设置成功");
				application.setLogon(true);
				startActivity(new Intent(LoginActivity.this,
						TabMainActivity.class));
				finish();

				// 请求后台
				// setRid(UrlConfig.setRidInterface, application.getUid(),
				// application.getToken(), application.getUid());
				break;

			case 6002:// 设置超时
				// setRid(UrlConfig.setRidInterface, application.getUid(),
				// application.getToken(),
				// "R" + application.getRegistrationID());
				dismissWaitDialog();
				//
//				application.setLogon(true);
//				startActivity(new Intent(LoginActivity.this,
//						TabMainActivity.class));
//				finish();
				try {
					ToastUtil.makeShortText(LoginActivity.this, "链接超时，请重新登录");
				} catch (Exception e) {
					// TODO: handle exception
				}
				// ToastUtil.makeShortText(LoginActivity.this, "设置超时");
				// application.setLogon(true);

				// if (logotype.equals("0")) {
				// application.setLogon(true);
				// Intent intent = new Intent(LoginActivity.this,
				// HomeActivity.class);
				// intent.putExtra("isRefresh", true);
				// startActivity(intent);
				// finish();
				// } else if (logotype.equals("1")) {
				// // 跳转到注册信息界面
				// startActivity(new Intent(LoginActivity.this,
				// RegisterNewsActivity.class));
				// }

				break;

			case 6011:// 10s内设置tag或alias大于3次
				showWaitDialog();
				mHandler.sendMessageDelayed(
						mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;

			default:
			}

		}

	};
	public GlobalVariables application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		application = (GlobalVariables) getApplicationContext();
		setTitle("快速登录", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
				TITLE_TYPE_TEXT, "注册");

		initView();
		initListenger();

	}

	EditText et_login_username, et_register_code;
	TextView tv_login_login, tv_login_forgot;
	LinearLayout llLeft, llright;

	private void initView() {

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		et_login_username = (EditText) findViewById(R.id.et_login_username);
		et_register_code = (EditText) findViewById(R.id.et_register_code);
		tv_login_login = (TextView) findViewById(R.id.tv_login_login);
		tv_login_forgot = (TextView) findViewById(R.id.tv_login_forgot);
	}

	private void initListenger() {
		tv_login_login.setOnClickListener(this);
		tv_login_forgot.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		String phone = "";
		switch (v.getId()) {
		case R.id.tv_login_login:// 登录
			phone = et_login_username.getText().toString();
			if (Utils.isEmpty(phone) || !Utils.isPhoneNumberValid(phone)) {
				ToastUtil.makeShortText(this, "请输入正确手机号");
				return;
			}

			String code = et_register_code.getText().toString();
			if (Utils.isEmpty(code)) {
				ToastUtil.makeShortText(this, "密码不能为空");
				return;
			}
			login(UrlConfig.loginB_Http, phone, code, true);
			break;
		case R.id.tv_login_forgot:// 忘记密码
			startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 注册
			startActivity(new Intent(LoginActivity.this,
					SignedActivityOne.class));

			break;

		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	/***
	 * 登录接口
	 * 
	 * @param url
	 *            请求地址
	 * @param phone
	 *            手机号
	 * @param password
	 *            密码
	 */
	private void login(String url, String phone, String password,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("password", password);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				LoginActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(LoginActivity.this),
				HttpStaticApi.loginB_Http, null, loadedtype);
	}

}
