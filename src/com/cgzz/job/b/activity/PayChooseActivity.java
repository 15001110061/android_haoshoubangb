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
 * @author wjm ѡ��֧����ʽ
 */
public class PayChooseActivity extends BaseActivity implements OnClickListener {
	// �̻�PID
	public static final String PARTNER = "2088021388300830";
	// �̻��տ��˺�
	public static final String SELLER = "sihu@haoshoubang.com";
	// �̻�˽Կ��pkcs8��ʽ
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL/guBMt/Bma0uPvNc2YrDiLn3ZYGyJzIwo9cZCijOn37N8QIk80Hx32KP2+pfQ22nSv0L6Jf2bMbsOQ/3Z+kP3oAgwRkBIWL8/aVtdz0CO3/gNKdCnKGAS5p53N73WL2RN24r0OdKbAuzuDjsUWQGR58kvQiJC4eAwOYchfN1x3AgMBAAECgYAt1NljC2raGtcgxrSNRVhtsTwHoofotKO8AG0t5QXPpP6ZUVtAm+yK1Y94/J7bNHb9xDkSKfmabl5OrEW44gZzlaMjaVNbxbGNX3aWDIRcEov0HQp9x2f7kms3IZwsJuaJrrQ3Qh9utvB/hPL/Bnkk6Xvy3+e9/90Qvezir1frMQJBAN/SN2NSNWNi7CwUGmHwnfdpANMeicABlnRC4PYc7uP94YUgqB/YegSNapjWzxGHuGVEvRvvLyFUAuOtgJcTgBUCQQDbdtI4UbrtY+GrQJZ9aHVRgfaTv0n8+dQdce0SpDdja9mU515jGiZJIKkF49JorA6meRbNi3MQAm994o0eFcFbAkB+ZWamDjzcHXcmBUxI1us+VuwCTZKY/cyLZ2FHW1uFIVCEL8cCBOwTOhFispxJWA3IEqOA7Pf+qEThco/VwUtpAkBGR3+8b1fYC4NJ7w4CLQBc0Kyg1cIg0/Q8Va5gqNvf57qut4T/YXFfO6lE7JcF1AuA/gfyXLTFLLJ9cp5UQw0/AkB1y/ugwCDHsuxh9F2Br8RwZpL0/FafOTh61MnMGnlTVtdjZKeSKwPAqe64pBLBwUBUOS2oKqLbxKdUXyOfoIZu";
	// ֧������Կ
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

