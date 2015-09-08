package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.HorizontalListViewAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.view.HorizontalListView;

/***
 * 
 * @author wjm 查看到岗
 * 
 */

public class LoogPostActivity extends BaseActivity implements OnClickListener {
	private LinearLayout llLeft;
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.arriveB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserArriveB(data);
					tv_lp_title1.setText(bundle.getString("arrivenum")
							.toString());
					tv_lp_title3.setText(bundle.getString("unarrivenum")
							.toString());

					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list1")).size() > 0) {

						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list1"));
					}

					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list2")).size() > 0) {

						TrainingData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list2"));
					}
					hListViewAdapter1.refreshMYData(TrainingData);
					hListViewAdapter2.refreshMYData(CurrentData);
					break;
				case HttpStaticApi.FAILURE_HTTP:
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
	HorizontalListView hl_lp_1, hl_lp_2;
	HorizontalListViewAdapter hListViewAdapter1, hListViewAdapter2;
	String orderid = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.activity_loogpost);
		setTitle("查看到岗", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_IMG, R.drawable.icon_phone);

		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");
		initView();
	}

	TextView tv_lp_title1, tv_lp_title3;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);// 左侧
		hl_lp_1 = (HorizontalListView) findViewById(R.id.hl_lp_1);
		hl_lp_2 = (HorizontalListView) findViewById(R.id.hl_lp_2);
		tv_lp_title1 = (TextView) findViewById(R.id.tv_lp_title1);
		tv_lp_title3 = (TextView) findViewById(R.id.tv_lp_title3);
		hListViewAdapter1 = new HorizontalListViewAdapter(LoogPostActivity.this);
		hl_lp_1.setAdapter(hListViewAdapter1);

		hListViewAdapter2 = new HorizontalListViewAdapter(LoogPostActivity.this);
		hl_lp_2.setAdapter(hListViewAdapter2);

		arrive(UrlConfig.arriveB, application.getUserId(),
				application.getToken(), orderid, true);
		llLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:
			onBackPressed();
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

	/***
	 * 查看到岗接口
	 */
	private void arrive(String url, String userid, String token,
			String orderid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("userid", userid);
		map.put("token", token);
		map.put("orderid", orderid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				LoogPostActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(LoogPostActivity.this),
				HttpStaticApi.arriveB_Http, null, loadedtype);
	}

}
