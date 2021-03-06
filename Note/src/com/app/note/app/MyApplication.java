package com.app.note.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	private static MyApplication instance;
	private List<Activity> activityList = new LinkedList<Activity>();

	public static MyApplication getInstances() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;

	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

	public static MyApplication getApp() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	public String getSystemDataATime() {
		// 24小时制
		SimpleDateFormat dateFormat24 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormat24.format(Calendar.getInstance().getTime());
	}
}
