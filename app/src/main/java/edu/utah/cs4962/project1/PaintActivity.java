package edu.utah.cs4962.project1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PaintActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        final PaintAreaView paintAreaView = new PaintAreaView(this);
        paintAreaView.setBackgroundColor(Color.WHITE);

        PaletteView paletteLayout = new PaletteView(this);

        rootLayout.addView(paintAreaView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout.LayoutParams paletteViewLP =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        paletteViewLP.gravity = Gravity.CENTER_HORIZONTAL;
        rootLayout.addView(paletteLayout, paletteViewLP);

        // Determine how many splotches we want on the palette
        for (int splotchIndex = 0; splotchIndex < 6; splotchIndex++) {

            PaintView paintView = new PaintView(this);

            if (splotchIndex == 0) {
                paintView.setColor(Color.RED);
            }
            if (splotchIndex == 1) {
                // Orange
                paintView.setColor(0xFFFFA500);
            }
            if (splotchIndex == 2) {
                paintView.setColor(Color.YELLOW);
            }
            if (splotchIndex == 3) {
                // Blue
                paintView.setColor(0xFF0000FF);
            }
            if (splotchIndex == 4) {
                // Green
                paintView.setColor(0xFF00FF00);
            }
            if (splotchIndex == 5) {
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
