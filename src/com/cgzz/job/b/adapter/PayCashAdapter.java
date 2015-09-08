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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.ReviewsItemAdapter.ViewHolder;
import com.cgzz.job.b.adapter.WorkcardAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.WorkcardAdapter.TelOnClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;

/***
 * 现金支付
 * 
 */
public class PayCashAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public PayCashAdapter(Context contexts) {
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
					.inflate(R.layout.activity_paycash_item, null);
			holder = new ViewHolder();
			holder.tv_review_name = (TextView) convertView
					.findViewById(R.id.tv_review_name);

			holder.iv_collection_picture = (ImageView) convertView
					.findViewById(R.id.iv_collection_picture);
			holder.tv_review_jiedan = (TextView) convertView
					.findViewById(R.id.tv_review_jiedan);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// if (jsonObject.has("earn_money")) {
		// bundle.putString("earn_money", jsonObject.getString("earn_money"));
		// }
		//
		// if (jsonObject.has("portrait")) {
		// bundle.putString("portrait", jsonObject.getString("portrait"));
		// }
		Map<String, String> map = data.get(position);
		holder.tv_review_name.setText(map.get("realname"));
		holder.tv_review_jiedan.setText(map.get("earn_money") + "元");
		String image = map.get("portrait");

		if (!Utils.isEmpty(image)) {
			ImageListener listener = ImageLoader.getImageListener(
					holder.iv_collection_picture, R.drawable.image_moren_pop,
					R.drawable.image_moren_pop);
			ImageContainer imageContainer = mImageLoader.get(image, listener);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView iv_collection_picture;
		TextView tv_review_name, tv_review_jiedan;
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
