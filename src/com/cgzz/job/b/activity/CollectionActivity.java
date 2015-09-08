package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.CollectionAdapter;
import com.cgzz.job.b.adapter.CollectionAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.ConsultingPagerAdapter;
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
 * @author wjm 我的收藏
 */
public class CollectionActivity extends BaseActivity implements
		OnClickListener, OnTelClickListener {
	private ArrayList<Map<String, String>> shufflingList;
	private ViewPager viewpager;
	private CustomListView lvCollection;
	private CollectionAdapter Collectionadapter;
	private int logoCollection = 1;// 页码标识
	private ArrayList<Map<String, String>> CollectionData = new ArrayList<Map<String, String>>();
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.collectionB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					if (loadedtype) {
						CollectionData.clear();
						logoCollection = 2;
					} else {
						logoCollection++;
					}

					bundle = ParserUtil.ParserCollectionB(data);

					if (((ArrayList<Map<String, String>>) bundle
							.getSerializable("list")).size() > 0) {
						btn_shanchu.setVisibility(View.VISIBLE);
						CollectionData
								.addAll((ArrayList<Map<String, String>>) bundle
										.getSerializable("list"));
						lvCollection.setCanLoadMore(true);
						lvCollection.removeHeaderView(nowanchengorders);
					} else {
						btn_shanchu.setVisibility(View.GONE);
						if (!loadedtype) {
							lvCollection.onLoadMorNodata();
						} else {
							lvCollection.setCanLoadMore(false);
							lvCollection.addHeaderView(nowanchengorders);
							lvCollection.setAdapter(Collectionadapter);
						}
					}

					Collectionadapter.refreshMYData(CollectionData);
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					lvCollection.onLoadMoreComplete();
					lvCollection.onRefreshComplete();
					break;
				}
				break;

			case HttpStaticApi.deleteCollectionB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(CollectionActivity.this, bundle
							.get("msg").toString());
					logoCollection = 1;
					collection(UrlConfig.collectionB_Http,
							application.getToken(), application.getUserId(),
							logoCollection, true);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(CollectionActivity.this, bundle
							.get("msg").toString());
					break;

				default:
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
		setContentView(R.layout.activity_collection);
		setTitle("我的收藏", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "注册");
		initView();

	}

	TextView btn_yaoqing, btn_shanchu;
	View nowanchengorders;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		btn_yaoqing = (TextView) findViewById(R.id.btn_yaoqing);
		btn_shanchu = (TextView) findViewById(R.id.btn_shanchu);
		llLeft.setOnClickListener(this);
		btn_yaoqing.setOnClickListener(this);
		btn_shanchu.setOnClickListener(this);
		// 下方列表
		// WindowManager m = this.getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// ivLine = (ImageView) findViewById(R.id.iv_line);
		// // 设置滑动线的宽度
		// android.view.ViewGroup.LayoutParams layoutParams = ivLine
		// .getLayoutParams();
		// LENGTH = d.getWidth() / 2;
		// layoutParams.width = LENGTH;
		// ivLine.setLayoutParams(layoutParams);
		// 下方列表
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// 没有的数据
		LayoutInflater inflater = getLayoutInflater();
		// 设置滑动线的宽度
		int h = d.getHeight();
		int px173 = getResources()
				.getDimensionPixelSize(R.dimen.dd_dimen_269px)
				+ getStatusHeight(CollectionActivity.this);//

		nowanchengorders = inflater.inflate(R.layout.layout_noshoucang, null);
		RelativeLayout rl_nologo_2 = (RelativeLayout) nowanchengorders
				.findViewById(R.id.rl_nologo_3);
		rl_nologo_2.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT, h - px173));

		// 填充listview
		List<ListView> listviews = new ArrayList<ListView>();
		lvCollection = new CustomListView(CollectionActivity.this);
		lvCollection.setFadingEdgeLength(0);
		lvCollection
				.setDivider(getResources().getDrawable(R.color.common_grey));
		lvCollection.setDividerHeight(Utils.dip2px(CollectionActivity.this, 0));
		lvCollection.setFooterDividersEnabled(false);
		lvCollection.setCanRefresh(true);// 关闭下拉刷新
		lvCollection.setCanLoadMore(false);// 打开加载更多

		Collectionadapter = new CollectionAdapter(CollectionActivity.this);

		Collectionadapter.setOnTelClickListener(this, 0);
		lvCollection.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {

				collection(UrlConfig.collectionB_Http, application.getToken(),
						application.getUserId(), logoCollection, false);
			}
		});

		lvCollection.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				lvCollection.removeHeaderView(nowanchengorders);
				logoCollection = 1;
				collection(UrlConfig.collectionB_Http, application.getToken(),
						application.getUserId(), logoCollection, true);

			}
		});
		logoCollection = 1;
		collection(UrlConfig.collectionB_Http, application.getToken(),
				application.getUserId(), logoCollection, true);
		lvCollection.setAdapter(Collectionadapter);

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
		case R.id.btn_yaoqing:// 邀请
			break;
		case R.id.btn_shanchu:// 删除
			// data.get(position).put("isxuanzhong", "0");
			StringBuffer params = new StringBuffer();
			for (int i = 0; i < CollectionData.size(); i++) {
				if ("1".equals(CollectionData.get(i).get("isxuanzhong"))) {
					params.append(CollectionData.get(i).get("userid") + ",");
				}

			}

			char ch = ',';
			if (!"".equals(params.toString()))
				deleteCollection(UrlConfig.deleteCollectionB_Http,
						application.getToken(), application.getUserId(),
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

	private void collection(String url, String token, String userid, int page,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("page", page + "");
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				CollectionActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(CollectionActivity.this),
				HttpStaticApi.collectionB_Http, null, loadedtype);
	}

	private void deleteCollection(String url, String token, String userid,
			String workerid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);

		map.put("userid", userid);
		map.put("workerid", workerid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST,
				CollectionActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(CollectionActivity.this),
				HttpStaticApi.deleteCollectionB_Http, null, loadedtype);
	}

	/***
	 * 打电话
	 */
	@Override
	public void onTelClick(int position, View v, int logo) {

		// intent.putExtra("orderid", );
		// popTheirProfile(CollectionData.get(position).get("phone"));
	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile(final String tel) {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two),
				Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("是否拨打酒店电话?");
		up.setText("是");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("否");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				Utils.call(tel, CollectionActivity.this);

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}

	public int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
}
