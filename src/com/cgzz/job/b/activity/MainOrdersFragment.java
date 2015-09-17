package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.CurrentAdapter;
import com.cgzz.job.b.adapter.CurrentAdapter.OnCancelOrderClickListener;
import com.cgzz.job.b.adapter.CurrentAdapter.OnRouteClickListener;
import com.cgzz.job.b.adapter.CurrentAdapter.OnTextClickListener;
import com.cgzz.job.b.adapter.CurrentAdapter.OnZJClickListener;
import com.cgzz.job.b.adapter.FillOrdersAdapter;
import com.cgzz.job.b.adapter.FillOrdersAdapter.OnZFClickListener;
import com.cgzz.job.b.adapter.ReviewsAdapter;
import com.cgzz.job.b.adapter.ReviewsAdapter.OnTelClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomDialog;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.b.view.CustomListView.OnRefreshListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm 主页：订单
 */
public class MainOrdersFragment extends BaseActivity implements OnClickListener, OnCancelOrderClickListener,
		OnRouteClickListener, OnTelClickListener, OnTextClickListener, OnZJClickListener, OnZFClickListener {
	private TextView tvCurrent, tvFinish, tvPingjia;
	private ArrayList<Map<String, String>> shufflingList;
	private boolean isManul;
	private ViewPager viewpager;
	private ImageView ivLine;
	private int LENGTH;
	private CustomListView lvCurrent, lvFillOrders, lvreviews;
	private CurrentAdapter Currentadapter;
	private FillOrdersAdapter FillOrdersadapter;

	private ReviewsAdapter reviewsAdapter;

	public static final int TYPE_TAB_Current = 0;
	public static final int TYPE_TAB_Finish = 1;
	public static final int TYPE_TAB_pingjia = 2;
	private int currentIndicatorPosition;
	private int logoCurrent = 1;// 页码标识
	private int logoFillOrders = 1;// 页码标识
	private int logoreviews = 1;// 页码标识
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> ReviewsData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.DandqianB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						CurrentData.clear();
						logoCurrent = 2;
					} else {
						logoCurrent++;
					}

					bundle = ParserUtil.ParserMylistDangqian(data);
					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

						CurrentData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
						lvCurrent.setCanLoadMore(true);// 打开加载更多
						lvCurrent.removeHeaderView(nowanchengorders);
					} else {

						if (!loadedtype) {
							lvCurrent.onLoadMorNodata();
						} else {
							lvCurrent.setCanLoadMore(false);
						
							try {
								lvCurrent.addHeaderView(nowanchengorders);
								lvCurrent.setAdapter(Currentadapter);
							} catch (Exception e) {
								ToastUtil.makeShortText(MainOrdersFragment.this, "暂无数据");
							}
						}

					}

					Currentadapter.refreshMYData(CurrentData);
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMylistDangqian(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.WanchengB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						TrainingData.clear();
						logoFillOrders = 2;
					} else {
						logoFillOrders++;
					}

					bundle = ParserUtil.ParserMylistDangqian(data);
					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

						TrainingData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
						lvFillOrders.setCanLoadMore(true);// 打开加载更多
						lvFillOrders.removeHeaderView(nowanchengorders);
					} else {
						if (!loadedtype) {
							lvFillOrders.onLoadMorNodata();
						} else {
							lvFillOrders.setCanLoadMore(false);
						
							try {
								lvFillOrders.addHeaderView(nowanchengorders);
								lvFillOrders.setAdapter(FillOrdersadapter);
							} catch (Exception e) {
								ToastUtil.makeShortText(MainOrdersFragment.this, "暂无数据");
							}
						}
					}

					FillOrdersadapter.refreshMYData(TrainingData);

					lvFillOrders.onLoadMoreComplete();
					lvFillOrders.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMylistDangqian(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());
					lvFillOrders.onLoadMoreComplete();
					lvFillOrders.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvFillOrders.onLoadMoreComplete();
					lvFillOrders.onRefreshComplete();
					break;
				}
				break;
			case HttpStaticApi.arrive_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.PingjiaB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						ReviewsData.clear();
						logoreviews = 2;
					} else {
						logoreviews++;
					}

					bundle = ParserUtil.ParserMylistDangqian(data);
					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

						ReviewsData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
						lvreviews.setCanLoadMore(true);// 打开加载更多
						lvreviews.removeHeaderView(nowanchengorders);
					} else {

						if (!loadedtype) {
							lvreviews.onLoadMorNodata();
						} else {
							lvreviews.setCanLoadMore(false);
					
							try {
								lvreviews.addHeaderView(nowanchengorders);
								lvreviews.setAdapter(reviewsAdapter);
							} catch (Exception e) {
								ToastUtil.makeShortText(MainOrdersFragment.this, "暂无数据");
							}
						}

					}

					reviewsAdapter.refreshMYData(ReviewsData);

					lvreviews.onLoadMoreComplete();
					lvreviews.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMylistDangqian(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());
					lvreviews.onLoadMoreComplete();
					lvreviews.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvreviews.onLoadMoreComplete();
					lvreviews.onRefreshComplete();
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

					if ("1".equals(application.getReviewHotel())) {
						lvCurrent.setCanRefresh(true);
						lvFillOrders.setCanRefresh(true);
						lvreviews.setCanRefresh(true);
						lvCurrent.removeHeaderView(nobangdingqiye);
						lvFillOrders.removeHeaderView(nobangdingqiye);
						lvreviews.removeHeaderView(nobangdingqiye);
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

			case HttpStaticApi.cancel_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());
					// refreshOrders();
					//// Intent mIntent = new Intent();
					//// mIntent.putExtra("isquxiao", "y");
					//// setResult(1, mIntent);
					// finish();

					logoreviews = 1;
					logoCurrent = 1;
					logoFillOrders = 1;
					getMylistDandqian(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "1",
							logoCurrent, true);
					getMylistWancheng(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "2",
							logoFillOrders, true);
					getMylistPingjia(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "3",
							logoreviews, true);

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MainOrdersFragment.this, bundle.get("msg").toString());

					// Intent mIntents = new Intent();
					// mIntents.putExtra("isquxiao", "n");
					// setResult(1, mIntents);
					// finish();
					break;

				default:
					break;
				}
				break;

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_orders);
		// setTitle("好帮手", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
		// TITLE_TYPE_TEXT, "注册");
		initView();
		releaseBroadcastReceiver();

	}

	View nowanchengorders, nolog, nobangdingqiye;
	TextView tv_nolog_log, tv_nolog_bangding;

	private void initView() {
		tvCurrent = (TextView) findViewById(R.id.tv_route_tab_invite);
		tvFinish = (TextView) findViewById(R.id.tv_route_tab_apply);
		tvPingjia = (TextView) findViewById(R.id.tv_route_tab_pingjia);

		tvCurrent.setOnClickListener(this);
		tvFinish.setOnClickListener(this);
		tvPingjia.setOnClickListener(this);
		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ivLine = (ImageView) findViewById(R.id.iv_line);

		LayoutInflater inflater = getLayoutInflater();
		// 设置滑动线的宽度
		android.view.ViewGroup.LayoutParams layoutParams = ivLine.getLayoutParams();
		LENGTH = d.getWidth() / 3;
		layoutParams.width = LENGTH;
		ivLine.setLayoutParams(layoutParams);
		int h = d.getHeight();
		int px173 = getResources().getDimensionPixelSize(R.dimen.dd_dimen_269px)
				+ getStatusHeight(MainOrdersFragment.this);//
		// 没有登录
		nolog = inflater.inflate(R.layout.layout_nolog, null);
		RelativeLayout rl_nologo_1 = (RelativeLayout) nolog.findViewById(R.id.rl_nologo_1);
		tv_nolog_log = (TextView) nolog.findViewById(R.id.tv_nolog_log);
		tv_nolog_log.setOnClickListener(this);
		rl_nologo_1
				.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, h - px173));
		// 没有的订单
		nowanchengorders = inflater.inflate(R.layout.layout_nowanchengorders, null);
		RelativeLayout rl_nologo_2 = (RelativeLayout) nowanchengorders.findViewById(R.id.rl_nologo_3);
		rl_nologo_2
				.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, h - px173));

		// 没有绑定企业
		nobangdingqiye = inflater.inflate(R.layout.layout_bangdingqiye, null);
		RelativeLayout rl_nologo_3 = (RelativeLayout) nobangdingqiye.findViewById(R.id.rl_nologo_1);

		tv_nolog_bangding = (TextView) rl_nologo_3.findViewById(R.id.tv_nolog_bangding);
		tv_nolog_bangding.setOnClickListener(this);

		rl_nologo_3
				.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, h - px173));

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCurrent = new CustomListView(MainOrdersFragment.this);
		lvCurrent.setFadingEdgeLength(0);
		lvCurrent.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCurrent.setDividerHeight(Utils.dip2px(MainOrdersFragment.this, 0));
		lvCurrent.setFooterDividersEnabled(false);
		lvCurrent.setCanRefresh(true);// 关闭下拉刷新
		lvCurrent.setCanLoadMore(false);// 打开加载更多
		Currentadapter = new CurrentAdapter(MainOrdersFragment.this);
		Currentadapter.setOnTextClickListener(this, 0);
		Currentadapter.setOnCancelOrderClickListener(this, 0);
		Currentadapter.setOnRouteClickListener(this, 0);
		Currentadapter.setOnZJClickListener(this, 0);
		// Currentadapter.setOnCancelOrderClickListener(this, 0);
		// Currentadapter.setOnRouteClickListener(this, 0);

		// Currentadapter.setOnTextClickListener(this, 0);

		lvCurrent.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getMylistDandqian(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "1",
						logoCurrent, false);
			}
		});

		lvCurrent.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvCurrent.removeHeaderView(nowanchengorders);
				logoCurrent = 1;
				getMylistDandqian(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "1",
						logoCurrent, true);

			}
		});

		lvFillOrders = new CustomListView(MainOrdersFragment.this);
		lvFillOrders.setFadingEdgeLength(0);
		lvFillOrders.setDivider(getResources().getDrawable(R.color.common_grey));
		lvFillOrders.setDividerHeight(Utils.dip2px(MainOrdersFragment.this, 0));
		lvFillOrders.setFooterDividersEnabled(false);
		lvFillOrders.setCanRefresh(true);// 关闭下拉刷新
		lvFillOrders.setCanLoadMore(false);// 打开加载更多

		FillOrdersadapter = new FillOrdersAdapter(MainOrdersFragment.this);
		FillOrdersadapter.setOnZFClickListener(this, 0);
		lvFillOrders.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				getMylistWancheng(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "2",
						logoFillOrders, false);
			}
		});

		lvFillOrders.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvFillOrders.removeHeaderView(nowanchengorders);
				logoFillOrders = 1;
				getMylistWancheng(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "2",
						logoFillOrders, true);
			}
		});

		lvreviews = new CustomListView(MainOrdersFragment.this);
		lvreviews.setFadingEdgeLength(0);
		lvreviews.setDivider(getResources().getDrawable(R.color.common_grey));
		lvreviews.setDividerHeight(Utils.dip2px(MainOrdersFragment.this, 0));
		lvreviews.setFooterDividersEnabled(false);
		lvreviews.setCanRefresh(true);// 关闭下拉刷新
		lvreviews.setCanLoadMore(false);// 打开加载更多

		reviewsAdapter = new ReviewsAdapter(MainOrdersFragment.this);
		reviewsAdapter.setOnTelClickListener(this, 0);

		lvreviews.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getMylistPingjia(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "3",
						logoreviews, false);
			}
		});

		lvreviews.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvreviews.removeHeaderView(nowanchengorders);
				logoreviews = 1;
				getMylistPingjia(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "3",
						logoreviews, true);

			}
		});

		if (!application.isLogon()) {
			lvCurrent.setCanRefresh(false);
			lvFillOrders.setCanRefresh(false);
			lvreviews.setCanRefresh(false);

			lvCurrent.addHeaderView(nolog);
			lvFillOrders.addHeaderView(nolog);
			lvreviews.addHeaderView(nolog);
		} else {

			if ("1".equals(application.getReviewHotel())) {
				logoreviews = 1;
				logoCurrent = 1;
				logoFillOrders = 1;
				getMylistDandqian(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "1",
						logoCurrent, true);
				getMylistWancheng(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "2",
						logoFillOrders, true);
				getMylistPingjia(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "3",
						logoreviews, true);

			} else {

				lvCurrent.setCanRefresh(false);
				lvFillOrders.setCanRefresh(false);
				lvreviews.setCanRefresh(false);

				lvCurrent.addHeaderView(nobangdingqiye);
				lvFillOrders.addHeaderView(nobangdingqiye);
				lvreviews.addHeaderView(nobangdingqiye);

			}

		}

		lvCurrent.setAdapter(Currentadapter);
		lvFillOrders.setAdapter(FillOrdersadapter);
		lvreviews.setAdapter(reviewsAdapter);

		listviews.add(lvCurrent);
		listviews.add(lvFillOrders);
		listviews.add(lvreviews);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(listviews);
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOnPageChangeListener(new ConsultingViewpagerPort());
		switchTabText(TYPE_TAB_Current);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (application.isLogon()) {
			if ("1".equals(application.getReviewHotel())) {
				lvCurrent.setCanRefresh(true);
				lvFillOrders.setCanRefresh(true);
				lvreviews.setCanRefresh(true);
				lvCurrent.removeHeaderView(nobangdingqiye);
				lvFillOrders.removeHeaderView(nobangdingqiye);
				lvreviews.removeHeaderView(nobangdingqiye);
				myhotel(UrlConfig.myhotelB_Http, application.getToken(), application.getUserId(), true);
			} else {
				lvCurrent.setCanRefresh(false);
				lvFillOrders.setCanRefresh(false);
				lvreviews.setCanRefresh(false);
				lvCurrent.removeHeaderView(nobangdingqiye);
				lvFillOrders.removeHeaderView(nobangdingqiye);
				lvreviews.removeHeaderView(nobangdingqiye);
				lvCurrent.addHeaderView(nobangdingqiye);
				lvFillOrders.addHeaderView(nobangdingqiye);
				lvreviews.addHeaderView(nobangdingqiye);
			}
		} else {
		}

	}

	public int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				statusHeight = activity.getResources().getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	/**
	 * 切换标签
	 * 
	 * @param type
	 */

	private void switchTabText(int type) {
		switch (type) {
		case TYPE_TAB_Current:
			tvCurrent.setEnabled(false);
			tvFinish.setEnabled(true);
			tvPingjia.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition, LENGTH * 0));
			currentIndicatorPosition = LENGTH * 0;

			tvCurrent.setTextColor(this.getResources().getColor(R.color.common_yellow));
			tvFinish.setTextColor(this.getResources().getColor(R.color.common_666666));
			tvPingjia.setTextColor(this.getResources().getColor(R.color.common_666666));
			break;
		case TYPE_TAB_Finish:
			tvCurrent.setEnabled(true);
			tvFinish.setEnabled(false);
			tvPingjia.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition, LENGTH * 1));
			currentIndicatorPosition = LENGTH * 1;
			tvCurrent.setTextColor(this.getResources().getColor(R.color.common_666666));
			tvFinish.setTextColor(this.getResources().getColor(R.color.common_yellow));
			tvPingjia.setTextColor(this.getResources().getColor(R.color.common_666666));
			break;

		case TYPE_TAB_pingjia:
			tvPingjia.setEnabled(false);
			tvCurrent.setEnabled(true);
			tvFinish.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition, LENGTH * 2));
			currentIndicatorPosition = LENGTH * 2;
			tvCurrent.setTextColor(this.getResources().getColor(R.color.common_666666));
			tvFinish.setTextColor(this.getResources().getColor(R.color.common_666666));
			tvPingjia.setTextColor(this.getResources().getColor(R.color.common_yellow));
			break;

		}
	}

	/**
	 * 获取一个平移的动画
	 */
	private Animation getAnimation(float fromXValue, float toXValue) {
		Animation animation = new TranslateAnimation(fromXValue, toXValue, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(200);
		return animation;
	}

	class ConsultingViewpagerPort implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			if (!isManul) {
				switchTabText(arg0);
			} else {
				isManul = false;
			}

		}
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.tv_route_tab_invite:// 新闻
			isManul = true;
			switchTabText(TYPE_TAB_Current);
			break;
		case R.id.tv_route_tab_apply:// 培训
			isManul = true;
			switchTabText(TYPE_TAB_Finish);
			break;

		case R.id.tv_route_tab_pingjia:// 培训
			isManul = true;
			switchTabText(TYPE_TAB_pingjia);
			break;
		case R.id.tv_nolog_log:// 登录
			startActivity(new Intent(MainOrdersFragment.this, LoginActivity.class));
			finish();

			break;
		case R.id.tv_nolog_bangding:// 绑定企业
			intent = new Intent(MainOrdersFragment.this, TheirProfileActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 当前订单
	 */
	private void getMylistDandqian(String url, String token, String userid, String type, int page, boolean loadedtype) {

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("type", type);
		map.put("page", page + "");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this), HttpStaticApi.DandqianB_Http, null,
				loadedtype);
	}

	/**
	 * 完成订单
	 */
	private void getMylistWancheng(String url, String token, String userid, String type, int page, boolean loadedtype) {

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("type", type);
		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this), HttpStaticApi.WanchengB_Http, null,
				loadedtype);
	}

	/**
	 * pingjia
	 */
	private void getMylistPingjia(String url, String token, String userid, String type, int page, boolean loadedtype) {

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("type", type);
		map.put("page", page + "");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this), HttpStaticApi.PingjiaB_Http, null,
				loadedtype);
	}

	// /**
	// * 完成订单
	// */
	// private void getTraining(String url, String token, String userid, int
	// page,
	// boolean loadedtype) {
	// url = url + "?apptype=2&token=" + token + "&userid=" + userid
	// + "&page=" + page + "&type=2";
	// AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
	// MainOrdersFragment.this, url, null, callBack,
	// GlobalVariables.getRequestQueue(MainOrdersFragment.this),
	// HttpStaticApi.Training_Http, null, loadedtype);
	//
	// }
	private void myhotel(String url, String token, String userid, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this), HttpStaticApi.myhotelB_Http, null,
				loadedtype);
	}

	/**
	 * 确认到达接口
	 */
	private void getArrive(String url, String token, String userid, String orderdetailid, boolean loadedtype) {

		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderdetailid", orderdetailid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this), HttpStaticApi.arrive_Http, null, loadedtype);

	}

	/***
	 * 去评价
	 */
	@Override
	public void onTelClick(int position, View v, int logo) {
		Intent intent = new Intent(MainOrdersFragment.this, ReviewsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("bangkelist", ReviewsData.get(position).get("bangkelist"));
		bundle.putString("id", ReviewsData.get(position).get("id"));
		intent.putExtras(bundle);
		startActivity(intent);

	}

	/**
	 * 查看到岗
	 */
	@Override
	public void onTextClick(int position, View v, int logo) {
		Intent intent = new Intent(MainOrdersFragment.this, LoogPostActivitys.class);
		Bundle bundle = new Bundle();
		bundle.putString("orderid", CurrentData.get(position).get("id"));
		intent.putExtras(bundle);
		startActivity(intent);

	}

	/**
	 * 验收通过
	 */

	@Override
	public void onCancelOrderClick(final int position, View v, int logo) {
		Intent intent = new Intent(MainOrdersFragment.this, OrdersConfirmedActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("bangkelist", CurrentData.get(position).get("bangkelist"));
		bundle.putString("standard_count", CurrentData.get(position).get("standard_count"));// 标间数量
		bundle.putString("suite_count", CurrentData.get(position).get("suite_count"));// 套间间数量
		bundle.putString("id", CurrentData.get(position).get("id"));
		bundle.putString("status", CurrentData.get(position).get("status"));
		bundle.putString("standard_count", CurrentData.get(position).get("standard_count"));
		bundle.putString("suite_count", CurrentData.get(position).get("suite_count"));
		bundle.putString("bounus", CurrentData.get(position).get("bounus"));
		bundle.putString("orderno", CurrentData.get(position).get("orderno"));

		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 取消订单
	 */
	@Override
	public void onRouteClick(final int position, View v, int logo) {

		if ("1".equals(CurrentData.get(position).get("status"))) {
			ToastUtil.makeShortText(MainOrdersFragment.this, "对方已确认订单信息,不能取消订单");
		} else {
			// Intent intent = new Intent(MainOrdersFragment.this,
			// CancelReasonActivity.class);
			// intent.putExtra("orderdetailid",
			// CurrentData.get(position).get("id"));
			// startActivity(intent);

			CustomDialog.alertDialog(MainOrdersFragment.this, false, true, true, null, "您确定要取消此订单吗？",
					new CustomDialog.PopUpDialogListener() {

						@Override
						public void doPositiveClick(Boolean isOk) {
							if (isOk) {// 确定
								cancel(UrlConfig.cancelB_Http, application.getToken(), application.getUserId(),
										CurrentData.get(position).get("id"), "", true);

							} else {

							}

						}
					});

		}
	}

	/**
	 * 追加价格
	 */
	@Override
	public void onZJClick(int position, View v, int logo) {
		Intent intent = new Intent(MainOrdersFragment.this, AdditionalPriceActivity.class);
		intent.putExtra("orderid", CurrentData.get(position).get("id"));
		intent.putExtra("bounus", CurrentData.get(position).get("bounus"));
		intent.putExtra("standard_price", CurrentData.get(position).get("standard_price"));
		intent.putExtra("suite_price", CurrentData.get(position).get("suite_price"));
		intent.putExtra("standard_count", CurrentData.get(position).get("standard_count"));
		intent.putExtra("suite_count", CurrentData.get(position).get("suite_count"));
		intent.putExtra("workercount", CurrentData.get(position).get("workercount"));

		startActivity(intent);

	}

	/**
	 * 完成中再次评价(详情)
	 */
	@Override
	public void onZFClick(int position, View v, int logo) {
//		Intent intent = new Intent(MainOrdersFragment.this, ReviewsActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("bangkelist", TrainingData.get(position).get("bangkelist"));
//		bundle.putString("id", TrainingData.get(position).get("id"));
//		intent.putExtras(bundle);
//		startActivity(intent);

		
		
		
		
		
		
		
		Intent intent = new Intent(MainOrdersFragment.this, OrderDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", TrainingData.get(position).get("id"));
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	OBDBroadcastReceiver recobdlist;

	private void releaseBroadcastReceiver() {
		recobdlist = new OBDBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cgzz.job.accesstype");
		registerReceiver(recobdlist, filter);
	}

	private class OBDBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String TYPE = bundle.getString("TYPE");
			if ("refreshOrders".equals(TYPE)) {
				String refresh = bundle.getString("refresh");
				// viewpager.setCurrentItem(item);
				if ("1".equals(refresh)) {
					logoreviews = 1;
					logoCurrent = 1;
					logoFillOrders = 1;
					getMylistDandqian(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "1",
							logoCurrent, true);
					getMylistWancheng(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "2",
							logoFillOrders, true);
					getMylistPingjia(UrlConfig.orderbMylistB, application.getToken(), application.getUserId(), "3",
							logoreviews, true);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (recobdlist != null)
			unregisterReceiver(recobdlist);

		super.onDestroy();
	}

	private void cancel(String url, String token, String userid, String orderdetailid, String cancelid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderdetailid);
		map.put("cancelid", cancelid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainOrdersFragment.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MainOrdersFragment.this), HttpStaticApi.cancel_Http, null, loadedtype);
	}

}
