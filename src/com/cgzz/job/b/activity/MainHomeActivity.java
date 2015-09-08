package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.GridHomeAdapter;
import com.cgzz.job.b.adapter.ViewPagerAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.GridHomeView;

public class MainHomeActivity extends BaseActivity
		implements OnClickListener, OnItemClickListener, OnPageChangeListener {
	private List<Map<String, String>> fromPassengerMaps;
	private LinearLayout addScroll;
	private ViewPagerAdapter viewPagerAdaPter;
	private final int GETDATAMessage = 101;
	private final int EmptyMessage = 102;
	private ArrayList<ImageView> mViews;
	private ScheduledExecutorService scheduledExecutorService;
	private boolean mark = true;
	private LinearLayout llright, llLeft, rl_home_one, rl_home_two;
	private TextView tv_title_left, tv_home_service;
	GridHomeView gridview;
	ViewPager viewpagerHeader;
	String count = "0";
	private ObserverCallBack callbackData = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {

			case HttpStaticApi.carousel_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserCarousel(data);
					fromPassengerMaps = (List<Map<String, String>>) bundle.getSerializable("list");

					if (fromPassengerMaps.size() > 0) {
						viewpagerHeader.setVisibility(View.VISIBLE);
						addScroll.setVisibility(View.VISIBLE);
						try {
							new Thread(new Runnable() {
								@Override
								public void run() {

									Message msg = handler.obtainMessage();
									msg.obj = fromPassengerMaps;
									msg.what = GETDATAMessage;
									handler.sendMessage(msg);
								}
							}).start();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				}
				break;

			case HttpStaticApi.bangkeCountB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserBangkeCount(data);
					count = bundle.getString("count").toString();
					tv_home_service.setText(count);
					break;
				case HttpStaticApi.FAILURE_HTTP:
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

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initView();
		if (application.isLogon()) {
			rl_home_one.setVisibility(View.VISIBLE);
			rl_home_two.setVisibility(View.GONE);
			bangkeCount(UrlConfig.bangkeCountB_Http, true);

		} else {
			rl_home_one.setVisibility(View.GONE);
			rl_home_two.setVisibility(View.VISIBLE);
		}
		carousel(UrlConfig.carousel_Http, "1", true);

	}

	private void initView() {
		rl_home_two = (LinearLayout) findViewById(R.id.rl_home_two);
		rl_home_one = (LinearLayout) findViewById(R.id.rl_home_one);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		tv_title_left = (TextView) findViewById(R.id.tv_title_left);
		tv_home_service = (TextView) findViewById(R.id.tv_home_service);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		rl_home_two.setOnClickListener(this);
		if (Utils.isEmpty(application.getUsercityName())) {

			tv_title_left.setText(application.getCityName());
		} else {
			tv_title_left.setText(application.getUsercityName());
		}
		gridview = (GridHomeView) findViewById(R.id.gridview);
		gridview.setAdapter(new GridHomeAdapter(this));
		gridview.setOnItemClickListener(this);
		// 图片轮询
		viewpagerHeader = (ViewPager) findViewById(R.id.vp);
		addScroll = (LinearLayout) findViewById(R.id.ll_scroll);
	}

	public void setAdapter() {
		viewPagerAdaPter = new ViewPagerAdapter(mViews);
		viewpagerHeader.setAdapter(viewPagerAdaPter);
		viewpagerHeader.setOnPageChangeListener(this);
		viewpagerHeader.setCurrentItem((mViews.size() == 1) ? 0 : 1);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		// case R.id.ll_title_left://
		// // popupwindow.showAsDropDown(arg0);
		// intent = new Intent(MainHomeActivity.this, SelectCityActivity.class);
		// startActivity(intent);
		// finish();
		// break;

		case R.id.rl_home_two:// 登录
			intent = new Intent(MainHomeActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
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

	/**
	 * 用户抢单接口
	 */
	private void getGrab(String url, String token, String userid, String orderid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.grab_Http, null, loadedtype);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent;
		if (arg2 == 0) {

			if (application.isLogon()) {
				intent = new Intent(MainHomeActivity.this, HelpPassengersActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("count", count);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainHomeActivity.this, "请先进行登录");
			}
		} else if (arg2 == 1) {
			if (application.isLogon()) {
				intent = new Intent(MainHomeActivity.this, SampledActivity.class);
				startActivity(intent);

			} else {
				ToastUtil.makeShortText(MainHomeActivity.this, "请先进行登录");
			}

		} else if (arg2 == 2) {
			if (application.isLogon()) {
				intent = new Intent(MainHomeActivity.this, PayRecordActivity.class);
				startActivity(intent);

			} else {
				ToastUtil.makeShortText(MainHomeActivity.this, "请先进行登录");
			}

		} else if (arg2 == 3) {

			if (application.isLogon()) {
				intent = new Intent(MainHomeActivity.this, MembersActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainHomeActivity.this, "请先进行登录");
			}

		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mViews == null || mViews.size() <= 1) {
			return;
		}
		// 这个地方是让他无线循环
		if (arg1 == 0.0) {
			if (arg0 == 0) {
				viewpagerHeader.setCurrentItem(mViews.size() - 2, false);
			} else if (arg0 == mViews.size() - 1) {
				viewpagerHeader.setCurrentItem(1, false);
			}
		} // 这两个判断是因为有时候滑动的时候像素不完全对
		else if (arg1 > 0.98 && arg0 == mViews.size() - 2) {
			viewpagerHeader.setCurrentItem(1, false);
		} else if (arg0 == 0 && arg1 < 0.00999) {
			viewpagerHeader.setCurrentItem(mViews.size() - 2, false);
		}
	}

	private int currentIndex;

	@Override
	public void onPageSelected(int position) {

		currentIndex = position;
		int childCount = addScroll.getChildCount();
		if (mViews.size() > 1) {// 页面改变改动小圆点
			for (int i = 0; i < childCount; i++) {
				View view = addScroll.getChildAt(i);
				view.setEnabled(false);
			}
			if (position == 0) {
				View views = addScroll.getChildAt(mViews.size() - 3);
				if (views != null) {
					views.setEnabled(true);
				}
			} else if (position == mViews.size() - 1) {
				View views = addScroll.getChildAt(0);
				if (views != null) {
					views.setEnabled(true);
				}
			} else {
				View views = addScroll.getChildAt(position - 1);
				if (views != null) {
					views.setEnabled(true);
				}
			}
		}

	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETDATAMessage:
				fromPassengerMaps = (ArrayList<Map<String, String>>) msg.obj;
				if (fromPassengerMaps != null && fromPassengerMaps.size() > 0) {
					mImageLoader = new ImageLoader(GlobalVariables.getRequestQueue(MainHomeActivity.this),
							new BitmapCache());
					mViews = new ArrayList<ImageView>();
					initImg(fromPassengerMaps);
					setAdapter();
					if (fromPassengerMaps.size() > 1) {
						scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
						// 设定执行线程计划,初始4s延迟,每次任务完成后延迟3s再执行一次任务
						scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 4, 3, TimeUnit.SECONDS);
					}
				} else {

				}
				break;

			case EmptyMessage:
				if (mark) {
					currentIndex++;
					viewpagerHeader.setCurrentItem(currentIndex, true);
				}
				break;
			}
		};
	};

	private class ViewPagerTask implements Runnable {

		@Override
		public void run() {
			handler.sendEmptyMessage(EmptyMessage);
		}
	}

	public void initImg(List<Map<String, String>> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		int size = (list.size() == 1) ? 1 : list.size() + 2;// 如果只有一个不需要轮询
		// 判断集合的大小，创建imageview
		for (int i = 0; i < size; i++) {
			ImageView imageView = new ImageView(MainHomeActivity.this);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			mViews.add(imageView);
		}
		int length = mViews.size();
		setImg(length, list);
	}

	@Override
	public void onDestroy() {

		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();// 结束所有的线程
		}
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}
		mark = false;
		super.onDestroy();
	}

	private boolean isStop = false;

	@Override
	public void onStop() {
		mark = false;
		super.onStop();
		if (scheduledExecutorService != null) {// 界面不可见结束所有的轮播
			scheduledExecutorService.shutdown();// 结束所有的线程
		}
		isStop = true;
	}

	private boolean isRunThread = true;

	public void setImg(int length, List<Map<String, String>> list) {
		int lengths = (length == 1) ? 1 : length - 2;// 判断当前有几个ImageView，如果只有一个不需要轮询
		int index = 0;
		// 给这个ImageVIew复制
		for (int i = 0; i < length; i++) {
			if (i < lengths) {// 如果是一个image就直接赋值，如果有多个就给他0和最后的角标不需要赋值
				index = (list.size() == 1) ? i : i + 1;

				Map<String, String> map = list.get(i);
				// 标题
				final String title = map.get("remark");
				// 路径
				String req_url_path = map.get("dict_param");
				final String url = req_url_path;
				try {
					ImageListener listener = ImageLoader.getImageListener(mViews.get(index),
							R.drawable.icon_loadfailed_bg, R.drawable.icon_loadfailed_bg);
					mImageLoader.get(map.get("dict_value"), listener);
				} catch (Exception e) {
				}
				mViews.get(index).setOnTouchListener(new OnTouchListener() {

					private long downTime;

					@Override
					public boolean onTouch(View vw, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							mark = false;
							downTime = System.currentTimeMillis();
							break;

						case MotionEvent.ACTION_CANCEL:
							if (isRunThread) {// 防止正在滑动的时候条目轮询
								isRunThread = false;
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										isRunThread = true;
										mark = true;
									}
								}).start();
							}
							break;
						case MotionEvent.ACTION_UP:
							if (System.currentTimeMillis() - downTime < 500) {
								// 当前的View的点击事件可以写在这里
								headViewPagerOnItemOnClick(title, url);
							}
							mark = true;
							break;
						}
						return true;
					}
				});

				if (mViews.size() > 1) {// 当图片dayu两张的时候
					View view = new View(MainHomeActivity.this);
					LayoutParams lay = new LayoutParams(8, 8);
					lay.leftMargin = 7;
					view.setBackgroundResource(R.drawable.select_radio);
					view.setLayoutParams(lay);
					view.setEnabled(false);
					addScroll.addView(view);
				}
			}
		}
		if (lengths == 1) {
			return;
		}
		Map<String, String> mapAfter = list.get(list.size() - 1);// 多张图给最后面的图设置背景
		ImageListener listenerAfter = ImageLoader.getImageListener(mViews.get(0), R.drawable.icon_loadfailed_bg,
				R.drawable.icon_loadfailed_bg);
		if (mapAfter.get("dict_value") != null)
			mImageLoader.get(mapAfter.get("dict_value"), listenerAfter);

		Map<String, String> mapFirst = list.get(0);// 多张图给最前面的图设置背景

		ImageListener listenerFirst = ImageLoader.getImageListener(mViews.get(length - 1),
				R.drawable.icon_loadfailed_bg, R.drawable.icon_loadfailed_bg);
		if (mapFirst.get("dict_value") != null)
			mImageLoader.get(mapFirst.get("dict_value"), listenerFirst);
	}

	public void headViewPagerOnItemOnClick(String title, String url) {
		if (!"".equals(url)) {
			Intent intent = new Intent(MainHomeActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, title);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
		} else {
		}
	}

	/**
	 * 获取轮播图
	 */
	private void carousel(String url, String apptype, boolean loadedtype) {
		url = UrlConfig.carousel_Http + "?apptype=1";
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
//		map.put("apptype", apptype);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.carousel_Http, null, loadedtype);

	}

	private void myhotel(String url, String token, String userid, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.myhotelB_Http, null, loadedtype);
	}

	/**
	 * 可服务帮客人数接口
	 */

	private void bangkeCount(String url, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainHomeActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainHomeActivity.this), HttpStaticApi.bangkeCountB_Http, null,
				loadedtype);
	}
}
