package com.cgzz.job.b.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cgzz.job.b.R;

public class CustomerRatingBar extends LinearLayout implements
		android.view.View.OnTouchListener {

	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	private ImageView image5;
	private int progress = 4;
	private static Drawable defDraw;
	private static Drawable highDraw;
	private OnProgressChangeListener listener = null;
	private View ratingView;
	private boolean touch;

	public interface OnProgressChangeListener {
		public void onProgressChange(View view, int progress);
	}

	public void setOnProgressChangeListener(View view,
			OnProgressChangeListener listener, boolean touch) {
		ratingView = view;
		this.listener = listener;
		this.touch = touch;
	}

	public CustomerRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.style_rating_bar, this);
		image1 = (ImageView) findViewById(R.id.rating_one);
		image2 = (ImageView) findViewById(R.id.rating_two);
		image3 = (ImageView) findViewById(R.id.rating_three);
		image4 = (ImageView) findViewById(R.id.rating_four);
		image5 = (ImageView) findViewById(R.id.rating_five);
		initDrawable();
		image1.setOnTouchListener(this);
		image2.setOnTouchListener(this);
		image3.setOnTouchListener(this);
		image4.setOnTouchListener(this);
		image5.setOnTouchListener(this);
	}

	public void setSize(ImageView view, int x, int y) {
		view.setLayoutParams(new LayoutParams(28, 26));
	}

	public void destroy() {
		if (defDraw != null) {
			defDraw = null;
		}

		if (highDraw != null) {
			highDraw = null;
		}

	}

	private void initDrawable() {
		if (defDraw == null) {
			defDraw = getResources().getDrawable(R.drawable.icon_star);
		}

		if (highDraw == null) {
			highDraw = getResources().getDrawable(R.drawable.icon_star);
		}
	}

	public int getProgress() {
		return progress;
	}

	@SuppressWarnings("deprecation")
	public void setProgress(int progress) {
		switch (progress) {

		case 1:
			image1.setBackgroundDrawable(highDraw);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			image4.setVisibility(View.GONE);
			image5.setVisibility(View.GONE);
//			image2.setBackgroundDrawable(defDraw);
//			image3.setBackgroundDrawable(defDraw);
//			image4.setBackgroundDrawable(defDraw);
//			image5.setBackgroundDrawable(defDraw);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 1);
			}
			break;

		case 2:
			image1.setBackgroundDrawable(highDraw);
			image2.setBackgroundDrawable(highDraw);
			
			image3.setVisibility(View.GONE);
			image4.setVisibility(View.GONE);
			image5.setVisibility(View.GONE);
//			image3.setBackgroundDrawable(defDraw);
//			image4.setBackgroundDrawable(defDraw);
//			image5.setBackgroundDrawable(defDraw);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 2);
			}
			break;

		case 3:
			image1.setBackgroundDrawable(highDraw);
			image2.setBackgroundDrawable(highDraw);
			image3.setBackgroundDrawable(highDraw);
			image4.setVisibility(View.GONE);
			image5.setVisibility(View.GONE);
//			image4.setBackgroundDrawable(defDraw);
//			image5.setBackgroundDrawable(defDraw);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 3);
			}
			break;

		case 4:
			image1.setBackgroundDrawable(highDraw);
			image2.setBackgroundDrawable(highDraw);
			image3.setBackgroundDrawable(highDraw);
			image4.setBackgroundDrawable(highDraw);
			image5.setVisibility(View.GONE);
//			image5.setBackgroundDrawable(defDraw);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 4);
			}
			break;

		case 5:
			image1.setBackgroundDrawable(highDraw);
			image2.setBackgroundDrawable(highDraw);
			image3.setBackgroundDrawable(highDraw);
			image4.setBackgroundDrawable(highDraw);
			image5.setBackgroundDrawable(highDraw);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 5);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!touch) {
			return false;
		}

		switch (v.getId()) {
		case R.id.rating_one:
			progress = 1;
			setProgress(1);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 1);
			}
			break;

		case R.id.rating_two:
			progress = 2;
			setProgress(2);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 2);
			}
			break;

		case R.id.rating_three:
			progress = 3;
			setProgress(3);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 3);
			}
			break;

		case R.id.rating_four:
			progress = 4;
			setProgress(4);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 4);
			}
			break;

		case R.id.rating_five:
			progress = 5;
			setProgress(5);
			if (listener != null && ratingView != null) {
				listener.onProgressChange(ratingView, 5);
			}
			break;

		default:
			break;
		}
		return false;
	}

}
