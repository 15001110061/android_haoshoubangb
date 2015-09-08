package com.cgzz.job.b.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgzz.job.b.R;

public class Myadapter4 extends BaseAdapter {
	private List<Map<String, String>> data;
	private Context context;
	private LayoutInflater inflater;
	ViewHolder viewHolder;
	public Myadapter4(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		data = new ArrayList<Map<String, String>>();
	}

	public void refreshData(List<Map<String, String>> couponList) {
		if (couponList != null) {
			this.data = couponList;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.gw_popwindow_bgs, null);
				viewHolder.tv1 = (TextView) convertView
						.findViewById(R.id.tv_list_item);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			} 
			viewHolder.tv1.setText(data.get(position).get("name"));
		return convertView;
	}

	class ViewHolder {
		TextView tv1;
	}
}
