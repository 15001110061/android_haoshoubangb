package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;

/***
 * 当前订单适配器
 * 
 */
public class MyIncomeAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public MyIncomeAdapter(Context contexts) {
		this.context = contexts;
		this.inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
		mImageLoader = new ImageLoader(
				GlobalVariables.getRequestQueue(context), new BitmapCache());
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
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = getInviteView(position, convertView);
		return convertView;
	}

	private View getInviteView(final int position, View convertView) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_myincome_item,
					null);
			holder = new ViewHolder();

			holder.tv_home_item_openings3 = (TextView) convertView
					.findViewById(R.id.tv_home_item_openings3);

			holder.tv_home_item_openings2 = (TextView) convertView
					.findViewById(R.id.tv_home_item_openings2);
			holder.tv_myincome_peonumber = (TextView) convertView
					.findViewById(R.id.tv_myincome_peonumber);

			holder.tv_myincome_peonumber2 = (TextView) convertView
					.findViewById(R.id.tv_myincome_peonumber2);

			holder.rl_home_one = (RelativeLayout) convertView
					.findViewById(R.id.rl_home_one);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);
		if ("".equals(map.get("year"))) {
			holder.rl_home_one.setVisibility(View.GONE);
		} else {
			holder.rl_home_one.setVisibility(View.VISIBLE);
			holder.tv_home_item_openings3.setText(map.get("year"));
		}

		holder.tv_myincome_peonumber2.setText(map.get("month"));

		holder.tv_myincome_peonumber.setText(map.get("num"));
		holder.tv_myincome_peonumber2.setText(map.get("income"));

		return convertView;
	}

	class ViewHolder {
		RelativeLayout rl_home_one;
		TextView tv_home_item_openings3, tv_home_item_openings2,
				tv_myincome_peonumber, tv_myincome_peonumber2;

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

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}

			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}
}
