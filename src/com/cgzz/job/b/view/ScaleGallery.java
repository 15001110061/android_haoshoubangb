package com.cgzz.job.b.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class ScaleGallery extends Gallery {
	Camera mCamera = new Camera();
	Matrix mMatrix = new Matrix();
	private int mScaleParams = 100;

	public ScaleGallery(Context context) {
		this(context, null);
	}

	public ScaleGallery(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScaleGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (android.os.Build.VERSION.SDK_INT <= 15)
			setStaticTransformationsEnabled(true);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		if (android.os.Build.VERSION.SDK_INT > 15) {
			return false;
		} else {
			t.clear();
			t.setTransformationType(Transformation.TYPE_MATRIX);
			final float offset = calculateOffsetOfCenter(child);
			transformViewRoom(child, t, offset);
			return true;
		}
	}
	
	

	public int getmScaleParams() {
		return mScaleParams;
	}

	public void setmScaleParams(int mScaleParams) {
		this.mScaleParams = mScaleParams;
	}

	// 获取父控件中心点 X 的位置
	protected int getCenterOfCoverflow() {
		return ((getWidth() - getPaddingLeft() - getPaddingRight()) >> 1)
				+ getPaddingLeft();
	}

	// 获取 child 中心点 X 的位置
	protected int getCenterOfView(View view) {
		return view.getLeft() + (view.getWidth() >> 1);
	}

	// 计算 child 偏离 父控件中心的 offset 值， -1 <= offset <= 1
	protected float calculateOffsetOfCenter(View view) {
		final int pCenter = getCenterOfCoverflow();
		final int cCenter = getCenterOfView(view);

		float offset = (cCenter - pCenter) / (pCenter * 1.0f);
		offset = Math.min(offset, 1.0f);
		offset = Math.max(offset, -1.0f);

		return offset;
//		final int childCenter = getCenterOfView(view);
//        final int childWidth = view.getWidth();
//        float offset = ((float) (childWidth - childCenter) / childWidth);
//        return offset;
	}

	void transformViewRoom(View child, Transformation t, float race) {
		Camera mCamera = new Camera();
		mCamera.save();
		final Matrix matrix = t.getMatrix();
		final int halfHeight = child.getMeasuredHeight() >> 1;
		final int halfWidth = child.getMeasuredWidth() >> 1;
		int halfViewHeight = getHeight()/2;
		// 平移 X、Y、Z 轴已达到立体效果
		mCamera.translate(0.0f, -Math.abs(race)*halfViewHeight/28, Math.abs(race) * mScaleParams);
		// 也可设置旋转效果
		mCamera.getMatrix(matrix);
		// 以 child 的中心点变换
		matrix.preTranslate(-halfWidth, -halfHeight);
		matrix.postTranslate(halfWidth, halfHeight);
		mCamera.restore();
//		// 设置 alpha 变换
//		t.setAlpha(1 - Math.abs(race));
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		boolean ret;
		// Android SDK 4.1
		if (android.os.Build.VERSION.SDK_INT > 15) {
			final float offset = calculateOffsetOfCenter(child);
			
			getTransformationMatrix(child, offset);

			final int saveCount = canvas.save();
			canvas.concat(mMatrix);
			ret = super.drawChild(canvas, child, drawingTime);
			canvas.restoreToCount(saveCount);
		} else {
			ret = super.drawChild(canvas, child, drawingTime);
		}
		return ret;
	}

	void getTransformationMatrix(View child, float offset) {
		final int halfWidth = child.getLeft() + (child.getMeasuredWidth() >> 1);
		final int halfHeight = child.getMeasuredHeight() >> 1;
		int halfViewHeight = getHeight()/2;
		mCamera.save();
		mCamera.translate(0.0f, -Math.abs(offset)*halfViewHeight/28, Math.abs(offset) * mScaleParams);
			
		mCamera.getMatrix(mMatrix);
		mMatrix.preTranslate(-halfWidth, -halfHeight);
		mMatrix.postTranslate(halfWidth, halfHeight);
		mCamera.restore();
	}
	
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	float velocityY) {
	// TODO Auto-generated method stub
	return false;
	}
}

