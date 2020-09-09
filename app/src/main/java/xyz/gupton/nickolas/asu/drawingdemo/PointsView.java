package xyz.gupton.nickolas.asu.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Adds up to 9 points and draws lines between them.
 * @author Nickolas Gupton
 */
class PointsView extends View implements View.OnTouchListener {

    /**
     * points | float[][]: points[pointNumber][0 for X, 1 for Y].
     */
    private final float[][] points = {
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f},
            {0f, 0f}
    };

    private int currentNum = 0;

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;

    /**
     * Sets up a new PointsView.
     * @param context   The Context (probably an Activity) the PointsView is running in.
     */
    public PointsView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the SketchyView is touched.
        setOnTouchListener(this);

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
        // Make the text size bigger.
        paint.setTextSize(40.0f);
        // Make the lines wider.
        paint.setStrokeWidth(3.0f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Paint the background a very light blue.
        canvas.drawColor(Color.WHITE);

        // Draw lines among the points
        paint.setColor(Color.GRAY);
        for (int i = 1; i < currentNum; i++) {
            canvas.drawLine(points[i-1][0], points[i-1][1], points[i][0], points[i][1], paint);
        }

        // Draw numbers
        paint.setColor(Color.BLACK);
        for (int i = 0; i < currentNum; i++) {
            canvas.drawText(String.valueOf(i+1), points[i][0]-14f, points[i][1]+14f, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // React to the touch event: down, move, cancel or up.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Do nothing.
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (currentNum == 9) {
                    currentNum = 0;
                }

                // Set new point B and force the Canvas to redraw.
                points[currentNum][0] = event.getX();
                points[currentNum][1] = event.getY();

                currentNum++;
                invalidate();
                break;
        }
        return true; // Indicate that the touch event has been handled.
    }

}

