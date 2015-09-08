package com.cgzz.job.b;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.utils.Utils;

public class BaseFragment extends Fragment {
	protected ProgressDialog waitDialog;
	public GlobalVariables application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Utils.addActivity(getActivity());
		application = (GlobalVariables) getActivity().getApplicationContext();
	}

	@Override
	public void onDestroy() {
		dismissDialog();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 全局等待对话框
	 */
	public void showWaitDialog() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (waitDialog == null || !waitDialog.isShowing()) {
					waitDialog = new ProgressDialog(getActivity());
					waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					waitDialog.setCanceledOnTouchOutside(false);
					ImageView view = new ImageView(getActivity());
					view.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					Animation loadAnimation = AnimationUtils.loadAnimation(
							getActivity(), R.anim.rotate);
					view.startAnimation(loadAnimation);
					loadAnimation.start();
					view.setImageResource(R.drawable.picture_loading);
					// waitDialog.setCancelable(false);
					waitDialog.show();
					waitDialog.setContentView(view);
				}

			}
		});

	}

	public void dismissDialog() {
		try {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (waitDialog != null && waitDialog.isShowing()) {
						waitDialog.dismiss();
						waitDialog = null;
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
