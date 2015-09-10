package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.Myadapter4;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm 成员修改
 */
public class MembersChangesActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.deletememberB_Http:// 删除接口
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersChangesActivity.this, bundle.get("msg").toString());

					Intent mIntents = new Intent();
					// mIntents.putExtra("isshanchu", "y");
					setResult(1, mIntents);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersChangesActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.updatememberB_Http:// 完成
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersChangesActivity.this, bundle.get("msg").toString());
					Intent mIntents = new Intent();
					// mIntents.putExtra("iswancheng", "y");
					setResult(1, mIntents);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersChangesActivity.this, bundle.get("msg").toString());
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_members_changes);
		setTitle("修改成员", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "完成");
		findView();
		init();
		Assignment();
		initmPopupWindowView();

	}

	ImageView iv_my_pic;
	CheckBox cb_seting_shoushi1, cb_seting_shoushi2, cb_seting_shoushi3;
	TextView tv_my_name, tv_my_describe, tv_my_jifen, tv_mech_shanchu, iv_my_income;
	RelativeLayout rl_mech_one;

	private void findView() {

		iv_my_pic = (ImageView) findViewById(R.id.iv_my_pic);
		cb_seting_shoushi1 = (CheckBox) findViewById(R.id.cb_seting_shoushi1);
		cb_seting_shoushi2 = (CheckBox) findViewById(R.id.cb_seting_shoushi2);
		cb_seting_shoushi3 = (CheckBox) findViewById(R.id.cb_seting_shoushi3);
		tv_my_name = (TextView) findViewById(R.id.tv_my_name);
		tv_my_describe = (TextView) findViewById(R.id.tv_my_describe);
		tv_my_jifen = (TextView) findViewById(R.id.tv_my_jifen);
		rl_mech_one = (RelativeLayout) findViewById(R.id.rl_mech_one);
		tv_mech_shanchu = (TextView) findViewById(R.id.tv_mech_shanchu);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		iv_my_income = (TextView) findViewById(R.id.iv_my_income);
	}

	LinearLayout llLeft, llright;

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		rl_mech_one.setOnClickListener(this);
		tv_mech_shanchu.setOnClickListener(this);
		cb_seting_shoushi1.setOnCheckedChangeListener(this);
		cb_seting_shoushi2.setOnCheckedChangeListener(this);
		cb_seting_shoushi3.setOnCheckedChangeListener(this);
	}

	String realname = "", mobile = "", department_value = "", portrait = "", id = "", department = "", is_order = "",
			is_check = "", is_select = "", type = "", userid = "";

	private void Assignment() {

		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		realname = intent.getStringExtra("realname");
		mobile = intent.getStringExtra("mobile");
		department_value = intent.getStringExtra("department_value");
		portrait = intent.getStringExtra("portrait");
		id = intent.getStringExtra("id");
		department = intent.getStringExtra("department");

		is_order = intent.getStringExtra("is_order");
		is_check = intent.getStringExtra("is_check");
		is_select = intent.getStringExtra("is_select");
		userid = intent.getStringExtra("userid");
		if ("tongyi".equals(type)) {
			tv_mech_shanchu.setVisibility(View.GONE);
		}

		if ("1".equals(is_order)) {
			cb_seting_shoushi3.setChecked(true);
		}

		if ("1".equals(is_check)) {
			cb_seting_shoushi2.setChecked(true);
		}

		if ("1".equals(is_select)) {
			cb_seting_shoushi1.setChecked(true);
		}

		zhiwuid = department;
		if (!Utils.isEmpty(portrait)) {
			// 头像
			ImageListener listener = ImageLoader.getImageListener(iv_my_pic, R.drawable.image_moren_pop,
					R.drawable.image_moren_pop);
			try {
				mImageLoader.get(portrait, listener);	
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		tv_my_name.setText(realname);
		tv_my_jifen.setText(mobile);
		// tv_my_describe.setText(department_value);

		if (!Utils.isEmpty(department_value)) {
			iv_my_income.setText(department_value);
		}

	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.rl_mech_one:// 选择部门

			if (popupwindow != null) {
				popupwindow.showAtLocation(findViewById(R.id.rl_signet_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getbiaojian();
				adapter.refreshData(listlv);
			}
			break;

		case R.id.tv_mech_shanchu:// 删除
			deletemember(UrlConfig.deletememberB_Http, application.getToken(), application.getUserId(), id, userid,
					true);
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 完成
			if (Utils.isEmpty(zhiwuid)) {
				ToastUtil.makeShortText(this, "职务不能为空");
				return;
			}
			String d = a + b + c;
			if (Utils.isEmpty(d)) {
				ToastUtil.makeShortText(this, "请至少选择一个权限");
				return;
			}

			if ("tongyi".equals(type)) {
				type = "1";
			} else {
				type = "3";
			}

			updatemember(UrlConfig.updatememberB_Http, application.getToken(), application.getUserId(), id, zhiwuid,
					type, userid, true);
			break;
		default:
			break;
		}

	}

	String a = "", b = "", c = "";

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (arg0 == cb_seting_shoushi1) {
			if (arg1) {
				c = "4";
			} else {
				c = "";
			}
		} else if (arg0 == cb_seting_shoushi2) {
			if (arg1) {
				b = "3";
			} else {
				b = "";
			}
		} else if (arg0 == cb_seting_shoushi3) {

			if (arg1) {
				a = "2";
			} else {
				a = "";
			}
		}

	}

	/**
	 *
	 */
	String zhiwuid = "";
	PopupWindow popupwindow;
	CustomListView lvCars;

	public void initmPopupWindowView() {
		// 获取自定义布局文件的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// 点击屏幕其他部分及Back键时PopupWindow消失
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					// popupwindow = null;
				}
				return false;
			}
		});

		// 车牌
		lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
		lvCars.setCacheColorHint(Color.TRANSPARENT);
		lvCars.setDivider(getResources().getDrawable(R.color.common_white));
		lvCars.setDividerHeight(Utils.dip2px(this, 0));
		lvCars.setFooterDividersEnabled(false);
		lvCars.setCanRefresh(false);// 关闭下拉刷新
		lvCars.setCanLoadMore(false);// 打开加载更多
		lvCars.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (lvCars == arg0) {
					iv_my_income.setText(listlv.get(arg2 - 1).get("name"));
					zhiwuid = listlv.get(arg2 - 1).get("id");
					popupwindow.dismiss();
				}
			}
		});
		adapter = new Myadapter4(this);
		lvCars.setAdapter(adapter);

	}

	Myadapter4 adapter;
	ArrayList<Map<String, String>> listlv;

	// 标间价格
	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "客房部");
		map.put("id", "1");
		list.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "财务部");
		map2.put("id", "2");
		list.add(map2);

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("name", "人事部");
		map3.put("id", "3");
		list.add(map3);

	
		return list;
	}

	/***
	 * 删除接口
	 */
	private void deletemember(String url, String token, String userid, String id, String deleteuserid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("id", id);
		map.put("deleteuserid", deleteuserid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersChangesActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersChangesActivity.this), HttpStaticApi.deletememberB_Http, null,
				loadedtype);
	}

	/***
	 * 同意后修改成员，忽略（同一个接口）
	 */
	private void updatemember(String url, String token, String userid, String id, String department, String type,
			String adduserid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("id", id);

		map.put("department", department);

		String d = a + b + c;
		String priority = "";
		if (Utils.isEmpty(d)) {
		} else {

			char ch = ';';
			priority = trimFirstAndLastChar(a + ";" + b + ";" + c, ch);

			map.put("priority", priority);
		}

		map.put("type", type);
		map.put("adduserid", adduserid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersChangesActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersChangesActivity.this), HttpStaticApi.updatememberB_Http, null,
				loadedtype);
	}

	public static String trimFirstAndLastChar(String source, char element) {
		boolean beginIndexFlag = true;
		boolean endIndexFlag = true;
		do {
			int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
			int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element)
					: source.length();
			source = source.substring(beginIndex, endIndex);
			beginIndexFlag = (source.indexOf(element) == 0);
			endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
		} while (beginIndexFlag || endIndexFlag);
		return source;
	}

}
