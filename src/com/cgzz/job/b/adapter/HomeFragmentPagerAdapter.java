package com.cgzz.job.b.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
	ArrayList<Fragment> fragmentListData;

	public HomeFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		fragmentListData = new ArrayList<Fragment>();
	}

	public void refreshMYData(ArrayList<Fragment> fragmentList) {
		if (fragmentList != null) {
			fragmentListData = fragmentList;
		}
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentListData.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentListData.size();
	}
	

	// public float getPageWidth(int position) {
	// if (position == 0 || position <= fragmentListData.size()-1) {
	// return 0.8f;
	// }
	// return 1f;
	// }

}
