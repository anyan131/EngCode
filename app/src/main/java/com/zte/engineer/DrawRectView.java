package com.zte.engineer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawRectView extends SurfaceView implements SurfaceHolder.Callback{

	private static final String TAG = "DrawRectView";
	private int mStep = 80;
	private Paint mPaint;
	private Rect mCenterRect;
	private Rect[] mValidRect = new Rect[4];
	private Rect[][] mMinRect = new Rect[4][];
	boolean[][] bRectFilled = new boolean[4][];
	private Point mTouchPointRect = null;
	private boolean mFirst = true;
	private SurfaceHolder mHolder ;
	private boolean debug = false;
	//aeon lee add start
	Rect[] mLeftRects = null;
	Rect[] mRightRects = null;
	Rect[] mTopRects = null;
	Rect[] mBottomRects = null;
	int hCount;
	int vCount;
	//aeon lee add end
	public DrawRectView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DrawRectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPaint();
		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(false);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Style.STROKE);
		// mPaint.setStrokeWidth((float) 1.5);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		final int action = event.getAction();
		final int x = (int) event.getX();
		final int y = (int) event.getY();
//		Log.i(TAG,String.format("_________ x : %d , y : %d ", x , y));
		if (action == MotionEvent.ACTION_DOWN 
				|| action == MotionEvent.ACTION_MOVE) {
			
			if(hasFilledAll(bRectFilled)){
				getContext().sendBroadcast(new Intent(TouchScreenTest.PRIVATE_ACTION).putExtra("TestPass", 2));
			} else {
			mTouchPointRect = findValidRect(x, y);
			
			if (mTouchPointRect != null) {
				Log.i(TAG,"________draw solid rect : "+ mTouchPointRect);
				
				//aeon lee modify start 2014-03-27
				bRectFilled[mTouchPointRect.x][mTouchPointRect.y] = true;
				Canvas canvas = mHolder.lockCanvas();
				mPaint.setStyle(Style.STROKE);
				redrawHollowRect(canvas);
				
				mPaint.setStyle(Style.FILL);
				for(int i=0;i<4;i++){
					for(int j=0;j<mMinRect[i].length;j++){
						if(bRectFilled[i][j]==true){
							canvas.drawRect(mMinRect[i][j], mPaint);
						}
					}
				}
				//mHolder.unlockCanvasAndPost(canvas);
				//aeon lee modify end 2014-03-27
				if(hasFilledAll(bRectFilled)){
					getContext().sendBroadcast(new Intent(TouchScreenTest.PRIVATE_ACTION).putExtra("TestPass", 2));
				}
								//aeon lee modify end 2014-03-27

				mHolder.unlockCanvasAndPost(canvas);

			}
			}
		}
		return true;
