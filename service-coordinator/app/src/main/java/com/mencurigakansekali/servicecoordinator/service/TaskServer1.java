package com.mencurigakansekali.servicecoordinator.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;

import com.mencurigakansekali.servicecoordinator.Task;

public class TaskServer1 extends Service {
    private Task.Stub tonePlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        tonePlayer = new TaskImpl("Server 1");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return tonePlayer;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }
}
