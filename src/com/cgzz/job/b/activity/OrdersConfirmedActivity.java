package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
import com.cgzz.job.b.adapter.Myadapter4;
import com.cgzz.job.b.adapter.OrdersConfirmedAdapter;
import com.cgzz.job.b.adapter.OrdersConfirmedAdapter.OnRefuseClickListener;
import com.cgzz.job.b.adapter.OrdersConfirmedAdapter.OnRouteClickListener;
import com.cgzz.job.b.adapter.OrdersConfirmedAdapter.OnTelClickListener;
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/***
 * @author wjm 订单确认
 */
public class OrdersConfirmedActivity extends BaseActivity
		implements OnClickListener, OnTelClickListener, OnRouteClickListener, OnRefuseClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private OrdersConfirmedAdapter ordersConfirmedAdapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callbackData = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {

			case HttpStaticApi.checkB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserCheck(data);

					Intent intent = new Intent(OrdersConfirmedActivity.this, PayChooseActivity.class);
					Bundle bundle2 = new Bundle();
					bundle2.putString("totalPrice", bundle.getString("totalPrice").toString());
					bundle2.putString("isSign", bundle.getString("isSign").toString());
					bundle2.putString("id", id);
					bundle2.putString("orderno", orderno);
					bundle2.putString("iscash", bundle.getString("iscash").toString());
					bundle2.putString("enddate", bundle.getString("enddate").toString());
					
					intent.putExtras(bundle2);
					startActivity(intent);

					refreshOrders();
					refreshSampled();
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(OrdersConfirmedActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			}
		}
	};
	LinearLayout llLeft, llright;
	String bangkelist = "", standard_count = "0", suite_count = "0", id = "", standard_countbk = "", suite_countbk = "",
			bounus = "", earnmoney = "", status = "", orderno = "";
	Bundle bundle = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordersconfirmed);

		Intent intent = getIntent();

		if ("4".equals(status)) {
			setTitle("订单确认", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "完成");

		} else {
			setTitle("订单确认", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "支付");
		}
		status = intent.getStringExtra("status");
		bangkelist = intent.getStringExtra("bangkelist");
		standard_countbk = intent.getStringExtra("standard_count");
		suite_countbk = intent.getStringExtra("suite_count");
		bounus = intent.getStringExtra("bounus");
		orderno = intent.getStringExtra("orderno");
		// 解析
		standard_count = intent.getStringExtra("standard_count");// 标间数量
		suite_count = intent.getStringExtra("suite_count");
		id = intent.getStringExtra("id");
		bundle = ParserUtil.ParserBangkelist(bangkelist, standard_count, suite_count, status);
		initView();
		releaseBroadcastReceiver();
		initmPopupWindowView();

	}

	TextView tv_oc_wenzi1, tv_oc_wenzi2, tv_oc_wenzi3, tv_oc_wenzi4;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);

		tv_oc_wenzi1 = (TextView) findViewById(R.id.tv_oc_wenzi1);
		tv_oc_wenzi2 = (TextView) findViewById(R.id.tv_oc_wenzi2);
		tv_oc_wenzi3 = (TextView) findViewById(R.id.tv_oc_wenzi3);
		tv_oc_wenzi4 = (TextView) findViewById(R.id.tv_oc_wenzi4);

		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		viewpager = (ViewPager) findViewById(R.id.viewpager);

		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(OrdersConfirmedActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(OrdersConfirmedActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(false);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		ordersConfirmedAdapter = new OrdersConfirmedAdapter(OrdersConfirmedActivity.this, standard_count, suite_count);
		ordersConfirmedAdapter.setOnTelClickListener(this, 0);
		ordersConfirmedAdapter.setOnRouteClickListener(this, 0);
		ordersConfirmedAdapter.setonRefuseClickListener(this, 0);
		lvCollection.setAdapter(ordersConfirmedAdapter);

		listviews.add(lvCollection);
		ConsultingPagerAdapter pagerAdapter = new ConsultingPagerAdapter(listviews);
		viewpager.setAdapter(pagerAdapter);

		if (((ArrayList<Map<String, String>>) bundle.getSerializable("list")).size() > 0) {

			CollectionData.addAll((ArrayList<Map<String, String>>) bundle.getSerializable("list"));
		}
		ordersConfirmedAdapter.refreshMYData(CollectionData);

		if (!"0".equals(standard_count)) {
			tv_oc_wenzi1.setText("共有标间" + standard_count + "间");
			tv_oc_wenzi2.setText("(已分" + setViewTitleBiao() + "间)");
		} else {
			tv_oc_wenzi1.setVisibility(View.GONE);
			tv_oc_wenzi2.setVisibility(View.GONE);
		}

		if (!"0".equals(suite_count)) {
			tv_oc_wenzi3.setText("套间" + suite_count + "间");
			tv_oc_wenzi4.setText("(已分" + setViewTitleTao() + "间)");
		} else {
			tv_oc_wenzi3.setVisibility(View.GONE);
			tv_oc_wenzi4.setVisibility(View.GONE);
		}

	}

	private int setViewTitleBiao() {
		int biaojian = 0;
		for (int i = 0; i < CollectionData.size(); i++) {
			biaojian += Integer.parseInt(CollectionData.get(i).get("biaojiaNnum"));
		}
		return biaojian;

	}

	private int setViewTitleTao() {
		int taojian = 0;
		for (int i = 0; i < CollectionData.size(); i++) {
			taojian += Integer.parseInt(CollectionData.get(i).get("taojianNum"));
		}
		return taojian;

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 完成
			if (CollectionData.size() != 0) {

				StringBuffer sb = new StringBuffer();

				if (!"0".equals(standard_count)) {
					int yifen = setViewTitleBiao();
					int biaojiannum = Integer.parseInt(standard_count);
					if (biaojiannum != yifen) {
						sb.append("您实际分配了" + yifen + "间标间(");
						if (biaojiannum > yifen) {
							sb.append("少于原定的" + standard_count + "间)");
						} else {
							ToastUtil.makeShortText(OrdersConfirmedActivity.this, "分配的房间不可以大于发单房间数");
							return;
						}

					}
				} else {
				}

				if (!"0".equals(suite_count)) {

					int yifen = setViewTitleTao();
					int taojiannum = Integer.parseInt(suite_count);
					if (taojiannum != yifen) {
						sb.append("您实际分配了" + yifen + "间套间(");
						if (taojiannum > yifen) {
							sb.append("少于原定的" + suite_count + "间)");
						} else {
							// sb.append("多于原定的" + suite_count + "间)");

							ToastUtil.makeShortText(OrdersConfirmedActivity.this, "分配的房间不可以大于发单房间数");
							return;
						}
					}

				} else {
				}
				if (Utils.isEmpty(sb.toString())) {
					StringBuffer params = new StringBuffer();
					for (int i = 0; i < CollectionData.size(); i++) {
						params.append(CollectionData.get(i).get("id") + ":");
						params.append(CollectionData.get(i).get("biaojiaNnum") + ":");
						params.append(CollectionData.get(i).get("taojianNum") + ":");
						params.append(CollectionData.get(i).get("dashangNum") + ":");
						params.append(CollectionData.get(i).get("userid") + ";");

					}

					char ch = ';';
					if (!"".equals(params.toString())) {
						check(UrlConfig.checkB_Http, application.getToken(), application.getUserId(), id,
								trimFirstAndLastChar(params.toString(), ch), status, true);
					} else {
						ToastUtil.makeShortText(OrdersConfirmedActivity.this, "暂无人员分配");
					}
				} else {
					sb.append("，\n确认后将进入付款流程，是否继续？");
					String html = sb.toString();

					CustomDialog.alertDialog(OrdersConfirmedActivity.this, false, true, true, null, html, false,
							new CustomDialog.PopUpDialogListener() {

								@Override
								public void doPositiveClick(Boolean isOk) {
									if (isOk) {// 确定

										StringBuffer params = new StringBuffer();
										for (int i = 0; i < CollectionData.size(); i++) {
											params.append(CollectionData.get(i).get("id") + ":");
											params.append(CollectionData.get(i).get("biaojiaNnum") + ":");
											params.append(CollectionData.get(i).get("taojianNum") + ":");
											params.append(CollectionData.get(i).get("dashangNum") + ":");
											params.append(CollectionData.get(i).get("userid") + ";");

										}

										char ch = ';';
										if (!"".equals(params.toString())) {
											check(UrlConfig.checkB_Http, application.getToken(),
													application.getUserId(), id,
													trimFirstAndLastChar(params.toString(), ch), status, true);
										} else {
											ToastUtil.makeShortText(OrdersConfirmedActivity.this, "暂无人员分配");
										}

									} else {

									}

								}
							});
				}
			} else {
				ToastUtil.makeShortText(OrdersConfirmedActivity.this, "暂无人员分配");
			}
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
			int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element)
					: source.length();
			source = source.substring(beginIndex, endIndex);
			beginIndexFlag = (source.indexOf(element) == 0);
			endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
		} while (beginIndexFlag || endIndexFlag);
		return source;
	}

	TextView vTelClick;
	int Telposition;
	String type = "";

	/***
	 * 打赏数量滚轮
	 */
	@Override
	public void onTelClick(int position, View v, int logo) {
		if (popupwindow != null) {
			tv_current_room.setVisibility(View.VISIBLE);
			tv_current_room2.setVisibility(View.VISIBLE);
			tv_current_room.setText("选择打赏金额");
			type = "1";
			// vTelClick=(TextView) v;
			Telposition = position;
			popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
			if (listlv != null)
				listlv.clear();
			listlv = getbiaojian();
			adapter.refreshData(listlv);

		}
	}

	/***
	 * 标间数量滚轮
	 */
	@Override
	public void onRouteClick(int position, View v, int logo) {
		// TODO Auto-generated method stub

		if (popupwindow != null) {
			type = "2";
			tv_current_room.setVisibility(View.VISIBLE);
			tv_current_room2.setVisibility(View.VISIBLE);
			tv_current_room.setText("选择标间数量");
			// vTelClick=(TextView) v;
			Telposition = position;
			popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
			if (listlv != null)
				listlv.clear();
			listlv = getbiaojian();
			adapter.refreshData(listlv);

		}
	}

	/***
	 * 套间数量滚轮
	 */
	@Override
	public void onRefuseClick(int position, View v, int logo) {
		// TODO Auto-generated method stub
		if (popupwindow != null) {
			type = "3";
			tv_current_room.setVisibility(View.VISIBLE);
			tv_current_room2.setVisibility(View.VISIBLE);
			tv_current_room.setText("选择套间数量");
			// vTelClick=(TextView) v;
			Telposition = position;
			popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
			if (listlv != null)
				listlv.clear();
			listlv = getbiaojian();
			adapter.refreshData(listlv);

		}
	}

	/***
	 * 订单验收接口
	 */
	private void check(String url, String token, String userid, String orderid, String params, String status,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderid", orderid);
		map.put("params", params);
		map.put("status", status);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, OrdersConfirmedActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(OrdersConfirmedActivity.this), HttpStaticApi.checkB_Http, null,
				loadedtype);
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

	OBDBroadcastReceiver recobdlist;

	private void releaseBroadcastReceiver() {
		recobdlist = new OBDBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.cgzz.job.accesstype");
		registerReceiver(recobdlist, filter);
	}

	private class OBDBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String TYPE = bundle.getString("TYPE");
			String html = "";
			if ("rodersconfirmedbiao".equals(TYPE)) {
				String biaoNum = bundle.getString("biaoNum");
				if (Integer.parseInt(biaoNum) > Integer.parseInt(standard_count)) {
					html = "(已分<font color=\"#CC0000\">" + biaoNum + "</font>间)";
				} else {
					html = "(已分<font color=\"#666666\">" + biaoNum + "</font>间)";
				}
				tv_oc_wenzi2.setText(Html.fromHtml(html));

			} else if ("rodersconfirmedbtao".equals(TYPE)) {
				String taoNum = bundle.getString("taoNum");
				if (Integer.parseInt(taoNum) > Integer.parseInt(suite_count)) {
					html = "(已分<font color=\"#CC0000\">" + taoNum + "</font>间)";
				} else {
					html = "(已分<font color=\"#666666\">" + taoNum + "</font>间)";
				}
				tv_oc_wenzi4.setText(Html.fromHtml(html));
			}

		}
	}

	@Override
	protected void onDestroy() {
		if (recobdlist != null)
			unregisterReceiver(recobdlist);

		super.onDestroy();
	}

	/**
	 *
	 */

	Myadapter4 adapter;
	ArrayList<Map<String, String>> listlv;
	PopupWindow popupwindow;
	CustomListView lvCars;
	TextView tv_current_room, tv_current_room3;
	View tv_current_room2;

	public void initmPopupWindowView() {
		// 获取自定义布局文件的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		ImageButton dis = (ImageButton) customView.findViewById(R.id.ib_dis);

		tv_current_room = (TextView) customView.findViewById(R.id.tv_current_room);
		tv_current_room2 = (View) customView.findViewById(R.id.tv_current_room2);
		tv_current_room3 = (TextView) customView.findViewById(R.id.tv_current_room3);
		// 创建PopupWindow宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// 点击屏幕其他部分及Back键时PopupWindow消失
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupwindow.dismiss();
			}
		});
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
		tv_current_room3.setOnClickListener(this);

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
					if ("1".equals(type)) {
						CollectionData.get(Telposition).put("dashangNum", listlv.get(arg2 - 1).get("name"));
						ordersConfirmedAdapter.refreshMYData(CollectionData);
						popupwindow.dismiss();
					} else if ("2".equals(type)) {
						CollectionData.get(Telposition).put("biaojiaNnum", listlv.get(arg2 - 1).get("name"));
						ordersConfirmedAdapter.refreshMYData(CollectionData);
						popupwindow.dismiss();
					} else if ("3".equals(type)) {
						CollectionData.get(Telposition).put("taojianNum", listlv.get(arg2 - 1).get("name"));
						ordersConfirmedAdapter.refreshMYData(CollectionData);
						popupwindow.dismiss();
					}

				}
			}
		});
		adapter = new Myadapter4(this);
		lvCars.setAdapter(adapter);

	} // 标间价格

	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// Map<String, String> map2 = new HashMap<String, String>();
		// map2.put("name", "0");
		// list.add(map2);
		for (int i = 0; i < 201; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}
		return list;
	}

}
