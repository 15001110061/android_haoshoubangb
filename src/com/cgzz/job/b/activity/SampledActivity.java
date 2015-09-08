package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.FillOrdersAdapter;
import com.cgzz.job.b.adapter.SampledAlreadyAdapter;
import com.cgzz.job.b.adapter.SampledWaitingAdapter;
import com.cgzz.job.b.adapter.SampledWaitingAdapter.OnCancelOrderClickListener;
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

/***
 * @author wjm ��ҳ�����շ���
 */
public class SampledActivity extends BaseActivity implements OnClickListener,
		OnCancelOrderClickListener {
	private TextView tvCurrent, tvFinish;
	private ArrayList<Map<String, String>> shufflingList;
	private boolean isManul;
	private ViewPager viewpager;
	private ImageView ivLine;
	private int LENGTH;
	private CustomListView lvCurrent, lvFillOrders;
	private SampledWaitingAdapter sampledWaitingAdapter;
	private SampledAlreadyAdapter sampledAlreadyAdapter;

	public static final int TYPE_TAB_Current = 0;
	public static final int TYPE_TAB_Finish = 1;
	private int currentIndicatorPosition;
	private int logoCurrent = 1;// ҳ���ʶ
	private int logoFillOrders = 1;// ҳ���ʶ
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.ChecklistDaiB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						CurrentData.clear();
						logoCurrent = 2;
					} else {
						logoCurrent++;
					}

					bundle = ParserUtil.ParserMylistDangqian(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						lvCurrent.setCanLoadMore(true);// �򿪼��ظ���
						lvCurrent.removeHeaderView(nowanchengorders);
					} else {

						if (!loadedtype) {
							lvCurrent.onLoadMorNodata();
						} else {
							lvCurrent.setCanLoadMore(false);
							lvCurrent.addHeaderView(nowanchengorders);
							lvCurrent.setAdapter(sampledWaitingAdapter);
						}

					}

					sampledWaitingAdapter.refreshMYData(CurrentData);
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMylistDangqian(data);
					ToastUtil.makeShortText(SampledActivity.this,
							bundle.get("msg").toString());
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCurrent.onLoadMoreComplete();
					lvCurrent.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.ChecklistYiB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						TrainingData.clear();
						logoFillOrders = 2;
					} else {
						logoFillOrders++;
					}

					bundle = ParserUtil.ParserMylistDangqian(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						TrainingData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						lvFillOrders.setCanLoadMore(true);// �򿪼��ظ���
						lvFillOrders.removeHeaderView(nowanchengorders);
					} else {
						if (!loadedtype) {
							lvFillOrders.onLoadMorNodata();
						} else {
							lvFillOrders.setCanLoadMore(false);
							lvFillOrders.addHeaderView(nowanchengorders);
							lvFillOrders.setAdapter(sampledAlreadyAdapter);
						}
					}

					sampledAlreadyAdapter.refreshMYData(TrainingData);

					lvFillOrders.onLoadMoreComplete();
					lvFillOrders.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMylistDangqian(data);
					ToastUtil.makeShortText(SampledActivity.this,
							bundle.get("msg").toString());
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
					ToastUtil.makeShortText(SampledActivity.this,
							bundle.get("msg").toString());
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SampledActivity.this,
							bundle.get("msg").toString());
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
		setContentView(R.layout.sampled_activity);
		setTitle("���շ���", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		initView();
		releaseBroadcastReceiver();

	}

	View nowanchengorders, nolog;
	TextView tv_nolog_log;
	LinearLayout llLeft, llright;

	private void initView() {
		tvCurrent = (TextView) findViewById(R.id.tv_route_tab_invite);
		tvFinish = (TextView) findViewById(R.id.tv_route_tab_apply);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		tvCurrent.setOnClickListener(this);
		tvFinish.setOnClickListener(this);
		// �·��б�
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ivLine = (ImageView) findViewById(R.id.iv_line);

		LayoutInflater inflater = getLayoutInflater();
		// ���û����ߵĿ��
		android.view.ViewGroup.LayoutParams layoutParams = ivLine
				.getLayoutParams();
		LENGTH = d.getWidth() / 2;
		layoutParams.width = LENGTH;
		ivLine.setLayoutParams(layoutParams);
		int h = d.getHeight();
		int px173 = getResources()
				.getDimensionPixelSize(R.dimen.dd_dimen_269px)
				+ getStatusHeight(SampledActivity.this);//
		// û�е�¼
		nolog = inflater.inflate(R.layout.layout_nolog, null);
		RelativeLayout rl_nologo_1 = (RelativeLayout) nolog
				.findViewById(R.id.rl_nologo_1);
		tv_nolog_log = (TextView) nolog.findViewById(R.id.tv_nolog_log);
		tv_nolog_log.setOnClickListener(this);
		rl_nologo_1.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - px173));
		// û�еĶ���
		nowanchengorders = inflater.inflate(R.layout.layout_nowanchengorders,
				null);
		RelativeLayout rl_nologo_2 = (RelativeLayout) nowanchengorders
				.findViewById(R.id.rl_nologo_3);
		rl_nologo_2.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - px173));

		// ���listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCurrent = new CustomListView(SampledActivity.this);
		lvCurrent.setFadingEdgeLength(0);
		lvCurrent.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCurrent.setDividerHeight(Utils.dip2px(SampledActivity.this, 0));
		lvCurrent.setFooterDividersEnabled(false);
		lvCurrent.setCanRefresh(true);// �ر�����ˢ��
		lvCurrent.setCanLoadMore(false);// �򿪼��ظ���
		sampledWaitingAdapter = new SampledWaitingAdapter(SampledActivity.this);
		sampledWaitingAdapter.setOnCancelOrderClickListener(this, 0);

		lvCurrent.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getChecklistDai(UrlConfig.checklistB_Http,
						application.getToken(), application.getUserId(), "1",
						logoCurrent, false);
			}
		});

		lvCurrent.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvCurrent.removeHeaderView(nowanchengorders);
				logoCurrent = 1;
				getChecklistDai(UrlConfig.checklistB_Http,
						application.getToken(), application.getUserId(), "1",
						logoCurrent, true);

			}
		});

		lvFillOrders = new CustomListView(SampledActivity.this);
		lvFillOrders.setFadingEdgeLength(0);
		lvFillOrders
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvFillOrders.setDividerHeight(Utils.dip2px(SampledActivity.this, 0));
		lvFillOrders.setFooterDividersEnabled(false);
		lvFillOrders.setCanRefresh(true);// �ر�����ˢ��
		lvFillOrders.setCanLoadMore(false);// �򿪼��ظ���

		sampledAlreadyAdapter = new SampledAlreadyAdapter(SampledActivity.this);

		lvFillOrders.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				getChecklistYi(UrlConfig.checklistB_Http,
						application.getToken(), application.getUserId(), "2",
						logoFillOrders, false);
			}
		});

		lvFillOrders.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvFillOrders.removeHeaderView(nowanchengorders);
				logoFillOrders = 1;
				getChecklistYi(UrlConfig.checklistB_Http,
						application.getToken(), application.getUserId(), "2",
						logoFillOrders, true);
			}
		});

		if (!application.isLogon()) {
			lvCurrent.setCanRefresh(false);
			lvFillOrders.setCanRefresh(false);

			lvCurrent.addHeaderView(nolog);
			lvFillOrders.addHeaderView(nolog);
		} else {

			logoCurrent = 1;
			logoFillOrders = 1;
			getChecklistDai(UrlConfig.checklistB_Http, application.getToken(),
					application.getUserId(), "1", logoCurrent, true);
			getChecklistYi(UrlConfig.checklistB_Http, application.getToken(),
					application.getUserId(), "2", logoFillOrders, true);
		}

		lvCurrent.setAdapter(sampledWaitingAdapter);
		lvFillOrders.setAdapter(sampledAlreadyAdapter);

		listviews.add(lvCurrent);
		listviews.add(lvFillOrders);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOnPageChangeListener(new ConsultingViewpagerPort());
		switchTabText(TYPE_TAB_Current);

	}

	public int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
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
	 * �л���ǩ
	 * 
	 * @param type
	 */

	private void switchTabText(int type) {
		switch (type) {
		case TYPE_TAB_Current:
			tvCurrent.setEnabled(false);
			tvFinish.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 0));
			currentIndicatorPosition = LENGTH * 0;

			tvCurrent.setTextColor(this.getResources().getColor(
					R.color.common_yellow));
			tvFinish.setTextColor(this.getResources().getColor(
					R.color.common_666666));
			break;
		case TYPE_TAB_Finish:
			tvCurrent.setEnabled(true);
			tvFinish.setEnabled(false);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 1));
			currentIndicatorPosition = LENGTH * 1;
			tvCurrent.setTextColor(this.getResources().getColor(
					R.color.common_666666));
			tvFinish.setTextColor(this.getResources().getColor(
					R.color.common_yellow));
			break;

		}
	}

	/**
	 * ��ȡһ��ƽ�ƵĶ���
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
		switch (arg0.getId()) {
		case R.id.tv_route_tab_invite:// ����
			isManul = true;
			switchTabText(TYPE_TAB_Current);
			break;
		case R.id.tv_route_tab_apply:// ��ѵ
			isManul = true;
			switchTabText(TYPE_TAB_Finish);
			break;

		case R.id.tv_nolog_log:// ��¼
			startActivity(new Intent(SampledActivity.this, LoginActivity.class));
			finish();
			break;
		case R.id.ll_title_left:// ����
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * ������
	 */
	private void getChecklistDai(String url, String token, String userid,
			String type, int page, boolean loadedtype) {

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("type", type);
		map.put("page", page + "");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				SampledActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(SampledActivity.this),
				HttpStaticApi.ChecklistDaiB_Http, null, loadedtype);
	}

	/**
	 * ������
	 */
	private void getChecklistYi(String url, String token, String userid,
			String type, int page, boolean loadedtype) {

		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("type", type);
		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				SampledActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(SampledActivity.this),
				HttpStaticApi.ChecklistYiB_Http, null, loadedtype);
	}

	/**
	 * ����ͨ��
	 */

	@Override
	public void onCancelOrderClick(final int position, View v, int logo) {
		Intent intent = new Intent(SampledActivity.this,
				OrdersConfirmedActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("bangkelist",
				CurrentData.get(position).get("bangkelist"));
		bundle.putString("standard_count",
				CurrentData.get(position).get("standard_count"));// �������
		bundle.putString("suite_count",
				CurrentData.get(position).get("suite_count"));// �׼������
		bundle.putString("id", CurrentData.get(position).get("id"));
		bundle.putString("status", CurrentData.get(position).get("status"));
		bundle.putString("standard_count",
				CurrentData.get(position).get("standard_count"));
		bundle.putString("suite_count",
				CurrentData.get(position).get("suite_count"));
		bundle.putString("bounus", CurrentData.get(position).get("bounus"));
		bundle.putString("orderno", CurrentData.get(position).get("orderno"));

		intent.putExtras(bundle);

		startActivity(intent);
		// int s = Integer.parseInt(CurrentData.get(position)
		// .get("standard_count"))
		// + Integer
		// .parseInt(CurrentData.get(position).get("suite_count"));
		// String html = "��ȷ������<font color=\"#FF971E\">" + s +
		// "</font>�䷿������ͨ����";
		//
		// CustomDialog.alertDialog(SampledActivity.this, false, true, true,
		// null,
		// html, true, new CustomDialog.PopUpDialogListener() {
		//
		// @Override
		// public void doPositiveClick(Boolean isOk) {
		// if (isOk) {// ȷ��
		// Intent intent = new Intent(SampledActivity.this,
		// OrdersConfirmedActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putString("bangkelist",
		// CurrentData.get(position).get("bangkelist"));
		// bundle.putString(
		// "standard_count",
		// CurrentData.get(position).get(
		// "standard_count"));// �������
		// bundle.putString("suite_count",
		// CurrentData.get(position)
		// .get("suite_count"));// �׼������
		// bundle.putString("id", CurrentData.get(position)
		// .get("id"));
		// intent.putExtras(bundle);
		// startActivity(intent);
		// } else {
		//
		// }
		//
		// }
		// });
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
			if ("refreshSampled".equals(TYPE)) {
				String refresh = bundle.getString("refresh");
				// viewpager.setCurrentItem(item);
				if ("1".equals(refresh)) {
					logoCurrent = 1;
					logoFillOrders = 1;
					getChecklistDai(UrlConfig.checklistB_Http,
							application.getToken(), application.getUserId(),
							"1", logoCurrent, true);
					getChecklistYi(UrlConfig.checklistB_Http,
							application.getToken(), application.getUserId(),
							"2", logoFillOrders, true);
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

}
