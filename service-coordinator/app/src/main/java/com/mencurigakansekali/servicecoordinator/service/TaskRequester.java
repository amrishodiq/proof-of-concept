package com.mencurigakansekali.servicecoordinator.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.mencurigakansekali.servicecoordinator.Task;

public class TaskRequester implements ServiceConnection {
    private static final String TAG = "TaskRequester";

    private Context context;
    private Task task;
    private String className;
    private Class serviceClass;
    private String name;
    public TaskRequester(Context context, String className) {
        this.context = context;
        this.className = className;
    }

    public void bind() throws ClassNotFoundException {
        Log.d(TAG, "bind "+className);
        serviceClass = Class.forName(className);
        name = serviceClass.getSimpleName();
        Intent intent = new Intent(context, serviceClass);
        intent.setAction(serviceClass.getName());
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void unBind() {
        context.unbindService(this);
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public String getName() {
        return name;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected");
        task = Task.Stub.asInterface(iBinder);
        Log.d(TAG, "task is not null");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG, "onServiceDisconnected");
        task = null;
    }

    public Task getTask() {
        return task;
    }
}
