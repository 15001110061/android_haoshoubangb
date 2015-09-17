package com.cgzz.job.b.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {
	ArrayList<ImageView> li;
	private int size;

	public ViewPagerAdapter(ArrayList<ImageView> li) {
		size = li.size();
		this.li = li;
	}

	@Override
	public int getCount() {
		return size;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);// 完全溢出view,避免数据多时出现重复现象
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(li.get(position), 0);
		
		return li.get(position);
	}
}
