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
 * @author wjm 支付凭证
 */
public class PayReceiptsActivity extends BaseActivity implements
		OnClickListener {
	String bangkesname, paytype, paytime, paymoney, orderno, realname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payreceipts);
		setTitle("支付凭证", true, TITLE_TYPE_IMG, R.drawable.stub_back, false,
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
		tv_pay_jine.setText("￥" + paymoney);
		if ("1".equals(paytype)) { // 支付方式1：向好手帮转账 2：好手帮签约用户 3:使用支付宝支付 4：现金支付
			iv_receipts_fukuanfangshi.setText("付款方式:通过向好手帮转账");
		} else if ("2".equals(paytype)) {
			iv_receipts_fukuanfangshi.setText("付款方式:通过好手帮签约用户转账");
		} else if ("3".equals(paytype)) {
			iv_receipts_fukuanfangshi.setText("付款方式:通过支付宝支付转账");
		} else if ("4".equals(paytype)) {
			iv_receipts_fukuanfangshi.setText("付款方式:现金支付");
		}

		iv_receipts_yanshou.setText("验收人员:" + realname);
		iv_receipts_fukuanduixiang.setText("付款对象:" + bangkesname);
		iv_receipts_danhao.setText("交易单号:" + orderno);
		iv_receipts_shijian.setText("交易时间:" + paytime);

	}

	private void init() {
		llLeft.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String url = null;
		switch (arg0.getId()) {
		case R.id.ll_title_left:// 返回
			finish();
			break;
		default:
			break;
		}

	}

}
