package com.uos.leitner;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class MotionControlService extends Service implements SensorEventListener {

    private static final String TAG = "MotionControlService";

    private float mGZ = 0;//gravity acceleration along the z axis
    private int mEventCountSinceGZChanged = 0;
    private static final int MAX_COUNT_GZ_CHANGE = 10;
    private boolean mStarted;
    private SensorManager mSensorManager;

//    private MotionControlServiceThread thread;

    public MotionControlService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand, Started: " + mStarted);

        if (!mStarted) {
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);

            mStarted = true;
        }

//        MotionControlServiceHandler handler = new MotionControlServiceHandler();
//        thread = new MotionControlServiceThread(handler);
//        thread.start();
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            float gz = event.values[2];
            if (mGZ == 0) {
                mGZ = gz;
            } else {
                if ((mGZ * gz) < 0) {
                    mEventCountSinceGZChanged++;
                    if (mEventCountSinceGZChanged == MAX_COUNT_GZ_CHANGE) {
                        mGZ = gz;
                        mEventCountSinceGZChanged = 0;
                        if (gz > 0) {
                            Log.d(TAG, "now screen is facing up.");
                            this.sendBroadcast(new Intent().setAction("stopTimer"));

                        } else if (gz < 0) {
                            Log.d(TAG, "now screen is facing down.");
                            this.sendBroadcast(new Intent().setAction("startTimer"));
                        }
                    }
                } else {
                    if (mEventCountSinceGZChanged > 0) {
                        mGZ = gz;
                        mEventCountSinceGZChanged = 0;
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    class MotionControlServiceHandler extends Handler {
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            Intent intent = new Intent(MotionControlService.this, MainActivity.class);
//
//            //토스트 띄우기
//            Toast.makeText(MotionControlService.this, "뜸?", Toast.LENGTH_LONG).show();
//        }
//    }

}