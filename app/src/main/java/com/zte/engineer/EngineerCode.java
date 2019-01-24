package com.zte.engineer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.newmobi.iic.TestSign;
//import com.squareup.leakcanary.LeakCanary;
import com.zte.engineer.CommitReportUtils.CommitUtils;
import com.zte.engineer.CommitReportUtils.Constants;
import com.zte.engineer.CommitReportUtils.StringUtils;

import java.util.ArrayList;


public class EngineerCode extends Activity {

    public static final int RESULT_PASS = 10;
    public static final int RESULT_FALSE = 20;
    public static final String STATE = "StateArrayList";
    public static final String AUTOTEST = "AUTOTEST";
    public static final String NORMALTEST = "NORMALTEST";
    //minjibing
    private static final String EXTRAS_CAMERA_FACING =
            "android.intent.extras.CAMERA_FACING";

    //it's a opportunistic way, don not recommended.
    private static boolean isAllPassed = false;
    private static String TEST_RESULT = "result";
    private Button fcButton;
    private Button btn_checkReport;
    private Button mCommitReportBt;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static final int REQUEST_SHOW_RESULT = 1000;
    public static final int RESULT_CLEAR_ALL = 1;

    private static final String TAG = "EngineerCode";
    final Intent intentToTestReportActivity = new Intent();
    private Context mComtext;

    public static final int[] stringIDs =
            {
                    R.string.software_version,
                    //R.string.battery_info,
                    //R.string.gpio_test,
                    //R.string.lcd,
                    R.string.backlight,
                    //R.string.touchpanel,
                    R.string.camera_front,
                    R.string.camera_back,
                    //R.string.key_test,
                    //R.string.vibrator,
                    R.string.ringer,
                    R.string.audio_loop,
                    R.string.earphone_audio_loop,
                    R.string.audio_receiver_new,
                    R.string.SIM,
                    R.string.imei,
                    R.string.serial_number,
                    R.string.sd_info,
                    R.string.bt_address,
                    R.string.wifi_address,
					R.string.g_sensor,
                    R.string.gyroscope_sensor,
                    R.string.m_sensor,
                    R.string.l_sensor,
                    R.string.p_sensor,
                    R.string.usb_camera,
                    R.string.ethernet,
                    //R.string.NM_fm_test,

                    R.string.serial_port,
                    R.string.NM_gps_test,
                    R.string.NM_i2c_test,
                    R.string.led_test,                    
                    R.string.board_code,
                    //alextao add for led test.



                    //R.string.audio_receiver,
                    //R.string.audio_receiver_new,
                    //R.string.lcd_off,
                    //R.string.memory,
                    //R.string.bt_detect,
                    //R.string.wifi_detect,
                    //R.string.radio_info,
                    //R.string.fm_test,
                    //R.string.imsi,
                    //R.string.sensor,
                    //R.string.g_sensor,
                    //R.string.p_sensor,
                    //FN0001234:Added by shihaijun
                    //R.string.camera,

                    //End for FN0001234
                    //R.string.gps,
                    //R.string.flash
            };

    ListView list;
    MyAdapter adapter;
    ItemContent[] items;

    final int testCount = stringIDs.length;
    int testCompleted;
    private boolean autoTest = false;
    private boolean startAuto = false;
    private boolean autoTestFactory = false;
    private int nowPosition = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComtext = this;
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(getApplication());

        Log.d("test", "onCreate");
        if (null != getIntent().getStringExtra(AUTOTEST)) {
            autoTest = true;
            autoTestFactory = true;
        }
        setContentView(R.layout.main);
        Resources r = getResources();
        prefs = getSharedPreferences("engineer", MODE_MULTI_PROCESS);
        editor = prefs.edit();
        initButton();
        list = (ListView) findViewById(R.id.MainList);


        testCompleted = 0;
        items = new ItemContent[testCount];

        ArrayList<SaveItems> itemState = null;
        if (null != savedInstanceState) {
            Log.i("alextao", "resume data");
            itemState = savedInstanceState.getParcelableArrayList(STATE);
        }
        if (null != itemState) {
            boolean checked = false;
            boolean pass = false;
            String title = null;
            for (int i = 0; i < testCount; i++) {
                checked = false;
                pass = false;
                title = r.getString(stringIDs[i]);
                for (Object o : itemState.toArray()) {
                    if (((SaveItems) o).title.equals(title)) {
                        checked = ((SaveItems) o).checked;
                        pass = ((SaveItems) o).pass;
                    }
                }
                items[i] = new ItemContent(title, checked, pass);
            }
        } else {
            for (int i = 0; i < testCount; i++) {
                items[i] = new ItemContent(r.getString(stringIDs[i]), false, false);
            }
        }
        isFirst();

