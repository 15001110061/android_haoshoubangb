package com.cgzz.job.b.activity;

import java.util.HashMap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

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

/***
 * @author wjm 更多设置
 */
public class SetingActivity extends BaseActivity implements OnClickListener,
		OnCheckedChangeListener {
	private LinearLayout llLeft, llright;
	private TextView iv_seting_exit, tv_seting_huanying, tv_seting_haoping,
			tv_seting_changjian, tv_seting_xieyi;
	private CheckBox cb_seting_shoushi, cb_seting_jiedan;
	private String stopAccept;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.stopAccept_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SetingActivity.this,
							bundle.get("msg").toString());
					if ("0".equals(stopAccept)) {
						cb_seting_jiedan.setChecked(false);
					} else {
						cb_seting_jiedan.setChecked(true);
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SetingActivity.this,
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seting);
		setTitle("设置", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		findView();
		init();

	}

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		iv_seting_exit = (TextView) findViewById(R.id.iv_seting_exit);
		tv_seting_huanying = (TextView) findViewById(R.id.tv_seting_huanying);
		tv_seting_changjian = (TextView) findViewById(R.id.tv_seting_changjian);
		tv_seting_haoping = (TextView) findViewById(R.id.tv_seting_haoping);
		cb_seting_jiedan = (CheckBox) findViewById(R.id.cb_seting_jiedan);
		tv_seting_xieyi = (TextView) findViewById(R.id.tv_seting_xieyi);
		cb_seting_shoushi = (CheckBox) findViewById(R.id.cb_seting_shoushi);
		if (application.isGestures()) {
			cb_seting_shoushi.setChecked(true);
		} else {
			cb_seting_shoushi.setChecked(false);
		}

		if (application.isStopAccept()) {
			cb_seting_jiedan.setChecked(true);
		} else {
			cb_seting_jiedan.setChecked(false);
		}
	}

	private void init() {
		llLeft.setOnClickListener(this);
		tv_seting_xieyi.setOnClickListener(this);
		iv_seting_exit.setOnClickListener(this);
		cb_seting_shoushi.setOnCheckedChangeListener(this);
		cb_seting_jiedan.setOnCheckedChangeListener(this);
		tv_seting_haoping.setOnClickListener(this);
		tv_seting_huanying.setOnClickListener(this);
		tv_seting_changjian.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String url = null;
		switch (arg0.getId()) {
		case R.id.iv_seting_newsremind:
			break;
		// case R.id.iv_seting_opinion:
		//
		// intent = new Intent(SetingActivity.this, OpinionActivity.class);
		// startActivity(intent);
		// break;

		case R.id.iv_seting_exit:
			popTheirProfile();
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;

		case R.id.tv_seting_huanying:// 欢迎
			intent = new Intent(SetingActivity.this, IntroductionActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_seting_haoping:// 好评
			// 进入安卓市场评分代码
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			intent = new Intent(Intent.ACTION_VIEW, uri);
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				ToastUtil.makeShortText(this, "不可以登录安卓市场");
			}
			break;
		case R.id.tv_seting_changjian:// 常见问题

			url = "http://www.haoshoubang.com/bangke/html/faq_b.html";
			intent = new Intent(SetingActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "常见问题");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);

			break;

		case R.id.tv_seting_xieyi:// 用户协议

			url = "http://www.haoshoubang.com/bangke/html/deal.html";
			intent = new Intent(SetingActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "用户协议");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);

			break;

		default:
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if (arg0 == cb_seting_shoushi) {
			if (arg1) {
				intent = new Intent(SetingActivity.this,
						GestureEditActivity.class);
				startActivity(intent);
				finish();
			} else {
				application.setGestures(false);
			}

		} else if (arg0 == cb_seting_jiedan) {
			if (arg1) {
				stopAccept(UrlConfig.stopAccept_Http, application.getToken(),
						application.getUserId(), "1", true);
			} else {
				stopAccept(UrlConfig.stopAccept_Http, application.getToken(),
						application.getUserId(), "0", true);
			}

		}
		// cb_seting_newsremind.setChecked(false);
	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile() {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two),
				Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("是否退出?");
		up.setText("是");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("否");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				application.setLogon(false);
				JPushInterface.stopPush(SetingActivity.this);
				Utils.closeActivity();

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}

	private void stopAccept(String url, String token, String userid,
			String type, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("type", type);// 0停止
		stopAccept = type;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				SetingActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SetingActivity.this),
				HttpStaticApi.stopAccept_Http, null, loadedtype);
	}
}
