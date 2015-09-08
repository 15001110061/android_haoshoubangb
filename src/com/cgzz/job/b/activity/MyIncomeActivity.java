package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.MyIncomeAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;

/***
 * @author wjm 收入
 */
public class MyIncomeActivity extends BaseActivity implements OnClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private TextView tv_myincome_jifen, tv_myincome_zichan;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private MyIncomeAdapter myIncomeAdapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private LinearLayout llLeft, llright;
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.myIncome_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMyIncome(data);
					tv_myincome_zichan.setText(bundle.get("totalincome")
							.toString());
					tv_myincome_jifen.setText(bundle.get("totalscore")
							.toString());
					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {

						CollectionData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
					} else {
						ToastUtil.makeShortText(
								MyIncomeActivity.this,
								MyIncomeActivity.this.getResources().getString(
										R.string.http_nodata));
					}

					myIncomeAdapter.refreshMYData(CollectionData);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myincome);
		setTitle("我的收入", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
				TITLE_TYPE_TEXT, "银行卡");
		initView();

	}

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_myincome_jifen = (TextView) findViewById(R.id.tv_myincome_jifen);

		tv_myincome_zichan = (TextView) findViewById(R.id.tv_myincome_zichan);
		llright.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		// 下方列表
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(MyIncomeActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(MyIncomeActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		myIncomeAdapter = new MyIncomeAdapter(MyIncomeActivity.this);
		lvCollection.setAdapter(myIncomeAdapter);

		getMyIncome(UrlConfig.myIncome_Http, application.getToken(),
				application.getUserId(), false);

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
		case R.id.ll_title_right:// 银行卡

			break;
		default:
			break;
		}
	}

	/**
	 * 我的收入接口
	 */
	private void getMyIncome(String url, String token, String userid,
			boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				MyIncomeActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(MyIncomeActivity.this),
				HttpStaticApi.myIncome_Http, null, loadedtype);
	}

}