        adapter = new MyAdapter(this, R.layout.checkmark_list,
                R.id.text, R.id.checkbox, items);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                implementItemClick(position);
            }
        };
        list.setOnItemClickListener(mOnItemClickListener);

    }

    private void implementItemClick(int position) {
        Intent intent = new Intent();
        nowPosition = position;

        switch (stringIDs[position]) {
            case R.string.battery_info: {
                intent.setClass(this, BatteryLog.class);
            }
            break;
            case R.string.touchpanel: {
                intent.setClass(this, TouchScreenTest.class);
            }
            break;

            case R.string.SIM: {
                intent.setClass(this, SIMTest.class);
            }
            break;

            case R.string.lcd: {
                intent.setClass(this, LcdTestActivity.class);
            }
            break;

            case R.string.vibrator: {
                intent.setClass(this, VibratorTest.class);
            }
            break;

            case R.string.backlight: {
                intent.setClass(this, BacklightTest.class);
            }
            break;

            case R.string.ringer: {
                intent.setClass(this, RingerTest.class);
            }
            break;

            case R.string.key_test: {
                intent.setClass(this, KeyTest.class);
            }
            break;

            case R.string.lcd_off: {
                intent.setClass(this, LcdOffTest.class);
            }
            break;

            case R.string.sd_info: {
                intent.setClass(this, SDcardTest.class);
            }
            break;

            case R.string.memory: {
                intent.setClass(this, MemoryTest.class);
            }
            break;

            case R.string.bt_address: {
                intent.setClass(this, BTAddressTest.class);
            }
            break;
            case R.string.bt_detect: {
                intent.setClassName(Launcher.BLUETOOTH_SETTINGS_PACKAGE, Launcher.BLUETOOTH_SETTINGS_CLASS);
            }
            break;
            case R.string.wifi_address: {
                //intent.setClass(this, WifiAddressTest.class);
                intent.setClass(this, AlexWiFiTest.class);
            }
            break;
            case R.string.wifi_detect: {
                intent.setClassName(Launcher.WIFI_SETTINGS_PACKAGE, Launcher.WIFI_SETTINGS_CLASS);
            }
            break;
            /*case R.string.radio_info:
            {
    			intent.setClassName(Launcher.RADIO_INFO_PACKAGE, 
    								Launcher.RADIO_INFO_TARGET_CLASS);
    		}
    		break;*/

           /* case R.string.audio_receiver: {
                intent.setClass(this, ReciverTest.class);
            }
            break;*/

            case R.string.audio_loop: {
                intent.setClass(this, AudioLoopTest.class);
            }
            break;
            case R.string.earphone_audio_loop: {
                intent.setClass(this, EarPhoneAudioLoopTest.class);
            }
            break;
            case R.string.audio_receiver_new: {
                intent.setClass(this, ReciverTest.class);
            }
            break;			
            /*
        R.string.touchscreen,
    		 */
            case R.string.fm_test: {
                intent.setClass(this, FMTest.class);
            }
            break;

            case R.string.camera: {
                intent.setClassName(Launcher.CAMERA_PACKAGE,
                        Launcher.CAMERA_TARGET_CLASS);
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            break;

    		/*case R.string.camera_front:
            {
    			intent.setClassName(Launcher.CAMERA_PACKAGE, 
						Launcher.CAMERA_TARGET_CLASS);
					intent.putExtra("android.intent.extras.CAMERA_FACING", 1);   			
    		}
    		break;*/
            case R.string.camera_back: {
//    			intent.setClassName(Launcher.CAMERA_PACKAGE, Launcher.CAMERA_TARGET_CLASS);
//				intent.setAction("android.media.action.IMAGE_CAPTURE");
//				intent.setComponent(new ComponentName("com.android.gallery3d","com.android.camera.CameraLauncher"));
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra(EXTRAS_CAMERA_FACING, android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
//				Log.i("CAMAPP","__________should to boot the back camera ...");
                intent.setClass(this, AlexBackCameraTest.class);
            }
            break;

            case R.string.camera_front: {
//    			if(android.hardware.Camera.getNumberOfCameras()<=0 || SaveItems.hasFrontFacingCamera()){
//    				Toast.makeText(this, R.string.non_front_camera, Toast.LENGTH_SHORT).show();
//    				return;

//    			intent.setClassName(Launcher.CAMERA_PACKAGE, Launcher.CAMERA_TARGET_CLASS);
//    			intent.setAction(ACTION_FRONT_CAMERA_TEST);
//    			intent.setAction("android.media.action.IMAGE_CAPTURE");
//				intent.setComponent(new ComponentName("com.android.gallery3d","com.android.camera.CameraLauncher"));
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra(EXTRAS_CAMERA_FACING, android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
//    			Log.i("CAMAPP","__________should to boot the front camera ...");
                intent.setClass(this, AlexFrontCamera.class);
            }
            break;

            case R.string.imei: {
                intent.setClass(this, ImeiTest.class);
            }
            break;

            case R.string.serial_number: {
                intent.setClass(this, SNTest.class);
            }
            break;

            case R.string.imsi: {
                intent.setClass(this, ImsiTest.class);
            }
            break;

            case R.string.sensor: {
                intent.setClass(this, SensorTest.class);
            }
            break;

            case R.string.g_sensor: {
                intent.setClass(this, GSensorTest.class);
            }
            break;
            case R.string.gyroscope_sensor: {
                intent.setClass(this, GyroscopeSensorTest.class);
            }
            break;

            case R.string.m_sensor: {
                intent.setClass(this, MSensorTest.class);
            }
            break;

            case R.string.l_sensor: {
                intent.setClass(this, LSensorTest.class);
            }
            break;

            case R.string.p_sensor: {
                intent.setClass(this, PSensorTest.class);
            }
            break;

            case R.string.usb_camera: {
                intent.setClass(this, UsbCameraTestActivity.class);
            }
            break;

            case R.string.ethernet: {
                intent.setClass(this, EthernetTestActivity.class);
            }
            break;

            /*case R.string.gps: {
                intent.setClassName(Launcher.GPS_TEST_PACKAGE,
                        Launcher.GPS_TEST_TRAGET_CLASS);
            }
            break;*/
            case R.string.flash: {
                intent.setClass(this, FlashLightTest.class);
            }
            break;
            case R.string.software_version: //xu add
            {
                intent.setClass(this, ProduceInfoListView.class);
            }
            break;
            case R.string.board_code: //xu add
            {
                intent.setClass(this, BoardCode.class);
            }
            break;


            case R.string.gpio_test: //modified by newmobi alextao.
            {
                intent.setClass(this, GPIO.class);
            }
            break;

            case R.string.serial_port: //modified by newmobi alextao.
            {
                intent.setClass(this, SerialPort.class);
                break;
            }
            case R.string.NM_fm_test: {
                //modify by lzg
                //intent.setClass(this, FmAlexTaoActivity.class);
                intent.setClass(this, FMTest.class);
                //end lzg
            }
            break;
            case R.string.NM_gps_test: {
                //modify by lzg
                //intent.setClass(this, AlexNewGPSTest.class);
                intent.setClass(this, GPSTestActivity.class);
                //end lzg
            }
            break;
            case R.string.NM_i2c_test: {

                intent.setClass(this, AlexIICTest.class);

            }
            break;
            case R.string.led_test:
                intent.setClass(this,LedTest.class);
                break;
            default: {
                intent.setClass(this, NotSupportNotification.class);
                intent.putExtra("notification", getString(stringIDs[position]));
            }
        }

        startActivityForResult(intent, position);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCompletedNum();
        if (!startAuto) {
            startAuto = true;
            autoTest(-1);
        }
        //add by lzg
        if (prefs.getString(Constants.COMMIT_FAG, "2").equals("1")) {
            mCommitReportBt.setText(R.string.commit_success);
            mCommitReportBt.setTextColor(Color.BLACK);
        } else if(prefs.getString(Constants.COMMIT_FAG, "2").equals("0")){
            mCommitReportBt.setText(R.string.commit_fail);
            mCommitReportBt.setTextColor(Color.RED);
        }else{
            mCommitReportBt.setText(R.string.no_commit_report);
            mCommitReportBt.setTextColor(Color.RED);
        }
        //end lzg
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoTestFactory && !autoTest) {
            Intent intent = new Intent();
            intent.setClassName(Launcher.FACTORY_RESET_PACKAGE, Launcher.FACTORY_RESET_CLASS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        Util.saveTestResult(this, TEST_RESULT, (isAllPassed ? 1 : 0) + "");
    }

    private void updateCompletedNum() {
        TextView t = (TextView) findViewById(R.id.completed);
        String s = getString(R.string.completed);

        int count = items.length;
        int completed = 0;
        for (int i = 0; i < count; i++) {
            if (items[i].isChecked()) {
                completed++;
            }
        }

        s = s + completed + "/" + count;
        t.setText(s);
    }

    /**
     * requestCode is position of list, see @implementItemClick
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        android.util.Log.d("sukey", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        Resources res = getResources();
        intentToTestReportActivity.putExtra("requestCode", requestCode);
        intentToTestReportActivity.putExtra("resultCode", resultCode);
        if (REQUEST_SHOW_RESULT == requestCode) {
            if (RESULT_CLEAR_ALL == resultCode) {
                clearAll();
            }
            return;
        }
        if (resultCode == RESULT_PASS) {
            items[requestCode].setChecked(true);
            items[requestCode].setPassed(true);
            editor.putString(res.getString(stringIDs[requestCode]), "PASS");
            //add by lzg
            editor.putString(StringUtils.getTestItemName(requestCode),"1");
            //end lzg
            editor.commit();
            adapter.replaceItems(items);
            list.setAdapter(adapter);
            updateCompletedNum();
            list.setSelection(requestCode);
            autoTest(requestCode);
        } else if (resultCode == RESULT_FALSE) {
            items[requestCode].setChecked(true);
            items[requestCode].setPassed(false);
            editor.putString(res.getString(stringIDs[requestCode]), "FAIL");
            //add by lzg
            editor.putString(StringUtils.getTestItemName(requestCode),"0");
            //end lzg
            editor.commit();
            adapter.replaceItems(items);
            list.setAdapter(adapter);
            updateCompletedNum();
            list.setSelection(requestCode);
            autoTest(requestCode);
        } else {
            switch (stringIDs[requestCode]) {

                //case R.string.radio_info:
                case R.string.fm_test:
                case R.string.camera:
                case R.string.bt_detect:
                case R.string.wifi_detect:
                case R.string.camera_front:
                case R.string.camera_back:
                case R.string.board_code://xu
                case R.string.software_version://xu
                    //case R.string.gps:
                {
                    /*new AlertDialog.Builder(this)
                    .setTitle(stringIDs[requestCode])
					.setPositiveButton(R.string.pass, new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					})
					.setNegativeButton(R.string.falsed, new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							items[requestCode].setChecked(true);
							items[requestCode].setPassed(false);
					adapter.replaceItems(items);
					list.setAdapter(adapter);
					updateCompletedNum();
							autoTest(requestCode);
				}
					}).show();*/
                    items[requestCode].setChecked(true);
                    items[requestCode].setPassed(true);
                    editor.putString(res.getString(stringIDs[requestCode]), "FAIL");
                    //add by lzg
                    editor.putString(StringUtils.getTestItemName(requestCode),"0");
                    //end lzg
                    editor.commit();
                    adapter.replaceItems(items);
                    list.setAdapter(adapter);
                    updateCompletedNum();
                    list.setSelection(requestCode);
                    autoTest(requestCode);
                }
                break;

                default: {
                    Util.log(" ", "request:" + requestCode + ", result:" + resultCode);
                }
                break;
            }

        }
        updatePassedState();
    }

    public void autoTest(int requestCode) {
        if (autoTest && (requestCode + 1) < adapter.getCount() && (requestCode + 1) >= nowPosition) {
            implementItemClick(requestCode + 1);
        } else {
            autoTest = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        ArrayList<SaveItems> stateArray = new ArrayList<SaveItems>();
        for (ItemContent item : items) {
            stateArray.add(new SaveItems(item.isChecked(), item.isPassed(), item.getTitle()));
        }
        outState.putParcelableArrayList(STATE, stateArray);
        Log.i("alextao", "EngineerCode saving data");
        super.onSaveInstanceState(outState);
    }

    //check if all items was passed. fengxiaojian,2014.09.15
    private void updatePassedState() {
        int count = items.length;
        for (int i = 0; i < count; i++) {
            if (!items[i].isPassed()) {
                isAllPassed = false;
                doSave();

                //NVRamUtil.writeFlag(0,104);//aeon lee block this
                return;
            }
        }
        isAllPassed = true;
        doSave();

        //NVRamUtil.writeFlag(1,104);//aeon lee block this
        Log.d(TAG, "hexs1 add writeflag OK");

    }
    //end

    private void doSave() {
        android.util.Log.d("sukey", "isAllPassed = " + isAllPassed);
        Util.saveTestResult(this, TEST_RESULT, (isAllPassed ? 1 : 0) + "");
    }

    //clear all previous results fengxiaojian,2014.09.23
    private void clearAll() {
        int count = items.length;
        for (int i = 0; i < count; i++) {
            items[i].setPassed(false);
            items[i].setChecked(false);
        }
        adapter.replaceItems(items);
        list.setAdapter(adapter);
        updateCompletedNum();
    }

    private void initButton() {
        fcButton = (Button) findViewById(R.id.full_check);
        fcButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(EngineerCode.this, TestResultsShow.class);
                startActivityForResult(intent, REQUEST_SHOW_RESULT);

            }
        });
        btn_checkReport = (Button) findViewById(R.id.check_report);
        btn_checkReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EngineerCode.this, "check report", Toast.LENGTH_SHORT).show();
                intentToTestReportActivity.setClass(EngineerCode.this, TestReport.class);
                startActivity(intentToTestReportActivity);
            }
        });
        //add by lzg
        mCommitReportBt = (Button)findViewById(R.id.commit_report);

        mCommitReportBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commitIntent = new Intent(EngineerCode.this, CommitReportActivity.class);
                startActivity(commitIntent);
            }
        });
        //end lzg
    }

    private class ItemContent {
        private boolean checked = false;
        private String title;
        private boolean pass = false;

        public ItemContent(String title, boolean checked, boolean pass) {
            this.title = title;
            this.checked = checked;
            this.pass = pass;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public void toggle() {
            checked = !checked;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setPassed(boolean pass) {
            this.pass = pass;
        }

        public boolean isPassed() {
            return pass;
        }
    }

    public class MyAdapter extends BaseAdapter {

        private Context mContext;
        private int layout;
        private int textID;
        private int checkboxID;
        private ItemContent[] items;
        LayoutInflater mInflater;

        public MyAdapter(Context context, int layout, int textID, int checkboxID, ItemContent[] items) {
            mInflater = LayoutInflater.from(context);
            this.layout = layout;
            this.textID = textID;
            this.checkboxID = checkboxID;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(layout, null);
                holder = new ViewHolder();
                holder.mTv = (TextView) convertView.findViewById(textID);
                holder.mCheckBox = (CheckBox) convertView.findViewById(checkboxID);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.mTv.setText(items[position].getTitle());
            holder.mCheckBox.setClickable(false);
            holder.mCheckBox.setChecked(items[position].isChecked());
            convertView.setBackgroundColor(items[position].isChecked() ? items[position].isPassed() ? Color.rgb(0x00, 0x8b, 0x00) : Color.RED : Color.GRAY);
            return convertView;
        }

        public void replaceItems(ItemContent[] items) {
            this.items = items;
        }
    }

    class ViewHolder {
        TextView mTv;
        CheckBox mCheckBox;
    }

    private void isFirst() {
        SharedPreferences share = getSharedPreferences("first", MODE_PRIVATE);
        SharedPreferences.Editor edt = share.edit();
        boolean isFirst = share.getBoolean("isFirst", true);
        if (isFirst) {
            Resources re = getResources();
            Toast.makeText(EngineerCode.this, "First", Toast.LENGTH_SHORT).show();
            //modify by lzg
            CommitUtils cu = new CommitUtils(mComtext);
            boolean isReadSuccess = cu.readStorageToReport();
            if(!isReadSuccess){
                for (int i = 0; i < testCount; i++) {
                    System.out.println("----lzg isFirst");
                    editor.putString(re.getString(stringIDs[i]), "NOT_TEST");
                    //add by lzg
                    editor.putString(StringUtils.getTestItemName(i),"*");
                    TestSign.JNISignflagAllClear();
                    //end lzg
                    editor.commit();
                }
            }
            //end lzg
            edt.putBoolean("isFirst", false);
            edt.commit();//TODO: maybe we should use apply()?? for the thread safe?
        }
    }
}

class SaveItems implements Parcelable {
    String title;
    boolean checked;
    boolean pass;

    public SaveItems(boolean checked, boolean pass, String title) {
        this.checked = checked;
        this.pass = pass;
        this.title = title;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeBooleanArray(new boolean[]{checked, pass});
        out.writeString(title);
    }

    public static final Parcelable.Creator<SaveItems> CREATOR
            = new Parcelable.Creator<SaveItems>() {
        public SaveItems createFromParcel(Parcel in) {
            return new SaveItems(in);
        }

        public SaveItems[] newArray(int size) {
            return new SaveItems[size];
        }
    };

    private SaveItems(Parcel in) {
        in.readBooleanArray(new boolean[]{checked, pass});
        title = in.readString();
    }

    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasBackFacingCamera() {
        final int CAMERA_FACING_BACK = 0;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public static boolean hasFrontFacingCamera() {
        final int CAMERA_FACING_BACK = 1;
        return checkCameraFacing(CAMERA_FACING_BACK);
    }

    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }
}
