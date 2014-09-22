package edu.utah.cs4962.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jesus Zarate on 9/19/14.
 */
public class PaintAreaView extends ViewGroup {


    private int _lineCount = -1;
    //HashMap<Integer, ArrayList<PointF>> _linePoints = new HashMap<Integer, ArrayList<PointF>>();
    HashMap<Integer, Line> _linePoints = new HashMap<Integer, Line>();
    private int _lineColor = Color.BLACK;

    public PaintAreaView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
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
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            _lineCount++;
            //ArrayList<PointF> newPointList = new ArrayList<PointF>();
            Line line = new Line();
            line.setColor(PaletteView._selectedColor);

            _linePoints.put(_lineCount, line);
        }

        _linePoints.get(_lineCount).linePoints.add(new PointF(x, y));

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
            polylinePaint.setColor(_linePoints.get(lineIndex).getColor());

            if (!_linePoints.isEmpty()) {
                try {
                    polylinePath.moveTo(_linePoints.get(lineIndex).linePoints.get(0).x,
                            _linePoints.get(lineIndex).linePoints.get(0).y);
                } catch (Exception e) {
                    continue;
                }

                for (PointF point : _linePoints.get(lineIndex).linePoints) {
                    polylinePath.lineTo(point.x, point.y);
                }
            }

            canvas.drawPath(polylinePath, polylinePaint);
        }
    }

    private class Line {
        ArrayList<PointF> linePoints = new ArrayList<PointF>();
        private int _color;

        public int getColor() {
            return _color;
        }

        public void setColor(int _color) {
            this._color = _color;
        }
    }
}

