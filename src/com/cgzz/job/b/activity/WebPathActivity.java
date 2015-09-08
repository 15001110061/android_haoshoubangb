package com.cgzz.job.b.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.utils.ScreenShot;
import com.cgzz.job.b.utils.ToastUtil;

/**
 * ������ʾ�򵥵�html�Ľ���.����title��url����.
 * 
 * 
 */
public class WebPathActivity extends BaseActivity implements OnClickListener {
	/**
	 * ��ʾ�ı���.
	 */
	public static final String ACTION_KEY_TITLE = "action_key_title";
	/**
	 * ���صĵ�url
	 */
	public static final String ACTION_KEY_URL = "action_key_url";
	private WebView webview;
	private ProgressBar progressbar;
	private String title, url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_path);
		Intent intent = getIntent();
		title = intent.getStringExtra(ACTION_KEY_TITLE);
		url = intent.getStringExtra(ACTION_KEY_URL);
		setTitle(title, true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "����");
		initView();
	}

	LinearLayout llLeft, llright;

	private void initView() {
		webview = (WebView) findViewById(R.id.webview);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);

		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient());
		webview.loadUrl(url);
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(View.GONE);
			} else {
				if (progressbar.getVisibility() == View.GONE)
					progressbar.setVisibility(View.VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			webview.goBack();
			return true;
		} else if (!webview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		webview.destroy();
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.ll_title_right:// ע��
			try {
				String fileName = String.valueOf(System.currentTimeMillis()) + ".png";
				String filePath = HttpStaticApi.Send_TheirProfile + "/" + fileName;
				ScreenShot.shoot(this, new File(filePath));
				ToastUtil.makeShortText(WebPathActivity.this, "�����ɹ�");
			} catch (Exception e) {
				ToastUtil.makeShortText(WebPathActivity.this, "����ʧ��");
			}

			break;

		default:
			break;
		}

	}

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /����ת��ΪBitmapͼƬ

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
