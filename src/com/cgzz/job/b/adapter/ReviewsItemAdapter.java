package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.view.CustomerRatingBarGo.OnProgressChangeListener;

/***
 * 去评价
 * 
 */
public class ReviewsItemAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;
	private static HashMap<Integer, Integer> isNum;
	private ImageLoader mImageLoader;

	public ReviewsItemAdapter(Context contexts) {
		this.context = contexts;
		this.inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
		isNum = new HashMap<Integer, Integer>();
		mImageLoader = new ImageLoader(
				GlobalVariables.getRequestQueue(context), new BitmapCache());
		initDate();
	}

	public void refreshMYData(List<Map<String, String>> dataGroup) {
		if (dataGroup != null) {
			data = dataGroup;
		}
		notifyDataSetChanged();

	}

	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i < 10; i++) {
			getIsNum().put(i, 0);
		}
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
			convertView = inflater.inflate(R.layout.fragment_reviewsitem_item,
					null);
			holder = new ViewHolder();
			holder.bar_review_item = (com.cgzz.job.b.view.CustomerRatingBarGo) convertView
					.findViewById(R.id.bar_review_item);
			holder.tv_review_name = (TextView) convertView
					.findViewById(R.id.tv_review_name);
			
			holder.iv_collection_picture = (ImageView) convertView
					.findViewById(R.id.iv_collection_picture);
			
			
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		 Map<String, String> map = data.get(position);
	
		 holder.tv_review_name.setText(map.get("realname"));

		 String image = map.get("portrait");
		 ImageListener listener = ImageLoader.getImageListener(
		 holder.iv_collection_picture, R.drawable.image_moren_pop,
		 R.drawable.image_moren_pop);
		 try {
				mImageLoader.get(image, listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		//
//			hMap.put("isShoucang","0");
//			hMap.put("Pingfen","0");
		
		holder.bar_review_item.setProgress( Integer.parseInt( map.get("Pingfen")));
		if("0".equals(map.get("isShoucang"))){
			holder.cb.setChecked(false);
		}else{
			holder.cb.setChecked(true);
		}
	

//		holder.bar_review_item.setOnClickListener(new TelOnClickListener(
//				position));

//		holder.cb.setOnClickListener(new TextOnClickListener(position));
		
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					data.get(position).put("isShoucang",  "1");
				}else{
					data.get(position).put("isShoucang",  "0");
				}
				
			}
		});
		
		
		
		holder.bar_review_item.setOnProgressChangeListener(	holder.bar_review_item, new OnProgressChangeListener() {
			
			@Override
			public void onProgressChange(View view, int progress) {
				data.get(position).put("Pingfen",  ""+progress);
			}
		}, true);
		return convertView;
	}

	class ViewHolder {
		 ImageView iv_collection_picture;
		//
		public CheckBox cb;
		TextView tv_review_name;
		com.cgzz.job.b.view.CustomerRatingBarGo bar_review_item;
	}

	public static HashMap<Integer, Integer> getIsNum() {
		return isNum;
	}

	public static void setIsNum(HashMap<Integer, Integer> isSelected) {
		ReviewsItemAdapter.isNum = isNum;
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
