package com.example.distinguish;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.Nullable;

public class RecordView extends View {
    private int mImageW = 42;                               //图片宽
    private int mImageH = 42;                               //图片高
    private int mMarginTop = 25, mTextMarginBottom = 11;        //图片居上距离 文字居底部距离
    private int SCREEN_HEIGHT = 0, SCREEN_WIDTH = 0;        //屏幕宽高

    private Paint mImagePaint, mTextPaint, mVoiceRectPaint, mBackgroundPaint;//文字 进度条 背景
    private int mVoiceRectW = 1, mVoiceRectH = 10, mInterval = 3, mVoiceRectMarginTop = 83; //宽、高、间隔、进度条居上距离
    private int mTextSize = 12;                             //文本字体大小

    private Bitmap mVoiceRecording;                             //图片
    private int mVoiceRectStart = 0, mDescriptionW = 0;             //进度条开始左边位置、文字长度

    private Rect mTextRect;                             //计算文字长度矩形
    private String mDescription = "松开停止";

    private int mCurrentPosition = 0;               //当前位置
    private int mBackgroundRound = 3;               //背景圆角

    private final int COUNT = 16;                   //进度条格数

    public RecordView(Context context) {
        super(context);
        init();
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mImageW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mImageW, dm);
        mImageH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mImageH, dm);
        mMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMarginTop, dm);
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTextSize, dm);
        mTextMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTextMarginBottom, dm);
        mVoiceRectW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mVoiceRectW, dm);
        mVoiceRectH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mVoiceRectH, dm);
        mInterval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mInterval, dm);
        mVoiceRectMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mVoiceRectMarginTop, dm);
        mBackgroundRound = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBackgroundRound, dm);
        mImagePaint = new Paint();

        mVoiceRectPaint = new Paint();
        mVoiceRectPaint.setColor(Color.WHITE);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.parseColor("#B3000000"));//背景颜色

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.parseColor("#B3FFFFFF"));//文字颜色

        mVoiceRecording = BitmapFactory.decodeResource(getResources(), R.drawable.voice_recording);
        mVoiceRecording = scaleBitmap(mVoiceRecording, mImageW, mImageH);
        mTextRect = new Rect();
        setWillNotDraw(false);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (SCREEN_HEIGHT == 0 || SCREEN_WIDTH == 0) {
            SCREEN_HEIGHT = getHeight();
            SCREEN_WIDTH = getWidth();
        }
        if (mVoiceRectStart <= 0) {
            mVoiceRectStart = SCREEN_WIDTH / 2 - (COUNT * mVoiceRectW + (COUNT - 1) * mInterval) / 2;
        }
        if (mDescriptionW <= 0) {
            mTextPaint.getTextBounds(mDescription, 0, mDescription.length(), mTextRect);
            mDescriptionW = mTextRect.width();
        }

        canvas.drawRoundRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, mBackgroundRound, mBackgroundRound, mBackgroundPaint);//画灰色背景和圆角

        canvas.drawBitmap(mVoiceRecording, SCREEN_WIDTH / 2 - mVoiceRecording.getWidth() / 2, mMarginTop, mImagePaint);//画mic图片

        //画前几个是白色 后几个是灰色
        for (int i = 0; i < COUNT; i++) {
            if (i < mCurrentPosition) {
                mVoiceRectPaint.setColor(Color.WHITE);
            } else {
                mVoiceRectPaint.setColor(Color.parseColor("#73FFFFFF"));
            }
            canvas.drawRect(i * mVoiceRectW + (i * mInterval) + mVoiceRectStart, mVoiceRectMarginTop, i * mVoiceRectW + (i * mInterval) + mVoiceRectW + mVoiceRectStart, mVoiceRectMarginTop + mVoiceRectH, mVoiceRectPaint);
        }
        canvas.drawText(mDescription, SCREEN_WIDTH / 2 - mDescriptionW / 2, SCREEN_HEIGHT - mTextMarginBottom, mTextPaint);//画文字 松开停止

    }

    public void setIndex(int p) {
        if(p < 0 ){
            p = 0;
        }
        if(p >= COUNT){
            p = COUNT -1 ;
        }

        mCurrentPosition = p;
        postInvalidate();
    }

     //设置 百分比

    public void setIndex(double percent){
        int p = (int) (COUNT * percent);
        setIndex(p);

    }


    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);              // 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }
}
