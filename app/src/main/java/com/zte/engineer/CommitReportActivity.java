package com.zte.engineer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.newmobi.iic.TestSign;
import com.zte.engineer.CommitReportUtils.CommitReportEntity;
import com.zte.engineer.CommitReportUtils.CommitUtils;
import com.zte.engineer.CommitReportUtils.Constants;
import com.zte.engineer.CommitReportUtils.StringUtils;

import org.json.JSONException;

public class CommitReportActivity extends Activity implements View.OnClickListener {

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditer;
    private Button mCommitBt;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private static final int SHOW_PROGRESS_DIALOG = 2001;
    private static final int CLOSE_PROGRESS_DIALOG = 2002;
    private static final int COMMIT_EXCEPTION = 2003;
    private CommitUtils mCommitUtils;
    private AlertDialog mImeiDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_commit_report);
        initView();
        IntentFilter iFilter = new  IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkChangeReceiver, iFilter);
    }
    private BroadcastReceiver mNetworkChangeReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(cm != null){
                    NetworkInfo mobileNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    NetworkInfo wifiNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if(mobileNetInfo.isConnected() || wifiNetInfo.isConnected()){
                        Toast.makeText(mContext, R.string.open_mobile_network_success, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(mContext, R.string.open_mobile_network_fail, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };
    private void initView() {
        mSp = this.getSharedPreferences("engineer", this.MODE_MULTI_PROCESS);
        mEditer = mSp.edit();
        mCommitBt = (Button) findViewById(R.id.commit_bt);
        if (mSp.getString(Constants.COMMIT_FAG, "2").equals("1")) {
            mCommitBt.setText(R.string.commit_success);
            mCommitBt.setBackgroundColor(Color.GREEN);
        } else if(mSp.getString(Constants.COMMIT_FAG, "2").equals("0")){
            mCommitBt.setText(R.string.commit_fail);
            mCommitBt.setBackgroundColor(Color.RED);
        }else{
            mCommitBt.setText(R.string.no_commit_report);
            mCommitBt.setBackgroundColor(Color.RED);
        }
        mCommitBt.setOnClickListener(this);
        initProgressDialog();
        initImeiDialog();
    }

    private void initProgressDialog(){
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    private void initImeiDialog(){
        mImeiDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(
                        getText(R.string.no_imei_tip).toString())
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        }).create();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.commit_bt) {
            String imeiString = StringUtils.getDeviceIMEI(mContext);
            if(imeiString != null && !imeiString.equals("")) {
                if (StringUtils.isNetworkConnected(mContext)) {
                    myHandler.removeMessages(SHOW_PROGRESS_DIALOG);
                    myHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
                    commitReportAction();
                } else {
                    Toast.makeText(mContext, R.string.opening_mobile_network, Toast.LENGTH_LONG).show();
                    //StringUtils.openMobileNetwork(mContext);
                }
            }else{
                displayImeiDialog();
            }
        }
    }

    private void displayImeiDialog()
    {
        if(mImeiDialog != null){
            mImeiDialog.show();
        }
    }

    private void cancleImeiDialog()
    {
        if(mImeiDialog != null && mImeiDialog.isShowing()){
            mImeiDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        cancleImeiDialog();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    private void commitReportAction() {
        new AsyncTask<Void, Void, CommitReportEntity>() {

            @Override
            protected CommitReportEntity doInBackground(Void... params) {
                CommitReportEntity commitReportEntity = null;
                mCommitUtils = new CommitUtils(mContext);
                try {
                    commitReportEntity = mCommitUtils.getCommitReport();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mEditer.putString(Constants.COMMIT_FAG, "0").commit();
                    mCommitUtils.writeReportToDeviceStorege();
                    myHandler.removeMessages(COMMIT_EXCEPTION);
                    myHandler.sendEmptyMessageDelayed(COMMIT_EXCEPTION,1500);
                    System.out.println("---lzg e="+e.getMessage());
                }
                return commitReportEntity;
            }

            @Override
            protected void onPostExecute(CommitReportEntity commitReportEntity) {
                super.onPostExecute(commitReportEntity);
                if (commitReportEntity != null) {
                    if (commitReportEntity.isSuccess()) {
                        mEditer.putString(Constants.COMMIT_FAG, "1").commit();
                        mCommitBt.setText(R.string.commit_success);
                        mCommitBt.setBackgroundColor(Color.GREEN);
                        System.out.println("---lzg commit success");
                    } else {
                        mEditer.putString(Constants.COMMIT_FAG, "0").commit();
                        mCommitBt.setText(R.string.commit_fail);
                        mCommitBt.setBackgroundColor(Color.RED);
                        System.out.println("---lzg commit fail");
                    }
                    mCommitUtils.writeReportToDeviceStorege();
                    myHandler.removeMessages(CLOSE_PROGRESS_DIALOG);
                    myHandler.sendEmptyMessageDelayed(CLOSE_PROGRESS_DIALOG,1500);
                }
            }

        }.execute();
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_PROGRESS_DIALOG:{
                    mProgressDialog.setMessage(getResources().getString(R.string.commit_report_tip));
                    mProgressDialog.show();
                    break;
                }
                case  CLOSE_PROGRESS_DIALOG:{
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    break;
                }
                case COMMIT_EXCEPTION:{
                    mCommitBt.setText(R.string.commit_fail);
                    mCommitBt.setBackgroundColor(Color.RED);
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    break;
                }
                default:
                    break;

            }
        }
    };

}
