package com.mencurigakansekali.servicecoordinator;

import com.mencurigakansekali.servicecoordinator.model.TaskModel;

interface Coordinator {
    void add(in String className);
    List<TaskModel> list();
    void remove(in String className);

    void exec(String className, in int[] sound);
    void stopAll();
}
