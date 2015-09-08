package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.Myadapter4;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.wheel.GoOffWheelView;
import com.cgzz.job.b.wheelview.NumericWheelAdapter;
import com.cgzz.job.b.wheelview.OnWheelScrollListener;
import com.cgzz.job.b.wheelview.WheelView;

/***
 * @author wjm 追加价格
 */
public class AdditionalPriceActivity extends BaseActivity implements OnClickListener {
	private int type = 0;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.changePriceB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(AdditionalPriceActivity.this, bundle.get("msg").toString());
					// tv_help_zhaobangke2.setClickable(true);
					refreshOrders();
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(AdditionalPriceActivity.this, bundle.get("msg").toString());
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
	String orderid = "", bounus = "", standard_price = "", suite_price = "", standard_count = "", suite_count = "",
			workercount = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_additionalprice);
		setTitle("追加价格", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "计价规则");
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");

		bounus = intent.getStringExtra("bounus");
		standard_price = intent.getStringExtra("standard_price");
		suite_price = intent.getStringExtra("suite_price");
		standard_count = intent.getStringExtra("standard_count");
		suite_count = intent.getStringExtra("suite_count");
		workercount = intent.getStringExtra("workercount");
		findView();
		init();
		initmPopupWindowView();
		AssignmentView();
	}

	LinearLayout llLeft, llright;
	RelativeLayout rl_addprice_1, rl_addprice_2, rl_addprice_3;
	TextView tv_addprice_danjia1, tv_addprice_danjia2, tv_addprice_danjia3, tv_help_zhaobangke2, tv_help_biaojianjiage8;

	private void findView() {

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		rl_addprice_1 = (RelativeLayout) findViewById(R.id.rl_addprice_1);
		rl_addprice_2 = (RelativeLayout) findViewById(R.id.rl_addprice_2);
		rl_addprice_3 = (RelativeLayout) findViewById(R.id.rl_addprice_3);

		tv_addprice_danjia1 = (TextView) findViewById(R.id.tv_addprice_danjia1);
		tv_addprice_danjia2 = (TextView) findViewById(R.id.tv_addprice_danjia2);
		tv_addprice_danjia3 = (TextView) findViewById(R.id.tv_addprice_danjia3);
		tv_help_zhaobangke2 = (TextView) findViewById(R.id.tv_help_zhaobangke2);
		tv_help_biaojianjiage8 = (TextView) findViewById(R.id.tv_help_biaojianjiage8);

	}

	private void init() {
		llLeft.setOnClickListener(this);
		rl_addprice_1.setOnClickListener(this);
		rl_addprice_2.setOnClickListener(this);
		rl_addprice_3.setOnClickListener(this);
		tv_help_zhaobangke2.setOnClickListener(this);
	}

	private void AssignmentView() {
		tv_addprice_danjia1.setText(bounus);
		tv_addprice_danjia2.setText(standard_price);
		tv_addprice_danjia3.setText(suite_price);
		if ("0".equals(standard_count)) {
			rl_addprice_2.setVisibility(View.GONE);
		}
		if ("0".equals(suite_count)) {
			rl_addprice_3.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;

		switch (arg0.getId()) {

		case R.id.ll_title_left:// 返回
			onBackPressed();
			break;

		case R.id.rl_addprice_1://
			if (popupwindow != null) {
				type = 1;
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getdashang();
				adapter.refreshData(listlv);
			}
			break;
		case R.id.rl_addprice_2://
			if (popupwindow != null) {
				type = 2;
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getbiaojian();

				adapter.refreshData(listlv);
			}
			break;
		case R.id.rl_addprice_3://
			if (popupwindow != null) {
				type = 3;
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = gettaofang();

				adapter.refreshData(listlv);
			}
			break;
		case R.id.tv_help_zhaobangke2:

			createOrderbB(UrlConfig.createOrderbB_Http, application.getToken(), application.getUserId(),
					tv_addprice_danjia2.getText().toString(), tv_addprice_danjia3.getText().toString(), orderid,
					tv_addprice_danjia1.getText().toString(), true);
			break;

		default:
			break;
		}

	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	/**
	 *
	 */

	PopupWindow popupwindow;
	CustomListView lvCars;

	public void initmPopupWindowView() {
		// 获取自定义布局文件的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		ImageButton dis = (ImageButton) customView.findViewById(R.id.ib_dis);
		// 创建PopupWindow宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// 点击屏幕其他部分及Back键时PopupWindow消失
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupwindow.dismiss();
			}
		});
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					// popupwindow = null;
				}
				return false;
			}
		});

		// 车牌
		lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
		lvCars.setCacheColorHint(Color.TRANSPARENT);
		lvCars.setDivider(getResources().getDrawable(R.color.common_white));
		lvCars.setDividerHeight(Utils.dip2px(this, 0));
		lvCars.setFooterDividersEnabled(false);
		lvCars.setCanRefresh(false);// 关闭下拉刷新
		lvCars.setCanLoadMore(false);// 打开加载更多
		lvCars.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (lvCars == arg0) {
					if (type == 1) {

						if (Integer.parseInt(listlv.get(arg2 - 1).get("name")) >= Integer.parseInt(bounus)) {
							tv_addprice_danjia1.setText(listlv.get(arg2 - 1).get("name"));
							try {
								int s = Integer.parseInt(workercount)
										* Integer.parseInt(tv_addprice_danjia1.getText().toString());
								tv_help_biaojianjiage8.setText(s+"");
							} catch (Exception e) {
								// TODO: handle exception
							}

						} else {
							ToastUtil.makeShortText(AdditionalPriceActivity.this, "请选择大于" + bounus + "元的金额");
						}

					} else if (type == 2) {

						if (Integer.parseInt(listlv.get(arg2 - 1).get("name")) >= Integer.parseInt(standard_price)) {
							tv_addprice_danjia2.setText(listlv.get(arg2 - 1).get("name"));
						} else {
							ToastUtil.makeShortText(AdditionalPriceActivity.this, "请选择大于" + standard_price + "元的单价");
						}

					} else if (type == 3) {

						if (Integer.parseInt(listlv.get(arg2 - 1).get("name")) >= Integer.parseInt(suite_price)) {
							tv_addprice_danjia3.setText(listlv.get(arg2 - 1).get("name"));
						} else {
							ToastUtil.makeShortText(AdditionalPriceActivity.this, "请选择大于" + suite_price + "元的单价");
						}
					}

					popupwindow.dismiss();
				}
			}
		});
		adapter = new Myadapter4(this);
		lvCars.setAdapter(adapter);

	}

	Myadapter4 adapter;
	ArrayList<Map<String, String>> listlv;

	private ArrayList<Map<String, String>> getdashang() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 31; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}
		return list;
	}

	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 5; i < 41; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}
		return list;
	}

	private ArrayList<Map<String, String>> gettaofang() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 20; i < 61; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}

		return list;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void createOrderbB(String url, String token, String userid, String standardPrice, String suitePrice,
			String orderid, String bounus, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);

		map.put("standardPrice", standardPrice);
		map.put("suitePrice", suitePrice);
		map.put("orderid", orderid);
		map.put("bounus", bounus);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, AdditionalPriceActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(AdditionalPriceActivity.this), HttpStaticApi.changePriceB_Http, null,
				loadedtype);
	}
}