				// ֧�������ش˴�֧���������ǩ�������֧����ǩ����Ϣ��ǩԼʱ֧�����ṩ�Ĺ�Կ����ǩ
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// �ж�resultStatus Ϊ��9000�������֧���ɹ�������״̬�������ɲο��ӿ��ĵ�
				if (TextUtils.equals(resultStatus, "9000")) {
					ToastUtil.makeShortText(PayChooseActivity.this, "֧���ɹ�");
					refreshOrders();
					refreshSampled();
					finish();
				} else {
					// �ж�resultStatus Ϊ�ǡ�9000����������֧��ʧ��
					// ��8000������֧�������Ϊ֧������ԭ�����ϵͳԭ���ڵȴ�֧�����ȷ�ϣ����ս����Ƿ�ɹ��Է�����첽֪ͨΪ׼��С����״̬��
					if (TextUtils.equals(resultStatus, "8000")) {
						ToastUtil.makeShortText(PayChooseActivity.this, "֧�����ȷ����");

					} else {
						// ����ֵ�Ϳ����ж�Ϊ֧��ʧ�ܣ������û�����ȡ��֧��������ϵͳ���صĴ���
						ToastUtil.makeShortText(PayChooseActivity.this, "֧��ʧ��");

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				ToastUtil.makeShortText(PayChooseActivity.this, "�����Ϊ��" + msg.obj);
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
		setTitle("ѡ��֧����ʽ", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");

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
		tv_paycash_titles.setText("��" + totalPrice);
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

		case R.id.rl_paychoose_1:// �ֽ�֧��

			intent = new Intent(PayChooseActivity.this, PayCashActivity.class);
			Bundle bundle1 = new Bundle();
			bundle1.putString("totalPrice", totalPrice);
			bundle1.putString("id", id);
			intent.putExtras(bundle1);
			startActivity(intent);
			break;

		case R.id.rl_paychoose_2:// ֧����
			OrderInfo = createLinkString(getOrderInfo("���ְ���ཻ�׷���", "�ͷ�������", totalPrice+"", orderno));
			orderinfo(UrlConfig.orderinfoB_Http, application.getToken(), application.getUserId(), OrderInfo, id, true);
			break;
		case R.id.rl_paychoose_3:// ����ְ�ת��

			intent = new Intent(PayChooseActivity.this, PayTransferActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("totalPrice", totalPrice);
			bundle2.putString("id", id);
			bundle2.putString("enddate", enddate);

			intent.putExtras(bundle2);
			startActivity(intent);

			break;

		case R.id.rl_paychoose_4:// ��������ְ�ǩԼ����ҵ
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
		intentobd.putExtra("refresh", "1");// 1ˢ��
		sendBroadcast(intentobd);
	}

	public void refreshSampled() {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "refreshSampled");
		intentobd.putExtra("refresh", "1");// 1ˢ��
		sendBroadcast(intentobd);
	}

	/**
	 * call alipay sdk pay. ����SDK֧��
	 * 
	 */
	public void pay(String signs, String orderInfo) {
		// ����
		// String orderInfo = getOrderInfo("���Ե���Ʒ", "�ò�����Ʒ����ϸ����", totalPrice);
		// �Զ�����RSA ǩ��
		// String sign = sign(signs);
		try {
			// �����sign ��URL����
			signs = URLEncoder.encode(signs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// �����ķ���֧���������淶�Ķ�����Ϣ
		final String payInfo = orderInfo + "&sign=\"" + signs + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask alipay = new PayTask(PayChooseActivity.this);
				// ����֧���ӿڣ���ȡ֧�����
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// �����첽����
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * ��ѯ�ն��豸�Ƿ����֧������֤�˻�
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// ����PayTask ����
				PayTask payTask = new PayTask(PayChooseActivity.this);
				// ���ò�ѯ�ӿڣ���ȡ��ѯ���
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
	 * get the sdk version. ��ȡSDK�汾��
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. ����������Ϣ
	 * 
	 */
	public static Map<String, String> getOrderInfo(String subject, String body, String price, String orderno) {
		Map<String, String> map = new HashMap<String, String>();

		// ǩԼ���������ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
		map.put("partner", "\"" + PARTNER + "\"");
		// ǩԼ����֧�����˺�
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
		map.put("seller_id", "\"" + SELLER + "\"");
		// �̻���վΨһ������
		orderInfo += "&out_trade_no=" + "\"" + orderno + "\"";
		map.put("out_trade_no", "\"" + orderno + "\"");
		// orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		// ��Ʒ����
		orderInfo += "&subject=" + "\"" + subject + "\"";
		map.put("subject", "\"" + subject + "\"");
		// ��Ʒ����
		orderInfo += "&body=" + "\"" + body + "\"";
		map.put("body", "\"" + body + "\"");
		// ��Ʒ���
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		map.put("total_fee", "\"" + price + "\"");
		// �������첽֪ͨҳ��·��
		orderInfo += "&notify_url=" + "\"" + UrlConfig.notify_url + "\"";
		map.put("notify_url", "\"" + UrlConfig.notify_url + "\"");
		// ����ӿ����ƣ� �̶�ֵ
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		map.put("service", "\"mobile.securitypay.pay\"");
		// ֧�����ͣ� �̶�ֵ
		orderInfo += "&payment_type=\"1\"";
		map.put("payment_type", "\"1\"");
		// �������룬 �̶�ֵ
		orderInfo += "&_input_charset=\"utf-8\"";
		map.put("_input_charset", "\"utf-8\"");
		// ����δ����׵ĳ�ʱʱ��
		// Ĭ��30���ӣ�һ����ʱ���ñʽ��׾ͻ��Զ����رա�
		// ȡֵ��Χ��1m��15d��
		// m-���ӣ�h-Сʱ��d-�죬1c-���죨���۽��׺�ʱ����������0��رգ���
		// �ò�����ֵ������С���㣬��1.5h����ת��Ϊ90m��
		orderInfo += "&it_b_pay=\"30m\"";
		map.put("it_b_pay", "\"30m\"");
		// extern_tokenΪ���������Ȩ��ȡ����alipay_open_id,���ϴ˲����û���ʹ����Ȩ���˻�����֧��
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// ֧��������������󣬵�ǰҳ����ת���̻�ָ��ҳ���·�����ɿ�
		orderInfo += "&return_url=\"m.alipay.com\"";
		map.put("return_url", "\"m.alipay.com\"");
		// �������п�֧���������ô˲���������ǩ���� �̶�ֵ ����ҪǩԼ���������п����֧��������ʹ�ã�
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

			if (i == keys.size() - 1) {// ƴ��ʱ�����������һ��&�ַ�
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * get the out_trade_no for an order. �����̻������ţ���ֵ���̻���Ӧ����Ψһ�����Զ����ʽ�淶��
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
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * 
	 * @param content
	 *            ��ǩ��������Ϣ
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. ��ȡǩ����ʽ
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
