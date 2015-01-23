package com.wyj.popupwindow;


import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.wyj.Activity.R;



import com.wyj.select.AbstractWheel;
import com.wyj.select.AbstractWheelTextAdapter;
import com.wyj.select.OnWheelChangedListener;
import com.wyj.select.OnWheelScrollListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MyPopupWindowsDate extends PopupWindow {
	
	private PopupWindow popupwindow;
	private Button orderform_date_confirm;
	private boolean scrolling = false;

	public MyPopupWindowsDate( Context mContext, final View parent,  Activity activity) {
		
		final Activity pactivity=activity;
		final Context mContexts=mContext;
		
		View customView = View.inflate(mContext, R.layout.order_form_popwindow_date,
				null);
		orderform_date_confirm =(Button) customView.findViewById(R.id.orderform_date_confirm);
		

		//插件滚动开始-------------------------------------------------------------------------------------------------
		final Calendar calendar = Calendar.getInstance();

		final AbstractWheel dateYearSelect = (AbstractWheel) customView.findViewById(R.id.orderform_date_year_view);
		final AbstractWheel dateMonthSelect = (AbstractWheel) customView.findViewById(R.id.orderform_date_month_view);
		final AbstractWheel dateDaySelect = (AbstractWheel) customView.findViewById(R.id.orderform_date_day_view);
		
		dateYearSelect.setCyclic(true);
		dateMonthSelect.setCyclic(true);
		dateDaySelect.setCyclic(true);
		DateYearAdapter dateYearAdapter = new DateYearAdapter(mContexts);
		dateYearSelect.setViewAdapter(dateYearAdapter);
		dateYearSelect.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				calendar.set(calendar.get(Calendar.YEAR) + dateYearSelect.getCurrentItem(), dateMonthSelect.getCurrentItem(), 1);
				DateDayAdapter dateDayAdapter = new DateDayAdapter(mContexts);
				dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				dateDaySelect.setViewAdapter(dateDayAdapter);
				dateDaySelect.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
			}
		});
		dateYearSelect.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					calendar.set(calendar.get(Calendar.YEAR) + dateYearSelect.getCurrentItem(), dateMonthSelect.getCurrentItem(), 1);
					DateDayAdapter dateDayAdapter = new DateDayAdapter(mContexts);
					dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					dateDaySelect.setViewAdapter(dateDayAdapter);
					dateDaySelect.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
				}
			}
		});

		DateMonthAdapter dateMonthAdapter = new DateMonthAdapter(mContexts);
		dateMonthSelect.setViewAdapter(dateMonthAdapter);
		dateMonthSelect.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				calendar.set(calendar.get(Calendar.YEAR) + dateYearSelect.getCurrentItem(), dateMonthSelect.getCurrentItem(), 1);
				DateDayAdapter dateDayAdapter = new DateDayAdapter(mContexts);
				dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				dateDaySelect.setViewAdapter(dateDayAdapter);
				dateDaySelect.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
			}
		});
		dateMonthSelect.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					calendar.set(calendar.get(Calendar.YEAR) + dateYearSelect.getCurrentItem(), dateMonthSelect.getCurrentItem(), 1);
					DateDayAdapter dateDayAdapter = new DateDayAdapter(mContexts);
					dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					dateDaySelect.setViewAdapter(dateDayAdapter);
					dateDaySelect.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
				}
			}
		});
		
		DateDayAdapter dateDayAdapter = new DateDayAdapter(mContexts);
		dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dateDaySelect.setViewAdapter(dateDayAdapter);
		
		dateMonthSelect.setCurrentItem(calendar.get(Calendar.MONTH));
		dateDaySelect.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		//插件滚动结束-------------------------------------------------------------------------------------------------

		popupwindow = new PopupWindow(customView);
		
		 //以下为弹窗后面的背景色设置
	 	ColorDrawable cd = new ColorDrawable(0x000000);
	 	popupwindow.setBackgroundDrawable(cd); 
	   	//产生背景变暗效果
	    WindowManager.LayoutParams lp=activity.getWindow().getAttributes(); 
		lp.alpha = 0.7f;
		activity.getWindow().setAttributes(lp);
		
		popupwindow.setWidth(LayoutParams.FILL_PARENT);
		popupwindow.setHeight(LayoutParams.FILL_PARENT);
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		popupwindow.setFocusable(true);
		popupwindow.setOutsideTouchable(true);
		popupwindow.setContentView(customView);

		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		
		// popupwindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0],
		// location[1]-popupwindow.getHeight());//显示在button的上面
		 popupwindow.showAsDropDown(parent,0,-400); //显示在button的下面

		// 自定义view添加触摸事件
		popupwindow.update();
		popupwindow.setOnDismissListener(new OnDismissListener() {		//恢复背景色
			
			public void onDismiss() {
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp=pactivity.getWindow().getAttributes();
    			lp.alpha = 1f;
    			pactivity.getWindow().setAttributes(lp);
			}
		});
		
		orderform_date_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				;
				
			//	String aaa=data[locationProvSelect.getCurrentItem()];
				
//				TextView list_find_zany=(TextView) parent;
//			//	list_find_zany.setText(aaa);
				popupwindow.dismiss();
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
	
	
	
	private class DateYearAdapter extends AbstractWheelTextAdapter {
		Calendar calendar = Calendar.getInstance();

		protected DateYearAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return 10;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView yearView = (TextView) view.findViewById(R.id.select_custom_text);
			yearView.setText((this.calendar.get(Calendar.YEAR) + index) + "年");
			return view;
		}
	}

	private class DateMonthAdapter extends AbstractWheelTextAdapter {
		protected DateMonthAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return 12;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView monthView = (TextView) view.findViewById(R.id.select_custom_text);
			monthView.setText((index + 1) + "月");
			return view;
		}
	}

	private class DateDayAdapter extends AbstractWheelTextAdapter {
		int dayCount = 30;
		
		protected DateDayAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return dayCount;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView monthView = (TextView) view.findViewById(R.id.select_custom_text);
			monthView.setText((index + 1) + "日");
			return view;
		}
	}


	
	

}

