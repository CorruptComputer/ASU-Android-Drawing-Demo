package xyz.gupton.nickolas.asu.drawingdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Draws shapes based off of 2 movable points.
 * @author Nickolas Gupton
 */
class GeometryView extends View implements View.OnTouchListener {

    /**
     * points | float[][]: points[0 for point A, 1 for point B][0 for X, 1 for Y].
     */
    private final float[][] points = {
            {0f, 0f},
            {0f, 0f}
    };

    /**
     * selectingA | boolean: True if A is the point which should green and selectable.
     */
    private boolean selectingA = true;

    /**
     * Tracks if the screen has been touched yet.
     */
    private boolean hasBeenTouched = false;

    /**
     * The Paint object needed to draw on the Canvas.
     */
    private Paint paint;

    /**
     * Sets up a new PointsView.
     * @param context   The Context (probably an Activity) the PointsView is running in.
     */
    public GeometryView(Context context) {
        // Call the constructor of the View class.
        super(context);

        // Make it so that the onTouch method gets called when the SketchyView is touched.
        setOnTouchListener(this);

        // Create a new paintbrush to use to draw to the Canvas.
        paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setTextSize(40.0f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!hasBeenTouched) {
            points[0][0] = getWidth() / 3.0f;
            points[0][1] = 2.0f * getHeight() / 3.0f;
            points[1][0] = 2.0f * getWidth() / 3.0f;
            points[1][1] = getHeight() / 3.0f;
        }

        // Paint the background a very light blue.
        canvas.drawColor(Color.WHITE);

        // Draw lines among the points
        paint.setColor(Color.BLACK);
        float cx = (points[0][0]+points[1][0])/2;
        float cy = (points[0][1]+points[1][1])/2;
        float rad = (float)Math.sqrt(Math.pow(cx-points[0][0], 2) + Math.pow(cy-points[0][1], 2));
        canvas.drawCircle(cx, cy, rad, paint);

        paint.setColor(Color.BLUE);
        canvas.drawRect(points[0][0]<points[1][0]?points[0][0]:points[1][0], // Get the X of the top left point.
                points[0][1]<points[1][1]?points[0][1]:points[1][1], // Get the Y of the top left point.
                points[0][0]>points[1][0]?points[0][0]:points[1][0], // Get the X of the bottom right point.
                points[0][1]>points[1][1]?points[0][1]:points[1][1], // Get the Y of the bottom right point.
                paint);

        paint.setColor(Color.WHITE);

        canvas.drawLine(points[0][0], points[0][1], points[1][0], points[1][1], paint);

        paint.setColor(Color.GREEN);
        canvas.drawCircle(selectingA?points[0][0]:points[1][0], selectingA?points[0][1]:points[1][1], 25, paint);

        paint.setColor(Color.RED);
        canvas.drawCircle(selectingA?points[1][0]:points[0][0], selectingA?points[1][1]:points[0][1], 25, paint);

        paint.setColor(Color.WHITE);
        canvas.drawText("A", points[0][0]-14f, points[0][1]+14f, paint);
        canvas.drawText("B", points[1][0]-14f, points[1][1]+14f, paint);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        hasBeenTouched = true;
        // React to the touch event: down, move, cancel or up.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (selectingA) {
                    points[0][0] = event.getX();
                    points[0][1] = event.getY();
                } else {
                    points[1][0] = event.getX();
                    points[1][1] = event.getY();
                }

                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (selectingA) {
                    points[0][0] = event.getX();
                    points[0][1] = event.getY();
                } else {
                    points[1][0] = event.getX();
                    points[1][1] = event.getY();
                }

                selectingA = !selectingA;
                invalidate();
                break;
        }
        return true; // Indicate that the touch event has been handled.
    }
}
