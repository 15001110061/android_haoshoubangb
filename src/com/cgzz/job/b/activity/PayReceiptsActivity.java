package com.cgzz.job.b.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgzz.job.b.BaseActivity;
import com.cgzz.job.b.R;

/***
 * @author wjm ֧��ƾ֤
 */
public class PayReceiptsActivity extends BaseActivity implements
		OnClickListener {
	String bangkesname, paytype, paytime, paymoney, orderno, realname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payreceipts);
		setTitle("֧��ƾ֤", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
				TITLE_TYPE_TEXT, "");
		Intent intent = getIntent();

		bangkesname = intent.getStringExtra("bangkesname");
		paytype = intent.getStringExtra("paytype");
		paytime = intent.getStringExtra("paytime");
		paymoney = intent.getStringExtra("paymoney");
		orderno = intent.getStringExtra("orderno");
		realname = intent.getStringExtra("realname");
		findView();
		init();

	}

	LinearLayout llLeft;
	TextView tv_pay_jine, iv_receipts_fukuanfangshi, iv_receipts_yanshou,
			iv_receipts_fukuanduixiang, iv_receipts_danhao,
			iv_receipts_shijian;

	private void findView() {
		llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
		tv_pay_jine = (TextView) findViewById(R.id.tv_pay_jine);
		iv_receipts_fukuanfangshi = (TextView) findViewById(R.id.iv_receipts_fukuanfangshi);
		iv_receipts_yanshou = (TextView) findViewById(R.id.iv_receipts_yanshou);
		iv_receipts_fukuanduixiang = (TextView) findViewById(R.id.iv_receipts_fukuanduixiang);
		iv_receipts_danhao = (TextView) findViewById(R.id.iv_receipts_danhao);
		iv_receipts_shijian = (TextView) findViewById(R.id.iv_receipts_shijian);
		tv_pay_jine.setText("��" + paymoney);
		if ("1".equals(paytype)) { // ֧����ʽ1������ְ�ת�� 2�����ְ�ǩԼ�û� 3:ʹ��֧����֧�� 4���ֽ�֧��
			iv_receipts_fukuanfangshi.setText("���ʽ:ͨ������ְ�ת��");
		} else if ("2".equals(paytype)) {
			iv_receipts_fukuanfangshi.setText("���ʽ:ͨ�����ְ�ǩԼ�û�ת��");
		} else if ("3".equals(paytype)) {
			iv_receipts_fukuanfangshi.setText("���ʽ:ͨ��֧����֧��ת��");
		} else if ("4".equals(paytype)) {
			iv_receipts_fukuanfangshi.setText("���ʽ:�ֽ�֧��");
		}

		iv_receipts_yanshou.setText("������Ա:" + realname);
		iv_receipts_fukuanduixiang.setText("�������:" + bangkesname);
		iv_receipts_danhao.setText("���׵���:" + orderno);
		iv_receipts_shijian.setText("����ʱ��:" + paytime);

	}

	private void init() {
		llLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String url = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// ����
			finish();
			break;
		default:
			break;
		}

	}

}
