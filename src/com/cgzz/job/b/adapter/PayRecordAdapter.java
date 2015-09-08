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
 * 
 * 
 */
public class PayRecordAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public PayRecordAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.activity_payrecord_item,
					null);
			holder = new ViewHolder();
			holder.tv_current_state = (TextView) convertView
					.findViewById(R.id.tv_current_state);

			holder.tv_current_room2 = (TextView) convertView
					.findViewById(R.id.tv_current_room2);
			holder.tv_current_room = (TextView) convertView
					.findViewById(R.id.tv_current_room);

			holder.tv_current_peonumber2 = (TextView) convertView
					.findViewById(R.id.tv_current_peonumber2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = data.get(position);

		String paytype = map.get("paytype");
		if ("1".equals(paytype)) { // 支付方式1：向好手帮转账 2：好手帮签约用户 3:使用支付宝支付 4：现金支付
			holder.tv_current_state.setText("向好手帮转账");
		} else if ("2".equals(paytype)) {
			holder.tv_current_state.setText("向好手帮签约用户转账");
		} else if ("3".equals(paytype)) {
			holder.tv_current_state.setText("支付宝支付转账");
		} else if ("4".equals(paytype)) {
			holder.tv_current_state.setText("现金支付");
		}
		holder.tv_current_room.setText("To:" + map.get("bangkesname"));

		holder.tv_current_peonumber2.setText(map.get("paytime"));
		holder.tv_current_room2.setText("￥" + map.get("paymoney"));
		return convertView;
	}

	class ViewHolder {

		TextView tv_current_state, tv_current_room2, tv_current_room,
				tv_current_peonumber2;
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
