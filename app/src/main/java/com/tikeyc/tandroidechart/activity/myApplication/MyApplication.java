package com.tikeyc.tandroidechart.activity.myApplication;

import android.app.Application;

import org.xutils.x;

/**
 * Created by public1 on 2017/4/24.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化xUtils
        x.Ext.init(this);

    }



}
