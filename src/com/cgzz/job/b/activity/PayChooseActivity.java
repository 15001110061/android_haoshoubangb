package com.cgzz.job.b.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.BaseActivityCloseListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.pay.PayResult;
import com.cgzz.job.b.pay.SignUtils;
import com.cgzz.job.b.utils.ToastUtil;

/***
 * @author wjm 选择支付方式
 */
public class PayChooseActivity extends BaseActivity implements OnClickListener {
	// 商户PID
	public static final String PARTNER = "2088021388300830";
	// 商户收款账号
	public static final String SELLER = "sihu@haoshoubang.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL/guBMt/Bma0uPvNc2YrDiLn3ZYGyJzIwo9cZCijOn37N8QIk80Hx32KP2+pfQ22nSv0L6Jf2bMbsOQ/3Z+kP3oAgwRkBIWL8/aVtdz0CO3/gNKdCnKGAS5p53N73WL2RN24r0OdKbAuzuDjsUWQGR58kvQiJC4eAwOYchfN1x3AgMBAAECgYAt1NljC2raGtcgxrSNRVhtsTwHoofotKO8AG0t5QXPpP6ZUVtAm+yK1Y94/J7bNHb9xDkSKfmabl5OrEW44gZzlaMjaVNbxbGNX3aWDIRcEov0HQp9x2f7kms3IZwsJuaJrrQ3Qh9utvB/hPL/Bnkk6Xvy3+e9/90Qvezir1frMQJBAN/SN2NSNWNi7CwUGmHwnfdpANMeicABlnRC4PYc7uP94YUgqB/YegSNapjWzxGHuGVEvRvvLyFUAuOtgJcTgBUCQQDbdtI4UbrtY+GrQJZ9aHVRgfaTv0n8+dQdce0SpDdja9mU515jGiZJIKkF49JorA6meRbNi3MQAm994o0eFcFbAkB+ZWamDjzcHXcmBUxI1us+VuwCTZKY/cyLZ2FHW1uFIVCEL8cCBOwTOhFispxJWA3IEqOA7Pf+qEThco/VwUtpAkBGR3+8b1fYC4NJ7w4CLQBc0Kyg1cIg0/Q8Va5gqNvf57qut4T/YXFfO6lE7JcF1AuA/gfyXLTFLLJ9cp5UQw0/AkB1y/ugwCDHsuxh9F2Br8RwZpL0/FafOTh61MnMGnlTVtdjZKeSKwPAqe64pBLBwUBUOS2oKqLbxKdUXyOfoIZu";
	// 支付宝公钥
	// public static final String RSA_PUBLIC = "";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.orderinfoB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserOrderinfo(data);
					// OrderInfo += "&notify_url=" + "\"" +
					// bundle.getString("notify_url") + "\"";
					// OrderInfo += "&out_trade_no=" + "\"" +
					// bundle.getString("orderno") + "\"";
					pay(bundle.getString("orderInfo") + "", OrderInfo);

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
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
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					ToastUtil.makeShortText(PayChooseActivity.this, "支付成功");
					refreshOrders();
					refreshSampled();
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.makeShortText(PayChooseActivity.this, "支付结果确认中");

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						ToastUtil.makeShortText(PayChooseActivity.this, "支付失败");

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				ToastUtil.makeShortText(PayChooseActivity.this, "检查结果为：" + msg.obj);
				break;
			}
			default:
				break;
			}
		};
	};

	String totalPrice = "", isSign = "", id = "", orderno = "", iscash = "", enddate = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paychoose);
		setTitle("选择支付方式", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");

		application.putClosePath(UrlConfig.PATH_KEY_PAY, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});

		Intent intent = getIntent();
		totalPrice = intent.getStringExtra("totalPrice");
		isSign = intent.getStringExtra("isSign");
		id = intent.getStringExtra("id");
		orderno = intent.getStringExtra("orderno");
		iscash = intent.getStringExtra("iscash");
		enddate = intent.getStringExtra("enddate");
		findView();
		init();

	}

	LinearLayout llLeft, llright;
	RelativeLayout rl_paychoose_4, rl_paychoose_1, rl_paychoose_2, rl_paychoose_3;
	TextView tv_paycash_titles;

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		rl_paychoose_4 = (RelativeLayout) findViewById(R.id.rl_paychoose_4);
		rl_paychoose_3 = (RelativeLayout) findViewById(R.id.rl_paychoose_3);
		rl_paychoose_2 = (RelativeLayout) findViewById(R.id.rl_paychoose_2);
		rl_paychoose_1 = (RelativeLayout) findViewById(R.id.rl_paychoose_1);
		tv_paycash_titles = (TextView) findViewById(R.id.tv_paycash_titles);
		tv_paycash_titles.setText("￥" + totalPrice);
		if ("0".equals(isSign)) {
			rl_paychoose_4.setVisibility(View.GONE);
		} else {
			rl_paychoose_4.setVisibility(View.VISIBLE);
		}

		if ("1".equals(iscash)) {
			rl_paychoose_1.setVisibility(View.VISIBLE);
			rl_paychoose_2.setVisibility(View.GONE);
			rl_paychoose_3.setVisibility(View.GONE);
			rl_paychoose_4.setVisibility(View.GONE);

		} else {
			rl_paychoose_1.setVisibility(View.GONE);

		}

	}

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		rl_paychoose_1.setOnClickListener(this);
		rl_paychoose_2.setOnClickListener(this);
		rl_paychoose_3.setOnClickListener(this);
		rl_paychoose_4.setOnClickListener(this);
	}

	String OrderInfo = "";

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {

		case R.id.rl_paychoose_1:// 现金支付

			intent = new Intent(PayChooseActivity.this, PayCashActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("totalPrice", totalPrice);
			bundle1.putString("id", id);
			intent.putExtras(bundle1);
			startActivity(intent);
			break;

		case R.id.rl_paychoose_2:// 支付宝
			OrderInfo = createLinkString(getOrderInfo("好手帮清洁交易费用", "客房清洁费用", totalPrice+"", orderno));
			orderinfo(UrlConfig.orderinfoB_Http, application.getToken(), application.getUserId(), OrderInfo, id, true);
			break;
		case R.id.rl_paychoose_3:// 向好手帮转账

			intent = new Intent(PayChooseActivity.this, PayTransferActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("totalPrice", totalPrice);
			bundle2.putString("id", id);
			bundle2.putString("enddate", enddate);

			intent.putExtras(bundle2);
			startActivity(intent);

			break;

		case R.id.rl_paychoose_4:// 我是与好手帮签约的企业
			intent = new Intent(PayChooseActivity.this, PaySignActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putString("totalPrice", totalPrice);
			bundle3.putString("id", id);

			intent.putExtras(bundle3);
			startActivity(intent);

			break;
		case R.id.ll_title_left://
			onBackPressed();
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		refreshOrders();
		finish();
	}

	public void refreshOrders() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshOrders");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	public void refreshSampled() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshSampled");
		intentobd.putExtra("refresh", "1");// 1刷新
		sendBroadcast(intentobd);
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String signs, String orderInfo) {
		// 订单
		// String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", totalPrice);
		// 对订单做RSA 签名
		// String sign = sign(signs);
		try {
			// 仅需对sign 做URL编码
			signs = URLEncoder.encode(signs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + signs + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayChooseActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayChooseActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static Map<String, String> getOrderInfo(String subject, String body, String price, String orderno) {
		Map<String, String> map = new HashMap<String, String>();

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
		map.put("partner", "\"" + PARTNER + "\"");
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
		map.put("seller_id", "\"" + SELLER + "\"");
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderno + "\"";
		map.put("out_trade_no", "\"" + orderno + "\"");
		// orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		map.put("subject", "\"" + subject + "\"");
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		map.put("body", "\"" + body + "\"");
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		map.put("total_fee", "\"" + price + "\"");
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + UrlConfig.notify_url + "\"";
		map.put("notify_url", "\"" + UrlConfig.notify_url + "\"");
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		map.put("service", "\"mobile.securitypay.pay\"");
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		map.put("payment_type", "\"1\"");
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		map.put("_input_charset", "\"utf-8\"");
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		map.put("it_b_pay", "\"30m\"");
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		map.put("return_url", "\"m.alipay.com\"");
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return map;
	}

	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15) + application.getUserId();
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private void orderinfo(String url, String token, String userid, String orderinfo, String orderid,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("orderInfo", orderinfo);
		map.put("orderid", orderid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, PayChooseActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(PayChooseActivity.this), HttpStaticApi.orderinfoB_Http, null,
				loadedtype);
	}

}
