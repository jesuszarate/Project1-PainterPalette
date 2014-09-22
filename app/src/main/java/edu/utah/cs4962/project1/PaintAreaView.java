package edu.utah.cs4962.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jesus Zarate on 9/19/14.
 */
public class PaintAreaView extends ViewGroup {


    private int _lineCount = -1;
    HashMap<Integer, ArrayList<PointF>> _linePoints = new HashMap<Integer, ArrayList<PointF>>();
    private int _lineColor = Color.BLACK;

    public PaintAreaView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);

//        mTransformedView = new View(context);
//        mTransformedView.setBackgroundColor(Color.GREEN);
//        mTransformedView.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
//        addView(mTransformedView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {
        for (int childIndex = 0; childIndex < getChildCount(); childIndex++) {
            PaintView child = (PaintView) getChildAt(childIndex);
            if (child.isActive) {
                _lineColor = child.getColor();
            }
//            View child = getChildAt(childIndex);
//            child.layout(
//                    getWidth()  / 2 - child.getMeasuredWidth()  / 2,
//                    getHeight() / 2 - child.getMeasuredHeight() / 2,
//                    getWidth()  / 2 + child.getMeasuredWidth()  / 2,
//                    getHeight() / 2 + child.getMeasuredHeight() / 2
//            );

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            _lineCount++;
            ArrayList<PointF> newPointList = new ArrayList<PointF>();
            _linePoints.put(_lineCount, newPointList);
        }

        _linePoints.get(_lineCount).add(new PointF(x, y));

        invalidate();

        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int lineIndex = 0; lineIndex < _linePoints.size(); lineIndex++) {
            Paint polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            polylinePaint.setStyle(Paint.Style.STROKE);
            polylinePaint.setStrokeWidth(2.0f);
            Path polylinePath = new Path();

            if(lineIndex == _linePoints.size() - 1){
                polylinePaint.setColor(PaletteView._selectedColor);
            }

            if (!_linePoints.isEmpty()) {
                try {
                    polylinePath.moveTo(_linePoints.get(lineIndex).get(0).x, _linePoints.get(lineIndex).get(0).y);
                } catch (Exception e) {
                    continue;
                }

                for (PointF point : _linePoints.get(lineIndex)) {
                    polylinePath.lineTo(point.x, point.y);
                }
            }
            canvas.drawPath(polylinePath, polylinePaint);
        }
    }

}
