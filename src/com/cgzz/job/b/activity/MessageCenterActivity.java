package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.MessageCenterAdapter;
import com.cgzz.job.b.adapter.MessageCenterAdapter.OnTelClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.b.view.CustomListView.OnRefreshListener;

/***
 * @author wjm 消息中心
 */
public class MessageCenterActivity extends BaseActivity implements
		OnClickListener, OnTelClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private MessageCenterAdapter messageCenterAdapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.messagelistB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					if (loadedtype) {
						CollectionData.clear();
						logoCollection = 2;
					} else {
						logoCollection++;
					}

					bundle = ParserUtil.ParserMessagelistB(data);

					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CollectionData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						rl_home_1.setVisibility(View.VISIBLE);
						rl_home_2.setVisibility(View.GONE);
						lvCollection.setCanLoadMore(true);
					} else {

						if (!loadedtype) {
							lvCollection.onLoadMorNodata();
							// ToastUtil.makeShortText(MessageCenterActivity.this,
							// MessageCenterActivity.this.getResources()
							// .getString(R.string.http_nodata));
						} else {
							lvCollection.setCanLoadMore(false);

							rl_home_1.setVisibility(View.GONE);
							rl_home_2.setVisibility(View.VISIBLE);
						}

					}

					messageCenterAdapter.refreshMYData(CollectionData);
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;
				}
				break;

			}
		}
	};
	LinearLayout llLeft;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		setTitle("消息中心", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		initView();

	}

	RelativeLayout rl_home_1, rl_home_2;
	View nozixun;

	@SuppressLint("NewApi")
	private void initView() {
		rl_home_1 = (RelativeLayout) findViewById(R.id.rl_home_1);
		rl_home_2 = (RelativeLayout) findViewById(R.id.rl_home_2);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		// 下方列表
		// WindowManager m = this.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// ivLine = (ImageView) findViewById(R.id.iv_line);
		// // 设置滑动线的宽度
		// android.view.ViewGroup.LayoutParams layoutParams = ivLine
		// .getLayoutParams();
		// LENGTH = d.getWidth() / 2;
		// layoutParams.width = LENGTH;
		// ivLine.setLayoutParams(layoutParams);

		// WindowManager m = this.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		//
		// LayoutInflater inflater = getLayoutInflater();
		// int h = d.getHeight();
		// nozixun = inflater.inflate(R.layout.layout_nozixun, null);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(MessageCenterActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(MessageCenterActivity.this,
				0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(true);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		messageCenterAdapter = new MessageCenterAdapter(
				MessageCenterActivity.this);
		messageCenterAdapter.setOnTelClickListener(this, 0);
		lvCollection.setAdapter(messageCenterAdapter);

		lvCollection.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getMessage(UrlConfig.messagelistB_Http, logoCollection,
						application.getToken(), application.getUserId(), false);
			}
		});

		lvCollection.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				logoCollection = 1;
				getMessage(UrlConfig.messagelistB_Http, logoCollection,
						application.getToken(), application.getUserId(), true);

			}
		});

		logoCollection = 1;
		getMessage(UrlConfig.messagelistB_Http, logoCollection,
				application.getToken(), application.getUserId(), true);
		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.tv_route_tab_apply://
			break;
		default:
			break;
		}
	}

	/**
	 * 我的消息
	 */
	private void getMessage(String url, int page, String token, String userid,
			boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("page", page + "");

		map.put("token", token);
		map.put("userid", userid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MessageCenterActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MessageCenterActivity.this),
				HttpStaticApi.messagelistB_Http, null, loadedtype);
	}

	@Override
	public void onTelClick(int position, View v, int logo) {
		if (!"".equals(CollectionData.get(position).get("url")))
			headViewPagerOnItemOnClick(CollectionData.get(position)
					.get("title"), CollectionData.get(position).get("url"));

	}

	public void headViewPagerOnItemOnClick(String title, String url) {
		if (!"".equals(url)) {
			Intent intent = new Intent(MessageCenterActivity.this,
					WebBrowserActivity.class);
			String t = "";
			if ("".equals(title)) {
				t = "系统消息";
			} else {
				t = title;
			}
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, t);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
		} else {
		}
	}
}
