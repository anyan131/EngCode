package com.zte.engineer.GPSUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Administrator on 2018/2/8.
 *
 */

public class SatelliteBaseView extends View {

    private String TAG = "SatelliteBaseView";
    public static final int STATE_UNFIXED = 1;
    public static final int STATE_USED_IN_FIX = 2;
    public static final int STATE_UNUSED_IN_FIX = 3;

    private SatelliteInfoManager mSatelliteManager;

    public SatelliteBaseView(Context context) {
        super(context);
    }

    public SatelliteBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SatelliteBaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void requestUpdate(SatelliteInfoManager manager){
        mSatelliteManager = manager;
        //this method is offer to non-UI thread to update the UI widgets.
        this.postInvalidate();
    }

    public SatelliteInfoManager getmSatelliteManager() {
        return mSatelliteManager;
    }
}
