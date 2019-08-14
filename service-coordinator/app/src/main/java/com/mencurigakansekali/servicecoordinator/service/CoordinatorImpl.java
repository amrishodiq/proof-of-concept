package com.mencurigakansekali.servicecoordinator.service;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.mencurigakansekali.servicecoordinator.Coordinator;
import com.mencurigakansekali.servicecoordinator.model.TaskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class ini mengendalikan service-service lain (TaskServer1 dan TaskServer2).
 */
public class CoordinatorImpl extends Coordinator.Stub {
    private static final String TAG = "CoordinatorImpl";
    private HashMap<String, TaskRequester> taskRequesterMap;
    private List<TaskModel> serviceClasses;

    private Context context;
    public CoordinatorImpl(Context context) {
        Log.d(TAG, "CoordinatorImpl Constructor");
        this.context = context;
        this.taskRequesterMap = new HashMap<>();
        this.serviceClasses = new ArrayList<>();
    }

    @Override
    public void add(String className) {
        Log.d(TAG, "add "+className);
        if (taskRequesterMap.get(className) != null) return;

        try {
            Log.d(TAG, "Create requester for "+className);
            TaskRequester requester = new TaskRequester(context, className);
            requester.bind();

            Log.d(TAG, "After binding, put to map");
            taskRequesterMap.put(className, requester);
            serviceClasses.add(new TaskModel(requester.getServiceClass().getSimpleName(), className, 1));
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "add "+className+" done");
    }

    @Override
    public void remove(String className) {
        TaskRequester requester = taskRequesterMap.get(className);
        if (requester != null) {
            requester.unBind();

            serviceClasses.remove(className);
            taskRequesterMap.remove(new TaskModel(requester.getServiceClass().getSimpleName(), className, 1));
        }
    }

    /**
     *
     * @return service-service yang sudah ada saat ini.
     * @throws RemoteException
     */
    @Override
    public List<TaskModel> list() throws RemoteException {
        return serviceClasses;
    }

    /**
     * Mem-proxy permintaan untuk menjalankan method di TaskServer dari UI.
     * @param className
     * @param sound
     */
    @Override
    public void exec(String className, int[] sound) {
        TaskRequester requester = taskRequesterMap.get(className);
        if (requester != null) {
            try {
                requester.getTask().exec(sound);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "No task requester for "+className);
        }
    }

    /**
     * Mematikan service-service yang dia koordinir.
     */
    @Override
    public void stopAll() {
        Log.d(TAG, "Number of services: "+ taskRequesterMap.size());
        Iterator<Map.Entry<String, TaskRequester>> iterator = taskRequesterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, TaskRequester> item = iterator.next();

            String name = item.getValue().getName();
            String className = item.getValue().getServiceClass().getName();

//            remove(className);
            item.getValue().unBind();
            Log.d(TAG, "- "+name+" "+className);
//            taskRequesterMap.remove(className);
            iterator.remove();
            serviceClasses.remove(new TaskModel(name, className, 1));
//            taskRequesterMap.remove(new TaskModel(name, className, 1));
        }
    }
}
