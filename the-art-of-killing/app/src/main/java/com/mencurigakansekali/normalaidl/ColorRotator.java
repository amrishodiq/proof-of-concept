package com.mencurigakansekali.normalaidl;

import android.os.RemoteException;
import android.util.Log;

public class ColorRotator {
    private static final String TAG = "Static";

    private static boolean started = false;
    public static void startExecution(final int[] colors, final OnColorChangeListener colorChangeListener) {
        if (!started) {
            new Thread() {
                @Override
                public void run() {
                    started = true;
                    int i = 0;
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }

                        notifyChanges(colors, i, colorChangeListener);
                        i++;
                        i %= Integer.MAX_VALUE;
                    }
                }
            }.start();
        } else {
            Log.d(TAG, "Executing ...");
        }
    }

    private static void notifyChanges(int[] colors, int newValue, OnColorChangeListener colorChangeListener) {
        try {
            int result = colors[newValue % colors.length];
            colorChangeListener.onColorChanged(
                    result
            );
        } catch (RemoteException e) {

        }
    }
}
