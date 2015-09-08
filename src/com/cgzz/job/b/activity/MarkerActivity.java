package com.cgzz.job.b.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.utils.ToastUtil;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class MarkerActivity extends BaseActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener,
		OnMapLoadedListener, OnClickListener, InfoWindowAdapter {
	private MarkerOptions markerOption;
	// private TextView markerText;
	// private RadioGroup radioOption;
	private AMap aMap;
	private MapView mapView;
	private Marker marker2;// 有跳动效果的marker对象
	private LatLng latlng = new LatLng(36.061, 103.834);
	double longitude, latitude;
	LatLng HotelLatLng;
	String hotelratingbar, hoteltitle, hotelsnippet;
	LinearLayout llLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_activity);
		setTitle("地址", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "注册");
		Intent intent = getIntent();

		latitude = Double.valueOf(intent.getStringExtra("latitude"));
		longitude = Double.valueOf(intent.getStringExtra("longitude"));
		HotelLatLng = new LatLng(latitude, longitude);

		hotelratingbar = intent.getStringExtra("hotelratingbar");
		hoteltitle = intent.getStringExtra("hoteltitle");
		hotelsnippet = intent.getStringExtra("hotelsnippet");
		/*
		 * 
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		// markerText = (TextView) findViewById(R.id.mark_listenter_text);
		// radioOption = (RadioGroup)
		// findViewById(R.id.custom_info_window_options);
		// Button clearMap = (Button) findViewById(R.id.clearMap);
		// clearMap.setOnClickListener(this);
		// Button resetMap = (Button) findViewById(R.id.resetMap);
		// resetMap.setOnClickListener(this);
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap();// 往地图上添加marker

		llLeft.setOnClickListener(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		// Marker marker = aMap.addMarker(new MarkerOptions()
		//
		// .title("好好学习")
		// .icon(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		// .perspective(true).draggable(true));
		// marker.setRotateAngle(90);// 设置marker旋转90度
		// marker.showInfoWindow();// 设置默认显示一个infowinfow
		// marker.setPositionByPixels(400, 400);

		markerOption = new MarkerOptions();
		markerOption.position(HotelLatLng);
		markerOption.title("1").snippet("");
		markerOption.perspective(true);
		markerOption.draggable(true);
		markerOption.icon(
		// BitmapDescriptorFactory
		// .fromResource(R.drawable.location_marker)
				BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);

		// ArrayList<BitmapDescriptor> giflist = new
		// ArrayList<BitmapDescriptor>();
		// giflist.add(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		// giflist.add(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_RED));
		// giflist.add(BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		// MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
		// .position(Constants.CHENGDU).title("成都市")
		// .snippet("成都市:30.679879, 104.064855").icons(giflist)
		// .perspective(true).draggable(true).period(50);
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		// markerOptionlst.add(markerOption1);
		List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
		marker2 = markerlst.get(0);
		marker2.showInfoWindow();// 设置默认显示一个infowinfow
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (marker.equals(marker2)) {
			if (aMap != null) {
				// jumpPoint(marker);
			}
		}
		// markerText.setText("你点击的是" + marker.getTitle());
		return false;
	}

	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(HotelLatLng);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * HotelLatLng.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * HotelLatLng.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		ToastUtil.makeShortText(this, "你点击了infoWindow窗口" + marker.getTitle());
		ToastUtil.makeShortText(MarkerActivity.this, "当前地图可视区域内Marker数量:"
				+ aMap.getMapScreenMarkers().size());
	}

	/**
	 * 监听拖动marker时事件回调
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
		String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
				+ marker.getPosition().latitude + ","
				+ marker.getPosition().longitude + ")";
		// markerText.setText(curDes);
	}

	/**
	 * 监听拖动marker结束事件回调
	 */
	@Override
	public void onMarkerDragEnd(Marker marker) {
		// markerText.setText(marker.getTitle() + "停止拖动");
	}

	/**
	 * 监听开始拖动marker事件回调
	 */
	@Override
	public void onMarkerDragStart(Marker marker) {
		// markerText.setText(marker.getTitle() + "开始拖动");
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
		// LatLngBounds bounds = new LatLngBounds.Builder()
		// .include(HotelLatLng).build();
		// aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				HotelLatLng.latitude, HotelLatLng.longitude), 14));

	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
		// if (radioOption.getCheckedRadioButtonId() !=
		// R.id.custom_info_contents) {
		// return null;
		// }
		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		// if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window)
		// {
		// return null;
		// }
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		// if (radioOption.getCheckedRadioButtonId() ==
		// R.id.custom_info_contents) {
		// ((ImageView) view.findViewById(R.id.badge))
		// .setImageResource(R.drawable.badge_sa);
		// } else if (radioOption.getCheckedRadioButtonId() ==
		// R.id.custom_info_window) {
		// ImageView imageView = (ImageView) view.findViewById(R.id.badge);
		// imageView.setImageResource(R.drawable.badge_wa);
		// }
		// String title = marker.getTitle();

		TextView titleUi = ((TextView) view.findViewById(R.id.win_hotel_title));
		titleUi.setText(hoteltitle);
		// if (title != null) {
		// SpannableString titleText = new SpannableString(title);
		// titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
		// titleText.length(), 0);
		// titleUi.setTextSize(15);
		// titleUi.setText(titleText);
		//
		// } else {
		// titleUi.setText("");
		// }
		// String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view
				.findViewById(R.id.win_hotel_snippet));

		snippetUi.setText(hotelsnippet);

		// if (snippet != null) {
		// SpannableString snippetText = new SpannableString(snippet);
		// snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
		// snippetText.length(), 0);
		// snippetUi.setTextSize(20);
		// snippetUi.setText(snippetText);
		// } else {
		// snippetUi.setText("");
		// }
		com.cgzz.job.b.view.CustomerRatingBar win_hotel_ratingbar = ((com.cgzz.job.b.view.CustomerRatingBar) view
				.findViewById(R.id.win_hotel_ratingbar));
		try {
			int star = Integer.parseInt(hotelratingbar);
			if (star >= 3) {
				win_hotel_ratingbar.setProgress(star);
			} else {
				win_hotel_ratingbar.setVisibility(View.GONE);
			}

		} catch (Exception e) {
			win_hotel_ratingbar.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_title_left:
			finish();
			break;
		default:
			break;
		}
	}
}
