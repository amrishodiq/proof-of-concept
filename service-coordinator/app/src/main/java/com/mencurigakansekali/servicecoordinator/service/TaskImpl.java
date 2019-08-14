package com.mencurigakansekali.servicecoordinator.service;

import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.mencurigakansekali.servicecoordinator.Task;

public class TaskImpl extends Task.Stub {
    private static final String TAG = "TaskImpl";
    private String serviceName;

    public TaskImpl(String serviceName) {
        this.serviceName = serviceName;
    }

    private boolean playing = false;

    @Override
    public int getPid() {
        return Process.myPid();
    }

    @Override
    public void exec(final int[] sound) throws RemoteException {
        if (!playing) {
            new Thread() {
                @Override
                public void run() {
                    Log.d(TAG, "Start playing");
                    for (int item: sound) {
                        Log.d(TAG, "- "+serviceName+": "+item);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                    Log.d(TAG, "Finished playing");
                    playing = false;
                }
            }.start();
            playing = true;
        } else {
            Log.d(TAG, "Currently playing");
        }
    }
}
