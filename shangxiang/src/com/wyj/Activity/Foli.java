package com.wyj.Activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wyj.calendar.ChinaDate;
import com.wyj.calendar.KCalendar;

import android.R.array;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyj.calendar.KCalendar.OnCalendarClickListener;
import com.wyj.calendar.KCalendar.OnCalendarDateChangedListener;
import com.wyj.http.WebApiUrl;
import com.wyj.pipe.Cms;
import com.wyj.pipe.SinhaPipeClient;
import com.wyj.pipe.SinhaPipeMethod;
import com.wyj.pipe.Utils;
import com.wyj.utils.StingUtil;
import com.wyj.Activity.R;

public class Foli extends MainActivity implements OnClickListener {

	String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式

	Button bt;
	KCalendar calendar;

	private TextView date_infos_left, date_infos_yangli, date_infos_yinli,
			date_infos_foli_or_birthday, add_user_birthday,
			date_infos_birthdayshow;
	private SinhaPipeClient httpClient;
	private SinhaPipeMethod httpMethod;
	private ProgressDialog pDialog = null;
	private JSONArray birthdaylist = new JSONArray();
	private LinearLayout date_add_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foli);
		findViewById();
		setAction();
		PopupWindows();
	}

	private void findViewById() {

		date_infos_left = (TextView) findViewById(R.id.date_infos_left);
		date_infos_yangli = (TextView) findViewById(R.id.date_infos_yangli);
		date_infos_yinli = (TextView) findViewById(R.id.date_infos_yinli);
		date_infos_foli_or_birthday = (TextView) findViewById(R.id.date_infos_foli_or_birthday);
		date_add_layout = (LinearLayout) findViewById(R.id.date_add_layout);

		date_infos_birthdayshow = (TextView) findViewById(R.id.date_infos_birthdayshow);
		add_user_birthday = (TextView) findViewById(R.id.add_user_birthday);

		add_user_birthday.setOnClickListener(this);
		
		date_infos_birthdayshow.setOnClickListener(this);
	}

	private void setAction() {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); // 获取当前年份
		int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码

		// Log.i("cccc", "------当前的日期----" + mYear + "---" + mMonth + "---" +
		// mDay);

		date_infos_left.setText(mDay + "");
		date_infos_yangli.setText(ChinaDate.get_yangli_today());
		date_infos_yinli.setText(ChinaDate.get_yinli(mYear, mMonth, mDay));
		if (!ChinaDate.oneDayiswhat(mYear, mMonth, mDay).equals("")) {
			date_infos_foli_or_birthday.setText(ChinaDate.oneDayiswhat(mYear,
					mMonth, mDay));
		} else {
			date_infos_foli_or_birthday.setVisibility(View.GONE);
		}
	}

	private void set_action_time() {
		// TODO Auto-generated method stub

		String[] dates = StingUtil.split(date, "-");
		int mYear = Integer.valueOf(dates[0]).intValue(); // 获取当前年份
		int mMonth = Integer.valueOf(dates[1]).intValue();// 获取当前月份
		int mDay = Integer.valueOf(dates[2]).intValue();// 获取当前月份的日期号码
		// Log.i("cccc", "------点击日历选中的日期----" + mMonth);
		int weekday = 0;
		try {
			weekday = StingUtil.dayForWeek(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		date_infos_left.setText(mDay + "");
		date_infos_yangli.setText(ChinaDate.get_yangli(mYear, mMonth, mDay,
				weekday + ""));
		date_infos_yinli.setText(ChinaDate.get_yinli(mYear, mMonth, mDay));
		if (!ChinaDate.oneDayiswhat(mYear, mMonth, mDay).equals("")) {
			date_infos_foli_or_birthday.setText(ChinaDate.oneDayiswhat(mYear,
					mMonth, mDay));
			date_infos_foli_or_birthday.setVisibility(View.VISIBLE);
		} else {
			date_infos_foli_or_birthday.setVisibility(View.GONE);
		}

		String birthd = "";
		String birth_id = "";
		if (birthdaylist.length() > 0) {
			for (int i = 0; i < birthdaylist.length(); i++) {

				try {
					JSONObject jsonobject2 = birthdaylist.getJSONObject(i);
					long retime = Integer.valueOf(
							jsonobject2.optString("reminddate", "")).intValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String date2 = sdf.format(new Date(retime * 1000));
					if (date2.equals(date)) { // 如果有当天的
						birthd = jsonobject2.optString("relativesname", "");
						birth_id = jsonobject2.optString("id", "");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (!birthd.equals("")) {
			set_birthday_time(birthd, birth_id);
		} else {
			date_infos_birthdayshow.setVisibility(View.GONE);
		}

		// if (date_add_layout.getChildCount() > 1) {
		//
		// TextView view = (TextView) date_add_layout.getChildAt(0);
		// }
	}

	private void set_birthday_time(String name, String id) { // 设置生日显示
		
		if(!id.equals("")){
			date_infos_birthdayshow.setText(name + "的生日");
			date_infos_birthdayshow.setVisibility(View.VISIBLE);
			date_infos_birthdayshow.setTag(id);
		}
		
		

		// TODO Auto-generated method stub

		// TextView view = new TextView(Foli.this);
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);
		// view.setText(name);
		//
		// Resources resource = (Resources) this.getResources();
		// ColorStateList csl = (ColorStateList) resource
		// .getColorStateList(R.color.text_normal);
		// view.setTextColor(csl);
		// Drawable drawable=
		// this.getResources().getDrawable(R.drawable.rili_17big);
		// /// 这一步必须要做,否则不会显示.
		// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
		// drawable.getMinimumHeight());
		// view.setCompoundDrawables(drawable, null, null, null);
		// view.setCompoundDrawablePadding(5);
		//
		// view.setTextSize(16);
		// params.setMargins(20, 0, 0, 0);
		// date_add_layout.addView(view,params);

	}

	public void PopupWindows() {

		LinearLayout ll_popup = (LinearLayout) findViewById(R.id.ll_popup);
		final TextView popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		calendar = (KCalendar) findViewById(R.id.popupwindow_calendar);
		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");

		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);
		}

		get_member_birthday();

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			@Override
			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {

					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
					set_action_time();
				}
			}

		});

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			@Override
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

		// 上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						calendar.lastMonth();
					}

				});

		// 下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						calendar.nextMonth();
					}
				});

	}

	private void get_member_birthday() {
		// TODO Auto-generated method stub

		this.httpClient = new SinhaPipeClient();
		if (Utils.CheckNetwork()) {

			if (!TextUtils.isEmpty(Cms.APP.getMemberId())) {
				showLoading();
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("mid", Cms.APP.getMemberId()));

				this.httpClient.Config("get", WebApiUrl.Getcalendarremindlist,
						params, true);
				this.httpMethod = new SinhaPipeMethod(this.httpClient,
						new SinhaPipeMethod.MethodCallback() {
							public void CallFinished(String error, Object result) {
								Log.i("bbbb", "-----请求回来----" + result);
								showLoading();
								if (null == error) {
									try {
										List<String> list = new ArrayList<String>(); // 设置标记列表
										JSONObject jsonobject = new JSONObject(
												(String) result);
										JSONArray jsonarr = jsonobject
												.getJSONArray("calendarlist");
										if (jsonobject.optString("code", "")
												.equals("succeed")) {

											for (int i = 0; i < jsonarr
													.length(); i++) {

												JSONObject jsonobject2 = jsonarr
														.getJSONObject(i);

												long retime = Integer.valueOf(
														jsonobject2.optString(
																"reminddate",
																"")).intValue();
												SimpleDateFormat sdf = new SimpleDateFormat(
														"yyyy-MM-dd");
												String date = sdf
														.format(new Date(
																retime * 1000));
												if (date.equals(ChinaDate
														.get_today_format())) { // 如果有当天的

													set_birthday_time(
															jsonobject2
																	.optString(
																			"relativesname",
																			""),
															jsonobject2
																	.optString(
																			"id",
																			""));
												}
												list.add(date);
											}
											birthdaylist = jsonarr;
											// list.add("2015-04-01");
											// list.add("2015-04-02");
											calendar.addMarks(list, 0);

										} else {
											Utils.ShowToast(Foli.this,
													jsonobject.optString("msg",
															""));
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {
									int err = R.string.dialog_system_error_content;
									if (error == httpClient.ERR_TIME_OUT) {
										err = R.string.dialog_network_error_timeout;
									}
									if (error == httpClient.ERR_GET_ERR) {
										err = R.string.dialog_network_error_getdata;
									}
									Utils.ShowToast(Foli.this, err);
								}
							}
						});
				this.httpMethod.start();

			} else {

				Utils.ShowToast(Foli.this, "未登录账户不能显示生日！");
			}

		} else {
			Utils.ShowToast(Foli.this, R.string.dialog_network_check_content);
		}
	}

	private void showLoading() {

		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		} else {
			Log.i("bbbb", "-----alert----");
			pDialog = new ProgressDialog(Foli.this.getParent().getParent());
			pDialog.setMessage("数据请求中。。。");
			pDialog.show();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add_user_birthday:
			if (TextUtils.isEmpty(Cms.APP.getMemberId())) {

				Utils.Dialog(getParent().getParent(), "提示", "请先登录！");
			} else {
				Intent intent = new Intent(Foli.this, AddBirthday.class);
				FoLiGroupTab.getInstance().switchActivity("AddBirthday",
						intent, -1, -1);
			}
			break;
		case R.id.date_infos_birthdayshow:
			String bid=(String) v.getTag();
			Intent inten=new Intent(Foli.this, Birthday_detail.class);
			inten.putExtra("bid", bid);
			FoLiGroupTab.getInstance().switchActivity("Birthday_detail",
					inten, -1, -1);
			break;

		}
	}

}
