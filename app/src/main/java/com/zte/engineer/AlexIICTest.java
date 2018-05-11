package com.zte.engineer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newmobi.iic.IICHelper;

/**
 * Created by Administrator on 2018/2/6.
 *
 * @author NewMobi Alextao
 */

public class AlexIICTest extends Activity implements View.OnClickListener {
    private Button iic1, iic2, iic3;
    private Button passBtn, failBtn;

    private TextView iic1_ret;
    private TextView iic2_ret;
    private TextView iic3_ret;

    int read_data = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_iic_test);

        initWidget();

       if(IICHelper.openIIC() < 0)
          Toast.makeText(this, "can not open serial port 1", Toast.LENGTH_SHORT).show();
       else
           Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();



    }

    /**
     * here init the view and bind the onclick listeners.
     */
    private void initWidget() {
        iic1 = (Button) findViewById(R.id.iic1);
        iic1.setOnClickListener(this);
        iic2 = (Button) findViewById(R.id.iic2);
        iic2.setOnClickListener(this);
        iic3 = (Button) findViewById(R.id.iic3);
        iic3.setOnClickListener(this);
        passBtn = (Button) findViewById(R.id.iic_pass);
        passBtn.setOnClickListener(this);
        failBtn = (Button) findViewById(R.id.iic_fail);
        failBtn.setOnClickListener(this);

        iic3_ret = (TextView) findViewById(R.id.iic3_ret);
        iic1_ret = (TextView) findViewById(R.id.iic1_ret);
        iic2_ret = (TextView) findViewById(R.id.iic2_ret);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iic1:
            case R.id.iic2:
                IICHelper.writeIIC(2,0,1 );
                read_data = IICHelper.readIIC(2,1);
                if(read_data == 249) {
//SUCCESS
                    iic1_ret.setText(getResources().getString(R.string.pass));
                    iic2_ret.setText(getResources().getString(R.string.pass));
                    iic1_ret.setTextColor(Color.GREEN);
                    iic2_ret.setTextColor(Color.GREEN);
                } else {
//FAIL
                    iic1_ret.setText(getResources().getString(R.string.fail));
                    iic2_ret.setText(getResources().getString(R.string.fail));
                    iic1_ret.setTextColor(Color.RED);
                    iic2_ret.setTextColor(Color.RED);
                }
                 break;
            case R.id.iic3:
                IICHelper.writeIIC(3,0,1 );
                read_data = IICHelper.readIIC(3,1);
                if(read_data == 249) {
//SUCCESS
                    iic3_ret.setText(getResources().getString(R.string.pass));
                    iic3_ret.setTextColor(Color.GREEN);
                } else {
//FAIL
                    iic3_ret.setText(getResources().getString(R.string.fail));
                    iic3_ret.setTextColor(Color.RED);
                }
                break;

            case R.id.iic_pass:
                setResult(10);
                finish();
                break;
            case R.id.iic_fail:
                setResult(20);
                finish();
                break;
        }
    }


}
