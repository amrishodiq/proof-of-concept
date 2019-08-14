package com.mencurigakansekali.servicecoordinator;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.mencurigakansekali.servicecoordinator.model.TaskModel;
import com.mencurigakansekali.servicecoordinator.service.CoordinatorServer;
import com.mencurigakansekali.servicecoordinator.service.TaskServer1;
import com.mencurigakansekali.servicecoordinator.service.TaskServer2;
import com.mencurigakansekali.servicecoordinator.ui.TaskAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        /*
        findViewById(R.id.play1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int[] data = new int[100];
                    for (int i=0; i<data.length; i++) {
                        data[i] = i;
                    }
                    coordinator.exec(serviceClasses[0], data);
                } catch (RemoteException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        findViewById(R.id.play2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int[] data = new int[100];
                    for (int i=0; i<data.length; i++) {
                        data[i] = i * 10;
                    }
                    coordinator.exec(serviceClasses[1], data);
                } catch (RemoteException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        findViewById(R.id.stop_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    coordinator.stopAll();
                } catch (RemoteException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        */
    }

    private TaskAdapter adapter;
    private List<TaskModel> items = new ArrayList<>();
    private FloatingActionButton fabAdd, fabStop;
    private TaskAdapter.ItemClickListener itemClickListener = new TaskAdapter.ItemClickListener() {
        @Override
        public void onItemClick(TaskModel clicked) {
            int[] data = new int[100];
            for (int i=0; i<data.length; i++) {
                data[i] = i * 10;
            }
            try {
                coordinator.exec(clicked.getClassName(), data);
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };

    private void initView() {
        RecyclerView list = findViewById(R.id.list);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(items);
        adapter.setListener(itemClickListener);

        list.setAdapter(adapter);

        fabStop = findViewById(R.id.stop);
        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    coordinator.stopAll();
                    fabAdd.setEnabled(true);
                } catch (RemoteException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        fabAdd = findViewById(R.id.add);
        fabAdd.setEnabled(false);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDefault();
                fabAdd.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bind();
    }

    @Override
    protected void onStop() {
        unBind();
        super.onStop();
    }

    private Coordinator coordinator;
    private String[] serviceClasses = new String[] {
            TaskServer1.class.getName(),
            TaskServer2.class.getName()
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected");
            coordinator = Coordinator.Stub.asInterface(iBinder);

            try {
                if (coordinator.list().size() > 0) {
                    load();
                } else {
                    addDefault();
                }
            } catch (RemoteException e) {

            } finally {

            }


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            coordinator = null;
        }
    };

    private void addDefault() {
        for (String className: serviceClasses) {
            try {
                Log.d(TAG, "Adding "+className);
                coordinator.add(className);
                Log.d(TAG, "Adding "+className+" done");
            } catch (RemoteException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                load();
            }
        });
    }

    private void load() {
        try {
            List<TaskModel> list = coordinator.list();
            Log.e(TAG, "Load existing");

            items.clear();
            items.addAll(list);
            adapter.notifyDataSetChanged();

            for (TaskModel item: list) {
                Log.e(TAG, "- "+item.getClassName());

            }
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void bind() {
        Intent intent = new Intent(this, CoordinatorServer.class);
        intent.setAction(CoordinatorServer.class.getName());
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void unBind() {
        unbindService(serviceConnection);
    }
}
