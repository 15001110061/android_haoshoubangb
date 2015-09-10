package com.cgzz.job.b.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.cgzz.job.b.R;
import com.cgzz.job.b.bean.SortModel2;
import com.cgzz.job.b.view.ScrollerNumberPicker.OnSelectListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 */
@SuppressWarnings("unused")
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;
	private Context context;
	// private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
	// private HashMap<String, List<Cityinfo>> city_map = new HashMap<String,
	// List<Cityinfo>>();
	// private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String,
	// List<Cityinfo>>();
	// private static ArrayList<SortModel2> province_list_code = new
	// ArrayList<SortModel2>();
	// private static ArrayList<String> city_list_code = new
	// ArrayList<String>();
	// private static ArrayList<String> couny_list_code = new
	// ArrayList<String>();

	private ArrayList<SortModel2> mPhotolist = new ArrayList<SortModel2>();
	private ArrayList<SortModel2> mPhotolist2 = new ArrayList<SortModel2>();
	private ArrayList<SortModel2> mPhotolist3 = new ArrayList<SortModel2>();

	// private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String city_string;

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
	}

	// 获取城市信息
	private void getaddressinfo() {
		// 读取城市信息string
	}

	private String getWeek(String pTime) {

		String Week = "";

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			try {
				c.setTime(format.parse(pTime));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "周日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "周一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "周二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "周三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "周四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "周五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "周六";
		}

		return Week;
	}

	public void setData() {
		mPhotolist.clear();
		mPhotolist2.clear();
		mPhotolist3.clear();

		counyPicker.setVisibility(View.VISIBLE);
		provincePicker.setVisibility(View.VISIBLE);
		cityPicker.setVisibility(View.VISIBLE);

		SortModel2 s1 = new SortModel2();
		s1.setName("今天");
		mPhotolist.add(s1);

		SortModel2 s2 = new SortModel2();
		s2.setName("明天");
		mPhotolist.add(s2);

		SortModel2 s3 = new SortModel2();
		s3.setName("后天");
		mPhotolist.add(s3);

		 java.text.SimpleDateFormat df = new
		 java.text.SimpleDateFormat("yyyy-MM-dd");
		 java.util.Calendar calendar = java.util.Calendar.getInstance();
		
		
		 java.text.SimpleDateFormat df2 = new
		 java.text.SimpleDateFormat("MM月dd");
		 java.util.Calendar calendar2 = java.util.Calendar.getInstance();
		
		 String ymd="",ymd2="";
		 for(int i=1;i<7;i++){
			 SortModel2 s6 = new SortModel2();
		 calendar.roll(java.util.Calendar.DAY_OF_YEAR, 1);
		 ymd=df.format(calendar.getTime());
		 
		 
		 calendar2.roll(java.util.Calendar.DAY_OF_YEAR, 1);
		 ymd2=df2.format(calendar2.getTime());
		 
		 if(i>2){
			 getWeek(ymd);
			 s6.setName(getWeek(ymd)+ymd2 );
			 s6.setId(ymd);
				mPhotolist.add(s6);
		 }
	
		 
			
			
			
		 
		 }

		for (int i = 0; i < 24; i++) {
			SortModel2 s4 = new SortModel2();
			s4.setName(i + "点");
			mPhotolist2.add(s4);
		}
		//

		SortModel2 s6 = new SortModel2();
		s6.setName("00");
		mPhotolist3.add(s6);

		SortModel2 s7 = new SortModel2();
		s7.setName("10");
		mPhotolist3.add(s7);

		SortModel2 s8 = new SortModel2();
		s8.setName("20");
		mPhotolist3.add(s8);

		SortModel2 s9 = new SortModel2();
		s9.setName("30");
		mPhotolist3.add(s9);

		SortModel2 s10 = new SortModel2();
		s10.setName("40");
		mPhotolist3.add(s10);

		SortModel2 s11 = new SortModel2();
		s11.setName("50");
		mPhotolist3.add(s11);

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		try {
			provincePicker.setData(mPhotolist);
			provincePicker.setDefault(0);
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(hour);
			counyPicker.setData(mPhotolist3);
			counyPicker.setDefault(minute / 10);
		} catch (Exception e) {
			provincePicker.setData(mPhotolist);
			provincePicker.setDefault(0);
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(0);
			counyPicker.setData(mPhotolist3);
			counyPicker.setDefault(0);
		}

	}

	public void setData1() {
		mPhotolist.clear();
		mPhotolist2.clear();
		mPhotolist3.clear();
		counyPicker.setVisibility(View.GONE);
		provincePicker.setVisibility(View.VISIBLE);
		cityPicker.setVisibility(View.VISIBLE);

		SortModel2 s1 = new SortModel2();
		s1.setName("01月");
		mPhotolist.add(s1);

		SortModel2 s2 = new SortModel2();
		s2.setName("02月");
		mPhotolist.add(s2);

		SortModel2 s3 = new SortModel2();
		s3.setName("03月");
		mPhotolist.add(s3);

		SortModel2 s4 = new SortModel2();
		s4.setName("04月");
		mPhotolist.add(s4);
		SortModel2 s5 = new SortModel2();
		s5.setName("05月");
		mPhotolist.add(s5);
		SortModel2 s6 = new SortModel2();
		s6.setName("06月");
		mPhotolist.add(s6);
		SortModel2 s7 = new SortModel2();
		s7.setName("07月");
		mPhotolist.add(s7);

		SortModel2 s8 = new SortModel2();
		s8.setName("08月");
		mPhotolist.add(s8);

		SortModel2 s9 = new SortModel2();
		s9.setName("09月");
		mPhotolist.add(s9);

		SortModel2 s10 = new SortModel2();
		s10.setName("10月");
		mPhotolist.add(s10);

		SortModel2 s11 = new SortModel2();
		s11.setName("11月");
		mPhotolist.add(s11);

		SortModel2 s12 = new SortModel2();
		s12.setName("12月");
		mPhotolist.add(s12);

		//

		for (int i = 1; i < 32; i++) {
			SortModel2 s20 = new SortModel2();
			s20.setName(i + "日");
			mPhotolist2.add(s20);
		}
		//

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;

		try {
			provincePicker.setData(mPhotolist);
			provincePicker.setDefault(month);
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(date - 1);
		} catch (Exception e) {
			provincePicker.setData(mPhotolist);
			provincePicker.setDefault(0);
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(0);

		}

	}

	public void setData2() {
		mPhotolist.clear();
		mPhotolist2.clear();
		mPhotolist3.clear();

		counyPicker.setVisibility(View.VISIBLE);
		provincePicker.setVisibility(View.GONE);
		cityPicker.setVisibility(View.VISIBLE);

		//

		for (int i = 0; i < 24; i++) {
			SortModel2 s4 = new SortModel2();
			s4.setName(i + "点");
			mPhotolist2.add(s4);
		}
		//

		SortModel2 s6 = new SortModel2();
		s6.setName("00");
		mPhotolist3.add(s6);

		SortModel2 s7 = new SortModel2();
		s7.setName("10");
		mPhotolist3.add(s7);

		SortModel2 s8 = new SortModel2();
		s8.setName("20");
		mPhotolist3.add(s8);

		SortModel2 s9 = new SortModel2();
		s9.setName("30");
		mPhotolist3.add(s9);

		SortModel2 s10 = new SortModel2();
		s10.setName("40");
		mPhotolist3.add(s10);

		SortModel2 s11 = new SortModel2();
		s11.setName("50");
		mPhotolist3.add(s11);

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		try {
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(8);
			counyPicker.setData(mPhotolist3);
			counyPicker.setDefault(3);
		} catch (Exception e) {
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(0);
			counyPicker.setData(mPhotolist3);
			counyPicker.setDefault(0);
		}

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		// citycodeUtil = CitycodeUtil.getSingleton();
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);

		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		counyPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {

			}

			@Override
			public void selecting(int id, String text) {

			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public String getCity_code_string() {
		return city_code_string;
	}

	public String getCity_string1() {
		// city_string = provincePicker.getSelectedText();
		return provincePicker.getSelectedText();
	}

	public String getCity_string2() {
		// city_string = cityPicker.getSelectedText();
		return cityPicker.getSelectedText();
	}

	public String getCity_string3() {
		// city_string = counyPicker.getSelectedText();
		return counyPicker.getSelectedText();
	}

	public int getSelected1() {
		return provincePicker.getSelected();
	}

	public  ArrayList<SortModel2> getPhotolist1() {
		return mPhotolist;
	}
	
	
	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
