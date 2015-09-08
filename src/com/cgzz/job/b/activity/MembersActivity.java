package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.MembersAdapter;
import com.cgzz.job.b.adapter.MembersAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.MembersAdapter.OnTextClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.view.CustomListView.OnRefreshListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * @author wjm 成员管理
 */
public class MembersActivity extends BaseActivity
		implements OnClickListener, OnItemClickListener, OnTelClickListener, OnTextClickListener {
	private TextView tvNews, tvTraining;
	private ImageLoader mImageLoader;
	private ArrayList<ImageView> mViews;
	private ArrayList<Map<String, String>> shufflingList;
	private boolean isRunThread = true;
	private boolean mark = true;
	private int currentIndex;
	private boolean isManul;
	private ViewPager viewpager;
	private ImageView ivLine;
	private int LENGTH;
	private CustomListView lvNews, lvTraining;
	private MembersAdapter membersAdapter, membersAdapter2;
	public static final int TYPE_TAB_INVITE = 0;
	public static final int TYPE_TAB_APPLY = 1;
	public static final int TYPE_TAB_BEING = 2;
	public static final int TYPE_TAB_DONE = 3;
	private int currentIndicatorPosition;
	// private int logoCONDUCT = 1;// 页码标识
	// private int logoTraining = 1;// 页码标识
	private ArrayList<Map<String, String>> NewsData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> fromPassengerMaps;
	private ObserverCallBack callbackData = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {

			case HttpStaticApi.member1B_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					NewsData.clear();
					bundle = ParserUtil.ParserMember(data);

					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

						NewsData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
					} else {
						ToastUtil.makeShortText(MembersActivity.this, "暂无成员管理");
					}
					membersAdapter.refreshMYData(NewsData);
					lvNews.onLoadMoreComplete();
					lvNews.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvNews.onLoadMoreComplete();
					lvNews.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.member2B_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					TrainingData.clear();

					bundle = ParserUtil.ParserMember(data);

					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

						TrainingData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
					} else {

					}
					membersAdapter2.refreshMYData(TrainingData);
					lvTraining.onLoadMoreComplete();
					lvTraining.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvTraining.onLoadMoreComplete();
					lvTraining.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.updatememberB_Http:// 忽略
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersActivity.this, bundle.get("msg").toString());
					member1(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
					member2(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersActivity.this, bundle.get("msg").toString());
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
		setContentView(R.layout.activity_members);
		setTitle("成员管理", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "添加");
		initView();
		releaseBroadcastReceiver();
	}

	LinearLayout llLeft, llright;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tvNews = (TextView) findViewById(R.id.tv_route_tab_invite);
		tvTraining = (TextView) findViewById(R.id.tv_route_tab_apply);
		tvNews.setOnClickListener(this);
		tvTraining.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ivLine = (ImageView) findViewById(R.id.iv_line);
		// 设置滑动线的宽度
		android.view.ViewGroup.LayoutParams layoutParams = ivLine.getLayoutParams();
		LENGTH = d.getWidth() / 2;
		layoutParams.width = LENGTH;
		ivLine.setLayoutParams(layoutParams);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvNews = new CustomListView(MembersActivity.this);
		lvNews.setFadingEdgeLength(0);
		lvNews.setDivider(getResources().getDrawable(R.color.common_grey));
		lvNews.setDividerHeight(Utils.dip2px(MembersActivity.this, 0));
		lvNews.setFooterDividersEnabled(false);
		lvNews.setCanRefresh(true);// 关闭下拉刷新
		lvNews.setCanLoadMore(false);// 打开加载更多
		membersAdapter = new MembersAdapter(MembersActivity.this);
		membersAdapter.setOnTelClickListener(this, 0);
		membersAdapter.setOnTextClickListener(this, 0);
		lvNews.setOnItemClickListener(this);
		lvNews.setAdapter(membersAdapter);
		// lvNews.setOnItemClickListener(this);
		// lvNews.setOnLoadListener(new OnLoadMoreListener() {
		//
		// @Override
		// public void onLoadMore() {
		//
		// member1(UrlConfig.memberB_Http, application.getToken(),
		// application.getUserId(), true);
		// }
		// });

		lvNews.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				member1(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);

			}
		});

		lvTraining = new CustomListView(MembersActivity.this);
		lvTraining.setFadingEdgeLength(0);
		lvTraining.setDivider(getResources().getDrawable(R.color.common_grey));
		lvTraining.setDividerHeight(Utils.dip2px(MembersActivity.this, 0));
		lvTraining.setFooterDividersEnabled(false);
		lvTraining.setCanRefresh(true);// 关闭下拉刷新
		lvTraining.setCanLoadMore(false);// 打开加载更多

		membersAdapter2 = new MembersAdapter(MembersActivity.this);
		lvTraining.setOnItemClickListener(this);
		lvTraining.setAdapter(membersAdapter2);
		// lvTraining.setOnItemClickListener(this);
		// lvTraining.setOnLoadListener(new OnLoadMoreListener() {
		//
		// @Override
		// public void onLoadMore() {
		// member2(UrlConfig.memberB_Http, application.getToken(),
		// application.getUserId(), true);
		// }
		// });

		lvTraining.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				member2(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
			}
		});
		member1(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
		member2(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
		listviews.add(lvNews);
		listviews.add(lvTraining);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(listviews);
		viewpager.setAdapter(pagerAdapter);
		viewpager.setOnPageChangeListener(new ConsultingViewpagerPort());
		switchTabText(TYPE_TAB_INVITE);
	}

	/**
	 * 切换标签
	 * 
	 * @param type
	 */

	private void switchTabText(int type) {
		switch (type) {
		case TYPE_TAB_INVITE:
			tvNews.setEnabled(false);
			tvTraining.setEnabled(true);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition, LENGTH * 0));
			currentIndicatorPosition = LENGTH * 0;
			tvNews.setTextColor(this.getResources().getColor(R.color.common_yellow));
			tvTraining.setTextColor(this.getResources().getColor(R.color.common_text));
			break;
		case TYPE_TAB_APPLY:
			tvNews.setEnabled(true);
			tvTraining.setEnabled(false);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition, LENGTH * 1));
			currentIndicatorPosition = LENGTH * 1;
			tvNews.setTextColor(this.getResources().getColor(R.color.common_text));
			tvTraining.setTextColor(this.getResources().getColor(R.color.common_yellow));
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

	public void headViewPagerOnItemOnClick(String title, String url) {
		if (!"".equals(url)) {
			Intent intent = new Intent(MembersActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, title);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
		} else {
		}
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
			switchTabText(TYPE_TAB_INVITE);
			break;
		case R.id.tv_route_tab_apply:// 培训
			isManul = true;
			switchTabText(TYPE_TAB_APPLY);
			break;

		case R.id.ll_title_left://
			finish();
			break;

		case R.id.ll_title_right:// 添加

			intent = new Intent(MembersActivity.this, MembersAddActivity.class);
			// Bundle bundle = new Bundle();
			// intent.putExtras(bundle);

			startActivityForResult(intent, 2);
			break;

		default:
			break;
		}
	}

	private void member1(String url, String token, String userid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("type", "1");

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersActivity.this), HttpStaticApi.member1B_Http, null, loadedtype);
	}

	private void member2(String url, String token, String userid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("type", "2");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersActivity.this), HttpStaticApi.member2B_Http, null, loadedtype);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == lvTraining) {

			Intent intent = new Intent(MembersActivity.this, MembersChangesActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("realname", TrainingData.get(arg2 - 1).get("realname"));
			bundle.putString("mobile", TrainingData.get(arg2 - 1).get("mobile"));
			bundle.putString("department", TrainingData.get(arg2 - 1).get("department"));
			bundle.putString("portrait", TrainingData.get(arg2 - 1).get("portrait"));
			bundle.putString("id", TrainingData.get(arg2 - 1).get("id"));
			bundle.putString("department_value", TrainingData.get(arg2 - 1).get("department_value"));
			bundle.putString("is_order", TrainingData.get(arg2 - 1).get("is_order"));
			bundle.putString("is_check", TrainingData.get(arg2 - 1).get("is_check"));
			bundle.putString("is_select", TrainingData.get(arg2 - 1).get("is_select"));

			bundle.putString("userid", TrainingData.get(arg2 - 1).get("userid"));

			intent.putExtras(bundle);
			// startActivity(intent);

			startActivityForResult(intent, 1);
		} else if (arg0 == lvNews) {
			if ("1".equals(NewsData.get(arg2 - 1).get("is_verify"))) {
				Intent intent = new Intent(MembersActivity.this, MembersChangesActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("realname", NewsData.get(arg2 - 1).get("realname"));
				bundle.putString("mobile", NewsData.get(arg2 - 1).get("mobile"));
				bundle.putString("department", NewsData.get(arg2 - 1).get("department"));
				bundle.putString("department_value", NewsData.get(arg2 - 1).get("department_value"));

				bundle.putString("portrait", NewsData.get(arg2 - 1).get("portrait"));
				bundle.putString("id", NewsData.get(arg2 - 1).get("id"));

				bundle.putString("is_order", NewsData.get(arg2 - 1).get("is_order"));
				bundle.putString("is_check", NewsData.get(arg2 - 1).get("is_check"));
				bundle.putString("is_select", NewsData.get(arg2 - 1).get("is_select"));

				bundle.putString("userid", NewsData.get(arg2 - 1).get("userid"));
				intent.putExtras(bundle);

				startActivityForResult(intent, 1);
			}
		}

	}

	/***
	 * 同意
	 */
	@Override
	public void onTelClick(int position, View v, int logo) {
		Intent intent = new Intent(MembersActivity.this, MembersChangesActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("type", "tongyi");
		bundle.putString("realname", NewsData.get(position).get("realname"));
		bundle.putString("mobile", NewsData.get(position).get("mobile"));
		bundle.putString("department", NewsData.get(position).get("department"));
		bundle.putString("department_value", NewsData.get(position).get("department_value"));

		bundle.putString("portrait", NewsData.get(position).get("portrait"));
		bundle.putString("id", NewsData.get(position).get("id"));

		bundle.putString("is_order", NewsData.get(position).get("is_order"));
		bundle.putString("is_check", NewsData.get(position).get("is_check"));
		bundle.putString("is_select", NewsData.get(position).get("is_select"));

		bundle.putString("userid", NewsData.get(position).get("userid"));
		
		intent.putExtras(bundle);
		// startActivity(intent);

		startActivityForResult(intent, 1);

	}

	/***
	 * 忽略
	 */
	@Override
	public void onTextClick(int position, View v, int logo) {
		// TODO Auto-generated method stub
		updatemember(UrlConfig.updatememberB_Http, application.getToken(), application.getUserId(),
				NewsData.get(position).get("id"), "-1", NewsData.get(position).get("userid"), true);
	}

	/***
	 * 同意后修改成员，忽略（同一个接口）
	 */
	private void updatemember(String url, String token, String userid, String id, String type, String adduserid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("id", id);

		map.put("type", type);
		map.put("adduserid", adduserid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersActivity.this), HttpStaticApi.updatememberB_Http, null,
				loadedtype);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {

			// 根据上面发送过去的请求吗来区别
			switch (requestCode) {
			case 1:
				member1(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
				member2(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
				break;
			case 2:
				member1(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
				member2(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
				break;

			default:
				break;
			}
		}

	}

	private OBDBroadcastReceiver recobdlist;

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
			if ("Members".equals(TYPE)) {
				member1(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
				member2(UrlConfig.memberB_Http, application.getToken(), application.getUserId(), true);
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
