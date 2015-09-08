package com.cgzz.job.b.view;

import java.util.ArrayList;

import com.cgzz.job.b.R;
import com.cgzz.job.b.view.ScrollerNumberPicker.OnSelectListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * 
 */
@SuppressWarnings("unused")
public class CityPicker2 extends LinearLayout {
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

	public CityPicker2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
	}

	public CityPicker2(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
	}

	// ��ȡ������Ϣ
	private void getaddressinfo() {
		// ��ȡ������Ϣstring
	}

	private ArrayList<String> mPhotolist = new ArrayList<String>();
	private ArrayList<String> mPhotolist2 = new ArrayList<String>();
	private ArrayList<String> mPhotolist3 = new ArrayList<String>();
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker2, this);
		// citycodeUtil = CitycodeUtil.getSingleton();
		// ��ȡ�ؼ�����
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);

		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
		counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);
		mPhotolist.add("����");
		mPhotolist.add("����");
		mPhotolist.add("����");
		//
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
		mPhotolist2.add("24��");
		//
		mPhotolist3.add("00");
		mPhotolist3.add("10");
		mPhotolist3.add("20");
		mPhotolist3.add("30");
		mPhotolist3.add("40");
		mPhotolist3.add("50");
		
		provincePicker.setData(mPhotolist);
		provincePicker.setDefault(0);
		cityPicker.setData(mPhotolist2);
		cityPicker.setDefault(0);
		counyPicker.setData(mPhotolist3);
		counyPicker.setDefault(1);
		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// System.out.println("id-->" + id + "text----->" + text);
				// if (text.equals("") || text == null)
				// return;
				// if (tempProvinceIndex != id)
				// {
				// System.out.println("end select");
				// String selectDay = cityPicker.getSelectedText();
				// if (selectDay == null || selectDay.equals(""))
				// return;
				// String selectMonth = counyPicker.getSelectedText();
				// if (selectMonth == null || selectMonth.equals(""))
				// return;
				// // ��������
				// cityPicker.setData(citycodeUtil.getCity(city_map,
				// citycodeUtil.getProvince_list_code().get(id)));
				// cityPicker.setDefault(1);
				// counyPicker.setData(citycodeUtil.getCouny(couny_map,
				// citycodeUtil.getCity_list_code().get(1)));
				// counyPicker.setDefault(1);
				// int lastDay = Integer.valueOf(provincePicker.getListSize());
				// if (id > lastDay)
				// {
				// provincePicker.setDefault(lastDay - 1);
				// }
				// }
				// tempProvinceIndex = id;
				// Message message = new Message();
				// message.what = REFRESH_VIEW;
				// handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// if (text.equals("") || text == null)
				// return;
				// if (temCityIndex != id)
				// {
				// String selectDay = provincePicker.getSelectedText();
				// if (selectDay == null || selectDay.equals(""))
				// return;
				// String selectMonth = counyPicker.getSelectedText();
				// if (selectMonth == null || selectMonth.equals(""))
				// return;
				//
				// counyPicker.setData(citycodeUtil.getCouny(couny_map,
				// citycodeUtil.getCity_list_code().get(id)));
				// counyPicker.setDefault(1);
				// int lastDay = Integer.valueOf(cityPicker.getListSize());
				// if (id > lastDay)
				// {
				// cityPicker.setDefault(lastDay - 1);
				// }
				// }
				// temCityIndex = id;
				// Message message = new Message();
				// message.what = REFRESH_VIEW;
				// handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// System.out.println("wjm===="+text);
			}
		});
		counyPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {

				// if (text.equals("") || text == null)
				// return;
				// if (tempCounyIndex != id)
				// {
				// String selectDay = provincePicker.getSelectedText();
				// if (selectDay == null || selectDay.equals(""))
				// return;
				// String selectMonth = cityPicker.getSelectedText();
				// if (selectMonth == null || selectMonth.equals(""))
				// return;
				// // ��������
				// city_code_string = citycodeUtil.getCouny_list_code()
				// .get(id);
				// int lastDay = Integer.valueOf(counyPicker.getListSize());
				// if (id > lastDay)
				// {
				// counyPicker.setDefault(lastDay - 1);
				// }
				// }
				// tempCounyIndex = id;
				// Message message = new Message();
				// message.what = REFRESH_VIEW;
				// handler.sendMessage(message);
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
		city_string = provincePicker.getSelectedText() ;
		return city_string;
	}
	public String getCity_string2() {
		city_string =  cityPicker.getSelectedText();
		return city_string;
	}
	public String getCity_string3() {
		city_string = counyPicker.getSelectedText();
		return city_string;
	}
	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
