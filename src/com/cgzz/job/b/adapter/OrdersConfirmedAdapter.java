package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.CurrentAdapter.OnRefuseClickListener;
import com.cgzz.job.b.adapter.CurrentAdapter.OnRouteClickListener;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * 订单确认
 * 
 */
public class OrdersConfirmedAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;

	private ImageLoader mImageLoader;
	String standard_count = "", suite_count = "";

	public OrdersConfirmedAdapter(Context contexts, String standard_count, String suite_count) {
		this.standard_count = standard_count;
		this.suite_count = suite_count;
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fragment_ordersconfirmed_item, null);
			holder = new ViewHolder();

			holder.tv_oc_jia1 = (TextView) convertView.findViewById(R.id.tv_oc_jia1);
			holder.tv_oc_jia2 = (TextView) convertView.findViewById(R.id.tv_oc_jia2);
			holder.tv_oc_jia3 = (TextView) convertView.findViewById(R.id.tv_oc_jia3);
			holder.tv_oc_jian1 = (TextView) convertView.findViewById(R.id.tv_oc_jian1);
			holder.tv_oc_jian2 = (TextView) convertView.findViewById(R.id.tv_oc_jian2);
			holder.tv_oc_jian3 = (TextView) convertView.findViewById(R.id.tv_oc_jian3);

			holder.iv_oc_picture = (ImageView) convertView.findViewById(R.id.iv_oc_picture);
			holder.tv_oc_name = (TextView) convertView.findViewById(R.id.tv_oc_name);
			holder.tv_oc_shu1 = (TextView) convertView.findViewById(R.id.tv_oc_shu1);
			holder.tv_oc_shu2 = (TextView) convertView.findViewById(R.id.tv_oc_shu2);
			holder.tv_oc_shu3 = (TextView) convertView.findViewById(R.id.tv_oc_shu3);

			holder.ll_order_biaojian = (LinearLayout) convertView.findViewById(R.id.ll_order_biaojian);
			holder.ll_order_taojian = (LinearLayout) convertView.findViewById(R.id.ll_order_taojian);
			holder.rl_home_item_c = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_c);
			holder.rl_home_item_d = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_d);

			holder.ll_order_dashang = (LinearLayout) convertView.findViewById(R.id.ll_order_dashang);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if ("0".equals(standard_count)) {// 标间
			holder.rl_home_item_c.setVisibility(View.GONE);
		} else {
			holder.rl_home_item_c.setVisibility(View.VISIBLE);
		}

		if ("0".equals(suite_count)) {// 套间
			holder.rl_home_item_d.setVisibility(View.GONE);
		} else {
			holder.rl_home_item_d.setVisibility(View.VISIBLE);
		}

		Map<String, String> map = data.get(position);

		holder.tv_oc_shu1.setText(map.get("biaojiaNnum"));
		holder.tv_oc_shu2.setText(map.get("taojianNum"));
		holder.tv_oc_shu3.setText(map.get("dashangNum"));
		holder.tv_oc_name.setText(map.get("realname"));
		String image = map.get("portrait");
		ImageListener listener = ImageLoader.getImageListener(holder.iv_oc_picture, R.drawable.image_moren,
				R.drawable.image_moren);
		ImageContainer imageContainer = mImageLoader.get(image, listener);
		//
		holder.ll_order_dashang.setOnClickListener(new TelOnClickListener(position));
		holder.tv_oc_shu3.setOnClickListener(new TelOnClickListener(position));
		holder.ll_order_biaojian.setOnClickListener(new RouteOnClickListener(position));
		holder.ll_order_taojian.setOnClickListener(new RefuseOnClickListener(position));

		/***
		 * 标间
		 */
		final TextView t1 = holder.tv_oc_shu1;
		holder.tv_oc_jia1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String oc_shu1 = t1.getText().toString();
				int s = Integer.parseInt(oc_shu1);
				if (s > 0) {
					t1.setText(s - 1 + "");
					data.get(position).put("biaojiaNnum", s - 1 + "");
					refreshOrdersBiao(context, setViewTitleBiao());
				} else {
					ToastUtil.makeShortText(context, "不能在减了");
				}

			}
		});

		holder.tv_oc_jian1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String oc_shu1 = t1.getText().toString();
				int s = Integer.parseInt(oc_shu1);

				data.get(position).put("biaojiaNnum", s + 1 + "");
				t1.setText(s + 1 + "");
				refreshOrdersBiao(context, setViewTitleBiao());

			}
		});

		/***
		 * 套件
		 */
		final TextView t2 = holder.tv_oc_shu2;
		holder.tv_oc_jia2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String oc_shu2 = t2.getText().toString();
				int s = Integer.parseInt(oc_shu2);
				if (s > 0) {
					t2.setText(s - 1 + "");
					data.get(position).put("taojianNum", s - 1 + "");
					refreshOrdersTao(context, setViewTitleTao());
				} else {
					ToastUtil.makeShortText(context, "不能在减了");
				}

			}
		});

		holder.tv_oc_jian2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String oc_shu2 = t2.getText().toString();
				int s = Integer.parseInt(oc_shu2);
				data.get(position).put("taojianNum", s + 1 + "");
				t2.setText(s + 1 + "");
				refreshOrdersTao(context, setViewTitleTao());
			}
		});

		/***
		 * 打赏
		 */
		final TextView t3 = holder.tv_oc_shu3;
		holder.tv_oc_jia3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String oc_shu3 = t3.getText().toString();
				int s = Integer.parseInt(oc_shu3);
				if (s > 0) {
					t3.setText(s - 1 + "");
					data.get(position).put("dashangNum", s - 1 + "");
				} else {
					ToastUtil.makeShortText(context, "不能在减了");
				}

			}
		});

		holder.tv_oc_jian3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String oc_shu3 = t3.getText().toString();
				int s = Integer.parseInt(oc_shu3);
				t3.setText(s + 1 + "");
				data.get(position).put("dashangNum", s + 1 + "");

			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView iv_oc_picture;
		LinearLayout ll_order_dashang, ll_order_biaojian, ll_order_taojian;
		TextView tv_oc_jia1, tv_oc_jia2, tv_oc_jia3, tv_oc_jian1, tv_oc_jian2, tv_oc_jian3, tv_oc_name, tv_oc_shu1,
				tv_oc_shu2, tv_oc_shu3;
		RelativeLayout rl_home_item_c, rl_home_item_d;
	}

	private int setViewTitleBiao() {
		int biaojian = 0;
		for (int i = 0; i < data.size(); i++) {
			biaojian += Integer.parseInt(data.get(i).get("biaojiaNnum"));
		}
		return biaojian;

	}

	private int setViewTitleTao() {
		int taojian = 0;
		for (int i = 0; i < data.size(); i++) {
			taojian += Integer.parseInt(data.get(i).get("taojianNum"));
		}
		return taojian;

	}

	public void refreshOrdersBiao(Context contexts, int biao) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "rodersconfirmedbiao");
		intentobd.putExtra("biaoNum", biao + "");
		contexts.sendBroadcast(intentobd);
	}

	public void refreshOrdersTao(Context contexts, int tao) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "rodersconfirmedbtao");
		intentobd.putExtra("taoNum", tao + "");
		contexts.sendBroadcast(intentobd);
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

	public void setOnTelClickListener(OnTelClickListener onTelClickListener, int logoUserInfos) {
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
