package com.cgzz.job.b.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

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
import com.cgzz.job.b.view.CityPicker;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.wheel.GoOffWheelView;
import com.cgzz.job.b.wheelview.NumericWheelAdapter;
import com.cgzz.job.b.wheelview.OnWheelScrollListener;
import com.cgzz.job.b.wheelview.WheelView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm 主页：发布订单
 */
public class HelpPassengersActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private int type = 0, typetime = 0;
	int min_standard_price = -1;
	int min_suite_price = -1;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.createOrderbB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(HelpPassengersActivity.this, bundle.get("msg").toString());
					// tv_help_zhaobangke2.setClickable(true);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(HelpPassengersActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.priceB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserpriceB(data);

					try {
						min_standard_price = Integer.parseInt(bundle.getString("min_standard_price"));
						min_suite_price = Integer.parseInt(bundle.getString("min_suite_price"));

						tv_help_biaojianjiage2.setText(min_standard_price + "");
						tv_help_taojianjiage2.setText(min_suite_price + "");

					} catch (Exception e) {
						min_standard_price = -1;
						min_suite_price = -1;
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(HelpPassengersActivity.this, bundle.get("msg").toString());
					try {
						new Thread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
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
	String count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helppassengers);
		setTitle("找帮客", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "计价规则");
		Intent intent = getIntent();
		count = intent.getStringExtra("count");
		findView();
		init();
		initmPopupWindowView();
		Assignment();
		priceB(UrlConfig.priceB_Http, application.getUserId(), application.getToken(), true);
	}

	LinearLayout llLeft, llright;
	private TextView tv_help_time, tv_help_biaojianjiage1, tv_help_biaojianjiage2, tv_help_biaojianjiage3,
			tv_help_xuqiu, tv_help_taojianjiage1, tv_help_taojianjiage2, tv_help_taojianjiage3, tv_help_bangkesnum1,
			tv_help_bangkesnum2, tv_help_bangkesnum3, tv_help_zhaobangke2, tv_help_shang, tv_help_xian, tv_help_can,
			tv_help_ba, tv_help_liu;
	private RelativeLayout rl_help_1, rl_help_2, tv_help_buchong;
	EditText et_help_biaojian, et_help_taofang;
	private CityPicker cityPicker;
	RelativeLayout ll_time;
	TextView timeok, timecancel;
	CheckBox cb_seting_shoushi;
	ImageButton dis;

	private void findView() {
		//
		ll_time = (RelativeLayout) findViewById(R.id.ll_time);
		cityPicker = (CityPicker) findViewById(R.id.citypicker);

		timeok = (TextView) findViewById(R.id.tv_time_ok);
		timecancel = (TextView) findViewById(R.id.tv_time_cancel);
		dis = (ImageButton) findViewById(R.id.ib_dis);
		timeok.setOnClickListener(this);
		timecancel.setOnClickListener(this);
		dis.setOnClickListener(this);
		//
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_help_time = (TextView) findViewById(R.id.tv_help_time);
		et_help_biaojian = (EditText) findViewById(R.id.et_help_biaojian);
		tv_help_biaojianjiage1 = (TextView) findViewById(R.id.tv_help_biaojianjiage1);
		tv_help_biaojianjiage2 = (TextView) findViewById(R.id.tv_help_biaojianjiage2);
		tv_help_biaojianjiage3 = (TextView) findViewById(R.id.tv_help_biaojianjiage3);
		tv_help_xuqiu = (TextView) findViewById(R.id.tv_help_xuqiu);
		et_help_taofang = (EditText) findViewById(R.id.et_help_taofang);
		tv_help_taojianjiage1 = (TextView) findViewById(R.id.tv_help_taojianjiage1);
		tv_help_taojianjiage2 = (TextView) findViewById(R.id.tv_help_taojianjiage2);
		tv_help_taojianjiage3 = (TextView) findViewById(R.id.tv_help_taojianjiage3);
		rl_help_1 = (RelativeLayout) findViewById(R.id.rl_help_1);
		rl_help_2 = (RelativeLayout) findViewById(R.id.rl_help_2);
		tv_help_bangkesnum1 = (TextView) findViewById(R.id.tv_help_bangkesnum1);

		tv_help_bangkesnum2 = (TextView) findViewById(R.id.tv_help_bangkesnum2);
		tv_help_bangkesnum3 = (TextView) findViewById(R.id.tv_help_bangkesnum3);
		tv_help_zhaobangke2 = (TextView) findViewById(R.id.tv_help_zhaobangke2);
		tv_help_buchong = (RelativeLayout) findViewById(R.id.tv_help_buchong);
		// tv_home_service = (TextView) findViewById(R.id.tv_home_service);

		//
		// ll = (RelativeLayout) findViewById(R.id.ll);
		// ll.addView(getDataPick());
		//
		tv_help_shang = (TextView) findViewById(R.id.tv_help_shang);
		tv_help_xian = (TextView) findViewById(R.id.tv_help_xian);
		tv_help_can = (TextView) findViewById(R.id.tv_help_can);
		tv_help_ba = (TextView) findViewById(R.id.tv_help_ba);
		tv_help_liu = (TextView) findViewById(R.id.tv_help_liu);
		//
		cb_seting_shoushi = (CheckBox) findViewById(R.id.cb_seting_shoushi);
		//
		rl_help_time1 = (RelativeLayout) findViewById(R.id.rl_help_time1);
		rl_help_time2 = (RelativeLayout) findViewById(R.id.rl_help_time2);
		rl_help_time3 = (RelativeLayout) findViewById(R.id.rl_help_time3);
		rl_help_time4 = (RelativeLayout) findViewById(R.id.rl_help_time4);
		tv_help_time1 = (TextView) findViewById(R.id.tv_help_time1);
		tv_help_time2 = (TextView) findViewById(R.id.tv_help_time2);
		tv_help_time3 = (TextView) findViewById(R.id.tv_help_time3);
		rl_help_time1.setVisibility(View.GONE);
		rl_help_time2.setVisibility(View.GONE);
		rl_help_time3.setVisibility(View.GONE);
		rl_help_time4.setVisibility(View.VISIBLE);
	}

	TextView tv_help_time1, tv_help_time2, tv_help_time3;
	RelativeLayout rl_help_time1, rl_help_time2, rl_help_time3, rl_help_time4;

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_help_time.setOnClickListener(this);
		tv_help_biaojianjiage1.setOnClickListener(this);
		tv_help_biaojianjiage2.setOnClickListener(this);
		tv_help_biaojianjiage3.setOnClickListener(this);

		tv_help_xuqiu.setOnClickListener(this);
		tv_help_taojianjiage1.setOnClickListener(this);
		tv_help_taojianjiage2.setOnClickListener(this);
		tv_help_taojianjiage3.setOnClickListener(this);
		tv_help_bangkesnum1.setOnClickListener(this);
		tv_help_bangkesnum2.setOnClickListener(this);
		tv_help_bangkesnum3.setOnClickListener(this);
		tv_help_zhaobangke2.setOnClickListener(this);
		tv_help_buchong.setOnClickListener(this);

		et_help_biaojian.addTextChangedListener(textWatcher);
		et_help_taofang.addTextChangedListener(textWatcher);
		cb_seting_shoushi.setOnCheckedChangeListener(this);

		tv_help_time1.setOnClickListener(this);
		tv_help_time2.setOnClickListener(this);
		tv_help_time3.setOnClickListener(this);

	}

	private void Assignment() {
		// tv_home_service.setText(count);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			tv_help_bangkesnum2.setText("" + bangkeRe());
		}
	};

	public static void closeInputMethod(Activity context) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	String start = "", end = "";

	@Override
	public void onClick(View arg0) {
		Intent intent = null;

		String biaojianshuliang = "";
		String biaojianjiage = "";
		String taofangshuliang = "";
		String taofangjiage = "";
		String bangkerenshu = "";

		switch (arg0.getId()) {
		case R.id.tv_help_time:// 选择时间
			closeInputMethod(HelpPassengersActivity.this);
			// ll.setVisibility(View.VISIBLE);
			ll_time.setVisibility(View.VISIBLE);
			cityPicker.setData();
			typetime = 1;
			break;

		case R.id.tv_help_xuqiu:// 隐藏或者显示
			if (rl_help_1.getVisibility() == 0) {

				Drawable drawable = getResources().getDrawable(R.drawable.icon_xiangxia);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tv_help_xuqiu.setCompoundDrawables(drawable, null, null, null);

				rl_help_1.setVisibility(View.GONE);
				rl_help_2.setVisibility(View.GONE);
			} else {
				rl_help_1.setVisibility(View.VISIBLE);
				rl_help_2.setVisibility(View.VISIBLE);
				Drawable drawable = getResources().getDrawable(R.drawable.icon_xiangshang);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				tv_help_xuqiu.setCompoundDrawables(drawable, null, null, null);
			}

			break;
		case R.id.tv_help_biaojianjiage1:// 标间价格
		case R.id.tv_help_biaojianjiage2:
		case R.id.tv_help_biaojianjiage3:
			if (popupwindow != null) {
				tv_current_room.setVisibility(View.VISIBLE);
				tv_current_room2.setVisibility(View.VISIBLE);
				tv_current_room3.setVisibility(View.VISIBLE);
				type = 1;
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getbiaojian();
				adapter.refreshData(listlv);
			}

			break;
		case R.id.tv_help_taojianjiage1:// 套房价格
		case R.id.tv_help_taojianjiage2:
		case R.id.tv_help_taojianjiage3:
			if (popupwindow != null) {
				tv_current_room.setVisibility(View.VISIBLE);
				tv_current_room2.setVisibility(View.VISIBLE);
				tv_current_room3.setVisibility(View.VISIBLE);
				type = 2;
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = gettaofang();

				adapter.refreshData(listlv);
			}

			break;
		case R.id.tv_help_bangkesnum1:// 帮客数量
		case R.id.tv_help_bangkesnum2:
		case R.id.tv_help_bangkesnum3:

			if (popupwindow != null) {
				tv_current_room.setVisibility(View.GONE);
				tv_current_room2.setVisibility(View.GONE);
				tv_current_room3.setVisibility(View.GONE);
				type = 3;
				popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getbangke();

				adapter.refreshData(listlv);
			}
			break;

		case R.id.tv_help_zhaobangke2:
			String time = tv_help_time.getText().toString();
			String time3 = tv_help_time3.getText().toString();
			if (!cb_seting_shoushi.isChecked()) {
				if (Utils.isEmpty(time)) {
					ToastUtil.makeShortText(this, "请选择时间");
					return;
				}
			}else{
				if (Utils.isEmpty(time3)) {
					ToastUtil.makeShortText(this, "请选择每天上班时间");
					return;
				}
			}

			biaojianshuliang = et_help_biaojian.getText().toString();
			biaojianjiage = tv_help_biaojianjiage2.getText().toString();
			taofangshuliang = et_help_taofang.getText().toString();
			taofangjiage = tv_help_taojianjiage2.getText().toString();
			bangkerenshu = tv_help_bangkesnum2.getText().toString();

			// if ("0".equals(tv_help_biaojianjiage2.getText().toString())) {
			//
			// ToastUtil.makeShortText(this, "请填写标间价格");
			// return;
			// } else {
			if (min_standard_price == -1) {
				ToastUtil.makeShortText(this, "标间最低价格未获取到");
				return;
			}
			if (Integer.parseInt(tv_help_biaojianjiage2.getText().toString()) < min_standard_price) {
				ToastUtil.makeShortText(this, "标间价格最低为" + min_standard_price + "元");
				return;
			} else {
				// if (Utils.isEmpty(biaojianshuliang)) {
				// ToastUtil.makeShortText(this, "请补充标间数量");
				// return;
				// }
				if (!Utils.isEmpty(biaojianshuliang)) {
					if (Integer.parseInt(biaojianshuliang) > 600) {
						ToastUtil.makeShortText(this, "标间数量已超出600间");
						return;
					}
				}
			}
			// }

			// 套房
			if (rl_help_1.getVisibility() == 0) {
				// if ("0".equals(tv_help_taojianjiage2.getText().toString())) {
				// ToastUtil.makeShortText(this, "请填写套房价格");
				// return;
				// } else {
				if (min_suite_price == -1) {
					ToastUtil.makeShortText(this, "套间最低价格未获取到");
					return;
				}
				if (Integer.parseInt(tv_help_taojianjiage2.getText().toString()) < min_suite_price) {
					ToastUtil.makeShortText(this, "套间价格最低为" + min_suite_price + "元");
					return;
				} else {
					// if (Utils.isEmpty(taofangshuliang)) {
					// ToastUtil.makeShortText(this, "请补充套房数量");
					// return;
					// }
					if (Utils.isEmpty(taofangshuliang)) {

						if (Integer.parseInt(taofangshuliang) > 200) {
							ToastUtil.makeShortText(this, "套房数量已超出200间");
							return;
						}
					}
				}

				// }
			}
			if (Utils.isEmpty(bangkerenshu) || Integer.parseInt(bangkerenshu) == 0) {
				ToastUtil.makeShortText(this, "请补帮客人数");
				return;
			}

			if ("1".equals(application.getReviewHotel())) {

				if (cb_seting_shoushi.isChecked()) {
					int i = CalculationTime(start, end);
					if (i < 7 && i > 0) {
						createOrderbB(UrlConfig.createOrderbB, application.getUserId(), application.getToken(), tv_help_time1.getText().toString()+" "+time3,
								biaojianshuliang, biaojianjiage, taofangshuliang, taofangjiage, bangkerenshu, bounus,
								havebar, havelaunch, iscash, mes, Voiceurl, length, tv_help_time1.getText().toString(),tv_help_time2.getText().toString(),true);
					} else if (i < 0 && i < -358) {
						createOrderbB(UrlConfig.createOrderbB, application.getUserId(), application.getToken(), tv_help_time1.getText().toString()+" "+time3,
								biaojianshuliang, biaojianjiage, taofangshuliang, taofangjiage, bangkerenshu, bounus,
								havebar, havelaunch, iscash, mes, Voiceurl, length,tv_help_time1.getText().toString(),tv_help_time2.getText().toString(), true);
					} else if (i == 0) {
						ToastUtil.makeShortText(HelpPassengersActivity.this, "请选择不同的时间");
					} else {
						ToastUtil.makeShortText(HelpPassengersActivity.this, "时间请选择7天内的");
					}
				} else {
					createOrderbB(UrlConfig.createOrderbB, application.getUserId(), application.getToken(), time,
							biaojianshuliang, biaojianjiage, taofangshuliang, taofangjiage, bangkerenshu, bounus,
							havebar, havelaunch, iscash, mes, Voiceurl, length,"","", true);
				}

			} else {
				ToastUtil.makeShortText(HelpPassengersActivity.this, "请先进入个人中心绑定企业");
			}
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 计价规则
			String url = "http://www.haoshoubang.com/bangke/html/role.html";
			intent = new Intent(HelpPassengersActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "计价规则");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
			break;

		case R.id.tv_help_buchong:// 补充内容

			intent = new Intent(HelpPassengersActivity.this, SupplementaryContentActivity.class);

			// String Voiceurl = "", havebar = "", havelaunch = "", iscash = "",
			// bounus = "",length="";

			Bundle bundle = new Bundle();
			bundle.putString("Voiceurl", Voiceurl);
			bundle.putString("havebar", havebar);
			bundle.putString("havelaunch", havelaunch);
			bundle.putString("iscash", iscash);
			bundle.putString("bounus", bounus);
			bundle.putString("mes", mes);
			bundle.putString("length", length);
			bundle.putString("FileName", FileName);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);

			break;

		case R.id.tv_current_room3:// 清除
			if (type == 1) {
				if ("-1".equals(min_standard_price + "")) {
					tv_help_biaojianjiage2.setText("0");
				} else {
					tv_help_biaojianjiage2.setText(min_standard_price + "");
				}

			} else if (type == 2) {
				if ("-1".equals(min_suite_price + "")) {
					tv_help_taojianjiage2.setText("0");
				} else {
					tv_help_taojianjiage2.setText(min_suite_price + "");
				}

			}
			popupwindow.dismiss();
			break;

		case R.id.tv_time_cancel:// 取消

			ll_time.setVisibility(View.GONE);
			break;
		case R.id.tv_time_ok://

			ll_time.setVisibility(View.GONE);
			if (typetime == 1) {

				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd ");
				java.util.Calendar calendar = java.util.Calendar.getInstance();
				// calendar.roll(java.util.Calendar.DAY_OF_YEAR,-1);
				// System.out.println( "昨天： "+df.format(calendar.getTime()));
				// calendar=java.util.Calendar.getInstance();
				// System.out.println( "今天： "+df.format(calendar.getTime()));
				// calendar.roll(java.util.Calendar.DAY_OF_YEAR,1);
				// System.out.println( "明天： "+df.format(calendar.getTime()));

				StringBuffer times = new StringBuffer();

				if ("今天".equals(cityPicker.getCity_string1())) {
					calendar = java.util.Calendar.getInstance();
					times.append(df.format(calendar.getTime()));
				} else if ("明天".equals(cityPicker.getCity_string1())) {
					calendar.roll(java.util.Calendar.DAY_OF_YEAR, 0);
					times.append(df.format(calendar.getTime()));
				} else if ("后天".equals(cityPicker.getCity_string1())) {
					calendar.roll(java.util.Calendar.DAY_OF_YEAR, 1);

					times.append(df.format(calendar.getTime()));
				}

				times.append(cityPicker.getCity_string2().replace("点", "") + ":");
				times.append(cityPicker.getCity_string3());
				tv_help_time.setText(times.toString() + "");
			} else if (typetime == 2) {// 长期开始
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-");
				java.util.Calendar calendar = java.util.Calendar.getInstance();
				StringBuffer times = new StringBuffer();
				times.append(df.format(calendar.getTime()));// 年
				String yue = "", ri = "";
				yue = cityPicker.getCity_string1();
				ri = cityPicker.getCity_string2();
				System.out.println("wjm==长期开始===yue" + yue + "ri==" + ri);
				times.append(yue.replace("月", "") + "-");// 月
				times.append(ri.replace("日", "") + " ");// 日
				tv_help_time1.setText(times.toString() + "");
				start = yue.replace("月", "") + ri.replace("日", "");
				// tv_help_time.setText("2015-" + yue + "-" + ri + " " + shii +
				// ":00");
			} else if (typetime == 3) {// 长期结束
				StringBuffer times = new StringBuffer();

				String yue = "", ri = "";
				yue = cityPicker.getCity_string1();
				ri = cityPicker.getCity_string2();

				System.out.println("wjm==长期结束===yue" + yue + "ri==" + ri);
				int i = CalculationTime(start, yue.replace("月", "") + ri.replace("日", ""));
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy");
				java.util.Calendar calendar = java.util.Calendar.getInstance();
				if (i < 7 && i > 0) {
					times.append(df.format(calendar.getTime()) + "-");// 年
					times.append(yue.replace("月", "") + "-");// 月
					times.append(ri.replace("日", "") + " ");// 日
					tv_help_time2.setText(times.toString() + "");
					end = yue.replace("月", "") + ri.replace("日", "");
				} else if (i < 0 && i < -358) {

					times.append(Integer.parseInt(df.format(calendar.getTime())) + 1 + "-");// 年
					times.append(yue.replace("月", "") + "-");// 月
					times.append(ri.replace("日", "") + " ");// 日
					tv_help_time2.setText(times.toString() + "");
					end = yue.replace("月", "") + ri.replace("日", "");
				} else if (i == 0) {
					ToastUtil.makeShortText(HelpPassengersActivity.this, "请选择不同的时间");
				} else {
					ToastUtil.makeShortText(HelpPassengersActivity.this, "时间请选择7天内的");
				}

			}else if (typetime ==4) {
				StringBuffer times = new StringBuffer();
				String cqshi = "", cqfen = "";
				
				cqshi = cityPicker.getCity_string2();
				cqfen = cityPicker.getCity_string3();
				times.append(cqshi.replace("点", "") + ":");// 
				times.append(cqfen+"");// 
				tv_help_time3.setText(times.toString() + "");
			}
			break;
		case R.id.ib_dis://
			ll_time.setVisibility(View.GONE);
			break;

		case R.id.tv_help_time1://
			ll_time.setVisibility(View.VISIBLE);
			cityPicker.setData1();
			typetime = 2;
			break;

		case R.id.tv_help_time2://
			if ("".equals(start)) {
				ToastUtil.makeShortText(HelpPassengersActivity.this, "请先选择开始日期");
			} else {
				ll_time.setVisibility(View.VISIBLE);
				cityPicker.setData1();
				typetime = 3;
			}

			break;

		case R.id.tv_help_time3://
			
			
			ll_time.setVisibility(View.VISIBLE);
			cityPicker.setData2();
			typetime = 4;
			
			
			
			break;

		default:
			break;
		}

	}

	public int CalculationTime(String str1, String str2) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMd");// 输入日期的格式
		Date date1 = null;
		try {
			date1 = simpleDateFormat.parse(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date2 = null;
		try {
			date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTime(date1);
		cal2.setTime(date2);
		double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);// 从间隔毫秒变成间隔天数
		System.out.println("wjm====相差" + (int) dayCount + "天");

		return (int) dayCount;
	}

	public int bangkeRe() {
		// 帮客数 ＝ 标间数 /15 + 套间数*1.5 / 15
		int bk = 0;
		String b = et_help_biaojian.getText().toString();
		if (Utils.isEmpty(b) || "0".equals(b)) {
			b = "0";
		}

		String t = et_help_taofang.getText().toString();
		if (Utils.isEmpty(t) || "0".equals(t)) {
			t = "0";
		}

		bk = (int) (Integer.parseInt(b) / 15 + Integer.parseInt(t) * 1.5 / 15);

		return bk;

	}

	String Voiceurl = "", havebar = "", havelaunch = "", iscash = "", bounus = "", mes = "", length = "", FileName = "";

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {

			Voiceurl = data.getStringExtra("Voiceurl");
			havebar = data.getStringExtra("havebar");
			havelaunch = data.getStringExtra("havelaunch");
			iscash = data.getStringExtra("iscash");
			bounus = data.getStringExtra("bounus");
			mes = data.getStringExtra("mes");
			length = data.getStringExtra("length");
			FileName = data.getStringExtra("FileName");

			if (!Utils.isEmpty(bounus)) {
				tv_help_shang.setVisibility(View.VISIBLE);
			}

			if (!Utils.isEmpty(iscash)) {
				tv_help_xian.setVisibility(View.VISIBLE);
			}

			if (!Utils.isEmpty(havelaunch)) {
				tv_help_can.setVisibility(View.VISIBLE);
			}

			if (!Utils.isEmpty(havebar)) {
				tv_help_ba.setVisibility(View.VISIBLE);
			}

			if (!Utils.isEmpty(mes) || !Utils.isEmpty(Voiceurl)) {
				tv_help_liu.setVisibility(View.VISIBLE);
			}

			// 根据上面发送过去的请求吗来区别
			switch (requestCode) {
			case 1:

				break;
			default:
				break;
			}
		}

	}

	/**
	 *
	 */

	PopupWindow popupwindow;
	CustomListView lvCars;
	TextView tv_current_room, tv_current_room3;
	View tv_current_room2;

	public void initmPopupWindowView() {
		// 获取自定义布局文件的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		ImageButton dis = (ImageButton) customView.findViewById(R.id.ib_dis);

		tv_current_room = (TextView) customView.findViewById(R.id.tv_current_room);
		tv_current_room2 = (View) customView.findViewById(R.id.tv_current_room2);
		tv_current_room3 = (TextView) customView.findViewById(R.id.tv_current_room3);
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
		tv_current_room3.setOnClickListener(this);

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
						tv_help_biaojianjiage2.setText(listlv.get(arg2 - 1).get("name"));
					} else if (type == 2) {
						tv_help_taojianjiage2.setText(listlv.get(arg2 - 1).get("name"));
					} else if (type == 3) {
						tv_help_bangkesnum2.setText(listlv.get(arg2 - 1).get("name"));
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

	// 标间价格
	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// Map<String, String> map2 = new HashMap<String, String>();
		// map2.put("name", "0");
		// list.add(map2);
		for (int i = 5; i < 41; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}
		return list;
	}

	private ArrayList<Map<String, String>> gettaofang() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// Map<String, String> map2 = new HashMap<String, String>();
		// map2.put("name", "0");
		// list.add(map2);
		for (int i = 20; i < 61; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}

		return list;
	}

	private ArrayList<Map<String, String>> getbangke() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < 51; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}

		return list;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (ll_time.getVisibility() == 0) {
			ll_time.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
			finish();
		}
	}

	/***
	 * 
	 */

	private LayoutInflater inflater = null;
	private WheelView shi;
	private WheelView fen;

	private WheelView month;
	private WheelView day;
	private int mYear = 1996;
	private int mMonth = 0;
	private int mDay = 1;
	View view = null;
	TextView tv_go_off_ok, tv_go_off_cancel, tv_go_off_title;
	GoOffWheelView goOffWheelView;

	// private View getDataPick() {
	// Calendar c = Calendar.getInstance();
	// int norYear = c.get(Calendar.YEAR);
	// int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
	// int curDate = c.get(Calendar.DATE);
	// int curHOUR = c.get(Calendar.HOUR_OF_DAY);
	// int curMINUTE = c.get(Calendar.MINUTE);
	// int curYear = mYear;
	// // int curMonth = mMonth + 1;
	// // int curDate = mDay;
	//
	// inflater = (LayoutInflater)
	// this.getSystemService(LAYOUT_INFLATER_SERVICE);
	// view = inflater.inflate(R.layout.wheel_date_picker, null);
	// ImageButton dis = (ImageButton) view.findViewById(R.id.ib_dis);
	// tv_go_off_ok = (TextView) view.findViewById(R.id.tv_go_off_ok);
	// tv_go_off_cancel = (TextView) view.findViewById(R.id.tv_go_off_cancel);
	// tv_go_off_title = (TextView) view.findViewById(R.id.tv_go_off_title);
	//
	// month = (WheelView) view.findViewById(R.id.month);
	// NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(this,
	// 1, 12, "%02d");
	// numericWheelAdapter2.setLabel("月");
	// month.setViewAdapter(numericWheelAdapter2);
	// month.setCyclic(false);
	// month.addScrollingListener(scrollListener);
	//
	// day = (WheelView) view.findViewById(R.id.day);
	// initDay(curYear, curMonth);
	// day.setCyclic(false);
	// day.addScrollingListener(scrollListener);
	//
	// shi = (WheelView) view.findViewById(R.id.shi);
	// NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this,
	// 1, 24);
	// numericWheelAdapter1.setLabel("时");
	// shi.setViewAdapter(numericWheelAdapter1);
	// shi.setCyclic(false);// 是否可循环滑动
	// shi.addScrollingListener(scrollListener);
	//
	// fen = (WheelView) view.findViewById(R.id.fen);
	// NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(this,
	// 0, 59);
	// numericWheelAdapter4.setLabel("分");
	// fen.setViewAdapter(numericWheelAdapter4);
	// fen.setCyclic(false);// 是否可循环滑动
	// fen.addScrollingListener(scrollListener);
	//
	// shi.setVisibleItems(7);// 设置显示行数
	// month.setVisibleItems(7);
	// day.setVisibleItems(7);
	// fen.setVisibleItems(7);
	//
	// month.setCurrentItem(curMonth - 1);
	// day.setCurrentItem(curDate - 1);
	// shi.setCurrentItem(curHOUR - 1);
	// // month.setCurrentItem(0);
	// // day.setCurrentItem(0);
	// fen.setCurrentItem(curMINUTE - 1);
	//
	// tv_go_off_ok.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// ll.setVisibility(View.GONE);
	//
	// }
	// });
	// tv_go_off_cancel.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// ll.setVisibility(View.GONE);
	// // tv_help_time.setText("");
	//
	// }
	// });
	// dis.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// ll.setVisibility(View.GONE);
	// }
	// });
	//
	// return view;
	// }

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int yue = month.getCurrentItem() + 1;
			int ri = day.getCurrentItem() + 1;
			int shii = shi.getCurrentItem() + 1;
			int fenn = fen.getCurrentItem();
			if (fenn == 0) {
				tv_help_time.setText("2015-" + yue + "-" + ri + " " + shii + ":00");
			} else {
				tv_help_time.setText("2015-" + yue + "-" + ri + " " + shii + ":" + fenn);
			}
			// initDay(n_year,n_month);

		}
	};

	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}

	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	private void createOrderbB(String url, String userid, String token, String dutydate, String standardCount,
			String standardPrice, String suiteCount, String suitePrice, String workercount, String bounus,
			String havebar, String havelaunch, String iscash, String message, String voiceurl, String length,String begindate,String enddate,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", token);
		map.put("apptype", "1");
		map.put("dutydate", dutydate);

		if (!Utils.isEmpty(standardCount)) {
			map.put("standardCount", standardCount);
			map.put("standardPrice", standardPrice);
		}

		if (!Utils.isEmpty(suiteCount)) {
			map.put("suiteCount", suiteCount);
			map.put("suitePrice", suitePrice);
		}

		map.put("workercount", workercount);

		if (!Utils.isEmpty(bounus)) {
			map.put("bounus", bounus);
		}

		if (!Utils.isEmpty(havebar)) {
			map.put("havebar", havebar);
		}
		if (!Utils.isEmpty(havelaunch)) {
			map.put("havelaunch", havelaunch);
		}
		if (!Utils.isEmpty(iscash)) {
			map.put("iscash", iscash);
		}
		if (!Utils.isEmpty(message)) {
			map.put("message", message);
		}
		if (!Utils.isEmpty(voiceurl)) {
			map.put("voiceurl", voiceurl);
			map.put("length", length);
		}
		
		if (cb_seting_shoushi.isChecked()) {
			
			map.put("begindate", begindate);
			map.put("enddate", enddate);
		}
		
		
		
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, HelpPassengersActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(HelpPassengersActivity.this), HttpStaticApi.createOrderbB_Http, null,
				loadedtype);
	}

	private void priceB(String url, String userid, String token, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", token);
		map.put("apptype", "1");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, HelpPassengersActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(HelpPassengersActivity.this), HttpStaticApi.priceB_Http, null,
				loadedtype);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (arg0 == cb_seting_shoushi) {
			if (arg1) {

				rl_help_time1.setVisibility(View.VISIBLE);
				rl_help_time2.setVisibility(View.VISIBLE);
				rl_help_time3.setVisibility(View.VISIBLE);
				rl_help_time4.setVisibility(View.GONE);

			} else {

				rl_help_time1.setVisibility(View.GONE);
				rl_help_time2.setVisibility(View.GONE);
				rl_help_time3.setVisibility(View.GONE);
				rl_help_time4.setVisibility(View.VISIBLE);

			}

		}

	}
}
