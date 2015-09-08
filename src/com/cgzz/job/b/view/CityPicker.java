package com.cgzz.job.b.view;

import java.util.ArrayList;

import com.cgzz.job.b.R;
import com.cgzz.job.b.view.ScrollerNumberPicker.OnSelectListener;

import android.annotation.SuppressLint;
import android.content.Context;
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
	private static ArrayList<String> province_list_code = new ArrayList<String>();
	private static ArrayList<String> city_list_code = new ArrayList<String>();
	private static ArrayList<String> couny_list_code = new ArrayList<String>();

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

	public void setData() {
		mPhotolist.clear();
		mPhotolist2.clear();
		mPhotolist3.clear();
		
		
		counyPicker.setVisibility(View.VISIBLE);
		provincePicker.setVisibility(View.VISIBLE);
		cityPicker.setVisibility(View.VISIBLE);
		
		
		mPhotolist.add("今天");
		mPhotolist.add("明天");
		mPhotolist.add("后天");
		//
		mPhotolist2.add("0点");
		mPhotolist2.add("1点");
		mPhotolist2.add("2点");
		mPhotolist2.add("3点");
		mPhotolist2.add("4点");
		mPhotolist2.add("5点");
		mPhotolist2.add("6点");
		mPhotolist2.add("7点");
		mPhotolist2.add("8点");
		mPhotolist2.add("9点");
		mPhotolist2.add("10点");
		mPhotolist2.add("11点");
		mPhotolist2.add("12点");
		mPhotolist2.add("13点");
		mPhotolist2.add("14点");
		mPhotolist2.add("15点");
		mPhotolist2.add("16点");
		mPhotolist2.add("17点");
		mPhotolist2.add("18点");
		mPhotolist2.add("19点");
		mPhotolist2.add("20点");
		mPhotolist2.add("21点");
		mPhotolist2.add("22点");
		mPhotolist2.add("23点");
		//
		mPhotolist3.add("00");
		mPhotolist3.add("10");
		mPhotolist3.add("20");
		mPhotolist3.add("30");
		mPhotolist3.add("40");
		mPhotolist3.add("50");

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
		mPhotolist.add("01月");
		mPhotolist.add("02月");
		mPhotolist.add("03月");
		mPhotolist.add("04月");
		mPhotolist.add("05月");
		mPhotolist.add("06月");
		mPhotolist.add("07月");
		mPhotolist.add("08月");
		mPhotolist.add("09月");
		mPhotolist.add("10月");
		mPhotolist.add("11月");
		mPhotolist.add("12月");
		//
		for (int i = 1; i < 32; i++) {
			mPhotolist2.add(i + "日");
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
//		mPhotolist.add("今天");
//		mPhotolist.add("明天");
//		mPhotolist.add("后天");
		
		counyPicker.setVisibility(View.VISIBLE);
		provincePicker.setVisibility(View.GONE);
		cityPicker.setVisibility(View.VISIBLE);
		
		
		
		//
		mPhotolist2.add("0点");
		mPhotolist2.add("1点");
		mPhotolist2.add("2点");
		mPhotolist2.add("3点");
		mPhotolist2.add("4点");
		mPhotolist2.add("5点");
		mPhotolist2.add("6点");
		mPhotolist2.add("7点");
		mPhotolist2.add("8点");
		mPhotolist2.add("9点");
		mPhotolist2.add("10点");
		mPhotolist2.add("11点");
		mPhotolist2.add("12点");
		mPhotolist2.add("13点");
		mPhotolist2.add("14点");
		mPhotolist2.add("15点");
		mPhotolist2.add("16点");
		mPhotolist2.add("17点");
		mPhotolist2.add("18点");
		mPhotolist2.add("19点");
		mPhotolist2.add("20点");
		mPhotolist2.add("21点");
		mPhotolist2.add("22点");
		mPhotolist2.add("23点");
		//
		mPhotolist3.add("00");
		mPhotolist3.add("10");
		mPhotolist3.add("20");
		mPhotolist3.add("30");
		mPhotolist3.add("40");
		mPhotolist3.add("50");

		Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month;
		int date = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;

		try {
//			provincePicker.setData(mPhotolist);
//			provincePicker.setDefault(0);
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(8);
			counyPicker.setData(mPhotolist3);
			counyPicker.setDefault(3);
		} catch (Exception e) {
//			provincePicker.setData(mPhotolist);
//			provincePicker.setDefault(0);
			cityPicker.setData(mPhotolist2);
			cityPicker.setDefault(0);
			counyPicker.setData(mPhotolist3);
			counyPicker.setDefault(0);
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ArrayList<String> mPhotolist = new ArrayList<String>();
	private ArrayList<String> mPhotolist2 = new ArrayList<String>();
	private ArrayList<String> mPhotolist3 = new ArrayList<String>();

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

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
