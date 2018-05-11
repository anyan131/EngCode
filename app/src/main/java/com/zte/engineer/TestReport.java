package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;




public class TestReport extends Activity{
	
	
	private static final String TAG = "TestReport";
	private ListView list;
	
	
	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.test_report);
		Log.i(TAG,"onCreate:");
		list = (ListView)findViewById(R.id.listView_testResult);
		AlexAdapter adapter = new AlexAdapter(this,R.layout.test_result_list,R.id.testItemText,R.id.testItemResult/*,mItemResults*/);
		list.setAdapter(adapter);
		
	}
	
	
	
	//this is for the test item.
	private class AlexAdapter extends BaseAdapter{
		
		private Context mContext;
    	private int layout;
    	private int itemTextID;
    	private int resultTextID;
    	//private ItemResult[] itemResults;
    	LayoutInflater mInflater;
		//Context context;
		
		
		private SharedPreferences prefs;
		private SharedPreferences.Editor editor;
		
		public AlexAdapter(Context context,int layout,int itemTextID,int resultTextID/*,ItemResult[] itemResults*/){
			mInflater = LayoutInflater.from(context);
			this.layout = layout;
			this.itemTextID = itemTextID;
			//this.itemResults = itemResults;
			this.mContext = context;
		
		}

       @Override
       public int getCount() {
           return EngineerCode.stringIDs.length;
       }

       @Override
       public Object getItem(int position) {
           return EngineerCode.stringIDs[position];
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           View view;
		   ViewHolder holder;
		   if(convertView == null){
			   view = mInflater.from(getApplicationContext()).inflate(layout,null);
			   holder = new ViewHolder();
			   holder.ItemText = (TextView)view.findViewById(R.id.testItemText);
			   holder.ItemResult = (TextView)view.findViewById(R.id.testItemResult);
			   //将holder存储在view中
			   view.setTag(holder);
		   }else{
			   view = convertView;
			   holder = (ViewHolder)view.getTag();
		   }
		   
		   //读取文件，同时填入对应项里面。
		   prefs = mContext.getSharedPreferences("engineer",MODE_MULTI_PROCESS);
		   editor = prefs.edit();
		   String key = getResources().getString(EngineerCode.stringIDs[position]);
		   
		   
		   holder.ItemText.setText(key);
//			if(prefs.getBoolean(key,false)){
//			holder.ItemResult.setText("TRUE");
//
//			}else{
//			holder.ItemResult.setText("FALSE");
//			}
           holder.ItemResult.setText(prefs.getString(key,"NOT_TEST"));
		   return view;
		   
       }
	   //优化一下ListView，避免重复加载。
	   class ViewHolder {
		   TextView ItemText;
		   TextView ItemResult;
	   }
	   
	}
	
	
}