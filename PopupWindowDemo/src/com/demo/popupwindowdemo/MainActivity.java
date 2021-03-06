package com.demo.popupwindowdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private PopupWindow popupwindow;
	private Button button;

	private ListTempleNameAdapter adapterListTemple;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);

	}

	private void loadDiscover() {

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button1:
			if (popupwindow != null && popupwindow.isShowing()) {
				popupwindow.dismiss();
				return;
			} else {
				initmPopupWindowView();
				popupwindow.showAsDropDown(v, 0, 5);
			}
			break;
		default:
			break;
		}

	}

	public void initmPopupWindowView() {

		// // 获取自定义布局文件pop.xml的视图
		View customView = getLayoutInflater().inflate(R.layout.popview_item,
				null, false);

		ListView List = (ListView) customView
				.findViewById(R.id.discover_container);
		JSONArray jas;
		try {
			jas = new JSONArray(
					"[{\"hall\":\"五台山（山西）\"},{\"hall\":\"五台山（山西）\"},{\"hall\":\"张三的\"},{\"hall\":\"李四的\"},{\"hall\":\"王武的\"}]");
			this.adapterListTemple = new ListTempleNameAdapter(
					MainActivity.this, jas);
			List.setAdapter(this.adapterListTemple);
			this.adapterListTemple.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				Log.i("aaaa", "-----弹窗点击1111111111111111111-");
			
				 popupwindow.dismiss();
			}
		});

		// 创建PopupWindow实例,200,150分别是宽度和高度-- popupwindow = new
		// PopupWindow(customView,200,150);
		popupwindow = new PopupWindow(customView);
		// 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
	//	 popupwindow.setAnimationStyle(R.style.AnimationFade);	 //从上往下弹出效果
//		customView.startAnimation(AnimationUtils.loadAnimation(
//				MainActivity.this, R.anim.fade_in));
		
		 //以下为弹窗后面的背景色设置
	 	ColorDrawable cd = new ColorDrawable(0x000000);
	 	popupwindow.setBackgroundDrawable(cd); 
	   	//产生背景变暗效果
	    WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);
		
		popupwindow.setWidth(LayoutParams.FILL_PARENT);
		popupwindow.setHeight(LayoutParams.FILL_PARENT);
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		popupwindow.setFocusable(true);
		popupwindow.setOutsideTouchable(true);
		popupwindow.setContentView(customView);
		
		int[] location = new int[2];
		button.getLocationOnScreen(location);
		// popupwindow.showAtLocation(button, Gravity.NO_GRAVITY, location[0],
		// location[1]-popupwindow.getHeight());//显示在button的上面
		 popupwindow.showAsDropDown(button); //显示在button的下面
//		popupwindow.showAtLocation(button, Gravity.NO_GRAVITY, location[0]
//				- popupwindow.getWidth(), location[1]); // 左边
//		popupwindow.showAtLocation(button, Gravity.NO_GRAVITY, location[0]
//				+ button.getWidth(), location[1]); // 右边
		// 自定义view添加触摸事件
		popupwindow.update();
		popupwindow.setOnDismissListener(new OnDismissListener() {		//恢复背景色
			
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp=getWindow().getAttributes();
    			lp.alpha = 1f;
    			getWindow().setAttributes(lp);
			}
		});
		
		
		customView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}

				return false;
			}
		});
	}

	public class ListTempleNameAdapter extends BaseAdapter {
		private Context context;
		public JSONArray data = new JSONArray();

		public class ListItem {

			public Button nameHall;

		}

		public ListTempleNameAdapter(Context context, JSONArray data) {
			this.context = context;
			this.data = data;
		}

		@Override
		public int getCount() {
			return this.data.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ListItem listItem;

			final JSONObject item = this.data.optJSONObject(position);

			if (null == convertView) {
				convertView = LayoutInflater.from(this.context).inflate(
						R.layout.temlpate_item, null);
				listItem = new ListItem();
				listItem.nameHall = (Button) convertView
						.findViewById(R.id.select_template_button);
				convertView.setTag(listItem);
			} else {
				listItem = (ListItem) convertView.getTag();
			}
		
			listItem.nameHall.setText(item.optString("hall", ""));
//			listItem.nameHall.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					button.setText(item.optString("hall", ""));
//					popupwindow.dismiss();
//				}
//			});
			return convertView;
		}

	}

}
