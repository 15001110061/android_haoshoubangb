package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.BaseActivityCloseListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.SearchAdapter;
import com.cgzz.job.b.adapter.SignedActivityThreeAdapter;
import com.cgzz.job.b.adapter.SignedActivityThreeAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.SignedActivityThreeAdapter.OnTextClickListener;
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 
 * @author wjm 注册第3步
 */
public class SignedActivityThree extends BaseActivity
		implements OnClickListener, TextWatcher, OnTelClickListener, OnTextClickListener {
	// private String mobile, password, faceurl, cardurl, portrait, realname,
	// latitude, longitude, address, card, role, mailbox;
	int isTrain = 0;
	int isSign = 0;
	int age, sex, introduceno;
	private EditText et_signed_nianxian;
	private AutoCompleteTextView et_signed_home;
	private SearchAdapter searchAdapter;
	LinearLayout llLeft, llright;
	public GlobalVariables application;
	private int type = 0;
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.search_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserSearch(data);

					if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {
						CollectionData.clear();
						CollectionData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
					} else {
						CollectionData.clear();
						// ToastUtil.makeShortText(SignedActivityThree.this,
						// SignedActivityThree.this.getResources()
						// .getString(R.string.http_nodata));
					}

					signedActivityThreeAdapter.refreshMYData(CollectionData);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserSearch(data);
					ToastUtil.makeShortText(SignedActivityThree.this, bundle.get("msg").toString());
					break;
				default:
					break;
				}
				break;

			case HttpStaticApi.applyB_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SignedActivityThree.this, bundle.get("msg").toString());
					Intent mIntents = new Intent();
					mIntents.putExtra("isshenhe", "y");

					application.setHotelid(hotelids);
					setResult(11, mIntents);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SignedActivityThree.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signed_three);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_REGISTERED, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});

		setTitle("快速注册", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "放弃");
		// Intent intent = getIntent();
		// mobile = intent.getStringExtra("mobile");
		// password = intent.getStringExtra("password");
		// portrait = intent.getStringExtra("portrait");// 头图.
		// card = intent.getStringExtra("card");// 身份证
		// realname = intent.getStringExtra("realname");// 真实姓名
		// cardurl = intent.getStringExtra("cardUrl");// 身份证图.
		// role = intent.getStringExtra("role");// 职务
		// mailbox = intent.getStringExtra("mailbox");// 邮箱

		initView();
		initListenger();
		// initPopWindow();

	}

	ViewPager viewpager;
	TextView tv_home_item_sign;
	SignedActivityThreeAdapter signedActivityThreeAdapter;
	CustomListView lvCollection;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		et_signed_home = (AutoCompleteTextView) findViewById(R.id.et_signed_home);

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		tv_home_item_sign = (TextView) findViewById(R.id.tv_home_item_sign);

		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(SignedActivityThree.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(SignedActivityThree.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		signedActivityThreeAdapter = new SignedActivityThreeAdapter(SignedActivityThree.this);
		lvCollection.setAdapter(signedActivityThreeAdapter);
		signedActivityThreeAdapter.setOnTextClickListener(this, 0);
		signedActivityThreeAdapter.setOnTelClickListener(this, 0);

		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(listviews);
		viewpager.setAdapter(pagerAdapter);
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_home_item_sign.setOnClickListener(this);
		et_signed_home.setOnClickListener(this);
		et_signed_home.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_signed_next:// 下一步

			break;

		case R.id.ll_title_right:// 放弃

		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.tv_home_item_sign:// 创建企业
			intent = new Intent(SignedActivityThree.this, SignedActivityFive.class);
			Bundle bundle = new Bundle();
			bundle.putString("hotelid", "");
			intent.putExtras(bundle);
			startActivityForResult(intent, 11);
			break;

		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	// private void initPopWindow() {
	//
	// searchAdapter = new SearchAdapter(this);
	// et_signed_home.setAdapter(searchAdapter);
	// searchAdapter.setOnTelClickListener(this, 0);
	//
	// et_signed_home.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	// et_signed_home.setText(CurrentData.get(position).get("name"));
	// Intent intent = new Intent(SignedActivityThree.this,
	// SignedActivityFive.class);
	// Bundle bundle = new Bundle();
	// bundle.putString("hotelid", CurrentData.get(position).get("id"));// .
	//
	// intent.putExtras(bundle);
	// startActivity(intent);
	//
	// }
	// });
	//
	// }

	// @Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (!"".equals(arg0.toString().trim())) {
			getSearch(UrlConfig.search_Http, arg0.toString().trim(), true);
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTelClick(int position, View v, int logo) {

		apply(UrlConfig.applyB_Http, application.getToken(), application.getUserId(),
				CollectionData.get(position).get("id"), true);
	}

	private void getSearch(String url, String keyword, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("keyword", keyword);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SignedActivityThree.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SignedActivityThree.this), HttpStaticApi.search_Http, null, loadedtype);
	}

	String hotelids = "";

	private void apply(String url, String token, String userid, String hotelid, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("hotelid", hotelid);
		hotelids = hotelid;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SignedActivityThree.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SignedActivityThree.this), HttpStaticApi.applyB_Http, null, loadedtype);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null)
			switch (requestCode) {
			case 11:
				String isshenhe = data.getStringExtra("isshenhe");
				if ("y".equals(isshenhe)) {
					// 审核中
					Intent mIntents = new Intent();
					mIntents.putExtra("isshenhe", "y");
					setResult(11, mIntents);
					finish();

				} else {

				}
			default:
				break;
			}
	}

	@Override
	public void onTextClick(final int position, View v, int logo) {

		CustomDialog.alertDialog(SignedActivityThree.this, false, true, true, null, "申请管理员", true,
				new CustomDialog.PopUpDialogListener() {

					@Override
					public void doPositiveClick(Boolean isOk) {
						if (isOk) {// 确定

							Intent intent = new Intent(SignedActivityThree.this, SignedActivityFive.class);
							Bundle bundle = new Bundle();
							bundle.putString("hotelid", CollectionData.get(position).get("id"));
							intent.putExtras(bundle);
							startActivityForResult(intent, 11);
						} else {

						}

					}
				});

	}

}
