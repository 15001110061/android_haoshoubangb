package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.NewsAdapter;
import com.cgzz.job.b.adapter.TrainingAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.b.view.CustomListView.OnRefreshListener;

/***
 * @author wjm 主页：统计
 */
public class MainConsultingFragment extends BaseActivity implements
		OnClickListener, OnItemClickListener {
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
	private NewsAdapter Newsadapter;
	private TrainingAdapter Trainingadapter;
	public static final int TYPE_TAB_INVITE = 0;
	public static final int TYPE_TAB_APPLY = 1;
	public static final int TYPE_TAB_BEING = 2;
	public static final int TYPE_TAB_DONE = 3;
	private int currentIndicatorPosition;
	private int logoCONDUCT = 1;// 页码标识
	private int logoTraining = 1;// 页码标识
	private ArrayList<Map<String, String>> NewsData = new ArrayList<Map<String, String>>();
	private ArrayList<Map<String, String>> TrainingData = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> fromPassengerMaps;
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {

			case HttpStaticApi.testa_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						NewsData.clear();
						logoCONDUCT = 2;
					} else {
						logoCONDUCT++;
					}
					// 测试数据
					shufflingList = new ArrayList<Map<String, String>>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("tv_new_title", "title");
					map.put("tv_new_message", "message");
					map.put("tv_new_checking", "查看" + logoCONDUCT);
					map.put("tv_new_picture",
							"http://pic.nipic.com/2008-05-29/200852973848511_2.jpg");

					map.put("req_url_path", "http://news.163.com/");
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);

					NewsData.addAll(shufflingList);
					Newsadapter.refreshMYData(NewsData);
					lvNews.onLoadMoreComplete();
					lvNews.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvNews.onLoadMoreComplete();
					lvNews.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.testb_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					if (loadedtype) {
						TrainingData.clear();
						logoTraining = 2;
					} else {
						logoTraining++;
					}
					// 测试数据
					shufflingList = new ArrayList<Map<String, String>>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("tv_new_title", "title");
					map.put("tv_new_message", "message");
					map.put("tv_new_checking", "查看" + logoTraining);
					map.put("tv_new_picture",
							"http://c.hiphotos.baidu.com/zhidao/pic/item/8435e5dde71190efd414429ecd1b9d16fcfa6080.jpg");

					map.put("req_url_path",
							"http://www.hao123.com/?tn=alading_hao_dg");
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);
					shufflingList.add(map);

					TrainingData.addAll(shufflingList);
					Trainingadapter.refreshMYData(TrainingData);
					lvTraining.onLoadMoreComplete();
					lvTraining.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvTraining.onLoadMoreComplete();
					lvTraining.onRefreshComplete();
					break;
				}
				break;

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_consulting);
//		setTitle("好帮手", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
//				TITLE_TYPE_TEXT, "注册");
		initView();
		// carousel(UrlConfig.carousel_Http, "2", true);

	}

	private void initView() {
		// 图片轮询
		tvNews = (TextView) findViewById(R.id.tv_route_tab_invite);
		tvTraining = (TextView) findViewById(R.id.tv_route_tab_apply);
		tvNews.setOnClickListener(this);
		tvTraining.setOnClickListener(this);

		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		ivLine = (ImageView) findViewById(R.id.iv_line);
		// 设置滑动线的宽度
		android.view.ViewGroup.LayoutParams layoutParams = ivLine
				.getLayoutParams();
		LENGTH = d.getWidth() / 2;
		layoutParams.width = LENGTH;
		ivLine.setLayoutParams(layoutParams);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvNews = new CustomListView(MainConsultingFragment.this);
		lvNews.setFadingEdgeLength(0);
		lvNews.setDivider(getResources().getDrawable(R.color.common_grey));
		lvNews.setDividerHeight(Utils.dip2px(MainConsultingFragment.this, 0));
		lvNews.setFooterDividersEnabled(false);
		lvNews.setCanRefresh(true);// 关闭下拉刷新
		lvNews.setCanLoadMore(true);// 打开加载更多
		Newsadapter = new NewsAdapter(MainConsultingFragment.this);
		lvNews.setAdapter(Newsadapter);
		lvNews.setOnItemClickListener(this);
		lvNews.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				getConsulting("https://www.baidu.com/", false);
			}
		});

		lvNews.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				getConsulting("https://www.baidu.com/", true);

			}
		});

		lvTraining = new CustomListView(MainConsultingFragment.this);
		lvTraining.setFadingEdgeLength(0);
		lvTraining.setDivider(getResources().getDrawable(R.color.common_grey));
		lvTraining.setDividerHeight(Utils
				.dip2px(MainConsultingFragment.this, 0));
		lvTraining.setFooterDividersEnabled(false);
		lvTraining.setCanRefresh(true);// 关闭下拉刷新
		lvTraining.setCanLoadMore(true);// 打开加载更多

		Trainingadapter = new TrainingAdapter(MainConsultingFragment.this);
		lvTraining.setAdapter(Trainingadapter);
		lvTraining.setOnItemClickListener(this);
		lvTraining.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				getTraining("https://www.baidu.com/", false);
			}
		});

		lvTraining.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				getTraining("https://www.baidu.com/", true);
			}
		});
//		logoCONDUCT = 1;
//		logoTraining = 1;
//		getConsulting("https://www.baidu.com/", false);
//		getTraining("https://www.baidu.com/", false);
		listviews.add(lvNews);
		listviews.add(lvTraining);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
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
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 0));
			currentIndicatorPosition = LENGTH * 0;
			tvNews.setTextColor(this.getResources().getColor(
					R.color.common_yellow));
			tvTraining.setTextColor(this.getResources().getColor(
					R.color.common_text));
			break;
		case TYPE_TAB_APPLY:
			tvNews.setEnabled(true);
			tvTraining.setEnabled(false);
			viewpager.setCurrentItem(type);
			ivLine.startAnimation(getAnimation(currentIndicatorPosition,
					LENGTH * 1));
			currentIndicatorPosition = LENGTH * 1;
			tvNews.setTextColor(this.getResources().getColor(
					R.color.common_text));
			tvTraining.setTextColor(this.getResources().getColor(
					R.color.common_yellow));
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
			Intent intent = new Intent(MainConsultingFragment.this,
					WebBrowserActivity.class);
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
		switch (arg0.getId()) {
		case R.id.tv_route_tab_invite:// 新闻
			isManul = true;
			switchTabText(TYPE_TAB_INVITE);
			break;
		case R.id.tv_route_tab_apply:// 培训
			isManul = true;
			switchTabText(TYPE_TAB_APPLY);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == lvNews) {

			headViewPagerOnItemOnClick(
					NewsData.get(arg2 - 1).get("tv_new_title"),
					NewsData.get(arg2 - 1).get("dict_param"));
		} else if (arg0 == lvTraining) {
			headViewPagerOnItemOnClick(
					TrainingData.get(arg2 - 1).get("tv_new_title"),
					TrainingData.get(arg2 - 1).get("dict_param"));
		}

	}

	/**
	 * 资讯列表
	 */
	private void getConsulting(String url, boolean loadedtype) {
		url = url;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				MainConsultingFragment.this, url, null, callBack,
				GlobalVariables.getRequestQueue(MainConsultingFragment.this),
				HttpStaticApi.testa_Http, null, loadedtype);

	}

	/**
	 * 资讯列表
	 */
	private void getTraining(String url, boolean loadedtype) {
		url = url;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				MainConsultingFragment.this, url, null, callBack,
				GlobalVariables.getRequestQueue(MainConsultingFragment.this),
				HttpStaticApi.testb_Http, null, loadedtype);

	}
}
