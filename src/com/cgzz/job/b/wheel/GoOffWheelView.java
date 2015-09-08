package com.cgzz.job.b.wheel;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cgzz.job.b.R;

/**
 * 出发时间.
 */
public class GoOffWheelView extends PopupWindow implements OnClickListener {

	private Activity mContext;
	private View view;
	private ViewFlipper viewfipper;
	private TextView tvSubmit, tvCancel;
	private String time;
	private MinuteSpeciallyAdapter minuteAdapter;
	private HourSpeciallyAdapter hourAdapter;
	private DateSpeciallyAdapter dateAdapter;
	private WheelView date, hour, minute;
	private WheelDateChoiseListener wheelDate;
	private int curYear;
	private int curMonth;
	private int curDay;
	private int curHour;
	private int curMinute;

	public GoOffWheelView(Activity context, WheelDateChoiseListener wheelDate) {
		super(context);
		mContext = context;
		this.wheelDate = wheelDate;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.dialog_go_off, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		date = (WheelView) view.findViewById(R.id.date);
		hour = (WheelView) view.findViewById(R.id.hour);
		minute = (WheelView) view.findViewById(R.id.minute);
		tvSubmit = (TextView) view.findViewById(R.id.tv_go_off_ok);
		tvCancel = (TextView) view.findViewById(R.id.tv_go_off_cancel);
		
//		FrameLayout fl_id = (FrameLayout) view.findViewById(R.id.fl_id);
//		fl_id.getBackground().setAlpha(100);//0~255透明度值
		tvSubmit.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(date, hour, minute);
			}
		};
		curHour = calendar.get(Calendar.HOUR_OF_DAY);
		curMinute = calendar.get(Calendar.MINUTE);
		curYear = calendar.get(Calendar.YEAR);
		curMonth = calendar.get(Calendar.MONTH);
		curDay = calendar.get(Calendar.DATE);
		hourAdapter = new HourSpeciallyAdapter(context, 0, 23, 5);
		hour.setViewAdapter(hourAdapter);
		hour.setCurrentItem(Integer.valueOf(curHour));
		hour.addChangingListener(listener);
		hour.setCyclic(true);
		dateAdapter = new DateSpeciallyAdapter(context, 0, 61, 0, calendar);
		date.setViewAdapter(dateAdapter);
		date.setCurrentItem(1);
		date.addChangingListener(listener);
		// 初始化,勿删.
