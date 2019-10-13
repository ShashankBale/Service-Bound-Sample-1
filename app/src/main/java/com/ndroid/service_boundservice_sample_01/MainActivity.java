package com.ndroid.service_boundservice_sample_01;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    int count = 0;
    private Button btnStartService;
    private Button btnStopService;
    private Button btnBindService;
    private Button btnUnbindService;
    private Button btnGetRandomNumber;
    private TextView tvThreadCount;
    private RandomNoGeneratorService randomNoGeneratorService;

    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    private Intent serviceIntent;
    private boolean isLoopStopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(getString(R.string.service_demo_tag), "MainActivity thread id: " + Thread.currentThread().getId());

        initObjects();
        addListeners();
    }

    private void addListeners() {
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
        btnGetRandomNumber.setOnClickListener(this);
    }

    private void initObjects() {
        //Register UIs
        btnStartService = findViewById(R.id.btnThreadStarter);
        btnStopService = findViewById(R.id.btnStopThread);
        btnBindService = findViewById(R.id.btnBindService);
        btnUnbindService = findViewById(R.id.btnUnbindService);
        btnGetRandomNumber = findViewById(R.id.btnGetRandomNumber);
        tvThreadCount = findViewById(R.id.tvThreadCount);

        //initialize objects
        serviceIntent = new Intent(getApplicationContext(), RandomNoGeneratorService.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnThreadStarter:
                isLoopStopped = true;
                startService(serviceIntent);
                break;
            case R.id.btnStopThread:
                stopService(serviceIntent);
                break;
            case R.id.btnBindService:
                doOnBindService();
                break;
            case R.id.btnUnbindService:
                doOnUnbindService();
                break;
            case R.id.btnGetRandomNumber:
                setRandomNumber();
                break;
        }
    }

    private void doOnBindService() {
        if (serviceConnection == null) { //init only if serviceConnection is null
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    RandomNoGeneratorService.MyServiceBinder myServiceBinder = (RandomNoGeneratorService.MyServiceBinder) iBinder;
                    randomNoGeneratorService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }

        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void doOnUnbindService() {
        if (isServiceBound) { //unbind only if it is bind
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomNumber() {
        if (isServiceBound) { //get value only if service is bind
            tvThreadCount.setText("Random number: " + randomNoGeneratorService.getRandomNumber());
        } else {
            tvThreadCount.setText("Service not bound");
        }
    }
}
