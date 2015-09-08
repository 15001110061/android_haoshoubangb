package com.cgzz.job.b.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.wheel.GoOffWheelView;
import com.cgzz.job.b.wheelview.NumericWheelAdapter;
import com.cgzz.job.b.wheelview.OnWheelScrollListener;
import com.cgzz.job.b.wheelview.WheelView;

/***
 * @author wjm 统计
 */
public class RechargeActivity extends BaseActivity implements OnClickListener {

	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.statisticsB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserStatistics(data);

					iv_re_msg_1.setText(bundle.getString("workercount").toString() + "次");
					iv_re_msg_2.setText(bundle.getString("ordercount").toString() + "单");

					iv_re_msg_3.setText(bundle.getString("totalbounus").toString() + "元");
					iv_re_title_zhichu.setText(bundle.getString("totalprice").toString() + "元");

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					break;

				default:
					break;
				}
				break;
			case HttpStaticApi.myhotelB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMyhotel(data);
					application.setBalance(bundle.getString("balance").toString());
					application.setPhone(bundle.getString("phone").toString());// 酒店电话
					application.setAddress(bundle.getString("address").toString());// 酒店地址
					application.setPriority(bundle.getString("priority").toString());// 是否能修改酒店信息
																						// 1：能
																						// 0：否
					application.setName(bundle.getString("name").toString());

					application.setLatitudeHotel(bundle.getString("latitude").toString());
					application.setLongitudeHotel(bundle.getString("longitude").toString());
					application.setReviewHotel(bundle.getString("reviewHotel").toString());
					application.setHotelid(bundle.getString("hotelid").toString());
					if (!Utils.isEmpty(bundle.getString("jifen"))) {
						application.setJifen(bundle.getString("jifen").toString());
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
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
		setContentView(R.layout.activity_recharge);
		// setTitle("统计", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
		// TITLE_TYPE_TEXT, "");
		findView();
		init();

		Assignment();

	}

	TextView iv_re_title_zhichu, iv_re_msg_1, iv_re_msg_2, iv_re_msg_3, iv_re_msg_4, tv_re_fapiao, iv_re_title_nian,
			iv_re_title_yue;
	RelativeLayout rl_re_time, llData;
	LinearLayout llLeft, llright;

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		iv_re_title_zhichu = (TextView) findViewById(R.id.iv_re_title_zhichu);
		rl_re_time = (RelativeLayout) findViewById(R.id.rl_re_time);
		iv_re_msg_1 = (TextView) findViewById(R.id.iv_re_msg_1);
		iv_re_msg_2 = (TextView) findViewById(R.id.iv_re_msg_2);
		iv_re_msg_3 = (TextView) findViewById(R.id.iv_re_msg_3);
		iv_re_msg_4 = (TextView) findViewById(R.id.iv_re_msg_4);
		tv_re_fapiao = (TextView) findViewById(R.id.tv_re_fapiao);
		iv_re_title_nian = (TextView) findViewById(R.id.iv_re_title_nian);
		iv_re_title_yue = (TextView) findViewById(R.id.iv_re_title_yue);

		//
		llData = (RelativeLayout) findViewById(R.id.ll);
		llData.addView(getDataPick());

	}

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_re_fapiao.setOnClickListener(this);
		rl_re_time.setOnClickListener(this);
	}

	private void Assignment() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat formatteryyyy = new SimpleDateFormat("yyyy");
		SimpleDateFormat formattermm = new SimpleDateFormat("MM");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String stryyyymm = formatter.format(curDate);

		String stryyyy = formatteryyyy.format(curDate);
		String strmm = formattermm.format(curDate);
		iv_re_title_nian.setText(stryyyy + "年");
		iv_re_title_yue.setText(strmm + "");
		if (application.isLogon())
			statistics(UrlConfig.statisticsB_Http, application.getToken(), application.getUserId(), stryyyymm, true);

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {

		case R.id.rl_re_time:// 选择日期
			llData.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (application.isLogon()) {
			myhotel(UrlConfig.myhotelB_Http, application.getToken(), application.getUserId(), true);
		} else {
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (llData.getVisibility() == 0) {
			llData.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
			finish();
		}
	}

	/***
	 * 
	 */

	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;

	// private WheelView shi;
	// private WheelView fen;
	// private WheelView day;
	// private int mYear = 1996;
	// private int mMonth = 0;
	// private int mDay = 1;
	View view = null;
	TextView tv_go_off_ok, tv_go_off_cancel, tv_go_off_title;
	GoOffWheelView goOffWheelView;
	NumericWheelAdapter numericWheelAdapter1, numericWheelAdapter2;

	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int curMonth = c.get(Calendar.MONTH);// 通过Calendar算出的月数要+1
		// int curDate = c.get(Calendar.DATE);

		// int curHOUR = c.get(Calendar.HOUR);
		// int curMINUTE = c.get(Calendar.MINUTE);
		// int curYear = mYear;
		// int curMonth = mMonth + 1;
		// int curDate = mDay;

		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.wheel_date_pickert, null);
		ImageButton dis = (ImageButton) view.findViewById(R.id.ib_dis);
		tv_go_off_ok = (TextView) view.findViewById(R.id.tv_go_off_ok);
		tv_go_off_cancel = (TextView) view.findViewById(R.id.tv_go_off_cancel);
		tv_go_off_title = (TextView) view.findViewById(R.id.tv_go_off_title);

		year = (WheelView) view.findViewById(R.id.year);
		numericWheelAdapter1 = new NumericWheelAdapter(this, norYear, norYear + 1);

		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(false);// 是否可循环滑动
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		numericWheelAdapter2 = new NumericWheelAdapter(this, 1, 12, "%02d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(false);
		month.addScrollingListener(scrollListener);

		month.setVisibleItems(7);
		year.setVisibleItems(7);

		month.setCurrentItem(curMonth);
		year.setCurrentItem(0);

		tv_go_off_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llData.setVisibility(View.GONE);
				if (!Utils.isEmpty(nian) && !Utils.isEmpty(yue)) {
					iv_re_title_nian.setText(nian + "年");
					iv_re_title_yue.setText(yue + "");
					statistics(UrlConfig.statisticsB_Http, application.getToken(), application.getUserId(),
							nian + "" + yue, true);
				}

			}
		});
		tv_go_off_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				llData.setVisibility(View.GONE);

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				llData.setVisibility(View.GONE);
			}
		});

		return view;
	}

	String nian = "";
	String yue = "";
	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int niannum = year.getCurrentItem();
			int yuenum = month.getCurrentItem();
			nian = numericWheelAdapter1.getItemText(niannum) + "";
			yue = numericWheelAdapter2.getItemText(yuenum) + "";

		}
	};

	private void statistics(String url, String token, String userid, String key, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("type", "1");

		map.put("key", key);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, RechargeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(RechargeActivity.this), HttpStaticApi.statisticsB_Http, null,
				loadedtype);
	}

	private void myhotel(String url, String token, String userid, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, RechargeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(RechargeActivity.this), HttpStaticApi.myhotelB_Http, null, loadedtype);
	}

}
