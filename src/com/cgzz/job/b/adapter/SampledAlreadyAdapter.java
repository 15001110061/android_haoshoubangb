package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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
 * 已经验收
 * 
 */
public class SampledAlreadyAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public SampledAlreadyAdapter(Context contexts) {
		this.context = contexts;
		this.inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
		mImageLoader = new ImageLoader(GlobalVariables.getRequestQueue(context), new BitmapCache());
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
		  View currentFocus = ((Activity)context).getCurrentFocus();
			
		    if (currentFocus != null) {
		        currentFocus.clearFocus();
		    }
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_sampled_yi_item, null);
			holder = new ViewHolder();
			holder.tv_current_name = (TextView) convertView.findViewById(R.id.tv_current_name);

			holder.tv_current_picture = (ImageView) convertView.findViewById(R.id.tv_current_picture);

			holder.tv_current_room = (TextView) convertView.findViewById(R.id.tv_current_room);

			holder.tv_current_peonumber2 = (TextView) convertView.findViewById(R.id.tv_current_peonumber2);

			holder.tv_current_time = (TextView) convertView.findViewById(R.id.tv_current_time);

			holder.tv_current_feiyong = (TextView) convertView.findViewById(R.id.tv_current_feiyong);

			holder.tv_current_state = (TextView) convertView.findViewById(R.id.tv_current_state);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);
		holder.tv_current_name.setText(map.get("bangkesname"));

		if (!"0".equals(map.get("standard_count"))) {
			holder.tv_current_room.setText("清洁房间:" + map.get("suite_count") + "间");
		} else {
			holder.tv_current_room.setText("清洁房间:" + map.get("standard_count") + "间");
		}

		String status = map.get("status");
		if ("4".equals(status)) {
			holder.tv_current_state.setText("未支付");
			// holder.tv_current_yanshou.setVisibility(View.GONE);
			// holder.tv_current_route.setVisibility(View.GONE);
			// holder.tv_current_zhifu.setVisibility(View.VISIBLE);
		}

		holder.tv_current_peonumber2.setText("帮客人数:" + map.get("workercount"));
		holder.tv_current_time.setText("开始时间:" + map.get("dutydate"));

		holder.tv_current_feiyong.setText("工作费用:" + map.get("totalprice"));
		String image = map.get("bangkesportrait");

		ImageListener listener = ImageLoader.getImageListener(holder.tv_current_picture, R.drawable.image_moren,
				R.drawable.image_moren);
		try {
			mImageLoader.get(image, listener);
		} catch (Exception e) {
		}
		return convertView;
	}

	class ViewHolder {
		ImageView tv_current_picture;

		TextView tv_current_name, tv_current_room, tv_current_peonumber2, tv_current_time, tv_current_feiyong,
				tv_current_state;
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
