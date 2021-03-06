package com.example.panduola;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.file.image;
import com.example.json.List_json;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * 
 * @author shangzhenxiang
 *
 */
public class Textlist extends Activity implements OnClickListener  , OnScrollListener {

    private static final String LISTGOOD = null;
	private ListView mListView;
    private View moreView;
    private List<HashMap<String, Object>> Listdata;
    private BaseListAdapter mAdapter;
    
    private String result;
    private int lastItem;
    private int count=0;
    private int page=1;
    private boolean  sj=true;
	private Handler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseadapterlist);
        mListView = (ListView) findViewById(R.id.xianlu2);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        mListView.addFooterView(moreView);
        
        Listdata =  new ArrayList<HashMap<String,Object>>();
        prepareData();
        ImageView fanhui=(ImageView)findViewById(R.id.listimg1);
        fanhui.setOnClickListener(this);
    }
    
    private void prepareData(){
    	
    	handler=new Handler() {  
            @Override  
            public void handleMessage(Message msg) { 
            	if (Listdata != null) {
            	//getData();
            	adadper();
            	}
                super.handleMessage(msg);  
            }  
        };  
        new Thread(new Runnable() {
			public void run() {
				send();
				Message m = handler.obtainMessage(); 
				handler.sendMessage(m); 
			}
		}).start(); 
    	
    }
    
    private void loadMoreData() {
		// TODO Auto-generated method stub
    	
        new Thread(new Runnable() {
			public void run() {
				send();
				Message m = handler.obtainMessage(); 
				handler.sendMessage(m); 
			}
		}).start(); 
    	
	}
 
    
	public void send() {
		   
		
		String target = List_json.LISTGOOD+"?page="+page;
		List_json  makejson= new List_json(this);
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(target);	
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpRequest);	
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				
				result = EntityUtils.toString(httpResponse.getEntity());	
				
				Listdata.addAll(makejson.parseJsonMulti(result));
				Log.i("aaaa",page+"-------"+Listdata.toString());
				page++;
				
				if(result.equals("0")){
					sj=false;	
				}
			}else{
				result="";
				 
			}	
					
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
			Log.i("aaaa","请求失败-------"+e.getMessage());
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("aaaa","请求失败2-------"+e.getMessage());
		}
		
		count = Listdata.size();
	
	}
	private void adadper() {
		// TODO Auto-generated method stub
		
		 mAdapter=new BaseListAdapter(this);
		 //mAdapter.setValues(Listdata);
		 mListView.setAdapter(mAdapter); 
	     mListView.setOnScrollListener(this); 
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		Log.i("aaaa", "firstVisibleItem="+firstVisibleItem+"\nvisibleItemCount="+
				visibleItemCount+"\ntotalItemCount"+totalItemCount);
		
		lastItem = firstVisibleItem + visibleItemCount - 1;  
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) { 
			
		Log.i("cccc", "SCROLL_STATE_IDLE="+this.SCROLL_STATE_IDLE);
		Log.i("cccc", "scrollState="+scrollState);
		Log.i("cccc", "count="+count);
		if(lastItem == count  && scrollState ==  this.SCROLL_STATE_IDLE){ 
		
			moreView.setVisibility(view.VISIBLE);
		    mHandler.sendEmptyMessage(0);			
		 } 
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
			   	
			   	 loadMoreData();  //
			   	
			    mAdapter.notifyDataSetChanged(); 
			    moreView.setVisibility(View.GONE); 
				break;
            case 1: 
				
            	Log.i("TAGaa1", "eeeeeeeeeeeeeee=");
				break;
			default:
				Log.i("TAGaa2", "qqqqqqqqqqqqqqq=");
				break;
			}
		};
	};

	@Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
        case R.id.listimg1:
        	Intent internt1=new Intent();
			internt1.setClass(Textlist.this,MainActivity.class );
			startActivity(internt1);
            break;
        }
    }

    private void  getData() {
        List<HashMap<String, Object>> maps = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();

		for(int i = 0;i<5;i++){
		map=new HashMap<String, Object>();
		map.put("title", "百乐大力神杯 7频震动 男用阴交自慰器");
		map.put("img", R.drawable.ccc);
		map.put("jiage", "78.0");
		map.put("old_jiage", "165");
		map.put("xiaoliang", "100");
		Listdata.add(map);
		}
		
		count = Listdata.size();
		Log.i("bbbb", "count="+count);  
		Log.i("bbbb", "Listdata="+Listdata); 
    }
    
    private class BaseListAdapter extends BaseAdapter implements OnClickListener {

        private Context mContext;
        private LayoutInflater inflater;
		private List<HashMap<String, Object>> mData;
        
        public BaseListAdapter(Context mContext) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);
            
            this.mData = Listdata;
        }
        
        public void setValues(List<HashMap<String, Object>> listdata) {
			// TODO Auto-generated method stub
			
        	this.mData = listdata;
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
                convertView = inflater.inflate(R.layout.items, null);
                System.out.println("viewHolder111111111111 = " +  (TextView)findViewById(R.id.title));
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
               
                viewHolder.sale_btn = (Button) convertView.findViewById(R.id.sale_btn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag(); 
            }
            
            System.out.println("viewHolder = " + viewHolder);
            Log.i("bbbb", "setImageBitmap111111="+this.mData.get(position).get("img")); 
            Bitmap imgs=image.GetLocalOrNetBitmap((String)this.mData.get(position).get("img"));
             Log.i("bbbb", "setImageBitmap="+imgs); 
            viewHolder.img.setImageBitmap(imgs);
             
          //  viewHolder.img.setBackgroundResource((Integer)this.mData.get(position).get("img"));
            viewHolder.title.setText((CharSequence) this.mData.get(position).get("title"));
            viewHolder.sale_btn.setOnClickListener(this);
            
            return convertView;
        }
        
        class ViewHolder {
        	 ImageView img;
			 TextView title;
			 TextView jiage;
			 TextView old_jiage;
			 TextView xiaoliang;
			 Button sale_btn;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id) {
            case R.id.basebutton:
                showInfo();
                break;
            }
        }
        
    	
        private void showInfo() {
            new AlertDialog.Builder(Textlist.this).setTitle("my listview").setMessage("introduce....").
            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    
                }
            }).show();
        }
    }
}