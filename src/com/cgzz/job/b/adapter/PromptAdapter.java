package com.cgzz.job.b.adapter;

/***
 * 文字提示的adapter
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cgzz.job.b.R;
import com.cgzz.job.b.bean.PoiLocation;

public class PromptAdapter extends BaseAdapter implements Filterable {
	private List<PoiLocation> infoList;
	private LayoutInflater inflater;

	public PromptAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		infoList = new ArrayList<PoiLocation>();
	}

	public void refreshData(List<PoiLocation> poiList) {
		if (poiList != null) {
			infoList = poiList;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infoList.get(arg0).getTitle();
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
			convertView = inflater.inflate(R.layout.item_destination_history,
					null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.address = (TextView) convertView
					.findViewById(R.id.tv_address);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(infoList.get(position).getTitle());
		viewHolder.address.setText(infoList.get(position).getAddress());

		return convertView;
	}

	class ViewHolder {
		TextView title, address, coordinate;

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
}
