package com.wyj.Activity;



import com.wyj.Activity.R;
import com.wyj.select.AbstractWheel;
import com.wyj.select.AbstractWheelTextAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class OrderForm extends Activity implements OnClickListener {

	private static String TAG = "OrderForm";

	private ImageView order_form_back;
	private ProgressDialog pDialog = null;
    private Button order_form_submit;
    private TextView buttonShowContentSelect;
    private LinearLayout  layoutContentSelect;
    private Button hide_wish_content;
    private  ScrollView ScrollView_form;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_form);

		findViewById();
		setListener();

	}

	private void findViewById() {

		order_form_back = (ImageView) findViewById(R.id.order_form_back);
		order_form_submit =(Button) findViewById(R.id.order_form_submit);
		
		this.buttonShowContentSelect = (TextView) findViewById(R.id.order_form_layout_wish_content_head_right);
		this.buttonShowContentSelect.setOnClickListener(this);
		this.hide_wish_content = (Button) findViewById(R.id.order_hide_select_content_button);
		this.hide_wish_content.setOnClickListener(this);
		
		ScrollView_form=(ScrollView) findViewById(R.id.order_form_ScrollView);
		
		layoutContentSelect = (LinearLayout) findViewById(R.id.create_order_select_content_layout);
		final AbstractWheel contentSelect = (AbstractWheel) OrderForm.this.findViewById(R.id.create_order_select_content_view);
		ContentAdapter contentAdapter = new ContentAdapter(OrderForm.this);
		contentAdapter.contents = new String[] { "身体健康，财源广进", "身体健康，财源广进", "的反反复复发射点发", "身体健康，财源广进", "身体健康，财源广进", "身体健康，财源广进" , "身体健康，财源广进", "身体健康，财源广进" , "身体健康，财源广进", "身体健康，财源广进" , "身体健康，财源广进", "身体健康，财源广进"  };
		contentSelect.setViewAdapter(contentAdapter);
		contentSelect.setCurrentItem(2);
	}

	private void setListener() {

		order_form_back.setOnClickListener(this);
		order_form_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_form_back:
			Intent bak_My_intent = new Intent(OrderForm.this, ListTemple.class);
			WishGroupTab.getInstance().switchActivity("ListTemple", bak_My_intent,
					-1, -1);
			break;
		case R.id.order_form_submit:
			Intent intent1 = new Intent(OrderForm.this, OrderFormDetail.class);
			WishGroupTab.getInstance().switchActivity("OrderFormDetail", intent1,
					-1, -1);
			break;
		case R.id.order_form_layout_wish_content_head_right:
		
			this.layoutContentSelect.setVisibility(View.VISIBLE);
			break;
		case R.id.order_hide_select_content_button:
			
			this.layoutContentSelect.setVisibility(View.GONE);
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
	
	private class ContentAdapter extends AbstractWheelTextAdapter {
		String[] contents = new String[] {};

		protected ContentAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE); 
		}

		@Override
		public int getItemsCount() {
			return this.contents.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView JSCView = (TextView) view.findViewById(R.id.select_custom_text);
			JSCView.setText(this.contents[index]);
			return view;
		}
	}

}
