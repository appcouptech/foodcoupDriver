package foodcoup.driver.demand.FCDViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;


import foodcoup.driver.demand.R;

@SuppressLint("AppCompatCustomView")
public class RoundedImageView  extends ImageView {

    private float cornerRadius;
    private boolean makeCircle;

    public RoundedImageView(Context context) {
        super(context);
    }

    public RoundedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RoundedImageView, 0, 0);
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getFloat(R.styleable.RoundedImageView_corner_radius, 0), context.getResources().getDisplayMetrics());
        makeCircle = typedArray.getBoolean(R.styleable.RoundedImageView_make_circle, false);
        typedArray.recycle();
    }

    public RoundedImageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.RoundedImageView, 0, 0);
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, typedArray.getFloat(R.styleable.RoundedImageView_corner_radius, 0), context.getResources().getDisplayMetrics());
        makeCircle = typedArray.getBoolean(R.styleable.RoundedImageView_make_circle, false);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

        int width = getWidth();
        int height = getHeight();

        Bitmap roundBitmap = getRoundedBitmap(bitmap, width, height);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
        roundBitmap.recycle();
    }

    public Bitmap getRoundedBitmap(Bitmap bitmap, int width, int height) {

        Bitmap earlyBitmap;

        if (bitmap.getWidth() != width || bitmap.getHeight() != width) {
            float smallest = Math.min(bitmap.getWidth(), bitmap.getHeight());
            float factor = smallest / width;
            earlyBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / factor), (int) (bitmap.getHeight() / factor), false);
        } else {
            earlyBitmap = bitmap;
        }

        Bitmap outPutBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outPutBitmap);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(0, 0, width, height);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#FFFFFF"));

        if (makeCircle) {
            canvas.drawCircle(width / 2 + 0.7f, height / 2 + 0.7f, width / 2 + 0.1f, paint);
        } else {
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(earlyBitmap, rect, rectF, paint);
        return outPutBitmap;
    }
}