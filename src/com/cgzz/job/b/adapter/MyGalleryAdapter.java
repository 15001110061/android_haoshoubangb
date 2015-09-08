package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cgzz.job.b.R;
import com.cgzz.job.b.utils.SystemParams;
import com.cgzz.job.b.view.CustomDialog;

@SuppressWarnings("deprecation")
public class MyGalleryAdapter extends BaseAdapter {

	private Context context;
	private SystemParams systemParams;
	private List<Map<String, String>> data;

	public MyGalleryAdapter(Context context) {
		this.context = context;
		systemParams = SystemParams.getInstance((Activity) context);
		data = new ArrayList<Map<String, String>>();
	}

	public void refreshMYData(List<Map<String, String>> dataGroup) {
		if (dataGroup != null) {
			data = dataGroup;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.adapter_gallery_item,
					null);
			// convertView.setLayoutParams(new Gallery.LayoutParams(
			// (int) systemParams.screenWidth * 7/ 8,
			// (int) systemParams.screenWidth *10 /9));
			// convertView.setLayoutParams(new Gallery.LayoutParams(
			// 509,
			// 710));

			convertView.setLayoutParams(new Gallery.LayoutParams(
					(int) systemParams.screenWidth * 3 / 4,
					LayoutParams.WRAP_CONTENT));

			holder = new ViewHolder();
			holder.tv_home_item_time = (TextView) convertView
					.findViewById(R.id.tv_home_item_time);
			holder.tv_home_item_site = (TextView) convertView
					.findViewById(R.id.tv_home_item_site);

			holder.tv_home_item_openings2 = (TextView) convertView
					.findViewById(R.id.tv_home_item_openings2);
			holder.tv_home_item_openings = (TextView) convertView
					.findViewById(R.id.tv_home_item_openings);
			holder.rl_home_item_c = (RelativeLayout) convertView
					.findViewById(R.id.rl_home_item_c);

			holder.rl_home_item_d = (RelativeLayout) convertView
					.findViewById(R.id.rl_home_item_d);
			holder.rl_home_item_e = (RelativeLayout) convertView
					.findViewById(R.id.rl_home_item_e);

			holder.tv_home_item_count = (TextView) convertView
					.findViewById(R.id.tv_home_item_count);

			holder.tv_home_titlename = (TextView) convertView
					.findViewById(R.id.tv_home_titlename);

			holder.rl_home_item_b = (RelativeLayout) convertView
					.findViewById(R.id.rl_home_item_b);

			holder.iv_home_item_voice = (ImageView) convertView
					.findViewById(R.id.iv_home_item_voice);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundColor(Color.alpha(1));

		Map<String, String> map = data.get(position);
		holder.tv_home_titlename.setText(map.get("name"));
		holder.tv_home_item_time.setText(map.get("dutydate"));
		holder.tv_home_item_site.setText(map.get("address"));
		holder.tv_home_item_openings.setText(map.get("made"));

		holder.tv_home_item_count
				.setText(position + 1 + "/" + map.get("count"));

		if ("0".equals(map.get("standard_price"))) {
			holder.tv_home_item_openings2.setText(map.get("suite_price"));
		} else {
			holder.tv_home_item_openings2.setText(map.get("standard_price"));
		}
		if ("0".equals(map.get("havebar"))) {
			holder.rl_home_item_c.setVisibility(View.GONE);
		}
		if ("0".equals(map.get("bounus"))) {
			holder.rl_home_item_d.setVisibility(View.GONE);
		}

		if ("0".equals(map.get("havelaunch"))) {
			holder.rl_home_item_e.setVisibility(View.GONE);
		}

		holder.rl_home_item_b
				.setOnClickListener(new CancelOrderOnClickListener(position));

		holder.iv_home_item_voice.setOnClickListener(new TelOnClickListener(
				position));

		holder.rl_home_item_c.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				CustomDialog.alertDialog(context, true, false, true,
//						R.drawable.icon_dashang, "Aaa",
//						new CustomDialog.PopUpDialogListener() {
//
//							@Override
//							public void doPositiveClick(Boolean isOk) {
//								if (isOk) {// 确定
//
//								}
//
//							}
//						});

			}
		});

		holder.rl_home_item_d.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				CustomDialog.alertDialog(context, true, false, true,
//						R.drawable.icon_xianjin, "Bbb",
//						new CustomDialog.PopUpDialogListener() {
//
//							@Override
//							public void doPositiveClick(Boolean isOk) {
//								if (isOk) {// 确定
//
//								}
//
//							}
//						});
			}
		});

		holder.rl_home_item_e.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				CustomDialog.alertDialog(context, true, false, true,
//						R.drawable.icon_wucan, "Ccc",
//						new CustomDialog.PopUpDialogListener() {
//
//							@Override
//							public void doPositiveClick(Boolean isOk) {
//								if (isOk) {// 确定
//
//								}
//
//							}
//						});
			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView iv_home_item_voice;
		TextView tv_home_item_time, tv_home_item_site, tv_home_item_openings2,
				tv_home_item_openings, tv_home_item_count, tv_home_titlename;
		RelativeLayout rl_home_item_c, rl_home_item_d, rl_home_item_e,
				rl_home_item_b;
	}

	class CancelOrderOnClickListener implements View.OnClickListener {

		private int position;

		public CancelOrderOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onCancelOrderClickListener != null) {
				onCancelOrderClickListener.onCancelOrderClick(position, v, p);
			}
		}

	}

	private OnCancelOrderClickListener onCancelOrderClickListener;
	private int p;

	public OnCancelOrderClickListener getonCancelOrderClickListener() {
		return onCancelOrderClickListener;
	}

	public void setOnCancelOrderClickListener(
			OnCancelOrderClickListener onCancelOrderClickListener,
			int logoUserInfos) {
		this.onCancelOrderClickListener = onCancelOrderClickListener;
		this.p = logoUserInfos;
	}

	public interface OnCancelOrderClickListener {
		public void onCancelOrderClick(int position, View v, int logo);
	}

	/***
	 * 打电话
	 */

	class TelOnClickListener implements View.OnClickListener {

		private int position;

		public TelOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onTelClickListener != null) {
				onTelClickListener.onTelClick(position, v, p1);
			}
		}

	}

	private OnTelClickListener onTelClickListener;
	private int p1;

	public OnTelClickListener getonTelClickListener() {
		return onTelClickListener;
	}

	public void setOnTelClickListener(OnTelClickListener onTelClickListener,
			int logoUserInfos) {
		this.onTelClickListener = onTelClickListener;
		this.p1 = logoUserInfos;
	}

	public interface OnTelClickListener {
		public void onTelClick(int position, View v, int logo);
	}

	public interface OnTextClickListener {
		public void onTextClick(int position, View v, int logo);
	}
}
