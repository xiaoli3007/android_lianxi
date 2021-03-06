/**
 * 
 */
package com.flysnow.sina.weibo;


import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 更多Activity
 * @author 
 * @since 2011-3-8
 */
public class MoreActivity extends Activity {
	
	private Intent intent;
	private Bundle budle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout2);    
		
		TextView a=(TextView) findViewById(R.id.textView111);
		
		intent= this.getIntent();  //接受的数据
		budle =intent.getExtras();
		
		String sex =budle.getString("sex");
		double height = budle.getDouble("height");
		String tizhong = this.getWeight(sex, height);
		
		/*使用CharSequence类getString()方法从XML中获取String*/
		CharSequence string2=getString(R.string.app_name);
		
		
		String z = "("+string2+")你是一个"+sex+",你的身高为"+height+"你的标准体重为"+tizhong;
		a.setText(z);
		
		Button back= (Button) findViewById(R.id.back);		//返回事件
		back.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//返回result 回上一个activity 
				MoreActivity.this.setResult(RESULT_OK, intent);
				//结束这个activity 
				MoreActivity.this.finish();
			}
		});
		
	}
	
	/* 四舍五入的method */
	private String format(double num) {
		NumberFormat formatter = new DecimalFormat("0.00");
		String s = formatter.format(num);
		return s;
	}
	
	private String getWeight(String sex, double height) {
		String weight = "";
		if (sex.equals("男性")) {
		weight = format((height - 80) * 0.7);
		} else {
		weight = format((height - 70) * 0.6);
		} 
		
		return weight;
	}
	
}
