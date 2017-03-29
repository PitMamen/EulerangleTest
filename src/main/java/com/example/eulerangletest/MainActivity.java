package com.example.eulerangletest;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

      private TextView tv1,tv2,tv3;
    private SensorManager mSensorManager;   //传感器管理
    private Sensor mSensorAcess;          //加速度传感器
    private  Sensor mSensorMagnetic;      //磁场传感器

    float[] acessvalues = new  float[3];       //用来存储传感器的数据
    float[] magneticvalues = new float[3];     //用来存储传感器的数据

    private  final  String  TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1  = (TextView) findViewById(R.id.textView);
        tv2  = (TextView) findViewById(R.id.textView2);
        tv3  = (TextView) findViewById(R.id.textView3);

         mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAcess = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  //加速度传感器
        mSensorMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);  //磁场传感器

        //给两个传感器分别注册监听器
         mSensorManager.registerListener(this,mSensorAcess,SensorManager.SENSOR_DELAY_NORMAL);
         mSensorManager.registerListener(this,mSensorMagnetic,SensorManager.SENSOR_DELAY_NORMAL);

        //更新显示数据的方法
          calculateOrientation();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){   //如果获取的传感器类型是 TYPE_ACCELEROMETER
            acessvalues = sensorEvent.values;       //赋值
        }else if (sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){         //如果获取的传感器类型是 TYPE_MAGNETIC_FIELD
            magneticvalues = sensorEvent.values;      //赋值
        }
            //更新
        calculateOrientation();

    }

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R,null,acessvalues,magneticvalues);
        SensorManager.getOrientation(R,values);


        //转换为角度
        values[0]  = (float) Math.toDegrees(values[0]);  //转换为角度的操作
        tv1.setText(Float.toString(values[0]));
        Log.d("haha", "角度一==: "+Float.toString(values[0]));

        values[1] = (float) Math.toDegrees(values[1]);//转换为角度的操作
        tv2.setText(Float.toString(values[1]));
        Log.d("haha", "角度二==: "+Float.toString(values[1]));

        values[2] = (float) Math.toDegrees(values[2]);//转换为角度的操作
         tv3.setText(Float.toString(values[2]));
        Log.d("haha", "角度三==: "+Float.toString(values[2]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);    // 当活动销毁时注销传感器监听
    }

    @Override
    protected void onPause() {
        super.onPause();
       mSensorManager.unregisterListener(this);      //当活动失去焦点时注销传感器监听
    }
}
