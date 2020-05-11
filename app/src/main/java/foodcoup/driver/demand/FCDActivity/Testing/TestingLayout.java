package foodcoup.driver.demand.FCDActivity.Testing;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import foodcoup.driver.demand.R;

public class TestingLayout extends AppCompatActivity {

    private GestureDetector gDetector;
    LinearLayout ll_gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_layout);

        ll_gesture = findViewById(R.id.ll_gesture);

       /* gDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent start, MotionEvent finish, float v, float v1) {
                if (start.getRawY() < finish.getRawY()) {
                    ((ImageView) findViewById(R.id.image_place_holder)).setImageResource(R.mipmap.ic_launcher);
                } else {
                    ((ImageView) findViewById(R.id.image_place_holder)).setImageResource(R.mipmap.ic_launcher);
                }
                return true;
            }
        });*/


    }

   /* @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }*/
}
