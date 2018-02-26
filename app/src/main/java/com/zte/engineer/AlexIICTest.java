package com.zte.engineer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.newmobi.iic.IICHelper;

/**
 * Created by Administrator on 2018/2/6.
 *
 * @author NewMobi Alextao
 */

public class AlexIICTest extends Activity implements View.OnClickListener {
    private Button iic1, iic2, iic3;
    private Button passBtn, failBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_iic_test);

        initWidget();

        IICHelper.openIIC();


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
    }

    @Override
    public void onClick(View v) {

    }


}
