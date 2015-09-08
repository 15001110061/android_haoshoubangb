package com.cgzz.job.b.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class SystemParams {
	private final String TAG = "SystemParams";
	private static SystemParams params;
	public float screenWidth;// 屏幕宽度，单位为px
	public float screenHeight;// 屏幕高度，单位为px
	public int densityDpi;// 屏幕密度，单位为dpi
	public float scale;// 缩放系数，值为 densityDpi/160
	public float fontScale;// 文字缩放系数，同scale

	public final static int SCREEN_ORIENTATION_VERTICAL = 1; // 屏幕状态：横屏
	public final static int SCREEN_ORIENTATION_HORIZONTAL = 2; // 屏幕状态：竖屏
	public int screenOrientation = SCREEN_ORIENTATION_VERTICAL;// 当前屏幕状态，默认为竖屏
	
	/**
	 * 私有构造方法
	 * 
	 * @param activity
	 */
	private SystemParams(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels - getStatusHeight(activity)-90-105-88-120;
		densityDpi = dm.densityDpi;
		scale = dm.density;
		fontScale = dm.scaledDensity;

		screenOrientation = screenHeight > screenWidth ? SCREEN_ORIENTATION_VERTICAL
				: SCREEN_ORIENTATION_HORIZONTAL;
		
	}

	/**
	 * 获取实例
	 * 
	 * @param activity
	 * @return
	 */
	public static SystemParams getInstance(Activity activity) {
		if (params == null) {
			params = new SystemParams(activity);
		}
		return params;
	}

	/**
	 * 获取一个新实例
	 * 
	 * @param activity
	 * @return
	 */
	public static SystemParams getNewInstance(Activity activity) {
		if (params != null) {
			params = null;
		}
		return getInstance(activity);
	}

	/**
	 * 参数信息
	 */
	public String toString() {
		return TAG
				+ ":[screenWidth: "
				+ screenWidth
				+ " screenHeight: "
				+ screenHeight
				+ " scale: "
				+ scale
				+ " fontScale: "
				+ fontScale
				+ " densityDpi: "
				+ densityDpi
				+ " screenOrientation: "
				+ (screenOrientation == SCREEN_ORIENTATION_VERTICAL ? "vertical"
						: "horizontal") + "]";
	}
	/**
	 * 获取状态栏高度
	 * @param activity
	 * @return
	 */
	public int getStatusHeight(Activity activity){  
	    int statusHeight = 0;  
	    Rect localRect = new Rect();  
	    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
	    statusHeight = localRect.top;  
	    if (0 == statusHeight){  
	        Class<?> localClass;  
	        try {  
	            localClass = Class.forName("com.android.internal.R$dimen");  
	            Object localObject = localClass.newInstance();  
	            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
	            statusHeight = activity.getResources().getDimensionPixelSize(i5);  
	        } catch (ClassNotFoundException e) {  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        } catch (InstantiationException e) {  
	            e.printStackTrace();  
	        } catch (NumberFormatException e) {  
	            e.printStackTrace();  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } catch (SecurityException e) {  
	            e.printStackTrace();  
	        } catch (NoSuchFieldException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return statusHeight;  
	} 
}

