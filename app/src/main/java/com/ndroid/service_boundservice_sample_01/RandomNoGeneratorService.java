package com.ndroid.service_boundservice_sample_01;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class RandomNoGeneratorService extends IntentService {
    private final String TAG = "RandomNoGenerator";
    private final int MIN = 0;
    private final int MAX = 100;
    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;
    private IBinder mBinder = new MyServiceBinder();

    public RandomNoGeneratorService() {
        super(RandomNoGeneratorService.class.getName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "In OnBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "In onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(TAG, "In OnRebind");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "In onHandleIntent, thread id: " + Thread.currentThread().getId());
        mIsRandomGeneratorOn = true;
        startRandomNumberGenerator();
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "In onStartCommand, thread id: " + Thread.currentThread().getId());
        return START_STICKY;
    }
    */

    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(TAG, "Thread id: " + Thread.currentThread().getId() + ", Random Number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread Interrupted");
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i(TAG, "Service Destroyed");
    }


    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false;
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }

    class MyServiceBinder extends Binder {
        public RandomNoGeneratorService getService() {
            return RandomNoGeneratorService.this;
        }
    }
}
