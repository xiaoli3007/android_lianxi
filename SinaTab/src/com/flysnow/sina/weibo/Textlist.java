package com.flysnow.sina.weibo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author shangzhenxiang
 *
 */
public class Textlist extends Activity {

    private ListView mListView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseadapterlist);
        mListView = (ListView) findViewById(R.id.xianlu2);
        mListView.setAdapter(new BaseListAdapter(this));
    }
    
    private List<HashMap<String, Object>> getData() {
        List<HashMap<String, Object>> maps = new ArrayList<HashMap<String,Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("image", R.drawable.ddd);
        map.put("title", "G1");
        map.put("info", "google 1");
        maps.add(map);
        
        map = new HashMap<String, Object>();
        map.put("image", R.drawable.ddd);
        map.put("title", "G2");
        map.put("info", "google 2");
        maps.add(map);
        
        map = new HashMap<String, Object>();
        map.put("image", R.drawable.ddd);
        map.put("title", "G3");
        map.put("info", "google 3");
        maps.add(map);
        return maps;
    }
    
    private class BaseListAdapter extends BaseAdapter implements OnClickListener {

        private Context mContext;
        private LayoutInflater inflater;
        
        public BaseListAdapter(Context mContext) {
            this.mContext = mContext;
            inflater = LayoutInflater.from(mContext);
        }
        
        @Override
        public int getCount() {
            return getData().size();
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
                convertView = inflater.inflate(R.layout.vlist2, null);
                System.out.println("viewHolder111111111111 = " +  (TextView)findViewById(R.id.title));
                viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.info = (TextView) convertView.findViewById(R.id.info);
                viewHolder.button = (Button) convertView.findViewById(R.id.basebutton);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            
            System.out.println("viewHolder = " + viewHolder);
            viewHolder.img.setBackgroundResource((Integer) getData().get(position).get("image"));
            viewHolder.title.setText((CharSequence) getData().get(position).get("title"));
            viewHolder.info.setText((CharSequence) getData().get(position).get("info"));
            viewHolder.button.setOnClickListener(this);
            
            return convertView;
        }
        
        class ViewHolder {
            ImageView img;
            TextView title;
            TextView info;
            Button button;
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