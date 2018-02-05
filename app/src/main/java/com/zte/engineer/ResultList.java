package com.zte.engineer;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ResultList extends ZteActivity{
	private ListView listView;
	private ArrayList<String> result =  new ArrayList<String>();
	private ArrayList<Integer> reCode =  new ArrayList<Integer>();
	private TextView totel;
	private TextView totel_pass;
	private TextView totel_fail;
	private static final String TAG = "ResultList";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_list);
        listView = (ListView) findViewById(R.id.resultlist);
        totel = (TextView) findViewById(R.id.totel);
        totel_pass = (TextView) findViewById(R.id.totelpass);
        totel_fail = (TextView) findViewById(R.id.totelfail);
        
        totel.setTextColor(Color.WHITE);
        totel_pass.setTextColor(Color.GREEN);
        totel_fail.setTextColor(Color.RED);   
        Intent intent = getIntent();
        result = intent.getStringArrayListExtra("result");
        reCode = intent.getIntegerArrayListExtra("recode");
        
        totel.setText(getResources().getString(R.string.totel)+reCode.size());
        totel_pass.setText(getResources().getString(R.string.totel_pass)+getPassNum(reCode));
        totel_fail.setText(getResources().getString(R.string.totel_fail)+(reCode.size() - getPassNum(reCode)));
        showLog();
		//add hexs start
		int fails = reCode.size() - getPassNum(reCode);
		Log.d(TAG,"hexs12 ------> fails ="+fails);
		if (fails==0){
		Log.d(TAG,"hexs12 ------> write pass flag");
		//writePassFlag(); //aeon lee block this
		}else{
		Log.d(TAG,"hexs12 ------> write fail flag");
		//writeFailFlag(); //aeon lee block this
		}
		//add hexs end
        ResultAdapter adapter = new ResultAdapter(this, result,reCode);
        listView.setAdapter(adapter);       
    }
	
	//hexs add start
    public static void writePassFlag() {
        NVRamUtil.writeFlag(1,104);
    }
		   
    public static void writeFailFlag() {
        NVRamUtil.writeFlag(0,104);
    }
    //hexs add end
	
    private void showLog(){
    	for (int i = 0; i < reCode.size(); i++) {
			Log.e("brice", "----->"+result.get(i));
			Log.e("brice", "!!!!!>"+reCode.get(i));
		}
    }
    
    private int getPassNum(ArrayList<Integer> code){
    	int num = 0;
    	for (int i = 0; i < code.size(); i++) {
			if (code.get(i) == 10) {
				num++;
			}
		}
    	return num;
    }
    
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}


	@Override
	public void onClick(View arg0) {
		
	}

}
