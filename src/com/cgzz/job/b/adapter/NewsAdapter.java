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
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;

/***
 * 新闻适配器
 * 
 */
public class NewsAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public NewsAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.fragment_consulting_item,
					null);
			holder = new ViewHolder();
			holder.tv_new_title = (TextView) convertView
					.findViewById(R.id.tv_new_title);
			holder.tv_new_message = (TextView) convertView
					.findViewById(R.id.tv_new_message);
			holder.tv_new_picture = (ImageView) convertView
					.findViewById(R.id.tv_new_picture);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);

		holder.tv_new_title.setText(map.get("tv_new_title"));
		holder.tv_new_message.setText(map.get("tv_new_message"));

		String image = map.get("tv_new_picture");
		ImageListener listener = ImageLoader.getImageListener(
				holder.tv_new_picture, R.drawable.icon_nor_user,
				R.drawable.icon_nor_user);
		try {
			 mImageLoader.get(image, listener);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_new_title, tv_new_message;
		ImageView tv_new_picture;
	}

	/***
	 * 查看详情
	 */

	class DetailsOnClickListener implements View.OnClickListener {

		private int position;

		public DetailsOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onDetailsClickListener != null) {
				onDetailsClickListener.onDetailsClick(position, v, p);
			}
		}

	}

	private OnDetailsClickListener onDetailsClickListener;
	private int p;

	public OnDetailsClickListener getonDetailsClickListener() {
		return onDetailsClickListener;
	}

	public void setonDetailsClickListener(
			OnDetailsClickListener onDetailsClickListener, int logoUserInfos) {
		this.onDetailsClickListener = onDetailsClickListener;
		this.p = logoUserInfos;
	}

	public interface OnDetailsClickListener {
		public void onDetailsClick(int position, View v, int logo);
	}

	/***
	 * 取消发布按钮
	 */

	class ConfirmationOnClickListener implements View.OnClickListener {

		private int position;

		public ConfirmationOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onConfirmationClickListener != null) {
				onConfirmationClickListener
						.onConfirmationClick(position, v, p8);
			}
		}

	}

	private OnConfirmationClickListener onConfirmationClickListener;
	private int p8;

	public OnConfirmationClickListener getonConfirmationClickListener() {
		return onConfirmationClickListener;
	}

	public void setonConfirmationClickListener(
			OnConfirmationClickListener onConfirmationClickListener,
			int logoUserInfos) {
		this.onConfirmationClickListener = onConfirmationClickListener;
		this.p8 = logoUserInfos;
	}

	public interface OnConfirmationClickListener {
		public void onConfirmationClick(int position, View v, int logo);
	}

	/***
	 * 取消邀请
	 */

	class RefuseOnClickListener implements View.OnClickListener {

		private int position;

		public RefuseOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onRefuseClickListener != null) {
				onRefuseClickListener.onRefuseClick(position, v, p4);
			}
		}

	}

	private OnRefuseClickListener onRefuseClickListener;
	private int p4;

	public OnRefuseClickListener getonRefuseClickListener() {
		return onRefuseClickListener;
	}

	public void setonRefuseClickListener(
			OnRefuseClickListener onRefuseClickListener, int logoUserInfos) {
		this.onRefuseClickListener = onRefuseClickListener;
		this.p4 = logoUserInfos;
	}

	public interface OnRefuseClickListener {
		public void onRefuseClick(int position, View v, int logo);
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
