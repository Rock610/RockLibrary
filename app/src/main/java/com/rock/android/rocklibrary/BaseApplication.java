package com.rock.android.rocklibrary;

import android.app.Application;

import com.rock.android.rocklibrary.NetWork.BaseNetWorkHelper;

/**
 * Created by rock on 15/10/14.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        BaseNetWorkHelper.start(this);
    }
}
