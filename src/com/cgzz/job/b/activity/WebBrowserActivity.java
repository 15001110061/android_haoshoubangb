package com.cgzz.job.b.activity;

import android.content.Intent;
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

/**
 * 用于显示简单的html的界面.传递title和url即可.
 * 
 * 
 */
public class WebBrowserActivity extends BaseActivity implements OnClickListener {
	/**
	 * 显示的标题.
	 */
	public static final String ACTION_KEY_TITLE = "action_key_title";
	/**
	 * 加载的的url
	 */
	public static final String ACTION_KEY_URL = "action_key_url";
	private WebView webview;
	private ProgressBar progressbar;
	private String title, url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		Intent intent = getIntent();
		title = intent.getStringExtra(ACTION_KEY_TITLE);
		url = intent.getStringExtra(ACTION_KEY_URL);
		setTitle("" + title, true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		initView();
	}

	LinearLayout llLeft, llright;

	private void initView() {

		webview = (WebView) findViewById(R.id.webview);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
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
		if (webview != null)
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
		if (webview != null)
			webview.destroy();
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;

		default:
			break;
		}

	}
}
