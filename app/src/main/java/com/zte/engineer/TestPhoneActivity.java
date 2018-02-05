
package com.zte.engineer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class TestPhoneActivity extends Activity {
    
    public static final int RESULT_PASS = 10;
    public static final int RESULT_FALSE = 20;
    private Button bt_China_Mobile;
    private Button bt_China_Unicom;
    private Button bt_China_Telecom;
    private Button bt_others;
    private Button bt_pass;
    private Button bt_no;
    
    private TextView China_Mobile;
    private TextView China_Unicom;
    private TextView China_Telecom;
    private EditText others;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_telephone);
        
        bt_China_Mobile = (Button) findViewById(R.id.bt_China_Mobile);
        bt_China_Unicom = (Button) findViewById(R.id.bt_China_Unicom);
        bt_China_Telecom = (Button) findViewById(R.id.bt_China_Telecom);
        bt_others = (Button) findViewById(R.id.bt_others);
        bt_pass = (Button) findViewById(R.id.bt_pass);
        bt_pass.setVisibility(View.VISIBLE);
        bt_no = (Button) findViewById(R.id.bt_no);
        bt_no.setVisibility(View.VISIBLE);
        
        China_Mobile = (TextView) findViewById(R.id.China_Mobile);
        China_Unicom = (TextView) findViewById(R.id.China_Mobile);
        China_Telecom = (TextView) findViewById(R.id.China_Mobile);
        others = (EditText) findViewById(R.id.others);
        
        bt_China_Mobile.setOnClickListener(new Phone_Onclick());
        bt_China_Unicom.setOnClickListener(new Phone_Onclick());
        bt_China_Telecom.setOnClickListener(new Phone_Onclick());
        bt_others.setOnClickListener(new Phone_Onclick());
        
        bt_pass.setOnClickListener(new Phone_Onclick());
        bt_no.setOnClickListener(new Phone_Onclick());
        
    }
    
    class Phone_Onclick implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
                switch (id) {
                    case R.id.bt_China_Mobile:
                        Uri uri_m = Uri.parse("tel:" + getResources().getString(R.string.China_Mobile_num)); 
                        Intent it_c = new Intent("android.intent.action.CALL", uri_m);   
                        startActivity(it_c); 
                        break;
                    case R.id.bt_China_Unicom:
                        Uri uri_u = Uri.parse("tel:" + getResources().getString(R.string.China_Unicom_num)); 
                        Intent it_u = new Intent(Intent.ACTION_CALL, uri_u);   
                        startActivity(it_u);
                        break;
                    case R.id.bt_China_Telecom:
                        Uri uri_t = Uri.parse("tel:" + getResources().getString(R.string.China_Telecom_num)); 
                        Intent it_t = new Intent(Intent.ACTION_CALL, uri_t);   
                        startActivity(it_t);
                        break;
                    case R.id.bt_others:
                        String num = others.getText().toString();
                        if(num != null && num.length() >=3){
                            Uri uri_o = Uri.parse("tel:" + num); 
                            Intent it_o = new Intent(Intent.ACTION_CALL, uri_o);   
                            startActivity(it_o);
                        }else{
                            Toast.makeText(TestPhoneActivity.this, R.string.note_phone, 1).show();
                        }
                        break;
                    case R.id.bt_pass:
                        //finishSelf(RESULT_PASS);
						finish();
                        break;
                    case R.id.bt_no:
                        //finishSelf(RESULT_PASS);
						finish();
                        break;
                    default:
                        break;
                }
        }
        
    }
    
    public void finishSelf(int result)
    {
        setResult(result);
        finish();
    }

}
