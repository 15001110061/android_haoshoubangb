package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.OrderDetailsAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * @author wjm 订单详情 
 */
public class OrderDetailsActivity extends BaseActivity implements OnClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private OrderDetailsAdapter orderDetailsAdapter;
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.completeDetailB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserCompleteDetailB(data);
					if (!Utils.isEmpty(bundle.getString("paymoney"))) {
						tv_paycash_titlew.setText("￥"+bundle.getString("paymoney"));
					}else{
						tv_paycash_titlew.setText("￥0");
					}
				
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
					} else {


					}

					orderDetailsAdapter.refreshMYData(CurrentData);
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;

				default:
					break;
				}
				break;
				
			}
		}
	};
	LinearLayout llLeft;
	String id = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetails);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		setTitle("订单详情", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "注册");

		initView();
		completeDetail(UrlConfig.completeDetailB_Http, application.getToken(),
				application.getUserId(), id, true);

	}

	TextView tv_paycash_titlew;
	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		tv_paycash_titlew = (TextView) findViewById(R.id.tv_paycash_titlew);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(OrderDetailsActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(OrderDetailsActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this);
		lvCollection.setAdapter(orderDetailsAdapter);


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

		default:
			break;
		}
	}

	private void completeDetail(String url, String token, String userid,String orderid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("orderid", orderid);
		
		
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				OrderDetailsActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(OrderDetailsActivity.this),
				HttpStaticApi.completeDetailB_Http, null, loadedtype);
	}
	

}
