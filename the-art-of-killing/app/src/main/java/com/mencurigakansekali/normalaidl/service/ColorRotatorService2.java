package com.mencurigakansekali.normalaidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import com.mencurigakansekali.normalaidl.LongTaskExecutor;

public class ColorRotatorService2 extends Service {
    private static final String TAG = "Service";
    private LongTaskExecutor.Stub executor = new ColorRotatorBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "After onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Before onBind");
        return executor;
    }
}
