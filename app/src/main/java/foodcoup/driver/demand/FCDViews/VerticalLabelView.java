package foodcoup.driver.demand.FCDViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import foodcoup.driver.demand.R;

@SuppressLint("AppCompatCustomView")
public class VerticalLabelView extends TextView {
    private TextPaint mTextPaint;
    private String mText;
    private int mAscent;
    private Rect text_bounds;

    final static int DEFAULT_TEXT_SIZE = 15;

    public VerticalLabelView(Context context) {
        super(context);
        initLabelView();
        text_bounds = new Rect();
    }

    public VerticalLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLabelView();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalLabelView);

        CharSequence s = a.getString(R.styleable.VerticalLabelView_text);
        if (s != null) setText(s.toString());

        setTextColor(a.getColor(R.styleable.VerticalLabelView_textColor, 0xFF000000));

        int textSize = a.getDimensionPixelOffset(R.styleable.VerticalLabelView_textSize, 0);
        if (textSize > 0) setTextSize(textSize);

        a.recycle();
        text_bounds = new Rect();
    }

    private final void initLabelView() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DEFAULT_TEXT_SIZE);
        mTextPaint.setColor(0xFF000000);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        setPadding(3, 3, 3, 3);
    }

    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
    }

    public void setTextSize(int size) {
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mTextPaint.getTextBounds(mText, 0, mText.length(), text_bounds);
        setMeasuredDimension(
                measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = text_bounds.height() + getPaddingLeft() + getPaddingRight();

            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = text_bounds.width() + getPaddingTop() + getPaddingBottom();

            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float text_horizontally_centered_origin_x = getPaddingLeft() + text_bounds.width()/2f;
        float text_horizontally_centered_origin_y = getPaddingTop() - mAscent;

        canvas.translate(text_horizontally_centered_origin_y, text_horizontally_centered_origin_x);
        canvas.rotate(-90);
        canvas.drawText(mText, 0, 0, mTextPaint);
    }
}
