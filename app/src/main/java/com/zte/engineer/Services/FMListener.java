package com.zte.engineer.Services;

import android.os.Bundle;

/**
 * Created by Administrator on 2018/3/1.
 *  activity connect my FM service should implements this interface to
 *  update UI.
 */

public interface FMListener {
    /**
     * callback method from service.
     * */
    void onCallback(Bundle bundle);

}
