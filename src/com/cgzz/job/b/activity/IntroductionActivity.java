package com.cgzz.job.b.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.OnViewChangeListener;
import com.cgzz.job.b.view.ScrollLayout;

public class IntroductionActivity extends BaseActivity implements
		OnViewChangeListener {

	private ScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private RelativeLayout startBtn;
	private LinearLayout pointLLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduction);
		// PackageManager pm = getPackageManager();
		// PackageInfo pi = null;
		// try {
		// pi = pm.getPackageInfo("com.llkj.pinpin", 0);
		// } catch (NameNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		getSharedPreferences(
				"FIRST" + Utils.getVersionName(IntroductionActivity.this), 0)
				.edit().putBoolean("FIRST", false).commit();
		initView();
	}
	Button startBtn1;
	private void initView() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);
		startBtn = (RelativeLayout) findViewById(R.id.startBtn);
		startBtn1= (Button) findViewById(R.id.startBtn1);
		startBtn.setOnClickListener(onClick);
		startBtn1.setOnClickListener(onClick);
		count = mScrollLayout.getChildCount();

		// Typeface fontFace = Typeface.createFromAsset(getAssets(),
		// "fzktjt.ttf");
		// startBtn.setTypeface(fontFace);

		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				redirect();
				break;
			case R.id.startBtn1:
				redirect();
				break;
			}
		}
	};

	private void redirect() {
		if (application.isLogon()) {
			startActivity(new Intent(IntroductionActivity.this,
					TabMainActivity.class));
		} else {
			startActivity(new Intent(IntroductionActivity.this,
					LoginActivity.class));
		}

		finish();
	}

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}
}