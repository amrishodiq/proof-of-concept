package com.mencurigakansekali.servicecoordinator.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mencurigakansekali.servicecoordinator.Coordinator;

public class CoordinatorServer extends Service {
    private Coordinator.Stub coordinator;

    @Override
    public void onCreate() {
        super.onCreate();

        coordinator = new CoordinatorImpl(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return coordinator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
