package com.example.panduola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private List<Map<String, Object>> listdata;
	private ListView listView;
	//private Index_Adapter myadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listdata = get_listdata();
		listView=(ListView) findViewById(R.id.xianlu);
		listView.setAdapter(new Index_Adapter(this));
	//    listView.setAdapter(new Index_Adapter(this)); //
	     TextView nanxing=(TextView)findViewById(R.id.nanxingwanju);
	     nanxing.setOnClickListener(this);
	}
	
	public void onClick (View v){
		
		switch (v.getId()) {
			case R.id.nanxingwanju:
				Intent internt1=new Intent();
				internt1.setClass(MainActivity.this, Textlist.class);
				startActivity(internt1);
				break;
	
			default:
				break;
		} 
		
	}
	
	
	private List<Map<String, Object>> get_listdata() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map=new HashMap<String, Object>();
		
		map.put("title", "日本进口2H2D延时喷剂 男性助勃");
		map.put("img", R.drawable.eee);
		map.put("jiage", "198.0");
		map.put("old_jiage", "237");
		map.put("xiaoliang", "1025");
		list.add(map);
		
		map=new HashMap<String, Object>();
		map.put("title", "百乐大力神杯 7频震动 男用阴交自慰器");
		map.put("img", R.drawable.ccc);
		map.put("jiage", "78.0");
		map.put("old_jiage", "165");
		map.put("xiaoliang", "100");
		list.add(map);
		
		map=new HashMap<String, Object>();
		map.put("title", "早泄克星 黑寡妇煞星 达克罗宁软膏");
		map.put("img", R.drawable.eee);
		map.put("jiage", "208.0");
		map.put("old_jiage", "447");
		map.put("xiaoliang", "145");
		list.add(map);
		
		return list;
	}
	
	

	
	 

	 public class Index_Adapter extends BaseAdapter {
		  
		 	
		    private Context mContext;
	        private LayoutInflater inflater=null;
	        
	        public Index_Adapter(Context mContext) {
	        	 this.mContext = mContext;
	        	 inflater = LayoutInflater.from(mContext);
	        	  
	       }

			@Override
	        public int getCount() {
	            return listdata.size();
	        }
	  
	      
			@Override
	        public Object getItem(int position) {
	            return listdata.get(position);
	        }
	     

	        @Override
	        public long getItemId(int position) {
	            return position;
	        }
	  
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	        	 ViewHolder viewHolder = null;
	           
	        	 if(convertView == null) {
	            	viewHolder = new ViewHolder();
	              	
	             convertView =inflater.inflate( R.layout.items, null);
	             
	              System.out.println("convertView = " + convertView);
	              System.out.println("viewHolder = " +  (TextView) findViewById(R.id.title)); 
	              viewHolder.img=(ImageView) convertView.findViewById(R.id.img);
	              viewHolder.title= (TextView)convertView.findViewById(R.id.title);
	              viewHolder.jiage= (TextView)convertView.findViewById(R.id.jiage);
	              viewHolder.old_jiage= (TextView)convertView.findViewById(R.id.old_jiage);
	              viewHolder.xiaoliang= (TextView)convertView.findViewById(R.id.xiaoliang);
	              viewHolder.sale_btn =(Button)convertView.findViewById(R.id.sale_btn);
		          convertView.setTag(viewHolder);
		             
	            } else {
	            	 Log.i("aaaa","11111111111111111111111111111111111");
	            	 viewHolder=(ViewHolder) convertView.getTag();
	            }
	            
	           // Log.i("aaaa",listdata.get(position).toString()+"13131313");
	            viewHolder.img.setBackgroundResource((Integer)listdata.get(position).get("img"));
	            viewHolder.title.setText((String)listdata.get(position).get("title"));
	            viewHolder.jiage.setText((String)listdata.get(position).get("jiage"));
	            viewHolder.old_jiage.setText((String)listdata.get(position).get("old_jiage"));
	            viewHolder.xiaoliang.setText((String)listdata.get(position).get("xiaoliang"));
	            viewHolder.sale_btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
	            
	            return convertView;
	        } 
	        
	        class ViewHolder {
				
				public ImageView img;
				public TextView title;
				public TextView jiage;
				public TextView old_jiage;
				public TextView xiaoliang;
				public Button sale_btn;
			}
	  
	    }
	 

	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			//Log.i("TAG","----"+keyCode);
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				
				//webView.goBack();
				exitApp();
				return false;
			default:
				break;
			}
			
			return super.onKeyDown(keyCode, event);
		}
		
		public void exitApp(){
			new AlertDialog.Builder(this).setTitle("确定要退出么？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					System.exit(0);
				}
			}).setNegativeButton("不确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).create().show();
		}
	
	

}