//		return super.onTouchEvent(event);
	}

	private void drawHollowRect(Canvas canvas) {
		
		int mX = getMeasuredWidth() /*getResources().getDisplayMetrics().widthPixels*/;
		int mY = getMeasuredHeight();
		if(mX == 720 && mY == 1280)
			mStep = 80;
		else if(mX == 480 && mY == 800)
        	mStep = 80;
		else if(mX == 540 && mY == 960)
        	mStep = 60;			
		else if(mX == 320 && mY == 480)
			mStep = 40;	
		else 
			mStep = 80;	
		
		Log.i(TAG, String.format(
				"______shihaijun____measure width: %d, measure height : %d",
				mX, mY));
		
		mY = (mY%10==0?mY:10*(mY/10));
		
		Log.i(TAG, String.format(
				"______shihaijun____measure width: %d, measure height : %d",
				mX, mY));


		hCount = (mX - 2*mStep) / mStep;
		mTopRects = new Rect[hCount];
		mBottomRects = new Rect[hCount];
		
		for (int i = 0; i < hCount; i++) {
			int left = (i+1) * mStep;
			mTopRects[i] = new Rect(left, 0, left + mStep, mStep);
			canvas.drawRect(mTopRects[i], mPaint);
			mBottomRects[i] = new Rect(left, mY - mStep, left + mStep, mY);
			canvas.drawRect(mBottomRects[i], mPaint);
		}


		vCount = mY/ mStep;
		mLeftRects = new Rect[vCount];
		mRightRects = new Rect[vCount];
		
		for (int i = 0; i < vCount; i++) {
			int top = i * mStep;
			mLeftRects[i] = new Rect(0, top, mStep, top + mStep);
			mRightRects[i] = new Rect(mX - mStep, top, mX-1, top + mStep);
			canvas.drawRect(mLeftRects[i], mPaint);
			canvas.drawRect(mRightRects[i], mPaint);
		}

		mValidRect[0] = new Rect(mLeftRects[0].left, mLeftRects[0].top,
				mLeftRects[vCount - 1].right, mLeftRects[vCount - 1].bottom);
		mValidRect[1] = new Rect(mTopRects[0].left, mTopRects[0].top,
				mTopRects[hCount - 1].right, mTopRects[hCount - 1].bottom);
		mValidRect[2] = new Rect(mRightRects[0].left, mRightRects[0].top,
				mRightRects[vCount - 1].right, mRightRects[vCount - 1].bottom);
		mValidRect[3] = new Rect(mBottomRects[0].left, mBottomRects[0].top,
				mBottomRects[hCount - 1].right, mBottomRects[hCount - 1].bottom);

		mMinRect[0] = mLeftRects;
		mMinRect[1] = mTopRects;
		mMinRect[2] = mRightRects;
		mMinRect[3] = mBottomRects;
		
		bRectFilled[0] = new boolean[vCount];
		bRectFilled[1] = new boolean[hCount];
		bRectFilled[2] = new boolean[vCount];
		bRectFilled[3] = new boolean[hCount];
		
		mCenterRect = new Rect(mLeftRects[0].right, mLeftRects[0].bottom,
				mBottomRects[hCount - 1].left, mBottomRects[hCount - 1].top);

		
		if(debug ){
			int i = 0 ;
			for(i = 0; i< 4;i++){
				Log.i(TAG,"________Valid Rect :"+ mValidRect[i]);
//				Log.i(TAG,"________Min Rect :"+ mMinRect[i]);
			}
			Log.i(TAG,"________Center Rect :"+ mCenterRect);
		}
		
	}
	//aeon lee add start 2014-03-27
	private void redrawHollowRect(Canvas canvas) {
		for (int i = 0; i < vCount; i++) {
			canvas.drawRect(mLeftRects[i], mPaint);
			canvas.drawRect(mRightRects[i], mPaint);
		}
		for (int i = 0; i < hCount; i++) {
			canvas.drawRect(mTopRects[i], mPaint);
			canvas.drawRect(mBottomRects[i], mPaint);
		}
	}
	//aeon lee add end 2014-03-27
	private void drawSolidRect(Rect rect){
		Log.i(TAG,"______ rect : " + rect);
		Canvas canvas = mHolder.lockCanvas();
		if(canvas == null){
			Log.d(TAG,"___________get canvas failure....");
			return;
		}
		mPaint.setStyle(Style.FILL);
		canvas.drawRect(rect, mPaint);
		mHolder.unlockCanvasAndPost(canvas);
	}
	
	Point findValidRect(int x, int y) {
		if (mCenterRect.contains(x, y)) {
			return null;
		} else {
			for (int i = 0; i < mValidRect.length; i++) {
				if (mValidRect[i].contains(x, y)) {
//					Log.i(TAG,"___________ Valid Rect index :" + i);
					for (int j = 0; j < mMinRect[i].length; j++) {
						if (mMinRect[i][j].contains(x, y)) {
							return new Point(i, j);
						}
					}
				}
			}
		}
		return null;
	}

	static boolean hasFilledAll(boolean[][] bArray){
		final boolean[][] mArray = bArray;
		for(boolean[] a1 : mArray){
			for(boolean b : a1){
				if(!b){
					return false;
				}
			}
		}
		
		return true;
	}
	
//	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

//	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Canvas canvas = mHolder.lockCanvas(null);
		drawHollowRect(canvas);
		mHolder.unlockCanvasAndPost(canvas);
	}

//	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
