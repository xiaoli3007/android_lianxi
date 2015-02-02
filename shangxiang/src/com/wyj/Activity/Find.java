package com.wyj.Activity;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

import com.wyj.adapter.FindListAdapter;
import com.wyj.adapter.ListTempleNameAdapter;
import com.wyj.dataprocessing.AsynTaskHelper;
import com.wyj.dataprocessing.BitmapManager;
import com.wyj.dataprocessing.JsonHelper;
import com.wyj.dataprocessing.JsonToListHelper;
import com.wyj.dataprocessing.AsynTaskHelper.OnDataDownloadListener;

import com.wyj.http.WebApiUrl;
import com.wyj.Activity.R;

import com.wyj.pipe.Cms;
import com.wyj.utils.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;

import android.widget.TextView;

import android.widget.Toast;

public class Find extends Activity {
	


	private TextView daochang_select_showname;
	private LinearLayout daochang_select;
	private List<Map<String, Object>> daochang_data; // 加载到适配器中的数据源
	private int tid = 0; // 道场id的标识
	private int mid=0;

	private View moreView;
	private ListView mListView;
	private List<Map<String, Object>> Listdata; // 加载到适配器中的数据源
	private JSONArray findlistdata;
	private FindListAdapter mAdapter;
	//private BaseListAdapter mAdapter;
	private int page = 1;
	private int pagesize = 30;
	private boolean isBottom = false;// 判断是否滚动到数据最后一条
	private int lastItemId;
	private int count;
	private PullToRefreshListView mPullRefreshListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Listdata = new ArrayList<Map<String, Object>>();
		View contenView = LayoutInflater.from(this.getParent()).inflate(
				R.layout.tab_find, null);

		setContentView(contenView);
		if(!TextUtils.isEmpty(Cms.APP.getMemberId())){
			mid=  Integer.valueOf(Cms.APP.getMemberId()).intValue();
		}
		int detail_tid = getIntent().getIntExtra("tid", 0);

		if (detail_tid != 0) {
			tid = detail_tid; // 详情页面返回寺庙的传值
		}
		
		
		ActivityfindViewById();
		ActivityAction();


	}
	
	private void ActivityfindViewById() {
		// TODO Auto-generated method stub
		
		daochang_select =(LinearLayout) findViewById(R.id.daochang_select);
		daochang_select_showname= (TextView) findViewById(R.id.daochang_select_showname);
		
	}
	
	private void ActivityAction() {
		// TODO Auto-generated method stub
		default_order_list();
		select_order_list();
		daochang_select.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(daochang_data!=null){
							new MyPopupWindows(Find.this,v,getParent().getParent(),daochang_data);	
						}else{
						    get_daochang_list(null, WebApiUrl.GET_TEMPLELIST, getParent(),v);
						}
						
						
					}
				});
	}


	private void get_daochang_list(Map<String, Object> map, String url,
			final Context context,final View v) {

		
		AsynTaskHelper asyntask = new AsynTaskHelper();
		asyntask.dataDownload(url, map, new OnDataDownloadListener() {
			@Override
			public void onDataDownload(String result) {
				if (result != null) {
					// Listdata.clear();
					List<Map<String, Object>> items;

					items = JsonToListHelper.gettemplelist_json(result);
					daochang_data=new ArrayList<Map<String,Object>>(); 
					Map<String, Object> map = new HashMap<String, Object>(){{put("templeid", 0);put("templename", "全部道场");}};
					daochang_data.add(map);
					daochang_data.addAll(items);
					//daochang_data =items;
					// 初始化------------------------------------

					// 初始化下拉选项------------------------------------
					
			new MyPopupWindows(Find.this,v,getParent().getParent(),daochang_data);

				} else {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}

			}
		}, context, "GET");

	}



	private void default_order_list() { // 默认加载 和更换道场加载

		if (isBottom) {
			isBottom = false;
		}

		page = 1;
		Listdata.clear();
		

		listAdapter(null, WebApiUrl.GET_ORDERLIST + "?p=" + page + "&&pz="
				+ pagesize + "&&tid=" + tid+ "&&mid=" + mid, getParent()); // 默认加载第一页
	}

	private void select_order_list() {  // 默认适配上拉下拉操作
		// TODO Auto-generated method stub
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.find_list);
		// 设置底部加载的字幕

		mPullRefreshListView.setMode(Mode.PULL_FROM_END); // 支持上拉和下拉操作
		mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				"加载中");
		mPullRefreshListView.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel("正在加载");
		mPullRefreshListView.getLoadingLayoutProxy(false, true)
				.setReleaseLabel("上拉加载");

		// 下拉更新时间
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// Do work to refresh the list here.
						// new
						// GetDataTask().execute(WebApiUrl.GET_ORDERLIST+"?p=5&&pz=1");
						pull_listAdapter(null, WebApiUrl.GET_ORDERLIST
								+ "?p=1&&pz=100&&tid=7", getParent());

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {

						if (!isBottom) {
							page++;
							morelistAdapter(null, WebApiUrl.GET_ORDERLIST
									+ "?p=" + page + "&&pz=" + pagesize
									+ "&&tid=" + tid+ "&&mid=" + mid, getParent());
						} else {
							

							Toast.makeText(getParent(), "没有了",
									Toast.LENGTH_SHORT).show();
							mPullRefreshListView.onRefreshComplete();

						}

					}
				});

		mListView = mPullRefreshListView.getRefreshableView();

		// mListView = (ListView) findViewById(R.id.find_list);
		mAdapter = new FindListAdapter(getBaseContext(), Listdata,tid);
		mListView.setAdapter(mAdapter);

