package edu.hogwarts.siesta.clock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.util.Date;

public class LiveWallpaper extends WallpaperService {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new TestPatternEngine();
	}

	class TestPatternEngine extends Engine {

		private final Handler mHandler = new Handler();

		private final Runnable mDrawPattern = new Runnable() {
			public void run() {
				drawFrame();
			}
		};

		private boolean mVisible;

		private Paint paint;

		TestPatternEngine() {
			paint = new Paint();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawPattern);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawPattern);
			}
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			drawFrame();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawPattern);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
				float yStep, int xPixels, int yPixels) {
			drawFrame();
		}

		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					drawPattern(c);
				}
			} finally {
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}
			mHandler.removeCallbacks(mDrawPattern);
			if (mVisible) {
				mHandler.postDelayed(mDrawPattern, 1000 / 25);
			}
		}

		void drawPattern(Canvas canvas) {

			paint.setColor(Color.parseColor("#000000"));
			canvas.drawPaint(paint);

			int w=canvas.getWidth();
			int h=canvas.getHeight();

			Date date=new Date();
			int hr=date.getHours()%12;
			int mn=date.getMinutes()/5;

			int g=64;
			int r=32;
			int x=w/2;
			int y=h/2+h/4;

			drawDigit(canvas,hr,x-g*2,y,r,g);
			drawDigit(canvas,mn,x+g*2,y,r,g);
		}

		void drawDigit(Canvas canvas,int n,int x,int y,int r,int g) {
			int N=0,A=1,B=2,C=4,D=8;
			int[] c={N,A,B,C,D,A|B,B|C,C|D,A|D,A|C,B|D,A|B|C|D};


			if((c[n]&A)!=0) {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x-g,y-g,r,paint);
			} else {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#000000"));
				canvas.drawCircle(x-g,y-g,r,paint);

				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x-g,y-g,r,paint);
			}

			if((c[n]&B)!=0) {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x+g,y-g,r,paint);
			} else {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#000000"));
				canvas.drawCircle(x+g,y-g,r,paint);

				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x+g,y-g,r,paint);
			}

			if((c[n]&C)!=0) {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x+g,y+g,r,paint);
			} else {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#000000"));
				canvas.drawCircle(x+g,y+g,r,paint);

				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x+g,y+g,r,paint);
			}

			if((c[n]&D)!=0) {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x-g,y+g,r,paint);
			} else {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor("#000000"));
				canvas.drawCircle(x-g,y+g,r,paint);

				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.parseColor("#FFFFFF"));
				canvas.drawCircle(x-g,y+g,r,paint);
			}

		}

	}
}
