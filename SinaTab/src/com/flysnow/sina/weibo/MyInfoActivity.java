/**
 * 
 */
package com.flysnow.sina.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 我的资料Activity
 * @author 
 * @since 2011-3-8
 */
public class MyInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_activity);
		
		Button danxuan =(Button) findViewById(R.id.danxuan);
		Button gallery =(Button) findViewById(R.id.Gallery);
		Button fliesave=(Button) findViewById(R.id.Filesave);
		Button sqls= (Button) findViewById(R.id.sqls);
		Button adapter= (Button) findViewById(R.id.adapter);
		danxuan.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				danxuan();
			}
		});
		
		gallery.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gallery();
			}
		});
		fliesave.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list_Action(1);
			}
		});
	  
		sqls.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				list_Action(2);
			}
		});
		
		adapter.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				list_Action(3);
			}
		});
				
	}
	
	public void danxuan(){
		
		Intent intent=new Intent();
		intent.setClass(MyInfoActivity.this, Danxuan.class);	
		startActivity(intent);
	}
	public void gallery(){
		
		Intent intent=new Intent();
		intent.setClass(MyInfoActivity.this, Gallery_image.class);	
		startActivity(intent);
	}
	
	public void list_Action(int types){
		
		switch (types) {
		
			case 1:
				Intent intent=new Intent();
				intent.setClass(MyInfoActivity.this, Filesave.class);	
				startActivity(intent);
				break;
			case 2:
				Intent intent2=new Intent();
				intent2.setClass(MyInfoActivity.this, Sqls.class);	
				startActivity(intent2);
				break;
			case 3:
				Intent intent3=new Intent();
				intent3.setClass(MyInfoActivity.this, Textlist.class);	
				startActivity(intent3);
				break;
			default:
				break;
		}
		
		
	}
	
	
}
