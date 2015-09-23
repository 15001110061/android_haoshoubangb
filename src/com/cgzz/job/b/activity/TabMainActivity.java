package com.cgzz.job.b.activity;

import com.cgzz.job.b.R;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

public class TabMainActivity extends TabActivity implements OnClickListener {
	private TabHost tabHost;
	private TextView main_tab_new_message;
	ImageButton buttom_1, buttom_2, buttom_3, buttom_4;
	private View currentButton;
	private String cityname = "", cityid = "",type="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabmain);
		Utils.closeActivity();
		Intent intents = getIntent();
		cityname = intents.getStringExtra("cityname");
		cityid = intents.getStringExtra("cityid");
		type = intents.getStringExtra("type");
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		// MainHomeActivity MainHomePageFragment
		intent = new Intent().setClass(this, MainHomeActivity.class);// 首页
		intent.putExtra("cityname", cityname);
		intent.putExtra("cityid", cityid);
		spec = tabHost.newTabSpec("1").setIndicator("1").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MainOrdersFragment.class);// 订单
		spec = tabHost.newTabSpec("2").setIndicator("2").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, RechargeActivity.class);// 统计
		spec = tabHost.newTabSpec("3").setIndicator("3").setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, MainMyFragment.class);// 我的

		spec = tabHost.newTabSpec("4").setIndicator("4").setContent(intent);
		tabHost.addTab(spec);

		buttom_1 = (ImageButton) findViewById(R.id.buttom_1);
		buttom_2 = (ImageButton) findViewById(R.id.buttom_2);
		buttom_3 = (ImageButton) findViewById(R.id.buttom_3);
		buttom_4 = (ImageButton) findViewById(R.id.buttom_4);


		buttom_1.setOnClickListener(this);
		buttom_2.setOnClickListener(this);
		buttom_3.setOnClickListener(this);
		buttom_4.setOnClickListener(this);

		
		
		if ("4".equals(type)) {
			tabHost.setCurrentTab(1);
			buttom_2.performClick();
		} else {
			tabHost.setCurrentTab(0);
			buttom_1.performClick();
		}
		
		
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.buttom_1://
			tabHost.setCurrentTabByTag("1");
			break;
		case R.id.buttom_2://
			tabHost.setCurrentTabByTag("2");
			break;
		case R.id.buttom_3://
			tabHost.setCurrentTabByTag("3");
			break;
		case R.id.buttom_4://
			tabHost.setCurrentTabByTag("4");
			break;
		default:
			break;
		}
		setButton(arg0);
	}

	private void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	private long firstTime = 0;

	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 1000) { // 如果两次按键时间间隔大于2秒，则不退出
			ToastUtil.makeShortText(this, "再按一次退出程序");
			firstTime = secondTime;// 更新firstTime
		} else {
			// 两次按键小于2秒时，退出应用
			Utils.closeActivity();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			// 需要监听的事件
			onBackPressed();
			return false;
		}
		return super.dispatchKeyEvent(event);
	}
}