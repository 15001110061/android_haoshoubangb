package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;

import android.app.Activity;
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

/***
 * 当前订单适配器
 * 
 */
public class CurrentAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;

	public CurrentAdapter(Context contexts) {
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

		View currentFocus = ((Activity) context).getCurrentFocus();

		if (currentFocus != null) {
			currentFocus.clearFocus();
		}

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_current_item, null);
			holder = new ViewHolder();
			holder.tv_current_name = (TextView) convertView.findViewById(R.id.tv_current_name);

			holder.tv_current_state = (TextView) convertView.findViewById(R.id.tv_current_state);
			holder.tv_current_picture = (ImageView) convertView.findViewById(R.id.tv_current_picture);

			holder.tv_current_room = (TextView) convertView.findViewById(R.id.tv_current_room);

			holder.tv_current_peonumber2 = (TextView) convertView.findViewById(R.id.tv_current_peonumber2);

			holder.tv_current_time = (TextView) convertView.findViewById(R.id.tv_current_time);

			holder.tv_current_feiyong = (TextView) convertView.findViewById(R.id.tv_current_feiyong);

			holder.tv_current_cancel_order = (TextView) convertView.findViewById(R.id.tv_current_cancel_order);
			holder.tv_current_yanshou = (TextView) convertView.findViewById(R.id.tv_current_yanshou);

			holder.tv_current_route = (TextView) convertView.findViewById(R.id.tv_current_route);

			holder.tv_current_zhuijia = (TextView) convertView.findViewById(R.id.tv_current_zhuijia);
			holder.tv_current_name2 = (TextView) convertView.findViewById(R.id.tv_current_name2);

			holder.rl_cur = (RelativeLayout) convertView.findViewById(R.id.rl_cur);

			holder.view_cur = (View) convertView.findViewById(R.id.view_cur);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, String> map = data.get(position);
		String status = map.get("status");
		if ("0".equals(status)) { // 0:即将开始，1：进行中
			holder.tv_current_state.setText("尚未开始");
			holder.rl_cur.setVisibility(View.VISIBLE);
			holder.view_cur.setVisibility(View.VISIBLE);
			holder.tv_current_yanshou.setVisibility(View.GONE);
			holder.tv_current_route.setVisibility(View.VISIBLE);
			// holder.tv_current_zhifu.setVisibility(View.GONE);
			holder.tv_current_zhuijia.setVisibility(View.VISIBLE);
		} else if ("1".equals(status)) {
			holder.tv_current_zhuijia.setVisibility(View.GONE);
			holder.tv_current_state.setText("进行中");
			holder.tv_current_yanshou.setVisibility(View.VISIBLE);
			holder.tv_current_yanshou.setText("验收通过");
			holder.rl_cur.setVisibility(View.VISIBLE);
			holder.view_cur.setVisibility(View.VISIBLE);
			// holder.tv_current_zhifu.setVisibility(View.GONE);
			holder.tv_current_route.setVisibility(View.GONE);
		} else if ("4".equals(status)) {
			holder.tv_current_zhuijia.setVisibility(View.GONE);
			holder.tv_current_state.setText("待支付");
			holder.tv_current_yanshou.setVisibility(View.VISIBLE);
			holder.tv_current_yanshou.setText("去支付");
			holder.rl_cur.setVisibility(View.VISIBLE);
			holder.view_cur.setVisibility(View.VISIBLE);
			holder.tv_current_route.setVisibility(View.GONE);
			// holder.tv_current_zhifu.setVisibility(View.VISIBLE);
		} else if ("-1".equals(status)) { // 0:即将开始，1：进行中
			holder.tv_current_state.setText("正在匹配");
			holder.tv_current_zhuijia.setVisibility(View.VISIBLE);
			holder.tv_current_yanshou.setVisibility(View.GONE);
	
			holder.rl_cur.setVisibility(View.VISIBLE);
			holder.view_cur.setVisibility(View.VISIBLE);
			holder.tv_current_cancel_order.setVisibility(View.GONE);
			holder.tv_current_route.setVisibility(View.GONE);
		} else if ("5".equals(status)) {
			holder.tv_current_state.setText("待支付");
			holder.rl_cur.setVisibility(View.GONE);	holder.tv_current_zhuijia.setVisibility(View.GONE);
			holder.view_cur.setVisibility(View.GONE);
		}

		// holder.tv_current_name2.setText("ID" + map.get("id"));
		holder.tv_current_name.setText(map.get("bangkesname"));
		if (!"0".equals(map.get("standard_count"))) {
			holder.tv_current_room.setText("清洁房间:" + map.get("standard_count") + "间");
		} else {
			holder.tv_current_room.setText("清洁房间:" + map.get("suite_count") + "间");
		}

		if ("1".equals(map.get("can_cancel"))) {
			holder.tv_current_cancel_order.setVisibility(View.VISIBLE);

		} else {
			holder.tv_current_cancel_order.setVisibility(View.GONE);

		}

		holder.tv_current_peonumber2.setText("帮客人数:" + map.get("workercount"));
		holder.tv_current_time.setText("开始时间:" + map.get("dutydate"));

		holder.tv_current_feiyong.setText("工作费用:" + map.get("totalprice"));

		holder.tv_current_route.setOnClickListener(new TextOnClickListener(position));

		holder.tv_current_yanshou.setOnClickListener(new CancelOrderOnClickListener(position));

		holder.tv_current_cancel_order.setOnClickListener(new RouteOnClickListener(position));

		holder.tv_current_zhuijia.setOnClickListener(new ZJOnClickListener(position));

		// holder.tv_current_zhifu.setOnClickListener(new ZFOnClickListener(
		// position));
		String image = map.get("bangkesportrait");
		ImageListener listener2 = ImageLoader.getImageListener(holder.tv_current_picture, R.drawable.image_moren,
				R.drawable.image_moren);
		try {
			mImageLoader.get(image, listener2);
		} catch (Exception e) {
		}

		// if (image != null && !"".equals(image)) {
		// // ImageListener listener = ImageLoader.getImageListener(
		// // holder.tv_current_picture, R.drawable.image_moren,
		// // R.drawable.image_moren);
		// // ImageContainer imageContainer = mImageLoader.get(image,
		// // listener);
		//
		//
		//
		// }
		return convertView;
	}

	class ViewHolder {
		ImageView tv_current_picture;

		TextView tv_current_name, tv_current_state, tv_current_room, tv_current_peonumber2, tv_current_time,
				tv_current_feiyong, tv_current_cancel_order, tv_current_yanshou, tv_current_route, tv_current_zhuijia,
				tv_current_name2;
		RelativeLayout rl_cur;
		View view_cur;
	}

	class ZFOnClickListener implements View.OnClickListener {

		private int position;

		public ZFOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onZFClickListener != null) {
				onZFClickListener.onZFClick(position, v, p13);
			}
		}

	}

	private OnZFClickListener onZFClickListener;
	private int p13;

	public OnZFClickListener getonZFClickListener() {
		return onZFClickListener;
	}

	public void setOnZFClickListener(OnZFClickListener onZFClickListener, int logoUserInfos) {
		this.onZFClickListener = onZFClickListener;
		this.p13 = logoUserInfos;
	}

	public interface OnZFClickListener {
		public void onZFClick(int position, View v, int logo);
	}

	class ZJOnClickListener implements View.OnClickListener {

		private int position;

		public ZJOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onZJClickListener != null) {
				onZJClickListener.onZJClick(position, v, p11);
			}
		}

	}

	private OnZJClickListener onZJClickListener;
	private int p11;

	public OnZJClickListener getonZJClickListener() {
		return onZJClickListener;
	}

	public void setOnZJClickListener(OnZJClickListener onZJClickListener, int logoUserInfos) {
		this.onZJClickListener = onZJClickListener;
		this.p11 = logoUserInfos;
	}

	public interface OnZJClickListener {
		public void onZJClick(int position, View v, int logo);
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

	/***
	 * 取消订单
	 */

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

	public void setOnCancelOrderClickListener(OnCancelOrderClickListener onCancelOrderClickListener,
			int logoUserInfos) {
		this.onCancelOrderClickListener = onCancelOrderClickListener;
		this.p = logoUserInfos;
	}

	public interface OnCancelOrderClickListener {
		public void onCancelOrderClick(int position, View v, int logo);
	}

	/***
	 * 查看路线
	 */

	class RouteOnClickListener implements View.OnClickListener {

		private int position;

		public RouteOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (onRouteClickListener != null) {
				onRouteClickListener.onRouteClick(position, v, p8);
			}
		}

	}

	private OnRouteClickListener onRouteClickListener;
	private int p8;

	public OnRouteClickListener getonRouteClickListener() {
		return onRouteClickListener;
	}

	public void setOnRouteClickListener(OnRouteClickListener onRouteClickListener, int logoUserInfos) {
		this.onRouteClickListener = onRouteClickListener;
		this.p8 = logoUserInfos;
	}

	public interface OnRouteClickListener {
		public void onRouteClick(int position, View v, int logo);
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

	public void setonRefuseClickListener(OnRefuseClickListener onRefuseClickListener, int logoUserInfos) {
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
