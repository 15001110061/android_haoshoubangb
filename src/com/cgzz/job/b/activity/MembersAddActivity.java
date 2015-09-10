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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm ��ӳ�Ա
 */
public class MembersAddActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	String adduserid = "";
	/**
	 * �첽�ص���������������
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.userbSearchB_Http:// ������ҵ�û�
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserUserbSearch(data);
					me_add_one.setVisibility(View.VISIBLE);

					tv_my_describe.setText(bundle.getString("department_value").toString());
					tv_my_name.setText(bundle.getString("realname").toString());
					tv_my_jifen.setText(bundle.getString("mobile").toString());
					String portrait = bundle.getString("portrait").toString();
					adduserid = bundle.getString("id").toString();
					if (!Utils.isEmpty(portrait)) {
						// ͷ��
						ImageListener listener = ImageLoader.getImageListener(iv_my_pic, R.drawable.image_moren_pop,
								R.drawable.image_moren_pop);
						try {
							mImageLoader.get(bundle.getString("portrait"), listener);
						} catch (Exception e) {
							// TODO: handle exception
						}
					
					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersAddActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.addmemberB_Http:// ȷ�����
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersAddActivity.this, bundle.get("msg").toString());
					Intent mIntents = new Intent();
					// mIntents.putExtra("iswancheng", "y");
					setResult(2, mIntents);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(MembersAddActivity.this, bundle.get("msg").toString());
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
		setContentView(R.layout.activity_members_add);
		// setTitle("�޸ĳ�Ա", true, TITLE_TYPE_IMG, R.drawable.stub_back, true,
		// TITLE_TYPE_TEXT, "���");
		findView();
		init();
		// Assignment();
		initmPopupWindowView();

	}

	ImageView iv_my_pic;
	CheckBox cb_seting_shoushi1, cb_seting_shoushi2, cb_seting_shoushi3;
	TextView tv_my_name, tv_my_describe, tv_my_jifen, tv_mech_shanchu, iv_my_income;
	RelativeLayout rl_mech_one, me_add_one;
	EditText et_signed_phones;

	private void findView() {

		et_signed_phones = (EditText) findViewById(R.id.et_signed_phones);

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
		me_add_one = (RelativeLayout) findViewById(R.id.me_add_one);
		me_add_one.setVisibility(View.GONE);
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

	// String realname = "", mobile = "", department = "", portrait = "", id =
	// "";

	// private void Assignment() {
	//
	// Intent intent = getIntent();
	// realname = intent.getStringExtra("realname");
	// mobile = intent.getStringExtra("mobile");
	// department = intent.getStringExtra("department");
	// portrait = intent.getStringExtra("portrait");
	// id = intent.getStringExtra("id");
	//
	// if (!Utils.isEmpty(portrait)) {
	// // ͷ��
	// ImageListener listener = ImageLoader.getImageListener(iv_my_pic,
	// R.drawable.icon_nor_user, R.drawable.icon_nor_user);
	// mImageLoader.get(application.getFaceUrl(), listener);
	// }
	// tv_my_name.setText(realname);
	// tv_my_jifen.setText(mobile);
	// tv_my_describe.setText(department);
	// }

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.rl_mech_one:// ѡ����

			if (popupwindow != null) {
				popupwindow.showAtLocation(findViewById(R.id.rl_signet_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getbiaojian();
				adapter.refreshData(listlv);
			}
			break;

		case R.id.tv_mech_shanchu:// ȷ�����
			if (Utils.isEmpty(zhiwuid)) {
				ToastUtil.makeShortText(this, "ְ����Ϊ��");
				return;
			}

			String d = a + b + c;
			if (Utils.isEmpty(d)) {
				ToastUtil.makeShortText(this, "������ѡ��һ��Ȩ��");
				return;
			}
			addmember(UrlConfig.addmemberB_Http, application.getToken(), application.getUserId(), adduserid, zhiwuid,
					true);
			break;
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.ll_title_right:// ������ҵ�û�
			String et = et_signed_phones.getText().toString();
			if (Utils.isEmpty(et)) {
				ToastUtil.makeShortText(this, "�����ֻ��Ż�����");
				return;
			}
			userbSearch(UrlConfig.userbSearchB_Http, application.getToken(), application.getUserId(), et, true);
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
		// ��ȡ�Զ��岼���ļ�����ͼ
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// ����PopupWindow��Ⱥ͸߶�
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// �����Ļ�������ּ�Back��ʱPopupWindow��ʧ
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		// �Զ���view��Ӵ����¼�
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

		// ����
		lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
		lvCars.setCacheColorHint(Color.TRANSPARENT);
		lvCars.setDivider(getResources().getDrawable(R.color.common_white));
		lvCars.setDividerHeight(Utils.dip2px(this, 0));
		lvCars.setFooterDividersEnabled(false);
		lvCars.setCanRefresh(false);// �ر�����ˢ��
		lvCars.setCanLoadMore(false);// �򿪼��ظ���
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

	// ���۸�
	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "�ͷ���");
		map.put("id", "1");
		list.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "����");
		map2.put("id", "2");
		list.add(map2);

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("name", "���²�");
		map3.put("id", "3");
		list.add(map3);
		return list;
	}

	/***
	 * ȷ�����
	 */
	private void addmember(String url, String token, String userid, String adduserid, String department,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("adduserid", adduserid);

		map.put("department", department);

		String d = a + b + c;
		String priority = "";
		if (Utils.isEmpty(d)) {
		} else {

			char ch = ';';
			priority = trimFirstAndLastChar(a + ";" + b + ";" + c, ch);

			map.put("priority", priority);
		}

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersAddActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersAddActivity.this), HttpStaticApi.addmemberB_Http, null,
				loadedtype);
	}

	/***
	 * ������ҵ�û�
	 */
	private void userbSearch(String url, String token, String userid, String keyword, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("keyword", keyword);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MembersAddActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MembersAddActivity.this), HttpStaticApi.userbSearchB_Http, null,
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
