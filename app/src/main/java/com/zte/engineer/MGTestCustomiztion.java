package com.zte.engineer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

public class MGTestCustomiztion extends Activity implements OnClickListener{
	private String[] mImageIds;
	private Button handBtn;
	private Button autoBtn;
	private GridView gridview;
	public static final String ACTION_SELF_TEST = "com.zte.engineer.action.MG_SELF_TEST";
	public static final String ACTION_LAUNCHER_TEST_LIST = "com.zte.engineer.action.TEST_LIST";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mg_test);
        mImageIds = getResources().getStringArray(R.array.bt_name);
        gridview = (GridView) findViewById(R.id.gridview);
        handBtn = (Button) findViewById(R.id.handtest);
        autoBtn = (Button) findViewById(R.id.autotest);
        handBtn.setOnClickListener(this);
        autoBtn.setOnClickListener(this);
        showLog();
      	gridview.setAdapter(new ItemAdapter(this,mImageIds));
        gridview.setVisibility(View.GONE);
    }
    
    private void showLog(){
    	Log.e("brice", "----->"+mImageIds.length);
    	for (int i = 0; i < mImageIds.length; i++) {
			Log.e("brice", "----->"+mImageIds[i]);
		}
    }
    
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.handtest:
			Intent intent = new Intent(ACTION_LAUNCHER_TEST_LIST);
			startActivity(intent);
                        finish();
			break;
        case R.id.autotest:
        	gridview.setVisibility(View.GONE);
        	Intent in = new Intent(ACTION_SELF_TEST);
			startActivity(in);	
			finish();
			break;
		default:
			break;
		}
	}
}
