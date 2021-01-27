package com.example.recover;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class MyScroller extends ViewGroup {

    private int mDownY;

    private int mLastY;

    private int screenHigh;

    private int[] childHigh;

    private int level=1;

    private final Scroller scroller=new Scroller(getContext());

    private Context mContext;

    public MyScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount=getChildCount();
        childHigh=new int[childCount];
        for(int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
        screenHigh=MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount=getChildCount();
        int high=0;
        int preHigh=0;
        for(int i=childCount-1;i>=0;i--){
            View childView=getChildAt(i);
            high=high+childView.getMeasuredHeight();
            childHigh[i]=childView.getMeasuredHeight();
            childView.layout(0,screenHigh-high,childView.getMeasuredWidth(),screenHigh-preHigh);
            scrollTo(0,-preHigh);
            preHigh=high;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int y= (int) ev.getRawY();
        int action=ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(level==1&y<screenHigh-childHigh[0]){
                    return false;
                }
                mDownY=y;
                mLastY=y;
                return true;
            case MotionEvent.ACTION_MOVE:
                mLastY=y;
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mDownY>=screenHigh-childHigh[0]&&mDownY-mLastY>100&&level==1){
                    scroller.startScroll(0,getScrollY(),0,childHigh[1]);
                    level=2;
                    invalidate();
                }else if(mDownY>=screenHigh-childHigh[0]-childHigh[1]&&mLastY-mDownY>100&&level==2){
                    scroller.startScroll(0,getScrollY(),0,-childHigh[1]);
                    level=1;
                    invalidate();
                }else if(mDownY<screenHigh-childHigh[0]-childHigh[1]&&level==2){
                    scroller.startScroll(0,getScrollY(),0,-childHigh[1]);
                    level=1;
                    invalidate();
                }else if(mDownY>=screenHigh-childHigh[0]-childHigh[1]&&mDownY-mLastY>100&&level==2){
                    scroller.startScroll(0,getScrollY(),0,childHigh[2]);
                    level=3;
                    invalidate();
                }else if(mDownY>=screenHigh-childHigh[0]-childHigh[1]-childHigh[2]&&mLastY-mDownY>100&&level==3){
                    scroller.startScroll(0,getScrollY(),0,-childHigh[2]);
                    level=2;
                    invalidate();
                }else if(mDownY<screenHigh-childHigh[0]-childHigh[1]-childHigh[2]&&level==3){
                    scroller.startScroll(0,getScrollY(),0,-childHigh[1]-childHigh[2]);
                    level=1;
                    invalidate();
                }
                this.setVisibility(VISIBLE);
                return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
            this.setVisibility(VISIBLE);
        }
    }

    public void open(){
        if(level==1){
            scroller.startScroll(0,getScrollY(),0,childHigh[1]);
            level=2;
            invalidate();
        }
    }
}
