package com.mencurigakansekali.normalaidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mencurigakansekali.normalaidl.service.ColorRotatorBinder;
import com.mencurigakansekali.normalaidl.service.ColorRotatorService1;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Activity";

    private View color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bind:
                        bind();
                        break;
                    case R.id.start:
                        start();
                        break;
                    case R.id.unbind:
                        unBind();
                        break;
                }
            }
        };

        stateBeforBound();

        color = findViewById(R.id.color);
        findViewById(R.id.bind).setOnClickListener(onClickListener);
        findViewById(R.id.start).setOnClickListener(onClickListener);
        findViewById(R.id.unbind).setOnClickListener(onClickListener);
    }

    private void stateBeforBound() {
        findViewById(R.id.bind).setEnabled(true);
        findViewById(R.id.start).setEnabled(false);
        findViewById(R.id.unbind).setEnabled(false);
    }

    private void stateAfterBound() {
        findViewById(R.id.bind).setEnabled(false);
        findViewById(R.id.start).setEnabled(true);
        findViewById(R.id.unbind).setEnabled(true);
    }

    private LongTaskExecutor executor;
    private void bind() {
        Log.d(TAG, "bind");
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "Connected to service.");
                executor = ColorRotatorBinder.asInterface(service);
                stateAfterBound();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "Disconnected from service. " +
                        "This only happened on extreme situations (like crashed/killed).");
                executor = null;
                stateBeforBound();
            }
        };

        Log.d(TAG, "Create intent");
        Intent intent = new Intent(this, ColorRotatorService1.class);
        intent.setAction(LongTaskExecutor.class.getName());

//        startService(intent);

        bindService(intent, connection, BIND_AUTO_CREATE);

        Log.d(TAG, "Intent bind with connection");

    }

    private void start() {
        Log.d(TAG, "start");
        try {
            int appPid = Process.myPid();
            int pid = executor.getPid();

            appendStatus("App PID: "+appPid+"\nBinder PID: "+pid);

            List<String> colors = new ArrayList<>();
            colors.add("#ef5350");
            colors.add("#EC407A");
            colors.add("#AB47BC");
            colors.add("#7E57C2");
            colors.add("#5C6BC0");
            colors.add("#42A5F5");
            colors.add("#29B6F6");
            colors.add("#26C6DA");
            colors.add("#26A69A");
            colors.add("#66BB6A");
            colors.add("#9CCC65");
            colors.add("#D4E157");
            colors.add("#FFEE58");
            colors.add("#FFCA28");
            colors.add("#FFA726");
            colors.add("#FF7043");


            executor.execute(colors, onColorChangeListener);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void unBind() {
        Log.d(TAG, "unBind");
        try {
            executor.stop();
        } catch (Exception e) {
            Log.e(TAG, "At this stage, it means that the service is not longer bound");
        }
        appendStatus("Unbind");
        stateBeforBound();
    }

    private void appendStatus(final String newStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String current = ((TextView) findViewById(R.id.status)).getText().toString();
                ((TextView) findViewById(R.id.status)).setText(current+"\n"+newStatus);
            }
        });
    }

    private void setColor(final int newColor) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                color.setBackgroundColor(newColor);
            }
        });
    }

    private OnColorChangeListener.Stub onColorChangeListener = new OnColorChangeListener.Stub() {
        @Override
        public void onColorChanged(int newColor) throws RemoteException {
            Log.d(TAG, "onColorChanged "+newColor);
            setColor(newColor);
        }
    };
}
