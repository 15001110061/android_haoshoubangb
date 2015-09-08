package com.cgzz.job.b.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.utils.Utils;

/***
 * @author wjm 关于
 */
public class AboutActivity extends BaseActivity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setTitle("关于", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "");
		findView();
		init();

	}

	LinearLayout llLeft;
	private TextView iv_about_websites, iv_about_customer, iv_about_terms, tv_about_versioncode;

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		iv_about_websites = (TextView) findViewById(R.id.iv_about_websites);
		iv_about_customer = (TextView) findViewById(R.id.iv_about_customer);
		iv_about_terms = (TextView) findViewById(R.id.iv_about_terms);
		tv_about_versioncode = (TextView) findViewById(R.id.tv_about_versioncode);
		tv_about_versioncode.setText("好手帮 " + getVersion(AboutActivity.this));
	}

	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return context.getString(R.string.version_unknown);
		}
	}

	private void init() {
		llLeft.setOnClickListener(this);
		iv_about_websites.setOnClickListener(this);
		iv_about_customer.setOnClickListener(this);
		iv_about_terms.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String url = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.iv_about_websites:// 公司网站
			url = "http://www.haoshoubang.com/";
			intent = new Intent(AboutActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "公司网站");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);

			break;
		case R.id.iv_about_customer:// 联系客服
			popTheirProfile();
			break;
		case R.id.iv_about_terms:// 条款与说明

			url = "http://www.haoshoubang.com/bangke/html/privacy.html";
			intent = new Intent(AboutActivity.this, WebBrowserActivity.class);
			intent.putExtra(WebBrowserActivity.ACTION_KEY_TITLE, "条款与说明");
			intent.putExtra(WebBrowserActivity.ACTION_KEY_URL, url);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	private PopupWindow popTheirProfile;

	public void popTheirProfile() {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("是否拨打客服电话?");
		up.setText("是");
		up.setTextColor(this.getResources().getColor(R.color.common_red));
		under.setText("否");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popTheirProfile.dismiss();
				Utils.calls(getResources().getString(R.string.call_customer), AboutActivity.this);

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
}
