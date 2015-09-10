package com.cgzz.job.b.activity;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

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
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm ��ҳ����
 */
public class MainMyFragment extends BaseActivity implements OnClickListener {
	private TextView tv_my_name, tv_my_describe, iv_my_income, iv_my_collection, iv_my_news, iv_my_recommend,
			tv_my_seting, iv_seting_about, iv_seting_opinion, tv_their_tel, tv_me_name2;
	// RelativeLayout rl_my_workcard;

	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.myhotelB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMyhotel(data);
					application.setBalance(bundle.getString("balance").toString());
					application.setPhone(bundle.getString("phone").toString());// �Ƶ�绰
					application.setAddress(bundle.getString("address").toString());// �Ƶ��ַ
					application.setPriority(bundle.getString("priority").toString());// �Ƿ����޸ľƵ���Ϣ
																						// 1����
																						// 0����
					application.setName(bundle.getString("name").toString());

					application.setLatitudeHotel(bundle.getString("latitude").toString());
					application.setLongitudeHotel(bundle.getString("longitude").toString());
					application.setReviewHotel(bundle.getString("reviewHotel").toString());
					application.setHotelid(bundle.getString("hotelid").toString());

					iv_my_chongzhi.setText("��" + application.getBalance());

					if (!Utils.isEmpty(bundle.getString("jifen"))) {
						application.setJifen(bundle.getString("jifen").toString());

						tv_my_jifen.setText(bundle.getString("jifen") + "����");
					}

					if (!Utils.isEmpty(bundle.getString("department"))) {
						tv_me_name2.setVisibility(View.VISIBLE);
						tv_me_name2.setText(bundle.getString("department") + "");
					} else {
						tv_me_name2.setVisibility(View.GONE);
					}
					// "reviewHotel": 1,//1:���ͨ����0������У�-1�����δͨ����2���޾Ƶ���Ϣ
					// if ("1".equals(application.getReviewHotel())) {
					// tv_my_describe.setVisibility(View.VISIBLE);
					// tv_my_describe.setText(application.getName());
					//
					// } else {
					// tv_my_describe.setVisibility(View.GONE);
					// }

					
					
					
					
