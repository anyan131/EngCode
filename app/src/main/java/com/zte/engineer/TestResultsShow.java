package com.zte.engineer;


import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.os.Bundle;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class TestResultsShow extends Activity implements View.OnClickListener{

    TextView mLastTestResult;
	Button clearButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
    	super.onCreate(savedInstanceState);

    	setContentView(R.layout.results_flag);
		mLastTestResult = (TextView)findViewById(R.id.all_results_show);
		clearButton = (Button)findViewById(R.id.clear_result);
		clearButton.setOnClickListener(this);
	}
	
	 @Override
	protected void onResume() {	
        super.onResume();
		mLastTestResult.setText(getText());
	}
	
	@Override
	public void onClick(View v){
	    mLastTestResult.setText(getTextById(R.string.no_action));
	    Util.saveTestResult(this, "result", "2");
		setResult(EngineerCode.RESULT_CLEAR_ALL);
		finish();
	};
	
	private String getText(){
	    String result = Util.getLastTestResult(this);
	    if("1".equals(result)){
		    return getTextById(R.string.all_passed);
		}else if("0".equals(result)){
		    return getTextById(R.string.partial_passed);
		}else{
		    return getTextById(R.string.no_action);
		}
	  }
	
	private String getTextById(int strId){
	    return getResources().getString(strId);
	}

}
