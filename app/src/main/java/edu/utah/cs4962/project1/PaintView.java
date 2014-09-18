package edu.utah.cs4962.project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by jesuszarate on 9/8/14.
 */
public class PaintView extends View {

    RectF _contentRect;
    float _radius;
    int _color = Color.CYAN;

    public interface OnSplotchTouchListener{
        public void onSplotchTouched(PaintView v);
    }
    OnSplotchTouchListener _onSplotchTouchListener = null;

    public PaintView(Context context) {
        super(context);
        setMinimumHeight(1000);
        setMinimumWidth(1000);

        // Colors: OPACITY/RED/GREEN/BLUE
        //this.setBackgroundColor(0XFF228844);
    }

    public int getColor() {
        return _color;
    }

    public void setColor(int _color) {
        this._color = _color;

        // Redraws the circle so it can be the new color.(Marks it for redraw,
        // then generates a onDraw() when it's ready)
        invalidate();
    }

    // The parameter is the interface type.
    public void setOnSplotchTouchListener(OnSplotchTouchListener listener){
        _onSplotchTouchListener = listener;
    }

    public OnSplotchTouchListener getOnSplotchTouchListener(){
        return _onSplotchTouchListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        // Check if click is inside the circle, by measuring the distance
        //  between the center of the circle and the radius of the circle.
        // -> If the point clicked is less than the radius of the circle
        //    then it is a click.
        float CircleCenterX = _contentRect.centerX();
        float CircleCenterY = _contentRect.centerY();

        // Distance formula-> sqrt((x1 - x2)^2 + (y1 - y2)^2)
        float distance = (float) Math.sqrt(Math.pow(CircleCenterX - x, 2) + Math.pow(CircleCenterY - y, 2));
        if (distance < _radius) {
            Log.i("paint_view", "Touched inside the circle");
            if (_onSplotchTouchListener != null)
            {
                _onSplotchTouchListener.onSplotchTouched(this);
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(_color);
        Path path = new Path();

        _contentRect = new RectF();
        _contentRect.left = getPaddingLeft();
        _contentRect.top = getPaddingTop();
        _contentRect.right = getWidth() - getPaddingRight();
        _contentRect.bottom = getHeight() - getPaddingBottom();

        PointF circleCenter = new PointF(_contentRect.centerX(), _contentRect.centerY());
        float maxRadius = Math.min(_contentRect.width() * 0.3f, _contentRect.height() * 0.3f);
        float minRadius = 0.25f * maxRadius;
        _radius = minRadius + (maxRadius - minRadius) * 0.5f;
        int pointCount = 30;

        for(int pointIndex = 0; pointIndex < pointCount; pointIndex++){
            //PointF point = new PointF();
            //point.x = getWidth() * 0.5f;
            //point.y = getHeight() * 0.5f;

            PointF point = new PointF();

            _radius += (Math.random() - 0.5) * 2.0 * 0.05 * _contentRect.width();

            // ((Math.random() - 0.5) * 2.0f) gives a number between -1 and 1
            //_radius += ((Math.random() - 0.5) * 2.0f) * (maxRadius - _radius);
            point.x = circleCenter.x + _radius * (float)Math.cos(((double)pointIndex /
                    (double)pointCount) * 2.0 * Math.PI);

            point.y = circleCenter.y + _radius * (float)Math.sin(((double)pointIndex /
                    (double)pointCount) * 2.0 * Math.PI);

            if(pointIndex == 0){
                path.moveTo(point.x, point.y);
            }
            else{
                path.lineTo(point.x, point.y);
            }
        }

        canvas.drawPath(path, paint);

        /**** Class Examples ****\
         // Must specify one of the flags in the paint.
         // -> ANTI_ALIAS_FLAG makes line look much smoother, to avoid jagged lines.
         Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

         //Param - float width of line
         linePaint.setStrokeWidth(10.0f);

         // drawLine Params - float startX, float startY, float stopX, float stopY, Paint paint.
         canvas.drawLine(10.0f, 10.0f, 100.0f, 100.0f, linePaint);

         // Four values would draw one line, if we did 8 then it would draw two lines.
         // NOTE: Look above for the values, first val startX, startY ...
         // NOTE2: Origin starts at the upper left hand corner.
         float[] points = {35.4f, 435.4f, 232.4f, 97.3f};

         // drawLines Params - (start reading floats, and stop reading)float[] pts, int offset, int count, Paint paint.
         canvas.drawLines(points, linePaint);

         // drawOval Params - float left, float top, float right, float bottom
         // Takes top left corner point and bottom right corner point of the rectangle
         // Giving 440.5 means width of the rectangle is 400, and 186.0 means height will be 100.
         linePaint.setColor(0x7F445588); // Half opacity.
         canvas.drawOval(new RectF(40.5f, 86.0f, 440.5f, 186.0f), linePaint);

         Path path = new Path();
         path.moveTo(40.0f, 200.0f);
         path.lineTo(250.0f, 600.0f);
         path.lineTo(300.0f, 760.0f);
         // Actually draws the line that we defined if we change the style to stroke.
         // Otherwise it would just fill the area of the path we defined.
         path.close(); // Same as calling lineTo(40.0f, 200.0f)
         linePaint.setStyle(Paint.Style.STROKE);
         canvas.drawPath(path, linePaint);
         */
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // First four lines are extract the bit mask
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // Pull the information associated with the Mode.
        // -> Unspecified - widthSpec, heightSpec contain no value, usually 0.
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        if (widthMode == MeasureSpec.AT_MOST){
            width = widthSpec;
        }
        if (heightMode == MeasureSpec.AT_MOST){
            height = heightSpec;
        }

        if (widthMode ==  MeasureSpec.EXACTLY){
            width = widthSpec;
            height = width;
        }
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSpec;
            width = height;
        }

        // TODO; RESPECT THE PADDING!
        if (width > height && widthMode != MeasureSpec.EXACTLY){
            width = height;
        }
        if (height > width && heightMode != MeasureSpec.EXACTLY){
            height = width;
        }

        // resolveSizeAndState(int size, int measureSpec, int childMeasuredState)
        // -> childMeasuredState - boolean asks if you are happy with the size or not.
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec,
                        width < getSuggestedMinimumWidth() ? MEASURED_STATE_TOO_SMALL: 0),
                resolveSizeAndState(height, heightMeasureSpec,
                        height < getSuggestedMinimumHeight() ? MEASURED_STATE_TOO_SMALL: 0));
    }
}
