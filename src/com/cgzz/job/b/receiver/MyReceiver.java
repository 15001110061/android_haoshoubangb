package com.cgzz.job.b.receiver;

import java.util.List;

import com.cgzz.job.b.activity.MembersActivity;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.http.ParserUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	Bundle bundles = null;
	public GlobalVariables application;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		application = (GlobalVariables) context.getApplicationContext();
//		System.out.println("wjm=Jpush==onReceive :" + intent.getAction() + ", extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//			System.out.println("wjm=Jpush==接收Registration Id :" + regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//			System.out.println("wjm=Jpush===接收到推送下来的自定义消息:" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//			System.out.println("wjm=Jpush==接收到推送下来的通知的ID:" + notifactionId);
//			
//			bundles = ParserUtil.JGlJSONC(bundle.getString(JPushInterface.EXTRA_EXTRA));
//
//			if (bundles != null && "5".equals(bundles.getString("type"))) {
//				application.setReddot(true);
//				redDot(context);
//			}
			
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//			System.out.println("wjm=Jpush==用户点击打开了通知");

			bundles = ParserUtil.JGlJSONC(bundle.getString(JPushInterface.EXTRA_EXTRA));

			if (bundles != null && "2".equals(bundles.getString("type"))) {
				if (getActivityName(context).equals("com.cgzz.job.b.activity.MembersActivity")) {
					MembersBroadcast(context);
				}else{
					Intent intents = new Intent(context, MembersActivity.class);
					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intents);
				}

			
			}

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
//			System.out.println("wjm=Jpush==用户收到到RICH PUSH CALLBACK:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
		} else {
		}
	}

//	private static String printBundle(Bundle bundle) {
//		StringBuilder sb = new StringBuilder();
//		for (String key : bundle.keySet()) {
//			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//			} else {
//				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//			}
//		}
//		return sb.toString();
//	}

	/***
	 * 栈顶的Activity名称
	 */
	public String getActivityName(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String packageName = cn.getClassName();
		return packageName;
	}

	public boolean isRunningForeground(Context c) {
		String packageName = getPackageName(c);
		String topActivityClassName = getTopActivityName(c);
		if (packageName != null && topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
			return true;
		} else {
			return false;
		}
	}

	public String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	public String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}

	public void MembersBroadcast(Context context) {
		Intent intentobd = new Intent("com.cgzz.job.accesstype");
		intentobd.putExtra("TYPE", "Members");
		context.sendBroadcast(intentobd);
	}
}
