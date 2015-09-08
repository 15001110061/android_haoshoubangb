package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.BaseActivityCloseListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.CollectionAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.PayCashAdapter;
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
 * @author wjm 现金支付
 */
public class PayCashActivity extends BaseActivity implements OnClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private PayCashAdapter payCashAdapter;
	private ArrayList<Map<String, String>> CurrentData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.bangkesB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserBangkes(data);
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CurrentData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
					} else {


					}

					payCashAdapter.refreshMYData(CurrentData);
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
			case HttpStaticApi.hsbB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(PayCashActivity.this,
							bundle.get("msg").toString());
					refreshOrders();
					refreshSampled();
					application.popClosePath(true, UrlConfig.PATH_KEY_PAY);
					
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(PayCashActivity.this,
							bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;
				
			}
		}
	};
	LinearLayout llLeft;
	String totalPrice = "";
	String id = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paycash);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		setTitle("现金支付", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "注册");

		application.putClosePath(UrlConfig.PATH_KEY_PAY,
				new BaseActivityCloseListener() {

					@Override
					public void onFinish() {
						setResult(RESULT_OK);
						finish();
					}
				});
		totalPrice = intent.getStringExtra("totalPrice");
		initView();
		bangkes(UrlConfig.bangkesB_Http, application.getToken(),
				application.getUserId(), id, true);

	}

	TextView btn_yaoqing, btn_shanchu;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		btn_yaoqing = (TextView) findViewById(R.id.btn_yaoqing);
		btn_shanchu = (TextView) findViewById(R.id.btn_shanchu);
		llLeft.setOnClickListener(this);
		btn_yaoqing.setOnClickListener(this);
		btn_shanchu.setOnClickListener(this);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(PayCashActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(PayCashActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		payCashAdapter = new PayCashAdapter(PayCashActivity.this);
		lvCollection.setAdapter(payCashAdapter);


//		collection(UrlConfig.collectionB_Http, application.getToken(),
//				application.getUserId(),  true);

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
		case R.id.btn_shanchu:// 付现金
			hsb(UrlConfig.hsbB_Http, application.getToken(),
					application.getUserId(), id, "4", true);
			break;

		default:
			break;
		}
	}

	private void bangkes(String url, String token, String userid,String orderid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("orderid", orderid);
		
		
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				PayCashActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(PayCashActivity.this),
				HttpStaticApi.bangkesB_Http, null, loadedtype);
	}
	
	
	private void hsb(String url, String token, String userid, String orderid,
			String type, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);

		map.put("type", type);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				PayCashActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(PayCashActivity.this),
				HttpStaticApi.hsbB_Http, null, loadedtype);
	}
	public void refreshSampled() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshSampled");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

}
