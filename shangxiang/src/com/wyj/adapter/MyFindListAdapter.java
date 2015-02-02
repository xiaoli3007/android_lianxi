package com.wyj.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.wyj.Activity.FindGroupTab;
import com.wyj.Activity.Find_item;
import com.wyj.Activity.WishGroupTab;

import com.wyj.Activity.R;
import com.wyj.Activity.TabMenu;

import com.wyj.dataprocessing.BitmapManager;

import com.wyj.utils.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MyFindListAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	public JSONArray data = new JSONArray();
	private List<Map<String, Object>> mData;
	private int tid;
	private ArrayList<Object> btnMap = new ArrayList<Object>();
	private Activity tabparent;

	public static class ListItem {
		ImageView list_find_headface;
		TextView list_find_content;
		TextView list_find_username;
		TextView list_find_address;
		TextView list_find_jiachi;
		TextView list_find_zan;
	}

	public MyFindListAdapter(Context context, List<Map<String, Object>> list,
			int tid, Activity activity) {
		this.context = context;
		this.mData = list;
		this.tid = tid;
		this.tabparent = activity;
	}

	@Override
	public int getCount() {
		return this.mData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ListItem listItem;

		if (convertView == null) {
			listItem = new ListItem();
			convertView = LayoutInflater.from(this.context).inflate(
					R.layout.list_find_items, null);
			listItem.list_find_headface = (ImageView) convertView
					.findViewById(R.id.list_find_headface);
			listItem.list_find_content = (TextView) convertView
					.findViewById(R.id.list_find_content);
			listItem.list_find_username = (TextView) convertView
					.findViewById(R.id.list_find_username);
			listItem.list_find_address = (TextView) convertView
					.findViewById(R.id.list_find_address);
			listItem.list_find_jiachi = (TextView) convertView
					.findViewById(R.id.list_find_jiachi);

			listItem.list_find_zan = (TextView) convertView
					.findViewById(R.id.list_find_zan);

			btnMap.add(listItem.list_find_zan);

			convertView.setTag(listItem);

		} else {
			listItem = (ListItem) convertView.getTag();
		}

		// viewHolder.img.setBackgroundResource(R.drawable.foot_07);
		BitmapManager.getInstance().loadBitmap(
				(String) this.mData.get(position).get("headface"),
				listItem.list_find_headface,
				Tools.readBitmap(this.context, R.drawable.foot_07));

		listItem.list_find_content.setText((CharSequence) this.mData.get(
				position).get("wishtext"));
		listItem.list_find_username.setText((CharSequence) this.mData.get(
				position).get("wishname"));

		String findaddress = "刚刚在"
				+ (String) this.mData.get(position).get("templename")
				+ (String) this.mData.get(position).get("alsowish")
				+ (String) this.mData.get(position).get("wishtype");

		int co_blessings = Integer.valueOf(
				(String) this.mData.get(position).get("co_blessings"))
				.intValue();
		String jiachipeople = "";
		if (co_blessings > 0) {
			jiachipeople = (String) this.mData.get(position).get(
					"name_blessings")
					+ "等" + co_blessings + "人加持";
		}
		listItem.list_find_address.setText(findaddress);
		listItem.list_find_jiachi.setText(jiachipeople);

		listItem.list_find_content.setTag(this.mData.get(position).get(
				"orderid"));
		listItem.list_find_content.setOnClickListener(this);
		listItem.list_find_zan.setTag(position);
		listItem.list_find_zan.setOnClickListener(this);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int Id = v.getId();
		if (R.id.list_find_content == Id) {

			TabMenu mainactivity = (TabMenu) tabparent; // 查找父级的父级

			if (FindGroupTab.getInstance() == null) {

				mainactivity.setCurrentActivity(2);
			} else {

				mainactivity.setCurrentActivity(2);
				Integer orderid = Integer.valueOf((String) v.getTag())
						.intValue();
				// 要跳转的Activity
				Intent intent = new Intent(this.context, Find_item.class);

				Bundle bu = new Bundle(); // 这个组件 存值
				bu.putInt("orderid", orderid);
				bu.putInt("tid", tid);
				intent.putExtras(bu); // 放到 intent 里面 然后 传出去
				// 把Activity转换成一个Window，然后转换成View

				FindGroupTab.getInstance().switchActivity("Find_item", intent,
						-1, -1);
			}

		}
		Log.i("bbbb", "-----位置1111----" + Id);
		if (R.id.list_find_zan == Id) {
			int position = (Integer) v.getTag();
			Log.i("bbbb", "-----位置222----" + mData.get(position).get("orderid"));
			// 遍历并更改按钮状态
			TextView list_find_zany = (TextView) v;
			Resources resource = (Resources) this.context.getResources();
			ColorStateList csl = (ColorStateList) resource
					.getColorStateList(R.color.color_text_selected);
			list_find_zany.setTextColor(csl);
			Drawable drawable = this.context.getResources().getDrawable(
					R.drawable.load_hover);
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			list_find_zany.setCompoundDrawables(drawable, null, null, null);
		}

	}

}