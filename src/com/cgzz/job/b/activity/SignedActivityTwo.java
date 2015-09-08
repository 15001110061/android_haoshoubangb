package com.cgzz.job.b.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.BaseActivityCloseListener;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.Myadapter4;
import com.cgzz.job.b.application.GlobalVariables;
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
import com.lidroid.xutils.ViewUtils;
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
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/***
 * 
 * @author wjm 注册第一步
 */
public class SignedActivityTwo extends BaseActivity implements OnClickListener {

	public LinearLayout llLeft;
	public GlobalVariables application;
	public EditText et_signed_identification, et_signed_name, et_signed_age;
	public TextView tv_signed_next, et_signed_inviting;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;
	private PopupWindow popTheirProfile;
	private boolean picture_typePop;
	private String mobile, password, faceurl, cardurl;
	private ImageView iv_signed_photo_card, iv_signed_photo_face;

	/**
	 * 异步回调回来并处理数据
	 */
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.register_Http://
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserRegister(data);
					application.setUserId(bundle.getString("userId").toString());
					ToastUtil.makeShortText(SignedActivityTwo.this, bundle.get("msg").toString());
					application.popClosePath(true, UrlConfig.PATH_KEY_REGISTERED);
					Intent intent = new Intent(SignedActivityTwo.this, SignedActivityThree.class);
					startActivity(intent);
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserRegister(data);
					ToastUtil.makeShortText(SignedActivityTwo.this, bundle.get("msg").toString());
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signed_two);
		application = (GlobalVariables) getApplicationContext();
		application.putClosePath(UrlConfig.PATH_KEY_REGISTERED, new BaseActivityCloseListener() {

			@Override
			public void onFinish() {
				setResult(RESULT_OK);
				finish();
			}
		});

		setTitle("快速注册", true, TITLE_TYPE_IMG, R.drawable.stub_back, false, TITLE_TYPE_TEXT, "注册");
		ViewUtils.inject(this);
		Intent intent = getIntent();
		mobile = intent.getStringExtra("mobile");
		password = intent.getStringExtra("password");
		initView();
		initListenger();
		initmPopupWindowView();

	}

	private void initView() {

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);

		et_signed_identification = (EditText) findViewById(R.id.et_signed_identification);

		et_signed_name = (EditText) findViewById(R.id.et_signed_name);
		et_signed_age = (EditText) findViewById(R.id.et_signed_age);
		et_signed_inviting = (TextView) findViewById(R.id.et_signed_inviting);

		tv_signed_next = (TextView) findViewById(R.id.tv_signed_next);
		// rb_boy = (TextView) findViewById(R.id.rb_boy);
		// rb_girl = (TextView) findViewById(R.id.rb_girl);
		iv_signed_photo_card = (ImageView) findViewById(R.id.iv_signed_photo_card);//
		iv_signed_photo_face = (ImageView) findViewById(R.id.iv_signed_photo_face);//
	}

	private void initListenger() {
		llLeft.setOnClickListener(this);
		// rb_girl.setOnClickListener(this);
		// rb_boy.setOnClickListener(this);
		tv_signed_next.setOnClickListener(this);
		iv_signed_photo_face.setOnClickListener(this);
		iv_signed_photo_card.setOnClickListener(this);
		et_signed_inviting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_signed_next:// 下一步

			// intent = new Intent(SignedActivityTwo.this,
			// SignedActivityThree.class);
			// Bundle bundle2 = new Bundle();
			// bundle2.putString("mobile", "");// .
			// bundle2.putString("password", "");// .
			// bundle2.putString("card", "");// 身份证.
			// bundle2.putString("portrait", "");// 头图.
			// bundle2.putString("cardUrl", "");// 身份证图.
			// bundle2.putString("role", "");// 职务
			// bundle2.putString("realname", "");// 真实姓名.
			// bundle2.putString("mailbox", "");// 邮箱.
			//
			// intent.putExtras(bundle2);
			// startActivity(intent);

			String mailbox = et_signed_age.getText().toString();
			if (Utils.isEmpty(mailbox)) {
				ToastUtil.makeShortText(this, "邮箱不能为空");
				return;
			}
			if (!isEmail(mailbox)) {
				ToastUtil.makeShortText(this, "请输入正确的邮箱");
				return;
			}
			String identification = et_signed_identification.getText().toString();
			if (Utils.isEmpty(identification)) {
				ToastUtil.makeShortText(this, "姓名不能为空");
				return;
			}else{
				
				if(!checkNameChese(identification)){
					ToastUtil.makeShortText(this, "姓名请输入中文");
					return;
				}
			}

			String name = et_signed_name.getText().toString();
			if (Utils.isEmpty(name)) {
				ToastUtil.makeShortText(this, "身份证号不能为空");
				return;
			}

			// String role = et_signed_inviting.getText().toString();
			if (Utils.isEmpty(zhiwuid)) {
				ToastUtil.makeShortText(this, "职务不能为空");
				return;
			}

			if (Utils.isEmpty(faceurl)) {
				ToastUtil.makeShortText(this, "请上传头像图片");
				return;
			}

			if (Utils.isEmpty(cardurl)) {
				ToastUtil.makeShortText(this, "请上传身份证图片");
				return;
			}

			//

			register(UrlConfig.registerB_Http, mobile, password, name, mailbox, identification, cardurl, zhiwuid,
					faceurl, true);

			break;
		case R.id.ll_title_left:// 返回
			onBackPressed();
			break;

		// case R.id.rb_boy:// 选男.
		// rb_girl.setEnabled(true);
		// rb_boy.setEnabled(false);
		// break;
		// case R.id.rb_girl:// 选女
		// rb_girl.setEnabled(false);
		// rb_boy.setEnabled(true);
		// break;

		case R.id.iv_signed_photo_card:// 头像

			popTheirProfile(false);
			break;

		case R.id.iv_signed_photo_face:// 身份证
			popTheirProfile(true);
			break;

		case R.id.et_signed_inviting://
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

	public boolean checkNameChese(String name) {

		boolean res = true;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

			if (!isChinese(cTemp[i])) {

				res = false;

				break;

			}

		}

		return res;

	}

	public boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

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
						sharedPreferences.getString("tempName2", ""));

				// 保存本次截图临时文件名字
				fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
				Editor editor = sharedPreferences.edit();
				editor.putString("tempName2", fileName);
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

	// 判断格式是否为email
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
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

					UpdateTextTask mTask = new UpdateTextTask(SignedActivityTwo.this);
					mTask.execute(photo);

				} else {

					iv_signed_photo_card.setImageBitmap(photo);

					UpdateTextTask2 mTask2 = new UpdateTextTask2(SignedActivityTwo.this);
					mTask2.execute(photo);
				}

				break;
			default:
				break;
			}
		}
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
			String url = getPath(SignedActivityTwo.this, uri);
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

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	};

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
				ToastUtil.makeShortText(SignedActivityTwo.this, "上传失败，请重新上传图片");
				iv_signed_photo_face.setImageDrawable(getResources().getDrawable(R.drawable.icon_jiahao_geren));
			}
		});
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
				ToastUtil.makeShortText(SignedActivityTwo.this, "上传失败，请重新上传图片");
				iv_signed_photo_card.setImageDrawable(getResources().getDrawable(R.drawable.icon_jiahao_shenfen));
			}
		});
	}

	private void register(String url, String mobile, String password, String card, String mail, String realname,
			String cardUrl, String role, String portrait, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("mobile", mobile);
		map.put("password", password);
		map.put("card", card);
		map.put("mail", mail);
		map.put("realname", realname);
		map.put("cardUrl", cardUrl);
		map.put("role", role);
		map.put("portrait", portrait);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SignedActivityTwo.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(SignedActivityTwo.this), HttpStaticApi.register_Http, null, loadedtype);
	}

	/**
	 *
	 */
	String zhiwuid = "";
	PopupWindow popupwindow;
	CustomListView lvCars;

	public void initmPopupWindowView() {
		// 获取自定义布局文件的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);
		// 创建PopupWindow宽度和高度
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// 点击屏幕其他部分及Back键时PopupWindow消失
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
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
					et_signed_inviting.setText(listlv.get(arg2 - 1).get("name"));
					zhiwuid = listlv.get(arg2 - 1).get("id");
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

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "客房部");
		map.put("id", "1");
		list.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "财务部");
		map2.put("id", "2");
		list.add(map2);

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("name", "人事部");
		map3.put("id", "3");
		list.add(map3);
		return list;
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

			String face = ImageTools.savePhotoToSDCard(params[0], HttpStaticApi.Send_TheirProfile, "Bface");

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

			String card = ImageTools.savePhotoToSDCard(params[0], HttpStaticApi.Send_TheirProfile, "Bcard");

			return card;
		}

		@Override
		protected void onPostExecute(String result) {

			testUploadCard(UrlConfig.uploadiv_Http, "file", result);
		}
	}

}
