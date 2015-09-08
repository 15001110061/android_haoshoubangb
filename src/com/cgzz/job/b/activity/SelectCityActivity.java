package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.SortAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.bean.SortModel;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.CharacterParser;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.view.ClearEditText;
import com.cgzz.job.b.view.PinyinComparator;
import com.cgzz.job.b.view.SideBar;
import com.cgzz.job.b.view.SideBar.OnTouchingLetterChangedListener;

public class SelectCityActivity extends BaseActivity implements OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */

	private PinyinComparator pinyinComparator;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.citylist_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserCitylist(data);

					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {
						SourceDateList = filledData((ArrayList<Map<String, String>>) bundle
								.getSerializable("list"));
						// 根据a-z进行排序源数据
						Collections.sort(SourceDateList, pinyinComparator);
						adapter = new SortAdapter(SelectCityActivity.this,
								SourceDateList);
						sortListView.setAdapter(adapter);

					} else {
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserLogin(data);
					ToastUtil.makeShortText(SelectCityActivity.this, bundle
							.get("msg").toString());
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
	LinearLayout llLeft, ll_one;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);
		setTitle("切换城市", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		initViews();
		citylist(UrlConfig.citylist_Http, true);
		// initData();
		initListenger();
	}

	// private void initData() {
	// ArrayList<Map<String, String>> cityListMap = new ArrayList<Map<String,
	// String>>();
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("name", "阿城");
	// map.put("id", "1");
	// cityListMap.add(map);
	//
	// map = new HashMap<String, String>();
	// map.put("name", "北京");
	// map.put("id", "2");
	// cityListMap.add(map);
	//
	// map = new HashMap<String, String>();
	// map.put("name", "北京2");
	// map.put("id", "2");
	// cityListMap.add(map);
	//
	// map = new HashMap<String, String>();
	// map.put("name", "吉林");
	// map.put("id", "2");
	// cityListMap.add(map);
	//
	// map = new HashMap<String, String>();
	// map.put("name", "长春");
	// map.put("id", "2");
	// cityListMap.add(map);
	//
	// map = new HashMap<String, String>();
	// map.put("name", "大连");
	// map.put("id", "2");
	// cityListMap.add(map);
	//
	// SourceDateList = filledData(cityListMap);
	// // 根据a-z进行排序源数据
	// Collections.sort(SourceDateList, pinyinComparator);
	// adapter = new SortAdapter(this, SourceDateList);
	// sortListView.setAdapter(adapter);
	// }
	TextView tv_select_dangqiancity;

	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		// tv_gps_city = (TextView) findViewById(R.id.tv_gps_city);
		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		// layout_city_gps_city = (RelativeLayout)
		// findViewById(R.id.layout_city_gps_city);

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		tv_select_dangqiancity = (TextView) findViewById(R.id.tv_select_dangqiancity);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		ll_one = (LinearLayout) findViewById(R.id.ll_one);
		if (!"".equals(application.getCityName())) {
			tv_select_dangqiancity.setText(application.getCityName());
		} else {
			ll_one.setVisibility(View.GONE);
		}
		// mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		// // 根据输入框输入值的改变来过滤搜索
		// mClearEditText.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
		// filterData(s.toString());
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// }
		// });

	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		// layout_city_gps_city.setOnClickListener(this);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				// Intent resultIntent = new Intent(SelectCityActivity.this,
				// VipAddAddressActivity.class);
				//
				SortModel sort = (SortModel) adapter.getItem(position);

				Intent intent = new Intent(SelectCityActivity.this,
						TabMainActivity.class);

				application.setUsercityCode(sort.getId());
				application.setUsercityName(sort.getName());

//				intent.putExtra("cityname", sort.getName());
//				intent.putExtra("cityid", sort.getId());
				startActivity(intent);
				finish();

				// resultIntent.putExtra("city", sort);
				// setResult(1111, resultIntent);
				// finish();
				Toast.makeText(SelectCityActivity.this, sort.getName(),
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(List<Map<String, String>> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		if (data == null) {
			return null;
		}
		for (int i = 0; i < data.size(); i++) {
			SortModel sortModel = new SortModel();

			sortModel.setName(data.get(i).get("name"));
			sortModel.setId(data.get(i).get("id"));
			String pinyin = characterParser.getSelling(data.get(i).get("name"));
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_left:// 返回
			onBackPressed();
			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(SelectCityActivity.this,
				TabMainActivity.class);
//		intent.putExtra("cityname", application.getUsercityName());
//		intent.putExtra("cityid", application.getUsercityCode());
		startActivity(intent);
		finish();
	}

	private void citylist(String url, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				SelectCityActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SelectCityActivity.this),
				HttpStaticApi.citylist_Http, null, loadedtype);
	}
}
