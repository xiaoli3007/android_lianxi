package com.wyj.Activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.wyj.dataprocessing.BitmapManager;
import com.wyj.dataprocessing.MyApplication;
import com.wyj.pipe.Cms;
import com.wyj.pipe.Utils;
import com.wyj.utils.FilePath;
import com.wyj.utils.Tools;
import com.wyj.Activity.R;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class My extends Activity implements OnClickListener {
	RelativeLayout action_login;
	TextView user;
	ImageView my_avatar_face;
	/* 头像名称 */
	private static String IMAGE_FILE_NAME="";
	private RelativeLayout memberlogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my);
		findViewById();
		setListener();
		
		if(!TextUtils.isEmpty(Cms.APP.getMemberId())){
			IMAGE_FILE_NAME= Cms.APP.getMemberId() + "_faceImage.jpg";
			
			
			
		}else{
			IMAGE_FILE_NAME= "_faceImage.jpg";
		}
	   member_is_login();
		
	}

	private void findViewById() {

		my_avatar_face = (ImageView) findViewById(R.id.avatar);
		action_login = (RelativeLayout) findViewById(R.id.login_action);
		user = (TextView) findViewById(R.id.member_center_username);
		memberlogout =(RelativeLayout) findViewById(R.id.memberlogout);
	}

	private void setListener() {
		action_login.setOnClickListener(this);
		memberlogout.setOnClickListener(this);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i("aaaa", "------登录回来了"+IMAGE_FILE_NAME);
		member_login_action();
	}

	private void member_login_action() {
		// TODO Auto-generated method stub
		
		try {
			Cms.memberInfo = new JSONObject(Cms.APP.getConfig());
			member_is_login();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void member_is_login() {
		
		String username = (!TextUtils.isEmpty(Cms.memberInfo.optString("membername", "")))?Cms.memberInfo.optString("membername", ""):"";
		String avatar =  (!TextUtils.isEmpty(Cms.memberInfo.optString("headface", "")))?Cms.memberInfo.optString("headface", ""):"";
		Log.i("aaaa", "------登录回来了"+username);
		if (username != "") {
			
			memberlogout.setVisibility(View.VISIBLE);
			user.setText(username);
			if(avatar!=""){
				if (Tools.fileIsExists(FilePath.ROOT_DIRECTORY + IMAGE_FILE_NAME)) {
					Bitmap bitmap = BitmapFactory
							.decodeFile(FilePath.ROOT_DIRECTORY + IMAGE_FILE_NAME);
					my_avatar_face.setImageBitmap(bitmap);
				} else {
					BitmapManager.getInstance().loadBitmap(avatar, my_avatar_face,
							Tools.readBitmap(My.this, R.drawable.me));
				}
			}else{
				my_avatar_face.setBackgroundResource(R.drawable.me);
			}
			
		} else {
			user.setText("立即登录");
		}
	} 
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_action:
			if(TextUtils.isEmpty(Cms.APP.getMobile())){
				UserGroupTab.getInstance().startActivityForResult(
						new Intent(My.this, Login.class), 1);

			} else {
				Intent intent = new Intent(My.this, User.class);
				UserGroupTab.getInstance().switchActivity("User", intent, -1,
						-1);
			}
			break;
			
		case R.id.memberlogout:
			Cms.APP.Logout();
			Utils.ShowToast(My.this, "退出成功");
			memberlogout.setVisibility(View.GONE);
			user.setText("立即登录");
			my_avatar_face.setImageResource(R.drawable.me);
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UserGroupTab.getInstance().onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
