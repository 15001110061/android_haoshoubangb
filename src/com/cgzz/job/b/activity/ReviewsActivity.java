package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.ReviewsItemAdapter;
import com.cgzz.job.b.adapter.ReviewsItemAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.ReviewsItemAdapter.OnTextClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.view.CustomerRatingBarGo;
import com.cgzz.job.b.view.CustomerRatingBarGo.OnProgressChangeListener;

/***
 * @author wjm 去评价
 */
public class ReviewsActivity extends BaseActivity implements OnClickListener,
		OnTelClickListener, OnTextClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private ReviewsItemAdapter reviewsItemAdapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.reviewB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(ReviewsActivity.this,
							bundle.get("msg").toString());
					refreshOrders();
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(ReviewsActivity.this,
							bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			}
		}
	};
	LinearLayout llLeft, llright;
	String bangkelist = "", id = "";
	Bundle bundle = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviews);
		setTitle("评价与收藏", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
				TITLE_TYPE_TEXT, "完成");
		Intent intent = getIntent();
		bangkelist = intent.getStringExtra("bangkelist");
		id = intent.getStringExtra("id");
		bundle = ParserUtil.ParserBangkelistPingjia(bangkelist);
		initView();

	}

	com.cgzz.job.b.view.CustomerRatingBarGo room_review_car1;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		room_review_car1 = (CustomerRatingBarGo) findViewById(R.id.room_review_car1);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(ReviewsActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(ReviewsActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		reviewsItemAdapter = new ReviewsItemAdapter(ReviewsActivity.this);
		lvCollection.setAdapter(reviewsItemAdapter);
		// reviewsItemAdapter.setOnTelClickListener(this, 0);

		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(
				listviews);
		viewpager.setAdapter(pagerAdapter);

		room_review_car1.setOnProgressChangeListener(room_review_car1,
				new OnProgressChangeListener() {

					@Override
					public void onProgressChange(View view, int progress) {

						// 遍历list的长度，将MyAdapter中的map值全部设为true
						for (int i = 0; i < CollectionData.size(); i++) {
							CollectionData.get(i).put("Pingfen", "" + progress);
						}

						// 刷新listview和TextView的显示
						reviewsItemAdapter.refreshMYData(null);
					}
				}, true);

		if (((ArrayList<Map<String, String>>) bundle.getSerializable("list"))
				.size() > 0) {

			CollectionData.addAll((ArrayList<Map<String, String>>) bundle
					.getSerializable("list"));
		}

		reviewsItemAdapter.refreshMYData(CollectionData);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 完成
			StringBuffer params = new StringBuffer();
			for (int i = 0; i < CollectionData.size(); i++) {
				params.append(CollectionData.get(i).get("id") + ":");
				params.append(CollectionData.get(i).get("Pingfen") + ":");
				params.append(CollectionData.get(i).get("isShoucang") + ":");
				params.append(CollectionData.get(i).get("userid") + ";");
			}

			char ch = ';';
			if (!"".equals(params.toString()))
				review(UrlConfig.reviewB_Http, application.getToken(),
						application.getUserId(), id,
						trimFirstAndLastChar(params.toString(), ch), true);

			break;
		default:
			break;
		}
	}

	public static String trimFirstAndLastChar(String source, char element) {
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do {
			int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
			int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source
					.lastIndexOf(element) : source.length();
			source = source.substring(beginIndex, endIndex);
			beginIndexFlag = (source.indexOf(element) == 0);
			endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
		} while (beginIndexFlag || endIndexFlag);
		return source;
	}

	@Override
	public void onTextClick(int position, View v, int logo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTelClick(int position, View v, int logo) {
	}

	/**
	 * 评价收藏接口
	 */
	private void review(String url, String token, String userid,
			String orderid, String params, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);
		map.put("params", params);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				ReviewsActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(ReviewsActivity.this),
				HttpStaticApi.reviewB_Http, null, loadedtype);
	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}
}
