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
	/** �����ؼ� */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** ѡ����� */
	private OnSelectingListener onSelectingListener;
	/** ˢ�½��� */
	private static final int REFRESH_VIEW = 0x001;
	/** ��ʱ���� */
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

	// ��ȡ������Ϣ
	private void getaddressinfo() {
		// ��ȡ������Ϣstring
	}

	public void setData() {
		mPhotolist.clear();
		mPhotolist2.clear();
		mPhotolist3.clear();
		
		
		counyPicker.setVisibility(View.VISIBLE);
		provincePicker.setVisibility(View.VISIBLE);
		cityPicker.setVisibility(View.VISIBLE);
		
		
		mPhotolist.add("����");
		mPhotolist.add("����");
		mPhotolist.add("����");
		//
		mPhotolist2.add("0��");
		mPhotolist2.add("1��");
		mPhotolist2.add("2��");
		mPhotolist2.add("3��");
		mPhotolist2.add("4��");
		mPhotolist2.add("5��");
		mPhotolist2.add("6��");
		mPhotolist2.add("7��");
		mPhotolist2.add("8��");
		mPhotolist2.add("9��");
		mPhotolist2.add("10��");
		mPhotolist2.add("11��");
		mPhotolist2.add("12��");
		mPhotolist2.add("13��");
		mPhotolist2.add("14��");
		mPhotolist2.add("15��");
		mPhotolist2.add("16��");
		mPhotolist2.add("17��");
		mPhotolist2.add("18��");
		mPhotolist2.add("19��");
		mPhotolist2.add("20��");
		mPhotolist2.add("21��");
		mPhotolist2.add("22��");
		mPhotolist2.add("23��");
		//
		mPhotolist3.add("00");
		mPhotolist3.add("10");
		mPhotolist3.add("20");
		mPhotolist3.add("30");
		mPhotolist3.add("40");
		mPhotolist3.add("50");

		Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�
		t.setToNow(); // ȡ��ϵͳʱ�䡣
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
		mPhotolist.add("01��");
		mPhotolist.add("02��");
		mPhotolist.add("03��");
		mPhotolist.add("04��");
		mPhotolist.add("05��");
		mPhotolist.add("06��");
		mPhotolist.add("07��");
		mPhotolist.add("08��");
		mPhotolist.add("09��");
		mPhotolist.add("10��");
		mPhotolist.add("11��");
		mPhotolist.add("12��");
		//
		for (int i = 1; i < 32; i++) {
			mPhotolist2.add(i + "��");
		}
		//

		Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�
		t.setToNow(); // ȡ��ϵͳʱ�䡣
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
//		mPhotolist.add("����");
//		mPhotolist.add("����");
//		mPhotolist.add("����");
		
		counyPicker.setVisibility(View.VISIBLE);
		provincePicker.setVisibility(View.GONE);
		cityPicker.setVisibility(View.VISIBLE);
		
		
		
		//
		mPhotolist2.add("0��");
		mPhotolist2.add("1��");
		mPhotolist2.add("2��");
		mPhotolist2.add("3��");
		mPhotolist2.add("4��");
		mPhotolist2.add("5��");
		mPhotolist2.add("6��");
		mPhotolist2.add("7��");
		mPhotolist2.add("8��");
		mPhotolist2.add("9��");
		mPhotolist2.add("10��");
		mPhotolist2.add("11��");
		mPhotolist2.add("12��");
		mPhotolist2.add("13��");
		mPhotolist2.add("14��");
		mPhotolist2.add("15��");
		mPhotolist2.add("16��");
		mPhotolist2.add("17��");
		mPhotolist2.add("18��");
		mPhotolist2.add("19��");
		mPhotolist2.add("20��");
		mPhotolist2.add("21��");
		mPhotolist2.add("22��");
		mPhotolist2.add("23��");
		//
		mPhotolist3.add("00");
		mPhotolist3.add("10");
		mPhotolist3.add("20");
		mPhotolist3.add("30");
		mPhotolist3.add("40");
		mPhotolist3.add("50");

		Time t = new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�
		t.setToNow(); // ȡ��ϵͳʱ�䡣
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
		// ��ȡ�ؼ�����
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
