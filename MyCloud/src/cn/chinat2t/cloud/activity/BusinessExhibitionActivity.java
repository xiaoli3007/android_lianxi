package cn.chinat2t.cloud.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.chinat2t.cloud.R;
import cn.chinat2t.cloud.bean.BusinessListBean;
import cn.chinat2t.cloud.bean.BusinessOrderListBean;
import cn.chinat2t.cloud.bean.BusinessExhibitionListBean;
import cn.chinat2t.cloud.http.BitmapManager;
import cn.chinat2t.cloud.http.CommunicationManager;
import cn.chinat2t.cloud.http.CommunicationResultListener;
import cn.chinat2t.cloud.utils.Tools;

public class BusinessExhibitionActivity extends Activity implements OnClickListener, OnScrollListener {
	private ListView businessExhibitionListView;
	private List<BusinessExhibitionListBean> businessExhibitionList;
	private List<BusinessExhibitionListBean> businessExhibitionSearchList;
	private BusinessExhibitionAdapter businessExhibitionAdapter;
	private EditText searchEt = null;
	private Button searchBtn = null;
	static boolean isSearch=false;
	String text="";
	private View moreView; //加载更多页面
	private int lastItem;
    private int count;
    private int count1;
	static int i=1;
	static int j=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business_exhibition);
		initViews();
		initData();

	}
	
	 private void loadMoreData(){ //加载更多数据
		 if(!isSearch)
		 {
	      	count = businessExhibitionAdapter.getCount(); 
	      	CommunicationManager.getInstance().getBusinessExhibitionList(i,(String) this.getResources().getText(R.string.pageSize),businessExhibitionResultListener);
	       	i++;
	       	count = businessExhibitionList.size();
		 }
		 else
		 {
		      	count1 = businessExhibitionAdapter.getCount(); 
		      	CommunicationManager.getInstance().getSearchBusinessExhibition(j,(String) this.getResources().getText(R.string.pageSize),text.trim(), searchResultListener);
		       	j++;
		       	count1 = businessExhibitionSearchList.size();
		 }
      }
	 
	private void initData(){
		CommunicationManager.getInstance().getBusinessExhibitionList(i,(String) this.getResources().getText(R.string.pageSize),businessExhibitionResultListener);
		i++;
      	count = businessExhibitionList.size();
	}
	private void initViews(){
		i=1;
		businessExhibitionList=new ArrayList<BusinessExhibitionListBean>();
		searchEt = (EditText) findViewById(R.id.business_exhibition_search_text);
		searchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            
            public void onFocusChange(View v, boolean hasFocus) {
            	 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(hasFocus){
                            //searchauto.setInputType(InputType.TYPE_NULL);
                             imm.showSoftInputFromInputMethod(searchEt.getWindowToken(), 0);
                    }
                    else{
                            imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
                    }
            }
    });
	searchEt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            	Log.i("wyq", keyCode+"键盘被点下");
                    if(keyCode == KeyEvent.KEYCODE_ENTER){//keyCode==KeyEvent.KEYCODE_ENTER
                    	Log.i("wyq", keyCode+"键盘被点下，执行了");
                    	 InputMethodManager    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    	 imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
                            return true;
                    }
                    return false;
            }
    });
		searchBtn = (Button) findViewById(R.id.business_exhibition_search_btn);
		searchBtn.setOnClickListener(this);
		businessExhibitionListView = (ListView) findViewById(R.id.business_exhibition_list);
		moreView =getLayoutInflater().inflate(R.layout.load, null);
		businessExhibitionListView.addFooterView(moreView);
		businessExhibitionListView.setOnScrollListener(this);
		businessExhibitionAdapter = new BusinessExhibitionAdapter();
		businessExhibitionListView.setAdapter(businessExhibitionAdapter);
		businessExhibitionListView.setOnItemClickListener(businessExhibitionItemClick);
	}
	
	OnItemClickListener businessExhibitionItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(BusinessExhibitionActivity.this,BusinessExhibitionDetailActivity.class);
			if(isSearch)
			{
				intent.putExtra("id", businessExhibitionSearchList.get(arg2).getId());
			}else
			intent.putExtra("id", businessExhibitionList.get(arg2).getId());
			//startActivity(intent);
			MoreGroup.getInstance().switchActivity("BusinessExhibitionDetailActivity",intent,-1,-1);
		}
	};
	private CommunicationResultListener businessExhibitionResultListener = new CommunicationResultListener() {
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				break;
				
			case CommunicationManager.SUCCEED:
				break;
			}
			Message msg = mHandler.obtainMessage();
			msg.what = result;
			msg.arg1 = 1;
			msg.obj = resultData;
			mHandler.sendMessage(msg);
		};
	};
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CommunicationManager.FAIL:
				
				break;
			case CommunicationManager.SUCCEED:
				if(msg.arg1 == 1){
					if("0".equals(msg.obj.toString().trim()))
					{
						return;
					}
					List tempList=new ArrayList<BusinessExhibitionListBean>();
					tempList=(List<BusinessExhibitionListBean>) msg.obj;
					businessExhibitionList.addAll(tempList);
					//businessExhibitionList = (List<BusinessExhibitionListBean>) msg.obj;
					businessExhibitionAdapter.setValues(businessExhibitionList);
					businessExhibitionAdapter.notifyDataSetChanged();
					count=businessExhibitionList.size();
				} 
				break;

			default:
				break;
			}
		};
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			MoreGroup.getInstance().back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	class BusinessExhibitionAdapter extends BaseAdapter{
		private List<BusinessExhibitionListBean> businessList = null;
		private HashMap<Integer, View> viewMap = null;
		
		public void setValues(List<BusinessExhibitionListBean> bList){
			this.businessList = bList;
			viewMap = new HashMap<Integer, View>();
		}
		
		@Override
		public int getCount() {
			if(businessList == null ) return 0;
			return businessList.size();
		}

		@Override
		public Object getItem(int position) {
			return businessList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = viewMap.get(position);
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(BusinessExhibitionActivity.this, R.layout.business_exhibition_listitem, null);
				holder = new ViewHolder();
				holder.thumb = (ImageView) convertView.findViewById(R.id.business_exhibition_list_Logo);
				holder.title = (TextView)convertView.findViewById(R.id.business_exhibition_list_name);
				holder.inputtime = (TextView)convertView.findViewById(R.id.business_exhibition_list_pubtime);
				holder.description = (TextView)convertView.findViewById(R.id.business_exhibition_list_description);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			BusinessExhibitionListBean p = businessList.get(position);
			holder.title.setText(p.getTitle());
			holder.inputtime.setText(p.getInputtime());
			holder.description.setText(p.getDescription());
			BitmapManager.getInstance().loadBitmap(p.getThumb(), holder.thumb, Tools.readBitmap(BusinessExhibitionActivity.this, R.drawable.image6));
//			holder.business_log
//			holder.name.setText(p.title);
//			holder.desc.setText(p.description);
//			BitmapManager.getInstance().loadBitmap(p.thumb, holder.iv, Tools.readBitmap(ProductActivity.this, R.drawable.image6));
//			
			return convertView;
		}
		class ViewHolder{
			public TextView title;
			public TextView description ;
			public TextView inputtime;
			public ImageView thumb;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.business_exhibition_search_btn:
			 InputMethodManager    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        	 imm.hideSoftInputFromWindow(searchBtn.getWindowToken(), 0);
			 
        	 text = searchEt.getText().toString();
			if(text.equals("")){
				//Toast.makeText(this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
				isSearch=false;
				businessExhibitionAdapter.setValues(businessExhibitionList);
				businessExhibitionAdapter.notifyDataSetChanged();
				return;
			}else
			{	j=1;
				businessExhibitionSearchList=new ArrayList<BusinessExhibitionListBean>();
				CommunicationManager.getInstance().getSearchBusinessExhibition(j,(String) this.getResources().getText(R.string.pageSize),text.trim(), searchResultListener);
				j++;
				count1=businessExhibitionSearchList.size();
				isSearch=true;
			}
			
			break;

		default:
			break;
		}
	}

	
	
	private CommunicationResultListener searchResultListener = new CommunicationResultListener() {
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				break;
				
			case CommunicationManager.SUCCEED:
				break;
			}
			Message msg = SearchmHandler.obtainMessage();
			msg.what = result;
			msg.arg1 = 1;
			msg.obj = resultData;
			SearchmHandler.sendMessage(msg);
		};
	};
	
	Handler SearchmHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CommunicationManager.FAIL:
				Toast.makeText(BusinessExhibitionActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
//				mSearchAdapter.setValue(null);
//				mSearchAdapter.notifyDataSetChanged();
				break;
			case CommunicationManager.SUCCEED:
				if(msg.arg1 == 1){
					if("0".equals(msg.obj.toString()))
					{
						return;
					}
					List tempList=new ArrayList<BusinessExhibitionListBean>();
					tempList=(List<BusinessExhibitionListBean>) msg.obj;
					businessExhibitionSearchList.addAll(tempList);
					//businessExhibitionSearchList = (List<BusinessExhibitionListBean>) msg.obj;
					businessExhibitionAdapter.setValues(businessExhibitionSearchList);
					businessExhibitionAdapter.notifyDataSetChanged();
					count1=businessExhibitionSearchList.size();
				}
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(!isSearch)
		{
			if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){ 
				//Log.i(TAG, "拉到最底部");
				moreView.setVisibility(view.VISIBLE);
			 
			    mHandler1.sendEmptyMessage(0);
				 
			}
		}
		else
		{
			if(lastItem == count1  && scrollState == this.SCROLL_STATE_IDLE){ 
				//Log.i(TAG, "拉到最底部");
				moreView.setVisibility(view.VISIBLE);
			 
			    mHandler1.sendEmptyMessage(0);
				 
			}
		}
	
	}
	private Handler mHandler1 = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
			     
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    loadMoreData();  //加载更多数据，这里可以使用异步加载
			    if(!isSearch)
			    	businessExhibitionAdapter.setValues(businessExhibitionList);
			    else
			    	 businessExhibitionAdapter.setValues(businessExhibitionSearchList);
			    businessExhibitionAdapter.notifyDataSetChanged();
			    
			    moreView.setVisibility(View.GONE); 
//			    
//			    if(count > 30){
//			    	Toast.makeText(MainActivity.this, "木有更多数据！", 3000).show();
//			          listView.removeFooterView(moreView); //移除底部视图
//			    }
//				Log.i(TAG, "加载更多数据");
				break;
            case 1:
				
				break;
			default:
				break;
			}
		};
	};
}
