package com.mencurigakansekali.servicecoordinator.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mencurigakansekali.servicecoordinator.Task;

public class TaskModel implements Parcelable {
    private String name;
    private String className;
    private int multiplicationFactor = 1;

    public TaskModel(String name, String className, int multiplicationFactor) {
        this.name = name;
        this.className = className;
        this.multiplicationFactor = multiplicationFactor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaskModel) {
            TaskModel target = (TaskModel) obj;
            if (name.equals(target.name) && className.equals(target.className)) {
                return true;
            }
            return false;
        }
        return false;
    }

    /************************** RELATED TO PARCELABLE **************************/
    private TaskModel(Parcel in) {
        name = in.readString();
        className = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(className);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<TaskModel> CREATOR = new Parcelable.Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel source) {
            return new TaskModel(source);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };
}
