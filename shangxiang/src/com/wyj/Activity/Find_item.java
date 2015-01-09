package com.wyj.Activity;


import java.util.List;
import java.util.Map;

import com.wyj.Activity.TabMenu;
import com.wyj.dataprocessing.AsynTaskHelper;
import com.wyj.dataprocessing.BitmapManager;
import com.wyj.dataprocessing.JsonToListHelper;
import com.wyj.dataprocessing.AsynTaskHelper.OnDataDownloadListener;
import com.wyj.define.templates;
import com.wyj.http.WebApiUrl;
import com.wyj.Activity.R;
import com.wyj.utils.Tools;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Find_item extends Activity implements OnClickListener
{
	
	private ListView mListView;
	private List<Map<String, Object>> ListMemberdata; 
	private int tid;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_item);
		
		ImageView back=(ImageView) findViewById(R.id.order_back);
		back.setOnClickListener(this);
		Button tongyuanqifu=(Button) findViewById(R.id.tongyuan);
		tongyuanqifu.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.find_list_item);
		
		 Intent intent= this.getIntent();  //接受的数据
		 Bundle budle =intent.getExtras();
		 int orderid=budle.getInt("orderid");
		 tid=budle.getInt("tid"); //寺庙的传值

		 api_show_detail(null,WebApiUrl.GET_ORDER_DETAIL+"?oid="+orderid,getParent());
	}
	
	private void api_show_detail(Map<String, Object> map,String order_detail_api_url,final Context context) {
		// TODO Auto-generated method stub
		AsynTaskHelper  asyntask = new AsynTaskHelper();
		asyntask.dataDownload(order_detail_api_url, map, new OnDataDownloadListener() {
			@Override
			public void onDataDownload(String result) {
				if (result != null) {
					
				List<Map<String, Object>> member_listitems;
				Map<String, Object> orderdetail_orderinfo;
				orderdetail_orderinfo = JsonToListHelper.orderdetail_orderinfo(result);		
				member_listitems =  JsonToListHelper.orderdetail_memberlist_json(result);	
				
				Ui_orderinfo(orderdetail_orderinfo);
				Ui_orderinfo_memberlist(member_listitems);
			
				}else {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}
				 
			}
		 }, context,"GET");
	}
	
	protected void Ui_orderinfo(Map<String, Object> orderdetail_orderinfo) {
		// TODO Auto-generated method stub
		TextView order_username=(TextView) findViewById(R.id.order_username);
		TextView templename=(TextView) findViewById(R.id.order_templename);
		TextView order_wishtext=(TextView) findViewById(R.id.order_wishtext);
		TextView order_blessingser=(TextView) findViewById(R.id.order_blessingser);
		
		order_username.setText( (String) orderdetail_orderinfo.get("wishname"));
		templename.setText( (String) orderdetail_orderinfo.get("templename"));
		order_wishtext.setText( (String) orderdetail_orderinfo.get("wishtext"));
		order_blessingser.setText( (String) orderdetail_orderinfo.get("blessingser"));
	}
	
	protected void Ui_orderinfo_memberlist(
			List<Map<String, Object>> member_listitems) {
		// TODO Auto-generated method stub
		
		mListView.setAdapter(new FindListAdapter(this,member_listitems));
	}

	

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.order_back:
	    	 //要跳转的Activity  
	        Intent intent = new Intent(Find_item.this, Find.class);
            intent.putExtra("tid",tid);

	        FindGroupTab.getInstance().switchActivity("Find_item",intent,-1,-1);
			break;		
		case R.id.tongyuan:
	    	 //要跳转的Activity  
	        TabMenu mainactivity=(TabMenu)getParent().getParent(); //查找父级的父级
	        
	        if(WishGroupTab.getInstance()==null)
			{
	        	Log.i("aaaa","------view为null-----");
	        	mainactivity.setCurrentActivity(1);
			}else{
				Log.i("aaaa","------view不为null-----");
			    mainactivity.setCurrentActivity(1);
			    Intent intent2 = new Intent(Find_item.this, ListTemple.class);
		        intent2.putExtra("tid",tid);
		        WishGroupTab.getInstance().switchActivity("ListTemple",intent2,-1,-1);
			}
	       
			break;	
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			FindGroupTab.getInstance().onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
    @Override  
    public void onBackPressed() {     
    	
    
    }  
    
    
    private class FindListAdapter extends BaseAdapter  {

        private Context mContext;
        private LayoutInflater inflater;
		private List<Map<String, Object>> mData;
        
          
        public FindListAdapter(Context mContext,List<Map<String, Object>> list) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);
            this.mData = list;
        }

		@Override
        public int getCount() {
            return this.mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.find_list_items, null);
                
                viewHolder.header = (ImageView) convertView.findViewById(R.id.order_member_list_head);
                viewHolder.username = (TextView) convertView.findViewById(R.id.order_member_list_username);
                viewHolder.time_diff = (TextView) convertView.findViewById(R.id.order_member_list_jiachi);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag(); 
            }
            
           
            //viewHolder.header.setBackgroundResource(R.drawable.foot_07);
            BitmapManager.getInstance().loadBitmap((String) this.mData.get(position).get("headface"), viewHolder.header, Tools.readBitmap(Find_item.this, R.drawable.foot_07));
            viewHolder.username.setText((CharSequence) this.mData.get(position).get("nickname"));
            viewHolder.time_diff.setText((CharSequence) this.mData.get(position).get("time_diff"));

            return convertView;
        }
        
        class ViewHolder {
        	 ImageView header;
			 TextView username;
			 TextView time_diff;
	
        } 	
   
    }
	

	             
	
	
}