//		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				// String data = arg0.getItemAtPosition(arg2).toString();
//
//				Integer orderid = (Integer) Listdata.get(arg2 - 1).get(
//						"orderid");
//				// 要跳转的Activity
//				Intent intent = new Intent(Find.this, Find_item.class);
//
//				Bundle bu = new Bundle(); // 这个组件 存值
//				bu.putInt("orderid", orderid);
//				bu.putInt("tid", tid);
//				intent.putExtras(bu); // 放到 intent 里面 然后 传出去
//				// 把Activity转换成一个Window，然后转换成View
//				Log.i("bbbb", "------传了没传啊----" + orderid);
//				FindGroupTab.getInstance().switchActivity("Find_item", intent,
//						-1, -1);
//			}
//		});
	}

	private void listAdapter(Map<String, Object> map, String url,
			final Context context) {
		Log.i("bbbb", "------列表---" + url);
		AsynTaskHelper asyntask = new AsynTaskHelper();
		asyntask.dataDownload(url, map, new OnDataDownloadListener() {

			@Override
			public void onDataDownload(String result) {
				if (result != null) {
					Log.i("bbbb", "------列表---" + result);
					
					List<Map<String, Object>> items;
					items = JsonHelper.jsonTolistmap(
							(String) result, "orderlist");
					
					Listdata.addAll(items);
					count = Listdata.size();
					mAdapter = new FindListAdapter(getBaseContext(), Listdata,tid);
					mListView.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();

					if (items.toString().equals("[]")) {
						isBottom = true;
					} else {
						lastItemId =Integer.valueOf((String)items.get(0).get("orderid")).intValue(); 
					}

				} else {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}

			}
		}, context, "GET");
	}

	// 上拉加载操作-------
	private void morelistAdapter(Map<String, Object> map, String url,
			final Context context) {
		AsynTaskHelper asyntask = new AsynTaskHelper();
		asyntask.more_dataDownload(url, map, new OnDataDownloadListener() {

			@Override
			public void onDataDownload(String result) {
				if (result != null) {
					// Listdata.clear();
					List<Map<String, Object>> items;
					items = JsonHelper.jsonTolistmap(
							(String) result, "orderlist");
					Listdata.addAll(items);
					count = Listdata.size();
					mAdapter.notifyDataSetChanged();

					if (items.toString().equals("[]")) {
						Toast.makeText(getParent(), "没有了", Toast.LENGTH_SHORT)
								.show();
						mPullRefreshListView.onRefreshComplete();
						isBottom = true;
					}

				} else {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}

			}
		}, context, "GET", mPullRefreshListView);
	}

	// 下拉更新操作-------
	private void pull_listAdapter(Map<String, Object> map, String url,
			final Context context) {
		AsynTaskHelper asyntask = new AsynTaskHelper();
		asyntask.pull_dataDownload(url, map, new OnDataDownloadListener() {

			@Override
			public void onDataDownload(String result) {
				if (result != null) {
					// Listdata.clear();
					List<Map<String, Object>> items;
					items = JsonHelper.jsonTolistmap(
							(String) result, "orderlist");

					// Log.i("bbbb","------上拉如果没有数据的时候-----"+items.toString());
					if (items.toString().equals("[]")) {
						mPullRefreshListView.onRefreshComplete();
						Toast.makeText(context, "没有最新的啦", Toast.LENGTH_SHORT)
								.show();
					} else {

						lastItemId = (Integer) items.get(0).get("orderid");
						Listdata.addAll(0, items);
						mAdapter.notifyDataSetChanged();
						mPullRefreshListView.onRefreshComplete();
					}

				} else {
					Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
				}

			}
		}, context, "GET");

	}

	
	
	public class MyPopupWindows extends PopupWindow {
		
		private PopupWindow popupwindow;
		private ListTempleNameAdapter adapterListTemple;
		
		public MyPopupWindows(Context mContext,  View parent, final Activity activity, final List<Map<String, Object>> items) {
			
			final Activity pactivity=activity;
			
			View customView = View.inflate(mContext, R.layout.popview_item,
					null);
			ListView List = (ListView) customView
					.findViewById(R.id.popview_discover_container);
			
			adapterListTemple = new ListTempleNameAdapter(
					Find.this, items);
			List.setAdapter(adapterListTemple);
			
			
			popupwindow = new PopupWindow(customView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);			
			 //以下为弹窗后面的背景色设置
		 	ColorDrawable cd = new ColorDrawable(0x000000);
		 	popupwindow.setBackgroundDrawable(cd); 
		   	//产生背景变暗效果
		    WindowManager.LayoutParams lp=activity.getWindow().getAttributes(); 
			lp.alpha = 0.7f;
			activity.getWindow().setAttributes(lp);
			
			popupwindow.setWidth(LayoutParams.FILL_PARENT);
			popupwindow.setHeight(LayoutParams.FILL_PARENT);
			popupwindow.setBackgroundDrawable(new BitmapDrawable());
			popupwindow.setFocusable(true);
			popupwindow.setOutsideTouchable(true);
			popupwindow.setContentView(customView);

			int[] location = new int[2];
			parent.getLocationOnScreen(location);

			popupwindow.showAsDropDown(parent); //显示在button的下面

			// 自定义view添加触摸事件
			popupwindow.update();
			popupwindow.setOnDismissListener(new OnDismissListener() {		//恢复背景色
				
				public void onDismiss() {
					// TODO Auto-generated method stub
					WindowManager.LayoutParams lp=pactivity.getWindow().getAttributes();
	    			lp.alpha = 1f;
	    			pactivity.getWindow().setAttributes(lp);
				}
			});
			
			List.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
					
					 tid = (Integer) items.get(arg2).get("templeid");
					 default_order_list();
					 daochang_select_showname.setText((String) items.get(arg2).get("templename"));
					 popupwindow.dismiss();
				}
			});
			adapterListTemple.notifyDataSetChanged();
			
			customView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (popupwindow != null && popupwindow.isShowing()) {
						popupwindow.dismiss();
						popupwindow = null;
					}

					return false;
				}
			});		
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FindGroupTab.getInstance().onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
