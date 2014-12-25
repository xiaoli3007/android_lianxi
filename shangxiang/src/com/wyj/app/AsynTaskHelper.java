package com.wyj.app;

import java.util.List;
import java.util.Map;

import com.wyj.http.HttpClientHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsynTaskHelper {
	// private static final String TAG = "AsynTaskHelper";

	public void dataDownload(String url, Map<String, Object> map,
			OnDataDownloadListener downloadListener, Context context,
			String methodString) {
		new MyTask(downloadListener, map, context, methodString).execute(url);
	}
	
	public void pull_dataDownload(String url, Map<String, Object> map,
			OnDataDownloadListener downloadListener, Context context,
			String methodString) {
		new GetDataTask(downloadListener).execute(url);
	}
	
	public void more_dataDownload(String url, Map<String, Object> map,
			OnDataDownloadListener downloadListener, Context context,
			String methodString) {
		new GetMoreDataTask(downloadListener).execute(url);
	}
	
	//默认的加载类-------------------------------------------------------------------------------------------
	private class MyTask extends AsyncTask<String, Void, String> {
		private OnDataDownloadListener downloadListener;
		private Map<String, Object> map;
		private ProgressDialog pDialog = null;
		private String methodString;

		public MyTask(OnDataDownloadListener downloadListener,
				Map<String, Object> map, Context context, String methodString) {
			this.downloadListener = downloadListener;
			this.map = map;
			this.methodString = methodString;
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("数据加载中。。。");
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String data = null;
			if (methodString.equals("GET")) {
				
				data = HttpClientHelper.httpUrl(params[0]);
				
			} else {

				//data = HttpClientHelper.doPostSubmit(params[0], map);
			}
			if (data != null) {
				return data;
			} else {
				System.out.println("没有数据！！");
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
		
			// 通过回调接口来传递数据
			downloadListener.onDataDownload(result);
			pDialog.dismiss();
			super.onPostExecute(result);
		}
	}
	
	
	//更新的加载类-------------------------------------------------------------------------------------------
    private class GetDataTask extends AsyncTask<String, Void, String> {
        
    	private OnDataDownloadListener downloadListener;
		
		public GetDataTask(OnDataDownloadListener downloadListener2) {
			// TODO Auto-generated constructor stub
			this.downloadListener = downloadListener2;
		}

		@Override 
		protected String doInBackground(String... params) {
			// Simulates a background job.
			try {
				
				Thread.sleep(3000);
			
			} catch (InterruptedException e) {
				
			}
			String data = null;
			
			data = HttpClientHelper.httpUrl(params[0]);
			
			if (data != null) {
				return data;
			} else {
				System.out.println("没有数据！！");
				return null;
			}		
		}

		@Override
		protected void onPostExecute(String result) {

			// 通过回调接口来传递数据
			downloadListener.onDataDownload(result);
			super.onPostExecute(result);
		}
	}
    
  //获取更多的加载类-------------------------------------------------------------------------------------------
    private class GetMoreDataTask extends AsyncTask<String, Void, String> {
        
    	private OnDataDownloadListener downloadListener;
		
		public GetMoreDataTask(OnDataDownloadListener downloadListener3) {
			// TODO Auto-generated constructor stub
			this.downloadListener = downloadListener3;
		}

		@Override 
		protected String doInBackground(String... params) {
			// Simulates a background job.
			try {
				
				Thread.sleep(3000);
			
			} catch (InterruptedException e) {
				
			}
			String data = null;
			
			data = HttpClientHelper.httpUrl(params[0]);
			
			if (data != null) {
				return data;
			} else {
				System.out.println("没有数据！！");
				return null;
			}		
		}

		@Override
		protected void onPostExecute(String result) {

			// 通过回调接口来传递数据
			downloadListener.onDataDownload(result);
			super.onPostExecute(result);
		}
	}
    
	public interface OnDataDownloadListener {
		void onDataDownload(String result);
	}
}