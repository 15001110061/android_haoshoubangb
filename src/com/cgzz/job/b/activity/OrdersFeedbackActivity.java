package com.cgzz.job.b.activity;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;

public class OrdersFeedbackActivity extends BaseActivity implements
		OnClickListener {

	private String orderid = "", phone = "";
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj,
				boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.detail_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserDetail(data);

					Assignment(bundle);

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserLogin(data);
					ToastUtil.makeShortText(OrdersFeedbackActivity.this, bundle
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ordersfeedback);
		application = (GlobalVariables) getApplicationContext();
		Intent intent = getIntent();
		orderid = intent.getStringExtra("orderid");// 订单id
		setTitle("我的订单", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");

		initView();
		init();

		detail(UrlConfig.detail_Http, application.getToken(),
				application.getUserId(), orderid, true);

	}

	private TextView tv_ordersfeedback_title, tv_ordersfeedback_time,
			tv_ordersfeedback_site, tv_ordersfeedback_2, tv_ordersfeedback_4,
			tv_ordersfeedback_call, tv_ordersfeedback_view_route;
	// private TextView tv_ordersfeedback_title, tv_ordersfeedback_training,
	// tv_ordersfeedback_view_route, tv_ordersfeedback_cancel_order,
	// tv_ordersfeedback_time, tv_ordersfeedback_site,
	// tv_ordersfeedback_call, tv_ordersfeedback_money,
	// tv_ordersfeedback_voice;
	ImageView iv_ordersfeedback_picture;
	LinearLayout llLeft;
	RelativeLayout rl_home_item_b;
	TextView tv_ordersfeedback_titlee;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		rl_home_item_b = (RelativeLayout) findViewById(R.id.rl_home_item_b);
		iv_ordersfeedback_picture = (ImageView) findViewById(R.id.iv_ordersfeedback_picture);
		tv_ordersfeedback_title = (TextView) findViewById(R.id.tv_ordersfeedback_title);
		tv_ordersfeedback_time = (TextView) findViewById(R.id.tv_ordersfeedback_time);
		tv_ordersfeedback_site = (TextView) findViewById(R.id.tv_ordersfeedback_site);
		tv_ordersfeedback_2 = (TextView) findViewById(R.id.tv_ordersfeedback_2);
		tv_ordersfeedback_4 = (TextView) findViewById(R.id.tv_ordersfeedback_4);
		tv_ordersfeedback_call = (TextView) findViewById(R.id.tv_ordersfeedback_call);
		tv_ordersfeedback_view_route = (TextView) findViewById(R.id.tv_ordersfeedback_view_route);
	}

	private void init() {
		tv_ordersfeedback_view_route.setOnClickListener(this);
		// tv_ordersfeedback_training.setOnClickListener(this);
		tv_ordersfeedback_call.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		rl_home_item_b.setOnClickListener(this);
	}

	String latitude = "", longitude = "", name = "", address = "", star = "";

	private void Assignment(Bundle bundle) {
		// 头像
		ImageListener listener = ImageLoader.getImageListener(
				iv_ordersfeedback_picture, R.drawable.icon_nor_user,
				R.drawable.icon_nor_user);
		mImageLoader.get(bundle.getString("front_photos"), listener);

		tv_ordersfeedback_title.setText(bundle.getString("name"));

		tv_ordersfeedback_time.setText(bundle.getString("dutydate"));

		tv_ordersfeedback_site.setText(bundle.getString("address"));

		tv_ordersfeedback_4.setText(bundle.getString("made"));

		if ("0".equals(bundle.getString("standard_price"))) {
			tv_ordersfeedback_2.setText(bundle.getString("suite_price"));
		} else {
			tv_ordersfeedback_2.setText(bundle.getString("standard_price"));
		}
		latitude = bundle.getString("latitude").toString();
		longitude = bundle.getString("longitude").toString();
		phone = bundle.get("phone").toString();

		name = bundle.get("name").toString();
		address = bundle.get("address").toString();
		star = bundle.get("star").toString();
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		// case R.id.tv_ordersfeedback_voice:// 语音播报
		// TTSController.getInstance(OrdersFeedbackActivity.this).playText(
		// tv_ordersfeedback_title.getText() + "于"
		// + tv_ordersfeedback_time.getText() + "到"
		// + tv_ordersfeedback_site.getText() + "。您预计可挣"
		// + tv_ordersfeedback_money.getText() + "元");
		// break;
		case R.id.tv_ordersfeedback_cancel_order:// 取消订单
			break;

		case R.id.tv_ordersfeedback_view_route:// 查看路线
			String url = "http://m.amap.com/?from=" + application.getLatitude()
					+ "," + application.getLongitude() + "("+application.getDesc()+")&to="
					+ latitude + "," + longitude + "("+name+")&type=1&opt=1";
			intent = new Intent(OrdersFeedbackActivity.this,
					WebPathActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "查看路线");

			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
			break;
		case R.id.tv_ordersfeedback_training:// 查看培训与留言
			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.rl_home_item_b:// 地图显示位置
			intent = new Intent(OrdersFeedbackActivity.this,
					MarkerActivity.class);
			intent.putExtra("latitude", latitude);
			intent.putExtra("longitude", longitude);
			intent.putExtra("hotelratingbar", star);
			intent.putExtra("hoteltitle", name);
			intent.putExtra("hotelsnippet", address);
			startActivity(intent);
			break;

		case R.id.tv_ordersfeedback_call:// 打电话
			popTheirProfile(phone);

			break;

		default:
			break;
		}

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
				Utils.calls(tel,
						OrdersFeedbackActivity.this);

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

	private void detail(String url, String token, String userid,
			String orderid, boolean loadedtype) {
		showWaitDialog();

		url = url + "?apptype=2&token=" + token + "&userid=" + userid
				+ "&orderid=" + orderid;

		HashMap map = new HashMap<String, String>();
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET,
				OrdersFeedbackActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(OrdersFeedbackActivity.this),
				HttpStaticApi.detail_Http, null, loadedtype);
	}
}

