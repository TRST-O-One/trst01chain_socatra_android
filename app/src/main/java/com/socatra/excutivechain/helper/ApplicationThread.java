package com.socatra.excutivechain.helper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Future;

public final class ApplicationThread {

    private static final String LOG_TAG = ApplicationThread.class.getName();

    private final static int THREAD_DELAY = 0;

    private static Handler ui;

    public static Runnable uiPost(final String clazz, final String msg, final Runnable run, final long delay) {
        final Runnable runner = new Runnable() {
            public void run() {
                final long startTime = System.nanoTime();
                try {
                    run.run();
                } catch (Throwable ex) {
                    Log.e(LOG_TAG, LOG_TAG, ex);
                } finally {
                    final String msg1 = "UI THREAD " + (null == msg ? "" : msg) + " (" + (System.nanoTime() - startTime) / 1000000 + "ms)";
                    Log.d(LOG_TAG, msg1);
                }
            }
        };
        ui.postDelayed(runner, delay);
        return runner;
    }

    public static Runnable uiPost(final String clazz, final String msg, final Runnable run) {
        return uiPost(clazz, msg, run, THREAD_DELAY);
    }

}

