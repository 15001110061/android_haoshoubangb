//package com.cgzz.job.b.utils;
//
//import java.util.HashMap;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Toast;
//import cn.sharesdk.framework.Platform;
//import cn.sharesdk.framework.PlatformActionListener;
//
///**
// * 分享监听回调的类
// */
//public class ShareUtil {
//	private Context context;
//	private ShareHandler handler;
//	private SharePlatformActionListener platformActionListener;
//	private CanUseAPI useApi;
//
//	public ShareUtil(Context context, CanUseAPI useApi) {
//		this.context = context;
//		this.useApi = useApi;
//		handler = new ShareHandler();
//		platformActionListener = new SharePlatformActionListener();
//	}
//
//	/**
//	 * 得到分享监听
//	 */
//	public PlatformActionListener getPlatformActionListener() {
//		return platformActionListener;
//	}
//
//	// 分享监听
//	class SharePlatformActionListener implements PlatformActionListener {
//		@Override
//		public void onCancel(Platform arg0, int arg1) {
//			Message msg = new Message();
//			msg.arg1 = 0;
//			handler.sendMessage(msg);
//		}
//
//		@Override
//		public void onComplete(Platform arg0, int arg1,
//				HashMap<String, Object> arg2) {
//			Message msg = new Message();
//			msg.arg1 = 1;
//			handler.sendMessage(msg);
//		}
//
//		@Override
//		public void onError(Platform arg0, int action, Throwable t) {
//			t.printStackTrace();
//
//			Message msg = new Message();
//			msg.arg1 = 2;
//			msg.arg2 = action;
//			msg.obj = t;
//			handler.sendMessage(msg);
//		}
//	}
//
//	// 分享使用
//	class ShareHandler extends Handler {
//		public void handleMessage(Message msg) {
//			if (msg.arg1 == 0) {
//				ToastUtil.makeShortText(context, "取消分享");
//			} else if (msg.arg1 == 1) {
//				ToastUtil.makeShortText(context, "分享成功");
//				// 分享接口
//				useApi.startApi();
//			}
//			if (msg.arg1 == 2) {
//				String text = actionToString(msg.arg2);
//				// 失败
//				if ("WechatClientNotExistException".equals(msg.obj.getClass()
//						.getSimpleName())) {
//					text = "目前您的微信版本过低或未安装微信，需要安装微信才能使用";
//				} else if ("WechatTimelineNotSupportedException".equals(msg.obj
//						.getClass().getSimpleName())) {
//					text = "目前您的微信版本过低或未安装微信，需要安装微信才能使用";
//				} else {
//					text = "分享失败";
//				}
//				ToastUtil.makeShortText(context, text);
//			}
//		};
//	}
//
//	/** 将action转换为String */
//	public static String actionToString(int action) {
//		switch (action) {
//		case Platform.ACTION_AUTHORIZING:
//			return "ACTION_AUTHORIZING";
//		case Platform.ACTION_GETTING_FRIEND_LIST:
//			return "ACTION_GETTING_FRIEND_LIST";
//		case Platform.ACTION_FOLLOWING_USER:
//			return "ACTION_FOLLOWING_USER";
//		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
//			return "ACTION_SENDING_DIRECT_MESSAGE";
//		case Platform.ACTION_TIMELINE:
//			return "ACTION_TIMELINE";
//		case Platform.ACTION_USER_INFOR:
//			return "ACTION_USER_INFOR";
//		case Platform.ACTION_SHARE:
//			return "ACTION_SHARE";
//		default:
//			return "UNKNOWN";
//		}
//	}
//
//	/**
//	 * 调用接口回调
//	 * 
//	 * @author Administrator
//	 */
//	public interface CanUseAPI {
//		public void startApi();
//	}
//}
