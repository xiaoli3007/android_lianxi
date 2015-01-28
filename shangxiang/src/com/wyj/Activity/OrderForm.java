package com.wyj.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.wyj.Activity.R;

import com.wyj.dataprocessing.JsonHelper;
import com.wyj.http.WebApiUrl;
import com.wyj.pipe.SinhaPipeClient;
import com.wyj.pipe.SinhaPipeMethod;
import com.wyj.pipe.Utils;
import com.wyj.popupwindow.MyPopupWindows;
import com.wyj.popupwindow.MyPopupWindowsCity;
import com.wyj.popupwindow.MyPopupWindowsDate;
import com.wyj.popupwindow.MyPopupWindowsIncense;
import com.wyj.select.AbstractWheel;
import com.wyj.select.AbstractWheelTextAdapter;
import com.wyj.select.OnWheelChangedListener;
import com.wyj.select.OnWheelScrollListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class OrderForm extends Activity implements OnClickListener {

	private static String TAG = "OrderForm";

	private ImageView order_form_back;
	private ProgressDialog pDialog = null;
	private Button order_form_submit;
	private TextView buttonShowContentSelect;
	private LinearLayout create_order_select_location_layout;
	private Button hide_wish_content;
	private ScrollView ScrollView_form;
	private EditText order_form_layout_wish_content_input;

	private TextView order_form_layout_wish_address_input;
	private TextView order_form_layout_wish_date_input;
	private TextView order_form_layout_wish_xiangtype_input;
	private boolean scrolling = false;
	
	private SinhaPipeClient httpClient;
	private SinhaPipeMethod httpMethod;
	private boolean showLoading = false;
	
	
	private String[] incense_data ;
	private String[] wishcontent_data ;
	private JSONArray citylist_data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_form);

		findViewById();
		setListener();

	}

	private void findViewById() {
		
		
		order_form_back = (ImageView) findViewById(R.id.order_form_back);
		order_form_submit = (Button) findViewById(R.id.order_form_submit);
		buttonShowContentSelect = (TextView) findViewById(R.id.order_form_layout_wish_content_head_right); // 显示祝福语的点击按钮
		ScrollView_form = (ScrollView) findViewById(R.id.order_form_ScrollView);
		order_form_layout_wish_content_input = (EditText) findViewById(R.id.order_form_layout_wish_content_input);

		order_form_layout_wish_address_input = (TextView) findViewById(R.id.order_form_layout_wish_address_input);
		order_form_layout_wish_date_input =(TextView) findViewById(R.id.order_form_layout_wish_date_input);
		order_form_layout_wish_xiangtype_input =(TextView) findViewById(R.id.order_form_layout_wish_xiangtype_input);
	}

	private void setListener() {
		buttonShowContentSelect.setOnClickListener(this);
		order_form_back.setOnClickListener(this);
		order_form_submit.setOnClickListener(this);
		order_form_layout_wish_address_input.setOnClickListener(this);
		order_form_layout_wish_date_input.setOnClickListener(this);
		order_form_layout_wish_xiangtype_input.setOnClickListener(this);
		
		this.httpClient = new SinhaPipeClient();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order_form_back:
			Intent bak_My_intent = new Intent(OrderForm.this, ListTemple.class);
			WishGroupTab.getInstance().switchActivity("ListTemple",
					bak_My_intent, -1, -1);
			break;
		case R.id.order_form_submit:
			Intent intent1 = new Intent(OrderForm.this, OrderFormDetail.class);
			WishGroupTab.getInstance().switchActivity("OrderFormDetail",
					intent1, -1, -1);
			break;
		case R.id.order_form_layout_wish_content_head_right:
			
			if(wishcontent_data!=null){
				 new MyPopupWindows(
					OrderForm.this, order_form_layout_wish_content_input,
					getParent().getParent(),wishcontent_data);
			}else{
				loadWishcontent();
			}
			
			break;
		case R.id.order_form_layout_wish_address_input:
		
		if(citylist_data!=null){
			 new MyPopupWindowsCity(
				OrderForm.this, order_form_layout_wish_address_input,
				getParent().getParent(),citylist_data);
		}else{
			loadCity();
		}

			break;
		case R.id.order_form_layout_wish_date_input:

			 new MyPopupWindowsDate(
					OrderForm.this, order_form_layout_wish_date_input,
					getParent().getParent());

			break;
		case R.id.order_form_layout_wish_xiangtype_input:
			
			if(incense_data!=null){
				 new MyPopupWindowsIncense(
					OrderForm.this, order_form_layout_wish_xiangtype_input,
					getParent().getParent(),incense_data);
			}else{
				loadIncense();
			}
			
		break;

		}
	}

	
	private void loadIncense() {
		if (Utils.CheckNetwork()) {
			showLoading();
			Log.i("aaaa", "-----GET请求22222222222222222222-");
			this.httpClient.Config("get", WebApiUrl.GetIncense, null, true);
			this.httpMethod = new SinhaPipeMethod(this.httpClient, new SinhaPipeMethod.MethodCallback() {
				public void CallFinished(String error, Object result) {
					showLoading();
					if (null == error) {
						loadIncense((String) result);
					} else {
						int err = R.string.dialog_system_error_content;
						if (error == httpClient.ERR_TIME_OUT) {
							err = R.string.dialog_network_error_timeout;
						}
						if (error == httpClient.ERR_GET_ERR) {
							err = R.string.dialog_network_error_getdata;
						}
						Utils.ShowToast(OrderForm.this, err);
					}
				}
			});
			this.httpMethod.start();
		} else {
			Utils.ShowToast(OrderForm.this, R.string.dialog_network_check_content);
		}
	}

	private void loadIncense(String s) {
		if (null != s) {
			try {
				JSONObject result = new JSONObject(s);
				if (result.optString("code", "").equals("succeed")) {
					
					JSONArray jsonArray = result.getJSONArray("wishgradeinfo");
				

					String[] arrString = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						
						JSONObject jsonboject2=jsonArray.getJSONObject(i);
						
						arrString[i] = jsonboject2.getString("name")+"  ￥"+jsonboject2.getString("price");
					
					}
					incense_data=arrString;
					new MyPopupWindowsIncense(
					OrderForm.this, order_form_layout_wish_xiangtype_input,
					getParent().getParent(),arrString);
					 
					
					
				} else {
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private void loadWishcontent() {
		if (Utils.CheckNetwork()) {
			showLoading();
			
			this.httpClient.Config("get", WebApiUrl.GetWishcontent, null, true);
			this.httpMethod = new SinhaPipeMethod(this.httpClient, new SinhaPipeMethod.MethodCallback() {
				public void CallFinished(String error, Object result) {
					showLoading();
					if (null == error) {
						loadWishcontent((String) result);
					} else {
						int err = R.string.dialog_system_error_content;
						if (error == httpClient.ERR_TIME_OUT) {
							err = R.string.dialog_network_error_timeout;
						}
						if (error == httpClient.ERR_GET_ERR) {
							err = R.string.dialog_network_error_getdata;
						}
						Utils.ShowToast(OrderForm.this, err);
					}
				}
			});
			this.httpMethod.start();
		} else {
			Utils.ShowToast(OrderForm.this, R.string.dialog_network_check_content);
		}
	}

	private void loadWishcontent(String s) {
		if (null != s) {
			try {
				JSONObject result = new JSONObject(s);
				if (result.optString("code", "").equals("succeed")) {
					
					JSONArray jsonArray = result.getJSONArray("wishtextchoice");
					Log.i("aaaa", "-----GET请求11111111111111-"+jsonArray.toString());
					
					String[] arrString = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						
//						JSONObject jsonboject2=jsonArray.getString(i);
//						Log.i("aaaa", "-----GET请求22222222222222222222-"+jsonboject2.toString());
						arrString[i] = jsonArray.getString(i);
					
					}
					wishcontent_data=arrString;
					new MyPopupWindows(
					OrderForm.this, order_form_layout_wish_content_input,
					getParent().getParent(),arrString);
					 
				} else {
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadCity() {
		if (Utils.CheckNetwork()) {
			showLoading();
			
			this.httpClient.Config("post", WebApiUrl.Getprovincecitylist, null, true);
			this.httpMethod = new SinhaPipeMethod(this.httpClient, new SinhaPipeMethod.MethodCallback() {
				public void CallFinished(String error, Object result) {
					showLoading();
					if (null == error) {
						loadCity((String) result);
					} else {
						int err = R.string.dialog_system_error_content;
						if (error == httpClient.ERR_TIME_OUT) {
							err = R.string.dialog_network_error_timeout;
						}
						if (error == httpClient.ERR_GET_ERR) {
							err = R.string.dialog_network_error_getdata;
						}
						Utils.ShowToast(OrderForm.this, err);
					}
				}
			});
			this.httpMethod.start();
		} else {
			Utils.ShowToast(OrderForm.this, R.string.dialog_network_check_content);
		}
	}

	private void loadCity(String s) {
		if (null != s) {
			try {
				JSONObject result = new JSONObject(s);
				if (result.optString("code", "").equals("succeed")) {
					
					
					JSONArray jsonArray = result.getJSONArray("province_city");
				
					citylist_data=jsonArray;
					 new MyPopupWindowsCity(
								OrderForm.this, order_form_layout_wish_address_input,
								getParent().getParent(), citylist_data);
					 
				} else {
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showLoading() {
		
		if(pDialog!=null){
			pDialog.dismiss();
		}else{
			
			pDialog = new ProgressDialog(getParent().getParent());
			pDialog.setMessage("数据加载中。。。");
			pDialog.show();
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
