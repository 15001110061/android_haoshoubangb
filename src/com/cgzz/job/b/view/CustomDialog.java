package com.cgzz.job.b.view;

import com.cgzz.job.b.R;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog {
	public static AlertDialog aDialog;
	public static PopUpDialogListener mListener;
	private static TextView btn_quxiao, btn_quding, tv_message, iv_dialog_title;
	static LinearLayout ll_dialog_title;

	public static interface PopUpDialogListener {
		public void doPositiveClick(Boolean isOk);
	}

	public static void alertDialog(final Context context, boolean isShowIV, boolean isShowQuxiao, boolean isShowQueding,
			Drawable IVResources, String message, boolean ishtml, PopUpDialogListener listener) {
		try {

			mListener = listener;
			final View view;
			view = LayoutInflater.from(context).inflate(R.layout.customdialog, null);
			btn_quxiao = (TextView) view.findViewById(R.id.btn_quxiao);
			btn_quding = (TextView) view.findViewById(R.id.btn_quding);
			tv_message = (TextView) view.findViewById(R.id.tv_message);

			ll_dialog_title = (LinearLayout) view.findViewById(R.id.ll_dialog_title);
			iv_dialog_title = (TextView) view.findViewById(R.id.iv_dialog_title);
			// 取消
			if (isShowQuxiao) {
				btn_quxiao.setVisibility(View.VISIBLE);
			} else {
				btn_quxiao.setVisibility(View.GONE);
			}

			// 确定
			if (isShowQueding) {
				btn_quding.setVisibility(View.VISIBLE);
			} else {
				btn_quding.setVisibility(View.GONE);
			}
			if (ishtml) {
				// 内容
				tv_message.setText(Html.fromHtml(message));
			} else {
				// 内容
				tv_message.setText(message);
			}

			// 上方图片
			if (isShowIV) {
				ll_dialog_title.setVisibility(View.VISIBLE);
				iv_dialog_title.setBackgroundDrawable(IVResources);

			} else {
				ll_dialog_title.setVisibility(View.GONE);
			}

			if (aDialog != null && aDialog.isShowing()) {
				aDialog.dismiss();
			}
			aDialog = new AlertDialog.Builder(context).create();
			aDialog.setCancelable(false);
			try {
				aDialog.show();
			} catch (Exception e) {
			}
			aDialog.getWindow().setContentView(view);

			btn_quxiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.doPositiveClick(false);
					aDialog.dismiss();
				}
			});
			btn_quding.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.doPositiveClick(true);
					aDialog.dismiss();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void alertDialog(final Context context, boolean isShowIV, boolean isShowQuxiao, boolean isShowQueding,
			Drawable IVResources, String message, PopUpDialogListener listener) {
		try {

			mListener = listener;
			final View view;
			view = LayoutInflater.from(context).inflate(R.layout.customdialog, null);
			btn_quxiao = (TextView) view.findViewById(R.id.btn_quxiao);
			btn_quding = (TextView) view.findViewById(R.id.btn_quding);
			tv_message = (TextView) view.findViewById(R.id.tv_message);

			ll_dialog_title = (LinearLayout) view.findViewById(R.id.ll_dialog_title);
			iv_dialog_title = (TextView) view.findViewById(R.id.iv_dialog_title);
			// 取消
			if (isShowQuxiao) {
				btn_quxiao.setVisibility(View.VISIBLE);
			} else {
				btn_quxiao.setVisibility(View.GONE);
			}

			// 确定
			if (isShowQueding) {
				btn_quding.setVisibility(View.VISIBLE);
			} else {
				btn_quding.setVisibility(View.GONE);
			}
			// 内容
			tv_message.setText(message);

			// 上方图片
			if (isShowIV) {
				ll_dialog_title.setVisibility(View.VISIBLE);
				iv_dialog_title.setBackgroundDrawable(IVResources);

			} else {
				ll_dialog_title.setVisibility(View.GONE);
			}

			if (aDialog != null && aDialog.isShowing()) {
				aDialog.dismiss();
			}
			aDialog = new AlertDialog.Builder(context).create();
			aDialog.setCancelable(false);
			try {
				aDialog.show();
			} catch (Exception e) {
			}
			aDialog.getWindow().setContentView(view);

			btn_quxiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.doPositiveClick(false);
					aDialog.dismiss();
				}
			});
			btn_quding.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.doPositiveClick(true);
					aDialog.dismiss();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
