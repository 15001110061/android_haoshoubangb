package com.cgzz.job.b.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.BaseActivityCloseListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;

/***
 * @author wjm 向好手帮转账
 */
public class PayTransferActivity extends BaseActivity implements OnClickListener {

	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.hsbB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(PayTransferActivity.this, "请您在规定时间内向好手帮转帐");
					refreshOrders();
					refreshSampled();
					application.popClosePath(true, UrlConfig.PATH_KEY_PAY);

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(PayTransferActivity.this, bundle.get("msg").toString());
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
	String totalPrice = "", id = "",enddate="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paytransfer);
		Intent intent = getIntent();
		totalPrice = intent.getStringExtra("totalPrice");
		id = intent.getStringExtra("id");
		enddate = intent.getStringExtra("enddate");
		setTitle("向好手帮转账", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		application.putClosePath(UrlConfig.PATH_KEY_PAY, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});
		findView();
		init();

	}

	LinearLayout llLeft, llright;
	TextView btn_shanchu, tv_paycash_title2,tv_paycash_title1;

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		btn_shanchu = (TextView) findViewById(R.id.btn_shanchu);
		tv_paycash_title2 = (TextView) findViewById(R.id.tv_paycash_title2);
		tv_paycash_title1 = (TextView) findViewById(R.id.tv_paycash_title1);
		tv_paycash_title2.setText("￥" + totalPrice);
		tv_paycash_title1.setText("" + enddate);
	}

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		btn_shanchu.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {

		case R.id.btn_shanchu://
			hsb(UrlConfig.hsbB_Http, application.getToken(), application.getUserId(), id, "1", true);
			break;
		case R.id.ll_title_left://
			onBackPressed();
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {

		finish();
	}

	private void hsb(String url, String token, String userid, String orderid, String type, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);

		map.put("type", type);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, PayTransferActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(PayTransferActivity.this), HttpStaticApi.hsbB_Http, null, loadedtype);
	}

	public void refreshSampled() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshSampled");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}
}
