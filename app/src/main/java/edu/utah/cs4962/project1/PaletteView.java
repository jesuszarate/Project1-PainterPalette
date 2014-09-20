package edu.utah.cs4962.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Jesus Zarate on 9/15/14.
 */
public class PaletteView extends ViewGroup {

    public static ArrayList<View> _children = new ArrayList<View>();

    public PaletteView(Context context) {
        super(context);
    }

    public void addColor(){

    }

    public void removeColor(PaintView paintView){
        paintView.setVisibility(GONE);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x00DC9D60);
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
        int childrenNotGone = 0;
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {

            View child = getChildAt(childIndex);
            _children.add(child);
            if (child.getVisibility() == GONE) {
                continue;
            }
            childWidthMax = Math.max(childWidthMax, child.getMeasuredWidth());
            childHeightMax = Math.max(childHeightMax, child.getMeasuredHeight());
            childrenNotGone++;
        }

        Rect layoutRect = new Rect();
        layoutRect.left = getPaddingLeft() + 9 *childWidthMax / 10;
        layoutRect.top = getPaddingTop() + 9 * childHeightMax / 10;
        layoutRect.right = getWidth() - getPaddingRight() - 9 * childWidthMax / 10;
        layoutRect.bottom = getHeight() - getPaddingBottom() - 9 * childHeightMax / 10;

        PointF paletteCenter = new PointF(layoutRect.centerX(), layoutRect.centerY());
        float radius = layoutRect.width();
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

            double angle = (double) childIndex / (double) childrenNotGone * 2 * ((Math.PI));
            int childCenterX = (int) (layoutRect.centerX() + layoutRect.width() * 0.6 * Math.cos(angle));
            int childCenterY = (int) (layoutRect.centerY() + layoutRect.height() * 0.6 * Math.sin(angle));

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
