package com.cgzz.job.b.activity;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;

/***
 * 
 * @author wjm 意见反馈
 * 
 */

public class OpinionActivity extends BaseActivity implements OnClickListener {
	private TextView tv_num, tv_opinion_submitted;
	private LinearLayout llLeft, llright;
	private EditText tv_opinion_content;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			switch (method) {
			case HttpStaticApi.opinion_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					ToastUtil.makeShortText(OpinionActivity.this, "意见提交成功");
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					ToastUtil.makeShortText(OpinionActivity.this, "意见提交失败");
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
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.activity_opinion);

		setTitle("意见反馈", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
				TITLE_TYPE_IMG, R.drawable.icon_phone);
		initView();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void initView() {
		// 多余10个字不让提交
		tv_opinion_content = (EditText) findViewById(R.id.tv_opinion_content);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);// 左侧
		llright = (LinearLayout) findViewById(R.id.ll_title_right);// 右侧
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_opinion_submitted = (TextView) findViewById(R.id.tv_opinion_submitted);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_opinion_submitted.setOnClickListener(this);
		tv_opinion_content.requestFocus();
		tv_opinion_content.addTextChangedListener(mEditText);

	}

	TextWatcher mEditText = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			tv_num.setText(tv_opinion_content.getText().length() + "/500");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:
			onBackPressed();
			break;
		case R.id.ll_title_right:// 拨打电话
			popTheirProfile();
			break;
		case R.id.tv_opinion_submitted:
			String c = tv_opinion_content.getText().toString();
			if (Utils.isEmpty(c)) {
				ToastUtil.makeShortText(OpinionActivity.this, "请填写内容");
				return;
			}

			opinion(UrlConfig.opinion_Http, application.getUserId(),
					application.getToken(), "1", c, true);
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

		title.setText("是否拨打客服电话?");
		up.setText("是");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("否");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				Utils.call(getResources().getString(R.string.call_customer),
						OpinionActivity.this);

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

	/***
	 * 意见反馈接口
	 */
	private void opinion(String url, String userid, String token,
			String apptype, String opinion, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", token);
		map.put("apptype", apptype);
		map.put("opinion", opinion);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				OpinionActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(OpinionActivity.this),
				HttpStaticApi.opinion_Http, null, loadedtype);
	}

}
