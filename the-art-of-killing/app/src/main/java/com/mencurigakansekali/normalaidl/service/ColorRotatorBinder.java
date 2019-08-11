package com.mencurigakansekali.normalaidl.service;

import android.graphics.Color;
import android.os.RemoteException;
import android.os.Process;

import com.mencurigakansekali.normalaidl.ColorRotator;
import com.mencurigakansekali.normalaidl.LongTaskExecutor;
import com.mencurigakansekali.normalaidl.OnColorChangeListener;

import java.util.List;

public class ColorRotatorBinder extends LongTaskExecutor.Stub {
    private static final String TAG = "Binder";

    @Override
    public int getPid() throws RemoteException {
        return Process.myPid();
    }

    @Override
    public int execute(List<String> colors, OnColorChangeListener listener) throws RemoteException {
        int[] color = new int[colors.size()];
        for (int i=0; i<colors.size(); i++) {
            color[i] = Color.parseColor(colors.get(i));
        }
        ColorRotator.startExecution(color, listener);
        return 0;
    }

    @Override
    public void stop() throws RemoteException {
        Process.killProcess(Process.myPid());
    }
}
