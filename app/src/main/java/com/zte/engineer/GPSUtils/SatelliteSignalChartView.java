package com.zte.engineer.GPSUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.zte.engineer.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class SatelliteSignalChartView extends SatelliteBaseView {

    //TAG for logging.
    private static final String TAG = "SatelliteSignalChartView";

    //some factors for drawing the view.
    private static final float PERCENT_80 = 0.8f;
    private static final float PERCENT_75 = 0.75f;
    private static final float PERCENT_50 = 0.5f;
    private static final float PERCENT_25 = 0.25f;
    private static final float RATIO_BASE = 100;
    private static final int BASE_LINE_OFFSET = 6;
    private static final int TEXT_OFFSET = 10;
    private static final int[] DIVIDER_RANKS = {15, 20, 25, 30, 35};

    //some paint for drawing different things.
    private Paint mLinePaint;
    private Paint mLine2Paint;
    private Paint mRectPaint;
    private Paint mRectLinePaint;
    private Paint mTextPaint;
    private Paint mBgPaint;


    public SatelliteSignalChartView(Context context) {
        super(context);
        onCreateView();
    }

    public SatelliteSignalChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public SatelliteSignalChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView();
    }

    //initialize the paints.
    private void onCreateView() {
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(false);
        mRectPaint.setStrokeWidth(2.0f);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(getResources().getColor(R.color.sigview_line_color));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1.0f);

        mLine2Paint = new Paint(mLinePaint);
        mLine2Paint.setStrokeWidth(0.5f);

        mRectLinePaint = new Paint(mRectPaint);
        mRectLinePaint.setColor(getResources().getColor(R.color.bar_outline));
        mRectLinePaint.setStyle(Paint.Style.STROKE);


        mTextPaint = new Paint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(10.0f);
        mTextPaint.setColor(getResources().getColor(R.color.sigview_text_color));

        mBgPaint = new Paint();
        mBgPaint.setColor(getResources().getColor(R.color.sigview_background));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float sigRectMaxHeight = (float) Math.floor(viewHeight * PERCENT_80);
        float baseLineY = sigRectMaxHeight + BASE_LINE_OFFSET;
        float rectRatio = sigRectMaxHeight / RATIO_BASE;
        //绘制背景
        canvas.drawPaint(mBgPaint);
        //绘制底部的线条。
        canvas.drawLine(0, baseLineY, viewWidth, baseLineY, mLinePaint);

        float line2YArr[] = {
                baseLineY - sigRectMaxHeight * PERCENT_25,
                baseLineY - sigRectMaxHeight * PERCENT_50,
                baseLineY - sigRectMaxHeight * PERCENT_75,
                baseLineY - sigRectMaxHeight
        };
        for (float i : line2YArr) {
            canvas.drawLine(0, i, viewWidth, i, mLine2Paint);
        }

        SatelliteInfoManager infoManager = getmSatelliteManager();
        if (infoManager != null) {
            List<SatelliteInfo> satelliteInfoList = infoManager.getmSatelliteInfoList();
            float bgRectWidth = computeBgRectWidth(satelliteInfoList.size());
            float barWidth = (float) Math.floor(bgRectWidth * PERCENT_75);
            float margin = (bgRectWidth - barWidth) / 2;
            for (int i = 0; i < satelliteInfoList.size(); i++) {
                SatelliteInfo satelliteInfo = satelliteInfoList.get(i);
                float barHeight = satelliteInfo.snr * rectRatio;
                float left = i * bgRectWidth + margin;
                float top = baseLineY - barHeight;
                float center = left + barWidth / 2;
                canvas.drawRect(left, top, left + barWidth, baseLineY, getSigBarPaint(satelliteInfo, infoManager));
                mRectLinePaint.setColor(satelliteInfo.color);
                canvas.drawRect(left, top, left + barWidth, baseLineY, mRectPaint);
                float textOffset = bgRectWidth - barWidth;
                canvas.drawText(String.valueOf(satelliteInfo.prn),
                        center, baseLineY + textOffset + TEXT_OFFSET, mTextPaint);
                String snrStr = String.format("%3.1f", satelliteInfo.snr);
                canvas.drawText(snrStr, center, top - textOffset, mTextPaint);

            }
        }
    }


    private Paint getSigBarPaint(SatelliteInfo info, SatelliteInfoManager manager) {
        if (!manager.isUseInFix(SatelliteInfoManager.PRN_ANY)) {
            mRectPaint.setColor(getResources().getColor(R.color.bar_used));
            mRectPaint.setStyle(Paint.Style.STROKE);
        } else {
            if (manager.isUseInFix(info.prn)) {
                mRectPaint.setColor(getResources().getColor(R.color.bar_used));
                mRectPaint.setStyle(Paint.Style.FILL);
            } else {
                mRectPaint.setColor(getResources().getColor(R.color.bar_unused));
                mRectPaint.setStyle(Paint.Style.FILL);
            }
        }
        return mRectPaint;
    }

    /**
     * compute the background rectangle width.
     *
     * @return the width of background  rectangle.
     */
    private float computeBgRectWidth(int size) {
        int divider = 0;
        for (int i = 0; i < DIVIDER_RANKS.length; i++) {
            int d = DIVIDER_RANKS[i];
            if (size < d) {
                divider = d;
                break;
            }
        }
        if (divider == 0) {
            divider = size;
        }
        float width = getWidth();
        return (float) Math.floor(width / divider);

    }
}
