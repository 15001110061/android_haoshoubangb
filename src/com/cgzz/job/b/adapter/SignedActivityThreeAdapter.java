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
import com.cgzz.job.b.adapter.CurrentAdapter.OnTextClickListener;
import com.cgzz.job.b.adapter.WorkcardAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.WorkcardAdapter.TelOnClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.utils.Utils;

/***
 * 注册低3步
 * 
 */
public class SignedActivityThreeAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public SignedActivityThreeAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.activity_three_item, null);
			holder = new ViewHolder();
			holder.tv_collection_peonumber = (TextView) convertView
					.findViewById(R.id.tv_collection_peonumber);

			holder.tv_collection_peonumber2 = (TextView) convertView
					.findViewById(R.id.tv_collection_peonumber2);
			holder.tv_current_yanshou = (TextView) convertView
					.findViewById(R.id.tv_current_yanshou);
			holder.tv_current_yanshou2 = (TextView) convertView
					.findViewById(R.id.tv_current_yanshou2);

			holder.iv_collection_picture = (ImageView) convertView
					.findViewById(R.id.iv_collection_picture);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);

		holder.tv_collection_peonumber.setText(map.get("name"));

		
		if (!Utils.isEmpty(map.get("realname"))) {
			holder.tv_collection_peonumber2.setText("管理员:" + map.get("realname"));
		}else{
			holder.tv_collection_peonumber2.setVisibility(View.INVISIBLE);
		}
	
		String image = map.get("front_photos");
		ImageListener listener = ImageLoader.getImageListener(
				holder.iv_collection_picture, R.drawable.image_moren,
				R.drawable.image_moren);
		ImageContainer imageContainer = mImageLoader.get(image, listener);

		if ("".equals(map.get("realname"))) {
			holder.tv_current_yanshou2.setVisibility(View.VISIBLE);
			holder.tv_current_yanshou.setVisibility(View.GONE);
			holder.tv_current_yanshou2
			.setOnClickListener(new TextOnClickListener(position));
		} else {
			holder.tv_current_yanshou2.setVisibility(View.GONE);
			holder.tv_current_yanshou.setVisibility(View.VISIBLE);
			holder.tv_current_yanshou
					.setOnClickListener(new TelOnClickListener(position));

		}

		return convertView;
	}

	class ViewHolder {
		ImageView iv_collection_picture;

		TextView tv_collection_peonumber, tv_collection_peonumber2,
				tv_current_yanshou, tv_current_yanshou2;

	}
	/***
	 * 发短信
	 */

	class TextOnClickListener implements View.OnClickListener {

		private int position;

		public TextOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onTextClickListener != null) {
				onTextClickListener.onTextClick(position, v, p2);
			}
		}

	}

	private OnTextClickListener onTextClickListener;
	private int p2;

	public OnTextClickListener getonTextClickListener() {
		return onTextClickListener;
	}

	public void setOnTextClickListener(OnTextClickListener onTextClickListener,
			int logoUserInfos) {
		this.onTextClickListener = onTextClickListener;
		this.p2 = logoUserInfos;
	}

	public interface OnTextClickListener {
		public void onTextClick(int position, View v, int logo);
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
