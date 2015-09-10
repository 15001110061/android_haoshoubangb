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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;

/***
 * 当前订单适配器
 * 
 */
public class MessageCenterAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public MessageCenterAdapter(Context contexts) {
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
			convertView = inflater
					.inflate(R.layout.fragment_message_item, null);
			holder = new ViewHolder();
			holder.tv_message_time = (TextView) convertView
					.findViewById(R.id.tv_message_time);
			holder.tv_message_title = (TextView) convertView
					.findViewById(R.id.tv_message_title);
			holder.iv_message_picture = (ImageView) convertView
					.findViewById(R.id.iv_message_picture);
			holder.tv_message_message = (TextView) convertView
					.findViewById(R.id.tv_message_message);
			holder.tv_message_but = (TextView) convertView
					.findViewById(R.id.tv_message_but);

			holder.relativeLayout1 = (RelativeLayout) convertView
					.findViewById(R.id.relativeLayout1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		Map<String, String> map = data.get(position);

		holder.tv_message_time.setText(map.get("sendtime"));

		holder.tv_message_message.setText(map.get("content"));
		if (!"".equals(map.get("title"))) {
			holder.tv_message_title.setText(map.get("title"));
		} else {
			holder.tv_message_title.setText("系统消息");
		}
		String image = map.get("imgurl");
		if (!"".equals(image)) {
			ImageListener listener = ImageLoader.getImageListener(
					holder.iv_message_picture, R.drawable.icon_loadfailed_bgs,
					R.drawable.icon_loadfailed_bgs);
			try {
				 mImageLoader.get(image, listener);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} else {
			holder.iv_message_picture.setVisibility(View.GONE);
		}

		String url = map.get("url");
		if (!"".equals(url)) {
			holder.relativeLayout1.setVisibility(View.VISIBLE);
		} else {
			holder.relativeLayout1.setVisibility(View.GONE);
		}

		holder.tv_message_but.setOnClickListener(new TelOnClickListener(
				position));

		return convertView;
	}

	class ViewHolder {
		ImageView iv_message_picture;

		TextView tv_message_time, tv_message_title, tv_message_message,
				tv_message_but;
		RelativeLayout relativeLayout1;
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
