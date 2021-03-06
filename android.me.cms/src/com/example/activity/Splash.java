package com.example.activity;

import com.example.services.AutoLoginService;
import com.example.cms.R;
import com.example.cms.ShangXiang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

public class Splash extends Activity {

	@Override
	public void onCreate(Bundle sinha) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(sinha);
		setContentView(R.layout.splash);

		TextView viewVersion = (TextView) findViewById(R.id.version_number);
		viewVersion.setText("Version " + ShangXiang.VERSION);

		if (!TextUtils.isEmpty(ShangXiang.APP.getMobile()) && !TextUtils.isEmpty(ShangXiang.APP.getPassword())) {
			Intent intent = new Intent(ShangXiang.APP, AutoLoginService.class);
			startService(intent);
		} else {
			ShangXiang.APP.Logout();
		}
		goHome();
	}

	private void goHome() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(Splash.this, Navigation.class);
				Splash.this.startActivity(intent);
				Splash.this.finish();
			}
		}, 1000);
	}
}