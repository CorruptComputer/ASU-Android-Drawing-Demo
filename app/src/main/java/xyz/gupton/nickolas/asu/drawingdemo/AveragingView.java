package xyz.gupton.nickolas.asu.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Shows the average of all the points tapped.
 * @author Nickolas Gupton
 */
class AveragingView extends View implements View.OnTouchListener {

    /**
     * xTotal | float: Keeps track of the total of all of the clicks in the X dimension.
     */
    private float xTotal = 0;

    /**
     * xTotal | float: Keeps track of the total of all of the clicks in the X dimension.
     */
    private float yTotal = 0;

    /**
     * numClicks | int: Keeps track of the number of times the screen has been clicked.
     */
    private int numClicks = 0;

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;

    /**
     * Sets up a new PointsView.
     * @param context   The Context (probably an Activity) the PointsView is running in.
     */
    public AveragingView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the SketchyView is touched.
        setOnTouchListener(this);

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Paint the background a very light blue.
        canvas.drawColor(Color.BLACK);

        // Draw lines among the points
        paint.setColor(Color.WHITE);
        canvas.drawCircle(xTotal/numClicks, yTotal/numClicks, (float)Math.sqrt(numClicks)*9f, paint);

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
                numClicks++;

                xTotal += event.getX();
                yTotal += event.getY();

                invalidate();
                break;
        }

        return true; // Indicate that the touch event has been handled.
    }
}
