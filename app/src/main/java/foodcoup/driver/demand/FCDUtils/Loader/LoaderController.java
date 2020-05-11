package foodcoup.driver.demand.FCDUtils.Loader;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.animation.LinearInterpolator;

public class LoaderController implements ValueAnimator.AnimatorUpdateListener {

    private LoaderView loaderView;
    private Paint rectPaint;
    private LinearGradient linearGradient;
    private float progress;
    private ValueAnimator valueAnimator;
    private float widthWeight = LoaderConstant.MAX_WEIGHT;
    private float heightWeight = LoaderConstant.MAX_WEIGHT;
    private boolean useGradient = LoaderConstant.USE_GRADIENT_DEFAULT;
    private int corners = LoaderConstant.CORNER_DEFAULT;

    private final static int MAX_COLOR_CONSTANT_VALUE = 255;
    private final static int ANIMATION_CYCLE_DURATION = 750; //milis

    LoaderController(LoaderView view) {
        loaderView = view;
        init();
    }

    private void init() {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        loaderView.setRectColor(rectPaint);
        setValueAnimator(0.5f, 1, ObjectAnimator.INFINITE);
    }

    public void onDraw(Canvas canvas) {
        onDraw(canvas, 0, 0, 0, 0);
    }

    void onDrawCirclar(Canvas canvas) {
        onDrawCircular(canvas);
    }

    private void onDrawCircular(Canvas canvas) {

        // circle

        int CanvasRadius;
        int CanvasPadding = 7;
        rectPaint.setAlpha((int) (progress * MAX_COLOR_CONSTANT_VALUE));

        CanvasRadius = Math.min(canvas.getWidth(),canvas.getHeight()/2);
        canvas.drawCircle(
                canvas.getWidth() / 2,
                canvas.getHeight() / 2,
                CanvasRadius - CanvasPadding,
                rectPaint
        );
    }


    public void onDraw(Canvas canvas, float left_pad, float top_pad, float right_pad, float bottom_pad) {

        // rectangle

        float margin_height = canvas.getHeight() * (1 - heightWeight) / 2;
        rectPaint.setAlpha((int) (progress * MAX_COLOR_CONSTANT_VALUE));
        if (useGradient) {
            prepareGradient(canvas.getWidth() * widthWeight);
        }
        canvas.drawRoundRect(new RectF(0 + left_pad,
                        margin_height + top_pad,
                        canvas.getWidth() * widthWeight - right_pad,
                        canvas.getHeight() - margin_height - bottom_pad),
                corners, corners,
                rectPaint);



    }

    void onSizeChanged() {
        linearGradient = null;
        startLoading();
    }

    private void prepareGradient(float width) {
        if (linearGradient == null) {
            linearGradient = new LinearGradient(0, 0, width, 0, rectPaint.getColor(),
                    LoaderConstant.COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR);
        }
        rectPaint.setShader(linearGradient);
    }

    void startLoading() {
        if (valueAnimator != null && !loaderView.valueSet()) {
            valueAnimator.cancel();
            init();
            valueAnimator.start();
        }
    }

    void setHeightWeight(float heightWeight) {
        this.heightWeight = validateWeight(heightWeight);
    }

    void setWidthWeight(float widthWeight) {
        this.widthWeight = validateWeight(widthWeight);
    }

    void setUseGradient(boolean useGradient) {
        this.useGradient = useGradient;
    }

    public void setCorners(int corners) {
        this.corners = corners;
    }

    private float validateWeight(float weight) {
        if (weight > LoaderConstant.MAX_WEIGHT)
            return LoaderConstant.MAX_WEIGHT;
        if (weight < LoaderConstant.MIN_WEIGHT)
            return LoaderConstant.MIN_WEIGHT;
        return weight;
    }

    void stopLoading() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            setValueAnimator(progress, 0, 0);
            valueAnimator.start();
        }
    }

    private void setValueAnimator(float begin, float end, int repeatCount) {
        valueAnimator = ValueAnimator.ofFloat(begin, end);
        valueAnimator.setRepeatCount(repeatCount);
        valueAnimator.setDuration(ANIMATION_CYCLE_DURATION);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        progress = (float) valueAnimator.getAnimatedValue();
        loaderView.invalidate();
    }

    void removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator.removeUpdateListener(this);
            valueAnimator.cancel();
        }
        progress = 0f;
    }
}