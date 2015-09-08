package com.cgzz.job.b.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.BaseActivityCloseListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.Myadapter4;
import com.cgzz.job.b.adapter.PromptAdapter;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.bean.PoiLocation;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ImageTools;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/***
 * 
 * @author wjm 注册第4步
 */
public class SignedActivityFive extends BaseActivity
		implements OnClickListener, TextWatcher, OnPoiSearchListener, OnItemClickListener {

	LinearLayout llLeft, llright;
	public GlobalVariables application;
	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callBack = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			// Message msg = new Message();
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.infoB_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserInfoB(data);
					setview(bundle);
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserInfoB(data);
					ToastUtil.makeShortText(SignedActivityFive.this, bundle.get("msg").toString());
					break;
				default:
					break;
				}
				break;

			case HttpStaticApi.createB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					// application.popClosePath(true,
					// UrlConfig.PATH_KEY_REGISTERED);
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SignedActivityFive.this, bundle.get("msg").toString());

					Intent mIntents = new Intent();
					mIntents.putExtra("isshenhe", "y");
					setResult(11, mIntents);
					finish();

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(SignedActivityFive.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}

		}
	};
	String latitude = "", longitude = "", address = "", hotelid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signed_five);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_REGISTERED, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});

		setTitle("三步完成注册", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "放弃");
		Intent intent = getIntent();
		hotelid = intent.getStringExtra("hotelid");
		if (!"".equals(hotelid)) {
			getInfo(UrlConfig.infoB_Http, hotelid, true);
		}
		initView();
		initListenger();
		initPopWindow();
		initmPopupWindowView();
	}

	AutoCompleteTextView et_signed_home;
	EditText et_signed_identification, et_signed_inviting2, et_signed_invitingtel2, et_signed_invitingtel;
	TextView et_signed_age, tv_signed_next, et_signed_identification2;
	ImageView iv_signed_photo_card, iv_signed_photo_face;

	private void initView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		et_signed_identification = (EditText) findViewById(R.id.et_signed_identification);
		et_signed_age = (TextView) findViewById(R.id.et_signed_age);
		et_signed_inviting2 = (EditText) findViewById(R.id.et_signed_inviting2);
		et_signed_invitingtel = (EditText) findViewById(R.id.et_signed_invitingtel);
		et_signed_invitingtel2 = (EditText) findViewById(R.id.et_signed_invitingtel2);
		iv_signed_photo_card = (ImageView) findViewById(R.id.iv_signed_photo_card);
		iv_signed_photo_face = (ImageView) findViewById(R.id.iv_signed_photo_face);
		et_signed_home = (AutoCompleteTextView) findViewById(R.id.et_signed_home2);
		tv_signed_next = (TextView) findViewById(R.id.tv_signed_next);
		et_signed_identification2 = (TextView) findViewById(R.id.et_signed_identification2);
	}

	private void setview(Bundle bundle) {

		et_signed_invitingtel.setText(bundle.get("phone").toString());
		et_signed_invitingtel2.setText(bundle.get("zzjgdm").toString());
		et_signed_identification.setText(bundle.get("name").toString());
		et_signed_home.setText(bundle.get("address").toString());

		et_signed_inviting2.setText(bundle.get("roomcounts").toString());
		if (!"".equals(bundle.get("license_photos"))) {
			// 头像
			ImageListener listener = ImageLoader.getImageListener(iv_signed_photo_face, R.drawable.icon_nor_user,
					R.drawable.icon_nor_user);
			mImageLoader.get(bundle.get("license_photos").toString(), listener);
			cardurl = bundle.get("license_photos").toString();
		}
		if (!"".equals(bundle.get("front_photos"))) {
			// 头像
			ImageListener listener = ImageLoader.getImageListener(iv_signed_photo_card, R.drawable.icon_nor_user,
					R.drawable.icon_nor_user);
			mImageLoader.get(bundle.get("front_photos").toString(), listener);
			faceurl = bundle.get("front_photos").toString();
		}

	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		et_signed_home.setOnClickListener(this);
		et_signed_home.addTextChangedListener(this);
		iv_signed_photo_card.setOnClickListener(this);
		iv_signed_photo_face.setOnClickListener(this);
		tv_signed_next.setOnClickListener(this);
		llright.setOnClickListener(this);
		et_signed_identification2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_signed_next:// 下一步
			if (Utils.isEmpty(star)) {
				ToastUtil.makeShortText(this, "请选择企业星级");
				return;
			}
			getCreate(UrlConfig.createB_Http, hotelid, "1", application.getUserId(),
					et_signed_identification.getText().toString(), address, et_signed_invitingtel.getText().toString(),
					et_signed_inviting2.getText().toString(), faceurl, cardurl,
					et_signed_invitingtel2.getText().toString(), application.getCityCode(), latitude, longitude, star,
					true);

			break;
		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.iv_signed_photo_card:// 左边

			popTheirProfile(false);
			break;

		case R.id.iv_signed_photo_face:// 右边
			popTheirProfile(true);
			break;
		case R.id.ll_title_right://
			application.popClosePath(true, UrlConfig.PATH_KEY_REGISTERED);
			break;
		case R.id.et_signed_identification2:// 星级

			if (popupwindow != null) {
				popupwindow.showAtLocation(findViewById(R.id.rl_signet_two), Gravity.BOTTOM, 0, 0);
				if (listlv != null)
					listlv.clear();
				listlv = getbiaojian();
				adapter.refreshData(listlv);
			}
			break;

		default:
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	private void initPopWindow() {

		prompAdapter = new PromptAdapter(this);
		et_signed_home.setAdapter(prompAdapter);
		prompAdapter.refreshData(prompList);
	}

	private List<PoiLocation> prompList = new ArrayList<PoiLocation>();
	private PromptAdapter prompAdapter;
	private PoiSearch poiSearch;// POI搜索
	private PoiSearch.Query query;// Poi查询条件类

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {
					List<PoiItem> poiItems = result.getPois();
					if (poiItems != null) {
						prompList.clear();
						for (int i = 0; i < poiItems.size(); i++) {
							PoiLocation ai = new PoiLocation();
							ai.setLat(poiItems.get(i).getLatLonPoint());
							ai.setTitle(poiItems.get(i).getTitle());

							prompList.add(ai);
						}
						// 刷新列表

						prompAdapter.refreshData(prompList);
						et_signed_home.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// 保存到配置文件，通过历史记录读取。并跳转页面
								PoiLocation poh2 = prompList.get(position);
								longitude = poh2.getLat().getLongitude() + "";
								latitude = poh2.getLat().getLatitude() + "";
								address = poh2.getTitle() + "";

							}
						});

					}

				}
			}
		}

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		doSearchQuery(arg0.toString().trim());

	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery(String searchText) {
		if (!Utils.isEmpty(searchText)) {

			query = new PoiSearch.Query(searchText, "", application.getCityCode());
			query.setPageSize(10);
			query.setPageNum(0);

			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();
		} else {
			// 显示历史记录
			// ToastUtil.makeShortText(DestinationActivity.this, "请输入搜索关键字");
		}

	}

	private String faceurl = "", cardurl = "";
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private boolean picture_typePop;
	PopupWindow popTheirProfile;

	public void popTheirProfile(final boolean typePop) {

		View popView = View.inflate(this, R.layout.pop_their_profile, null);

		ImageButton dis = (ImageButton) popView.findViewById(R.id.ib_dis);
		popTheirProfile = new PopupWindow(popView);
		popTheirProfile.setBackgroundDrawable(new BitmapDrawable());// 没有此句点击外部不会消失
		popTheirProfile.setOutsideTouchable(true);
		popTheirProfile.setFocusable(true);
		popTheirProfile.setAnimationStyle(R.style.MyPopupAnimation);
		popTheirProfile.setWidth(LayoutParams.FILL_PARENT);
		popTheirProfile.setHeight(LayoutParams.WRAP_CONTENT);
		popTheirProfile.showAtLocation(findViewById(R.id.rl_signet_two), Gravity.BOTTOM, 0, 0);

		TextView up = (TextView) popView.findViewById(R.id.tv_pop_up);
		TextView title = (TextView) popView.findViewById(R.id.tv_title);
		TextView under = (TextView) popView.findViewById(R.id.tv_pop_under);

		title.setText("请选择打开方式");
		up.setText("拍照");
		under.setText("相册");

		up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picture_typePop = typePop;
				popTheirProfile.dismiss();

				Uri imageUri = null;
				String fileName = null;
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// if (crop) {
				// 删除上一次截图的临时文件
				SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
				ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(),
						sharedPreferences.getString("tempName3", ""));

				// 保存本次截图临时文件名字
				fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
				Editor editor = sharedPreferences.edit();
				editor.putString("tempName3", fileName);
				editor.commit();
				// }else {
				// REQUEST_CODE = TAKE_PICTURE;
				// fileName = "image.jpg";
				// }
				imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, CROP);

			}
		});
		under.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picture_typePop = typePop;
				popTheirProfile.dismiss();

				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(openAlbumIntent, CROP);

			}
		});
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popTheirProfile.dismiss();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					String fileName;
					fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName2", "");
					uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
				}
				cropImage(uri, 100, 100, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				// if (photoUri != null) {
				// photo = BitmapFactory.decodeFile(photoUri.getPath());
				// }
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photo.compress(Bitmap.CompressFormat.PNG, 30, stream);
					}
				}
				if (picture_typePop) {
					iv_signed_photo_face.setImageBitmap(photo);

					UpdateTextTask mTask = new UpdateTextTask(SignedActivityFive.this);
					mTask.execute(photo);
				} else {

					iv_signed_photo_card.setImageBitmap(photo);

					UpdateTextTask2 mTask2 = new UpdateTextTask2(SignedActivityFive.this);
					mTask2.execute(photo);
				}

				break;
			default:
				break;
			}
		}
	}

	public void testUploadCard(String url, String name, String path) {

		// 设置请求参数的编码
		RequestParams params = new RequestParams(); // 默认编码UTF-8

		// 添加文件
		params.addBodyParameter("type", "1");
		params.addBodyParameter(name, new File(path));
		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				showWaitDialog2();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
				} else {
				}
			}

			// 成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dismissWaitDialog2();
				Bundle bundle = null;
				bundle = ParserUtil.ParserUpload(responseInfo.result);
				bundle.get("url");

				cardurl = bundle.get("url").toString();
			}

			// 失败
			@Override
			public void onFailure(HttpException error, String msg) {
				dismissWaitDialog2();
				ToastUtil.makeShortText(SignedActivityFive.this, "上传失败，请重新上传图片");
				iv_signed_photo_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_jiahao_menshi));

			}
		});
	}

	public void testUploadFace(String url, String name, String path) {

		// 设置请求参数的编码
		RequestParams params = new RequestParams(); // 默认编码UTF-8

		// 添加文件
		params.addBodyParameter("type", "1");
		params.addBodyParameter(name, new File(path));
		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				showWaitDialog2();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
				} else {
				}
			}

			// 成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				dismissWaitDialog2();
				Bundle bundle = null;
				bundle = ParserUtil.ParserUpload(responseInfo.result);
				bundle.get("url");
				faceurl = bundle.get("url").toString();
			}

			// 失败
			@Override
			public void onFailure(HttpException error, String msg) {
				dismissWaitDialog2();
				ToastUtil.makeShortText(SignedActivityFive.this, "上传失败，请重新上传图片");
				iv_signed_photo_face.setImageDrawable(getResources().getDrawable(R.drawable.icon_jiahao_yingye));
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = getPath(SignedActivityFive.this, uri);
			intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
		} else {
			intent.setDataAndType(uri, "image/*");
		}
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 4);

		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	private void getCreate(String url, String hotelid, String type, String userid, String name, String address,
			String phone, String roomcounts, String msUrl, String yyzzUrl, String zzjgdm, String cityno,
			String latitude, String longitude, String star, boolean loadedtype) {

		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("type", type + "");
		map.put("userid", userid + "");
		map.put("name", name + "");
		map.put("address", address + "");
		map.put("phone", phone + "");
		map.put("roomcounts", roomcounts + "");
		map.put("msUrl", msUrl + "");

		if (!Utils.isEmpty(hotelid)) {
			map.put("hotelid", hotelid + "");
		}

		map.put("latitude", latitude + "");
		map.put("longitude", longitude + "");
		map.put("cityno", cityno + "");
		map.put("zzjgdm", zzjgdm + "");
		map.put("yyzzUrl", yyzzUrl + "");
		map.put("star", star);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SignedActivityFive.this, url, map, callBack,
				GlobalVariables.getRequestQueue(SignedActivityFive.this), HttpStaticApi.createB_Http, null, loadedtype);
	}

	private void getInfo(String url, String hotelid, boolean loadedtype) {
		url = url + "?hotelid=" + hotelid;
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.GET, SignedActivityFive.this, url, null, callBack,
				GlobalVariables.getRequestQueue(SignedActivityFive.this), HttpStaticApi.infoB_Http, null, loadedtype);

	}

	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			} else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	class UpdateTextTask extends AsyncTask<Bitmap, Integer, String> {
		private Context context;

		UpdateTextTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Bitmap... params) {
			showWaitDialog2();

			String face = ImageTools.savePhotoToSDCard(params[0], HttpStaticApi.Send_TheirProfile, "BMface");

			return face;
		}

		@Override
		protected void onPostExecute(String result) {
			testUploadFace(UrlConfig.uploadiv_Http, "file", result);
		}
	}

	class UpdateTextTask2 extends AsyncTask<Bitmap, Integer, String> {
		private Context context;

		UpdateTextTask2(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Bitmap... params) {
			showWaitDialog2();

			String card = ImageTools.savePhotoToSDCard(params[0], HttpStaticApi.Send_TheirProfile, "BYcard");

			return card;
		}

		@Override
		protected void onPostExecute(String result) {

			testUploadCard(UrlConfig.uploadiv_Http, "file", result);
		}
	}

	/**
	 *
	 */
	String star = "";
	PopupWindow popupwindow;
	CustomListView lvCars;
	TextView tv_current_room, tv_current_room3;
	View tv_current_room2;

	public void initmPopupWindowView() {
		// 获取自定义布局文件的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		ImageButton dis = (ImageButton) customView.findViewById(R.id.ib_dis);

		tv_current_room = (TextView) customView.findViewById(R.id.tv_current_room);
		tv_current_room2 = (View) customView.findViewById(R.id.tv_current_room2);
		tv_current_room3 = (TextView) customView.findViewById(R.id.tv_current_room3);
		// 创建PopupWindow宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// 点击屏幕其他部分及Back键时PopupWindow消失
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupwindow.dismiss();
			}
		});
		// 自定义view添加触摸事件
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					// popupwindow = null;
				}
				return false;
			}
		});
		tv_current_room3.setOnClickListener(this);

		// 车牌
		lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
		lvCars.setCacheColorHint(Color.TRANSPARENT);
		lvCars.setDivider(getResources().getDrawable(R.color.common_white));
		lvCars.setDividerHeight(Utils.dip2px(this, 0));
		lvCars.setFooterDividersEnabled(false);
		lvCars.setCanRefresh(false);// 关闭下拉刷新
		lvCars.setCanLoadMore(false);// 打开加载更多
		lvCars.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (lvCars == arg0) {
					et_signed_identification2.setText(listlv.get(arg2 - 1).get("name"));
					star = listlv.get(arg2 - 1).get("id");
					popupwindow.dismiss();
				}
			}
		});
		adapter = new Myadapter4(this);
		lvCars.setAdapter(adapter);

	}

	Myadapter4 adapter;
	ArrayList<Map<String, String>> listlv;

	// 标间价格
	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Map<String, String> map5 = new HashMap<String, String>();
		map5.put("name", "5星");
		map5.put("id", "5");
		list.add(map5);
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("name", "4星");
		map4.put("id", "4");
		list.add(map4);
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("name", "3星");
		map3.put("id", "3");
		list.add(map3);

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "2星");
		map2.put("id", "2");
		list.add(map2);

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("name", "宾馆与连锁酒店");
		map1.put("id", "1");
		list.add(map1);

		Map<String, String> map0 = new HashMap<String, String>();
		map0.put("name", "其他");
		map0.put("id", "0");
		list.add(map0);
		return list;
	}
}
