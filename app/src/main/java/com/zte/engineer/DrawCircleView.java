package com.zte.engineer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawCircleView extends SurfaceView implements SurfaceHolder.Callback{

	private static final String TAG = "DrawCircleView";
	Paint mPaint = null;
	Rect[] mCircleRects = new Rect[5] ;
	boolean[] hadFill = new boolean[5];
	final int RADIUS = 70;
	boolean bFirst = true;
	Canvas mCanvas = new Canvas();
	Point mCurPoint = null;
	SurfaceHolder mHolder = null;
	private boolean debug = false; 
	
	public DrawCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		
	}
	
	private void initPaint(){
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.GREEN);
	}
	
	private void setCircleRects(Rect[] rects){
		
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		height = height%10==0 ? height : 10*(height/10);
		Log.i(TAG, String.format(
				"____setCircleRects(...)______measure width: %d, measure height : %d",
				width, height));
		
		rects[0] = new Rect(0,0,RADIUS*2,RADIUS*2);//The upper left corner
		rects[1] = new Rect(width - RADIUS*2,0,width , RADIUS*2); // The upper right corner
		rects[2] = new Rect(0,height - RADIUS*2,RADIUS*2,height); // The lower left corner
		rects[3] = new Rect(width - RADIUS*2 , height - RADIUS*2,width,height);//The lower right corner
		rects[4] = new Rect(width/2 - RADIUS , height/2 - RADIUS, width/2 + RADIUS,height/2 + RADIUS);//center 
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			int x = (int) event.getX();
			int y = (int) event.getY();
			final int len = mCircleRects.length;
			for(int i=0; i < len ; i++){
//				final Point p = mPoint[i];
				if(mCircleRects[i].contains(x, y)){
					Log.i(TAG,"______draw stroken circle no."+i);
					hadFill[i]=true;
					drawSolidCircle(i);
				}
			}
		}
		return true;
//		return super.onTouchEvent(event);
	}

	private void drawHollowCircle(){
		//draw hollow circle
		Canvas canvas = mHolder.lockCanvas(null);
		mPaint.setStyle(Style.STROKE);
		
		for(Rect r : mCircleRects){
			canvas.drawCircle(r.centerX(), r.centerY(), RADIUS, mPaint);
		}
		
		mHolder.unlockCanvasAndPost(canvas);
		
	}
	
	private void drawSolidCircle(int index){
		//aeon lee modify start 2014-03-27 
		Canvas canvas = mHolder.lockCanvas();
		
		//draw solid circle
		Log.i(TAG,"________draw fill circle INDEX: "+ index);
		for(int i=0;i<mCircleRects.length;i++){
			mPaint.setStyle(Style.STROKE);
			canvas.drawCircle(mCircleRects[i].centerX(), mCircleRects[i].centerY(), RADIUS, mPaint);
			if(hadFill[i]==true){
				mPaint.setStyle(Style.FILL);
				canvas.drawCircle(mCircleRects[i].centerX(),
					mCircleRects[i].centerY(), RADIUS, mPaint);
			}
		}
		//aeon lee modify end 2014-03-27 
		if(fillAll(hadFill)){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getContext().sendBroadcast(
							new Intent(TouchScreenTest.PRIVATE_ACTION)
									.putExtra("TestPass", 1));
				}
			}, 500);
		}
		mHolder.unlockCanvasAndPost(canvas);
	}
	
	/**
	 * @param bArray
	 * @return Return true if all circles had been filled.Or false. 
	 */
	boolean fillAll(boolean[] bArray){
		
		for(boolean b : bArray){
			if(!b){
				Log.i(TAG, "____fillAll____b :" + b);
				return false;
			}
		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initPaint();
		setCircleRects(mCircleRects);
		drawHollowCircle();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	
}
