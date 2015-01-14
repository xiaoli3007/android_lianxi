package com.wyj.Activity;



import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;

import com.wyj.dataprocessing.AccessNetwork;
import com.wyj.dataprocessing.JsonHelper;
import com.wyj.dataprocessing.JsonToListHelper;
import com.wyj.dataprocessing.MyApplication;
import com.wyj.dataprocessing.RegularUtil;
import com.wyj.http.AccessTokenKeeper;
import com.wyj.http.Constants;
import com.wyj.http.WebApiUrl;
import com.wyj.member_db.Member_model;
import com.wyj.Activity.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends Activity implements OnClickListener
{
	
	ImageView logo;
	ImageView weibo_login;
	Button reg;
	Button login_submit;
	TextView  username;
	TextView  passwd;
	private ProgressDialog pDialog = null;
	private String login_username="";
    private static final String TAG = "weibosdk";

    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;
    
    private AuthInfo mAuthInfo;
    
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    
    private LoginButton mLoginBtnDefault;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 去除标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		findViewById() ;	
		setListener() ;
		
		
		// TODO Auto-generated method stub
		  // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

		//mSsoHandler = new SsoHandler(Login.this, mAuthInfo);
		
		mLoginBtnDefault = (LoginButton) findViewById(R.id.login_button_default);
	    mLoginBtnDefault.setWeiboAuthInfo(mAuthInfo, new AuthListener());
	    mLoginBtnDefault.setBackgroundResource(R.drawable.login_17);

		  // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
//        mAccessToken = AccessTokenKeeper.readAccessToken(this);
//        if (mAccessToken.isSessionValid()) {
//            updateTokenView(true); 
//        }
        /**
         * 注销按钮：该按钮未做任何封装，直接调用对应 API 接口
         */
        final Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutAPI(Login.this, Constants.APP_KEY, 
                        AccessTokenKeeper.readAccessToken(Login.this)).logout(new LogOutRequestListener());
            }
        });
        
	} 
	
	private void findViewById() {
		logo=(ImageView) findViewById(R.id.logo);
		reg=(Button) findViewById(R.id.reg_button);	
		login_submit=(Button) findViewById(R.id.login_button);
		username = (TextView) findViewById(R.id.login_username); 
		passwd = (TextView) findViewById(R.id.login_passwd);
		//weibo_login =(ImageView) findViewById(R.id.weibo_logo);
		mTokenText = (TextView) findViewById(R.id.token_text_view);
	 
	}

	private void setListener() {
		username.clearFocus(); 
		logo.setOnClickListener(this);
		reg.setOnClickListener(this);
		login_submit.setOnClickListener(this);
		//weibo_login.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logo:
			//finish();		
			Intent intent=new Intent();
			intent.setClass(Login.this, TabMenu.class);
			startActivity(intent);
			break;
		case R.id.reg_button:
			Intent reg_intent=new Intent();
			reg_intent.setClass(Login.this, Reg.class);
			startActivity(reg_intent);
			break;
		case R.id.login_button:
			login_submit();
			break;
	
		}
		

	}


	private void login_submit() {
		// TODO Auto-generated method stub
		
		String reg_username = username.getText().toString();
		String reg_passwd = passwd.getText().toString();
		Member_model Member_models= new Member_model(this); 
		if (!RegularUtil.checkPhone(this, reg_username)) {
			
			//设置焦点信息;  
			username.setFocusable(true);  
			username.setFocusableInTouchMode(true);  
			username.requestFocus();  
			username.requestFocusFromTouch();  
		} else if(!RegularUtil.checkPassword(this, reg_passwd)){
		
			//设置焦点信息;  
			passwd.setFocusable(true);  
			passwd.setFocusableInTouchMode(true);  
			passwd.requestFocus();  
			passwd.requestFocusFromTouch();  
			
		} else {
			
			logincheckapi( reg_username,  reg_passwd);
//			RegularUtil.alert_msg(this, "登录成功！");					
		}
		
	}
	
	private void logincheckapi(final String reg_username, String reg_passwd) {
		// TODO Auto-generated method stub
		//接口验证注册
	
		  //利用Handler更新UI  
        final Handler h = new Handler(){  
            @Override  
            public void handleMessage(Message msg) {  
                if(msg.what==0x123){  
                	pDialog.dismiss();
                	String backmsg=msg.obj.toString();
                	Map<String, Object> resmsg = new HashMap<String, Object>();
                	resmsg =JsonToListHelper.jsontoCode(backmsg); 
                	if(resmsg.get("code").equals("succeed")){
                		login_username=reg_username;
                		RegularUtil.alert_msg(Login.this, "登录成功"); 
            			login();
                	}else{
                		RegularUtil.alert_msg(Login.this, "登录失败，"+resmsg.get("msg")); 
                	} 
                	
                	
                 }  
            }
        }; 
        String params="mobile="+reg_username+"&password="+reg_passwd;
        pDialog = new ProgressDialog(Login.this);
		pDialog.setMessage("请求中。。。");
		pDialog.show();
        new Thread(new AccessNetwork("POST", WebApiUrl.LOGIN, params, h)).start();  
     
	}
	
	private void login() {		//登录传值-----------------------
		// TODO Auto-generated method stub
		Intent login_intent=new Intent();
		login_intent.setClass(Login.this, My.class);
		Bundle bu=new Bundle();// 这个组件 存值
		bu.putString("username", login_username);
		login_intent.putExtras(bu);
		//设置返回数据
		Login.this.setResult(1, login_intent);
		
		session_member();
	
	} 
	
	 private void session_member() {		//登录获取账户信息
		// TODO Auto-generated method stub
		   MyApplication.getInstances().setName(login_username);//设置用户名
		   
		    final Handler h = new Handler(){  
	            @Override  
	            public void handleMessage(Message msg) {  
	                if(msg.what==0x123){  
	                	
	                	pDialog.dismiss();
	                	String backmsg=msg.obj.toString();
	                	Map<String, Object> resmsg = new HashMap<String, Object>();
	                	Map<String, Object> jsontosingle = new HashMap<String, Object>();
	                	
	                	String[] keyNames={"memberid","membername","headface","nickname","truename","sex","area","tmb_headface"};
	                	jsontosingle =JsonHelper.jsonStringToMap(backmsg,keyNames,"memberinfo"); 
	                	
	                	
	                	int memberid=Integer.valueOf(String.valueOf(jsontosingle.get("memberid"))).intValue();
	                	int sex=Integer.valueOf(String.valueOf(jsontosingle.get("sex"))).intValue();
	                	MyApplication.getInstances().setMemberid(memberid);//设置用户ID
	                	MyApplication.getInstances().setSex(sex);//设置用户ID
	                	MyApplication.getInstances().setHeadface((String) jsontosingle.get("headface"));//设置用户名
	                	MyApplication.getInstances().setArea((String) jsontosingle.get("area"));//设置用户名
	                	MyApplication.getInstances().setTruename((String) jsontosingle.get("truename"));//设置用户名
	                	
	                	
	                	 //Log.i(TAG,"------用户信息"+jsontosingle.get("memberid"));
	                	
	                	resmsg =JsonToListHelper.jsontoCode(backmsg); 
	                	
	                	if(resmsg.get("code").equals("succeed")){
	                		
	                		RegularUtil.alert_msg(Login.this, "登录成功"); 
	                		//关闭Activity
	                		Login.this.finish();
	                	}else{
	                		
	                		RegularUtil.alert_msg(Login.this, "登录失败，"+resmsg.get("msg")); 
	                	} 
	                	
	                	
	                 }  
	            }   
	        }; 
	        String params="mobile="+login_username;
	        pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("登录中。。。");
			pDialog.show();
	        new Thread(new AccessNetwork("GET", WebApiUrl.GET_USERINFO, params, h)).start();  		   
	}

	/**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
//        if (mSsoHandler != null) {
//            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
        
        mLoginBtnDefault.onActivityResult(requestCode, resultCode, data);
    }
	
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);
                
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(Login.this, mAccessToken);
             //   Toast.makeText(Login.this,  "授权成功", Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(Login.this,  "取消授权", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(Login.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            
          Log.i("aaaa","------微博返回信息"+e.getMessage());
        }
    }
    
    /**
     * 显示当前 Token 信息。
     * 
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = "Token：%1$s \n有效期：%2$s";
             
       // mTokenText.setText(mAccessToken.getToken());
        
        UsersAPI mUsersAPI = new UsersAPI(Login.this,Constants.APP_KEY,mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener);
        
//        String message = String.format(format, mAccessToken.getToken(), date);
//        if (hasExisted) {
//            message =  "Token 仍在有效期内，无需再次登录。\n" + message;
//        }
//        mTokenText.setText(message);
    }
    
    private RequestListener mListener = new RequestListener() {
    	
    	public void onComplete(String response) {
	    	if (!TextUtils.isEmpty(response)) {
	    		// 调用User#parse 将JSON串解析成User对象
	    		User user = User.parse(response);
	    		
	    		sina_login(user.id,user.screen_name,mAccessToken.getToken(),user.avatar_large);
	    	    mTokenText.setText(user.screen_name);
	    	}
    	}


		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
    };
    

	private void sina_login(final String name, String screen_name, String token,
			String avatar_large) {
		// TODO Auto-generated method stub
		
		  //利用Handler更新UI  
        final Handler sina = new Handler(){  
            @Override  
            public void handleMessage(Message msg) {  
                if(msg.what==0x123){  
                	pDialog.dismiss();
                	String backmsg=msg.obj.toString();
                	if(backmsg!=""){
                		Map<String, Object> resmsg = new HashMap<String, Object>();
	                   	 Log.i("aaaa","------第三方登录返回信息"+backmsg);
	                   	resmsg =JsonToListHelper.jsontoCode(backmsg); 
	                   	if(resmsg.get("code").equals("succeed")){
	                   		login_username="sina"+name;
	                   		RegularUtil.alert_msg(Login.this, "登录成功"); 
	               			login();
	                   	}else{
	                   		RegularUtil.alert_msg(Login.this, "登录失败，"+resmsg.get("msg")); 
	                   	} 
                	}else{
                		RegularUtil.alert_msg(Login.this, "登录失败"); 
                	} 	
                 }  
            }
        }; 
		String params="name="+name+"&token="+token+"&hf="+avatar_large+"&nname="+screen_name+"&regtype=sina";
		Log.i("aaaa","------发出的信息"+params); 
		 pDialog = new ProgressDialog(Login.this);
		 pDialog.setMessage("请求中。。。");
		 pDialog.show();
        new Thread(new AccessNetwork("POST", WebApiUrl.THREE_LOGIN, params, sina)).start(); 
	} 
     
    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(Login.this);
                        mTokenText.setText("注销成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }     
 
        @Override
        public void onWeiboException(WeiboException e) {
        	 mTokenText.setText("注销失败");
        }
    }
    
	
	
}