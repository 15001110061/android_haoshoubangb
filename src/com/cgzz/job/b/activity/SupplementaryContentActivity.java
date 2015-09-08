package com.cgzz.job.b.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;
import com.cgzz.job.b.adapter.Myadapter4;
import com.cgzz.job.b.application.GlobalVariables;
import com.cgzz.job.b.bean.SystemConstant;
import com.cgzz.job.b.http.AnsynHttpRequest;
import com.cgzz.job.b.http.HttpStaticApi;
import com.cgzz.job.b.http.ObserverCallBack;
import com.cgzz.job.b.http.ParserUtil;
import com.cgzz.job.b.http.UrlConfig;
import com.cgzz.job.b.utils.ToastUtil;
import com.cgzz.job.b.utils.Utils;
import com.cgzz.job.b.view.CustomListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * @author wjm ��������
 */
public class SupplementaryContentActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
	private LinearLayout llLeft, llright;
	private ObserverCallBack callBack = new ObserverCallBack() {

		@Override
		public void back(String data, int encoding, int method, Object obj, boolean loadedtype) {
			dismissWaitDialog();
			Bundle bundle = null;
			switch (method) {
			case HttpStaticApi.myIncome_Http:
				switch (encoding) {
				case HttpStaticApi.SUCCESS_HTTP:

					break;
				case HttpStaticApi.FAILURE_HTTP:
					break;
				}
				break;

			}
		}
	};
	/**
	 * ��Ϣ������
	 */
	private Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 4:
				/**
				 * ��̬��׽��������仯������UI
				 */
				int volume = (Integer) msg.obj; // ��ȡ����������С
				LevelListDrawable levelDrawable = (LevelListDrawable) ivRecord.getDrawable();
				levelDrawable.setLevel(volume);
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supplementarycontent);
		setTitle("��������", true, TITLE_TYPE_IMG, R.drawable.stub_back, true, TITLE_TYPE_TEXT, "ȷ��");
		Intent intent = getIntent();
		Voiceurl = intent.getStringExtra("Voiceurl");
		havebar = intent.getStringExtra("havebar");
		havelaunch = intent.getStringExtra("havelaunch");
		iscash = intent.getStringExtra("iscash");
		bounus = intent.getStringExtra("bounus");
		mes = intent.getStringExtra("mes");
		length = intent.getStringExtra("length");
		FileName = intent.getStringExtra("FileName");

		initView();
		setView();

	}

	CheckBox iv_su_cb1, iv_su_cb2, iv_su_cb3, iv_su_cb4;
	Button sendMsg;
	EditText content;
	RelativeLayout rl_tam_2, rl_tam_1;
	TextView tv_tam_liuyan;
	Button btn_record;
	Vibrator mVibrator;
	ProgressBar progressbar;
	LinearLayout before_recored_layout, start_recored_layout;
	ImageButton btn_open_record;
	TextView iv_su_1, tv_tam_time;
	private View record_view;
	private ImageView ivRecord;
	private Dialog recordDialog;

	private void initView() {
		//
		LayoutInflater inflater = this.getLayoutInflater();
		record_view = inflater.inflate(R.layout.record_dialog, null);
		ivRecord = (ImageView) record_view.findViewById(R.id.iv_record);
		recordDialog = new Dialog(this, R.style.dialog);
		recordDialog.setContentView(record_view);

		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		llright = (LinearLayout) findViewById(R.id.ll_title_right);
		iv_su_cb1 = (CheckBox) findViewById(R.id.iv_su_cb1);

		iv_su_cb2 = (CheckBox) findViewById(R.id.iv_su_cb2);
		iv_su_cb3 = (CheckBox) findViewById(R.id.iv_su_cb3);
		iv_su_cb4 = (CheckBox) findViewById(R.id.iv_su_cb4);
		sendMsg = (Button) findViewById(R.id.sendMsg);
		content = (EditText) findViewById(R.id.content);
		btn_record = (Button) findViewById(R.id.btn_start_record);
		tv_tam_liuyan = (TextView) findViewById(R.id.tv_tam_liuyan);
		rl_tam_2 = (RelativeLayout) findViewById(R.id.rl_tam_2);
		rl_tam_1 = (RelativeLayout) findViewById(R.id.rl_tam_1);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
		before_recored_layout = (LinearLayout) findViewById(R.id.before_recored_layout);
		start_recored_layout = (LinearLayout) findViewById(R.id.start_recored_layout);
		iv_su_1 = (TextView) findViewById(R.id.iv_su_1);
		tv_tam_time = (TextView) findViewById(R.id.tv_tam_time);
		btn_open_record = (ImageButton) findViewById(R.id.btn_open_record);
		mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
		initmPopupWindowView();

		// int h= getResources().getDimensionPixelSize(R.dimen.dd_dimen_96px)
		// + getStatusHeight(SupplementaryContentActivity.this);//
//		 int w= getResources().getDimensionPixelSize(R.dimen.dd_dimen_302px)
//		 + getStatusHeight(SupplementaryContentActivity.this);//
//
//		 RelativeLayout.LayoutParams linearParams =
//		 (RelativeLayout.LayoutParams) rl_tam_1.getLayoutParams();
//		 linearParams.width = w;
////		 linearParams.height = h;
//		 rl_tam_1.setLayoutParams(linearParams);
		//

		if (!Utils.isEmpty(bounus)) {
			iv_su_cb1.setChecked(true);
			iv_su_1.setText("����" + bounus + "Ԫ/��");
		}

		if (!Utils.isEmpty(iscash)) {
			iv_su_cb2.setChecked(true);
		}

		if (!Utils.isEmpty(havelaunch)) {
			iv_su_cb3.setChecked(true);
		}
		if (!Utils.isEmpty(havebar)) {
			iv_su_cb4.setChecked(true);
		}
		if (!Utils.isEmpty(mes)) {
			rl_tam_2.setVisibility(View.VISIBLE);
			tv_tam_liuyan.setText(mes);
		}
		if (!Utils.isEmpty(FileName) && !Utils.isEmpty(Voiceurl)) {
			rl_tam_1.setVisibility(View.VISIBLE);
			progressbar.setVisibility(View.GONE);
			tv_tam_time.setVisibility(View.VISIBLE);
			tv_tam_time.setText(length + "s");
			
		}

	}

	private void setView() {
		llright.setOnClickListener(this);
		llLeft.setOnClickListener(this);
		sendMsg.setOnClickListener(this);
		rl_tam_1.setOnClickListener(this);
		iv_su_cb1.setOnCheckedChangeListener(this);
		iv_su_cb2.setOnCheckedChangeListener(this);
		iv_su_cb3.setOnCheckedChangeListener(this);
		iv_su_cb4.setOnCheckedChangeListener(this);
		btn_record.setOnTouchListener(new MyRecordTouchListener());

	}

	String mes = "";

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_title_left:// ����
			finish();
			break;
		case R.id.ll_title_right:// ȷ��

			Intent mIntents = new Intent();
			mIntents.putExtra("Voiceurl", Voiceurl);

			mIntents.putExtra("havebar", havebar);
			mIntents.putExtra("havelaunch", havelaunch);
			mIntents.putExtra("iscash", iscash);
			mIntents.putExtra("bounus", bounus);
			mIntents.putExtra("mes", mes);
			mIntents.putExtra("length", length);
			mIntents.putExtra("FileName", FileName);
			setResult(1, mIntents);
			finish();

			break;
		case R.id.sendMsg:// ����
			mes = content.getText().toString();
			if (Utils.isEmpty(mes)) {
				ToastUtil.makeShortText(this, "����д����");
				mes = "";
				return;
			}
			content.setText("");
			rl_tam_2.setVisibility(View.VISIBLE);
			tv_tam_liuyan.setText(mes);
			break;

		case R.id.rl_tam_1://

			if (!Utils.isEmpty(FileName) && !Utils.isEmpty(Voiceurl)) {
				MediaPlayer2(FileName);
			}

			break;

		default:
			break;
		}
	}

	public void openRecordWindow(View view) {
		InputMethodManager manager = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
		if (manager.isActive()) {
			manager.hideSoftInputFromWindow(content.getWindowToken(), 0);
		}

		if (before_recored_layout.getVisibility() == View.VISIBLE) {
			// btn_open_record.setImageResource(R.drawable.btn_keyboard);
			before_recored_layout.setVisibility(View.GONE);
			start_recored_layout.setVisibility(View.VISIBLE);

			Resources resources = getResources();
			Drawable btnDrawable = resources.getDrawable(R.drawable.icon_jianpan);
			btn_open_record.setBackgroundDrawable(btnDrawable);

		} else {
			// btn_open_record.setImageResource(R.drawable.btn_intercon);
			before_recored_layout.setVisibility(View.VISIBLE);
			start_recored_layout.setVisibility(View.GONE);
			manager.showSoftInput(content, 0);

			Resources resources = getResources();
			Drawable btnDrawable = resources.getDrawable(R.drawable.icon_yuyin);
			btn_open_record.setBackgroundDrawable(btnDrawable);
		}
	}

	/**
	 * �ҵ�����ӿ�
	 */
	private void getMyIncome(String url, String token, String userid, boolean loadedtype) {
		HashMap map = new HashMap<String, String>();
		map.put("apptype", 2 + "");
		map.put("token", token);
		map.put("userid", userid);
		AnsynHttpRequest.requestGetOrPost(AnsynHttpRequest.POST, SupplementaryContentActivity.this, url, map, callBack,
				GlobalVariables.getRequestQueue(SupplementaryContentActivity.this), HttpStaticApi.myIncome_Http, null,
				loadedtype);
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (arg0 == iv_su_cb1) {
			if (arg1) {
				if (popupwindow != null) {
					popupwindow.showAtLocation(findViewById(R.id.rl_seting_two), Gravity.BOTTOM, 0, 0);
				}

			} else {
				iv_su_1.setText("����");
				bounus = "";
			}
		}
		if (arg0 == iv_su_cb2) {
			if (arg1) {
				iscash = "1";
			} else {
				iscash = "";
			}
		}
		if (arg0 == iv_su_cb3) {
			if (arg1) {
				havelaunch = "1";
			} else {
				havelaunch = "";
			}
		}
		if (arg0 == iv_su_cb4) {
			if (arg1) {
				havebar = "1";
			} else {
				havebar = "";
			}
		}
	}

	/**
	 * ����������Ϣ������
	 * 
	 * @author Administrator
	 */
	MediaRecorder mRecorder;

	private final class MyRecordTouchListener implements View.OnTouchListener {
		private MediaPlayer mediaPlayer;
		private File file = null;
		private FileOutputStream dos = null;
		private long startTime;
		private boolean ifexit = true; // SD���Ƿ���ڵı�ʶ
		// private RecordThread thread = null;

		public boolean onTouch(View v, MotionEvent event) {
			try {
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:

					btn_record.setText("�ɿ� ����");
					btn_record.setBackgroundResource(R.drawable.shape_current_item_et_bgs);
					mediaPlayer = MediaPlayer.create(SupplementaryContentActivity.this, R.raw.play_completed);
					mediaPlayer.start();
					mVibrator.vibrate(new long[] { 100, 50 }, -1);
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

						FileName = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis()
								+ String.valueOf(application.getUserId()) + ".mp3";
						file = new File(FileName);
						// dos = new FileOutputStream(file);
						mRecorder = new MediaRecorder();
						mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
						mRecorder.setOutputFile(FileName);
						mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
						try {
							mRecorder.prepare();
						} catch (IOException e) {
						}
						mRecorder.start();
					} else {
						ToastUtil.makeShortText(SupplementaryContentActivity.this, "SD��������");
						ifexit = false;
						return true;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					startTime = System.currentTimeMillis();

					recordDialog.show();
					// ����������׽�����߳�
					// thread = new RecordThread(mRecorder);
					// thread.start();
					start();
					startTime = System.currentTimeMillis();
					break;
				case MotionEvent.ACTION_UP:
					recordDialog.dismiss();
					// thread.pause();
					mRecorder.stop();
					mRecorder.release();
					mRecorder = null;

					mHandler.removeCallbacks(mPollTask);
					// recordDialog.dismiss();
					btn_record.setText("��ס ˵��");
					btn_record.setBackgroundResource(R.drawable.shape_current_item_et_bg);
					mediaPlayer.release();

					mediaPlayer = null;
					if (ifexit == false)
						return true;
					// �ر�������׽�����߳�
					long endTime = System.currentTimeMillis();
					final long recordTime = endTime - startTime;
					if (recordTime < 1000) {
						rl_tam_1.setVisibility(View.GONE);
						file.delete();
					} else {
						rl_tam_1.setVisibility(View.VISIBLE);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									testVoice(UrlConfig.uploadiv_Http, "file", file, recordTime / 1000 + "");
								} catch (Exception e) {
									e.printStackTrace();
									if (null != file)
										file.delete();
									if (null != mediaPlayer)
										mediaPlayer.release();
								}
							}
						}).start();
					}
					mVibrator.cancel();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (null != file)
					file.delete();
				if (null != mediaPlayer)
					mediaPlayer.release();
			}
			return true;
		}
	}

	/**
	 * ������˷������ı仯
	 * 
	 * @author Administrator
	 */
	// public final class RecordThread extends Thread {
	// // private AudioRecord audioRecord;
	// // private int bufferSize = 100; // ¼�ƻ����С
	// private boolean isRun = true;
	// // private FileOutputStream output;
	// private int BLOW_BOUNDARY = 30; // �����ֵ֮�� �����¼�
	// MediaRecorder mediaRecorder;
	// private double BASE = 2700.0;
	//
	// public RecordThread(MediaRecorder mediaRecorder) {
	// this.mediaRecorder = mediaRecorder;
	// // bufferSize =
	// // AudioRecord.getMinBufferSize(SystemConstant.SAMPLE_RATE_IN_HZ,
	// // SystemConstant.CHANNEL_CONFIG,
	// // SystemConstant.AUDIO_FORMAT);
	// // ������Ƶ��¼�Ƶ�����CHANNEL_IN_STEREOΪ˫������CHANNEL_CONFIGURATION_MONOΪ������
	// // ��Ƶ���ݸ�ʽ:PCM 16λÿ����������֤�豸֧�֡�PCM 8λÿ����������һ���ܵõ��豸֧�֡�
	// // audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
	// // SystemConstant.SAMPLE_RATE_IN_HZ,
	// // SystemConstant.CHANNEL_CONFIG, SystemConstant.AUDIO_FORMAT,
	// // bufferSize);
	// }
	//
	// public void run() {
	// long time = 0;
	// long currenttime = 0;
	// long endtime = 0;
	// int total = 0;
	// int number = 0;
	// // audioRecord.startRecording();
	// // ���ڶ�ȡ�� buffer
	// // byte[] buffer = new byte[bufferSize];
	//
	// try {
	// while (isRun) {
	// number++;
	// currenttime = System.currentTimeMillis();
	// // int r = audioRecord.read(buffer, 0, bufferSize);// ��ȡ��������
	// // int v = 0;
	// // for (int i = 0; i < buffer.length; i++) {
	// // v += Math.abs(buffer[i]);// ȡ����ֵ����Ϊ����Ϊ��
	// // }
	// // output.write(buffer, 0, r);
	// // int value = Integer.valueOf(v / r);// ��õ�ǰ����ֵ��ƽ��ֵ
	// // total = total + value;
	// endtime = System.currentTimeMillis();
	// time = time + (endtime - currenttime);
	// // ������ʱ�����500������ߴ�������5�Σ��Ŵ�����Ƶ����time >= 500 ||
	//
	// double amp = getAmplitude(mediaRecorder);
	//
	// System.out.println("wjm===getMaxAmplitude=" + amp);
	// if (number > 5) {
	// int valume = total / number;
	// total = 0;
	// number = 0;
	// time = 0;
	// // �����Ĵ�С�ﵽһ����ֵ
	// // if (valume > BLOW_BOUNDARY) {
	// // ������Ϣ֪ͨ������ ��������
	// // int ratio = (int) (mediaRecorder.getMaxAmplitude() /
	// // BASE);
	// // System.out.println("wjm===ratio=" + ratio);
	// // int db = 0;// �ֱ�
	// // if (ratio > 1)
	// // db = (int) (20 * Math.log10(ratio));
	// // System.out.println("wjm��" + db / 4);
	// android.os.Message msg = handle.obtainMessage();
	// msg.obj = (int) amp;
	// msg.what = 4;
	// handle.sendMessage(msg);
	// // }
	// }
	// }
	// } catch (Exception e) {
	// System.out.println("wjm====e" + e);
	// } finally {
	//
	// // try {
	// // output.close();
	// // audioRecord.stop();
	// // audioRecord.release();
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// }
	// }
	//
	// public void pause() {
	// isRun = false;
	// // try {
	// // output.close();
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // }
	// }
	//
	// public void start() {
	// if (isRun) {
	// super.start();
	// }
	// }
	// }

	public double getAmplitude(MediaRecorder mediaRecorder) {
		if (mediaRecorder != null)
			return (mediaRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	private Handler mHandler = new Handler();

	private static final int POLL_INTERVAL = 300;

	private void start() {
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = getAmplitude(mRecorder);
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void updateDisplay(double signalEMA) {
		switch ((int) signalEMA) {
		case 0:
		case 1:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback001);
			break;
		case 2:
		case 3:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback002);

			break;
		case 4:
		case 5:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback003);
			break;
		case 6:
		case 7:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback004);
			break;
		case 8:
		case 9:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback005);
			break;
		case 10:
		case 11:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback006);
			break;
		default:
			ivRecord.setImageResource(R.drawable.voicesearch_feedback007);
			break;
		}
	}

	public void testVoice(String url, String name, File file, final String recordTime) {

		// ������������ı���
		RequestParams params = new RequestParams(); // Ĭ�ϱ���UTF-8
		// ����ļ�
		params.addBodyParameter("type", "4");
		params.addBodyParameter(name, file);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				rl_tam_1.setVisibility(View.VISIBLE);
				progressbar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {

				if (isUploading) {
					progressbar.setProgress((int) (current * 100 / total));
				} else {
					progressbar.setVisibility(View.GONE);
				}
			}

			// �ɹ�
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				tv_tam_time.setText(recordTime + "s");
				tv_tam_time.setVisibility(View.VISIBLE);
				progressbar.setVisibility(View.GONE);
				dismissWaitDialog();
				Bundle bundle = null;
				bundle = ParserUtil.ParserUpload(responseInfo.result);
				Voiceurl = bundle.get("url").toString();
//				length = bundle.get("length").toString();
				length=recordTime;
				System.out.println("wjm====length="+length);
			}

			// ʧ��
			@Override
			public void onFailure(HttpException error, String msg) {
				progressbar.setVisibility(View.GONE);
				rl_tam_1.setVisibility(View.GONE);
			}
		});

		// new RequestCallBack<String>()
	}

	PopupWindow popupwindow;
	CustomListView lvCars;
	String Voiceurl = "", havebar = "", havelaunch = "", iscash = "", bounus = "", length = "", FileName = "",recordTimes="";

	public void initmPopupWindowView() {
		// ��ȡ�Զ��岼���ļ�����ͼ
		View customView = getLayoutInflater().inflate(R.layout.popview_item, null, false);

		// ����PopupWindow��Ⱥ͸߶�
		popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

		ImageButton ib_dis = (ImageButton) customView.findViewById(R.id.ib_dis);
		popupwindow.setAnimationStyle(R.style.MyPopupAnimation);
		popupwindow.setOutsideTouchable(true);
		// �����Ļ�������ּ�Back��ʱPopupWindow��ʧ
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		// �Զ���view��Ӵ����¼�
		customView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
				return false;
			}
		});
		ib_dis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}

			}
		});
		popupwindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

				if ("".equals(bounus)) {
					iv_su_cb1.setChecked(false);
				}
			}
		});
		// ����
		lvCars = (CustomListView) customView.findViewById(R.id.listView_select);
		lvCars.setCacheColorHint(Color.TRANSPARENT);
		lvCars.setDivider(getResources().getDrawable(R.color.common_white));
		lvCars.setDividerHeight(Utils.dip2px(this, 0));
		lvCars.setFooterDividersEnabled(false);
		lvCars.setCanRefresh(false);// �ر�����ˢ��
		lvCars.setCanLoadMore(false);// �򿪼��ظ���
		lvCars.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (lvCars == arg0) {
					iv_su_1.setText("����" + listlv.get(arg2 - 1).get("name") + "Ԫ/��");
					bounus = listlv.get(arg2 - 1).get("name");
					popupwindow.dismiss();
				}
			}
		});
		adapter = new Myadapter4(this);
		lvCars.setAdapter(adapter);
		listlv = getbiaojian();
		adapter.refreshData(listlv);
	}

	Myadapter4 adapter;
	ArrayList<Map<String, String>> listlv;

	private ArrayList<Map<String, String>> getbiaojian() {
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 31; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + "");
			list.add(map);
		}
		return list;
	}

	MediaPlayer mediaPlayer;

	public void MediaPlayer2(String url) {

		try {
			if (mediaPlayer != null) {
				mediaPlayer.stop();// ֹͣ����
				mediaPlayer.release();// �ͷ���Դ
				mediaPlayer = null;
			}

			if (mediaPlayer == null)
				mediaPlayer = new MediaPlayer();
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.reset();// ����Ϊ��ʼ״̬
				mediaPlayer.stop();// ֹͣ����
				mediaPlayer.release();// �ͷ���Դ
			}
			try {
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepare();// ����
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();// ��ʼ��ָ�����
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {// ��������¼�
				@Override
				public void onCompletion(MediaPlayer arg0) {
					mediaPlayer.stop();// ֹͣ����
					mediaPlayer.release();// �ͷ���Դ
					mediaPlayer = null;
				}
			});
			mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {// �������¼�
				@Override
				public boolean onError(MediaPlayer player, int arg1, int arg2) {
					mediaPlayer.stop();// ֹͣ����
					mediaPlayer.release();// �ͷ���Դ
					return false;
				}
			});

		} catch (Exception e) {
			ToastUtil.makeShortText(SupplementaryContentActivity.this, "���ų���");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();// ֹͣ����
					mediaPlayer.release();// �ͷ���Դ
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
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