					if ("1".equals(application.getReviewHotel())) {
						tv_my_describe.setVisibility(View.VISIBLE);
						tv_my_describe.setText(application.getName());
						tv_my_describe2.setVisibility(View.GONE);
					} else if ("-1".equals(application.getReviewHotel())) {
						tv_my_describe2.setVisibility(View.VISIBLE);
						tv_my_describe2.setText("���δͨ��,����������");
						tv_my_describe.setVisibility(View.GONE);
					} else if ("0".equals(application.getReviewHotel())) {
						tv_my_describe2.setVisibility(View.VISIBLE);
						tv_my_describe2.setText("�����");
						tv_my_describe.setVisibility(View.GONE);
					} else if ("2".equals(application.getReviewHotel())) {
						tv_my_describe2.setVisibility(View.VISIBLE);
						tv_my_describe2.setText("�����ҵ��Ϣ");
						tv_my_describe.setVisibility(View.GONE);
					} else {
						tv_my_describe.setVisibility(View.GONE);

					}
					
					
					
					
					
					
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mine_my);
		// setTitle("�ð���", false, TITLE_TYPE_IMG, R.drawable.stub_back, false,
		// TITLE_TYPE_TEXT, "ע��");
		findView();
		init();
		Assignment();
		// ΢��ע���ʼ��
		// wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
		// wxApi.registerApp(WX_APP_ID);
		addWXPlatform();
		addSMS();
	}

	private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	/**
	 * @�������� : ���΢��ƽ̨����
	 * @return AppID��wx21282fa0dad9d60e
	 *         AppSecret��2dbe367ac38677e2db55fcf8f2f9bb9d
	 */
	private void addWXPlatform() {
		// ע�⣺��΢����Ȩ��ʱ�򣬱��봫��appSecret
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appId = "wx2522cb04ae6b46af";
		String appSecret = "7a407e911dd7bf3333b514679170a733";
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(MainMyFragment.this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(MainMyFragment.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		wxCircleHandler.showCompressToast(false);
	}

	/**
	 * ��Ӷ���ƽ̨
	 */
	private void addSMS() {
		// ��Ӷ���
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	TextView tv_my_jifen, iv_my_chongzhi, tv_my_describe2;
	// ShareUtil shareUtil;
	private PopupWindow pwShare;
	private View viewShare;
	private RelativeLayout rlMessage, rlWechat, rlWeMoment, rl_my_1, rl_my_2;
	ImageView iv_my_pic;

	private void findView() {

		rl_my_1 = (RelativeLayout) findViewById(R.id.rl_my_1);
		rl_my_2 = (RelativeLayout) findViewById(R.id.rl_my_2);

		if (application.isLogon()) {
			rl_my_1.setVisibility(View.VISIBLE);
			rl_my_2.setVisibility(View.GONE);
		} else {
			rl_my_1.setVisibility(View.GONE);
			rl_my_2.setVisibility(View.VISIBLE);

		}

		iv_my_pic = (ImageView) findViewById(R.id.iv_my_pic);
		// rl_my_workcard = (RelativeLayout) findViewById(R.id.rl_my_workcard);
		tv_my_name = (TextView) findViewById(R.id.tv_my_name);
		tv_my_describe = (TextView) findViewById(R.id.tv_my_describe);
		iv_seting_about = (TextView) findViewById(R.id.iv_seting_about);
		iv_my_income = (TextView) findViewById(R.id.iv_my_income);
		iv_my_collection = (TextView) findViewById(R.id.iv_my_collection);
		iv_my_news = (TextView) findViewById(R.id.iv_my_news);
		iv_my_recommend = (TextView) findViewById(R.id.iv_my_recommend);
		tv_my_seting = (TextView) findViewById(R.id.tv_my_seting);
		iv_seting_opinion = (TextView) findViewById(R.id.iv_seting_opinion);
		tv_my_jifen = (TextView) findViewById(R.id.tv_my_jifen);
		iv_my_chongzhi = (TextView) findViewById(R.id.iv_my_chongzhi);
		tv_me_name2 = (TextView) findViewById(R.id.tv_me_name2);
		tv_my_describe2 = (TextView) findViewById(R.id.tv_my_describe2);
		// ShareSDK.initSDK(this);
		// shareUtil = new ShareUtil(this, this);

		viewShare = LayoutInflater.from(this).inflate(R.layout.share_popup, null);
		ImageButton dis = (ImageButton) viewShare.findViewById(R.id.ib_dis);
		rlWechat = (RelativeLayout) viewShare.findViewById(R.id.relative_wechat);
		rlWeMoment = (RelativeLayout) viewShare.findViewById(R.id.relative_wemoment);
		rlMessage = (RelativeLayout) viewShare.findViewById(R.id.relative_shortmessage);

		pwShare = new PopupWindow(viewShare);
		pwShare.setBackgroundDrawable(new BitmapDrawable());// û�д˾����ⲿ������ʧ
		pwShare.setOutsideTouchable(true);
		pwShare.setFocusable(true);
		pwShare.setAnimationStyle(R.style.MyPopupAnimation);
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pwShare.dismiss();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (application.isLogon()) {
			
			
			tv_my_name.setText(application.getRealname());
			iv_my_chongzhi.setText(application.getBalance());
			
			// ͷ��
			ImageListener listener = ImageLoader.getImageListener(iv_my_pic, R.drawable.icon_touxiangmoren,
					R.drawable.icon_touxiangmoren);
			try {
				mImageLoader.get(application.getFaceUrl(), listener);
			} catch (Exception e) {
				// TODO: handle exception
			}
		
			tv_my_name.setText(application.getRealname());

			if ("1".equals(application.getReviewHotel())) {
				tv_my_describe.setVisibility(View.VISIBLE);
				tv_my_describe.setText(application.getName());
				tv_my_describe2.setVisibility(View.GONE);
			} else if ("-1".equals(application.getReviewHotel())) {
				tv_my_describe2.setVisibility(View.VISIBLE);
				tv_my_describe2.setText("���δͨ��,����������");
				tv_my_describe.setVisibility(View.GONE);
			} else if ("0".equals(application.getReviewHotel())) {
				tv_my_describe2.setVisibility(View.VISIBLE);
				tv_my_describe2.setText("�����");
				tv_my_describe.setVisibility(View.GONE);
			} else if ("2".equals(application.getReviewHotel())) {
				tv_my_describe2.setVisibility(View.VISIBLE);
				tv_my_describe2.setText("�����ҵ��Ϣ");
				tv_my_describe.setVisibility(View.GONE);
			} else {
				tv_my_describe.setVisibility(View.GONE);

			}

			// "reviewHotel": 1,//1:���ͨ����0������У�-1�����δͨ����2���޾Ƶ���Ϣ

			myhotel(UrlConfig.myhotelB_Http, application.getToken(), application.getUserId(), true);
		} else {
		}

	}

	private void init() {
		// rl_my_workcard.setOnClickListener(this);
		iv_my_income.setOnClickListener(this);
		iv_my_news.setOnClickListener(this);
		iv_my_collection.setOnClickListener(this);
		tv_my_seting.setOnClickListener(this);
		iv_my_recommend.setOnClickListener(this);
		iv_seting_about.setOnClickListener(this);
		iv_seting_opinion.setOnClickListener(this);
		iv_my_pic.setOnClickListener(this);
		tv_my_describe.setOnClickListener(this);
		tv_my_name.setOnClickListener(this);

		rlMessage.setOnClickListener(this);
		rlWechat.setOnClickListener(this);
		rlWeMoment.setOnClickListener(this);
		rl_my_2.setOnClickListener(this);
		rl_my_1.setOnClickListener(this);
	}

	private void Assignment() {
		// ͷ��
//		if (!Utils.isEmpty(application.getFaceUrl())) {
//			ImageListener listener = ImageLoader.getImageListener(iv_my_pic, R.drawable.icon_nor_user,
//					R.drawable.icon_nor_user);
//			mImageLoader.get(application.getFaceUrl(), listener);
//		}

//		tv_my_name.setText(application.getRealname());
//		iv_my_chongzhi.setText(application.getBalance());
		// "reviewHotel": 1,//1:���ͨ����0������У�-1�����δͨ����2���޾Ƶ���Ϣ

//		if ("1".equals(application.getReviewHotel())) {
//			tv_my_describe.setVisibility(View.VISIBLE);
//			tv_my_describe.setText(application.getName());
//		} else {
//			tv_my_describe.setVisibility(View.GONE);
//		}
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.iv_my_collection:// �ҵ��ղ�
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, CollectionActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		case R.id.iv_my_recommend:// �Ƽ�������

			if (application.isLogon()) {
				pwShare.setWidth(LayoutParams.MATCH_PARENT);
				pwShare.setHeight(LayoutParams.WRAP_CONTENT);
				pwShare.showAtLocation(arg0, Gravity.BOTTOM, 0, 0);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		case R.id.tv_my_seting:// ��������
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, SetingActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;

		case R.id.iv_seting_about:// ����
			intent = new Intent(MainMyFragment.this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_seting_opinion:// �������
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, OpinionActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;

		case R.id.rl_my_1:// ��������

		case R.id.tv_my_name:
		case R.id.tv_my_describe:
		case R.id.iv_my_pic:
			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, TheirProfileActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;

		case R.id.relative_wechat:// ΢�ź���
			pwShare.dismiss();
			UMImage urlImage = new UMImage(MainMyFragment.this, R.drawable.ic_splash);

			WeiXinShareContent weixinContent = new WeiXinShareContent();
			weixinContent.setShareContent("�ú��ְ�App�������ٻ��ͷ������Ա������רҵ��ȫ���̸��٣���ȫʡ�ģ�Ч�ʴ����ߡ�");
			weixinContent.setTitle("�շ�����һ���Ƶ��ù�����");
			weixinContent.setTargetUrl("http://www.haoshoubang.com/m/index.html");
			weixinContent.setShareMedia(urlImage);
			mController.setShareMedia(weixinContent);

			mController.directShare(MainMyFragment.this, SHARE_MEDIA.WEIXIN, new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
					// String showText = "����ɹ�";
					// if (eCode != StatusCode.ST_CODE_SUCCESSED) {
					// showText = "����ʧ�� [" + eCode + "]";
					// }
					// Toast.makeText(MainMyFragment.this, showText,
					// Toast.LENGTH_SHORT).show();
				}
			});

			break;

		case R.id.relative_wemoment:// ����Ȧ
			pwShare.dismiss();
			UMImage urlImage2 = new UMImage(MainMyFragment.this, R.drawable.ic_splash);
			// ��������Ȧ���������
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent("�ú��ְ�App�������ٻ��ͷ������Ա������רҵ��ȫ���̸��٣���ȫʡ�ģ�Ч�ʴ����ߡ�");
			circleMedia.setTitle("�շ�����һ���Ƶ��ù�����");
			circleMedia.setShareMedia(urlImage2);
			circleMedia.setTargetUrl("http://www.haoshoubang.com/m/index.html");
			mController.setShareMedia(circleMedia);

			mController.directShare(MainMyFragment.this, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				}
			});

			break;
		case R.id.relative_shortmessage:// ����
			pwShare.dismiss();
			SmsShareContent sms = new SmsShareContent();
			sms.setShareContent("�ո����˺��ְ�app�����������˿ͷ������Ա��������ã�����רҵ������ȫ���٣�Ч�ʴ����ߣ��������ݷ��������ġ�����������t.im/hsbbc");
			mController.setShareMedia(sms);

			mController.directShare(MainMyFragment.this, SHARE_MEDIA.SMS, new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
				}
			});

			break;
		case R.id.rl_my_2:// ��¼
			intent = new Intent(MainMyFragment.this, LoginActivity.class);
			startActivity(intent);
			finish();

			break;
		case R.id.iv_my_news:// ��Ϣ����

			if (application.isLogon()) {
				intent = new Intent(MainMyFragment.this, MessageCenterActivity.class);
				startActivity(intent);
			} else {
				ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
			}

			break;
		// case R.id.iv_my_income:// ��ֵ
		// if (application.isLogon()) {
		//
		// intent = new Intent(MainMyFragment.this,
		// RechargeActivity.class);
		// startActivity(intent);
		//
		//
		// } else {
		// ToastUtil.makeShortText(MainMyFragment.this, "���Ƚ��е�¼");
		// }
		//
		// break;
		default:
			break;
		}

	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void myhotel(String url, String token, String userid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, MainMyFragment.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(MainMyFragment.this), HttpStaticApi.myhotelB_Http, null, loadedtype);
	}

	// private IWXAPI wxApi;
	/**
	 * ΢�ŷ��� ��������ṩһ��������ҳ��ʾ����������ο�����ʾ�����룩
	 * 
	 * @param flag
	 *            (0:����΢�ź��ѣ�1������΢������Ȧ)
	 */
	// String WX_APP_ID = "wx21282fa0dad9d60e";

	// private void wechatShare(int flag) {
	//
	// WXWebpageObject webpage = new WXWebpageObject();
	// webpage.webpageUrl = "https://open.weixin.qq.com/";
	// WXMediaMessage msg = new WXMediaMessage(webpage);
	// msg.title = "title";
	// msg.description = "message";
	// // �����滻һ���Լ��������ͼƬ��Դ
	// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
	// R.drawable.ic_splash);
	// // msg.setThumbImage(thumb);
	//
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = String.valueOf(System.currentTimeMillis());
	// req.message = msg;
	// req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
	// : SendMessageToWX.Req.WXSceneTimeline;
	// wxApi.sendReq(req);
	// }

}
