package foodcoup.driver.demand.FCDViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class AC_BoldTextview extends TextView {
    public AC_BoldTextview(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        this.setTypeface(face);
    }

    public AC_BoldTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        this.setTypeface(face);
    }

    public AC_BoldTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}
