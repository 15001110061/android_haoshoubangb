package com.cgzz.job.b.activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.TTSController;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomDialog;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechUser;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import cn.jpush.android.api.InstrumentedActivity;

public class SplashActivity extends InstrumentedActivity implements AMapLocationListener, Runnable {
	private GlobalVariables application;
	Handler handlers = new Handler();
	// AlertDialog show;
	Object downloadurl = null;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			Bundle bundle = null;
			Message msg = new Message();
			switch (method) {
			case HttpStaticApi.version_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					bundle = ParserUtil.ParserVersion(data);
					downloadurl = bundle.get("downloadurl");
					if (downloadurl != null) {
						final String url2 = bundle.get("downloadurl").toString();
						if ("1".equals(bundle.get("forced_upgrade").toString())) {
							CustomDialog.alertDialog(SplashActivity.this, false, false, true, null,
									bundle.get("message").toString(), new CustomDialog.PopUpDialogListener() {

								@Override
								public void doPositiveClick(Boolean isOk) {
									if (isOk) {// 确定
										String url = url2;
										Intent intent = new Intent(Intent.ACTION_VIEW);
										intent.setData(Uri.parse(url));
										startActivity(intent);
										openActivity();
									} else {
										openActivity();

									}

								}
							});

						} else {
							CustomDialog.alertDialog(SplashActivity.this, false, true, true, null,
									bundle.get("message").toString(), new CustomDialog.PopUpDialogListener() {

								@Override
								public void doPositiveClick(Boolean isOk) {
									if (isOk) {// 确定
										String url = url2;
										Intent intent = new Intent(Intent.ACTION_VIEW);
										intent.setData(Uri.parse(url));
										startActivity(intent);
										openActivity();
									} else {
										openActivity();

									}

								}
							});
						}

					}

					break;
				case HttpStaticApi.FAILURE_HTTP:
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
	Boolean user_first;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.layout_splash);
		application = (GlobalVariables) getApplicationContext();

		SharedPreferences setting = getSharedPreferences("FIRST" + Utils.getVersionName(SplashActivity.this), 0);
		user_first = setting.getBoolean("FIRST", true);

		// 科大
		SpeechUser.getUser().login(SplashActivity.this, null, null, "appid=" + getString(R.string.app_id), listener);
		TTSController ttsManager = TTSController.getInstance(this);// 初始化语音模块
		ttsManager.init();
		TTSController.getInstance(this).startSpeaking();

		// 友盟
		MobclickAgent.setDebugMode(true);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);

		Location();

	}

	private void Location() {
		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 4000);

	}

	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener() {

		@Override
		public void onData(byte[] arg0) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {
			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}
	};

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();

	@Override
	public void run() {
		if (aMapLocation == null) {
//			ToastUtil.makeShortText(SplashActivity.this, "定位失败");
			stopLocation();// 销毁掉定位

			openActivity();

		} else {
			if (downloadurl == null)
				openActivity();
		}

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			stopLocation();// 销毁掉定位
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			// String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
			// + "\n精 度 :" + location.getAccuracy() + "米"
			// + "\n定位方式:" + location.getProvider() + "\n城市编码:" + cityCode
			// + "\n位置描述:" + desc + "\n省:" + location.getProvince()
			// + "\n市:" + location.getCity() + "\n区(县):"
			// + location.getDistrict() + "\n区域编码:" + location.getAdCode());
			// ToastUtil.makeShortText(SplashActivity.this, "" + str);
			application.setLatitude(geoLat + "");
			application.setLongitude(geoLng + "");
			application.setCityCode(cityCode);
			application.setCityName(location.getCity());
			application.setDesc(desc);
			getVersion(UrlConfig.version_Http, true);

		}

	}

	private LocationManagerProxy aMapLocManager = null;

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();// 停止定位
	}

	/**
	 * 版本更新
	 */
	private void getVersion(String url, boolean loadedtype) {
		PackageManager manager;
		PackageInfo info = null;
		manager = this.getPackageManager();
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		url = url + "?version=" + info.versionCode + "&apptype=1";
		;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, SplashActivity.this, url, null, callbackData,
				GlobalVariables.getRequestQueue(SplashActivity.this), HttpStaticApi.version_Http, null, loadedtype);

	}

	private void openActivity() {
		if (user_first) {// 第一次
			Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
			startActivity(intent);
			finish();

		} else {

			// if (application.isLogon()) {

			if (application.isGestures()) {
				Intent intent = new Intent(SplashActivity.this, GestureVerifyActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(SplashActivity.this, TabMainActivity.class);
				intent.putExtra("cityname", application.getCityName());
				intent.putExtra("cityid", application.getCityCode());
				startActivity(intent);
			}

			finish();
		}
	}
}
