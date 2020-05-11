package foodcoup.driver.demand.FCDUtils.Loader;

import android.graphics.Paint;

public interface LoaderView {


    void setRectColor(Paint rectPaint);

    void invalidate();

    boolean valueSet();

}
