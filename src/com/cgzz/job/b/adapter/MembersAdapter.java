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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;

/***
 * µ±«∞∂©µ•  ≈‰∆˜
 * 
 */
public class MembersAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public MembersAdapter(Context contexts) {
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
			convertView = inflater.inflate(R.layout.activity_members_item, null);
			holder = new ViewHolder();
			holder.tv_me_queding = (TextView) convertView.findViewById(R.id.tv_me_queding);

			holder.tv_me_quxiao = (TextView) convertView.findViewById(R.id.tv_me_quxiao);

			holder.tv_me_name = (TextView) convertView.findViewById(R.id.tv_me_name);

			holder.tv_me_name2 = (TextView) convertView.findViewById(R.id.tv_me_name2);

			holder.tv_me_tel = (TextView) convertView.findViewById(R.id.tv_me_tel);

			holder.rl_collection_two = (LinearLayout) convertView.findViewById(R.id.rl_collection_two);
			holder.rl_collection_three = (LinearLayout) convertView.findViewById(R.id.rl_collection_three);

			holder.tv_me_hulue = (TextView) convertView.findViewById(R.id.tv_me_hulue);
			holder.tv_me_fadan = (TextView) convertView.findViewById(R.id.tv_me_fadan);

			holder.tv_me_yanshou = (TextView) convertView.findViewById(R.id.tv_me_yanshou);

			holder.tv_me_chakan = (TextView) convertView.findViewById(R.id.tv_me_chakan);
			holder.iv_collection_picture = (ImageView) convertView.findViewById(R.id.iv_collection_picture);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//
		Map<String, String> map = data.get(position);
		holder.tv_me_name.setText(map.get("realname"));

		if (!Utils.isEmpty(map.get("department_value"))) {
			holder.tv_me_name2.setText(map.get("department_value"));
		} else {
			holder.tv_me_name2.setVisibility(View.GONE);
		}

		holder.tv_me_tel.setText(map.get("mobile"));
		if ("1".equals(map.get("is_verify"))) {//  «∑Ò…Û∫À£®1£∫“—…Û∫À£¨0£∫Œ¥…Û∫À£¨-1£∫“—∫ˆ¬‘£©
			holder.rl_collection_two.setVisibility(View.VISIBLE);
			holder.rl_collection_three.setVisibility(View.GONE);
			holder.tv_me_hulue.setVisibility(View.GONE);
			if ("1".equals(map.get("is_order"))) {
				holder.tv_me_fadan.setVisibility(View.VISIBLE);
			} else {
				holder.tv_me_fadan.setVisibility(View.GONE);
			}
			if ("1".equals(map.get("is_check"))) {
				holder.tv_me_yanshou.setVisibility(View.VISIBLE);
			} else {
				holder.tv_me_yanshou.setVisibility(View.GONE);
			}
			if ("1".equals(map.get("is_select"))) {
				holder.tv_me_chakan.setVisibility(View.VISIBLE);
			} else {
				holder.tv_me_chakan.setVisibility(View.GONE);
			}

		} else if ("0".equals(map.get("is_verify"))) {
			holder.rl_collection_two.setVisibility(View.GONE);
			holder.rl_collection_three.setVisibility(View.VISIBLE);
		} else if ("-1".equals(map.get("is_verify"))) {
			holder.rl_collection_two.setVisibility(View.VISIBLE);
			holder.rl_collection_three.setVisibility(View.GONE);
			holder.tv_me_fadan.setVisibility(View.GONE);
			holder.tv_me_yanshou.setVisibility(View.GONE);
			holder.tv_me_chakan.setVisibility(View.GONE);
			holder.tv_me_hulue.setVisibility(View.VISIBLE);
		}

		String image = map.get("portrait");

		ImageListener listener = ImageLoader.getImageListener(holder.iv_collection_picture, R.drawable.image_moren_pop,
				R.drawable.image_moren_pop);
		try {
			mImageLoader.get(image, listener);
		} catch (Exception e) {
		}

		//
		// holder.iv_collection_tel.setOnClickListener(new TelOnClickListener(
		// position));
		holder.tv_me_queding.setOnClickListener(new TelOnClickListener(position));

		holder.tv_me_quxiao.setOnClickListener(new TextOnClickListener(position));

		return convertView;
	}

	class ViewHolder {
		LinearLayout rl_collection_two, rl_collection_three;
		ImageView iv_collection_picture;

		TextView tv_me_queding, tv_me_quxiao, tv_me_name, tv_me_name2, tv_me_tel, tv_me_hulue, tv_me_fadan,
				tv_me_yanshou, tv_me_chakan;

	}

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

	public void setOnTelClickListener(OnTelClickListener onTelClickListener, int logoUserInfos) {
		this.onTelClickListener = onTelClickListener;
		this.p1 = logoUserInfos;
	}

	public interface OnTelClickListener {
		public void onTelClick(int position, View v, int logo);
	}

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

	public void setOnTextClickListener(OnTextClickListener onTextClickListener, int logoUserInfos) {
		this.onTextClickListener = onTextClickListener;
		this.p2 = logoUserInfos;
	}

	public interface OnTextClickListener {
		public void onTextClick(int position, View v, int logo);
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
