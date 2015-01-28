package com.wyj.Activity;


import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wyj.Activity.TabMenu;
import com.wyj.adapter.FindItemListAdapter;
import com.wyj.dataprocessing.AsynTaskHelper;
import com.wyj.dataprocessing.BitmapManager;

import com.wyj.dataprocessing.AsynTaskHelper.OnDataDownloadListener;

import com.wyj.http.WebApiUrl;
import com.wyj.Activity.R;
import com.wyj.utils.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;

import android.view.View;

import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Find_item extends Activity implements OnClickListener {

	private ListView mListView;
	private int tid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_item);

		ImageView back = (ImageView) findViewById(R.id.order_back);
		back.setOnClickListener(this);
		Button tongyuanqifu = (Button) findViewById(R.id.tongyuan);
		tongyuanqifu.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.find_list_item);

		Intent intent = this.getIntent(); // 接受的数据
		Bundle budle = intent.getExtras();
		int orderid = budle.getInt("orderid");
		tid = budle.getInt("tid"); // 寺庙的传值

		Log.i("aaaa", "------orderid-----"+orderid);
		api_show_detail(null, WebApiUrl.GET_ORDER_DETAIL + "?oid=" + orderid,
				getParent());
	}

	private void api_show_detail(Map<String, Object> map,
			String order_detail_api_url, final Context context) {
		// TODO Auto-generated method stub
		AsynTaskHelper asyntask = new AsynTaskHelper();
		asyntask.dataDownload(order_detail_api_url, map,
				new OnDataDownloadListener() {
					@Override
					public void onDataDownload(String result) {
						if (result != null) {

							try {
								JSONObject jsonobject = new JSONObject(result);
								JSONObject orderinfoobject = jsonobject
										.getJSONObject("orderinfo");
								Ui_orderinfo(orderinfoobject);
								
								JSONArray member_jiachilist= jsonobject.getJSONArray("blessings_members");
								Ui_orderinfo_memberlist(member_jiachilist);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							} else {
							Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT)
									.show();
						}

					}
				}, context, "GET");
	}

	protected void Ui_orderinfo(JSONObject orderdetail_orderinfo) {
		// TODO Auto-generated method stub
		ImageView order_people_head = (ImageView) findViewById(R.id.finditem_people_head);
		TextView finditem_username = (TextView) findViewById(R.id.finditem_username);
		TextView finditem_templename = (TextView) findViewById(R.id.finditem_templename);
		TextView finditem_wishtext = (TextView) findViewById(R.id.finditem_wishtext);
		TextView finditem_blessingser = (TextView) findViewById(R.id.finditem_blessingser);

		BitmapManager.getInstance().loadBitmap(
				orderdetail_orderinfo.optString("headface", ""),
				order_people_head,
				Tools.readBitmap(Find_item.this, R.drawable.foot_07));
		finditem_username.setText(orderdetail_orderinfo.optString("wishname",
				""));
		finditem_templename.setText("刚刚在"
				+ orderdetail_orderinfo.optString("templename", "")
				+ orderdetail_orderinfo.optString("alsowish", "")
				+ orderdetail_orderinfo.optString("wishtype", "") + "");
		finditem_wishtext.setText(orderdetail_orderinfo.optString("wishtext",
				""));
		
		String bless="";
		if(!orderdetail_orderinfo.optString(
				"co_blessings").equals("0")){
			 bless=orderdetail_orderinfo.optString(
						"blessingser", "")+"等"+orderdetail_orderinfo.optString(
								"co_blessings")+"人刚刚加持过";
		}
		finditem_blessingser.setText(bless);
	}

	protected void Ui_orderinfo_memberlist(
			JSONArray member_listitems) {
		// TODO Auto-generated method stub

		mListView.setAdapter(new FindItemListAdapter(this, member_listitems));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.order_back:
			// 要跳转的Activity
			Intent intent = new Intent(Find_item.this, Find.class);
			intent.putExtra("tid", tid);

			FindGroupTab.getInstance().switchActivity("Find_item", intent, -1,
					-1);
			break;
		case R.id.tongyuan:
			// 要跳转的Activity
			TabMenu mainactivity = (TabMenu) getParent().getParent(); // 查找父级的父级

			if (WishGroupTab.getInstance() == null) {
				
				mainactivity.setCurrentActivity(1);
			} else {
				Log.i("aaaa", "------view不为null-----");
				mainactivity.setCurrentActivity(1);
				Intent intent2 = new Intent(Find_item.this, ListTemple.class);
				intent2.putExtra("tid", tid);
				WishGroupTab.getInstance().switchActivity("ListTemple",
						intent2, -1, -1);
			}

			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FindGroupTab.getInstance().onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



}
