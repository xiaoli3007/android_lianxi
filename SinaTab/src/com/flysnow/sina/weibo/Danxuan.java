/**
 * 
 */
package com.flysnow.sina.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 单选 多选 Activity
 * @author 
 * @since 2011-3-8
 */
public class Danxuan extends Activity {

		private static final String[] countriesStr ={ " 北京市", " 上海市", " 天津市", " 重庆市" };
		private TextView myTextView;
		private Spinner mySpinner;
		private ArrayAdapter<String> adapter;
		Animation myAnimation;
		@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.danxuan);
		
		/* 以findViewById() 取得对象*/
		myTextView = (TextView) findViewById(R.id.TextView_Show);
		mySpinner = (Spinner) findViewById(R.id.spinner_City);
		
		/* 取得Animation 定义在res/anim 目录下*/
		myAnimation = AnimationUtils.loadAnimation(this, R.anim.my_anim);
		adapter=new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, countriesStr );
		
		/* myspinner_dropdown 为自定义下拉菜单样式定义在res/layout 目录下*/
		adapter.setDropDownViewResource(R.layout.xiala_content);
		
		
		/* 将ArrayAdapter 添加Spinner 对象中*/
		mySpinner.setAdapter(adapter);
		
		/*下拉菜单弹出的内容选项被选中事件处理*/
		mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,
			int arg2, long arg3) {
			// TODO Auto-generated method stub
			/* 将所选mySpinner 的值带入myTextView 中*/
			myTextView.setText("您选择的是："+ countriesStr [arg2]);
			/* 将mySpinner 显示*/
			arg0.setVisibility(View.VISIBLE);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			myTextView.setText("NONE");
			}
		});
		
		
		/*下拉菜单弹出的内容选项触屏事件处理*/
		mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			/* 将mySpinner 运行Animation */
			v.startAnimation(myAnimation);
			/* 将mySpinner 隐藏*/
			v.setVisibility(View.INVISIBLE);
			return false;
			}
		});
		
		
		/*下拉菜单弹出的内容选项焦点改变事件处理*/
		mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
			public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			}
		});
		
		
		}
		
		
		
		
		
		
		
	}