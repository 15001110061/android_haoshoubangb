package com.cgzz.job.b.activity;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseFragment;
import com.cgzz.job.b.R;

public class HomePageFragment_fragment extends BaseFragment implements
		OnClickListener {
	private View mMainView;
	private TextView tv_home_item_page, tv_home_item_time, tv_home_item_site,
			tv_home_item_openings, tv_home_item_money, tv_home_item_title;
	private int mIndex;
	private Context context;
	private Map<String, String> map;

	public HomePageFragment_fragment(Map<String, String> map2, int index) {
		this.mIndex = index;
		this.map = map2;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = getActivity().getLayoutInflater();
		mMainView = inflater.inflate(R.layout.fragment_home_subitemw,
				(ViewGroup) getActivity().findViewById(R.id.viewpager), false);

		initView(mMainView);
		init();
	}

	RelativeLayout rl_home_item_e, rl_home_item_f, rl_home_item_c,
			rl_home_item_d;
	ImageView iv_home_item_voice, iv_home_item_detailed;

	private void initView(View mMainView) {
		context = getActivity();
		tv_home_item_title = (TextView) mMainView
				.findViewById(R.id.tv_home_item_title);
		tv_home_item_page = (TextView) mMainView
				.findViewById(R.id.tv_home_item_page);
		tv_home_item_time = (TextView) mMainView
				.findViewById(R.id.tv_home_item_time);
		tv_home_item_site = (TextView) mMainView
				.findViewById(R.id.tv_home_item_site);

		tv_home_item_openings = (TextView) mMainView
				.findViewById(R.id.tv_home_item_openings);
		tv_home_item_money = (TextView) mMainView
				.findViewById(R.id.tv_home_item_money);

		rl_home_item_c = (RelativeLayout) mMainView
				.findViewById(R.id.rl_home_item_c);
		rl_home_item_d = (RelativeLayout) mMainView
				.findViewById(R.id.rl_home_item_d);
		rl_home_item_e = (RelativeLayout) mMainView
				.findViewById(R.id.rl_home_item_e);
		// rl_home_item_f = (RelativeLayout) mMainView
		// .findViewById(R.id.rl_home_item_f);
		iv_home_item_voice = (ImageView) mMainView
				.findViewById(R.id.iv_home_item_voice);
		iv_home_item_detailed = (ImageView) mMainView
				.findViewById(R.id.iv_home_item_detailed);
		tv_home_item_title.setText(map.get("tv_current_name"));

	}

	private void init() {
		iv_home_item_voice.setOnClickListener(this);
		iv_home_item_detailed.setOnClickListener(this);
		rl_home_item_e.setOnClickListener(this);
		// rl_home_item_f.setOnClickListener(this);
		rl_home_item_c.setOnClickListener(this);
		rl_home_item_d.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) mMainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}
		return mMainView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mIndex == 1) {
			setUserVisibleHint(true);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_home_item_voice:// 语音播报
//			TTSController.getInstance(context).playText(
//					tv_home_item_title.getText() + "�?
//							+ tv_home_item_time.getText() + "�?
//							+ tv_home_item_site.getText() + "。您"
//							+ tv_home_item_money.getText());
			break;
		case R.id.iv_home_item_detailed:// 详细
			break;

		case R.id.rl_home_item_c://

//			CustomDialog.alertDialog(context, true, false, true,
//					R.drawable.icon_dashang, "Aaa",
//					new CustomDialog.PopUpDialogListener() {
//
//						@Override
//						public void doPositiveClick(Boolean isOk) {
//							if (isOk) {// 确定
//
//							}
//
//						}
//					});
			break;
		case R.id.rl_home_item_d://
//			CustomDialog.alertDialog(context, true, false, true,
//					R.drawable.icon_xianjin, "Bbb",false,
//					new CustomDialog.PopUpDialogListener() {
//
//						@Override
//						public void doPositiveClick(Boolean isOk) {
//							if (isOk) {// 确定
//
//							}
//
//						}
//					});
			break;
		case R.id.rl_home_item_e://
//			CustomDialog.alertDialog(context, true, false, true,
//					R.drawable.icon_wucan, "Ccc",
//					new CustomDialog.PopUpDialogListener() {
//
//						@Override
//						public void doPositiveClick(Boolean isOk) {
//							if (isOk) {// 确定
//
//							}
//
//						}
//					});
			break;
		// case R.id.rl_home_item_f://
		// ToastUtil.makeShortText(context, "4");
		//
		// break;

		default:
			break;
		}

	}
}
