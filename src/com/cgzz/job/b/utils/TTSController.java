package com.cgzz.job.b.utils;

import android.content.Context;
import android.os.Bundle;

import com.cgzz.job.b.R;
import com.cgzz.job.b.application.GlobalVariables;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;

/**
 * 语音播报组件
 * 
 */
public class TTSController implements SynthesizerListener {

	public static TTSController ttsManager;
	private Context mContext;
	// 合成对象.
	private SpeechSynthesizer mSpeechSynthesizer;
	public GlobalVariables application;
	TTSController(Context context) {
		mContext = context;
		application = (GlobalVariables)mContext. getApplicationContext();
	}

	public static TTSController getInstance(Context context) {
		if (ttsManager == null) {
			ttsManager = new TTSController(context);
		}
		return ttsManager;
	}

	public void init() {
	
		SpeechUser.getUser().login(mContext, null, null,
				"appid=" + mContext.getString(R.string.app_id), listener);
		// 初始化合成对象.
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
		initSpeechSynthesizer();
	}

	/**
	 * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
	 * 
	 * @param
	 */
	public void playText(String playText) {
//		if(application.isSetupPhonetic()){
			if (!isfinish) {
				return;
			}
			if (null == mSpeechSynthesizer) {
				// 创建合成对象.
				mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
				initSpeechSynthesizer();
			}
			// 进行语音合成.
			mSpeechSynthesizer.startSpeaking(playText, this);
//		}
	

	}

	public void stopSpeaking() {
		if (mSpeechSynthesizer != null)
			mSpeechSynthesizer.stopSpeaking();
	}

	public void startSpeaking() {
		isfinish = true;
	}

	private void initSpeechSynthesizer() {
		// 设置发音人
		mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		// 设置语速
		mSpeechSynthesizer.setParameter(SpeechConstant.SPEED, "tts_speed");
		// 设置音量
		mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "tts_volume");
		// 设置语调
		mSpeechSynthesizer.setParameter(SpeechConstant.PITCH, "tts_pitch");

	}

	/**
	 * 用户登录回调监听器.
	 */
	private SpeechListener listener = new SpeechListener() {

		@Override
		public void onData(byte[] arg0) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error != null) {

			}
		}

		@Override
		public void onEvent(int arg0, Bundle arg1) {
		}
	};

	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	boolean isfinish = true;

	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub
		isfinish = true;
	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub
		isfinish = false;

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		if (mSpeechSynthesizer != null) {
			mSpeechSynthesizer.stopSpeaking();
		}
	}

}
