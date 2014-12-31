package com.wyj.framework;

import java.util.ArrayList;
import java.util.List;

import com.wyj.Activity.TabMenu;
import com.wyj.tabmenu.R;


import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;

public class BaseGroup extends ActivityGroup {

	private static final String TAG = "BaseGroup";
	protected TabStack stack = new TabStack();
	protected ViewFlipper containerFlipper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	public void switchActivity(String id, Intent intent, int inAnimation,
			int outAnimation) {
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window window = getLocalActivityManager().startActivity(id, intent);
		View v = window.getDecorView();
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		v.setLayoutParams(param);
		if (inAnimation != -1) {
			try {
				containerFlipper.setInAnimation(AnimationUtils.loadAnimation(
						this, inAnimation));
				containerFlipper.setOutAnimation(AnimationUtils.loadAnimation(
						this, outAnimation));
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		} else {
			containerFlipper.setInAnimation(null);
			containerFlipper.setOutAnimation(null);
		}
		// printViewFlipper(); 
		 Log.i("bbbb","-idactivity---"+id);
		
		containerFlipper.addView(v);
		containerFlipper.showNext(); 
		if (inAnimation == R.anim.in_left_right) {
			containerFlipper.removeViewAt(stack.size());
		}
		stack.push(id); 
	} 

	public void back() {
		
		 
		if (stack.size() > 1) {
			//stack.
			if(stack.top().equals("Find") || stack.top().equals("My"))
			{
				Log.i("bbbb","find退出----------"); 
				//((TabMenu)getParent()).exitApp();
			}else
			{
				Log.i("bbbb","删除页面----------"+stack.size()); 
				containerFlipper.showPrevious();
				containerFlipper.removeViewAt(stack.size() - 1);
//				View view_old=containerFlipper.getChildAt(stack.size() - 1);
//				containerFlipper.removeView(view_old);
				stack.pop();
			}
		} else {
			
			Log.i("bbbb","<1----------"); 
			((TabMenu)getParent()).exitApp();
		}
	}

	public void noAnimationback() {
		if (stack.size() > 1) {
			containerFlipper.showPrevious();
			containerFlipper.removeViewAt(stack.size() - 1);
			stack.pop();
		} else {
			((TabMenu)getParent()).exitApp();
		}
	}
	
	public Activity getActivityByTag(String tag){
		return getLocalActivityManager().getActivity(tag);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!stack.isEmpty()) {
				back();
			} else {
				((TabMenu)getParent()).onKeyDown(keyCode, event);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void popSome(String id) {
		int sum = stack.getTheSumToPop(id);
		containerFlipper.removeViews(stack.size() - sum, sum - 1);
		stack.popSome(id);
		containerFlipper
				.setDisplayedChild(containerFlipper.getChildCount() - 1);
	}
	
}
