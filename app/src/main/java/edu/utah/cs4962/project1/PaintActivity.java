package edu.utah.cs4962.project1;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PaintActivity extends Activity {

    //PaintView _paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        final PaintAreaView paintAreaView = new PaintAreaView(this);
        paintAreaView.setBackgroundColor(Color.WHITE);

        rootLayout.addView(paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        final PaletteView paletteLayout = new PaletteView(this);
        //rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.addView(paletteLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));

        // Determine how many splotches we want on the palette
        for (int splotchIndex = 0; splotchIndex < 6; splotchIndex++) {

            PaintView paintView = new PaintView(this);

            if(splotchIndex == 0) {
                paintView.setColor(Color.RED);
            }
            if(splotchIndex == 1) {
                // Orange
                paintView.setColor(0xFFFFA500);
            }
            if(splotchIndex == 2) {
                paintView.setColor(Color.YELLOW);
            }
            if(splotchIndex == 3) {
                // Blue
                paintView.setColor(0xFF0000FF);
            }
            if(splotchIndex == 4) {
                // Green
                paintView.setColor(0xFF00FF00);
            }
            if(splotchIndex == 5) {
                // Purple
                paintView.setColor(0xFF800080);
            }

            paletteLayout.addView(paintView, new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT));

            paintView.setOnSplotchTouchListener(new PaintView.OnSplotchTouchListener() {
                @Override
                public void onSplotchTouched(PaintView v) {
                    paintAreaView.invalidate();
                }
            });
        }
        setContentView(rootLayout);


    }

}
