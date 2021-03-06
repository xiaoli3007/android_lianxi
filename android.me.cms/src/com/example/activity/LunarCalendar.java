package com.example.activity;

import java.util.Calendar;

import com.example.cms.BaseFragment;
import com.example.cms.R;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;

public class LunarCalendar extends BaseFragment implements OnTouchListener {
	private TextView viewTitle;
	private Button buttonPrevMonth;
	private Button buttonNextMonth;
	private Button buttonCreateEvent;
	private Button buttonShowEvent;
	private RelativeLayout layoutCalendar;

	private static final int CAL_LAYOUT_ID = 55;
	private ViewFlipper viewFlipper;
	private Calendar calStartDate = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private GridView gridCurrent;
	private GridView gridFirst;
	private GridView gridLast;

	private int mMonthViewCurrentMonth = 0;
	private int mMonthViewCurrentYear = 0;
	private int firstDayOfWeek = Calendar.MONDAY;
	
	private GestureDetector mGesture = null;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sinha) {
		View view = inflater.inflate(R.layout.calendar, null);

		Calendar calendar = Calendar.getInstance();

		this.viewTitle = (TextView) view.findViewById(R.id.calendar_title_text);
		this.viewTitle.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
		this.buttonPrevMonth = (Button) view.findViewById(R.id.calendar_prev_button);
		this.buttonPrevMonth.setOnClickListener(this);
		this.buttonNextMonth = (Button) view.findViewById(R.id.calendar_next_button);
		this.buttonNextMonth.setOnClickListener(this);
		this.buttonCreateEvent = (Button) view.findViewById(R.id.calendar_create_event_button);
		this.buttonCreateEvent.setOnClickListener(this);
		this.buttonShowEvent = (Button) view.findViewById(R.id.calendar_show_event_button);
		this.buttonShowEvent.setOnClickListener(this);
		this.layoutCalendar = (RelativeLayout) view.findViewById(R.id.calendar_view);

		return view;
	}

	public void onActivityCreated(Bundle sinha) {
		super.onActivityCreated(sinha);
		updateStartDateForMonth();
		initCalendarView();
	}

	private void updateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1);
		mMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		mMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);

		String s = calStartDate.get(Calendar.YEAR) + "-" + (calStartDate.get(Calendar.MONTH) + 1);
		this.viewTitle.setText(s);
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	private void initCalendarView() {
		viewFlipper = new ViewFlipper(getActivity());
		viewFlipper.setId(CAL_LAYOUT_ID);
		calStartDate = getCalendarStartDate();
		CreateGirdView();
		RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		this.layoutCalendar.addView(viewFlipper, params_cal);

		LinearLayout br = new LinearLayout(getActivity());
		RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 1);
		params_br.addRule(RelativeLayout.BELOW, CAL_LAYOUT_ID);
		br.setBackgroundColor(getResources().getColor(R.color.background_normal));
		this.layoutCalendar.addView(br, params_br);
	}
	
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(firstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(firstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(firstDayOfWeek);
		}
		return calStartDate;
	}
	
	private void CreateGirdView() {
		Calendar firstCalendar = Calendar.getInstance();
		Calendar currentCalendar = Calendar.getInstance();
		Calendar lastCalendar = Calendar.getInstance();
		firstCalendar.setTime(calStartDate.getTime());
		currentCalendar.setTime(calStartDate.getTime());
		lastCalendar.setTime(calStartDate.getTime());

		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGesture.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
	
	}
}