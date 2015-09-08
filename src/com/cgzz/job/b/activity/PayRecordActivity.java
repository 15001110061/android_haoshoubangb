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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.PayRecordAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.cgzz.job.b.view.CustomListView.OnLoadMoreListener;
import com.cgzz.job.b.view.CustomListView.OnRefreshListener;

/***
 * @author wjm 支付记录
 */
public class PayRecordActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private PayRecordAdapter payRecordAdapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.paylistB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					if (loadedtype) {
						CollectionData.clear();
						logoCollection = 2;
					} else {
						logoCollection++;
					}

					bundle = ParserUtil.ParserPaylistB(data);

					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {
						CollectionData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
						lvCollection.setCanLoadMore(false);
					} else {

						if (!loadedtype) {
							lvCollection.onLoadMorNodata();
						} else {
							ToastUtil.makeShortText(PayRecordActivity.this, "暂无支付记录");
							lvCollection.setCanLoadMore(false);

						}
					}

					payRecordAdapter.refreshMYData(CollectionData);
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;

				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserPaylistB(data);
					ToastUtil.makeShortText(PayRecordActivity.this, bundle.get("msg").toString());
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
		setContentView(R.layout.activity_payrecord);
		setTitle("支付记录", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		initView();

	}

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llLeft.setOnClickListener(this);
		// 下方列表
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(PayRecordActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(PayRecordActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		payRecordAdapter = new PayRecordAdapter(PayRecordActivity.this);
		lvCollection.setAdapter(payRecordAdapter);
		lvCollection.setOnItemClickListener(this);
		lvCollection.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				paylist(UrlConfig.paylistB_Http, application.getToken(), application.getUserId(), logoCollection,
						false);
			}
		});

		lvCollection.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				logoCollection = 1;
				paylist(UrlConfig.paylistB_Http, application.getToken(), application.getUserId(), logoCollection, true);

			}
		});

		logoCollection = 1;
		paylist(UrlConfig.paylistB_Http, application.getToken(), application.getUserId(), logoCollection, true);

		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(listviews);
		viewpager.setAdapter(pagerAdapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.btn_yaoqing:// 邀请
			break;
		case R.id.btn_shanchu:// 删除
			break;

		default:
			break;
		}
	}

	private void paylist(String url, String token, String userid, int page, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, PayRecordActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(PayRecordActivity.this), HttpStaticApi.paylistB_Http, null, loadedtype);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// ev_bank_type.setText(CurrentData.get(arg2 - 1).get("name"));
		Intent intent = new Intent(PayRecordActivity.this, PayReceiptsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("bangkesname", CollectionData.get(arg2 - 1).get("bangkesname"));

		bundle.putString("paytype", CollectionData.get(arg2 - 1).get("paytype"));
		bundle.putString("paytime", CollectionData.get(arg2 - 1).get("paytime"));
		bundle.putString("paymoney", CollectionData.get(arg2 - 1).get("paymoney"));

		bundle.putString("orderno", CollectionData.get(arg2 - 1).get("orderno"));
		bundle.putString("realname", CollectionData.get(arg2 - 1).get("realname"));
		intent.putExtras(bundle);
		startActivity(intent);
	}

}
