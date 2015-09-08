package com.cgzz.job.b.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.cgzz.job.b.R;

public class Utils {
	public static String path = null;
	public static List<Activity> stackActivity = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		stackActivity.add(activity);
	}

	public static void closeActivity() {
		try {
			for (int i = 0; i < stackActivity.size(); i++) {
				if (!stackActivity.get(i).isFinishing()) {
					stackActivity.get(i).finish();
				}
			}
			stackActivity.clear();
		} catch (Exception e) {
		}
	}

	/**
	 * 获取app的存储目录
	 * <p>
	 * 一般情况下是这样的/storage/emulated/0/Android/data/包名/
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppDir(Context context) {
		return (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? (Environment
				.getExternalStorageDirectory().getPath() + "/Android/data/")
				: (context.getCacheDir().getPath()))
				+ context.getPackageName();
	}

	public static Intent photo(Context context) {

		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = getAppDir(context) + "/image/";
			File files = new File(filePath);
			if (!files.exists()) {
				files.mkdirs();
			}

			File file = new File(filePath, String.valueOf(System
					.currentTimeMillis()) + ".jpg");
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			path = file.getPath();
			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		} else {
			ToastUtil.makeShortText(context, "请插入内存卡");
		}
		return openCameraIntent;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取设备ID
	 * 
	 * @param context
	 * @return
	 */

	/**
	 * 获取应用AppKey
	 * 
	 * @return
	 */
	public static String getAppKey(Context context) {
		ApplicationInfo applicationInfo = null;
		String appKey = null;
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			appKey = applicationInfo.metaData.getString("app_key");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appKey;
	}

	public static String getAppSecret(Context context) {
		ApplicationInfo applicationInfo = null;
		String appSecret = null;
		try {
			applicationInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			appSecret = applicationInfo.metaData.getString("app_secret");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return appSecret;
	}

	public static boolean hasSDCard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * bitmap转成byte数组
	 * 
	 * @param avatar
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap avatar) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		avatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();

	}

	/**
	 * 此方法用于现场互动界面,格式固定. e.g.day=18,mounth=九月.
	 * 
	 * @param day
	 * @param month
	 * @return
	 */

	/**
	 * 转化月份.数字转汉字
	 * 
	 * @param num
	 * @return
	 */
	public static String Num2Hanzi(int num) {
		StringBuffer sb = new StringBuffer();
		String[] strings = new String[] { "一", "二", "三", "四", "五", "六", "七",
				"八", "九", "十", "十一", "十二" };
		return sb.append(strings[num]).append("月").toString();
	}

	/**
	 * 转化月份.汉字转数字
	 * 
	 * @param hanzi
	 * @return
	 */
	public static int Hanzi2Num(String hanzi) {
		int num = 0;
		int[] numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		String[] strings = new String[] { "一月", "二月", "三月", "四月", "五月", "六月",
				"七月", "八月", "九月", "十月", "十一月", "十二月" };
		for (int i = 0; i < strings.length; i++) {

			if (hanzi.equals(strings[i])) {
				num = numbers[i];
			}
		}
		return num;
	}

	/**
	 * 获取手机上的联系人电话.
	 * 
	 * @return
	 */
	public static void getContacts(Context context,
			final IContactCallback callback) {
		final Context ctx = context;
		new Thread(new Runnable() {
			@Override
			public void run() {

				StringBuffer sb = new StringBuffer();
				Cursor cursor = ctx.getContentResolver().query(
						ContactsContract.Contacts.CONTENT_URI, null, null,
						null, null);
				if (cursor != null) {
					while (cursor.moveToNext()) {

						String contactId = cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts._ID));
						String displayName = cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						int numberCount = cursor.getInt(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						if (numberCount > 0) {
							Cursor c = ctx
									.getContentResolver()
									.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
											null,
											ContactsContract.CommonDataKinds.Phone.CONTACT_ID
													+ "=" + contactId, null,
											null);
							if (c != null) {
								while (c.moveToNext()) {
									String number = c.getString(c
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									if (number.contains(" ")) {
										number = number.replace(" ", "");
									}
									if (number.contains("-")) {
										number = number.replace("-", "");
									}
									// 截取后11位.针对前面有+86前缀的.
									if (number.length() > 11) {
										number = number.subSequence(
												number.length() - 11,
												number.length()).toString();
									}

									sb.append(number);
									sb.append(",");
								}
								c.close();
								c = null;
							}
						}
					}
					cursor.close();
					cursor = null;
				}
				if (sb.toString().endsWith(",")) {
					sb.delete(sb.length() - 1, sb.length());
				}
				callback.callback(sb.toString());
			}

		}).start();
	}

	public interface IContactCallback {
		void callback(String string);
	}

	/**
	 * 判断edittext是否null
	 */
	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	/**
	 * 拨打电话
	 * 
	 * @param number
	 */

	public static byte[] readStream(DataInputStream inStream)
			throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 获取要从流中读取的数据长度 网络流数据格式 length+data
		int totalCount = inStream.readInt();
		byte[] buffer = new byte[totalCount];
		int len = -1;
		int readCount = 0;
		while (readCount < totalCount) {
			len = inStream.read(buffer, 0, totalCount - readCount);
			readCount += len;
			outStream.write(buffer, 0, len);
		}
		byte[] datas = outStream.toByteArray();
		outStream.close();
		return datas;
	}

	/**
	 *  * 用来判断服务是否运行.  * @param context  * @param className 判断的服务名字  * @return
	 * true 在运行 false 不在运行  
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 判断网络连接
	 * 
	 * @return true：连接网络（wif和3G） 。false：无网络连接
	 */
	public static boolean getDecideNetwork(Context context) {
		State state = null;
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获得网络类型
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();// wifi网络
		if (State.CONNECTED == state) {// 网络连接状态
			return true;
		}
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();// gprs网络
		if (State.CONNECTED == state) {// 网络连接状态
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @author lvliuyan
	 * */
	public static boolean isEmpty(String str) {
		if (str != null && !str.equals("")&& !str.equals("null")) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isPhoneNumberValid(String phoneNumber) {

		boolean isValid = false;
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static void dial(String number, Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method dial = iTelephony.getClass().getDeclaredMethod("dial",
					String.class);
			dial.invoke(iTelephony, number);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void call(String number, Context context) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method dial = iTelephony.getClass().getDeclaredMethod("call",
					String.class);
			dial.invoke(iTelephony, number);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void calls(String number, Context context) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ number));
		context.startActivity(intent);
	}

	public static void texts(String number, Context context) {

		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
				+ number));
		context.startActivity(intent);
	}

	public static String getAppInfo(Context context) {
		try {
			String pkName = context.getPackageName();
			String versionName = context.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			int versionCode = context.getPackageManager().getPackageInfo(
					pkName, 0).versionCode;
			return pkName + "   " + versionName + "  " + versionCode;
		} catch (Exception e) {
		}
		return null;
	}

	public static String getVersionName(Context context) {
		{
			try {
				PackageInfo pi = context.getPackageManager().getPackageInfo(
						context.getPackageName(), 0);
				return pi.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return context.getString(R.string.version_unknown);
			}
		}
	}
}
