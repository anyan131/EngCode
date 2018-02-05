package com.zte.engineer;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BTAddressTest extends ZteActivity {
	/*
	 * Define some aliases to make these debugging flags easier to refer to.
	 */
	private final static String TAG = "BTAddressTest";

	TextView mBluetoothStatus;
	TextView mBluetoothAddress;

	BluetoothAdapter mBluetooth;

	boolean isManualTurnOn = false;

	// Receiver the bluetooth status change
	protected BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// Get bluetooth status change action
			if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
						BluetoothAdapter.ERROR)) {
				case BluetoothAdapter.STATE_ON:
                    ((Button) findViewById(R.id.singlebutton_pass_button)).setEnabled(true);
					mBluetoothStatus.setText(String.format(
							getString(R.string.bt_status),
							getString(R.string.on)));
					// Get current bluetooth adapter
					mBluetooth = BluetoothAdapter.getDefaultAdapter();
					mBluetoothAddress.setText(String.format(
							getString(R.string.bt_address_is),
							mBluetooth.getAddress()));
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
                    ((Button) findViewById(R.id.singlebutton_pass_button)).setEnabled(false);
					mBluetoothStatus.setText(String.format(
							getString(R.string.bt_status),
							getString(R.string.off)));
					mBluetoothAddress.setText(String.format(
							getString(R.string.bt_address_is),
							getString(R.string.unknown)));
					break;
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.singlebuttonview);

		// Get current bluetooth adapter
		mBluetooth = BluetoothAdapter.getDefaultAdapter();
		// Get current bluetooth status
		boolean isEnable = mBluetooth.isEnabled();

		TextView mTextView = (TextView) findViewById(R.id.singlebutton_textview);
		mBluetoothStatus = (TextView) findViewById(R.id.singlebutton_textview_2);
		mBluetoothAddress = (TextView) findViewById(R.id.singlebutton_textview_3);

        ((Button) findViewById(R.id.singlebutton_pass_button)).setEnabled(false);
		mTextView.setText(R.string.bt_address);

        IntentFilter filter = new IntentFilter(
                BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, filter);
		if (true == isEnable) {
            ((Button) findViewById(R.id.singlebutton_pass_button)).setEnabled(true);
			mBluetoothStatus.setText(String.format(
					getString(R.string.bt_status), getString(R.string.on)));
			mBluetoothAddress
					.setText(String.format(getString(R.string.bt_address_is),
							mBluetooth.getAddress()));
		} else {
			mBluetooth.enable();
			isManualTurnOn = true;
			mBluetoothStatus.setText(String.format(
					getString(R.string.bt_status), getString(R.string.off)));
			mBluetoothAddress.setText(R.string.bt_read_address);
		}

		// Set intent filter

		// register filter receiver

		// button progress
		((Button) findViewById(R.id.singlebutton_pass_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.singlebutton_false_button))
				.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mBluetoothReceiver);
		super.onDestroy();
	}

	@Override
	public void finishSelf(int result) {

		if (true == isManualTurnOn) {
			mBluetooth.disable();
		}
		super.finishSelf(result);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.singlebutton_pass_button:
			finishSelf(RESULT_PASS);
			break;
		case R.id.singlebutton_false_button:
			finishSelf(RESULT_FALSE);
			break;
		default:
			finishSelf(RESULT_PASS);
			break;
		}

	}

    @Override
    public void onBackPressed() {
        finishSelf(RESULT_FALSE);
	    super.onBackPressed();
    }
}
