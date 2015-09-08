package com.cgzz.job.b.adapter;

/***
 * 文字提示的adapter
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.CurrentAdapter.OnTelClickListener;
import com.cgzz.job.b.adapter.CurrentAdapter.OnTextClickListener;
import com.cgzz.job.b.adapter.CurrentAdapter.TelOnClickListener;
import com.cgzz.job.b.bean.PoiLocation;

public class SearchAdapter extends BaseAdapter implements Filterable {
	private LayoutInflater inflater;
	private List<Map<String, String>> data;

	public SearchAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
	}

	public void refreshMYData(List<Map<String, String>> dataGroup) {
		if (dataGroup != null) {
			data = dataGroup;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.item_destination_sech, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.tv_signed_next = (TextView) convertView
					.findViewById(R.id.tv_signed_next);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = data.get(position);
		viewHolder.title.setText(map.get("name"));
		if (data.size() ==( position + 1)) {
			viewHolder.tv_signed_next.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tv_signed_next.setVisibility(View.GONE);
		}
		viewHolder.tv_signed_next.setOnClickListener(new TelOnClickListener(
				position));
		return convertView;
	}

	class ViewHolder {
		TextView title, address, coordinate, tv_signed_next;

	}

	@Override
	public Filter getFilter() {
		return new MyFilter();
	}

	class MyFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults result = new FilterResults();
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
		}

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

	public void setOnTelClickListener(OnTelClickListener onTelClickListener,
			int logoUserInfos) {
		this.onTelClickListener = onTelClickListener;
		this.p1 = logoUserInfos;
	}

	public interface OnTelClickListener {
		public void onTelClick(int position, View v, int logo);
	}
}
