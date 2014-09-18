package com.example.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.panduola.R;


public class List_json {

	
	 public static  String LISTGOOD ="http://192.168.1.30/app.php";
	
	
	 public List<HashMap<String, Object>> parseJsonMulti(String strResult) { 
		 
		 List<HashMap<String, Object>> maps = new ArrayList<HashMap<String,Object>>();
         HashMap<String, Object> map = new HashMap<String, Object>();
	    	  
	      try { 
	    	   
	    	  JSONArray jsonObjs = new JSONArray(strResult);
	          //  JSONArray jsonObjs = new JSONObject(strResult).getJSONArray("singers"); 
	          
	            String s = "";   
	            
                
	            for(int i = 0; i < jsonObjs.length() ; i++){ 

	                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i)) ; 

	                int id = jsonObj.getInt("id"); 
	                String title = jsonObj.getString("title");   
	                String description = jsonObj.getString("description");
	                String thumb = jsonObj.getString("thumb");
	                
	        		map=new HashMap<String, Object>();
	        		map.put("title", title);
	        		map.put("img", R.drawable.ccc);
	        		map.put("jiage", "78.0");
	        		map.put("old_jiage", "165");
	        		map.put("xiaoliang", "100");
	        		maps.add(map);
	        	  }
	           
	        } catch (JSONException e) { 

	           e.printStackTrace(); 

	        } 
	      return maps;
	    } 
}
