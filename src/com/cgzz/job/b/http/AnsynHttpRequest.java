package com.cgzz.job.b.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.cgzz.job.b.R;
import com.cgzz.job.b.activity.LoginActivity;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomDialog;

/**
 * 异步数据请求
 */
public class AnsynHttpRequest {
	public static final int POST = 1; // post 提交
	public static final int GET = 2; // get 提交

	/***
	 * get和post请求方法
	 * 
	 * @param sendType
	 *            请求类型：get和post
	 * @param context
	 *            上下文
	 * @param url
	 *            请求地址
	 * @param map
	 *            post使用到的
	 * @param callBack
	 *            异步回调
	 * @param mQueue
	 *            volly最终请求类
	 * @param i
	 *            请求的方法对应的int值（整个项目中唯一不重复的）
	 * @param obj
	 *            此参数用于一些http请求结果回调里,需要请求前传递一些参数的方法.不需要时,可以传空,不处理即可.
	 */
	public static void requestGetOrPost(final int sendType,
			final Context context, String url, final Map<String, String> map,
			final ObserverCallBack callBack, RequestQueue mQueue, final int i,
			final Object obj, final boolean loadedtype) {
		url = Utf8URLencode(url);
		switch (sendType) {
		case POST:
			System.out.println("wjm===POST==map==" + map);
			StringRequest stringRequest = new StringRequest(Method.POST, url,
					new Response.Listener<String>() {// 成功回调
						@Override
						public void onResponse(String response) {
							System.out.println("wjm=========POST===:"
									+ response);
							if (isContinue(context, response) == 200) {
								callBack.back(response,
										HttpStaticApi.SUCCESS_HTTP, i, obj,
										loadedtype);
							} else if (isContinue(context, response) == 201) {
								callBack.back(response,
										HttpStaticApi.FAILURE_MSG_HTTP, i, obj,
										loadedtype);
							} else if (isContinue(context, response) == 0) {
								ToastUtil.makeShortText(
										context,
										context.getResources().getString(
												R.string.http_remote_e));
								callBack.back(null, HttpStaticApi.FAILURE_HTTP,
										i, obj, loadedtype);
							} else if (isContinue(context, response) == -1) {

								GlobalVariables application = (GlobalVariables) context
										.getApplicationContext();
								application.setLogon(false);
								CustomDialog.alertDialog(context, false, true,
										true, null, "您的账户在其他地方登录\n是否重新进行登录?",
										false,
										new CustomDialog.PopUpDialogListener() {

											@Override
											public void doPositiveClick(
													Boolean isOk) {
												Utils.closeActivity();
												if (isOk) {// 确定
													context.startActivity(new Intent(
															context,
															LoginActivity.class));
													((Activity) context)
															.finish();
												} else {

												}

											}
										});
								ToastUtil.makeShortText(
										context,
										context.getResources().getString(
												R.string.http_remote_login));

							}
						}
					}, new Response.ErrorListener() { // 请求失败
						@Override
						public void onErrorResponse(VolleyError error) {
							ToastUtil.makeShortText(
									context,
									context.getResources().getString(
											R.string.http_request_failed));
							callBack.back(null, HttpStaticApi.FAILURE_HTTP, i,
									obj, loadedtype);
						}
					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					return map;
				}

				@Override
				protected Response<String> parseNetworkResponse(
						NetworkResponse response) {
					try {

						String jsonString = new String(response.data, "UTF-8");
						return Response.success(jsonString,
								HttpHeaderParser.parseCacheHeaders(response));
					} catch (UnsupportedEncodingException e) {
						return Response.error(new ParseError(e));
					} catch (Exception je) {
						return Response.error(new ParseError(je));
					}
				}
			};

			mQueue.add(stringRequest);
			break;
		case GET:
			Response.Listener<String> listener = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					System.out.println("wjm=========GET===:" + response);

					if (isContinue(context, response) == 200) {
						callBack.back(response, HttpStaticApi.SUCCESS_HTTP, i,
								obj, loadedtype);
					} else if (isContinue(context, response) == 201) {
						callBack.back(response, HttpStaticApi.FAILURE_MSG_HTTP,
								i, obj, loadedtype);
					} else if (isContinue(context, response) == 0) {
						ToastUtil.makeShortText(context, context.getResources()
								.getString(R.string.http_remote_e));
						callBack.back(null, HttpStaticApi.FAILURE_HTTP, i, obj,
								loadedtype);
					} else if (isContinue(context, response) == -1) {

						GlobalVariables application = (GlobalVariables) context
								.getApplicationContext();
						application.setLogon(false);
						CustomDialog.alertDialog(context, false, true, true,
								null, "您的账户在其他地方登录\n是否重新进行登录?", false,
								new CustomDialog.PopUpDialogListener() {

									@Override
									public void doPositiveClick(Boolean isOk) {
										Utils.closeActivity();
										if (isOk) {// 确定
											context.startActivity(new Intent(
													context,
													LoginActivity.class));
											((Activity) context).finish();
										} else {

										}

									}
								});
						ToastUtil.makeShortText(context, context.getResources()
								.getString(R.string.http_remote_login));

					}
				}
			};
			Response.ErrorListener errorListener = new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					ToastUtil.makeShortText(context, context.getResources()
							.getString(R.string.http_request_failed));
					callBack.back(null, HttpStaticApi.FAILURE_HTTP, i, obj,
							loadedtype);
				}
			};
			StringRequest stringRequest2 = new StringRequest(url, listener,
					errorListener) {
				@Override
				protected Response<String> parseNetworkResponse(
						NetworkResponse response) {
					try {

						String jsonString = new String(response.data, "UTF-8");
						return Response.success(jsonString,
								HttpHeaderParser.parseCacheHeaders(response));
					} catch (UnsupportedEncodingException e) {
						return Response.error(new ParseError(e));
					} catch (Exception je) {
						return Response.error(new ParseError(je));
					}
				}
			};
			mQueue.add(stringRequest2);
			break;
		default:
			break;
		}

	}

	private static int isContinue(Context context, String json) {
		int isSuc = 0;
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(json);
			if (jsonObject.has("code")) {
				String code = jsonObject.optString("code");
				if ("".equals(code)) {
					if (jsonObject.has("status")) {
						int state = jsonObject.optInt("status");

						if (state == 200) {
							isSuc = 200;
						} else if (state == 201) {
							isSuc = 201;
						}

					}
				} else if ("-1".equals(code)) {
					isSuc = -1;
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuc = 0;
		}

		return isSuc;
	}

	static PopupWindow winDialog;

	// private static void showWinDialog(final Context context) {
	// final View view = LayoutInflater.from(context).inflate(
	// R.layout.main_weixin_top_rights, null);
	// winDialog = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
	// ViewGroup.LayoutParams.WRAP_CONTENT);
	// TextView btn_game_win_check_price = (TextView) view
	// .findViewById(R.id.btn_game_win_check_price);// 确定
	// TextView btn_game_win_share = (TextView) view
	// .findViewById(R.id.btn_game_win_share);// 取消
	//
	// TextView textView2 = (TextView) view.findViewById(R.id.textView2);
	// textView2.setText("您的账户在其他地方登录\n是否重新进行登录?");
	//
	// btn_game_win_check_price.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// context.startActivity(new Intent(context, LoginActivity.class));
	// Utils.closeActivity();
	// }
	// });
	// btn_game_win_share.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// context.startActivity(new Intent(context, HomeActivity.class));
	// Utils.closeActivity();
	//
	// }
	// });
	// winDialog.setOutsideTouchable(true);
	// winDialog.setFocusable(true);
	// winDialog.setTouchInterceptor(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
	// winDialog.dismiss();
	// // context.startActivity(new Intent(context,
	// // HomeActivity.class));
	// // Utils.closeActivity();
	// return true;
	// }
	// return false;
	// }
	// });
	// // // 此行代码可以在返回键按下的时候,使CancelDialog消失.
	// // winDialog.setBackgroundDrawable(new BitmapDrawable());
	// try {
	// winDialog.showAtLocation(((Activity) context).getWindow()
	// .getDecorView(), Gravity.CENTER, 0, 0);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// // }
	//
	// }

	/**
	 * Utf8URL编码
	 * 
	 * @param s
	 * @return
	 */
	public static String Utf8URLencode(String text) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 0 && c <= 255) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception ex) {
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}

	public static void readBitmapViaVolley(String imgUrl,
			final ImageView imageView, RequestQueue mQueue) {
		ImageRequest imgRequest = new ImageRequest(imgUrl,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						imageView.setBackgroundResource(0);
						imageView.setImageBitmap(arg0);
					}
				}, 300, 200, Config.ARGB_8888, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {

					}
				});
		mQueue.add(imgRequest);
	}

	// public static void readBitmapVolley(String imgUrl,
	// final ImageView imageView, Context context) {
	// ImageLoader mImageLoader = new ImageLoader(
	// GlobalVariables.getRequestQueue(context), new BitmapCache());
	// ImageListener listener = ImageLoader.getImageListener(imageView,
	// R.drawable.pictureloading, R.drawable.pictureloading);
	// mImageLoader.get(imgUrl, listener, 0, 0);
	// }

	// public static class BitmapCache implements ImageCache {
	// private LruCache<String, Bitmap> mCache;
	//
	// public BitmapCache() {
	// int maxSize = 10 * 1024 * 1024;
	// mCache = new LruCache<String, Bitmap>(maxSize) {
	// @Override
	// protected int sizeOf(String key, Bitmap value) {
	// return value.getRowBytes() * value.getHeight();
	// }
	//
	// };
	// }
	//
	// @Override
	// public Bitmap getBitmap(String url) {
	// return mCache.get(url);
	// }
	//
	// @Override
	// public void putBitmap(String url, Bitmap bitmap) {
	// mCache.put(url, bitmap);
	// }
	//
	// }

}
