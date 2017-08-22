package com.example.pc_.wangyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc- on 2017/6/14.
 */
public class MyHorizontalScrollView  extends ViewGroup{


    //解决出现的两个问题：空白页面显示、抬起手指自动恢复选择显示完整图片
    //会卡住在半路
    //关键类
    public Scroller scroller;
    public VelocityTracker velocityTracker;
    public Context context;
    public List<View> viewList=new ArrayList<>();

    //标志
    public int lastY=0;
    public int lastX=0;
    public int lastInterY=0;
    public int lastInterX=0;
    public int childWidth;
    public int childSizeCount;
    public int childIndex;
    public int childHeightSize;
    public int childWidthSize;
    public boolean isLastOne=false;

    public int[] drawablesId;



//    public void myAddView(){
//        for (int aDrawablesId : drawablesId) {
//
//            Looper.prepareMainLooper();
//            ImageView imageView = new ImageView(context);
//            imageView.setImageResource(aDrawablesId);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            addView(imageView);
//            Looper.loop();
//
//        }
//    }


    public MyHorizontalScrollView(Context context) {
        super(context);
        init();
    }

    public MyHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        init();
    }

    public MyHorizontalScrollView(Context context,AttributeSet attributeSet,int defstyle) {
        super(context,attributeSet,defstyle);
        init();
    }

    public void init(){
        if(scroller==null){
            scroller=new Scroller(getContext());
            velocityTracker=VelocityTracker.obtain();

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isInterceptd=false;
        int x=(int) ev.getX();
        int y=(int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isInterceptd=false;
                if(!scroller.isFinished()){
                    scroller.abortAnimation();
                    isInterceptd=true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX=x-lastInterX;
                int deltaY=y-lastInterY;
                isInterceptd = Math.abs(deltaX) > Math.abs(deltaY);
                break;
            case MotionEvent.ACTION_UP:
                isInterceptd=false;
                break;
            default:
                break;

        }
        lastX=x;
        lastY=y;
        lastInterX=x;
        lastInterY=y;
        return isInterceptd;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        int x=(int)  event.getX();
        int y=(int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                int deltax = x - lastX;

                if(childIndex==0){
                    if(deltax<0){
                        scrollBy(-deltax, 0);
                    }
                }else if(childIndex==3){
                    if(deltax>0){
                        scrollBy(-deltax, 0);
                    }
                }else {
                    scrollBy(-deltax, 0);
                }



                //手向右，View也向右移动，

                break;
            case MotionEvent.ACTION_UP:
                //已经移动的距离
                int scrollX = getScrollX();
                int scrollToChildIndex = scrollX / childSizeCount;
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 40) {
                    //手指向右滑动，页面下标变小
                    //速度等于尾减去首
                    childIndex = xVelocity > 0 ? childIndex - 1 : childIndex + 1;
                } else {
                    //(scrollX+childWidth/2) scrollX大于一半就下一页
                    //等于框减内容的位置，框的位置是0，0
                    childIndex = (scrollX + childWidth / 2) / childWidth;
                }
                //将下标控制在0到childCount-1间
                childIndex = Math.max(0, Math.min(childIndex, childSizeCount - 1));
                int dx = childIndex * childWidth - scrollX;
                //dx>0，view向左移动，dx<0，view向右移动
                smoothScrollBy(dx, 0);
                velocityTracker.clear();

                break;
            default:
                break;
        }
        lastX=x;
        lastY=y;
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth;
        int measuredHeight;
        int childCount=getChildCount();

        //这是根据父View的模式和View的布局参数确定的MeasureSpec
        //然后我们根据这个MeasureSpec再去测量View的大小
        int widthSpaceSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthSpaceMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize=MeasureSpec.getSize(heightMeasureSpec);
        int heihtSpaceMode=MeasureSpec.getMode(heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        if(childCount==0){
            setMeasuredDimension(50,50);
            childHeightSize=50;
        }
        else if(widthSpaceMode==MeasureSpec.AT_MOST&&heihtSpaceMode==MeasureSpec.AT_MOST){
            View childView=getChildAt(0);
            measuredWidth=childView.getMeasuredWidth()*childCount;
            measuredHeight=childView.getMeasuredHeight();
            childHeightSize=measuredHeight;
            setMeasuredDimension(measuredWidth,measuredHeight);
        }
        else if(widthSpaceMode==MeasureSpec.AT_MOST){
            View childView=getChildAt(0);

            measuredWidth=childView.getMeasuredWidth()*childCount;
            childHeightSize=heightSpaceSize;
            setMeasuredDimension(measuredWidth,heightSpaceSize);
        }
        else if(heihtSpaceMode==MeasureSpec.AT_MOST){
            View childView=getChildAt(0);
            measuredHeight=childView.getMeasuredHeight();
            childHeightSize=measuredHeight;
            setMeasuredDimension(widthSpaceSize,measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView1=getChildAt(1);
        int measuredWidthSize1=childView1.getMeasuredWidth();
        childWidthSize=measuredWidthSize1;
        int childLeft=0;
        int childCount=getChildCount();
        childSizeCount=childCount;
        for(int i=0;i<childCount;i++){
            View childView=getChildAt(i);
            viewList.add(childView);
            if(childView.getVisibility()!=View.GONE){
                childWidth=measuredWidthSize1;
                childView.layout(childLeft,0,childLeft+measuredWidthSize1,childView.getMeasuredHeight());
                childLeft+=measuredWidthSize1;
            }

        }

    }
    public void   smoothScrollBy(int dx,int dy){
        scroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();

    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            //scroller.getCurrX()当前应该在的位置
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        velocityTracker.recycle();
        super.onDetachedFromWindow();
    }
    public  ITellToParent iTellToParent;
    public interface  ITellToParent{
        void removeViews();
    }
    public void addChanger(ITellToParent iTellToParent){
        this.iTellToParent=iTellToParent;
    }

}
