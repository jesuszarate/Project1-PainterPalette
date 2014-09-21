package edu.utah.cs4962.project1;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jesus Zarate on 9/15/14.
 */
public class PaletteView extends ViewGroup {

    public static ArrayList<View> _children = new ArrayList<View>();
    private ArrayList<PointF> _points = new ArrayList<PointF>();
    private HashMap<PaintView, PointF> _centerPosOfSplotches = new HashMap<PaintView, PointF>();

//    private float _initialXPos = 0.0f;
//    private float _initialYPos = 0.0f;

    private Rect _layoutRect;

    private int _childrenNotGone = 0;

    public PaletteView(Context context) {
        super(context);
    }

    public void addNewColor() {
        PaintView paintView = new PaintView(getContext());

        paintView.setColor(0xFFFF1493);
        addView(paintView, new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT));
        invalidate();
    }

    public void removeColor(PaintView paintView) {
        paintView.setVisibility(GONE);
//        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        PaintView child;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            child = (PaintView) getChildAt(childIndex);
            if (child.isActive) {// && isCorrectDistance(childIndex, child.getRadius(), x, y)) {
                child.setX(x - child.getWidth() / 2);
                child.setY(y - child.getHeight() / 2);

//                // On mouse click down, save the original points of the
//                // paint splotch.
//                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
//                    _initialXPos = x;
//                    _initialYPos = y;
//                }

                if (event.getActionMasked() == MotionEvent.ACTION_UP) {

                    // If the paint is dragged on top of another paint mix them together to
                    // create a new color.
                    if (mixPaint(child, x, y)) {
                        addNewColor();
                    }

                    // Otherwise return the paint to its original location
                    ObjectAnimator animator = new ObjectAnimator();
                    animator.setTarget(child);
                    animator.setPropertyName("x");
                    animator.setDuration(200);

                    float centerOfChildX = child.getWidth() / 2;
                    float centerOfChildY = child.getHeight() / 2;

                    // Set the splotch back to its original place. Figure out how to
                    //  move from the endpoint back to the original position.
                    animator.setValues(
                            PropertyValuesHolder.ofFloat("x",
                                    new float[]{x - child.getWidth() / 2, _centerPosOfSplotches.get(child).x - centerOfChildX}),
                            PropertyValuesHolder.ofFloat("y",
                                    new float[]{y - child.getHeight() / 2, _centerPosOfSplotches.get(child).y - centerOfChildY})
                    );
                    animator.start();

                }
            }
        }
        return true;
    }

    /**
     *
     * @param SelectedChild
     * @param x - x point of the position of the selected child.
     * @param y - y point of the position of the selected child.
     * @return True - If the selected splotch is over another splotch so
     * the colors can be mixed.
     */
    private boolean mixPaint(PaintView SelectedChild, float x, float y) {

        Iterator itr = _centerPosOfSplotches.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry pairs = (Map.Entry)itr.next();
            if(!pairs.getKey().equals(SelectedChild)){

                float childCenterX = ((PointF)pairs.getValue()).x;
                float childCenterY = ((PointF)pairs.getValue()).y;
                float distance = (float) Math.sqrt(Math.pow(childCenterX - x, 2) + Math.pow(childCenterY - y, 2));
                if(distance < ((PaintView)pairs.getKey()).getRadius()){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if click is inside the circle, by measuring the distance
     * between the center of the circle and the radius of the circle.
     * -> If the point clicked is less than the radius of the circle
     * then it is a click.
     *
     * @param radius
     * @param x
     * @param y
     * @return
     */
    private Boolean isCorrectDistance(int childIndex, float radius, float x, float y) {

        double angle = (double) childIndex / (double) _childrenNotGone * 2 * ((Math.PI));
        int childCenterX = (int) (_layoutRect.centerX() + _layoutRect.width() * 0.6 * Math.cos(angle));
        int childCenterY = (int) (_layoutRect.centerY() + _layoutRect.height() * 0.6 * Math.sin(angle));


        //float CircleCenterX = contentRect.centerX();
        //float CircleCenterY = contentRect.centerY();

        // Distance formula-> sqrt((x1 - x2)^2 + (y1 - y2)^2)
        //float distance = (float) Math.sqrt(Math.pow(CircleCenterX - x, 2) + Math.pow(CircleCenterY - y, 2));
        float distance = (float) Math.sqrt(Math.pow(childCenterX - x, 2) + Math.pow(childCenterY - y, 2));

        if (distance < radius) {
            Log.i("paint_view", "Touched inside the circle");
            return true;
        }
        return false;

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x55DC9D60);
        //paint.setColor(Color.GREEN);
        //Path path = new Path();

        RectF paletteRect = new RectF();
        paletteRect.left = getPaddingLeft();
        paletteRect.top = getPaddingTop();
        paletteRect.right = getWidth() - getPaddingRight();
        paletteRect.bottom = getHeight() - getPaddingBottom();

        canvas.drawOval(paletteRect, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int width = Math.max(widthSpec, getSuggestedMinimumWidth());
        int height = Math.max(heightSpec, getSuggestedMinimumHeight());

        int childState = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {

            View child = getChildAt(childIndex);
            //child.measure(MeasureSpec.AT_MOST | 75, MeasureSpec.AT_MOST | 75);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState));
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

        int childWidthMax = 0;
        int childHeightMax = 0;
        _childrenNotGone = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {

            View child = getChildAt(childIndex);
            _children.add(child);
            if (child.getVisibility() == GONE) {
                continue;
            }
            childWidthMax = Math.max(childWidthMax, child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax, child.getMeasuredHeight());
            _childrenNotGone++;
        }

        _layoutRect = new Rect();
        _layoutRect.left = getPaddingLeft() + 9 * childWidthMax / 10;
        _layoutRect.top = getPaddingTop() + 9 * childHeightMax / 10;
        _layoutRect.right = getWidth() - getPaddingRight() - 9 * childWidthMax / 10;
        _layoutRect.bottom = getHeight() - getPaddingBottom() - 9 * childHeightMax / 10;

        PointF paletteCenter = new PointF(_layoutRect.centerX(), _layoutRect.centerY());
        float radius = _layoutRect.width();
        int pointCount = 50;
        Path path = new Path();
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
            PointF point = new PointF();

            point.x = paletteCenter.x + radius * (float) Math.cos(((double) pointIndex /
                    (double) pointCount) * 2.0 * Math.PI);

            point.y = paletteCenter.y + radius * (float) Math.sin(((double) pointIndex /
                    (double) pointCount) * 2.0 * Math.PI);

            if (pointIndex == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }

        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {

            double angle = (double) childIndex / (double) _childrenNotGone * 2 * ((Math.PI));
            int childCenterX = (int) (_layoutRect.centerX() + _layoutRect.width() * 0.6 * Math.cos(angle));
            int childCenterY = (int) (_layoutRect.centerY() + _layoutRect.height() * 0.6 * Math.sin(angle));

            _centerPosOfSplotches.put((PaintView) getChildAt(childIndex), new PointF(childCenterX, childCenterY));

            View child = getChildAt(childIndex);
            Rect childLayout = new Rect();

            if (child.getVisibility() == GONE) {
                childLayout.left = 0;
                childLayout.top = 0;
                childLayout.right = 0;
                childLayout.bottom = 0;
            } else {
                childLayout.left = childCenterX - childWidthMax / 2;
                childLayout.top = childCenterY - childHeightMax / 2;
                childLayout.right = childCenterX + childWidthMax / 2;
                childLayout.bottom = childCenterY + childHeightMax / 2;
            }
            child.layout(childLayout.left, childLayout.top, childLayout.right, childLayout.bottom);
        }

    }
}
