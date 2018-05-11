package com.zte.engineer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProduceInfoListView extends Activity {

    private static final String TAG = "ProduceInfoListView";
    private ListView listView = null;
    private Resources r = null;
    private int[] stringsIDs = null;
    private static final int SOFTWARE_VERSION = 0, HARDWARE_VERSION = 1,
            PSN_CODE = 2, FLAG = 3, BOARD_SERIAL = 4, BLUETOOTH_ADDRESS = 5,
            FLASH_TYPE = 6, SMSSECURITY = 7;

    private String[] infos = {"", "", "", "", "", "", "", ""};

    private static final String VALUE_ID = "name";
    private static final String VALUE_FLASHSIZE = "total size";

    private static final String PATH = "/proc/driver/";
    private static final String FILENAME = "nand";

    private BluetoothAdapter mBluetooth = null;
    private MyAdapter myAdapter = null;
    private boolean isManualTurnOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initStringList();
//        mBluetooth = BluetoothAdapter.getDefaultAdapter();
//        if (!mBluetooth.isEnabled()) {
//            mBluetooth.enable();
//            isManualTurnOn = true;
//        }
        IntentFilter filter = new IntentFilter(
                BluetoothAdapter.ACTION_STATE_CHANGED);
        //registerReceiver(mBluetoothReceiver, filter);

        r = getResources();
        getInfos();
        listView = new ListView(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        myAdapter = new MyAdapter(getBaseContext());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new MyOnItemClickListener());
        setContentView(listView);

    }

    private void initStringList() {
        if (hasSmsSecurity()) {
            stringsIDs = new int[]{R.string.software_version//,
                    //	R.string.hardware_version, R.string.PSN_code,
                    //	R.string.flag, R.string.Board_serial,
                    //	R.string.Bluetooth_address, R.string.flash_type,
                    //	R.string.smssecurity
            };
        } else {
            stringsIDs = new int[]{R.string.software_version//,
                    //R.string.hardware_version, R.string.PSN_code,
                    //	R.string.flag, R.string.Board_serial,
                    //	R.string.Bluetooth_address, R.string.flash_type
            };
        }
    }

    private boolean hasSmsSecurity() {
        PackageInfo sms = null;
        try {
            sms = getPackageManager().getPackageInfo("com.zte.smssecurity", 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null == sms ? false : true;
    }

    private class MyOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            switch (stringsIDs[position]) {
                case R.string.smssecurity:
                    intent.setAction("com.zte.smssecurity.action.SMS_SECURITY_SETTING");
                    ProduceInfoListView.this.startActivity(intent);
                    break;
                case R.string.flag:
                    intent.putExtra("flag", infos[FLAG]);
                    intent.setClass(ProduceInfoListView.this, TestFlag.class);
                    // ProduceInfoListView.this.startActivity(intent);
                    break;
            }
        }
    }

    private void getInfos() {
        /*
		 * IBinder binder= ServiceManager.getService("NvRAMAgent"); NvRAMAgent
		 * agent = NvRAMAgent.Stub.asInterface(binder); int file_lid=25;//The
		 * lid of AP_CFG_REEB_PRODUCT_INFO_LID is 25 byte[] buff = null; try {
		 * buff = agent.readFile(file_lid);//read buffer from
		 * AP_CFG_REEB_PRODUCT_INFO_LID } catch (RemoteException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

        infos[SOFTWARE_VERSION] = Build.DISPLAY;
		/*
		infos[HARDWARE_VERSION] = "WMBX";
		infos[PSN_CODE] = ((TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		infos[FLAG] = "";// new String(buff,103,13);
		//String SN = TelephonyManager.getDefault().getSN();
		//Phone mPhone = PhoneFactory.getDefaultPhone();
		//String SN = mPhone.getSN();
        String SN = SystemProperties.get("gsm.serial");
		SN = SN.length() > 16 ? SN.substring(0, 16) : SN;
		StringBuffer stringBuff = new StringBuffer();
		for (char c : SN.toCharArray()) {
			stringBuff.append(c);
		}
		infos[BOARD_SERIAL] = stringBuff.toString();// SystemProperties.get("ro.serialno");
		infos[BLUETOOTH_ADDRESS] = mBluetooth.getAddress();
		File file = new File(PATH, FILENAME);
		try {
			if (file.isFile() && file.canRead()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String buf = null;
				while (null != (buf = reader.readLine())) {
					Log.d(TAG, buf);
					for (String s : buf.split(",")) {
						s = s.trim();
						String sLowerCase = s.toLowerCase();
						try {
							if (sLowerCase.startsWith(VALUE_ID)) {
								infos[FLASH_TYPE] = s.split(":")[1].trim();
							} else if (sLowerCase.startsWith(VALUE_FLASHSIZE)) {

							}
							Log.d(TAG, s);
						} catch (ArrayIndexOutOfBoundsException e) {
							Log.d(TAG, e.toString());
						}
					}
				}
			}
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}
		*/

    }

    @Override
    public void onDestroy() {
      //  unregisterReceiver(mBluetoothReceiver);
        //if (true == isManualTurnOn) {
        //    mBluetooth.disable();
        //}
        super.onDestroy();
    }

    protected BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (BluetoothAdapter.STATE_ON == intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    infos[BLUETOOTH_ADDRESS] = BluetoothAdapter
                            .getDefaultAdapter().getAddress();
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(10);
        finish();
        super.onBackPressed();

    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Context mContext;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return stringsIDs.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.produce_info, null);
            }
            ((TextView) convertView.findViewById(R.id.produce_text_title))
                    .setText(r.getString(stringsIDs[position]));
            ((TextView) convertView.findViewById(R.id.produce_text))
                    .setText(infos[position]);
            return convertView;
        }
    }
}