//		TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(
//				new int[] { R.attr.dialog_publish_time_now });
//		int resourceId = typedArray.getResourceId(0, 0);
//		typedArray.recycle();
//		String first = mContext.getString(resourceId); "嗯" + "_" +
		time = curYear + "-"
				+ ((curMonth) < 9 ? "0" + (curMonth + 1) : (curMonth + 1))
				+ "-" + (curDay < 10 ? "0" + curDay : curDay) + " "
				+ (curHour < 10 ? "0" + curHour : curHour) + ":"
				+ (curMinute / 10 + "0");
		minuteAdapter = new MinuteSpeciallyAdapter(context, 0, 5, 5);
		minute.setViewAdapter(minuteAdapter);
		minute.setCurrentItem(curMinute / 10);
		minute.addChangingListener(listener);
		minute.setCyclic(true);
		viewfipper.addView(view);
		viewfipper.setFlipInterval(6000000);
		hour.setEnabled(true);
		minute.setEnabled(true);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
	}

	private String getDate(int index) {
		Calendar c = Calendar.getInstance();
		// 现在时间
		int dayOfYear = c.get(Calendar.DAY_OF_YEAR) + index - 1;
		c.set(Calendar.DAY_OF_YEAR, dayOfYear);
		curMonth = c.get(Calendar.MONTH);
		curDay = c.get(Calendar.DATE);
		StringBuffer sb = new StringBuffer();
		if (index == 0) {
//			TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(
//					new int[] { R.attr.dialog_publish_time_now });
//			int resourceId = typedArray.getResourceId(0, 0);
//			typedArray.recycle();
//			sb.append(mContext.getString(resourceId));
		} else if (index == 1) {
			sb.append("今天");
		} else {
			sb.append(curMonth + 1).append("月").append(curDay).append("日")
					.append(" ").append(getWeekDay(c));
		}
		return sb.toString();
	}

	private String getHour(int index) {
		StringBuffer sb = new StringBuffer();
		if (index < 10) {
			sb.append("0");
		}
		sb.append(index);
		return sb.toString();
	}

	private String getMinute(int index) {
		StringBuffer sb = new StringBuffer();
		sb.append(index);
		sb.append("0");
		return sb.toString();
	}

	private String getWeekDay(Calendar c) {
		String week = "";
		if (c != null) {
			switch (c.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				week = "周一";
				break;
			case Calendar.TUESDAY:
				week = "周二";
				break;
			case Calendar.WEDNESDAY:
				week = "周三";
				break;
			case Calendar.THURSDAY:
				week = "周四";
				break;
			case Calendar.FRIDAY:
				week = "周五";
				break;
			case Calendar.SATURDAY:
				week = "周六";
				break;
			case Calendar.SUNDAY:
				week = "周日";
				break;
			default:
				week = "周一";
				break;
			}
		} else {
			week = "周一";
		}
		return week;
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}

	private void updateDays(WheelView date, WheelView hour, WheelView minute) {
		Calendar c = Calendar.getInstance();

		if (date.getCurrentItem() == 0) {// 随叫随到的时候.时分显示为当前时分.
			hour.setCurrentItem(c.get(Calendar.HOUR_OF_DAY));
			minute.setCurrentItem(c.get(Calendar.MINUTE) / 10);
			hour.setEnabled(false);
			minute.setEnabled(false);
			curHour = hour.getCurrentItem();
			curMinute = minute.getCurrentItem();
			curYear = c.get(Calendar.YEAR);
			curMonth = c.get(Calendar.MONTH);
			curDay = c.get(Calendar.DATE);
//			TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(
//					new int[] { R.attr.dialog_publish_time_now });
//			int resourceId = typedArray.getResourceId(0, 0);
//			typedArray.recycle();
//			String first = mContext.getString(resourceId); "嗯" + "_" +
			time =  curYear + "-"
					+ ((curMonth) < 9 ? "0" + (curMonth + 1) : (curMonth + 1))
					+ "-" + (curDay < 10 ? "0" + curDay : curDay) + " "
					+ (curHour < 10 ? "0" + curHour : curHour) + ":"
					+ (curMinute + "0");
		} else {
			if (date.getCurrentItem() != 1) {
				// 现在时间
				int dayOfYear = c.get(Calendar.DAY_OF_YEAR)
						+ date.getCurrentItem() - 1;
				c.set(Calendar.DAY_OF_YEAR, dayOfYear);
			}
			hour.setEnabled(true);
			minute.setEnabled(true);
			curHour = hour.getCurrentItem();
			curMinute = minute.getCurrentItem();
			curYear = c.get(Calendar.YEAR);
			curMonth = c.get(Calendar.MONTH);
			curDay = c.get(Calendar.DATE);
			time = curYear + "-"
					+ ((curMonth) < 9 ? "0" + (curMonth + 1) : (curMonth + 1))
					+ "-" + (curDay < 10 ? "0" + curDay : curDay) + " "
					+ (curHour < 10 ? "0" + curHour : curHour) + ":"
					+ (curMinute + "0");
		}

	}

	/**
	 * 定制分钟适配器
	 */
	private class MinuteSpeciallyAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public MinuteSpeciallyAdapter(Context context, int minValue,
				int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(24);
			setTextColor(context.getResources().getColor(R.color.light_black));
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return getMinute(index);
		}

	}

	/**
	 * 定制小时适配器
	 */
	private class HourSpeciallyAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public HourSpeciallyAdapter(Context context, int minValue,
				int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(24);
			setTextColor(context.getResources().getColor(R.color.light_black));
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return getHour(index);
		}

	}

	/**
	 * 定制日期适配器
	 */
	private class DateSpeciallyAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;
		private Calendar c;

		/**
		 * Constructor
		 */
		public DateSpeciallyAdapter(Context context, int minValue,
				int maxValue, int current, Calendar c) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			this.c = c;
			setTextSize(24);
			setTextColor(context.getResources().getColor(R.color.light_black));
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return getDate(index);
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_go_off_ok:
			wheelDate.chooseTime(time);
			this.dismiss();
			break;
		case R.id.tv_go_off_cancel:
			this.dismiss();
			break;
		default:
			break;
		}
		this.dismiss();
	}

	public interface WheelDateChoiseListener {
		public void chooseTime(String date);
	}

}

