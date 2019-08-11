package com.mencurigakansekali.normalaidl;

import com.mencurigakansekali.normalaidl.OnColorChangeListener;

interface LongTaskExecutor {
    int getPid();
    int execute(in List<String> colors, OnColorChangeListener listener);
    void stop();
}
