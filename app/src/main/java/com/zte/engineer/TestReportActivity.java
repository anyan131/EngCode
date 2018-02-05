package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.os.Bundle;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.res.Resources;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;
import android.content.SharedPreferences;

/**
*here I add an activity for show the test results.
*Add by alextao1207.
*
*/


public class TestReportActivity extends Activity /*implements View.OnClickListener*/{
	
	private static final String TAG = "TestReportActivity";
	private ListView mResultList;
	private int itemCount;
	private ItemResult[] mItemResults;
	private int requestCode;
	private int resultCode;
	private static final String RESULT_STATE = "RESULT_STATE";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.test_report);
		Log.i(TAG,"onCreate:");
		mResultList = (ListView)findViewById(R.id.listView_testResult);
		itemCount = EngineerCode.stringIDs.length;
		mItemResults = new ItemResult[itemCount];
		Resources resource = getResources();
		ArrayList<ItemReportSave> irsList = null;
		if(savedInstanceState == null) 
			Log.i(TAG,"NULL savedInstanceState");
		if(null != savedInstanceState){
			Log.i(TAG,"START TAKE DATA FROM BUNDLE");
			irsList = savedInstanceState.getParcelableArrayList(RESULT_STATE);
		}
		if(null != irsList){
			String title = null;
			String result = null;
			for(int i = 0; i < itemCount; i++){
				title = resource.getString(EngineerCode.stringIDs[i]);
				for(Object o:irsList.toArray()){
					if(((ItemReportSave)o).title.equals(title)){
						result = ((ItemReportSave)o).result;
					}
				}
				mItemResults[i] = new ItemResult(title,result);
			}
		}else{
			for(int i=0; i<itemCount;i++){
			mItemResults[i] = new ItemResult(resource.getString(EngineerCode.stringIDs[i]),TestResult.NOT_TEST);
			}
		}
		Log.i(TAG,"start getting intent");
		
		
		
		/**
		*考虑使用Intent来更新列表项。
		*之前默认用NOT_TEST来填充每一项。
		*从Intent取得相对应的结果，因为在父类的{@link onActivityResult}方法中
		*已经取得了相应的结果，requestCode是每一项的ID，而resultCode是相对应的结果。
		*备注：RESULT_PASS = 10(=> TestResult.PASS), RESULT_FALSE = 20(=>TestResult.FAIL)
		*/
		Intent i = getIntent();
		/**
		*此处的Intent应该携带两个参数。
		*第一个参数为requestCode,用来找到对应的项，
		*第二个参数为resultCode,用来设置对应项的结果。
		*/
		requestCode = i.getIntExtra("requestCode",0);//默认值给0
		resultCode = i.getIntExtra("resultCode",0);//默认值给0
		if(requestCode != 0){
			if(resultCode == 10 ){
				mItemResults[requestCode].setResult(TestResult.PASS);
			}else if(resultCode == 20){
				mItemResults[requestCode].setResult(TestResult.FAIL);
			}
		}
		
		
		AlexAdapter adapter = new AlexAdapter(this,R.layout.test_result_list,R.id.testItemText,R.id.testItemResult,mItemResults);
		mResultList.setAdapter(adapter);
	}
	
	
	@Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
    }
	/**
	*这个方法用来保存数据。当退出这个界面时，
	*相关的数据必须得到保存，否则影响用户体验。
	*应当保存数据有：
	*	
	*	1.对应的项目的值。
	*/
	@Override
    protected void onSaveInstanceState(Bundle outState) {
		
		Log.i(TAG,"onSaveInstanceState");
		ArrayList<ItemReportSave> resultArray = new ArrayList<ItemReportSave>();
		for(ItemResult ir : mItemResults){
			resultArray.add(new ItemReportSave(ir.getTitle(),ir.getResult().toString()));
		}
		outState.putParcelableArrayList(RESULT_STATE,resultArray);
		Log.i(TAG,"saving data");
        super.onSaveInstanceState(outState);
    }
	
	
	
	
	//this is for the test item.
	private class AlexAdapter extends BaseAdapter{
		
		private Context mContext;
    	private int layout;
    	private int itemTextID;
    	private int resultTextID;
    	private ItemResult[] itemResults;
    	LayoutInflater mInflater;
		
		public AlexAdapter(Context context,int layout,int itemTextID,int resultTextID,ItemResult[] itemResults){
			mInflater = LayoutInflater.from(context);
			this.layout = layout;
			this.itemTextID = itemTextID;
			this.itemResults = itemResults;
		
		}

       @Override
       public int getCount() {
           return itemResults.length;
       }

       @Override
       public Object getItem(int position) {
           return itemResults[position];
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
		   holder.ItemText.setText(itemResults[position].getTitle());
		   holder.ItemResult.setText(itemResults[position].getResult().toString());
		   return view;
		   //if(convertView == null){
			//   convertView = mInflater.inflate(layout,null);
		   //}
		   //TextView ItemText = (TextView)convertView.findViewById(R.id.testItemText);
		   //TextView ItemResult = (TextView)convertView.findViewById(R.id.testItemResult);
		   //ItemText.setText(itemResults[position].getTitle());
		   //ItemResult.setText(itemResults[position].getResult().toString());
		   //return convertView;
       }
	   //优化一下ListView，避免重复加载。
	   class ViewHolder {
		   TextView ItemText;
		   TextView ItemResult;
	   }
	   
	}
	   
	   
	   
	//describe a single item result.

	/**
	*@author Alextao
	*@param title
	*@param result
	*/
	 private class ItemResult{
        private String title;
        private TestResult result;
		private String resultS;

       public ItemResult(String title, TestResult result) {
           this.title = title;
           this.result = result;
       }
	   
	   public ItemResult(String title,String result){
			this.title = title;
			this.resultS = result;
	   }
	   
	   

       public String getTitle() {
           return title;
       }

       public void setTitle(String title) {
           this.title = title;
       }

       public TestResult getResult() {
           return result;
       }

       public void setResult(TestResult result) {
           this.result = result;
       }
	   public String getResultS(){
		   return resultS;
	   }
	   public void setResultS(String resultS){
		   this.resultS = resultS;
	   }
   }
	




}