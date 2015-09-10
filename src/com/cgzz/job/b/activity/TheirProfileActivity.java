package com.cgzz.job.b.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
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
import com.cgzz.job.b.view.CustomDialog;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm 基本资料
 */
public class TheirProfileActivity extends BaseActivity
		implements OnClickListener, TextWatcher, OnPoiSearchListener, OnItemClickListener {
	// public ImageLoader mImageLoader;
	RelativeLayout rl_my_workcard;
	private ObserverCallBack callbackData = new ObserverCallBack() {
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.updateHotelB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(TheirProfileActivity.this, bundle.get("msg").toString());

					application.setEmail(et_their_email.getText().toString());
					application.setRealname(iv_their_name.getText().toString());
					if (!Utils.isEmpty(faceurl)) {
						application.setFaceUrl(faceurl);
					}
					finish();
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(TheirProfileActivity.this, bundle.get("msg").toString());
					break;

				default:
					break;
				}
				break;

			case HttpStaticApi.exitHotelB_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(TheirProfileActivity.this, bundle.get("msg").toString());
					application.setHotelid("");
					ll_their_qiye.setVisibility(View.GONE);
					tv_their_bangding2.setVisibility(View.GONE);
					tv_their_bangding.setText("企业绑定");
					application.setReviewHotel("2");
					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				case HttpStaticApi.FAILURE_MSG_HTTP:
					bundle = ParserUtil.ParserMsg(data);
					ToastUtil.makeShortText(TheirProfileActivity.this, bundle.get("msg").toString());
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theirprofile);
		setTitle("基本资料", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "完成");
		findView();
		initPopWindow();
		Assignment();
		init();
	}

	private TextView iv_their_credit, tv_their_tel, tv_their_passwords, tv_their_bangding, tv_their_bangding2;
	private ImageView iv_their_pic;
	private LinearLayout llLeft, llright, ll_their_qiye;
	private AutoCompleteTextView et_their_destination;
	private EditText et_their_email, iv_their_name, iv_their_qiyetel;
	String latitudes = "", longitudes = "", address = "";

	private void findView() {
		iv_their_pic = (ImageView) findViewById(R.id.iv_their_pic);// 头像
		iv_their_credit = (TextView) findViewById(R.id.iv_their_credit);// 信用值
		tv_their_tel = (TextView) findViewById(R.id.tv_their_tel);// 电话
		et_their_email = (EditText) findViewById(R.id.et_their_email);// email
		iv_their_name = (EditText) findViewById(R.id.iv_their_name);// 姓名
		tv_their_passwords = (TextView) findViewById(R.id.tv_their_passwords);// 修改密码
		tv_their_bangding = (TextView) findViewById(R.id.tv_their_bangding);// 是否绑定企业
		iv_their_qiyetel = (EditText) findViewById(R.id.iv_their_qiyetel);// 企业电话
		et_their_destination = (AutoCompleteTextView) findViewById(R.id.et_their_destination);// 企业地址
		ll_their_qiye = (LinearLayout) findViewById(R.id.ll_their_qiye);// 是否显示企业信息
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		tv_their_bangding2 = (TextView) findViewById(R.id.tv_their_bangding2);// 审核状态
	}

	private void Assignment() {
		latitudes = application.getLatitudeHotel();
		longitudes = application.getLongitudeHotel();
		address = application.getAddress();
		mImageLoader = new ImageLoader(GlobalVariables.getRequestQueue(TheirProfileActivity.this), new BitmapCache());
		// 头像
		ImageListener listener = ImageLoader.getImageListener(iv_their_pic, R.drawable.icon_touxiangmoren,
				R.drawable.icon_touxiangmoren);
		try {
			mImageLoader.get(application.getFaceUrl(), listener);// 头像地址
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		iv_their_credit.setText(application.getJifen() + "积分");//
		tv_their_tel.setText(application.getMobile());// 个人电话
		et_their_email.setText(application.getEmail());// 企业邮箱
		iv_their_name.setText(application.getRealname());// 个人人名
		iv_their_qiyetel.setText(application.getPhone());// 企业电话
		et_their_destination.setText(application.getAddress());// 企业地址
		// "reviewHotel": 1,//1:审核通过，0：审核中，-1：审核未通过，2：无酒店信息
		if ("0".equals(application.getReviewHotel())) {
			tv_their_bangding2.setText("审核中 ");
			ll_their_qiye.setVisibility(View.GONE);
			tv_their_bangding.setText("企业绑定");
		} else if ("-1".equals(application.getReviewHotel())) {
			ll_their_qiye.setVisibility(View.GONE);
			tv_their_bangding.setText("企业绑定");
			tv_their_bangding2.setText("审核失败");
		} else if ("1".equals(application.getReviewHotel())) {
			ll_their_qiye.setVisibility(View.VISIBLE);
			tv_their_bangding.setText("解除企业绑定");

			// 是否能修改酒店信息 1：能 0：否
			if ("0".equals(application.getPriority())) {
				iv_their_qiyetel.setEnabled(false);
				et_their_destination.setEnabled(false);
			} else if ("1".equals(application.getPriority())) {
				iv_their_qiyetel.setEnabled(true);
				et_their_destination.setEnabled(true);
			}

		} else if ("2".equals(application.getReviewHotel())) {
			ll_their_qiye.setVisibility(View.GONE);
			tv_their_bangding.setText("企业绑定");
		}

	}

	private void init() {
		llLeft.setOnClickListener(this);
		llright.setOnClickListener(this);
		tv_their_passwords.setOnClickListener(this);
		iv_their_pic.setOnClickListener(this);
		tv_their_bangding.setOnClickListener(this);
		et_their_destination.addTextChangedListener(this);
		et_their_destination.setOnItemClickListener(this);
	}

	String name = "";
	String age = "";
	String gongling = "";
	String email = "";

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {

		case R.id.ll_title_left:// 返回
			finish();
			break;
		case R.id.ll_title_right:// 完成

			// if (Utils.isEmpty(address)) {
			// ToastUtil.makeShortText(this, "请填写家庭住址");
			// return;
			// }

			name = iv_their_name.getText().toString();
			if (Utils.isEmpty(name)) {
				ToastUtil.makeShortText(this, "姓名不能为空");
				return;
			} else {

				if (!checkNameChese(name)) {
					ToastUtil.makeShortText(this, "姓名请输入中文");
					return;
				}
			}

			email = et_their_email.getText().toString();
			if (Utils.isEmpty(email)) {
				ToastUtil.makeShortText(this, "邮箱不能为空");
				return;
			}
			if (!isEmail(email)) {
				ToastUtil.makeShortText(this, "请输入正确的邮箱");
				return;
			}

			updateHotel(UrlConfig.updateHotelB_Http, application.getUserId(), application.getToken(), "1", faceurl,
					email, name, address, iv_their_qiyetel.getText().toString(), latitudes, longitudes, true);
			break;

		case R.id.tv_their_passwords:// 修改密码

			intent = new Intent(TheirProfileActivity.this, ChangePasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_their_pic:// 头像
			popTheirProfile(true);
			break;

		case R.id.tv_their_bangding:// 是否绑定企业

			if ("0".equals(application.getReviewHotel())) {
				// tv_their_bangding2.setText("审核中 ");
				// ll_their_qiye.setVisibility(View.GONE);
				// tv_their_bangding.setText("企业绑定");

				CustomDialog.alertDialog(TheirProfileActivity.this, false, true, true, null, "是否取消审核中的申请", false,
						new CustomDialog.PopUpDialogListener() {

							@Override
							public void doPositiveClick(Boolean isOk) {
								if (isOk) {// 确定
									exitHotel(UrlConfig.exitHotelB_Http, application.getToken(),
											application.getUserId(), application.getHotelid(), true);
								} else {

								}

							}
						});

			} else if ("-1".equals(application.getReviewHotel())) {
				// ll_their_qiye.setVisibility(View.GONE);
				// tv_their_bangding.setText("企业绑定");
				// tv_their_bangding2.setText("审核失败");
				intent = new Intent(TheirProfileActivity.this, SignedActivityThree.class);
				startActivityForResult(intent, 11);
			} else if ("1".equals(application.getReviewHotel())) {
				// ll_their_qiye.setVisibility(View.VISIBLE);
				// tv_their_bangding.setText("解除企业绑定");

				CustomDialog.alertDialog(TheirProfileActivity.this, false, true, true, null, "是否解除企业", false,
						new CustomDialog.PopUpDialogListener() {

							@Override
							public void doPositiveClick(Boolean isOk) {
								if (isOk) {// 确定
									exitHotel(UrlConfig.exitHotelB_Http, application.getToken(),
											application.getUserId(), application.getHotelid(), true);
								} else {

								}

							}
						});

			} else if ("2".equals(application.getReviewHotel())) {
				// ll_their_qiye.setVisibility(View.GONE);
				// tv_their_bangding.setText("企业绑定");
				intent = new Intent(TheirProfileActivity.this, SignedActivityThree.class);
				startActivityForResult(intent, 11);
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

	// 判断格式是否为email
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	private void initPopWindow() {

		prompAdapter = new PromptAdapter(this);
		et_their_destination.setAdapter(prompAdapter);
		prompAdapter.refreshData(prompList);
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

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
						et_their_destination.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								// 保存到配置文件，通过历史记录读取。并跳转页面
								PoiLocation poh2 = prompList.get(position);
								longitudes = poh2.getLat().getLongitude() + "";
								latitudes = poh2.getLat().getLatitude() + "";
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	private List<PoiLocation> prompList = new ArrayList<PoiLocation>();
	private PromptAdapter prompAdapter;
	private PoiSearch poiSearch;// POI搜索
	private PoiSearch.Query query;// Poi查询条件类

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

	private PopupWindow popTheirProfile;

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
				popTheirProfile.dismiss();

				Uri imageUri = null;
				String fileName = null;
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// if (crop) {
				// 删除上一次截图的临时文件
				SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
				ImageTools.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(),
						sharedPreferences.getString("tempName1", ""));

				// 保存本次截图临时文件名字
				fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
				Editor editor = sharedPreferences.edit();
				editor.putString("tempName1", fileName);
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

	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

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
					fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName1", "");
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
				iv_their_pic.setImageBitmap(photo);

				UpdateTextTask mTask = new UpdateTextTask(TheirProfileActivity.this);
				mTask.execute(photo);

				break;

			default:
				break;
			}
		} else if (resultCode == 11) {
			if (data != null) {
				String isshenhe = data.getStringExtra("isshenhe");
				if ("y".equals(isshenhe)) {
					// 审核中
					application.setReviewHotel("0");
					tv_their_bangding2.setVisibility(View.VISIBLE);
					tv_their_bangding2.setText("审核中 ");

					// tv_their_bangding.setClickable(false);
				} else {

				}
			}
		}
	}

	// 截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			String url = getPath(TheirProfileActivity.this, uri);
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

	String faceurl = "";

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
				// application.setFaceUrl(bundle.get("url").toString());
				faceurl = bundle.get("url").toString();

			}

			// 失败
			@Override
			public void onFailure(HttpException error, String msg) {
				dismissWaitDialog2();
				ToastUtil.makeShortText(TheirProfileActivity.this, "上传失败，请重新上传图片");
				iv_their_pic.setImageDrawable(getResources().getDrawable(R.drawable.icon_nor_user));
			}
		});
	}

	private void updateHotel(String url, String userid, String token, String apptype, String portrait, String email,
			String realname, String companyAddress, String companyPhone, String latitudes, String longitudes,
			boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", apptype);
		map.put("userid", userid);
		map.put("token", token);
		if (!Utils.isEmpty(portrait)) 
		map.put("portrait", portrait);
		map.put("email", email);

		map.put("realname", realname);

		if ("0".equals(application.getReviewHotel())) {

		} else if ("-1".equals(application.getReviewHotel())) {

		} else if ("1".equals(application.getReviewHotel())) {

			// 是否能修改酒店信息 1：能 0：否
			if ("0".equals(application.getPriority())) {

			} else if ("1".equals(application.getPriority())) {

				if (!Utils.isEmpty(companyAddress)) {
					map.put("companyAddress", companyAddress);
					map.put("companyPhone", companyPhone);
					map.put("latitude", latitudes);
					map.put("longitude", longitudes);

				}

			}
		} else if ("2".equals(application.getReviewHotel())) {

		}

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, TheirProfileActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(TheirProfileActivity.this), HttpStaticApi.updateHotelB_Http, null,
				loadedtype);
	}

	private void exitHotel(String url, String token, String userid, String hotelid, boolean loadedtype) {
		showWaitDialog();
		HashMap map = new HashMap<String, String>();
		map.put("apptype", "1");
		map.put("token", token);
		map.put("userid", userid);
		map.put("hotelid", hotelid);

		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, TheirProfileActivity.this, url, map, callbackData,
				GlobalVariables.getRequestQueue(TheirProfileActivity.this), HttpStaticApi.exitHotelB_Http, null,
				loadedtype);
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

			String face = ImageTools.savePhotoToSDCard(params[0], HttpStaticApi.Send_TheirProfile, "face");

			return face;
		}

		@Override
		protected void onPostExecute(String result) {

			testUploadFace(UrlConfig.uploadiv_Http, "file", result);
		}
	}

}
