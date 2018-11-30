package com.zte.engineer;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BoardCode extends Activity {
    private static final String TAG = "BoardCode";
    private final boolean LOG = false;
	long starttime = 0;
	long endtime = 0;
    int flag = 0;//default calibration flag. no calibration

    private Button pass, fail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alextao_boardcode);
        setTitle(getString(R.string.board_code));
        /*
         * IBinder binder= ServiceManager.getService("NvRAMAgent"); NvRAMAgent
		 * agent = NvRAMAgent.Stub.asInterface(binder); int file_lid=25;//The
		 * lid of AP_CFG_REEB_PRODUCT_INFO_LID is 25 byte[] buff = null; try {
		 * buff = agent.readFile(file_lid);//read buffer from
		 * AP_CFG_REEB_PRODUCT_INFO_LID } catch (RemoteException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } StringBuffer
		 * boardCode = new StringBuffer();
		 * boardCode.append(isLetterOrNumber(buff[60])?buff[60]:"0"+" ");
		 * boardCode.append(isLetterOrNumber(buff[61])?buff[61]:"0"+"\n");
		 * boardCode.append(isLetterOrNumber(buff[62])?buff[62]:"0");
		 */
        TextView text = (TextView) findViewById(R.id.text);
        pass = (Button) findViewById(R.id.pass_btn);
        fail = (Button) findViewById(R.id.fail_btn);
        pass.setEnabled(false);

        text.setTextSize(20);
        //TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //String barcode = null;
        //barcode = TelephonyManager.getDefault().getSN();
        //Phone mPhone = PhoneFactory.getDefaultPhone();
        //String barcode = mPhone.getSN();
        String barcode = SystemProperties.get("gsm.serial");
		char a = barcode.length() < 63 ? '0' : barcode.charAt(62);
		char b = barcode.length() < 62 ? '0' : barcode.charAt(61);
		char c = barcode.length() < 61 ? '0' : barcode.charAt(60);
		char d = barcode.length() < 58 ? '0' : barcode.charAt(57);
		char e = barcode.length() < 59 ? '0' : barcode.charAt(58);
		char f = barcode.length() < 51 ? '0' : barcode.charAt(50);
        String boardcode = barcode.length() < 16 ? barcode : barcode.substring(0, 16);
		StringBuffer boardCode = new StringBuffer();
        boardCode.append("RF Status : \n");
		boardCode.append((isLetterOrNumber(c) ? c : "0") + " ");
		boardCode.append((isLetterOrNumber(b) ? b : "0") + "\n");
        if (c == '0' && b == '0') {
            boardCode.append("No calibration\n");
        } else if (b == '1' && c == '0') {
            flag = 1;//calibration fail
            boardCode.append("FAIL\n");
        } else if (c == '1' && b == '0') {
            flag = 2;//calibration success.
            pass.setEnabled(true);
            boardCode.append("PASS\n");
        }

        pass.setOnClickListener(mClickListener);
        fail.setOnClickListener(mClickListener);
        //boardCode.append((isLetterOrNumber(a) ? a : "0") + "\n");
        //boardCode.append("SW Version Status : \n");
        //boardCode.append((isLetterOrNumber(f) ? f : "0") + "\n");
		//boardCode.append("Wifi Coupling Status : \n");
		//boardCode.append((isLetterOrNumber(d) ? d : "0") + "\n");
		//boardCode.append("RF Coupling Status : \n");
		//boardCode.append((isLetterOrNumber(e) ? e : "0") + "\n");

        //add by jiangxiaowei for read product info-reserved data start
        
      /*  IBinder binder = ServiceManager.getService("NvRAMAgent");
        NvRAMAgent agent = NvRAMAgent.Stub.asInterface(binder);
		starttime =System.currentTimeMillis();
        int file_lid = 36;//AP_CFG_REEB_PRODUCT_INFO_LID is 36
        byte[] buff = null;
		
        try {
            buff = agent.readFile(file_lid);// read buffer from nvram
        } catch (RemoteException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

		endtime=System.currentTimeMillis();*/
    /*
		android.util.Log.i("dengyanjun", " AAAAA boardCode runtime="+(endtime-starttime));
        int rf_locate = 206;
        int wifi_locate = 211;
        int gps_locate = 212;
        if(buff != null) {
			//String tembuf = new String(buff);
			//android.util.Log.i("dengyanjun", " boardCode tembuf="+tembuf);
            if(LOG) {
                for(int i = 0; i < buff.length; i++) {
                    byte be = buff[i];
                    String s = "" +  (byte) ((be >> 7) & 0x1) + (byte) ((be >> 6) & 0x1)
                        + (byte) ((be >> 5) & 0x1) + (byte) ((be >> 4) & 0x1)
                        + (byte) ((be >> 3) & 0x1) + (byte) ((be >> 2) & 0x1)
                        + (byte) ((be >> 1) & 0x1) + (byte) ((be >> 0) & 0x1);
                    android.util.Log.d(TAG, "product info servered i=" + i + " data="+s);
                }
            }
        }

        boardCode.append("ZTE Intelligent Platform RF CPT Status : \n");
        if(buff != null) {
            if(buff.length >= rf_locate + 1) {
                byte be = buff[rf_locate];// wifi cpt is 3/2 bit
                boardCode.append(String.valueOf((byte) ((be >> 3) & 0x1)));
                boardCode.append(String.valueOf((byte) ((be >> 2) & 0x1)) + "\n");
            }
        }
		
		
        boardCode.append("ZTE Intelligent Platform Wifi CPT Status : \n");
        if(buff != null) {
            if(buff.length >= wifi_locate + 1) {
                byte be = buff[wifi_locate];// wifi cpt is 3/2 bit
                boardCode.append(String.valueOf((byte) ((be >> 3) & 0x1)));
                boardCode.append(String.valueOf((byte) ((be >> 2) & 0x1)) + "\n");
            }
        }

        boardCode.append("ZTE Intelligent Platform GPS CPT Status : \n");
        if(buff != null) {
            if(buff.length >= gps_locate + 1) {
                byte be = buff[gps_locate];// wifi cpt is 5/4 bit
                boardCode.append(String.valueOf((byte) ((be >> 5) & 0x1)));
                boardCode.append(String.valueOf((byte) ((be >> 4) & 0x1)) + "\n");
            }
        }
        */
        //end
        // starttime =System.currentTimeMillis();
        //	android.util.Log.i("dengyanjun", " DDDDDD boardCode runtime="+(starttime-endtime));
        //boardCode.append(boardcode);
        text.setText(boardCode.toString());


	}

	private boolean isLetterOrNumber(char b) {
        return (b <= 'Z' && b >= 'A') || (b >= '0' && b <= '9') || (b <= 'z' && b >= 'a');
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
      switch (v.getId()){
          case R.id.pass_btn:
              setResult(10);
              finish();
              break;
          case R.id.fail_btn:
              setResult(20);
              finish();
              break;
      }
        }
    };

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: "+flag);
        switch (flag) {
            case 1:
                setResult(ZteActivity.RESULT_FALSE);
                break;
            case 2:
                setResult(ZteActivity.RESULT_PASS);
                break;
            default:
                setResult(ZteActivity.RESULT_FALSE);
                break;
	}
        super.onBackPressed();
    }
}
