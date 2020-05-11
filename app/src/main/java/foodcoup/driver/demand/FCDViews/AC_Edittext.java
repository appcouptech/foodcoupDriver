package foodcoup.driver.demand.FCDViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class AC_Edittext extends EditText {

    public AC_Edittext(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Regular.ttf");
        this.setTypeface(face);
    }

    public AC_Edittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Regular.ttf");
        this.setTypeface(face);
    }

    public AC_Edittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/RobotoSlab-Regular.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}
