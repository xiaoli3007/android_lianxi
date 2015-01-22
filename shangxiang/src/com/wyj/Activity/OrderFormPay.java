package com.wyj.Activity;

import com.wyj.Activity.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderFormPay extends Activity implements OnClickListener {

	private static String TAG = "OrderFormPay";

	private ImageView order_form_pay_back;
	private ProgressDialog pDialog = null;
	private Button pay_alipay_button;
	private Button pay_weixin_button;
	private Button pay_yinlian_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_form_pay);

		findViewById();
		setListener();

	}

	private void findViewById() {

		order_form_pay_back = (ImageView) findViewById(R.id.order_form_pay_back);
		pay_alipay_button = ( Button) findViewById(R.id.order_form_pay_alipay_submit);
		pay_weixin_button = ( Button) findViewById(R.id.order_form_pay_weixin_submit);
		pay_yinlian_button = ( Button) findViewById(R.id.order_form_pay_yinlian_submit);
	}

	private void setListener() {

		order_form_pay_back.setOnClickListener(this);
		pay_alipay_button.setOnClickListener(this);
		pay_weixin_button.setOnClickListener(this);
		pay_yinlian_button.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_form_pay_back:
			Intent bak_My_intent = new Intent(OrderFormPay.this, OrderFormDetail.class);
			WishGroupTab.getInstance().switchActivity("OrderFormDetail", bak_My_intent,
					-1, -1);
			break;
		case R.id.order_form_pay_alipay_submit:
			Intent intent2 = new Intent(OrderFormPay.this, OrderPaySucc.class);
			WishGroupTab.getInstance().switchActivity("OrderPaySucc", intent2,
					-1, -1);
			break;
		case R.id.order_form_pay_weixin_submit:
			Intent intent3 = new Intent(OrderFormPay.this, OrderPaySucc.class);
			WishGroupTab.getInstance().switchActivity("OrderPaySucc", intent3,
					-1, -1);
			break;
		case R.id.order_form_pay_yinlian_submit:
			Intent intent4 = new Intent(OrderFormPay.this, OrderPaySucc.class);
			WishGroupTab.getInstance().switchActivity("OrderPaySucc", intent4,
					-1, -1);
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			WishGroupTab.getInstance().onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}