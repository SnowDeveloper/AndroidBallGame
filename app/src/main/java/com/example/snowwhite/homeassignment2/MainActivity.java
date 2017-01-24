package com.example.snowwhite.homeassignment2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor sensor;
    private int movement = 10;
    private ImageView ball, hurdle;
    private RelativeLayout layout = null;
    AnimatorSet animatorSet ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_main);

        ball = (ImageView) findViewById(R.id.imageView);
        hurdle = (ImageView) findViewById(R.id.imageView1);
        layout = (RelativeLayout) findViewById(R.id.name_view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Making the ball to jump on touch
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            animatorSet = new AnimatorSet();
            ObjectAnimator xAnim;
            if (ball.getX() + ball.getWidth() < Math.round(layout.getWidth()/2)) {
                xAnim = ObjectAnimator.ofFloat(ball, "translationX", hurdle.getX() +
                        hurdle.getWidth() + 300.0f);
            } else {
                xAnim = ObjectAnimator.ofFloat(ball, "translationX", hurdle.getX() - 300.0f);
            }

            ObjectAnimator moveUp = ObjectAnimator.ofFloat(ball, "translationY", -180.0f);
            moveUp.setDuration(2500);

            ObjectAnimator moveUpDown = ObjectAnimator.ofFloat(ball, "translationY", 0.0f);
            moveUp.setDuration(2500);

            xAnim.setDuration(2500);
            animatorSet.play(moveUp).with(xAnim);
            animatorSet.play(moveUpDown).after(moveUp);

            animatorSet.start();

            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public final void onSensorChanged(SensorEvent event){
        //When the device is being rotated then the sensors makes the ball move
        if (event.values[1] > 2) {
            if (ball.getX() + ball.getWidth() + movement <= layout.getWidth()) {
                if (!((ball.getX()+ ball.getWidth()) <= hurdle.getX() &&
                        ball.getX() + ball.getWidth() + movement > hurdle.getX())) {
                    ball.setX(ball.getX() + movement);
                }
            }
        }
        else if (event.values[1] < -2) {
            if (ball.getX() - movement >= 0 ) {
                if (!(ball.getX() >= hurdle.getX() + hurdle.getWidth() &&
                        ball.getX() - movement < hurdle.getX() + hurdle.getWidth())) {
                    ball.setX(ball.getX() - movement);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
